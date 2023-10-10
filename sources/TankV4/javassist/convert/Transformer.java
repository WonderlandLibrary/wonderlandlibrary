package javassist.convert;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public abstract class Transformer implements Opcode {
   private Transformer next;

   public Transformer(Transformer var1) {
      this.next = var1;
   }

   public Transformer getNext() {
      return this.next;
   }

   public void initialize(ConstPool var1, CodeAttribute var2) {
   }

   public void initialize(ConstPool var1, CtClass var2, MethodInfo var3) throws CannotCompileException {
      this.initialize(var1, var3.getCodeAttribute());
   }

   public void clean() {
   }

   public abstract int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws CannotCompileException, BadBytecode;

   public int extraLocals() {
      return 0;
   }

   public int extraStack() {
      return 0;
   }
}
