/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.client.utils.Punycode;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class PublicSuffixFilter
implements CookieAttributeHandler {
    private final CookieAttributeHandler wrapped;
    private Set<String> exceptions;
    private Set<String> suffixes;

    public PublicSuffixFilter(CookieAttributeHandler wrapped) {
        this.wrapped = wrapped;
    }

    public void setPublicSuffixes(Collection<String> suffixes) {
        this.suffixes = new HashSet<String>(suffixes);
    }

    public void setExceptions(Collection<String> exceptions) {
        this.exceptions = new HashSet<String>(exceptions);
    }

    @Override
    public boolean match(Cookie cookie, CookieOrigin origin) {
        if (this.isForPublicSuffix(cookie)) {
            return false;
        }
        return this.wrapped.match(cookie, origin);
    }

    @Override
    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        this.wrapped.parse(cookie, value);
    }

    @Override
    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        this.wrapped.validate(cookie, origin);
    }

    private boolean isForPublicSuffix(Cookie cookie) {
        int nextdot;
        String domain = cookie.getDomain();
        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        domain = Punycode.toUnicode(domain);
        if (this.exceptions != null && this.exceptions.contains(domain)) {
            return false;
        }
        if (this.suffixes == null) {
            return false;
        }
        do {
            if (this.suffixes.contains(domain)) {
                return true;
            }
            if (!domain.startsWith("*.")) continue;
            domain = domain.substring(2);
        } while ((nextdot = domain.indexOf(46)) != -1 && (domain = "*" + domain.substring(nextdot)).length() > 0);
        return false;
    }
}

