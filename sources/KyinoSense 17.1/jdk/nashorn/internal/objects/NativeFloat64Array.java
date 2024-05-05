/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
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

public final class NativeFloat64Array
extends ArrayBufferView {
    public static final int BYTES_PER_ELEMENT = 8;
    private static PropertyMap $nasgenmap$;
    private static final ArrayBufferView.Factory FACTORY;

    public static NativeFloat64Array constructor(boolean newObj, Object self, Object ... args2) {
        return (NativeFloat64Array)NativeFloat64Array.constructorImpl(newObj, args2, FACTORY);
    }

    NativeFloat64Array(NativeArrayBuffer buffer, int byteOffset, int length) {
        super(buffer, byteOffset, length);
    }

    @Override
    protected ArrayBufferView.Factory factory() {
        return FACTORY;
    }

    @Override
    protected boolean isFloatArray() {
        return true;
    }

    protected static Object set(Object self, Object array, Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }

    protected static NativeFloat64Array subarray(Object self, Object begin, Object end) {
        return (NativeFloat64Array)ArrayBufferView.subarrayImpl(self, begin, end);
    }

    @Override
    protected ScriptObject getPrototype(Global global) {
        return global.getFloat64ArrayPrototype();
    }

    static {
        FACTORY = new ArrayBufferView.Factory(8){

            @Override
            public ArrayBufferView construct(NativeArrayBuffer buffer, int byteOffset, int length) {
                return new NativeFloat64Array(buffer, byteOffset, length);
            }

            public Float64ArrayData createArrayData(ByteBuffer nb, int start, int length) {
                return new Float64ArrayData(nb.asDoubleBuffer(), start, length);
            }

            @Override
            public String getClassName() {
                return "Float64Array";
            }
        };
        NativeFloat64Array.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private static final class Float64ArrayData
    extends TypedArrayData<DoubleBuffer> {
        private static final MethodHandle GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Float64ArrayData.class, "getElem", Double.TYPE, Integer.TYPE).methodHandle();
        private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Float64ArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Double.TYPE).methodHandle();

        private Float64ArrayData(DoubleBuffer nb, int start, int end) {
            super(((DoubleBuffer)nb.position(start).limit(end)).slice(), end - start);
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
            return Double.TYPE;
        }

        @Override
        public Class<?> getBoxedElementType() {
            return Double.class;
        }

        private double getElem(int index) {
            try {
                return ((DoubleBuffer)this.nb).get(index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        private void setElem(int index, double elem) {
            try {
                if (index < ((DoubleBuffer)this.nb).limit()) {
                    ((DoubleBuffer)this.nb).put(index, elem);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        @Override
        public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
            if (returnType == Integer.TYPE) {
                return null;
            }
            return this.getContinuousElementGetter(this.getClass(), GET_ELEM, returnType, programPoint);
        }

        @Override
        public int getInt(int index) {
            return (int)this.getDouble(index);
        }

        @Override
        public double getDouble(int index) {
            return this.getElem(index);
        }

        @Override
        public double getDoubleOptimistic(int index, int programPoint) {
            return this.getElem(index);
        }

        @Override
        public Object getObject(int index) {
            return this.getDouble(index);
        }

        @Override
        public ArrayData set(int index, Object value, boolean strict) {
            return this.set(index, JSType.toNumber(value), strict);
        }

        @Override
        public ArrayData set(int index, int value, boolean strict) {
            return this.set(index, (double)value, strict);
        }

        @Override
        public ArrayData set(int index, double value, boolean strict) {
            this.setElem(index, value);
            return this;
        }
    }
}

