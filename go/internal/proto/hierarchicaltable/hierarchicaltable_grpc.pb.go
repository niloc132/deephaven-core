// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v3.21.2
// source: deephaven/proto/hierarchicaltable.proto

package hierarchicaltable

import (
	context "context"
	table "github.com/deephaven/deephaven-core/go/internal/proto/table"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// HierarchicalTableServiceClient is the client API for HierarchicalTableService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type HierarchicalTableServiceClient interface {
	// Applies a rollup operation to a Table and exports the resulting RollupTable
	Rollup(ctx context.Context, in *RollupRequest, opts ...grpc.CallOption) (*RollupResponse, error)
	// Applies a tree operation to a Table and exports the resulting TreeTable
	Tree(ctx context.Context, in *TreeRequest, opts ...grpc.CallOption) (*TreeResponse, error)
	// Applies operations to an existing HierarchicalTable (RollupTable or TreeTable) and exports the resulting
	// HierarchicalTable
	Apply(ctx context.Context, in *HierarchicalTableApplyRequest, opts ...grpc.CallOption) (*HierarchicalTableApplyResponse, error)
	// Creates a view associating a Table of expansion keys and actions with an existing HierarchicalTable and exports
	// the resulting HierarchicalTableView for subsequent snapshot or subscription requests
	View(ctx context.Context, in *HierarchicalTableViewRequest, opts ...grpc.CallOption) (*HierarchicalTableViewResponse, error)
	// Exports the source Table for a HierarchicalTable (Rollup or TreeTable)
	ExportSource(ctx context.Context, in *HierarchicalTableSourceExportRequest, opts ...grpc.CallOption) (*table.ExportedTableCreationResponse, error)
}

type hierarchicalTableServiceClient struct {
	cc grpc.ClientConnInterface
}

func NewHierarchicalTableServiceClient(cc grpc.ClientConnInterface) HierarchicalTableServiceClient {
	return &hierarchicalTableServiceClient{cc}
}

