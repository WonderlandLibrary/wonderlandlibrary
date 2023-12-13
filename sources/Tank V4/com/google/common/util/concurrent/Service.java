package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public interface Service {
   Service startAsync();

   boolean isRunning();

   Service.State state();

   Service stopAsync();

   void awaitRunning();

   void awaitRunning(long var1, TimeUnit var3) throws TimeoutException;

   void awaitTerminated();

   void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException;

   Throwable failureCause();

   void addListener(Service.Listener var1, Executor var2);

   @Beta
   public abstract static class Listener {
      public void starting() {
      }

      public void running() {
      }

      public void stopping(Service.State var1) {
      }

      public void terminated(Service.State var1) {
      }

      public void failed(Service.State var1, Throwable var2) {
      }
   }

   @Beta
   public static enum State {
      NEW {
         boolean isTerminal() {
            return false;
         }
      },
      STARTING {
         boolean isTerminal() {
            return false;
         }
      },
      RUNNING {
         boolean isTerminal() {
            return false;
         }
      },
      STOPPING {
         boolean isTerminal() {
            return false;
         }
      },
      TERMINATED {
         boolean isTerminal() {
            return true;
         }
      },
      FAILED {
         boolean isTerminal() {
            return true;
         }
      };

      private static final Service.State[] $VALUES = new Service.State[]{NEW, STARTING, RUNNING, STOPPING, TERMINATED, FAILED};

      private State() {
      }

      abstract boolean isTerminal();

      State(Object var3) {
         this();
      }
   }
}
