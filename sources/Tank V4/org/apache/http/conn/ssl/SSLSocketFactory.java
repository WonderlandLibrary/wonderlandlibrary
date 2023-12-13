package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.TextUtils;

/** @deprecated */
@Deprecated
@ThreadSafe
public class SSLSocketFactory implements LayeredConnectionSocketFactory, SchemeLayeredSocketFactory, LayeredSchemeSocketFactory, LayeredSocketFactory {
   public static final String TLS = "TLS";
   public static final String SSL = "SSL";
   public static final String SSLV2 = "SSLv2";
   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
   private final javax.net.ssl.SSLSocketFactory socketfactory;
   private final HostNameResolver nameResolver;
   private volatile X509HostnameVerifier hostnameVerifier;
   private final String[] supportedProtocols;
   private final String[] supportedCipherSuites;

   public static SSLSocketFactory getSocketFactory() throws SSLInitializationException {
      return new SSLSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   public static SSLSocketFactory getSystemSocketFactory() throws SSLInitializationException {
      return new SSLSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, HostNameResolver var6) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().useProtocol(var1).setSecureRandom(var5).loadKeyMaterial(var2, var3 != null ? var3.toCharArray() : null).loadTrustMaterial(var4).build(), var6);
   }

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, TrustStrategy var6, X509HostnameVerifier var7) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().useProtocol(var1).setSecureRandom(var5).loadKeyMaterial(var2, var3 != null ? var3.toCharArray() : null).loadTrustMaterial(var4, var6).build(), var7);
   }

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, X509HostnameVerifier var6) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().useProtocol(var1).setSecureRandom(var5).loadKeyMaterial(var2, var3 != null ? var3.toCharArray() : null).loadTrustMaterial(var4).build(), var6);
   }

   public SSLSocketFactory(KeyStore var1, String var2, KeyStore var3) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadKeyMaterial(var1, var2 != null ? var2.toCharArray() : null).loadTrustMaterial(var3).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(KeyStore var1, String var2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadKeyMaterial(var1, var2 != null ? var2.toCharArray() : null).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(KeyStore var1) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial(var1).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(TrustStrategy var1, X509HostnameVerifier var2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial((KeyStore)null, var1).build(), var2);
   }

   public SSLSocketFactory(TrustStrategy var1) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial((KeyStore)null, var1).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(SSLContext var1) {
      this(var1, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(SSLContext var1, HostNameResolver var2) {
      this.socketfactory = var1.getSocketFactory();
      this.hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
      this.nameResolver = var2;
      this.supportedProtocols = null;
      this.supportedCipherSuites = null;
   }

   public SSLSocketFactory(SSLContext var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), (String[])null, (String[])null, var2);
   }

   public SSLSocketFactory(SSLContext var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this(((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), var2, var3, var4);
   }

   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)var1, (String[])null, (String[])null, var2);
   }

   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this.socketfactory = (javax.net.ssl.SSLSocketFactory)Args.notNull(var1, "SSL socket factory");
      this.supportedProtocols = var2;
      this.supportedCipherSuites = var3;
      this.hostnameVerifier = var4 != null ? var4 : BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
      this.nameResolver = null;
   }

   public Socket createSocket(HttpParams var1) throws IOException {
      return this.createSocket((HttpContext)null);
   }

   public Socket createSocket() throws IOException {
      return this.createSocket((HttpContext)null);
   }

   public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, UnknownHostException, ConnectTimeoutException {
      Args.notNull(var2, "Remote address");
      Args.notNull(var4, "HTTP parameters");
      HttpHost var5;
      if (var2 instanceof HttpInetSocketAddress) {
         var5 = ((HttpInetSocketAddress)var2).getHttpHost();
      } else {
         var5 = new HttpHost(var2.getHostName(), var2.getPort(), "https");
      }

      int var6 = HttpConnectionParams.getConnectionTimeout(var4);
      return this.connectSocket(var6, var1, var5, var2, var3, (HttpContext)null);
   }

   public boolean isSecure(Socket var1) throws IllegalArgumentException {
      Args.notNull(var1, "Socket");
      Asserts.check(var1 instanceof SSLSocket, "Socket not created by this factory");
      Asserts.check(!var1.isClosed(), "Socket is closed");
      return true;
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpParams var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, (HttpContext)null);
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, (HttpContext)null);
   }

   public void setHostnameVerifier(X509HostnameVerifier var1) {
      Args.notNull(var1, "Hostname verifier");
      this.hostnameVerifier = var1;
   }

   public X509HostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException, UnknownHostException, ConnectTimeoutException {
      InetAddress var7;
      if (this.nameResolver != null) {
         var7 = this.nameResolver.resolve(var2);
      } else {
         var7 = InetAddress.getByName(var2);
      }

      InetSocketAddress var8 = null;
      if (var4 != null || var5 > 0) {
         var8 = new InetSocketAddress(var4, var5 > 0 ? var5 : 0);
      }

      HttpInetSocketAddress var9 = new HttpInetSocketAddress(new HttpHost(var2, var3), var7, var3);
      return this.connectSocket(var1, var9, var8, var6);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, var4);
   }

   protected void prepareSocket(SSLSocket var1) throws IOException {
   }

   private void internalPrepareSocket(SSLSocket var1) throws IOException {
      if (this.supportedProtocols != null) {
         var1.setEnabledProtocols(this.supportedProtocols);
      }

      if (this.supportedCipherSuites != null) {
         var1.setEnabledCipherSuites(this.supportedCipherSuites);
      }

      this.prepareSocket(var1);
   }

   public Socket createSocket(HttpContext var1) throws IOException {
      SSLSocket var2 = (SSLSocket)this.socketfactory.createSocket();
      this.internalPrepareSocket(var2);
      return var2;
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
      this.internalPrepareSocket(var5);
      var5.startHandshake();
      this.verifyHostname(var5, var2);
      return var5;
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
