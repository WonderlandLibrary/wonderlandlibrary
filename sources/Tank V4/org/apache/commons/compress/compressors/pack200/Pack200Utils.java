package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.jar.Pack200.Unpacker;

public class Pack200Utils {
   private Pack200Utils() {
   }

   public static void normalize(File var0) throws IOException {
      normalize(var0, var0, (Map)null);
   }

   public static void normalize(File var0, Map var1) throws IOException {
      normalize(var0, var0, var1);
   }

   public static void normalize(File var0, File var1) throws IOException {
      normalize(var0, var1, (Map)null);
   }

   public static void normalize(File var0, File var1, Map var2) throws IOException {
      if (var2 == null) {
         var2 = new HashMap();
      }

      ((Map)var2).put("pack.segment.limit", "-1");
      File var3 = File.createTempFile("commons-compress", "pack200normalize");
      var3.deleteOnExit();
      FileOutputStream var4 = new FileOutputStream(var3);
      Object var5 = null;
      Packer var6 = Pack200.newPacker();
      var6.properties().putAll((Map)var2);
      var6.pack(new JarFile(var0), var4);
      var5 = null;
      var4.close();
      var4 = null;
      Unpacker var7 = Pack200.newUnpacker();
      JarOutputStream var10 = new JarOutputStream(new FileOutputStream(var1));
      var7.unpack(var3, (JarOutputStream)var10);
      if (var5 != null) {
         ((JarFile)var5).close();
      }

      if (var10 != null) {
         var10.close();
      }

      var3.delete();
   }
}
