package javassist.expr;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class ConstructorCall extends MethodCall {
   protected ConstructorCall(int var1, CodeIterator var2, CtClass var3, MethodInfo var4) {
      super(var1, var2, var3, var4);
   }

   public String getMethodName() {
      return this.isSuper() ? "super" : "this";
   }

   public CtMethod getMethod() throws NotFoundException {
      throw new NotFoundException("this is a constructor call.  Call getConstructor().");
   }

   public CtConstructor getConstructor() throws NotFoundException {
      return this.getCtClass().getConstructor(this.getSignature());
   }

   public boolean isSuper() {
      return super.isSuper();
   }
}
