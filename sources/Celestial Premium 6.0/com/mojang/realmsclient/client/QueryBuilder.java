/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueryBuilder {
    private final Map<String, String> queryParams = new HashMap<String, String>();

    public static QueryBuilder of(String key, String value) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.queryParams.put(key, value);
        return queryBuilder;
    }

    public static QueryBuilder empty() {
        return new QueryBuilder();
    }

    public QueryBuilder with(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public QueryBuilder with(Object key, Object value) {
        this.queryParams.put(String.valueOf(key), String.valueOf(value));
        return this;
    }

    public String toQueryString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> keyIterator = this.queryParams.keySet().iterator();
        if (!keyIterator.hasNext()) {
            return null;
        }
        String firstKey = keyIterator.next();
        stringBuilder.append(firstKey).append("=").append(this.encodeString(this.queryParams.get(firstKey)));
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            stringBuilder.append("&").append(key).append("=").append(this.encodeString(this.queryParams.get(key)));
        }
        return stringBuilder.toString();
    }

    private String encodeString(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return value;
        }
    }
}

