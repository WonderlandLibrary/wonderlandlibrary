/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.arrays.ByteBufferArrayData;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import jdk.nashorn.internal.runtime.arrays.DeletedRangeArrayFilter;
import jdk.nashorn.internal.runtime.arrays.FrozenArrayFilter;
import jdk.nashorn.internal.runtime.arrays.IntArrayData;
import jdk.nashorn.internal.runtime.arrays.LengthNotWritableFilter;
import jdk.nashorn.internal.runtime.arrays.NonExtensibleArrayFilter;
import jdk.nashorn.internal.runtime.arrays.NumberArrayData;
import jdk.nashorn.internal.runtime.arrays.ObjectArrayData;
import jdk.nashorn.internal.runtime.arrays.SealedArrayFilter;
import jdk.nashorn.internal.runtime.arrays.SparseArrayData;

public abstract class ArrayData {
    protected static final int CHUNK_SIZE = 32;
    public static final ArrayData EMPTY_ARRAY = new UntouchedArrayData();
    private long length;
    protected static final CompilerConstants.Call THROW_UNWARRANTED = CompilerConstants.staticCall(MethodHandles.lookup(), ArrayData.class, "throwUnwarranted", Void.TYPE, ArrayData.class, Integer.TYPE, Integer.TYPE);

    protected ArrayData(long length) {
        this.length = length;
    }

    public static ArrayData initialArray() {
        return new IntArrayData();
    }

    protected static void throwUnwarranted(ArrayData data, int programPoint, int index) {
        throw new UnwarrantedOptimismException(data.getObject(index), programPoint);
    }

    protected static int alignUp(int size) {
        return size + 32 - 1 & 0xFFFFFFE0;
    }

    public static ArrayData allocate(long length) {
        if (length == 0L) {
            return new IntArrayData();
        }
        if (length >= 131072L) {
            return new SparseArrayData(EMPTY_ARRAY, length);
        }
        return new DeletedRangeArrayFilter(new IntArrayData((int)length), 0L, length - 1L);
    }

    public static ArrayData allocate(Object array) {
        Class<?> clazz = array.getClass();
        if (clazz == int[].class) {
            return new IntArrayData((int[])array, ((int[])array).length);
        }
        if (clazz == double[].class) {
            return new NumberArrayData((double[])array, ((double[])array).length);
        }
        return new ObjectArrayData((Object[])array, ((Object[])array).length);
    }

    public static ArrayData allocate(int[] array) {
        return new IntArrayData(array, array.length);
    }

    public static ArrayData allocate(double[] array) {
        return new NumberArrayData(array, array.length);
    }

    public static ArrayData allocate(Object[] array) {
        return new ObjectArrayData(array, array.length);
    }

    public static ArrayData allocate(ByteBuffer buf) {
        return new ByteBufferArrayData(buf);
    }

    public static ArrayData freeze(ArrayData underlying) {
        return new FrozenArrayFilter(underlying);
    }

    public static ArrayData seal(ArrayData underlying) {
        return new SealedArrayFilter(underlying);
    }

    public static ArrayData preventExtension(ArrayData underlying) {
        return new NonExtensibleArrayFilter(underlying);
    }

    public static ArrayData setIsLengthNotWritable(ArrayData underlying) {
        return new LengthNotWritableFilter(underlying);
    }

    public final long length() {
        return this.length;
    }

    public abstract ArrayData copy();

    public abstract Object[] asObjectArray();

    public Object asArrayOfType(Class<?> componentType) {
        return JSType.convertArray(this.asObjectArray(), componentType);
    }

    public void setLength(long length) {
        this.length = length;
    }

    protected final long increaseLength() {
        return ++this.length;
    }

    protected final long decreaseLength() {
        return --this.length;
    }

    public abstract ArrayData shiftLeft(int var1);

    public abstract ArrayData shiftRight(int var1);

    public abstract ArrayData ensure(long var1);

    public abstract ArrayData shrink(long var1);

    public abstract ArrayData set(int var1, Object var2, boolean var3);

    public abstract ArrayData set(int var1, int var2, boolean var3);

    public abstract ArrayData set(int var1, double var2, boolean var4);

    public ArrayData setEmpty(int index) {
        return this;
    }

    public ArrayData setEmpty(long lo, long hi) {
        return this;
    }

    public abstract int getInt(int var1);

    public Type getOptimisticType() {
        return Type.OBJECT;
    }

    public int getIntOptimistic(int index, int programPoint) {
        throw new UnwarrantedOptimismException(this.getObject(index), programPoint, this.getOptimisticType());
    }

    public abstract double getDouble(int var1);

    public double getDoubleOptimistic(int index, int programPoint) {
        throw new UnwarrantedOptimismException(this.getObject(index), programPoint, this.getOptimisticType());
    }

    public abstract Object getObject(int var1);

    public abstract boolean has(int var1);

    public boolean canDelete(int index, boolean strict) {
        return true;
    }

    public boolean canDelete(long longIndex, boolean strict) {
        return true;
    }

    public final ArrayData safeDelete(long fromIndex, long toIndex, boolean strict) {
        if (fromIndex <= toIndex && this.canDelete(fromIndex, strict)) {
            return this.delete(fromIndex, toIndex);
        }
        return this;
    }

