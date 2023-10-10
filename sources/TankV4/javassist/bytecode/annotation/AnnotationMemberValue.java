package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class AnnotationMemberValue extends MemberValue {
   Annotation value;

   public AnnotationMemberValue(ConstPool var1) {
      this((Annotation)null, var1);
   }

   public AnnotationMemberValue(Annotation var1, ConstPool var2) {
      super('@', var2);
      this.value = var1;
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException {
      return AnnotationImpl.make(var1, this.getType(var1), var2, this.value);
   }

   Class getType(ClassLoader var1) throws ClassNotFoundException {
      if (this.value == null) {
         throw new ClassNotFoundException("no type specified");
      } else {
         return loadClass(var1, this.value.getTypeName());
      }
   }

   public Annotation getValue() {
      return this.value;
   }

   public void setValue(Annotation var1) {
      this.value = var1;
   }

   public String toString() {
      return this.value.toString();
   }

   public void write(AnnotationsWriter var1) throws IOException {
      var1.annotationValue();
      this.value.write(var1);
   }

   public void accept(MemberValueVisitor var1) {
      var1.visitAnnotationMemberValue(this);
   }
}
