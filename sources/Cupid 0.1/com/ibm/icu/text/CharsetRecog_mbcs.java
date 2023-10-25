package com.ibm.icu.text;

import java.util.Arrays;

abstract class CharsetRecog_mbcs extends CharsetRecognizer {
  abstract String getName();
  
  int match(CharsetDetector det, int[] commonChars) {
    int singleByteCharCount = 0;
    int doubleByteCharCount = 0;
    int commonCharCount = 0;
    int badCharCount = 0;
    int totalCharCount = 0;
    int confidence = 0;
    iteratedChar iter = new iteratedChar();
    iter.reset();
    while (nextChar(iter, det)) {
      totalCharCount++;
      if (iter.error) {
        badCharCount++;
      } else {
        long cv = iter.charValue & 0xFFFFFFFFL;
        if (cv <= 255L) {
          singleByteCharCount++;
        } else {
          doubleByteCharCount++;
          if (commonChars != null)
            if (Arrays.binarySearch(commonChars, (int)cv) >= 0)
              commonCharCount++;  
        } 
      } 
      if (badCharCount >= 2 && badCharCount * 5 >= doubleByteCharCount)
        return confidence; 
    } 
    if (doubleByteCharCount <= 10 && badCharCount == 0) {
      if (doubleByteCharCount == 0 && totalCharCount < 10) {
        confidence = 0;
      } else {
        confidence = 10;
      } 
    } else if (doubleByteCharCount < 20 * badCharCount) {
      confidence = 0;
    } else if (commonChars == null) {
      confidence = 30 + doubleByteCharCount - 20 * badCharCount;
      if (confidence > 100)
        confidence = 100; 
    } else {
      double maxVal = Math.log((doubleByteCharCount / 4.0F));
      double scaleFactor = 90.0D / maxVal;
      confidence = (int)(Math.log((commonCharCount + 1)) * scaleFactor + 10.0D);
      confidence = Math.min(confidence, 100);
    } 
    return confidence;
  }
  
  abstract boolean nextChar(iteratedChar paramiteratedChar, CharsetDetector paramCharsetDetector);
  
  static class iteratedChar {
    int charValue = 0;
    
    int index = 0;
    
    int nextIndex = 0;
    
    boolean error = false;
    
    boolean done = false;
    
    void reset() {
      this.charValue = 0;
      this.index = -1;
      this.nextIndex = 0;
      this.error = false;
      this.done = false;
    }
    
    int nextByte(CharsetDetector det) {
      if (this.nextIndex >= det.fRawLength) {
        this.done = true;
        return -1;
      } 
      int byteValue = det.fRawInput[this.nextIndex++] & 0xFF;
      return byteValue;
    }
  }
  
  static class CharsetRecog_sjis extends CharsetRecog_mbcs {
    static int[] commonChars = new int[] { 
        33088, 33089, 33090, 33093, 33115, 33129, 33130, 33141, 33142, 33440, 
        33442, 33444, 33449, 33450, 33451, 33453, 33455, 33457, 33459, 33461, 
        33463, 33469, 33470, 33473, 33476, 33477, 33478, 33480, 33481, 33484, 
        33485, 33500, 33504, 33511, 33512, 33513, 33514, 33520, 33521, 33601, 
        33603, 33614, 33615, 33624, 33630, 33634, 33639, 33653, 33654, 33673, 
        33674, 33675, 33677, 33683, 36502, 37882, 38314 };
    
    boolean nextChar(CharsetRecog_mbcs.iteratedChar it, CharsetDetector det) {
      it.index = it.nextIndex;
      it.error = false;
      int firstByte = it.charValue = it.nextByte(det);
      if (firstByte < 0)
        return false; 
      if (firstByte <= 127 || (firstByte > 160 && firstByte <= 223))
        return true; 
      int secondByte = it.nextByte(det);
      if (secondByte < 0)
        return false; 
      it.charValue = firstByte << 8 | secondByte;
      if ((secondByte < 64 || secondByte > 127) && (secondByte < 128 || secondByte > 255))
        it.error = true; 
      return true;
    }
    
    CharsetMatch match(CharsetDetector det) {
      int confidence = match(det, commonChars);
      return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
    }
    
