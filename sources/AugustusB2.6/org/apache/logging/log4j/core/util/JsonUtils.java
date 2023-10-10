// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

public final class JsonUtils
{
    private static final char[] HC;
    private static final int[] ESC_CODES;
    private static final ThreadLocal<char[]> _qbufLocal;
    
    private static char[] getQBuf() {
        char[] _qbuf = JsonUtils._qbufLocal.get();
        if (_qbuf == null) {
            _qbuf = new char[] { '\\', '\0', '0', '0', '\0', '\0' };
            JsonUtils._qbufLocal.set(_qbuf);
        }
        return _qbuf;
    }
    
    public static void quoteAsString(final CharSequence input, final StringBuilder output) {
        final char[] qbuf = getQBuf();
        final int escCodeCount = JsonUtils.ESC_CODES.length;
        int inPtr = 0;
        final int inputLen = input.length();
    Label_0133:
        while (inPtr < inputLen) {
            while (true) {
                final char c = input.charAt(inPtr);
                if (c < escCodeCount && JsonUtils.ESC_CODES[c] != 0) {
                    final char d = input.charAt(inPtr++);
                    final int escCode = JsonUtils.ESC_CODES[d];
                    final int length = (escCode < 0) ? _appendNumeric(d, qbuf) : _appendNamed(escCode, qbuf);
                    output.append(qbuf, 0, length);
                    break;
                }
                output.append(c);
                if (++inPtr >= inputLen) {
                    break Label_0133;
                }
            }
        }
    }
    
    private static int _appendNumeric(final int value, final char[] qbuf) {
        qbuf[1] = 'u';
        qbuf[4] = JsonUtils.HC[value >> 4];
        qbuf[5] = JsonUtils.HC[value & 0xF];
        return 6;
    }
    
    private static int _appendNamed(final int esc, final char[] qbuf) {
        qbuf[1] = (char)esc;
        return 2;
    }
    
    static {
        HC = "0123456789ABCDEF".toCharArray();
        final int[] table = new int[128];
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
        _qbufLocal = new ThreadLocal<char[]>();
    }
}
