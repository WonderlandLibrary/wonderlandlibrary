/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.Collections;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.ArrayBufferView;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArrayBuffer;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.TypedArrayData;

public final class NativeUint8ClampedArray
extends ArrayBufferView {
    public static final int BYTES_PER_ELEMENT = 1;
    private static PropertyMap $nasgenmap$;
    private static final ArrayBufferView.Factory FACTORY;

    public static NativeUint8ClampedArray constructor(boolean newObj, Object self, Object ... args2) {
        return (NativeUint8ClampedArray)NativeUint8ClampedArray.constructorImpl(newObj, args2, FACTORY);
    }

    NativeUint8ClampedArray(NativeArrayBuffer buffer, int byteOffset, int length) {
        super(buffer, byteOffset, length);
    }

    @Override
    protected ArrayBufferView.Factory factory() {
        return FACTORY;
    }

    protected static Object set(Object self, Object array, Object offset) {
        return ArrayBufferView.setImpl(self, array, offset);
    }

    protected static NativeUint8ClampedArray subarray(Object self, Object begin, Object end) {
        return (NativeUint8ClampedArray)ArrayBufferView.subarrayImpl(self, begin, end);
    }

    @Override
    protected ScriptObject getPrototype(Global global) {
        return global.getUint8ClampedArrayPrototype();
    }

    static {
        FACTORY = new ArrayBufferView.Factory(1){

            @Override
            public ArrayBufferView construct(NativeArrayBuffer buffer, int byteOffset, int length) {
                return new NativeUint8ClampedArray(buffer, byteOffset, length);
            }

            public Uint8ClampedArrayData createArrayData(ByteBuffer nb, int start, int end) {
                return new Uint8ClampedArrayData(nb, start, end);
            }

            @Override
            public String getClassName() {
                return "Uint8ClampedArray";
            }
        };
        NativeUint8ClampedArray.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private static final class Uint8ClampedArrayData
    extends TypedArrayData<ByteBuffer> {
        private static final MethodHandle GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
        private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();
        private static final MethodHandle RINT_D = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "rint", Double.TYPE, Double.TYPE).methodHandle();
        private static final MethodHandle RINT_O = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "rint", Object.class, Object.class).methodHandle();
        private static final MethodHandle CLAMP_LONG = CompilerConstants.staticCall(MethodHandles.lookup(), Uint8ClampedArrayData.class, "clampLong", Long.TYPE, Long.TYPE).methodHandle();

        private Uint8ClampedArrayData(ByteBuffer nb, int start, int end) {
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
            return Integer.TYPE;
        }

        private int getElem(int index) {
            try {
                return ((ByteBuffer)this.nb).get(index) & 0xFF;
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        @Override
        public MethodHandle getElementSetter(Class<?> elementType) {
            MethodHandle setter = super.getElementSetter(elementType);
            if (setter != null) {
                if (elementType == Object.class) {
                    return Lookup.MH.filterArguments(setter, 2, RINT_O);
                }
                if (elementType == Double.TYPE) {
                    return Lookup.MH.filterArguments(setter, 2, RINT_D);
                }
                if (elementType == Long.TYPE) {
                    return Lookup.MH.filterArguments(setter, 2, CLAMP_LONG);
                }
            }
            return setter;
        }

        private void setElem(int index, int elem) {
            try {
                if (index < ((ByteBuffer)this.nb).limit()) {
                    byte clamped = (elem & 0xFFFFFF00) == 0 ? (byte)elem : (elem < 0 ? (byte)0 : -1);
                    ((ByteBuffer)this.nb).put(index, clamped);
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new ClassCastException();
            }
        }

        @Override
        public boolean isClamped() {
            return true;
        }

        @Override
        public boolean isUnsigned() {
            return true;
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
            return this.set(index, JSType.toNumber(value), strict);
        }

        @Override
        public ArrayData set(int index, int value, boolean strict) {
            this.setElem(index, value);
            return this;
        }

        @Override
        public ArrayData set(int index, double value, boolean strict) {
            return this.set(index, Uint8ClampedArrayData.rint(value), strict);
        }

        private static double rint(double rint) {
            return (int)Math.rint(rint);
        }

        private static Object rint(Object rint) {
            return Uint8ClampedArrayData.rint(JSType.toNumber(rint));
        }

        private static long clampLong(long l) {
            if (l < 0L) {
                return 0L;
            }
            if (l > 255L) {
                return 255L;
            }
            return l;
        }
    }
}

