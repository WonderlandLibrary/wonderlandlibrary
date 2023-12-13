package javassist.bytecode.analysis;

import java.util.Iterator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class Analyzer implements Opcode {
   private final SubroutineScanner scanner = new SubroutineScanner();
   private CtClass clazz;
   private Analyzer.ExceptionInfo[] exceptions;
   private Frame[] frames;
   private Subroutine[] subroutines;

   public Frame[] analyze(CtClass var1, MethodInfo var2) throws BadBytecode {
      this.clazz = var1;
      CodeAttribute var3 = var2.getCodeAttribute();
      if (var3 == null) {
         return null;
      } else {
         int var4 = var3.getMaxLocals();
         int var5 = var3.getMaxStack();
         int var6 = var3.getCodeLength();
         CodeIterator var7 = var3.iterator();
         IntQueue var8 = new IntQueue();
         this.exceptions = this.buildExceptionInfo(var2);
         this.subroutines = this.scanner.scan(var2);
         Executor var9 = new Executor(var1.getClassPool(), var2.getConstPool());
         this.frames = new Frame[var6];
         this.frames[var7.lookAhead()] = this.firstFrame(var2, var4, var5);
         var8.add(var7.next());

         while(!var8.isEmpty()) {
            this.analyzeNextEntry(var2, var7, var8, var9);
         }

         return this.frames;
      }
   }

   public Frame[] analyze(CtMethod var1) throws BadBytecode {
      return this.analyze(var1.getDeclaringClass(), var1.getMethodInfo2());
   }

   private void analyzeNextEntry(MethodInfo var1, CodeIterator var2, IntQueue var3, Executor var4) throws BadBytecode {
      int var5 = var3.take();
      var2.move(var5);
      var2.next();
      Frame var6 = this.frames[var5].copy();
      Subroutine var7 = this.subroutines[var5];

      try {
         var4.execute(var1, var5, var2, var6, var7);
      } catch (RuntimeException var10) {
         throw new BadBytecode(var10.getMessage() + "[pos = " + var5 + "]", var10);
      }

      int var8 = var2.byteAt(var5);
      if (var8 == 170) {
         this.mergeTableSwitch(var3, var5, var2, var6);
      } else if (var8 == 171) {
         this.mergeLookupSwitch(var3, var5, var2, var6);
      } else if (var8 == 169) {
         this.mergeRet(var3, var2, var5, var6, var7);
      } else if (Util.isJumpInstruction(var8)) {
         int var9 = Util.getJumpTarget(var5, var2);
         if (Util.isJsr(var8)) {
            this.mergeJsr(var3, this.frames[var5], this.subroutines[var9], var5, this.lookAhead(var2, var5));
         } else if (!Util.isGoto(var8)) {
            this.merge(var3, var6, this.lookAhead(var2, var5));
         }

         this.merge(var3, var6, var9);
      } else if (var8 != 191 && !Util.isReturn(var8)) {
         this.merge(var3, var6, this.lookAhead(var2, var5));
      }

      this.mergeExceptionHandlers(var3, var1, var5, var6);
   }

   private Analyzer.ExceptionInfo[] buildExceptionInfo(MethodInfo var1) {
      ConstPool var2 = var1.getConstPool();
      ClassPool var3 = this.clazz.getClassPool();
      ExceptionTable var4 = var1.getCodeAttribute().getExceptionTable();
      Analyzer.ExceptionInfo[] var5 = new Analyzer.ExceptionInfo[var4.size()];

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         int var7 = var4.catchType(var6);

         Type var8;
         try {
            var8 = var7 == 0 ? Type.THROWABLE : Type.get(var3.get(var2.getClassInfo(var7)));
         } catch (NotFoundException var10) {
            throw new IllegalStateException(var10.getMessage());
         }

         var5[var6] = new Analyzer.ExceptionInfo(var4.startPc(var6), var4.endPc(var6), var4.handlerPc(var6), var8);
      }

      return var5;
   }

   private Frame firstFrame(MethodInfo var1, int var2, int var3) {
      int var4 = 0;
      Frame var5 = new Frame(var2, var3);
      if ((var1.getAccessFlags() & 8) == 0) {
         var5.setLocal(var4++, Type.get(this.clazz));
      }

      CtClass[] var6;
      try {
         var6 = Descriptor.getParameterTypes(var1.getDescriptor(), this.clazz.getClassPool());
      } catch (NotFoundException var9) {
         throw new RuntimeException(var9);
      }

      for(int var7 = 0; var7 < var6.length; ++var7) {
         Type var8 = this.zeroExtend(Type.get(var6[var7]));
         var5.setLocal(var4++, var8);
         if (var8.getSize() == 2) {
            var5.setLocal(var4++, Type.TOP);
         }
      }

      return var5;
   }

   private int getNext(CodeIterator var1, int var2, int var3) throws BadBytecode {
      var1.move(var2);
      var1.next();
      int var4 = var1.lookAhead();
      var1.move(var3);
      var1.next();
      return var4;
   }

   private int lookAhead(CodeIterator var1, int var2) throws BadBytecode {
      if (!var1.hasNext()) {
         throw new BadBytecode("Execution falls off end! [pos = " + var2 + "]");
      } else {
         return var1.lookAhead();
      }
   }

   private void merge(IntQueue var1, Frame var2, int var3) {
      Frame var4 = this.frames[var3];
      boolean var5;
      if (var4 == null) {
         this.frames[var3] = var2.copy();
         var5 = true;
      } else {
         var5 = var4.merge(var2);
      }

      if (var5) {
         var1.add(var3);
      }

   }

   private void mergeExceptionHandlers(IntQueue var1, MethodInfo var2, int var3, Frame var4) {
      for(int var5 = 0; var5 < this.exceptions.length; ++var5) {
         Analyzer.ExceptionInfo var6 = this.exceptions[var5];
         if (var3 >= Analyzer.ExceptionInfo.access$100(var6) && var3 < Analyzer.ExceptionInfo.access$200(var6)) {
            Frame var7 = var4.copy();
            var7.clearStack();
            var7.push(Analyzer.ExceptionInfo.access$300(var6));
            this.merge(var1, var7, Analyzer.ExceptionInfo.access$400(var6));
         }
      }

   }

   private void mergeJsr(IntQueue var1, Frame var2, Subroutine var3, int var4, int var5) throws BadBytecode {
      if (var3 == null) {
         throw new BadBytecode("No subroutine at jsr target! [pos = " + var4 + "]");
      } else {
         Frame var6 = this.frames[var5];
         boolean var7 = false;
         if (var6 == null) {
            var6 = this.frames[var5] = var2.copy();
            var7 = true;
         } else {
            for(int var8 = 0; var8 < var2.localsLength(); ++var8) {
               if (!var3.isAccessed(var8)) {
                  Type var9 = var6.getLocal(var8);
                  Type var10 = var2.getLocal(var8);
                  if (var9 == null) {
                     var6.setLocal(var8, var10);
                     var7 = true;
                  } else {
                     var10 = var9.merge(var10);
                     var6.setLocal(var8, var10);
                     if (!var10.equals(var9) || var10.popChanged()) {
                        var7 = true;
                     }
                  }
               }
            }
         }

         if (!var6.isJsrMerged()) {
            var6.setJsrMerged(true);
            var7 = true;
         }

         if (var7 && var6.isRetMerged()) {
            var1.add(var5);
         }

      }
   }

   private void mergeLookupSwitch(IntQueue var1, int var2, CodeIterator var3, Frame var4) throws BadBytecode {
      int var5 = (var2 & -4) + 4;
      this.merge(var1, var4, var2 + var3.s32bitAt(var5));
      var5 += 4;
      int var6 = var3.s32bitAt(var5);
      int var10000 = var6 * 8;
      var5 += 4;
      int var7 = var10000 + var5;

      for(var5 += 4; var5 < var7; var5 += 8) {
         int var8 = var3.s32bitAt(var5) + var2;
         this.merge(var1, var4, var8);
      }

   }

   private void mergeRet(IntQueue var1, CodeIterator var2, int var3, Frame var4, Subroutine var5) throws BadBytecode {
      if (var5 == null) {
         throw new BadBytecode("Ret on no subroutine! [pos = " + var3 + "]");
      } else {
         Iterator var6 = var5.callers().iterator();

         while(var6.hasNext()) {
            int var7 = (Integer)var6.next();
            int var8 = this.getNext(var2, var7, var3);
            boolean var9 = false;
            Frame var10 = this.frames[var8];
            if (var10 == null) {
               var10 = this.frames[var8] = var4.copyStack();
               var9 = true;
            } else {
               var9 = var10.mergeStack(var4);
            }

            Iterator var11 = var5.accessed().iterator();

            while(var11.hasNext()) {
               int var12 = (Integer)var11.next();
               Type var13 = var10.getLocal(var12);
               Type var14 = var4.getLocal(var12);
               if (var13 != var14) {
                  var10.setLocal(var12, var14);
                  var9 = true;
               }
            }

            if (!var10.isRetMerged()) {
               var10.setRetMerged(true);
               var9 = true;
            }

            if (var9 && var10.isJsrMerged()) {
               var1.add(var8);
            }
         }

      }
   }

   private void mergeTableSwitch(IntQueue var1, int var2, CodeIterator var3, Frame var4) throws BadBytecode {
      int var5 = (var2 & -4) + 4;
      this.merge(var1, var4, var2 + var3.s32bitAt(var5));
      var5 += 4;
      int var6 = var3.s32bitAt(var5);
      var5 += 4;
      int var7 = var3.s32bitAt(var5);
      int var10000 = (var7 - var6 + 1) * 4;
      var5 += 4;

      for(int var8 = var10000 + var5; var5 < var8; var5 += 4) {
         int var9 = var3.s32bitAt(var5) + var2;
         this.merge(var1, var4, var9);
      }

   }

   private Type zeroExtend(Type var1) {
      return var1 != Type.SHORT && var1 != Type.BYTE && var1 != Type.CHAR && var1 != Type.BOOLEAN ? var1 : Type.INTEGER;
   }

   private static class ExceptionInfo {
      private int end;
      private int handler;
      private int start;
      private Type type;

      private ExceptionInfo(int var1, int var2, int var3, Type var4) {
         this.start = var1;
         this.end = var2;
         this.handler = var3;
         this.type = var4;
      }

      ExceptionInfo(int var1, int var2, int var3, Type var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      static int access$100(Analyzer.ExceptionInfo var0) {
         return var0.start;
      }

      static int access$200(Analyzer.ExceptionInfo var0) {
         return var0.end;
      }

      static Type access$300(Analyzer.ExceptionInfo var0) {
         return var0.type;
      }

      static int access$400(Analyzer.ExceptionInfo var0) {
         return var0.handler;
      }
   }
}
