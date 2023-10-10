package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SourceFileAttribute extends AttributeInfo {
   public static final String tag = "SourceFile";

   SourceFileAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public SourceFileAttribute(ConstPool var1, String var2) {
      super(var1, "SourceFile");
      int var3 = var1.addUtf8Info(var2);
      byte[] var4 = new byte[]{(byte)(var3 >>> 8), (byte)var3};
      this.set(var4);
   }

   public String getFileName() {
      return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return new SourceFileAttribute(var1, this.getFileName());
   }
}