func (c *hierarchicalTableServiceClient) Rollup(ctx context.Context, in *RollupRequest, opts ...grpc.CallOption) (*RollupResponse, error) {
	out := new(RollupResponse)
	err := c.cc.Invoke(ctx, "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Rollup", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *hierarchicalTableServiceClient) Tree(ctx context.Context, in *TreeRequest, opts ...grpc.CallOption) (*TreeResponse, error) {
	out := new(TreeResponse)
	err := c.cc.Invoke(ctx, "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Tree", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *hierarchicalTableServiceClient) Apply(ctx context.Context, in *HierarchicalTableApplyRequest, opts ...grpc.CallOption) (*HierarchicalTableApplyResponse, error) {
	out := new(HierarchicalTableApplyResponse)
	err := c.cc.Invoke(ctx, "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Apply", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *hierarchicalTableServiceClient) View(ctx context.Context, in *HierarchicalTableViewRequest, opts ...grpc.CallOption) (*HierarchicalTableViewResponse, error) {
	out := new(HierarchicalTableViewResponse)
	err := c.cc.Invoke(ctx, "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/View", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *hierarchicalTableServiceClient) ExportSource(ctx context.Context, in *HierarchicalTableSourceExportRequest, opts ...grpc.CallOption) (*table.ExportedTableCreationResponse, error) {
	out := new(table.ExportedTableCreationResponse)
	err := c.cc.Invoke(ctx, "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/ExportSource", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// HierarchicalTableServiceServer is the server API for HierarchicalTableService service.
// All implementations must embed UnimplementedHierarchicalTableServiceServer
// for forward compatibility
type HierarchicalTableServiceServer interface {
	// Applies a rollup operation to a Table and exports the resulting RollupTable
	Rollup(context.Context, *RollupRequest) (*RollupResponse, error)
	// Applies a tree operation to a Table and exports the resulting TreeTable
	Tree(context.Context, *TreeRequest) (*TreeResponse, error)
	// Applies operations to an existing HierarchicalTable (RollupTable or TreeTable) and exports the resulting
	// HierarchicalTable
	Apply(context.Context, *HierarchicalTableApplyRequest) (*HierarchicalTableApplyResponse, error)
	// Creates a view associating a Table of expansion keys and actions with an existing HierarchicalTable and exports
	// the resulting HierarchicalTableView for subsequent snapshot or subscription requests
	View(context.Context, *HierarchicalTableViewRequest) (*HierarchicalTableViewResponse, error)
	// Exports the source Table for a HierarchicalTable (Rollup or TreeTable)
	ExportSource(context.Context, *HierarchicalTableSourceExportRequest) (*table.ExportedTableCreationResponse, error)
	mustEmbedUnimplementedHierarchicalTableServiceServer()
}

// UnimplementedHierarchicalTableServiceServer must be embedded to have forward compatible implementations.
type UnimplementedHierarchicalTableServiceServer struct {
}

func (UnimplementedHierarchicalTableServiceServer) Rollup(context.Context, *RollupRequest) (*RollupResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Rollup not implemented")
}
func (UnimplementedHierarchicalTableServiceServer) Tree(context.Context, *TreeRequest) (*TreeResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Tree not implemented")
}
func (UnimplementedHierarchicalTableServiceServer) Apply(context.Context, *HierarchicalTableApplyRequest) (*HierarchicalTableApplyResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Apply not implemented")
}
func (UnimplementedHierarchicalTableServiceServer) View(context.Context, *HierarchicalTableViewRequest) (*HierarchicalTableViewResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method View not implemented")
}
func (UnimplementedHierarchicalTableServiceServer) ExportSource(context.Context, *HierarchicalTableSourceExportRequest) (*table.ExportedTableCreationResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method ExportSource not implemented")
}
func (UnimplementedHierarchicalTableServiceServer) mustEmbedUnimplementedHierarchicalTableServiceServer() {
}

// UnsafeHierarchicalTableServiceServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to HierarchicalTableServiceServer will
// result in compilation errors.
type UnsafeHierarchicalTableServiceServer interface {
	mustEmbedUnimplementedHierarchicalTableServiceServer()
}

func RegisterHierarchicalTableServiceServer(s grpc.ServiceRegistrar, srv HierarchicalTableServiceServer) {
	s.RegisterService(&HierarchicalTableService_ServiceDesc, srv)
}

func _HierarchicalTableService_Rollup_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(RollupRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HierarchicalTableServiceServer).Rollup(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Rollup",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HierarchicalTableServiceServer).Rollup(ctx, req.(*RollupRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _HierarchicalTableService_Tree_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(TreeRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HierarchicalTableServiceServer).Tree(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Tree",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HierarchicalTableServiceServer).Tree(ctx, req.(*TreeRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _HierarchicalTableService_Apply_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HierarchicalTableApplyRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HierarchicalTableServiceServer).Apply(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/Apply",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HierarchicalTableServiceServer).Apply(ctx, req.(*HierarchicalTableApplyRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _HierarchicalTableService_View_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HierarchicalTableViewRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HierarchicalTableServiceServer).View(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/View",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HierarchicalTableServiceServer).View(ctx, req.(*HierarchicalTableViewRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _HierarchicalTableService_ExportSource_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HierarchicalTableSourceExportRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HierarchicalTableServiceServer).ExportSource(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/io.deephaven.proto.backplane.grpc.HierarchicalTableService/ExportSource",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HierarchicalTableServiceServer).ExportSource(ctx, req.(*HierarchicalTableSourceExportRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// HierarchicalTableService_ServiceDesc is the grpc.ServiceDesc for HierarchicalTableService service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var HierarchicalTableService_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "io.deephaven.proto.backplane.grpc.HierarchicalTableService",
	HandlerType: (*HierarchicalTableServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Rollup",
			Handler:    _HierarchicalTableService_Rollup_Handler,
		},
		{
			MethodName: "Tree",
			Handler:    _HierarchicalTableService_Tree_Handler,
		},
		{
			MethodName: "Apply",
			Handler:    _HierarchicalTableService_Apply_Handler,
		},
		{
			MethodName: "View",
			Handler:    _HierarchicalTableService_View_Handler,
		},
		{
			MethodName: "ExportSource",
			Handler:    _HierarchicalTableService_ExportSource_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "deephaven/proto/hierarchicaltable.proto",
}
