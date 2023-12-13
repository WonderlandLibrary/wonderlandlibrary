package org.apache.http.protocol;

import org.apache.http.HttpConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class HttpCoreContext implements HttpContext {
   public static final String HTTP_CONNECTION = "http.connection";
   public static final String HTTP_REQUEST = "http.request";
   public static final String HTTP_RESPONSE = "http.response";
   public static final String HTTP_TARGET_HOST = "http.target_host";
   public static final String HTTP_REQ_SENT = "http.request_sent";
   private final HttpContext context;

   public static HttpCoreContext create() {
      return new HttpCoreContext(new BasicHttpContext());
   }

   public static HttpCoreContext adapt(HttpContext var0) {
      Args.notNull(var0, "HTTP context");
      return var0 instanceof HttpCoreContext ? (HttpCoreContext)var0 : new HttpCoreContext(var0);
   }

   public HttpCoreContext(HttpContext var1) {
      this.context = var1;
   }

   public HttpCoreContext() {
      this.context = new BasicHttpContext();
   }

   public Object getAttribute(String var1) {
      return this.context.getAttribute(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.context.setAttribute(var1, var2);
   }

   public Object removeAttribute(String var1) {
      return this.context.removeAttribute(var1);
   }

   public Object getAttribute(String var1, Class var2) {
      Args.notNull(var2, "Attribute class");
      Object var3 = this.getAttribute(var1);
      return var3 == null ? null : var2.cast(var3);
   }

   public HttpConnection getConnection(Class var1) {
      return (HttpConnection)this.getAttribute("http.connection", var1);
   }

   public HttpConnection getConnection() {
      return (HttpConnection)this.getAttribute("http.connection", HttpConnection.class);
   }

   public HttpRequest getRequest() {
      return (HttpRequest)this.getAttribute("http.request", HttpRequest.class);
   }

   public boolean isRequestSent() {
      Boolean var1 = (Boolean)this.getAttribute("http.request_sent", Boolean.class);
      return var1 != null && var1;
   }

   public HttpResponse getResponse() {
      return (HttpResponse)this.getAttribute("http.response", HttpResponse.class);
   }

   public void setTargetHost(HttpHost var1) {
      this.setAttribute("http.target_host", var1);
   }

   public HttpHost getTargetHost() {
      return (HttpHost)this.getAttribute("http.target_host", HttpHost.class);
   }
}
