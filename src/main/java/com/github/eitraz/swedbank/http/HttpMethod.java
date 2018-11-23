package com.github.eitraz.swedbank.http;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public final class HttpMethod {
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    public static final HttpMethod PUT = new HttpMethod("PUT");
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    public static final HttpMethod TRACE = new HttpMethod("TRACE");

    private static final List<HttpMethod> ALL_METHODS = Arrays.asList(
            GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
    );

    private final String method;

    private HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public HttpURLConnection setRequestMethodForConnection(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(getMethod());
            return connection;
        } catch (ProtocolException e) {
            throw new RuntimeException(String.format("Failed to set HTTP request method '%s'", getMethod()), e);
        }
    }

    public static HttpURLConnection setRequestMethod(HttpURLConnection connection, HttpMethod method) {
        return method.setRequestMethodForConnection(connection);
    }

    public static HttpMethod parse(final String method) {
        return ALL_METHODS.stream()
                          .filter(httpMethod -> httpMethod.getMethod().equalsIgnoreCase(method))
                          .findFirst()
                          .orElseThrow(() -> new UnsupportedOperationException(method + " is not a supported HTTP method"));
    }
}
