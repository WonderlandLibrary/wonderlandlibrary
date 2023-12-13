package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public abstract class Request {
   protected HttpURLConnection connection;
   private boolean connected;
   protected String url;
   private static final int DEFAULT_READ_TIMEOUT = 60000;
   private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

   public Request(String var1, int var2, int var3) {
      try {
         this.url = var1;
         Proxy var4 = RealmsClientConfig.getProxy();
         if (var4 != null) {
            this.connection = (HttpURLConnection)(new URL(var1)).openConnection(var4);
         } else {
            this.connection = (HttpURLConnection)(new URL(var1)).openConnection();
         }

         this.connection.setConnectTimeout(var2);
         this.connection.setReadTimeout(var3);
      } catch (MalformedURLException var5) {
         throw new RealmsHttpException(var5.getMessage(), var5);
      } catch (IOException var6) {
         throw new RealmsHttpException(var6.getMessage(), var6);
      }
   }

   public void cookie(String var1, String var2) {
      cookie(this.connection, var1, var2);
   }

   public static void cookie(HttpURLConnection var0, String var1, String var2) {
      String var3 = var0.getRequestProperty("Cookie");
      if (var3 == null) {
         var0.setRequestProperty("Cookie", var1 + "=" + var2);
      } else {
         var0.setRequestProperty("Cookie", var3 + ";" + var1 + "=" + var2);
      }

   }

   public Request header(String var1, String var2) {
      this.connection.addRequestProperty(var1, var2);
      return this;
   }

   public int responseCode() {
      try {
         this.connect();
         return this.connection.getResponseCode();
      } catch (Exception var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }

   public int getRetryAfterHeader() {
      return getRetryAfterHeader(this.connection);
   }

   public static int getRetryAfterHeader(HttpURLConnection var0) {
      String var1 = var0.getHeaderField("Retry-After");

      try {
         return Integer.valueOf(var1);
      } catch (Exception var3) {
         return 5;
      }
   }

   public String text() {
      try {
         this.connect();
         String var1 = null;
         if (this.responseCode() >= 400) {
            var1 = this.read(this.connection.getErrorStream());
         } else {
            var1 = this.read(this.connection.getInputStream());
         }

         this.dispose();
         return var1;
      } catch (IOException var2) {
         throw new RealmsHttpException(var2.getMessage(), var2);
      }
   }

   private String read(InputStream var1) throws IOException {
      if (var1 == null) {
         return "";
      } else {
         InputStreamReader var2 = new InputStreamReader(var1, "UTF-8");
         StringBuilder var3 = new StringBuilder();

         for(int var4 = var2.read(); var4 != -1; var4 = var2.read()) {
            var3.append((char)var4);
         }

         return var3.toString();
      }
   }

   private void dispose() {
      byte[] var1 = new byte[1024];

      InputStream var3;
      try {
         boolean var2 = false;
         var3 = this.connection.getInputStream();

         while(true) {
            if (var3.read(var1) <= 0) {
               var3.close();
               break;
            }
         }
      } catch (Exception var7) {
         label65: {
            try {
               var3 = this.connection.getErrorStream();
               boolean var4 = false;
               if (var3 == null) {
                  break label65;
               }

               while(true) {
                  if (var3.read(var1) <= 0) {
                     var3.close();
                     break;
                  }
               }
            } catch (IOException var6) {
            }

            if (this.connection != null) {
               this.connection.disconnect();
            }

            return;
         }

         if (this.connection != null) {
            this.connection.disconnect();
         }

         return;
      }

      if (this.connection != null) {
         this.connection.disconnect();
      }

   }

   protected Request connect() {
      if (!this.connected) {
         Request var1 = this.doConnect();
         this.connected = true;
         return var1;
      } else {
         return this;
      }
   }

   protected abstract Request doConnect();

   public static Request get(String var0) {
      return new Request.Get(var0, 5000, 60000);
   }

   public static Request get(String var0, int var1, int var2) {
      return new Request.Get(var0, var1, var2);
   }

   public static Request post(String var0, String var1) {
      return new Request.Post(var0, var1.getBytes(), 5000, 60000);
   }

   public static Request post(String var0, String var1, int var2, int var3) {
      return new Request.Post(var0, var1.getBytes(), var2, var3);
   }

   public static Request delete(String var0) {
      return new Request.Delete(var0, 5000, 60000);
   }

   public static Request put(String var0, String var1) {
      return new Request.Put(var0, var1.getBytes(), 5000, 60000);
   }

   public static Request put(String var0, String var1, int var2, int var3) {
      return new Request.Put(var0, var1.getBytes(), var2, var3);
   }

   public String getHeader(String var1) {
      return getHeader(this.connection, var1);
   }

   public static String getHeader(HttpURLConnection var0, String var1) {
      try {
         return var0.getHeaderField(var1);
      } catch (Exception var3) {
         return "";
      }
   }

   public static class Post extends Request {
      private byte[] content;

      public Post(String var1, byte[] var2, int var3, int var4) {
         super(var1, var3, var4);
         this.content = var2;
      }

      public Request.Post doConnect() {
         try {
            if (this.content != null) {
               this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            }

            this.connection.setDoInput(true);
            this.connection.setDoOutput(true);
            this.connection.setUseCaches(false);
            this.connection.setRequestMethod("POST");
            OutputStream var1 = this.connection.getOutputStream();
            var1.write(this.content);
            var1.flush();
            return this;
         } catch (Exception var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
         }
      }

      public Request doConnect() {
         return this.doConnect();
      }
   }

   public static class Put extends Request {
      private byte[] content;

      public Put(String var1, byte[] var2, int var3, int var4) {
         super(var1, var3, var4);
         this.content = var2;
      }

      public Request.Put doConnect() {
         try {
            if (this.content != null) {
               this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            }

            this.connection.setDoOutput(true);
            this.connection.setDoInput(true);
            this.connection.setRequestMethod("PUT");
            OutputStream var1 = this.connection.getOutputStream();
            var1.write(this.content);
            var1.flush();
            return this;
         } catch (Exception var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
         }
      }

      public Request doConnect() {
         return this.doConnect();
      }
   }

   public static class Get extends Request {
      public Get(String var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public Request.Get doConnect() {
         try {
            this.connection.setDoInput(true);
            this.connection.setDoOutput(true);
            this.connection.setUseCaches(false);
            this.connection.setRequestMethod("GET");
            return this;
         } catch (Exception var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
         }
      }

      public Request doConnect() {
         return this.doConnect();
      }
   }

   public static class Delete extends Request {
      public Delete(String var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      public Request.Delete doConnect() {
         try {
            this.connection.setDoOutput(true);
            this.connection.setRequestMethod("DELETE");
            this.connection.connect();
            return this;
         } catch (Exception var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
         }
      }

      public Request doConnect() {
         return this.doConnect();
      }
   }
}
