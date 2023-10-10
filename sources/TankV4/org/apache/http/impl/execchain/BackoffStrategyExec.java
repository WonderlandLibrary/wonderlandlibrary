package org.apache.http.impl.execchain;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Immutable
public class BackoffStrategyExec implements ClientExecChain {
   private final ClientExecChain requestExecutor;
   private final ConnectionBackoffStrategy connectionBackoffStrategy;
   private final BackoffManager backoffManager;

   public BackoffStrategyExec(ClientExecChain var1, ConnectionBackoffStrategy var2, BackoffManager var3) {
      Args.notNull(var1, "HTTP client request executor");
      Args.notNull(var2, "Connection backoff strategy");
      Args.notNull(var3, "Backoff manager");
      this.requestExecutor = var1;
      this.connectionBackoffStrategy = var2;
      this.backoffManager = var3;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      CloseableHttpResponse var5 = null;

      try {
         var5 = this.requestExecutor.execute(var1, var2, var3, var4);
      } catch (Exception var7) {
         if (var5 != null) {
            var5.close();
         }

         if (this.connectionBackoffStrategy.shouldBackoff((Throwable)var7)) {
            this.backoffManager.backOff(var1);
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof HttpException) {
            throw (HttpException)var7;
         }

         if (var7 instanceof IOException) {
            throw (IOException)var7;
         }

         throw new UndeclaredThrowableException(var7);
      }

      if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)var5)) {
         this.backoffManager.backOff(var1);
      } else {
         this.backoffManager.probe(var1);
      }

      return var5;
   }
}
