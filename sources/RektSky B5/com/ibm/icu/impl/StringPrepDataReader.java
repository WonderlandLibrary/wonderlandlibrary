/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUDebug;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class StringPrepDataReader
implements ICUBinary.Authenticate {
    private static final boolean debug = ICUDebug.enabled("NormalizerDataReader");
    private ByteBuffer byteBuffer;
    private int unicodeVersion;
    private static final int DATA_FORMAT_ID = 1397772880;
    private static final byte[] DATA_FORMAT_VERSION = new byte[]{3, 2, 5, 2};

    public StringPrepDataReader(ByteBuffer bytes) throws IOException {
        if (debug) {
            System.out.println("Bytes in buffer " + bytes.remaining());
        }
        this.byteBuffer = bytes;
        this.unicodeVersion = ICUBinary.readHeader(this.byteBuffer, 1397772880, this);
        if (debug) {
            System.out.println("Bytes left in byteBuffer " + this.byteBuffer.remaining());
        }
    }

    public char[] read(int length) throws IOException {
        return ICUBinary.getChars(this.byteBuffer, length, 0);
    }

    @Override
    public boolean isDataVersionAcceptable(byte[] version) {
        return version[0] == DATA_FORMAT_VERSION[0] && version[2] == DATA_FORMAT_VERSION[2] && version[3] == DATA_FORMAT_VERSION[3];
    }

    public int[] readIndexes(int length) throws IOException {
        int[] indexes = new int[length];
        for (int i2 = 0; i2 < length; ++i2) {
            indexes[i2] = this.byteBuffer.getInt();
        }
        return indexes;
    }

    public byte[] getUnicodeVersion() {
        return ICUBinary.getVersionByteArrayFromCompactInt(this.unicodeVersion);
    }
}

