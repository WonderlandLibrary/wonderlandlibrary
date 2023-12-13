package javassist.tools;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.analysis.FramePrinter;

public class framedump {
   private framedump() {
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         System.err.println("Usage: java javassist.tools.framedump <fully-qualified class name>");
      } else {
         ClassPool var1 = ClassPool.getDefault();
         CtClass var2 = var1.get(var0[0]);
         System.out.println("Frame Dump of " + var2.getName() + ":");
         FramePrinter.print(var2, System.out);
      }
   }
}
