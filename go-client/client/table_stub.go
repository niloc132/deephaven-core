package client

import (
	"context"
	"errors"
	"fmt"
	"io"
	"time"

	"github.com/apache/arrow/go/v8/arrow/flight"
	"github.com/apache/arrow/go/v8/arrow/memory"
	"google.golang.org/grpc"

	tablepb2 "github.com/deephaven/deephaven-core/go-client/internal/proto/table"
	ticketpb2 "github.com/deephaven/deephaven-core/go-client/internal/proto/ticket"
)

// A tableStub wraps table.proto gRPC requests.
type tableStub struct {
	client *Client

	stub tablepb2.TableServiceClient // The stub for the table gRPC requests.
}

// newTableStub creates a new table stub that can be used to make table gRPC requests.
func newTableStub(client *Client) tableStub {
	stub := tablepb2.NewTableServiceClient(client.grpcChannel)

	return tableStub{client: client, stub: stub}
}

// createInputTable simply wraps the CreateInputTable gRPC call and returns the resulting table.
// See inputTableStub for more details on how it is used.
func (ts *tableStub) createInputTable(ctx context.Context, req *tablepb2.CreateInputTableRequest) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	resp, err := ts.stub.CreateInputTable(ctx, req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// batchErrorPart is a single error in a batchError.
type batchErrorPart struct {
	ServerMsg string                   // The error message returned by the server
	ResultId  *tablepb2.TableReference // The result ID of the table which caused the error
}

// batchError is an error that might be returned by batch().
// This is typically never displayed, and gets immediately wrapped in a QueryError.
type batchError struct {
	parts []batchErrorPart
}

func (err batchError) Error() string {
	return fmt.Sprintf("batch error in %d tables", len(err.parts))
}

// batch executes a batch (query) request on the server and returns the resulting tables.
// Only the operations which were given a non-nil result ticket (the ResultId field) will be returned as tables.
// The tables will be returned in an arbitrary order.
// Each table's ticket will match exactly one result ticket in one of the operations,
// so this can be used to identify the tables and put them back in order.
// This may return a batchError.
func (ts *tableStub) batch(ctx context.Context, ops []*tablepb2.BatchTableRequest_Operation) ([]*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	req := tablepb2.BatchTableRequest{Ops: ops}
	resp, err := ts.stub.Batch(ctx, &req)
	if err != nil {
		return nil, err
	}
	defer resp.CloseSend()

	exportedTables := []*TableHandle{}

	var errors []batchErrorPart

	for {
		created, err := resp.Recv()
		if err == io.EOF {
			break
		} else if err != nil {
			return nil, err
		}

		if !created.Success {
			part := batchErrorPart{
				ServerMsg: created.GetErrorInfo(),
				ResultId:  created.ResultId,
			}
			errors = append(errors, part)
		}

		if created.Success {
			if _, ok := created.ResultId.Ref.(*tablepb2.TableReference_Ticket); ok {
				newTable, err := parseCreationResponse(ts.client, created)
				if err != nil {
					return nil, err
				}
				exportedTables = append(exportedTables, newTable)
			}
		}
	}

	if len(errors) > 0 {
		return nil, batchError{parts: errors}
	}

	return exportedTables, nil
}

// fetchTable exports (or re-exports) a table on the server so that it can be referred to by a new ticket.
func (ts *tableStub) fetchTable(ctx context.Context, oldTicket *ticketpb2.Ticket) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	sourceId := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: oldTicket}}
	resultId := ts.client.newTicket()

	req := tablepb2.FetchTableRequest{SourceId: &sourceId, ResultId: &resultId}
	resp, err := ts.stub.FetchTable(ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// OpenTable opens a globally-scoped table with the given name on the server.
func (ts *tableStub) OpenTable(ctx context.Context, name string) (*TableHandle, error) {
	fieldId := fieldId{appId: "scope", fieldName: name}
	if tbl, ok := ts.client.tables[fieldId]; ok {
		return ts.fetchTable(ctx, tbl.ticket)
	} else {
		return nil, errors.New("no table by the name " + name + " (maybe it isn't fetched?)")
	}
}

// EmptyTableQuery is like EmptyTable, except it can be used as part of a query.
func (ts *tableStub) EmptyTableQuery(numRows int64) QueryNode {
	qb := newQueryBuilder(ts.client, nil)
	qb.ops = append(qb.ops, emptyTableOp{numRows: numRows})
	return qb.curRootNode()
}

// EmptyTable creates a new empty table in the global scope.
//
// The table will have zero columns and the specified number of rows.
func (ts *tableStub) EmptyTable(ctx context.Context, numRows int64) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	result := ts.client.newTicket()

	req := tablepb2.EmptyTableRequest{ResultId: &result, Size: numRows}
	resp, err := ts.stub.EmptyTable(ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// TimeTableQuery is like TimeTable, except it can be used as part of a query.
func (ts *tableStub) TimeTableQuery(period time.Duration, startTime time.Time) QueryNode {
	qb := newQueryBuilder(ts.client, nil)
	qb.ops = append(qb.ops, timeTableOp{period: period, startTime: startTime})
	return qb.curRootNode()
}

// TimeTable creates a ticking time table in the global scope.
// The period is time between adding new rows to the table.
// The startTime is the time of the first row in the table.
func (ts *tableStub) TimeTable(ctx context.Context, period time.Duration, startTime time.Time) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	result := ts.client.newTicket()

	// TODO: Is this affected by timezones? Does it need to be the monotonic reading?
	realStartTime := startTime.UnixNano()

	req := tablepb2.TimeTableRequest{ResultId: &result, PeriodNanos: period.Nanoseconds(), StartTimeNanos: realStartTime}
	resp, err := ts.stub.TimeTable(ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// parseCreationResponse turns a gRPC table creation response, which is returned by most table gRPC methods, into a usable TableHandle.
func parseCreationResponse(client *Client, resp *tablepb2.ExportedTableCreationResponse) (*TableHandle, error) {
	if !resp.Success {
		return nil, errors.New("server error: `" + resp.GetErrorInfo() + "`")
	}

	respTicket := resp.ResultId.GetTicket()
	if respTicket == nil {
		return nil, errors.New("server response did not have ticket")
	}

	schema, err := flight.DeserializeSchema(resp.SchemaHeader, memory.DefaultAllocator)
	if err != nil {
		return nil, err
	}

	return newTableHandle(client, respTicket, schema, resp.Size, resp.IsStatic), nil
}

// dropColumns is a wrapper around the DropColumns gRPC request.
func (ts *tableStub) dropColumns(ctx context.Context, table *TableHandle, cols []string) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	result := ts.client.newTicket()

	source := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: table.ticket}}

	req := tablepb2.DropColumnsRequest{ResultId: &result, SourceId: &source, ColumnNames: cols}
	resp, err := ts.stub.DropColumns(ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// selectOrUpdateOp is just a way to refer to one of the Update, View, UpdateView, Select, or LazyUpdate methods on the table stub.
type selectOrUpdateOp func(tablepb2.TableServiceClient, context.Context, *tablepb2.SelectOrUpdateRequest, ...grpc.CallOption) (*tablepb2.ExportedTableCreationResponse, error)

// doSelectOrUpdate wraps Update, View, UpdateView, Select, and LazyUpdate gRPC requests.
func (ts *tableStub) doSelectOrUpdate(ctx context.Context, table *TableHandle, formulas []string, op selectOrUpdateOp) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	result := ts.client.newTicket()
	source := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: table.ticket}}

	req := tablepb2.SelectOrUpdateRequest{ResultId: &result, SourceId: &source, ColumnSpecs: formulas}
	resp, err := op(ts.stub, ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

// update wraps the Update gRPC request.
func (ts *tableStub) update(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.doSelectOrUpdate(ctx, table, formulas, tablepb2.TableServiceClient.Update)
}

// lazyUpdadte wraps the LazyUpdate gRPC request.
func (ts *tableStub) lazyUpdate(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.doSelectOrUpdate(ctx, table, formulas, tablepb2.TableServiceClient.LazyUpdate)
}

// updateView wraps the UpdateView gRPC request.
func (ts *tableStub) updateView(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.doSelectOrUpdate(ctx, table, formulas, tablepb2.TableServiceClient.UpdateView)
}

// view wraps the View gRPC request.
func (ts *tableStub) view(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.doSelectOrUpdate(ctx, table, formulas, tablepb2.TableServiceClient.View)
}

// selectTbl wraps the Select gRPC request
func (ts *tableStub) selectTbl(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.doSelectOrUpdate(ctx, table, formulas, tablepb2.TableServiceClient.Select)
}

// makeRequest is a convenience function to perform all the boilerplate required to actually make a gRPC request.
// The op argument should simply create a request given the result and source ID and call the appropriate gRPC method.
func (ts *tableStub) makeRequest(ctx context.Context, table *TableHandle, op reqOp) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	result := ts.client.newTicket()
	source := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: table.ticket}}

	resp, err := op(ctx, &result, &source)

	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

