package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
   private final int maxRetries;
   private final long retryInterval;

   public DefaultServiceUnavailableRetryStrategy(int var1, int var2) {
      Args.positive(var1, "Max retries");
      Args.positive(var2, "Retry interval");
      this.maxRetries = var1;
      this.retryInterval = (long)var2;
   }

   public DefaultServiceUnavailableRetryStrategy() {
      this(1, 1000);
   }

   public boolean retryRequest(HttpResponse var1, int var2, HttpContext var3) {
      return var2 <= this.maxRetries && var1.getStatusLine().getStatusCode() == 503;
   }

   public long getRetryInterval() {
      return this.retryInterval;
   }
}
