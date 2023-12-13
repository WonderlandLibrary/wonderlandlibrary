package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class URIUtils {
   /** @deprecated */
   @Deprecated
   public static URI createURI(String var0, String var1, int var2, String var3, String var4, String var5) throws URISyntaxException {
      StringBuilder var6 = new StringBuilder();
      if (var1 != null) {
         if (var0 != null) {
            var6.append(var0);
            var6.append("://");
         }

         var6.append(var1);
         if (var2 > 0) {
            var6.append(':');
            var6.append(var2);
         }
      }

      if (var3 == null || !var3.startsWith("/")) {
         var6.append('/');
      }

      if (var3 != null) {
         var6.append(var3);
      }

      if (var4 != null) {
         var6.append('?');
         var6.append(var4);
      }

      if (var5 != null) {
         var6.append('#');
         var6.append(var5);
      }

      return new URI(var6.toString());
   }

   public static URI rewriteURI(URI var0, HttpHost var1, boolean var2) throws URISyntaxException {
      Args.notNull(var0, "URI");
      if (var0.isOpaque()) {
         return var0;
      } else {
         URIBuilder var3 = new URIBuilder(var0);
         if (var1 != null) {
            var3.setScheme(var1.getSchemeName());
            var3.setHost(var1.getHostName());
            var3.setPort(var1.getPort());
         } else {
            var3.setScheme((String)null);
            var3.setHost((String)null);
            var3.setPort(-1);
         }

         if (var2) {
            var3.setFragment((String)null);
         }

         if (TextUtils.isEmpty(var3.getPath())) {
            var3.setPath("/");
         }

         return var3.build();
      }
   }

   public static URI rewriteURI(URI var0, HttpHost var1) throws URISyntaxException {
      return rewriteURI(var0, var1, false);
   }

   public static URI rewriteURI(URI var0) throws URISyntaxException {
      Args.notNull(var0, "URI");
      if (var0.isOpaque()) {
         return var0;
      } else {
         URIBuilder var1 = new URIBuilder(var0);
         if (var1.getUserInfo() != null) {
            var1.setUserInfo((String)null);
         }

         if (TextUtils.isEmpty(var1.getPath())) {
            var1.setPath("/");
         }

         if (var1.getHost() != null) {
            var1.setHost(var1.getHost().toLowerCase(Locale.ENGLISH));
         }

         var1.setFragment((String)null);
         return var1.build();
      }
   }

   public static URI resolve(URI var0, String var1) {
      return resolve(var0, URI.create(var1));
   }

   public static URI resolve(URI var0, URI var1) {
      Args.notNull(var0, "Base URI");
      Args.notNull(var1, "Reference URI");
      URI var2 = var1;
      String var3 = var1.toString();
      if (var3.startsWith("?")) {
         return resolveReferenceStartingWithQueryString(var0, var1);
      } else {
         boolean var4 = var3.length() == 0;
         if (var4) {
            var2 = URI.create("#");
         }

         URI var5 = var0.resolve(var2);
         if (var4) {
            String var6 = var5.toString();
            var5 = URI.create(var6.substring(0, var6.indexOf(35)));
         }

         return normalizeSyntax(var5);
      }
   }

   private static URI resolveReferenceStartingWithQueryString(URI var0, URI var1) {
      String var2 = var0.toString();
      var2 = var2.indexOf(63) > -1 ? var2.substring(0, var2.indexOf(63)) : var2;
      return URI.create(var2 + var1.toString());
   }

   private static URI normalizeSyntax(URI var0) {
      if (!var0.isOpaque() && var0.getAuthority() != null) {
         Args.check(var0.isAbsolute(), "Base URI must be absolute");
         String var1 = var0.getPath() == null ? "" : var0.getPath();
         String[] var2 = var1.split("/");
         Stack var3 = new Stack();
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            if (var7.length() != 0 && !".".equals(var7)) {
               if ("..".equals(var7)) {
                  if (!var3.isEmpty()) {
                     var3.pop();
                  }
               } else {
                  var3.push(var7);
               }
            }
         }

         StringBuilder var10 = new StringBuilder();
         Iterator var11 = var3.iterator();

         String var13;
         while(var11.hasNext()) {
            var13 = (String)var11.next();
            var10.append('/').append(var13);
         }

         if (var1.lastIndexOf(47) == var1.length() - 1) {
            var10.append('/');
         }

         try {
            String var12 = var0.getScheme().toLowerCase();
            var13 = var0.getAuthority().toLowerCase();
            URI var14 = new URI(var12, var13, var10.toString(), (String)null, (String)null);
            if (var0.getQuery() == null && var0.getFragment() == null) {
               return var14;
            } else {
               StringBuilder var8 = new StringBuilder(var14.toASCIIString());
               if (var0.getQuery() != null) {
                  var8.append('?').append(var0.getRawQuery());
               }

               if (var0.getFragment() != null) {
                  var8.append('#').append(var0.getRawFragment());
               }

               return URI.create(var8.toString());
            }
         } catch (URISyntaxException var9) {
            throw new IllegalArgumentException(var9);
         }
      } else {
         return var0;
      }
   }

   public static HttpHost extractHost(URI var0) {
      if (var0 == null) {
         return null;
      } else {
         HttpHost var1 = null;
         if (var0.isAbsolute()) {
            int var2 = var0.getPort();
            String var3 = var0.getHost();
            if (var3 == null) {
               var3 = var0.getAuthority();
               if (var3 != null) {
                  int var4 = var3.indexOf(64);
                  if (var4 >= 0) {
                     if (var3.length() > var4 + 1) {
                        var3 = var3.substring(var4 + 1);
                     } else {
                        var3 = null;
                     }
                  }

                  if (var3 != null) {
                     int var5 = var3.indexOf(58);
                     if (var5 >= 0) {
                        int var6 = var5 + 1;
                        int var7 = 0;

                        for(int var8 = var6; var8 < var3.length() && Character.isDigit(var3.charAt(var8)); ++var8) {
                           ++var7;
                        }

                        if (var7 > 0) {
                           try {
                              var2 = Integer.parseInt(var3.substring(var6, var6 + var7));
                           } catch (NumberFormatException var9) {
                           }
                        }

                        var3 = var3.substring(0, var5);
                     }
                  }
               }
            }

            String var10 = var0.getScheme();
            if (var3 != null) {
               var1 = new HttpHost(var3, var2, var10);
            }
         }

         return var1;
      }
   }

   public static URI resolve(URI var0, HttpHost var1, List var2) throws URISyntaxException {
      Args.notNull(var0, "Request URI");
      URIBuilder var3;
      if (var2 != null && !var2.isEmpty()) {
         var3 = new URIBuilder((URI)var2.get(var2.size() - 1));
         String var4 = var3.getFragment();

         for(int var5 = var2.size() - 1; var4 == null && var5 >= 0; --var5) {
            var4 = ((URI)var2.get(var5)).getFragment();
         }

         var3.setFragment(var4);
      } else {
         var3 = new URIBuilder(var0);
      }

      if (var3.getFragment() == null) {
         var3.setFragment(var0.getFragment());
      }

      if (var1 != null && !var3.isAbsolute()) {
         var3.setScheme(var1.getSchemeName());
         var3.setHost(var1.getHostName());
         var3.setPort(var1.getPort());
      }

      return var3.build();
   }

   private URIUtils() {
   }
}
