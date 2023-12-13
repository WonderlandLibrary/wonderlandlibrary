package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class StringMemberValue extends MemberValue {
   int valueIndex;

   public StringMemberValue(int var1, ConstPool var2) {
      super('s', var2);
      this.valueIndex = var1;
   }

   public StringMemberValue(String var1, ConstPool var2) {
      super('s', var2);
      this.setValue(var1);
   }

   public StringMemberValue(ConstPool var1) {
      super('s', var1);
      this.setValue("");
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return this.getValue();
   }

   Class getType(ClassLoader var1) {
      return String.class;
   }

   public String getValue() {
      return this.cp.getUtf8Info(this.valueIndex);
   }

   public void setValue(String var1) {
      this.valueIndex = this.cp.addUtf8Info(var1);
   }

   public String toString() {
      return "\"" + this.getValue() + "\"";
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitStringMemberValue(this);
   }
}
