/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.nio.Buffer;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ContinuousArrayData;

public abstract class TypedArrayData<T extends Buffer>
extends ContinuousArrayData {
    protected final T nb;

    protected TypedArrayData(T nb, int elementLength) {
        super(elementLength);
        this.nb = nb;
    }

    public final int getElementLength() {
        return (int)this.length();
    }

    public boolean isUnsigned() {
        return false;
    }

    public boolean isClamped() {
        return false;
    }

    @Override
    public boolean canDelete(int index, boolean strict) {
        return false;
    }

    @Override
    public boolean canDelete(long longIndex, boolean strict) {
        return false;
    }

    @Override
    public TypedArrayData<T> copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] asObjectArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayData shiftLeft(int by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayData shiftRight(int by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayData ensure(long safeIndex) {
        return this;
    }

    @Override
    public ArrayData shrink(long newLength) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean has(int index) {
        return 0 <= index && (long)index < this.length();
    }

    @Override
    public ArrayData delete(int index) {
        return this;
    }

    @Override
    public ArrayData delete(long fromIndex, long toIndex) {
        return this;
    }

    @Override
    public TypedArrayData<T> convert(Class<?> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayData slice(long from, long to) {
        throw new UnsupportedOperationException();
    }

    protected abstract MethodHandle getGetElem();

    protected abstract MethodHandle getSetElem();

    @Override
    public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
        MethodHandle getter = this.getContinuousElementGetter(this.getClass(), this.getGetElem(), returnType, programPoint);
        if (getter != null) {
            return Lookup.filterReturnType(getter, returnType);
        }
        return getter;
    }

    @Override
    public MethodHandle getElementSetter(Class<?> elementType) {
        return this.getContinuousElementSetter(this.getClass(), Lookup.filterArgumentType(this.getSetElem(), 2, elementType), elementType);
    }

    @Override
    protected MethodHandle getContinuousElementSetter(Class<? extends ContinuousArrayData> clazz, MethodHandle setHas, Class<?> elementType) {
        MethodHandle mh = Lookup.filterArgumentType(setHas, 2, elementType);
        return Lookup.MH.asType(mh, mh.type().changeParameterType(0, clazz));
    }

    @Override
    public GuardedInvocation findFastGetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        GuardedInvocation inv = super.findFastGetIndexMethod(clazz, desc, request);
        if (inv != null) {
            return inv;
        }
        return null;
    }

    @Override
    public GuardedInvocation findFastSetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        GuardedInvocation inv = super.findFastSetIndexMethod(clazz, desc, request);
        if (inv != null) {
            return inv;
        }
        return null;
    }
}

