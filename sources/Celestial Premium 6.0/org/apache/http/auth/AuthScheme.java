/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;

public interface AuthScheme {
    public void processChallenge(Header var1) throws MalformedChallengeException;

    public String getSchemeName();

    public String getParameter(String var1);

    public String getRealm();

    public boolean isConnectionBased();

    public boolean isComplete();

    @Deprecated
    public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException;
}

