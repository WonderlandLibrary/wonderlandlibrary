package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Predicates {
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
  
  public static <T> Predicate<T> not(Predicate<T> predicate) {
    return new NotPredicate<T>(predicate);
  }
  
  public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
    return new AndPredicate<T>(defensiveCopy(components));
  }
  
  public static <T> Predicate<T> and(Predicate<? super T>... components) {
    return new AndPredicate<T>(defensiveCopy(components));
  }
  
  public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
    return new AndPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
  }
  
  public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
    return new OrPredicate<T>(defensiveCopy(components));
  }
  
  public static <T> Predicate<T> or(Predicate<? super T>... components) {
    return new OrPredicate<T>(defensiveCopy(components));
  }
  
  public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
    return new OrPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
  }
  
  public static <T> Predicate<T> equalTo(@Nullable T target) {
    return (target == null) ? isNull() : new IsEqualToPredicate<T>(target);
  }
  
  @GwtIncompatible("Class.isInstance")
  public static Predicate<Object> instanceOf(Class<?> clazz) {
    return new InstanceOfPredicate(clazz);
  }
  
  @GwtIncompatible("Class.isAssignableFrom")
  @Beta
  public static Predicate<Class<?>> assignableFrom(Class<?> clazz) {
    return new AssignableFromPredicate(clazz);
  }
  
  public static <T> Predicate<T> in(Collection<? extends T> target) {
    return new InPredicate<T>(target);
  }
  
  public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
    return new CompositionPredicate<A, Object>(predicate, function);
  }
  
  @GwtIncompatible("java.util.regex.Pattern")
  public static Predicate<CharSequence> containsPattern(String pattern) {
    return new ContainsPatternFromStringPredicate(pattern);
  }
  
  @GwtIncompatible("java.util.regex.Pattern")
  public static Predicate<CharSequence> contains(Pattern pattern) {
    return new ContainsPatternPredicate(pattern);
  }
  
  enum ObjectPredicate implements Predicate<Object> {
    ALWAYS_TRUE {
      public boolean apply(@Nullable Object o) {
        return true;
      }
      
      public String toString() {
        return "Predicates.alwaysTrue()";
      }
    },
    ALWAYS_FALSE {
      public boolean apply(@Nullable Object o) {
        return false;
      }
      
      public String toString() {
        return "Predicates.alwaysFalse()";
      }
    },
    IS_NULL {
      public boolean apply(@Nullable Object o) {
        return (o == null);
      }
      
      public String toString() {
        return "Predicates.isNull()";
      }
    },
    NOT_NULL {
      public boolean apply(@Nullable Object o) {
        return (o != null);
      }
      
      public String toString() {
        return "Predicates.notNull()";
      }
    };
    
    <T> Predicate<T> withNarrowedType() {
      return this;
    }
  }
  
  private static class NotPredicate<T> implements Predicate<T>, Serializable {
    final Predicate<T> predicate;
    
    private static final long serialVersionUID = 0L;
    
    NotPredicate(Predicate<T> predicate) {
      this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
    }
    
    public boolean apply(@Nullable T t) {
      return !this.predicate.apply(t);
    }
    
    public int hashCode() {
      return this.predicate.hashCode() ^ 0xFFFFFFFF;
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof NotPredicate) {
        NotPredicate<?> that = (NotPredicate)obj;
        return this.predicate.equals(that.predicate);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.not(" + this.predicate.toString() + ")";
    }
  }
  
  private static final Joiner COMMA_JOINER = Joiner.on(',');
  
  private static class AndPredicate<T> implements Predicate<T>, Serializable {
    private final List<? extends Predicate<? super T>> components;
    
    private static final long serialVersionUID = 0L;
    
    private AndPredicate(List<? extends Predicate<? super T>> components) {
      this.components = components;
    }
    
    public boolean apply(@Nullable T t) {
      for (int i = 0; i < this.components.size(); i++) {
        if (!((Predicate<T>)this.components.get(i)).apply(t))
          return false; 
      } 
      return true;
    }
    
    public int hashCode() {
      return this.components.hashCode() + 306654252;
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof AndPredicate) {
        AndPredicate<?> that = (AndPredicate)obj;
        return this.components.equals(that.components);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.and(" + Predicates.COMMA_JOINER.join(this.components) + ")";
    }
  }
  
  private static class OrPredicate<T> implements Predicate<T>, Serializable {
    private final List<? extends Predicate<? super T>> components;
    
    private static final long serialVersionUID = 0L;
    
    private OrPredicate(List<? extends Predicate<? super T>> components) {
      this.components = components;
    }
    
    public boolean apply(@Nullable T t) {
      for (int i = 0; i < this.components.size(); i++) {
        if (((Predicate<T>)this.components.get(i)).apply(t))
          return true; 
      } 
      return false;
    }
    
    public int hashCode() {
      return this.components.hashCode() + 87855567;
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof OrPredicate) {
        OrPredicate<?> that = (OrPredicate)obj;
        return this.components.equals(that.components);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.or(" + Predicates.COMMA_JOINER.join(this.components) + ")";
    }
  }
  
  private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable {
    private final T target;
    
    private static final long serialVersionUID = 0L;
    
    private IsEqualToPredicate(T target) {
      this.target = target;
    }
    
    public boolean apply(T t) {
      return this.target.equals(t);
    }
    
    public int hashCode() {
      return this.target.hashCode();
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof IsEqualToPredicate) {
        IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
        return this.target.equals(that.target);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.equalTo(" + this.target + ")";
    }
  }
  
  @GwtIncompatible("Class.isInstance")
  private static class InstanceOfPredicate implements Predicate<Object>, Serializable {
    private final Class<?> clazz;
    
    private static final long serialVersionUID = 0L;
    
    private InstanceOfPredicate(Class<?> clazz) {
      this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
    }
    
    public boolean apply(@Nullable Object o) {
      return this.clazz.isInstance(o);
    }
    
    public int hashCode() {
      return this.clazz.hashCode();
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof InstanceOfPredicate) {
        InstanceOfPredicate that = (InstanceOfPredicate)obj;
        return (this.clazz == that.clazz);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.instanceOf(" + this.clazz.getName() + ")";
    }
  }
  
  @GwtIncompatible("Class.isAssignableFrom")
  private static class AssignableFromPredicate implements Predicate<Class<?>>, Serializable {
    private final Class<?> clazz;
    
    private static final long serialVersionUID = 0L;
    
    private AssignableFromPredicate(Class<?> clazz) {
      this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
    }
    
    public boolean apply(Class<?> input) {
      return this.clazz.isAssignableFrom(input);
    }
    
    public int hashCode() {
      return this.clazz.hashCode();
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof AssignableFromPredicate) {
        AssignableFromPredicate that = (AssignableFromPredicate)obj;
        return (this.clazz == that.clazz);
      } 
      return false;
    }
    
    public String toString() {
      return "Predicates.assignableFrom(" + this.clazz.getName() + ")";
    }
  }
  
  private static class InPredicate<T> implements Predicate<T>, Serializable {
    private final Collection<?> target;
    
    private static final long serialVersionUID = 0L;
    
    private InPredicate(Collection<?> target) {
      this.target = Preconditions.<Collection>checkNotNull(target);
    }
    
    public boolean apply(@Nullable T t) {
      try {
        return this.target.contains(t);
      } catch (NullPointerException e) {
        return false;
      } catch (ClassCastException e) {
        return false;
      } 
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof InPredicate) {
        InPredicate<?> that = (InPredicate)obj;
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
  
  private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
    final Predicate<B> p;
    
    final Function<A, ? extends B> f;
    
    private static final long serialVersionUID = 0L;
    
    private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
      this.p = Preconditions.<Predicate<B>>checkNotNull(p);
      this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
    }
    
    public boolean apply(@Nullable A a) {
      return this.p.apply(this.f.apply(a));
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof CompositionPredicate) {
        CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
        return (this.f.equals(that.f) && this.p.equals(that.p));
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
  
  @GwtIncompatible("Only used by other GWT-incompatible code.")
  private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable {
    final Pattern pattern;
    
    private static final long serialVersionUID = 0L;
    
    ContainsPatternPredicate(Pattern pattern) {
      this.pattern = Preconditions.<Pattern>checkNotNull(pattern);
    }
    
    public boolean apply(CharSequence t) {
      return this.pattern.matcher(t).find();
    }
    
    public int hashCode() {
      return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) });
    }
    
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof ContainsPatternPredicate) {
        ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
        return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && Objects.equal(Integer.valueOf(this.pattern.flags()), Integer.valueOf(that.pattern.flags())));
      } 
      return false;
    }
    
    public String toString() {
      String patternString = Objects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
      return "Predicates.contains(" + patternString + ")";
    }
  }
  
  @GwtIncompatible("Only used by other GWT-incompatible code.")
  private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate {
    private static final long serialVersionUID = 0L;
    
    ContainsPatternFromStringPredicate(String string) {
      super(Pattern.compile(string));
    }
    
    public String toString() {
      return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
    }
  }
  
  private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
    return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
  }
  
  private static <T> List<T> defensiveCopy(T... array) {
    return defensiveCopy(Arrays.asList(array));
  }
  
  static <T> List<T> defensiveCopy(Iterable<T> iterable) {
    ArrayList<T> list = new ArrayList<T>();
    for (T element : iterable)
      list.add(Preconditions.checkNotNull(element)); 
    return list;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Predicates.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */