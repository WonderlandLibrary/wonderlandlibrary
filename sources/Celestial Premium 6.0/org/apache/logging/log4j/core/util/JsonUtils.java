/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

public final class JsonUtils {
    private static final char[] HC = "0123456789ABCDEF".toCharArray();
    private static final int[] ESC_CODES;
    private static final ThreadLocal<char[]> _qbufLocal;

    private static char[] getQBuf() {
        char[] _qbuf = _qbufLocal.get();
        if (_qbuf == null) {
            _qbuf = new char[6];
            _qbuf[0] = 92;
            _qbuf[2] = 48;
            _qbuf[3] = 48;
            _qbufLocal.set(_qbuf);
        }
        return _qbuf;
    }

    public static void quoteAsString(CharSequence input, StringBuilder output) {
        char[] qbuf = JsonUtils.getQBuf();
        int escCodeCount = ESC_CODES.length;
        int inPtr = 0;
        int inputLen = input.length();
        block0: while (inPtr < inputLen) {
            char c;
            while ((c = input.charAt(inPtr)) >= escCodeCount || ESC_CODES[c] == 0) {
                output.append(c);
                if (++inPtr < inputLen) continue;
                break block0;
            }
            char d = input.charAt(inPtr++);
            int escCode = ESC_CODES[d];
            int length = escCode < 0 ? JsonUtils._appendNumeric(d, qbuf) : JsonUtils._appendNamed(escCode, qbuf);
            output.append(qbuf, 0, length);
        }
    }

    private static int _appendNumeric(int value, char[] qbuf) {
        qbuf[1] = 117;
        qbuf[4] = HC[value >> 4];
        qbuf[5] = HC[value & 0xF];
        return 6;
    }

    private static int _appendNamed(int esc, char[] qbuf) {
        qbuf[1] = (char)esc;
        return 2;
    }

    static {
        int[] table = new int[128];
        for (int i = 0; i < 32; ++i) {
            table[i] = -1;
        }
        table[34] = 34;
        table[92] = 92;
        table[8] = 98;
        table[9] = 116;
        table[12] = 102;
        table[10] = 110;
        table[13] = 114;
        ESC_CODES = table;
        _qbufLocal = new ThreadLocal();
    }
}

