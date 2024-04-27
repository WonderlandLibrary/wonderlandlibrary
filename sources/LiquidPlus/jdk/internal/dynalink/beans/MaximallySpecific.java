/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.beans.SingleDynamicMethod;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.TypeUtilities;

class MaximallySpecific {
    private static final MethodTypeGetter<MethodHandle> METHOD_HANDLE_TYPE_GETTER = new MethodTypeGetter<MethodHandle>(){

        @Override
        MethodType getMethodType(MethodHandle t) {
            return t.type();
        }
    };
    private static final MethodTypeGetter<SingleDynamicMethod> DYNAMIC_METHOD_TYPE_GETTER = new MethodTypeGetter<SingleDynamicMethod>(){

        @Override
        MethodType getMethodType(SingleDynamicMethod t) {
            return t.getMethodType();
        }
    };

    MaximallySpecific() {
    }

    static List<SingleDynamicMethod> getMaximallySpecificMethods(List<SingleDynamicMethod> methods, boolean varArgs) {
        return MaximallySpecific.getMaximallySpecificSingleDynamicMethods(methods, varArgs, null, null);
    }

    static List<MethodHandle> getMaximallySpecificMethodHandles(List<MethodHandle> methods, boolean varArgs, Class<?>[] argTypes, LinkerServices ls) {
        return MaximallySpecific.getMaximallySpecificMethods(methods, varArgs, argTypes, ls, METHOD_HANDLE_TYPE_GETTER);
    }

    static List<SingleDynamicMethod> getMaximallySpecificSingleDynamicMethods(List<SingleDynamicMethod> methods, boolean varArgs, Class<?>[] argTypes, LinkerServices ls) {
        return MaximallySpecific.getMaximallySpecificMethods(methods, varArgs, argTypes, ls, DYNAMIC_METHOD_TYPE_GETTER);
    }

    private static <T> List<T> getMaximallySpecificMethods(List<T> methods, boolean varArgs, Class<?>[] argTypes, LinkerServices ls, MethodTypeGetter<T> methodTypeGetter) {
        if (methods.size() < 2) {
            return methods;
        }
        LinkedList<T> maximals = new LinkedList<T>();
        for (T m : methods) {
            MethodType methodType = methodTypeGetter.getMethodType(m);
            boolean lessSpecific = false;
            Iterator maximal = maximals.iterator();
            block6: while (maximal.hasNext()) {
                Object max = maximal.next();
                switch (MaximallySpecific.isMoreSpecific(methodType, methodTypeGetter.getMethodType(max), varArgs, argTypes, ls)) {
                    case TYPE_1_BETTER: {
                        maximal.remove();
                        continue block6;
                    }
                    case TYPE_2_BETTER: {
                        lessSpecific = true;
                        continue block6;
                    }
                    case INDETERMINATE: {
                        continue block6;
                    }
                }
                throw new AssertionError();
            }
            if (lessSpecific) continue;
            maximals.addLast(m);
        }
        return maximals;
    }

    private static ConversionComparator.Comparison isMoreSpecific(MethodType t1, MethodType t2, boolean varArgs, Class<?>[] argTypes, LinkerServices ls) {
        int pc1 = t1.parameterCount();
        int pc2 = t2.parameterCount();
        assert (varArgs || pc1 == pc2 && (argTypes == null || argTypes.length == pc1));
        assert (argTypes == null == (ls == null));
        int maxPc = Math.max(Math.max(pc1, pc2), argTypes == null ? 0 : argTypes.length);
        boolean t1MoreSpecific = false;
        boolean t2MoreSpecific = false;
        for (int i = 1; i < maxPc; ++i) {
            Class<?> c2;
            Class<?> c1 = MaximallySpecific.getParameterClass(t1, pc1, i, varArgs);
            if (c1 == (c2 = MaximallySpecific.getParameterClass(t2, pc2, i, varArgs))) continue;
            ConversionComparator.Comparison cmp = MaximallySpecific.compare(c1, c2, argTypes, i, ls);
            if (cmp == ConversionComparator.Comparison.TYPE_1_BETTER && !t1MoreSpecific) {
                t1MoreSpecific = true;
                if (t2MoreSpecific) {
                    return ConversionComparator.Comparison.INDETERMINATE;
                }
            }
            if (cmp != ConversionComparator.Comparison.TYPE_2_BETTER || t2MoreSpecific) continue;
            t2MoreSpecific = true;
            if (!t1MoreSpecific) continue;
            return ConversionComparator.Comparison.INDETERMINATE;
        }
        if (t1MoreSpecific) {
            return ConversionComparator.Comparison.TYPE_1_BETTER;
        }
        if (t2MoreSpecific) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }

    private static ConversionComparator.Comparison compare(Class<?> c1, Class<?> c2, Class<?>[] argTypes, int i, LinkerServices cmp) {
        ConversionComparator.Comparison c;
        if (cmp != null && (c = cmp.compareConversion(argTypes[i], c1, c2)) != ConversionComparator.Comparison.INDETERMINATE) {
            return c;
        }
        if (TypeUtilities.isSubtype(c1, c2)) {
            return ConversionComparator.Comparison.TYPE_1_BETTER;
        }
        if (TypeUtilities.isSubtype(c2, c1)) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }

    private static Class<?> getParameterClass(MethodType t, int l, int i, boolean varArgs) {
        return varArgs && i >= l - 1 ? t.parameterType(l - 1).getComponentType() : t.parameterType(i);
    }

    private static abstract class MethodTypeGetter<T> {
        private MethodTypeGetter() {
        }

        abstract MethodType getMethodType(T var1);
    }
}

