package javassist.tools.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

public class Viewer extends ClassLoader {
   private String server;
   private int port;

   public static void main(String[] var0) throws Throwable {
      if (var0.length >= 3) {
         Viewer var1 = new Viewer(var0[0], Integer.parseInt(var0[1]));
         String[] var2 = new String[var0.length - 3];
         System.arraycopy(var0, 3, var2, 0, var0.length - 3);
         var1.run(var0[2], var2);
      } else {
         System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
      }

   }

   public Viewer(String var1, int var2) {
      this.server = var1;
      this.port = var2;
   }

   public String getServer() {
      return this.server;
   }

   public int getPort() {
      return this.port;
   }

   public void run(String var1, String[] var2) throws Throwable {
      Class var3 = this.loadClass(var1);

      try {
         var3.getDeclaredMethod("main", String[].class).invoke((Object)null, var2);
      } catch (InvocationTargetException var5) {
         throw var5.getTargetException();
      }
   }

   protected synchronized Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
      Class var3 = this.findLoadedClass(var1);
      if (var3 == null) {
         var3 = this.findClass(var1);
      }

      if (var3 == null) {
         throw new ClassNotFoundException(var1);
      } else {
         if (var2) {
            this.resolveClass(var3);
         }

         return var3;
      }
   }

   protected Class findClass(String var1) throws ClassNotFoundException {
      Class var2 = null;
      if (var1.startsWith("java.") || var1.startsWith("javax.") || var1.equals("javassist.tools.web.Viewer")) {
         var2 = this.findSystemClass(var1);
      }

      if (var2 == null) {
         try {
            byte[] var3 = this.fetchClass(var1);
            if (var3 != null) {
               var2 = this.defineClass(var1, var3, 0, var3.length);
            }
         } catch (Exception var4) {
         }
      }

      return var2;
   }

   protected byte[] fetchClass(String var1) throws Exception {
      URL var3 = new URL("http", this.server, this.port, "/" + var1.replace('.', '/') + ".class");
      URLConnection var4 = var3.openConnection();
      var4.connect();
      int var5 = var4.getContentLength();
      InputStream var6 = var4.getInputStream();
      byte[] var2;
      if (var5 <= 0) {
         var2 = this.readStream(var6);
      } else {
         var2 = new byte[var5];
         int var7 = 0;

         do {
            int var8 = var6.read(var2, var7, var5 - var7);
            if (var8 < 0) {
               var6.close();
               throw new IOException("the stream was closed: " + var1);
            }

            var7 += var8;
         } while(var7 < var5);
      }

      var6.close();
      return var2;
   }

   private byte[] readStream(InputStream var1) throws IOException {
      byte[] var2 = new byte[4096];
      int var3 = 0;
      int var4 = 0;

      byte[] var5;
      do {
         var3 += var4;
         if (var2.length - var3 <= 0) {
            var5 = new byte[var2.length * 2];
            System.arraycopy(var2, 0, var5, 0, var3);
            var2 = var5;
         }

         var4 = var1.read(var2, var3, var2.length - var3);
      } while(var4 >= 0);

      var5 = new byte[var3];
      System.arraycopy(var2, 0, var5, 0, var3);
      return var5;
   }
}
