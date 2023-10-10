package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.bytecode.annotation.MemberValue;

public class AnnotationDefaultAttribute extends AttributeInfo {
   public static final String tag = "AnnotationDefault";

   public AnnotationDefaultAttribute(ConstPool var1, byte[] var2) {
      super(var1, "AnnotationDefault", var2);
   }

   public AnnotationDefaultAttribute(ConstPool var1) {
      this(var1, new byte[]{0, 0});
   }

   AnnotationDefaultAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      AnnotationsAttribute.Copier var3 = new AnnotationsAttribute.Copier(this.info, this.constPool, var1, var2);

      try {
         var3.memberValue(0);
         return new AnnotationDefaultAttribute(var1, var3.close());
      } catch (Exception var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public MemberValue getDefaultValue() {
      try {
         return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseMemberValue();
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public void setDefaultValue(MemberValue var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      AnnotationsWriter var3 = new AnnotationsWriter(var2, this.constPool);

      try {
         var1.write(var3);
         var3.close();
      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }

      this.set(var2.toByteArray());
   }

   public String toString() {
      return this.getDefaultValue().toString();
   }
}
