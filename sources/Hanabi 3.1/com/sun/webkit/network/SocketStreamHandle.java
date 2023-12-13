package com.sun.webkit.network;

import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.Proxy.Type;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

final class SocketStreamHandle {
   private static final Pattern FIRST_LINE_PATTERN = Pattern.compile("^HTTP/1.[01]\\s+(\\d{3})(?:\\s.*)?$");
   private static final Logger logger = Logger.getLogger(SocketStreamHandle.class.getName());
   private static final ThreadPoolExecutor threadPool;
   private final String host;
   private final int port;
   private final boolean ssl;
   private final WebPage webPage;
   private final long data;
   private volatile Socket socket;
   private volatile State state;
   private volatile boolean connected;

   private SocketStreamHandle(String var1, int var2, boolean var3, WebPage var4, long var5) {
      this.state = SocketStreamHandle.State.ACTIVE;
      this.host = var1;
      this.port = var2;
      this.ssl = var3;
      this.webPage = var4;
      this.data = var5;
   }

   private static SocketStreamHandle fwkCreate(String var0, int var1, boolean var2, WebPage var3, long var4) {
      SocketStreamHandle var6 = new SocketStreamHandle(var0, var1, var2, var3, var4);
      logger.log(Level.FINEST, "Starting {0}", var6);
      threadPool.submit(() -> {
         var6.run();
      });
      return var6;
   }

   private void run() {
      if (this.webPage == null) {
         logger.log(Level.FINEST, "{0} is not associated with any web page, aborted", this);
         this.didFail(0, "Web socket is not associated with any web page");
         this.didClose();
      } else {
         AccessController.doPrivileged(() -> {
            this.doRun();
            return null;
         }, this.webPage.getAccessControlContext());
      }
   }

   private void doRun() {
      Object var1 = null;
      String var2 = null;

      try {
         logger.log(Level.FINEST, "{0} started", this);
         this.connect();
         this.connected = true;
         logger.log(Level.FINEST, "{0} connected", this);
         this.didOpen();
         InputStream var3 = this.socket.getInputStream();

         while(true) {
            byte[] var4 = new byte[8192];
            int var5 = var3.read(var4);
            if (var5 <= 0) {
               logger.log(Level.FINEST, "{0} connection closed by remote host", this);
               break;
            }

            if (logger.isLoggable(Level.FINEST)) {
               logger.log(Level.FINEST, String.format("%s received len: [%d], data:%s", this, var5, dump(var4, var5)));
            }

            this.didReceiveData(var4, var5);
         }
      } catch (UnknownHostException var7) {
         var1 = var7;
         var2 = "Unknown host";
      } catch (ConnectException var8) {
         var1 = var8;
         var2 = "Unable to connect";
      } catch (NoRouteToHostException var9) {
         var1 = var9;
         var2 = "No route to host";
      } catch (PortUnreachableException var10) {
         var1 = var10;
         var2 = "Port unreachable";
      } catch (SocketException var11) {
         if (this.state != SocketStreamHandle.State.ACTIVE) {
            if (logger.isLoggable(Level.FINEST)) {
               logger.log(Level.FINEST, String.format("%s exception (most likely caused by local close)", this), var11);
            }
         } else {
            var1 = var11;
            var2 = "Socket error";
         }
      } catch (SSLException var12) {
         var1 = var12;
         var2 = "SSL error";
      } catch (IOException var13) {
         var1 = var13;
         var2 = "I/O error";
      } catch (SecurityException var14) {
         var1 = var14;
         var2 = "Security error";
      } catch (Throwable var15) {
         var1 = var15;
      }

      if (var1 != null) {
         if (var2 == null) {
            var2 = "Unknown error";
            logger.log(Level.WARNING, String.format("%s unexpected error", this), (Throwable)var1);
         } else {
            logger.log(Level.FINEST, String.format("%s exception", this), (Throwable)var1);
         }

         this.didFail(0, var2);
      }

      try {
         this.socket.close();
      } catch (IOException var6) {
      }

      this.didClose();
      logger.log(Level.FINEST, "{0} finished", this);
   }

