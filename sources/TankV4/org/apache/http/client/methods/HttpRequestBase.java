package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;

@NotThreadSafe
public abstract class HttpRequestBase extends AbstractExecutionAwareRequest implements HttpUriRequest, Configurable {
   private ProtocolVersion version;
   private URI uri;
   private RequestConfig config;

   public abstract String getMethod();

   public void setProtocolVersion(ProtocolVersion var1) {
      this.version = var1;
   }

   public ProtocolVersion getProtocolVersion() {
      return this.version != null ? this.version : HttpProtocolParams.getVersion(this.getParams());
   }

   public URI getURI() {
      return this.uri;
   }

   public RequestLine getRequestLine() {
      String var1 = this.getMethod();
      ProtocolVersion var2 = this.getProtocolVersion();
      URI var3 = this.getURI();
      String var4 = null;
      if (var3 != null) {
         var4 = var3.toASCIIString();
      }

      if (var4 == null || var4.length() == 0) {
         var4 = "/";
      }

      return new BasicRequestLine(var1, var4, var2);
   }

   public RequestConfig getConfig() {
      return this.config;
   }

   public void setConfig(RequestConfig var1) {
      this.config = var1;
   }

   public void setURI(URI var1) {
      this.uri = var1;
   }

   public void started() {
   }

   public void releaseConnection() {
      this.reset();
   }

   public String toString() {
      return this.getMethod() + " " + this.getURI() + " " + this.getProtocolVersion();
   }
}
