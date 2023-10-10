// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KClass;
import kotlin.ExceptionsKt__ExceptionsKt;

public final class Reflection
{
    private static final ExceptionsKt__ExceptionsKt factory$2fb20bc2;
    
    public static KClass getOrCreateKotlinClass(Class javaClass) {
        javaClass = (Class<?>)javaClass;
        return new ClassReference(javaClass);
    }
    
    public static String renderLambdaToString(final Lambda lambda) {
        return Reflection.factory$2fb20bc2.renderLambdaToString(lambda);
    }
    
    public static String renderLambdaToString(final FunctionBase lambda) {
        return ExceptionsKt__ExceptionsKt.renderLambdaToString(lambda);
    }
    
    static {
        ExceptionsKt__ExceptionsKt impl;
        try {
            impl = (ExceptionsKt__ExceptionsKt)Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
        }
        catch (ClassCastException ex) {
            impl = null;
        }
        catch (ClassNotFoundException ex2) {
            impl = null;
        }
        catch (InstantiationException ex3) {
            impl = null;
        }
        catch (IllegalAccessException ex4) {
            impl = null;
        }
        factory$2fb20bc2 = ((impl != null) ? impl : new ExceptionsKt__ExceptionsKt());
    }
}
