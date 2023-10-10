package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationsWriter;

public class ParameterAnnotationsAttribute extends AttributeInfo {
   public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
   public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";

   public ParameterAnnotationsAttribute(ConstPool var1, String var2, byte[] var3) {
      super(var1, var2, var3);
   }

   public ParameterAnnotationsAttribute(ConstPool var1, String var2) {
      this(var1, var2, new byte[]{0});
   }

   ParameterAnnotationsAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public int numParameters() {
      return this.info[0] & 255;
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      AnnotationsAttribute.Copier var3 = new AnnotationsAttribute.Copier(this.info, this.constPool, var1, var2);

      try {
         var3.parameters();
         return new ParameterAnnotationsAttribute(var1, this.getName(), var3.close());
      } catch (Exception var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public Annotation[][] getAnnotations() {
      try {
         return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseParameters();
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public void setAnnotations(Annotation[][] var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      AnnotationsWriter var3 = new AnnotationsWriter(var2, this.constPool);

      try {
         int var4 = var1.length;
         var3.numParameters(var4);
         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               var3.close();
               break;
            }

            Annotation[] var6 = var1[var5];
            var3.numAnnotations(var6.length);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var6[var7].write(var3);
            }

            ++var5;
         }
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      }

      this.set(var2.toByteArray());
   }

   void renameClass(String var1, String var2) {
      HashMap var3 = new HashMap();
      var3.put(var1, var2);
      this.renameClass(var3);
   }

   void renameClass(Map var1) {
      AnnotationsAttribute.Renamer var2 = new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), var1);

      try {
         var2.parameters();
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   void getRefClasses(Map var1) {
      this.renameClass(var1);
   }

   public String toString() {
      Annotation[][] var1 = this.getAnnotations();
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while(var3 < var1.length) {
         Annotation[] var4 = var1[var3++];
         int var5 = 0;

         while(var5 < var4.length) {
            var2.append(var4[var5++].toString());
            if (var5 != var4.length) {
               var2.append(" ");
            }
         }

         if (var3 != var1.length) {
            var2.append(", ");
         }
      }

      return var2.toString();
   }
}
