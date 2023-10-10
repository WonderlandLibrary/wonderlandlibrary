package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

public class AIMDBackoffManager implements BackoffManager {
   private final ConnPoolControl connPerRoute;
   private final Clock clock;
   private final Map lastRouteProbes;
   private final Map lastRouteBackoffs;
   private long coolDown;
   private double backoffFactor;
   private int cap;

   public AIMDBackoffManager(ConnPoolControl var1) {
      this(var1, new SystemClock());
   }

   AIMDBackoffManager(ConnPoolControl var1, Clock var2) {
      this.coolDown = 5000L;
      this.backoffFactor = 0.5D;
      this.cap = 2;
      this.clock = var2;
      this.connPerRoute = var1;
      this.lastRouteProbes = new HashMap();
      this.lastRouteBackoffs = new HashMap();
   }

   public void backOff(HttpRoute var1) {
      ConnPoolControl var2;
      synchronized(var2 = this.connPerRoute){}
      int var3 = this.connPerRoute.getMaxPerRoute(var1);
      Long var4 = this.getLastUpdate(this.lastRouteBackoffs, var1);
      long var5 = this.clock.getCurrentTime();
      if (var5 - var4 >= this.coolDown) {
         this.connPerRoute.setMaxPerRoute(var1, this.getBackedOffPoolSize(var3));
         this.lastRouteBackoffs.put(var1, var5);
      }
   }

   private int getBackedOffPoolSize(int var1) {
      return var1 <= 1 ? 1 : (int)Math.floor(this.backoffFactor * (double)var1);
   }

   public void probe(HttpRoute var1) {
      ConnPoolControl var2;
      synchronized(var2 = this.connPerRoute){}
      int var3 = this.connPerRoute.getMaxPerRoute(var1);
      int var4 = var3 >= this.cap ? this.cap : var3 + 1;
      Long var5 = this.getLastUpdate(this.lastRouteProbes, var1);
      Long var6 = this.getLastUpdate(this.lastRouteBackoffs, var1);
      long var7 = this.clock.getCurrentTime();
      if (var7 - var5 >= this.coolDown && var7 - var6 >= this.coolDown) {
         this.connPerRoute.setMaxPerRoute(var1, var4);
         this.lastRouteProbes.put(var1, var7);
      }
   }

   private Long getLastUpdate(Map var1, HttpRoute var2) {
      Long var3 = (Long)var1.get(var2);
      if (var3 == null) {
         var3 = 0L;
      }

      return var3;
   }

   public void setBackoffFactor(double var1) {
      Args.check(var1 > 0.0D && var1 < 1.0D, "Backoff factor must be 0.0 < f < 1.0");
      this.backoffFactor = var1;
   }

   public void setCooldownMillis(long var1) {
      Args.positive(this.coolDown, "Cool down");
      this.coolDown = var1;
   }

   public void setPerHostConnectionCap(int var1) {
      Args.positive(var1, "Per host connection cap");
      this.cap = var1;
   }
}
