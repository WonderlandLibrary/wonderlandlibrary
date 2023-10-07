/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.lookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Method;
import java.util.List;

public interface MethodHandleFunctionality {
    public MethodHandle filterArguments(MethodHandle var1, int var2, MethodHandle ... var3);

    public MethodHandle filterReturnValue(MethodHandle var1, MethodHandle var2);

    public MethodHandle guardWithTest(MethodHandle var1, MethodHandle var2, MethodHandle var3);

    public MethodHandle insertArguments(MethodHandle var1, int var2, Object ... var3);

    public MethodHandle dropArguments(MethodHandle var1, int var2, Class<?> ... var3);

    public MethodHandle dropArguments(MethodHandle var1, int var2, List<Class<?>> var3);

    public MethodHandle foldArguments(MethodHandle var1, MethodHandle var2);

    public MethodHandle explicitCastArguments(MethodHandle var1, MethodType var2);

    public MethodHandle arrayElementGetter(Class<?> var1);

    public MethodHandle arrayElementSetter(Class<?> var1);

    public MethodHandle throwException(Class<?> var1, Class<? extends Throwable> var2);

    public MethodHandle catchException(MethodHandle var1, Class<? extends Throwable> var2, MethodHandle var3);

    public MethodHandle constant(Class<?> var1, Object var2);

    public MethodHandle identity(Class<?> var1);

    public MethodHandle asType(MethodHandle var1, MethodType var2);

    public MethodHandle asCollector(MethodHandle var1, Class<?> var2, int var3);

    public MethodHandle asSpreader(MethodHandle var1, Class<?> var2, int var3);

    public MethodHandle bindTo(MethodHandle var1, Object var2);

    public MethodHandle getter(MethodHandles.Lookup var1, Class<?> var2, String var3, Class<?> var4);

    public MethodHandle staticGetter(MethodHandles.Lookup var1, Class<?> var2, String var3, Class<?> var4);

    public MethodHandle setter(MethodHandles.Lookup var1, Class<?> var2, String var3, Class<?> var4);

    public MethodHandle staticSetter(MethodHandles.Lookup var1, Class<?> var2, String var3, Class<?> var4);

    public MethodHandle find(Method var1);

    public MethodHandle findStatic(MethodHandles.Lookup var1, Class<?> var2, String var3, MethodType var4);

    public MethodHandle findVirtual(MethodHandles.Lookup var1, Class<?> var2, String var3, MethodType var4);

    public MethodHandle findSpecial(MethodHandles.Lookup var1, Class<?> var2, String var3, MethodType var4, Class<?> var5);

    public SwitchPoint createSwitchPoint();

    public MethodHandle guardWithTest(SwitchPoint var1, MethodHandle var2, MethodHandle var3);

    public MethodType type(Class<?> var1, Class<?> ... var2);
}

