package org.apache.commons.compress.archivers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public final class Lister {
   private static final ArchiveStreamFactory factory = new ArchiveStreamFactory();

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         usage();
      } else {
         System.out.println("Analysing " + var0[0]);
         File var1 = new File(var0[0]);
         if (!var1.isFile()) {
            System.err.println(var1 + " doesn't exist or is a directory");
         }

         BufferedInputStream var2 = new BufferedInputStream(new FileInputStream(var1));
         ArchiveInputStream var3;
         if (var0.length > 1) {
            var3 = factory.createArchiveInputStream(var0[1], var2);
         } else {
            var3 = factory.createArchiveInputStream(var2);
         }

         System.out.println("Created " + var3.toString());

         ArchiveEntry var4;
         while((var4 = var3.getNextEntry()) != null) {
            System.out.println(var4.getName());
         }

         var3.close();
         var2.close();
      }
   }

   private static void usage() {
      System.out.println("Parameters: archive-name [archive-type]");
   }
}
