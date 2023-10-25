/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.Arrays;

public class GrowableDataBuffer {
    static final int VAL_GROW_QUANTUM = 1024;
    static final int MAX_VAL_GROW = 0x100000;
    static final int MIN_OBJ_GROW = 32;
    static WeakLink buflist = new WeakLink();
    byte[] vals;
    int writevalpos;
    int readvalpos;
    int savevalpos;
    Object[] objs;
    int writeobjpos;
    int readobjpos;
    int saveobjpos;

    public static GrowableDataBuffer getBuffer(int n) {
        return GrowableDataBuffer.getBuffer(n, 32);
    }

    public static synchronized GrowableDataBuffer getBuffer(int n, int n2) {
        WeakLink weakLink = buflist;
        WeakLink weakLink2 = GrowableDataBuffer.buflist.next;
        while (weakLink2 != null) {
            GrowableDataBuffer growableDataBuffer = (GrowableDataBuffer)weakLink2.bufref.get();
            WeakLink weakLink3 = weakLink2.next;
            if (growableDataBuffer == null) {
                weakLink.next = weakLink2 = weakLink3;
                continue;
            }
            if (growableDataBuffer.valueCapacity() >= n && growableDataBuffer.objectCapacity() >= n2) {
                weakLink.next = weakLink3;
                return growableDataBuffer;
            }
            weakLink = weakLink2;
            weakLink2 = weakLink3;
        }
        return new GrowableDataBuffer(n, n2);
    }

    public static synchronized void returnBuffer(GrowableDataBuffer growableDataBuffer) {
        Object object;
        int n = growableDataBuffer.valueCapacity();
        int n2 = growableDataBuffer.objectCapacity();
        growableDataBuffer.reset();
        WeakLink weakLink = buflist;
        WeakLink weakLink2 = GrowableDataBuffer.buflist.next;
        while (weakLink2 != null) {
            object = (GrowableDataBuffer)weakLink2.bufref.get();
            WeakLink weakLink3 = weakLink2.next;
            if (object == null) {
                weakLink.next = weakLink2 = weakLink3;
                continue;
            }
            int n3 = ((GrowableDataBuffer)object).valueCapacity();
            int n4 = ((GrowableDataBuffer)object).objectCapacity();
            if (n3 > n || n3 == n && n4 >= n2) break;
            weakLink = weakLink2;
            weakLink2 = weakLink3;
        }
        object = new WeakLink();
        ((WeakLink)object).bufref = new WeakReference<GrowableDataBuffer>(growableDataBuffer);
        weakLink.next = object;
        ((WeakLink)object).next = weakLink2;
    }

    private GrowableDataBuffer(int n, int n2) {
        this.vals = new byte[n];
        this.objs = new Object[n2];
    }

    public int readValuePosition() {
        return this.readvalpos;
    }

    public int writeValuePosition() {
        return this.writevalpos;
    }

    public int readObjectPosition() {
        return this.readobjpos;
    }

    public int writeObjectPosition() {
        return this.writeobjpos;
    }

    public int valueCapacity() {
        return this.vals.length;
    }

    public int objectCapacity() {
        return this.objs.length;
    }

    public void save() {
        this.savevalpos = this.readvalpos;
        this.saveobjpos = this.readobjpos;
    }

    public void restore() {
        this.readvalpos = this.savevalpos;
        this.readobjpos = this.saveobjpos;
    }

    public boolean hasValues() {
        return this.readvalpos < this.writevalpos;
    }

    public boolean hasObjects() {
        return this.readobjpos < this.writeobjpos;
    }

    public boolean isEmpty() {
        return this.writevalpos == 0;
    }

    public void reset() {
        this.writevalpos = 0;
        this.savevalpos = 0;
        this.readvalpos = 0;
        this.saveobjpos = 0;
        this.readobjpos = 0;
        if (this.writeobjpos > 0) {
            Arrays.fill(this.objs, 0, this.writeobjpos, null);
            this.writeobjpos = 0;
        }
    }

    public void append(GrowableDataBuffer growableDataBuffer) {
        this.ensureWriteCapacity(growableDataBuffer.writevalpos);
        System.arraycopy(growableDataBuffer.vals, 0, this.vals, this.writevalpos, growableDataBuffer.writevalpos);
        this.writevalpos += growableDataBuffer.writevalpos;
        if (this.writeobjpos + growableDataBuffer.writeobjpos > this.objs.length) {
            this.objs = Arrays.copyOf(this.objs, this.writeobjpos + growableDataBuffer.writeobjpos);
        }
        System.arraycopy(growableDataBuffer.objs, 0, this.objs, this.writeobjpos, growableDataBuffer.writeobjpos);
        this.writeobjpos += growableDataBuffer.writeobjpos;
    }

    private void ensureWriteCapacity(int n) {
        if (n > this.vals.length - this.writevalpos) {
            n = this.writevalpos + n - this.vals.length;
            int n2 = Math.min(this.vals.length, 0x100000);
            if (n2 < n) {
                n2 = n;
            }
            int n3 = this.vals.length + n2;
            n3 = n3 + 1023 & 0xFFFFFC00;
            this.vals = Arrays.copyOf(this.vals, n3);
        }
    }

    private void ensureReadCapacity(int n) {
        if (this.readvalpos + n > this.writevalpos) {
            throw new BufferOverflowException();
        }
    }

    public void putBoolean(boolean bl) {
        this.putByte(bl ? (byte)1 : 0);
    }

    public void putByte(byte by) {
        this.ensureWriteCapacity(1);
        this.vals[this.writevalpos++] = by;
    }

    public void putChar(char c) {
        this.ensureWriteCapacity(2);
        this.vals[this.writevalpos++] = (byte)(c >> 8);
        this.vals[this.writevalpos++] = (byte)c;
    }

    public void putShort(short s) {
        this.ensureWriteCapacity(2);
        this.vals[this.writevalpos++] = (byte)(s >> 8);
        this.vals[this.writevalpos++] = (byte)s;
    }

    public void putInt(int n) {
        this.ensureWriteCapacity(4);
        this.vals[this.writevalpos++] = (byte)(n >> 24);
        this.vals[this.writevalpos++] = (byte)(n >> 16);
        this.vals[this.writevalpos++] = (byte)(n >> 8);
        this.vals[this.writevalpos++] = (byte)n;
    }

    public void putLong(long l) {
        this.ensureWriteCapacity(8);
        this.vals[this.writevalpos++] = (byte)(l >> 56);
        this.vals[this.writevalpos++] = (byte)(l >> 48);
        this.vals[this.writevalpos++] = (byte)(l >> 40);
        this.vals[this.writevalpos++] = (byte)(l >> 32);
        this.vals[this.writevalpos++] = (byte)(l >> 24);
        this.vals[this.writevalpos++] = (byte)(l >> 16);
        this.vals[this.writevalpos++] = (byte)(l >> 8);
        this.vals[this.writevalpos++] = (byte)l;
    }

    public void putFloat(float f) {
        this.putInt(Float.floatToIntBits(f));
    }

    public void putDouble(double d) {
        this.putLong(Double.doubleToLongBits(d));
    }

    public void putObject(Object object) {
        if (this.writeobjpos >= this.objs.length) {
            this.objs = Arrays.copyOf(this.objs, this.writeobjpos + 32);
        }
        this.objs[this.writeobjpos++] = object;
    }

    public byte peekByte(int n) {
        if (n >= this.writevalpos) {
            throw new BufferOverflowException();
        }
        return this.vals[n];
    }

    public Object peekObject(int n) {
        if (n >= this.writeobjpos) {
            throw new BufferOverflowException();
        }
        return this.objs[n];
    }

    public boolean getBoolean() {
        this.ensureReadCapacity(1);
        return this.vals[this.readvalpos++] != 0;
    }

    public byte getByte() {
        this.ensureReadCapacity(1);
        return this.vals[this.readvalpos++];
    }

    public int getUByte() {
        this.ensureReadCapacity(1);
        return this.vals[this.readvalpos++] & 0xFF;
    }

    public char getChar() {
        this.ensureReadCapacity(2);
        int n = this.vals[this.readvalpos++];
        n = n << 8 | this.vals[this.readvalpos++] & 0xFF;
        return (char)n;
    }

    public short getShort() {
        this.ensureReadCapacity(2);
        int n = this.vals[this.readvalpos++];
        n = n << 8 | this.vals[this.readvalpos++] & 0xFF;
        return (short)n;
    }

    public int getInt() {
        this.ensureReadCapacity(4);
        int n = this.vals[this.readvalpos++];
        n = n << 8 | this.vals[this.readvalpos++] & 0xFF;
        n = n << 8 | this.vals[this.readvalpos++] & 0xFF;
        n = n << 8 | this.vals[this.readvalpos++] & 0xFF;
        return n;
    }

    public long getLong() {
        this.ensureReadCapacity(8);
        long l = this.vals[this.readvalpos++];
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        l = l << 8 | (long)(this.vals[this.readvalpos++] & 0xFF);
        return l;
    }

    public float getFloat() {
        return Float.intBitsToFloat(this.getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(this.getLong());
    }

    public Object getObject() {
        if (this.readobjpos >= this.objs.length) {
            throw new BufferOverflowException();
        }
        return this.objs[this.readobjpos++];
    }

    static class WeakLink {
        WeakReference<GrowableDataBuffer> bufref;
        WeakLink next;

        WeakLink() {
        }
    }
}

