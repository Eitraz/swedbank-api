package com.github.eitraz.swedbank.exception;

import com.github.eitraz.swedbank.http.HttpMethod;

@SuppressWarnings("unused")
public class SwedbankClientException extends Exception {
    private final HttpMethod requestMethod;
    private final String requestUrl;
    private final int responseStatusCode;
    private final String responseBody;

    public SwedbankClientException(HttpMethod requestMethod, String requestUrl, int responseStatusCode, String responseBody) {
        super(createMessage(requestMethod, requestUrl, responseStatusCode, responseBody));
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.responseStatusCode = responseStatusCode;
        this.responseBody = responseBody;
    }

    public SwedbankClientException(HttpMethod requestMethod, String requestUrl, int responseStatusCode, String responseBody, Throwable cause) {
        super(createMessage(requestMethod, requestUrl, responseStatusCode, responseBody), cause);
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.responseStatusCode = responseStatusCode;
        this.responseBody = responseBody;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    private static String createMessage(HttpMethod requestMethod, String requestUrl, int responseStatus, String responseBody) {
        return String.format("%s request for %s failed with status %d and body: %s",
                requestMethod != null ? requestMethod.getMethod() : "Unknown method",
                requestUrl,
                responseStatus,
                responseBody);
    }
}
