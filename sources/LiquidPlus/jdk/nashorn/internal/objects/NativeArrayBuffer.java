/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.ArrayBufferView;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class NativeArrayBuffer
extends ScriptObject {
    private final ByteBuffer nb;
    private static PropertyMap $nasgenmap$;

    protected NativeArrayBuffer(ByteBuffer nb, Global global) {
        super(global.getArrayBufferPrototype(), $nasgenmap$);
        this.nb = nb;
    }

    protected NativeArrayBuffer(ByteBuffer nb) {
        this(nb, Global.instance());
    }

    protected NativeArrayBuffer(int byteLength) {
        this(ByteBuffer.allocateDirect(byteLength));
    }

    protected NativeArrayBuffer(NativeArrayBuffer other, int begin, int end) {
        this(NativeArrayBuffer.cloneBuffer(other.getNioBuffer(), begin, end));
    }

    public static NativeArrayBuffer constructor(boolean newObj, Object self, Object ... args2) {
        if (!newObj) {
            throw ECMAErrors.typeError("constructor.requires.new", "ArrayBuffer");
        }
        if (args2.length == 0) {
            return new NativeArrayBuffer(0);
        }
        return new NativeArrayBuffer(JSType.toInt32(args2[0]));
    }

    private static ByteBuffer cloneBuffer(ByteBuffer original, int begin, int end) {
        ByteBuffer clone = ByteBuffer.allocateDirect(original.capacity());
        original.rewind();
        clone.put(original);
        original.rewind();
        clone.flip();
        clone.position(begin);
        clone.limit(end);
        return clone.slice();
    }

    ByteBuffer getNioBuffer() {
        return this.nb;
    }

    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }

    public static int byteLength(Object self) {
        return ((NativeArrayBuffer)self).getByteLength();
    }

    public static boolean isView(Object self, Object obj) {
        return obj instanceof ArrayBufferView;
    }

    public static NativeArrayBuffer slice(Object self, Object begin0, Object end0) {
        NativeArrayBuffer arrayBuffer = (NativeArrayBuffer)self;
        int byteLength = arrayBuffer.getByteLength();
        int begin = NativeArrayBuffer.adjustIndex(JSType.toInt32(begin0), byteLength);
        int end = NativeArrayBuffer.adjustIndex(end0 != ScriptRuntime.UNDEFINED ? JSType.toInt32(end0) : byteLength, byteLength);
        return new NativeArrayBuffer(arrayBuffer, begin, Math.max(end, begin));
    }

    public static Object slice(Object self, int begin, int end) {
        NativeArrayBuffer arrayBuffer = (NativeArrayBuffer)self;
        int byteLength = arrayBuffer.getByteLength();
        return new NativeArrayBuffer(arrayBuffer, NativeArrayBuffer.adjustIndex(begin, byteLength), Math.max(NativeArrayBuffer.adjustIndex(end, byteLength), begin));
    }

    public static Object slice(Object self, int begin) {
        return NativeArrayBuffer.slice(self, begin, ((NativeArrayBuffer)self).getByteLength());
    }

    static int adjustIndex(int index, int length) {
        return index < 0 ? NativeArrayBuffer.clamp(index + length, length) : NativeArrayBuffer.clamp(index, length);
    }

    private static int clamp(int index, int length) {
        if (index < 0) {
            return 0;
        }
        if (index > length) {
            return length;
        }
        return index;
    }

    int getByteLength() {
        return this.nb.limit();
    }

    ByteBuffer getBuffer() {
        return this.nb;
    }

    ByteBuffer getBuffer(int offset) {
        return (ByteBuffer)this.nb.duplicate().position(offset);
    }

    ByteBuffer getBuffer(int offset, int length) {
        return (ByteBuffer)this.getBuffer(offset).limit(length);
    }

    static {
        NativeArrayBuffer.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(1);
        arrayList.add(AccessorProperty.create("byteLength", 7, cfr_ldc_0(), null));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeArrayBuffer.class, "byteLength", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

