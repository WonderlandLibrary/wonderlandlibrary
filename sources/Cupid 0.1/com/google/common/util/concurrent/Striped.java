package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Beta
public abstract class Striped<L> {
  private static final int LARGE_LAZY_CUTOFF = 1024;
  
  private Striped() {}
  
  public Iterable<L> bulkGet(Iterable<?> keys) {
    Object[] array = Iterables.toArray(keys, Object.class);
    if (array.length == 0)
      return (Iterable<L>)ImmutableList.of(); 
    int[] stripes = new int[array.length];
    for (int i = 0; i < array.length; i++)
      stripes[i] = indexFor(array[i]); 
    Arrays.sort(stripes);
    int previousStripe = stripes[0];
    array[0] = getAt(previousStripe);
    for (int j = 1; j < array.length; j++) {
      int currentStripe = stripes[j];
      if (currentStripe == previousStripe) {
        array[j] = array[j - 1];
      } else {
        array[j] = getAt(currentStripe);
        previousStripe = currentStripe;
      } 
    } 
    List<L> asList = Arrays.asList((L[])array);
    return Collections.unmodifiableList(asList);
  }
  
  public static Striped<Lock> lock(int stripes) {
    return new CompactStriped<Lock>(stripes, new Supplier<Lock>() {
          public Lock get() {
            return new Striped.PaddedLock();
          }
        });
  }
  
  public static Striped<Lock> lazyWeakLock(int stripes) {
    return lazy(stripes, new Supplier<Lock>() {
          public Lock get() {
            return new ReentrantLock(false);
          }
        });
  }
  
  private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
    return (stripes < 1024) ? new SmallLazyStriped<L>(stripes, supplier) : new LargeLazyStriped<L>(stripes, supplier);
  }
  
  public static Striped<Semaphore> semaphore(int stripes, final int permits) {
    return new CompactStriped<Semaphore>(stripes, new Supplier<Semaphore>() {
          public Semaphore get() {
            return new Striped.PaddedSemaphore(permits);
          }
        });
  }
  
  public static Striped<Semaphore> lazyWeakSemaphore(int stripes, final int permits) {
    return lazy(stripes, new Supplier<Semaphore>() {
          public Semaphore get() {
            return new Semaphore(permits, false);
          }
        });
  }
  
  public static Striped<ReadWriteLock> readWriteLock(int stripes) {
    return new CompactStriped<ReadWriteLock>(stripes, READ_WRITE_LOCK_SUPPLIER);
  }
  
  public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
    return lazy(stripes, READ_WRITE_LOCK_SUPPLIER);
  }
  
  private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier<ReadWriteLock>() {
      public ReadWriteLock get() {
        return new ReentrantReadWriteLock();
      }
    };
  
  private static final int ALL_SET = -1;
  
  private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
    final int mask;
    
    PowerOfTwoStriped(int stripes) {
      Preconditions.checkArgument((stripes > 0), "Stripes must be positive");
      this.mask = (stripes > 1073741824) ? -1 : (Striped.ceilToPowerOfTwo(stripes) - 1);
    }
    
    final int indexFor(Object key) {
      int hash = Striped.smear(key.hashCode());
      return hash & this.mask;
    }
    
    public final L get(Object key) {
      return getAt(indexFor(key));
    }
  }
  
  private static class CompactStriped<L> extends PowerOfTwoStriped<L> {
    private final Object[] array;
    
    private CompactStriped(int stripes, Supplier<L> supplier) {
      super(stripes);
      Preconditions.checkArgument((stripes <= 1073741824), "Stripes must be <= 2^30)");
      this.array = new Object[this.mask + 1];
      for (int i = 0; i < this.array.length; i++)
        this.array[i] = supplier.get(); 
    }
    
    public L getAt(int index) {
      return (L)this.array[index];
    }
    
    public int size() {
      return this.array.length;
    }
  }
  
  @VisibleForTesting
  static class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
    final AtomicReferenceArray<ArrayReference<? extends L>> locks;
    
    final Supplier<L> supplier;
    
    final int size;
    
    final ReferenceQueue<L> queue = new ReferenceQueue<L>();
    
    SmallLazyStriped(int stripes, Supplier<L> supplier) {
      super(stripes);
      this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
      this.locks = new AtomicReferenceArray<ArrayReference<? extends L>>(this.size);
      this.supplier = supplier;
    }
    
    public L getAt(int index) {
      if (this.size != Integer.MAX_VALUE)
        Preconditions.checkElementIndex(index, size()); 
      ArrayReference<? extends L> existingRef = this.locks.get(index);
      L existing = (existingRef == null) ? null : existingRef.get();
      if (existing != null)
        return existing; 
      L created = (L)this.supplier.get();
      ArrayReference<L> newRef = new ArrayReference<L>(created, index, this.queue);
      while (!this.locks.compareAndSet(index, existingRef, newRef)) {
        existingRef = this.locks.get(index);
        existing = (existingRef == null) ? null : existingRef.get();
        if (existing != null)
          return existing; 
      } 
      drainQueue();
      return created;
    }
    
    private void drainQueue() {
      Reference<? extends L> ref;
      while ((ref = this.queue.poll()) != null) {
        ArrayReference<? extends L> arrayRef = (ArrayReference<? extends L>)ref;
        this.locks.compareAndSet(arrayRef.index, arrayRef, null);
      } 
    }
    
    public int size() {
      return this.size;
    }
    
    private static final class ArrayReference<L> extends WeakReference<L> {
      final int index;
      
      ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
        super(referent, queue);
        this.index = index;
      }
    }
  }
  
  @VisibleForTesting
  static class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
    final ConcurrentMap<Integer, L> locks;
    
    final Supplier<L> supplier;
    
    final int size;
    
    LargeLazyStriped(int stripes, Supplier<L> supplier) {
      super(stripes);
      this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
      this.supplier = supplier;
      this.locks = (new MapMaker()).weakValues().makeMap();
    }
    
    public L getAt(int index) {
      if (this.size != Integer.MAX_VALUE)
        Preconditions.checkElementIndex(index, size()); 
      L existing = this.locks.get(Integer.valueOf(index));
      if (existing != null)
        return existing; 
      L created = (L)this.supplier.get();
      existing = this.locks.putIfAbsent(Integer.valueOf(index), created);
      return (L)Objects.firstNonNull(existing, created);
    }
    
    public int size() {
      return this.size;
    }
  }
  
  private static int ceilToPowerOfTwo(int x) {
    return 1 << IntMath.log2(x, RoundingMode.CEILING);
  }
  
  private static int smear(int hashCode) {
    hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
    return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
  }
  
  public abstract L get(Object paramObject);
  
  public abstract L getAt(int paramInt);
  
  abstract int indexFor(Object paramObject);
  
  public abstract int size();
  
  private static class PaddedLock extends ReentrantLock {
    long q1;
    
    long q2;
    
    long q3;
    
    PaddedLock() {
      super(false);
    }
  }
  
  private static class PaddedSemaphore extends Semaphore {
    long q1;
    
    long q2;
    
    long q3;
    
    PaddedSemaphore(int permits) {
      super(permits, false);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\Striped.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */