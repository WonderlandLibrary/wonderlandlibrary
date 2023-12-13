package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Immutable
public class RetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final HttpRequestRetryHandler retryHandler;

   public RetryExec(ClientExecChain var1, HttpRequestRetryHandler var2) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "HTTP request retry handler");
      this.requestExecutor = var1;
      this.retryHandler = var2;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      Header[] var5 = var2.getAllHeaders();
      int var6 = 1;

      while(true) {
         try {
            return this.requestExecutor.execute(var1, var2, var3, var4);
         } catch (IOException var9) {
            if (var4 != null && var4.isAborted()) {
               this.log.debug("Request has been aborted");
               throw var9;
            }

            if (!this.retryHandler.retryRequest(var9, var6, var3)) {
               if (var9 instanceof NoHttpResponseException) {
                  NoHttpResponseException var8 = new NoHttpResponseException(var1.getTargetHost().toHostString() + " failed to respond");
                  var8.setStackTrace(var9.getStackTrace());
                  throw var8;
               }

               throw var9;
            }

            if (this.log.isInfoEnabled()) {
               this.log.info("I/O exception (" + var9.getClass().getName() + ") caught when processing request to " + var1 + ": " + var9.getMessage());
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug(var9.getMessage(), var9);
            }

            if (!Proxies.isRepeatable(var2)) {
               this.log.debug("Cannot retry non-repeatable request");
               throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", var9);
            }

            var2.setHeaders(var5);
            if (this.log.isInfoEnabled()) {
               this.log.info("Retrying request to " + var1);
            }

            ++var6;
         }
      }
   }
}
