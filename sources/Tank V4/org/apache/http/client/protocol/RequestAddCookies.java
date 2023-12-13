package org.apache.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class RequestAddCookies implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      String var3 = var1.getRequestLine().getMethod();
      if (!var3.equalsIgnoreCase("CONNECT")) {
         HttpClientContext var4 = HttpClientContext.adapt(var2);
         CookieStore var5 = var4.getCookieStore();
         if (var5 == null) {
            this.log.debug("Cookie store not specified in HTTP context");
         } else {
            Lookup var6 = var4.getCookieSpecRegistry();
            if (var6 == null) {
               this.log.debug("CookieSpec registry not specified in HTTP context");
            } else {
               HttpHost var7 = var4.getTargetHost();
               if (var7 == null) {
                  this.log.debug("Target host not set in the context");
               } else {
                  RouteInfo var8 = var4.getHttpRoute();
                  if (var8 == null) {
                     this.log.debug("Connection route not set in the context");
                  } else {
                     RequestConfig var9 = var4.getRequestConfig();
                     String var10 = var9.getCookieSpec();
                     if (var10 == null) {
                        var10 = "best-match";
                     }

                     if (this.log.isDebugEnabled()) {
                        this.log.debug("CookieSpec selected: " + var10);
                     }

                     URI var11 = null;
                     if (var1 instanceof HttpUriRequest) {
                        var11 = ((HttpUriRequest)var1).getURI();
                     } else {
                        try {
                           var11 = new URI(var1.getRequestLine().getUri());
                        } catch (URISyntaxException var25) {
                        }
                     }

                     String var12 = var11 != null ? var11.getPath() : null;
                     String var13 = var7.getHostName();
                     int var14 = var7.getPort();
                     if (var14 < 0) {
                        var14 = var8.getTargetHost().getPort();
                     }

                     CookieOrigin var15 = new CookieOrigin(var13, var14 >= 0 ? var14 : 0, !TextUtils.isEmpty(var12) ? var12 : "/", var8.isSecure());
                     CookieSpecProvider var16 = (CookieSpecProvider)var6.lookup(var10);
                     if (var16 == null) {
                        throw new HttpException("Unsupported cookie policy: " + var10);
                     } else {
                        CookieSpec var17 = var16.create(var4);
                        ArrayList var18 = new ArrayList(var5.getCookies());
                        ArrayList var19 = new ArrayList();
                        Date var20 = new Date();
                        Iterator var21 = var18.iterator();

                        while(var21.hasNext()) {
                           Cookie var22 = (Cookie)var21.next();
                           if (!var22.isExpired(var20)) {
                              if (var17.match(var22, var15)) {
                                 if (this.log.isDebugEnabled()) {
                                    this.log.debug("Cookie " + var22 + " match " + var15);
                                 }

                                 var19.add(var22);
                              }
                           } else if (this.log.isDebugEnabled()) {
                              this.log.debug("Cookie " + var22 + " expired");
                           }
                        }

                        Header var23;
                        if (!var19.isEmpty()) {
                           List var26 = var17.formatCookies(var19);
                           Iterator var28 = var26.iterator();

                           while(var28.hasNext()) {
                              var23 = (Header)var28.next();
                              var1.addHeader(var23);
                           }
                        }

                        int var27 = var17.getVersion();
                        if (var27 > 0) {
                           boolean var29 = false;
                           Iterator var30 = var19.iterator();

                           label106:
                           while(true) {
                              Cookie var24;
                              do {
                                 if (!var30.hasNext()) {
                                    if (var29) {
                                       var23 = var17.getVersionHeader();
                                       if (var23 != null) {
                                          var1.addHeader(var23);
                                       }
                                    }
                                    break label106;
                                 }

                                 var24 = (Cookie)var30.next();
                              } while(var27 == var24.getVersion() && var24 instanceof SetCookie2);

                              var29 = true;
                           }
                        }

                        var2.setAttribute("http.cookie-spec", var17);
                        var2.setAttribute("http.cookie-origin", var15);
                     }
                  }
               }
            }
         }
      }
   }
}
