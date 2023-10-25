package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import java.io.InputStream;

public abstract class Normalizer2 {
  public enum Mode {
    COMPOSE, DECOMPOSE, FCD, COMPOSE_CONTIGUOUS;
  }
  
  public static Normalizer2 getNFCInstance() {
    return (Normalizer2)(Norm2AllModes.getNFCInstance()).comp;
  }
  
  public static Normalizer2 getNFDInstance() {
    return (Normalizer2)(Norm2AllModes.getNFCInstance()).decomp;
  }
  
  public static Normalizer2 getNFKCInstance() {
    return (Normalizer2)(Norm2AllModes.getNFKCInstance()).comp;
  }
  
  public static Normalizer2 getNFKDInstance() {
    return (Normalizer2)(Norm2AllModes.getNFKCInstance()).decomp;
  }
  
  public static Normalizer2 getNFKCCasefoldInstance() {
    return (Normalizer2)(Norm2AllModes.getNFKC_CFInstance()).comp;
  }
  
  public static Normalizer2 getInstance(InputStream data, String name, Mode mode) {
    Norm2AllModes all2Modes = Norm2AllModes.getInstance(data, name);
    switch (mode) {
      case COMPOSE:
        return (Normalizer2)all2Modes.comp;
      case DECOMPOSE:
        return (Normalizer2)all2Modes.decomp;
      case FCD:
        return (Normalizer2)all2Modes.fcd;
      case COMPOSE_CONTIGUOUS:
        return (Normalizer2)all2Modes.fcc;
    } 
    return null;
  }
  
  public String normalize(CharSequence src) {
    if (src instanceof String) {
      int spanLength = spanQuickCheckYes(src);
      if (spanLength == src.length())
        return (String)src; 
      StringBuilder sb = (new StringBuilder(src.length())).append(src, 0, spanLength);
      return normalizeSecondAndAppend(sb, src.subSequence(spanLength, src.length())).toString();
    } 
    return normalize(src, new StringBuilder(src.length())).toString();
  }
  
  public abstract StringBuilder normalize(CharSequence paramCharSequence, StringBuilder paramStringBuilder);
  
  public abstract Appendable normalize(CharSequence paramCharSequence, Appendable paramAppendable);
  
  public abstract StringBuilder normalizeSecondAndAppend(StringBuilder paramStringBuilder, CharSequence paramCharSequence);
  
  public abstract StringBuilder append(StringBuilder paramStringBuilder, CharSequence paramCharSequence);
  
  public abstract String getDecomposition(int paramInt);
  
  public String getRawDecomposition(int c) {
    return null;
  }
  
  public int composePair(int a, int b) {
    return -1;
  }
  
  public int getCombiningClass(int c) {
    return 0;
  }
  
  public abstract boolean isNormalized(CharSequence paramCharSequence);
  
  public abstract Normalizer.QuickCheckResult quickCheck(CharSequence paramCharSequence);
  
  public abstract int spanQuickCheckYes(CharSequence paramCharSequence);
  
  public abstract boolean hasBoundaryBefore(int paramInt);
  
  public abstract boolean hasBoundaryAfter(int paramInt);
  
  public abstract boolean isInert(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\Normalizer2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */