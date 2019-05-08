package com.github.eitraz.swedbank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eitraz.swedbank.bank.BankType;
import com.github.eitraz.swedbank.exception.SwedbankClientException;
import com.github.eitraz.swedbank.http.HttpMethod;
import com.github.eitraz.swedbank.model.Link;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.eitraz.swedbank.http.HttpMethod.*;
import static java.util.Optional.ofNullable;

public class SwedbankClient {
    private static final Logger logger = LoggerFactory.getLogger(SwedbankClient.class);

    private static final String DSID = "dsid";
    private static final String BASE_URI = "https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/";
    private static final String API_VERSION = "v5";

    private final BankType bankType;
    private final String authorization;

    private Map<String, String> cookies = new HashMap<>();

    public SwedbankClient(BankType bankType) {
        this.bankType = bankType;
        authorization = base64Encode(String.format("%s:%s", bankType.getId(), UUID.randomUUID().toString().toUpperCase()));
    }

    private String base64Encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    @SuppressWarnings("WeakerAccess")
    public <T> T get(String path, Class<T> responseType) throws SwedbankClientException {
        return get(path, null, responseType);
    }

    @SuppressWarnings("WeakerAccess")
    public <T> T get(String path, Map<String, String> queryParameters, Class<T> responseType) throws SwedbankClientException {
        return stringToJsonObject(
                doRequest(GET, path, queryParameters, null),
                responseType);
    }

