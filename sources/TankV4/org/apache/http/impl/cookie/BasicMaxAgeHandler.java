package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicMaxAgeHandler extends AbstractCookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for max-age attribute");
      } else {
         int var3;
         try {
            var3 = Integer.parseInt(var2);
         } catch (NumberFormatException var5) {
            throw new MalformedCookieException("Invalid max-age attribute: " + var2);
         }

         if (var3 < 0) {
            throw new MalformedCookieException("Negative max-age attribute: " + var2);
         } else {
            var1.setExpiryDate(new Date(System.currentTimeMillis() + (long)var3 * 1000L));
         }
      }
   }
}