type ctxt = context.Context
type ticketRef = *ticketpb2.Ticket
type tblRef = *tablepb2.TableReference
type tblResp = *tablepb2.ExportedTableCreationResponse

// A reqOp is a function that should perform a gRPC request.
type reqOp func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error)

// selectDistinct is a wrapper around the SelectDistinct gRPC operation.
func (ts *tableStub) selectDistinct(ctx context.Context, table *TableHandle, formulas []string) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		req := tablepb2.SelectDistinctRequest{ResultId: resultId, SourceId: sourceId, ColumnNames: formulas}
		return ts.stub.SelectDistinct(ctx, &req)
	})
}

// sortBy is a wrapper around the Sort gRPC operation.
func (ts *tableStub) sortBy(ctx context.Context, table *TableHandle, cols []SortColumn) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		var sorts []*tablepb2.SortDescriptor
		for _, col := range cols {
			var dir tablepb2.SortDescriptor_SortDirection
			if col.descending {
				dir = tablepb2.SortDescriptor_DESCENDING
			} else {
				dir = tablepb2.SortDescriptor_ASCENDING
			}

			sort := tablepb2.SortDescriptor{ColumnName: col.colName, IsAbsolute: false, Direction: dir}
			sorts = append(sorts, &sort)
		}

		req := tablepb2.SortTableRequest{ResultId: resultId, SourceId: sourceId, Sorts: sorts}
		return ts.stub.Sort(ctx, &req)
	})
}

