/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;
import jdk.nashorn.internal.runtime.arrays.DeletedRangeArrayFilter;
import jdk.nashorn.internal.runtime.arrays.IntElements;
import jdk.nashorn.internal.runtime.arrays.NumberArrayData;
import jdk.nashorn.internal.runtime.arrays.ObjectArrayData;
import jdk.nashorn.internal.runtime.arrays.SparseArrayData;
import jdk.nashorn.internal.runtime.arrays.UndefinedArrayFilter;

final class IntArrayData
extends ContinuousArrayData
implements IntElements {
    private int[] array;
    private static final MethodHandle HAS_GET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), IntArrayData.class, "getElem", Integer.TYPE, Integer.TYPE).methodHandle();
    private static final MethodHandle SET_ELEM = CompilerConstants.specialCall(MethodHandles.lookup(), IntArrayData.class, "setElem", Void.TYPE, Integer.TYPE, Integer.TYPE).methodHandle();

    IntArrayData() {
        this(new int[32], 0);
    }

    IntArrayData(int length) {
        super(length);
        this.array = new int[ArrayData.nextSize(length)];
    }

    IntArrayData(int[] array, int length) {
        super(length);
        assert (array == null || array.length >= length);
        this.array = array;
    }

    @Override
    public final Class<?> getElementType() {
        return Integer.TYPE;
    }

    @Override
    public final Class<?> getBoxedElementType() {
        return Integer.class;
    }

    @Override
    public final int getElementWeight() {
        return 1;
    }

    @Override
    public final ContinuousArrayData widest(ContinuousArrayData otherData) {
        return otherData;
    }

    @Override
    public Object[] asObjectArray() {
        return this.toObjectArray(true);
    }

    private int getElem(int index) {
        if (this.has(index)) {
            return this.array[index];
        }
        throw new ClassCastException();
    }

    private void setElem(int index, int elem) {
        if (this.hasRoomFor(index)) {
            this.array[index] = elem;
            return;
        }
        throw new ClassCastException();
    }

    @Override
    public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
        return this.getContinuousElementGetter(HAS_GET_ELEM, returnType, programPoint);
    }

    @Override
    public MethodHandle getElementSetter(Class<?> elementType) {
        return elementType == Integer.TYPE ? this.getContinuousElementSetter(SET_ELEM, elementType) : null;
    }

    @Override
    public IntArrayData copy() {
        return new IntArrayData((int[])this.array.clone(), (int)this.length());
    }

    @Override
    public Object asArrayOfType(Class<?> componentType) {
        if (componentType == Integer.TYPE) {
            int len = (int)this.length();
            return this.array.length == len ? (int[])this.array.clone() : Arrays.copyOf(this.array, len);
        }
        return super.asArrayOfType(componentType);
    }

    private Object[] toObjectArray(boolean trim) {
        assert (this.length() <= (long)this.array.length) : "length exceeds internal array size";
        int len = (int)this.length();
        Object[] oarray = new Object[trim ? len : this.array.length];
        for (int index = 0; index < len; ++index) {
            oarray[index] = this.array[index];
        }
        return oarray;
    }

    private double[] toDoubleArray() {
        assert (this.length() <= (long)this.array.length) : "length exceeds internal array size";
        int len = (int)this.length();
        double[] darray = new double[this.array.length];
        for (int index = 0; index < len; ++index) {
            darray[index] = this.array[index];
        }
        return darray;
    }

    private NumberArrayData convertToDouble() {
        return new NumberArrayData(this.toDoubleArray(), (int)this.length());
    }

    private ObjectArrayData convertToObject() {
        return new ObjectArrayData(this.toObjectArray(false), (int)this.length());
    }

    @Override
    public ArrayData convert(Class<?> type) {
        if (type == Integer.class || type == Byte.class || type == Short.class) {
            return this;
        }
        if (type == Double.class || type == Float.class) {
            return this.convertToDouble();
        }
        return this.convertToObject();
    }

    @Override
    public ArrayData shiftLeft(int by) {
        if ((long)by >= this.length()) {
            this.shrink(0L);
        } else {
            System.arraycopy(this.array, by, this.array, 0, this.array.length - by);
        }
        this.setLength(Math.max(0L, this.length() - (long)by));
        return this;
    }

    @Override
    public ArrayData shiftRight(int by) {
        ArrayData newData = this.ensure((long)by + this.length() - 1L);
        if (newData != this) {
            newData.shiftRight(by);
            return newData;
        }
        System.arraycopy(this.array, 0, this.array, by, this.array.length - by);
        return this;
    }

    @Override
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072L) {
            return new SparseArrayData(this, safeIndex + 1L);
        }
        int alen = this.array.length;
        if (safeIndex >= (long)alen) {
            int newLength = ArrayData.nextSize((int)safeIndex);
            this.array = Arrays.copyOf(this.array, newLength);
        }
        if (safeIndex >= this.length()) {
            this.setLength(safeIndex + 1L);
        }
        return this;
    }

    @Override
    public ArrayData shrink(long newLength) {
        Arrays.fill(this.array, (int)newLength, this.array.length, 0);
        return this;
    }

    @Override
    public ArrayData set(int index, Object value, boolean strict) {
        if (JSType.isRepresentableAsInt(value)) {
            return this.set(index, JSType.toInt32(value), strict);
        }
        if (value == ScriptRuntime.UNDEFINED) {
            return new UndefinedArrayFilter(this).set(index, value, strict);
        }
        ArrayData newData = this.convert(value == null ? Object.class : value.getClass());
        return newData.set(index, value, strict);
    }

    @Override
    public ArrayData set(int index, int value, boolean strict) {
        this.array[index] = value;
        this.setLength(Math.max((long)(index + 1), this.length()));
        return this;
    }

    @Override
    public ArrayData set(int index, double value, boolean strict) {
        if (JSType.isRepresentableAsInt(value)) {
            this.array[index] = (int)value;
            this.setLength(Math.max((long)(index + 1), this.length()));
            return this;
        }
        return this.convert(Double.class).set(index, value, strict);
    }

    @Override
    public int getInt(int index) {
        return this.array[index];
    }

    @Override
    public int getIntOptimistic(int index, int programPoint) {
        return this.array[index];
    }

    @Override
    public double getDouble(int index) {
        return this.array[index];
    }

    @Override
    public double getDoubleOptimistic(int index, int programPoint) {
        return this.array[index];
    }

    @Override
    public Object getObject(int index) {
        return this.array[index];
    }

    @Override
    public boolean has(int index) {
        return 0 <= index && (long)index < this.length();
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
    public Object pop() {
        int len = (int)this.length();
        if (len == 0) {
            return ScriptRuntime.UNDEFINED;
        }
        int newLength = len - 1;
        int elem = this.array[newLength];
        this.array[newLength] = 0;
        this.setLength(newLength);
        return elem;
    }

    @Override
    public ArrayData slice(long from, long to) {
        return new IntArrayData(Arrays.copyOfRange(this.array, (int)from, (int)to), (int)(to - (from < 0L ? from + this.length() : from)));
    }

    @Override
    public ArrayData fastSplice(int start, int removed2, int added) throws UnsupportedOperationException {
        ArrayData returnValue;
        long oldLength = this.length();
        long newLength = oldLength - (long)removed2 + (long)added;
        if (newLength > 131072L && newLength > (long)this.array.length) {
            throw new UnsupportedOperationException();
        }
        ArrayData arrayData = returnValue = removed2 == 0 ? EMPTY_ARRAY : new IntArrayData(Arrays.copyOfRange(this.array, start, start + removed2), removed2);
        if (newLength != oldLength) {
            int[] newArray;
            if (newLength > (long)this.array.length) {
                newArray = new int[ArrayData.nextSize((int)newLength)];
                System.arraycopy(this.array, 0, newArray, 0, start);
            } else {
                newArray = this.array;
            }
            System.arraycopy(this.array, start + removed2, newArray, start + added, (int)(oldLength - (long)start - (long)removed2));
            this.array = newArray;
            this.setLength(newLength);
        }
        return returnValue;
    }

    @Override
    public double fastPush(int arg) {
        int len = (int)this.length();
        if (len == this.array.length) {
            this.array = Arrays.copyOf(this.array, IntArrayData.nextSize(len));
        }
        this.array[len] = arg;
        return this.increaseLength();
    }

    @Override
    public int fastPopInt() {
        if (this.length() == 0L) {
            throw new ClassCastException();
        }
        int newLength = (int)this.decreaseLength();
        int elem = this.array[newLength];
        this.array[newLength] = 0;
        return elem;
    }

    @Override
    public double fastPopDouble() {
        return this.fastPopInt();
    }

    @Override
    public Object fastPopObject() {
        return this.fastPopInt();
    }

    @Override
    public ContinuousArrayData fastConcat(ContinuousArrayData otherData) {
        int otherLength = (int)otherData.length();
        int thisLength = (int)this.length();
        assert (otherLength > 0 && thisLength > 0);
        int[] otherArray = ((IntArrayData)otherData).array;
        int newLength = otherLength + thisLength;
        int[] newArray = new int[ArrayData.alignUp(newLength)];
        System.arraycopy(this.array, 0, newArray, 0, thisLength);
        System.arraycopy(otherArray, 0, newArray, thisLength, otherLength);
        return new IntArrayData(newArray, newLength);
    }

    public String toString() {
        assert (this.length() <= (long)this.array.length) : this.length() + " > " + this.array.length;
        return this.getClass().getSimpleName() + ':' + Arrays.toString(Arrays.copyOf(this.array, (int)this.length()));
    }
}

