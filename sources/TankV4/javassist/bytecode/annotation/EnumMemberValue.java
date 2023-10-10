package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public class EnumMemberValue extends MemberValue {
   int typeIndex;
   int valueIndex;

   public EnumMemberValue(int var1, int var2, ConstPool var3) {
      super('e', var3);
      this.typeIndex = var1;
      this.valueIndex = var2;
   }

   public EnumMemberValue(ConstPool var1) {
      super('e', var1);
      this.typeIndex = this.valueIndex = 0;
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException {
      try {
         return this.getType(var1).getField(this.getValue()).get((Object)null);
      } catch (NoSuchFieldException var5) {
         throw new ClassNotFoundException(this.getType() + "." + this.getValue());
      } catch (IllegalAccessException var6) {
         throw new ClassNotFoundException(this.getType() + "." + this.getValue());
      }
   }

   Class getType(ClassLoader var1) throws ClassNotFoundException {
      return loadClass(var1, this.getType());
   }

   public String getType() {
      return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
   }

   public void setType(String var1) {
      this.typeIndex = this.cp.addUtf8Info(Descriptor.of(var1));
   }

   public String getValue() {
      return this.cp.getUtf8Info(this.valueIndex);
   }

   public void setValue(String var1) {
      this.valueIndex = this.cp.addUtf8Info(var1);
   }

   public String toString() {
      return this.getType() + "." + this.getValue();
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitEnumMemberValue(this);
   }
}
