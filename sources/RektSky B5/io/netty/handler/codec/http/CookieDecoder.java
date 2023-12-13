/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.HttpHeaderDateFormat;
import io.netty.util.internal.StringUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class CookieDecoder {
    private static final char COMMA = ',';

    public static Set<Cookie> decode(String header) {
        int i2;
        ArrayList<String> names = new ArrayList<String>(8);
        ArrayList<String> values = new ArrayList<String>(8);
        CookieDecoder.extractKeyValuePairs(header, names, values);
        if (names.isEmpty()) {
            return Collections.emptySet();
        }
        int version = 0;
        if (((String)names.get(0)).equalsIgnoreCase("Version")) {
            try {
                version = Integer.parseInt((String)values.get(0));
            }
            catch (NumberFormatException e2) {
                // empty catch block
            }
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (names.size() <= i2) {
            return Collections.emptySet();
        }
        TreeSet<Cookie> cookies = new TreeSet<Cookie>();
        while (i2 < names.size()) {
            String name = (String)names.get(i2);
            String value = (String)values.get(i2);
            if (value == null) {
                value = "";
            }
            DefaultCookie c2 = new DefaultCookie(name, value);
            boolean discard = false;
            boolean secure = false;
            boolean httpOnly = false;
            String comment = null;
            String commentURL = null;
            String domain = null;
            String path = null;
            long maxAge = Long.MIN_VALUE;
            ArrayList<Integer> ports = new ArrayList<Integer>(2);
            int j2 = i2 + 1;
            while (j2 < names.size()) {
                name = (String)names.get(j2);
                value = (String)values.get(j2);
                if ("Discard".equalsIgnoreCase(name)) {
                    discard = true;
                } else if ("Secure".equalsIgnoreCase(name)) {
                    secure = true;
                } else if ("HTTPOnly".equalsIgnoreCase(name)) {
                    httpOnly = true;
                } else if ("Comment".equalsIgnoreCase(name)) {
                    comment = value;
                } else if ("CommentURL".equalsIgnoreCase(name)) {
                    commentURL = value;
                } else if ("Domain".equalsIgnoreCase(name)) {
                    domain = value;
                } else if ("Path".equalsIgnoreCase(name)) {
                    path = value;
                } else if ("Expires".equalsIgnoreCase(name)) {
                    try {
                        long maxAgeMillis = HttpHeaderDateFormat.get().parse(value).getTime() - System.currentTimeMillis();
                        maxAge = maxAgeMillis / 1000L + (long)(maxAgeMillis % 1000L != 0L ? 1 : 0);
                    }
                    catch (ParseException e3) {}
                } else if ("Max-Age".equalsIgnoreCase(name)) {
                    maxAge = Integer.parseInt(value);
                } else if ("Version".equalsIgnoreCase(name)) {
                    version = Integer.parseInt(value);
                } else {
                    String[] portList;
                    if (!"Port".equalsIgnoreCase(name)) break;
                    for (String s1 : portList = StringUtil.split(value, ',')) {
                        try {
                            ports.add(Integer.valueOf(s1));
                        }
                        catch (NumberFormatException e4) {
                            // empty catch block
                        }
                    }
                }
                ++j2;
                ++i2;
            }
            c2.setVersion(version);
            c2.setMaxAge(maxAge);
            c2.setPath(path);
            c2.setDomain(domain);
            c2.setSecure(secure);
            c2.setHttpOnly(httpOnly);
            if (version > 0) {
                c2.setComment(comment);
            }
            if (version > 1) {
                c2.setCommentUrl(commentURL);
                c2.setPorts(ports);
                c2.setDiscard(discard);
            }
            cookies.add(c2);
            ++i2;
        }
        return cookies;
    }

    private static void extractKeyValuePairs(String header, List<String> names, List<String> values) {
        int headerLen = header.length();
        int i2 = 0;
        block10: while (i2 != headerLen) {
            switch (header.charAt(i2)) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case ',': 
                case ';': {
                    ++i2;
                    continue block10;
                }
            }
            while (i2 != headerLen) {
                String value;
                String name;
                if (header.charAt(i2) == '$') {
                    ++i2;
                    continue;
                }
                if (i2 == headerLen) {
                    name = null;
                    value = null;
                } else {
                    int newNameStart = i2;
                    block12: while (true) {
                        switch (header.charAt(i2)) {
                            case ';': {
                                name = header.substring(newNameStart, i2);
                                value = null;
                                break block12;
                            }
                            case '=': {
                                name = header.substring(newNameStart, i2);
                                if (++i2 == headerLen) {
                                    value = "";
                                    break block12;
                                }
                                int newValueStart = i2;
                                char c2 = header.charAt(i2);
                                if (c2 == '\"' || c2 == '\'') {
                                    StringBuilder newValueBuf = new StringBuilder(header.length() - i2);
                                    char q2 = c2;
                                    boolean hadBackslash = false;
                                    ++i2;
                                    block13: while (true) {
                                        if (i2 == headerLen) {
                                            value = newValueBuf.toString();
                                            break block12;
                                        }
                                        if (hadBackslash) {
                                            hadBackslash = false;
                                            c2 = header.charAt(i2++);
                                            switch (c2) {
                                                case '\"': 
                                                case '\'': 
                                                case '\\': {
                                                    newValueBuf.setCharAt(newValueBuf.length() - 1, c2);
                                                    continue block13;
                                                }
                                            }
                                            newValueBuf.append(c2);
                                            continue;
                                        }
                                        if ((c2 = header.charAt(i2++)) == q2) {
                                            value = newValueBuf.toString();
                                            break block12;
                                        }
                                        newValueBuf.append(c2);
                                        if (c2 != '\\') continue;
                                        hadBackslash = true;
                                    }
                                }
                                int semiPos = header.indexOf(59, i2);
                                if (semiPos > 0) {
                                    value = header.substring(newValueStart, semiPos);
                                    i2 = semiPos;
                                    break block12;
                                }
                                value = header.substring(newValueStart);
                                i2 = headerLen;
                                break block12;
                            }
                            default: {
                                if (++i2 != headerLen) continue block12;
                                name = header.substring(newNameStart);
                                value = null;
                                break block12;
                            }
                        }
                        break;
                    }
                }
                names.add(name);
                values.add(value);
                continue block10;
            }
            break block10;
        }
    }

    private CookieDecoder() {
    }
}

