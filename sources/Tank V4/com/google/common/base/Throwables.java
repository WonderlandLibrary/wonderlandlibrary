package com.google.common.base;

import com.google.common.annotations.Beta;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public final class Throwables {
   private Throwables() {
   }

   public static void propagateIfInstanceOf(@Nullable Throwable var0, Class var1) throws Throwable {
      if (var0 != null && var1.isInstance(var0)) {
         throw (Throwable)var1.cast(var0);
      }
   }

   public static void propagateIfPossible(@Nullable Throwable var0) {
      propagateIfInstanceOf(var0, Error.class);
      propagateIfInstanceOf(var0, RuntimeException.class);
   }

   public static void propagateIfPossible(@Nullable Throwable var0, Class var1) throws Throwable {
      propagateIfInstanceOf(var0, var1);
      propagateIfPossible(var0);
   }

   public static void propagateIfPossible(@Nullable Throwable var0, Class var1, Class var2) throws Throwable, Throwable {
      Preconditions.checkNotNull(var2);
      propagateIfInstanceOf(var0, var1);
      propagateIfPossible(var0, var2);
   }

   public static RuntimeException propagate(Throwable var0) {
      propagateIfPossible((Throwable)Preconditions.checkNotNull(var0));
      throw new RuntimeException(var0);
   }

   public static Throwable getRootCause(Throwable var0) {
      Throwable var1;
      while((var1 = var0.getCause()) != null) {
         var0 = var1;
      }

      return var0;
   }

   @Beta
   public static List getCausalChain(Throwable var0) {
      Preconditions.checkNotNull(var0);

      ArrayList var1;
      for(var1 = new ArrayList(4); var0 != null; var0 = var0.getCause()) {
         var1.add(var0);
      }

      return Collections.unmodifiableList(var1);
   }

   public static String getStackTraceAsString(Throwable var0) {
      StringWriter var1 = new StringWriter();
      var0.printStackTrace(new PrintWriter(var1));
      return var1.toString();
   }
}
