package client

import (
	"context"
	"errors"
	"io"
	"time"

	"github.com/apache/arrow/go/arrow/flight"
	"github.com/apache/arrow/go/arrow/memory"

	tablepb2 "github.com/deephaven/deephaven-core/go-client/internal/proto/table"
)

type tableStub struct {
	client *Client

	stub tablepb2.TableServiceClient
}

func NewTableStub(client *Client) (tableStub, error) {
	stub := tablepb2.NewTableServiceClient(client.GrpcChannel())

	return tableStub{client: client, stub: stub}, nil
}

func (ts *tableStub) batch(ctx context.Context, ops []*tablepb2.BatchTableRequest_Operation) ([]TableHandle, error) {
	ctx = ts.client.WithToken(ctx)

	req := tablepb2.BatchTableRequest{Ops: ops}
	resp, err := ts.stub.Batch(ctx, &req)
	if err != nil {
		return nil, err
	}
	defer resp.CloseSend()

	exportedTables := []TableHandle{}

	for {
		created, err := resp.Recv()
		if err == io.EOF {
			break
		} else if err != nil {
			return nil, err
		}

		if !created.Success {
			return nil, errors.New(created.GetErrorInfo())
		}

		if _, ok := created.ResultId.Ref.(*tablepb2.TableReference_Ticket); ok {
			newTable, err := parseCreationResponse(ts.client, created)
			if err != nil {
				return nil, err
			}
			exportedTables = append(exportedTables, newTable)
		}
	}

	return exportedTables, nil
}

// Like `EmptyTable`, except it can be used as part of a query.
func (ts *tableStub) EmptyTableQuery(numRows int64) QueryNode {
	qb := newQueryBuilder(ts.client, nil)
	qb.ops = append(qb.ops, EmptyTableOp{numRows: numRows})
	return qb.curRootNode()
}

// Creates a new empty table in the global scope.
//
// The table will have zero columns and the specified number of rows.
func (ts *tableStub) EmptyTable(ctx context.Context, numRows int64) (TableHandle, error) {
	ctx = ts.client.WithToken(ctx)

	result := ts.client.NewTicket()

	req := tablepb2.EmptyTableRequest{ResultId: &result, Size: numRows}
	resp, err := ts.stub.EmptyTable(ctx, &req)
	if err != nil {
		return TableHandle{}, err
	}

	return parseCreationResponse(ts.client, resp)
}

// Like `TimeTable`, except it can be used as part of a query
func (ts *tableStub) TimeTableQuery(period int64, startTime *int64) QueryNode {
	var realStartTime int64
	if startTime == nil {
		// TODO: Same question as for TimeTable
		realStartTime = time.Now().UnixNano()
	} else {
		realStartTime = *startTime
	}

	qb := newQueryBuilder(ts.client, nil)
	qb.ops = append(qb.ops, TimeTableOp{period: period, startTime: realStartTime})
	return qb.curRootNode()
}

// Creates a ticking time table in the global scope.
// The period is in nanoseconds and represents the interval between adding new rows to the table.
// The startTime is in nanoseconds since epoch and defaults to the current time when it is nil.
func (ts *tableStub) TimeTable(ctx context.Context, period int64, startTime *int64) (TableHandle, error) {
	ctx = ts.client.WithToken(ctx)

	result := ts.client.NewTicket()

	var realStartTime int64
	if startTime == nil {
		// TODO: Is this affected by timezones? Does it need to be the monotonic reading?
		realStartTime = time.Now().UnixNano()
	} else {
		realStartTime = *startTime
	}

	req := tablepb2.TimeTableRequest{ResultId: &result, PeriodNanos: period, StartTimeNanos: realStartTime}
	resp, err := ts.stub.TimeTable(ctx, &req)
	if err != nil {
		return TableHandle{}, err
	}

	return parseCreationResponse(ts.client, resp)
}

func (ts *tableStub) DropColumns(ctx context.Context, table *TableHandle, cols []string) (TableHandle, error) {
	ctx = ts.client.WithToken(ctx)

	result := ts.client.NewTicket()

	source := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: table.ticket}}

	req := tablepb2.DropColumnsRequest{ResultId: &result, SourceId: &source, ColumnNames: cols}
	resp, err := ts.stub.DropColumns(ctx, &req)
	if err != nil {
		return TableHandle{}, err
	}

	return parseCreationResponse(ts.client, resp)
}

func (ts *tableStub) Update(ctx context.Context, table *TableHandle, formulas []string) (TableHandle, error) {
	ctx = ts.client.WithToken(ctx)

	result := ts.client.NewTicket()

	source := tablepb2.TableReference{Ref: &tablepb2.TableReference_Ticket{Ticket: table.ticket}}

	req := tablepb2.SelectOrUpdateRequest{ResultId: &result, SourceId: &source, ColumnSpecs: formulas}
	resp, err := ts.stub.Update(ctx, &req)
	if err != nil {
		return TableHandle{}, err
	}

	return parseCreationResponse(ts.client, resp)
}

func parseCreationResponse(client *Client, resp *tablepb2.ExportedTableCreationResponse) (TableHandle, error) {
	if !resp.Success {
		return TableHandle{}, errors.New("server error: `" + resp.GetErrorInfo() + "`")
	}

	respTicket := resp.ResultId.GetTicket()
	if respTicket == nil {
		return TableHandle{}, errors.New("server response did not have ticket")
	}

	alloc := memory.NewGoAllocator()
	schema, err := flight.DeserializeSchema(resp.SchemaHeader, alloc)
	if err != nil {
		return TableHandle{}, err
	}

	return newTableHandle(client, respTicket, schema, resp.Size, resp.IsStatic), nil
}
