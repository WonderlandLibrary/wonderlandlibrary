package org.apache.http.client.config;

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;

public class RequestConfig implements Cloneable {
   public static final RequestConfig DEFAULT = (new RequestConfig.Builder()).build();
   private final boolean expectContinueEnabled;
   private final HttpHost proxy;
   private final InetAddress localAddress;
   private final boolean staleConnectionCheckEnabled;
   private final String cookieSpec;
   private final boolean redirectsEnabled;
   private final boolean relativeRedirectsAllowed;
   private final boolean circularRedirectsAllowed;
   private final int maxRedirects;
   private final boolean authenticationEnabled;
   private final Collection targetPreferredAuthSchemes;
   private final Collection proxyPreferredAuthSchemes;
   private final int connectionRequestTimeout;
   private final int connectTimeout;
   private final int socketTimeout;

   RequestConfig(boolean var1, HttpHost var2, InetAddress var3, boolean var4, String var5, boolean var6, boolean var7, boolean var8, int var9, boolean var10, Collection var11, Collection var12, int var13, int var14, int var15) {
      this.expectContinueEnabled = var1;
      this.proxy = var2;
      this.localAddress = var3;
      this.staleConnectionCheckEnabled = var4;
      this.cookieSpec = var5;
      this.redirectsEnabled = var6;
      this.relativeRedirectsAllowed = var7;
      this.circularRedirectsAllowed = var8;
      this.maxRedirects = var9;
      this.authenticationEnabled = var10;
      this.targetPreferredAuthSchemes = var11;
      this.proxyPreferredAuthSchemes = var12;
      this.connectionRequestTimeout = var13;
      this.connectTimeout = var14;
      this.socketTimeout = var15;
   }

   public boolean isExpectContinueEnabled() {
      return this.expectContinueEnabled;
   }

   public HttpHost getProxy() {
      return this.proxy;
   }

   public InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public boolean isStaleConnectionCheckEnabled() {
      return this.staleConnectionCheckEnabled;
   }

   public String getCookieSpec() {
      return this.cookieSpec;
   }

   public boolean isRedirectsEnabled() {
      return this.redirectsEnabled;
   }

   public boolean isRelativeRedirectsAllowed() {
      return this.relativeRedirectsAllowed;
   }

   public boolean isCircularRedirectsAllowed() {
      return this.circularRedirectsAllowed;
   }

   public int getMaxRedirects() {
      return this.maxRedirects;
   }

   public boolean isAuthenticationEnabled() {
      return this.authenticationEnabled;
   }

   public Collection getTargetPreferredAuthSchemes() {
      return this.targetPreferredAuthSchemes;
   }

   public Collection getProxyPreferredAuthSchemes() {
      return this.proxyPreferredAuthSchemes;
   }

   public int getConnectionRequestTimeout() {
      return this.connectionRequestTimeout;
   }

   public int getConnectTimeout() {
      return this.connectTimeout;
   }

   public int getSocketTimeout() {
      return this.socketTimeout;
   }

   protected RequestConfig clone() throws CloneNotSupportedException {
      return (RequestConfig)super.clone();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(", expectContinueEnabled=").append(this.expectContinueEnabled);
      var1.append(", proxy=").append(this.proxy);
      var1.append(", localAddress=").append(this.localAddress);
      var1.append(", staleConnectionCheckEnabled=").append(this.staleConnectionCheckEnabled);
      var1.append(", cookieSpec=").append(this.cookieSpec);
      var1.append(", redirectsEnabled=").append(this.redirectsEnabled);
      var1.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
      var1.append(", maxRedirects=").append(this.maxRedirects);
      var1.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
      var1.append(", authenticationEnabled=").append(this.authenticationEnabled);
      var1.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
      var1.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
      var1.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
      var1.append(", connectTimeout=").append(this.connectTimeout);
      var1.append(", socketTimeout=").append(this.socketTimeout);
      var1.append("]");
      return var1.toString();
   }

   public static RequestConfig.Builder custom() {
      return new RequestConfig.Builder();
   }

   public static RequestConfig.Builder copy(RequestConfig var0) {
      return (new RequestConfig.Builder()).setExpectContinueEnabled(var0.isExpectContinueEnabled()).setProxy(var0.getProxy()).setLocalAddress(var0.getLocalAddress()).setStaleConnectionCheckEnabled(var0.isStaleConnectionCheckEnabled()).setCookieSpec(var0.getCookieSpec()).setRedirectsEnabled(var0.isRedirectsEnabled()).setRelativeRedirectsAllowed(var0.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(var0.isCircularRedirectsAllowed()).setMaxRedirects(var0.getMaxRedirects()).setAuthenticationEnabled(var0.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(var0.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(var0.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(var0.getConnectionRequestTimeout()).setConnectTimeout(var0.getConnectTimeout()).setSocketTimeout(var0.getSocketTimeout());
   }

   protected Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   public static class Builder {
      private boolean expectContinueEnabled;
      private HttpHost proxy;
      private InetAddress localAddress;
      private boolean staleConnectionCheckEnabled = true;
      private String cookieSpec;
      private boolean redirectsEnabled = true;
      private boolean relativeRedirectsAllowed = true;
      private boolean circularRedirectsAllowed;
      private int maxRedirects = 50;
      private boolean authenticationEnabled = true;
      private Collection targetPreferredAuthSchemes;
      private Collection proxyPreferredAuthSchemes;
      private int connectionRequestTimeout = -1;
      private int connectTimeout = -1;
      private int socketTimeout = -1;

      Builder() {
      }

      public RequestConfig.Builder setExpectContinueEnabled(boolean var1) {
         this.expectContinueEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setProxy(HttpHost var1) {
         this.proxy = var1;
         return this;
      }

      public RequestConfig.Builder setLocalAddress(InetAddress var1) {
         this.localAddress = var1;
         return this;
      }

      public RequestConfig.Builder setStaleConnectionCheckEnabled(boolean var1) {
         this.staleConnectionCheckEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setCookieSpec(String var1) {
         this.cookieSpec = var1;
         return this;
      }

      public RequestConfig.Builder setRedirectsEnabled(boolean var1) {
         this.redirectsEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setRelativeRedirectsAllowed(boolean var1) {
         this.relativeRedirectsAllowed = var1;
         return this;
      }

      public RequestConfig.Builder setCircularRedirectsAllowed(boolean var1) {
         this.circularRedirectsAllowed = var1;
         return this;
      }

      public RequestConfig.Builder setMaxRedirects(int var1) {
         this.maxRedirects = var1;
         return this;
      }

      public RequestConfig.Builder setAuthenticationEnabled(boolean var1) {
         this.authenticationEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setTargetPreferredAuthSchemes(Collection var1) {
         this.targetPreferredAuthSchemes = var1;
         return this;
      }

      public RequestConfig.Builder setProxyPreferredAuthSchemes(Collection var1) {
         this.proxyPreferredAuthSchemes = var1;
         return this;
      }

      public RequestConfig.Builder setConnectionRequestTimeout(int var1) {
         this.connectionRequestTimeout = var1;
         return this;
      }

      public RequestConfig.Builder setConnectTimeout(int var1) {
         this.connectTimeout = var1;
         return this;
      }

      public RequestConfig.Builder setSocketTimeout(int var1) {
         this.socketTimeout = var1;
         return this;
      }

      public RequestConfig build() {
         return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout);
      }
   }
}
