# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: deephaven/proto/config.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x1c\x64\x65\x65phaven/proto/config.proto\x12!io.deephaven.proto.backplane.grpc\" \n\x1e\x41uthenticationConstantsRequest\"\x1f\n\x1d\x43onfigurationConstantsRequest\"\xf3\x01\n\x1f\x41uthenticationConstantsResponse\x12k\n\rconfig_values\x18\x01 \x03(\x0b\x32T.io.deephaven.proto.backplane.grpc.AuthenticationConstantsResponse.ConfigValuesEntry\x1a\x63\n\x11\x43onfigValuesEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12=\n\x05value\x18\x02 \x01(\x0b\x32..io.deephaven.proto.backplane.grpc.ConfigValue:\x02\x38\x01\"\xf1\x01\n\x1e\x43onfigurationConstantsResponse\x12j\n\rconfig_values\x18\x01 \x03(\x0b\x32S.io.deephaven.proto.backplane.grpc.ConfigurationConstantsResponse.ConfigValuesEntry\x1a\x63\n\x11\x43onfigValuesEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12=\n\x05value\x18\x02 \x01(\x0b\x32..io.deephaven.proto.backplane.grpc.ConfigValue:\x02\x38\x01\"-\n\x0b\x43onfigValue\x12\x16\n\x0cstring_value\x18\x02 \x01(\tH\x00\x42\x06\n\x04kind2\xdc\x02\n\rConfigService\x12\xa5\x01\n\x1aGetAuthenticationConstants\x12\x41.io.deephaven.proto.backplane.grpc.AuthenticationConstantsRequest\x1a\x42.io.deephaven.proto.backplane.grpc.AuthenticationConstantsResponse\"\x00\x12\xa2\x01\n\x19GetConfigurationConstants\x12@.io.deephaven.proto.backplane.grpc.ConfigurationConstantsRequest\x1a\x41.io.deephaven.proto.backplane.grpc.ConfigurationConstantsResponse\"\x00\x42\x42H\x01P\x01Z<github.com/deephaven/deephaven-core/go/internal/proto/configb\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'deephaven.proto.config_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'H\001P\001Z<github.com/deephaven/deephaven-core/go/internal/proto/config'
  _AUTHENTICATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._options = None
  _AUTHENTICATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_options = b'8\001'
  _CONFIGURATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._options = None
  _CONFIGURATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_options = b'8\001'
  _AUTHENTICATIONCONSTANTSREQUEST._serialized_start=67
  _AUTHENTICATIONCONSTANTSREQUEST._serialized_end=99
  _CONFIGURATIONCONSTANTSREQUEST._serialized_start=101
  _CONFIGURATIONCONSTANTSREQUEST._serialized_end=132
  _AUTHENTICATIONCONSTANTSRESPONSE._serialized_start=135
  _AUTHENTICATIONCONSTANTSRESPONSE._serialized_end=378
  _AUTHENTICATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_start=279
  _AUTHENTICATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_end=378
  _CONFIGURATIONCONSTANTSRESPONSE._serialized_start=381
  _CONFIGURATIONCONSTANTSRESPONSE._serialized_end=622
  _CONFIGURATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_start=279
  _CONFIGURATIONCONSTANTSRESPONSE_CONFIGVALUESENTRY._serialized_end=378
  _CONFIGVALUE._serialized_start=624
  _CONFIGVALUE._serialized_end=669
  _CONFIGSERVICE._serialized_start=672
  _CONFIGSERVICE._serialized_end=1020
# @@protoc_insertion_point(module_scope)
