// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.ForOverride;
import com.google.j2objc.annotations.RetainedWith;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class Converter<A, B> implements Function<A, B>
{
    private final boolean handleNullAutomatically;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient Converter<B, A> reverse;
    
    protected Converter() {
        this(true);
    }
    
    Converter(final boolean handleNullAutomatically) {
        this.handleNullAutomatically = handleNullAutomatically;
    }
    
    @ForOverride
    protected abstract B doForward(final A p0);
    
    @ForOverride
    protected abstract A doBackward(final B p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    public final B convert(@CheckForNull final A a) {
        return this.correctedDoForward(a);
    }
    
    @CheckForNull
    B correctedDoForward(@CheckForNull final A a) {
        if (this.handleNullAutomatically) {
            return (a == null) ? null : Preconditions.checkNotNull(this.doForward(a));
        }
        return this.unsafeDoForward(a);
    }
    
    @CheckForNull
    A correctedDoBackward(@CheckForNull final B b) {
        if (this.handleNullAutomatically) {
            return (b == null) ? null : Preconditions.checkNotNull(this.doBackward(b));
        }
        return this.unsafeDoBackward(b);
    }
    
    @CheckForNull
    private B unsafeDoForward(@CheckForNull final A a) {
        return this.doForward(NullnessCasts.uncheckedCastNullableTToT(a));
    }
    
    @CheckForNull
    private A unsafeDoBackward(@CheckForNull final B b) {
        return this.doBackward(NullnessCasts.uncheckedCastNullableTToT(b));
    }
    
    @CanIgnoreReturnValue
    public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
        Preconditions.checkNotNull(fromIterable, (Object)"fromIterable");
        return new Iterable<B>() {
            @Override
            public Iterator<B> iterator() {
                return new Iterator<B>() {
                    private final Iterator<? extends A> fromIterator = fromIterable.iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return this.fromIterator.hasNext();
                    }
                    
                    @CheckForNull
                    @Override
                    public B next() {
                        return Converter.this.convert(this.fromIterator.next());
                    }
                    
                    @Override
                    public void remove() {
                        this.fromIterator.remove();
                    }
                };
            }
        };
    }
    
    @CanIgnoreReturnValue
    public Converter<B, A> reverse() {
        final Converter<B, A> result = this.reverse;
        return (result == null) ? (this.reverse = (Converter<B, A>)new ReverseConverter((Converter<Object, Object>)this)) : result;
    }
    
    public final <C> Converter<A, C> andThen(final Converter<B, C> secondConverter) {
        return (Converter<A, C>)this.doAndThen((Converter<B, Object>)secondConverter);
    }
    
     <C> Converter<A, C> doAndThen(final Converter<B, C> secondConverter) {
        return (Converter<A, C>)new ConverterComposition((Converter<Object, Object>)this, (Converter<Object, Object>)Preconditions.checkNotNull(secondConverter));
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public final B apply(@CheckForNull final A a) {
        return this.convert(a);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return super.equals(object);
    }
    
    public static <A, B> Converter<A, B> from(final Function<? super A, ? extends B> forwardFunction, final Function<? super B, ? extends A> backwardFunction) {
        return new FunctionBasedConverter<A, B>((Function)forwardFunction, (Function)backwardFunction);
    }
    
    public static <T> Converter<T, T> identity() {
        return (Converter<T, T>)IdentityConverter.INSTANCE;
    }
    
    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable
    {
        final Converter<A, B> original;
        private static final long serialVersionUID = 0L;
        
        ReverseConverter(final Converter<A, B> original) {
            this.original = original;
        }
        
        @Override
        protected A doForward(final B b) {
            throw new AssertionError();
        }
        
        @Override
        protected B doBackward(final A a) {
            throw new AssertionError();
        }
        
        @CheckForNull
        @Override
        A correctedDoForward(@CheckForNull final B b) {
            return this.original.correctedDoBackward(b);
        }
        
        @CheckForNull
        @Override
        B correctedDoBackward(@CheckForNull final A a) {
            return this.original.correctedDoForward(a);
        }
        
        @Override
        public Converter<A, B> reverse() {
            return this.original;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof ReverseConverter) {
                final ReverseConverter<?, ?> that = (ReverseConverter<?, ?>)object;
                return this.original.equals(that.original);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ~this.original.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.original);
            return new StringBuilder(10 + String.valueOf(value).length()).append(value).append(".reverse()").toString();
        }
    }
    
    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable
    {
        final Converter<A, B> first;
        final Converter<B, C> second;
        private static final long serialVersionUID = 0L;
        
        ConverterComposition(final Converter<A, B> first, final Converter<B, C> second) {
            this.first = first;
            this.second = second;
        }
        
        @Override
        protected C doForward(final A a) {
            throw new AssertionError();
        }
        
        @Override
        protected A doBackward(final C c) {
            throw new AssertionError();
        }
        
        @CheckForNull
        @Override
        C correctedDoForward(@CheckForNull final A a) {
            return this.second.correctedDoForward(this.first.correctedDoForward(a));
        }
        
        @CheckForNull
        @Override
        A correctedDoBackward(@CheckForNull final C c) {
            return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof ConverterComposition) {
                final ConverterComposition<?, ?, ?> that = (ConverterComposition<?, ?, ?>)object;
                return this.first.equals(that.first) && this.second.equals(that.second);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return 31 * this.first.hashCode() + this.second.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.first);
            final String value2 = String.valueOf(this.second);
            return new StringBuilder(10 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(".andThen(").append(value2).append(")").toString();
        }
    }
    
    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable
    {
        private final Function<? super A, ? extends B> forwardFunction;
        private final Function<? super B, ? extends A> backwardFunction;
        
        private FunctionBasedConverter(final Function<? super A, ? extends B> forwardFunction, final Function<? super B, ? extends A> backwardFunction) {
            this.forwardFunction = Preconditions.checkNotNull(forwardFunction);
            this.backwardFunction = Preconditions.checkNotNull(backwardFunction);
        }
        
        @Override
        protected B doForward(final A a) {
            return (B)this.forwardFunction.apply((Object)a);
        }
        
        @Override
        protected A doBackward(final B b) {
            return (A)this.backwardFunction.apply((Object)b);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof FunctionBasedConverter) {
                final FunctionBasedConverter<?, ?> that = (FunctionBasedConverter<?, ?>)object;
                return this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.forwardFunction);
            final String value2 = String.valueOf(this.backwardFunction);
            return new StringBuilder(18 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Converter.from(").append(value).append(", ").append(value2).append(")").toString();
        }
    }
    
    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable
    {
        static final IdentityConverter<?> INSTANCE;
        private static final long serialVersionUID = 0L;
        
        @Override
        protected T doForward(final T t) {
            return t;
        }
        
        @Override
        protected T doBackward(final T t) {
            return t;
        }
        
        @Override
        public IdentityConverter<T> reverse() {
            return this;
        }
        
        @Override
         <S> Converter<T, S> doAndThen(final Converter<T, S> otherConverter) {
            return Preconditions.checkNotNull(otherConverter, (Object)"otherConverter");
        }
        
        @Override
        public String toString() {
            return "Converter.identity()";
        }
        
        private Object readResolve() {
            return IdentityConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new IdentityConverter<Object>();
        }
    }
}
