package org.apache.http.impl.cookie;

import java.util.Collections;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

@NotThreadSafe
public class IgnoreSpec extends CookieSpecBase {
   public int getVersion() {
      return 0;
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      return Collections.emptyList();
   }

   public List formatCookies(List var1) {
      return Collections.emptyList();
   }

   public Header getVersionHeader() {
      return null;
   }
}
