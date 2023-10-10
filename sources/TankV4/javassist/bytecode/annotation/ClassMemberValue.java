package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.SignatureAttribute;

public class ClassMemberValue extends MemberValue {
   int valueIndex;

   public ClassMemberValue(int var1, ConstPool var2) {
      super('c', var2);
      this.valueIndex = var1;
   }

   public ClassMemberValue(String var1, ConstPool var2) {
      super('c', var2);
      this.setValue(var1);
   }

   public ClassMemberValue(ConstPool var1) {
      super('c', var1);
      this.setValue("java.lang.Class");
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException {
      String var4 = this.getValue();
      if (var4.equals("void")) {
         return Void.TYPE;
      } else if (var4.equals("int")) {
         return Integer.TYPE;
      } else if (var4.equals("byte")) {
         return Byte.TYPE;
      } else if (var4.equals("long")) {
         return Long.TYPE;
      } else if (var4.equals("double")) {
         return Double.TYPE;
      } else if (var4.equals("float")) {
         return Float.TYPE;
      } else if (var4.equals("char")) {
         return Character.TYPE;
      } else if (var4.equals("short")) {
         return Short.TYPE;
      } else {
         return var4.equals("boolean") ? Boolean.TYPE : loadClass(var1, var4);
      }
   }

   Class getType(ClassLoader var1) throws ClassNotFoundException {
      return loadClass(var1, "java.lang.Class");
   }

   public String getValue() {
      String var1 = this.cp.getUtf8Info(this.valueIndex);

      try {
         return SignatureAttribute.toTypeSignature(var1).jvmTypeName();
      } catch (BadBytecode var3) {
         throw new RuntimeException(var3);
      }
   }

   public void setValue(String var1) {
      String var2 = Descriptor.of(var1);
      this.valueIndex = this.cp.addUtf8Info(var2);
   }

   public String toString() {
      return this.getValue().replace('$', '.') + ".class";
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitClassMemberValue(this);
   }
}
