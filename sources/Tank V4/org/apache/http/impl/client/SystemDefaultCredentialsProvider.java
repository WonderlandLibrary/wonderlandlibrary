package org.apache.http.impl.client;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.Authenticator.RequestorType;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@ThreadSafe
public class SystemDefaultCredentialsProvider implements CredentialsProvider {
   private static final Map SCHEME_MAP = new ConcurrentHashMap();
   private final BasicCredentialsProvider internal = new BasicCredentialsProvider();

   private static String translateScheme(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = (String)SCHEME_MAP.get(var0);
         return var1 != null ? var1 : var0;
      }
   }

   public void setCredentials(AuthScope var1, Credentials var2) {
      this.internal.setCredentials(var1, var2);
   }

   private static PasswordAuthentication getSystemCreds(AuthScope var0, RequestorType var1) {
      String var2 = var0.getHost();
      int var3 = var0.getPort();
      String var4 = var3 == 443 ? "https" : "http";
      return Authenticator.requestPasswordAuthentication(var2, (InetAddress)null, var3, var4, (String)null, translateScheme(var0.getScheme()), (URL)null, var1);
   }

   public Credentials getCredentials(AuthScope var1) {
      Args.notNull(var1, "Auth scope");
      Credentials var2 = this.internal.getCredentials(var1);
      if (var2 != null) {
         return var2;
      } else {
         if (var1.getHost() != null) {
            PasswordAuthentication var3 = getSystemCreds(var1, RequestorType.SERVER);
            if (var3 == null) {
               var3 = getSystemCreds(var1, RequestorType.PROXY);
            }

            if (var3 != null) {
               String var4 = System.getProperty("http.auth.ntlm.domain");
               if (var4 != null) {
                  return new NTCredentials(var3.getUserName(), new String(var3.getPassword()), (String)null, var4);
               }

               if ("NTLM".equalsIgnoreCase(var1.getScheme())) {
                  return new NTCredentials(var3.getUserName(), new String(var3.getPassword()), (String)null, (String)null);
               }

               return new UsernamePasswordCredentials(var3.getUserName(), new String(var3.getPassword()));
            }
         }

         return null;
      }
   }

   public void clear() {
      this.internal.clear();
   }

   static {
      SCHEME_MAP.put("Basic".toUpperCase(Locale.ENGLISH), "Basic");
      SCHEME_MAP.put("Digest".toUpperCase(Locale.ENGLISH), "Digest");
      SCHEME_MAP.put("NTLM".toUpperCase(Locale.ENGLISH), "NTLM");
      SCHEME_MAP.put("negotiate".toUpperCase(Locale.ENGLISH), "SPNEGO");
      SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ENGLISH), "Kerberos");
   }
}
