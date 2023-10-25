package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;

final class BidiWriter {
  static final char LRM_CHAR = '‎';
  
  static final char RLM_CHAR = '‏';
  
  static final int MASK_R_AL = 8194;
  
  private static boolean IsCombining(int type) {
    return ((1 << type & 0x1C0) != 0);
  }
  
  private static String doWriteForward(String src, int options) {
    StringBuffer stringBuffer1;
    StringBuilder stringBuilder;
    switch (options & 0xA) {
      case 0:
        return src;
      case 2:
        stringBuffer1 = new StringBuffer(src.length());
        i = 0;
        while (true) {
          int c = UTF16.charAt(src, i);
          i += UTF16.getCharCount(c);
          UTF16.append(stringBuffer1, UCharacter.getMirror(c));
          if (i >= src.length())
            return stringBuffer1.toString(); 
        } 
      case 8:
        stringBuilder = new StringBuilder(src.length());
        i = 0;
        while (true) {
          char c = src.charAt(i++);
          if (!Bidi.IsBidiControlChar(c))
            stringBuilder.append(c); 
          if (i >= src.length())
            return stringBuilder.toString(); 
        } 
    } 
    StringBuffer dest = new StringBuffer(src.length());
    int i = 0;
    while (true) {
      int c = UTF16.charAt(src, i);
      i += UTF16.getCharCount(c);
      if (!Bidi.IsBidiControlChar(c))
        UTF16.append(dest, UCharacter.getMirror(c)); 
      if (i >= src.length())
        return dest.toString(); 
    } 
  }
  
  private static String doWriteForward(char[] text, int start, int limit, int options) {
    return doWriteForward(new String(text, start, limit - start), options);
  }
  
  static String writeReverse(String src, int options) {
    StringBuffer dest = new StringBuffer(src.length());
    switch (options & 0xB) {
      case 0:
        srcLength = src.length();
        do {
          int i = srcLength;
          srcLength -= UTF16.getCharCount(UTF16.charAt(src, srcLength - 1));
          dest.append(src.substring(srcLength, i));
        } while (srcLength > 0);
        return dest.toString();
      case 1:
        srcLength = src.length();
        do {
          int c, i = srcLength;
          do {
            c = UTF16.charAt(src, srcLength - 1);
            srcLength -= UTF16.getCharCount(c);
          } while (srcLength > 0 && IsCombining(UCharacter.getType(c)));
          dest.append(src.substring(srcLength, i));
        } while (srcLength > 0);
        return dest.toString();
    } 
    int srcLength = src.length();
    do {
      int i = srcLength;
      int c = UTF16.charAt(src, srcLength - 1);
      srcLength -= UTF16.getCharCount(c);
      if ((options & 0x1) != 0)
        while (srcLength > 0 && IsCombining(UCharacter.getType(c))) {
          c = UTF16.charAt(src, srcLength - 1);
          srcLength -= UTF16.getCharCount(c);
        }  
      if ((options & 0x8) != 0 && Bidi.IsBidiControlChar(c))
        continue; 
      int j = srcLength;
      if ((options & 0x2) != 0) {
        c = UCharacter.getMirror(c);
        UTF16.append(dest, c);
        j += UTF16.getCharCount(c);
      } 
      dest.append(src.substring(j, i));
    } while (srcLength > 0);
    return dest.toString();
  }
  
  static String doWriteReverse(char[] text, int start, int limit, int options) {
    return writeReverse(new String(text, start, limit - start), options);
  }
  
  static String writeReordered(Bidi bidi, int options) {
    char[] text = bidi.text;
    int runCount = bidi.countRuns();
    if ((bidi.reorderingOptions & 0x1) != 0) {
      options |= 0x4;
      options &= 0xFFFFFFF7;
    } 
    if ((bidi.reorderingOptions & 0x2) != 0) {
      options |= 0x8;
      options &= 0xFFFFFFFB;
    } 
    if (bidi.reorderingMode != 4 && bidi.reorderingMode != 5 && bidi.reorderingMode != 6 && bidi.reorderingMode != 3)
      options &= 0xFFFFFFFB; 
    StringBuilder dest = new StringBuilder(((options & 0x4) != 0) ? (bidi.length * 2) : bidi.length);
    if ((options & 0x10) == 0) {
      if ((options & 0x4) == 0) {
        for (int run = 0; run < runCount; run++) {
          BidiRun bidiRun = bidi.getVisualRun(run);
          if (bidiRun.isEvenRun()) {
            dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
          } else {
            dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
          } 
        } 
      } else {
        byte[] dirProps = bidi.dirProps;
        for (int run = 0; run < runCount; run++) {
          BidiRun bidiRun = bidi.getVisualRun(run);
          int markFlag = 0;
          markFlag = (bidi.runs[run]).insertRemove;
          if (markFlag < 0)
            markFlag = 0; 
          if (bidiRun.isEvenRun()) {
            char uc;
            if (bidi.isInverse() && dirProps[bidiRun.start] != 0)
              markFlag |= 0x1; 
            if ((markFlag & 0x1) != 0) {
              uc = '‎';
            } else if ((markFlag & 0x4) != 0) {
              uc = '‏';
            } else {
              uc = Character.MIN_VALUE;
            } 
            if (uc != '\000')
              dest.append(uc); 
            dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
            if (bidi.isInverse() && dirProps[bidiRun.limit - 1] != 0)
              markFlag |= 0x2; 
            if ((markFlag & 0x2) != 0) {
              uc = '‎';
            } else if ((markFlag & 0x8) != 0) {
              uc = '‏';
            } else {
              uc = Character.MIN_VALUE;
            } 
            if (uc != '\000')
              dest.append(uc); 
          } else {
            char uc;
            if (bidi.isInverse() && !bidi.testDirPropFlagAt(8194, bidiRun.limit - 1))
              markFlag |= 0x4; 
            if ((markFlag & 0x1) != 0) {
              uc = '‎';
            } else if ((markFlag & 0x4) != 0) {
              uc = '‏';
            } else {
              uc = Character.MIN_VALUE;
            } 
            if (uc != '\000')
              dest.append(uc); 
            dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
            if (bidi.isInverse() && (0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0)
              markFlag |= 0x8; 
            if ((markFlag & 0x2) != 0) {
              uc = '‎';
            } else if ((markFlag & 0x8) != 0) {
              uc = '‏';
            } else {
              uc = Character.MIN_VALUE;
            } 
            if (uc != '\000')
              dest.append(uc); 
          } 
        } 
      } 
    } else if ((options & 0x4) == 0) {
      for (int run = runCount; --run >= 0; ) {
        BidiRun bidiRun = bidi.getVisualRun(run);
        if (bidiRun.isEvenRun()) {
          dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
          continue;
        } 
        dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options));
      } 
    } else {
      byte[] dirProps = bidi.dirProps;
      for (int run = runCount; --run >= 0; ) {
        BidiRun bidiRun = bidi.getVisualRun(run);
        if (bidiRun.isEvenRun()) {
          if (dirProps[bidiRun.limit - 1] != 0)
            dest.append('‎'); 
          dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
          if (dirProps[bidiRun.start] != 0)
            dest.append('‎'); 
          continue;
        } 
        if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0)
          dest.append('‏'); 
        dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options));
        if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.limit - 1])) == 0)
          dest.append('‏'); 
      } 
    } 
    return dest.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BidiWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */