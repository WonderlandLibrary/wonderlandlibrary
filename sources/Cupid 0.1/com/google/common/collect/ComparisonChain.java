package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ComparisonChain {
  private ComparisonChain() {}
  
  public static ComparisonChain start() {
    return ACTIVE;
  }
  
  private static final ComparisonChain ACTIVE = new ComparisonChain() {
      public ComparisonChain compare(Comparable<Comparable> left, Comparable right) {
        return classify(left.compareTo(right));
      }
      
      public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, Comparator<T> comparator) {
        return classify(comparator.compare(left, right));
      }
      
      public ComparisonChain compare(int left, int right) {
        return classify(Ints.compare(left, right));
      }
      
      public ComparisonChain compare(long left, long right) {
        return classify(Longs.compare(left, right));
      }
      
      public ComparisonChain compare(float left, float right) {
        return classify(Float.compare(left, right));
      }
      
      public ComparisonChain compare(double left, double right) {
        return classify(Double.compare(left, right));
      }
      
      public ComparisonChain compareTrueFirst(boolean left, boolean right) {
        return classify(Booleans.compare(right, left));
      }
      
      public ComparisonChain compareFalseFirst(boolean left, boolean right) {
        return classify(Booleans.compare(left, right));
      }
      
      ComparisonChain classify(int result) {
        return (result < 0) ? ComparisonChain.LESS : ((result > 0) ? ComparisonChain.GREATER : ComparisonChain.ACTIVE);
      }
      
      public int result() {
        return 0;
      }
    };
  
  private static final ComparisonChain LESS = new InactiveComparisonChain(-1);
  
  private static final ComparisonChain GREATER = new InactiveComparisonChain(1);
  
  public abstract ComparisonChain compare(Comparable<?> paramComparable1, Comparable<?> paramComparable2);
  
  public abstract <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, Comparator<T> paramComparator);
  
  public abstract ComparisonChain compare(int paramInt1, int paramInt2);
  
  public abstract ComparisonChain compare(long paramLong1, long paramLong2);
  
  public abstract ComparisonChain compare(float paramFloat1, float paramFloat2);
  
  public abstract ComparisonChain compare(double paramDouble1, double paramDouble2);
  
  public abstract ComparisonChain compareTrueFirst(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract ComparisonChain compareFalseFirst(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract int result();
  
  private static final class InactiveComparisonChain extends ComparisonChain {
    final int result;
    
    InactiveComparisonChain(int result) {
      this.result = result;
    }
    
    public ComparisonChain compare(@Nullable Comparable left, @Nullable Comparable right) {
      return this;
    }
    
    public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, @Nullable Comparator<T> comparator) {
      return this;
    }
    
    public ComparisonChain compare(int left, int right) {
      return this;
    }
    
    public ComparisonChain compare(long left, long right) {
      return this;
    }
    
    public ComparisonChain compare(float left, float right) {
      return this;
    }
    
    public ComparisonChain compare(double left, double right) {
      return this;
    }
    
    public ComparisonChain compareTrueFirst(boolean left, boolean right) {
      return this;
    }
    
    public ComparisonChain compareFalseFirst(boolean left, boolean right) {
      return this;
    }
    
    public int result() {
      return this.result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ComparisonChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */