package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class RFC2965DomainAttributeHandler implements CookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for domain attribute");
      } else if (var2.trim().length() == 0) {
         throw new MalformedCookieException("Blank value for domain attribute");
      } else {
         String var3 = var2.toLowerCase(Locale.ENGLISH);
         if (!var2.startsWith(".")) {
            var3 = '.' + var3;
         }

         var1.setDomain(var3);
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var3 = var2.getHost().toLowerCase(Locale.ENGLISH);
      if (var1.getDomain() == null) {
         throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
      } else {
         String var4 = var1.getDomain().toLowerCase(Locale.ENGLISH);
         if (var1 instanceof ClientCookie && ((ClientCookie)var1).containsAttribute("domain")) {
            if (!var4.startsWith(".")) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var1.getDomain() + "\" violates RFC 2109: domain must start with a dot");
            }

            int var5 = var4.indexOf(46, 1);
            if ((var5 < 0 || var5 == var4.length() - 1) && !var4.equals(".local")) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var1.getDomain() + "\" violates RFC 2965: the value contains no embedded dots " + "and the value is not .local");
            }

            if (var4 == false) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var1.getDomain() + "\" violates RFC 2965: effective host name does not " + "domain-match domain attribute.");
            }

            String var6 = var3.substring(0, var3.length() - var4.length());
            if (var6.indexOf(46) != -1) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var1.getDomain() + "\" violates RFC 2965: " + "effective host minus domain may not contain any dots");
            }
         } else if (!var1.getDomain().equals(var3)) {
            throw new CookieRestrictionViolationException("Illegal domain attribute: \"" + var1.getDomain() + "\"." + "Domain of origin: \"" + var3 + "\"");
         }

      }
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var3 = var2.getHost().toLowerCase(Locale.ENGLISH);
      String var4 = var1.getDomain();
      if (var4 == false) {
         return false;
      } else {
         String var5 = var3.substring(0, var3.length() - var4.length());
         return var5.indexOf(46) == -1;
      }
   }
}
