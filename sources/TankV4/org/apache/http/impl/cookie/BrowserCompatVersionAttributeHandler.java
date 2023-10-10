package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BrowserCompatVersionAttributeHandler extends AbstractCookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for version attribute");
      } else {
         int var3 = 0;

         try {
            var3 = Integer.parseInt(var2);
         } catch (NumberFormatException var5) {
         }

         var1.setVersion(var3);
      }
   }
}
