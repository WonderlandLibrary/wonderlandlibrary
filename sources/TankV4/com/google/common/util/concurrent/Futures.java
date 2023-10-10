package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
   private static final AsyncFunction DEREFERENCER = new AsyncFunction() {
      public ListenableFuture apply(ListenableFuture var1) {
         return var1;
      }

      public ListenableFuture apply(Object var1) throws Exception {
         return this.apply((ListenableFuture)var1);
      }
   };
   private static final Ordering WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function() {
      public Boolean apply(Constructor var1) {
         return Arrays.asList(var1.getParameterTypes()).contains(String.class);
      }

      public Object apply(Object var1) {
         return this.apply((Constructor)var1);
      }
   }).reverse();

   private Futures() {
   }

   public static CheckedFuture makeChecked(ListenableFuture var0, Function var1) {
      return new Futures.MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(var0), var1);
   }

   public static ListenableFuture immediateFuture(@Nullable Object var0) {
      return new Futures.ImmediateSuccessfulFuture(var0);
   }

   public static CheckedFuture immediateCheckedFuture(@Nullable Object var0) {
      return new Futures.ImmediateSuccessfulCheckedFuture(var0);
   }

   public static ListenableFuture immediateFailedFuture(Throwable var0) {
      Preconditions.checkNotNull(var0);
      return new Futures.ImmediateFailedFuture(var0);
   }

   public static ListenableFuture immediateCancelledFuture() {
      return new Futures.ImmediateCancelledFuture();
   }

   public static CheckedFuture immediateFailedCheckedFuture(Exception var0) {
      Preconditions.checkNotNull(var0);
      return new Futures.ImmediateFailedCheckedFuture(var0);
   }

   public static ListenableFuture withFallback(ListenableFuture var0, FutureFallback var1) {
      return withFallback(var0, var1, MoreExecutors.sameThreadExecutor());
   }

   public static ListenableFuture withFallback(ListenableFuture var0, FutureFallback var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      return new Futures.FallbackFuture(var0, var1, var2);
   }

   public static ListenableFuture transform(ListenableFuture var0, AsyncFunction var1) {
      return transform(var0, (AsyncFunction)var1, MoreExecutors.sameThreadExecutor());
   }

   public static ListenableFuture transform(ListenableFuture var0, AsyncFunction var1, Executor var2) {
      Futures.ChainingListenableFuture var3 = new Futures.ChainingListenableFuture(var1, var0);
      var0.addListener(var3, var2);
      return var3;
   }

   public static ListenableFuture transform(ListenableFuture var0, Function var1) {
      return transform(var0, (Function)var1, MoreExecutors.sameThreadExecutor());
   }

   public static ListenableFuture transform(ListenableFuture var0, Function var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      AsyncFunction var3 = new AsyncFunction(var1) {
         final Function val$function;

         {
            this.val$function = var1;
         }

         public ListenableFuture apply(Object var1) {
            Object var2 = this.val$function.apply(var1);
            return Futures.immediateFuture(var2);
         }
      };
      return transform(var0, var3, var2);
   }

   public static Future lazyTransform(Future var0, Function var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new Future(var0, var1) {
         final Future val$input;
         final Function val$function;

         {
            this.val$input = var1;
            this.val$function = var2;
         }

         public boolean cancel(boolean var1) {
            return this.val$input.cancel(var1);
         }

         public boolean isCancelled() {
            return this.val$input.isCancelled();
         }

         public boolean isDone() {
            return this.val$input.isDone();
         }

         public Object get() throws InterruptedException, ExecutionException {
            return this.applyTransformation(this.val$input.get());
         }

         public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
            return this.applyTransformation(this.val$input.get(var1, var3));
         }

         private Object applyTransformation(Object var1) throws ExecutionException {
            try {
               return this.val$function.apply(var1);
            } catch (Throwable var3) {
               throw new ExecutionException(var3);
            }
         }
      };
   }

   public static ListenableFuture dereference(ListenableFuture var0) {
      return transform(var0, DEREFERENCER);
   }

   @Beta
   public static ListenableFuture allAsList(ListenableFuture... var0) {
      return listFuture(ImmutableList.copyOf((Object[])var0), true, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static ListenableFuture allAsList(Iterable var0) {
      return listFuture(ImmutableList.copyOf(var0), true, MoreExecutors.sameThreadExecutor());
   }

   public static ListenableFuture nonCancellationPropagating(ListenableFuture var0) {
      return new Futures.NonCancellationPropagatingFuture(var0);
   }

   @Beta
   public static ListenableFuture successfulAsList(ListenableFuture... var0) {
      return listFuture(ImmutableList.copyOf((Object[])var0), false, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static ListenableFuture successfulAsList(Iterable var0) {
      return listFuture(ImmutableList.copyOf(var0), false, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static ImmutableList inCompletionOrder(Iterable var0) {
      ConcurrentLinkedQueue var1 = Queues.newConcurrentLinkedQueue();
      ImmutableList.Builder var2 = ImmutableList.builder();
      SerializingExecutor var3 = new SerializingExecutor(MoreExecutors.sameThreadExecutor());
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         ListenableFuture var5 = (ListenableFuture)var4.next();
         AsyncSettableFuture var6 = AsyncSettableFuture.create();
         var1.add(var6);
         var5.addListener(new Runnable(var1, var5) {
            final ConcurrentLinkedQueue val$delegates;
            final ListenableFuture val$future;

            {
               this.val$delegates = var1;
               this.val$future = var2;
            }

            public void run() {
               ((AsyncSettableFuture)this.val$delegates.remove()).setFuture(this.val$future);
            }
         }, var3);
         var2.add((Object)var6);
      }

      return var2.build();
   }

   public static void addCallback(ListenableFuture var0, FutureCallback var1) {
      addCallback(var0, var1, MoreExecutors.sameThreadExecutor());
   }

   public static void addCallback(ListenableFuture var0, FutureCallback var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      Runnable var3 = new Runnable(var0, var1) {
         final ListenableFuture val$future;
         final FutureCallback val$callback;

         {
            this.val$future = var1;
            this.val$callback = var2;
         }

         public void run() {
            Object var1;
            try {
               var1 = Uninterruptibles.getUninterruptibly(this.val$future);
            } catch (ExecutionException var3) {
               this.val$callback.onFailure(var3.getCause());
               return;
            } catch (RuntimeException var4) {
               this.val$callback.onFailure(var4);
               return;
            } catch (Error var5) {
               this.val$callback.onFailure(var5);
               return;
            }

            this.val$callback.onSuccess(var1);
         }
      };
      var0.addListener(var3, var2);
   }

   public static Object get(Future var0, Class var1) throws Exception {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(var1), "Futures.get exception type (%s) must not be a RuntimeException", var1);

      try {
         return var0.get();
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw newWithCause(var1, var3);
      } catch (ExecutionException var4) {
         wrapAndThrowExceptionOrError(var4.getCause(), var1);
         throw new AssertionError();
      }
   }

   public static Object get(Future var0, long var1, TimeUnit var3, Class var4) throws Exception {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(var4), "Futures.get exception type (%s) must not be a RuntimeException", var4);

      try {
         return var0.get(var1, var3);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw newWithCause(var4, var6);
      } catch (TimeoutException var7) {
         throw newWithCause(var4, var7);
      } catch (ExecutionException var8) {
         wrapAndThrowExceptionOrError(var8.getCause(), var4);
         throw new AssertionError();
      }
   }

   private static void wrapAndThrowExceptionOrError(Throwable var0, Class var1) throws Exception {
      if (var0 instanceof Error) {
         throw new ExecutionError((Error)var0);
      } else if (var0 instanceof RuntimeException) {
         throw new UncheckedExecutionException(var0);
      } else {
         throw newWithCause(var1, var0);
      }
   }

   public static Object getUnchecked(Future var0) {
      Preconditions.checkNotNull(var0);

      try {
         return Uninterruptibles.getUninterruptibly(var0);
      } catch (ExecutionException var2) {
         wrapAndThrowUnchecked(var2.getCause());
         throw new AssertionError();
      }
   }

   private static void wrapAndThrowUnchecked(Throwable var0) {
      if (var0 instanceof Error) {
         throw new ExecutionError((Error)var0);
      } else {
         throw new UncheckedExecutionException(var0);
      }
   }

   private static Exception newWithCause(Class var0, Throwable var1) {
      List var2 = Arrays.asList(var0.getConstructors());
      Iterator var3 = preferringStrings(var2).iterator();

      Exception var5;
      do {
         if (!var3.hasNext()) {
            throw new IllegalArgumentException("No appropriate constructor for exception of type " + var0 + " in response to chained exception", var1);
         }

         Constructor var4 = (Constructor)var3.next();
         var5 = (Exception)newFromConstructor(var4, var1);
      } while(var5 == null);

      if (var5.getCause() == null) {
         var5.initCause(var1);
      }

      return var5;
   }

   private static List preferringStrings(List var0) {
      return WITH_STRING_PARAM_FIRST.sortedCopy(var0);
   }

   @Nullable
   private static Object newFromConstructor(Constructor var0, Throwable var1) {
      Class[] var2 = var0.getParameterTypes();
      Object[] var3 = new Object[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Class var5 = var2[var4];
         if (var5.equals(String.class)) {
            var3[var4] = var1.toString();
         } else {
            if (!var5.equals(Throwable.class)) {
               return null;
            }

            var3[var4] = var1;
         }
      }

      try {
         return var0.newInstance(var3);
      } catch (IllegalArgumentException var6) {
         return null;
      } catch (InstantiationException var7) {
         return null;
      } catch (IllegalAccessException var8) {
         return null;
      } catch (InvocationTargetException var9) {
         return null;
      }
   }

   private static ListenableFuture listFuture(ImmutableList var0, boolean var1, Executor var2) {
      return new Futures.CombinedFuture(var0, var1, var2, new Futures.FutureCombiner() {
         public List combine(List var1) {
            ArrayList var2 = Lists.newArrayList();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               Optional var4 = (Optional)var3.next();
               var2.add(var4 != null ? var4.orNull() : null);
            }

            return Collections.unmodifiableList(var2);
         }

         public Object combine(List var1) {
            return this.combine(var1);
         }
      });
   }

   private static class MappingCheckedFuture extends AbstractCheckedFuture {
      final Function mapper;

      MappingCheckedFuture(ListenableFuture var1, Function var2) {
         super(var1);
         this.mapper = (Function)Preconditions.checkNotNull(var2);
      }

      protected Exception mapException(Exception var1) {
         return (Exception)this.mapper.apply(var1);
      }
   }

   private static class CombinedFuture extends AbstractFuture {
      private static final Logger logger = Logger.getLogger(Futures.CombinedFuture.class.getName());
      ImmutableCollection futures;
      final boolean allMustSucceed;
      final AtomicInteger remaining;
      Futures.FutureCombiner combiner;
      List values;
      final Object seenExceptionsLock = new Object();
      Set seenExceptions;

      CombinedFuture(ImmutableCollection var1, boolean var2, Executor var3, Futures.FutureCombiner var4) {
         this.futures = var1;
         this.allMustSucceed = var2;
         this.remaining = new AtomicInteger(var1.size());
         this.combiner = var4;
         this.values = Lists.newArrayListWithCapacity(var1.size());
         this.init(var3);
      }

      protected void init(Executor var1) {
         this.addListener(new Runnable(this) {
            final Futures.CombinedFuture this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               if (this.this$0.isCancelled()) {
                  Iterator var1 = this.this$0.futures.iterator();

                  while(var1.hasNext()) {
                     ListenableFuture var2 = (ListenableFuture)var1.next();
                     var2.cancel(this.this$0.wasInterrupted());
                  }
               }

               this.this$0.futures = null;
               this.this$0.values = null;
               this.this$0.combiner = null;
            }
         }, MoreExecutors.sameThreadExecutor());
         if (this.futures.isEmpty()) {
            this.set(this.combiner.combine(ImmutableList.of()));
         } else {
            int var2;
            for(var2 = 0; var2 < this.futures.size(); ++var2) {
               this.values.add((Object)null);
            }

            var2 = 0;
            Iterator var3 = this.futures.iterator();

            while(var3.hasNext()) {
               ListenableFuture var4 = (ListenableFuture)var3.next();
               int var5 = var2++;
               var4.addListener(new Runnable(this, var5, var4) {
                  final int val$index;
                  final ListenableFuture val$listenable;
                  final Futures.CombinedFuture this$0;

                  {
                     this.this$0 = var1;
                     this.val$index = var2;
                     this.val$listenable = var3;
                  }

                  public void run() {
                     Futures.CombinedFuture.access$400(this.this$0, this.val$index, this.val$listenable);
                  }
               }, var1);
            }

         }
      }

      private void setExceptionAndMaybeLog(Throwable var1) {
         boolean var2 = false;
         boolean var3 = true;
         if (this.allMustSucceed) {
            var2 = super.setException(var1);
            Object var4;
            synchronized(var4 = this.seenExceptionsLock){}
            if (this.seenExceptions == null) {
               this.seenExceptions = Sets.newHashSet();
            }

            var3 = this.seenExceptions.add(var1);
         }

         if (var1 instanceof Error || this.allMustSucceed && !var2 && var3) {
            logger.log(Level.SEVERE, "input future failed.", var1);
         }

      }

      private void setOneValue(int var1, Future var2) {
         List var3 = this.values;
         if (this.isDone() || var3 == null) {
            Preconditions.checkState(this.allMustSucceed || this.isCancelled(), "Future was done before all dependencies completed");
         }

         int var4;
         Futures.FutureCombiner var5;
         try {
            Preconditions.checkState(var2.isDone(), "Tried to set value from future which is not done");
            Object var12 = Uninterruptibles.getUninterruptibly(var2);
            if (var3 != null) {
               var3.set(var1, Optional.fromNullable(var12));
            }
         } catch (CancellationException var9) {
            if (this.allMustSucceed) {
               this.cancel(false);
            }

            var4 = this.remaining.decrementAndGet();
            Preconditions.checkState(var4 >= 0, "Less than 0 remaining futures");
            if (var4 == 0) {
               var5 = this.combiner;
               if (var5 != null && var3 != null) {
                  this.set(var5.combine(var3));
               } else {
                  Preconditions.checkState(this.isDone());
               }

               return;
            }

            return;
         } catch (ExecutionException var10) {
            this.setExceptionAndMaybeLog(var10.getCause());
            var4 = this.remaining.decrementAndGet();
            Preconditions.checkState(var4 >= 0, "Less than 0 remaining futures");
            if (var4 == 0) {
               var5 = this.combiner;
               if (var5 != null && var3 != null) {
                  this.set(var5.combine(var3));
               } else {
                  Preconditions.checkState(this.isDone());
               }

               return;
            }

            return;
         } catch (Throwable var11) {
            this.setExceptionAndMaybeLog(var11);
            var4 = this.remaining.decrementAndGet();
            Preconditions.checkState(var4 >= 0, "Less than 0 remaining futures");
            if (var4 == 0) {
               var5 = this.combiner;
               if (var5 != null && var3 != null) {
                  this.set(var5.combine(var3));
               } else {
                  Preconditions.checkState(this.isDone());
               }

               return;
            }

            return;
         }

         var4 = this.remaining.decrementAndGet();
         Preconditions.checkState(var4 >= 0, "Less than 0 remaining futures");
         if (var4 == 0) {
            var5 = this.combiner;
            if (var5 != null && var3 != null) {
               this.set(var5.combine(var3));
            } else {
               Preconditions.checkState(this.isDone());
            }
         }

      }

      static void access$400(Futures.CombinedFuture var0, int var1, Future var2) {
         var0.setOneValue(var1, var2);
      }
   }

   private interface FutureCombiner {
      Object combine(List var1);
   }

   private static class NonCancellationPropagatingFuture extends AbstractFuture {
      NonCancellationPropagatingFuture(ListenableFuture var1) {
         Preconditions.checkNotNull(var1);
         Futures.addCallback(var1, new FutureCallback(this, var1) {
            final ListenableFuture val$delegate;
            final Futures.NonCancellationPropagatingFuture this$0;

            {
               this.this$0 = var1;
               this.val$delegate = var2;
            }

            public void onSuccess(Object var1) {
               this.this$0.set(var1);
            }

            public void onFailure(Throwable var1) {
               if (this.val$delegate.isCancelled()) {
                  this.this$0.cancel(false);
               } else {
                  this.this$0.setException(var1);
               }

            }
         }, MoreExecutors.sameThreadExecutor());
      }
   }

   private static class ChainingListenableFuture extends AbstractFuture implements Runnable {
      private AsyncFunction function;
      private ListenableFuture inputFuture;
      private volatile ListenableFuture outputFuture;
      private final CountDownLatch outputCreated;

      private ChainingListenableFuture(AsyncFunction var1, ListenableFuture var2) {
         this.outputCreated = new CountDownLatch(1);
         this.function = (AsyncFunction)Preconditions.checkNotNull(var1);
         this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(var2);
      }

      public boolean cancel(boolean var1) {
         if (super.cancel(var1)) {
            this.cancel(this.inputFuture, var1);
            this.cancel(this.outputFuture, var1);
            return true;
         } else {
            return false;
         }
      }

      private void cancel(@Nullable Future var1, boolean var2) {
         if (var1 != null) {
            var1.cancel(var2);
         }

      }

      public void run() {
         label44: {
            label43: {
               label54: {
                  try {
                     Object var1;
                     try {
                        var1 = Uninterruptibles.getUninterruptibly(this.inputFuture);
                     } catch (CancellationException var4) {
                        this.cancel(false);
                        break label44;
                     } catch (ExecutionException var5) {
                        this.setException(var5.getCause());
                        break label43;
                     }

                     ListenableFuture var2 = this.outputFuture = (ListenableFuture)Preconditions.checkNotNull(this.function.apply(var1), "AsyncFunction may not return null.");
                     if (this.isCancelled()) {
                        var2.cancel(this.wasInterrupted());
                        this.outputFuture = null;
                        break label54;
                     }

                     var2.addListener(new Runnable(this, var2) {
                        final ListenableFuture val$outputFuture;
                        final Futures.ChainingListenableFuture this$0;

                        {
                           this.this$0 = var1;
                           this.val$outputFuture = var2;
                        }

                        public void run() {
                           try {
                              this.this$0.set(Uninterruptibles.getUninterruptibly(this.val$outputFuture));
                           } catch (CancellationException var3) {
                              this.this$0.cancel(false);
                              Futures.ChainingListenableFuture.access$302(this.this$0, (ListenableFuture)null);
                              return;
                           } catch (ExecutionException var4) {
                              this.this$0.setException(var4.getCause());
                              Futures.ChainingListenableFuture.access$302(this.this$0, (ListenableFuture)null);
                              return;
                           }

                           Futures.ChainingListenableFuture.access$302(this.this$0, (ListenableFuture)null);
                        }
                     }, MoreExecutors.sameThreadExecutor());
                  } catch (UndeclaredThrowableException var6) {
                     this.setException(var6.getCause());
                     this.function = null;
                     this.inputFuture = null;
                     this.outputCreated.countDown();
                     return;
                  } catch (Throwable var7) {
                     this.setException(var7);
                     this.function = null;
                     this.inputFuture = null;
                     this.outputCreated.countDown();
                     return;
                  }

                  this.function = null;
                  this.inputFuture = null;
                  this.outputCreated.countDown();
                  return;
               }

               this.function = null;
               this.inputFuture = null;
               this.outputCreated.countDown();
               return;
            }

            this.function = null;
            this.inputFuture = null;
            this.outputCreated.countDown();
            return;
         }

         this.function = null;
         this.inputFuture = null;
         this.outputCreated.countDown();
      }

      ChainingListenableFuture(AsyncFunction var1, ListenableFuture var2, Object var3) {
         this(var1, var2);
      }

      static ListenableFuture access$302(Futures.ChainingListenableFuture var0, ListenableFuture var1) {
         return var0.outputFuture = var1;
      }
   }

   private static class FallbackFuture extends AbstractFuture {
      private volatile ListenableFuture running;

      FallbackFuture(ListenableFuture var1, FutureFallback var2, Executor var3) {
         this.running = var1;
         Futures.addCallback(this.running, new FutureCallback(this, var2) {
            final FutureFallback val$fallback;
            final Futures.FallbackFuture this$0;

            {
               this.this$0 = var1;
               this.val$fallback = var2;
            }

            public void onSuccess(Object var1) {
               this.this$0.set(var1);
            }

            public void onFailure(Throwable var1) {
               if (!this.this$0.isCancelled()) {
                  try {
                     Futures.FallbackFuture.access$102(this.this$0, this.val$fallback.create(var1));
                     if (this.this$0.isCancelled()) {
                        Futures.FallbackFuture.access$100(this.this$0).cancel(this.this$0.wasInterrupted());
                        return;
                     }

                     Futures.addCallback(Futures.FallbackFuture.access$100(this.this$0), new FutureCallback(this) {
                        final <undefinedtype> this$1;

                        {
                           this.this$1 = var1;
                        }

                        public void onSuccess(Object var1) {
                           this.this$1.this$0.set(var1);
                        }

                        public void onFailure(Throwable var1) {
                           if (Futures.FallbackFuture.access$100(this.this$1.this$0).isCancelled()) {
                              this.this$1.this$0.cancel(false);
                           } else {
                              this.this$1.this$0.setException(var1);
                           }

                        }
                     }, MoreExecutors.sameThreadExecutor());
                  } catch (Throwable var3) {
                     this.this$0.setException(var3);
                  }

               }
            }
         }, var3);
      }

      public boolean cancel(boolean var1) {
         if (super.cancel(var1)) {
            this.running.cancel(var1);
            return true;
         } else {
            return false;
         }
      }

      static ListenableFuture access$102(Futures.FallbackFuture var0, ListenableFuture var1) {
         return var0.running = var1;
      }

      static ListenableFuture access$100(Futures.FallbackFuture var0) {
         return var0.running;
      }
   }

   private static class ImmediateFailedCheckedFuture extends Futures.ImmediateFuture implements CheckedFuture {
      private final Exception thrown;

      ImmediateFailedCheckedFuture(Exception var1) {
         super(null);
         this.thrown = var1;
      }

      public Object get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }

      public Object checkedGet() throws Exception {
         throw this.thrown;
      }

      public Object checkedGet(long var1, TimeUnit var3) throws Exception {
         Preconditions.checkNotNull(var3);
         throw this.thrown;
      }
   }

   private static class ImmediateCancelledFuture extends Futures.ImmediateFuture {
      private final CancellationException thrown = new CancellationException("Immediate cancelled future.");

      ImmediateCancelledFuture() {
         super(null);
      }

      public boolean isCancelled() {
         return true;
      }

      public Object get() {
         throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
      }
   }

   private static class ImmediateFailedFuture extends Futures.ImmediateFuture {
      private final Throwable thrown;

      ImmediateFailedFuture(Throwable var1) {
         super(null);
         this.thrown = var1;
      }

      public Object get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }
   }

   private static class ImmediateSuccessfulCheckedFuture extends Futures.ImmediateFuture implements CheckedFuture {
      @Nullable
      private final Object value;

      ImmediateSuccessfulCheckedFuture(@Nullable Object var1) {
         super(null);
         this.value = var1;
      }

      public Object get() {
         return this.value;
      }

      public Object checkedGet() {
         return this.value;
      }

      public Object checkedGet(long var1, TimeUnit var3) {
         Preconditions.checkNotNull(var3);
         return this.value;
      }
   }

   private static class ImmediateSuccessfulFuture extends Futures.ImmediateFuture {
      @Nullable
      private final Object value;

      ImmediateSuccessfulFuture(@Nullable Object var1) {
         super(null);
         this.value = var1;
      }

      public Object get() {
         return this.value;
      }
   }

   private abstract static class ImmediateFuture implements ListenableFuture {
      private static final Logger log = Logger.getLogger(Futures.ImmediateFuture.class.getName());

      private ImmediateFuture() {
      }

      public void addListener(Runnable var1, Executor var2) {
         Preconditions.checkNotNull(var1, "Runnable was null.");
         Preconditions.checkNotNull(var2, "Executor was null.");

         try {
            var2.execute(var1);
         } catch (RuntimeException var4) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + var1 + " with executor " + var2, var4);
         }

      }

      public boolean cancel(boolean var1) {
         return false;
      }

      public abstract Object get() throws ExecutionException;

      public Object get(long var1, TimeUnit var3) throws ExecutionException {
         Preconditions.checkNotNull(var3);
         return this.get();
      }

      public boolean isCancelled() {
         return false;
      }

      public boolean isDone() {
         return true;
      }

      ImmediateFuture(Object var1) {
         this();
      }
   }
}
