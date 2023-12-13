package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicExpiresHandler extends AbstractCookieAttributeHandler {
   private final String[] datepatterns;

   public BasicExpiresHandler(String[] var1) {
      Args.notNull(var1, "Array of date patterns");
      this.datepatterns = var1;
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for expires attribute");
      } else {
         Date var3 = org.apache.http.client.utils.DateUtils.parseDate(var2, this.datepatterns);
         if (var3 == null) {
            throw new MalformedCookieException("Unable to parse expires attribute: " + var2);
         } else {
            var1.setExpiryDate(var3);
         }
      }
   }
}
