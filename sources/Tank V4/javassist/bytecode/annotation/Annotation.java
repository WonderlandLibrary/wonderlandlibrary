package javassist.bytecode.annotation;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public class Annotation {
   ConstPool pool;
   int typeIndex;
   LinkedHashMap members;

   public Annotation(int var1, ConstPool var2) {
      this.pool = var2;
      this.typeIndex = var1;
      this.members = null;
   }

   public Annotation(String var1, ConstPool var2) {
      this(var2.addUtf8Info(Descriptor.of(var1)), var2);
   }

   public Annotation(ConstPool var1, CtClass var2) throws NotFoundException {
      this(var1.addUtf8Info(Descriptor.of(var2.getName())), var1);
      if (!var2.isInterface()) {
         throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
      } else {
         CtMethod[] var3 = var2.getDeclaredMethods();
         if (var3.length > 0) {
            this.members = new LinkedHashMap();
         }

         for(int var4 = 0; var4 < var3.length; ++var4) {
            CtClass var5 = var3[var4].getReturnType();
            this.addMemberValue(var3[var4].getName(), createMemberValue(var1, var5));
         }

      }
   }

   public static MemberValue createMemberValue(ConstPool var0, CtClass var1) throws NotFoundException {
      if (var1 == CtClass.booleanType) {
         return new BooleanMemberValue(var0);
      } else if (var1 == CtClass.byteType) {
         return new ByteMemberValue(var0);
      } else if (var1 == CtClass.charType) {
         return new CharMemberValue(var0);
      } else if (var1 == CtClass.shortType) {
         return new ShortMemberValue(var0);
      } else if (var1 == CtClass.intType) {
         return new IntegerMemberValue(var0);
      } else if (var1 == CtClass.longType) {
         return new LongMemberValue(var0);
      } else if (var1 == CtClass.floatType) {
         return new FloatMemberValue(var0);
      } else if (var1 == CtClass.doubleType) {
         return new DoubleMemberValue(var0);
      } else if (var1.getName().equals("java.lang.Class")) {
         return new ClassMemberValue(var0);
      } else if (var1.getName().equals("java.lang.String")) {
         return new StringMemberValue(var0);
      } else if (var1.isArray()) {
         CtClass var5 = var1.getComponentType();
         MemberValue var3 = createMemberValue(var0, var5);
         return new ArrayMemberValue(var3, var0);
      } else if (var1.isInterface()) {
         Annotation var4 = new Annotation(var0, var1);
         return new AnnotationMemberValue(var4, var0);
      } else {
         EnumMemberValue var2 = new EnumMemberValue(var0);
         var2.setType(var1.getName());
         return var2;
      }
   }

   public void addMemberValue(int var1, MemberValue var2) {
      Annotation.Pair var3 = new Annotation.Pair();
      var3.name = var1;
      var3.value = var2;
      this.addMemberValue(var3);
   }

   public void addMemberValue(String var1, MemberValue var2) {
      Annotation.Pair var3 = new Annotation.Pair();
      var3.name = this.pool.addUtf8Info(var1);
      var3.value = var2;
      if (this.members == null) {
         this.members = new LinkedHashMap();
      }

      this.members.put(var1, var3);
   }

   private void addMemberValue(Annotation.Pair var1) {
      String var2 = this.pool.getUtf8Info(var1.name);
      if (this.members == null) {
         this.members = new LinkedHashMap();
      }

      this.members.put(var2, var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("@");
      var1.append(this.getTypeName());
      if (this.members != null) {
         var1.append("(");
         Iterator var2 = this.members.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(var3).append("=").append(this.getMemberValue(var3));
            if (var2.hasNext()) {
               var1.append(", ");
            }
         }

         var1.append(")");
      }

      return var1.toString();
   }

   public String getTypeName() {
      return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
   }

   public Set getMemberNames() {
      return this.members == null ? null : this.members.keySet();
   }

   public MemberValue getMemberValue(String var1) {
      if (this.members == null) {
         return null;
      } else {
         Annotation.Pair var2 = (Annotation.Pair)this.members.get(var1);
         return var2 == null ? null : var2.value;
      }
   }

   public Object toAnnotationType(ClassLoader var1, ClassPool var2) throws ClassNotFoundException, NoSuchClassError {
      return AnnotationImpl.make(var1, MemberValue.loadClass(var1, this.getTypeName()), var2, this);
   }

   public void write(AnnotationsWriter var1) throws IOException {
      String var2 = this.pool.getUtf8Info(this.typeIndex);
      if (this.members == null) {
         var1.annotation(var2, 0);
      } else {
         var1.annotation(var2, this.members.size());
         Iterator var3 = this.members.values().iterator();

         while(var3.hasNext()) {
            Annotation.Pair var4 = (Annotation.Pair)var3.next();
            var1.memberValuePair(var4.name);
            var4.value.write(var1);
         }

      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1 instanceof Annotation) {
         Annotation var2 = (Annotation)var1;
         if (!this.getTypeName().equals(var2.getTypeName())) {
            return false;
         } else {
            LinkedHashMap var3 = var2.members;
            if (this.members == var3) {
               return true;
            } else if (this.members == null) {
               return var3 == null;
            } else {
               return var3 == null ? false : this.members.equals(var3);
            }
         }
      } else {
         return false;
      }
   }

   static class Pair {
      int name;
      MemberValue value;
   }
}
