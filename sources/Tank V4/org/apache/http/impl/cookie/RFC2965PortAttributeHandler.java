package org.apache.http.impl.cookie;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.util.Args;

@Immutable
public class RFC2965PortAttributeHandler implements CookieAttributeHandler {
   private static int[] parsePortAttribute(String var0) throws MalformedCookieException {
      StringTokenizer var1 = new StringTokenizer(var0, ",");
      int[] var2 = new int[var1.countTokens()];

      try {
         for(int var3 = 0; var1.hasMoreTokens(); ++var3) {
            var2[var3] = Integer.parseInt(var1.nextToken().trim());
            if (var2[var3] < 0) {
               throw new MalformedCookieException("Invalid Port attribute.");
            }
         }

         return var2;
      } catch (NumberFormatException var4) {
         throw new MalformedCookieException("Invalid Port attribute: " + var4.getMessage());
      }
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var1 instanceof SetCookie2) {
         SetCookie2 var3 = (SetCookie2)var1;
         if (var2 != null && var2.trim().length() > 0) {
            int[] var4 = parsePortAttribute(var2);
            var3.setPorts(var4);
         }
      }

   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      int var3 = var2.getPort();
      if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("port") && var3 < var1.getPorts()) {
         throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
      }
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      int var3 = var2.getPort();
      if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("port")) {
         if (var1.getPorts() == null) {
            return false;
         }

         if (var3 < var1.getPorts()) {
            return false;
         }
      }

      return true;
   }
}
