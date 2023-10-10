package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class InterfaceMethodrefInfo extends MemberrefInfo {
   static final int tag = 11;

   public InterfaceMethodrefInfo(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public InterfaceMethodrefInfo(DataInputStream var1, int var2) throws IOException {
      super(var1, var2);
   }

   public int getTag() {
      return 11;
   }

   public String getTagName() {
      return "Interface";
   }

   protected int copy2(ConstPool var1, int var2, int var3) {
      return var1.addInterfaceMethodrefInfo(var2, var3);
   }
}
