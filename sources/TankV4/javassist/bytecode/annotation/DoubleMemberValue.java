package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class DoubleMemberValue extends MemberValue {
   int valueIndex;

   public DoubleMemberValue(int var1, ConstPool var2) {
      super('D', var2);
      this.valueIndex = var1;
   }

   public DoubleMemberValue(double var1, ConstPool var3) {
      super('D', var3);
      this.setValue(var1);
   }

   public DoubleMemberValue(ConstPool var1) {
      super('D', var1);
      this.setValue(0.0D);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Double(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Double.TYPE;
   }

   public double getValue() {
      return this.cp.getDoubleInfo(this.valueIndex);
   }

   public void setValue(double var1) {
      this.valueIndex = this.cp.addDoubleInfo(var1);
   }

   public String toString() {
      return Double.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitDoubleMemberValue(this);
   }
}
