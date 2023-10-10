package org.apache.http.impl.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

@NotThreadSafe
public abstract class GGSSchemeBase extends AuthSchemeBase {
   private final Log log;
   private final Base64 base64codec;
   private final boolean stripPort;
   private GGSSchemeBase.State state;
   private byte[] token;

   GGSSchemeBase(boolean var1) {
      this.log = LogFactory.getLog(this.getClass());
      this.base64codec = new Base64(0);
      this.stripPort = var1;
      this.state = GGSSchemeBase.State.UNINITIATED;
   }

   GGSSchemeBase() {
      this(false);
   }

   protected GSSManager getManager() {
      return GSSManager.getInstance();
   }

   protected byte[] generateGSSToken(byte[] var1, Oid var2, String var3) throws GSSException {
      byte[] var4 = var1;
      if (var1 == null) {
         var4 = new byte[0];
      }

      GSSManager var5 = this.getManager();
      GSSName var6 = var5.createName("HTTP@" + var3, GSSName.NT_HOSTBASED_SERVICE);
      GSSContext var7 = var5.createContext(var6.canonicalize(var2), var2, (GSSCredential)null, 0);
      var7.requestMutualAuth(true);
      var7.requestCredDeleg(true);
      return var7.initSecContext(var4, 0, var4.length);
   }

   protected abstract byte[] generateToken(byte[] var1, String var2) throws GSSException;

   public boolean isComplete() {
      return this.state == GGSSchemeBase.State.TOKEN_GENERATED || this.state == GGSSchemeBase.State.FAILED;
   }

   /** @deprecated */
   @Deprecated
   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, (HttpContext)null);
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      Args.notNull(var2, "HTTP request");
      switch(this.state) {
      case UNINITIATED:
         throw new AuthenticationException(this.getSchemeName() + " authentication has not been initiated");
      case FAILED:
         throw new AuthenticationException(this.getSchemeName() + " authentication has failed");
      case CHALLENGE_RECEIVED:
         try {
            HttpRoute var4 = (HttpRoute)var3.getAttribute("http.route");
            if (var4 == null) {
               throw new AuthenticationException("Connection route is not available");
            } else {
               HttpHost var5;
               if (this.isProxy()) {
                  var5 = var4.getProxyHost();
                  if (var5 == null) {
                     var5 = var4.getTargetHost();
                  }
               } else {
                  var5 = var4.getTargetHost();
               }

               String var6;
               if (!this.stripPort && var5.getPort() > 0) {
                  var6 = var5.toHostString();
               } else {
                  var6 = var5.getHostName();
               }

               if (this.log.isDebugEnabled()) {
                  this.log.debug("init " + var6);
               }

               this.token = this.generateToken(this.token, var6);
               this.state = GGSSchemeBase.State.TOKEN_GENERATED;
            }
         } catch (GSSException var7) {
            this.state = GGSSchemeBase.State.FAILED;
            if (var7.getMajor() != 9 && var7.getMajor() != 8) {
               if (var7.getMajor() == 13) {
                  throw new InvalidCredentialsException(var7.getMessage(), var7);
               }

               if (var7.getMajor() != 10 && var7.getMajor() != 19 && var7.getMajor() != 20) {
                  throw new AuthenticationException(var7.getMessage());
               }

               throw new AuthenticationException(var7.getMessage(), var7);
            }

            throw new InvalidCredentialsException(var7.getMessage(), var7);
         }
      case TOKEN_GENERATED:
         String var8 = new String(this.base64codec.encode(this.token));
         if (this.log.isDebugEnabled()) {
            this.log.debug("Sending response '" + var8 + "' back to the auth server");
         }

         CharArrayBuffer var9 = new CharArrayBuffer(32);
         if (this.isProxy()) {
            var9.append("Proxy-Authorization");
         } else {
            var9.append("Authorization");
         }

         var9.append(": Negotiate ");
         var9.append(var8);
         return new BufferedHeader(var9);
      default:
         throw new IllegalStateException("Illegal state: " + this.state);
      }
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      String var4 = var1.substringTrimmed(var2, var3);
      if (this.log.isDebugEnabled()) {
         this.log.debug("Received challenge '" + var4 + "' from the auth server");
      }

      if (this.state == GGSSchemeBase.State.UNINITIATED) {
         this.token = Base64.decodeBase64(var4.getBytes());
         this.state = GGSSchemeBase.State.CHALLENGE_RECEIVED;
      } else {
         this.log.debug("Authentication already attempted");
         this.state = GGSSchemeBase.State.FAILED;
      }

   }

   static enum State {
      UNINITIATED,
      CHALLENGE_RECEIVED,
      TOKEN_GENERATED,
      FAILED;

      private static final GGSSchemeBase.State[] $VALUES = new GGSSchemeBase.State[]{UNINITIATED, CHALLENGE_RECEIVED, TOKEN_GENERATED, FAILED};
   }
}
