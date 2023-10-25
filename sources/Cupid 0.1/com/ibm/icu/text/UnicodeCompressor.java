package com.ibm.icu.text;

public final class UnicodeCompressor implements SCSU {
  private static boolean[] sSingleTagTable = new boolean[] { 
      false, true, true, true, true, true, true, true, true, false, 
      false, true, true, false, true, true, true, true, true, true, 
      true, true, true, true, true, true, true, true, true, true, 
      true, true, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false };
  
  private static boolean[] sUnicodeTagTable = new boolean[] { 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false, true, true, true, true, true, true, 
      true, true, true, true, true, true, true, true, true, true, 
      true, true, true, false, false, false, false, false, false, false, 
      false, false, false, false, false, false };
  
  private int fCurrentWindow = 0;
  
  private int[] fOffsets = new int[8];
  
  private int fMode = 0;
  
  private int[] fIndexCount = new int[256];
  
  private int[] fTimeStamps = new int[8];
  
  private int fTimeStamp = 0;
  
  public UnicodeCompressor() {
    reset();
  }
  
  public static byte[] compress(String buffer) {
    return compress(buffer.toCharArray(), 0, buffer.length());
  }
  
  public static byte[] compress(char[] buffer, int start, int limit) {
    UnicodeCompressor comp = new UnicodeCompressor();
    int len = Math.max(4, 3 * (limit - start) + 1);
    byte[] temp = new byte[len];
    int byteCount = comp.compress(buffer, start, limit, (int[])null, temp, 0, len);
    byte[] result = new byte[byteCount];
    System.arraycopy(temp, 0, result, 0, byteCount);
    return result;
  }
  
