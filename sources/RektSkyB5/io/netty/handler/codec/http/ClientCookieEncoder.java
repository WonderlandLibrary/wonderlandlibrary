/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieEncoderUtil;
import io.netty.handler.codec.http.DefaultCookie;

public final class ClientCookieEncoder {
    public static String encode(String name, String value) {
        return ClientCookieEncoder.encode((Cookie)new DefaultCookie(name, value));
    }

    public static String encode(Cookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie");
        }
        StringBuilder buf = CookieEncoderUtil.stringBuilder();
        ClientCookieEncoder.encode(buf, cookie);
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }

    public static String encode(Cookie ... cookies) {
        if (cookies == null) {
            throw new NullPointerException("cookies");
        }
        StringBuilder buf = CookieEncoderUtil.stringBuilder();
        for (Cookie c2 : cookies) {
            if (c2 == null) break;
            ClientCookieEncoder.encode(buf, c2);
        }
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }

    public static String encode(Iterable<Cookie> cookies) {
        if (cookies == null) {
            throw new NullPointerException("cookies");
        }
        StringBuilder buf = CookieEncoderUtil.stringBuilder();
        for (Cookie c2 : cookies) {
            if (c2 == null) break;
            ClientCookieEncoder.encode(buf, c2);
        }
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }

    private static void encode(StringBuilder buf, Cookie c2) {
        if (c2.getVersion() >= 1) {
            CookieEncoderUtil.add(buf, "$Version", 1L);
        }
        CookieEncoderUtil.add(buf, c2.getName(), c2.getValue());
        if (c2.getPath() != null) {
            CookieEncoderUtil.add(buf, "$Path", c2.getPath());
        }
        if (c2.getDomain() != null) {
            CookieEncoderUtil.add(buf, "$Domain", c2.getDomain());
        }
        if (c2.getVersion() >= 1 && !c2.getPorts().isEmpty()) {
            buf.append('$');
            buf.append("Port");
            buf.append('=');
            buf.append('\"');
            for (int port : c2.getPorts()) {
                buf.append(port);
                buf.append(',');
            }
            buf.setCharAt(buf.length() - 1, '\"');
            buf.append(';');
            buf.append(' ');
        }
    }

    private ClientCookieEncoder() {
    }
}

