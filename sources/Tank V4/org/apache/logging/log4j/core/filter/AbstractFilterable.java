package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractFilterable implements Filterable {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private volatile Filter filter;

   protected AbstractFilterable(Filter var1) {
      this.filter = var1;
   }

   protected AbstractFilterable() {
   }

   public Filter getFilter() {
      return this.filter;
   }

   public synchronized void addFilter(Filter var1) {
      if (this.filter == null) {
         this.filter = var1;
      } else if (var1 instanceof CompositeFilter) {
         this.filter = ((CompositeFilter)this.filter).addFilter(var1);
      } else {
         Filter[] var2 = new Filter[]{this.filter, var1};
         this.filter = CompositeFilter.createFilters(var2);
      }

   }

   public synchronized void removeFilter(Filter var1) {
      if (this.filter == var1) {
         this.filter = null;
      } else if (var1 instanceof CompositeFilter) {
         CompositeFilter var2 = (CompositeFilter)var1;
         var2 = var2.removeFilter(var1);
         if (var2.size() > 1) {
            this.filter = var2;
         } else if (var2.size() == 1) {
            Iterator var3 = var2.iterator();
            this.filter = (Filter)var3.next();
         } else {
            this.filter = null;
         }
      }

   }

   public boolean hasFilter() {
      return this.filter != null;
   }

   public void startFilter() {
      if (this.filter != null && this.filter instanceof LifeCycle) {
         ((LifeCycle)this.filter).start();
      }

   }

   public void stopFilter() {
      if (this.filter != null && this.filter instanceof LifeCycle) {
         ((LifeCycle)this.filter).stop();
      }

   }

   public boolean isFiltered(LogEvent var1) {
      return this.filter != null && this.filter.filter(var1) == Filter.Result.DENY;
   }
}
