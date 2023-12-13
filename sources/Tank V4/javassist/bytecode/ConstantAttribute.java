package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ConstantAttribute extends AttributeInfo {
   public static final String tag = "ConstantValue";

   ConstantAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public ConstantAttribute(ConstPool var1, int var2) {
      super(var1, "ConstantValue");
      byte[] var3 = new byte[]{(byte)(var2 >>> 8), (byte)var2};
      this.set(var3);
   }

   public int getConstantValue() {
      return ByteArray.readU16bit(this.get(), 0);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      int var3 = this.getConstPool().copy(this.getConstantValue(), var1, var2);
      return new ConstantAttribute(var1, var3);
   }
}
