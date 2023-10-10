package javassist.bytecode.analysis;

import java.io.PrintStream;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

public final class FramePrinter {
   private final PrintStream stream;

   public FramePrinter(PrintStream var1) {
      this.stream = var1;
   }

   public static void print(CtClass var0, PrintStream var1) {
      (new FramePrinter(var1)).print(var0);
   }

   public void print(CtClass var1) {
      CtMethod[] var2 = var1.getDeclaredMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.print(var2[var3]);
      }

   }

   private String getMethodString(CtMethod var1) {
      try {
         return Modifier.toString(var1.getModifiers()) + " " + var1.getReturnType().getName() + " " + var1.getName() + Descriptor.toString(var1.getSignature()) + ";";
      } catch (NotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void print(CtMethod var1) {
      this.stream.println("\n" + this.getMethodString(var1));
      MethodInfo var2 = var1.getMethodInfo2();
      ConstPool var3 = var2.getConstPool();
      CodeAttribute var4 = var2.getCodeAttribute();
      if (var4 != null) {
         Frame[] var5;
         try {
            var5 = (new Analyzer()).analyze(var1.getDeclaringClass(), var2);
         } catch (BadBytecode var11) {
            throw new RuntimeException(var11);
         }

         int var6 = String.valueOf(var4.getCodeLength()).length();
         CodeIterator var7 = var4.iterator();

         while(var7.hasNext()) {
            int var8;
            try {
               var8 = var7.next();
            } catch (BadBytecode var10) {
               throw new RuntimeException(var10);
            }

            this.stream.println(var8 + ": " + InstructionPrinter.instructionString(var7, var8, var3));
            this.addSpacing(var6 + 3);
            Frame var9 = var5[var8];
            if (var9 == null) {
               this.stream.println("--DEAD CODE--");
            } else {
               this.printStack(var9);
               this.addSpacing(var6 + 3);
               this.printLocals(var9);
            }
         }

      }
   }

   private void printStack(Frame var1) {
      this.stream.print("stack [");
      int var2 = var1.getTopIndex();

      for(int var3 = 0; var3 <= var2; ++var3) {
         if (var3 > 0) {
            this.stream.print(", ");
         }

         Type var4 = var1.getStack(var3);
         this.stream.print(var4);
      }

      this.stream.println("]");
   }

   private void printLocals(Frame var1) {
      this.stream.print("locals [");
      int var2 = var1.localsLength();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var3 > 0) {
            this.stream.print(", ");
         }

         Type var4 = var1.getLocal(var3);
         this.stream.print(var4 == null ? "empty" : var4.toString());
      }

      this.stream.println("]");
   }

   private void addSpacing(int var1) {
      while(var1-- > 0) {
         this.stream.print(' ');
      }

   }
}
