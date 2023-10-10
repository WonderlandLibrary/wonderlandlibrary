// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.graphics;

import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlinx.coroutines.DispatchedKt;
import kotlinx.coroutines.DispatchedContinuation;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CompletedExceptionally;
import java.io.Serializable;
import kotlin.TypeCastException;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.reflect.KClass;
import kotlin.Pair;
import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
import com.maltaisn.msdfgdx.gen.ParameterException;

public class Pixmap
{
    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;
    private final float x3;
    private final float y3;
    
    @Override
    public String toString() {
        return "(" + this.x1 + ", " + this.y1 + "; " + this.x2 + ", " + this.y2 + "); " + this.x3 + ", " + this.y3;
    }
    
    public Pixmap(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }
    
    public static Void paramError(final Object message) {
        throw new ParameterException(String.valueOf(message));
    }
    
    public static Object createFailure(final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        return new Result.Failure(exception);
    }
    
    public static void throwOnFailure(final Object $this$throwOnFailure) {
        if ($this$throwOnFailure instanceof Result.Failure) {
            throw ((Result.Failure)$this$throwOnFailure).exception;
        }
    }
    
    public static <A, B> Pair<A, B> to(final A $this$to, final B that) {
        return new Pair<A, B>($this$to, that);
    }
    
    public static Integer boxInt(final int primitive) {
        return primitive;
    }
    
    public static Float boxFloat(final float primitive) {
        return primitive;
    }
    
    public static Character boxChar(final char primitive) {
        return primitive;
    }
    
    private static int mod(int a, final int b) {
        if ((a %= b) >= 0) {
            return a;
        }
        return a + b;
    }
    
    private static int differenceModulo(final int a, final int b, final int c) {
        return mod(mod(a, c) - mod(b, c), c);
    }
    
