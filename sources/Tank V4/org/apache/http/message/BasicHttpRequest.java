package org.apache.http.message;

import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest {
   private final String method;
   private final String uri;
   private RequestLine requestline;

   public BasicHttpRequest(String var1, String var2) {
      this.method = (String)Args.notNull(var1, "Method name");
      this.uri = (String)Args.notNull(var2, "Request URI");
      this.requestline = null;
   }

   public BasicHttpRequest(String var1, String var2, ProtocolVersion var3) {
      this(new BasicRequestLine(var1, var2, var3));
   }

   public BasicHttpRequest(RequestLine var1) {
      this.requestline = (RequestLine)Args.notNull(var1, "Request line");
      this.method = var1.getMethod();
      this.uri = var1.getUri();
   }

   public ProtocolVersion getProtocolVersion() {
      return this.getRequestLine().getProtocolVersion();
   }

   public RequestLine getRequestLine() {
      if (this.requestline == null) {
         this.requestline = new BasicRequestLine(this.method, this.uri, HttpVersion.HTTP_1_1);
      }

      return this.requestline;
   }

   public String toString() {
      return this.method + " " + this.uri + " " + this.headergroup;
   }
}
