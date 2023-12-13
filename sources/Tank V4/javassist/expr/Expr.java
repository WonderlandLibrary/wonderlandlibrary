package javassist.expr;

import java.util.Iterator;
import java.util.LinkedList;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public abstract class Expr implements Opcode {
   int currentPos;
   CodeIterator iterator;
   CtClass thisClass;
   MethodInfo thisMethod;
   boolean edited;
   int maxLocals;
   int maxStack;
   static final String javaLangObject = "java.lang.Object";

   protected Expr(int var1, CodeIterator var2, CtClass var3, MethodInfo var4) {
      this.currentPos = var1;
      this.iterator = var2;
      this.thisClass = var3;
      this.thisMethod = var4;
   }

   public CtClass getEnclosingClass() {
      return this.thisClass;
   }

   protected final ConstPool getConstPool() {
      return this.thisMethod.getConstPool();
   }

   protected final boolean edited() {
      return this.edited;
   }

   protected final int locals() {
      return this.maxLocals;
   }

   protected final int stack() {
      return this.maxStack;
   }

   protected final boolean withinStatic() {
      return (this.thisMethod.getAccessFlags() & 8) != 0;
   }

   public CtBehavior where() {
      MethodInfo var1 = this.thisMethod;
      CtBehavior[] var2 = this.thisClass.getDeclaredBehaviors();

      for(int var3 = var2.length - 1; var3 >= 0; --var3) {
         if (var2[var3].getMethodInfo2() == var1) {
            return var2[var3];
         }
      }

      CtConstructor var5 = this.thisClass.getClassInitializer();
      if (var5 != null && var5.getMethodInfo2() == var1) {
         return var5;
      } else {
         for(int var4 = var2.length - 1; var4 >= 0; --var4) {
            if (this.thisMethod.getName().equals(var2[var4].getMethodInfo2().getName()) && this.thisMethod.getDescriptor().equals(var2[var4].getMethodInfo2().getDescriptor())) {
               return var2[var4];
            }
         }

         throw new RuntimeException("fatal: not found");
      }
   }

   public CtClass[] mayThrow() {
      ClassPool var1 = this.thisClass.getClassPool();
      ConstPool var2 = this.thisMethod.getConstPool();
      LinkedList var3 = new LinkedList();

      int var6;
      int var7;
      try {
         CodeAttribute var4 = this.thisMethod.getCodeAttribute();
         ExceptionTable var5 = var4.getExceptionTable();
         var6 = this.currentPos;
         var7 = var5.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            if (var5.startPc(var8) <= var6 && var6 < var5.endPc(var8)) {
               int var9 = var5.catchType(var8);
               if (var9 > 0) {
                  try {
                     addClass(var3, var1.get(var2.getClassInfo(var9)));
                  } catch (NotFoundException var12) {
                  }
               }
            }
         }
      } catch (NullPointerException var13) {
      }

      ExceptionsAttribute var14 = this.thisMethod.getExceptionsAttribute();
      if (var14 != null) {
         String[] var15 = var14.getExceptions();
         if (var15 != null) {
            var6 = var15.length;

            for(var7 = 0; var7 < var6; ++var7) {
               try {
                  addClass(var3, var1.get(var15[var7]));
               } catch (NotFoundException var11) {
               }
            }
         }
      }

      return (CtClass[])((CtClass[])var3.toArray(new CtClass[var3.size()]));
   }

   private static void addClass(LinkedList var0, CtClass var1) {
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            var0.add(var1);
            return;
         }
      } while(var2.next() != var1);

   }

   public int indexOfBytecode() {
      return this.currentPos;
   }

   public int getLineNumber() {
      return this.thisMethod.getLineNumber(this.currentPos);
   }

   public String getFileName() {
      ClassFile var1 = this.thisClass.getClassFile2();
      return var1 == null ? null : var1.getSourceFile();
   }

   static final boolean checkResultValue(CtClass var0, String var1) throws CannotCompileException {
      boolean var2 = var1.indexOf("$_") >= 0;
      if (!var2 && var0 != CtClass.voidType) {
         throw new CannotCompileException("the resulting value is not stored in $_");
      } else {
         return var2;
      }
   }

   static final void storeStack(CtClass[] var0, boolean var1, int var2, Bytecode var3) {
      storeStack0(0, var0.length, var0, var2 + 1, var3);
      if (var1) {
         var3.addOpcode(1);
      }

      var3.addAstore(var2);
   }

   private static void storeStack0(int var0, int var1, CtClass[] var2, int var3, Bytecode var4) {
      if (var0 < var1) {
         CtClass var5 = var2[var0];
         int var6;
         if (var5 instanceof CtPrimitiveType) {
            var6 = ((CtPrimitiveType)var5).getDataSize();
         } else {
            var6 = 1;
         }

         storeStack0(var0 + 1, var1, var2, var3 + var6, var4);
         var4.addStore(var3, var5);
      }
   }

   public abstract void replace(String var1) throws CannotCompileException;

   public void replace(String var1, ExprEditor var2) throws CannotCompileException {
      this.replace(var1);
      if (var2 != null) {
         this.runEditor(var2, this.iterator);
      }

   }

   protected void replace0(int var1, Bytecode var2, int var3) throws BadBytecode {
      byte[] var4 = var2.get();
      this.edited = true;
      int var5 = var4.length - var3;

      for(int var6 = 0; var6 < var3; ++var6) {
         this.iterator.writeByte(0, var1 + var6);
      }

      if (var5 > 0) {
         var1 = this.iterator.insertGapAt(var1, var5, false).position;
      }

      this.iterator.write(var4, var1);
      this.iterator.insert(var2.getExceptionTable(), var1);
      this.maxLocals = var2.getMaxLocals();
      this.maxStack = var2.getMaxStack();
   }

   protected void runEditor(ExprEditor var1, CodeIterator var2) throws CannotCompileException {
      CodeAttribute var3 = var2.get();
      int var4 = var3.getMaxLocals();
      int var5 = var3.getMaxStack();
      int var6 = this.locals();
      var3.setMaxStack(this.stack());
      var3.setMaxLocals(var6);
      ExprEditor.LoopContext var7 = new ExprEditor.LoopContext(var6);
      int var8 = var2.getCodeLength();
      int var9 = var2.lookAhead();
      var2.move(this.currentPos);
      if (var1.doit(this.thisClass, this.thisMethod, var7, var2, var9)) {
         this.edited = true;
      }

      var2.move(var9 + var2.getCodeLength() - var8);
      var3.setMaxLocals(var4);
      var3.setMaxStack(var5);
      this.maxLocals = var7.maxLocals;
      this.maxStack += var7.maxStack;
   }
}
