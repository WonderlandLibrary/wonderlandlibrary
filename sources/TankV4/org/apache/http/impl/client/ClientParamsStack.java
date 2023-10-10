package org.apache.http.impl.client;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class ClientParamsStack extends AbstractHttpParams {
   protected final HttpParams applicationParams;
   protected final HttpParams clientParams;
   protected final HttpParams requestParams;
   protected final HttpParams overrideParams;

   public ClientParamsStack(HttpParams var1, HttpParams var2, HttpParams var3, HttpParams var4) {
      this.applicationParams = var1;
      this.clientParams = var2;
      this.requestParams = var3;
      this.overrideParams = var4;
   }

   public ClientParamsStack(ClientParamsStack var1) {
      this(var1.getApplicationParams(), var1.getClientParams(), var1.getRequestParams(), var1.getOverrideParams());
   }

   public ClientParamsStack(ClientParamsStack var1, HttpParams var2, HttpParams var3, HttpParams var4, HttpParams var5) {
      this(var2 != null ? var2 : var1.getApplicationParams(), var3 != null ? var3 : var1.getClientParams(), var4 != null ? var4 : var1.getRequestParams(), var5 != null ? var5 : var1.getOverrideParams());
   }

   public final HttpParams getApplicationParams() {
      return this.applicationParams;
   }

   public final HttpParams getClientParams() {
      return this.clientParams;
   }

   public final HttpParams getRequestParams() {
      return this.requestParams;
   }

   public final HttpParams getOverrideParams() {
      return this.overrideParams;
   }

   public Object getParameter(String var1) {
      Args.notNull(var1, "Parameter name");
      Object var2 = null;
      if (this.overrideParams != null) {
         var2 = this.overrideParams.getParameter(var1);
      }

      if (var2 == null && this.requestParams != null) {
         var2 = this.requestParams.getParameter(var1);
      }

      if (var2 == null && this.clientParams != null) {
         var2 = this.clientParams.getParameter(var1);
      }

      if (var2 == null && this.applicationParams != null) {
         var2 = this.applicationParams.getParameter(var1);
      }

      return var2;
   }

   public HttpParams setParameter(String var1, Object var2) throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
   }

   public boolean removeParameter(String var1) {
      throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
   }

   public HttpParams copy() {
      return this;
   }
}
