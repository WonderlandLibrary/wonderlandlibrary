package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractFilter implements Filter, LifeCycle {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   protected final Filter.Result onMatch;
   protected final Filter.Result onMismatch;
   private boolean started;

   protected AbstractFilter() {
      this((Filter.Result)null, (Filter.Result)null);
   }

   protected AbstractFilter(Filter.Result var1, Filter.Result var2) {
      this.onMatch = var1 == null ? Filter.Result.NEUTRAL : var1;
      this.onMismatch = var2 == null ? Filter.Result.DENY : var2;
   }

   public void start() {
      this.started = true;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void stop() {
      this.started = false;
   }

   public final Filter.Result getOnMismatch() {
      return this.onMismatch;
   }

   public final Filter.Result getOnMatch() {
      return this.onMatch;
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }

   public Filter.Result filter(org.apache.logging.log4j.core.Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return Filter.Result.NEUTRAL;
   }

   public Filter.Result filter(org.apache.logging.log4j.core.Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return Filter.Result.NEUTRAL;
   }

   public Filter.Result filter(org.apache.logging.log4j.core.Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return Filter.Result.NEUTRAL;
   }

   public Filter.Result filter(LogEvent var1) {
      return Filter.Result.NEUTRAL;
   }
}
