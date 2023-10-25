/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieEncoderUtil;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.HttpHeaderDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public final class ServerCookieEncoder {
    public static String encode(String string, String string2) {
        return ServerCookieEncoder.encode((Cookie)new DefaultCookie(string, string2));
    }

    public static List<String> encode(Iterable<Cookie> iterable) {
        if (iterable == null) {
            throw new NullPointerException("cookies");
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Cookie cookie : iterable) {
            if (cookie == null) break;
            arrayList.add(ServerCookieEncoder.encode(cookie));
        }
        return arrayList;
    }

    public static String encode(Cookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie");
        }
        StringBuilder stringBuilder = CookieEncoderUtil.stringBuilder();
        CookieEncoderUtil.add(stringBuilder, cookie.getName(), cookie.getValue());
        if (cookie.getMaxAge() != Long.MIN_VALUE) {
            if (cookie.getVersion() == 0) {
                CookieEncoderUtil.addUnquoted(stringBuilder, "Expires", HttpHeaderDateFormat.get().format(new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000L)));
            } else {
                CookieEncoderUtil.add(stringBuilder, "Max-Age", cookie.getMaxAge());
            }
        }
        if (cookie.getPath() != null) {
            if (cookie.getVersion() > 0) {
                CookieEncoderUtil.add(stringBuilder, "Path", cookie.getPath());
            } else {
                CookieEncoderUtil.addUnquoted(stringBuilder, "Path", cookie.getPath());
            }
        }
        if (cookie.getDomain() != null) {
            if (cookie.getVersion() > 0) {
                CookieEncoderUtil.add(stringBuilder, "Domain", cookie.getDomain());
            } else {
                CookieEncoderUtil.addUnquoted(stringBuilder, "Domain", cookie.getDomain());
            }
        }
        if (cookie.isSecure()) {
            stringBuilder.append("Secure");
            stringBuilder.append(';');
            stringBuilder.append(' ');
        }
        if (cookie.isHttpOnly()) {
            stringBuilder.append("HTTPOnly");
            stringBuilder.append(';');
            stringBuilder.append(' ');
        }
        if (cookie.getVersion() >= 1) {
            if (cookie.getComment() != null) {
                CookieEncoderUtil.add(stringBuilder, "Comment", cookie.getComment());
            }
            CookieEncoderUtil.add(stringBuilder, "Version", 1L);
            if (cookie.getCommentUrl() != null) {
                CookieEncoderUtil.addQuoted(stringBuilder, "CommentURL", cookie.getCommentUrl());
            }
            if (!cookie.getPorts().isEmpty()) {
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
            if (cookie.isDiscard()) {
                stringBuilder.append("Discard");
                stringBuilder.append(';');
                stringBuilder.append(' ');
            }
        }
        return CookieEncoderUtil.stripTrailingSeparator(stringBuilder);
    }

    public static List<String> encode(Collection<Cookie> collection) {
        if (collection == null) {
            throw new NullPointerException("cookies");
        }
        ArrayList<String> arrayList = new ArrayList<String>(collection.size());
        for (Cookie cookie : collection) {
            if (cookie == null) break;
            arrayList.add(ServerCookieEncoder.encode(cookie));
        }
        return arrayList;
    }

    private ServerCookieEncoder() {
    }

    public static List<String> encode(Cookie ... cookieArray) {
        if (cookieArray == null) {
            throw new NullPointerException("cookies");
        }
        ArrayList<String> arrayList = new ArrayList<String>(cookieArray.length);
        for (Cookie cookie : cookieArray) {
            if (cookie == null) break;
            arrayList.add(ServerCookieEncoder.encode(cookie));
        }
        return arrayList;
    }
}

