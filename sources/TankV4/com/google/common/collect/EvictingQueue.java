package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

@Beta
@GwtIncompatible("java.util.ArrayDeque")
public final class EvictingQueue extends ForwardingQueue implements Serializable {
   private final Queue delegate;
   @VisibleForTesting
   final int maxSize;
   private static final long serialVersionUID = 0L;

   private EvictingQueue(int var1) {
      Preconditions.checkArgument(var1 >= 0, "maxSize (%s) must >= 0", var1);
      this.delegate = new ArrayDeque(var1);
      this.maxSize = var1;
   }

   public static EvictingQueue create(int var0) {
      return new EvictingQueue(var0);
   }

   public int remainingCapacity() {
      return this.maxSize - this.size();
   }

   protected Queue delegate() {
      return this.delegate;
   }

   public boolean offer(Object var1) {
      return this.add(var1);
   }

   public boolean add(Object var1) {
      Preconditions.checkNotNull(var1);
      if (this.maxSize == 0) {
         return true;
      } else {
         if (this.size() == this.maxSize) {
            this.delegate.remove();
         }

         this.delegate.add(var1);
         return true;
      }
   }

   public boolean addAll(Collection var1) {
      return this.standardAddAll(var1);
   }

   public boolean contains(Object var1) {
      return this.delegate().contains(Preconditions.checkNotNull(var1));
   }

   public boolean remove(Object var1) {
      return this.delegate().remove(Preconditions.checkNotNull(var1));
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
