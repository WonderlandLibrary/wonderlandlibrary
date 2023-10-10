package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;

@NotThreadSafe
public class URIBuilder {
   private String scheme;
   private String encodedSchemeSpecificPart;
   private String encodedAuthority;
   private String userInfo;
   private String encodedUserInfo;
   private String host;
   private int port;
   private String path;
   private String encodedPath;
   private String encodedQuery;
   private List queryParams;
   private String query;
   private String fragment;
   private String encodedFragment;

   public URIBuilder() {
      this.port = -1;
   }

   public URIBuilder(String var1) throws URISyntaxException {
      this.digestURI(new URI(var1));
   }

   public URIBuilder(URI var1) {
      this.digestURI(var1);
   }

   private List parseQuery(String var1, Charset var2) {
      return var1 != null && var1.length() > 0 ? URLEncodedUtils.parse(var1, var2) : null;
   }

   public URI build() throws URISyntaxException {
      return new URI(this.buildString());
   }

   private String buildString() {
      StringBuilder var1 = new StringBuilder();
      if (this.scheme != null) {
         var1.append(this.scheme).append(':');
      }

      if (this.encodedSchemeSpecificPart != null) {
         var1.append(this.encodedSchemeSpecificPart);
      } else {
         if (this.encodedAuthority != null) {
            var1.append("//").append(this.encodedAuthority);
         } else if (this.host != null) {
            var1.append("//");
            if (this.encodedUserInfo != null) {
               var1.append(this.encodedUserInfo).append("@");
            } else if (this.userInfo != null) {
               var1.append(this.encodeUserInfo(this.userInfo)).append("@");
            }

            if (InetAddressUtils.isIPv6Address(this.host)) {
               var1.append("[").append(this.host).append("]");
            } else {
               var1.append(this.host);
            }

            if (this.port >= 0) {
               var1.append(":").append(this.port);
            }
         }

         if (this.encodedPath != null) {
            var1.append(normalizePath(this.encodedPath));
         } else if (this.path != null) {
            var1.append(this.encodePath(normalizePath(this.path)));
         }

         if (this.encodedQuery != null) {
            var1.append("?").append(this.encodedQuery);
         } else if (this.queryParams != null) {
            var1.append("?").append(this.encodeUrlForm(this.queryParams));
         } else if (this.query != null) {
            var1.append("?").append(this.encodeUric(this.query));
         }
      }

      if (this.encodedFragment != null) {
         var1.append("#").append(this.encodedFragment);
      } else if (this.fragment != null) {
         var1.append("#").append(this.encodeUric(this.fragment));
      }

      return var1.toString();
   }

   private void digestURI(URI var1) {
      this.scheme = var1.getScheme();
      this.encodedSchemeSpecificPart = var1.getRawSchemeSpecificPart();
      this.encodedAuthority = var1.getRawAuthority();
      this.host = var1.getHost();
      this.port = var1.getPort();
      this.encodedUserInfo = var1.getRawUserInfo();
      this.userInfo = var1.getUserInfo();
      this.encodedPath = var1.getRawPath();
      this.path = var1.getPath();
      this.encodedQuery = var1.getRawQuery();
      this.queryParams = this.parseQuery(var1.getRawQuery(), Consts.UTF_8);
      this.encodedFragment = var1.getRawFragment();
      this.fragment = var1.getFragment();
   }

   private String encodeUserInfo(String var1) {
      return URLEncodedUtils.encUserInfo(var1, Consts.UTF_8);
   }

   private String encodePath(String var1) {
      return URLEncodedUtils.encPath(var1, Consts.UTF_8);
   }

   private String encodeUrlForm(List var1) {
      return URLEncodedUtils.format((Iterable)var1, (Charset)Consts.UTF_8);
   }

   private String encodeUric(String var1) {
      return URLEncodedUtils.encUric(var1, Consts.UTF_8);
   }

   public URIBuilder setScheme(String var1) {
      this.scheme = var1;
      return this;
   }

   public URIBuilder setUserInfo(String var1) {
      this.userInfo = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      this.encodedUserInfo = null;
      return this;
   }

   public URIBuilder setUserInfo(String var1, String var2) {
      return this.setUserInfo(var1 + ':' + var2);
   }

   public URIBuilder setHost(String var1) {
      this.host = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      return this;
   }

   public URIBuilder setPort(int var1) {
      this.port = var1 < 0 ? -1 : var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      return this;
   }

   public URIBuilder setPath(String var1) {
      this.path = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedPath = null;
      return this;
   }

   public URIBuilder removeQuery() {
      this.queryParams = null;
      this.query = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public URIBuilder setQuery(String var1) {
      this.queryParams = this.parseQuery(var1, Consts.UTF_8);
      this.query = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   public URIBuilder setParameters(List var1) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      } else {
         this.queryParams.clear();
      }

      this.queryParams.addAll(var1);
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder addParameters(List var1) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      this.queryParams.addAll(var1);
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder setParameters(NameValuePair... var1) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      } else {
         this.queryParams.clear();
      }

      NameValuePair[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         NameValuePair var5 = var2[var4];
         this.queryParams.add(var5);
      }

      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder addParameter(String var1, String var2) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      this.queryParams.add(new BasicNameValuePair(var1, var2));
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder setParameter(String var1, String var2) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      if (!this.queryParams.isEmpty()) {
         Iterator var3 = this.queryParams.iterator();

         while(var3.hasNext()) {
            NameValuePair var4 = (NameValuePair)var3.next();
            if (var4.getName().equals(var1)) {
               var3.remove();
            }
         }
      }

      this.queryParams.add(new BasicNameValuePair(var1, var2));
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder clearParameters() {
      this.queryParams = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   public URIBuilder setCustomQuery(String var1) {
      this.query = var1;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.queryParams = null;
      return this;
   }

   public URIBuilder setFragment(String var1) {
      this.fragment = var1;
      this.encodedFragment = null;
      return this;
   }

   public boolean isAbsolute() {
      return this.scheme != null;
   }

   public boolean isOpaque() {
      return this.path == null;
   }

   public String getScheme() {
      return this.scheme;
   }

   public String getUserInfo() {
      return this.userInfo;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getPath() {
      return this.path;
   }

   public List getQueryParams() {
      return this.queryParams != null ? new ArrayList(this.queryParams) : new ArrayList();
   }

   public String getFragment() {
      return this.fragment;
   }

   public String toString() {
      return this.buildString();
   }

   private static String normalizePath(String var0) {
      String var1 = var0;
      if (var0 == null) {
         return null;
      } else {
         int var2;
         for(var2 = 0; var2 < var1.length() && var1.charAt(var2) == '/'; ++var2) {
         }

         if (var2 > 1) {
            var1 = var1.substring(var2 - 1);
         }

         return var1;
      }
   }
}
