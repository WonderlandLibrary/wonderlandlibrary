/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client.utils;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.utils.Idn;

@Immutable
public class Rfc3492Idn
implements Idn {
    private static final int base = 36;
    private static final int tmin = 1;
    private static final int tmax = 26;
    private static final int skew = 38;
    private static final int damp = 700;
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final char delimiter = '-';
    private static final String ACE_PREFIX = "xn--";

    private int adapt(int delta, int numpoints, boolean firsttime) {
        int d = delta;
        d = firsttime ? (d /= 700) : (d /= 2);
        d += d / numpoints;
        int k = 0;
        while (d > 455) {
            d /= 35;
            k += 36;
        }
        return k + 36 * d / (d + 38);
    }

    private int digit(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        }
        if (c >= 'a' && c <= 'z') {
            return c - 97;
        }
        if (c >= '0' && c <= '9') {
            return c - 48 + 26;
        }
        throw new IllegalArgumentException("illegal digit: " + c);
    }

    public String toUnicode(String punycode) {
        StringBuilder unicode = new StringBuilder(punycode.length());
        StringTokenizer tok = new StringTokenizer(punycode, ".");
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken();
            if (unicode.length() > 0) {
                unicode.append('.');
            }
            if (t.startsWith(ACE_PREFIX)) {
                t = this.decode(t.substring(4));
            }
            unicode.append(t);
        }
        return unicode.toString();
    }

    protected String decode(String s) {
        String input = s;
        int n = 128;
        int i = 0;
        int bias = 72;
        StringBuilder output = new StringBuilder(input.length());
        int lastdelim = input.lastIndexOf(45);
        if (lastdelim != -1) {
            output.append(input.subSequence(0, lastdelim));
            input = input.substring(lastdelim + 1);
        }
        while (input.length() > 0) {
            int oldi = i;
            int w = 1;
            int k = 36;
            while (input.length() != 0) {
                char c = input.charAt(0);
                input = input.substring(1);
                int digit = this.digit(c);
                i += digit * w;
                int t = k <= bias + 1 ? 1 : (k >= bias + 26 ? 26 : k - bias);
                if (digit < t) break;
                w *= 36 - t;
                k += 36;
            }
            bias = this.adapt(i - oldi, output.length() + 1, oldi == 0);
            n += i / (output.length() + 1);
            output.insert(i %= output.length() + 1, (char)n);
            ++i;
        }
        return output.toString();
    }
}

