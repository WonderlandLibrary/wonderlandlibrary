package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

@Immutable
public class HttpService {
   private volatile HttpParams params;
   private volatile HttpProcessor processor;
   private volatile HttpRequestHandlerMapper handlerMapper;
   private volatile ConnectionReuseStrategy connStrategy;
   private volatile HttpResponseFactory responseFactory;
   private volatile HttpExpectationVerifier expectationVerifier;

   /** @deprecated */
   @Deprecated
   public HttpService(HttpProcessor var1, ConnectionReuseStrategy var2, HttpResponseFactory var3, HttpRequestHandlerResolver var4, HttpExpectationVerifier var5, HttpParams var6) {
      this(var1, var2, var3, (HttpRequestHandlerMapper)(new HttpService.HttpRequestHandlerResolverAdapter(var4)), (HttpExpectationVerifier)var5);
      this.params = var6;
   }

   /** @deprecated */
   @Deprecated
   public HttpService(HttpProcessor var1, ConnectionReuseStrategy var2, HttpResponseFactory var3, HttpRequestHandlerResolver var4, HttpParams var5) {
      this(var1, var2, var3, (HttpRequestHandlerMapper)(new HttpService.HttpRequestHandlerResolverAdapter(var4)), (HttpExpectationVerifier)null);
      this.params = var5;
   }

   /** @deprecated */
   @Deprecated
   public HttpService(HttpProcessor var1, ConnectionReuseStrategy var2, HttpResponseFactory var3) {
      this.params = null;
      this.processor = null;
      this.handlerMapper = null;
      this.connStrategy = null;
      this.responseFactory = null;
      this.expectationVerifier = null;
      this.setHttpProcessor(var1);
      this.setConnReuseStrategy(var2);
      this.setResponseFactory(var3);
   }

   public HttpService(HttpProcessor var1, ConnectionReuseStrategy var2, HttpResponseFactory var3, HttpRequestHandlerMapper var4, HttpExpectationVerifier var5) {
      this.params = null;
      this.processor = null;
      this.handlerMapper = null;
      this.connStrategy = null;
      this.responseFactory = null;
      this.expectationVerifier = null;
      this.processor = (HttpProcessor)Args.notNull(var1, "HTTP processor");
      this.connStrategy = (ConnectionReuseStrategy)(var2 != null ? var2 : DefaultConnectionReuseStrategy.INSTANCE);
      this.responseFactory = (HttpResponseFactory)(var3 != null ? var3 : DefaultHttpResponseFactory.INSTANCE);
      this.handlerMapper = var4;
      this.expectationVerifier = var5;
   }

   public HttpService(HttpProcessor var1, ConnectionReuseStrategy var2, HttpResponseFactory var3, HttpRequestHandlerMapper var4) {
      this(var1, var2, var3, (HttpRequestHandlerMapper)var4, (HttpExpectationVerifier)null);
   }

   public HttpService(HttpProcessor var1, HttpRequestHandlerMapper var2) {
      this(var1, (ConnectionReuseStrategy)null, (HttpResponseFactory)null, (HttpRequestHandlerMapper)var2, (HttpExpectationVerifier)null);
   }

   /** @deprecated */
   @Deprecated
   public void setHttpProcessor(HttpProcessor var1) {
      Args.notNull(var1, "HTTP processor");
      this.processor = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setConnReuseStrategy(ConnectionReuseStrategy var1) {
      Args.notNull(var1, "Connection reuse strategy");
      this.connStrategy = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setResponseFactory(HttpResponseFactory var1) {
      Args.notNull(var1, "Response factory");
      this.responseFactory = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setParams(HttpParams var1) {
      this.params = var1;
   }

   /** @deprecated */
   @Deprecated
   public void setHandlerResolver(HttpRequestHandlerResolver var1) {
      this.handlerMapper = new HttpService.HttpRequestHandlerResolverAdapter(var1);
   }

   /** @deprecated */
   @Deprecated
   public void setExpectationVerifier(HttpExpectationVerifier var1) {
      this.expectationVerifier = var1;
   }

   /** @deprecated */
   @Deprecated
   public HttpParams getParams() {
      return this.params;
   }

   public void handleRequest(HttpServerConnection var1, HttpContext var2) throws IOException, HttpException {
      var2.setAttribute("http.connection", var1);
      HttpResponse var3 = null;

      try {
         HttpRequest var4 = var1.receiveRequestHeader();
         if (var4 instanceof HttpEntityEnclosingRequest) {
            if (((HttpEntityEnclosingRequest)var4).expectContinue()) {
               var3 = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 100, var2);
               if (this.expectationVerifier != null) {
                  try {
                     this.expectationVerifier.verify(var4, var3, var2);
                  } catch (HttpException var6) {
                     var3 = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, var2);
                     this.handleException(var6, var3);
                  }
               }

               if (var3.getStatusLine().getStatusCode() < 200) {
                  var1.sendResponseHeader(var3);
                  var1.flush();
                  var3 = null;
                  var1.receiveRequestEntity((HttpEntityEnclosingRequest)var4);
               }
            } else {
               var1.receiveRequestEntity((HttpEntityEnclosingRequest)var4);
            }
         }

         var2.setAttribute("http.request", var4);
         if (var3 == null) {
            var3 = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, var2);
            this.processor.process(var4, var2);
            this.doService(var4, var3, var2);
         }

         if (var4 instanceof HttpEntityEnclosingRequest) {
            HttpEntity var5 = ((HttpEntityEnclosingRequest)var4).getEntity();
            EntityUtils.consume(var5);
         }
      } catch (HttpException var7) {
         var3 = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, var2);
         this.handleException(var7, var3);
      }

      var2.setAttribute("http.response", var3);
      this.processor.process(var3, var2);
      var1.sendResponseHeader(var3);
      var1.sendResponseEntity(var3);
      var1.flush();
      if (!this.connStrategy.keepAlive(var3, var2)) {
         var1.close();
      }

   }

   protected void handleException(HttpException var1, HttpResponse var2) {
      if (var1 instanceof MethodNotSupportedException) {
         var2.setStatusCode(501);
      } else if (var1 instanceof UnsupportedHttpVersionException) {
         var2.setStatusCode(505);
      } else if (var1 instanceof ProtocolException) {
         var2.setStatusCode(400);
      } else {
         var2.setStatusCode(500);
      }

      String var3 = var1.getMessage();
      if (var3 == null) {
         var3 = var1.toString();
      }

      byte[] var4 = EncodingUtils.getAsciiBytes(var3);
      ByteArrayEntity var5 = new ByteArrayEntity(var4);
      var5.setContentType("text/plain; charset=US-ASCII");
      var2.setEntity(var5);
   }

   protected void doService(HttpRequest var1, HttpResponse var2, HttpContext var3) throws HttpException, IOException {
      HttpRequestHandler var4 = null;
      if (this.handlerMapper != null) {
         var4 = this.handlerMapper.lookup(var1);
      }

      if (var4 != null) {
         var4.handle(var1, var2, var3);
      } else {
         var2.setStatusCode(501);
      }

   }

   /** @deprecated */
   @Deprecated
   private static class HttpRequestHandlerResolverAdapter implements HttpRequestHandlerMapper {
      private final HttpRequestHandlerResolver resolver;

      public HttpRequestHandlerResolverAdapter(HttpRequestHandlerResolver var1) {
         this.resolver = var1;
      }

      public HttpRequestHandler lookup(HttpRequest var1) {
         return this.resolver.lookup(var1.getRequestLine().getUri());
      }
   }
}
