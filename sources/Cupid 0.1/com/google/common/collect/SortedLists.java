package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
final class SortedLists {
  public enum KeyPresentBehavior {
    ANY_PRESENT {
      <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex) {
        return foundIndex;
      }
    },
    LAST_PRESENT {
      <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex) {
        int lower = foundIndex;
        int upper = list.size() - 1;
        while (lower < upper) {
          int middle = lower + upper + 1 >>> 1;
          int c = comparator.compare(list.get(middle), key);
          if (c > 0) {
            upper = middle - 1;
            continue;
          } 
          lower = middle;
        } 
        return lower;
      }
    },
    FIRST_PRESENT {
      <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex) {
        int lower = 0;
        int upper = foundIndex;
        while (lower < upper) {
          int middle = lower + upper >>> 1;
          int c = comparator.compare(list.get(middle), key);
          if (c < 0) {
            lower = middle + 1;
            continue;
          } 
          upper = middle;
        } 
        return lower;
      }
    },
    FIRST_AFTER {
      public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex) {
        return LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
      }
    },
    LAST_BEFORE {
      public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex) {
        return FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
      }
    };
    
    abstract <E> int resultIndex(Comparator<? super E> param1Comparator, E param1E, List<? extends E> param1List, int param1Int);
  }
  
  public enum KeyAbsentBehavior {
    NEXT_LOWER {
      int resultIndex(int higherIndex) {
        return higherIndex - 1;
      }
    },
    NEXT_HIGHER {
      public int resultIndex(int higherIndex) {
        return higherIndex;
      }
    },
    INVERTED_INSERTION_INDEX {
      public int resultIndex(int higherIndex) {
        return higherIndex ^ 0xFFFFFFFF;
      }
    };
    
    abstract int resultIndex(int param1Int);
  }
  
  public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
    Preconditions.checkNotNull(e);
    return binarySearch(list, (E)Preconditions.checkNotNull(e), Ordering.natural(), presentBehavior, absentBehavior);
  }
  
  public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
    return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior);
  }
  
  public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
    return binarySearch(Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
  }
  
  public static <E> int binarySearch(List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
    Preconditions.checkNotNull(comparator);
    Preconditions.checkNotNull(list);
    Preconditions.checkNotNull(presentBehavior);
    Preconditions.checkNotNull(absentBehavior);
    if (!(list instanceof java.util.RandomAccess))
      list = Lists.newArrayList(list); 
    int lower = 0;
    int upper = list.size() - 1;
    while (lower <= upper) {
      int middle = lower + upper >>> 1;
      int c = comparator.compare(key, list.get(middle));
      if (c < 0) {
        upper = middle - 1;
        continue;
      } 
      if (c > 0) {
        lower = middle + 1;
        continue;
      } 
      return lower + presentBehavior.<E>resultIndex(comparator, key, list.subList(lower, upper + 1), middle - lower);
    } 
    return absentBehavior.resultIndex(lower);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */