/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArrayBuffer;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public class NativeDataView
extends ScriptObject {
    private static PropertyMap $nasgenmap$;
    public final Object buffer;
    public final int byteOffset;
    public final int byteLength;
    private final ByteBuffer buf;

    private NativeDataView(NativeArrayBuffer arrBuf) {
        this(arrBuf, arrBuf.getBuffer(), 0);
    }

    private NativeDataView(NativeArrayBuffer arrBuf, int offset) {
        this(arrBuf, NativeDataView.bufferFrom(arrBuf, offset), offset);
    }

    private NativeDataView(NativeArrayBuffer arrBuf, int offset, int length) {
        this(arrBuf, NativeDataView.bufferFrom(arrBuf, offset, length), offset, length);
    }

    private NativeDataView(NativeArrayBuffer arrBuf, ByteBuffer buf, int offset) {
        this(arrBuf, buf, offset, buf.capacity() - offset);
    }

    private NativeDataView(NativeArrayBuffer arrBuf, ByteBuffer buf, int offset, int length) {
        super(Global.instance().getDataViewPrototype(), $nasgenmap$);
        this.buffer = arrBuf;
        this.byteOffset = offset;
        this.byteLength = length;
        this.buf = buf;
    }

    public static NativeDataView constructor(boolean newObj, Object self, Object ... args2) {
        if (args2.length == 0 || !(args2[0] instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        NativeArrayBuffer arrBuf = (NativeArrayBuffer)args2[0];
        switch (args2.length) {
            case 1: {
                return new NativeDataView(arrBuf);
            }
            case 2: {
                return new NativeDataView(arrBuf, JSType.toInt32(args2[1]));
            }
        }
        return new NativeDataView(arrBuf, JSType.toInt32(args2[1]), JSType.toInt32(args2[2]));
    }

    public static NativeDataView constructor(boolean newObj, Object self, Object arrBuf, int offset) {
        if (!(arrBuf instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        return new NativeDataView((NativeArrayBuffer)arrBuf, offset);
    }

    public static NativeDataView constructor(boolean newObj, Object self, Object arrBuf, int offset, int length) {
        if (!(arrBuf instanceof NativeArrayBuffer)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", new String[0]);
        }
        return new NativeDataView((NativeArrayBuffer)arrBuf, offset, length);
    }

    public static int getInt8(Object self, Object byteOffset) {
        try {
            return NativeDataView.getBuffer(self).get(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt8(Object self, int byteOffset) {
        try {
            return NativeDataView.getBuffer(self).get(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getUint8(Object self, Object byteOffset) {
        try {
            return 0xFF & NativeDataView.getBuffer(self).get(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getUint8(Object self, int byteOffset) {
        try {
            return 0xFF & NativeDataView.getBuffer(self).get(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt16(Object self, Object byteOffset, Object littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getShort(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt16(Object self, int byteOffset) {
        try {
            return NativeDataView.getBuffer(self, false).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt16(Object self, int byteOffset, boolean littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getUint16(Object self, Object byteOffset, Object littleEndian) {
        try {
            return 0xFFFF & NativeDataView.getBuffer(self, littleEndian).getShort(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getUint16(Object self, int byteOffset) {
        try {
            return 0xFFFF & NativeDataView.getBuffer(self, false).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getUint16(Object self, int byteOffset, boolean littleEndian) {
        try {
            return 0xFFFF & NativeDataView.getBuffer(self, littleEndian).getShort(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt32(Object self, Object byteOffset, Object littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt32(Object self, int byteOffset) {
        try {
            return NativeDataView.getBuffer(self, false).getInt(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static int getInt32(Object self, int byteOffset, boolean littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getInt(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getUint32(Object self, Object byteOffset, Object littleEndian) {
        try {
            return 0xFFFFFFFFL & (long)NativeDataView.getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getUint32(Object self, int byteOffset) {
        try {
            return JSType.toUint32(NativeDataView.getBuffer(self, false).getInt(JSType.toInt32(byteOffset)));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getUint32(Object self, int byteOffset, boolean littleEndian) {
        try {
            return JSType.toUint32(NativeDataView.getBuffer(self, littleEndian).getInt(JSType.toInt32(byteOffset)));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat32(Object self, Object byteOffset, Object littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getFloat(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat32(Object self, int byteOffset) {
        try {
            return NativeDataView.getBuffer(self, false).getFloat(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat32(Object self, int byteOffset, boolean littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getFloat(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat64(Object self, Object byteOffset, Object littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getDouble(JSType.toInt32(byteOffset));
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat64(Object self, int byteOffset) {
        try {
            return NativeDataView.getBuffer(self, false).getDouble(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static double getFloat64(Object self, int byteOffset, boolean littleEndian) {
        try {
            return NativeDataView.getBuffer(self, littleEndian).getDouble(byteOffset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt8(Object self, Object byteOffset, Object value) {
        try {
            NativeDataView.getBuffer(self).put(JSType.toInt32(byteOffset), (byte)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt8(Object self, int byteOffset, int value) {
        try {
            NativeDataView.getBuffer(self).put(byteOffset, (byte)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint8(Object self, Object byteOffset, Object value) {
        try {
            NativeDataView.getBuffer(self).put(JSType.toInt32(byteOffset), (byte)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint8(Object self, int byteOffset, int value) {
        try {
            NativeDataView.getBuffer(self).put(byteOffset, (byte)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt16(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putShort(JSType.toInt32(byteOffset), (short)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt16(Object self, int byteOffset, int value) {
        try {
            NativeDataView.getBuffer(self, false).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt16(Object self, int byteOffset, int value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint16(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putShort(JSType.toInt32(byteOffset), (short)JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint16(Object self, int byteOffset, int value) {
        try {
            NativeDataView.getBuffer(self, false).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint16(Object self, int byteOffset, int value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putShort(byteOffset, (short)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt32(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putInt(JSType.toInt32(byteOffset), JSType.toInt32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt32(Object self, int byteOffset, int value) {
        try {
            NativeDataView.getBuffer(self, false).putInt(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setInt32(Object self, int byteOffset, int value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putInt(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint32(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putInt(JSType.toInt32(byteOffset), (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint32(Object self, int byteOffset, double value) {
        try {
            NativeDataView.getBuffer(self, false).putInt(byteOffset, (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setUint32(Object self, int byteOffset, double value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putInt(byteOffset, (int)JSType.toUint32(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat32(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putFloat((int)JSType.toUint32(byteOffset), (float)JSType.toNumber(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat32(Object self, int byteOffset, double value) {
        try {
            NativeDataView.getBuffer(self, false).putFloat(byteOffset, (float)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat32(Object self, int byteOffset, double value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putFloat(byteOffset, (float)value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat64(Object self, Object byteOffset, Object value, Object littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putDouble((int)JSType.toUint32(byteOffset), JSType.toNumber(value));
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat64(Object self, int byteOffset, double value) {
        try {
            NativeDataView.getBuffer(self, false).putDouble(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    public static Object setFloat64(Object self, int byteOffset, double value, boolean littleEndian) {
        try {
            NativeDataView.getBuffer(self, littleEndian).putDouble(byteOffset, value);
            return ScriptRuntime.UNDEFINED;
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.offset", new String[0]);
        }
    }

    private static ByteBuffer bufferFrom(NativeArrayBuffer nab, int offset) {
        try {
            return nab.getBuffer(offset);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.constructor.offset", new String[0]);
        }
    }

    private static ByteBuffer bufferFrom(NativeArrayBuffer nab, int offset, int length) {
        try {
            return nab.getBuffer(offset, length);
        }
        catch (IllegalArgumentException iae) {
            throw ECMAErrors.rangeError(iae, "dataview.constructor.offset", new String[0]);
        }
    }

    private static NativeDataView checkSelf(Object self) {
        if (!(self instanceof NativeDataView)) {
            throw ECMAErrors.typeError("not.an.arraybuffer.in.dataview", ScriptRuntime.safeToString(self));
        }
        return (NativeDataView)self;
    }

    private static ByteBuffer getBuffer(Object self) {
        return NativeDataView.checkSelf((Object)self).buf;
    }

    private static ByteBuffer getBuffer(Object self, Object littleEndian) {
        return NativeDataView.getBuffer(self, JSType.toBoolean(littleEndian));
    }

    private static ByteBuffer getBuffer(Object self, boolean littleEndian) {
        return NativeDataView.getBuffer(self).order(littleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
    }

    static {
        NativeDataView.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(3);
        arrayList.add(AccessorProperty.create("buffer", 7, cfr_ldc_0(), null));
        arrayList.add(AccessorProperty.create("byteOffset", 7, cfr_ldc_1(), null));
        arrayList.add(AccessorProperty.create("byteLength", 7, cfr_ldc_2(), null));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$buffer() {
        return this.buffer;
    }

    public int G$byteOffset() {
        return this.byteOffset;
    }

    public int G$byteLength() {
        return this.byteLength;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDataView.class, "G$buffer", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_1() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDataView.class, "G$byteOffset", MethodType.fromMethodDescriptorString("()I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_2() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDataView.class, "G$byteLength", MethodType.fromMethodDescriptorString("()I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

