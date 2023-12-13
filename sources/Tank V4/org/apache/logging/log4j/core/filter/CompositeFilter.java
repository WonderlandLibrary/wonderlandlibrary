package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(
   name = "filters",
   category = "Core",
   printObject = true
)
public final class CompositeFilter implements Iterable, Filter, LifeCycle {
   private final List filters;
   private final boolean hasFilters;
   private boolean isStarted;

   private CompositeFilter() {
      this.filters = new ArrayList();
      this.hasFilters = false;
   }

   private CompositeFilter(List var1) {
      if (var1 == null) {
         this.filters = Collections.unmodifiableList(new ArrayList());
         this.hasFilters = false;
      } else {
         this.filters = Collections.unmodifiableList(var1);
         this.hasFilters = this.filters.size() > 0;
      }
   }

   public CompositeFilter addFilter(Filter var1) {
      ArrayList var2 = new ArrayList(this.filters);
      var2.add(var1);
      return new CompositeFilter(Collections.unmodifiableList(var2));
   }

   public CompositeFilter removeFilter(Filter var1) {
      ArrayList var2 = new ArrayList(this.filters);
      var2.remove(var1);
      return new CompositeFilter(Collections.unmodifiableList(var2));
   }

   public Iterator iterator() {
      return this.filters.iterator();
   }

   public List getFilters() {
      return this.filters;
   }

   public boolean hasFilters() {
      return this.hasFilters;
   }

   public int size() {
      return this.filters.size();
   }

   public void start() {
      Iterator var1 = this.filters.iterator();

      while(var1.hasNext()) {
         Filter var2 = (Filter)var1.next();
         if (var2 instanceof LifeCycle) {
            ((LifeCycle)var2).start();
         }
      }

      this.isStarted = true;
   }

   public void stop() {
      Iterator var1 = this.filters.iterator();

      while(var1.hasNext()) {
         Filter var2 = (Filter)var1.next();
         if (var2 instanceof LifeCycle) {
            ((LifeCycle)var2).stop();
         }
      }

      this.isStarted = false;
   }

   public boolean isStarted() {
      return this.isStarted;
   }

   public Filter.Result getOnMismatch() {
      return Filter.Result.NEUTRAL;
   }

   public Filter.Result getOnMatch() {
      return Filter.Result.NEUTRAL;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      Filter.Result var6 = Filter.Result.NEUTRAL;
      Iterator var7 = this.filters.iterator();

      do {
         if (!var7.hasNext()) {
            return var6;
         }

         Filter var8 = (Filter)var7.next();
         var6 = var8.filter(var1, var2, var3, var4, var5);
      } while(var6 != Filter.Result.ACCEPT && var6 != Filter.Result.DENY);

      return var6;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      Filter.Result var6 = Filter.Result.NEUTRAL;
      Iterator var7 = this.filters.iterator();

      do {
         if (!var7.hasNext()) {
            return var6;
         }

         Filter var8 = (Filter)var7.next();
         var6 = var8.filter(var1, var2, var3, var4, var5);
      } while(var6 != Filter.Result.ACCEPT && var6 != Filter.Result.DENY);

      return var6;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      Filter.Result var6 = Filter.Result.NEUTRAL;
      Iterator var7 = this.filters.iterator();

      do {
         if (!var7.hasNext()) {
            return var6;
         }

         Filter var8 = (Filter)var7.next();
         var6 = var8.filter(var1, var2, var3, var4, var5);
      } while(var6 != Filter.Result.ACCEPT && var6 != Filter.Result.DENY);

      return var6;
   }

   public Filter.Result filter(LogEvent var1) {
      Filter.Result var2 = Filter.Result.NEUTRAL;
      Iterator var3 = this.filters.iterator();

      do {
         if (!var3.hasNext()) {
            return var2;
         }

         Filter var4 = (Filter)var3.next();
         var2 = var4.filter(var1);
      } while(var2 != Filter.Result.ACCEPT && var2 != Filter.Result.DENY);

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      Filter var3;
      for(Iterator var2 = this.filters.iterator(); var2.hasNext(); var1.append(var3.toString())) {
         var3 = (Filter)var2.next();
         if (var1.length() == 0) {
            var1.append("{");
         } else {
            var1.append(", ");
         }
      }

      if (var1.length() > 0) {
         var1.append("}");
      }

      return var1.toString();
   }

   @PluginFactory
   public static CompositeFilter createFilters(@PluginElement("Filters") Filter[] var0) {
      Object var1 = var0 != null && var0.length != 0 ? Arrays.asList(var0) : new ArrayList();
      return new CompositeFilter((List)var1);
   }
}
