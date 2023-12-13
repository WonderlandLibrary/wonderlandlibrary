package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class IntegerMemberValue extends MemberValue {
   int valueIndex;

   public IntegerMemberValue(int var1, ConstPool var2) {
      super('I', var2);
      this.valueIndex = var1;
   }

   public IntegerMemberValue(ConstPool var1, int var2) {
      super('I', var1);
      this.setValue(var2);
   }

   public IntegerMemberValue(ConstPool var1) {
      super('I', var1);
      this.setValue(0);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Integer(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Integer.TYPE;
   }

   public int getValue() {
      return this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(int var1) {
      this.valueIndex = this.cp.addIntegerInfo(var1);
   }

   public String toString() {
      return Integer.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitIntegerMemberValue(this);
   }
}
