package com.github.eitraz.swedbank.model;

import com.github.eitraz.swedbank.http.HttpMethod;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"WeakerAccess", "unused"})
@Getter
@Setter
public class Link {
    private String method;
    private String uri;

    public Link() {
    }

    public Link(HttpMethod method, String uri) {
        this.method = method.getMethod();
        this.uri = uri;
    }
}
