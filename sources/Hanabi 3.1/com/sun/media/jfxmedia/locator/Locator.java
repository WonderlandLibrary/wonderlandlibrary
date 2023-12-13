package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Locator {
   public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
   private static final int MAX_CONNECTION_ATTEMPTS = 5;
   private static final long CONNECTION_RETRY_INTERVAL = 1000L;
   private static final int CONNECTION_TIMEOUT = 300000;
   protected String contentType = "application/octet-stream";
   protected long contentLength = -1L;
   protected URI uri;
   private Map connectionProperties;
   private final Object propertyLock = new Object();
   private String uriString = null;
   private String scheme = null;
   private String protocol = null;
   private LocatorCache.CacheReference cacheEntry = null;
   private boolean canBlock = false;
   private CountDownLatch readySignal = new CountDownLatch(1);
   private boolean isIpod;

   private LocatorConnection getConnection(URI var1, String var2) throws MalformedURLException, IOException {
      LocatorConnection var3 = new LocatorConnection();
      HttpURLConnection var4 = (HttpURLConnection)var1.toURL().openConnection();
      var4.setRequestMethod(var2);
      var4.setConnectTimeout(300000);
      var4.setReadTimeout(300000);
      synchronized(this.propertyLock) {
         if (this.connectionProperties != null) {
            Iterator var6 = this.connectionProperties.keySet().iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               Object var8 = this.connectionProperties.get(var7);
               if (var8 instanceof String) {
                  var4.setRequestProperty(var7, (String)var8);
               }
            }
         }
      }

      var3.responseCode = var4.getResponseCode();
      if (var4.getResponseCode() == 200) {
         var3.connection = var4;
      } else {
         closeConnection(var4);
         var3.connection = null;
      }

      return var3;
   }

   private static long getContentLengthLong(URLConnection var0) {
      Method var1 = (Method)AccessController.doPrivileged(() -> {
         try {
            return URLConnection.class.getMethod("getContentLengthLong");
         } catch (NoSuchMethodException var1) {
            return null;
         }
      });

      try {
         return var1 != null ? (Long)var1.invoke(var0) : (long)var0.getContentLength();
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
         return -1L;
      }
   }

   public Locator(URI var1) throws URISyntaxException {
      if (var1 == null) {
         throw new NullPointerException("uri == null!");
      } else {
         this.uriString = var1.toASCIIString();
         this.scheme = var1.getScheme();
         if (this.scheme == null) {
            throw new IllegalArgumentException("uri.getScheme() == null! uri == '" + var1 + "'");
         } else {
            this.scheme = this.scheme.toLowerCase();
            if (this.scheme.equals("jar")) {
               URI var2 = new URI(this.uriString.substring(4));
               this.protocol = var2.getScheme();
               if (this.protocol == null) {
                  throw new IllegalArgumentException("uri.getScheme() == null! subURI == '" + var2 + "'");
               }

               this.protocol = this.protocol.toLowerCase();
            } else {
               this.protocol = this.scheme;
            }

            if (HostUtils.isIOS() && this.protocol.equals("ipod-library")) {
               this.isIpod = true;
            }

            if (!this.isIpod && !MediaManager.canPlayProtocol(this.protocol)) {
               throw new UnsupportedOperationException("Unsupported protocol \"" + this.protocol + "\"");
            } else {
               if (this.protocol.equals("http") || this.protocol.equals("https")) {
                  this.canBlock = true;
               }

               this.uri = var1;
            }
         }
      }
   }

   private InputStream getInputStream(URI var1) throws MalformedURLException, IOException {
      URL var2 = var1.toURL();
      URLConnection var3 = var2.openConnection();
      synchronized(this.propertyLock) {
         if (this.connectionProperties != null) {
            Iterator var5 = this.connectionProperties.keySet().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               Object var7 = this.connectionProperties.get(var6);
               if (var7 instanceof String) {
                  var3.setRequestProperty(var6, (String)var7);
               }
            }
         }
      }

      InputStream var4 = var2.openStream();
      this.contentLength = getContentLengthLong(var3);
      return var4;
   }

   public void cacheMedia() {
      LocatorCache.CacheReference var1 = LocatorCache.locatorCache().fetchURICache(this.uri);
      if (null == var1) {
         InputStream var3;
         try {
            var3 = this.getInputStream(this.uri);
         } catch (Throwable var12) {
            return;
         }

         ByteBuffer var2 = ByteBuffer.allocateDirect((int)this.contentLength);
         byte[] var4 = new byte[8192];
         byte var5 = 0;

         while((long)var5 < this.contentLength) {
            int var6;
            try {
               var6 = var3.read(var4);
            } catch (IOException var11) {
               try {
                  var3.close();
               } catch (Throwable var9) {
               }

               if (Logger.canLog(1)) {
                  Logger.logMsg(1, "IOException trying to preload media: " + var11);
               }

               return;
            }

            if (var6 == -1) {
               break;
            }

            var2.put(var4, 0, var6);
         }

         try {
            var3.close();
         } catch (Throwable var10) {
         }

         this.cacheEntry = LocatorCache.locatorCache().registerURICache(this.uri, var2, this.contentType);
         this.canBlock = false;
      }

   }

   public boolean canBlock() {
      return this.canBlock;
   }

   public void init() throws URISyntaxException, IOException, FileNotFoundException {
      try {
         int var1 = this.uriString.indexOf("/");
         if (var1 != -1 && this.uriString.charAt(var1 + 1) != '/') {
            if (this.protocol.equals("file")) {
               this.uriString = this.uriString.replaceFirst("/", "///");
            } else if (this.protocol.equals("http") || this.protocol.equals("https")) {
               this.uriString = this.uriString.replaceFirst("/", "//");
            }
         }

         if (System.getProperty("os.name").toLowerCase().indexOf("win") == -1 && this.protocol.equals("file")) {
            int var2 = this.uriString.indexOf("/~/");
            if (var2 != -1) {
               this.uriString = this.uriString.substring(0, var2) + System.getProperty("user.home") + this.uriString.substring(var2 + 2);
            }
         }

         this.uri = new URI(this.uriString);
         this.cacheEntry = LocatorCache.locatorCache().fetchURICache(this.uri);
         if (null != this.cacheEntry) {
            this.contentType = this.cacheEntry.getMIMEType();
            this.contentLength = (long)this.cacheEntry.getBuffer().capacity();
            if (Logger.canLog(1)) {
               Logger.logMsg(1, "Locator init cache hit:\n    uri " + this.uri + "\n    type " + this.contentType + "\n    length " + this.contentLength);
            }

            return;
         }

         boolean var20 = false;
         boolean var3 = false;
         boolean var4 = true;
         if (this.isIpod) {
            this.contentType = MediaUtils.filenameToContentType(this.uriString);
         } else {
            for(int var5 = 0; var5 < 5; ++var5) {
               try {
                  if (!this.scheme.equals("http") && !this.scheme.equals("https")) {
                     if (this.scheme.equals("file") || this.scheme.equals("jar")) {
                        InputStream var21 = this.getInputStream(this.uri);
                        var21.close();
                        var20 = true;
                        this.contentType = MediaUtils.filenameToContentType(this.uriString);
                     }
                  } else {
                     LocatorConnection var6 = this.getConnection(this.uri, "HEAD");
                     if (var6 == null || var6.connection == null) {
                        var6 = this.getConnection(this.uri, "GET");
                     }

                     if (var6 != null && var6.connection != null) {
                        var20 = true;
                        this.contentType = var6.connection.getContentType();
                        this.contentLength = getContentLengthLong(var6.connection);
                        closeConnection(var6.connection);
                        var6.connection = null;
                     } else if (var6 != null && var6.responseCode == 404) {
                        var3 = true;
                     }
                  }

                  if (var20) {
                     if ("audio/x-wav".equals(this.contentType)) {
                        this.contentType = this.getContentTypeFromFileSignature(this.uri);
                        if (!MediaManager.canPlayContentType(this.contentType)) {
                           var4 = false;
                        }
                        break;
                     }

                     if (this.contentType != null && MediaManager.canPlayContentType(this.contentType)) {
                        break;
                     }

                     this.contentType = MediaUtils.filenameToContentType(this.uriString);
                     if ("application/octet-stream".equals(this.contentType)) {
                        this.contentType = this.getContentTypeFromFileSignature(this.uri);
                     }

                     if (!MediaManager.canPlayContentType(this.contentType)) {
                        var4 = false;
                     }
                     break;
                  }
               } catch (IOException var15) {
                  if (var5 + 1 >= 5) {
                     throw var15;
                  }
               }

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var14) {
               }
            }
         }

         if (!this.isIpod && !var20) {
            if (var3) {
               throw new FileNotFoundException("media is unavailable (" + this.uri.toString() + ")");
            }

            throw new IOException("could not connect to media (" + this.uri.toString() + ")");
         }

         if (!var4) {
            throw new MediaException("media type not supported (" + this.uri.toString() + ")");
         }
      } catch (FileNotFoundException var16) {
         throw var16;
      } catch (IOException var17) {
         throw var17;
      } catch (MediaException var18) {
         throw var18;
      } finally {
         this.readySignal.countDown();
      }

   }

   public String getContentType() {
      try {
         this.readySignal.await();
      } catch (Exception var2) {
      }

      return this.contentType;
   }

   public String getProtocol() {
      return this.protocol;
   }

   public long getContentLength() {
      try {
         this.readySignal.await();
      } catch (Exception var2) {
      }

      return this.contentLength;
   }

   public void waitForReadySignal() {
      try {
         this.readySignal.await();
      } catch (Exception var2) {
      }

   }

   public URI getURI() {
      return this.uri;
   }

   public String toString() {
      return LocatorCache.locatorCache().isCached(this.uri) ? "{LocatorURI uri: " + this.uri.toString() + " (media cached)}" : "{LocatorURI uri: " + this.uri.toString() + "}";
   }

   public String getStringLocation() {
      return this.uri.toString();
   }

   public void setConnectionProperty(String var1, Object var2) {
      synchronized(this.propertyLock) {
         if (this.connectionProperties == null) {
            this.connectionProperties = new TreeMap();
         }

         this.connectionProperties.put(var1, var2);
      }
   }

   public ConnectionHolder createConnectionHolder() throws IOException {
      if (null != this.cacheEntry) {
         if (Logger.canLog(1)) {
            Logger.logMsg(1, "Locator.createConnectionHolder: media cached, creating memory connection holder");
         }

         return ConnectionHolder.createMemoryConnectionHolder(this.cacheEntry.getBuffer());
      } else {
         ConnectionHolder var1;
         if ("file".equals(this.scheme)) {
            var1 = ConnectionHolder.createFileConnectionHolder(this.uri);
         } else if (!this.uri.toString().endsWith(".m3u8") && !this.uri.toString().endsWith(".m3u")) {
            synchronized(this.propertyLock) {
               var1 = ConnectionHolder.createURIConnectionHolder(this.uri, this.connectionProperties);
            }
         } else {
            var1 = ConnectionHolder.createHLSConnectionHolder(this.uri);
         }

         return var1;
      }
   }

   private String getContentTypeFromFileSignature(URI var1) throws MalformedURLException, IOException {
      InputStream var2 = this.getInputStream(var1);
      byte[] var3 = new byte[22];
      int var4 = var2.read(var3);
      var2.close();
      return MediaUtils.fileSignatureToContentType(var3, var4);
   }

   static void closeConnection(URLConnection var0) {
      if (var0 instanceof HttpURLConnection) {
         HttpURLConnection var1 = (HttpURLConnection)var0;

         try {
            if (var1.getResponseCode() < 400 && var1.getInputStream() != null) {
               var1.getInputStream().close();
            }
         } catch (IOException var5) {
            try {
               if (var1.getErrorStream() != null) {
                  var1.getErrorStream().close();
               }
            } catch (IOException var4) {
            }
         }
      }

   }

   private static class LocatorConnection {
      public HttpURLConnection connection;
      public int responseCode;

      private LocatorConnection() {
         this.connection = null;
         this.responseCode = 200;
      }

      // $FF: synthetic method
      LocatorConnection(Object var1) {
         this();
      }
   }
}
