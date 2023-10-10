package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Immutable
public class ServiceUnavailableRetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final ServiceUnavailableRetryStrategy retryStrategy;

   public ServiceUnavailableRetryExec(ClientExecChain var1, ServiceUnavailableRetryStrategy var2) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "Retry strategy");
      this.requestExecutor = var1;
      this.retryStrategy = var2;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Header[] var5 = var2.getAllHeaders();
      int var6 = 1;

      while(true) {
         CloseableHttpResponse var7 = this.requestExecutor.execute(var1, var2, var3, var4);

         try {
            if (!this.retryStrategy.retryRequest(var7, var6, var3)) {
               return var7;
            }

            var7.close();
            long var8 = this.retryStrategy.getRetryInterval();
            if (var8 > 0L) {
               try {
                  this.log.trace("Wait for " + var8);
                  Thread.sleep(var8);
               } catch (InterruptedException var11) {
                  Thread.currentThread().interrupt();
                  throw new InterruptedIOException();
               }
            }

            var2.setHeaders(var5);
         } catch (RuntimeException var12) {
            var7.close();
            throw var12;
         }

         ++var6;
      }
   }
}
