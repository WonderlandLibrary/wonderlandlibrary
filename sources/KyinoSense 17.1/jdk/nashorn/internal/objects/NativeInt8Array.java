/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.Collections;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.objects.ArrayBufferView;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArrayBuffer;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;

public final class NativeInt8Array
extends ArrayBufferView {
    public static final int BYTES_PER_ELEMENT = 1;
    private static PropertyMap $nasgenmap$;
    private static final ArrayBufferView.Factory FACTORY;

    public static NativeInt8Array constructor(boolean newObj, Object self, Object ... args2) {
        return (NativeInt8Array)NativeInt8Array.constructorImpl(newObj, args2, FACTORY);
    }

    NativeInt8Array(NativeArrayBuffer buffer, int byteOffset, int length) {
        super(buffer, byteOffset, length);
    }

    @Override
    protected ArrayBufferView.Factory factory() {
        return FACTORY;
    }

    protected static Object set(Object self, Object array, Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }

    protected static NativeInt8Array subarray(Object self, Object begin, Object end) {
        return (NativeInt8Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }

    @Override
    protected ScriptObject getPrototype(Global global) {
        return global.getInt8ArrayPrototype();
    }

    static {
        FACTORY = new ArrayBufferView.Factory(1){

            @Override
            public ArrayBufferView construct(NativeArrayBuffer buffer, int byteOffset, int length) {
                return new NativeInt8Array(buffer, byteOffset, length);
            }

            public Int8ArrayData createArrayData(ByteBuffer nb, int start, int end) {
                return new Int8ArrayData(nb, start, end);
            }

            @Override
            public String getClassName() {
                return "Int8Array";
            }
        };
        NativeInt8Array.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private static final class Int8ArrayData
    extends TypedArrayData<ByteBuffer> {
        private static final MethodHandle GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int8ArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
        private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int8ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();

        private Int8ArrayData(ByteBuffer nb, int start, int end) {
            super(((ByteBuffer)nb.position(start).limit(end)).slice(), end - start);
        }

        @Override
        protected MethodHandle getGetElem() {
            return GET_ELEM;
        }

        @Override
        protected MethodHandle getSetElem() {
            return SET_ELEM;
        }

        @Override
        public Class<?> getElementType() {
            return Integer.TYPE;
        }

        @Override
        public Class<?> getBoxedElementType() {
            return Integer.class;
        }

        private int getElem(int index) {
            try {
                return ((ByteBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        private void setElem(int index, int elem) {
            try {
                if (index < ((ByteBuffer)this.nb).limit()) {
                    ((ByteBuffer)this.nb).put(index, (byte)elem);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        @Override
        public int getInt(int index) {
            return this.getElem(index);
        }

        @Override
        public int getIntOptimistic(int index, int programPoint) {
            return this.getElem(index);
        }

        @Override
        public double getDouble(int index) {
            return this.getInt(index);
        }

        @Override
        public double getDoubleOptimistic(int index, int programPoint) {
            return this.getElem(index);
        }

        @Override
        public Object getObject(int index) {
            return this.getInt(index);
        }

        @Override
        public ArrayData set(int index, Object value, boolean strict) {
            return this.set(index, JSType.toInt32(value), strict);
        }

        @Override
        public ArrayData set(int index, int value, boolean strict) {
            this.setElem(index, value);
            return this;
        }

        @Override
        public ArrayData set(int index, double value, boolean strict) {
            return this.set(index, (int)value, strict);
        }
    }
}

