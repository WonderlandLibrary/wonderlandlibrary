package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class LocalVariableTypeAttribute extends LocalVariableAttribute {
   public static final String tag = "LocalVariableTypeTable";

   public LocalVariableTypeAttribute(ConstPool var1) {
      super(var1, "LocalVariableTypeTable", new byte[2]);
      ByteArray.write16bit(0, this.info, 0);
   }

   LocalVariableTypeAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   private LocalVariableTypeAttribute(ConstPool var1, byte[] var2) {
      super(var1, "LocalVariableTypeTable", var2);
   }

   String renameEntry(String var1, String var2, String var3) {
      return SignatureAttribute.renameClass(var1, var2, var3);
   }

   String renameEntry(String var1, Map var2) {
      return SignatureAttribute.renameClass(var1, var2);
   }

   LocalVariableAttribute makeThisAttr(ConstPool var1, byte[] var2) {
      return new LocalVariableTypeAttribute(var1, var2);
   }
}
