package io.deephaven.server.partitionedtable;

import com.google.rpc.Code;
import io.deephaven.engine.table.PartitionedTable;
import io.deephaven.engine.table.Table;
import io.deephaven.extensions.barrage.util.ExportUtil;
import io.deephaven.extensions.barrage.util.GrpcUtil;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import io.deephaven.proto.backplane.grpc.ExportedTableCreationResponse;
import io.deephaven.proto.backplane.grpc.FetchObjectResponse;
import io.deephaven.proto.backplane.grpc.GetTablesRequest;
import io.deephaven.proto.backplane.grpc.MergeRequest;
import io.deephaven.proto.backplane.grpc.PartitionByRequest;
import io.deephaven.proto.backplane.grpc.PartitionByResponse;
import io.deephaven.proto.backplane.grpc.PartitionedTableServiceGrpc;
import io.deephaven.server.session.SessionService;
import io.deephaven.server.session.SessionState;
import io.deephaven.server.session.TicketRouter;
import io.grpc.stub.StreamObserver;

import javax.inject.Inject;

import static io.deephaven.extensions.barrage.util.GrpcUtil.safelyExecute;

public class PartitionedTableServiceGrpcImpl extends PartitionedTableServiceGrpc.PartitionedTableServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(PartitionedTableServiceGrpcImpl.class);

    private final TicketRouter ticketRouter;
    private final SessionService sessionService;

    @Inject
    public PartitionedTableServiceGrpcImpl(TicketRouter ticketRouter, SessionService sessionService) {
        this.ticketRouter = ticketRouter;
        this.sessionService = sessionService;
    }

    @Override
    public void partitionBy(PartitionByRequest request, StreamObserver<PartitionByResponse> responseObserver) {
        GrpcUtil.rpcWrapper(log, responseObserver, () -> {
            final SessionState session = sessionService.getCurrentSession();

            SessionState.ExportObject<Table> targetTable =
                    ticketRouter.resolve(session, request.getTableId(), "tableId");

            session.newExport(request.getResultId(), "resultId")
                    .require(targetTable)
                    .onError(responseObserver)
                    .submit(() -> {
                        PartitionedTable partitionedTable = targetTable.get().partitionBy(request.getDropKeys(), request.getKeyColumnNamesList().toArray(String[]::new));
                        safelyExecute(() -> {
                            responseObserver.onNext(PartitionByResponse.getDefaultInstance());
                            responseObserver.onCompleted();
                        });
                        return partitionedTable;
                    });

        });
    }

    @Override
    public void merge(MergeRequest request, StreamObserver<ExportedTableCreationResponse> responseObserver) {
        GrpcUtil.rpcWrapper(log, responseObserver, () -> {
            final SessionState session = sessionService.getCurrentSession();

            SessionState.ExportObject<PartitionedTable> partitionedTable =
                    ticketRouter.resolve(session, request.getPartitionedTable(), "partitionedTable");

            session.newExport(request.getResultId(), "resultId")
                    .require(partitionedTable)
                    .onError(responseObserver)
                    .submit(() -> {
                        Table merged = partitionedTable.get().merge();
                        safelyExecute(() -> {
                            responseObserver.onNext(ExportUtil.buildTableCreationResponse(request.getResultId(), merged));
                            responseObserver.onCompleted();
                        });
                        return merged;
                    });
        });

    }

    @Override
    public void getTable(GetTablesRequest request, StreamObserver<ExportedTableCreationResponse> responseObserver) {
        GrpcUtil.rpcWrapper(log, responseObserver, () -> {
            final SessionState session = sessionService.getCurrentSession();

            SessionState.ExportObject<PartitionedTable> partitionedTable =
                    ticketRouter.resolve(session, request.getTicket(), "partitionedTable");
            if (!request.hasRow()) {
                throw GrpcUtil.statusRuntimeException(Code.INVALID_ARGUMENT, "Only row is supported to fetch a table from a partitionedtable");
            }
            session.newExport(request.getResultId(), "resultId")
                    .require(partitionedTable)
                    .onError(responseObserver)
                    .submit(() -> {
                        Table table = (Table) partitionedTable.get().table().getColumnSource(partitionedTable.get().constituentColumnName()).get(request.getRow());//TODO not this
                        safelyExecute(() -> {
                            responseObserver.onNext(ExportUtil.buildTableCreationResponse(request.getResultId(), table));
                            responseObserver.onCompleted();
                        });
                        return table;
                    });
        });
    }
}
