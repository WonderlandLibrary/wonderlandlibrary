package com.sun.prism.impl;

import java.lang.ref.WeakReference;
import java.util.Iterator;

public abstract class BaseResourcePool implements ResourcePool {
   private static final int FOREVER = 1024;
   private static final int RECENTLY_USEFUL = 100;
   private static final int RECENT = 10;
   private static final Predicate[] stageTesters = new Predicate[6];
   private static final String[] stageReasons = new String[6];
   long managedSize;
   final long origTarget;
   long curTarget;
   final long maxSize;
   final ResourcePool sharedParent;
   private final Thread managerThread;
   private WeakLinkedList resourceHead;

   protected BaseResourcePool(long var1, long var3) {
      this((ResourcePool)null, var1, var3);
   }

   protected BaseResourcePool(ResourcePool var1) {
      this(var1, var1.target(), var1.max());
   }

   protected BaseResourcePool(ResourcePool var1, long var2, long var4) {
      this.resourceHead = new WeakLinkedList();
      this.sharedParent = var1;
      this.origTarget = this.curTarget = var2;
      this.maxSize = var1 == null ? var4 : Math.min(var1.max(), var4);
      this.managerThread = Thread.currentThread();
   }

   public boolean cleanup(long var1) {
      if (this.used() + var1 <= this.target()) {
         return true;
      } else {
         long var3 = this.used();
         long var5 = this.target() / 16L;
         if (var5 < var1) {
            var5 = var1;
         }

         if (PrismSettings.poolDebug) {
            System.err.printf("Need %,d (hoping for %,d) from pool: %s\n", var1, var5, this);
            this.printSummary(false);
         }

         boolean var7;
         try {
            Disposer.cleanUp();
            if (PrismSettings.poolDebug) {
               System.err.println("Pruning obsolete in pool: " + this);
            }

            this.cleanup((var0) -> {
               return false;
            });
            if (this.used() + var5 > this.target()) {
               for(int var15 = 0; var15 < stageTesters.length; ++var15) {
                  if (PrismSettings.poolDebug) {
                     System.err.println(stageReasons[var15] + " in pool: " + this);
                  }

                  this.cleanup(stageTesters[var15]);
                  if (this.used() + var5 <= this.target()) {
                     boolean var8 = true;
                     return var8;
                  }
               }

               long var16 = this.max() - this.used();
               if (var5 > var16) {
                  var5 = var1;
               }

               if (var5 <= var16) {
                  long var18 = (this.max() - this.origTarget()) / 32L;
                  if (var18 < var5) {
                     var18 = var5;
                  } else if (var18 > var16) {
                     var18 = var16;
                  }

                  this.setTarget(this.used() + var18);
                  if (PrismSettings.poolDebug || PrismSettings.verbose) {
                     System.err.printf("Growing pool %s target to %,d\n", this, this.target());
                  }

                  boolean var11 = true;
                  return var11;
               }

               int var9 = 0;

               while(true) {
                  if (var9 < 2) {
                     this.pruneLastChance(var9 > 0);
                     if (this.used() + var1 > this.max()) {
                        ++var9;
                        continue;
                     }

                     if (this.used() + var1 > this.target()) {
                        this.setTarget(this.used() + var1);
                        if (PrismSettings.poolDebug || PrismSettings.verbose) {
                           System.err.printf("Growing pool %s target to %,d\n", this, this.target());
                        }
                     }

                     boolean var10 = true;
                     return var10;
                  }

                  boolean var17 = false;
                  return var17;
               }
            }

            var7 = true;
         } finally {
            if (PrismSettings.poolDebug) {
               System.err.printf("cleaned up %,d from pool: %s\n", var3 - this.used(), this);
               this.printSummary(false);
               System.err.println();
            }

         }

         return var7;
      }
   }

   private void pruneLastChance(boolean var1) {
      System.gc();
      if (var1) {
         try {
            Thread.sleep(20L);
         } catch (InterruptedException var3) {
         }
      }

      Disposer.cleanUp();
      if (PrismSettings.poolDebug) {
         if (var1) {
            System.err.print("Last chance pruning");
         } else {
            System.err.print("Pruning everything");
         }

         System.err.println(" in pool: " + this);
      }

      this.cleanup((var0) -> {
         return true;
      });
   }

