// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt;

import java.io.FilterOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.DataInputStream;
import java.io.DataInput;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class NBTIO
{
    public static CompoundTag readFile(final String path) throws IOException {
        return readFile(new File(path));
    }
    
    public static CompoundTag readFile(final File file) throws IOException {
        return readFile(file, true, false);
    }
    
    public static CompoundTag readFile(final String path, final boolean compressed, final boolean littleEndian) throws IOException {
        return readFile(new File(path), compressed, littleEndian);
    }
    
    public static CompoundTag readFile(final File file, final boolean compressed, final boolean littleEndian) throws IOException {
        InputStream in = new FileInputStream(file);
        if (compressed) {
            in = new GZIPInputStream(in);
        }
        final Tag tag = readTag(in, littleEndian);
        if (!(tag instanceof CompoundTag)) {
            throw new IOException("Root tag is not a CompoundTag!");
        }
        return (CompoundTag)tag;
    }
    
    public static void writeFile(final CompoundTag tag, final String path) throws IOException {
        writeFile(tag, new File(path));
    }
    
    public static void writeFile(final CompoundTag tag, final File file) throws IOException {
        writeFile(tag, file, true, false);
    }
    
    public static void writeFile(final CompoundTag tag, final String path, final boolean compressed, final boolean littleEndian) throws IOException {
        writeFile(tag, new File(path), compressed, littleEndian);
    }
    
    public static void writeFile(final CompoundTag tag, final File file, final boolean compressed, final boolean littleEndian) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        if (compressed) {
            out = new GZIPOutputStream(out);
        }
        writeTag(out, tag, littleEndian);
        out.close();
    }
    
    public static CompoundTag readTag(final InputStream in) throws IOException {
        return readTag(in, false);
    }
    
    public static CompoundTag readTag(final InputStream in, final boolean littleEndian) throws IOException {
        return readTag(littleEndian ? new LittleEndianDataInputStream(in) : new DataInputStream(in));
    }
    
    public static CompoundTag readTag(final DataInput in) throws IOException {
        final int id = in.readByte();
        if (id != 10) {
            throw new IOException(String.format("Expected root tag to be a CompoundTag, was %s", id));
        }
        in.skipBytes(in.readUnsignedShort());
        final CompoundTag tag = new CompoundTag();
        tag.read(in);
        return tag;
    }
    
    public static void writeTag(final OutputStream out, final CompoundTag tag) throws IOException {
        writeTag(out, tag, false);
    }
    
    public static void writeTag(final OutputStream out, final CompoundTag tag, final boolean littleEndian) throws IOException {
        writeTag(littleEndian ? new LittleEndianDataOutputStream(out) : new DataOutputStream(out), tag);
    }
    
    public static void writeTag(final DataOutput out, final CompoundTag tag) throws IOException {
        out.writeByte(10);
        out.writeUTF("");
        tag.write(out);
    }
    
    private static final class LittleEndianDataInputStream extends FilterInputStream implements DataInput
    {
        private LittleEndianDataInputStream(final InputStream in) {
            super(in);
        }
        
        @Override
        public int read(final byte[] b) throws IOException {
            return this.in.read(b, 0, b.length);
        }
        
        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return this.in.read(b, off, len);
        }
        
        @Override
        public void readFully(final byte[] b) throws IOException {
            this.readFully(b, 0, b.length);
        }
        
        @Override
        public void readFully(final byte[] b, final int off, final int len) throws IOException {
            if (len < 0) {
                throw new IndexOutOfBoundsException();
            }
            int read;
            for (int pos = 0; pos < len; pos += read) {
                read = this.in.read(b, off + pos, len - pos);
                if (read < 0) {
                    throw new EOFException();
                }
            }
        }
        
        @Override
        public int skipBytes(final int n) throws IOException {
            int total = 0;
            for (int skipped = 0; total < n && (skipped = (int)this.in.skip(n - total)) > 0; total += skipped) {}
            return total;
        }
        
        @Override
        public boolean readBoolean() throws IOException {
            final int val = this.in.read();
            if (val < 0) {
                throw new EOFException();
            }
            return val != 0;
        }
        
        @Override
        public byte readByte() throws IOException {
            final int val = this.in.read();
            if (val < 0) {
                throw new EOFException();
            }
            return (byte)val;
        }
        
        @Override
        public int readUnsignedByte() throws IOException {
            final int val = this.in.read();
            if (val < 0) {
                throw new EOFException();
            }
            return val;
        }
        
        @Override
        public short readShort() throws IOException {
            final int b1 = this.in.read();
            final int b2 = this.in.read();
            if ((b1 | b2) < 0) {
                throw new EOFException();
            }
            return (short)(b1 | b2 << 8);
        }
        
        @Override
        public int readUnsignedShort() throws IOException {
            final int b1 = this.in.read();
            final int b2 = this.in.read();
            if ((b1 | b2) < 0) {
                throw new EOFException();
            }
            return b1 | b2 << 8;
        }
        
        @Override
        public char readChar() throws IOException {
            final int b1 = this.in.read();
            final int b2 = this.in.read();
            if ((b1 | b2) < 0) {
                throw new EOFException();
            }
            return (char)(b1 | b2 << 8);
        }
        
        @Override
        public int readInt() throws IOException {
            final int b1 = this.in.read();
            final int b2 = this.in.read();
            final int b3 = this.in.read();
            final int b4 = this.in.read();
            if ((b1 | b2 | b3 | b4) < 0) {
                throw new EOFException();
            }
            return b1 | b2 << 8 | b3 << 16 | b4 << 24;
        }
        
        @Override
        public long readLong() throws IOException {
            final long b1 = this.in.read();
            final long b2 = this.in.read();
            final long b3 = this.in.read();
            final long b4 = this.in.read();
            final long b5 = this.in.read();
            final long b6 = this.in.read();
            final long b7 = this.in.read();
            final long b8 = this.in.read();
            if ((b1 | b2 | b3 | b4 | b5 | b6 | b7 | b8) < 0L) {
                throw new EOFException();
            }
            return b1 | b2 << 8 | b3 << 16 | b4 << 24 | b5 << 32 | b6 << 40 | b7 << 48 | b8 << 56;
        }
        
        @Override
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(this.readInt());
        }
        
        @Override
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(this.readLong());
        }
        
        @Override
        public String readLine() throws IOException {
            throw new UnsupportedOperationException("Use readUTF.");
        }
        
        @Override
        public String readUTF() throws IOException {
            final byte[] bytes = new byte[this.readUnsignedShort()];
            this.readFully(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
    
    private static final class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput
    {
        private LittleEndianDataOutputStream(final OutputStream out) {
            super(out);
        }
        
        @Override
        public synchronized void write(final int b) throws IOException {
            this.out.write(b);
        }
        
        @Override
        public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
            this.out.write(b, off, len);
        }
        
        @Override
        public void flush() throws IOException {
            this.out.flush();
        }
        
        @Override
        public void writeBoolean(final boolean b) throws IOException {
            this.out.write(b ? 1 : 0);
        }
        
        @Override
        public void writeByte(final int b) throws IOException {
            this.out.write(b);
        }
        
        @Override
        public void writeShort(final int s) throws IOException {
            this.out.write(s & 0xFF);
            this.out.write(s >>> 8 & 0xFF);
        }
        
        @Override
        public void writeChar(final int c) throws IOException {
            this.out.write(c & 0xFF);
            this.out.write(c >>> 8 & 0xFF);
        }
        
        @Override
        public void writeInt(final int i) throws IOException {
            this.out.write(i & 0xFF);
            this.out.write(i >>> 8 & 0xFF);
            this.out.write(i >>> 16 & 0xFF);
            this.out.write(i >>> 24 & 0xFF);
        }
        
        @Override
        public void writeLong(final long l) throws IOException {
            this.out.write((int)(l & 0xFFL));
            this.out.write((int)(l >>> 8 & 0xFFL));
            this.out.write((int)(l >>> 16 & 0xFFL));
            this.out.write((int)(l >>> 24 & 0xFFL));
            this.out.write((int)(l >>> 32 & 0xFFL));
            this.out.write((int)(l >>> 40 & 0xFFL));
            this.out.write((int)(l >>> 48 & 0xFFL));
            this.out.write((int)(l >>> 56 & 0xFFL));
        }
        
        @Override
        public void writeFloat(final float f) throws IOException {
            this.writeInt(Float.floatToIntBits(f));
        }
        
        @Override
        public void writeDouble(final double d) throws IOException {
            this.writeLong(Double.doubleToLongBits(d));
        }
        
        @Override
        public void writeBytes(final String s) throws IOException {
            for (int len = s.length(), index = 0; index < len; ++index) {
                this.out.write((byte)s.charAt(index));
            }
        }
        
        @Override
        public void writeChars(final String s) throws IOException {
            for (int len = s.length(), index = 0; index < len; ++index) {
                final char c = s.charAt(index);
                this.out.write(c & '\u00ff');
                this.out.write(c >>> 8 & 0xFF);
            }
        }
        
        @Override
        public void writeUTF(final String s) throws IOException {
            final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            this.writeShort(bytes.length);
            this.write(bytes);
        }
    }
}
