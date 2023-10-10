package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class LongMemberValue extends MemberValue {
   int valueIndex;

   public LongMemberValue(int var1, ConstPool var2) {
      super('J', var2);
      this.valueIndex = var1;
   }

   public LongMemberValue(long var1, ConstPool var3) {
      super('J', var3);
      this.setValue(var1);
   }

   public LongMemberValue(ConstPool var1) {
      super('J', var1);
      this.setValue(0L);
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Long(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Long.TYPE;
   }

   public long getValue() {
      return this.cp.getLongInfo(this.valueIndex);
   }

   public void setValue(long var1) {
      this.valueIndex = this.cp.addLongInfo(var1);
   }

   public String toString() {
      return Long.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitLongMemberValue(this);
   }
}
