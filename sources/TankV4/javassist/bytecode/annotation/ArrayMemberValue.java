package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ArrayMemberValue extends MemberValue {
   MemberValue type;
   MemberValue[] values;

   public ArrayMemberValue(ConstPool var1) {
      super('[', var1);
      this.type = null;
      this.values = null;
   }

   public ArrayMemberValue(MemberValue var1, ConstPool var2) {
      super('[', var2);
      this.type = var1;
      this.values = null;
   }

   Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException {
      if (this.values == null) {
         throw new ClassNotFoundException("no array elements found: " + var3.getName());
      } else {
         int var4 = this.values.length;
         Class var5;
         if (this.type == null) {
            var5 = var3.getReturnType().getComponentType();
            if (var5 == null || var4 > 0) {
               throw new ClassNotFoundException("broken array type: " + var3.getName());
            }
         } else {
            var5 = this.type.getType(var1);
         }

         Object var6 = Array.newInstance(var5, var4);

         for(int var7 = 0; var7 < var4; ++var7) {
            Array.set(var6, var7, this.values[var7].getValue(var1, var2, var3));
         }

         return var6;
      }
   }

   Class getType(ClassLoader var1) throws ClassNotFoundException {
      if (this.type == null) {
         throw new ClassNotFoundException("no array type specified");
      } else {
         Object var2 = Array.newInstance(this.type.getType(var1), 0);
         return var2.getClass();
      }
   }

   public MemberValue getType() {
      return this.type;
   }

   public MemberValue[] getValue() {
      return this.values;
   }

   public void setValue(MemberValue[] var1) {
      this.values = var1;
      if (var1 != null && var1.length > 0) {
         this.type = var1[0];
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("{");
      if (this.values != null) {
         for(int var2 = 0; var2 < this.values.length; ++var2) {
            var1.append(this.values[var2].toString());
            if (var2 + 1 < this.values.length) {
               var1.append(", ");
            }
         }
      }

      var1.append("}");
      return var1.toString();
   }

   public void write(AnnotationsWriter var1) throws IOException {
      int var2 = this.values == null ? 0 : this.values.length;
      var1.arrayValue(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.values[var3].write(var1);
      }

   }

   public void accept(MemberValueVisitor var1) {
      var1.visitArrayMemberValue(this);
   }
}
