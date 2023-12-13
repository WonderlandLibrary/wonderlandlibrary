package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SyntheticAttribute extends AttributeInfo {
   public static final String tag = "Synthetic";

   SyntheticAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public SyntheticAttribute(ConstPool var1) {
      super(var1, "Synthetic", new byte[0]);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return new SyntheticAttribute(var1);
   }
}
