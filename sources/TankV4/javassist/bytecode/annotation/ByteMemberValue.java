package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ByteMemberValue extends MemberValue {
   int valueIndex;

   public ByteMemberValue(int var1, ConstPool var2) {
      super('B', var2);
      this.valueIndex = var1;
   }

   public ByteMemberValue(byte var1, ConstPool var2) {
      super('B', var2);
      this.setValue(var1);
   }

   public ByteMemberValue(ConstPool var1) {
      super('B', var1);
      this.setValue((byte)0);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Byte(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Byte.TYPE;
   }

   public byte getValue() {
      return (byte)this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(byte var1) {
      this.valueIndex = this.cp.addIntegerInfo(var1);
   }

   public String toString() {
      return Byte.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitByteMemberValue(this);
   }
}
