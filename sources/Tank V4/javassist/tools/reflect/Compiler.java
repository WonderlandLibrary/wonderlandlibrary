package javassist.tools.reflect;

import java.io.PrintStream;
import javassist.ClassPool;
import javassist.CtClass;

public class Compiler {
   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         help(System.err);
      } else {
         CompiledClass[] var1 = new CompiledClass[var0.length];
         int var2 = parse(var0, var1);
         if (var2 < 1) {
            System.err.println("bad parameter.");
         } else {
            processClasses(var1, var2);
         }
      }
   }

   private static void processClasses(CompiledClass[] var0, int var1) throws Exception {
      Reflection var2 = new Reflection();
      ClassPool var3 = ClassPool.getDefault();
      var2.start(var3);

      int var4;
      for(var4 = 0; var4 < var1; ++var4) {
         CtClass var5 = var3.get(var0[var4].classname);
         if (var0[var4].metaobject == null && var0[var4].classobject == null) {
            System.err.println(var5.getName() + ": not reflective");
         } else {
            String var6;
            if (var0[var4].metaobject == null) {
               var6 = "javassist.tools.reflect.Metaobject";
            } else {
               var6 = var0[var4].metaobject;
            }

            String var7;
            if (var0[var4].classobject == null) {
               var7 = "javassist.tools.reflect.ClassMetaobject";
            } else {
               var7 = var0[var4].classobject;
            }

            if (!var2.makeReflective(var5, var3.get(var6), var3.get(var7))) {
               System.err.println("Warning: " + var5.getName() + " is reflective.  It was not changed.");
            }

            System.err.println(var5.getName() + ": " + var6 + ", " + var7);
         }
      }

      for(var4 = 0; var4 < var1; ++var4) {
         var2.onLoad(var3, var0[var4].classname);
         var3.get(var0[var4].classname).writeFile();
      }

   }

   private static int parse(String[] var0, CompiledClass[] var1) {
      int var2 = -1;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         String var4 = var0[var3];
         CompiledClass var10000;
         if (var4.equals("-m")) {
            if (var2 < 0 || var3 + 1 > var0.length) {
               return -1;
            }

            var10000 = var1[var2];
            ++var3;
            var10000.metaobject = var0[var3];
         } else if (var4.equals("-c")) {
            if (var2 < 0 || var3 + 1 > var0.length) {
               return -1;
            }

            var10000 = var1[var2];
            ++var3;
            var10000.classobject = var0[var3];
         } else {
            if (var4.charAt(0) == '-') {
               return -1;
            }

            CompiledClass var5 = new CompiledClass();
            var5.classname = var4;
            var5.metaobject = null;
            var5.classobject = null;
            ++var2;
            var1[var2] = var5;
         }
      }

      return var2 + 1;
   }

   private static void help(PrintStream var0) {
      var0.println("Usage: java javassist.tools.reflect.Compiler");
      var0.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
   }
}
