package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@Beta
@ThreadSafe
public class CycleDetectingLockFactory {
   private static final ConcurrentMap lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
   final CycleDetectingLockFactory.Policy policy;
   private static final ThreadLocal acquiredLocks = new ThreadLocal() {
      protected ArrayList initialValue() {
         return Lists.newArrayListWithCapacity(3);
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };

   public static CycleDetectingLockFactory newInstance(CycleDetectingLockFactory.Policy var0) {
      return new CycleDetectingLockFactory(var0);
   }

   public ReentrantLock newReentrantLock(String var1) {
      return this.newReentrantLock(var1, false);
   }

   public ReentrantLock newReentrantLock(String var1, boolean var2) {
      return (ReentrantLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(var2) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, new CycleDetectingLockFactory.LockGraphNode(var1), var2));
   }

   public ReentrantReadWriteLock newReentrantReadWriteLock(String var1) {
      return this.newReentrantReadWriteLock(var1, false);
   }

   public ReentrantReadWriteLock newReentrantReadWriteLock(String var1, boolean var2) {
      return (ReentrantReadWriteLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(var2) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, new CycleDetectingLockFactory.LockGraphNode(var1), var2));
   }

   public static CycleDetectingLockFactory.WithExplicitOrdering newInstanceWithExplicitOrdering(Class var0, CycleDetectingLockFactory.Policy var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Map var2 = getOrCreateNodes(var0);
      return new CycleDetectingLockFactory.WithExplicitOrdering(var1, var2);
   }

   private static Map getOrCreateNodes(Class var0) {
      Map var1 = (Map)lockGraphNodesPerType.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         Map var2 = createNodes(var0);
         var1 = (Map)lockGraphNodesPerType.putIfAbsent(var0, var2);
         return (Map)Objects.firstNonNull(var1, var2);
      }
   }

   @VisibleForTesting
   static Map createNodes(Class var0) {
      EnumMap var1 = Maps.newEnumMap(var0);
      Enum[] var2 = (Enum[])var0.getEnumConstants();
      int var3 = var2.length;
      ArrayList var4 = Lists.newArrayListWithCapacity(var3);
      Enum[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Enum var8 = var5[var7];
         CycleDetectingLockFactory.LockGraphNode var9 = new CycleDetectingLockFactory.LockGraphNode(getLockName(var8));
         var4.add(var9);
         var1.put(var8, var9);
      }

      int var10;
      for(var10 = 1; var10 < var3; ++var10) {
         ((CycleDetectingLockFactory.LockGraphNode)var4.get(var10)).checkAcquiredLocks(CycleDetectingLockFactory.Policies.THROW, var4.subList(0, var10));
      }

      for(var10 = 0; var10 < var3 - 1; ++var10) {
         ((CycleDetectingLockFactory.LockGraphNode)var4.get(var10)).checkAcquiredLocks(CycleDetectingLockFactory.Policies.DISABLED, var4.subList(var10 + 1, var3));
      }

      return Collections.unmodifiableMap(var1);
   }

   private static String getLockName(Enum var0) {
      return var0.getDeclaringClass().getSimpleName() + "." + var0.name();
   }

   private CycleDetectingLockFactory(CycleDetectingLockFactory.Policy var1) {
      this.policy = (CycleDetectingLockFactory.Policy)Preconditions.checkNotNull(var1);
   }

   private void aboutToAcquire(CycleDetectingLockFactory.CycleDetectingLock var1) {
      if (!var1.isAcquiredByCurrentThread()) {
         ArrayList var2 = (ArrayList)acquiredLocks.get();
         CycleDetectingLockFactory.LockGraphNode var3 = var1.getLockGraphNode();
         var3.checkAcquiredLocks(this.policy, var2);
         var2.add(var3);
      }

   }

   private void lockStateChanged(CycleDetectingLockFactory.CycleDetectingLock var1) {
      if (!var1.isAcquiredByCurrentThread()) {
         ArrayList var2 = (ArrayList)acquiredLocks.get();
         CycleDetectingLockFactory.LockGraphNode var3 = var1.getLockGraphNode();

         for(int var4 = var2.size() - 1; var4 >= 0; --var4) {
            if (var2.get(var4) == var3) {
               var2.remove(var4);
               break;
            }
         }
      }

   }

   static Logger access$100() {
      return logger;
   }

   CycleDetectingLockFactory(CycleDetectingLockFactory.Policy var1, Object var2) {
      this(var1);
   }

   static void access$600(CycleDetectingLockFactory var0, CycleDetectingLockFactory.CycleDetectingLock var1) {
      var0.aboutToAcquire(var1);
   }

   static void access$700(CycleDetectingLockFactory var0, CycleDetectingLockFactory.CycleDetectingLock var1) {
      var0.lockStateChanged(var1);
   }

   private class CycleDetectingReentrantWriteLock extends WriteLock {
      final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
      final CycleDetectingLockFactory this$0;

      CycleDetectingReentrantWriteLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock var2) {
         super(var2);
         this.this$0 = var1;
         this.readWriteLock = var2;
      }

      public void lock() {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         super.lock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         super.lockInterruptibly();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }

      public boolean tryLock() {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         boolean var1 = super.tryLock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
         return var1;
      }

      public boolean tryLock(long var1, TimeUnit var3) throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         boolean var4 = super.tryLock(var1, var3);
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
         return var4;
      }

      public void unlock() {
         super.unlock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }
   }

   private class CycleDetectingReentrantReadLock extends ReadLock {
      final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
      final CycleDetectingLockFactory this$0;

      CycleDetectingReentrantReadLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock var2) {
         super(var2);
         this.this$0 = var1;
         this.readWriteLock = var2;
      }

      public void lock() {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         super.lock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         super.lockInterruptibly();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }

      public boolean tryLock() {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         boolean var1 = super.tryLock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
         return var1;
      }

      public boolean tryLock(long var1, TimeUnit var3) throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this.readWriteLock);
         boolean var4 = super.tryLock(var1, var3);
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
         return var4;
      }

      public void unlock() {
         super.unlock();
         CycleDetectingLockFactory.access$700(this.this$0, this.readWriteLock);
      }
   }

   final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLockFactory.CycleDetectingLock {
      private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
      private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
      private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
      final CycleDetectingLockFactory this$0;

      private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.LockGraphNode var2, boolean var3) {
         super(var3);
         this.this$0 = var1;
         this.readLock = var1.new CycleDetectingReentrantReadLock(var1, this);
         this.writeLock = var1.new CycleDetectingReentrantWriteLock(var1, this);
         this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(var2);
      }

      public ReadLock readLock() {
         return this.readLock;
      }

      public WriteLock writeLock() {
         return this.writeLock;
      }

      public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
         return this.lockGraphNode;
      }

      public boolean isAcquiredByCurrentThread() {
         return this.isWriteLockedByCurrentThread() || this.getReadHoldCount() > 0;
      }

      public Lock writeLock() {
         return this.writeLock();
      }

      public Lock readLock() {
         return this.readLock();
      }

      CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.LockGraphNode var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLockFactory.CycleDetectingLock {
      private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
      final CycleDetectingLockFactory this$0;

      private CycleDetectingReentrantLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.LockGraphNode var2, boolean var3) {
         super(var3);
         this.this$0 = var1;
         this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(var2);
      }

      public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
         return this.lockGraphNode;
      }

      public boolean isAcquiredByCurrentThread() {
         return this.isHeldByCurrentThread();
      }

      public void lock() {
         CycleDetectingLockFactory.access$600(this.this$0, this);
         super.lock();
         CycleDetectingLockFactory.access$700(this.this$0, this);
      }

      public void lockInterruptibly() throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this);
         super.lockInterruptibly();
         CycleDetectingLockFactory.access$700(this.this$0, this);
      }

      public boolean tryLock() {
         CycleDetectingLockFactory.access$600(this.this$0, this);
         boolean var1 = super.tryLock();
         CycleDetectingLockFactory.access$700(this.this$0, this);
         return var1;
      }

      public boolean tryLock(long var1, TimeUnit var3) throws InterruptedException {
         CycleDetectingLockFactory.access$600(this.this$0, this);
         boolean var4 = super.tryLock(var1, var3);
         CycleDetectingLockFactory.access$700(this.this$0, this);
         return var4;
      }

      public void unlock() {
         super.unlock();
         CycleDetectingLockFactory.access$700(this.this$0, this);
      }

      CycleDetectingReentrantLock(CycleDetectingLockFactory var1, CycleDetectingLockFactory.LockGraphNode var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class LockGraphNode {
      final Map allowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
      final Map disallowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
      final String lockName;

      LockGraphNode(String var1) {
         this.lockName = (String)Preconditions.checkNotNull(var1);
      }

      String getLockName() {
         return this.lockName;
      }

      void checkAcquiredLocks(CycleDetectingLockFactory.Policy var1, List var2) {
         int var3 = 0;

         for(int var4 = var2.size(); var3 < var4; ++var3) {
            this.checkAcquiredLock(var1, (CycleDetectingLockFactory.LockGraphNode)var2.get(var3));
         }

      }

      void checkAcquiredLock(CycleDetectingLockFactory.Policy var1, CycleDetectingLockFactory.LockGraphNode var2) {
         Preconditions.checkState(this != var2, "Attempted to acquire multiple locks with the same rank " + var2.getLockName());
         if (!this.allowedPriorLocks.containsKey(var2)) {
            CycleDetectingLockFactory.PotentialDeadlockException var3 = (CycleDetectingLockFactory.PotentialDeadlockException)this.disallowedPriorLocks.get(var2);
            if (var3 != null) {
               CycleDetectingLockFactory.PotentialDeadlockException var7 = new CycleDetectingLockFactory.PotentialDeadlockException(var2, this, var3.getConflictingStackTrace());
               var1.handlePotentialDeadlock(var7);
            } else {
               Set var4 = Sets.newIdentityHashSet();
               CycleDetectingLockFactory.ExampleStackTrace var5 = var2.findPathTo(this, var4);
               if (var5 == null) {
                  this.allowedPriorLocks.put(var2, new CycleDetectingLockFactory.ExampleStackTrace(var2, this));
               } else {
                  CycleDetectingLockFactory.PotentialDeadlockException var6 = new CycleDetectingLockFactory.PotentialDeadlockException(var2, this, var5);
                  this.disallowedPriorLocks.put(var2, var6);
                  var1.handlePotentialDeadlock(var6);
               }

            }
         }
      }

      @Nullable
      private CycleDetectingLockFactory.ExampleStackTrace findPathTo(CycleDetectingLockFactory.LockGraphNode var1, Set var2) {
         if (!var2.add(this)) {
            return null;
         } else {
            CycleDetectingLockFactory.ExampleStackTrace var3 = (CycleDetectingLockFactory.ExampleStackTrace)this.allowedPriorLocks.get(var1);
            if (var3 != null) {
               return var3;
            } else {
               Iterator var4 = this.allowedPriorLocks.entrySet().iterator();

               Entry var5;
               CycleDetectingLockFactory.LockGraphNode var6;
               do {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  var5 = (Entry)var4.next();
                  var6 = (CycleDetectingLockFactory.LockGraphNode)var5.getKey();
                  var3 = var6.findPathTo(var1, var2);
               } while(var3 == null);

               CycleDetectingLockFactory.ExampleStackTrace var7 = new CycleDetectingLockFactory.ExampleStackTrace(var6, this);
               var7.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)var5.getValue()).getStackTrace());
               var7.initCause(var3);
               return var7;
            }
         }
      }
   }

   private interface CycleDetectingLock {
      CycleDetectingLockFactory.LockGraphNode getLockGraphNode();

      boolean isAcquiredByCurrentThread();
   }

   @Beta
   public static final class PotentialDeadlockException extends CycleDetectingLockFactory.ExampleStackTrace {
      private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;

      private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode var1, CycleDetectingLockFactory.LockGraphNode var2, CycleDetectingLockFactory.ExampleStackTrace var3) {
         super(var1, var2);
         this.conflictingStackTrace = var3;
         this.initCause(var3);
      }

      public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
         return this.conflictingStackTrace;
      }

      public String getMessage() {
         StringBuilder var1 = new StringBuilder(super.getMessage());

         for(Object var2 = this.conflictingStackTrace; var2 != null; var2 = ((Throwable)var2).getCause()) {
            var1.append(", ").append(((Throwable)var2).getMessage());
         }

         return var1.toString();
      }

      PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode var1, CycleDetectingLockFactory.LockGraphNode var2, CycleDetectingLockFactory.ExampleStackTrace var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class ExampleStackTrace extends IllegalStateException {
      static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
      static Set EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), CycleDetectingLockFactory.ExampleStackTrace.class.getName(), CycleDetectingLockFactory.LockGraphNode.class.getName());

      ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode var1, CycleDetectingLockFactory.LockGraphNode var2) {
         super(var1.getLockName() + " -> " + var2.getLockName());
         StackTraceElement[] var3 = this.getStackTrace();
         int var4 = 0;

         for(int var5 = var3.length; var4 < var5; ++var4) {
            if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(var3[var4].getClassName())) {
               this.setStackTrace(EMPTY_STACK_TRACE);
               break;
            }

            if (!EXCLUDED_CLASS_NAMES.contains(var3[var4].getClassName())) {
               this.setStackTrace((StackTraceElement[])Arrays.copyOfRange(var3, var4, var5));
               break;
            }
         }

      }
   }

   @Beta
   public static final class WithExplicitOrdering extends CycleDetectingLockFactory {
      private final Map lockGraphNodes;

      @VisibleForTesting
      WithExplicitOrdering(CycleDetectingLockFactory.Policy var1, Map var2) {
         super(var1, null);
         this.lockGraphNodes = var2;
      }

      public ReentrantLock newReentrantLock(Enum var1) {
         return this.newReentrantLock(var1, false);
      }

      public ReentrantLock newReentrantLock(Enum var1, boolean var2) {
         return (ReentrantLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(var2) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(var1), var2));
      }

      public ReentrantReadWriteLock newReentrantReadWriteLock(Enum var1) {
         return this.newReentrantReadWriteLock(var1, false);
      }

      public ReentrantReadWriteLock newReentrantReadWriteLock(Enum var1, boolean var2) {
         return (ReentrantReadWriteLock)(this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(var2) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(var1), var2));
      }
   }

   @Beta
   public static enum Policies implements CycleDetectingLockFactory.Policy {
      THROW {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException var1) {
            throw var1;
         }
      },
      WARN {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException var1) {
            CycleDetectingLockFactory.access$100().log(Level.SEVERE, "Detected potential deadlock", var1);
         }
      },
      DISABLED {
         public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException var1) {
         }
      };

      private static final CycleDetectingLockFactory.Policies[] $VALUES = new CycleDetectingLockFactory.Policies[]{THROW, WARN, DISABLED};

      private Policies() {
      }

      Policies(Object var3) {
         this();
      }
   }

   @Beta
   @ThreadSafe
   public interface Policy {
      void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException var1);
   }
}
