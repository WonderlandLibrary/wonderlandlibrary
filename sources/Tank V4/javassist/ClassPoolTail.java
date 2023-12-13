package javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

final class ClassPoolTail {
   protected ClassPathList pathList = null;

   public ClassPoolTail() {
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[class path: ");

      for(ClassPathList var2 = this.pathList; var2 != null; var2 = var2.next) {
         var1.append(var2.path.toString());
         var1.append(File.pathSeparatorChar);
      }

      var1.append(']');
      return var1.toString();
   }

   public synchronized ClassPath insertClassPath(ClassPath var1) {
      this.pathList = new ClassPathList(var1, this.pathList);
      return var1;
   }

   public synchronized ClassPath appendClassPath(ClassPath var1) {
      ClassPathList var2 = new ClassPathList(var1, (ClassPathList)null);
      ClassPathList var3 = this.pathList;
      if (var3 == null) {
         this.pathList = var2;
      } else {
         while(true) {
            if (var3.next == null) {
               var3.next = var2;
               break;
            }

            var3 = var3.next;
         }
      }

      return var1;
   }

   public synchronized void removeClassPath(ClassPath var1) {
      ClassPathList var2 = this.pathList;
      if (var2 != null) {
         if (var2.path == var1) {
            this.pathList = var2.next;
         } else {
            while(var2.next != null) {
               if (var2.next.path == var1) {
                  var2.next = var2.next.next;
               } else {
                  var2 = var2.next;
               }
            }
         }
      }

      var1.close();
   }

   public ClassPath appendSystemPath() {
      return this.appendClassPath((ClassPath)(new ClassClassPath()));
   }

   public ClassPath insertClassPath(String var1) throws NotFoundException {
      return this.insertClassPath(makePathObject(var1));
   }

   public ClassPath appendClassPath(String var1) throws NotFoundException {
      return this.appendClassPath(makePathObject(var1));
   }

   private static ClassPath makePathObject(String var0) throws NotFoundException {
      String var1 = var0.toLowerCase();
      if (!var1.endsWith(".jar") && !var1.endsWith(".zip")) {
         int var2 = var0.length();
         if (var2 <= 2 || var0.charAt(var2 - 1) != '*' || var0.charAt(var2 - 2) != '/' && var0.charAt(var2 - 2) != File.separatorChar) {
            return new DirClassPath(var0);
         } else {
            String var3 = var0.substring(0, var2 - 2);
            return new JarDirClassPath(var3);
         }
      } else {
         return new JarClassPath(var0);
      }
   }

   void writeClassfile(String var1, OutputStream var2) throws NotFoundException, IOException, CannotCompileException {
      InputStream var3 = this.openClassfile(var1);
      if (var3 == null) {
         throw new NotFoundException(var1);
      } else {
         copyStream(var3, var2);
         var3.close();
      }
   }

   InputStream openClassfile(String var1) throws NotFoundException {
      ClassPathList var2 = this.pathList;
      InputStream var3 = null;

      NotFoundException var4;
      for(var4 = null; var2 != null; var2 = var2.next) {
         try {
            var3 = var2.path.openClassfile(var1);
         } catch (NotFoundException var6) {
            if (var4 == null) {
               var4 = var6;
            }
         }

         if (var3 != null) {
            return var3;
         }
      }

      if (var4 != null) {
         throw var4;
      } else {
         return null;
      }
   }

   public URL find(String var1) {
      ClassPathList var2 = this.pathList;

      for(URL var3 = null; var2 != null; var2 = var2.next) {
         var3 = var2.path.find(var1);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   public static byte[] readStream(InputStream var0) throws IOException {
      byte[][] var1 = new byte[8][];
      int var2 = 4096;

      for(int var3 = 0; var3 < 8; ++var3) {
         var1[var3] = new byte[var2];
         int var4 = 0;
         boolean var5 = false;

         do {
            int var9 = var0.read(var1[var3], var4, var2 - var4);
            if (var9 < 0) {
               byte[] var6 = new byte[var2 - 4096 + var4];
               int var7 = 0;

               for(int var8 = 0; var8 < var3; ++var8) {
                  System.arraycopy(var1[var8], 0, var6, var7, var7 + 4096);
                  var7 = var7 + var7 + 4096;
               }

               System.arraycopy(var1[var3], 0, var6, var7, var4);
               return var6;
            }

            var4 += var9;
         } while(var4 < var2);

         var2 *= 2;
      }

      throw new IOException("too much data");
   }

   public static void copyStream(InputStream var0, OutputStream var1) throws IOException {
      int var2 = 4096;
      byte[] var3 = null;

      for(int var4 = 0; var4 < 64; ++var4) {
         if (var4 < 8) {
            var2 *= 2;
            var3 = new byte[var2];
         }

         int var5 = 0;
         boolean var6 = false;

         do {
            int var7 = var0.read(var3, var5, var2 - var5);
            if (var7 < 0) {
               var1.write(var3, 0, var5);
               return;
            }

            var5 += var7;
         } while(var5 < var2);

         var1.write(var3);
      }

      throw new IOException("too much data");
   }
}
