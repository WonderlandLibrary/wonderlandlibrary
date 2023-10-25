package com.jcraft.jorbis;

class Util {
  static int ilog(int v) {
    int ret = 0;
    while (v != 0) {
      ret++;
      v >>>= 1;
    } 
    return ret;
  }
  
  static int ilog2(int v) {
    int ret = 0;
    while (v > 1) {
      ret++;
      v >>>= 1;
    } 
    return ret;
  }
  
  static int icount(int v) {
    int ret = 0;
    while (v != 0) {
      ret += v & 0x1;
      v >>>= 1;
    } 
    return ret;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */