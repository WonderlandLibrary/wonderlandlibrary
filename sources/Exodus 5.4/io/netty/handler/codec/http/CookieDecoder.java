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

    private CookieDecoder() {
    }

    private static void extractKeyValuePairs(String string, List<String> list, List<String> list2) {
        int n = string.length();
        int n2 = 0;
        block10: while (n2 != n) {
            switch (string.charAt(n2)) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case ',': 
                case ';': {
                    ++n2;
                    continue block10;
                }
            }
            while (n2 != n) {
                String string2;
                String string3;
                if (string.charAt(n2) == '$') {
                    ++n2;
                    continue;
                }
                if (n2 == n) {
                    string3 = null;
                    string2 = null;
                } else {
                    int n3 = n2;
                    block12: while (true) {
                        switch (string.charAt(n2)) {
                            case ';': {
                                string3 = string.substring(n3, n2);
                                string2 = null;
                                break block12;
                            }
                            case '=': {
                                string3 = string.substring(n3, n2);
                                if (++n2 == n) {
                                    string2 = "";
                                    break block12;
                                }
                                int n4 = n2;
                                char c = string.charAt(n2);
                                if (c == '\"' || c == '\'') {
                                    StringBuilder stringBuilder = new StringBuilder(string.length() - n2);
                                    char c2 = c;
                                    boolean bl = false;
                                    ++n2;
                                    block13: while (true) {
                                        if (n2 == n) {
                                            string2 = stringBuilder.toString();
                                            break block12;
                                        }
                                        if (bl) {
                                            bl = false;
                                            c = string.charAt(n2++);
                                            switch (c) {
                                                case '\"': 
                                                case '\'': 
                                                case '\\': {
                                                    stringBuilder.setCharAt(stringBuilder.length() - 1, c);
                                                    continue block13;
                                                }
                                            }
                                            stringBuilder.append(c);
                                            continue;
                                        }
                                        if ((c = string.charAt(n2++)) == c2) {
                                            string2 = stringBuilder.toString();
                                            break block12;
                                        }
                                        stringBuilder.append(c);
                                        if (c != '\\') continue;
                                        bl = true;
                                    }
                                }
                                int n5 = string.indexOf(59, n2);
                                if (n5 > 0) {
                                    string2 = string.substring(n4, n5);
                                    n2 = n5;
                                    break block12;
                                }
                                string2 = string.substring(n4);
                                n2 = n;
                                break block12;
                            }
                            default: {
                                if (++n2 != n) continue block12;
                                string3 = string.substring(n3);
                                string2 = null;
                                break block12;
                            }
                        }
                        break;
                    }
                }
                list.add(string3);
                list2.add(string2);
                continue block10;
            }
            break block10;
        }
    }

    public static Set<Cookie> decode(String string) {
        int n;
        ArrayList<String> arrayList = new ArrayList<String>(8);
        ArrayList<String> arrayList2 = new ArrayList<String>(8);
        CookieDecoder.extractKeyValuePairs(string, arrayList, arrayList2);
        if (arrayList.isEmpty()) {
            return Collections.emptySet();
        }
        int n2 = 0;
        if (((String)arrayList.get(0)).equalsIgnoreCase("Version")) {
            try {
                n2 = Integer.parseInt((String)arrayList2.get(0));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            n = 1;
        } else {
            n = 0;
        }
        if (arrayList.size() <= n) {
            return Collections.emptySet();
        }
        TreeSet<Cookie> treeSet = new TreeSet<Cookie>();
        while (n < arrayList.size()) {
            String string2 = (String)arrayList.get(n);
            String string3 = (String)arrayList2.get(n);
            if (string3 == null) {
                string3 = "";
            }
            DefaultCookie defaultCookie = new DefaultCookie(string2, string3);
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = false;
            String string4 = null;
            String string5 = null;
            String string6 = null;
            String string7 = null;
            long l = Long.MIN_VALUE;
            ArrayList<Integer> arrayList3 = new ArrayList<Integer>(2);
            int n3 = n + 1;
            while (n3 < arrayList.size()) {
                string2 = (String)arrayList.get(n3);
                string3 = (String)arrayList2.get(n3);
                if ("Discard".equalsIgnoreCase(string2)) {
                    bl = true;
                } else if ("Secure".equalsIgnoreCase(string2)) {
                    bl2 = true;
                } else if ("HTTPOnly".equalsIgnoreCase(string2)) {
                    bl3 = true;
                } else if ("Comment".equalsIgnoreCase(string2)) {
                    string4 = string3;
                } else if ("CommentURL".equalsIgnoreCase(string2)) {
                    string5 = string3;
                } else if ("Domain".equalsIgnoreCase(string2)) {
                    string6 = string3;
                } else if ("Path".equalsIgnoreCase(string2)) {
                    string7 = string3;
                } else if ("Expires".equalsIgnoreCase(string2)) {
                    try {
                        long l2 = HttpHeaderDateFormat.get().parse(string3).getTime() - System.currentTimeMillis();
                        l = l2 / 1000L + (long)(l2 % 1000L != 0L ? 1 : 0);
                    }
                    catch (ParseException parseException) {}
                } else if ("Max-Age".equalsIgnoreCase(string2)) {
                    l = Integer.parseInt(string3);
                } else if ("Version".equalsIgnoreCase(string2)) {
                    n2 = Integer.parseInt(string3);
                } else {
                    String[] stringArray;
                    if (!"Port".equalsIgnoreCase(string2)) break;
                    for (String string8 : stringArray = StringUtil.split(string3, ',')) {
                        try {
                            arrayList3.add(Integer.valueOf(string8));
                        }
                        catch (NumberFormatException numberFormatException) {
                            // empty catch block
                        }
                    }
                }
                ++n3;
                ++n;
            }
            defaultCookie.setVersion(n2);
            defaultCookie.setMaxAge(l);
            defaultCookie.setPath(string7);
            defaultCookie.setDomain(string6);
            defaultCookie.setSecure(bl2);
            defaultCookie.setHttpOnly(bl3);
            if (n2 > 0) {
                defaultCookie.setComment(string4);
            }
            if (n2 > 1) {
                defaultCookie.setCommentUrl(string5);
                defaultCookie.setPorts(arrayList3);
                defaultCookie.setDiscard(bl);
            }
            treeSet.add(defaultCookie);
            ++n;
        }
        return treeSet;
    }
}

