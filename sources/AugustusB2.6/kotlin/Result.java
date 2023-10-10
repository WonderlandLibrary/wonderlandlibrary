// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import java.io.Serializable;

public final class Result<T> implements Serializable
{
    private final Object value;
    public static final Companion Companion;
    
    @Override
    public final String toString() {
        final Object value;
        if ((value = this.value) instanceof Failure) {
            return value.toString();
        }
        return "Success(" + value + ')';
    }
    
    public static final boolean isSuccess-impl(final Object $this) {
        return !($this instanceof Failure);
    }
    
    public static final boolean isFailure-impl(final Object $this) {
        return $this instanceof Failure;
    }
    
    public static final Throwable exceptionOrNull-impl(final Object $this) {
        if ($this instanceof Failure) {
            return ((Failure)$this).exception;
        }
        return null;
    }
    
    public static Object constructor-impl(final Object value) {
        return value;
    }
    
    static {
        Companion = new Companion((byte)0);
    }
    
    @Override
    public final int hashCode() {
        final Object value = this.value;
        if (value != null) {
            return value.hashCode();
        }
        return 0;
    }
    
    @Override
    public final boolean equals(Object first) {
        final Object value = this.value;
        final Object o = first;
        first = value;
        return o instanceof Result && Intrinsics.areEqual(first, ((Result)o).value);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
    
    public static final class Failure implements Serializable
    {
        public final Throwable exception;
        
        @Override
        public final boolean equals(final Object other) {
            return other instanceof Failure && Intrinsics.areEqual(this.exception, ((Failure)other).exception);
        }
        
        @Override
        public final int hashCode() {
            return this.exception.hashCode();
        }
        
        @Override
        public final String toString() {
            return "Failure(" + this.exception + ')';
        }
        
        public Failure(final Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            this.exception = exception;
        }
    }
}
