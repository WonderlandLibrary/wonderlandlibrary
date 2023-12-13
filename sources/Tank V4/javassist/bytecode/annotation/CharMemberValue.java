package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class CharMemberValue extends MemberValue {
   int valueIndex;

   public CharMemberValue(int var1, ConstPool var2) {
      super('C', var2);
      this.valueIndex = var1;
   }

   public CharMemberValue(char var1, ConstPool var2) {
      super('C', var2);
      this.setValue(var1);
   }

   public CharMemberValue(ConstPool var1) {
      super('C', var1);
      this.setValue('\u0000');
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) {
      return new Character(this.getValue());
   }

   Class getType(ClassLoader var1) {
      return Character.TYPE;
   }

   public char getValue() {
      return (char)this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(char var1) {
      this.valueIndex = this.cp.addIntegerInfo(var1);
   }

   public String toString() {
      return Character.toString(this.getValue());
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitCharMemberValue(this);
   }
}
