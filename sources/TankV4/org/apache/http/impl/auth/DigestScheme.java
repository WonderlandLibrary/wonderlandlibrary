package org.apache.http.impl.auth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
public class DigestScheme extends RFC2617Scheme {
   private static final char[] HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private boolean complete;
   private static final int QOP_UNKNOWN = -1;
   private static final int QOP_MISSING = 0;
   private static final int QOP_AUTH_INT = 1;
   private static final int QOP_AUTH = 2;
   private String lastNonce;
   private long nounceCount;
   private String cnonce;
   private String a1;
   private String a2;

   public DigestScheme(Charset var1) {
      super(var1);
      this.complete = false;
   }

   /** @deprecated */
   @Deprecated
   public DigestScheme(ChallengeState var1) {
      super(var1);
   }

   public DigestScheme() {
      this(Consts.ASCII);
   }

   public void processChallenge(Header var1) throws MalformedChallengeException {
      super.processChallenge(var1);
      this.complete = true;
   }

   public boolean isComplete() {
      String var1 = this.getParameter("stale");
      return "true".equalsIgnoreCase(var1) ? false : this.complete;
   }

   public String getSchemeName() {
      return "digest";
   }

   public boolean isConnectionBased() {
      return false;
   }

   public void overrideParamter(String var1, String var2) {
      this.getParameters().put(var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, new BasicHttpContext());
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      Args.notNull(var1, "Credentials");
      Args.notNull(var2, "HTTP request");
      if (this.getParameter("realm") == null) {
         throw new AuthenticationException("missing realm in challenge");
      } else if (this.getParameter("nonce") == null) {
         throw new AuthenticationException("missing nonce in challenge");
      } else {
         this.getParameters().put("methodname", var2.getRequestLine().getMethod());
         this.getParameters().put("uri", var2.getRequestLine().getUri());
         String var4 = this.getParameter("charset");
         if (var4 == null) {
            this.getParameters().put("charset", this.getCredentialsCharset(var2));
         }

         return this.createDigestHeader(var1, var2);
      }
   }

