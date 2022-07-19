# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: deephaven/proto/object.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from pydeephaven.proto import ticket_pb2 as deephaven_dot_proto_dot_ticket__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x1c\x64\x65\x65phaven/proto/object.proto\x12!io.deephaven.proto.backplane.grpc\x1a\x1c\x64\x65\x65phaven/proto/ticket.proto\"W\n\x12\x46\x65tchObjectRequest\x12\x41\n\tsource_id\x18\x01 \x01(\x0b\x32..io.deephaven.proto.backplane.grpc.TypedTicket\"z\n\x13\x46\x65tchObjectResponse\x12\x0c\n\x04type\x18\x01 \x01(\t\x12\x0c\n\x04\x64\x61ta\x18\x02 \x01(\x0c\x12G\n\x0ftyped_export_id\x18\x03 \x03(\x0b\x32..io.deephaven.proto.backplane.grpc.TypedTicket2\x8f\x01\n\rObjectService\x12~\n\x0b\x46\x65tchObject\x12\x35.io.deephaven.proto.backplane.grpc.FetchObjectRequest\x1a\x36.io.deephaven.proto.backplane.grpc.FetchObjectResponse\"\x00\x42IH\x01P\x01ZCgithub.com/deephaven/deephaven-core/go-client/internal/proto/objectb\x06proto3')



_FETCHOBJECTREQUEST = DESCRIPTOR.message_types_by_name['FetchObjectRequest']
_FETCHOBJECTRESPONSE = DESCRIPTOR.message_types_by_name['FetchObjectResponse']
FetchObjectRequest = _reflection.GeneratedProtocolMessageType('FetchObjectRequest', (_message.Message,), {
  'DESCRIPTOR' : _FETCHOBJECTREQUEST,
  '__module__' : 'pydeephaven.proto.object_pb2'
  # @@protoc_insertion_point(class_scope:io.deephaven.proto.backplane.grpc.FetchObjectRequest)
  })
_sym_db.RegisterMessage(FetchObjectRequest)

FetchObjectResponse = _reflection.GeneratedProtocolMessageType('FetchObjectResponse', (_message.Message,), {
  'DESCRIPTOR' : _FETCHOBJECTRESPONSE,
  '__module__' : 'pydeephaven.proto.object_pb2'
  # @@protoc_insertion_point(class_scope:io.deephaven.proto.backplane.grpc.FetchObjectResponse)
  })
_sym_db.RegisterMessage(FetchObjectResponse)

_OBJECTSERVICE = DESCRIPTOR.services_by_name['ObjectService']
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'H\001P\001ZCgithub.com/deephaven/deephaven-core/go-client/internal/proto/object'
  _FETCHOBJECTREQUEST._serialized_start=97
  _FETCHOBJECTREQUEST._serialized_end=184
  _FETCHOBJECTRESPONSE._serialized_start=186
  _FETCHOBJECTRESPONSE._serialized_end=308
  _OBJECTSERVICE._serialized_start=311
  _OBJECTSERVICE._serialized_end=454
# @@protoc_insertion_point(module_scope)
