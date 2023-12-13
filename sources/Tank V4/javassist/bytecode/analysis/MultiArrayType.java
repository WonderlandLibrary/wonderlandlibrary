package javassist.bytecode.analysis;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class MultiArrayType extends Type {
   private MultiType component;
   private int dims;

   public MultiArrayType(MultiType var1, int var2) {
      super((CtClass)null);
      this.component = var1;
      this.dims = var2;
   }

   public CtClass getCtClass() {
      CtClass var1 = this.component.getCtClass();
      if (var1 == null) {
         return null;
      } else {
         ClassPool var2 = var1.getClassPool();
         if (var2 == null) {
            var2 = ClassPool.getDefault();
         }

         String var3 = this.arrayName(var1.getName(), this.dims);

         try {
            return var2.get(var3);
         } catch (NotFoundException var5) {
            throw new RuntimeException(var5);
         }
      }
   }

   boolean popChanged() {
      return this.component.popChanged();
   }

   public int getDimensions() {
      return this.dims;
   }

   public Type getComponent() {
      return (Type)(this.dims == 1 ? this.component : new MultiArrayType(this.component, this.dims - 1));
   }

   public int getSize() {
      return 1;
   }

   public boolean isArray() {
      return true;
   }

   public boolean isAssignableFrom(Type var1) {
      throw new UnsupportedOperationException("Not implemented");
   }

   public boolean isReference() {
      return true;
   }

   public boolean isAssignableTo(Type var1) {
      if (eq(var1.getCtClass(), Type.OBJECT.getCtClass())) {
         return true;
      } else if (eq(var1.getCtClass(), Type.CLONEABLE.getCtClass())) {
         return true;
      } else if (eq(var1.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
         return true;
      } else if (!var1.isArray()) {
         return false;
      } else {
         Type var2 = this.getRootComponent(var1);
         int var3 = var1.getDimensions();
         if (var3 > this.dims) {
            return false;
         } else if (var3 < this.dims) {
            if (eq(var2.getCtClass(), Type.OBJECT.getCtClass())) {
               return true;
            } else if (eq(var2.getCtClass(), Type.CLONEABLE.getCtClass())) {
               return true;
            } else {
               return eq(var2.getCtClass(), Type.SERIALIZABLE.getCtClass());
            }
         } else {
            return this.component.isAssignableTo(var2);
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MultiArrayType)) {
         return false;
      } else {
         MultiArrayType var2 = (MultiArrayType)var1;
         return this.component.equals(var2.component) && this.dims == var2.dims;
      }
   }

   public String toString() {
      return this.arrayName(this.component.toString(), this.dims);
   }
}
