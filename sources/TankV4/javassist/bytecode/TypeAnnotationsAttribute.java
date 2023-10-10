package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.annotation.TypeAnnotationsWriter;

public class TypeAnnotationsAttribute extends AttributeInfo {
   public static final String visibleTag = "RuntimeVisibleTypeAnnotations";
   public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";

   public TypeAnnotationsAttribute(ConstPool var1, String var2, byte[] var3) {
      super(var1, var2, var3);
   }

   TypeAnnotationsAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public int numAnnotations() {
      return ByteArray.readU16bit(this.info, 0);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      TypeAnnotationsAttribute.Copier var3 = new TypeAnnotationsAttribute.Copier(this.info, this.constPool, var1, var2);

      try {
         var3.annotationArray();
         return new TypeAnnotationsAttribute(var1, this.getName(), var3.close());
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   void renameClass(String var1, String var2) {
      HashMap var3 = new HashMap();
      var3.put(var1, var2);
      this.renameClass(var3);
   }

   void renameClass(Map var1) {
      TypeAnnotationsAttribute.Renamer var2 = new TypeAnnotationsAttribute.Renamer(this.info, this.getConstPool(), var1);

      try {
         var2.annotationArray();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   void getRefClasses(Map var1) {
      this.renameClass(var1);
   }

   static class SubCopier extends TypeAnnotationsAttribute.SubWalker {
      ConstPool srcPool;
      ConstPool destPool;
      Map classnames;
      TypeAnnotationsWriter writer;

      SubCopier(byte[] var1, ConstPool var2, ConstPool var3, Map var4, TypeAnnotationsWriter var5) {
         super(var1);
         this.srcPool = var2;
         this.destPool = var3;
         this.classnames = var4;
         this.writer = var5;
      }

      void typeParameterTarget(int var1, int var2, int var3) throws Exception {
         this.writer.typeParameterTarget(var2, var3);
      }

      void supertypeTarget(int var1, int var2) throws Exception {
         this.writer.supertypeTarget(var2);
      }

      void typeParameterBoundTarget(int var1, int var2, int var3, int var4) throws Exception {
         this.writer.typeParameterBoundTarget(var2, var3, var4);
      }

      void emptyTarget(int var1, int var2) throws Exception {
         this.writer.emptyTarget(var2);
      }

      void formalParameterTarget(int var1, int var2) throws Exception {
         this.writer.formalParameterTarget(var2);
      }

      void throwsTarget(int var1, int var2) throws Exception {
         this.writer.throwsTarget(var2);
      }

      int localvarTarget(int var1, int var2, int var3) throws Exception {
         this.writer.localVarTarget(var2, var3);
         return super.localvarTarget(var1, var2, var3);
      }

      void localvarTarget(int var1, int var2, int var3, int var4, int var5) throws Exception {
         this.writer.localVarTargetTable(var3, var4, var5);
      }

      void catchTarget(int var1, int var2) throws Exception {
         this.writer.catchTarget(var2);
      }

      void offsetTarget(int var1, int var2, int var3) throws Exception {
         this.writer.offsetTarget(var2, var3);
      }

      void typeArgumentTarget(int var1, int var2, int var3, int var4) throws Exception {
         this.writer.typeArgumentTarget(var2, var3, var4);
      }

      int typePath(int var1, int var2) throws Exception {
         this.writer.typePath(var2);
         return super.typePath(var1, var2);
      }

      void typePath(int var1, int var2, int var3) throws Exception {
         this.writer.typePathPath(var2, var3);
      }
   }

   static class Copier extends AnnotationsAttribute.Copier {
      TypeAnnotationsAttribute.SubCopier sub;

      Copier(byte[] var1, ConstPool var2, ConstPool var3, Map var4) {
         super(var1, var2, var3, var4, false);
         TypeAnnotationsWriter var5 = new TypeAnnotationsWriter(this.output, var3);
         this.writer = var5;
         this.sub = new TypeAnnotationsAttribute.SubCopier(var1, var2, var3, var4, var5);
      }

      int annotationArray(int var1, int var2) throws Exception {
         this.writer.numAnnotations(var2);

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.info[var1] & 255;
            var1 = this.sub.targetInfo(var1 + 1, var4);
            var1 = this.sub.typePath(var1);
            var1 = this.annotation(var1);
         }

         return var1;
      }
   }

   static class Renamer extends AnnotationsAttribute.Renamer {
      TypeAnnotationsAttribute.SubWalker sub;

      Renamer(byte[] var1, ConstPool var2, Map var3) {
         super(var1, var2, var3);
         this.sub = new TypeAnnotationsAttribute.SubWalker(var1);
      }

      int annotationArray(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.info[var1] & 255;
            var1 = this.sub.targetInfo(var1 + 1, var4);
            var1 = this.sub.typePath(var1);
            var1 = this.annotation(var1);
         }

         return var1;
      }
   }

   static class SubWalker {
      byte[] info;

      SubWalker(byte[] var1) {
         this.info = var1;
      }

      final int targetInfo(int var1, int var2) throws Exception {
         int var3;
         int var4;
         switch(var2) {
         case 0:
         case 1:
            var3 = this.info[var1] & 255;
            this.typeParameterTarget(var1, var2, var3);
            return var1 + 1;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         default:
            throw new RuntimeException("invalid target type: " + var2);
         case 16:
            var3 = ByteArray.readU16bit(this.info, var1);
            this.supertypeTarget(var1, var3);
            return var1 + 2;
         case 17:
         case 18:
            var3 = this.info[var1] & 255;
            var4 = this.info[var1 + 1] & 255;
            this.typeParameterBoundTarget(var1, var2, var3, var4);
            return var1 + 2;
         case 19:
         case 20:
         case 21:
            this.emptyTarget(var1, var2);
            return var1;
         case 22:
            var3 = this.info[var1] & 255;
            this.formalParameterTarget(var1, var3);
            return var1 + 1;
         case 23:
            var3 = ByteArray.readU16bit(this.info, var1);
            this.throwsTarget(var1, var3);
            return var1 + 2;
         case 64:
         case 65:
            var3 = ByteArray.readU16bit(this.info, var1);
            return this.localvarTarget(var1 + 2, var2, var3);
         case 66:
            var3 = ByteArray.readU16bit(this.info, var1);
            this.catchTarget(var1, var3);
            return var1 + 2;
         case 67:
         case 68:
         case 69:
         case 70:
            var3 = ByteArray.readU16bit(this.info, var1);
            this.offsetTarget(var1, var2, var3);
            return var1 + 2;
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
            var3 = ByteArray.readU16bit(this.info, var1);
            var4 = this.info[var1 + 2] & 255;
            this.typeArgumentTarget(var1, var2, var3, var4);
            return var1 + 3;
         }
      }

      void typeParameterTarget(int var1, int var2, int var3) throws Exception {
      }

      void supertypeTarget(int var1, int var2) throws Exception {
      }

      void typeParameterBoundTarget(int var1, int var2, int var3, int var4) throws Exception {
      }

      void emptyTarget(int var1, int var2) throws Exception {
      }

      void formalParameterTarget(int var1, int var2) throws Exception {
      }

      void throwsTarget(int var1, int var2) throws Exception {
      }

      int localvarTarget(int var1, int var2, int var3) throws Exception {
         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = ByteArray.readU16bit(this.info, var1);
            int var6 = ByteArray.readU16bit(this.info, var1 + 2);
            int var7 = ByteArray.readU16bit(this.info, var1 + 4);
            this.localvarTarget(var1, var2, var5, var6, var7);
            var1 += 6;
         }

         return var1;
      }

      void localvarTarget(int var1, int var2, int var3, int var4, int var5) throws Exception {
      }

      void catchTarget(int var1, int var2) throws Exception {
      }

      void offsetTarget(int var1, int var2, int var3) throws Exception {
      }

      void typeArgumentTarget(int var1, int var2, int var3, int var4) throws Exception {
      }

      final int typePath(int var1) throws Exception {
         int var2 = this.info[var1++] & 255;
         return this.typePath(var1, var2);
      }

      int typePath(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.info[var1] & 255;
            int var5 = this.info[var1 + 1] & 255;
            this.typePath(var1, var4, var5);
            var1 += 2;
         }

         return var1;
      }

      void typePath(int var1, int var2, int var3) throws Exception {
      }
   }

   static class TAWalker extends AnnotationsAttribute.Walker {
      TypeAnnotationsAttribute.SubWalker subWalker;

      TAWalker(byte[] var1) {
         super(var1);
         this.subWalker = new TypeAnnotationsAttribute.SubWalker(var1);
      }

      int annotationArray(int var1, int var2) throws Exception {
         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.info[var1] & 255;
            var1 = this.subWalker.targetInfo(var1 + 1, var4);
            var1 = this.subWalker.typePath(var1);
            var1 = this.annotation(var1);
         }

         return var1;
      }
   }
}
