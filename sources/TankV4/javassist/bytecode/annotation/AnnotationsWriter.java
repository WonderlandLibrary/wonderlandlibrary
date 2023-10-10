package javassist.bytecode.annotation;

import java.io.IOException;
import java.io.OutputStream;
import javassist.bytecode.ByteArray;
import javassist.bytecode.ConstPool;

public class AnnotationsWriter {
   protected OutputStream output;
   private ConstPool pool;

   public AnnotationsWriter(OutputStream var1, ConstPool var2) {
      this.output = var1;
      this.pool = var2;
   }

   public ConstPool getConstPool() {
      return this.pool;
   }

   public void close() throws IOException {
      this.output.close();
   }

   public void numParameters(int var1) throws IOException {
      this.output.write(var1);
   }

   public void numAnnotations(int var1) throws IOException {
      this.write16bit(var1);
   }

   public void annotation(String var1, int var2) throws IOException {
      this.annotation(this.pool.addUtf8Info(var1), var2);
   }

   public void annotation(int var1, int var2) throws IOException {
      this.write16bit(var1);
      this.write16bit(var2);
   }

   public void memberValuePair(String var1) throws IOException {
      this.memberValuePair(this.pool.addUtf8Info(var1));
   }

   public void memberValuePair(int var1) throws IOException {
      this.write16bit(var1);
   }

   public void constValueIndex(boolean var1) throws IOException {
      this.constValueIndex(90, this.pool.addIntegerInfo(var1 ? 1 : 0));
   }

   public void constValueIndex(byte var1) throws IOException {
      this.constValueIndex(66, this.pool.addIntegerInfo(var1));
   }

   public void constValueIndex(char var1) throws IOException {
      this.constValueIndex(67, this.pool.addIntegerInfo(var1));
   }

   public void constValueIndex(short var1) throws IOException {
      this.constValueIndex(83, this.pool.addIntegerInfo(var1));
   }

   public void constValueIndex(int var1) throws IOException {
      this.constValueIndex(73, this.pool.addIntegerInfo(var1));
   }

   public void constValueIndex(long var1) throws IOException {
      this.constValueIndex(74, this.pool.addLongInfo(var1));
   }

   public void constValueIndex(float var1) throws IOException {
      this.constValueIndex(70, this.pool.addFloatInfo(var1));
   }

   public void constValueIndex(double var1) throws IOException {
      this.constValueIndex(68, this.pool.addDoubleInfo(var1));
   }

   public void constValueIndex(String var1) throws IOException {
      this.constValueIndex(115, this.pool.addUtf8Info(var1));
   }

   public void constValueIndex(int var1, int var2) throws IOException {
      this.output.write(var1);
      this.write16bit(var2);
   }

   public void enumConstValue(String var1, String var2) throws IOException {
      this.enumConstValue(this.pool.addUtf8Info(var1), this.pool.addUtf8Info(var2));
   }

   public void enumConstValue(int var1, int var2) throws IOException {
      this.output.write(101);
      this.write16bit(var1);
      this.write16bit(var2);
   }

   public void classInfoIndex(String var1) throws IOException {
      this.classInfoIndex(this.pool.addUtf8Info(var1));
   }

   public void classInfoIndex(int var1) throws IOException {
      this.output.write(99);
      this.write16bit(var1);
   }

   public void annotationValue() throws IOException {
      this.output.write(64);
   }

   public void arrayValue(int var1) throws IOException {
      this.output.write(91);
      this.write16bit(var1);
   }

   protected void write16bit(int var1) throws IOException {
      byte[] var2 = new byte[2];
      ByteArray.write16bit(var1, var2, 0);
      this.output.write(var2);
   }
}
