package javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClassFileWriter {
   private ByteStream output = new ByteStream(512);
   private ClassFileWriter.ConstPoolWriter constPool;
   private ClassFileWriter.FieldWriter fields;
   private ClassFileWriter.MethodWriter methods;
   int thisClass;
   int superClass;

   public ClassFileWriter(int var1, int var2) {
      this.output.writeInt(-889275714);
      this.output.writeShort(var2);
      this.output.writeShort(var1);
      this.constPool = new ClassFileWriter.ConstPoolWriter(this.output);
      this.fields = new ClassFileWriter.FieldWriter(this.constPool);
      this.methods = new ClassFileWriter.MethodWriter(this.constPool);
   }

   public ClassFileWriter.ConstPoolWriter getConstPool() {
      return this.constPool;
   }

   public ClassFileWriter.FieldWriter getFieldWriter() {
      return this.fields;
   }

   public ClassFileWriter.MethodWriter getMethodWriter() {
      return this.methods;
   }

   public byte[] end(int var1, int var2, int var3, int[] var4, ClassFileWriter.AttributeWriter var5) {
      this.constPool.end();
      this.output.writeShort(var1);
      this.output.writeShort(var2);
      this.output.writeShort(var3);
      if (var4 == null) {
         this.output.writeShort(0);
      } else {
         int var6 = var4.length;
         this.output.writeShort(var6);

         for(int var7 = 0; var7 < var6; ++var7) {
            this.output.writeShort(var4[var7]);
         }
      }

      this.output.enlarge(this.fields.dataSize() + this.methods.dataSize() + 6);

      try {
         this.output.writeShort(this.fields.size());
         this.fields.write(this.output);
         this.output.writeShort(this.methods.numOfMethods());
         this.methods.write(this.output);
      } catch (IOException var8) {
      }

      writeAttribute(this.output, var5, 0);
      return this.output.toByteArray();
   }

   public void end(DataOutputStream var1, int var2, int var3, int var4, int[] var5, ClassFileWriter.AttributeWriter var6) throws IOException {
      this.constPool.end();
      this.output.writeTo(var1);
      var1.writeShort(var2);
      var1.writeShort(var3);
      var1.writeShort(var4);
      if (var5 == null) {
         var1.writeShort(0);
      } else {
         int var7 = var5.length;
         var1.writeShort(var7);

         for(int var8 = 0; var8 < var7; ++var8) {
            var1.writeShort(var5[var8]);
         }
      }

      var1.writeShort(this.fields.size());
      this.fields.write(var1);
      var1.writeShort(this.methods.numOfMethods());
      this.methods.write(var1);
      if (var6 == null) {
         var1.writeShort(0);
      } else {
         var1.writeShort(var6.size());
         var6.write(var1);
      }

   }

   static void writeAttribute(ByteStream var0, ClassFileWriter.AttributeWriter var1, int var2) {
      if (var1 == null) {
         var0.writeShort(var2);
      } else {
         var0.writeShort(var1.size() + var2);
         DataOutputStream var3 = new DataOutputStream(var0);

         try {
            var1.write(var3);
            var3.flush();
         } catch (IOException var5) {
         }

      }
   }

   public static final class ConstPoolWriter {
      ByteStream output;
      protected int startPos;
      protected int num;

      ConstPoolWriter(ByteStream var1) {
         this.output = var1;
         this.startPos = var1.getPos();
         this.num = 1;
         this.output.writeShort(1);
      }

      public int[] addClassInfo(String[] var1) {
         int var2 = var1.length;
         int[] var3 = new int[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = this.addClassInfo(var1[var4]);
         }

         return var3;
      }

      public int addClassInfo(String var1) {
         int var2 = this.addUtf8Info(var1);
         this.output.write(7);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addClassInfo(int var1) {
         this.output.write(7);
         this.output.writeShort(var1);
         return this.num++;
      }

      public int addNameAndTypeInfo(String var1, String var2) {
         return this.addNameAndTypeInfo(this.addUtf8Info(var1), this.addUtf8Info(var2));
      }

      public int addNameAndTypeInfo(int var1, int var2) {
         this.output.write(12);
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addFieldrefInfo(int var1, int var2) {
         this.output.write(9);
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addMethodrefInfo(int var1, int var2) {
         this.output.write(10);
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addInterfaceMethodrefInfo(int var1, int var2) {
         this.output.write(11);
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addMethodHandleInfo(int var1, int var2) {
         this.output.write(15);
         this.output.write(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addMethodTypeInfo(int var1) {
         this.output.write(16);
         this.output.writeShort(var1);
         return this.num++;
      }

      public int addInvokeDynamicInfo(int var1, int var2) {
         this.output.write(18);
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addStringInfo(String var1) {
         int var2 = this.addUtf8Info(var1);
         this.output.write(8);
         this.output.writeShort(var2);
         return this.num++;
      }

      public int addIntegerInfo(int var1) {
         this.output.write(3);
         this.output.writeInt(var1);
         return this.num++;
      }

      public int addFloatInfo(float var1) {
         this.output.write(4);
         this.output.writeFloat(var1);
         return this.num++;
      }

      public int addLongInfo(long var1) {
         this.output.write(5);
         this.output.writeLong(var1);
         int var3 = this.num;
         this.num += 2;
         return var3;
      }

      public int addDoubleInfo(double var1) {
         this.output.write(6);
         this.output.writeDouble(var1);
         int var3 = this.num;
         this.num += 2;
         return var3;
      }

      public int addUtf8Info(String var1) {
         this.output.write(1);
         this.output.writeUTF(var1);
         return this.num++;
      }

      void end() {
         this.output.writeShort(this.startPos, this.num);
      }
   }

   public static final class MethodWriter {
      protected ByteStream output = new ByteStream(256);
      protected ClassFileWriter.ConstPoolWriter constPool;
      private int methodCount;
      protected int codeIndex;
      protected int throwsIndex;
      protected int stackIndex;
      private int startPos;
      private boolean isAbstract;
      private int catchPos;
      private int catchCount;

      MethodWriter(ClassFileWriter.ConstPoolWriter var1) {
         this.constPool = var1;
         this.methodCount = 0;
         this.codeIndex = 0;
         this.throwsIndex = 0;
         this.stackIndex = 0;
      }

      public void begin(int var1, String var2, String var3, String[] var4, ClassFileWriter.AttributeWriter var5) {
         int var6 = this.constPool.addUtf8Info(var2);
         int var7 = this.constPool.addUtf8Info(var3);
         int[] var8;
         if (var4 == null) {
            var8 = null;
         } else {
            var8 = this.constPool.addClassInfo(var4);
         }

         this.begin(var1, var6, var7, var8, var5);
      }

      public void begin(int var1, int var2, int var3, int[] var4, ClassFileWriter.AttributeWriter var5) {
         ++this.methodCount;
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         this.output.writeShort(var3);
         this.isAbstract = (var1 & 1024) != 0;
         int var6 = this.isAbstract ? 0 : 1;
         if (var4 != null) {
            ++var6;
         }

         ClassFileWriter.writeAttribute(this.output, var5, var6);
         if (var4 != null) {
            this.writeThrows(var4);
         }

         if (!this.isAbstract) {
            if (this.codeIndex == 0) {
               this.codeIndex = this.constPool.addUtf8Info("Code");
            }

            this.startPos = this.output.getPos();
            this.output.writeShort(this.codeIndex);
            this.output.writeBlank(12);
         }

         this.catchPos = -1;
         this.catchCount = 0;
      }

      private void writeThrows(int[] var1) {
         if (this.throwsIndex == 0) {
            this.throwsIndex = this.constPool.addUtf8Info("Exceptions");
         }

         this.output.writeShort(this.throwsIndex);
         this.output.writeInt(var1.length * 2 + 2);
         this.output.writeShort(var1.length);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.output.writeShort(var1[var2]);
         }

      }

      public void add(int var1) {
         this.output.write(var1);
      }

      public void add16(int var1) {
         this.output.writeShort(var1);
      }

      public void add32(int var1) {
         this.output.writeInt(var1);
      }

      public void addInvoke(int var1, String var2, String var3, String var4) {
         int var5 = this.constPool.addClassInfo(var2);
         int var6 = this.constPool.addNameAndTypeInfo(var3, var4);
         int var7 = this.constPool.addMethodrefInfo(var5, var6);
         this.add(var1);
         this.add16(var7);
      }

      public void codeEnd(int var1, int var2) {
         if (!this.isAbstract) {
            this.output.writeShort(this.startPos + 6, var1);
            this.output.writeShort(this.startPos + 8, var2);
            this.output.writeInt(this.startPos + 10, this.output.getPos() - this.startPos - 14);
            this.catchPos = this.output.getPos();
            this.catchCount = 0;
            this.output.writeShort(0);
         }

      }

      public void addCatch(int var1, int var2, int var3, int var4) {
         ++this.catchCount;
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         this.output.writeShort(var3);
         this.output.writeShort(var4);
      }

      public void end(StackMapTable.Writer var1, ClassFileWriter.AttributeWriter var2) {
         if (!this.isAbstract) {
            this.output.writeShort(this.catchPos, this.catchCount);
            int var3 = var1 == null ? 0 : 1;
            ClassFileWriter.writeAttribute(this.output, var2, var3);
            if (var1 != null) {
               if (this.stackIndex == 0) {
                  this.stackIndex = this.constPool.addUtf8Info("StackMapTable");
               }

               this.output.writeShort(this.stackIndex);
               byte[] var4 = var1.toByteArray();
               this.output.writeInt(var4.length);
               this.output.write(var4);
            }

            this.output.writeInt(this.startPos + 2, this.output.getPos() - this.startPos - 6);
         }
      }

      public int size() {
         return this.output.getPos() - this.startPos - 14;
      }

      int numOfMethods() {
         return this.methodCount;
      }

      int dataSize() {
         return this.output.size();
      }

      void write(OutputStream var1) throws IOException {
         this.output.writeTo(var1);
      }
   }

   public static final class FieldWriter {
      protected ByteStream output = new ByteStream(128);
      protected ClassFileWriter.ConstPoolWriter constPool;
      private int fieldCount;

      FieldWriter(ClassFileWriter.ConstPoolWriter var1) {
         this.constPool = var1;
         this.fieldCount = 0;
      }

      public void add(int var1, String var2, String var3, ClassFileWriter.AttributeWriter var4) {
         int var5 = this.constPool.addUtf8Info(var2);
         int var6 = this.constPool.addUtf8Info(var3);
         this.add(var1, var5, var6, var4);
      }

      public void add(int var1, int var2, int var3, ClassFileWriter.AttributeWriter var4) {
         ++this.fieldCount;
         this.output.writeShort(var1);
         this.output.writeShort(var2);
         this.output.writeShort(var3);
         ClassFileWriter.writeAttribute(this.output, var4, 0);
      }

      int size() {
         return this.fieldCount;
      }

      int dataSize() {
         return this.output.size();
      }

      void write(OutputStream var1) throws IOException {
         this.output.writeTo(var1);
      }
   }

   public interface AttributeWriter {
      int size();

      void write(DataOutputStream var1) throws IOException;
   }
}
