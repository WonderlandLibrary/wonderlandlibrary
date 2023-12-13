package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class Utf8Info extends ConstInfo {
   static final int tag = 1;
   String string;

   public Utf8Info(String var1, int var2) {
      super(var2);
      this.string = var1;
   }

   public Utf8Info(DataInputStream var1, int var2) throws IOException {
      super(var2);
      this.string = var1.readUTF();
   }

   public int hashCode() {
      return this.string.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof Utf8Info && ((Utf8Info)var1).string.equals(this.string);
   }

   public int getTag() {
      return 1;
   }

   public int copy(ConstPool var1, ConstPool var2, Map var3) {
      return var2.addUtf8Info(this.string);
   }

   public void write(DataOutputStream var1) throws IOException {
      var1.writeByte(1);
      var1.writeUTF(this.string);
   }

   public void print(PrintWriter var1) {
      var1.print("UTF8 \"");
      var1.print(this.string);
      var1.println("\"");
   }
}
