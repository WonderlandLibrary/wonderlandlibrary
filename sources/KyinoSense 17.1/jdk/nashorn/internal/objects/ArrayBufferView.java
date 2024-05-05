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
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.objects.NativeArrayBuffer;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;

public abstract class ArrayBufferView
extends ScriptObject {
    private final NativeArrayBuffer buffer;
    private final int byteOffset;
    private static PropertyMap $nasgenmap$;

    private ArrayBufferView(NativeArrayBuffer buffer, int byteOffset, int elementLength, Global global) {
        super($nasgenmap$);
        int bytesPerElement = this.bytesPerElement();
        ArrayBufferView.checkConstructorArgs(buffer.getByteLength(), bytesPerElement, byteOffset, elementLength);
        this.setProto(this.getPrototype(global));
        this.buffer = buffer;
        this.byteOffset = byteOffset;
        assert (byteOffset % bytesPerElement == 0);
        int start = byteOffset / bytesPerElement;
        ByteBuffer newNioBuffer = buffer.getNioBuffer().duplicate().order(ByteOrder.nativeOrder());
        TypedArrayData<?> data = this.factory().createArrayData(newNioBuffer, start, start + elementLength);
        this.setArray(data);
    }

    protected ArrayBufferView(NativeArrayBuffer buffer, int byteOffset, int elementLength) {
        this(buffer, byteOffset, elementLength, Global.instance());
    }

    private static void checkConstructorArgs(int byteLength, int bytesPerElement, int byteOffset, int elementLength) {
        if (byteOffset < 0 || elementLength < 0) {
            throw new RuntimeException("byteOffset or length must not be negative, byteOffset=" + byteOffset + ", elementLength=" + elementLength + ", bytesPerElement=" + bytesPerElement);
        }
        if (byteOffset + elementLength * bytesPerElement > byteLength) {
            throw new RuntimeException("byteOffset + byteLength out of range, byteOffset=" + byteOffset + ", elementLength=" + elementLength + ", bytesPerElement=" + bytesPerElement);
        }
        if (byteOffset % bytesPerElement != 0) {
            throw new RuntimeException("byteOffset must be a multiple of the element size, byteOffset=" + byteOffset + " bytesPerElement=" + bytesPerElement);
        }
    }

    private int bytesPerElement() {
        return this.factory().bytesPerElement;
    }

    public static Object buffer(Object self) {
        return ((ArrayBufferView)self).buffer;
    }

    public static int byteOffset(Object self) {
        return ((ArrayBufferView)self).byteOffset;
    }

    public static int byteLength(Object self) {
        ArrayBufferView view = (ArrayBufferView)self;
        return ((TypedArrayData)view.getArray()).getElementLength() * view.bytesPerElement();
    }

    public static int length(Object self) {
        return ((ArrayBufferView)self).elementLength();
    }

    @Override
    public final Object getLength() {
        return this.elementLength();
    }

    private int elementLength() {
        return ((TypedArrayData)this.getArray()).getElementLength();
    }

    protected abstract Factory factory();

    protected abstract ScriptObject getPrototype(Global var1);

    @Override
    public final String getClassName() {
        return this.factory().getClassName();
    }

    protected boolean isFloatArray() {
        return false;
    }

    protected static ArrayBufferView constructorImpl(boolean newObj, Object[] args2, Factory factory) {
        ArrayBufferView dest;
        int length;
        Object arg0;
        Object object = arg0 = args2.length != 0 ? args2[0] : Integer.valueOf(0);
        if (!newObj) {
            throw ECMAErrors.typeError("constructor.requires.new", factory.getClassName());
        }
        if (arg0 instanceof NativeArrayBuffer) {
            int length2;
            int byteOffset;
            NativeArrayBuffer buffer = (NativeArrayBuffer)arg0;
            int n = byteOffset = args2.length > 1 ? JSType.toInt32(args2[1]) : 0;
            if (args2.length > 2) {
                length2 = JSType.toInt32(args2[2]);
            } else {
                if ((buffer.getByteLength() - byteOffset) % factory.bytesPerElement != 0) {
                    throw new RuntimeException("buffer.byteLength - byteOffset must be a multiple of the element size");
                }
                length2 = (buffer.getByteLength() - byteOffset) / factory.bytesPerElement;
            }
            return factory.construct(buffer, byteOffset, length2);
        }
        if (arg0 instanceof ArrayBufferView) {
            length = ((ArrayBufferView)arg0).elementLength();
            dest = factory.construct(length);
        } else if (arg0 instanceof NativeArray) {
            length = ArrayBufferView.lengthToInt(((NativeArray)arg0).getArray().length());
            dest = factory.construct(length);
        } else {
            double dlen = JSType.toNumber(arg0);
            int length3 = ArrayBufferView.lengthToInt(Double.isInfinite(dlen) ? 0L : JSType.toLong(dlen));
            return factory.construct(length3);
        }
        ArrayBufferView.copyElements(dest, length, (ScriptObject)arg0, 0);
        return dest;
    }

    protected static Object setImpl(Object self, Object array, Object offset0) {
        int length;
        ArrayBufferView dest = (ArrayBufferView)self;
        if (array instanceof ArrayBufferView) {
            length = ((ArrayBufferView)array).elementLength();
        } else if (array instanceof NativeArray) {
            length = (int)(((NativeArray)array).getArray().length() & Integer.MAX_VALUE);
        } else {
            throw new RuntimeException("argument is not of array type");
        }
        ScriptObject source = (ScriptObject)array;
        int offset = JSType.toInt32(offset0);
        if (dest.elementLength() < length + offset || offset < 0) {
            throw new RuntimeException("offset or array length out of bounds");
        }
        ArrayBufferView.copyElements(dest, length, source, offset);
        return ScriptRuntime.UNDEFINED;
    }

    private static void copyElements(ArrayBufferView dest, int length, ScriptObject source, int offset) {
        if (!dest.isFloatArray()) {
            int i = 0;
            int j = offset;
            while (i < length) {
                dest.set(j, source.getInt(i, -1), 0);
                ++i;
                ++j;
            }
        } else {
            int i = 0;
            int j = offset;
            while (i < length) {
                dest.set(j, source.getDouble(i, -1), 0);
                ++i;
                ++j;
            }
        }
    }

    private static int lengthToInt(long length) {
        if (length > Integer.MAX_VALUE || length < 0L) {
            throw ECMAErrors.rangeError("inappropriate.array.buffer.length", JSType.toString(length));
        }
        return (int)(length & Integer.MAX_VALUE);
    }

    protected static ScriptObject subarrayImpl(Object self, Object begin0, Object end0) {
        ArrayBufferView arrayView = (ArrayBufferView)self;
        int byteOffset = arrayView.byteOffset;
        int bytesPerElement = arrayView.bytesPerElement();
        int elementLength = arrayView.elementLength();
        int begin = NativeArrayBuffer.adjustIndex(JSType.toInt32(begin0), elementLength);
        int end = NativeArrayBuffer.adjustIndex(end0 != ScriptRuntime.UNDEFINED ? JSType.toInt32(end0) : elementLength, elementLength);
        int length = Math.max(end - begin, 0);
        assert (byteOffset % bytesPerElement == 0);
        return arrayView.factory().construct(arrayView.buffer, begin * bytesPerElement + byteOffset, length);
    }

    @Override
    protected GuardedInvocation findGetIndexMethod(CallSiteDescriptor desc, LinkRequest request) {
        GuardedInvocation inv = this.getArray().findFastGetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findGetIndexMethod(desc, request);
    }

    @Override
    protected GuardedInvocation findSetIndexMethod(CallSiteDescriptor desc, LinkRequest request) {
        GuardedInvocation inv = this.getArray().findFastSetIndexMethod(this.getArray().getClass(), desc, request);
        if (inv != null) {
            return inv;
        }
        return super.findSetIndexMethod(desc, request);
    }

    static {
        ArrayBufferView.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(4);
        arrayList.add(AccessorProperty.create("buffer", 7, cfr_ldc_0(), null));
        arrayList.add(AccessorProperty.create("byteOffset", 7, cfr_ldc_1(), null));
        arrayList.add(AccessorProperty.create("byteLength", 7, cfr_ldc_2(), null));
        arrayList.add(AccessorProperty.create("length", 7, cfr_ldc_3(), null));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(ArrayBufferView.class, "buffer", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(ArrayBufferView.class, "byteOffset", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)I", null));
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
            return MethodHandles.lookup().findStatic(ArrayBufferView.class, "byteLength", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_3() {
        try {
            return MethodHandles.lookup().findStatic(ArrayBufferView.class, "length", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    protected static abstract class Factory {
        final int bytesPerElement;
        final int maxElementLength;

        public Factory(int bytesPerElement) {
            this.bytesPerElement = bytesPerElement;
            this.maxElementLength = Integer.MAX_VALUE / bytesPerElement;
        }

        public final ArrayBufferView construct(int elementLength) {
            if (elementLength > this.maxElementLength) {
                throw ECMAErrors.rangeError("inappropriate.array.buffer.length", JSType.toString(elementLength));
            }
            return this.construct(new NativeArrayBuffer(elementLength * this.bytesPerElement), 0, elementLength);
        }

        public abstract ArrayBufferView construct(NativeArrayBuffer var1, int var2, int var3);

        public abstract TypedArrayData<?> createArrayData(ByteBuffer var1, int var2, int var3);

        public abstract String getClassName();
    }
}

