package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class MethodrefInfo extends MemberrefInfo {
   static final int tag = 10;

   public MethodrefInfo(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public MethodrefInfo(DataInputStream var1, int var2) throws IOException {
      super(var1, var2);
   }

   public int getTag() {
      return 10;
   }

   public String getTagName() {
      return "Method";
   }

   protected int copy2(ConstPool var1, int var2, int var3) {
      return var1.addMethodrefInfo(var2, var3);
   }
}
