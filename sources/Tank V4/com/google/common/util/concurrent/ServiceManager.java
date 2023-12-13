package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class ServiceManager {
   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
   private static final ListenerCallQueue.Callback HEALTHY_CALLBACK = new ListenerCallQueue.Callback("healthy()") {
      void call(ServiceManager.Listener var1) {
         var1.healthy();
      }

      void call(Object var1) {
         this.call((ServiceManager.Listener)var1);
      }
   };
   private static final ListenerCallQueue.Callback STOPPED_CALLBACK = new ListenerCallQueue.Callback("stopped()") {
      void call(ServiceManager.Listener var1) {
         var1.stopped();
      }

      void call(Object var1) {
         this.call((ServiceManager.Listener)var1);
      }
   };
   private final ServiceManager.ServiceManagerState state;
   private final ImmutableList services;

   public ServiceManager(Iterable var1) {
      ImmutableList var2 = ImmutableList.copyOf(var1);
      if (var2.isEmpty()) {
         logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new ServiceManager.EmptyServiceManagerWarning());
         var2 = ImmutableList.of(new ServiceManager.NoOpService());
      }

      this.state = new ServiceManager.ServiceManagerState(var2);
      this.services = var2;
      WeakReference var3 = new WeakReference(this.state);
      ListeningExecutorService var4 = MoreExecutors.sameThreadExecutor();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Service var6 = (Service)var5.next();
         var6.addListener(new ServiceManager.ServiceListener(var6, var3), var4);
         Preconditions.checkArgument(var6.state() == Service.State.NEW, "Can only manage NEW services, %s", var6);
      }

      this.state.markReady();
   }

   public void addListener(ServiceManager.Listener var1, Executor var2) {
      this.state.addListener(var1, var2);
   }

   public void addListener(ServiceManager.Listener var1) {
      this.state.addListener(var1, MoreExecutors.sameThreadExecutor());
   }

   public ServiceManager startAsync() {
      Iterator var1 = this.services.iterator();

      Service var2;
      while(var1.hasNext()) {
         var2 = (Service)var1.next();
         Service.State var3 = var2.state();
         Preconditions.checkState(var3 == Service.State.NEW, "Service %s is %s, cannot start it.", var2, var3);
      }

      var1 = this.services.iterator();

      while(var1.hasNext()) {
         var2 = (Service)var1.next();

         try {
            var2.startAsync();
         } catch (IllegalStateException var4) {
            logger.log(Level.WARNING, "Unable to start Service " + var2, var4);
         }
      }

      return this;
   }

   public void awaitHealthy() {
      this.state.awaitHealthy();
   }

   public void awaitHealthy(long var1, TimeUnit var3) throws TimeoutException {
      this.state.awaitHealthy(var1, var3);
   }

   public ServiceManager stopAsync() {
      Iterator var1 = this.services.iterator();

      while(var1.hasNext()) {
         Service var2 = (Service)var1.next();
         var2.stopAsync();
      }

      return this;
   }

   public void awaitStopped() {
      this.state.awaitStopped();
   }

   public void awaitStopped(long var1, TimeUnit var3) throws TimeoutException {
      this.state.awaitStopped(var1, var3);
   }

   public boolean isHealthy() {
      Iterator var1 = this.services.iterator();

      Service var2;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         var2 = (Service)var1.next();
      } while(var2.isRunning());

      return false;
   }

   public ImmutableMultimap servicesByState() {
      return this.state.servicesByState();
   }

   public ImmutableMap startupTimes() {
      return this.state.startupTimes();
   }

   public String toString() {
      return Objects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(ServiceManager.NoOpService.class)))).toString();
   }

   static Logger access$200() {
      return logger;
   }

   static ListenerCallQueue.Callback access$300() {
      return STOPPED_CALLBACK;
   }

   static ListenerCallQueue.Callback access$400() {
      return HEALTHY_CALLBACK;
   }

   private static final class EmptyServiceManagerWarning extends Throwable {
      private EmptyServiceManagerWarning() {
      }

      EmptyServiceManagerWarning(Object var1) {
         this();
      }
   }

   private static final class NoOpService extends AbstractService {
      private NoOpService() {
      }

      protected void doStart() {
         this.notifyStarted();
      }

      protected void doStop() {
         this.notifyStopped();
      }

      NoOpService(Object var1) {
         this();
      }
   }

   private static final class ServiceListener extends Service.Listener {
      final Service service;
      final WeakReference state;

      ServiceListener(Service var1, WeakReference var2) {
         this.service = var1;
         this.state = var2;
      }

      public void starting() {
         ServiceManager.ServiceManagerState var1 = (ServiceManager.ServiceManagerState)this.state.get();
         if (var1 != null) {
            var1.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
            if (!(this.service instanceof ServiceManager.NoOpService)) {
               ServiceManager.access$200().log(Level.FINE, "Starting {0}.", this.service);
            }
         }

      }

      public void running() {
         ServiceManager.ServiceManagerState var1 = (ServiceManager.ServiceManagerState)this.state.get();
         if (var1 != null) {
            var1.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
         }

      }

      public void stopping(Service.State var1) {
         ServiceManager.ServiceManagerState var2 = (ServiceManager.ServiceManagerState)this.state.get();
         if (var2 != null) {
            var2.transitionService(this.service, var1, Service.State.STOPPING);
         }

      }

      public void terminated(Service.State var1) {
         ServiceManager.ServiceManagerState var2 = (ServiceManager.ServiceManagerState)this.state.get();
         if (var2 != null) {
            if (!(this.service instanceof ServiceManager.NoOpService)) {
               ServiceManager.access$200().log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[]{this.service, var1});
            }

            var2.transitionService(this.service, var1, Service.State.TERMINATED);
         }

      }

      public void failed(Service.State var1, Throwable var2) {
         ServiceManager.ServiceManagerState var3 = (ServiceManager.ServiceManagerState)this.state.get();
         if (var3 != null) {
            if (!(this.service instanceof ServiceManager.NoOpService)) {
               ServiceManager.access$200().log(Level.SEVERE, "Service " + this.service + " has failed in the " + var1 + " state.", var2);
            }

            var3.transitionService(this.service, var1, Service.State.FAILED);
         }

      }
   }

   private static final class ServiceManagerState {
      final Monitor monitor = new Monitor();
      @GuardedBy("monitor")
      final SetMultimap servicesByState = Multimaps.newSetMultimap(new EnumMap(Service.State.class), new Supplier(this) {
         final ServiceManager.ServiceManagerState this$0;

         {
            this.this$0 = var1;
         }

         public Set get() {
            return Sets.newLinkedHashSet();
         }

         public Object get() {
            return this.get();
         }
      });
      @GuardedBy("monitor")
      final Multiset states;
      @GuardedBy("monitor")
      final Map startupTimers;
      @GuardedBy("monitor")
      boolean ready;
      @GuardedBy("monitor")
      boolean transitioned;
      final int numberOfServices;
      final Monitor.Guard awaitHealthGuard;
      final Monitor.Guard stoppedGuard;
      @GuardedBy("monitor")
      final List listeners;

      ServiceManagerState(ImmutableCollection var1) {
         this.states = this.servicesByState.keys();
         this.startupTimers = Maps.newIdentityHashMap();
         this.awaitHealthGuard = new Monitor.Guard(this, this.monitor) {
            final ServiceManager.ServiceManagerState this$0;

            {
               this.this$0 = var1;
            }

            public boolean isSatisfied() {
               return this.this$0.states.count(Service.State.RUNNING) == this.this$0.numberOfServices || this.this$0.states.contains(Service.State.STOPPING) || this.this$0.states.contains(Service.State.TERMINATED) || this.this$0.states.contains(Service.State.FAILED);
            }
         };
         this.stoppedGuard = new Monitor.Guard(this, this.monitor) {
            final ServiceManager.ServiceManagerState this$0;

            {
               this.this$0 = var1;
            }

            public boolean isSatisfied() {
               return this.this$0.states.count(Service.State.TERMINATED) + this.this$0.states.count(Service.State.FAILED) == this.this$0.numberOfServices;
            }
         };
         this.listeners = Collections.synchronizedList(new ArrayList());
         this.numberOfServices = var1.size();
         this.servicesByState.putAll(Service.State.NEW, var1);
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Service var3 = (Service)var2.next();
            this.startupTimers.put(var3, Stopwatch.createUnstarted());
         }

      }

      void markReady() {
         this.monitor.enter();
         if (!this.transitioned) {
            this.ready = true;
            this.monitor.leave();
         } else {
            ArrayList var1 = Lists.newArrayList();
            Iterator var2 = this.servicesByState().values().iterator();

            while(var2.hasNext()) {
               Service var3 = (Service)var2.next();
               if (var3.state() != Service.State.NEW) {
                  var1.add(var3);
               }
            }

            throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + var1);
         }
      }

      void addListener(ServiceManager.Listener var1, Executor var2) {
         Preconditions.checkNotNull(var1, "listener");
         Preconditions.checkNotNull(var2, "executor");
         this.monitor.enter();
         if (!this.stoppedGuard.isSatisfied()) {
            this.listeners.add(new ListenerCallQueue(var1, var2));
         }

         this.monitor.leave();
      }

      void awaitHealthy() {
         this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
         this.checkHealthy();
         this.monitor.leave();
      }

      void awaitHealthy(long var1, TimeUnit var3) throws TimeoutException {
         this.monitor.enter();
         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, var1, var3)) {
            throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
         } else {
            this.checkHealthy();
            this.monitor.leave();
         }
      }

      void awaitStopped() {
         this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
         this.monitor.leave();
      }

      void awaitStopped(long var1, TimeUnit var3) throws TimeoutException {
         this.monitor.enter();
         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, var1, var3)) {
            throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(ImmutableSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
         } else {
            this.monitor.leave();
         }
      }

      ImmutableMultimap servicesByState() {
         ImmutableSetMultimap.Builder var1 = ImmutableSetMultimap.builder();
         this.monitor.enter();
         Iterator var2 = this.servicesByState.entries().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            if (!(var3.getValue() instanceof ServiceManager.NoOpService)) {
               var1.put(var3.getKey(), var3.getValue());
            }
         }

         this.monitor.leave();
         return var1.build();
      }

      ImmutableMap startupTimes() {
         this.monitor.enter();
         ArrayList var1 = Lists.newArrayListWithCapacity(this.states.size() - this.states.count(Service.State.NEW) + this.states.count(Service.State.STARTING));
         Iterator var2 = this.startupTimers.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Service var4 = (Service)var3.getKey();
            Stopwatch var5 = (Stopwatch)var3.getValue();
            if (!var5.isRunning() && !this.servicesByState.containsEntry(Service.State.NEW, var4) && !(var4 instanceof ServiceManager.NoOpService)) {
               var1.add(Maps.immutableEntry(var4, var5.elapsed(TimeUnit.MILLISECONDS)));
            }
         }

         this.monitor.leave();
         Collections.sort(var1, Ordering.natural().onResultOf(new Function(this) {
            final ServiceManager.ServiceManagerState this$0;

            {
               this.this$0 = var1;
            }

            public Long apply(Entry var1) {
               return (Long)var1.getValue();
            }

            public Object apply(Object var1) {
               return this.apply((Entry)var1);
            }
         }));
         ImmutableMap.Builder var7 = ImmutableMap.builder();
         Iterator var8 = var1.iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            var7.put(var9);
         }

         return var7.build();
      }

      void transitionService(Service var1, Service.State var2, Service.State var3) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkArgument(var2 != var3);
         this.monitor.enter();
         this.transitioned = true;
         if (!this.ready) {
            this.monitor.leave();
            this.executeListeners();
         } else {
            Preconditions.checkState(this.servicesByState.remove(var2, var1), "Service %s not at the expected location in the state map %s", var1, var2);
            Preconditions.checkState(this.servicesByState.put(var3, var1), "Service %s in the state map unexpectedly at %s", var1, var3);
            Stopwatch var4 = (Stopwatch)this.startupTimers.get(var1);
            if (var2 == Service.State.NEW) {
               var4.start();
            }

            if (var3.compareTo(Service.State.RUNNING) >= 0 && var4.isRunning()) {
               var4.stop();
               if (!(var1 instanceof ServiceManager.NoOpService)) {
                  ServiceManager.access$200().log(Level.FINE, "Started {0} in {1}.", new Object[]{var1, var4});
               }
            }

            if (var3 == Service.State.FAILED) {
               this.fireFailedListeners(var1);
            }

            if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
               this.fireHealthyListeners();
            } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
               this.fireStoppedListeners();
            }

            this.monitor.leave();
            this.executeListeners();
         }
      }

      @GuardedBy("monitor")
      void fireStoppedListeners() {
         ServiceManager.access$300().enqueueOn(this.listeners);
      }

      @GuardedBy("monitor")
      void fireHealthyListeners() {
         ServiceManager.access$400().enqueueOn(this.listeners);
      }

      @GuardedBy("monitor")
      void fireFailedListeners(Service var1) {
         (new ListenerCallQueue.Callback(this, "failed({service=" + var1 + "})", var1) {
            final Service val$service;
            final ServiceManager.ServiceManagerState this$0;

            {
               this.this$0 = var1;
               this.val$service = var3;
            }

            void call(ServiceManager.Listener var1) {
               var1.failure(this.val$service);
            }

            void call(Object var1) {
               this.call((ServiceManager.Listener)var1);
            }
         }).enqueueOn(this.listeners);
      }

      void executeListeners() {
         Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");

         for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
            ((ListenerCallQueue)this.listeners.get(var1)).execute();
         }

      }

      @GuardedBy("monitor")
      void checkHealthy() {
         if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
            throw new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
         }
      }
   }

   @Beta
   public abstract static class Listener {
      public void healthy() {
      }

      public void stopped() {
      }

      public void failure(Service var1) {
      }
   }
}
