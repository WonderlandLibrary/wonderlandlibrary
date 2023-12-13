package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class EnclosingMethodAttribute extends AttributeInfo {
   public static final String tag = "EnclosingMethod";

   EnclosingMethodAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public EnclosingMethodAttribute(ConstPool var1, String var2, String var3, String var4) {
      super(var1, "EnclosingMethod");
      int var5 = var1.addClassInfo(var2);
      int var6 = var1.addNameAndTypeInfo(var3, var4);
      byte[] var7 = new byte[]{(byte)(var5 >>> 8), (byte)var5, (byte)(var6 >>> 8), (byte)var6};
      this.set(var7);
   }

   public EnclosingMethodAttribute(ConstPool var1, String var2) {
      super(var1, "EnclosingMethod");
      int var3 = var1.addClassInfo(var2);
      byte var4 = 0;
      byte[] var5 = new byte[]{(byte)(var3 >>> 8), (byte)var3, (byte)(var4 >>> 8), (byte)var4};
      this.set(var5);
   }

   public int classIndex() {
      return ByteArray.readU16bit(this.get(), 0);
   }

   public int methodIndex() {
      return ByteArray.readU16bit(this.get(), 2);
   }

   public String className() {
      return this.getConstPool().getClassInfo(this.classIndex());
   }

   public String methodName() {
      ConstPool var1 = this.getConstPool();
      int var2 = this.methodIndex();
      if (var2 == 0) {
         return "<clinit>";
      } else {
         int var3 = var1.getNameAndTypeName(var2);
         return var1.getUtf8Info(var3);
      }
   }

   public String methodDescriptor() {
      ConstPool var1 = this.getConstPool();
      int var2 = this.methodIndex();
      int var3 = var1.getNameAndTypeDescriptor(var2);
      return var1.getUtf8Info(var3);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return this.methodIndex() == 0 ? new EnclosingMethodAttribute(var1, this.className()) : new EnclosingMethodAttribute(var1, this.className(), this.methodName(), this.methodDescriptor());
   }
}