    String getName() {
      return "Shift_JIS";
    }
    
    public String getLanguage() {
      return "ja";
    }
  }
  
  static class CharsetRecog_big5 extends CharsetRecog_mbcs {
    static int[] commonChars = new int[] { 
        41280, 41281, 41282, 41283, 41287, 41289, 41333, 41334, 42048, 42054, 
        42055, 42056, 42065, 42068, 42071, 42084, 42090, 42092, 42103, 42147, 
        42148, 42151, 42177, 42190, 42193, 42207, 42216, 42237, 42304, 42312, 
        42328, 42345, 42445, 42471, 42583, 42593, 42594, 42600, 42608, 42664, 
        42675, 42681, 42707, 42715, 42726, 42738, 42816, 42833, 42841, 42970, 
        43171, 43173, 43181, 43217, 43219, 43236, 43260, 43456, 43474, 43507, 
        43627, 43706, 43710, 43724, 43772, 44103, 44111, 44208, 44242, 44377, 
        44745, 45024, 45290, 45423, 45747, 45764, 45935, 46156, 46158, 46412, 
        46501, 46525, 46544, 46552, 46705, 47085, 47207, 47428, 47832, 47940, 
        48033, 48593, 49860, 50105, 50240, 50271 };
    
    boolean nextChar(CharsetRecog_mbcs.iteratedChar it, CharsetDetector det) {
      it.index = it.nextIndex;
      it.error = false;
      int firstByte = it.charValue = it.nextByte(det);
      if (firstByte < 0)
        return false; 
      if (firstByte <= 127 || firstByte == 255)
        return true; 
      int secondByte = it.nextByte(det);
      if (secondByte < 0)
        return false; 
      it.charValue = it.charValue << 8 | secondByte;
      if (secondByte < 64 || secondByte == 127 || secondByte == 255)
        it.error = true; 
      return true;
    }
    
    CharsetMatch match(CharsetDetector det) {
      int confidence = match(det, commonChars);
      return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
    }
    
    String getName() {
      return "Big5";
    }
    
    public String getLanguage() {
      return "zh";
    }
  }
  
  static abstract class CharsetRecog_euc extends CharsetRecog_mbcs {
    boolean nextChar(CharsetRecog_mbcs.iteratedChar it, CharsetDetector det) {
      it.index = it.nextIndex;
      it.error = false;
      int firstByte = 0;
      int secondByte = 0;
      int thirdByte = 0;
      firstByte = it.charValue = it.nextByte(det);
      if (firstByte < 0) {
        it.done = true;
      } else if (firstByte > 141) {
        secondByte = it.nextByte(det);
        it.charValue = it.charValue << 8 | secondByte;
        if (firstByte >= 161 && firstByte <= 254) {
          if (secondByte < 161)
            it.error = true; 
        } else if (firstByte == 142) {
          if (secondByte < 161)
            it.error = true; 
        } else if (firstByte == 143) {
          thirdByte = it.nextByte(det);
          it.charValue = it.charValue << 8 | thirdByte;
          if (thirdByte < 161)
            it.error = true; 
        } 
      } 
      return !it.done;
    }
    
    static class CharsetRecog_euc_jp extends CharsetRecog_euc {
      static int[] commonChars = new int[] { 
          41377, 41378, 41379, 41382, 41404, 41418, 41419, 41430, 41431, 42146, 
          42148, 42150, 42152, 42154, 42155, 42156, 42157, 42159, 42161, 42163, 
          42165, 42167, 42169, 42171, 42173, 42175, 42176, 42177, 42179, 42180, 
          42182, 42183, 42184, 42185, 42186, 42187, 42190, 42191, 42192, 42206, 
          42207, 42209, 42210, 42212, 42216, 42217, 42218, 42219, 42220, 42223, 
          42226, 42227, 42402, 42403, 42404, 42406, 42407, 42410, 42413, 42415, 
          42416, 42419, 42421, 42423, 42424, 42425, 42431, 42435, 42438, 42439, 
          42440, 42441, 42443, 42448, 42453, 42454, 42455, 42462, 42464, 42465, 
          42469, 42473, 42474, 42475, 42476, 42477, 42483, 47273, 47572, 47854, 
          48072, 48880, 49079, 50410, 50940, 51133, 51896, 51955, 52188, 52689 };
      
