/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import java.util.Date;
import java.util.List;
import org.apache.http.cookie.Cookie;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface CookieStore {
    public void addCookie(Cookie var1);

    public List<Cookie> getCookies();

    public boolean clearExpired(Date var1);

    public void clear();
}