   private void cleanup(Predicate var1) {
      WeakLinkedList var2 = this.resourceHead;
      WeakLinkedList var3 = var2.next;

      while(true) {
         while(var3 != null) {
            ManagedResource var4 = var3.getResource();
            if (ManagedResource._isgone(var4)) {
               if (PrismSettings.poolDebug) {
                  showLink("unlinking", var3, false);
               }

               this.recordFree(var3.size);
               var3 = var3.next;
               var2.next = var3;
            } else if (!var4.isPermanent() && !var4.isLocked() && var1.test(var4)) {
               if (PrismSettings.poolDebug) {
                  showLink("pruning", var3, true);
               }

               var4.free();
               var4.resource = null;
               this.recordFree(var3.size);
               var3 = var3.next;
               var2.next = var3;
            } else {
               var2 = var3;
               var3 = var3.next;
            }
         }

         return;
      }
   }

   static void showLink(String var0, WeakLinkedList var1, boolean var2) {
      ManagedResource var3 = var1.getResource();
      System.err.printf("%s: %s (size=%,d)", var0, var3, var1.size);
      if (var3 != null) {
         if (var2) {
            System.err.printf(" (age=%d)", var3.getAge());
         }

         if (var3.isPermanent()) {
            System.err.print(" perm");
         }

         if (var3.isLocked()) {
            System.err.print(" lock");
         }

         if (var3.isInteresting()) {
            System.err.print(" int");
         }
      }

      System.err.println();
   }

   public void freeDisposalRequestedAndCheckResources(boolean var1) {
      boolean var2 = false;
      WeakLinkedList var3 = this.resourceHead;
      WeakLinkedList var4 = var3.next;

      while(var4 != null) {
         ManagedResource var5 = var4.getResource();
         if (ManagedResource._isgone(var5)) {
            this.recordFree(var4.size);
            var4 = var4.next;
            var3.next = var4;
         } else {
            if (!var5.isPermanent()) {
               if (var5.isLocked() && !var5.wasMismatched()) {
                  if (var1) {
                     var5.unlockall();
                  } else {
                     var5.setMismatched();
                     var2 = true;
                  }
               }

               var5.bumpAge(1024);
            }

            var3 = var4;
            var4 = var4.next;
         }
      }

      if (PrismSettings.poolStats || var2) {
         if (var2) {
            System.err.println("Outstanding resource locks detected:");
         }

         this.printSummary(true);
         System.err.println();
      }

   }

   static String commas(long var0) {
      return String.format("%,d", var0);
   }

   public void printSummary(boolean var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      long var8 = 0L;
      int var10 = 0;
      boolean var11 = false;
      double var12 = (double)this.used() * 100.0 / (double)this.max();
      double var14 = (double)this.target() * 100.0 / (double)this.max();
      System.err.printf("%s: %,d used (%.1f%%), %,d target (%.1f%%), %,d max\n", this, this.used(), var12, this.target(), var14, this.max());

      for(WeakLinkedList var16 = this.resourceHead.next; var16 != null; var16 = var16.next) {
         ManagedResource var17 = var16.getResource();
         ++var10;
         if (var17 != null && var17.isValid() && !var17.isDisposalRequested()) {
            int var18 = var17.getAge();
            var8 += (long)var18;
            if (var18 >= 1024) {
               ++var7;
            }

            if (var17.wasMismatched()) {
               ++var6;
            }

            if (var17.isPermanent()) {
               ++var4;
            } else if (var17.isLocked()) {
               ++var3;
               if (var11 && var1) {
                  Iterator var19 = var17.lockedFrom.iterator();

                  while(var19.hasNext()) {
                     Throwable var20 = (Throwable)var19.next();
                     var20.printStackTrace(System.err);
                  }

                  var17.lockedFrom.clear();
               }
            }

            if (var17.isInteresting()) {
               ++var5;
            }
         } else {
            ++var2;
         }
      }

      double var21 = (double)var8 / (double)var10;
      System.err.println(var10 + " total resources being managed");
      System.err.printf("average resource age is %.1f frames\n", var21);
      printpoolpercent(var7, var10, "at maximum supported age");
      printpoolpercent(var4, var10, "marked permanent");
      printpoolpercent(var6, var10, "have had mismatched locks");
      printpoolpercent(var3, var10, "locked");
      printpoolpercent(var5, var10, "contain interesting data");
      printpoolpercent(var2, var10, "disappeared");
   }