      String getName() {
        return "EUC-JP";
      }
      
      CharsetMatch match(CharsetDetector det) {
        int confidence = match(det, commonChars);
        return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
      }
      
      public String getLanguage() {
        return "ja";
      }
    }
    
    static class CharsetRecog_euc_kr extends CharsetRecog_euc {
      static int[] commonChars = new int[] { 
          45217, 45235, 45253, 45261, 45268, 45286, 45293, 45304, 45306, 45308, 
          45496, 45497, 45511, 45527, 45538, 45994, 46011, 46274, 46287, 46297, 
          46315, 46501, 46517, 46527, 46535, 46569, 46835, 47023, 47042, 47054, 
          47270, 47278, 47286, 47288, 47291, 47337, 47531, 47534, 47564, 47566, 
          47613, 47800, 47822, 47824, 47857, 48103, 48115, 48125, 48301, 48314, 
          48338, 48374, 48570, 48576, 48579, 48581, 48838, 48840, 48863, 48878, 
          48888, 48890, 49057, 49065, 49088, 49124, 49131, 49132, 49144, 49319, 
          49327, 49336, 49338, 49339, 49341, 49351, 49356, 49358, 49359, 49366, 
          49370, 49381, 49403, 49404, 49572, 49574, 49590, 49622, 49631, 49654, 
          49656, 50337, 50637, 50862, 51151, 51153, 51154, 51160, 51173, 51373 };
      
      String getName() {
        return "EUC-KR";
      }
      
      CharsetMatch match(CharsetDetector det) {
        int confidence = match(det, commonChars);
        return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
      }
      
      public String getLanguage() {
        return "ko";
      }
    }
  }
  
