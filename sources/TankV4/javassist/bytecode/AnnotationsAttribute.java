package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class AnnotationsAttribute extends AttributeInfo {
   public static final String visibleTag = "RuntimeVisibleAnnotations";
   public static final String invisibleTag = "RuntimeInvisibleAnnotations";

   public AnnotationsAttribute(ConstPool var1, String var2, byte[] var3) {
      super(var1, var2, var3);
   }

   public AnnotationsAttribute(ConstPool var1, String var2) {
      this(var1, var2, new byte[]{0, 0});
   }

   AnnotationsAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public int numAnnotations() {
      return ByteArray.readU16bit(this.info, 0);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      AnnotationsAttribute.Copier var3 = new AnnotationsAttribute.Copier(this.info, this.constPool, var1, var2);

      try {
         var3.annotationArray();
         return new AnnotationsAttribute(var1, this.getName(), var3.close());
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   public Annotation getAnnotation(String var1) {
      Annotation[] var2 = this.getAnnotations();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getTypeName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public void addAnnotation(Annotation var1) {
      String var2 = var1.getTypeName();
      Annotation[] var3 = this.getAnnotations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getTypeName().equals(var2)) {
            var3[var4] = var1;
            this.setAnnotations(var3);
            return;
         }
      }

      Annotation[] var5 = new Annotation[var3.length + 1];
      System.arraycopy(var3, 0, var5, 0, var3.length);
      var5[var3.length] = var1;
      this.setAnnotations(var5);
   }

   public Annotation[] getAnnotations() {
      try {
         return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseAnnotations();
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public void setAnnotations(Annotation[] var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      AnnotationsWriter var3 = new AnnotationsWriter(var2, this.constPool);

      try {
         int var4 = var1.length;
         var3.numAnnotations(var4);
         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               var3.close();
               break;
            }

            var1[var5].write(var3);
            ++var5;
         }
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      }

      this.set(var2.toByteArray());
   }

   public void setAnnotation(Annotation var1) {
      this.setAnnotations(new Annotation[]{var1});
   }

   void renameClass(String var1, String var2) {
      HashMap var3 = new HashMap();
      var3.put(var1, var2);
      this.renameClass(var3);
   }

   void renameClass(Map var1) {
      AnnotationsAttribute.Renamer var2 = new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), var1);

      try {
         var2.annotationArray();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   void getRefClasses(Map var1) {
      this.renameClass(var1);
   }

   public String toString() {
      Annotation[] var1 = this.getAnnotations();
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while(var3 < var1.length) {
         var2.append(var1[var3++].toString());
         if (var3 != var1.length) {
            var2.append(", ");
         }
      }

      return var2.toString();
   }

   static class Parser extends AnnotationsAttribute.Walker {
      ConstPool pool;
      Annotation[][] allParams;
      Annotation[] allAnno;
      Annotation currentAnno;
      MemberValue currentMember;

      Parser(byte[] var1, ConstPool var2) {
         super(var1);
         this.pool = var2;
      }

      Annotation[][] parseParameters() throws Exception {
         this.parameters();
         return this.allParams;
      }

      Annotation[] parseAnnotations() throws Exception {
         this.annotationArray();
         return this.allAnno;
      }

      MemberValue parseMemberValue() throws Exception {
         this.memberValue(0);
         return this.currentMember;
      }

      void parameters(int var1, int var2) throws Exception {
         Annotation[][] var3 = new Annotation[var1][];

         for(int var4 = 0; var4 < var1; ++var4) {
            var2 = this.annotationArray(var2);
            var3[var4] = this.allAnno;
         }

         this.allParams = var3;
      }

      int annotationArray(int var1, int var2) throws Exception {
         Annotation[] var3 = new Annotation[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var1 = this.annotation(var1);
            var3[var4] = this.currentAnno;
         }

         this.allAnno = var3;
         return var1;
      }

      int annotation(int var1, int var2, int var3) throws Exception {
         this.currentAnno = new Annotation(var2, this.pool);
         return super.annotation(var1, var2, var3);
      }

      int memberValuePair(int var1, int var2) throws Exception {
         var1 = super.memberValuePair(var1, var2);
         this.currentAnno.addMemberValue(var2, this.currentMember);
         return var1;
      }

      void constValueMember(int var1, int var2) throws Exception {
         ConstPool var4 = this.pool;
         Object var3;
         switch(var1) {
         case 66:
            var3 = new ByteMemberValue(var2, var4);
            break;
         case 67:
            var3 = new CharMemberValue(var2, var4);
            break;
         case 68:
            var3 = new DoubleMemberValue(var2, var4);
            break;
         case 70:
            var3 = new FloatMemberValue(var2, var4);
            break;
         case 73:
            var3 = new IntegerMemberValue(var2, var4);
            break;
         case 74:
            var3 = new LongMemberValue(var2, var4);
            break;
         case 83:
            var3 = new ShortMemberValue(var2, var4);
            break;
         case 90:
            var3 = new BooleanMemberValue(var2, var4);
            break;
         case 115:
            var3 = new StringMemberValue(var2, var4);
            break;
         default:
            throw new RuntimeException("unknown tag:" + var1);
         }

         this.currentMember = (MemberValue)var3;
         super.constValueMember(var1, var2);
      }

      void enumMemberValue(int var1, int var2, int var3) throws Exception {
         this.currentMember = new EnumMemberValue(var2, var3, this.pool);
         super.enumMemberValue(var1, var2, var3);
      }

      void classMemberValue(int var1, int var2) throws Exception {
         this.currentMember = new ClassMemberValue(var2, this.pool);
         super.classMemberValue(var1, var2);
      }

      int annotationMemberValue(int var1) throws Exception {
         Annotation var2 = this.currentAnno;
         var1 = super.annotationMemberValue(var1);
         this.currentMember = new AnnotationMemberValue(this.currentAnno, this.pool);
         this.currentAnno = var2;
         return var1;
      }

      int arrayMemberValue(int var1, int var2) throws Exception {
         ArrayMemberValue var3 = new ArrayMemberValue(this.pool);
         MemberValue[] var4 = new MemberValue[var2];

         for(int var5 = 0; var5 < var2; ++var5) {
            var1 = this.memberValue(var1);
            var4[var5] = this.currentMember;
         }

         var3.setValue(var4);
         this.currentMember = var3;
         return var1;
      }
   }

   static class Copier extends AnnotationsAttribute.Walker {
      ByteArrayOutputStream output;
      AnnotationsWriter writer;
      ConstPool srcPool;
      ConstPool destPool;
      Map classnames;

      Copier(byte[] var1, ConstPool var2, ConstPool var3, Map var4) {
         this(var1, var2, var3, var4, true);
      }

      Copier(byte[] var1, ConstPool var2, ConstPool var3, Map var4, boolean var5) {
         super(var1);
         this.output = new ByteArrayOutputStream();
         if (var5) {
            this.writer = new AnnotationsWriter(this.output, var3);
         }

         this.srcPool = var2;
         this.destPool = var3;
         this.classnames = var4;
      }

      byte[] close() throws IOException {
         this.writer.close();
         return this.output.toByteArray();
      }

      void parameters(int var1, int var2) throws Exception {
         this.writer.numParameters(var1);
         super.parameters(var1, var2);
      }

      int annotationArray(int var1, int var2) throws Exception {
         this.writer.numAnnotations(var2);
         return super.annotationArray(var1, var2);
      }

      int annotation(int var1, int var2, int var3) throws Exception {
         this.writer.annotation(this.copyType(var2), var3);
         return super.annotation(var1, var2, var3);
      }

      int memberValuePair(int var1, int var2) throws Exception {
         this.writer.memberValuePair(this.copy(var2));
         return super.memberValuePair(var1, var2);
      }

      void constValueMember(int var1, int var2) throws Exception {
         this.writer.constValueIndex(var1, this.copy(var2));
         super.constValueMember(var1, var2);
      }

      void enumMemberValue(int var1, int var2, int var3) throws Exception {
         this.writer.enumConstValue(this.copyType(var2), this.copy(var3));
         super.enumMemberValue(var1, var2, var3);
      }

      void classMemberValue(int var1, int var2) throws Exception {
         this.writer.classInfoIndex(this.copyType(var2));
         super.classMemberValue(var1, var2);
      }

      int annotationMemberValue(int var1) throws Exception {
         this.writer.annotationValue();
         return super.annotationMemberValue(var1);
      }

      int arrayMemberValue(int var1, int var2) throws Exception {
         this.writer.arrayValue(var2);
         return super.arrayMemberValue(var1, var2);
      }

      int copy(int var1) {
         return this.srcPool.copy(var1, this.destPool, this.classnames);
      }

      int copyType(int var1) {
         String var2 = this.srcPool.getUtf8Info(var1);
         String var3 = Descriptor.rename(var2, this.classnames);
         return this.destPool.addUtf8Info(var3);
      }
   }

   static class Renamer extends AnnotationsAttribute.Walker {
      ConstPool cpool;
      Map classnames;

      Renamer(byte[] var1, ConstPool var2, Map var3) {
         super(var1);
         this.cpool = var2;
         this.classnames = var3;
      }

      int annotation(int var1, int var2, int var3) throws Exception {
         this.renameType(var1 - 4, var2);
         return super.annotation(var1, var2, var3);
      }

      void enumMemberValue(int var1, int var2, int var3) throws Exception {
         this.renameType(var1 + 1, var2);
         super.enumMemberValue(var1, var2, var3);
      }

      void classMemberValue(int var1, int var2) throws Exception {
         this.renameType(var1 + 1, var2);
         super.classMemberValue(var1, var2);
      }

      private void renameType(int var1, int var2) {
         String var3 = this.cpool.getUtf8Info(var2);
         String var4 = Descriptor.rename(var3, this.classnames);
         if (!var3.equals(var4)) {
            int var5 = this.cpool.addUtf8Info(var4);
            ByteArray.write16bit(var5, this.info, var1);
         }

      }
   }

   static class Walker {
      byte[] info;

      Walker(byte[] var1) {
         this.info = var1;
      }

      final void parameters() throws Exception {
         int var1 = this.info[0] & 255;
         this.parameters(var1, 1);
      }

      void parameters(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2 = this.annotationArray(var2);
         }

      }

      final void annotationArray() throws Exception {
         this.annotationArray(0);
      }

      final int annotationArray(int var1) throws Exception {
         int var2 = ByteArray.readU16bit(this.info, var1);
         return this.annotationArray(var1 + 2, var2);
      }

      int annotationArray(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1 = this.annotation(var1);
         }

         return var1;
      }

      final int annotation(int var1) throws Exception {
         int var2 = ByteArray.readU16bit(this.info, var1);
         int var3 = ByteArray.readU16bit(this.info, var1 + 2);
         return this.annotation(var1 + 4, var2, var3);
      }

      int annotation(int var1, int var2, int var3) throws Exception {
         for(int var4 = 0; var4 < var3; ++var4) {
            var1 = this.memberValuePair(var1);
         }

         return var1;
      }

      final int memberValuePair(int var1) throws Exception {
         int var2 = ByteArray.readU16bit(this.info, var1);
         return this.memberValuePair(var1 + 2, var2);
      }

      int memberValuePair(int var1, int var2) throws Exception {
         return this.memberValue(var1);
      }

      final int memberValue(int var1) throws Exception {
         int var2 = this.info[var1] & 255;
         int var3;
         if (var2 == 101) {
            var3 = ByteArray.readU16bit(this.info, var1 + 1);
            int var4 = ByteArray.readU16bit(this.info, var1 + 3);
            this.enumMemberValue(var1, var3, var4);
            return var1 + 5;
         } else if (var2 == 99) {
            var3 = ByteArray.readU16bit(this.info, var1 + 1);
            this.classMemberValue(var1, var3);
            return var1 + 3;
         } else if (var2 == 64) {
            return this.annotationMemberValue(var1 + 1);
         } else if (var2 == 91) {
            var3 = ByteArray.readU16bit(this.info, var1 + 1);
            return this.arrayMemberValue(var1 + 3, var3);
         } else {
            var3 = ByteArray.readU16bit(this.info, var1 + 1);
            this.constValueMember(var2, var3);
            return var1 + 3;
         }
      }

      void constValueMember(int var1, int var2) throws Exception {
      }

      void enumMemberValue(int var1, int var2, int var3) throws Exception {
      }

      void classMemberValue(int var1, int var2) throws Exception {
      }

      int annotationMemberValue(int var1) throws Exception {
         return this.annotation(var1);
      }

      int arrayMemberValue(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1 = this.memberValue(var1);
         }

         return var1;
      }
   }
}
