package org.apache.http.params;

import java.util.Set;

/** @deprecated */
@Deprecated
public abstract class AbstractHttpParams implements HttpParams, HttpParamsNames {
   protected AbstractHttpParams() {
   }

   public long getLongParameter(String var1, long var2) {
      Object var4 = this.getParameter(var1);
      return var4 == null ? var2 : (Long)var4;
   }

   public HttpParams setLongParameter(String var1, long var2) {
      this.setParameter(var1, var2);
      return this;
   }

   public int getIntParameter(String var1, int var2) {
      Object var3 = this.getParameter(var1);
      return var3 == null ? var2 : (Integer)var3;
   }

   public HttpParams setIntParameter(String var1, int var2) {
      this.setParameter(var1, var2);
      return this;
   }

   public double getDoubleParameter(String var1, double var2) {
      Object var4 = this.getParameter(var1);
      return var4 == null ? var2 : (Double)var4;
   }

   public HttpParams setDoubleParameter(String var1, double var2) {
      this.setParameter(var1, var2);
      return this;
   }

   public HttpParams setBooleanParameter(String var1, boolean var2) {
      this.setParameter(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
      return this;
   }

   public boolean isParameterTrue(String var1) {
      return this.getBooleanParameter(var1, false);
   }

   public boolean isParameterFalse(String var1) {
      return 0 == null;
   }

   public Set getNames() {
      throw new UnsupportedOperationException();
   }
}