  static class CharsetRecog_gb_18030 extends CharsetRecog_mbcs {
    boolean nextChar(CharsetRecog_mbcs.iteratedChar it, CharsetDetector det) {
      // Byte code:
      //   0: aload_1
      //   1: aload_1
      //   2: getfield nextIndex : I
      //   5: putfield index : I
      //   8: aload_1
      //   9: iconst_0
      //   10: putfield error : Z
      //   13: iconst_0
      //   14: istore_3
      //   15: iconst_0
      //   16: istore #4
      //   18: iconst_0
      //   19: istore #5
      //   21: iconst_0
      //   22: istore #6
      //   24: aload_1
      //   25: aload_1
      //   26: aload_2
      //   27: invokevirtual nextByte : (Lcom/ibm/icu/text/CharsetDetector;)I
      //   30: dup_x1
      //   31: putfield charValue : I
      //   34: istore_3
      //   35: iload_3
      //   36: ifge -> 47
      //   39: aload_1
      //   40: iconst_1
      //   41: putfield done : Z
      //   44: goto -> 210
      //   47: iload_3
      //   48: sipush #128
      //   51: if_icmpgt -> 57
      //   54: goto -> 210
      //   57: aload_1
      //   58: aload_2
      //   59: invokevirtual nextByte : (Lcom/ibm/icu/text/CharsetDetector;)I
      //   62: istore #4
      //   64: aload_1
      //   65: aload_1
      //   66: getfield charValue : I
      //   69: bipush #8
      //   71: ishl
      //   72: iload #4
      //   74: ior
      //   75: putfield charValue : I
      //   78: iload_3
      //   79: sipush #129
      //   82: if_icmplt -> 210
      //   85: iload_3
      //   86: sipush #254
      //   89: if_icmpgt -> 210
      //   92: iload #4
      //   94: bipush #64
      //   96: if_icmplt -> 106
      //   99: iload #4
      //   101: bipush #126
      //   103: if_icmple -> 210
      //   106: iload #4
      //   108: bipush #80
      //   110: if_icmplt -> 124
      //   113: iload #4
      //   115: sipush #254
      //   118: if_icmpgt -> 124
      //   121: goto -> 210
      //   124: iload #4
      //   126: bipush #48
      //   128: if_icmplt -> 205
      //   131: iload #4
      //   133: bipush #57
      //   135: if_icmpgt -> 205
      //   138: aload_1
      //   139: aload_2
      //   140: invokevirtual nextByte : (Lcom/ibm/icu/text/CharsetDetector;)I
      //   143: istore #5
      //   145: iload #5
      //   147: sipush #129
      //   150: if_icmplt -> 205
      //   153: iload #5
      //   155: sipush #254
      //   158: if_icmpgt -> 205
      //   161: aload_1
      //   162: aload_2
      //   163: invokevirtual nextByte : (Lcom/ibm/icu/text/CharsetDetector;)I
      //   166: istore #6
      //   168: iload #6
      //   170: bipush #48
      //   172: if_icmplt -> 205
      //   175: iload #6
      //   177: bipush #57
      //   179: if_icmpgt -> 205
      //   182: aload_1
      //   183: aload_1
      //   184: getfield charValue : I
      //   187: bipush #16
      //   189: ishl
      //   190: iload #5
      //   192: bipush #8
      //   194: ishl
      //   195: ior
      //   196: iload #6
      //   198: ior
      //   199: putfield charValue : I
      //   202: goto -> 210
      //   205: aload_1
      //   206: iconst_1
      //   207: putfield error : Z
      //   210: aload_1
      //   211: getfield done : Z
      //   214: ifne -> 221
      //   217: iconst_1
      //   218: goto -> 222
      //   221: iconst_0
      //   222: ireturn
      // Line number table:
      //   Java source line number -> byte code offset
      //   #462	-> 0
      //   #463	-> 8
      //   #464	-> 13
      //   #465	-> 15
      //   #466	-> 18
      //   #467	-> 21
      //   #470	-> 24
      //   #472	-> 35
      //   #474	-> 39
      //   #475	-> 44
      //   #478	-> 47
      //   #480	-> 54
      //   #483	-> 57
      //   #484	-> 64
      //   #486	-> 78
      //   #488	-> 92
      //   #489	-> 121
      //   #493	-> 124
      //   #494	-> 138
      //   #496	-> 145
      //   #497	-> 161
      //   #499	-> 168
      //   #500	-> 182
      //   #501	-> 202
      //   #506	-> 205
      //   #511	-> 210
      // Local variable table:
      //   start	length	slot	name	descriptor
      //   0	223	0	this	Lcom/ibm/icu/text/CharsetRecog_mbcs$CharsetRecog_gb_18030;
      //   0	223	1	it	Lcom/ibm/icu/text/CharsetRecog_mbcs$iteratedChar;
      //   0	223	2	det	Lcom/ibm/icu/text/CharsetDetector;
      //   15	208	3	firstByte	I
      //   18	205	4	secondByte	I
      //   21	202	5	thirdByte	I
      //   24	199	6	fourthByte	I
    }
    
    static int[] commonChars = new int[] { 
        41377, 41378, 41379, 41380, 41392, 41393, 41457, 41459, 41889, 41900, 
        41914, 45480, 45496, 45502, 45755, 46025, 46070, 46323, 46525, 46532, 
        46563, 46767, 46804, 46816, 47010, 47016, 47037, 47062, 47069, 47284, 
        47327, 47350, 47531, 47561, 47576, 47610, 47613, 47821, 48039, 48086, 
        48097, 48122, 48316, 48347, 48382, 48588, 48845, 48861, 49076, 49094, 
        49097, 49332, 49389, 49611, 49883, 50119, 50396, 50410, 50636, 50935, 
        51192, 51371, 51403, 51413, 51431, 51663, 51706, 51889, 51893, 51911, 
        51920, 51926, 51957, 51965, 52460, 52728, 52906, 52932, 52946, 52965, 
        53173, 53186, 53206, 53442, 53445, 53456, 53460, 53671, 53930, 53938, 
        53941, 53947, 53972, 54211, 54224, 54269, 54466, 54490, 54754, 54992 };
    
    String getName() {
      return "GB18030";
    }
    
    CharsetMatch match(CharsetDetector det) {
      int confidence = match(det, commonChars);
      return (confidence == 0) ? null : new CharsetMatch(det, this, confidence);
    }
    
    public String getLanguage() {
      return "zh";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CharsetRecog_mbcs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */