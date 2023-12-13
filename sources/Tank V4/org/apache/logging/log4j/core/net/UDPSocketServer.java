package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

public class UDPSocketServer extends AbstractServer implements Runnable {
   private final Logger logger;
   private static final int MAX_PORT = 65534;
   private volatile boolean isActive = true;
   private final DatagramSocket server;
   private final int maxBufferSize = 67584;

   public UDPSocketServer(int var1) throws IOException {
      this.server = new DatagramSocket(var1);
      this.logger = LogManager.getLogger(this.getClass().getName() + '.' + var1);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         int var1 = Integer.parseInt(var0[0]);
         if (var1 > 0 && var1 < 65534) {
            if (var0.length == 2 && var0[1].length() > 0) {
               ConfigurationFactory.setConfigurationFactory(new UDPSocketServer.ServerConfigurationFactory(var0[1]));
            }

            UDPSocketServer var2 = new UDPSocketServer(var1);
            Thread var3 = new Thread(var2);
            var3.start();
            BufferedReader var4 = new BufferedReader(new InputStreamReader(System.in));

            String var5;
            do {
               var5 = var4.readLine();
            } while(var5 != null && !var5.equalsIgnoreCase("Quit") && !var5.equalsIgnoreCase("Stop") && !var5.equalsIgnoreCase("Exit"));

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
            byte[] var1 = new byte[67584];
            DatagramPacket var2 = new DatagramPacket(var1, var1.length);
            this.server.receive(var2);
            ObjectInputStream var3 = new ObjectInputStream(new ByteArrayInputStream(var2.getData(), var2.getOffset(), var2.getLength()));
            LogEvent var4 = (LogEvent)var3.readObject();
            if (var4 != null) {
               this.log(var4);
            }
         } catch (OptionalDataException var5) {
            this.logger.error((String)("OptionalDataException eof=" + var5.eof + " length=" + var5.length), (Throwable)var5);
         } catch (ClassNotFoundException var6) {
            this.logger.error((String)"Unable to locate LogEvent class", (Throwable)var6);
         } catch (EOFException var7) {
            this.logger.info("EOF encountered");
         } catch (IOException var8) {
            this.logger.error((String)"Exception encountered on accept. Ignoring. Stack Trace :", (Throwable)var8);
         }
      }

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
}