    public PropertyDescriptor getDescriptor(Global global, int index) {
        return global.newDataDescriptor(this.getObject(index), true, true, true);
    }

    public abstract ArrayData delete(int var1);

    public abstract ArrayData delete(long var1, long var3);

    public abstract ArrayData convert(Class<?> var1);

    public ArrayData push(boolean strict, Object ... items) {
        if (items.length == 0) {
            return this;
        }
        Class<?> widest = ArrayData.widestType(items);
        ArrayData newData = this.convert(widest);
        long pos = newData.length;
        for (Object item : items) {
            newData = newData.ensure(pos);
            newData.set((int)pos++, item, strict);
        }
        return newData;
    }

    public ArrayData push(boolean strict, Object item) {
        return this.push(strict, new Object[]{item});
    }

    public abstract Object pop();

    public abstract ArrayData slice(long var1, long var3);

    public ArrayData fastSplice(int start, int removed2, int added) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    static Class<?> widestType(Object ... items) {
        assert (items.length > 0);
        Class widest = Integer.class;
        for (Object item : items) {
            if (item == null) {
                return Object.class;
            }
            Class<?> itemClass = item.getClass();
            if (itemClass == Double.class || itemClass == Float.class || itemClass == Long.class) {
                if (widest != Integer.class) continue;
                widest = Double.class;
                continue;
            }
            if (itemClass == Integer.class || itemClass == Short.class || itemClass == Byte.class) continue;
            return Object.class;
        }
        return widest;
    }

    protected List<Long> computeIteratorKeys() {
        ArrayList<Long> keys2 = new ArrayList<Long>();
        long len = this.length();
        long i = 0L;
        while (i < len) {
            if (this.has((int)i)) {
                keys2.add(i);
            }
            i = this.nextIndex(i);
        }
        return keys2;
    }

    public Iterator<Long> indexIterator() {
        return this.computeIteratorKeys().iterator();
    }

    public static int nextSize(int size) {
        return ArrayData.alignUp(size + 1) * 2;
    }

    long nextIndex(long index) {
        return index + 1L;
    }

    static Object invoke(MethodHandle mh, Object arg) {
        try {
            return mh.invoke(arg);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public GuardedInvocation findFastCallMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }

    public GuardedInvocation findFastGetMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request, String operator) {
        return null;
    }

    public GuardedInvocation findFastGetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }

    public GuardedInvocation findFastSetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }

    private static class UntouchedArrayData
    extends ContinuousArrayData {
        private UntouchedArrayData() {
            super(0L);
        }

        private ArrayData toRealArrayData() {
            return new IntArrayData(0);
        }

        private ArrayData toRealArrayData(int index) {
            IntArrayData newData = new IntArrayData(index + 1);
            return new DeletedRangeArrayFilter(newData, 0L, index);
        }

        @Override
        public ContinuousArrayData copy() {
            assert (this.length() == 0L);
            return this;
        }

        @Override
        public Object asArrayOfType(Class<?> componentType) {
            return Array.newInstance(componentType, 0);
        }

        @Override
        public Object[] asObjectArray() {
            return ScriptRuntime.EMPTY_ARRAY;
        }

        @Override
        public ArrayData ensure(long safeIndex) {
            assert (safeIndex >= 0L);
            if (safeIndex >= 131072L) {
                return new SparseArrayData(this, safeIndex + 1L);
            }
            return this.toRealArrayData((int)safeIndex);
        }

        @Override
        public ArrayData convert(Class<?> type) {
            return this.toRealArrayData().convert(type);
        }

        @Override
        public ArrayData delete(int index) {
            return new DeletedRangeArrayFilter(this, index, index);
        }

        @Override
        public ArrayData delete(long fromIndex, long toIndex) {
            return new DeletedRangeArrayFilter(this, fromIndex, toIndex);
        }

        @Override
        public ArrayData shiftLeft(int by) {
            return this;
        }

        @Override
        public ArrayData shiftRight(int by) {
            return this;
        }

        @Override
        public ArrayData shrink(long newLength) {
            return this;
        }

        @Override
        public ArrayData set(int index, Object value, boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }

        @Override
        public ArrayData set(int index, int value, boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }

        @Override
        public ArrayData set(int index, double value, boolean strict) {
            return this.toRealArrayData(index).set(index, value, strict);
        }

        @Override
        public int getInt(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override
        public double getDouble(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override
        public Object getObject(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override
        public boolean has(int index) {
            return false;
        }

        @Override
        public Object pop() {
            return ScriptRuntime.UNDEFINED;
        }

        @Override
        public ArrayData push(boolean strict, Object item) {
            return this.toRealArrayData().push(strict, item);
        }

        @Override
        public ArrayData slice(long from, long to) {
            return this;
        }

        @Override
        public ContinuousArrayData fastConcat(ContinuousArrayData otherData) {
            return otherData.copy();
        }

        public String toString() {
            return this.getClass().getSimpleName();
        }

        @Override
        public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
            return null;
        }

        @Override
        public MethodHandle getElementSetter(Class<?> elementType) {
            return null;
        }

        @Override
        public Class<?> getElementType() {
            return Integer.TYPE;
        }

        @Override
        public Class<?> getBoxedElementType() {
            return Integer.class;
        }
    }
}

