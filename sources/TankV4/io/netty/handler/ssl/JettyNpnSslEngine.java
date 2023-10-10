package io.netty.handler.ssl;

import java.nio.ByteBuffer;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import org.eclipse.jetty.npn.NextProtoNego;
import org.eclipse.jetty.npn.NextProtoNego.ClientProvider;
import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;

final class JettyNpnSslEngine extends SSLEngine {
   private static boolean available;
   private final SSLEngine engine;
   private final JettyNpnSslSession session;
   static final boolean $assertionsDisabled = !JettyNpnSslEngine.class.desiredAssertionStatus();

   static boolean isAvailable() {
      updateAvailability();
      return available;
   }

   private static void updateAvailability() {
      if (!available) {
         try {
            ClassLoader var0 = ClassLoader.getSystemClassLoader().getParent();
            if (var0 == null) {
               var0 = ClassLoader.getSystemClassLoader();
            }

            Class.forName("sun.security.ssl.NextProtoNegoExtension", true, var0);
            available = true;
         } catch (Exception var1) {
         }

      }
   }

   JettyNpnSslEngine(SSLEngine var1, List var2, boolean var3) {
      if (!$assertionsDisabled && var2.isEmpty()) {
         throw new AssertionError();
      } else {
         this.engine = var1;
         this.session = new JettyNpnSslSession(var1);
         if (var3) {
            NextProtoNego.put(var1, new ServerProvider(this, var2) {
               final List val$nextProtocols;
               final JettyNpnSslEngine this$0;

               {
                  this.this$0 = var1;
                  this.val$nextProtocols = var2;
               }

               public void unsupported() {
                  this.this$0.getSession().setApplicationProtocol((String)this.val$nextProtocols.get(this.val$nextProtocols.size() - 1));
               }

               public List protocols() {
                  return this.val$nextProtocols;
               }

               public void protocolSelected(String var1) {
                  this.this$0.getSession().setApplicationProtocol(var1);
               }
            });
         } else {
            String[] var4 = (String[])var2.toArray(new String[var2.size()]);
            String var5 = var4[var4.length - 1];
            NextProtoNego.put(var1, new ClientProvider(this, var4, var5) {
               final String[] val$list;
               final String val$fallback;
               final JettyNpnSslEngine this$0;

               {
                  this.this$0 = var1;
                  this.val$list = var2;
                  this.val$fallback = var3;
               }

               public boolean supports() {
                  return true;
               }

               public void unsupported() {
                  JettyNpnSslEngine.access$000(this.this$0).setApplicationProtocol((String)null);
               }

               public String selectProtocol(List var1) {
                  String[] var2 = this.val$list;
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     String var5 = var2[var4];
                     if (var1.contains(var5)) {
                        return var5;
                     }
                  }

                  return this.val$fallback;
               }
            });
         }

      }
   }

   public JettyNpnSslSession getSession() {
      return this.session;
   }

   public void closeInbound() throws SSLException {
      NextProtoNego.remove(this.engine);
      this.engine.closeInbound();
   }

   public void closeOutbound() {
      NextProtoNego.remove(this.engine);
      this.engine.closeOutbound();
   }

   public String getPeerHost() {
      return this.engine.getPeerHost();
   }

   public int getPeerPort() {
      return this.engine.getPeerPort();
   }

   public SSLEngineResult wrap(ByteBuffer var1, ByteBuffer var2) throws SSLException {
      return this.engine.wrap(var1, var2);
   }

   public SSLEngineResult wrap(ByteBuffer[] var1, ByteBuffer var2) throws SSLException {
      return this.engine.wrap(var1, var2);
   }

   public SSLEngineResult wrap(ByteBuffer[] var1, int var2, int var3, ByteBuffer var4) throws SSLException {
      return this.engine.wrap(var1, var2, var3, var4);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer var2) throws SSLException {
      return this.engine.unwrap(var1, var2);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2) throws SSLException {
      return this.engine.unwrap(var1, var2);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2, int var3, int var4) throws SSLException {
      return this.engine.unwrap(var1, var2, var3, var4);
   }

   public Runnable getDelegatedTask() {
      return this.engine.getDelegatedTask();
   }

   public boolean isInboundDone() {
      return this.engine.isInboundDone();
   }

   public boolean isOutboundDone() {
      return this.engine.isOutboundDone();
   }

   public String[] getSupportedCipherSuites() {
      return this.engine.getSupportedCipherSuites();
   }

   public String[] getEnabledCipherSuites() {
      return this.engine.getEnabledCipherSuites();
   }

   public void setEnabledCipherSuites(String[] var1) {
      this.engine.setEnabledCipherSuites(var1);
   }

   public String[] getSupportedProtocols() {
      return this.engine.getSupportedProtocols();
   }

   public String[] getEnabledProtocols() {
      return this.engine.getEnabledProtocols();
   }

   public void setEnabledProtocols(String[] var1) {
      this.engine.setEnabledProtocols(var1);
   }

   public SSLSession getHandshakeSession() {
      return this.engine.getHandshakeSession();
   }

   public void beginHandshake() throws SSLException {
      this.engine.beginHandshake();
   }

   public HandshakeStatus getHandshakeStatus() {
      return this.engine.getHandshakeStatus();
   }

   public void setUseClientMode(boolean var1) {
      this.engine.setUseClientMode(var1);
   }

   public boolean getUseClientMode() {
      return this.engine.getUseClientMode();
   }

   public void setNeedClientAuth(boolean var1) {
      this.engine.setNeedClientAuth(var1);
   }

   public boolean getNeedClientAuth() {
      return this.engine.getNeedClientAuth();
   }

   public void setWantClientAuth(boolean var1) {
      this.engine.setWantClientAuth(var1);
   }

   public boolean getWantClientAuth() {
      return this.engine.getWantClientAuth();
   }

   public void setEnableSessionCreation(boolean var1) {
      this.engine.setEnableSessionCreation(var1);
   }

   public boolean getEnableSessionCreation() {
      return this.engine.getEnableSessionCreation();
   }

   public SSLParameters getSSLParameters() {
      return this.engine.getSSLParameters();
   }

   public void setSSLParameters(SSLParameters var1) {
      this.engine.setSSLParameters(var1);
   }

   public SSLSession getSession() {
      return this.getSession();
   }

   static JettyNpnSslSession access$000(JettyNpnSslEngine var0) {
      return var0.session;
   }
}