   private static void printpoolpercent(int var0, int var1, String var2) {
      double var3 = (double)var0 * 100.0 / (double)var1;
      System.err.printf("%,d resources %s (%.1f%%)\n", var0, var2, var3);
   }

   public boolean isManagerThread() {
      return Thread.currentThread() == this.managerThread;
   }

   public final long managed() {
      return this.managedSize;
   }

   public long used() {
      return this.sharedParent != null ? this.sharedParent.used() : this.managedSize;
   }

   public final long max() {
      return this.maxSize;
   }

   public final long origTarget() {
      return this.origTarget;
   }

   public final long target() {
      return this.curTarget;
   }

   public final void setTarget(long var1) {
      if (var1 > this.maxSize) {
         throw new IllegalArgumentException("New target " + var1 + " larger than max " + this.maxSize);
      } else if (var1 < this.origTarget) {
         throw new IllegalArgumentException("New target " + var1 + " smaller than initial target " + this.origTarget);
      } else {
         this.curTarget = var1;
      }
   }

   public boolean prepareForAllocation(long var1) {
      return this.cleanup(var1);
   }

   public final void recordAllocated(long var1) {
      this.managedSize += var1;
   }

   public final void resourceManaged(ManagedResource var1) {
      long var2 = this.size(var1.resource);
      this.resourceHead.insert(var1, var2);
      this.recordAllocated(var2);
   }

   public final void resourceFreed(ManagedResource var1) {
      WeakLinkedList var2 = this.resourceHead;
      WeakLinkedList var3 = var2.next;

      while(true) {
         while(var3 != null) {
            ManagedResource var4 = var3.getResource();
            if (var4 != null && var4 != var1) {
               var2 = var3;
               var3 = var3.next;
            } else {
               this.recordFree(var3.size);
               var3 = var3.next;
               var2.next = var3;
               if (var4 == var1) {
                  return;
               }
            }
         }

         throw new IllegalStateException("unmanaged resource freed from pool " + this);
      }
   }

   public final void recordFree(long var1) {
      this.managedSize -= var1;
      if (this.managedSize < 0L) {
         throw new IllegalStateException("Negative resource amount");
      }
   }

   static {
      stageTesters[0] = (var0) -> {
         return !var0.isInteresting() && var0.getAge() > 1024;
      };
      stageReasons[0] = "Pruning unuseful older than 1024";
      stageTesters[1] = (var0) -> {
         return !var0.isInteresting() && var0.getAge() > 512;
      };
      stageReasons[1] = "Pruning unuseful older than 512";
      stageTesters[2] = (var0) -> {
         return !var0.isInteresting() && var0.getAge() > 10;
      };
      stageReasons[2] = "Pruning unuseful older than 10";
      stageTesters[3] = (var0) -> {
         return var0.getAge() > 1024;
      };
      stageReasons[3] = "Pruning all older than 1024";
      stageTesters[4] = (var0) -> {
         return var0.getAge() > 512;
      };
      stageReasons[4] = "Pruning all older than 512";
      stageTesters[5] = (var0) -> {
         return var0.getAge() > 100;
      };
      stageReasons[5] = "Pruning all older than 100";
   }

   static class WeakLinkedList {
      final WeakReference theResourceRef;
      final long size;
      WeakLinkedList next;

      WeakLinkedList() {
         this.theResourceRef = null;
         this.size = 0L;
      }

      WeakLinkedList(ManagedResource var1, long var2, WeakLinkedList var4) {
         this.theResourceRef = new WeakReference(var1);
         this.size = var2;
         this.next = var4;
      }

      void insert(ManagedResource var1, long var2) {
         this.next = new WeakLinkedList(var1, var2, this.next);
      }

      ManagedResource getResource() {
         return (ManagedResource)this.theResourceRef.get();
      }
   }

   interface Predicate {
      boolean test(ManagedResource var1);
   }
}
