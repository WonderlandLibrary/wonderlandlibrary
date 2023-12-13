package org.apache.http.conn.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.InetAddressUtils;

@Immutable
public abstract class AbstractVerifier implements X509HostnameVerifier {
   private static final String[] BAD_COUNTRY_2LDS = new String[]{"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};
   private final Log log = LogFactory.getLog(this.getClass());

   public final void verify(String var1, SSLSocket var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("host to verify is null");
      } else {
         SSLSession var3 = var2.getSession();
         if (var3 == null) {
            InputStream var4 = var2.getInputStream();
            var4.available();
            var3 = var2.getSession();
            if (var3 == null) {
               var2.startHandshake();
               var3 = var2.getSession();
            }
         }

         Certificate[] var6 = var3.getPeerCertificates();
         X509Certificate var5 = (X509Certificate)var6[0];
         this.verify(var1, var5);
      }
   }

   public final boolean verify(String var1, SSLSession var2) {
      try {
         Certificate[] var3 = var2.getPeerCertificates();
         X509Certificate var4 = (X509Certificate)var3[0];
         this.verify(var1, var4);
         return true;
      } catch (SSLException var5) {
         return false;
      }
   }

   public final void verify(String var1, X509Certificate var2) throws SSLException {
      String[] var3 = getCNs(var2);
      String[] var4 = getSubjectAlts(var2, var1);
      this.verify(var1, var3, var4);
   }

   public final void verify(String var1, String[] var2, String[] var3, boolean var4) throws SSLException {
      LinkedList var5 = new LinkedList();
      if (var2 != null && var2.length > 0 && var2[0] != null) {
         var5.add(var2[0]);
      }

      if (var3 != null) {
         String[] var6 = var3;
         int var7 = var3.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (var9 != null) {
               var5.add(var9);
            }
         }
      }

      if (var5.isEmpty()) {
         String var18 = "Certificate for <" + var1 + "> doesn't contain CN or DNS subjectAlt";
         throw new SSLException(var18);
      } else {
         StringBuilder var17 = new StringBuilder();
         String var19 = this.normaliseIPv6Address(var1.trim().toLowerCase(Locale.US));
         boolean var20 = false;
         Iterator var21 = var5.iterator();

         while(var21.hasNext()) {
            String var10 = (String)var21.next();
            var10 = var10.toLowerCase(Locale.US);
            var17.append(" <");
            var17.append(var10);
            var17.append('>');
            if (var21.hasNext()) {
               var17.append(" OR");
            }

            String[] var11 = var10.split("\\.");
            boolean var12 = var11.length >= 3 && var11[0].endsWith("*") && this == var10 && var1 != null;
            if (var12) {
               String var13 = var11[0];
               if (var13.length() <= 1) {
                  var20 = var19.endsWith(var10.substring(1));
               } else {
                  String var14 = var13.substring(0, var13.length() - 1);
                  String var15 = var10.substring(var13.length());
                  String var16 = var19.substring(var14.length());
                  var20 = var19.startsWith(var14) && var16.endsWith(var15);
               }

               if (var20 && var4) {
                  var20 = countDots(var19) == countDots(var10);
               }
            } else {
               var20 = var19.equals(this.normaliseIPv6Address(var10));
            }

            if (var20) {
               break;
            }
         }

         if (!var20) {
            throw new SSLException("hostname in certificate didn't match: <" + var1 + "> !=" + var17);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean acceptableCountryWildcard(String var0) {
      String[] var1 = var0.split("\\.");
      if (var1.length == 3 && var1[2].length() == 2) {
         return Arrays.binarySearch(BAD_COUNTRY_2LDS, var1[1]) < 0;
      } else {
         return true;
      }
   }

   public static String[] getCNs(X509Certificate var0) {
      LinkedList var1 = new LinkedList();
      String var2 = var0.getSubjectX500Principal().toString();
      StringTokenizer var3 = new StringTokenizer(var2, ",+");

      while(var3.hasMoreTokens()) {
         String var4 = var3.nextToken().trim();
         if (var4.length() > 3 && var4.substring(0, 3).equalsIgnoreCase("CN=")) {
            var1.add(var4.substring(3));
         }
      }

      if (!var1.isEmpty()) {
         String[] var5 = new String[var1.size()];
         var1.toArray(var5);
         return var5;
      } else {
         return null;
      }
   }

   private static String[] getSubjectAlts(X509Certificate var0, String var1) {
      byte var2;
      if (var1 != null) {
         var2 = 7;
      } else {
         var2 = 2;
      }

      LinkedList var3 = new LinkedList();
      Collection var4 = null;

      try {
         var4 = var0.getSubjectAlternativeNames();
      } catch (CertificateParsingException var10) {
      }

      if (var4 != null) {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            List var6 = (List)var5.next();
            int var8 = (Integer)var6.get(0);
            if (var8 == var2) {
               String var9 = (String)var6.get(1);
               var3.add(var9);
            }
         }
      }

      if (!var3.isEmpty()) {
         String[] var11 = new String[var3.size()];
         var3.toArray(var11);
         return var11;
      } else {
         return null;
      }
   }

   public static String[] getDNSSubjectAlts(X509Certificate var0) {
      return getSubjectAlts(var0, (String)null);
   }

   public static int countDots(String var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) == '.') {
            ++var1;
         }
      }

      return var1;
   }

   private String normaliseIPv6Address(String var1) {
      if (var1 != null && InetAddressUtils.isIPv6Address(var1)) {
         try {
            InetAddress var2 = InetAddress.getByName(var1);
            return var2.getHostAddress();
         } catch (UnknownHostException var3) {
            this.log.error("Unexpected error converting " + var1, var3);
            return var1;
         }
      } else {
         return var1;
      }
   }

   static {
      Arrays.sort(BAD_COUNTRY_2LDS);
   }
}
