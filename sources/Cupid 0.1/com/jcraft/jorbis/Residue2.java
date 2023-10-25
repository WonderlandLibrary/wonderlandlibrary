package com.jcraft.jorbis;

class Residue2 extends Residue0 {
  int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
    int i = 0;
    for (i = 0; i < ch && 
      nonzero[i] == 0; i++);
    if (i == ch)
      return 0; 
    return _2inverse(vb, vl, in, ch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\Residue2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */