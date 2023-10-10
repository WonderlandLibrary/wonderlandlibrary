package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@ThreadSafe
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
   public static final String TLS = "TLS";
   public static final String SSL = "SSL";
   public static final String SSLV2 = "SSLv2";
   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
   private final javax.net.ssl.SSLSocketFactory socketfactory;
   private final X509HostnameVerifier hostnameVerifier;
   private final String[] supportedProtocols;
   private final String[] supportedCipherSuites;

   public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
      return new SSLConnectionSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
      return new SSLConnectionSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLConnectionSocketFactory(SSLContext var1) {
      this(var1, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLConnectionSocketFactory(SSLContext var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), (String[])null, (String[])null, var2);
   }

   public SSLConnectionSocketFactory(SSLContext var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this(((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), var2, var3, var4);
   }

   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)var1, (String[])null, (String[])null, var2);
   }

   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this.socketfactory = (javax.net.ssl.SSLSocketFactory)Args.notNull(var1, "SSL socket factory");
      this.supportedProtocols = var2;
      this.supportedCipherSuites = var3;
      this.hostnameVerifier = var4 != null ? var4 : BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
   }

   protected void prepareSocket(SSLSocket var1) throws IOException {
   }

   public Socket createSocket(HttpContext var1) throws IOException {
      return SocketFactory.getDefault().createSocket();
   }

   public Socket connectSocket(int var1, Socket var2, HttpHost var3, InetSocketAddress var4, InetSocketAddress var5, HttpContext var6) throws IOException {
      Args.notNull(var3, "HTTP host");
      Args.notNull(var4, "Remote address");
      Socket var7 = var2 != null ? var2 : this.createSocket(var6);
      if (var5 != null) {
         var7.bind(var5);
      }

      try {
         var7.connect(var4, var1);
      } catch (IOException var11) {
         try {
            var7.close();
         } catch (IOException var10) {
         }

         throw var11;
      }

      if (var7 instanceof SSLSocket) {
         SSLSocket var8 = (SSLSocket)var7;
         var8.startHandshake();
         this.verifyHostname(var8, var3.getHostName());
         return var7;
      } else {
         return this.createLayeredSocket(var7, var3.getHostName(), var4.getPort(), var6);
      }
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpContext var4) throws IOException {
      SSLSocket var5 = (SSLSocket)this.socketfactory.createSocket(var1, var2, var3, true);
      if (this.supportedProtocols != null) {
         var5.setEnabledProtocols(this.supportedProtocols);
      }

      if (this.supportedCipherSuites != null) {
         var5.setEnabledCipherSuites(this.supportedCipherSuites);
      }

      this.prepareSocket(var5);
      var5.startHandshake();
      this.verifyHostname(var5, var2);
      return var5;
   }

   X509HostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   private void verifyHostname(SSLSocket var1, String var2) throws IOException {
      try {
         this.hostnameVerifier.verify(var2, var1);
      } catch (IOException var6) {
         try {
            var1.close();
         } catch (Exception var5) {
         }

         throw var6;
      }
   }
}
