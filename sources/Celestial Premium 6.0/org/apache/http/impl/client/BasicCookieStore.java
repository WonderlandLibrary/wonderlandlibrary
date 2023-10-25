/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@ThreadSafe
public class BasicCookieStore
implements CookieStore,
Serializable {
    private static final long serialVersionUID = -7581093305228232025L;
    @GuardedBy(value="this")
    private final TreeSet<Cookie> cookies = new TreeSet<Cookie>(new CookieIdentityComparator());

    @Override
    public synchronized void addCookie(Cookie cookie) {
        if (cookie != null) {
            this.cookies.remove(cookie);
            if (!cookie.isExpired(new Date())) {
                this.cookies.add(cookie);
            }
        }
    }

    public synchronized void addCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                this.addCookie(cooky);
            }
        }
    }

    @Override
    public synchronized List<Cookie> getCookies() {
        return new ArrayList<Cookie>(this.cookies);
    }

    @Override
    public synchronized boolean clearExpired(Date date) {
        if (date == null) {
            return false;
        }
        boolean removed = false;
        Iterator<Cookie> it = this.cookies.iterator();
        while (it.hasNext()) {
            if (!it.next().isExpired(date)) continue;
            it.remove();
            removed = true;
        }
        return removed;
    }

    @Override
    public synchronized void clear() {
        this.cookies.clear();
    }

    public synchronized String toString() {
        return this.cookies.toString();
    }
}

