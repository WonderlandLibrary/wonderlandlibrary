package org.apache.http.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class ImmutableHttpProcessor implements HttpProcessor {
   private final HttpRequestInterceptor[] requestInterceptors;
   private final HttpResponseInterceptor[] responseInterceptors;

   public ImmutableHttpProcessor(HttpRequestInterceptor[] var1, HttpResponseInterceptor[] var2) {
      int var3;
      if (var1 != null) {
         var3 = var1.length;
         this.requestInterceptors = new HttpRequestInterceptor[var3];
         System.arraycopy(var1, 0, this.requestInterceptors, 0, var3);
      } else {
         this.requestInterceptors = new HttpRequestInterceptor[0];
      }

      if (var2 != null) {
         var3 = var2.length;
         this.responseInterceptors = new HttpResponseInterceptor[var3];
         System.arraycopy(var2, 0, this.responseInterceptors, 0, var3);
      } else {
         this.responseInterceptors = new HttpResponseInterceptor[0];
      }

   }

   public ImmutableHttpProcessor(List var1, List var2) {
      int var3;
      if (var1 != null) {
         var3 = var1.size();
         this.requestInterceptors = (HttpRequestInterceptor[])var1.toArray(new HttpRequestInterceptor[var3]);
      } else {
         this.requestInterceptors = new HttpRequestInterceptor[0];
      }

      if (var2 != null) {
         var3 = var2.size();
         this.responseInterceptors = (HttpResponseInterceptor[])var2.toArray(new HttpResponseInterceptor[var3]);
      } else {
         this.responseInterceptors = new HttpResponseInterceptor[0];
      }

   }

   /** @deprecated */
   @Deprecated
   public ImmutableHttpProcessor(HttpRequestInterceptorList var1, HttpResponseInterceptorList var2) {
      int var3;
      int var4;
      if (var1 != null) {
         var3 = var1.getRequestInterceptorCount();
         this.requestInterceptors = new HttpRequestInterceptor[var3];

         for(var4 = 0; var4 < var3; ++var4) {
            this.requestInterceptors[var4] = var1.getRequestInterceptor(var4);
         }
      } else {
         this.requestInterceptors = new HttpRequestInterceptor[0];
      }

      if (var2 != null) {
         var3 = var2.getResponseInterceptorCount();
         this.responseInterceptors = new HttpResponseInterceptor[var3];

         for(var4 = 0; var4 < var3; ++var4) {
            this.responseInterceptors[var4] = var2.getResponseInterceptor(var4);
         }
      } else {
         this.responseInterceptors = new HttpResponseInterceptor[0];
      }

   }

   public ImmutableHttpProcessor(HttpRequestInterceptor... var1) {
      this((HttpRequestInterceptor[])var1, (HttpResponseInterceptor[])null);
   }

   public ImmutableHttpProcessor(HttpResponseInterceptor... var1) {
      this((HttpRequestInterceptor[])null, (HttpResponseInterceptor[])var1);
   }

   public void process(HttpRequest var1, HttpContext var2) throws IOException, HttpException {
      HttpRequestInterceptor[] var3 = this.requestInterceptors;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         HttpRequestInterceptor var6 = var3[var5];
         var6.process(var1, var2);
      }

   }

   public void process(HttpResponse var1, HttpContext var2) throws IOException, HttpException {
      HttpResponseInterceptor[] var3 = this.responseInterceptors;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         HttpResponseInterceptor var6 = var3[var5];
         var6.process(var1, var2);
      }

   }
}
