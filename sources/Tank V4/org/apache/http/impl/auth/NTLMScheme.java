package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class NTLMScheme extends AuthSchemeBase {
   private final NTLMEngine engine;
   private NTLMScheme.State state;
   private String challenge;

   public NTLMScheme(NTLMEngine var1) {
      Args.notNull(var1, "NTLM engine");
      this.engine = var1;
      this.state = NTLMScheme.State.UNINITIATED;
      this.challenge = null;
   }

   public NTLMScheme() {
      this(new NTLMEngineImpl());
   }

   public String getSchemeName() {
      return "ntlm";
   }

   public String getParameter(String var1) {
      return null;
   }

   public String getRealm() {
      return null;
   }

   public boolean isConnectionBased() {
      return true;
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      this.challenge = var1.substringTrimmed(var2, var3);
      if (this.challenge.length() == 0) {
         if (this.state == NTLMScheme.State.UNINITIATED) {
            this.state = NTLMScheme.State.CHALLENGE_RECEIVED;
         } else {
            this.state = NTLMScheme.State.FAILED;
         }
      } else {
         if (this.state.compareTo(NTLMScheme.State.MSG_TYPE1_GENERATED) < 0) {
            this.state = NTLMScheme.State.FAILED;
            throw new MalformedChallengeException("Out of sequence NTLM response message");
         }

         if (this.state == NTLMScheme.State.MSG_TYPE1_GENERATED) {
            this.state = NTLMScheme.State.MSG_TYPE2_RECEVIED;
         }
      }

   }

   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      NTCredentials var3 = null;

      try {
         var3 = (NTCredentials)var1;
      } catch (ClassCastException var6) {
         throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + var1.getClass().getName());
      }

      String var4 = null;
      if (this.state == NTLMScheme.State.FAILED) {
         throw new AuthenticationException("NTLM authentication failed");
      } else {
         if (this.state == NTLMScheme.State.CHALLENGE_RECEIVED) {
            var4 = this.engine.generateType1Msg(var3.getDomain(), var3.getWorkstation());
            this.state = NTLMScheme.State.MSG_TYPE1_GENERATED;
         } else {
            if (this.state != NTLMScheme.State.MSG_TYPE2_RECEVIED) {
               throw new AuthenticationException("Unexpected state: " + this.state);
            }

            var4 = this.engine.generateType3Msg(var3.getUserName(), var3.getPassword(), var3.getDomain(), var3.getWorkstation(), this.challenge);
            this.state = NTLMScheme.State.MSG_TYPE3_GENERATED;
         }

         CharArrayBuffer var5 = new CharArrayBuffer(32);
         if (this.isProxy()) {
            var5.append("Proxy-Authorization");
         } else {
            var5.append("Authorization");
         }

         var5.append(": NTLM ");
         var5.append(var4);
         return new BufferedHeader(var5);
      }
   }

   public boolean isComplete() {
      return this.state == NTLMScheme.State.MSG_TYPE3_GENERATED || this.state == NTLMScheme.State.FAILED;
   }

   static enum State {
      UNINITIATED,
      CHALLENGE_RECEIVED,
      MSG_TYPE1_GENERATED,
      MSG_TYPE2_RECEVIED,
      MSG_TYPE3_GENERATED,
      FAILED;

      private static final NTLMScheme.State[] $VALUES = new NTLMScheme.State[]{UNINITIATED, CHALLENGE_RECEIVED, MSG_TYPE1_GENERATED, MSG_TYPE2_RECEVIED, MSG_TYPE3_GENERATED, FAILED};
   }
}
