package org.apache.http.client.protocol;

import java.util.List;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Lookup;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

@NotThreadSafe
public class HttpClientContext extends HttpCoreContext {
   public static final String HTTP_ROUTE = "http.route";
   public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
   public static final String COOKIESPEC_REGISTRY = "http.cookiespec-registry";
   public static final String COOKIE_SPEC = "http.cookie-spec";
   public static final String COOKIE_ORIGIN = "http.cookie-origin";
   public static final String COOKIE_STORE = "http.cookie-store";
   public static final String CREDS_PROVIDER = "http.auth.credentials-provider";
   public static final String AUTH_CACHE = "http.auth.auth-cache";
   public static final String TARGET_AUTH_STATE = "http.auth.target-scope";
   public static final String PROXY_AUTH_STATE = "http.auth.proxy-scope";
   public static final String USER_TOKEN = "http.user-token";
   public static final String AUTHSCHEME_REGISTRY = "http.authscheme-registry";
   public static final String REQUEST_CONFIG = "http.request-config";

   public static HttpClientContext adapt(HttpContext var0) {
      return var0 instanceof HttpClientContext ? (HttpClientContext)var0 : new HttpClientContext(var0);
   }

   public static HttpClientContext create() {
      return new HttpClientContext(new BasicHttpContext());
   }

   public HttpClientContext(HttpContext var1) {
      super(var1);
   }

   public HttpClientContext() {
   }

   public RouteInfo getHttpRoute() {
      return (RouteInfo)this.getAttribute("http.route", HttpRoute.class);
   }

   public List getRedirectLocations() {
      return (List)this.getAttribute("http.protocol.redirect-locations", List.class);
   }

   public CookieStore getCookieStore() {
      return (CookieStore)this.getAttribute("http.cookie-store", CookieStore.class);
   }

   public void setCookieStore(CookieStore var1) {
      this.setAttribute("http.cookie-store", var1);
   }

   public CookieSpec getCookieSpec() {
      return (CookieSpec)this.getAttribute("http.cookie-spec", CookieSpec.class);
   }

   public CookieOrigin getCookieOrigin() {
      return (CookieOrigin)this.getAttribute("http.cookie-origin", CookieOrigin.class);
   }

   private Lookup getLookup(String var1, Class var2) {
      return (Lookup)this.getAttribute(var1, Lookup.class);
   }

   public Lookup getCookieSpecRegistry() {
      return this.getLookup("http.cookiespec-registry", CookieSpecProvider.class);
   }

   public void setCookieSpecRegistry(Lookup var1) {
      this.setAttribute("http.cookiespec-registry", var1);
   }

   public Lookup getAuthSchemeRegistry() {
      return this.getLookup("http.authscheme-registry", AuthSchemeProvider.class);
   }

   public void setAuthSchemeRegistry(Lookup var1) {
      this.setAttribute("http.authscheme-registry", var1);
   }

   public CredentialsProvider getCredentialsProvider() {
      return (CredentialsProvider)this.getAttribute("http.auth.credentials-provider", CredentialsProvider.class);
   }

   public void setCredentialsProvider(CredentialsProvider var1) {
      this.setAttribute("http.auth.credentials-provider", var1);
   }

   public AuthCache getAuthCache() {
      return (AuthCache)this.getAttribute("http.auth.auth-cache", AuthCache.class);
   }

   public void setAuthCache(AuthCache var1) {
      this.setAttribute("http.auth.auth-cache", var1);
   }

   public AuthState getTargetAuthState() {
      return (AuthState)this.getAttribute("http.auth.target-scope", AuthState.class);
   }

   public AuthState getProxyAuthState() {
      return (AuthState)this.getAttribute("http.auth.proxy-scope", AuthState.class);
   }

   public Object getUserToken(Class var1) {
      return this.getAttribute("http.user-token", var1);
   }

   public Object getUserToken() {
      return this.getAttribute("http.user-token");
   }

   public void setUserToken(Object var1) {
      this.setAttribute("http.user-token", var1);
   }

   public RequestConfig getRequestConfig() {
      RequestConfig var1 = (RequestConfig)this.getAttribute("http.request-config", RequestConfig.class);
      return var1 != null ? var1 : RequestConfig.DEFAULT;
   }

   public void setRequestConfig(RequestConfig var1) {
      this.setAttribute("http.request-config", var1);
   }
}
