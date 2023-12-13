package org.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/** @deprecated */
@Deprecated
public class DecompressingHttpClient implements HttpClient {
   private final HttpClient backend;
   private final HttpRequestInterceptor acceptEncodingInterceptor;
   private final HttpResponseInterceptor contentEncodingInterceptor;

   public DecompressingHttpClient() {
      this(new DefaultHttpClient());
   }

   public DecompressingHttpClient(HttpClient var1) {
      this(var1, new RequestAcceptEncoding(), new ResponseContentEncoding());
   }

   DecompressingHttpClient(HttpClient var1, HttpRequestInterceptor var2, HttpResponseInterceptor var3) {
      this.backend = var1;
      this.acceptEncodingInterceptor = var2;
      this.contentEncodingInterceptor = var3;
   }

   public HttpParams getParams() {
      return this.backend.getParams();
   }

   public ClientConnectionManager getConnectionManager() {
      return this.backend.getConnectionManager();
   }

   public HttpResponse execute(HttpUriRequest var1) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (HttpContext)((HttpContext)null));
   }

   public HttpClient getHttpClient() {
      return this.backend;
   }

   HttpHost getHttpHost(HttpUriRequest var1) {
      URI var2 = var1.getURI();
      return URIUtils.extractHost(var2);
   }

   public HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (HttpContext)var2);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException, ClientProtocolException {
      return this.execute(var1, var2, (HttpContext)null);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      HttpResponse var6;
      HttpException var13;
      label40: {
         IOException var12;
         label39: {
            RuntimeException var7;
            try {
               Object var4 = var3 != null ? var3 : new BasicHttpContext();
               Object var5;
               if (var2 instanceof HttpEntityEnclosingRequest) {
                  var5 = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var2);
               } else {
                  var5 = new RequestWrapper(var2);
               }

               this.acceptEncodingInterceptor.process((HttpRequest)var5, (HttpContext)var4);
               var6 = this.backend.execute((HttpHost)var1, (HttpRequest)var5, (HttpContext)var4);

               try {
                  this.contentEncodingInterceptor.process(var6, (HttpContext)var4);
                  if (Boolean.TRUE.equals(((HttpContext)var4).getAttribute("http.client.response.uncompressed"))) {
                     var6.removeHeaders("Content-Length");
                     var6.removeHeaders("Content-Encoding");
                     var6.removeHeaders("Content-MD5");
                  }

                  return var6;
               } catch (HttpException var8) {
                  var13 = var8;
                  break label40;
               } catch (IOException var9) {
                  var12 = var9;
                  break label39;
               } catch (RuntimeException var10) {
                  var7 = var10;
               }
            } catch (HttpException var11) {
               throw new ClientProtocolException(var11);
            }

            EntityUtils.consume(var6.getEntity());
            throw var7;
         }

         EntityUtils.consume(var6.getEntity());
         throw var12;
      }

      EntityUtils.consume(var6.getEntity());
      throw var13;
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2) throws IOException, ClientProtocolException {
      return this.execute((HttpHost)this.getHttpHost(var1), (HttpRequest)var1, (ResponseHandler)var2);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2, HttpContext var3) throws IOException, ClientProtocolException {
      return this.execute(this.getHttpHost(var1), var1, var2, var3);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3) throws IOException, ClientProtocolException {
      return this.execute(var1, var2, var3, (HttpContext)null);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3, HttpContext var4) throws IOException, ClientProtocolException {
      HttpResponse var5 = this.execute(var1, var2, var4);
      Object var6 = var3.handleResponse(var5);
      HttpEntity var7 = var5.getEntity();
      if (var7 != null) {
         EntityUtils.consume(var7);
      }

      return var6;
   }
}
