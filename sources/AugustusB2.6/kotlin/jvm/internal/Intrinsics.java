// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.util.Arrays;
import kotlin.UninitializedPropertyAccessException;
import kotlin.KotlinNullPointerException;

public class Intrinsics
{
    private Intrinsics() {
    }
    
    public static void throwNpe() {
        throw sanitizeStackTrace(new KotlinNullPointerException());
    }
    
    public static void throwUninitializedPropertyAccessException(String propertyName) {
        propertyName = "lateinit property " + propertyName + " has not been initialized";
        throw sanitizeStackTrace(new UninitializedPropertyAccessException(propertyName));
    }
    
    public static void checkExpressionValueIsNotNull(final Object value, final String expression) {
        if (value == null) {
            throw sanitizeStackTrace(new IllegalStateException(expression + " must not be null"));
        }
    }
    
    public static void checkParameterIsNotNull(final Object value, String paramName) {
        if (value == null) {
            final StackTraceElement stackTraceElement;
            final String className = (stackTraceElement = Thread.currentThread().getStackTrace()[3]).getClassName();
            paramName = stackTraceElement.getMethodName();
            throw sanitizeStackTrace(new IllegalArgumentException("Parameter specified as non-null is null: method " + className + "." + paramName + ", parameter " + paramName));
        }
    }
    
    public static boolean areEqual(final Object first, final Object second) {
        if (first == null) {
            return second == null;
        }
        return first.equals(second);
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T throwable) {
        return sanitizeStackTrace(throwable, Intrinsics.class.getName());
    }
    
    static <T extends Throwable> T sanitizeStackTrace(final T throwable, final String classNameToDrop) {
        final StackTraceElement[] stackTrace;
        final int size = (stackTrace = throwable.getStackTrace()).length;
        int lastIntrinsic = -1;
        for (int i = 0; i < size; ++i) {
            if (classNameToDrop.equals(stackTrace[i].getClassName())) {
                lastIntrinsic = i;
            }
        }
        final StackTraceElement[] newStackTrace = Arrays.copyOfRange(stackTrace, lastIntrinsic + 1, size);
        throwable.setStackTrace(newStackTrace);
        return throwable;
    }
}
