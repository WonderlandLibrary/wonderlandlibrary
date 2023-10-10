package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.status.StatusLogger;

public class DatagramOutputStream extends OutputStream {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final int SHIFT_1 = 8;
   private static final int SHIFT_2 = 16;
   private static final int SHIFT_3 = 24;
   private DatagramSocket ds;
   private final InetAddress address;
   private final int port;
   private byte[] data;
   private final byte[] header;
   private final byte[] footer;

   public DatagramOutputStream(String var1, int var2, byte[] var3, byte[] var4) {
      this.port = var2;
      this.header = var3;
      this.footer = var4;

      String var6;
      try {
         this.address = InetAddress.getByName(var1);
      } catch (UnknownHostException var8) {
         var6 = "Could not find host " + var1;
         LOGGER.error((String)var6, (Throwable)var8);
         throw new AppenderLoggingException(var6, var8);
      }

      try {
         this.ds = new DatagramSocket();
      } catch (SocketException var7) {
         var6 = "Could not instantiate DatagramSocket to " + var1;
         LOGGER.error((String)var6, (Throwable)var7);
         throw new AppenderLoggingException(var6, var7);
      }
   }

   public synchronized void write(byte[] var1, int var2, int var3) throws IOException {
      this.copy(var1, var2, var3);
   }

   public synchronized void write(int var1) throws IOException {
      this.copy(new byte[]{(byte)(var1 >>> 24), (byte)(var1 >>> 16), (byte)(var1 >>> 8), (byte)var1}, 0, 4);
   }

   public synchronized void write(byte[] var1) throws IOException {
      this.copy(var1, 0, var1.length);
   }

   public synchronized void flush() throws IOException {
      if (this.data != null && this.ds != null && this.address != null) {
         if (this.footer != null) {
            this.copy(this.footer, 0, this.footer.length);
         }

         DatagramPacket var1 = new DatagramPacket(this.data, this.data.length, this.address, this.port);
         this.ds.send(var1);
      }

      this.data = null;
      if (this.header != null) {
         this.copy(this.header, 0, this.header.length);
      }

   }

   public synchronized void close() throws IOException {
      if (this.ds != null) {
         if (this.data != null) {
            this.flush();
         }

         this.ds.close();
         this.ds = null;
      }

   }

   private void copy(byte[] var1, int var2, int var3) {
      int var4 = this.data == null ? 0 : this.data.length;
      byte[] var5 = new byte[var3 + var4];
      if (this.data != null) {
         System.arraycopy(this.data, 0, var5, 0, this.data.length);
      }

      System.arraycopy(var1, var2, var5, var4, var3);
      this.data = var5;
   }
}
