package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncResidue {
   public static FuncResidue[] residue_P = new FuncResidue[]{new Residue0(), new Residue1(), new Residue2()};

   abstract void pack(Object var1, Buffer var2);

   abstract Object unpack(Info var1, Buffer var2);

   abstract Object look(DspState var1, InfoMode var2, Object var3);

   abstract void free_info(Object var1);

   abstract void free_look(Object var1);

   abstract int inverse(Block var1, Object var2, float[][] var3, int[] var4, int var5);
}
