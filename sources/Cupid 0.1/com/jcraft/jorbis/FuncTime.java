package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncTime {
  public static FuncTime[] time_P = new FuncTime[] { new Time0() };
  
  abstract void pack(Object paramObject, Buffer paramBuffer);
  
  abstract Object unpack(Info paramInfo, Buffer paramBuffer);
  
  abstract Object look(DspState paramDspState, InfoMode paramInfoMode, Object paramObject);
  
  abstract void free_info(Object paramObject);
  
  abstract void free_look(Object paramObject);
  
  abstract int inverse(Block paramBlock, Object paramObject, float[] paramArrayOffloat1, float[] paramArrayOffloat2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\FuncTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */