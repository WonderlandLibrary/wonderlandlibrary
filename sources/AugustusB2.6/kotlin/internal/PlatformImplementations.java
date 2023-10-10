// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.internal;

import java.lang.reflect.Method;
import kotlin.jvm.internal.Intrinsics;

public class PlatformImplementations
{
    public static void addSuppressed(final Throwable cause, final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        final Method method = ReflectAddSuppressedMethod.method;
        if (method != null) {
            method.invoke(cause, exception);
        }
    }
    
    private static final class ReflectAddSuppressedMethod
    {
        public static final Method method;
        
        static {
            new ReflectAddSuppressedMethod();
            final Class throwableClass;
            final Method[] methods = (throwableClass = Throwable.class).getMethods();
            Intrinsics.checkExpressionValueIsNotNull(methods, "throwableClass.methods");
            final Method[] array = methods;
            while (true) {
                for (int length = methods.length, i = 0; i < length; ++i) {
                    final Method method2;
                    final Method it;
                    final Method value = it = (method2 = array[i]);
                    Intrinsics.checkExpressionValueIsNotNull(value, "it");
                    boolean b = false;
                    Label_0111: {
                        if (Intrinsics.areEqual(value.getName(), "addSuppressed")) {
                            final Class<?>[] parameterTypes = it.getParameterTypes();
                            Intrinsics.checkExpressionValueIsNotNull(parameterTypes, "it.parameterTypes");
                            final Class<?>[] array2 = parameterTypes;
                            Intrinsics.checkParameterIsNotNull(parameterTypes, "$this$singleOrNull");
                            if (Intrinsics.areEqual((array2.length == 1) ? array2[0] : null, throwableClass)) {
                                b = true;
                                break Label_0111;
                            }
                        }
                        b = false;
                    }
                    if (b) {
                        final Method method3 = method2;
                        method = method3;
                        return;
                    }
                }
                final Method method3 = null;
                continue;
            }
        }
    }
}
