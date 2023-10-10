package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AuthSchemeBase implements ContextAwareAuthScheme {
   private ChallengeState challengeState;

   /** @deprecated */
   @Deprecated
   public AuthSchemeBase(ChallengeState var1) {
      this.challengeState = var1;
   }

   public AuthSchemeBase() {
   }

   public void processChallenge(Header var1) throws MalformedChallengeException {
      Args.notNull(var1, "Header");
      String var2 = var1.getName();
      if (var2.equalsIgnoreCase("WWW-Authenticate")) {
         this.challengeState = ChallengeState.TARGET;
      } else {
         if (!var2.equalsIgnoreCase("Proxy-Authenticate")) {
            throw new MalformedChallengeException("Unexpected header name: " + var2);
         }

         this.challengeState = ChallengeState.PROXY;
      }

      CharArrayBuffer var3;
      int var4;
      if (var1 instanceof FormattedHeader) {
         var3 = ((FormattedHeader)var1).getBuffer();
         var4 = ((FormattedHeader)var1).getValuePos();
      } else {
         String var5 = var1.getValue();
         if (var5 == null) {
            throw new MalformedChallengeException("Header value is null");
         }

         var3 = new CharArrayBuffer(var5.length());
         var3.append(var5);
         var4 = 0;
      }

      while(var4 < var3.length() && HTTP.isWhitespace(var3.charAt(var4))) {
         ++var4;
      }

      int var8;
      for(var8 = var4; var4 < var3.length() && !HTTP.isWhitespace(var3.charAt(var4)); ++var4) {
      }

      String var7 = var3.substring(var8, var4);
      if (!var7.equalsIgnoreCase(this.getSchemeName())) {
         throw new MalformedChallengeException("Invalid scheme identifier: " + var7);
      } else {
         this.parseChallenge(var3, var4, var3.length());
      }
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      return this.authenticate(var1, var2);
   }

   protected abstract void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException;

   public boolean isProxy() {
      return this.challengeState != null && this.challengeState == ChallengeState.PROXY;
   }

   public ChallengeState getChallengeState() {
      return this.challengeState;
   }

   public String toString() {
      String var1 = this.getSchemeName();
      return var1 != null ? var1.toUpperCase(Locale.US) : super.toString();
   }
}
