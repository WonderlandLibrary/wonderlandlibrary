package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import javax.annotation.Nullable;

@Beta
public final class Closer implements Closeable {
   private static final Closer.Suppressor SUPPRESSOR;
   @VisibleForTesting
   final Closer.Suppressor suppressor;
   private final Deque stack = new ArrayDeque(4);
   private Throwable thrown;

   public static Closer create() {
      return new Closer(SUPPRESSOR);
   }

   @VisibleForTesting
   Closer(Closer.Suppressor var1) {
      this.suppressor = (Closer.Suppressor)Preconditions.checkNotNull(var1);
   }

   public Closeable register(@Nullable Closeable var1) {
      if (var1 != null) {
         this.stack.addFirst(var1);
      }

      return var1;
   }

   public RuntimeException rethrow(Throwable var1) throws IOException {
      Preconditions.checkNotNull(var1);
      this.thrown = var1;
      Throwables.propagateIfPossible(var1, IOException.class);
      throw new RuntimeException(var1);
   }

   public RuntimeException rethrow(Throwable var1, Class var2) throws IOException, Exception {
      Preconditions.checkNotNull(var1);
      this.thrown = var1;
      Throwables.propagateIfPossible(var1, IOException.class);
      Throwables.propagateIfPossible(var1, var2);
      throw new RuntimeException(var1);
   }

   public RuntimeException rethrow(Throwable var1, Class var2, Class var3) throws IOException, Exception, Exception {
      Preconditions.checkNotNull(var1);
      this.thrown = var1;
      Throwables.propagateIfPossible(var1, IOException.class);
      Throwables.propagateIfPossible(var1, var2, var3);
      throw new RuntimeException(var1);
   }

   public void close() throws IOException {
      Throwable var1 = this.thrown;

      while(!this.stack.isEmpty()) {
         Closeable var2 = (Closeable)this.stack.removeFirst();

         try {
            var2.close();
         } catch (Throwable var4) {
            if (var1 == null) {
               var1 = var4;
            } else {
               this.suppressor.suppress(var2, var1, var4);
            }
         }
      }

      if (this.thrown == null && var1 != null) {
         Throwables.propagateIfPossible(var1, IOException.class);
         throw new AssertionError(var1);
      }
   }

   static {
      SUPPRESSOR = (Closer.Suppressor)(Closer.SuppressingSuppressor.isAvailable() ? Closer.SuppressingSuppressor.INSTANCE : Closer.LoggingSuppressor.INSTANCE);
   }

   @VisibleForTesting
   static final class SuppressingSuppressor implements Closer.Suppressor {
      static final Closer.SuppressingSuppressor INSTANCE = new Closer.SuppressingSuppressor();
      static final Method addSuppressed = getAddSuppressed();

      static boolean isAvailable() {
         return addSuppressed != null;
      }

      private static Method getAddSuppressed() {
         try {
            return Throwable.class.getMethod("addSuppressed", Throwable.class);
         } catch (Throwable var1) {
            return null;
         }
      }

      public void suppress(Closeable var1, Throwable var2, Throwable var3) {
         if (var2 != var3) {
            try {
               addSuppressed.invoke(var2, var3);
            } catch (Throwable var5) {
               Closer.LoggingSuppressor.INSTANCE.suppress(var1, var2, var3);
            }

         }
      }
   }

   @VisibleForTesting
   static final class LoggingSuppressor implements Closer.Suppressor {
      static final Closer.LoggingSuppressor INSTANCE = new Closer.LoggingSuppressor();

      public void suppress(Closeable var1, Throwable var2, Throwable var3) {
         Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + var1, var3);
      }
   }

   @VisibleForTesting
   interface Suppressor {
      void suppress(Closeable var1, Throwable var2, Throwable var3);
   }
}