    public static int getProgressionLastElement(final int start, final int end, final int step) {
        if (step > 0) {
            if (start >= end) {
                return end;
            }
            return end - differenceModulo(end, start, step);
        }
        else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (start <= end) {
                return end;
            }
            return end + differenceModulo(start, end, -step);
        }
    }
    
    public static <T> Class<T> getJavaObjectType(final KClass<T> $this$javaObjectType) {
        Intrinsics.checkParameterIsNotNull($this$javaObjectType, "$this$javaObjectType");
        final Class thisJClass;
        if (!(thisJClass = ((ClassBasedDeclarationContainer)$this$javaObjectType).getJClass()).isPrimitive()) {
            final Class clazz = thisJClass;
            if (clazz == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)clazz;
        }
        else {
            final String name = thisJClass.getName();
            Serializable s3 = null;
            Serializable s2 = null;
            Label_0294: {
                if (name != null) {
                    final String s = name;
                    switch (name.hashCode()) {
                        case 64711720: {
                            if (s.equals("boolean")) {
                                s2 = (s3 = Boolean.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3625364: {
                            if (s.equals("void")) {
                                s2 = (s3 = Void.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3039496: {
                            if (s.equals("byte")) {
                                s2 = (s3 = Byte.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case -1325958191: {
                            if (s.equals("double")) {
                                s2 = (s3 = Double.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3052374: {
                            if (s.equals("char")) {
                                s2 = (s3 = Character.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 109413500: {
                            if (s.equals("short")) {
                                s2 = (s3 = Short.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 97526364: {
                            if (s.equals("float")) {
                                s2 = (s3 = Float.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 104431: {
                            if (s.equals("int")) {
                                s2 = (s3 = Integer.class);
                                break Label_0294;
                            }
                            break;
                        }
                        case 3327612: {
                            if (s.equals("long")) {
                                s2 = (s3 = Long.class);
                                break Label_0294;
                            }
                            break;
                        }
                    }
                }
                s2 = (s3 = thisJClass);
            }
            if (s3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)s2;
        }
    }
    
    public static <T> Object toState(Object $this$toState) {
        if (Result.isSuccess-impl($this$toState)) {
            throwOnFailure($this$toState = $this$toState);
            return $this$toState;
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl($this$toState);
        if (exceptionOrNull-impl == null) {
            Intrinsics.throwNpe();
        }
        return new CompletedExceptionally(exceptionOrNull-impl, false, 2);
    }
    
    public static String getHexAddress(final Object $this$hexAddress) {
        Intrinsics.checkParameterIsNotNull($this$hexAddress, "$this$hexAddress");
        final String hexString = Integer.toHexString(System.identityHashCode($this$hexAddress));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }
    
    public static String toDebugString(final Continuation<?> $this$toDebugString) {
        Intrinsics.checkParameterIsNotNull($this$toDebugString, "$this$toDebugString");
        if ($this$toDebugString instanceof DispatchedContinuation) {
            return $this$toDebugString.toString();
        }
        Object o;
        try {
            final Result.Companion companion = Result.Companion;
            final Continuation $this$runCatching = $this$toDebugString;
            o = Result.constructor-impl(new StringBuilder().append($this$runCatching).append('@').append(getHexAddress($this$runCatching)).toString());
        }
        catch (Throwable exception) {
            final Result.Companion companion2 = Result.Companion;
            o = Result.constructor-impl(createFailure(exception));
        }
        final String s;
        return (Result.exceptionOrNull-impl(s = (String)o) == null) ? s : ($this$toDebugString.getClass().getName() + '@' + getHexAddress($this$toDebugString));
    }
    
    public static String getClassSimpleName(final Object $this$classSimpleName) {
        Intrinsics.checkParameterIsNotNull($this$classSimpleName, "$this$classSimpleName");
        final String simpleName = $this$classSimpleName.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
    
    public static boolean isCancellableMode(final int $this$isCancellableMode) {
        return $this$isCancellableMode == 1;
    }
    
    public static <T> void resumeMode(Continuation<? super T> $this$resumeMode, T value, final int mode) {
        Intrinsics.checkParameterIsNotNull($this$resumeMode, "$this$resumeMode");
        switch (mode) {
            case 0: {
                $this$resumeMode = $this$resumeMode;
                final Result.Companion companion = Result.Companion;
                $this$resumeMode.resumeWith(Result.constructor-impl(value));
            }
            case 1: {
                DispatchedKt.resumeCancellable($this$resumeMode, value);
            }
            case 2: {
                DispatchedKt.resumeDirect($this$resumeMode, value);
            }
            case 3: {
                final DispatchedContinuation this_$iv;
                (this_$iv = $this$resumeMode).getContext();
                final Object countOrElement$iv$iv = this_$iv.countOrElement;
                final Object oldValue$iv$iv = ThreadContextKt.updateThreadContext((CoroutineContext)mode, countOrElement$iv$iv);
                try {
                    final Continuation<T> continuation = this_$iv.continuation;
                    value = value;
                    final Continuation<T> continuation2 = continuation;
                    final Result.Companion companion2 = Result.Companion;
                    continuation2.resumeWith(Result.constructor-impl(value));
                    final Unit instance = Unit.INSTANCE;
                }
                finally {
                    ThreadContextKt.restoreThreadContext((CoroutineContext)mode, oldValue$iv$iv);
                }
            }
            case 4: {}
            default: {
                throw new IllegalStateException(("Invalid mode " + mode).toString());
            }
        }
    }
    
    public static <T> void resumeWithExceptionMode(Continuation<? super T> $this$resumeWithExceptionMode, Throwable exception, final int mode) {
        Intrinsics.checkParameterIsNotNull($this$resumeWithExceptionMode, "$this$resumeWithExceptionMode");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        switch (mode) {
            case 0: {
                $this$resumeWithExceptionMode = $this$resumeWithExceptionMode;
                final Result.Companion companion = Result.Companion;
                $this$resumeWithExceptionMode.resumeWith(Result.constructor-impl(createFailure(exception)));
            }
            case 1: {
                DispatchedKt.resumeCancellableWithException($this$resumeWithExceptionMode, exception);
            }
            case 2: {
                DispatchedKt.resumeDirectWithException($this$resumeWithExceptionMode, exception);
            }
            case 3: {
                final DispatchedContinuation this_$iv;
                (this_$iv = $this$resumeWithExceptionMode).getContext();
                final Object countOrElement$iv$iv = this_$iv.countOrElement;
                final Object oldValue$iv$iv = ThreadContextKt.updateThreadContext((CoroutineContext)mode, countOrElement$iv$iv);
                try {
                    final Continuation<T> continuation = this_$iv.continuation;
                    exception = exception;
                    final Result.Companion companion2 = Result.Companion;
                    $this$resumeWithExceptionMode.resumeWith(Result.constructor-impl(createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, $this$resumeWithExceptionMode))));
                    final Unit instance = Unit.INSTANCE;
                }
                finally {
                    ThreadContextKt.restoreThreadContext((CoroutineContext)mode, oldValue$iv$iv);
                }
            }
            case 4: {}
            default: {
                throw new IllegalStateException(("Invalid mode " + mode).toString());
            }
        }
    }
    
    public enum Format
    {
        Alpha, 
        Intensity, 
        LuminanceAlpha, 
        RGB565, 
        RGBA4444, 
        RGB888, 
        RGBA8888;
    }
}
