package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
   public static final DefaultHttpRequestRetryHandler INSTANCE = new DefaultHttpRequestRetryHandler();
   private final int retryCount;
   private final boolean requestSentRetryEnabled;
   private final Set nonRetriableClasses;

   protected DefaultHttpRequestRetryHandler(int var1, boolean var2, Collection var3) {
      this.retryCount = var1;
      this.requestSentRetryEnabled = var2;
      this.nonRetriableClasses = new HashSet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Class var5 = (Class)var4.next();
         this.nonRetriableClasses.add(var5);
      }

   }

   public DefaultHttpRequestRetryHandler(int var1, boolean var2) {
      this(var1, var2, Arrays.asList(InterruptedIOException.class, UnknownHostException.class, ConnectException.class, SSLException.class));
   }

   public DefaultHttpRequestRetryHandler() {
      this(3, false);
   }

   public boolean retryRequest(IOException var1, int var2, HttpContext var3) {
      Args.notNull(var1, "Exception parameter");
      Args.notNull(var3, "HTTP context");
      if (var2 > this.retryCount) {
         return false;
      } else if (this.nonRetriableClasses.contains(var1.getClass())) {
         return false;
      } else {
         Iterator var4 = this.nonRetriableClasses.iterator();

         Class var5;
         do {
            if (!var4.hasNext()) {
               HttpClientContext var6 = HttpClientContext.adapt(var3);
               HttpRequest var7 = var6.getRequest();
               if (var7 != false) {
                  return false;
               }

               if (var7 == false) {
                  return true;
               }

               if (var6.isRequestSent() && !this.requestSentRetryEnabled) {
                  return false;
               }

               return true;
            }

            var5 = (Class)var4.next();
         } while(!var5.isInstance(var1));

         return false;
      }
   }

   public boolean isRequestSentRetryEnabled() {
      return this.requestSentRetryEnabled;
   }

   public int getRetryCount() {
      return this.retryCount;
   }
}