   private static MessageDigest createMessageDigest(String var0) throws UnsupportedDigestAlgorithmException {
      try {
         return MessageDigest.getInstance(var0);
      } catch (Exception var2) {
         throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + var0);
      }
   }

   private Header createDigestHeader(Credentials var1, HttpRequest var2) throws AuthenticationException {
      String var3 = this.getParameter("uri");
      String var4 = this.getParameter("realm");
      String var5 = this.getParameter("nonce");
      String var6 = this.getParameter("opaque");
      String var7 = this.getParameter("methodname");
      String var8 = this.getParameter("algorithm");
      if (var8 == null) {
         var8 = "MD5";
      }

      HashSet var9 = new HashSet(8);
      byte var10 = -1;
      String var11 = this.getParameter("qop");
      String var13;
      if (var11 != null) {
         StringTokenizer var12 = new StringTokenizer(var11, ",");

         while(var12.hasMoreTokens()) {
            var13 = var12.nextToken().trim();
            var9.add(var13.toLowerCase(Locale.US));
         }

         if (var2 instanceof HttpEntityEnclosingRequest && var9.contains("auth-int")) {
            var10 = 1;
         } else if (var9.contains("auth")) {
            var10 = 2;
         }
      } else {
         var10 = 0;
      }

      if (var10 == -1) {
         throw new AuthenticationException("None of the qop methods is supported: " + var11);
      } else {
         String var32 = this.getParameter("charset");
         if (var32 == null) {
            var32 = "ISO-8859-1";
         }

         var13 = var8;
         if (var8.equalsIgnoreCase("MD5-sess")) {
            var13 = "MD5";
         }

         MessageDigest var14;
         try {
            var14 = createMessageDigest(var13);
         } catch (UnsupportedDigestAlgorithmException var30) {
            throw new AuthenticationException("Unsuppported digest algorithm: " + var13);
         }

         String var15 = var1.getUserPrincipal().getName();
         String var16 = var1.getPassword();
         if (var5.equals(this.lastNonce)) {
            ++this.nounceCount;
         } else {
            this.nounceCount = 1L;
            this.cnonce = null;
            this.lastNonce = var5;
         }

         StringBuilder var17 = new StringBuilder(256);
         Formatter var18 = new Formatter(var17, Locale.US);
         var18.format("%08x", this.nounceCount);
         var18.close();
         String var19 = var17.toString();
         if (this.cnonce == null) {
            this.cnonce = createCnonce();
         }

         this.a1 = null;
         this.a2 = null;
         String var20;
         if (var8.equalsIgnoreCase("MD5-sess")) {
            var17.setLength(0);
            var17.append(var15).append(':').append(var4).append(':').append(var16);
            var20 = encode(var14.digest(EncodingUtils.getBytes(var17.toString(), var32)));
            var17.setLength(0);
            var17.append(var20).append(':').append(var5).append(':').append(this.cnonce);
            this.a1 = var17.toString();
         } else {
            var17.setLength(0);
            var17.append(var15).append(':').append(var4).append(':').append(var16);
            this.a1 = var17.toString();
         }

         var20 = encode(var14.digest(EncodingUtils.getBytes(this.a1, var32)));
         if (var10 == 2) {
            this.a2 = var7 + ':' + var3;
         } else if (var10 == 1) {
            HttpEntity var21 = null;
            if (var2 instanceof HttpEntityEnclosingRequest) {
               var21 = ((HttpEntityEnclosingRequest)var2).getEntity();
            }

            if (var21 != null && !var21.isRepeatable()) {
               if (!var9.contains("auth")) {
                  throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
               }

               var10 = 2;
               this.a2 = var7 + ':' + var3;
            } else {
               HttpEntityDigester var22 = new HttpEntityDigester(var14);

               try {
                  if (var21 != null) {
                     var21.writeTo(var22);
                  }

                  var22.close();
               } catch (IOException var31) {
                  throw new AuthenticationException("I/O error reading entity content", var31);
               }

               this.a2 = var7 + ':' + var3 + ':' + encode(var22.getDigest());
            }
         } else {
            this.a2 = var7 + ':' + var3;
         }

         String var33 = encode(var14.digest(EncodingUtils.getBytes(this.a2, var32)));
         String var34;
         if (var10 == 0) {
            var17.setLength(0);
            var17.append(var20).append(':').append(var5).append(':').append(var33);
            var34 = var17.toString();
         } else {
            var17.setLength(0);
            var17.append(var20).append(':').append(var5).append(':').append(var19).append(':').append(this.cnonce).append(':').append(var10 == 1 ? "auth-int" : "auth").append(':').append(var33);
            var34 = var17.toString();
         }

         String var23 = encode(var14.digest(EncodingUtils.getAsciiBytes(var34)));
         CharArrayBuffer var24 = new CharArrayBuffer(128);
         if (this.isProxy()) {
            var24.append("Proxy-Authorization");
         } else {
            var24.append("Authorization");
         }

         var24.append(": Digest ");
         ArrayList var25 = new ArrayList(20);
         var25.add(new BasicNameValuePair("username", var15));
         var25.add(new BasicNameValuePair("realm", var4));
         var25.add(new BasicNameValuePair("nonce", var5));
         var25.add(new BasicNameValuePair("uri", var3));
         var25.add(new BasicNameValuePair("response", var23));
         if (var10 != 0) {
            var25.add(new BasicNameValuePair("qop", var10 == 1 ? "auth-int" : "auth"));
            var25.add(new BasicNameValuePair("nc", var19));
            var25.add(new BasicNameValuePair("cnonce", this.cnonce));
         }

         var25.add(new BasicNameValuePair("algorithm", var8));
         if (var6 != null) {
            var25.add(new BasicNameValuePair("opaque", var6));
         }

         for(int var26 = 0; var26 < var25.size(); ++var26) {
            BasicNameValuePair var27 = (BasicNameValuePair)var25.get(var26);
            if (var26 > 0) {
               var24.append(", ");
            }

            String var28 = var27.getName();
            boolean var29 = "nc".equals(var28) || "qop".equals(var28) || "algorithm".equals(var28);
            BasicHeaderValueFormatter.INSTANCE.formatNameValuePair(var24, var27, !var29);
         }

         return new BufferedHeader(var24);
      }
   }

   String getCnonce() {
      return this.cnonce;
   }

   String getA1() {
      return this.a1;
   }

   String getA2() {
      return this.a2;
   }

   static String encode(byte[] var0) {
      int var1 = var0.length;
      char[] var2 = new char[var1 * 2];

      for(int var3 = 0; var3 < var1; ++var3) {
         int var4 = var0[var3] & 15;
         int var5 = (var0[var3] & 240) >> 4;
         var2[var3 * 2] = HEXADECIMAL[var5];
         var2[var3 * 2 + 1] = HEXADECIMAL[var4];
      }

      return new String(var2);
   }

   public static String createCnonce() {
      SecureRandom var0 = new SecureRandom();
      byte[] var1 = new byte[8];
      var0.nextBytes(var1);
      return encode(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DIGEST [complete=").append(this.complete).append(", nonce=").append(this.lastNonce).append(", nc=").append(this.nounceCount).append("]");
      return var1.toString();
   }
}
