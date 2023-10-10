package org.apache.http.protocol;

import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public final class DefaultedHttpContext implements HttpContext {
   private final HttpContext local;
   private final HttpContext defaults;

   public DefaultedHttpContext(HttpContext var1, HttpContext var2) {
      this.local = (HttpContext)Args.notNull(var1, "HTTP context");
      this.defaults = var2;
   }

   public Object getAttribute(String var1) {
      Object var2 = this.local.getAttribute(var1);
      return var2 == null ? this.defaults.getAttribute(var1) : var2;
   }

   public Object removeAttribute(String var1) {
      return this.local.removeAttribute(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.local.setAttribute(var1, var2);
   }

   public HttpContext getDefaults() {
      return this.defaults;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[local: ").append(this.local);
      var1.append("defaults: ").append(this.defaults);
      var1.append("]");
      return var1.toString();
   }
}
