package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

/** @deprecated */
@Deprecated
public class RouteSpecificPool {
   private final Log log = LogFactory.getLog(this.getClass());
   protected final HttpRoute route;
   protected final int maxEntries;
   protected final ConnPerRoute connPerRoute;
   protected final LinkedList freeEntries;
   protected final Queue waitingThreads;
   protected int numEntries;

   /** @deprecated */
   @Deprecated
   public RouteSpecificPool(HttpRoute var1, int var2) {
      this.route = var1;
      this.maxEntries = var2;
      this.connPerRoute = new ConnPerRoute(this) {
         final RouteSpecificPool this$0;

         {
            this.this$0 = var1;
         }

         public int getMaxForRoute(HttpRoute var1) {
            return this.this$0.maxEntries;
         }
      };
      this.freeEntries = new LinkedList();
      this.waitingThreads = new LinkedList();
      this.numEntries = 0;
   }

   public RouteSpecificPool(HttpRoute var1, ConnPerRoute var2) {
      this.route = var1;
      this.connPerRoute = var2;
      this.maxEntries = var2.getMaxForRoute(var1);
      this.freeEntries = new LinkedList();
      this.waitingThreads = new LinkedList();
      this.numEntries = 0;
   }

   public final HttpRoute getRoute() {
      return this.route;
   }

   public final int getMaxEntries() {
      return this.maxEntries;
   }

   public boolean isUnused() {
      return this.numEntries < 1 && this.waitingThreads.isEmpty();
   }

   public int getCapacity() {
      return this.connPerRoute.getMaxForRoute(this.route) - this.numEntries;
   }

   public final int getEntryCount() {
      return this.numEntries;
   }

   public BasicPoolEntry allocEntry(Object var1) {
      if (!this.freeEntries.isEmpty()) {
         ListIterator var2 = this.freeEntries.listIterator(this.freeEntries.size());

         while(var2.hasPrevious()) {
            BasicPoolEntry var3 = (BasicPoolEntry)var2.previous();
            if (var3.getState() == null || LangUtils.equals(var1, var3.getState())) {
               var2.remove();
               return var3;
            }
         }
      }

      if (this.getCapacity() == 0 && !this.freeEntries.isEmpty()) {
         BasicPoolEntry var6 = (BasicPoolEntry)this.freeEntries.remove();
         var6.shutdownEntry();
         OperatedClientConnection var7 = var6.getConnection();

         try {
            var7.close();
         } catch (IOException var5) {
            this.log.debug("I/O error closing connection", var5);
         }

         return var6;
      } else {
         return null;
      }
   }

   public void freeEntry(BasicPoolEntry var1) {
      if (this.numEntries < 1) {
         throw new IllegalStateException("No entry created for this pool. " + this.route);
      } else if (this.numEntries <= this.freeEntries.size()) {
         throw new IllegalStateException("No entry allocated from this pool. " + this.route);
      } else {
         this.freeEntries.add(var1);
      }
   }

   public void createdEntry(BasicPoolEntry var1) {
      Args.check(this.route.equals(var1.getPlannedRoute()), "Entry not planned for this pool");
      ++this.numEntries;
   }

   public boolean deleteEntry(BasicPoolEntry var1) {
      boolean var2 = this.freeEntries.remove(var1);
      if (var2) {
         --this.numEntries;
      }

      return var2;
   }

   public void dropEntry() {
      Asserts.check(this.numEntries > 0, "There is no entry that could be dropped");
      --this.numEntries;
   }

   public void queueThread(WaitingThread var1) {
      Args.notNull(var1, "Waiting thread");
      this.waitingThreads.add(var1);
   }

   public boolean hasThread() {
      return !this.waitingThreads.isEmpty();
   }

   public WaitingThread nextThread() {
      return (WaitingThread)this.waitingThreads.peek();
   }

   public void removeThread(WaitingThread var1) {
      if (var1 != null) {
         this.waitingThreads.remove(var1);
      }
   }
}
