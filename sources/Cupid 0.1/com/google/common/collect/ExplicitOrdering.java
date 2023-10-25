package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable {
  final ImmutableMap<T, Integer> rankMap;
  
  private static final long serialVersionUID = 0L;
  
  ExplicitOrdering(List<T> valuesInOrder) {
    this(buildRankMap(valuesInOrder));
  }
  
  ExplicitOrdering(ImmutableMap<T, Integer> rankMap) {
    this.rankMap = rankMap;
  }
  
  public int compare(T left, T right) {
    return rank(left) - rank(right);
  }
  
  private int rank(T value) {
    Integer rank = this.rankMap.get(value);
    if (rank == null)
      throw new Ordering.IncomparableValueException(value); 
    return rank.intValue();
  }
  
  private static <T> ImmutableMap<T, Integer> buildRankMap(List<T> valuesInOrder) {
    ImmutableMap.Builder<T, Integer> builder = ImmutableMap.builder();
    int rank = 0;
    for (T value : valuesInOrder)
      builder.put(value, Integer.valueOf(rank++)); 
    return builder.build();
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof ExplicitOrdering) {
      ExplicitOrdering<?> that = (ExplicitOrdering)object;
      return this.rankMap.equals(that.rankMap);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.rankMap.hashCode();
  }
  
  public String toString() {
    return "Ordering.explicit(" + this.rankMap.keySet() + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ExplicitOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */