/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.util;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataURI {
    private final String originalUri;
    private final String originalData;
    private final String mimeType;
    private final String mimeSubtype;
    private final Map<String, String> parameters;
    private final boolean base64;
    private final byte[] data;

    public static boolean matchScheme(String string) {
        if (string == null || string.length() < 6) {
            return false;
        }
        return (string = string.stripLeading()).length() > 5 && "data:".equalsIgnoreCase(string.substring(0, 5));
    }

    public static DataURI tryParse(String string) {
        if (!DataURI.matchScheme(string)) {
            return null;
        }
        int n = (string = string.trim()).indexOf(44, 5);
        if (n < 0) {
            throw new IllegalArgumentException("Invalid URI: " + string);
        }
        String string2 = "text";
        String string3 = "plain";
        boolean bl = false;
        String[] arrstring = string.substring(5, n).split(";");
        Map<String, String> map = Collections.emptyMap();
        if (arrstring.length > 0) {
            int n2 = 0;
            int n3 = arrstring[0].indexOf(47);
            if (n3 > 0) {
                string2 = arrstring[0].substring(0, n3);
                string3 = arrstring[0].substring(n3 + 1);
                n2 = 1;
            }
            for (int i = n2; i < arrstring.length; ++i) {
                String string4 = arrstring[i];
                int n4 = string4.indexOf(61);
                if (n4 < 0) {
                    if (i < arrstring.length - 1) {
                        throw new IllegalArgumentException("Invalid URI: " + string);
                    }
                    bl = "base64".equalsIgnoreCase(arrstring[arrstring.length - 1]);
                    continue;
                }
                if (map.isEmpty()) {
                    map = new HashMap<String, String>();
                }
                map.put(string4.substring(0, n4).toLowerCase(), string4.substring(n4 + 1));
            }
        }
        String string5 = string.substring(n + 1);
        Charset charset = Charset.defaultCharset();
        return new DataURI(string, string5, string2, string3, map, bl, bl ? Base64.getDecoder().decode(string5) : URLDecoder.decode(string5.replace("+", "%2B"), charset).getBytes(charset));
    }

    private DataURI(String string, String string2, String string3, String string4, Map<String, String> map, boolean bl, byte[] arrby) {
        this.originalUri = string;
        this.originalData = string2;
        this.mimeType = string3;
        this.mimeSubtype = string4;
        this.parameters = map;
        this.base64 = bl;
        this.data = arrby;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getMimeSubtype() {
        return this.mimeSubtype;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public boolean isBase64() {
        return this.base64;
    }

    public byte[] getData() {
        return this.data;
    }

    public String toString() {
        if (this.originalData.length() < 32) {
            return this.originalUri;
        }
        return this.originalUri.substring(0, this.originalUri.length() - this.originalData.length()) + this.originalData.substring(0, 14) + "..." + this.originalData.substring(this.originalData.length() - 14);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DataURI)) {
            return false;
        }
        DataURI dataURI = (DataURI)object;
        return this.base64 == dataURI.base64 && Objects.equals(this.mimeType, dataURI.mimeType) && Objects.equals(this.mimeSubtype, dataURI.mimeSubtype) && Arrays.equals(this.data, dataURI.data);
    }

    public int hashCode() {
        int n = Objects.hash(this.mimeType, this.mimeSubtype, this.base64);
        n = 31 * n + Arrays.hashCode(this.data);
        return n;
    }
}

