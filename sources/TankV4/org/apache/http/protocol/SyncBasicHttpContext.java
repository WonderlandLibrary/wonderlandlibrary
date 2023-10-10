package org.apache.http.protocol;

/** @deprecated */
@Deprecated
public class SyncBasicHttpContext extends BasicHttpContext {
   public SyncBasicHttpContext(HttpContext var1) {
      super(var1);
   }

   public SyncBasicHttpContext() {
   }

   public synchronized Object getAttribute(String var1) {
      return super.getAttribute(var1);
   }

   public synchronized void setAttribute(String var1, Object var2) {
      super.setAttribute(var1, var2);
   }

   public synchronized Object removeAttribute(String var1) {
      return super.removeAttribute(var1);
   }

   public synchronized void clear() {
      super.clear();
   }
}
