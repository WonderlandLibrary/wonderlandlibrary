/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration.impl;

import java.util.Locale;

public class Utils {
    public static final Locale localeFromString(String s) {
        String language = s;
        String region = "";
        String variant = "";
        int x = language.indexOf("_");
        if (x != -1) {
            region = language.substring(x + 1);
            language = language.substring(0, x);
        }
        if ((x = region.indexOf("_")) != -1) {
            variant = region.substring(x + 1);
            region = region.substring(0, x);
        }
        return new Locale(language, region, variant);
    }

    public static String chineseNumber(long n, ChineseDigits zh) {
        if (n < 0L) {
            n = -n;
        }
        if (n <= 10L) {
            if (n == 2L) {
                return String.valueOf(zh.liang);
            }
            return String.valueOf(zh.digits[(int)n]);
        }
        char[] buf = new char[40];
        char[] digits = String.valueOf(n).toCharArray();
        boolean inZero = true;
        boolean forcedZero = false;
        int x = buf.length;
        int i = digits.length;
        int u = -1;
        int l = -1;
        while (--i >= 0) {
            if (u == -1) {
                if (l != -1) {
                    buf[--x] = zh.levels[l];
                    inZero = true;
                    forcedZero = false;
                }
                ++u;
            } else {
                buf[--x] = zh.units[u++];
                if (u == 3) {
                    u = -1;
                    ++l;
                }
            }
            int d = digits[i] - 48;
            if (d == 0) {
                if (x < buf.length - 1 && u != 0) {
                    buf[x] = 42;
                }
                if (inZero || forcedZero) {
                    buf[--x] = 42;
                    continue;
                }
                buf[--x] = zh.digits[0];
                inZero = true;
                forcedZero = u == 1;
                continue;
            }
            inZero = false;
            buf[--x] = zh.digits[d];
        }
        if (n > 1000000L) {
            boolean last = true;
            int i2 = buf.length - 3;
            while (buf[i2] != '0') {
                boolean bl = last = !last;
                if ((i2 -= 8) > x) continue;
            }
            i2 = buf.length - 7;
            do {
                if (buf[i2] == zh.digits[0] && !last) {
                    buf[i2] = 42;
                }
                boolean bl = last = !last;
            } while ((i2 -= 8) > x);
            if (n >= 100000000L) {
                i2 = buf.length - 8;
                do {
                    boolean empty = true;
                    int e = Math.max(x - 1, i2 - 8);
                    for (int j = i2 - 1; j > e; --j) {
                        if (buf[j] == '*') continue;
                        empty = false;
                        break;
                    }
                    if (!empty) continue;
                    buf[i2] = buf[i2 + 1] != '*' && buf[i2 + 1] != zh.digits[0] ? zh.digits[0] : 42;
                } while ((i2 -= 8) > x);
            }
        }
        for (i = x; i < buf.length; ++i) {
            if (buf[i] != zh.digits[2] || i < buf.length - 1 && buf[i + 1] == zh.units[0] || i > x && (buf[i - 1] == zh.units[0] || buf[i - 1] == zh.digits[0] || buf[i - 1] == '*')) continue;
            buf[i] = zh.liang;
        }
        if (buf[x] == zh.digits[1] && (zh.ko || buf[x + 1] == zh.units[0])) {
            ++x;
        }
        int w = x;
        for (int r = x; r < buf.length; ++r) {
            if (buf[r] == '*') continue;
            buf[w++] = buf[r];
        }
        return new String(buf, x, w - x);
    }

    public static class ChineseDigits {
        final char[] digits;
        final char[] units;
        final char[] levels;
        final char liang;
        final boolean ko;
        public static final ChineseDigits DEBUG = new ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
        public static final ChineseDigits TRADITIONAL = new ChineseDigits("\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341", "\u5341\u767e\u5343", "\u842c\u5104\u5146", '\u5169', false);
        public static final ChineseDigits SIMPLIFIED = new ChineseDigits("\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341", "\u5341\u767e\u5343", "\u4e07\u4ebf\u5146", '\u4e24', false);
        public static final ChineseDigits KOREAN = new ChineseDigits("\uc601\uc77c\uc774\uc0bc\uc0ac\uc624\uc721\uce60\ud314\uad6c\uc2ed", "\uc2ed\ubc31\ucc9c", "\ub9cc\uc5b5?", '\uc774', true);

        ChineseDigits(String digits, String units, String levels, char liang, boolean ko) {
            this.digits = digits.toCharArray();
            this.units = units.toCharArray();
            this.levels = levels.toCharArray();
            this.liang = liang;
            this.ko = ko;
        }
    }
}

