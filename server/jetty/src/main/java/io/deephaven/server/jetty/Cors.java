package io.deephaven.server.jetty;

import io.deephaven.configuration.Configuration;
import io.deephaven.server.browserstreaming.BrowserStreamInterceptor;
import io.grpc.InternalStatus;
import io.grpc.internal.GrpcUtil;
import jakarta.servlet.DispatcherType;
import org.apache.arrow.flight.auth.AuthConstants;
import org.apache.arrow.flight.auth2.Auth2Constants;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class Cors {
    public static final String ALLOWED_ORIGINS = Configuration.getInstance().getStringWithDefault("deephaven.http.cores.allowedOrigins", "*");
    public static final String ALLOWED_METHODS = Configuration.getInstance().getStringWithDefault("deephaven.http.cores.allowedMethods", "POST");
    public static final String ALLOWED_REQUEST_HEADERS = Configuration.getInstance().getStringWithDefault("deephaven.http.cores.allowedRequestHeaders", "");
    public static final String EXPOSED_RESPONSE_HEADERS = Configuration.getInstance().getStringWithDefault("deephaven.http.cores.allowedResponseHeaders", "");

    public static void enableCrossOriginRequests(WebAppContext context) {
        // If requested, permit CORS requests
        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);

        // Permit all origins
        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, ALLOWED_ORIGINS);

        // Only support POST - technically gRPC can use GET, but we don't use any of those methods
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, ALLOWED_METHODS);

        // Required request headers for gRPC, gRPC-web, flight, and deephaven
        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, joinNonEmptyWithComma(
                // Required for CORS itself to work
                HttpHeader.ORIGIN.asString(),
                CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER,

                // Required for gRPC
                GrpcUtil.CONTENT_TYPE_KEY.name(),
                GrpcUtil.TIMEOUT_KEY.name(),

                // Optional for gRPC
                GrpcUtil.MESSAGE_ENCODING_KEY.name(),
                GrpcUtil.MESSAGE_ACCEPT_ENCODING_KEY.name(),
                GrpcUtil.CONTENT_ENCODING_KEY.name(),
                GrpcUtil.CONTENT_ACCEPT_ENCODING_KEY.name(),

                // Required for gRPC-web
                "x-grpc-web",
                // Optional for gRPC-web
                "x-user-agent",

                // Required for Flight auth 1/2
                AuthConstants.TOKEN_NAME,
                Auth2Constants.AUTHORIZATION_HEADER,

                // Required for DH gRPC browser bidi stream support
                BrowserStreamInterceptor.TICKET_HEADER_NAME,
                BrowserStreamInterceptor.SEQUENCE_HEADER_NAME,
                BrowserStreamInterceptor.HALF_CLOSE_HEADER_NAME,

                // Specified by configuration
                ALLOWED_REQUEST_HEADERS));

        // Response headers that the browser will need to be able to decode
        holder.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM, joinNonEmptyWithComma(
                Auth2Constants.AUTHORIZATION_HEADER,
                GrpcUtil.CONTENT_TYPE_KEY.name(),
                InternalStatus.CODE_KEY.name(),
                InternalStatus.MESSAGE_KEY.name(),
                // Not used (yet?), see io.grpc.protobuf.StatusProto
                "grpc-status-details-bin",

                // Specified by configuration
                EXPOSED_RESPONSE_HEADERS));

        // Add the filter on all requests
        context.addFilter(holder, "/*", EnumSet.noneOf(DispatcherType.class));
    }

    private static String joinNonEmptyWithComma(String... values) {
        return Arrays.stream(values)
                .filter(str -> !str.isBlank())
                .collect(Collectors.joining(","));
    }
}
