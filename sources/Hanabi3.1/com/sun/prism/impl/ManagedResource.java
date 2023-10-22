package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;
import java.util.ArrayList;

public abstract class ManagedResource implements GraphicsResource {
   static final boolean trackLockSources = false;
   protected Object resource;
   private final ResourcePool pool;
   private int lockcount;
   private int employcount;
   ArrayList lockedFrom;
   private boolean permanent;
   private boolean mismatchDetected;
   private boolean disposalRequested;
   private int age;

   static boolean _isgone(ManagedResource var0) {
      if (var0 == null) {
         return true;
      } else if (var0.disposalRequested) {
         var0.free();
         var0.resource = null;
         var0.disposalRequested = false;
         return true;
      } else {
         return !var0.isValid();
      }
   }

   protected ManagedResource(Object var1, ResourcePool var2) {
      this.resource = var1;
      this.pool = var2;
      this.manage();
      this.lock();
   }

   private void manage() {
      this.pool.resourceManaged(this);
   }

   public final Object getResource() {
      this.assertLocked();
      return this.resource;
   }

   public final ResourcePool getPool() {
      return this.pool;
   }

   public boolean isValid() {
      return this.resource != null && !this.disposalRequested;
   }

   public boolean isDisposalRequested() {
      return this.disposalRequested;
   }

   public final boolean isLocked() {
      return this.lockcount > 0;
   }

   public final int getLockCount() {
      return this.lockcount;
   }

   public final void assertLocked() {
      if (this.lockcount <= 0) {
         throw new IllegalStateException("Operation requires resource lock");
      }
   }

   public final boolean isPermanent() {
      return this.permanent;
   }

   public final boolean isInteresting() {
      return this.employcount > 0;
   }

   public final int getInterestCount() {
      return this.employcount;
   }

   public void free() {
   }

   public int getAge() {
      return this.age;
   }

   public final void dispose() {
      if (this.pool.isManagerThread()) {
         Object var1 = this.resource;
         if (var1 != null) {
            this.free();
            this.disposalRequested = false;
            this.resource = null;
            this.pool.resourceFreed(this);
         }
      } else {
         this.disposalRequested = true;
      }

   }

   public final void makePermanent() {
      this.assertLocked();
      this.permanent = true;
   }

   public final Object lock() {
      ++this.lockcount;
      this.age = 0;
      return this.resource;
   }

   void unlockall() {
      this.lockcount = 0;
   }

   public final void unlock() {
      this.assertLocked();
      --this.lockcount;
   }

   public final void contentsUseful() {
      this.assertLocked();
      ++this.employcount;
   }

   public final void contentsNotUseful() {
      if (this.employcount <= 0) {
         throw new IllegalStateException("Resource obsoleted too many times");
      } else {
         --this.employcount;
      }
   }

   public final boolean wasMismatched() {
      return this.mismatchDetected;
   }

   public final void setMismatched() {
      this.mismatchDetected = true;
   }

   public final void bumpAge(int var1) {
      int var2 = this.age;
      if (var2 < var1) {
         this.age = var2 + 1;
      }

   }
}
