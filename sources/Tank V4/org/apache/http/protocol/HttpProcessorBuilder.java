package org.apache.http.protocol;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

public class HttpProcessorBuilder {
   private ChainBuilder requestChainBuilder;
   private ChainBuilder responseChainBuilder;

   public static HttpProcessorBuilder create() {
      return new HttpProcessorBuilder();
   }

   HttpProcessorBuilder() {
   }

   private ChainBuilder getRequestChainBuilder() {
      if (this.requestChainBuilder == null) {
         this.requestChainBuilder = new ChainBuilder();
      }

      return this.requestChainBuilder;
   }

   private ChainBuilder getResponseChainBuilder() {
      if (this.responseChainBuilder == null) {
         this.responseChainBuilder = new ChainBuilder();
      }

      return this.responseChainBuilder;
   }

   public HttpProcessorBuilder addFirst(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getRequestChainBuilder().addFirst(var1);
         return this;
      }
   }

   public HttpProcessorBuilder addLast(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getRequestChainBuilder().addLast(var1);
         return this;
      }
   }

   public HttpProcessorBuilder add(HttpRequestInterceptor var1) {
      return this.addLast(var1);
   }

   public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor... var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getRequestChainBuilder().addAllFirst((Object[])var1);
         return this;
      }
   }

   public HttpProcessorBuilder addAllLast(HttpRequestInterceptor... var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getRequestChainBuilder().addAllLast((Object[])var1);
         return this;
      }
   }

   public HttpProcessorBuilder addAll(HttpRequestInterceptor... var1) {
      return this.addAllLast(var1);
   }

   public HttpProcessorBuilder addFirst(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getResponseChainBuilder().addFirst(var1);
         return this;
      }
   }

   public HttpProcessorBuilder addLast(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getResponseChainBuilder().addLast(var1);
         return this;
      }
   }

   public HttpProcessorBuilder add(HttpResponseInterceptor var1) {
      return this.addLast(var1);
   }

   public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor... var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getResponseChainBuilder().addAllFirst((Object[])var1);
         return this;
      }
   }

   public HttpProcessorBuilder addAllLast(HttpResponseInterceptor... var1) {
      if (var1 == null) {
         return this;
      } else {
         this.getResponseChainBuilder().addAllLast((Object[])var1);
         return this;
      }
   }

   public HttpProcessorBuilder addAll(HttpResponseInterceptor... var1) {
      return this.addAllLast(var1);
   }

   public HttpProcessor build() {
      return new ImmutableHttpProcessor(this.requestChainBuilder != null ? this.requestChainBuilder.build() : null, this.responseChainBuilder != null ? this.responseChainBuilder.build() : null);
   }
}
