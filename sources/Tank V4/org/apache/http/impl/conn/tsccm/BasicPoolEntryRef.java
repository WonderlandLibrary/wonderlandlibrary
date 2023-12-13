package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public class BasicPoolEntryRef extends WeakReference {
   private final HttpRoute route;

   public BasicPoolEntryRef(BasicPoolEntry var1, ReferenceQueue var2) {
      super(var1, var2);
      Args.notNull(var1, "Pool entry");
      this.route = var1.getPlannedRoute();
   }

   public final HttpRoute getRoute() {
      return this.route;
   }
}
