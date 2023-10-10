package javassist.bytecode.analysis;

import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;

public class Util implements Opcode {
   public static int getJumpTarget(int var0, CodeIterator var1) {
      int var2 = var1.byteAt(var0);
      var0 += var2 != 201 && var2 != 200 ? var1.s16bitAt(var0 + 1) : var1.s32bitAt(var0 + 1);
      return var0;
   }

   public static boolean isJumpInstruction(int var0) {
      return var0 >= 153 && var0 <= 168 || var0 == 198 || var0 == 199 || var0 == 201 || var0 == 200;
   }

   public static boolean isGoto(int var0) {
      return var0 == 167 || var0 == 200;
   }

   public static boolean isJsr(int var0) {
      return var0 == 168 || var0 == 201;
   }

   public static boolean isReturn(int var0) {
      return var0 >= 172 && var0 <= 177;
   }
}
