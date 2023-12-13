package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator {
   private final Log log = LogFactory.getLog(this.getClass());
   protected final SchemeRegistry schemeRegistry;
   protected final DnsResolver dnsResolver;

   public DefaultClientConnectionOperator(SchemeRegistry var1) {
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.dnsResolver = new SystemDefaultDnsResolver();
   }

   public DefaultClientConnectionOperator(SchemeRegistry var1, DnsResolver var2) {
      Args.notNull(var1, "Scheme registry");
      Args.notNull(var2, "DNS resolver");
      this.schemeRegistry = var1;
      this.dnsResolver = var2;
   }

   public OperatedClientConnection createConnection() {
      return new DefaultClientConnection();
   }

   private SchemeRegistry getSchemeRegistry(HttpContext var1) {
      SchemeRegistry var2 = (SchemeRegistry)var1.getAttribute("http.scheme-registry");
      if (var2 == null) {
         var2 = this.schemeRegistry;
      }

      return var2;
   }

   public void openConnection(OperatedClientConnection var1, HttpHost var2, InetAddress var3, HttpContext var4, HttpParams var5) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "Target host");
      Args.notNull(var5, "HTTP parameters");
      Asserts.check(!var1.isOpen(), "Connection must not be open");
      SchemeRegistry var6 = this.getSchemeRegistry(var4);
      Scheme var7 = var6.getScheme(var2.getSchemeName());
      SchemeSocketFactory var8 = var7.getSchemeSocketFactory();
      InetAddress[] var9 = this.resolveHostname(var2.getHostName());
      int var10 = var7.resolvePort(var2.getPort());

      for(int var11 = 0; var11 < var9.length; ++var11) {
         InetAddress var12 = var9[var11];
         boolean var13 = var11 == var9.length - 1;
         Socket var14 = var8.createSocket(var5);
         var1.opening(var14, var2);
         HttpInetSocketAddress var15 = new HttpInetSocketAddress(var2, var12, var10);
         InetSocketAddress var16 = null;
         if (var3 != null) {
            var16 = new InetSocketAddress(var3, 0);
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("Connecting to " + var15);
         }

         try {
            Socket var17 = var8.connectSocket(var14, var15, var16, var5);
            if (var14 != var17) {
               var14 = var17;
               var1.opening(var17, var2);
            }

            this.prepareSocket(var14, var4, var5);
            var1.openCompleted(var8.isSecure(var14), var5);
            return;
         } catch (ConnectException var18) {
            if (var13) {
               throw var18;
            }
         } catch (ConnectTimeoutException var19) {
            if (var13) {
               throw var19;
            }
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug("Connect to " + var15 + " timed out. " + "Connection will be retried using another IP address");
         }
      }

   }

   public void updateSecureConnection(OperatedClientConnection var1, HttpHost var2, HttpContext var3, HttpParams var4) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "Target host");
      Args.notNull(var4, "Parameters");
      Asserts.check(var1.isOpen(), "Connection must be open");
      SchemeRegistry var5 = this.getSchemeRegistry(var3);
      Scheme var6 = var5.getScheme(var2.getSchemeName());
      Asserts.check(var6.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
      SchemeLayeredSocketFactory var7 = (SchemeLayeredSocketFactory)var6.getSchemeSocketFactory();
      Socket var8 = var7.createLayeredSocket(var1.getSocket(), var2.getHostName(), var6.resolvePort(var2.getPort()), var4);
      this.prepareSocket(var8, var3, var4);
      var1.update(var8, var2, var7.isSecure(var8), var4);
   }

   protected void prepareSocket(Socket var1, HttpContext var2, HttpParams var3) throws IOException {
      var1.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(var3));
      var1.setSoTimeout(HttpConnectionParams.getSoTimeout(var3));
      int var4 = HttpConnectionParams.getLinger(var3);
      if (var4 >= 0) {
         var1.setSoLinger(var4 > 0, var4);
      }

   }

   protected InetAddress[] resolveHostname(String var1) throws UnknownHostException {
      return this.dnsResolver.resolve(var1);
   }
}
