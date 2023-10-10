package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager extends TCPSocketManager {
   public static final int DEFAULT_PORT = 6514;
   private static final TLSSocketManager.TLSSocketManagerFactory FACTORY = new TLSSocketManager.TLSSocketManagerFactory();
   private SSLConfiguration sslConfig;

   public TLSSocketManager(String var1, OutputStream var2, Socket var3, SSLConfiguration var4, InetAddress var5, String var6, int var7, int var8, boolean var9, Layout var10) {
      super(var1, var2, var3, var5, var6, var7, var8, var9, var10);
      this.sslConfig = var4;
   }

   public static TLSSocketManager getSocketManager(SSLConfiguration var0, String var1, int var2, int var3, boolean var4, Layout var5) {
      if (Strings.isEmpty(var1)) {
         throw new IllegalArgumentException("A host name is required");
      } else {
         if (var2 <= 0) {
            var2 = 6514;
         }

         if (var3 == 0) {
            var3 = 30000;
         }

         return (TLSSocketManager)getManager("TLS:" + var1 + ":" + var2, new TLSSocketManager.TLSFactoryData(var0, var1, var2, var3, var4, var5), FACTORY);
      }
   }

   protected Socket createSocket(String var1, int var2) throws IOException {
      SSLSocketFactory var3 = createSSLSocketFactory(this.sslConfig);
      return var3.createSocket(var1, var2);
   }

   private static SSLSocketFactory createSSLSocketFactory(SSLConfiguration var0) {
      SSLSocketFactory var1;
      if (var0 != null) {
         var1 = var0.getSSLSocketFactory();
      } else {
         var1 = (SSLSocketFactory)SSLSocketFactory.getDefault();
      }

      return var1;
   }

   static Logger access$300() {
      return LOGGER;
   }

   static Logger access$700() {
      return LOGGER;
   }

   static SSLSocketFactory access$900(SSLConfiguration var0) {
      return createSSLSocketFactory(var0);
   }

   private static class TLSSocketManagerFactory implements ManagerFactory {
      private TLSSocketManagerFactory() {
      }

      public TLSSocketManager createManager(String var1, TLSSocketManager.TLSFactoryData var2) {
         InetAddress var3 = null;
         Object var4 = null;
         Socket var5 = null;

         try {
            var3 = this.resolveAddress(TLSSocketManager.TLSFactoryData.access$100(var2));
            var5 = this.createSocket(var2);
            var4 = var5.getOutputStream();
            this.checkDelay(TLSSocketManager.TLSFactoryData.access$200(var2), (OutputStream)var4);
         } catch (IOException var7) {
            TLSSocketManager.access$300().error("TLSSocketManager (" + var1 + ") " + var7);
            var4 = new ByteArrayOutputStream();
         } catch (TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException var8) {
            return null;
         }

         return this.createManager(var1, (OutputStream)var4, var5, var2.sslConfig, var3, TLSSocketManager.TLSFactoryData.access$100(var2), TLSSocketManager.TLSFactoryData.access$400(var2), TLSSocketManager.TLSFactoryData.access$200(var2), TLSSocketManager.TLSFactoryData.access$500(var2), TLSSocketManager.TLSFactoryData.access$600(var2));
      }

      private InetAddress resolveAddress(String var1) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException {
         try {
            InetAddress var2 = InetAddress.getByName(var1);
            return var2;
         } catch (UnknownHostException var4) {
            TLSSocketManager.access$700().error((String)("Could not find address of " + var1), (Throwable)var4);
            throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException(this);
         }
      }

      private void checkDelay(int var1, OutputStream var2) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException {
         if (var1 == 0 && var2 == null) {
            throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException(this);
         }
      }

      private Socket createSocket(TLSSocketManager.TLSFactoryData var1) throws IOException {
         SSLSocketFactory var2 = TLSSocketManager.access$900(var1.sslConfig);
         SSLSocket var3 = (SSLSocket)var2.createSocket(TLSSocketManager.TLSFactoryData.access$100(var1), TLSSocketManager.TLSFactoryData.access$400(var1));
         return var3;
      }

      private TLSSocketManager createManager(String var1, OutputStream var2, Socket var3, SSLConfiguration var4, InetAddress var5, String var6, int var7, int var8, boolean var9, Layout var10) {
         return new TLSSocketManager(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (TLSSocketManager.TLSFactoryData)var2);
      }

      TLSSocketManagerFactory(Object var1) {
         this();
      }

      private class TLSSocketManagerFactoryException extends Exception {
         final TLSSocketManager.TLSSocketManagerFactory this$0;

         private TLSSocketManagerFactoryException(TLSSocketManager.TLSSocketManagerFactory var1) {
            this.this$0 = var1;
         }

         TLSSocketManagerFactoryException(TLSSocketManager.TLSSocketManagerFactory var1, Object var2) {
            this(var1);
         }
      }
   }

   private static class TLSFactoryData {
      protected SSLConfiguration sslConfig;
      private final String host;
      private final int port;
      private final int delay;
      private final boolean immediateFail;
      private final Layout layout;

      public TLSFactoryData(SSLConfiguration var1, String var2, int var3, int var4, boolean var5, Layout var6) {
         this.host = var2;
         this.port = var3;
         this.delay = var4;
         this.immediateFail = var5;
         this.layout = var6;
         this.sslConfig = var1;
      }

      static String access$100(TLSSocketManager.TLSFactoryData var0) {
         return var0.host;
      }

      static int access$200(TLSSocketManager.TLSFactoryData var0) {
         return var0.delay;
      }

      static int access$400(TLSSocketManager.TLSFactoryData var0) {
         return var0.port;
      }

      static boolean access$500(TLSSocketManager.TLSFactoryData var0) {
         return var0.immediateFail;
      }

      static Layout access$600(TLSSocketManager.TLSFactoryData var0) {
         return var0.layout;
      }
   }
}
