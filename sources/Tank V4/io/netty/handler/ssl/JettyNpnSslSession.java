package io.netty.handler.ssl;

import java.security.Principal;
import java.security.cert.Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;

final class JettyNpnSslSession implements SSLSession {
   private final SSLEngine engine;
   private volatile String applicationProtocol;

   JettyNpnSslSession(SSLEngine var1) {
      this.engine = var1;
   }

   void setApplicationProtocol(String var1) {
      if (var1 != null) {
         var1 = var1.replace(':', '_');
      }

      this.applicationProtocol = var1;
   }

   public String getProtocol() {
      String var1 = this.unwrap().getProtocol();
      String var2 = this.applicationProtocol;
      if (var2 == null) {
         return var1 != null ? var1.replace(':', '_') : null;
      } else {
         StringBuilder var3 = new StringBuilder(32);
         if (var1 != null) {
            var3.append(var1.replace(':', '_'));
            var3.append(':');
         } else {
            var3.append("null:");
         }

         var3.append(var2);
         return var3.toString();
      }
   }

   private SSLSession unwrap() {
      return this.engine.getSession();
   }

   public byte[] getId() {
      return this.unwrap().getId();
   }

   public SSLSessionContext getSessionContext() {
      return this.unwrap().getSessionContext();
   }

   public long getCreationTime() {
      return this.unwrap().getCreationTime();
   }

   public long getLastAccessedTime() {
      return this.unwrap().getLastAccessedTime();
   }

   public void invalidate() {
      this.unwrap().invalidate();
   }

   public boolean isValid() {
      return this.unwrap().isValid();
   }

   public void putValue(String var1, Object var2) {
      this.unwrap().putValue(var1, var2);
   }

   public Object getValue(String var1) {
      return this.unwrap().getValue(var1);
   }

   public void removeValue(String var1) {
      this.unwrap().removeValue(var1);
   }

   public String[] getValueNames() {
      return this.unwrap().getValueNames();
   }

   public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
      return this.unwrap().getPeerCertificates();
   }

   public Certificate[] getLocalCertificates() {
      return this.unwrap().getLocalCertificates();
   }

   public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
      return this.unwrap().getPeerCertificateChain();
   }

   public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
      return this.unwrap().getPeerPrincipal();
   }

   public Principal getLocalPrincipal() {
      return this.unwrap().getLocalPrincipal();
   }

   public String getCipherSuite() {
      return this.unwrap().getCipherSuite();
   }

   public String getPeerHost() {
      return this.unwrap().getPeerHost();
   }

   public int getPeerPort() {
      return this.unwrap().getPeerPort();
   }

   public int getPacketBufferSize() {
      return this.unwrap().getPacketBufferSize();
   }

   public int getApplicationBufferSize() {
      return this.unwrap().getApplicationBufferSize();
   }
}
