package com.ibm.icu.impl;

public class ImplicitCEGenerator {
  static final boolean DEBUG = false;
  
  static final long topByte = 4278190080L;
  
  static final long bottomByte = 255L;
  
  static final long fourBytes = 4294967295L;
  
  static final int MAX_INPUT = 2228225;
  
  public static final int CJK_BASE = 19968;
  
  public static final int CJK_LIMIT = 40909;
  
  public static final int CJK_COMPAT_USED_BASE = 64014;
  
  public static final int CJK_COMPAT_USED_LIMIT = 64048;
  
  public static final int CJK_A_BASE = 13312;
  
  public static final int CJK_A_LIMIT = 19894;
  
  public static final int CJK_B_BASE = 131072;
  
  public static final int CJK_B_LIMIT = 173783;
  
  public static final int CJK_C_BASE = 173824;
  
  public static final int CJK_C_LIMIT = 177973;
  
  public static final int CJK_D_BASE = 177984;
  
  public static final int CJK_D_LIMIT = 178206;
  
  int final3Multiplier;
  
  int final4Multiplier;
  
  int final3Count;
  
  int final4Count;
  
  int medialCount;
  
  int min3Primary;
  
  int min4Primary;
  
  int max4Primary;
  
  int minTrail;
  
  int maxTrail;
  
  int max3Trail;
  
  int max4Trail;
  
  int min4Boundary;
  
  public int getGap4() {
    return this.final4Multiplier - 1;
  }
  
  public int getGap3() {
    return this.final3Multiplier - 1;
  }
  
  public ImplicitCEGenerator(int minPrimary, int maxPrimary) {
    this(minPrimary, maxPrimary, 4, 254, 1, 1);
  }
  
  public ImplicitCEGenerator(int minPrimary, int maxPrimary, int minTrail, int maxTrail, int gap3, int primaries3count) {
    if (minPrimary < 0 || minPrimary >= maxPrimary || maxPrimary > 255)
      throw new IllegalArgumentException("bad lead bytes"); 
    if (minTrail < 0 || minTrail >= maxTrail || maxTrail > 255)
      throw new IllegalArgumentException("bad trail bytes"); 
    if (primaries3count < 1)
      throw new IllegalArgumentException("bad three-byte primaries"); 
    this.minTrail = minTrail;
    this.maxTrail = maxTrail;
    this.min3Primary = minPrimary;
    this.max4Primary = maxPrimary;
    this.final3Multiplier = gap3 + 1;
    this.final3Count = (maxTrail - minTrail + 1) / this.final3Multiplier;
    this.max3Trail = minTrail + (this.final3Count - 1) * this.final3Multiplier;
    this.medialCount = maxTrail - minTrail + 1;
    int threeByteCount = this.medialCount * this.final3Count;
    int primariesAvailable = maxPrimary - minPrimary + 1;
    int primaries4count = primariesAvailable - primaries3count;
    int min3ByteCoverage = primaries3count * threeByteCount;
    this.min4Primary = minPrimary + primaries3count;
    this.min4Boundary = min3ByteCoverage;
    int totalNeeded = 2228225 - this.min4Boundary;
    int neededPerPrimaryByte = divideAndRoundUp(totalNeeded, primaries4count);
    int neededPerFinalByte = divideAndRoundUp(neededPerPrimaryByte, this.medialCount * this.medialCount);
    int gap4 = (maxTrail - minTrail - 1) / neededPerFinalByte;
    if (gap4 < 1)
      throw new IllegalArgumentException("must have larger gap4s"); 
    this.final4Multiplier = gap4 + 1;
    this.final4Count = neededPerFinalByte;
    this.max4Trail = minTrail + (this.final4Count - 1) * this.final4Multiplier;
    if (primaries4count * this.medialCount * this.medialCount * this.final4Count < 2228225)
      throw new IllegalArgumentException("internal error"); 
  }
  
  public static int divideAndRoundUp(int a, int b) {
    return 1 + (a - 1) / b;
  }
  
