package com.sun.webkit.network;

import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLHandshakeException;

final class URLLoader extends URLLoaderBase implements Runnable {
   private static final Logger logger = Logger.getLogger(URLLoader.class.getName());
   private static final int MAX_BUF_COUNT = 3;
   private static final String GET = "GET";
   private static final String HEAD = "HEAD";
   private static final String DELETE = "DELETE";
   private final WebPage webPage;
   private final ByteBufferPool byteBufferPool;
   private final boolean asynchronous;
   private String url;
   private String method;
   private final String headers;
   private FormDataElement[] formDataElements;
   private final long data;
   private volatile boolean canceled = false;

   URLLoader(WebPage var1, ByteBufferPool var2, boolean var3, String var4, String var5, String var6, FormDataElement[] var7, long var8) {
      this.webPage = var1;
      this.byteBufferPool = var2;
      this.asynchronous = var3;
      this.url = var4;
      this.method = var5;
      this.headers = var6;
      this.formDataElements = var7;
      this.data = var8;
   }

   public void fwkCancel() {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("data: [0x%016X]", this.data));
      }

      this.canceled = true;
   }

   public void run() {
      AccessController.doPrivileged(() -> {
         this.doRun();
         return null;
      }, this.webPage.getAccessControlContext());
   }

   private void doRun() {
      Object var1 = null;
      byte var2 = 0;

      try {
         boolean var3 = true;
         boolean var4 = true;

         while(true) {
            String var5 = this.url;
            if (this.url.startsWith("file:")) {
               int var6 = this.url.indexOf(63);
               if (var6 != -1) {
                  var5 = this.url.substring(0, var6);
               }
            }

            URL var38 = URLs.newURL(var5);
            workaround7177996(var38);
            URLConnection var7 = var38.openConnection();
            this.prepareConnection(var7);

            try {
               this.sendRequest(var7, var3);
               this.receiveResponse(var7);
               break;
            } catch (HttpRetryException var24) {
               if (!var3) {
                  throw var24;
               }

               var3 = false;
            } catch (SocketException var25) {
               if (!"Connection reset".equals(var25.getMessage()) || !var4) {
                  throw var25;
               }

               var4 = false;
            } finally {
               close(var7);
            }
         }
      } catch (MalformedURLException var27) {
         var1 = var27;
         var2 = 2;
      } catch (AccessControlException var28) {
         var1 = var28;
         var2 = 8;
      } catch (UnknownHostException var29) {
         var1 = var29;
         var2 = 1;
      } catch (NoRouteToHostException var30) {
         var1 = var30;
         var2 = 6;
      } catch (ConnectException var31) {
         var1 = var31;
         var2 = 4;
      } catch (SocketException var32) {
         var1 = var32;
         var2 = 5;
      } catch (SSLHandshakeException var33) {
         var1 = var33;
         var2 = 3;
      } catch (SocketTimeoutException var34) {
         var1 = var34;
         var2 = 7;
      } catch (InvalidResponseException var35) {
         var1 = var35;
         var2 = 9;
      } catch (FileNotFoundException var36) {
         var1 = var36;
         var2 = 11;
      } catch (Throwable var37) {
         var1 = var37;
         var2 = 99;
      }

      if (var1 != null) {
         if (var2 == 99) {
            logger.log(Level.WARNING, "Unexpected error", (Throwable)var1);
         } else {
            logger.log(Level.FINEST, "Load error", (Throwable)var1);
         }

         this.didFail(var2, ((Throwable)var1).getMessage());
      }

   }

   private static void workaround7177996(URL var0) throws FileNotFoundException {
      if (var0.getProtocol().equals("file")) {
         String var1 = var0.getHost();
         if (var1 != null && !var1.equals("") && !var1.equals("~") && !var1.equalsIgnoreCase("localhost")) {
            if (System.getProperty("os.name").startsWith("Windows")) {
               String var2 = null;

               try {
                  var2 = URLDecoder.decode(var0.getPath(), "UTF-8");
               } catch (UnsupportedEncodingException var4) {
               }

               var2 = var2.replace('/', '\\');
               var2 = var2.replace('|', ':');
               File var3 = new File("\\\\" + var1 + var2);
               if (!var3.exists()) {
                  throw new FileNotFoundException("File not found: " + var0);
               }
            } else {
               throw new FileNotFoundException("File not found: " + var0);
            }
         }
      }
   }

   private void prepareConnection(URLConnection var1) throws IOException {
      var1.setConnectTimeout(30000);
      var1.setReadTimeout(3600000);
      var1.setUseCaches(false);
      Locale var2 = Locale.getDefault();
      String var3 = "";
      if (!var2.equals(Locale.US) && !var2.equals(Locale.ENGLISH)) {
         var3 = var2.getCountry().isEmpty() ? var2.getLanguage() + "," : var2.getLanguage() + "-" + var2.getCountry() + ",";
      }

      var1.setRequestProperty("Accept-Language", var3.toLowerCase() + "en-us;q=0.8,en;q=0.7");
      var1.setRequestProperty("Accept-Encoding", "gzip");
      var1.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      if (this.headers != null && this.headers.length() > 0) {
         String[] var4 = this.headers.split("\n");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            int var8 = var7.indexOf(58);
            if (var8 > 0) {
               var1.addRequestProperty(var7.substring(0, var8), var7.substring(var8 + 2));
            }
         }
      }

      if (var1 instanceof HttpURLConnection) {
         HttpURLConnection var9 = (HttpURLConnection)var1;
         var9.setRequestMethod(this.method);
         var9.setInstanceFollowRedirects(false);
      }

   }

   private void sendRequest(URLConnection var1, boolean var2) throws IOException {
      OutputStream var3 = null;
      boolean var32 = false;

      try {
         var32 = true;
         long var4 = 0L;
         boolean var6 = this.formDataElements != null && var1 instanceof HttpURLConnection && !this.method.equals("DELETE");
         boolean var7 = this.method.equals("GET") || this.method.equals("HEAD");
         int var9;
         if (!var6) {
            if (!var7 && var1 instanceof HttpURLConnection) {
               var1.setRequestProperty("Content-Length", "0");
            }
         } else {
            var1.setDoOutput(true);
            FormDataElement[] var8 = this.formDataElements;
            var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               FormDataElement var11 = var8[var10];
               var11.open();
               var4 += var11.getSize();
            }

            if (var2) {
               HttpURLConnection var43 = (HttpURLConnection)var1;
               if (var4 <= 2147483647L) {
                  var43.setFixedLengthStreamingMode((int)var4);
               } else {
                  var43.setChunkedStreamingMode(0);
               }
            }
         }

         int var44 = var7 ? 3 : 1;
         var1.setConnectTimeout(var1.getConnectTimeout() / var44);
         var9 = 0;

         while(!this.canceled) {
            try {
               var1.connect();
               break;
            } catch (SocketTimeoutException var37) {
               ++var9;
               if (var9 >= var44) {
                  throw var37;
               }
            } catch (IllegalArgumentException var38) {
               throw new MalformedURLException(this.url);
            }
         }

         if (var6) {
            var3 = var1.getOutputStream();
            byte[] var45 = new byte[4096];
            long var46 = 0L;
            FormDataElement[] var13 = this.formDataElements;
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               FormDataElement var16 = var13[var15];
               InputStream var17 = var16.getInputStream();

               int var18;
               while((var18 = var17.read(var45)) > 0) {
                  var3.write(var45, 0, var18);
                  var46 += (long)var18;
                  this.didSendData(var46, var4);
               }

               var16.close();
            }

            var3.flush();
            var3.close();
            var3 = null;
            var32 = false;
         } else {
            var32 = false;
         }
      } finally {
         if (var32) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var34) {
               }
            }

            if (this.formDataElements != null && var1 instanceof HttpURLConnection) {
               FormDataElement[] var20 = this.formDataElements;
               int var21 = var20.length;

               for(int var22 = 0; var22 < var21; ++var22) {
                  FormDataElement var23 = var20[var22];

                  try {
                     var23.close();
                  } catch (IOException var33) {
                  }
               }
            }

         }
      }

      if (var3 != null) {
         try {
            var3.close();
         } catch (IOException var36) {
         }
      }

      if (this.formDataElements != null && var1 instanceof HttpURLConnection) {
         FormDataElement[] var40 = this.formDataElements;
         int var5 = var40.length;

         for(int var41 = 0; var41 < var5; ++var41) {
            FormDataElement var42 = var40[var41];

            try {
               var42.close();
            } catch (IOException var35) {
            }
         }
      }

   }

   private void receiveResponse(URLConnection var1) throws IOException, InterruptedException {
      if (!this.canceled) {
         InputStream var2 = null;
         if (var1 instanceof HttpURLConnection) {
            HttpURLConnection var3 = (HttpURLConnection)var1;
            int var4 = var3.getResponseCode();
            if (var4 == -1) {
               throw new InvalidResponseException();
            }

            if (this.canceled) {
               return;
            }

            switch (var4) {
               case 301:
               case 302:
               case 303:
               case 307:
                  this.willSendRequest((URLConnection)var1);
               case 305:
               case 306:
               default:
                  if (var4 >= 400 && !this.method.equals("HEAD")) {
                     var2 = var3.getErrorStream();
                  }
                  break;
               case 304:
                  this.didReceiveResponse((URLConnection)var1);
                  this.didFinishLoading();
                  return;
            }
         }

         if (this.url.startsWith("ftp:") || this.url.startsWith("ftps:")) {
            boolean var22 = false;
            boolean var23 = false;
            String var5 = ((URLConnection)var1).getURL().getPath();
            if (var5 != null && !var5.isEmpty() && !var5.endsWith("/") && !var5.contains(";type=d")) {
               String var6 = ((URLConnection)var1).getContentType();
               if ("text/plain".equalsIgnoreCase(var6) || "text/html".equalsIgnoreCase(var6)) {
                  var22 = true;
                  var23 = true;
               }
            } else {
               var22 = true;
            }

            if (var22) {
               var1 = new DirectoryURLConnection((URLConnection)var1, var23);
            }
         }

         if (this.url.startsWith("file:") && "text/plain".equals(((URLConnection)var1).getContentType()) && ((URLConnection)var1).getHeaderField("content-length") == null) {
            var1 = new DirectoryURLConnection((URLConnection)var1);
         }

         this.didReceiveResponse((URLConnection)var1);
         if (this.method.equals("HEAD")) {
            this.didFinishLoading();
         } else {
            Object var24 = null;

            try {
               var24 = var2 == null ? ((URLConnection)var1).getInputStream() : var2;
            } catch (HttpRetryException var20) {
               throw var20;
            } catch (IOException var21) {
               if (logger.isLoggable(Level.FINE)) {
                  logger.log(Level.FINE, String.format("Exception caught: [%s], %s", var21.getClass().getSimpleName(), var21.getMessage()));
               }
            }

            String var25 = ((URLConnection)var1).getContentEncoding();
            if (var24 != null) {
               try {
                  if ("gzip".equalsIgnoreCase(var25)) {
                     var24 = new GZIPInputStream((InputStream)var24);
                  } else if ("deflate".equalsIgnoreCase(var25)) {
                     var24 = new InflaterInputStream((InputStream)var24);
                  }
               } catch (IOException var19) {
                  if (logger.isLoggable(Level.FINE)) {
                     logger.log(Level.FINE, String.format("Exception caught: [%s], %s", var19.getClass().getSimpleName(), var19.getMessage()));
                  }
               }
            }

            ByteBufferAllocator var26 = this.byteBufferPool.newAllocator(3);
            ByteBuffer var27 = null;

            try {
               if (var24 != null) {
                  byte[] var7 = new byte[8192];

                  while(!this.canceled) {
                     int var8;
                     try {
                        var8 = ((InputStream)var24).read(var7);
                     } catch (EOFException var17) {
                        var8 = -1;
                     }

                     if (var8 == -1) {
                        break;
                     }

                     if (var27 == null) {
                        var27 = var26.allocate();
                     }

                     int var9 = var27.remaining();
                     if (var8 < var9) {
                        var27.put(var7, 0, var8);
                     } else {
                        var27.put(var7, 0, var9);
                        var27.flip();
                        this.didReceiveData(var27, var26);
                        var27 = null;
                        int var10 = var8 - var9;
                        if (var10 > 0) {
                           var27 = var26.allocate();
                           var27.put(var7, var9, var10);
                        }
                     }
                  }
               }

               if (!this.canceled) {
                  if (var27 != null && var27.position() > 0) {
                     var27.flip();
                     this.didReceiveData(var27, var26);
                     var27 = null;
                  }

                  this.didFinishLoading();
               }
            } finally {
               if (var27 != null) {
                  var26.release(var27);
               }

            }

         }
      }
   }

   private static void close(URLConnection var0) {
      if (var0 instanceof HttpURLConnection) {
         InputStream var1 = ((HttpURLConnection)var0).getErrorStream();
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var4) {
            }
         }
      }

      try {
         var0.getInputStream().close();
      } catch (IOException var3) {
      }

   }

   private void didSendData(long var1, long var3) {
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyDidSendData(var1, var3);
         }

      });
   }

   private void notifyDidSendData(long var1, long var3) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("totalBytesSent: [%d], totalBytesToBeSent: [%d], data: [0x%016X]", var1, var3, this.data));
      }

      twkDidSendData(var1, var3, this.data);
   }

   private void willSendRequest(URLConnection var1) throws InterruptedException {
      int var2 = extractStatus(var1);
      String var3 = var1.getContentType();
      String var4 = extractContentEncoding(var1);
      long var5 = extractContentLength(var1);
      String var7 = extractHeaders(var1);
      String var8 = adjustUrlForWebKit(this.url);
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyWillSendRequest(var2, var3, var4, var5, var7, var8);
         }

      });
   }

   private void notifyWillSendRequest(int var1, String var2, String var3, long var4, String var6, String var7) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", var1, var2, var3, var4, var7, this.data, Util.formatHeaders(var6)));
      }

      twkWillSendRequest(var1, var2, var3, var4, var6, var7, this.data);
   }

   private void didReceiveResponse(URLConnection var1) {
      int var2 = extractStatus(var1);
      String var3 = var1.getContentType();
      String var4 = extractContentEncoding(var1);
      long var5 = extractContentLength(var1);
      String var7 = extractHeaders(var1);
      String var8 = adjustUrlForWebKit(this.url);
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyDidReceiveResponse(var2, var3, var4, var5, var7, var8);
         }

      });
   }

   private void notifyDidReceiveResponse(int var1, String var2, String var3, long var4, String var6, String var7) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", var1, var2, var3, var4, var7, this.data, Util.formatHeaders(var6)));
      }

      twkDidReceiveResponse(var1, var2, var3, var4, var6, var7, this.data);
   }

   private void didReceiveData(ByteBuffer var1, ByteBufferAllocator var2) {
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyDidReceiveData(var1, var1.position(), var1.remaining());
         }

         var2.release(var1);
      });
   }

   private void notifyDidReceiveData(ByteBuffer var1, int var2, int var3) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("byteBuffer: [%s], position: [%s], remaining: [%s], data: [0x%016X]", var1, var2, var3, this.data));
      }

      twkDidReceiveData(var1, var2, var3, this.data);
   }

   private void didFinishLoading() {
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyDidFinishLoading();
         }

      });
   }

   private void notifyDidFinishLoading() {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("data: [0x%016X]", this.data));
      }

      twkDidFinishLoading(this.data);
   }

   private void didFail(int var1, String var2) {
      String var3 = adjustUrlForWebKit(this.url);
      this.callBack(() -> {
         if (!this.canceled) {
            this.notifyDidFail(var1, var3, var2);
         }

      });
   }

   private void notifyDidFail(int var1, String var2, String var3) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, String.format("errorCode: [%d], url: [%s], message: [%s], data: [0x%016X]", var1, var2, var3, this.data));
      }

      twkDidFail(var1, var2, var3, this.data);
   }

   private void callBack(Runnable var1) {
      if (this.asynchronous) {
         Invoker.getInvoker().invokeOnEventThread(var1);
      } else {
         var1.run();
      }

   }

   private static int extractStatus(URLConnection var0) {
      int var1 = 0;
      if (var0 instanceof HttpURLConnection) {
         try {
            var1 = ((HttpURLConnection)var0).getResponseCode();
         } catch (IOException var3) {
         }
      }

      return var1;
   }

   private static String extractContentEncoding(URLConnection var0) {
      String var1 = var0.getContentEncoding();
      if ("gzip".equalsIgnoreCase(var1) || "deflate".equalsIgnoreCase(var1)) {
         var1 = null;
         String var2 = var0.getContentType();
         if (var2 != null) {
            int var3 = var2.indexOf("charset=");
            if (var3 >= 0) {
               var1 = var2.substring(var3 + 8);
               var3 = var1.indexOf(";");
               if (var3 > 0) {
                  var1 = var1.substring(0, var3);
               }
            }
         }
      }

      return var1;
   }

   private static long extractContentLength(URLConnection var0) {
      try {
         return Long.parseLong(var0.getHeaderField("content-length"));
      } catch (Exception var2) {
         return -1L;
      }
   }

   private static String extractHeaders(URLConnection var0) {
      StringBuilder var1 = new StringBuilder();
      Map var2 = var0.getHeaderFields();
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         String var5 = (String)var4.getKey();
         List var6 = (List)var4.getValue();
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            var1.append(var5 != null ? var5 : "");
            var1.append(':').append(var8).append('\n');
         }
      }

      return var1.toString();
   }

   private static String adjustUrlForWebKit(String var0) {
      try {
         var0 = Util.adjustUrlForWebKit(var0);
      } catch (Exception var2) {
      }

      return var0;
   }

   private static final class InvalidResponseException extends IOException {
      private InvalidResponseException() {
         super("Invalid server response");
      }

      // $FF: synthetic method
      InvalidResponseException(Object var1) {
         this();
      }
   }
}
