package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;

@Immutable
public class StandardHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
   private final Map idempotentMethods;

   public StandardHttpRequestRetryHandler(int var1, boolean var2) {
      super(var1, var2);
      this.idempotentMethods = new ConcurrentHashMap();
      this.idempotentMethods.put("GET", Boolean.TRUE);
      this.idempotentMethods.put("HEAD", Boolean.TRUE);
      this.idempotentMethods.put("PUT", Boolean.TRUE);
      this.idempotentMethods.put("DELETE", Boolean.TRUE);
      this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
      this.idempotentMethods.put("TRACE", Boolean.TRUE);
   }

   public StandardHttpRequestRetryHandler() {
      this(3, false);
   }

   protected boolean handleAsIdempotent(HttpRequest var1) {
      String var2 = var1.getRequestLine().getMethod().toUpperCase(Locale.US);
      Boolean var3 = (Boolean)this.idempotentMethods.get(var2);
      return var3 != null && var3;
   }
}
