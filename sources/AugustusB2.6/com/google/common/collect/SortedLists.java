// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.RandomAccess;
import com.google.common.base.Function;
import java.util.Comparator;
import com.google.common.base.Preconditions;
import java.util.List;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
@Beta
final class SortedLists
{
    private SortedLists() {
    }
    
    public static <E extends Comparable> int binarySearch(final List<? extends E> list, final E e, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.checkNotNull(e);
        return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K extends Comparable> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, final K key, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.checkNotNull(key);
        return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, @ParametricNullness final K key, final Comparator<? super K> keyComparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        return binarySearch(Lists.transform(list, (Function<? super E, ? extends K>)keyFunction), key, keyComparator, presentBehavior, absentBehavior);
    }
    
    public static <E> int binarySearch(List<? extends E> list, @ParametricNullness final E key, final Comparator<? super E> comparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(list);
        Preconditions.checkNotNull(presentBehavior);
        Preconditions.checkNotNull(absentBehavior);
        if (!(list instanceof RandomAccess)) {
            list = (List<? extends E>)Lists.newArrayList((Iterable<?>)list);
        }
        int lower = 0;
        int upper = list.size() - 1;
        while (lower <= upper) {
            final int middle = lower + upper >>> 1;
            final int c = comparator.compare((Object)key, (Object)list.get(middle));
            if (c < 0) {
                upper = middle - 1;
            }
            else {
                if (c <= 0) {
                    return lower + presentBehavior.resultIndex(comparator, key, list.subList(lower, upper + 1), middle - lower);
                }
                lower = middle + 1;
            }
        }
        return absentBehavior.resultIndex(lower);
    }
    
    enum KeyPresentBehavior
    {
        ANY_PRESENT(0) {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, @ParametricNullness final E key, final List<? extends E> list, final int foundIndex) {
                return foundIndex;
            }
        }, 
        LAST_PRESENT(1) {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, @ParametricNullness final E key, final List<? extends E> list, final int foundIndex) {
                int lower = foundIndex;
                int upper = list.size() - 1;
                while (lower < upper) {
                    final int middle = lower + upper + 1 >>> 1;
                    final int c = comparator.compare((Object)list.get(middle), (Object)key);
                    if (c > 0) {
                        upper = middle - 1;
                    }
                    else {
                        lower = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_PRESENT(2) {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, @ParametricNullness final E key, final List<? extends E> list, final int foundIndex) {
                int lower = 0;
                int upper = foundIndex;
                while (lower < upper) {
                    final int middle = lower + upper >>> 1;
                    final int c = comparator.compare((Object)list.get(middle), (Object)key);
                    if (c < 0) {
                        lower = middle + 1;
                    }
                    else {
                        upper = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_AFTER(3) {
            public <E> int resultIndex(final Comparator<? super E> comparator, @ParametricNullness final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$4.LAST_PRESENT.resultIndex(comparator, key, list, foundIndex) + 1;
            }
        }, 
        LAST_BEFORE(4) {
            public <E> int resultIndex(final Comparator<? super E> comparator, @ParametricNullness final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$5.FIRST_PRESENT.resultIndex(comparator, key, list, foundIndex) - 1;
            }
        };
        
        abstract <E> int resultIndex(final Comparator<? super E> p0, @ParametricNullness final E p1, final List<? extends E> p2, final int p3);
        
        private static /* synthetic */ KeyPresentBehavior[] $values() {
            return new KeyPresentBehavior[] { KeyPresentBehavior.ANY_PRESENT, KeyPresentBehavior.LAST_PRESENT, KeyPresentBehavior.FIRST_PRESENT, KeyPresentBehavior.FIRST_AFTER, KeyPresentBehavior.LAST_BEFORE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    enum KeyAbsentBehavior
    {
        NEXT_LOWER(0) {
            @Override
            int resultIndex(final int higherIndex) {
                return higherIndex - 1;
            }
        }, 
        NEXT_HIGHER(1) {
            public int resultIndex(final int higherIndex) {
                return higherIndex;
            }
        }, 
        INVERTED_INSERTION_INDEX(2) {
            public int resultIndex(final int higherIndex) {
                return ~higherIndex;
            }
        };
        
        abstract int resultIndex(final int p0);
        
        private static /* synthetic */ KeyAbsentBehavior[] $values() {
            return new KeyAbsentBehavior[] { KeyAbsentBehavior.NEXT_LOWER, KeyAbsentBehavior.NEXT_HIGHER, KeyAbsentBehavior.INVERTED_INSERTION_INDEX };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
