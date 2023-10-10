// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.lang.reflect.Array;
import java.nio.Buffer;

public class Pointer
{
    public static final Pointer NULL;
    protected long peer;
    
    public static final Pointer createConstant(final long peer) {
        return new Opaque(peer);
    }
    
    public static final Pointer createConstant(final int peer) {
        return new Opaque((long)peer & -1L);
    }
    
    Pointer() {
    }
    
    public Pointer(final long peer) {
        this.peer = peer;
    }
    
    public Pointer share(final long offset) {
        return this.share(offset, 0L);
    }
    
    public Pointer share(final long offset, final long sz) {
        if (offset == 0L) {
            return this;
        }
        return new Pointer(this.peer + offset);
    }
    
    public void clear(final long size) {
        this.setMemory(0L, size, (byte)0);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o != null && o instanceof Pointer && ((Pointer)o).peer == this.peer);
    }
    
    @Override
    public int hashCode() {
        return (int)((this.peer >>> 32) + (this.peer & -1L));
    }
    
    public long indexOf(final long offset, final byte value) {
        return Native.indexOf(this, this.peer, offset, value);
    }
    
    public void read(final long offset, final byte[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final short[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final char[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final int[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final long[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final float[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final double[] buf, final int index, final int length) {
        Native.read(this, this.peer, offset, buf, index, length);
    }
    
    public void read(final long offset, final Pointer[] buf, final int index, final int length) {
        for (int i = 0; i < length; ++i) {
            final Pointer p = this.getPointer(offset + i * Native.POINTER_SIZE);
            final Pointer oldp = buf[i + index];
            if (oldp == null || p == null || p.peer != oldp.peer) {
                buf[i + index] = p;
            }
        }
    }
    
    public void write(final long offset, final byte[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final short[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final char[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final int[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final long[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final float[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long offset, final double[] buf, final int index, final int length) {
        Native.write(this, this.peer, offset, buf, index, length);
    }
    
    public void write(final long bOff, final Pointer[] buf, final int index, final int length) {
        for (int i = 0; i < length; ++i) {
            this.setPointer(bOff + i * Native.POINTER_SIZE, buf[index + i]);
        }
    }
    
    Object getValue(final long offset, final Class<?> type, final Object currentValue) {
        Object result = null;
        if (Structure.class.isAssignableFrom(type)) {
            Structure s = (Structure)currentValue;
            if (Structure.ByReference.class.isAssignableFrom(type)) {
                s = Structure.updateStructureByReference(type, s, this.getPointer(offset));
            }
            else {
                s.useMemory(this, (int)offset, true);
                s.read();
            }
            result = s;
        }
        else if (type == Boolean.TYPE || type == Boolean.class) {
            result = Function.valueOf(this.getInt(offset) != 0);
        }
        else if (type == Byte.TYPE || type == Byte.class) {
            result = this.getByte(offset);
        }
        else if (type == Short.TYPE || type == Short.class) {
            result = this.getShort(offset);
        }
        else if (type == Character.TYPE || type == Character.class) {
            result = this.getChar(offset);
        }
        else if (type == Integer.TYPE || type == Integer.class) {
            result = this.getInt(offset);
        }
        else if (type == Long.TYPE || type == Long.class) {
            result = this.getLong(offset);
        }
        else if (type == Float.TYPE || type == Float.class) {
            result = this.getFloat(offset);
        }
        else if (type == Double.TYPE || type == Double.class) {
            result = this.getDouble(offset);
        }
        else if (Pointer.class.isAssignableFrom(type)) {
            final Pointer p = this.getPointer(offset);
            if (p != null) {
                final Pointer oldp = (currentValue instanceof Pointer) ? ((Pointer)currentValue) : null;
                if (oldp == null || p.peer != oldp.peer) {
                    result = p;
                }
                else {
                    result = oldp;
                }
            }
        }
        else if (type == String.class) {
            final Pointer p = this.getPointer(offset);
            result = ((p != null) ? p.getString(0L) : null);
        }
        else if (type == WString.class) {
            final Pointer p = this.getPointer(offset);
            result = ((p != null) ? new WString(p.getWideString(0L)) : null);
        }
        else if (Callback.class.isAssignableFrom(type)) {
            final Pointer fp = this.getPointer(offset);
            if (fp == null) {
                result = null;
            }
            else {
                Callback cb = (Callback)currentValue;
                final Pointer oldfp = CallbackReference.getFunctionPointer(cb);
                if (!fp.equals(oldfp)) {
                    cb = CallbackReference.getCallback(type, fp);
                }
                result = cb;
            }
        }
        else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
            final Pointer bp = this.getPointer(offset);
            if (bp == null) {
                result = null;
            }
            else {
                final Pointer oldbp = (currentValue == null) ? null : Native.getDirectBufferPointer((Buffer)currentValue);
                if (oldbp == null || !oldbp.equals(bp)) {
                    throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
                }
                result = currentValue;
            }
        }
        else if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMapped nm = (NativeMapped)currentValue;
            if (nm != null) {
                final Object value = this.getValue(offset, nm.nativeType(), null);
                result = nm.fromNative(value, new FromNativeContext(type));
                if (nm.equals(result)) {
                    result = nm;
                }
            }
            else {
                final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                final Object value2 = this.getValue(offset, tc.nativeType(), null);
                result = tc.fromNative(value2, new FromNativeContext(type));
            }
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Reading \"" + type + "\" from memory is not supported");
            }
            result = currentValue;
            if (result == null) {
                throw new IllegalStateException("Need an initialized array");
            }
            this.readArray(offset, result, type.getComponentType());
        }
        return result;
    }
    
    private void readArray(final long offset, final Object o, final Class<?> cls) {
        int length = 0;
        length = Array.getLength(o);
        final Object result = o;
        if (cls == Byte.TYPE) {
            this.read(offset, (byte[])result, 0, length);
        }
        else if (cls == Short.TYPE) {
            this.read(offset, (short[])result, 0, length);
        }
        else if (cls == Character.TYPE) {
            this.read(offset, (char[])result, 0, length);
        }
        else if (cls == Integer.TYPE) {
            this.read(offset, (int[])result, 0, length);
        }
        else if (cls == Long.TYPE) {
            this.read(offset, (long[])result, 0, length);
        }
        else if (cls == Float.TYPE) {
            this.read(offset, (float[])result, 0, length);
        }
        else if (cls == Double.TYPE) {
            this.read(offset, (double[])result, 0, length);
        }
        else if (Pointer.class.isAssignableFrom(cls)) {
            this.read(offset, (Pointer[])result, 0, length);
        }
        else if (Structure.class.isAssignableFrom(cls)) {
            final Structure[] sarray = (Structure[])result;
            if (Structure.ByReference.class.isAssignableFrom(cls)) {
                final Pointer[] parray = this.getPointerArray(offset, sarray.length);
                for (int i = 0; i < sarray.length; ++i) {
                    sarray[i] = Structure.updateStructureByReference(cls, sarray[i], parray[i]);
                }
            }
            else {
                Structure first = sarray[0];
                if (first == null) {
                    first = Structure.newInstance(cls, this.share(offset));
                    first.conditionalAutoRead();
                    sarray[0] = first;
                }
                else {
                    first.useMemory(this, (int)offset, true);
                    first.read();
                }
                final Structure[] tmp = first.toArray(sarray.length);
                for (int j = 1; j < sarray.length; ++j) {
                    if (sarray[j] == null) {
                        sarray[j] = tmp[j];
                    }
                    else {
                        sarray[j].useMemory(this, (int)(offset + j * sarray[j].size()), true);
                        sarray[j].read();
                    }
                }
            }
        }
        else {
            if (!NativeMapped.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Reading array of " + cls + " from memory not supported");
            }
            final NativeMapped[] array = (NativeMapped[])result;
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            final int size = Native.getNativeSize(result.getClass(), result) / array.length;
            for (int j = 0; j < array.length; ++j) {
                final Object value = this.getValue(offset + size * j, tc.nativeType(), array[j]);
                array[j] = (NativeMapped)tc.fromNative(value, new FromNativeContext(cls));
            }
        }
    }
    
    public byte getByte(final long offset) {
        return Native.getByte(this, this.peer, offset);
    }
    
    public char getChar(final long offset) {
        return Native.getChar(this, this.peer, offset);
    }
    
    public short getShort(final long offset) {
        return Native.getShort(this, this.peer, offset);
    }
    
    public int getInt(final long offset) {
        return Native.getInt(this, this.peer, offset);
    }
    
    public long getLong(final long offset) {
        return Native.getLong(this, this.peer, offset);
    }
    
    public NativeLong getNativeLong(final long offset) {
        return new NativeLong((NativeLong.SIZE == 8) ? this.getLong(offset) : this.getInt(offset));
    }
    
    public float getFloat(final long offset) {
        return Native.getFloat(this, this.peer, offset);
    }
    
    public double getDouble(final long offset) {
        return Native.getDouble(this, this.peer, offset);
    }
    
    public Pointer getPointer(final long offset) {
        return Native.getPointer(this.peer + offset);
    }
    
    public ByteBuffer getByteBuffer(final long offset, final long length) {
        return Native.getDirectByteBuffer(this, this.peer, offset, length).order(ByteOrder.nativeOrder());
    }
    
    public String getWideString(final long offset) {
        return Native.getWideString(this, this.peer, offset);
    }
    
    public String getString(final long offset) {
        return this.getString(offset, Native.getDefaultStringEncoding());
    }
    
    public String getString(final long offset, final String encoding) {
        return Native.getString(this, offset, encoding);
    }
    
    public byte[] getByteArray(final long offset, final int arraySize) {
        final byte[] buf = new byte[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public char[] getCharArray(final long offset, final int arraySize) {
        final char[] buf = new char[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public short[] getShortArray(final long offset, final int arraySize) {
        final short[] buf = new short[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public int[] getIntArray(final long offset, final int arraySize) {
        final int[] buf = new int[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public long[] getLongArray(final long offset, final int arraySize) {
        final long[] buf = new long[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public float[] getFloatArray(final long offset, final int arraySize) {
        final float[] buf = new float[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public double[] getDoubleArray(final long offset, final int arraySize) {
        final double[] buf = new double[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public Pointer[] getPointerArray(final long offset) {
        final List<Pointer> array = new ArrayList<Pointer>();
        int addOffset = 0;
        for (Pointer p = this.getPointer(offset); p != null; p = this.getPointer(offset + addOffset)) {
            array.add(p);
            addOffset += Native.POINTER_SIZE;
        }
        return array.toArray(new Pointer[0]);
    }
    
    public Pointer[] getPointerArray(final long offset, final int arraySize) {
        final Pointer[] buf = new Pointer[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public String[] getStringArray(final long offset) {
        return this.getStringArray(offset, -1, Native.getDefaultStringEncoding());
    }
    
    public String[] getStringArray(final long offset, final String encoding) {
        return this.getStringArray(offset, -1, encoding);
    }
    
    public String[] getStringArray(final long offset, final int length) {
        return this.getStringArray(offset, length, Native.getDefaultStringEncoding());
    }
    
    public String[] getWideStringArray(final long offset) {
        return this.getWideStringArray(offset, -1);
    }
    
    public String[] getWideStringArray(final long offset, final int length) {
        return this.getStringArray(offset, length, "--WIDE-STRING--");
    }
    
    public String[] getStringArray(final long offset, final int length, final String encoding) {
        final List<String> strings = new ArrayList<String>();
        int addOffset = 0;
        if (length != -1) {
            Pointer p = this.getPointer(offset + addOffset);
            int count = 0;
            while (count++ < length) {
                final String s = (p == null) ? null : ("--WIDE-STRING--".equals(encoding) ? p.getWideString(0L) : p.getString(0L, encoding));
                strings.add(s);
                if (count < length) {
                    addOffset += Native.POINTER_SIZE;
                    p = this.getPointer(offset + addOffset);
                }
            }
        }
        else {
            Pointer p;
            while ((p = this.getPointer(offset + addOffset)) != null) {
                final String s2 = "--WIDE-STRING--".equals(encoding) ? p.getWideString(0L) : p.getString(0L, encoding);
                strings.add(s2);
                addOffset += Native.POINTER_SIZE;
            }
        }
        return strings.toArray(new String[0]);
    }
    
    void setValue(final long offset, final Object value, final Class<?> type) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            this.setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
        }
        else if (type == Byte.TYPE || type == Byte.class) {
            this.setByte(offset, (byte)((value == null) ? 0 : ((byte)value)));
        }
        else if (type == Short.TYPE || type == Short.class) {
            this.setShort(offset, (short)((value == null) ? 0 : ((short)value)));
        }
        else if (type == Character.TYPE || type == Character.class) {
            this.setChar(offset, (value == null) ? '\0' : ((char)value));
        }
        else if (type == Integer.TYPE || type == Integer.class) {
            this.setInt(offset, (value == null) ? 0 : ((int)value));
        }
        else if (type == Long.TYPE || type == Long.class) {
            this.setLong(offset, (value == null) ? 0L : ((long)value));
        }
        else if (type == Float.TYPE || type == Float.class) {
            this.setFloat(offset, (value == null) ? 0.0f : ((float)value));
        }
        else if (type == Double.TYPE || type == Double.class) {
            this.setDouble(offset, (value == null) ? 0.0 : ((double)value));
        }
        else if (type == Pointer.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (type == String.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (type == WString.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (Structure.class.isAssignableFrom(type)) {
            final Structure s = (Structure)value;
            if (Structure.ByReference.class.isAssignableFrom(type)) {
                this.setPointer(offset, (s == null) ? null : s.getPointer());
                if (s != null) {
                    s.autoWrite();
                }
            }
            else {
                s.useMemory(this, (int)offset, true);
                s.write();
            }
        }
        else if (Callback.class.isAssignableFrom(type)) {
            this.setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
        }
        else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
            final Pointer p = (value == null) ? null : Native.getDirectBufferPointer((Buffer)value);
            this.setPointer(offset, p);
        }
        else if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            final Class<?> nativeType = tc.nativeType();
            this.setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
            }
            this.writeArray(offset, value, type.getComponentType());
        }
    }
    
    private void writeArray(final long offset, final Object value, final Class<?> cls) {
        if (cls == Byte.TYPE) {
            final byte[] buf = (byte[])value;
            this.write(offset, buf, 0, buf.length);
        }
        else if (cls == Short.TYPE) {
            final short[] buf2 = (short[])value;
            this.write(offset, buf2, 0, buf2.length);
        }
        else if (cls == Character.TYPE) {
            final char[] buf3 = (char[])value;
            this.write(offset, buf3, 0, buf3.length);
        }
        else if (cls == Integer.TYPE) {
            final int[] buf4 = (int[])value;
            this.write(offset, buf4, 0, buf4.length);
        }
        else if (cls == Long.TYPE) {
            final long[] buf5 = (long[])value;
            this.write(offset, buf5, 0, buf5.length);
        }
        else if (cls == Float.TYPE) {
            final float[] buf6 = (float[])value;
            this.write(offset, buf6, 0, buf6.length);
        }
        else if (cls == Double.TYPE) {
            final double[] buf7 = (double[])value;
            this.write(offset, buf7, 0, buf7.length);
        }
        else if (Pointer.class.isAssignableFrom(cls)) {
            final Pointer[] buf8 = (Pointer[])value;
            this.write(offset, buf8, 0, buf8.length);
        }
        else if (Structure.class.isAssignableFrom(cls)) {
            final Structure[] sbuf = (Structure[])value;
            if (Structure.ByReference.class.isAssignableFrom(cls)) {
                final Pointer[] buf9 = new Pointer[sbuf.length];
                for (int i = 0; i < sbuf.length; ++i) {
                    if (sbuf[i] == null) {
                        buf9[i] = null;
                    }
                    else {
                        buf9[i] = sbuf[i].getPointer();
                        sbuf[i].write();
                    }
                }
                this.write(offset, buf9, 0, buf9.length);
            }
            else {
                Structure first = sbuf[0];
                if (first == null) {
                    first = Structure.newInstance(cls, this.share(offset));
                    sbuf[0] = first;
                }
                else {
                    first.useMemory(this, (int)offset, true);
                }
                first.write();
                final Structure[] tmp = first.toArray(sbuf.length);
                for (int j = 1; j < sbuf.length; ++j) {
                    if (sbuf[j] == null) {
                        sbuf[j] = tmp[j];
                    }
                    else {
                        sbuf[j].useMemory(this, (int)(offset + j * sbuf[j].size()), true);
                    }
                    sbuf[j].write();
                }
            }
        }
        else {
            if (!NativeMapped.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Writing array of " + cls + " to memory not supported");
            }
            final NativeMapped[] buf10 = (NativeMapped[])value;
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            final Class<?> nativeType = tc.nativeType();
            final int size = Native.getNativeSize(value.getClass(), value) / buf10.length;
            for (int k = 0; k < buf10.length; ++k) {
                final Object element = tc.toNative(buf10[k], new ToNativeContext());
                this.setValue(offset + k * size, element, nativeType);
            }
        }
    }
    
    public void setMemory(final long offset, final long length, final byte value) {
        Native.setMemory(this, this.peer, offset, length, value);
    }
    
    public void setByte(final long offset, final byte value) {
        Native.setByte(this, this.peer, offset, value);
    }
    
    public void setShort(final long offset, final short value) {
        Native.setShort(this, this.peer, offset, value);
    }
    
    public void setChar(final long offset, final char value) {
        Native.setChar(this, this.peer, offset, value);
    }
    
    public void setInt(final long offset, final int value) {
        Native.setInt(this, this.peer, offset, value);
    }
    
    public void setLong(final long offset, final long value) {
        Native.setLong(this, this.peer, offset, value);
    }
    
    public void setNativeLong(final long offset, final NativeLong value) {
        if (NativeLong.SIZE == 8) {
            this.setLong(offset, value.longValue());
        }
        else {
            this.setInt(offset, value.intValue());
        }
    }
    
    public void setFloat(final long offset, final float value) {
        Native.setFloat(this, this.peer, offset, value);
    }
    
    public void setDouble(final long offset, final double value) {
        Native.setDouble(this, this.peer, offset, value);
    }
    
    public void setPointer(final long offset, final Pointer value) {
        Native.setPointer(this, this.peer, offset, (value != null) ? value.peer : 0L);
    }
    
    public void setWideString(final long offset, final String value) {
        Native.setWideString(this, this.peer, offset, value);
    }
    
    public void setString(final long offset, final WString value) {
        this.setWideString(offset, (value == null) ? null : value.toString());
    }
    
    public void setString(final long offset, final String value) {
        this.setString(offset, value, Native.getDefaultStringEncoding());
    }
    
    public void setString(final long offset, final String value, final String encoding) {
        final byte[] data = Native.getBytes(value, encoding);
        this.write(offset, data, 0, data.length);
        this.setByte(offset + data.length, (byte)0);
    }
    
    public String dump(final long offset, final int size) {
        final int BYTES_PER_ROW = 4;
        final String TITLE = "memory dump";
        final StringWriter sw = new StringWriter("memory dump".length() + 2 + size * 2 + size / 4 * 4);
        final PrintWriter out = new PrintWriter(sw);
        out.println("memory dump");
        for (int i = 0; i < size; ++i) {
            final byte b = this.getByte(offset + i);
            if (i % 4 == 0) {
                out.print("[");
            }
            if (b >= 0 && b < 16) {
                out.print("0");
            }
            out.print(Integer.toHexString(b & 0xFF));
            if (i % 4 == 3 && i < size - 1) {
                out.println("]");
            }
        }
        if (sw.getBuffer().charAt(sw.getBuffer().length() - 2) != ']') {
            out.println("]");
        }
        return sw.toString();
    }
    
    @Override
    public String toString() {
        return "native@0x" + Long.toHexString(this.peer);
    }
    
    public static long nativeValue(final Pointer p) {
        return (p == null) ? 0L : p.peer;
    }
    
    public static void nativeValue(final Pointer p, final long value) {
        p.peer = value;
    }
    
    static {
        NULL = null;
    }
    
    private static class Opaque extends Pointer
    {
        private final String MSG;
        
        private Opaque(final long peer) {
            super(peer);
            this.MSG = "This pointer is opaque: " + this;
        }
        
        @Override
        public Pointer share(final long offset, final long size) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void clear(final long size) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public long indexOf(final long offset, final byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final byte[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final char[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final short[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final int[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final long[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final float[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final double[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long bOff, final Pointer[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final byte[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final char[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final short[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final int[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final long[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final float[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final double[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long bOff, final Pointer[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public ByteBuffer getByteBuffer(final long offset, final long length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public byte getByte(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public char getChar(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public short getShort(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public int getInt(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public long getLong(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public float getFloat(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public double getDouble(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public Pointer getPointer(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String getString(final long bOff, final String encoding) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String getWideString(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setByte(final long bOff, final byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setChar(final long bOff, final char value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setShort(final long bOff, final short value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setInt(final long bOff, final int value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setLong(final long bOff, final long value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setFloat(final long bOff, final float value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setDouble(final long bOff, final double value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setPointer(final long offset, final Pointer value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setString(final long offset, final String value, final String encoding) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setWideString(final long offset, final String value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setMemory(final long offset, final long size, final byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String dump(final long offset, final int size) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String toString() {
            return "const@0x" + Long.toHexString(this.peer);
        }
    }
}
