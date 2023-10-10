package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class BooleanMemberValue extends MemberValue {
   int valueIndex;

   public BooleanMemberValue(int var1, ConstPool var2) {
      super('Z', var2);
      this.valueIndex = var1;
   }

   public BooleanMemberValue(boolean var1, ConstPool var2) {
      super('Z', var2);
      this.setValue(var1);
   }

   public BooleanMemberValue(ConstPool var1) {
      super('Z', var1);
      this.setValue(false);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Boolean(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Boolean.TYPE;
   }

   public void setValue(boolean var1) {
      this.valueIndex = this.cp.addIntegerInfo(var1 ? 1 : 0);
   }

   public String toString() {
      return this != false ? "true" : "false";
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitBooleanMemberValue(this);
   }
}