// where is a wrapper around the UnstructuredFilter gRPC operation.
func (ts *tableStub) where(ctx context.Context, table *TableHandle, filters []string) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		req := tablepb2.UnstructuredFilterTableRequest{ResultId: resultId, SourceId: sourceId, Filters: filters}
		return ts.stub.UnstructuredFilter(ctx, &req)
	})
}

// headOrTail is a wrapper around the Head and Tail gRPC operations (the isHead argument selects which one it is).
func (ts *tableStub) headOrTail(ctx context.Context, table *TableHandle, numRows int64, isHead bool) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		req := tablepb2.HeadOrTailRequest{ResultId: resultId, SourceId: sourceId, NumRows: numRows}
		if isHead {
			return ts.stub.Head(ctx, &req)
		} else {
			return ts.stub.Tail(ctx, &req)
		}
	})
}

// naturalJoin is a wrapper around the naturalJoin gRPC operation.
func (ts *tableStub) naturalJoin(ctx context.Context, leftTable *TableHandle, rightTable *TableHandle, on []string, joins []string) (*TableHandle, error) {
	return ts.makeRequest(ctx, leftTable, func(ctx ctxt, resultId ticketRef, leftId tblRef) (tblResp, error) {
		rightId := &tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: rightTable.ticket}}
		req := tablepb2.NaturalJoinTablesRequest{ResultId: resultId, LeftId: leftId, RightId: rightId, ColumnsToMatch: on, ColumnsToAdd: joins}
		return ts.stub.NaturalJoinTables(ctx, &req)
	})
}

// crossJoin is a wrapper around the crossJoin gRPC operation.
func (ts *tableStub) crossJoin(ctx context.Context, leftTable *TableHandle, rightTable *TableHandle, on []string, joins []string, reserveBits int32) (*TableHandle, error) {
	return ts.makeRequest(ctx, leftTable, func(ctx ctxt, resultId ticketRef, leftId tblRef) (tblResp, error) {
		rightId := &tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: rightTable.ticket}}
		req := tablepb2.CrossJoinTablesRequest{ResultId: resultId, LeftId: leftId, RightId: rightId, ColumnsToMatch: on, ColumnsToAdd: joins, ReserveBits: reserveBits}
		return ts.stub.CrossJoinTables(ctx, &req)
	})
}

