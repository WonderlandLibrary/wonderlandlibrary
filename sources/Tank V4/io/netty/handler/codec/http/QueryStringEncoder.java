package io.netty.handler.codec.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

public class QueryStringEncoder {
   private final Charset charset;
   private final String uri;
   private final List params;

   public QueryStringEncoder(String var1) {
      this(var1, HttpConstants.DEFAULT_CHARSET);
   }

   public QueryStringEncoder(String var1, Charset var2) {
      this.params = new ArrayList();
      if (var1 == null) {
         throw new NullPointerException("getUri");
      } else if (var2 == null) {
         throw new NullPointerException("charset");
      } else {
         this.uri = var1;
         this.charset = var2;
      }
   }

   public void addParam(String var1, String var2) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else if (var2 == null) {
         throw new NullPointerException("value");
      } else {
         this.params.add(new QueryStringEncoder.Param(var1, var2));
      }
   }

   public URI toUri() throws URISyntaxException {
      return new URI(this.toString());
   }

   public String toString() {
      if (this.params.isEmpty()) {
         return this.uri;
      } else {
         StringBuilder var1 = (new StringBuilder(this.uri)).append('?');

         for(int var2 = 0; var2 < this.params.size(); ++var2) {
            QueryStringEncoder.Param var3 = (QueryStringEncoder.Param)this.params.get(var2);
            var1.append(encodeComponent(var3.name, this.charset));
            var1.append('=');
            var1.append(encodeComponent(var3.value, this.charset));
            if (var2 != this.params.size() - 1) {
               var1.append('&');
            }
         }

         return var1.toString();
      }
   }

   private static String encodeComponent(String var0, Charset var1) {
      try {
         return URLEncoder.encode(var0, var1.name()).replace("+", "%20");
      } catch (UnsupportedEncodingException var3) {
         throw new UnsupportedCharsetException(var1.name());
      }
   }

   private static final class Param {
      final String name;
      final String value;

      Param(String var1, String var2) {
         this.value = var2;
         this.name = var1;
      }
   }
}
