/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;

public final class OptimisticReturnFilters {
    private static final MethodHandle[] ENSURE_INT;
    private static final MethodHandle[] ENSURE_NUMBER;
    private static final int VOID_TYPE_INDEX;
    private static final int BOOLEAN_TYPE_INDEX;
    private static final int CHAR_TYPE_INDEX;
    private static final int LONG_TYPE_INDEX;
    private static final int FLOAT_TYPE_INDEX;

    public static MethodHandle filterOptimisticReturnValue(MethodHandle mh, Class<?> expectedReturnType, int programPoint) {
        if (!UnwarrantedOptimismException.isValid(programPoint)) {
            return mh;
        }
        MethodType type = mh.type();
        TypeDescriptor.OfField actualReturnType = type.returnType();
        if (TypeUtilities.isConvertibleWithoutLoss(actualReturnType, expectedReturnType)) {
            return mh;
        }
        MethodHandle guard = OptimisticReturnFilters.getOptimisticTypeGuard(expectedReturnType, actualReturnType);
        return guard == null ? mh : Lookup.MH.filterReturnValue(mh, Lookup.MH.insertArguments(guard, guard.type().parameterCount() - 1, programPoint));
    }

    public static GuardedInvocation filterOptimisticReturnValue(GuardedInvocation inv, CallSiteDescriptor desc) {
        if (!NashornCallSiteDescriptor.isOptimistic(desc)) {
            return inv;
        }
        return inv.replaceMethods(OptimisticReturnFilters.filterOptimisticReturnValue(inv.getInvocation(), desc.getMethodType().returnType(), NashornCallSiteDescriptor.getProgramPoint(desc)), inv.getGuard());
    }

    private static MethodHandle getOptimisticTypeGuard(Class<?> actual, Class<?> provable) {
        MethodHandle guard;
        int provableTypeIndex = OptimisticReturnFilters.getProvableTypeIndex(provable);
        if (actual == Integer.TYPE) {
            guard = ENSURE_INT[provableTypeIndex];
        } else if (actual == Double.TYPE) {
            guard = ENSURE_NUMBER[provableTypeIndex];
        } else {
            guard = null;
            assert (!actual.isPrimitive()) : actual + ", " + provable;
        }
        if (guard != null && !provable.isPrimitive()) {
            return guard.asType(guard.type().changeParameterType(0, provable));
        }
        return guard;
    }

    private static int getProvableTypeIndex(Class<?> provable) {
        int accTypeIndex = JSType.getAccessorTypeIndex(provable);
        if (accTypeIndex != -1) {
            return accTypeIndex;
        }
        if (provable == Boolean.TYPE) {
            return BOOLEAN_TYPE_INDEX;
        }
        if (provable == Void.TYPE) {
            return VOID_TYPE_INDEX;
        }
        if (provable == Byte.TYPE || provable == Short.TYPE) {
            return 0;
        }
        if (provable == Character.TYPE) {
            return CHAR_TYPE_INDEX;
        }
        if (provable == Long.TYPE) {
            return LONG_TYPE_INDEX;
        }
        if (provable == Float.TYPE) {
            return FLOAT_TYPE_INDEX;
        }
        throw new AssertionError((Object)provable.getName());
    }

    private static int ensureInt(long arg, int programPoint) {
        if (JSType.isRepresentableAsInt(arg)) {
            return (int)arg;
        }
        throw UnwarrantedOptimismException.createNarrowest(arg, programPoint);
    }

    private static int ensureInt(double arg, int programPoint) {
        if (JSType.isStrictlyRepresentableAsInt(arg)) {
            return (int)arg;
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.NUMBER);
    }

    public static int ensureInt(Object arg, int programPoint) {
        double d;
        if (OptimisticReturnFilters.isPrimitiveNumberWrapper(arg) && JSType.isStrictlyRepresentableAsInt(d = ((Number)arg).doubleValue())) {
            return (int)d;
        }
        throw UnwarrantedOptimismException.createNarrowest(arg, programPoint);
    }

    private static boolean isPrimitiveNumberWrapper(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> c = obj.getClass();
        return c == Integer.class || c == Double.class || c == Long.class || c == Float.class || c == Short.class || c == Byte.class;
    }

    private static int ensureInt(boolean arg, int programPoint) {
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }

    private static int ensureInt(char arg, int programPoint) {
        throw new UnwarrantedOptimismException(Character.valueOf(arg), programPoint, Type.OBJECT);
    }

    private static int ensureInt(int programPoint) {
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint, Type.OBJECT);
    }

    private static double ensureNumber(long arg, int programPoint) {
        if (JSType.isRepresentableAsDouble(arg)) {
            return arg;
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }

    public static double ensureNumber(Object arg, int programPoint) {
        if (OptimisticReturnFilters.isPrimitiveNumberWrapper(arg) && (arg.getClass() != Long.class || JSType.isRepresentableAsDouble((Long)arg))) {
            return ((Number)arg).doubleValue();
        }
        throw new UnwarrantedOptimismException(arg, programPoint, Type.OBJECT);
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), OptimisticReturnFilters.class, name, Lookup.MH.type(rtype, types));
    }

    static {
        MethodHandle INT_DOUBLE = OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Double.TYPE, Integer.TYPE);
        ENSURE_INT = new MethodHandle[]{null, INT_DOUBLE, OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Object.class, Integer.TYPE), OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Integer.TYPE), OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Boolean.TYPE, Integer.TYPE), OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Character.TYPE, Integer.TYPE), OptimisticReturnFilters.findOwnMH("ensureInt", Integer.TYPE, Long.TYPE, Integer.TYPE), INT_DOUBLE.asType(INT_DOUBLE.type().changeParameterType(0, Float.TYPE))};
        VOID_TYPE_INDEX = ENSURE_INT.length - 5;
        BOOLEAN_TYPE_INDEX = ENSURE_INT.length - 4;
        CHAR_TYPE_INDEX = ENSURE_INT.length - 3;
        LONG_TYPE_INDEX = ENSURE_INT.length - 2;
        FLOAT_TYPE_INDEX = ENSURE_INT.length - 1;
        ENSURE_NUMBER = new MethodHandle[]{null, null, OptimisticReturnFilters.findOwnMH("ensureNumber", Double.TYPE, Object.class, Integer.TYPE), ENSURE_INT[VOID_TYPE_INDEX].asType(ENSURE_INT[VOID_TYPE_INDEX].type().changeReturnType(Double.TYPE)), ENSURE_INT[BOOLEAN_TYPE_INDEX].asType(ENSURE_INT[BOOLEAN_TYPE_INDEX].type().changeReturnType(Double.TYPE)), ENSURE_INT[CHAR_TYPE_INDEX].asType(ENSURE_INT[CHAR_TYPE_INDEX].type().changeReturnType(Double.TYPE)), OptimisticReturnFilters.findOwnMH("ensureNumber", Double.TYPE, Long.TYPE, Integer.TYPE), null};
    }
}

