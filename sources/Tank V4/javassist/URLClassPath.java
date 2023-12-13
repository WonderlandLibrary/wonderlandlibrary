package javassist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClassPath implements ClassPath {
   protected String hostname;
   protected int port;
   protected String directory;
   protected String packageName;

   public URLClassPath(String var1, int var2, String var3, String var4) {
      this.hostname = var1;
      this.port = var2;
      this.directory = var3;
      this.packageName = var4;
   }

   public String toString() {
      return this.hostname + ":" + this.port + this.directory;
   }

   public InputStream openClassfile(String var1) {
      try {
         URLConnection var2 = this.openClassfile0(var1);
         if (var2 != null) {
            return var2.getInputStream();
         }
      } catch (IOException var3) {
      }

      return null;
   }

   private URLConnection openClassfile0(String var1) throws IOException {
      if (this.packageName != null && !var1.startsWith(this.packageName)) {
         return null;
      } else {
         String var2 = this.directory + var1.replace('.', '/') + ".class";
         return fetchClass0(this.hostname, this.port, var2);
      }
   }

   public URL find(String var1) {
      try {
         URLConnection var2 = this.openClassfile0(var1);
         InputStream var3 = var2.getInputStream();
         if (var3 != null) {
            var3.close();
            return var2.getURL();
         }
      } catch (IOException var4) {
      }

      return null;
   }

   public void close() {
   }

   public static byte[] fetchClass(String var0, int var1, String var2, String var3) throws IOException {
      URLConnection var5 = fetchClass0(var0, var1, var2 + var3.replace('.', '/') + ".class");
      int var6 = var5.getContentLength();
      InputStream var7 = var5.getInputStream();
      byte[] var4;
      if (var6 <= 0) {
         var4 = ClassPoolTail.readStream(var7);
      } else {
         var4 = new byte[var6];
         int var8 = 0;

         do {
            int var9 = var7.read(var4, var8, var6 - var8);
            if (var9 < 0) {
               throw new IOException("the stream was closed: " + var3);
            }

            var8 += var9;
         } while(var8 < var6);
      }

      var7.close();
      return var4;
   }

   private static URLConnection fetchClass0(String var0, int var1, String var2) throws IOException {
      URL var3;
      try {
         var3 = new URL("http", var0, var1, var2);
      } catch (MalformedURLException var5) {
         throw new IOException("invalid URL?");
      }

      URLConnection var4 = var3.openConnection();
      var4.connect();
      return var4;
   }
}
