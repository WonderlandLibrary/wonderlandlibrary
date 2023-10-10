package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Immutable
public class ProtocolExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final HttpProcessor httpProcessor;

   public ProtocolExec(ClientExecChain var1, HttpProcessor var2) {
      Args.notNull(var1, "HTTP client request executor");
      Args.notNull(var2, "HTTP protocol processor");
      this.requestExecutor = var1;
      this.httpProcessor = var2;
   }

   void rewriteRequestURI(HttpRequestWrapper var1, HttpRoute var2) throws ProtocolException {
      try {
         URI var3 = var1.getURI();
         if (var3 != null) {
            if (var2.getProxyHost() != null && !var2.isTunnelled()) {
               if (!var3.isAbsolute()) {
                  HttpHost var4 = var2.getTargetHost();
                  var3 = URIUtils.rewriteURI(var3, var4, true);
               } else {
                  var3 = URIUtils.rewriteURI(var3);
               }
            } else if (var3.isAbsolute()) {
               var3 = URIUtils.rewriteURI(var3, (HttpHost)null, true);
            } else {
               var3 = URIUtils.rewriteURI(var3);
            }

            var1.setURI(var3);
         }

      } catch (URISyntaxException var5) {
         throw new ProtocolException("Invalid URI: " + var1.getRequestLine().getUri(), var5);
      }
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      HttpRequest var5 = var2.getOriginal();
      URI var6 = null;
      if (var5 instanceof HttpUriRequest) {
         var6 = ((HttpUriRequest)var5).getURI();
      } else {
         String var7 = var5.getRequestLine().getUri();

         try {
            var6 = URI.create(var7);
         } catch (IllegalArgumentException var15) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Unable to parse '" + var7 + "' as a valid URI; " + "request URI and Host header may be inconsistent", var15);
            }
         }
      }

      var2.setURI(var6);
      this.rewriteRequestURI(var2, var1);
      HttpParams var16 = var2.getParams();
      HttpHost var8 = (HttpHost)var16.getParameter("http.virtual-host");
      if (var8 != null && var8.getPort() == -1) {
         int var9 = var1.getTargetHost().getPort();
         if (var9 != -1) {
            var8 = new HttpHost(var8.getHostName(), var9, var8.getSchemeName());
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("Using virtual host" + var8);
         }
      }

      HttpHost var17 = null;
      if (var8 != null) {
         var17 = var8;
      } else if (var6 != null && var6.isAbsolute() && var6.getHost() != null) {
         var17 = new HttpHost(var6.getHost(), var6.getPort(), var6.getScheme());
      }

      if (var17 == null) {
         var17 = var1.getTargetHost();
      }

      if (var6 != null) {
         String var10 = var6.getUserInfo();
         if (var10 != null) {
            Object var11 = var3.getCredentialsProvider();
            if (var11 == null) {
               var11 = new BasicCredentialsProvider();
               var3.setCredentialsProvider((CredentialsProvider)var11);
            }

            ((CredentialsProvider)var11).setCredentials(new AuthScope(var17), new UsernamePasswordCredentials(var10));
         }
      }

      var3.setAttribute("http.target_host", var17);
      var3.setAttribute("http.route", var1);
      var3.setAttribute("http.request", var2);
      this.httpProcessor.process(var2, var3);
      CloseableHttpResponse var18 = this.requestExecutor.execute(var1, var2, var3, var4);

      try {
         var3.setAttribute("http.response", var18);
         this.httpProcessor.process(var18, var3);
         return var18;
      } catch (RuntimeException var12) {
         var18.close();
         throw var12;
      } catch (IOException var13) {
         var18.close();
         throw var13;
      } catch (HttpException var14) {
         var18.close();
         throw var14;
      }
   }
}