  public int compress(char[] charBuffer, int charBufferStart, int charBufferLimit, int[] charsRead, byte[] byteBuffer, int byteBufferStart, int byteBufferLimit) {
    int bytePos = byteBufferStart;
    int ucPos = charBufferStart;
    int curUC = -1;
    int curIndex = -1;
    int nextUC = -1;
    int forwardUC = -1;
    int whichWindow = 0;
    int hiByte = 0;
    int loByte = 0;
    if (byteBuffer.length < 4 || byteBufferLimit - byteBufferStart < 4)
      throw new IllegalArgumentException("byteBuffer.length < 4"); 
    label182: while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
      switch (this.fMode) {
        case 0:
          label172: while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
            curUC = charBuffer[ucPos++];
            if (ucPos < charBufferLimit) {
              nextUC = charBuffer[ucPos];
            } else {
              nextUC = -1;
            } 
            if (curUC < 128) {
              loByte = curUC & 0xFF;
              if (sSingleTagTable[loByte]) {
                if (bytePos + 1 >= byteBufferLimit) {
                  ucPos--;
                  break label182;
                } 
                byteBuffer[bytePos++] = 1;
              } 
              byteBuffer[bytePos++] = (byte)loByte;
              continue;
            } 
            if (inDynamicWindow(curUC, this.fCurrentWindow)) {
              byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[this.fCurrentWindow] + 128);
              continue;
            } 
            if (!isCompressible(curUC)) {
              if (nextUC != -1 && isCompressible(nextUC)) {
                if (bytePos + 2 >= byteBufferLimit) {
                  ucPos--;
                  break label182;
                } 
                byteBuffer[bytePos++] = 14;
                byteBuffer[bytePos++] = (byte)(curUC >>> 8);
                byteBuffer[bytePos++] = (byte)(curUC & 0xFF);
                continue;
              } 
              if (bytePos + 3 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              byteBuffer[bytePos++] = 15;
              hiByte = curUC >>> 8;
              loByte = curUC & 0xFF;
              if (sUnicodeTagTable[hiByte])
                byteBuffer[bytePos++] = -16; 
              byteBuffer[bytePos++] = (byte)hiByte;
              byteBuffer[bytePos++] = (byte)loByte;
              this.fMode = 1;
              break;
            } 
            if ((whichWindow = findDynamicWindow(curUC)) != -1) {
              if (ucPos + 1 < charBufferLimit) {
                forwardUC = charBuffer[ucPos + 1];
              } else {
                forwardUC = -1;
              } 
              if (inDynamicWindow(nextUC, whichWindow) && inDynamicWindow(forwardUC, whichWindow)) {
                if (bytePos + 1 >= byteBufferLimit) {
                  ucPos--;
                  break label182;
                } 
                byteBuffer[bytePos++] = (byte)(16 + whichWindow);
                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                this.fCurrentWindow = whichWindow;
                continue;
              } 
              if (bytePos + 1 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              byteBuffer[bytePos++] = (byte)(1 + whichWindow);
              byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
              continue;
            } 
            if ((whichWindow = findStaticWindow(curUC)) != -1 && !inStaticWindow(nextUC, whichWindow)) {
              if (bytePos + 1 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              byteBuffer[bytePos++] = (byte)(1 + whichWindow);
              byteBuffer[bytePos++] = (byte)(curUC - sOffsets[whichWindow]);
              continue;
            } 
            curIndex = makeIndex(curUC);
            this.fIndexCount[curIndex] = this.fIndexCount[curIndex] + 1;
            if (ucPos + 1 < charBufferLimit) {
              forwardUC = charBuffer[ucPos + 1];
            } else {
              forwardUC = -1;
            } 
            if (this.fIndexCount[curIndex] > 1 || (curIndex == makeIndex(nextUC) && curIndex == makeIndex(forwardUC))) {
              if (bytePos + 2 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              whichWindow = getLRDefinedWindow();
              byteBuffer[bytePos++] = (byte)(24 + whichWindow);
              byteBuffer[bytePos++] = (byte)curIndex;
              byteBuffer[bytePos++] = (byte)(curUC - sOffsetTable[curIndex] + 128);
              this.fOffsets[whichWindow] = sOffsetTable[curIndex];
              this.fCurrentWindow = whichWindow;
              this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
              continue;
            } 
            if (bytePos + 3 >= byteBufferLimit) {
              ucPos--;
              break label182;
            } 
            byteBuffer[bytePos++] = 15;
            hiByte = curUC >>> 8;
            loByte = curUC & 0xFF;
            if (sUnicodeTagTable[hiByte]) {
              byteBuffer[bytePos++] = -16;
              break label172;
            } 
            byteBuffer[bytePos++] = (byte)hiByte;
            byteBuffer[bytePos++] = (byte)loByte;
            this.fMode = 1;
          } 
        case 1:
          while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
            curUC = charBuffer[ucPos++];
            if (ucPos < charBufferLimit) {
              nextUC = charBuffer[ucPos];
            } else {
              nextUC = -1;
            } 
            if (!isCompressible(curUC) || (nextUC != -1 && !isCompressible(nextUC))) {
              if (bytePos + 2 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              hiByte = curUC >>> 8;
              loByte = curUC & 0xFF;
              if (sUnicodeTagTable[hiByte])
                byteBuffer[bytePos++] = -16; 
              byteBuffer[bytePos++] = (byte)hiByte;
              byteBuffer[bytePos++] = (byte)loByte;
              continue;
            } 
            if (curUC < 128) {
              loByte = curUC & 0xFF;
              if (nextUC != -1 && nextUC < 128 && !sSingleTagTable[loByte]) {
                if (bytePos + 1 >= byteBufferLimit) {
                  ucPos--;
                  break label182;
                } 
                whichWindow = this.fCurrentWindow;
                byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                byteBuffer[bytePos++] = (byte)loByte;
                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                this.fMode = 0;
                break;
              } 
              if (bytePos + 1 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              byteBuffer[bytePos++] = 0;
              byteBuffer[bytePos++] = (byte)loByte;
              continue;
            } 
            if ((whichWindow = findDynamicWindow(curUC)) != -1) {
              if (inDynamicWindow(nextUC, whichWindow)) {
                if (bytePos + 1 >= byteBufferLimit) {
                  ucPos--;
                  break label182;
                } 
                byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                this.fCurrentWindow = whichWindow;
                this.fMode = 0;
                break;
              } 
              if (bytePos + 2 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              hiByte = curUC >>> 8;
              loByte = curUC & 0xFF;
              if (sUnicodeTagTable[hiByte])
                byteBuffer[bytePos++] = -16; 
              byteBuffer[bytePos++] = (byte)hiByte;
              byteBuffer[bytePos++] = (byte)loByte;
              continue;
            } 
            curIndex = makeIndex(curUC);
            this.fIndexCount[curIndex] = this.fIndexCount[curIndex] + 1;
            if (ucPos + 1 < charBufferLimit) {
              forwardUC = charBuffer[ucPos + 1];
            } else {
              forwardUC = -1;
            } 
            if (this.fIndexCount[curIndex] > 1 || (curIndex == makeIndex(nextUC) && curIndex == makeIndex(forwardUC))) {
              if (bytePos + 2 >= byteBufferLimit) {
                ucPos--;
                break label182;
              } 
              whichWindow = getLRDefinedWindow();
              byteBuffer[bytePos++] = (byte)(232 + whichWindow);
              byteBuffer[bytePos++] = (byte)curIndex;
              byteBuffer[bytePos++] = (byte)(curUC - sOffsetTable[curIndex] + 128);
              this.fOffsets[whichWindow] = sOffsetTable[curIndex];
              this.fCurrentWindow = whichWindow;
              this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
              this.fMode = 0;
              break;
            } 
            if (bytePos + 2 >= byteBufferLimit) {
              ucPos--;
              break label182;
            } 
            hiByte = curUC >>> 8;
            loByte = curUC & 0xFF;
            if (sUnicodeTagTable[hiByte])
              byteBuffer[bytePos++] = -16; 
            byteBuffer[bytePos++] = (byte)hiByte;
            byteBuffer[bytePos++] = (byte)loByte;
          } 
      } 
    } 
    if (charsRead != null)
      charsRead[0] = ucPos - charBufferStart; 
    return bytePos - byteBufferStart;
  }
  
  public void reset() {
    this.fOffsets[0] = 128;
    this.fOffsets[1] = 192;
    this.fOffsets[2] = 1024;
    this.fOffsets[3] = 1536;
    this.fOffsets[4] = 2304;
    this.fOffsets[5] = 12352;
    this.fOffsets[6] = 12448;
    this.fOffsets[7] = 65280;
    int i;
    for (i = 0; i < 8; i++)
      this.fTimeStamps[i] = 0; 
    for (i = 0; i <= 255; i++)
      this.fIndexCount[i] = 0; 
    this.fTimeStamp = 0;
    this.fCurrentWindow = 0;
    this.fMode = 0;
  }
  
  private static int makeIndex(int c) {
    if (c >= 192 && c < 320)
      return 249; 
    if (c >= 592 && c < 720)
      return 250; 
    if (c >= 880 && c < 1008)
      return 251; 
    if (c >= 1328 && c < 1424)
      return 252; 
    if (c >= 12352 && c < 12448)
      return 253; 
    if (c >= 12448 && c < 12576)
      return 254; 
    if (c >= 65376 && c < 65439)
      return 255; 
    if (c >= 128 && c < 13312)
      return c / 128 & 0xFF; 
    if (c >= 57344 && c <= 65535)
      return (c - 44032) / 128 & 0xFF; 
    return 0;
  }
  
  private boolean inDynamicWindow(int c, int whichWindow) {
    return (c >= this.fOffsets[whichWindow] && c < this.fOffsets[whichWindow] + 128);
  }
  
  private static boolean inStaticWindow(int c, int whichWindow) {
    return (c >= sOffsets[whichWindow] && c < sOffsets[whichWindow] + 128);
  }
  
  private static boolean isCompressible(int c) {
    return (c < 13312 || c >= 57344);
  }
  
  private int findDynamicWindow(int c) {
    for (int i = 7; i >= 0; i--) {
      if (inDynamicWindow(c, i)) {
        this.fTimeStamps[i] = this.fTimeStamps[i] + 1;
        return i;
      } 
    } 
    return -1;
  }
  
  private static int findStaticWindow(int c) {
    for (int i = 7; i >= 0; i--) {
      if (inStaticWindow(c, i))
        return i; 
    } 
    return -1;
  }
  
  private int getLRDefinedWindow() {
    int leastRU = Integer.MAX_VALUE;
    int whichWindow = -1;
    for (int i = 7; i >= 0; i--) {
      if (this.fTimeStamps[i] < leastRU) {
        leastRU = this.fTimeStamps[i];
        whichWindow = i;
      } 
    } 
    return whichWindow;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnicodeCompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */