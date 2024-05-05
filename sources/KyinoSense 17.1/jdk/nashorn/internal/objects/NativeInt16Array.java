/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
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

public final class NativeInt16Array
extends ArrayBufferView {
    private static PropertyMap $nasgenmap$;
    public static final int BYTES_PER_ELEMENT = 2;
    private static final ArrayBufferView.Factory FACTORY;

    public static NativeInt16Array constructor(boolean newObj, Object self, Object ... args2) {
        return (NativeInt16Array)NativeInt16Array.constructorImpl(newObj, args2, FACTORY);
    }

    NativeInt16Array(NativeArrayBuffer buffer, int byteOffset, int byteLength) {
        super(buffer, byteOffset, byteLength);
    }

    @Override
    protected ArrayBufferView.Factory factory() {
        return FACTORY;
    }

    protected static Object set(Object self, Object array, Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }

    protected static NativeInt16Array subarray(Object self, Object begin, Object end) {
        return (NativeInt16Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }

    @Override
    protected ScriptObject getPrototype(Global global) {
        return global.getInt16ArrayPrototype();
    }

    static {
        FACTORY = new ArrayBufferView.Factory(2){

            @Override
            public ArrayBufferView construct(NativeArrayBuffer buffer, int byteOffset, int length) {
                return new NativeInt16Array(buffer, byteOffset, length);
            }

            public Int16ArrayData createArrayData(ByteBuffer nb, int start, int end) {
                return new Int16ArrayData(nb.asShortBuffer(), start, end);
            }

            @Override
            public String getClassName() {
                return "Int16Array";
            }
        };
        NativeInt16Array.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private static final class Int16ArrayData
    extends TypedArrayData<ShortBuffer> {
        private static final MethodHandle GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int16ArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
        private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Int16ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();

        private Int16ArrayData(ShortBuffer nb, int start, int end) {
            super(((ShortBuffer)nb.position(start).limit(end)).slice(), end - start);
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
                return ((ShortBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        private void setElem(int index, int elem) {
            try {
                if (index < ((ShortBuffer)this.nb).limit()) {
                    ((ShortBuffer)this.nb).put(index, (short)elem);
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

