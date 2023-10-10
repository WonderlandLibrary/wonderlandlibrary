package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class RFC2617Scheme extends AuthSchemeBase {
   private final Map params;
   private final Charset credentialsCharset;

   /** @deprecated */
   @Deprecated
   public RFC2617Scheme(ChallengeState var1) {
      super(var1);
      this.params = new HashMap();
      this.credentialsCharset = Consts.ASCII;
   }

   public RFC2617Scheme(Charset var1) {
      this.params = new HashMap();
      this.credentialsCharset = var1 != null ? var1 : Consts.ASCII;
   }

   public RFC2617Scheme() {
      this(Consts.ASCII);
   }

   public Charset getCredentialsCharset() {
      return this.credentialsCharset;
   }

   String getCredentialsCharset(HttpRequest var1) {
      String var2 = (String)var1.getParams().getParameter("http.auth.credential-charset");
      if (var2 == null) {
         var2 = this.getCredentialsCharset().name();
      }

      return var2;
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      BasicHeaderValueParser var4 = BasicHeaderValueParser.INSTANCE;
      ParserCursor var5 = new ParserCursor(var2, var1.length());
      HeaderElement[] var6 = var4.parseElements(var1, var5);
      if (var6.length == 0) {
         throw new MalformedChallengeException("Authentication challenge is empty");
      } else {
         this.params.clear();
         HeaderElement[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            HeaderElement var10 = var7[var9];
            this.params.put(var10.getName(), var10.getValue());
         }

      }
   }

   protected Map getParameters() {
      return this.params;
   }

   public String getParameter(String var1) {
      return var1 == null ? null : (String)this.params.get(var1.toLowerCase(Locale.ENGLISH));
   }

   public String getRealm() {
      return this.getParameter("realm");
   }
}
