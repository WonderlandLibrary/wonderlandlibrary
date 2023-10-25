/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import java.util.Map;
import java.util.Queue;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface AuthenticationStrategy {
    public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3);

    public Map<String, Header> getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException;

    public Queue<AuthOption> select(Map<String, Header> var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException;

    public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3);

    public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3);
}

