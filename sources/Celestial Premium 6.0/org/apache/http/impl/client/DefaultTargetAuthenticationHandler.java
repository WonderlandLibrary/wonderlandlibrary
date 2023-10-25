/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.client;

import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.impl.client.AbstractAuthenticationHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
@Immutable
public class DefaultTargetAuthenticationHandler
extends AbstractAuthenticationHandler {
    @Override
    public boolean isAuthenticationRequested(HttpResponse response, HttpContext context) {
        Args.notNull(response, "HTTP response");
        int status = response.getStatusLine().getStatusCode();
        return status == 401;
    }

    @Override
    public Map<String, Header> getChallenges(HttpResponse response, HttpContext context) throws MalformedChallengeException {
        Args.notNull(response, "HTTP response");
        Header[] headers = response.getHeaders("WWW-Authenticate");
        return this.parseChallenges(headers);
    }

    @Override
    protected List<String> getAuthPreferences(HttpResponse response, HttpContext context) {
        List authpref = (List)response.getParams().getParameter("http.auth.target-scheme-pref");
        if (authpref != null) {
            return authpref;
        }
        return super.getAuthPreferences(response, context);
    }
}

