package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;

public class TCPSocketManager extends AbstractSocketManager {
   public static final int DEFAULT_RECONNECTION_DELAY = 30000;
   private static final int DEFAULT_PORT = 4560;
   private static final TCPSocketManager.TCPSocketManagerFactory FACTORY = new TCPSocketManager.TCPSocketManagerFactory();
   private final int reconnectionDelay;
   private TCPSocketManager.Reconnector connector = null;
   private Socket socket;
   private final boolean retry;
   private final boolean immediateFail;

   public TCPSocketManager(String var1, OutputStream var2, Socket var3, InetAddress var4, String var5, int var6, int var7, boolean var8, Layout var9) {
      super(var1, var2, var4, var5, var6, var9);
      this.reconnectionDelay = var7;
      this.socket = var3;
      this.immediateFail = var8;
      this.retry = var7 > 0;
      if (var3 == null) {
         this.connector = new TCPSocketManager.Reconnector(this, this);
         this.connector.setDaemon(true);
         this.connector.setPriority(1);
         this.connector.start();
      }

   }

   public static TCPSocketManager getSocketManager(String var0, int var1, int var2, boolean var3, Layout var4) {
      if (Strings.isEmpty(var0)) {
         throw new IllegalArgumentException("A host name is required");
      } else {
         if (var1 <= 0) {
            var1 = 4560;
         }

         if (var2 == 0) {
            var2 = 30000;
         }

         return (TCPSocketManager)getManager("TCP:" + var0 + ":" + var1, new TCPSocketManager.FactoryData(var0, var1, var2, var3, var4), FACTORY);
      }
   }

   protected void write(byte[] var1, int var2, int var3) {
      if (this.socket == null) {
         if (this.connector != null && !this.immediateFail) {
            this.connector.latch();
         }

         if (this.socket == null) {
            String var4 = "Error writing to " + this.getName() + " socket not available";
            throw new AppenderLoggingException(var4);
         }
      }

      synchronized(this){}

      try {
         this.getOutputStream().write(var1, var2, var3);
      } catch (IOException var8) {
         if (this.retry && this.connector == null) {
            this.connector = new TCPSocketManager.Reconnector(this, this);
            this.connector.setDaemon(true);
            this.connector.setPriority(1);
            this.connector.start();
         }

         String var6 = "Error writing to " + this.getName();
         throw new AppenderLoggingException(var6, var8);
      }

   }

   protected synchronized void close() {
      super.close();
      if (this.connector != null) {
         this.connector.shutdown();
         this.connector.interrupt();
         this.connector = null;
      }

   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("protocol", "tcp");
      var1.put("direction", "out");
      return var1;
   }

