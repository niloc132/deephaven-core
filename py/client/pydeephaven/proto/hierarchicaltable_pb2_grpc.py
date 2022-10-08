# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from pydeephaven.proto import hierarchicaltable_pb2 as deephaven_dot_proto_dot_hierarchicaltable__pb2


class HierarchicalTableServiceStub(object):
    """
    This service provides tools to create and view hierarchical tables (rollups and trees).
    """

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.Rollup = channel.unary_unary(
                '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Rollup',
                request_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupRequest.SerializeToString,
                response_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupResponse.FromString,
                )
        self.Tree = channel.unary_unary(
                '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Tree',
                request_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeRequest.SerializeToString,
                response_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeResponse.FromString,
                )
        self.ExportSource = channel.unary_unary(
                '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/ExportSource',
                request_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportRequest.SerializeToString,
                response_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportResponse.FromString,
                )
        self.View = channel.unary_unary(
                '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/View',
                request_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewRequest.SerializeToString,
                response_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewResponse.FromString,
                )


class HierarchicalTableServiceServicer(object):
    """
    This service provides tools to create and view hierarchical tables (rollups and trees).
    """

    def Rollup(self, request, context):
        """
        Performs a rollup operation on a table and exports a default hierarchical table view of the result.
        """
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def Tree(self, request, context):
        """
        Performs a tree operation on a table and exports a default hierarchical table view of the result.
        """
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def ExportSource(self, request, context):
        """
        Exports the source table for a hierarchical table view.
        """
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def View(self, request, context):
        """
        Derives a new hierarchical table view from an existing hierarchical table view.
        """
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_HierarchicalTableServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'Rollup': grpc.unary_unary_rpc_method_handler(
                    servicer.Rollup,
                    request_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupRequest.FromString,
                    response_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupResponse.SerializeToString,
            ),
            'Tree': grpc.unary_unary_rpc_method_handler(
                    servicer.Tree,
                    request_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeRequest.FromString,
                    response_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeResponse.SerializeToString,
            ),
            'ExportSource': grpc.unary_unary_rpc_method_handler(
                    servicer.ExportSource,
                    request_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportRequest.FromString,
                    response_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportResponse.SerializeToString,
            ),
            'View': grpc.unary_unary_rpc_method_handler(
                    servicer.View,
                    request_deserializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewRequest.FromString,
                    response_serializer=deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewResponse.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'io.deephaven.proto.backplane.grpc.HierarchicalTableService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class HierarchicalTableService(object):
    """
    This service provides tools to create and view hierarchical tables (rollups and trees).
    """

    @staticmethod
    def Rollup(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Rollup',
            deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupRequest.SerializeToString,
            deephaven_dot_proto_dot_hierarchicaltable__pb2.RollupResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def Tree(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Tree',
            deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeRequest.SerializeToString,
            deephaven_dot_proto_dot_hierarchicaltable__pb2.TreeResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def ExportSource(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/ExportSource',
            deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportRequest.SerializeToString,
            deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableSourceExportResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def View(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/io.deephaven.proto.backplane.grpc.HierarchicalTableService/View',
            deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewRequest.SerializeToString,
            deephaven_dot_proto_dot_hierarchicaltable__pb2.HierarchicalTableViewResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)
