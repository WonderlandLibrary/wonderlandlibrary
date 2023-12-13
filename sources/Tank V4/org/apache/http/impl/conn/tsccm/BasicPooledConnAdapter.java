package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.AbstractPooledConnAdapter;

/** @deprecated */
@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter {
   protected BasicPooledConnAdapter(ThreadSafeClientConnManager var1, AbstractPoolEntry var2) {
      super(var1, var2);
      this.markReusable();
   }

   protected ClientConnectionManager getManager() {
      return super.getManager();
   }

   protected AbstractPoolEntry getPoolEntry() {
      return super.getPoolEntry();
   }

   protected void detach() {
      super.detach();
   }
}
