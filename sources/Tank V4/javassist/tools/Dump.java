package javassist.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ClassFilePrinter;

public class Dump {
   private Dump() {
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.err.println("Usage: java Dump <class file name>");
      } else {
         DataInputStream var1 = new DataInputStream(new FileInputStream(var0[0]));
         ClassFile var2 = new ClassFile(var1);
         PrintWriter var3 = new PrintWriter(System.out, true);
         var3.println("*** constant pool ***");
         var2.getConstPool().print(var3);
         var3.println();
         var3.println("*** members ***");
         ClassFilePrinter.print(var2, var3);
      }
   }
}
