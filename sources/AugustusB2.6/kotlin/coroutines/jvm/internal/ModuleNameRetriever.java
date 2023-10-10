// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import kotlin.jvm.internal.Intrinsics;

final class ModuleNameRetriever
{
    private static final Cache notOnJava9;
    private static Cache cache;
    public static final ModuleNameRetriever INSTANCE;
    
    public final String getModuleName(final BaseContinuationImpl continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Cache cache2;
        Cache buildCache;
        if ((buildCache = (cache2 = ModuleNameRetriever.cache)) == null) {
            cache2 = (buildCache = buildCache(continuation));
        }
        final Cache cache = buildCache;
        if (cache2 == ModuleNameRetriever.notOnJava9) {
            return null;
        }
        final Method getModuleMethod = cache.getModuleMethod;
        if (getModuleMethod != null) {
            final Object invoke = getModuleMethod.invoke(continuation.getClass(), new Object[0]);
            if (invoke != null) {
                final Object module = invoke;
                final Method getDescriptorMethod = cache.getDescriptorMethod;
                if (getDescriptorMethod != null) {
                    final Object invoke2 = getDescriptorMethod.invoke(module, new Object[0]);
                    if (invoke2 != null) {
                        final Object descriptor = invoke2;
                        final Method nameMethod = cache.nameMethod;
                        Object o = (nameMethod != null) ? nameMethod.invoke(descriptor, new Object[0]) : null;
                        if (!(o instanceof String)) {
                            o = null;
                        }
                        return (String)o;
                    }
                }
                return null;
            }
        }
        return null;
    }
    
    private static Cache buildCache(final BaseContinuationImpl continuation) {
        try {
            final Method getModuleMethod = Class.class.getDeclaredMethod("getModule", (Class<?>[])new Class[0]);
            final Method getDescriptorMethod = continuation.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", (Class<?>[])new Class[0]);
            final Method nameMethod = continuation.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", (Class<?>[])new Class[0]);
            return ModuleNameRetriever.cache = new Cache(getModuleMethod, getDescriptorMethod, nameMethod);
        }
        catch (Exception ex) {
            return ModuleNameRetriever.cache = ModuleNameRetriever.notOnJava9;
        }
    }
    
    private ModuleNameRetriever() {
    }
    
    static {
        INSTANCE = new ModuleNameRetriever();
        notOnJava9 = new Cache(null, null, null);
    }
    
    public static final class Cache
    {
        public final Method getModuleMethod;
        public final Method getDescriptorMethod;
        public final Method nameMethod;
        
        public Cache(final Method getModuleMethod, final Method getDescriptorMethod, final Method nameMethod) {
            this.getModuleMethod = getModuleMethod;
            this.getDescriptorMethod = getDescriptorMethod;
            this.nameMethod = nameMethod;
        }
        
        public static StackTraceElement getStackTraceElement(BaseContinuationImpl $this$getStackTraceElementImpl) {
            Intrinsics.checkParameterIsNotNull($this$getStackTraceElementImpl, "$this$getStackTraceElementImpl");
            final DebugMetadata debugMetadata2 = $this$getStackTraceElementImpl.getClass().getAnnotation(DebugMetadata.class);
            if (debugMetadata2 == null) {
                return null;
            }
            final DebugMetadata debugMetadata = debugMetadata2;
            final int v = debugMetadata.v();
            if (v > 1) {
                $this$getStackTraceElementImpl = (BaseContinuationImpl)("Debug metadata version mismatch. Expected: " + 1 + ", got " + v + ". Please update the Kotlin standard library.");
                throw new IllegalStateException($this$getStackTraceElementImpl.toString());
            }
            final int label;
            final int lineNumber = ((label = getLabel($this$getStackTraceElementImpl)) < 0) ? -1 : debugMetadata.l()[label];
            final String moduleName;
            final String moduleAndClass = ((moduleName = ModuleNameRetriever.INSTANCE.getModuleName($this$getStackTraceElementImpl)) == null) ? debugMetadata.c() : (moduleName + '/' + debugMetadata.c());
            return new StackTraceElement(moduleAndClass, debugMetadata.m(), debugMetadata.f(), lineNumber);
        }
        
        private static int getLabel(final BaseContinuationImpl $this$getLabel) {
            int n2;
            try {
                final Field field;
                final Field value = field = $this$getLabel.getClass().getDeclaredField("label");
                Intrinsics.checkExpressionValueIsNotNull(value, "field");
                value.setAccessible(true);
                Object value2;
                if (!((value2 = field.get($this$getLabel)) instanceof Integer)) {
                    value2 = null;
                }
                final Integer n = (Integer)value2;
                n2 = ((n != null) ? n : 0) - 1;
            }
            catch (Exception ex) {
                n2 = -1;
            }
            return n2;
        }
    }
}
