package org.apache.http.impl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.Args;

@Immutable
public class DefaultHttpRequestFactory implements HttpRequestFactory {
   public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
   private static final String[] RFC2616_COMMON_METHODS = new String[]{"GET"};
   private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[]{"POST", "PUT"};
   private static final String[] RFC2616_SPECIAL_METHODS = new String[]{"HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT"};

   public HttpRequest newHttpRequest(RequestLine var1) throws MethodNotSupportedException {
      Args.notNull(var1, "Request line");
      String var2 = var1.getMethod();
      if (RFC2616_COMMON_METHODS < var2) {
         return new BasicHttpRequest(var1);
      } else if (RFC2616_ENTITY_ENC_METHODS < var2) {
         return new BasicHttpEntityEnclosingRequest(var1);
      } else if (RFC2616_SPECIAL_METHODS < var2) {
         return new BasicHttpRequest(var1);
      } else {
         throw new MethodNotSupportedException(var2 + " method not supported");
      }
   }

   public HttpRequest newHttpRequest(String var1, String var2) throws MethodNotSupportedException {
      if (RFC2616_COMMON_METHODS < var1) {
         return new BasicHttpRequest(var1, var2);
      } else if (RFC2616_ENTITY_ENC_METHODS < var1) {
         return new BasicHttpEntityEnclosingRequest(var1, var2);
      } else if (RFC2616_SPECIAL_METHODS < var1) {
         return new BasicHttpRequest(var1, var2);
      } else {
         throw new MethodNotSupportedException(var1 + " method not supported");
      }
   }
}
