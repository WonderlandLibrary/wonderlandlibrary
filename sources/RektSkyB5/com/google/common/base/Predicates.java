/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Predicates {
    private static final Joiner COMMA_JOINER = Joiner.on(',');

    private Predicates() {
    }

    @GwtCompatible(serializable=true)
    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }

    @GwtCompatible(serializable=true)
    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }

    @GwtCompatible(serializable=true)
    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    @GwtCompatible(serializable=true)
    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return new NotPredicate<T>(predicate);
    }

    public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate(Predicates.defensiveCopy(components));
    }

    public static <T> Predicate<T> and(Predicate<? super T> ... components) {
        return new AndPredicate(Predicates.defensiveCopy(components));
    }

    public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
        return new AndPredicate(Predicates.asList(Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate(Predicates.defensiveCopy(components));
    }

    public static <T> Predicate<T> or(Predicate<? super T> ... components) {
        return new OrPredicate(Predicates.defensiveCopy(components));
    }

    public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
        return new OrPredicate(Predicates.asList(Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> equalTo(@Nullable T target) {
        return target == null ? Predicates.isNull() : new IsEqualToPredicate(target);
    }

    @GwtIncompatible(value="Class.isInstance")
    public static Predicate<Object> instanceOf(Class<?> clazz) {
        return new InstanceOfPredicate(clazz);
    }

    @GwtIncompatible(value="Class.isAssignableFrom")
    @Beta
    public static Predicate<Class<?>> assignableFrom(Class<?> clazz) {
        return new AssignableFromPredicate(clazz);
    }

    public static <T> Predicate<T> in(Collection<? extends T> target) {
        return new InPredicate(target);
    }

    public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
        return new CompositionPredicate(predicate, function);
    }

    @GwtIncompatible(value="java.util.regex.Pattern")
    public static Predicate<CharSequence> containsPattern(String pattern) {
        return new ContainsPatternFromStringPredicate(pattern);
    }

    @GwtIncompatible(value="java.util.regex.Pattern")
    public static Predicate<CharSequence> contains(Pattern pattern) {
        return new ContainsPatternPredicate(pattern);
    }

    private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
        return Arrays.asList(first, second);
    }

    private static <T> List<T> defensiveCopy(T ... array) {
        return Predicates.defensiveCopy(Arrays.asList(array));
    }

    static <T> List<T> defensiveCopy(Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<T>();
        for (T element : iterable) {
            list.add(Preconditions.checkNotNull(element));
        }
        return list;
    }

    @GwtIncompatible(value="Only used by other GWT-incompatible code.")
    private static class ContainsPatternFromStringPredicate
    extends ContainsPatternPredicate {
        private static final long serialVersionUID = 0L;

        ContainsPatternFromStringPredicate(String string) {
            super(Pattern.compile(string));
        }

        @Override
        public String toString() {
            return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
        }
    }

    @GwtIncompatible(value="Only used by other GWT-incompatible code.")
    private static class ContainsPatternPredicate
    implements Predicate<CharSequence>,
    Serializable {
        final Pattern pattern;
        private static final long serialVersionUID = 0L;

        ContainsPatternPredicate(Pattern pattern) {
            this.pattern = Preconditions.checkNotNull(pattern);
        }

        @Override
        public boolean apply(CharSequence t2) {
            return this.pattern.matcher(t2).find();
        }

        public int hashCode() {
            return Objects.hashCode(this.pattern.pattern(), this.pattern.flags());
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof ContainsPatternPredicate) {
                ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
                return Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && Objects.equal(this.pattern.flags(), that.pattern.flags());
            }
            return false;
        }

        public String toString() {
            String patternString = Objects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
            return "Predicates.contains(" + patternString + ")";
        }
    }

    private static class CompositionPredicate<A, B>
    implements Predicate<A>,
    Serializable {
        final Predicate<B> p;
        final Function<A, ? extends B> f;
        private static final long serialVersionUID = 0L;

        private CompositionPredicate(Predicate<B> p2, Function<A, ? extends B> f2) {
            this.p = Preconditions.checkNotNull(p2);
            this.f = Preconditions.checkNotNull(f2);
        }

        @Override
        public boolean apply(@Nullable A a2) {
            return this.p.apply(this.f.apply(a2));
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof CompositionPredicate) {
                CompositionPredicate that = (CompositionPredicate)obj;
                return this.f.equals(that.f) && this.p.equals(that.p);
            }
            return false;
        }

        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }

        public String toString() {
            return this.p.toString() + "(" + this.f.toString() + ")";
        }
    }

    private static class InPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final Collection<?> target;
        private static final long serialVersionUID = 0L;

        private InPredicate(Collection<?> target) {
            this.target = Preconditions.checkNotNull(target);
        }

        @Override
        public boolean apply(@Nullable T t2) {
            try {
                return this.target.contains(t2);
            }
            catch (NullPointerException e2) {
                return false;
            }
            catch (ClassCastException e3) {
                return false;
            }
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof InPredicate) {
                InPredicate that = (InPredicate)obj;
                return this.target.equals(that.target);
            }
            return false;
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            return "Predicates.in(" + this.target + ")";
        }
    }

    @GwtIncompatible(value="Class.isAssignableFrom")
    private static class AssignableFromPredicate
    implements Predicate<Class<?>>,
    Serializable {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;

        private AssignableFromPredicate(Class<?> clazz) {
            this.clazz = Preconditions.checkNotNull(clazz);
        }

        @Override
        public boolean apply(Class<?> input) {
            return this.clazz.isAssignableFrom(input);
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof AssignableFromPredicate) {
                AssignableFromPredicate that = (AssignableFromPredicate)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }

        public String toString() {
            return "Predicates.assignableFrom(" + this.clazz.getName() + ")";
        }
    }

    @GwtIncompatible(value="Class.isInstance")
    private static class InstanceOfPredicate
    implements Predicate<Object>,
    Serializable {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;

        private InstanceOfPredicate(Class<?> clazz) {
            this.clazz = Preconditions.checkNotNull(clazz);
        }

        @Override
        public boolean apply(@Nullable Object o2) {
            return this.clazz.isInstance(o2);
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof InstanceOfPredicate) {
                InstanceOfPredicate that = (InstanceOfPredicate)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }

        public String toString() {
            return "Predicates.instanceOf(" + this.clazz.getName() + ")";
        }
    }

    private static class IsEqualToPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final T target;
        private static final long serialVersionUID = 0L;

        private IsEqualToPredicate(T target) {
            this.target = target;
        }

        @Override
        public boolean apply(T t2) {
            return this.target.equals(t2);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                IsEqualToPredicate that = (IsEqualToPredicate)obj;
                return this.target.equals(that.target);
            }
            return false;
        }

        public String toString() {
            return "Predicates.equalTo(" + this.target + ")";
        }
    }

    private static class OrPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;

        private OrPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override
        public boolean apply(@Nullable T t2) {
            for (int i2 = 0; i2 < this.components.size(); ++i2) {
                if (!this.components.get(i2).apply(t2)) continue;
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof OrPredicate) {
                OrPredicate that = (OrPredicate)obj;
                return this.components.equals(that.components);
            }
            return false;
        }

        public String toString() {
            return "Predicates.or(" + COMMA_JOINER.join(this.components) + ")";
        }
    }

    private static class AndPredicate<T>
    implements Predicate<T>,
    Serializable {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;

        private AndPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override
        public boolean apply(@Nullable T t2) {
            for (int i2 = 0; i2 < this.components.size(); ++i2) {
                if (this.components.get(i2).apply(t2)) continue;
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof AndPredicate) {
                AndPredicate that = (AndPredicate)obj;
                return this.components.equals(that.components);
            }
            return false;
        }

        public String toString() {
            return "Predicates.and(" + COMMA_JOINER.join(this.components) + ")";
        }
    }

    private static class NotPredicate<T>
    implements Predicate<T>,
    Serializable {
        final Predicate<T> predicate;
        private static final long serialVersionUID = 0L;

        NotPredicate(Predicate<T> predicate) {
            this.predicate = Preconditions.checkNotNull(predicate);
        }

        @Override
        public boolean apply(@Nullable T t2) {
            return !this.predicate.apply(t2);
        }

        public int hashCode() {
            return ~this.predicate.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof NotPredicate) {
                NotPredicate that = (NotPredicate)obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }

        public String toString() {
            return "Predicates.not(" + this.predicate.toString() + ")";
        }
    }

    static enum ObjectPredicate implements Predicate<Object>
    {
        ALWAYS_TRUE{

            @Override
            public boolean apply(@Nullable Object o2) {
                return true;
            }

            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        }
        ,
        ALWAYS_FALSE{

            @Override
            public boolean apply(@Nullable Object o2) {
                return false;
            }

            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        }
        ,
        IS_NULL{

            @Override
            public boolean apply(@Nullable Object o2) {
                return o2 == null;
            }

            public String toString() {
                return "Predicates.isNull()";
            }
        }
        ,
        NOT_NULL{

            @Override
            public boolean apply(@Nullable Object o2) {
                return o2 != null;
            }

            public String toString() {
                return "Predicates.notNull()";
            }
        };


        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }
}