   private void connect() throws IOException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkConnect(this.host, this.port);
      }

      boolean var2 = false;
      IOException var3 = null;
      boolean var4 = false;
      ProxySelector var5 = (ProxySelector)AccessController.doPrivileged(() -> {
         return ProxySelector.getDefault();
      });
      if (var5 != null) {
         URI var6;
         try {
            var6 = new URI((this.ssl ? "https" : "http") + "://" + this.host);
         } catch (URISyntaxException var11) {
            throw new IOException(var11);
         }

         if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s selecting proxies for: [%s]", this, var6));
         }

         List var7 = var5.select(var6);
         if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s selected proxies: %s", this, var7));
         }

         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            Proxy var9 = (Proxy)var8.next();
            if (logger.isLoggable(Level.FINEST)) {
               logger.log(Level.FINEST, String.format("%s trying proxy: [%s]", this, var9));
            }

            if (var9.type() == Type.DIRECT) {
               var4 = true;
            }

            try {
               this.connect(var9);
               var2 = true;
               break;
            } catch (IOException var12) {
               logger.log(Level.FINEST, String.format("%s exception", this), var12);
               var3 = var12;
               if (var9.address() != null) {
                  var5.connectFailed(var6, var9.address(), var12);
               }
            }
         }
      }

      if (!var2 && !var4) {
         logger.log(Level.FINEST, "{0} trying direct connection", this);
         this.connect(Proxy.NO_PROXY);
         var2 = true;
      }

      if (!var2) {
         throw var3;
      }
   }

   private void connect(Proxy var1) throws IOException {
      synchronized(this) {
         if (this.state != SocketStreamHandle.State.ACTIVE) {
            throw new SocketException("Close requested");
         }

         this.socket = new Socket(var1);
      }

      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("%s connecting to: [%s:%d]", this, this.host, this.port));
      }

      this.socket.connect(new InetSocketAddress(this.host, this.port));
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("%s connected to: [%s:%d]", this, this.host, this.port));
      }

      if (this.ssl) {
         synchronized(this) {
            if (this.state != SocketStreamHandle.State.ACTIVE) {
               throw new SocketException("Close requested");
            }

            logger.log(Level.FINEST, "{0} starting SSL handshake", this);
            this.socket = HttpsURLConnection.getDefaultSSLSocketFactory().createSocket(this.socket, this.host, this.port, true);
         }

         ((SSLSocket)this.socket).startHandshake();
      }

   }

   private int fwkSend(byte[] var1) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("%s sending len: [%d], data:%s", this, var1.length, dump(var1, var1.length)));
      }

      if (this.connected) {
         try {
            this.socket.getOutputStream().write(var1);
            return var1.length;
         } catch (IOException var3) {
            logger.log(Level.FINEST, String.format("%s exception", this), var3);
            this.didFail(0, "I/O error");
            return 0;
         }
      } else {
         logger.log(Level.FINEST, "{0} not connected", this);
         this.didFail(0, "Not connected");
         return 0;
      }
   }

   private void fwkClose() {
      synchronized(this) {
         logger.log(Level.FINEST, "{0}", this);
         this.state = SocketStreamHandle.State.CLOSE_REQUESTED;

         try {
            if (this.socket != null) {
               this.socket.close();
            }
         } catch (IOException var4) {
         }

      }
   }

   private void fwkNotifyDisposed() {
      logger.log(Level.FINEST, "{0}", this);
      this.state = SocketStreamHandle.State.DISPOSED;
   }

   private void didOpen() {
      Invoker.getInvoker().postOnEventThread(() -> {
         if (this.state == SocketStreamHandle.State.ACTIVE) {
            this.notifyDidOpen();
         }

      });
   }

   private void didReceiveData(byte[] var1, int var2) {
      Invoker.getInvoker().postOnEventThread(() -> {
         if (this.state == SocketStreamHandle.State.ACTIVE) {
            this.notifyDidReceiveData(var1, var2);
         }

      });
   }

   private void didFail(int var1, String var2) {
      Invoker.getInvoker().postOnEventThread(() -> {
         if (this.state == SocketStreamHandle.State.ACTIVE) {
            this.notifyDidFail(var1, var2);
         }

      });
   }

   private void didClose() {
      Invoker.getInvoker().postOnEventThread(() -> {
         if (this.state != SocketStreamHandle.State.DISPOSED) {
            this.notifyDidClose();
         }

      });
   }

   private void notifyDidOpen() {
      logger.log(Level.FINEST, "{0}", this);
      twkDidOpen(this.data);
   }

   private void notifyDidReceiveData(byte[] var1, int var2) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("%s, len: [%d], data:%s", this, var2, dump(var1, var2)));
      }

      twkDidReceiveData(var1, var2, this.data);
   }

   private void notifyDidFail(int var1, String var2) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("%s, errorCode: %d, errorDescription: %s", this, var1, var2));
      }

      twkDidFail(var1, var2, this.data);
   }

   private void notifyDidClose() {
      logger.log(Level.FINEST, "{0}", this);
      twkDidClose(this.data);
   }

   private static native void twkDidOpen(long var0);

   private static native void twkDidReceiveData(byte[] var0, int var1, long var2);

   private static native void twkDidFail(int var0, String var1, long var2);

   private static native void twkDidClose(long var0);

   private static String dump(byte[] var0, int var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while(var3 < var1) {
         StringBuilder var4 = new StringBuilder();
         StringBuilder var5 = new StringBuilder();

         for(int var6 = 0; var6 < 16; ++var3) {
            if (var3 >= var1) {
               var4.append("   ");
            } else {
               int var7 = var0[var3] & 255;
               var4.append(String.format("%02x ", var7));
               var5.append(var7 >= 32 && var7 <= 126 ? (char)var7 : '.');
            }

            ++var6;
         }

         var2.append(String.format("%n  ")).append(var4).append(' ').append(var5);
      }

      return var2.toString();
   }

   public String toString() {
      return String.format("SocketStreamHandle{host=%s, port=%d, ssl=%s, data=0x%016X, state=%s, connected=%s}", this.host, this.port, this.ssl, this.data, this.state, this.connected);
   }

   static {
      threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10L, TimeUnit.SECONDS, new SynchronousQueue(), new CustomThreadFactory());
   }

   private static final class CustomThreadFactory implements ThreadFactory {
      private final ThreadGroup group;
      private final AtomicInteger index;

      private CustomThreadFactory() {
         this.index = new AtomicInteger(1);
         SecurityManager var1 = System.getSecurityManager();
         this.group = var1 != null ? var1.getThreadGroup() : Thread.currentThread().getThreadGroup();
      }

      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(this.group, var1, "SocketStreamHandle-" + this.index.getAndIncrement());
         var2.setDaemon(true);
         if (var2.getPriority() != 5) {
            var2.setPriority(5);
         }

         return var2;
      }

      // $FF: synthetic method
      CustomThreadFactory(Object var1) {
         this();
      }
   }

   private static enum State {
      ACTIVE,
      CLOSE_REQUESTED,
      DISPOSED;
   }
}
