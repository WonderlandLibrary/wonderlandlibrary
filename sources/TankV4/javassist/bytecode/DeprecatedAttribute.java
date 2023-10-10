package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class DeprecatedAttribute extends AttributeInfo {
   public static final String tag = "Deprecated";

   DeprecatedAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public DeprecatedAttribute(ConstPool var1) {
      super(var1, "Deprecated", new byte[0]);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return new DeprecatedAttribute(var1);
   }
}
