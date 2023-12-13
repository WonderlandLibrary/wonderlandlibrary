package com.sun.webkit.network;

import com.sun.webkit.WebPage;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

final class NetworkContext {
   private static final Logger logger = Logger.getLogger(NetworkContext.class.getName());
   private static final int THREAD_POOL_SIZE = 20;
   private static final long THREAD_POOL_KEEP_ALIVE_TIME = 10000L;
   private static final int DEFAULT_HTTP_MAX_CONNECTIONS = 5;
   private static final int BYTE_BUFFER_SIZE = 40960;
   private static final ThreadPoolExecutor threadPool;
   private static final ByteBufferPool byteBufferPool;

   private NetworkContext() {
      throw new AssertionError();
   }

   private static boolean canHandleURL(String var0) {
      URL var1 = null;

      try {
         var1 = URLs.newURL(var0);
      } catch (MalformedURLException var3) {
      }

      return var1 != null;
   }

   private static URLLoaderBase fwkLoad(WebPage var0, boolean var1, String var2, String var3, String var4, FormDataElement[] var5, long var6) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("webPage: [%s], asynchronous: [%s], url: [%s], method: [%s], formDataElements: %s, data: [0x%016X], headers:%n%s", var0, var1, var2, var3, var5 != null ? Arrays.asList(var5) : "[null]", var6, Util.formatHeaders(var4)));
      }

      URLLoader var8 = new URLLoader(var0, byteBufferPool, var1, var2, var3, var4, var5, var6);
      if (var1) {
         threadPool.submit(var8);
         if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "active count: [{0}], pool size: [{1}], max pool size: [{2}], task count: [{3}], completed task count: [{4}]", new Object[]{threadPool.getActiveCount(), threadPool.getPoolSize(), threadPool.getMaximumPoolSize(), threadPool.getTaskCount(), threadPool.getCompletedTaskCount()});
         }

         return var8;
      } else {
         var8.run();
         return null;
      }
   }

   private static int fwkGetMaximumHTTPConnectionCountPerHost() {
      int var0 = (Integer)AccessController.doPrivileged(() -> {
         return Integer.getInteger("http.maxConnections", -1);
      });
      return var0 >= 0 ? var0 : 5;
   }

   static {
      threadPool = new ThreadPoolExecutor(20, 20, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new URLLoaderThreadFactory());
      threadPool.allowCoreThreadTimeOut(true);
      byteBufferPool = ByteBufferPool.newInstance(40960);
   }

   private static final class URLLoaderThreadFactory implements ThreadFactory {
      private final ThreadGroup group;
      private final AtomicInteger index;
      private static final Permission modifyThreadGroupPerm = new RuntimePermission("modifyThreadGroup");
      private static final Permission modifyThreadPerm = new RuntimePermission("modifyThread");

      private URLLoaderThreadFactory() {
         this.index = new AtomicInteger(1);
         SecurityManager var1 = System.getSecurityManager();
         this.group = var1 != null ? var1.getThreadGroup() : Thread.currentThread().getThreadGroup();
      }

      public Thread newThread(Runnable var1) {
         return (Thread)AccessController.doPrivileged(() -> {
            Thread var2 = new Thread(this.group, var1, "URL-Loader-" + this.index.getAndIncrement());
            var2.setDaemon(true);
            if (var2.getPriority() != 5) {
               var2.setPriority(5);
            }

            return var2;
         }, (AccessControlContext)null, modifyThreadGroupPerm, modifyThreadPerm);
      }

      // $FF: synthetic method
      URLLoaderThreadFactory(Object var1) {
         this();
      }
   }
}