// exactJoin is a wrapper around the exactJoin gRPC operation.
func (ts *tableStub) exactJoin(ctx context.Context, leftTable *TableHandle, rightTable *TableHandle, on []string, joins []string) (*TableHandle, error) {
	return ts.makeRequest(ctx, leftTable, func(ctx ctxt, resultId ticketRef, leftId tblRef) (tblResp, error) {
		rightId := &tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: rightTable.ticket}}
		req := tablepb2.ExactJoinTablesRequest{ResultId: resultId, LeftId: leftId, RightId: rightId, ColumnsToMatch: on, ColumnsToAdd: joins}
		return ts.stub.ExactJoinTables(ctx, &req)
	})
}

// asOfJoin is a wrapper around the asOfJoin gRPC operation.
func (ts *tableStub) asOfJoin(ctx context.Context, leftTable *TableHandle, rightTable *TableHandle, on []string, joins []string, matchRule MatchRule) (*TableHandle, error) {
	return ts.makeRequest(ctx, leftTable, func(ctx ctxt, resultId ticketRef, leftId tblRef) (tblResp, error) {
		rightId := &tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: rightTable.ticket}}
		var asOfMatchRule tablepb2.AsOfJoinTablesRequest_MatchRule
		switch matchRule {
		case MatchRuleLessThanEqual:
			asOfMatchRule = tablepb2.AsOfJoinTablesRequest_LESS_THAN_EQUAL
		case MatchRuleLessThan:
			asOfMatchRule = tablepb2.AsOfJoinTablesRequest_LESS_THAN
		case MatchRuleGreaterThanEqual:
			asOfMatchRule = tablepb2.AsOfJoinTablesRequest_GREATER_THAN_EQUAL
		case MatchRuleGreaterThan:
			asOfMatchRule = tablepb2.AsOfJoinTablesRequest_GREATER_THAN
		default:
			panic("invalid match rule")
		}

		req := tablepb2.AsOfJoinTablesRequest{ResultId: resultId, LeftId: leftId, RightId: rightId, ColumnsToMatch: on, ColumnsToAdd: joins, AsOfMatchRule: asOfMatchRule}
		return ts.stub.AsOfJoinTables(ctx, &req)
	})
}

// headOrTailBy is a wrapper around the HeadBy and TailBy gRPC operations (which one it is can be selected using isHead).
func (ts *tableStub) headOrTailBy(ctx context.Context, table *TableHandle, numRows int64, by []string, isHead bool) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		req := tablepb2.HeadOrTailByRequest{ResultId: resultId, SourceId: sourceId, NumRows: numRows, GroupByColumnSpecs: by}
		if isHead {
			return ts.stub.HeadBy(ctx, &req)
		} else {
			return ts.stub.TailBy(ctx, &req)
		}
	})
}

// dedicatedAggOp is actually a convenience method to perform the ComboAggregate gRPC operation with only a single aggregation.
func (ts *tableStub) dedicatedAggOp(ctx context.Context, table *TableHandle, by []string, countColumn string, kind tablepb2.ComboAggregateRequest_AggType) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		var agg tablepb2.ComboAggregateRequest_Aggregate
		if kind == tablepb2.ComboAggregateRequest_COUNT && countColumn != "" {
			agg = tablepb2.ComboAggregateRequest_Aggregate{Type: kind, ColumnName: countColumn}
		} else {
			agg = tablepb2.ComboAggregateRequest_Aggregate{Type: kind}
		}

		aggs := []*tablepb2.ComboAggregateRequest_Aggregate{&agg}

		req := tablepb2.ComboAggregateRequest{ResultId: resultId, SourceId: sourceId, Aggregates: aggs, GroupByColumns: by}
		return ts.stub.ComboAggregate(ctx, &req)
	})
}

// ungroup is a wrapper around the Ungroup gRPC method.
func (ts *tableStub) ungroup(ctx context.Context, table *TableHandle, cols []string, nullFill bool) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		req := tablepb2.UngroupRequest{ResultId: resultId, SourceId: sourceId, NullFill: nullFill, ColumnsToUngroup: cols}
		return ts.stub.Ungroup(ctx, &req)
	})
}

