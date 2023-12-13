package javassist;

public final class CtPrimitiveType extends CtClass {
   private char descriptor;
   private String wrapperName;
   private String getMethodName;
   private String mDescriptor;
   private int returnOp;
   private int arrayType;
   private int dataSize;

   CtPrimitiveType(String var1, char var2, String var3, String var4, String var5, int var6, int var7, int var8) {
      super(var1);
      this.descriptor = var2;
      this.wrapperName = var3;
      this.getMethodName = var4;
      this.mDescriptor = var5;
      this.returnOp = var6;
      this.arrayType = var7;
      this.dataSize = var8;
   }

   public boolean isPrimitive() {
      return true;
   }

   public int getModifiers() {
      return 17;
   }

   public char getDescriptor() {
      return this.descriptor;
   }

   public String getWrapperName() {
      return this.wrapperName;
   }

   public String getGetMethodName() {
      return this.getMethodName;
   }

   public String getGetMethodDescriptor() {
      return this.mDescriptor;
   }

   public int getReturnOp() {
      return this.returnOp;
   }

   public int getArrayType() {
      return this.arrayType;
   }

   public int getDataSize() {
      return this.dataSize;
   }
}
