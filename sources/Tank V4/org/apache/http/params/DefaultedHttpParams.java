package org.apache.http.params;

import java.util.HashSet;
import java.util.Set;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public final class DefaultedHttpParams extends AbstractHttpParams {
   private final HttpParams local;
   private final HttpParams defaults;

   public DefaultedHttpParams(HttpParams var1, HttpParams var2) {
      this.local = (HttpParams)Args.notNull(var1, "Local HTTP parameters");
      this.defaults = var2;
   }

   public HttpParams copy() {
      HttpParams var1 = this.local.copy();
      return new DefaultedHttpParams(var1, this.defaults);
   }

   public Object getParameter(String var1) {
      Object var2 = this.local.getParameter(var1);
      if (var2 == null && this.defaults != null) {
         var2 = this.defaults.getParameter(var1);
      }

      return var2;
   }

   public boolean removeParameter(String var1) {
      return this.local.removeParameter(var1);
   }

   public HttpParams setParameter(String var1, Object var2) {
      return this.local.setParameter(var1, var2);
   }

   public HttpParams getDefaults() {
      return this.defaults;
   }

   public Set getNames() {
      HashSet var1 = new HashSet(this.getNames(this.defaults));
      var1.addAll(this.getNames(this.local));
      return var1;
   }

   public Set getDefaultNames() {
      return new HashSet(this.getNames(this.defaults));
   }

   public Set getLocalNames() {
      return new HashSet(this.getNames(this.local));
   }

   private Set getNames(HttpParams var1) {
      if (var1 instanceof HttpParamsNames) {
         return ((HttpParamsNames)var1).getNames();
      } else {
         throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
      }
   }
}
