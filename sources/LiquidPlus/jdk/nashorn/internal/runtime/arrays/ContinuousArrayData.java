/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name="arrays")
public abstract class ContinuousArrayData
extends ArrayData {
    protected static final MethodHandle FAST_ACCESS_GUARD = Lookup.MH.dropArguments(CompilerConstants.staticCall(MethodHandles.lookup(), ContinuousArrayData.class, "guard", Boolean.TYPE, Class.class, ScriptObject.class).methodHandle(), 2, Integer.TYPE);

    protected ContinuousArrayData(long length) {
        super(length);
    }

    public final boolean hasRoomFor(int index) {
        return this.has(index) || (long)index == this.length() && this.ensure(index) == this;
    }

    public boolean isEmpty() {
        return this.length() == 0L;
    }

    public abstract MethodHandle getElementGetter(Class<?> var1, int var2);

    public abstract MethodHandle getElementSetter(Class<?> var1);

    protected final int throwHas(int index) {
        if (!this.has(index)) {
            throw new ClassCastException();
        }
        return index;
    }

    @Override
    public abstract ContinuousArrayData copy();

    public abstract Class<?> getElementType();

    @Override
    public Type getOptimisticType() {
        return Type.typeFor(this.getElementType());
    }

    public abstract Class<?> getBoxedElementType();

    public ContinuousArrayData widest(ContinuousArrayData otherData) {
        Class<?> elementType = this.getElementType();
        return Type.widest(elementType, otherData.getElementType()) == elementType ? this : otherData;
    }

    protected final MethodHandle getContinuousElementGetter(MethodHandle get, Class<?> returnType, int programPoint) {
        return this.getContinuousElementGetter(this.getClass(), get, returnType, programPoint);
    }

    protected final MethodHandle getContinuousElementSetter(MethodHandle set, Class<?> returnType) {
        return this.getContinuousElementSetter(this.getClass(), set, returnType);
    }

    protected MethodHandle getContinuousElementGetter(Class<? extends ContinuousArrayData> clazz, MethodHandle getHas, Class<?> returnType, int programPoint) {
        boolean isOptimistic = UnwarrantedOptimismException.isValid(programPoint);
        int fti = JSType.getAccessorTypeIndex(getHas.type().returnType());
        int ti = JSType.getAccessorTypeIndex(returnType);
        MethodHandle mh = getHas;
        if (isOptimistic && ti < fti) {
            mh = Lookup.MH.insertArguments(ArrayData.THROW_UNWARRANTED.methodHandle(), 1, programPoint);
        }
        mh = Lookup.MH.asType(mh, mh.type().changeReturnType(returnType).changeParameterType(0, clazz));
        if (!isOptimistic) {
            return Lookup.filterReturnType(mh, returnType);
        }
        return mh;
    }

    protected MethodHandle getContinuousElementSetter(Class<? extends ContinuousArrayData> clazz, MethodHandle setHas, Class<?> elementType) {
        return Lookup.MH.asType(setHas, setHas.type().changeParameterType(2, elementType).changeParameterType(0, clazz));
    }

    private static final boolean guard(Class<? extends ContinuousArrayData> clazz, ScriptObject sobj) {
        return sobj != null && sobj.getArray().getClass() == clazz;
    }

    @Override
    public GuardedInvocation findFastGetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        Object[] args2;
        int index;
        MethodType callType = desc.getMethodType();
        Class<?> indexType = callType.parameterType(1);
        Class<?> returnType = callType.returnType();
        if (ContinuousArrayData.class.isAssignableFrom(clazz) && indexType == Integer.TYPE && this.has(index = ((Integer)(args2 = request.getArguments())[args2.length - 1]).intValue())) {
            MethodHandle getArray = ScriptObject.GET_ARRAY.methodHandle();
            int programPoint = NashornCallSiteDescriptor.isOptimistic(desc) ? NashornCallSiteDescriptor.getProgramPoint(desc) : -1;
            MethodHandle getElement = this.getElementGetter(returnType, programPoint);
            if (getElement != null) {
                getElement = Lookup.MH.filterArguments(getElement, 0, Lookup.MH.asType(getArray, getArray.type().changeReturnType(clazz)));
                MethodHandle guard = Lookup.MH.insertArguments(FAST_ACCESS_GUARD, 0, clazz);
                return new GuardedInvocation(getElement, guard, (SwitchPoint)null, ClassCastException.class);
            }
        }
        return null;
    }

    @Override
    public GuardedInvocation findFastSetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        MethodHandle setElement;
        Object[] args2;
        int index;
        MethodType callType = desc.getMethodType();
        Class<?> indexType = callType.parameterType(1);
        Class<?> elementType = callType.parameterType(2);
        if (ContinuousArrayData.class.isAssignableFrom(clazz) && indexType == Integer.TYPE && this.hasRoomFor(index = ((Integer)(args2 = request.getArguments())[args2.length - 2]).intValue()) && (setElement = this.getElementSetter(elementType)) != null) {
            MethodHandle getArray = ScriptObject.GET_ARRAY.methodHandle();
            getArray = Lookup.MH.asType(getArray, getArray.type().changeReturnType(this.getClass()));
            setElement = Lookup.MH.filterArguments(setElement, 0, getArray);
            MethodHandle guard = Lookup.MH.insertArguments(FAST_ACCESS_GUARD, 0, clazz);
            return new GuardedInvocation(setElement, guard, (SwitchPoint)null, ClassCastException.class);
        }
        return null;
    }

    public double fastPush(int arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public double fastPush(long arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public double fastPush(double arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public double fastPush(Object arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public int fastPopInt() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public double fastPopDouble() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public Object fastPopObject() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }

    public ContinuousArrayData fastConcat(ContinuousArrayData otherData) {
        throw new ClassCastException(String.valueOf(this.getClass()) + " != " + String.valueOf(otherData.getClass()));
    }
}

