/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieEncoderUtil;
import io.netty.handler.codec.http.DefaultCookie;

public final class ClientCookieEncoder {
    private ClientCookieEncoder() {
    }

    public static String encode(Cookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie");
        }
        StringBuilder stringBuilder = CookieEncoderUtil.stringBuilder();
        ClientCookieEncoder.encode(stringBuilder, cookie);
        return CookieEncoderUtil.stripTrailingSeparator(stringBuilder);
    }

    private static void encode(StringBuilder stringBuilder, Cookie cookie) {
        if (cookie.getVersion() >= 1) {
            CookieEncoderUtil.add(stringBuilder, "$Version", 1L);
        }
        CookieEncoderUtil.add(stringBuilder, cookie.getName(), cookie.getValue());
        if (cookie.getPath() != null) {
            CookieEncoderUtil.add(stringBuilder, "$Path", cookie.getPath());
        }
        if (cookie.getDomain() != null) {
            CookieEncoderUtil.add(stringBuilder, "$Domain", cookie.getDomain());
        }
        if (cookie.getVersion() >= 1 && !cookie.getPorts().isEmpty()) {
            stringBuilder.append('$');
            stringBuilder.append("Port");
            stringBuilder.append('=');
            stringBuilder.append('\"');
            for (int n : cookie.getPorts()) {
                stringBuilder.append(n);
                stringBuilder.append(',');
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '\"');
            stringBuilder.append(';');
            stringBuilder.append(' ');
        }
    }

    public static String encode(Iterable<Cookie> iterable) {
        if (iterable == null) {
            throw new NullPointerException("cookies");
        }
        StringBuilder stringBuilder = CookieEncoderUtil.stringBuilder();
        for (Cookie cookie : iterable) {
            if (cookie == null) break;
            ClientCookieEncoder.encode(stringBuilder, cookie);
        }
        return CookieEncoderUtil.stripTrailingSeparator(stringBuilder);
    }

    public static String encode(String string, String string2) {
        return ClientCookieEncoder.encode((Cookie)new DefaultCookie(string, string2));
    }

    public static String encode(Cookie ... cookieArray) {
        if (cookieArray == null) {
            throw new NullPointerException("cookies");
        }
        StringBuilder stringBuilder = CookieEncoderUtil.stringBuilder();
        for (Cookie cookie : cookieArray) {
            if (cookie == null) break;
            ClientCookieEncoder.encode(stringBuilder, cookie);
        }
        return CookieEncoderUtil.stripTrailingSeparator(stringBuilder);
    }
}