   protected Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.createSocket(var1.getHostName(), var2);
   }

   protected Socket createSocket(String var1, int var2) throws IOException {
      return new Socket(var1, var2);
   }

   static int access$000(TCPSocketManager var0) {
      return var0.reconnectionDelay;
   }

   static OutputStream access$100(TCPSocketManager var0) {
      return var0.getOutputStream();
   }

   static void access$200(TCPSocketManager var0, OutputStream var1) {
      var0.setOutputStream(var1);
   }

   static Socket access$302(TCPSocketManager var0, Socket var1) {
      return var0.socket = var1;
   }

   static TCPSocketManager.Reconnector access$402(TCPSocketManager var0, TCPSocketManager.Reconnector var1) {
      return var0.connector = var1;
   }

   static Logger access$500() {
      return LOGGER;
   }

   static Logger access$600() {
      return LOGGER;
   }

   static Logger access$700() {
      return LOGGER;
   }

   static Logger access$800() {
      return LOGGER;
   }

   static Logger access$1000() {
      return LOGGER;
   }

   static Logger access$1500() {
      return LOGGER;
   }

   protected static class TCPSocketManagerFactory implements ManagerFactory {
      public TCPSocketManager createManager(String var1, TCPSocketManager.FactoryData var2) {
         InetAddress var3;
         try {
            var3 = InetAddress.getByName(TCPSocketManager.FactoryData.access$900(var2));
         } catch (UnknownHostException var6) {
            TCPSocketManager.access$1000().error((String)("Could not find address of " + TCPSocketManager.FactoryData.access$900(var2)), (Throwable)var6);
            return null;
         }

         try {
            Socket var5 = new Socket(TCPSocketManager.FactoryData.access$900(var2), TCPSocketManager.FactoryData.access$1100(var2));
            OutputStream var8 = var5.getOutputStream();
            return new TCPSocketManager(var1, var8, var5, var3, TCPSocketManager.FactoryData.access$900(var2), TCPSocketManager.FactoryData.access$1100(var2), TCPSocketManager.FactoryData.access$1200(var2), TCPSocketManager.FactoryData.access$1300(var2), TCPSocketManager.FactoryData.access$1400(var2));
         } catch (IOException var7) {
            TCPSocketManager.access$1500().error("TCPSocketManager (" + var1 + ") " + var7);
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            return TCPSocketManager.FactoryData.access$1200(var2) == 0 ? null : new TCPSocketManager(var1, var4, (Socket)null, var3, TCPSocketManager.FactoryData.access$900(var2), TCPSocketManager.FactoryData.access$1100(var2), TCPSocketManager.FactoryData.access$1200(var2), TCPSocketManager.FactoryData.access$1300(var2), TCPSocketManager.FactoryData.access$1400(var2));
         }
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (TCPSocketManager.FactoryData)var2);
      }
   }

   private static class FactoryData {
      private final String host;
      private final int port;
      private final int delay;
      private final boolean immediateFail;
      private final Layout layout;

      public FactoryData(String var1, int var2, int var3, boolean var4, Layout var5) {
         this.host = var1;
         this.port = var2;
         this.delay = var3;
         this.immediateFail = var4;
         this.layout = var5;
      }

      static String access$900(TCPSocketManager.FactoryData var0) {
         return var0.host;
      }

      static int access$1100(TCPSocketManager.FactoryData var0) {
         return var0.port;
      }

      static int access$1200(TCPSocketManager.FactoryData var0) {
         return var0.delay;
      }

      static boolean access$1300(TCPSocketManager.FactoryData var0) {
         return var0.immediateFail;
      }

      static Layout access$1400(TCPSocketManager.FactoryData var0) {
         return var0.layout;
      }
   }

   private class Reconnector extends Thread {
      private final CountDownLatch latch;
      private boolean shutdown;
      private final Object owner;
      final TCPSocketManager this$0;

      public Reconnector(TCPSocketManager var1, OutputStreamManager var2) {
         this.this$0 = var1;
         this.latch = new CountDownLatch(1);
         this.shutdown = false;
         this.owner = var2;
      }

      public void latch() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
         }

      }

      public void shutdown() {
         this.shutdown = true;
      }

      public void run() {
         while(!this.shutdown) {
            try {
               sleep((long)TCPSocketManager.access$000(this.this$0));
               Socket var1 = this.this$0.createSocket(this.this$0.address, this.this$0.port);
               OutputStream var2 = var1.getOutputStream();
               synchronized(this.owner){}

               try {
                  TCPSocketManager.access$100(this.this$0).close();
               } catch (IOException var7) {
               }

               TCPSocketManager.access$200(this.this$0, var2);
               TCPSocketManager.access$302(this.this$0, var1);
               TCPSocketManager.access$402(this.this$0, (TCPSocketManager.Reconnector)null);
               this.shutdown = true;
               TCPSocketManager.access$500().debug("Connection to " + this.this$0.host + ":" + this.this$0.port + " reestablished.");
            } catch (InterruptedException var8) {
               TCPSocketManager.access$600().debug("Reconnection interrupted.");
               this.latch.countDown();
               continue;
            } catch (ConnectException var9) {
               TCPSocketManager.access$700().debug(this.this$0.host + ":" + this.this$0.port + " refused connection");
               this.latch.countDown();
               continue;
            } catch (IOException var10) {
               TCPSocketManager.access$800().debug("Unable to reconnect to " + this.this$0.host + ":" + this.this$0.port);
               this.latch.countDown();
               continue;
            }

            this.latch.countDown();
         }

      }
   }
}
