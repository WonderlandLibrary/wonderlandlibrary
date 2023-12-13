package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class FieldrefInfo extends MemberrefInfo {
   static final int tag = 9;

   public FieldrefInfo(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public FieldrefInfo(DataInputStream var1, int var2) throws IOException {
      super(var1, var2);
   }

   public int getTag() {
      return 9;
   }

   public String getTagName() {
      return "Field";
   }

   protected int copy2(ConstPool var1, int var2, int var3) {
      return var1.addFieldrefInfo(var2, var3);
   }
}
