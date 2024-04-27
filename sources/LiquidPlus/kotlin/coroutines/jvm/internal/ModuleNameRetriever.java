/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c2\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever;", "", "()V", "cache", "Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "notOnJava9", "buildCache", "continuation", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getModuleName", "", "Cache", "kotlin-stdlib"})
final class ModuleNameRetriever {
    @NotNull
    public static final ModuleNameRetriever INSTANCE = new ModuleNameRetriever();
    @NotNull
    private static final Cache notOnJava9 = new Cache(null, null, null);
    @Nullable
    private static Cache cache;

    private ModuleNameRetriever() {
    }

    @Nullable
    public final String getModuleName(@NotNull BaseContinuationImpl continuation) {
        Object object;
        Cache cache;
        Intrinsics.checkNotNullParameter(continuation, "continuation");
        Cache cache2 = ModuleNameRetriever.cache;
        Cache cache3 = cache = cache2 == null ? this.buildCache(continuation) : cache2;
        if (cache == notOnJava9) {
            return null;
        }
        Method method = cache.getModuleMethod;
        Object object2 = object = method == null ? null : method.invoke(continuation.getClass(), new Object[0]);
        if (object == null) {
            return null;
        }
        Object module2 = object;
        Method method2 = cache.getDescriptorMethod;
        Method method3 = method = method2 == null ? null : method2.invoke(module2, new Object[0]);
        if (method == null) {
            return null;
        }
        Method descriptor = method;
        method2 = cache.nameMethod;
        method = method2 == null ? null : method2.invoke(descriptor, new Object[0]);
        return method instanceof String ? (String)((Object)method) : null;
    }

    private final Cache buildCache(BaseContinuationImpl continuation) {
        try {
            Cache cache;
            Method getModuleMethod = Class.class.getDeclaredMethod("getModule", new Class[0]);
            Class<?> methodClass = continuation.getClass().getClassLoader().loadClass("java.lang.Module");
            Method getDescriptorMethod = methodClass.getDeclaredMethod("getDescriptor", new Class[0]);
            Class<?> moduleDescriptorClass = continuation.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor");
            Method nameMethod = moduleDescriptorClass.getDeclaredMethod("name", new Class[0]);
            Cache it = cache = new Cache(getModuleMethod, getDescriptorMethod, nameMethod);
            boolean bl = false;
            ModuleNameRetriever.cache = it;
            return cache;
        }
        catch (Exception ignored) {
            Cache cache;
            Cache it = cache = notOnJava9;
            boolean bl = false;
            ModuleNameRetriever.cache = it;
            return cache;
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0006R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "", "getModuleMethod", "Ljava/lang/reflect/Method;", "getDescriptorMethod", "nameMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "kotlin-stdlib"})
    private static final class Cache {
        @JvmField
        @Nullable
        public final Method getModuleMethod;
        @JvmField
        @Nullable
        public final Method getDescriptorMethod;
        @JvmField
        @Nullable
        public final Method nameMethod;

        public Cache(@Nullable Method getModuleMethod, @Nullable Method getDescriptorMethod, @Nullable Method nameMethod) {
            this.getModuleMethod = getModuleMethod;
            this.getDescriptorMethod = getDescriptorMethod;
            this.nameMethod = nameMethod;
        }
    }
}

