package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ShortMemberValue extends MemberValue {
   int valueIndex;

   public ShortMemberValue(int var1, ConstPool var2) {
      super('S', var2);
      this.valueIndex = var1;
   }

   public ShortMemberValue(short var1, ConstPool var2) {
      super('S', var2);
      this.setValue(var1);
   }

   public ShortMemberValue(ConstPool var1) {
      super('S', var1);
      this.setValue((short)0);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Short(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Short.TYPE;
   }

   public short getValue() {
      return (short)this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(short var1) {
      this.valueIndex = this.cp.addIntegerInfo(var1);
   }

   public String toString() {
      return Short.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitShortMemberValue(this);
   }
}
