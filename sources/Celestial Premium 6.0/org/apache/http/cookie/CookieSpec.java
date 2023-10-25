/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.cookie;

import java.util.List;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface CookieSpec {
    public int getVersion();

    public List<Cookie> parse(Header var1, CookieOrigin var2) throws MalformedCookieException;

    public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException;

    public boolean match(Cookie var1, CookieOrigin var2);

    public List<Header> formatCookies(List<Cookie> var1);

    public Header getVersionHeader();
}

