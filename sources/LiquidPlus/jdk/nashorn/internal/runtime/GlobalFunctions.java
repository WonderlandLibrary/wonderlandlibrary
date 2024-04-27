/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.URIUtils;

public final class GlobalFunctions {
    public static final MethodHandle PARSEINT = GlobalFunctions.findOwnMH("parseInt", Double.TYPE, Object.class, Object.class, Object.class);
    public static final MethodHandle PARSEINT_OI = GlobalFunctions.findOwnMH("parseInt", Double.TYPE, Object.class, Object.class, Integer.TYPE);
    public static final MethodHandle PARSEINT_Z = Lookup.MH.dropArguments(Lookup.MH.dropArguments(Lookup.MH.constant(Double.TYPE, Double.NaN), 0, Boolean.TYPE), 0, Object.class);
    public static final MethodHandle PARSEINT_I = Lookup.MH.dropArguments(Lookup.MH.identity(Integer.TYPE), 0, Object.class);
    public static final MethodHandle PARSEINT_O = GlobalFunctions.findOwnMH("parseInt", Double.TYPE, Object.class, Object.class);
    public static final MethodHandle PARSEFLOAT = GlobalFunctions.findOwnMH("parseFloat", Double.TYPE, Object.class, Object.class);
    public static final MethodHandle IS_NAN_I = Lookup.MH.dropArguments(Lookup.MH.constant(Boolean.TYPE, false), 0, Object.class);
    public static final MethodHandle IS_NAN_J = Lookup.MH.dropArguments(Lookup.MH.constant(Boolean.TYPE, false), 0, Object.class);
    public static final MethodHandle IS_NAN_D = Lookup.MH.dropArguments(Lookup.MH.findStatic(MethodHandles.lookup(), Double.class, "isNaN", Lookup.MH.type(Boolean.TYPE, Double.TYPE)), 0, Object.class);
    public static final MethodHandle IS_NAN = GlobalFunctions.findOwnMH("isNaN", Boolean.TYPE, Object.class, Object.class);
    public static final MethodHandle IS_FINITE = GlobalFunctions.findOwnMH("isFinite", Boolean.TYPE, Object.class, Object.class);
    public static final MethodHandle ENCODE_URI = GlobalFunctions.findOwnMH("encodeURI", Object.class, Object.class, Object.class);
    public static final MethodHandle ENCODE_URICOMPONENT = GlobalFunctions.findOwnMH("encodeURIComponent", Object.class, Object.class, Object.class);
    public static final MethodHandle DECODE_URI = GlobalFunctions.findOwnMH("decodeURI", Object.class, Object.class, Object.class);
    public static final MethodHandle DECODE_URICOMPONENT = GlobalFunctions.findOwnMH("decodeURIComponent", Object.class, Object.class, Object.class);
    public static final MethodHandle ESCAPE = GlobalFunctions.findOwnMH("escape", String.class, Object.class, Object.class);
    public static final MethodHandle UNESCAPE = GlobalFunctions.findOwnMH("unescape", String.class, Object.class, Object.class);
    public static final MethodHandle ANONYMOUS = GlobalFunctions.findOwnMH("anonymous", Object.class, Object.class);
    private static final String UNESCAPED = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@*_+-./";

    private GlobalFunctions() {
    }

    public static double parseInt(Object self, Object string, Object rad) {
        return GlobalFunctions.parseIntInternal(JSType.trimLeft(JSType.toString(string)), JSType.toInt32(rad));
    }

    public static double parseInt(Object self, Object string, int rad) {
        return GlobalFunctions.parseIntInternal(JSType.trimLeft(JSType.toString(string)), rad);
    }

    public static double parseInt(Object self, Object string) {
        return GlobalFunctions.parseIntInternal(JSType.trimLeft(JSType.toString(string)), 0);
    }

    private static double parseIntInternal(String str, int rad) {
        int digit;
        int length = str.length();
        int radix = rad;
        if (length == 0) {
            return Double.NaN;
        }
        boolean negative = false;
        int idx = 0;
        char firstChar = str.charAt(idx);
        if (firstChar < '0') {
            if (firstChar == '-') {
                negative = true;
            } else if (firstChar != '+') {
                return Double.NaN;
            }
            ++idx;
        }
        boolean stripPrefix = true;
        if (radix != 0) {
            if (radix < 2 || radix > 36) {
                return Double.NaN;
            }
            if (radix != 16) {
                stripPrefix = false;
            }
        } else {
            radix = 10;
        }
        if (stripPrefix && idx + 1 < length) {
            char c1 = str.charAt(idx);
            char c2 = str.charAt(idx + 1);
            if (c1 == '0' && (c2 == 'x' || c2 == 'X')) {
                radix = 16;
                idx += 2;
            }
        }
        double result = 0.0;
        boolean entered = false;
        while (idx < length && (digit = GlobalFunctions.fastDigit(str.charAt(idx++), radix)) >= 0) {
            entered = true;
            result *= (double)radix;
            result += (double)digit;
        }
        return entered ? (negative ? -result : result) : Double.NaN;
    }

