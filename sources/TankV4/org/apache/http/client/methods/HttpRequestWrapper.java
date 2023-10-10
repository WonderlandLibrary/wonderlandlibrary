package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpParams;

@NotThreadSafe
public class HttpRequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
   private final HttpRequest original;
   private final String method;
   private ProtocolVersion version;
   private URI uri;

   private HttpRequestWrapper(HttpRequest var1) {
      this.original = var1;
      this.version = this.original.getRequestLine().getProtocolVersion();
      this.method = this.original.getRequestLine().getMethod();
      if (var1 instanceof HttpUriRequest) {
         this.uri = ((HttpUriRequest)var1).getURI();
      } else {
         this.uri = null;
      }

      this.setHeaders(var1.getAllHeaders());
   }

   public ProtocolVersion getProtocolVersion() {
      return this.version != null ? this.version : this.original.getProtocolVersion();
   }

   public void setProtocolVersion(ProtocolVersion var1) {
      this.version = var1;
   }

   public URI getURI() {
      return this.uri;
   }

   public void setURI(URI var1) {
      this.uri = var1;
   }

   public String getMethod() {
      return this.method;
   }

   public void abort() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }

   public boolean isAborted() {
      return false;
   }

   public RequestLine getRequestLine() {
      String var1 = null;
      if (this.uri != null) {
         var1 = this.uri.toASCIIString();
      } else {
         var1 = this.original.getRequestLine().getUri();
      }

      if (var1 == null || var1.length() == 0) {
         var1 = "/";
      }

      return new BasicRequestLine(this.method, var1, this.getProtocolVersion());
   }

   public HttpRequest getOriginal() {
      return this.original;
   }

   public String toString() {
      return this.getRequestLine() + " " + this.headergroup;
   }

   public static HttpRequestWrapper wrap(HttpRequest var0) {
      if (var0 == null) {
         return null;
      } else {
         return (HttpRequestWrapper)(var0 instanceof HttpEntityEnclosingRequest ? new HttpRequestWrapper.HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var0) : new HttpRequestWrapper(var0));
      }
   }

   /** @deprecated */
   @Deprecated
   public HttpParams getParams() {
      if (this.params == null) {
         this.params = this.original.getParams().copy();
      }

      return this.params;
   }

   HttpRequestWrapper(HttpRequest var1, Object var2) {
      this(var1);
   }

   static class HttpEntityEnclosingRequestWrapper extends HttpRequestWrapper implements HttpEntityEnclosingRequest {
      private HttpEntity entity;

      public HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest var1) {
         super(var1, null);
         this.entity = var1.getEntity();
      }

      public HttpEntity getEntity() {
         return this.entity;
      }

      public void setEntity(HttpEntity var1) {
         this.entity = var1;
      }

      public boolean expectContinue() {
         Header var1 = this.getFirstHeader("Expect");
         return var1 != null && "100-continue".equalsIgnoreCase(var1.getValue());
      }
   }
}
