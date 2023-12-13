/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    private static final char[] _hexcodes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int[] _shifts = new int[]{28, 24, 20, 16, 12, 8, 4, 0};

    public static void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (index < 0 || index >= data.length) {
            throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
        }
        if (stream == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
        }
        long display_offset = offset + (long)index;
        StringBuilder buffer = new StringBuilder(74);
        for (int j2 = index; j2 < data.length; j2 += 16) {
            int k2;
            int chars_read = data.length - j2;
            if (chars_read > 16) {
                chars_read = 16;
            }
            HexDump.dump(buffer, display_offset).append(' ');
            for (k2 = 0; k2 < 16; ++k2) {
                if (k2 < chars_read) {
                    HexDump.dump(buffer, data[k2 + j2]);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (k2 = 0; k2 < chars_read; ++k2) {
                if (data[k2 + j2] >= 32 && data[k2 + j2] < 127) {
                    buffer.append((char)data[k2 + j2]);
                    continue;
                }
                buffer.append('.');
            }
            buffer.append(EOL);
            stream.write(buffer.toString().getBytes(Charset.defaultCharset()));
            stream.flush();
            buffer.setLength(0);
            display_offset += (long)chars_read;
        }
    }

    private static StringBuilder dump(StringBuilder _lbuffer, long value) {
        for (int j2 = 0; j2 < 8; ++j2) {
            _lbuffer.append(_hexcodes[(int)(value >> _shifts[j2]) & 0xF]);
        }
        return _lbuffer;
    }

    private static StringBuilder dump(StringBuilder _cbuffer, byte value) {
        for (int j2 = 0; j2 < 2; ++j2) {
            _cbuffer.append(_hexcodes[value >> _shifts[j2 + 6] & 0xF]);
        }
        return _cbuffer;
    }
}

