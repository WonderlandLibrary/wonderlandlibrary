package javassist.convert;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public class TransformBefore extends TransformCall {
   protected CtClass[] parameterTypes;
   protected int locals;
   protected int maxLocals;
   protected byte[] saveCode;
   protected byte[] loadCode;

   public TransformBefore(Transformer var1, CtMethod var2, CtMethod var3) throws NotFoundException {
      super(var1, var2, var3);
      this.methodDescriptor = var2.getMethodInfo2().getDescriptor();
      this.parameterTypes = var2.getParameterTypes();
      this.locals = 0;
      this.maxLocals = 0;
      this.saveCode = this.loadCode = null;
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
      super.initialize(var1, var2);
      this.locals = 0;
      this.maxLocals = var2.getMaxLocals();
      this.saveCode = this.loadCode = null;
   }

   protected int match(int var1, int var2, CodeIterator var3, int var4, ConstPool var5) throws BadBytecode {
      if (this.newIndex == 0) {
         String var6 = Descriptor.ofParameters(this.parameterTypes) + 'V';
         var6 = Descriptor.insertParameter(this.classname, var6);
         int var7 = var5.addNameAndTypeInfo(this.newMethodname, var6);
         int var8 = var5.addClassInfo(this.newClassname);
         this.newIndex = var5.addMethodrefInfo(var8, var7);
         this.constPool = var5;
      }

      if (this.saveCode == null) {
         this.makeCode(this.parameterTypes, var5);
      }

      return this.match2(var2, var3);
   }

   protected int match2(int var1, CodeIterator var2) throws BadBytecode {
      var2.move(var1);
      var2.insert(this.saveCode);
      var2.insert(this.loadCode);
      int var3 = var2.insertGap(3);
      var2.writeByte(184, var3);
      var2.write16bit(this.newIndex, var3 + 1);
      var2.insert(this.loadCode);
      return var2.next();
   }

   public int extraLocals() {
      return this.locals;
   }

   protected void makeCode(CtClass[] var1, ConstPool var2) {
      Bytecode var3 = new Bytecode(var2, 0, 0);
      Bytecode var4 = new Bytecode(var2, 0, 0);
      int var5 = this.maxLocals;
      int var6 = var1 == null ? 0 : var1.length;
      var4.addAload(var5);
      this.makeCode2(var3, var4, 0, var6, var1, var5 + 1);
      var3.addAstore(var5);
      this.saveCode = var3.get();
      this.loadCode = var4.get();
   }

   private void makeCode2(Bytecode var1, Bytecode var2, int var3, int var4, CtClass[] var5, int var6) {
      if (var3 < var4) {
         int var7 = var2.addLoad(var6, var5[var3]);
         this.makeCode2(var1, var2, var3 + 1, var4, var5, var6 + var7);
         var1.addStore(var6, var5[var3]);
      } else {
         this.locals = var6 - this.maxLocals;
      }

   }
}