// aggBy is a wrapper around the ComboAggregate gRPC request.
func (ts *tableStub) aggBy(ctx context.Context, table *TableHandle, aggs []aggPart, by []string) (*TableHandle, error) {
	return ts.makeRequest(ctx, table, func(ctx ctxt, resultId ticketRef, sourceId tblRef) (tblResp, error) {
		var reqAggs []*tablepb2.ComboAggregateRequest_Aggregate
		for _, agg := range aggs {
			reqAgg := tablepb2.ComboAggregateRequest_Aggregate{Type: agg.kind, ColumnName: agg.columnName, MatchPairs: agg.matchPairs, Percentile: agg.percentile, AvgMedian: agg.avgMedian}
			reqAggs = append(reqAggs, &reqAgg)
		}

		req := &tablepb2.ComboAggregateRequest{ResultId: resultId, SourceId: sourceId, Aggregates: reqAggs, GroupByColumns: by}
		return ts.stub.ComboAggregate(ctx, req)
	})
}

// merge is a wrapper around the MergeTables gRPC request.
func (ts *tableStub) merge(ctx context.Context, sortBy string, others []*TableHandle) (*TableHandle, error) {
	ctx, err := ts.client.withToken(ctx)
	if err != nil {
		return nil, err
	}

	resultId := ts.client.newTicket()

	sourceIds := make([]tblRef, len(others))
	for i, handle := range others {
		sourceIds[i] = &tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: handle.ticket}}
	}

	req := tablepb2.MergeTablesRequest{ResultId: &resultId, SourceIds: sourceIds, KeyColumn: sortBy}
	resp, err := ts.stub.MergeTables(ctx, &req)
	if err != nil {
		return nil, err
	}

	return parseCreationResponse(ts.client, resp)
}

type serialOpsState struct {
	client *Client

	// The list of root/exported nodes.
	// The tables these create will eventually be returned to the user.
	exportedNodes []QueryNode

	// A map containing query nodes that have already been processed and the resulting tables.
	finishedNodes map[QueryNode]*TableHandle
}

func (state *serialOpsState) isExported(node QueryNode) bool {
	for _, exNode := range state.exportedNodes {
		if node == exNode {
			return true
		}
	}
	return false
}

func (state *serialOpsState) processNode(ctx context.Context, node QueryNode) (*TableHandle, error) {
	// If this node has already been processed, just return the old result.
	if tbl, ok := state.finishedNodes[node]; ok {
		return tbl, nil
	}

	if node.index == -1 {
		oldTable := node.builder.table

		if state.isExported(node) {
			// This node is exported, so in order to avoid two having TableHandles with the same ticket we need to re-export the old table.
			newTable, err := state.client.tableStub.fetchTable(ctx, oldTable.ticket)
			if err != nil {
				return nil, err
			}
			state.finishedNodes[node] = newTable
			return newTable, nil
		} else {
			// This node isn't exported, so it's okay to just reuse the existing table.
			state.finishedNodes[node] = oldTable
			return oldTable, nil
		}
	}

	op := node.builder.ops[node.index]

	var children []*TableHandle
	for _, childNode := range op.childQueries() {
		childTbl, err := state.processNode(ctx, childNode)
		if err != nil {
			return nil, err
		}
		children = append(children, childTbl)
	}

	tbl, err := op.execSerialOp(ctx, &state.client.tableStub, children)
	if err != nil {
		return nil, err
	}

	state.finishedNodes[node] = tbl
	return tbl, nil
}

func execSerial(ctx context.Context, client *Client, nodes []QueryNode) ([]*TableHandle, error) {
	state := serialOpsState{
		client: client,

		exportedNodes: nodes,

		finishedNodes: make(map[QueryNode]*TableHandle),
	}

	var result []*TableHandle

	exported := make(map[QueryNode]struct{})

	for _, node := range nodes {
		// If it's already been exported, we'll need to re-export it again manually.
		if _, ok := exported[node]; ok {
			// The node has already been exported. To avoid aliased TableHandles,
			// we need to re-export it.
			oldTable := state.finishedNodes[node]
			tbl, err := client.tableStub.fetchTable(ctx, oldTable.ticket)
			if err != nil {
				// TODO: Wrap
				return nil, err
			}
			result = append(result, tbl)
		} else {
			exported[node] = struct{}{}

			tbl, err := state.processNode(ctx, node)
			if err != nil {
				// TODO: Wrap
				return nil, err
			}
			result = append(result, tbl)
		}
	}

	for node, tbl := range state.finishedNodes {
		if !state.isExported(node) {
			err := tbl.Release(ctx)
			if err != nil {
				// TODO: Wrap
				return nil, err
			}
		}
	}

	assert(len(result) == len(nodes), "wrong number of tables in result")
	return result, nil
}
