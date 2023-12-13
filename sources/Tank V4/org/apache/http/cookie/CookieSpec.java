package org.apache.http.cookie;

import java.util.List;
import org.apache.http.Header;

public interface CookieSpec {
   int getVersion();

   List parse(Header var1, CookieOrigin var2) throws MalformedCookieException;

   void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException;

   boolean match(Cookie var1, CookieOrigin var2);

   List formatCookies(List var1);

   Header getVersionHeader();
}