    public <T> T post(String path, Object data, Class<T> responseType) throws SwedbankClientException {
        return stringToJsonObject(
                doRequest(POST, path, null, jsonObjectToString(data)),
                responseType);
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public <T> T put(String path, Object data, Class<T> responseType) throws SwedbankClientException {
        return stringToJsonObject(
                doRequest(PUT, path, null, jsonObjectToString(data)),
                responseType);
    }

    public <T> T follow(Link link, Class<T> responseType) throws SwedbankClientException {
        HttpMethod method = HttpMethod.parse(link.getMethod());
        String path = link.getUri();

        // Remove version from path
        path = StringUtils.removeStart(path, "/" + API_VERSION);

        Map<String, String> queryParameters;

        // Parse query parameters
        int indexOfQueryParams = path.indexOf("?");
        if (indexOfQueryParams != -1) {
            String queryParamsString = path.substring(indexOfQueryParams + 1);
            queryParameters = Arrays.stream(queryParamsString.split("&"))
                    .map(s -> StringUtils.split(s, "=", 2))
                    .collect(Collectors.toMap(
                            a -> a[0],
                            a -> a.length > 1 ? a[1] : "",
                            (a, b) -> a));

            path = path.substring(0, indexOfQueryParams);
        }
        // No query params
        else {
            queryParameters = null;
        }

        return stringToJsonObject(
                doRequest(method, path, queryParameters, null),
                responseType);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    private String jsonObjectToString(Object data) {
        if (data == null)
            return null;

        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JSON string from object", e);
        }
    }

    private <T> T stringToJsonObject(String string, Class<T> type) {
        if (string == null)
            return null;

        try {
            return getObjectMapper().readValue(string, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + type.getName() + " object from JSON string", e);
        }
    }

    private synchronized String doRequest(HttpMethod method, String path, Map<String, String> queryParameters, String data) throws SwedbankClientException {
        // DSID
        String dsid = RandomStringUtils.randomAlphabetic(16);

        // Null check for query parameters and headers
        queryParameters = ofNullable(queryParameters).orElseGet(HashMap::new);
        Map<String, String> headers = new HashMap<>();

        // Setup query parameters and headers
        queryParameters.put(DSID, dsid);
        headers.put("Cookie", getCookiesAsString(dsid));

        // Default headers
        headers.put("User-Agent", bankType.getUserAgent());
        headers.put("Authorization", authorization);
        headers.put("Accept", "*/*");
        headers.put("Accept-Language", "sv-se");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("Connection", "keep-alive");
        headers.put("Proxy-Connection", "keep-alive");
        headers.put("X-Client", "@fdpc/portal-private/157.0.0");

        HttpURLConnection connection = openConnection(getUrl(path, queryParameters));
        try {

            setRequestMethod(connection, method);

            // Set headers
            headers.forEach(connection::setRequestProperty);

            logRequest(connection, data);

            if (data != null) {
                setOutput(connection, data);
            }

            int responseCode = getResponseCode(connection);
            String responseBody = getResponseBody(connection, responseCode);

            logResponse(connection, responseCode, responseBody);

            // Valid response code
            if (responseCode < 400) {
                setCookies(connection);
                return responseBody;
            }
            // Invalid response code
            else {
                throw new SwedbankClientException(method, connection.getURL().toString(), responseCode, responseBody);
            }
        } finally {
            connection.disconnect();
        }
    }

    private void logRequest(HttpURLConnection connection, String data) {
        logger.info(">> Request {} {}{}",
                connection.getRequestMethod(),
                connection.getURL().toString(),
                ofNullable(data).map(body -> String.format(" with request body: %s", body))
                        .orElse(""));

        connection.getRequestProperties()
                .forEach((key, value) -> logger.debug(">> Request header: {} = {}", key, value));
    }

    private void logResponse(HttpURLConnection connection, int responseCode, String responseBody) {
        logger.info("<< Response for {} {} with status code {} and body: {}",
                connection.getRequestMethod(),
                connection.getURL().toString(),
                responseCode,
                responseBody);

        connection.getHeaderFields()
                .forEach((key, value) -> logger.debug("<< Response header: {} = {}", key, value));
    }

    private URL getUrl(String path, Map<String, String> queryParameters) {
        try {
            String queryParameter = queryParameters.entrySet().stream()
                    .map(q -> q.getKey() + "=" + q.getValue())
                    .collect(Collectors.joining("&"));

            // Remove leading '/'
            while (path.startsWith("/") && path.length() > 1) {
                path = path.substring(1);
            }

            return new URL(BASE_URI + API_VERSION + "/" + path + "?" + queryParameter);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    private HttpURLConnection openConnection(URL url) {
        try {
            logger.debug("Opening connection for {}", url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            return connection;
        } catch (IOException e) {
            throw new RuntimeException("Failed to open HTTP connection", e);
        }
    }

    private void setOutput(HttpURLConnection connection, String data) {
        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.setDoOutput(true);

        try {
            IOUtils.write(data, connection.getOutputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to HTTP output stream", e);
        }
    }

    private String getCookiesAsString(String dsid) {
        cookies.put(DSID, dsid);

        logger.debug(">> Request cookies: {}", cookies);

        return this.cookies.entrySet().stream()
                .map(e -> String.format("%s=%s;", e.getKey(), e.getValue()))
                .collect(Collectors.joining());
    }

    private void setCookies(HttpURLConnection connection) {
        Map<String, String> responseCookies = connection.getHeaderFields()
                .entrySet().stream()
                .filter(e -> "Set-Cookie".equalsIgnoreCase(e.getKey()))
                .map(e -> parseCookie(e.getValue()))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a
                ));

        cookies.putAll(responseCookies);
        logger.debug("<< Response cookies: {}", cookies);
    }

    private Map<String, String> parseCookie(List<String> headerValues) {
        return headerValues.stream()
                .map(this::parseCookie)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a
                ));
    }

    private Map<String, String> parseCookie(String headerValue) {
        return Arrays.stream(headerValue.split(";"))
                .filter(s -> s.contains("="))
                .map(s -> StringUtils.split(s, "=", 2))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> a.length > 1 ? a[1] : "",
                        (a, b) -> a));
    }

    private int getResponseCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get HTTP response code", e);
        }
    }

    private String getResponseBody(HttpURLConnection connection, int responseCode) {
        try (InputStream inputStream = getResponseInputStream(connection, responseCode)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read response input stream", e);
        }
    }

    private InputStream getResponseInputStream(HttpURLConnection connection, int responseCode) {
        try {
            return responseCode < 400 ? connection.getInputStream() : connection.getErrorStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain response input stream", e);
        }
    }

}
