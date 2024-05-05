/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.ECMAErrors;

public final class URIUtils {
    private static final String URI_UNESCAPED_NONALPHANUMERIC = "-_.!~*'()";
    private static final String URI_RESERVED = ";/?:@&=+$,#";

    private URIUtils() {
    }

    static String encodeURI(Object self, String string) {
        return URIUtils.encode(self, string, false);
    }

    static String encodeURIComponent(Object self, String string) {
        return URIUtils.encode(self, string, true);
    }

    static String decodeURI(Object self, String string) {
        return URIUtils.decode(self, string, false);
    }

    static String decodeURIComponent(Object self, String string) {
        return URIUtils.decode(self, string, true);
    }

    private static String encode(Object self, String string, boolean component) {
        if (string.isEmpty()) {
            return string;
        }
        int len = string.length();
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < len; ++k) {
            int V;
            int C = string.charAt(k);
            if (URIUtils.isUnescaped((char)C, component)) {
                sb.append((char)C);
                continue;
            }
            if (C >= 56320 && C <= 57343) {
                return URIUtils.error(string, k);
            }
            if (C < 55296 || C > 56319) {
                V = C;
            } else {
                if (++k == len) {
                    return URIUtils.error(string, k);
                }
                char kChar = string.charAt(k);
                if (kChar < '\udc00' || kChar > '\udfff') {
                    return URIUtils.error(string, k);
                }
                V = (C - 55296) * 1024 + (kChar - 56320) + 65536;
            }
            try {
                sb.append(URIUtils.toHexEscape(V));
                continue;
            }
            catch (Exception e) {
                throw ECMAErrors.uriError(e, "bad.uri", string, Integer.toString(k));
            }
        }
        return sb.toString();
    }

    private static String decode(Object self, String string, boolean component) {
        if (string.isEmpty()) {
            return string;
        }
        int len = string.length();
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < len; ++k) {
            int j;
            int minV;
            int V;
            int n;
            char C;
            char ch = string.charAt(k);
            if (ch != '%') {
                sb.append(ch);
                continue;
            }
            int start = k;
            if (k + 2 >= len) {
                return URIUtils.error(string, k);
            }
            int B = URIUtils.toHexByte(string.charAt(k + 1), string.charAt(k + 2));
            if (B < 0) {
                return URIUtils.error(string, k + 1);
            }
            k += 2;
            if ((B & 0x80) == 0) {
                C = (char)B;
                if (!component && URI_RESERVED.indexOf(C) >= 0) {
                    for (int j2 = start; j2 <= k; ++j2) {
                        sb.append(string.charAt(j2));
                    }
                    continue;
                }
                sb.append(C);
                continue;
            }
            if ((B & 0xC0) == 128) {
                return URIUtils.error(string, k);
            }
            if ((B & 0x20) == 0) {
                n = 2;
                V = B & 0x1F;
                minV = 128;
            } else if ((B & 0x10) == 0) {
                n = 3;
                V = B & 0xF;
                minV = 2048;
            } else if ((B & 8) == 0) {
                n = 4;
                V = B & 7;
                minV = 65536;
            } else if ((B & 4) == 0) {
                n = 5;
                V = B & 3;
                minV = 0x200000;
            } else if ((B & 2) == 0) {
                n = 6;
                V = B & 1;
                minV = 0x4000000;
            } else {
                return URIUtils.error(string, k);
            }
            if (k + 3 * (n - 1) >= len) {
                return URIUtils.error(string, k);
            }
            for (j = 1; j < n; ++j) {
                if (string.charAt(++k) != '%') {
                    return URIUtils.error(string, k);
                }
                B = URIUtils.toHexByte(string.charAt(k + 1), string.charAt(k + 2));
                if (B < 0 || (B & 0xC0) != 128) {
                    return URIUtils.error(string, k + 1);
                }
                V = V << 6 | B & 0x3F;
                k += 2;
            }
            if (V < minV || V >= 55296 && V <= 57343) {
                V = Integer.MAX_VALUE;
            }
            if (V < 65536) {
                C = (char)V;
                if (!component && URI_RESERVED.indexOf(C) >= 0) {
                    for (j = start; j != k; ++j) {
                        sb.append(string.charAt(j));
                    }
                    continue;
                }
                sb.append(C);
                continue;
            }
            if (V > 0x10FFFF) {
                return URIUtils.error(string, k);
            }
            int L = (V - 65536 & 0x3FF) + 56320;
            int H = (V - 65536 >> 10 & 0x3FF) + 55296;
            sb.append((char)H);
            sb.append((char)L);
        }
        return sb.toString();
    }

    private static int hexDigit(char ch) {
        char chu = Character.toUpperCase(ch);
        if (chu >= '0' && chu <= '9') {
            return chu - 48;
        }
        if (chu >= 'A' && chu <= 'F') {
            return chu - 65 + 10;
        }
        return -1;
    }

    private static int toHexByte(char ch1, char ch2) {
        int i1 = URIUtils.hexDigit(ch1);
        int i2 = URIUtils.hexDigit(ch2);
        if (i1 >= 0 && i2 >= 0) {
            return i1 << 4 | i2;
        }
        return -1;
    }

    private static String toHexEscape(int u0) {
        int len;
        int u = u0;
        byte[] b = new byte[6];
        if (u <= 127) {
            b[0] = (byte)u;
            len = 1;
        } else {
            len = 2;
            for (int mask = u >>> 11; mask != 0; mask >>>= 5) {
                ++len;
            }
            for (int i = len - 1; i > 0; --i) {
                b[i] = (byte)(0x80 | u & 0x3F);
                u >>>= 6;
            }
            b[0] = (byte)(~((1 << 8 - len) - 1) | u);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            sb.append('%');
            if ((b[i] & 0xFF) < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b[i] & 0xFF).toUpperCase());
        }
        return sb.toString();
    }

    private static String error(String string, int index) {
        throw ECMAErrors.uriError("bad.uri", string, Integer.toString(index));
    }

    private static boolean isUnescaped(char ch, boolean component) {
        if ('A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || '0' <= ch && ch <= '9') {
            return true;
        }
        if (URI_UNESCAPED_NONALPHANUMERIC.indexOf(ch) >= 0) {
            return true;
        }
        if (!component) {
            return URI_RESERVED.indexOf(ch) >= 0;
        }
        return false;
    }
}

