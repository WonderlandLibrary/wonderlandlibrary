/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Beta
public class LittleEndianDataOutputStream
extends FilterOutputStream
implements DataOutput {
    public LittleEndianDataOutputStream(OutputStream out) {
        super(new DataOutputStream(Preconditions.checkNotNull(out)));
    }

    @Override
    public void write(byte[] b2, int off, int len) throws IOException {
        this.out.write(b2, off, len);
    }

    @Override
    public void writeBoolean(boolean v2) throws IOException {
        ((DataOutputStream)this.out).writeBoolean(v2);
    }

    @Override
    public void writeByte(int v2) throws IOException {
        ((DataOutputStream)this.out).writeByte(v2);
    }

    @Override
    @Deprecated
    public void writeBytes(String s2) throws IOException {
        ((DataOutputStream)this.out).writeBytes(s2);
    }

    @Override
    public void writeChar(int v2) throws IOException {
        this.writeShort(v2);
    }

    @Override
    public void writeChars(String s2) throws IOException {
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            this.writeChar(s2.charAt(i2));
        }
    }

    @Override
    public void writeDouble(double v2) throws IOException {
        this.writeLong(Double.doubleToLongBits(v2));
    }

    @Override
    public void writeFloat(float v2) throws IOException {
        this.writeInt(Float.floatToIntBits(v2));
    }

    @Override
    public void writeInt(int v2) throws IOException {
        this.out.write(0xFF & v2);
        this.out.write(0xFF & v2 >> 8);
        this.out.write(0xFF & v2 >> 16);
        this.out.write(0xFF & v2 >> 24);
    }

    @Override
    public void writeLong(long v2) throws IOException {
        byte[] bytes = Longs.toByteArray(Long.reverseBytes(v2));
        this.write(bytes, 0, bytes.length);
    }

    @Override
    public void writeShort(int v2) throws IOException {
        this.out.write(0xFF & v2);
        this.out.write(0xFF & v2 >> 8);
    }

    @Override
    public void writeUTF(String str) throws IOException {
        ((DataOutputStream)this.out).writeUTF(str);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
    }
}

