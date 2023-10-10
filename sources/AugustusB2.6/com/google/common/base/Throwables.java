// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.AbstractList;
import java.util.Arrays;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Method;
import javax.annotation.CheckForNull;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Throwables
{
    @GwtIncompatible
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
    @GwtIncompatible
    @VisibleForTesting
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    @CheckForNull
    @GwtIncompatible
    private static final Object jla;
    @CheckForNull
    @GwtIncompatible
    private static final Method getStackTraceElementMethod;
    @CheckForNull
    @GwtIncompatible
    private static final Method getStackTraceDepthMethod;
    
    private Throwables() {
    }
    
    @GwtIncompatible
    public static <X extends Throwable> void throwIfInstanceOf(final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        Preconditions.checkNotNull(throwable);
        if (declaredType.isInstance(throwable)) {
            throw declaredType.cast(throwable);
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfInstanceOf(@CheckForNull final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        if (throwable != null) {
            throwIfInstanceOf(throwable, (Class<Throwable>)declaredType);
        }
    }
    
    public static void throwIfUnchecked(final Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static void propagateIfPossible(@CheckForNull final Throwable throwable) {
        if (throwable != null) {
            throwIfUnchecked(throwable);
        }
    }
    
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfPossible(@CheckForNull final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        propagateIfInstanceOf(throwable, (Class<Throwable>)declaredType);
        propagateIfPossible(throwable);
    }
    
    @GwtIncompatible
    public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@CheckForNull final Throwable throwable, final Class<X1> declaredType1, final Class<X2> declaredType2) throws X1, X2, Throwable {
        Preconditions.checkNotNull(declaredType2);
        propagateIfInstanceOf(throwable, declaredType1);
        propagateIfPossible(throwable, declaredType2);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static RuntimeException propagate(final Throwable throwable) {
        throwIfUnchecked(throwable);
        throw new RuntimeException(throwable);
    }
    
    public static Throwable getRootCause(Throwable throwable) {
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
            if (throwable == slowPointer) {
                throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
            }
            if (advanceSlowPointer) {
                slowPointer = slowPointer.getCause();
            }
            advanceSlowPointer = !advanceSlowPointer;
        }
        return throwable;
    }
    
    @Beta
    public static List<Throwable> getCausalChain(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        final List<Throwable> causes = new ArrayList<Throwable>(4);
        causes.add(throwable);
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
            causes.add(throwable);
            if (throwable == slowPointer) {
                throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
            }
            if (advanceSlowPointer) {
                slowPointer = slowPointer.getCause();
            }
            advanceSlowPointer = !advanceSlowPointer;
        }
        return Collections.unmodifiableList((List<? extends Throwable>)causes);
    }
    
    @CheckForNull
    @Beta
    @GwtIncompatible
    public static <X extends Throwable> X getCauseAs(final Throwable throwable, final Class<X> expectedCauseType) {
        try {
            return expectedCauseType.cast(throwable.getCause());
        }
        catch (ClassCastException e) {
            e.initCause(throwable);
            throw e;
        }
    }
    
    @GwtIncompatible
    public static String getStackTraceAsString(final Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
    
    @Beta
    @GwtIncompatible
    public static List<StackTraceElement> lazyStackTrace(final Throwable throwable) {
        return lazyStackTraceIsLazy() ? jlaStackTrace(throwable) : Collections.unmodifiableList((List<? extends StackTraceElement>)Arrays.asList((T[])throwable.getStackTrace()));
    }
    
    @Beta
    @GwtIncompatible
    public static boolean lazyStackTraceIsLazy() {
        return Throwables.getStackTraceElementMethod != null && Throwables.getStackTraceDepthMethod != null;
    }
    
    @GwtIncompatible
    private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
        Preconditions.checkNotNull(t);
        return new AbstractList<StackTraceElement>() {
            @Override
            public StackTraceElement get(final int n) {
                return (StackTraceElement)invokeAccessibleNonThrowingMethod(Objects.requireNonNull(Throwables.getStackTraceElementMethod), Objects.requireNonNull(Throwables.jla), new Object[] { t, n });
            }
            
            @Override
            public int size() {
                return (int)invokeAccessibleNonThrowingMethod(Objects.requireNonNull(Throwables.getStackTraceDepthMethod), Objects.requireNonNull(Throwables.jla), new Object[] { t });
            }
        };
    }
    
    @GwtIncompatible
    private static Object invokeAccessibleNonThrowingMethod(final Method method, final Object receiver, final Object... params) {
        try {
            return method.invoke(receiver, params);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e2) {
            throw propagate(e2.getCause());
        }
    }
    
    @CheckForNull
    @GwtIncompatible
    private static Object getJLA() {
        try {
            final Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, null);
            final Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", (Class<?>[])new Class[0]);
            return langAccess.invoke(null, new Object[0]);
        }
        catch (ThreadDeath death) {
            throw death;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    @CheckForNull
    @GwtIncompatible
    private static Method getGetMethod() {
        return getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
    }
    
    @CheckForNull
    @GwtIncompatible
    private static Method getSizeMethod(final Object jla) {
        try {
            final Method getStackTraceDepth = getJlaMethod("getStackTraceDepth", Throwable.class);
            if (getStackTraceDepth == null) {
                return null;
            }
            try {
                getStackTraceDepth.invoke(jla, new Throwable());
                return getStackTraceDepth;
            }
            catch (IllegalAccessException e) {
                return null;
            }
        }
        catch (UnsupportedOperationException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (InvocationTargetException ex3) {}
    }
    
    @CheckForNull
    @GwtIncompatible
    private static Method getJlaMethod(final String name, final Class<?>... parameterTypes) throws ThreadDeath {
        try {
            return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(name, parameterTypes);
        }
        catch (ThreadDeath death) {
            throw death;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    static {
        jla = getJLA();
        getStackTraceElementMethod = ((Throwables.jla == null) ? null : getGetMethod());
        getStackTraceDepthMethod = ((Throwables.jla == null) ? null : getSizeMethod(Throwables.jla));
    }
}
