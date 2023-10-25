/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

public final class RtspMethods {
    public static final HttpMethod REDIRECT;
    public static final HttpMethod OPTIONS;
    public static final HttpMethod SETUP;
    private static final Map<String, HttpMethod> methodMap;
    public static final HttpMethod DESCRIBE;
    public static final HttpMethod RECORD;
    public static final HttpMethod PLAY;
    public static final HttpMethod TEARDOWN;
    public static final HttpMethod PAUSE;
    public static final HttpMethod GET_PARAMETER;
    public static final HttpMethod SET_PARAMETER;
    public static final HttpMethod ANNOUNCE;

    static {
        OPTIONS = HttpMethod.OPTIONS;
        DESCRIBE = new HttpMethod("DESCRIBE");
        ANNOUNCE = new HttpMethod("ANNOUNCE");
        SETUP = new HttpMethod("SETUP");
        PLAY = new HttpMethod("PLAY");
        PAUSE = new HttpMethod("PAUSE");
        TEARDOWN = new HttpMethod("TEARDOWN");
        GET_PARAMETER = new HttpMethod("GET_PARAMETER");
        SET_PARAMETER = new HttpMethod("SET_PARAMETER");
        REDIRECT = new HttpMethod("REDIRECT");
        RECORD = new HttpMethod("RECORD");
        methodMap = new HashMap<String, HttpMethod>();
        methodMap.put(DESCRIBE.toString(), DESCRIBE);
        methodMap.put(ANNOUNCE.toString(), ANNOUNCE);
        methodMap.put(GET_PARAMETER.toString(), GET_PARAMETER);
        methodMap.put(OPTIONS.toString(), OPTIONS);
        methodMap.put(PAUSE.toString(), PAUSE);
        methodMap.put(PLAY.toString(), PLAY);
        methodMap.put(RECORD.toString(), RECORD);
        methodMap.put(REDIRECT.toString(), REDIRECT);
        methodMap.put(SETUP.toString(), SETUP);
        methodMap.put(SET_PARAMETER.toString(), SET_PARAMETER);
        methodMap.put(TEARDOWN.toString(), TEARDOWN);
    }

    public static HttpMethod valueOf(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if ((string = string.trim().toUpperCase()).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        HttpMethod httpMethod = methodMap.get(string);
        if (httpMethod != null) {
            return httpMethod;
        }
        return new HttpMethod(string);
    }

    private RtspMethods() {
    }
}