  public int getRawFromImplicit(int implicit) {
    int result, b3 = implicit & 0xFF;
    implicit >>= 8;
    int b2 = implicit & 0xFF;
    implicit >>= 8;
    int b1 = implicit & 0xFF;
    implicit >>= 8;
    int b0 = implicit & 0xFF;
    if (b0 < this.min3Primary || b0 > this.max4Primary || b1 < this.minTrail || b1 > this.maxTrail)
      return -1; 
    b1 -= this.minTrail;
    if (b0 < this.min4Primary) {
      if (b2 < this.minTrail || b2 > this.max3Trail || b3 != 0)
        return -1; 
      b2 -= this.minTrail;
      int remainder = b2 % this.final3Multiplier;
      if (remainder != 0)
        return -1; 
      b0 -= this.min3Primary;
      b2 /= this.final3Multiplier;
      result = (b0 * this.medialCount + b1) * this.final3Count + b2;
    } else {
      if (b2 < this.minTrail || b2 > this.maxTrail || b3 < this.minTrail || b3 > this.max4Trail)
        return -1; 
      b2 -= this.minTrail;
      b3 -= this.minTrail;
      int remainder = b3 % this.final4Multiplier;
      if (remainder != 0)
        return -1; 
      b3 /= this.final4Multiplier;
      b0 -= this.min4Primary;
      result = ((b0 * this.medialCount + b1) * this.medialCount + b2) * this.final4Count + b3 + this.min4Boundary;
    } 
    if (result < 0 || result > 2228225)
      return -1; 
    return result;
  }
  
  public int getImplicitFromRaw(int cp) {
    if (cp < 0 || cp > 2228225)
      throw new IllegalArgumentException("Code point out of range " + Utility.hex(cp)); 
    int last0 = cp - this.min4Boundary;
    if (last0 < 0) {
      int i = cp / this.final3Count;
      last0 = cp % this.final3Count;
      int j = i / this.medialCount;
      i %= this.medialCount;
      last0 = this.minTrail + last0 * this.final3Multiplier;
      i = this.minTrail + i;
      j = this.min3Primary + j;
      if (j >= this.min4Primary)
        throw new IllegalArgumentException("4-byte out of range: " + Utility.hex(cp) + ", " + Utility.hex(j)); 
      return (j << 24) + (i << 16) + (last0 << 8);
    } 
    int last1 = last0 / this.final4Count;
    last0 %= this.final4Count;
    int last2 = last1 / this.medialCount;
    last1 %= this.medialCount;
    int last3 = last2 / this.medialCount;
    last2 %= this.medialCount;
    last0 = this.minTrail + last0 * this.final4Multiplier;
    last1 = this.minTrail + last1;
    last2 = this.minTrail + last2;
    last3 = this.min4Primary + last3;
    if (last3 > this.max4Primary)
      throw new IllegalArgumentException("4-byte out of range: " + Utility.hex(cp) + ", " + Utility.hex(last3)); 
    return (last3 << 24) + (last2 << 16) + (last1 << 8) + last0;
  }
  
  public int getImplicitFromCodePoint(int cp) {
    cp = swapCJK(cp) + 1;
    return getImplicitFromRaw(cp);
  }
  
  static int NON_CJK_OFFSET = 1114112;
  
  public static int swapCJK(int i) {
    if (i >= 19968) {
      if (i < 40909)
        return i - 19968; 
      if (i < 64014)
        return i + NON_CJK_OFFSET; 
      if (i < 64048)
        return i - 64014 + 20941; 
      if (i < 131072)
        return i + NON_CJK_OFFSET; 
      if (i < 173783)
        return i; 
      if (i < 173824)
        return i + NON_CJK_OFFSET; 
      if (i < 177973)
        return i; 
      if (i < 177984)
        return i + NON_CJK_OFFSET; 
      if (i < 178206)
        return i; 
      return i + NON_CJK_OFFSET;
    } 
    if (i < 13312)
      return i + NON_CJK_OFFSET; 
    if (i < 19894)
      return i - 13312 + 20941 + 34; 
    return i + NON_CJK_OFFSET;
  }
  
  public int getMinTrail() {
    return this.minTrail;
  }
  
  public int getMaxTrail() {
    return this.maxTrail;
  }
  
  public int getCodePointFromRaw(int i) {
    i--;
    int result = 0;
    if (i >= NON_CJK_OFFSET) {
      result = i - NON_CJK_OFFSET;
    } else if (i >= 131072) {
      result = i;
    } else if (i < 40869) {
      if (i < 20941) {
        result = i + 19968;
      } else if (i < 20975) {
        result = i + 64014 - 20941;
      } else {
        result = i + 13312 - 20941 - 34;
      } 
    } else {
      result = -1;
    } 
    return result;
  }
  
  public int getRawFromCodePoint(int i) {
    return swapCJK(i) + 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ImplicitCEGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */