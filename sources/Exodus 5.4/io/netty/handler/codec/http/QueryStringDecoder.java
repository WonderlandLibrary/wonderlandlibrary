/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.HttpConstants;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryStringDecoder {
    private final String uri;
    private Map<String, List<String>> params;
    private int nParams;
    private String path;
    private final Charset charset;
    private static final int DEFAULT_MAX_PARAMS = 1024;
    private final int maxParams;
    private final boolean hasPath;

    public QueryStringDecoder(String string, Charset charset) {
        this(string, charset, true);
    }

    public String path() {
        if (this.path == null) {
            if (!this.hasPath) {
                this.path = "";
                return "";
            }
            int n = this.uri.indexOf(63);
            if (n < 0) {
                this.path = this.uri;
            } else {
                this.path = this.uri.substring(0, n);
                return this.path;
            }
        }
        return this.path;
    }

    private static char decodeHexNibble(char c) {
        if ('0' <= c && c <= '9') {
            return (char)(c - 48);
        }
        if ('a' <= c && c <= 'f') {
            return (char)(c - 97 + 10);
        }
        if ('A' <= c && c <= 'F') {
            return (char)(c - 65 + 10);
        }
        return '\uffff';
    }

    public QueryStringDecoder(String string, Charset charset, boolean bl) {
        this(string, charset, bl, 1024);
    }

    public QueryStringDecoder(String string) {
        this(string, HttpConstants.DEFAULT_CHARSET);
    }

    public static String decodeComponent(String string, Charset charset) {
        int n;
        if (string == null) {
            return "";
        }
        int n2 = string.length();
        boolean bl = false;
        for (int i = 0; i < n2; ++i) {
            n = string.charAt(i);
            if (n != 37 && n != 43) continue;
            bl = true;
            break;
        }
        if (!bl) {
            return string;
        }
        byte[] byArray = new byte[n2];
        n = 0;
        block5: for (int i = 0; i < n2; ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '+': {
                    byArray[n++] = 32;
                    continue block5;
                }
                case '%': {
                    if (i == n2 - 1) {
                        throw new IllegalArgumentException("unterminated escape sequence at end of string: " + string);
                    }
                    if ((c = string.charAt(++i)) == '%') {
                        byArray[n++] = 37;
                        continue block5;
                    }
                    if (i == n2 - 1) {
                        throw new IllegalArgumentException("partial escape sequence at end of string: " + string);
                    }
                    c = QueryStringDecoder.decodeHexNibble(c);
                    char c2 = QueryStringDecoder.decodeHexNibble(string.charAt(++i));
                    if (c == '\uffff' || c2 == '\uffff') {
                        throw new IllegalArgumentException("invalid escape sequence `%" + string.charAt(i - 1) + string.charAt(i) + "' at index " + (i - 2) + " of: " + string);
                    }
                    c = (char)(c * 16 + c2);
                }
                default: {
                    byArray[n++] = (byte)c;
                }
            }
        }
        return new String(byArray, 0, n, charset);
    }

    public QueryStringDecoder(String string, boolean bl) {
        this(string, HttpConstants.DEFAULT_CHARSET, bl);
    }

    private boolean addParam(Map<String, List<String>> map, String string, String string2) {
        if (this.nParams >= this.maxParams) {
            return false;
        }
        List<String> list = map.get(string);
        if (list == null) {
            list = new ArrayList<String>(1);
            map.put(string, list);
        }
        list.add(string2);
        ++this.nParams;
        return true;
    }

    public QueryStringDecoder(URI uRI, Charset charset) {
        this(uRI, charset, 1024);
    }

    public static String decodeComponent(String string) {
        return QueryStringDecoder.decodeComponent(string, HttpConstants.DEFAULT_CHARSET);
    }

    private void decodeParams(String string) {
        int n;
        this.params = new LinkedHashMap<String, List<String>>();
        LinkedHashMap<String, List<String>> linkedHashMap = this.params;
        this.nParams = 0;
        String string2 = null;
        int n2 = 0;
        for (n = 0; n < string.length(); ++n) {
            char c = string.charAt(n);
            if (c == '=' && string2 == null) {
                if (n2 != n) {
                    string2 = QueryStringDecoder.decodeComponent(string.substring(n2, n), this.charset);
                }
                n2 = n + 1;
                continue;
            }
            if (c != '&' && c != ';') continue;
            if (string2 == null && n2 != n) {
                if (!this.addParam(linkedHashMap, QueryStringDecoder.decodeComponent(string.substring(n2, n), this.charset), "")) {
                    return;
                }
            } else if (string2 != null) {
                if (!this.addParam(linkedHashMap, string2, QueryStringDecoder.decodeComponent(string.substring(n2, n), this.charset))) {
                    return;
                }
                string2 = null;
            }
            n2 = n + 1;
        }
        if (n2 != n) {
            if (string2 == null) {
                this.addParam(linkedHashMap, QueryStringDecoder.decodeComponent(string.substring(n2, n), this.charset), "");
            } else {
                this.addParam(linkedHashMap, string2, QueryStringDecoder.decodeComponent(string.substring(n2, n), this.charset));
            }
        } else if (string2 != null) {
            this.addParam(linkedHashMap, string2, "");
        }
    }

    public Map<String, List<String>> parameters() {
        if (this.params == null) {
            if (this.hasPath) {
                int n = this.path().length();
                if (this.uri.length() == n) {
                    return Collections.emptyMap();
                }
                this.decodeParams(this.uri.substring(n + 1));
            } else {
                if (this.uri.isEmpty()) {
                    return Collections.emptyMap();
                }
                this.decodeParams(this.uri);
            }
        }
        return this.params;
    }

    public QueryStringDecoder(String string, Charset charset, boolean bl, int n) {
        if (string == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("maxParams: " + n + " (expected: a positive integer)");
        }
        this.uri = string;
        this.charset = charset;
        this.maxParams = n;
        this.hasPath = bl;
    }

    public QueryStringDecoder(URI uRI) {
        this(uRI, HttpConstants.DEFAULT_CHARSET);
    }

    public QueryStringDecoder(URI uRI, Charset charset, int n) {
        if (uRI == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("maxParams: " + n + " (expected: a positive integer)");
        }
        String string = uRI.getRawPath();
        if (string != null) {
            this.hasPath = true;
        } else {
            string = "";
            this.hasPath = false;
        }
        this.uri = string + '?' + uRI.getRawQuery();
        this.charset = charset;
        this.maxParams = n;
    }
}

