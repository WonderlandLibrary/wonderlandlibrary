package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;

public final class ComposedCharIter {
  public static final char DONE = '￿';
  
  private final Normalizer2Impl n2impl;
  
  private String decompBuf;
  
  public ComposedCharIter() {
    this(false, 0);
  }
  
  public ComposedCharIter(boolean compat, int options) {
    if (compat) {
      this.n2impl = (Norm2AllModes.getNFKCInstance()).impl;
    } else {
      this.n2impl = (Norm2AllModes.getNFCInstance()).impl;
    } 
  }
  
  public boolean hasNext() {
    if (this.nextChar == -1)
      findNextChar(); 
    return (this.nextChar != -1);
  }
  
  public char next() {
    if (this.nextChar == -1)
      findNextChar(); 
    this.curChar = this.nextChar;
    this.nextChar = -1;
    return (char)this.curChar;
  }
  
  public String decomposition() {
    if (this.decompBuf != null)
      return this.decompBuf; 
    return "";
  }
  
  private void findNextChar() {
    // Byte code:
    //   0: aload_0
    //   1: getfield curChar : I
    //   4: iconst_1
    //   5: iadd
    //   6: istore_1
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield decompBuf : Ljava/lang/String;
    //   12: iload_1
    //   13: ldc 65535
    //   15: if_icmpge -> 46
    //   18: aload_0
    //   19: aload_0
    //   20: getfield n2impl : Lcom/ibm/icu/impl/Normalizer2Impl;
    //   23: iload_1
    //   24: invokevirtual getDecomposition : (I)Ljava/lang/String;
    //   27: putfield decompBuf : Ljava/lang/String;
    //   30: aload_0
    //   31: getfield decompBuf : Ljava/lang/String;
    //   34: ifnull -> 40
    //   37: goto -> 51
    //   40: iinc #1, 1
    //   43: goto -> 12
    //   46: iconst_m1
    //   47: istore_1
    //   48: goto -> 51
    //   51: aload_0
    //   52: iload_1
    //   53: putfield nextChar : I
    //   56: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #141	-> 0
    //   #142	-> 7
    //   #144	-> 12
    //   #145	-> 18
    //   #146	-> 30
    //   #149	-> 37
    //   #151	-> 40
    //   #153	-> 46
    //   #154	-> 48
    //   #157	-> 51
    //   #158	-> 56
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   0	57	0	this	Lcom/ibm/icu/text/ComposedCharIter;
    //   7	50	1	c	I
  }
  
  private int curChar = 0;
  
  private int nextChar = -1;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\ComposedCharIter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */