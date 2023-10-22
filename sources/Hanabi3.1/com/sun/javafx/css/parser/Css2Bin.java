package com.sun.javafx.css.parser;

import com.sun.javafx.css.Stylesheet;
import java.io.File;
import java.io.IOException;

public final class Css2Bin {
   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         throw new IllegalArgumentException("expected file name as argument");
      } else {
         try {
            String var1 = var0[0];
            String var2 = var0.length > 1 ? var0[1] : var1.substring(0, var1.lastIndexOf(46) + 1).concat("bss");
            convertToBinary(var1, var2);
         } catch (Exception var3) {
            System.err.println(var3.toString());
            var3.printStackTrace(System.err);
            System.exit(-1);
         }

      }
   }

   public static void convertToBinary(String var0, String var1) throws IOException {
      if (var0 != null && var1 != null) {
         if (var0.equals(var1)) {
            throw new IllegalArgumentException("input file and output file cannot be the same");
         } else {
            File var2 = new File(var0);
            File var3 = new File(var1);
            Stylesheet.convertToBinary(var2, var3);
         }
      } else {
         throw new IllegalArgumentException("parameters cannot be null");
      }
   }
}
