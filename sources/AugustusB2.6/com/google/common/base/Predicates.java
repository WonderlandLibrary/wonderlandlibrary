// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.io.Serializable;
import javax.annotation.CheckForNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.Collection;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Predicates
{
    private Predicates() {
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }
    
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return new NotPredicate<T>(predicate);
    }
    
    public static <T> Predicate<T> and(final Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate<T>((List)defensiveCopy(components));
    }
    
    @SafeVarargs
    public static <T> Predicate<T> and(final Predicate<? super T>... components) {
        return new AndPredicate<T>((List)defensiveCopy(components));
    }
    
    public static <T> Predicate<T> and(final Predicate<? super T> first, final Predicate<? super T> second) {
        return new AndPredicate<T>((List)asList(Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
    }
    
    public static <T> Predicate<T> or(final Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate<T>((List)defensiveCopy(components));
    }
    
    @SafeVarargs
    public static <T> Predicate<T> or(final Predicate<? super T>... components) {
        return new OrPredicate<T>((List)defensiveCopy(components));
    }
    
    public static <T> Predicate<T> or(final Predicate<? super T> first, final Predicate<? super T> second) {
        return new OrPredicate<T>((List)asList(Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
    }
    
    public static <T> Predicate<T> equalTo(@ParametricNullness final T target) {
        return (target == null) ? isNull() : new IsEqualToPredicate((Object)target).withNarrowedType();
    }
    
    @GwtIncompatible
    public static <T> Predicate<T> instanceOf(final Class<?> clazz) {
        return new InstanceOfPredicate<T>((Class)clazz);
    }
    
    @GwtIncompatible
    @Beta
    public static Predicate<Class<?>> subtypeOf(final Class<?> clazz) {
        return new SubtypeOfPredicate((Class)clazz);
    }
    
    public static <T> Predicate<T> in(final Collection<? extends T> target) {
        return new InPredicate<T>((Collection)target);
    }
    
    public static <A, B> Predicate<A> compose(final Predicate<B> predicate, final Function<A, ? extends B> function) {
        return new CompositionPredicate<A, Object>((Predicate)predicate, (Function)function);
    }
    
    @GwtIncompatible
    public static Predicate<CharSequence> containsPattern(final String pattern) {
        return new ContainsPatternFromStringPredicate(pattern);
    }
    
    @GwtIncompatible("java.util.regex.Pattern")
    public static Predicate<CharSequence> contains(final Pattern pattern) {
        return new ContainsPatternPredicate(new JdkPattern(pattern));
    }
    
    private static String toStringHelper(final String methodName, final Iterable<?> components) {
        final StringBuilder builder = new StringBuilder("Predicates.").append(methodName).append('(');
        boolean first = true;
        for (final Object o : components) {
            if (!first) {
                builder.append(',');
            }
            builder.append(o);
            first = false;
        }
        return builder.append(')').toString();
    }
    
    private static <T> List<Predicate<? super T>> asList(final Predicate<? super T> first, final Predicate<? super T> second) {
        return Arrays.asList(first, second);
    }
    
    private static <T> List<T> defensiveCopy(final T... array) {
        return defensiveCopy(Arrays.asList(array));
    }
    
    static <T> List<T> defensiveCopy(final Iterable<T> iterable) {
        final ArrayList<T> list = new ArrayList<T>();
        for (final T element : iterable) {
            list.add(Preconditions.checkNotNull(element));
        }
        return list;
    }
    
    enum ObjectPredicate implements Predicate<Object>
    {
        ALWAYS_TRUE(0) {
            @Override
            public boolean apply(@CheckForNull final Object o) {
                return true;
            }
            
            @Override
            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        }, 
        ALWAYS_FALSE(1) {
            @Override
            public boolean apply(@CheckForNull final Object o) {
                return false;
            }
            
            @Override
            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        }, 
        IS_NULL(2) {
            @Override
            public boolean apply(@CheckForNull final Object o) {
                return o == null;
            }
            
            @Override
            public String toString() {
                return "Predicates.isNull()";
            }
        }, 
        NOT_NULL(3) {
            @Override
            public boolean apply(@CheckForNull final Object o) {
                return o != null;
            }
            
            @Override
            public String toString() {
                return "Predicates.notNull()";
            }
        };
        
         <T> Predicate<T> withNarrowedType() {
            return (Predicate<T>)this;
        }
        
        private static /* synthetic */ ObjectPredicate[] $values() {
            return new ObjectPredicate[] { ObjectPredicate.ALWAYS_TRUE, ObjectPredicate.ALWAYS_FALSE, ObjectPredicate.IS_NULL, ObjectPredicate.NOT_NULL };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private static class NotPredicate<T> implements Predicate<T>, Serializable
    {
        final Predicate<T> predicate;
        private static final long serialVersionUID = 0L;
        
        NotPredicate(final Predicate<T> predicate) {
            this.predicate = Preconditions.checkNotNull(predicate);
        }
        
        @Override
        public boolean apply(@ParametricNullness final T t) {
            return !this.predicate.apply(t);
        }
        
        @Override
        public int hashCode() {
            return ~this.predicate.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof NotPredicate) {
                final NotPredicate<?> that = (NotPredicate<?>)obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.predicate);
            return new StringBuilder(16 + String.valueOf(value).length()).append("Predicates.not(").append(value).append(")").toString();
        }
    }
    
    private static class AndPredicate<T> implements Predicate<T>, Serializable
    {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;
        
        private AndPredicate(final List<? extends Predicate<? super T>> components) {
            this.components = components;
        }
        
        @Override
        public boolean apply(@ParametricNullness final T t) {
            for (int i = 0; i < this.components.size(); ++i) {
                if (!((Predicate)this.components.get(i)).apply(t)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof AndPredicate) {
                final AndPredicate<?> that = (AndPredicate<?>)obj;
                return this.components.equals(that.components);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return toStringHelper("and", this.components);
        }
    }
    
    private static class OrPredicate<T> implements Predicate<T>, Serializable
    {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;
        
        private OrPredicate(final List<? extends Predicate<? super T>> components) {
            this.components = components;
        }
        
        @Override
        public boolean apply(@ParametricNullness final T t) {
            for (int i = 0; i < this.components.size(); ++i) {
                if (((Predicate)this.components.get(i)).apply(t)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof OrPredicate) {
                final OrPredicate<?> that = (OrPredicate<?>)obj;
                return this.components.equals(that.components);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return toStringHelper("or", this.components);
        }
    }
    
    private static class IsEqualToPredicate implements Predicate<Object>, Serializable
    {
        private final Object target;
        private static final long serialVersionUID = 0L;
        
        private IsEqualToPredicate(final Object target) {
            this.target = target;
        }
        
        @Override
        public boolean apply(@CheckForNull final Object o) {
            return this.target.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.target.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                final IsEqualToPredicate that = (IsEqualToPredicate)obj;
                return this.target.equals(that.target);
            }
            return false;
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.target);
            return new StringBuilder(20 + String.valueOf(value).length()).append("Predicates.equalTo(").append(value).append(")").toString();
        }
        
         <T> Predicate<T> withNarrowedType() {
            return (Predicate<T>)this;
        }
    }
    
    @GwtIncompatible
    private static class InstanceOfPredicate<T> implements Predicate<T>, Serializable
    {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;
        
        private InstanceOfPredicate(final Class<?> clazz) {
            this.clazz = Preconditions.checkNotNull(clazz);
        }
        
        @Override
        public boolean apply(@ParametricNullness final T o) {
            return this.clazz.isInstance(o);
        }
        
        @Override
        public int hashCode() {
            return this.clazz.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof InstanceOfPredicate) {
                final InstanceOfPredicate<?> that = (InstanceOfPredicate<?>)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }
        
        @Override
        public String toString() {
            final String name = this.clazz.getName();
            return new StringBuilder(23 + String.valueOf(name).length()).append("Predicates.instanceOf(").append(name).append(")").toString();
        }
    }
    
    @GwtIncompatible
    private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable
    {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;
        
        private SubtypeOfPredicate(final Class<?> clazz) {
            this.clazz = Preconditions.checkNotNull(clazz);
        }
        
        @Override
        public boolean apply(final Class<?> input) {
            return this.clazz.isAssignableFrom(input);
        }
        
        @Override
        public int hashCode() {
            return this.clazz.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof SubtypeOfPredicate) {
                final SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }
        
        @Override
        public String toString() {
            final String name = this.clazz.getName();
            return new StringBuilder(22 + String.valueOf(name).length()).append("Predicates.subtypeOf(").append(name).append(")").toString();
        }
    }
    
    private static class InPredicate<T> implements Predicate<T>, Serializable
    {
        private final Collection<?> target;
        private static final long serialVersionUID = 0L;
        
        private InPredicate(final Collection<?> target) {
            this.target = Preconditions.checkNotNull(target);
        }
        
        @Override
        public boolean apply(@ParametricNullness final T t) {
            try {
                return this.target.contains(t);
            }
            catch (NullPointerException | ClassCastException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                return false;
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof InPredicate) {
                final InPredicate<?> that = (InPredicate<?>)obj;
                return this.target.equals(that.target);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.target.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.target);
            return new StringBuilder(15 + String.valueOf(value).length()).append("Predicates.in(").append(value).append(")").toString();
        }
    }
    
    private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable
    {
        final Predicate<B> p;
        final Function<A, ? extends B> f;
        private static final long serialVersionUID = 0L;
        
        private CompositionPredicate(final Predicate<B> p, final Function<A, ? extends B> f) {
            this.p = Preconditions.checkNotNull(p);
            this.f = Preconditions.checkNotNull(f);
        }
        
        @Override
        public boolean apply(@ParametricNullness final A a) {
            return this.p.apply((B)this.f.apply(a));
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof CompositionPredicate) {
                final CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
                return this.f.equals(that.f) && this.p.equals(that.p);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.p);
            final String value2 = String.valueOf(this.f);
            return new StringBuilder(2 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append("(").append(value2).append(")").toString();
        }
    }
    
    @GwtIncompatible
    private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable
    {
        final CommonPattern pattern;
        private static final long serialVersionUID = 0L;
        
        ContainsPatternPredicate(final CommonPattern pattern) {
            this.pattern = Preconditions.checkNotNull(pattern);
        }
        
        @Override
        public boolean apply(final CharSequence t) {
            return this.pattern.matcher(t).find();
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.pattern.pattern(), this.pattern.flags());
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof ContainsPatternPredicate) {
                final ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
                return Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern.flags() == that.pattern.flags();
            }
            return false;
        }
        
        @Override
        public String toString() {
            final String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
            return new StringBuilder(21 + String.valueOf(patternString).length()).append("Predicates.contains(").append(patternString).append(")").toString();
        }
    }
    
    @GwtIncompatible
    private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate
    {
        private static final long serialVersionUID = 0L;
        
        ContainsPatternFromStringPredicate(final String string) {
            super(Platform.compilePattern(string));
        }
        
        @Override
        public String toString() {
            final String pattern = this.pattern.pattern();
            return new StringBuilder(28 + String.valueOf(pattern).length()).append("Predicates.containsPattern(").append(pattern).append(")").toString();
        }
    }
}
