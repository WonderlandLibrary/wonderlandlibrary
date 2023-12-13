package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class StringInfo extends ConstInfo {
   static final int tag = 8;
   int string;

   public StringInfo(int var1, int var2) {
      super(var2);
      this.string = var1;
   }

   public StringInfo(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.string = var1.readUnsignedShort();
   }

   public int hashCode() {
      return this.string;
   }

   public boolean equals(Object var1) {
      return var1 instanceof StringInfo && ((StringInfo)var1).string == this.string;
   }

   public int getTag() {
      return 8;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addStringInfo(var1.getUtf8Info(this.string));
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(8);
      var1.writeShort(this.string);
   }

   public void print(PrintWriter var1) {
      var1.print("String #");
      var1.println(this.string);
   }
}
