package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@Immutable
public class DefaultRedirectHandler implements RedirectHandler {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";

   public boolean isRedirectRequested(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      int var3 = var1.getStatusLine().getStatusCode();
      switch(var3) {
      case 301:
      case 302:
      case 307:
         HttpRequest var4 = (HttpRequest)var2.getAttribute("http.request");
         String var5 = var4.getRequestLine().getMethod();
         return var5.equalsIgnoreCase("GET") || var5.equalsIgnoreCase("HEAD");
      case 303:
         return true;
      case 304:
      case 305:
      case 306:
      default:
         return false;
      }
   }

   public URI getLocationURI(HttpResponse var1, HttpContext var2) throws ProtocolException {
      Args.notNull(var1, "HTTP response");
      Header var3 = var1.getFirstHeader("location");
      if (var3 == null) {
         throw new ProtocolException("Received redirect response " + var1.getStatusLine() + " but no location header");
      } else {
         String var4 = var3.getValue();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Redirect requested to location '" + var4 + "'");
         }

         URI var5;
         try {
            var5 = new URI(var4);
         } catch (URISyntaxException var13) {
            throw new ProtocolException("Invalid redirect URI: " + var4, var13);
         }

         HttpParams var6 = var1.getParams();
         if (!var5.isAbsolute()) {
            if (var6.isParameterTrue("http.protocol.reject-relative-redirect")) {
               throw new ProtocolException("Relative redirect location '" + var5 + "' not allowed");
            }

            HttpHost var7 = (HttpHost)var2.getAttribute("http.target_host");
            Asserts.notNull(var7, "Target host");
            HttpRequest var8 = (HttpRequest)var2.getAttribute("http.request");

            try {
               URI var9 = new URI(var8.getRequestLine().getUri());
               URI var10 = URIUtils.rewriteURI(var9, var7, true);
               var5 = URIUtils.resolve(var10, var5);
            } catch (URISyntaxException var12) {
               throw new ProtocolException(var12.getMessage(), var12);
            }
         }

         if (var6.isParameterFalse("http.protocol.allow-circular-redirects")) {
            RedirectLocations var14 = (RedirectLocations)var2.getAttribute("http.protocol.redirect-locations");
            if (var14 == null) {
               var14 = new RedirectLocations();
               var2.setAttribute("http.protocol.redirect-locations", var14);
            }

            URI var15;
            if (var5.getFragment() != null) {
               try {
                  HttpHost var16 = new HttpHost(var5.getHost(), var5.getPort(), var5.getScheme());
                  var15 = URIUtils.rewriteURI(var5, var16, true);
               } catch (URISyntaxException var11) {
                  throw new ProtocolException(var11.getMessage(), var11);
               }
            } else {
               var15 = var5;
            }

            if (var14.contains(var15)) {
               throw new CircularRedirectException("Circular redirect to '" + var15 + "'");
            }

            var14.add(var15);
         }

         return var5;
      }
   }
}