    public static double parseFloat(Object self, Object string) {
        int end;
        String str = JSType.trimLeft(JSType.toString(string));
        int length = str.length();
        if (length == 0) {
            return Double.NaN;
        }
        int start = 0;
        boolean negative = false;
        char ch = str.charAt(0);
        if (ch == '-') {
            ++start;
            negative = true;
        } else if (ch == '+') {
            ++start;
        } else if (ch == 'N' && str.startsWith("NaN")) {
            return Double.NaN;
        }
        if (start == length) {
            return Double.NaN;
        }
        ch = str.charAt(start);
        if (ch == 'I' && str.substring(start).startsWith("Infinity")) {
            return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        boolean dotSeen = false;
        boolean exponentOk = false;
        int exponentOffset = -1;
        block8: for (end = start; end < length; ++end) {
            ch = str.charAt(end);
            switch (ch) {
                case '.': {
                    if (exponentOffset != -1 || dotSeen) break block8;
                    dotSeen = true;
                    continue block8;
                }
                case 'E': 
                case 'e': {
                    if (exponentOffset != -1) break block8;
                    exponentOffset = end;
                    continue block8;
                }
                case '+': 
                case '-': {
                    if (exponentOffset == end - 1) continue block8;
                    break block8;
                }
                case '0': 
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    if (exponentOffset == -1) continue block8;
                    exponentOk = true;
                    continue block8;
                }
            }
        }
        if (exponentOffset != -1 && !exponentOk) {
            end = exponentOffset;
        }
        if (start == end) {
            return Double.NaN;
        }
        try {
            double result = Double.valueOf(str.substring(start, end));
            return negative ? -result : result;
        }
        catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static boolean isNaN(Object self, Object number) {
        return Double.isNaN(JSType.toNumber(number));
    }

    public static boolean isFinite(Object self, Object number) {
        double value = JSType.toNumber(number);
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }

    public static Object encodeURI(Object self, Object uri) {
        return URIUtils.encodeURI(self, JSType.toString(uri));
    }

    public static Object encodeURIComponent(Object self, Object uri) {
        return URIUtils.encodeURIComponent(self, JSType.toString(uri));
    }

    public static Object decodeURI(Object self, Object uri) {
        return URIUtils.decodeURI(self, JSType.toString(uri));
    }

    public static Object decodeURIComponent(Object self, Object uri) {
        return URIUtils.decodeURIComponent(self, JSType.toString(uri));
    }

    public static String escape(Object self, Object string) {
        String str = JSType.toString(string);
        int length = str.length();
        if (length == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < length; ++k) {
            char ch = str.charAt(k);
            if (UNESCAPED.indexOf(ch) != -1) {
                sb.append(ch);
                continue;
            }
            if (ch < '\u0100') {
                sb.append('%');
                if (ch < '\u0010') {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
                continue;
            }
            sb.append("%u");
            if (ch < '\u1000') {
                sb.append('0');
            }
            sb.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
        }
        return sb.toString();
    }

    public static String unescape(Object self, Object string) {
        String str = JSType.toString(string);
        int length = str.length();
        if (length == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < length; ++k) {
            char ch = str.charAt(k);
            if (ch != '%') {
                sb.append(ch);
                continue;
            }
            if (k < length - 5 && str.charAt(k + 1) == 'u') {
                try {
                    ch = (char)Integer.parseInt(str.substring(k + 2, k + 6), 16);
                    sb.append(ch);
                    k += 5;
                    continue;
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            if (k < length - 2) {
                try {
                    ch = (char)Integer.parseInt(str.substring(k + 1, k + 3), 16);
                    sb.append(ch);
                    k += 2;
                    continue;
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public static Object anonymous(Object self) {
        return ScriptRuntime.UNDEFINED;
    }

    private static int fastDigit(int ch, int radix) {
        int n = -1;
        if (ch >= 48 && ch <= 57) {
            n = ch - 48;
        } else if (radix > 10) {
            if (ch >= 97 && ch <= 122) {
                n = ch - 97 + 10;
            } else if (ch >= 65 && ch <= 90) {
                n = ch - 65 + 10;
            }
        }
        return n < radix ? n : -1;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), GlobalFunctions.class, name, Lookup.MH.type(rtype, types));
    }
}

