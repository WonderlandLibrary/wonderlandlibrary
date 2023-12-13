package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;
import org.apache.http.util.CharArrayBuffer;

/** @deprecated */
@Deprecated
@Immutable
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final List DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("negotiate", "NTLM", "Digest", "Basic"));

   protected Map parseChallenges(Header[] var1) throws MalformedChallengeException {
      HashMap var2 = new HashMap(var1.length);
      Header[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Header var6 = var3[var5];
         CharArrayBuffer var7;
         int var8;
         if (var6 instanceof FormattedHeader) {
            var7 = ((FormattedHeader)var6).getBuffer();
            var8 = ((FormattedHeader)var6).getValuePos();
         } else {
            String var9 = var6.getValue();
            if (var9 == null) {
               throw new MalformedChallengeException("Header value is null");
            }

            var7 = new CharArrayBuffer(var9.length());
            var7.append(var9);
            var8 = 0;
         }

         while(var8 < var7.length() && HTTP.isWhitespace(var7.charAt(var8))) {
            ++var8;
         }

         int var12;
         for(var12 = var8; var8 < var7.length() && !HTTP.isWhitespace(var7.charAt(var8)); ++var8) {
         }

         String var11 = var7.substring(var12, var8);
         var2.put(var11.toLowerCase(Locale.US), var6);
      }

      return var2;
   }

   protected List getAuthPreferences() {
      return DEFAULT_SCHEME_PRIORITY;
   }

   protected List getAuthPreferences(HttpResponse var1, HttpContext var2) {
      return this.getAuthPreferences();
   }

   public AuthScheme selectScheme(Map var1, HttpResponse var2, HttpContext var3) throws AuthenticationException {
      AuthSchemeRegistry var4 = (AuthSchemeRegistry)var3.getAttribute("http.authscheme-registry");
      Asserts.notNull(var4, "AuthScheme registry");
      List var5 = this.getAuthPreferences(var2, var3);
      if (var5 == null) {
         var5 = DEFAULT_SCHEME_PRIORITY;
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug("Authentication schemes in the order of preference: " + var5);
      }

      AuthScheme var6 = null;
      Iterator var7 = var5.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         Header var9 = (Header)var1.get(var8.toLowerCase(Locale.ENGLISH));
         if (var9 != null) {
            if (this.log.isDebugEnabled()) {
               this.log.debug(var8 + " authentication scheme selected");
            }

            try {
               var6 = var4.getAuthScheme(var8, var2.getParams());
               break;
            } catch (IllegalStateException var11) {
               if (this.log.isWarnEnabled()) {
                  this.log.warn("Authentication scheme " + var8 + " not supported");
               }
            }
         } else if (this.log.isDebugEnabled()) {
            this.log.debug("Challenge for " + var8 + " authentication scheme not available");
         }
      }

      if (var6 == null) {
         throw new AuthenticationException("Unable to respond to any of these challenges: " + var1);
      } else {
         return var6;
      }
   }
}
