package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest {
   private final HttpRequest original;
   private URI uri;
   private String method;
   private ProtocolVersion version;
   private int execCount;

   public RequestWrapper(HttpRequest var1) throws ProtocolException {
      Args.notNull(var1, "HTTP request");
      this.original = var1;
      this.setParams(var1.getParams());
      this.setHeaders(var1.getAllHeaders());
      if (var1 instanceof HttpUriRequest) {
         this.uri = ((HttpUriRequest)var1).getURI();
         this.method = ((HttpUriRequest)var1).getMethod();
         this.version = null;
      } else {
         RequestLine var2 = var1.getRequestLine();

         try {
            this.uri = new URI(var2.getUri());
         } catch (URISyntaxException var4) {
            throw new ProtocolException("Invalid request URI: " + var2.getUri(), var4);
         }

         this.method = var2.getMethod();
         this.version = var1.getProtocolVersion();
      }

      this.execCount = 0;
   }

   public void resetHeaders() {
      this.headergroup.clear();
      this.setHeaders(this.original.getAllHeaders());
   }

   public String getMethod() {
      return this.method;
   }

   public void setMethod(String var1) {
      Args.notNull(var1, "Method name");
      this.method = var1;
   }

   public ProtocolVersion getProtocolVersion() {
      if (this.version == null) {
         this.version = HttpProtocolParams.getVersion(this.getParams());
      }

      return this.version;
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

   public RequestLine getRequestLine() {
      String var1 = this.getMethod();
      ProtocolVersion var2 = this.getProtocolVersion();
      String var3 = null;
      if (this.uri != null) {
         var3 = this.uri.toASCIIString();
      }

      if (var3 == null || var3.length() == 0) {
         var3 = "/";
      }

      return new BasicRequestLine(var1, var3, var2);
   }

   public void abort() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }

   public boolean isAborted() {
      return false;
   }

   public HttpRequest getOriginal() {
      return this.original;
   }

   public boolean isRepeatable() {
      return true;
   }

   public int getExecCount() {
      return this.execCount;
   }

   public void incrementExecCount() {
      ++this.execCount;
   }
}
