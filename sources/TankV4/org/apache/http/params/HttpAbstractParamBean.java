package org.apache.http.params;

import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public abstract class HttpAbstractParamBean {
   protected final HttpParams params;

   public HttpAbstractParamBean(HttpParams var1) {
      this.params = (HttpParams)Args.notNull(var1, "HTTP parameters");
   }
}
