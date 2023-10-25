package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
  private static final long serialVersionUID = 4954918890077093841L;
  
  public static <L, R> Pair<L, R> of(L left, R right) {
    return new ImmutablePair<L, R>(left, right);
  }
  
  public final L getKey() {
    return getLeft();
  }
  
  public R getValue() {
    return getRight();
  }
  
  public int compareTo(Pair<L, R> other) {
    return (new CompareToBuilder()).append(getLeft(), other.getLeft()).append(getRight(), other.getRight()).toComparison();
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (obj instanceof Map.Entry) {
      Map.Entry<?, ?> other = (Map.Entry<?, ?>)obj;
      return (ObjectUtils.equals(getKey(), other.getKey()) && ObjectUtils.equals(getValue(), other.getValue()));
    } 
    return false;
  }
  
  public int hashCode() {
    return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
  }
  
  public String toString() {
    return '(' + getLeft() + ',' + getRight() + ')';
  }
  
  public String toString(String format) {
    return String.format(format, new Object[] { getLeft(), getRight() });
  }
  
  public abstract L getLeft();
  
  public abstract R getRight();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\tuple\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */