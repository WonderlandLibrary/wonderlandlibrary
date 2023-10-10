package javassist.bytecode.annotation;

import java.io.IOException;
import java.io.OutputStream;
import javassist.bytecode.ConstPool;

public class TypeAnnotationsWriter extends AnnotationsWriter {
   public TypeAnnotationsWriter(OutputStream var1, ConstPool var2) {
      super(var1, var2);
   }

   public void numAnnotations(int var1) throws IOException {
      super.numAnnotations(var1);
   }

   public void typeParameterTarget(int var1, int var2) throws IOException {
      this.output.write(var1);
      this.output.write(var2);
   }

   public void supertypeTarget(int var1) throws IOException {
      this.output.write(16);
      this.write16bit(var1);
   }

   public void typeParameterBoundTarget(int var1, int var2, int var3) throws IOException {
      this.output.write(var1);
      this.output.write(var2);
      this.output.write(var3);
   }

   public void emptyTarget(int var1) throws IOException {
      this.output.write(var1);
   }

   public void formalParameterTarget(int var1) throws IOException {
      this.output.write(22);
      this.output.write(var1);
   }

   public void throwsTarget(int var1) throws IOException {
      this.output.write(23);
      this.write16bit(var1);
   }

   public void localVarTarget(int var1, int var2) throws IOException {
      this.output.write(var1);
      this.write16bit(var2);
   }

   public void localVarTargetTable(int var1, int var2, int var3) throws IOException {
      this.write16bit(var1);
      this.write16bit(var2);
      this.write16bit(var3);
   }

   public void catchTarget(int var1) throws IOException {
      this.output.write(66);
      this.write16bit(var1);
   }

   public void offsetTarget(int var1, int var2) throws IOException {
      this.output.write(var1);
      this.write16bit(var2);
   }

   public void typeArgumentTarget(int var1, int var2, int var3) throws IOException {
      this.output.write(var1);
      this.write16bit(var2);
      this.output.write(var3);
   }

   public void typePath(int var1) throws IOException {
      this.output.write(var1);
   }

   public void typePathPath(int var1, int var2) throws IOException {
      this.output.write(var1);
      this.output.write(var2);
   }
}
