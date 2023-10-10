package io.netty.handler.codec.http.cors;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.internal.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CorsConfig {
   private final String origin;
   private final boolean enabled;
   private final Set exposeHeaders;
   private final boolean allowCredentials;
   private final long maxAge;
   private final Set allowedRequestMethods;
   private final Set allowedRequestHeaders;
   private final boolean allowNullOrigin;

   private CorsConfig(CorsConfig.Builder var1) {
      this.origin = CorsConfig.Builder.access$000(var1);
      this.enabled = CorsConfig.Builder.access$100(var1);
      this.exposeHeaders = CorsConfig.Builder.access$200(var1);
      this.allowCredentials = CorsConfig.Builder.access$300(var1);
      this.maxAge = CorsConfig.Builder.access$400(var1);
      this.allowedRequestMethods = CorsConfig.Builder.access$500(var1);
      this.allowedRequestHeaders = CorsConfig.Builder.access$600(var1);
      this.allowNullOrigin = CorsConfig.Builder.access$700(var1);
   }

   public boolean isCorsSupportEnabled() {
      return this.enabled;
   }

   public String origin() {
      return this.origin;
   }

   public boolean isNullOriginAllowed() {
      return this.allowNullOrigin;
   }

   public Set exposedHeaders() {
      return Collections.unmodifiableSet(this.exposeHeaders);
   }

   public boolean isCredentialsAllowed() {
      return this.allowCredentials;
   }

   public long maxAge() {
      return this.maxAge;
   }

   public Set allowedRequestMethods() {
      return Collections.unmodifiableSet(this.allowedRequestMethods);
   }

   public Set allowedRequestHeaders() {
      return Collections.unmodifiableSet(this.allowedRequestHeaders);
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "[enabled=" + this.enabled + ", origin=" + this.origin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ']';
   }

   public static CorsConfig.Builder anyOrigin() {
      return new CorsConfig.Builder("*");
   }

   public static CorsConfig.Builder withOrigin(String var0) {
      return new CorsConfig.Builder(var0);
   }

   CorsConfig(CorsConfig.Builder var1, Object var2) {
      this(var1);
   }

   public static class Builder {
      private final String origin;
      private boolean allowNullOrigin;
      private boolean enabled = true;
      private boolean allowCredentials;
      private final Set exposeHeaders = new HashSet();
      private long maxAge;
      private final Set requestMethods = new HashSet();
      private final Set requestHeaders = new HashSet();

      public Builder(String var1) {
         this.origin = var1;
      }

      public CorsConfig.Builder allowNullOrigin() {
         this.allowNullOrigin = true;
         return this;
      }

      public CorsConfig.Builder disable() {
         this.enabled = false;
         return this;
      }

      public CorsConfig.Builder exposeHeaders(String... var1) {
         this.exposeHeaders.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig.Builder allowCredentials() {
         this.allowCredentials = true;
         return this;
      }

      public CorsConfig.Builder maxAge(long var1) {
         this.maxAge = var1;
         return this;
      }

      public CorsConfig.Builder allowedRequestMethods(HttpMethod... var1) {
         this.requestMethods.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig.Builder allowedRequestHeaders(String... var1) {
         this.requestHeaders.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig build() {
         return new CorsConfig(this);
      }

      static String access$000(CorsConfig.Builder var0) {
         return var0.origin;
      }

      static boolean access$100(CorsConfig.Builder var0) {
         return var0.enabled;
      }

      static Set access$200(CorsConfig.Builder var0) {
         return var0.exposeHeaders;
      }

      static boolean access$300(CorsConfig.Builder var0) {
         return var0.allowCredentials;
      }

      static long access$400(CorsConfig.Builder var0) {
         return var0.maxAge;
      }

      static Set access$500(CorsConfig.Builder var0) {
         return var0.requestMethods;
      }

      static Set access$600(CorsConfig.Builder var0) {
         return var0.requestHeaders;
      }

      static boolean access$700(CorsConfig.Builder var0) {
         return var0.allowNullOrigin;
      }
   }
}
