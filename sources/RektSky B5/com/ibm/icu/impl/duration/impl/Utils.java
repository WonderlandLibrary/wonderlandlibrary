/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration.impl;

import java.util.Locale;

public class Utils {
    public static final Locale localeFromString(String s2) {
        String language = s2;
        String region = "";
        String variant = "";
        int x2 = language.indexOf("_");
        if (x2 != -1) {
            region = language.substring(x2 + 1);
            language = language.substring(0, x2);
        }
        if ((x2 = region.indexOf("_")) != -1) {
            variant = region.substring(x2 + 1);
            region = region.substring(0, x2);
        }
        return new Locale(language, region, variant);
    }

    public static String chineseNumber(long n2, ChineseDigits zh) {
        if (n2 < 0L) {
            n2 = -n2;
        }
        if (n2 <= 10L) {
            if (n2 == 2L) {
                return String.valueOf(zh.liang);
            }
            return String.valueOf(zh.digits[(int)n2]);
        }
        char[] buf = new char[40];
        char[] digits = String.valueOf(n2).toCharArray();
        boolean inZero = true;
        boolean forcedZero = false;
        int x2 = buf.length;
        int i2 = digits.length;
        int u2 = -1;
        int l2 = -1;
        while (--i2 >= 0) {
            if (u2 == -1) {
                if (l2 != -1) {
                    buf[--x2] = zh.levels[l2];
                    inZero = true;
                    forcedZero = false;
                }
                ++u2;
            } else {
                buf[--x2] = zh.units[u2++];
                if (u2 == 3) {
                    u2 = -1;
                    ++l2;
                }
            }
            int d2 = digits[i2] - 48;
            if (d2 == 0) {
                if (x2 < buf.length - 1 && u2 != 0) {
                    buf[x2] = 42;
                }
                if (inZero || forcedZero) {
                    buf[--x2] = 42;
                    continue;
                }
                buf[--x2] = zh.digits[0];
                inZero = true;
                forcedZero = u2 == 1;
                continue;
            }
            inZero = false;
            buf[--x2] = zh.digits[d2];
        }
        if (n2 > 1000000L) {
            boolean last = true;
            int i3 = buf.length - 3;
            while (buf[i3] != '0') {
                boolean bl = last = !last;
                if ((i3 -= 8) > x2) continue;
            }
            i3 = buf.length - 7;
            do {
                if (buf[i3] == zh.digits[0] && !last) {
                    buf[i3] = 42;
                }
                boolean bl = last = !last;
            } while ((i3 -= 8) > x2);
            if (n2 >= 100000000L) {
                i3 = buf.length - 8;
                do {
                    boolean empty = true;
                    int e2 = Math.max(x2 - 1, i3 - 8);
                    for (int j2 = i3 - 1; j2 > e2; --j2) {
                        if (buf[j2] == '*') continue;
                        empty = false;
                        break;
                    }
                    if (!empty) continue;
                    buf[i3] = buf[i3 + 1] != '*' && buf[i3 + 1] != zh.digits[0] ? zh.digits[0] : 42;
                } while ((i3 -= 8) > x2);
            }
        }
        for (i2 = x2; i2 < buf.length; ++i2) {
            if (buf[i2] != zh.digits[2] || i2 < buf.length - 1 && buf[i2 + 1] == zh.units[0] || i2 > x2 && (buf[i2 - 1] == zh.units[0] || buf[i2 - 1] == zh.digits[0] || buf[i2 - 1] == '*')) continue;
            buf[i2] = zh.liang;
        }
        if (buf[x2] == zh.digits[1] && (zh.ko || buf[x2 + 1] == zh.units[0])) {
            ++x2;
        }
        int w2 = x2;
        for (int r2 = x2; r2 < buf.length; ++r2) {
            if (buf[r2] == '*') continue;
            buf[w2++] = buf[r2];
        }
        return new String(buf, x2, w2 - x2);
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

