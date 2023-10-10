package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class EventBus {
   private static final LoadingCache flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader() {
      public Set load(Class var1) {
         return TypeToken.of(var1).getTypes().rawTypes();
      }

      public Object load(Object var1) throws Exception {
         return this.load((Class)var1);
      }
   });
   private final SetMultimap subscribersByType;
   private final ReadWriteLock subscribersByTypeLock;
   private final SubscriberFindingStrategy finder;
   private final ThreadLocal eventsToDispatch;
   private final ThreadLocal isDispatching;
   private SubscriberExceptionHandler subscriberExceptionHandler;

   public EventBus() {
      this("default");
   }

   public EventBus(String var1) {
      this((SubscriberExceptionHandler)(new EventBus.LoggingSubscriberExceptionHandler(var1)));
   }

   public EventBus(SubscriberExceptionHandler var1) {
      this.subscribersByType = HashMultimap.create();
      this.subscribersByTypeLock = new ReentrantReadWriteLock();
      this.finder = new AnnotatedSubscriberFinder();
      this.eventsToDispatch = new ThreadLocal(this) {
         final EventBus this$0;

         {
            this.this$0 = var1;
         }

         protected Queue initialValue() {
            return new LinkedList();
         }

         protected Object initialValue() {
            return this.initialValue();
         }
      };
      this.isDispatching = new ThreadLocal(this) {
         final EventBus this$0;

         {
            this.this$0 = var1;
         }

         protected Boolean initialValue() {
            return false;
         }

         protected Object initialValue() {
            return this.initialValue();
         }
      };
      this.subscriberExceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(var1);
   }

   public void register(Object var1) {
      Multimap var2 = this.finder.findAllSubscribers(var1);
      this.subscribersByTypeLock.writeLock().lock();
      this.subscribersByType.putAll(var2);
      this.subscribersByTypeLock.writeLock().unlock();
   }

   public void unregister(Object var1) {
      Multimap var2 = this.finder.findAllSubscribers(var1);
      Iterator var3 = var2.asMap().entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         Class var5 = (Class)var4.getKey();
         Collection var6 = (Collection)var4.getValue();
         this.subscribersByTypeLock.writeLock().lock();
         Set var7 = this.subscribersByType.get(var5);
         if (!var7.containsAll(var6)) {
            throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + var1 + " registered?");
         }

         var7.removeAll(var6);
         this.subscribersByTypeLock.writeLock().unlock();
      }

   }

   public void post(Object var1) {
      Set var2 = this.flattenHierarchy(var1.getClass());
      boolean var3 = false;

      for(Iterator var4 = var2.iterator(); var4.hasNext(); this.subscribersByTypeLock.readLock().unlock()) {
         Class var5 = (Class)var4.next();
         this.subscribersByTypeLock.readLock().lock();
         Set var6 = this.subscribersByType.get(var5);
         if (!var6.isEmpty()) {
            var3 = true;
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               EventSubscriber var8 = (EventSubscriber)var7.next();
               this.enqueueEvent(var1, var8);
            }
         }
      }

      if (!var3 && !(var1 instanceof DeadEvent)) {
         this.post(new DeadEvent(this, var1));
      }

      this.dispatchQueuedEvents();
   }

   void enqueueEvent(Object var1, EventSubscriber var2) {
      ((Queue)this.eventsToDispatch.get()).offer(new EventBus.EventWithSubscriber(var1, var2));
   }

   void dispatchQueuedEvents() {
      if (!(Boolean)this.isDispatching.get()) {
         this.isDispatching.set(true);
         Queue var1 = (Queue)this.eventsToDispatch.get();

         EventBus.EventWithSubscriber var2;
         while((var2 = (EventBus.EventWithSubscriber)var1.poll()) != null) {
            this.dispatch(var2.event, var2.subscriber);
         }

         this.isDispatching.remove();
         this.eventsToDispatch.remove();
      }
   }

   void dispatch(Object var1, EventSubscriber var2) {
      try {
         var2.handleEvent(var1);
      } catch (InvocationTargetException var6) {
         InvocationTargetException var3 = var6;

         try {
            this.subscriberExceptionHandler.handleException(var3.getCause(), new SubscriberExceptionContext(this, var1, var2.getSubscriber(), var2.getMethod()));
         } catch (Throwable var5) {
            Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", var5, var6.getCause()), var5);
         }
      }

   }

   @VisibleForTesting
   Set flattenHierarchy(Class var1) {
      try {
         return (Set)flattenHierarchyCache.getUnchecked(var1);
      } catch (UncheckedExecutionException var3) {
         throw Throwables.propagate(var3.getCause());
      }
   }

   static class EventWithSubscriber {
      final Object event;
      final EventSubscriber subscriber;

      public EventWithSubscriber(Object var1, EventSubscriber var2) {
         this.event = Preconditions.checkNotNull(var1);
         this.subscriber = (EventSubscriber)Preconditions.checkNotNull(var2);
      }
   }

   private static final class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {
      private final Logger logger;

      public LoggingSubscriberExceptionHandler(String var1) {
         this.logger = Logger.getLogger(EventBus.class.getName() + "." + (String)Preconditions.checkNotNull(var1));
      }

      public void handleException(Throwable var1, SubscriberExceptionContext var2) {
         this.logger.log(Level.SEVERE, "Could not dispatch event: " + var2.getSubscriber() + " to " + var2.getSubscriberMethod(), var1.getCause());
      }
   }
}
