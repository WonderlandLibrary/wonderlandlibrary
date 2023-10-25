package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Present<T> extends Optional<T> {
  private final T reference;
  
  private static final long serialVersionUID = 0L;
  
  Present(T reference) {
    this.reference = reference;
  }
  
  public boolean isPresent() {
    return true;
  }
  
  public T get() {
    return this.reference;
  }
  
  public T or(T defaultValue) {
    Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
    return this.reference;
  }
  
  public Optional<T> or(Optional<? extends T> secondChoice) {
    Preconditions.checkNotNull(secondChoice);
    return this;
  }
  
  public T or(Supplier<? extends T> supplier) {
    Preconditions.checkNotNull(supplier);
    return this.reference;
  }
  
  public T orNull() {
    return this.reference;
  }
  
  public Set<T> asSet() {
    return Collections.singleton(this.reference);
  }
  
  public <V> Optional<V> transform(Function<? super T, V> function) {
    return new Present(Preconditions.checkNotNull((T)function.apply(this.reference), "the Function passed to Optional.transform() must not return null."));
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof Present) {
      Present<?> other = (Present)object;
      return this.reference.equals(other.reference);
    } 
    return false;
  }
  
  public int hashCode() {
    return 1502476572 + this.reference.hashCode();
  }
  
  public String toString() {
    return "Optional.of(" + this.reference + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Present.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */