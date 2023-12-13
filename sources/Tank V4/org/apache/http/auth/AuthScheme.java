package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

public interface AuthScheme {
   void processChallenge(Header var1) throws MalformedChallengeException;

   String getSchemeName();

   String getParameter(String var1);

   String getRealm();

   boolean isConnectionBased();

   boolean isComplete();

   /** @deprecated */
   @Deprecated
   Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException;
}
