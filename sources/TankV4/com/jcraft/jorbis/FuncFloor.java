package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncFloor {
   public static FuncFloor[] floor_P = new FuncFloor[]{new Floor0(), new Floor1()};

   abstract void pack(Object var1, Buffer var2);

   abstract Object unpack(Info var1, Buffer var2);

   abstract Object look(DspState var1, InfoMode var2, Object var3);

   abstract void free_info(Object var1);

   abstract void free_look(Object var1);

   abstract void free_state(Object var1);

   abstract int forward(Block var1, Object var2, float[] var3, float[] var4, Object var5);

   abstract Object inverse1(Block var1, Object var2, Object var3);

   abstract int inverse2(Block var1, Object var2, Object var3, float[] var4);
}
