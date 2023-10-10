package org.apache.http.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class UriHttpRequestHandlerMapper implements HttpRequestHandlerMapper {
   private final UriPatternMatcher matcher;

   protected UriHttpRequestHandlerMapper(UriPatternMatcher var1) {
      this.matcher = (UriPatternMatcher)Args.notNull(var1, "Pattern matcher");
   }

   public UriHttpRequestHandlerMapper() {
      this(new UriPatternMatcher());
   }

   public void register(String var1, HttpRequestHandler var2) {
      Args.notNull(var1, "Pattern");
      Args.notNull(var2, "Handler");
      this.matcher.register(var1, var2);
   }

   public void unregister(String var1) {
      this.matcher.unregister(var1);
   }

   protected String getRequestPath(HttpRequest var1) {
      String var2 = var1.getRequestLine().getUri();
      int var3 = var2.indexOf("?");
      if (var3 != -1) {
         var2 = var2.substring(0, var3);
      } else {
         var3 = var2.indexOf("#");
         if (var3 != -1) {
            var2 = var2.substring(0, var3);
         }
      }

      return var2;
   }

   public HttpRequestHandler lookup(HttpRequest var1) {
      Args.notNull(var1, "HTTP request");
      return (HttpRequestHandler)this.matcher.lookup(this.getRequestPath(var1));
   }
}
