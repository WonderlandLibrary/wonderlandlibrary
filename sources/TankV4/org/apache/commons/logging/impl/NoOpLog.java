package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;

public class NoOpLog implements Log, Serializable {
   private static final long serialVersionUID = 561423906191706148L;

   public NoOpLog() {
   }

   public NoOpLog(String var1) {
   }

   public void trace(Object var1) {
   }

   public void trace(Object var1, Throwable var2) {
   }

   public void debug(Object var1) {
   }

   public void debug(Object var1, Throwable var2) {
   }

   public void info(Object var1) {
   }

   public void info(Object var1, Throwable var2) {
   }

   public void warn(Object var1) {
   }

   public void warn(Object var1, Throwable var2) {
   }

   public void error(Object var1) {
   }

   public void error(Object var1, Throwable var2) {
   }

   public void fatal(Object var1) {
   }

   public void fatal(Object var1, Throwable var2) {
   }

   public final boolean isDebugEnabled() {
      return false;
   }

   public final boolean isErrorEnabled() {
      return false;
   }

   public final boolean isFatalEnabled() {
      return false;
   }

   public final boolean isInfoEnabled() {
      return false;
   }

   public final boolean isTraceEnabled() {
      return false;
   }

   public final boolean isWarnEnabled() {
      return false;
   }
}
