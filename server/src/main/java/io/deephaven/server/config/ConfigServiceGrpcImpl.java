package io.deephaven.server.config;

import io.deephaven.configuration.Configuration;
import io.deephaven.extensions.barrage.util.GrpcUtil;
import io.deephaven.internal.log.LoggerFactory;
import io.deephaven.io.logger.Logger;
import io.deephaven.proto.backplane.grpc.AuthenticationConstantsRequest;
import io.deephaven.proto.backplane.grpc.AuthenticationConstantsResponse;
import io.deephaven.proto.backplane.grpc.ConfigPair;
import io.deephaven.proto.backplane.grpc.ConfigServiceGrpc;
import io.deephaven.proto.backplane.grpc.ConfigurationConstantsRequest;
import io.deephaven.proto.backplane.grpc.ConfigurationConstantsResponse;
import io.grpc.stub.StreamObserver;

import javax.inject.Inject;
import java.util.Arrays;

public class ConfigServiceGrpcImpl extends ConfigServiceGrpc.ConfigServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(ConfigServiceGrpcImpl.class);
    private static final String AUTH_CLIENT_CONFIG_PROPERTY = "authentication.client.configuration.list";
    private static final String CLIENT_CONFIG_PROPERTY = "client.configuration.list";

    private final Configuration configuration = Configuration.getInstance();

    @Inject
    public ConfigServiceGrpcImpl() {
    }

    @Override
    public void getAuthenticationConstants(AuthenticationConstantsRequest request, StreamObserver<AuthenticationConstantsResponse> responseObserver) {
        GrpcUtil.rpcWrapper(log, responseObserver, () -> {
            AuthenticationConstantsResponse.Builder builder = AuthenticationConstantsResponse.newBuilder();
            Arrays.stream(AUTH_CLIENT_CONFIG_PROPERTY.split(","))
                    .map(key -> ConfigPair.newBuilder()
                            .setKey(key)
                            .setStringValue(configuration.getProperty(key))
                            .build())
                    .forEach(builder::addConfigValues);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void getConfigurationConstants(ConfigurationConstantsRequest request, StreamObserver<ConfigurationConstantsResponse> responseObserver) {
        GrpcUtil.rpcWrapper(log, responseObserver, () -> {
            ConfigurationConstantsResponse.Builder builder = ConfigurationConstantsResponse.newBuilder();
            Arrays.stream(CLIENT_CONFIG_PROPERTY.split(","))
                    .map(key -> ConfigPair.newBuilder()
                            .setKey(key)
                            .setStringValue(configuration.getProperty(key))
                            .build())
                    .forEach(builder::addConfigValues);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        });
    }
}
