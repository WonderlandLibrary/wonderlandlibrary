/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.TypeUtilities;

public interface LinkerServices {
    public MethodHandle asType(MethodHandle var1, MethodType var2);

    public MethodHandle asTypeLosslessReturn(MethodHandle var1, MethodType var2);

    public MethodHandle getTypeConverter(Class<?> var1, Class<?> var2);

    public boolean canConvert(Class<?> var1, Class<?> var2);

    public GuardedInvocation getGuardedInvocation(LinkRequest var1) throws Exception;

    public ConversionComparator.Comparison compareConversion(Class<?> var1, Class<?> var2, Class<?> var3);

    public MethodHandle filterInternalObjects(MethodHandle var1);

    public static class Implementation {
        public static MethodHandle asTypeLosslessReturn(LinkerServices linkerServices, MethodHandle handle, MethodType fromType) {
            TypeDescriptor.OfField handleReturnType = handle.type().returnType();
            return linkerServices.asType(handle, TypeUtilities.isConvertibleWithoutLoss(handleReturnType, fromType.returnType()) ? fromType : fromType.changeReturnType((Class<?>)handleReturnType));
        }
    }
}

