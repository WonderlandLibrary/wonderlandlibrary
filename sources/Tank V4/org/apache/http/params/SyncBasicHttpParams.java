package org.apache.http.params;

import org.apache.http.annotation.ThreadSafe;

/** @deprecated */
@Deprecated
@ThreadSafe
public class SyncBasicHttpParams extends BasicHttpParams {
   private static final long serialVersionUID = 5387834869062660642L;

   public synchronized boolean removeParameter(String var1) {
      return super.removeParameter(var1);
   }

   public synchronized HttpParams setParameter(String var1, Object var2) {
      return super.setParameter(var1, var2);
   }

   public synchronized Object getParameter(String var1) {
      return super.getParameter(var1);
   }

   public synchronized boolean isParameterSet(String var1) {
      return super.isParameterSet(var1);
   }

   public synchronized boolean isParameterSetLocally(String var1) {
      return super.isParameterSetLocally(var1);
   }

   public synchronized void setParameters(String[] var1, Object var2) {
      super.setParameters(var1, var2);
   }

   public synchronized void clear() {
      super.clear();
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
