package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

public class SocketServer extends AbstractServer implements Runnable {
   private final Logger logger;
   private static final int MAX_PORT = 65534;
   private volatile boolean isActive = true;
   private final ServerSocket server;
   private final ConcurrentMap handlers = new ConcurrentHashMap();

   public SocketServer(int var1) throws IOException {
      this.server = new ServerSocket(var1);
      this.logger = LogManager.getLogger(this.getClass().getName() + '.' + var1);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         int var1 = Integer.parseInt(var0[0]);
         if (var1 > 0 && var1 < 65534) {
            if (var0.length == 2 && var0[1].length() > 0) {
               ConfigurationFactory.setConfigurationFactory(new SocketServer.ServerConfigurationFactory(var0[1]));
            }

            SocketServer var2 = new SocketServer(var1);
            Thread var3 = new Thread(var2);
            var3.start();
            Charset var4 = Charset.defaultCharset();
            BufferedReader var5 = new BufferedReader(new InputStreamReader(System.in, var4));

            String var6;
            do {
               var6 = var5.readLine();
            } while(var6 != null && !var6.equalsIgnoreCase("Quit") && !var6.equalsIgnoreCase("Stop") && !var6.equalsIgnoreCase("Exit"));

            var2.shutdown();
            var3.join();
         } else {
            System.err.println("Invalid port number");
            printUsage();
         }
      } else {
         System.err.println("Incorrect number of arguments");
         printUsage();
      }
   }

   private static void printUsage() {
      System.out.println("Usage: ServerSocket port configFilePath");
   }

   public void shutdown() {
      this.isActive = false;
      Thread.currentThread().interrupt();
   }

   public void run() {
      while(this.isActive) {
         try {
            Socket var1 = this.server.accept();
            var1.setSoLinger(true, 0);
            SocketServer.SocketHandler var2 = new SocketServer.SocketHandler(this, var1);
            this.handlers.put(var2.getId(), var2);
            var2.start();
         } catch (IOException var5) {
            System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
            var5.printStackTrace();
         }
      }

      Iterator var7 = this.handlers.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var8 = (Entry)var7.next();
         SocketServer.SocketHandler var3 = (SocketServer.SocketHandler)var8.getValue();
         var3.shutdown();

         try {
            var3.join();
         } catch (InterruptedException var6) {
         }
      }

   }

   static void access$000(SocketServer var0, LogEvent var1) {
      var0.log(var1);
   }

   static Logger access$100(SocketServer var0) {
      return var0.logger;
   }

   static ConcurrentMap access$200(SocketServer var0) {
      return var0.handlers;
   }

   private static class ServerConfigurationFactory extends XMLConfigurationFactory {
      private final String path;

      public ServerConfigurationFactory(String var1) {
         this.path = var1;
      }

      public Configuration getConfiguration(String var1, URI var2) {
         if (this.path != null && this.path.length() > 0) {
            File var3 = null;
            ConfigurationFactory.ConfigurationSource var4 = null;

            try {
               var3 = new File(this.path);
               FileInputStream var5 = new FileInputStream(var3);
               var4 = new ConfigurationFactory.ConfigurationSource(var5, var3);
            } catch (FileNotFoundException var9) {
            }

            if (var4 == null) {
               try {
                  URL var10 = new URL(this.path);
                  var4 = new ConfigurationFactory.ConfigurationSource(var10.openStream(), this.path);
               } catch (MalformedURLException var7) {
               } catch (IOException var8) {
               }
            }

            try {
               if (var4 != null) {
                  return new XMLConfiguration(var4);
               }
            } catch (Exception var6) {
            }

            System.err.println("Unable to process configuration at " + this.path + ", using default.");
         }

         return super.getConfiguration(var1, var2);
      }
   }

   private class SocketHandler extends Thread {
      private final ObjectInputStream ois;
      private boolean shutdown;
      final SocketServer this$0;

      public SocketHandler(SocketServer var1, Socket var2) throws IOException {
         this.this$0 = var1;
         this.shutdown = false;
         this.ois = new ObjectInputStream(var2.getInputStream());
      }

      public void shutdown() {
         this.shutdown = true;
         this.interrupt();
      }

      public void run() {
         boolean var1 = false;

         try {
            while(!this.shutdown) {
               LogEvent var2 = (LogEvent)this.ois.readObject();
               if (var2 != null) {
                  SocketServer.access$000(this.this$0, var2);
               }
            }
         } catch (EOFException var5) {
            var1 = true;
         } catch (OptionalDataException var6) {
            SocketServer.access$100(this.this$0).error((String)("OptionalDataException eof=" + var6.eof + " length=" + var6.length), (Throwable)var6);
         } catch (ClassNotFoundException var7) {
            SocketServer.access$100(this.this$0).error((String)"Unable to locate LogEvent class", (Throwable)var7);
         } catch (IOException var8) {
            SocketServer.access$100(this.this$0).error((String)"IOException encountered while reading from socket", (Throwable)var8);
         }

         if (!var1) {
            try {
               this.ois.close();
            } catch (Exception var4) {
            }
         }

         SocketServer.access$200(this.this$0).remove(this.getId());
      }
   }
}
