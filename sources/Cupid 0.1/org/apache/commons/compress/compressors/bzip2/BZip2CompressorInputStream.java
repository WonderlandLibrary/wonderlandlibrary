package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class BZip2CompressorInputStream extends CompressorInputStream implements BZip2Constants {
  private int last;
  
  private int origPtr;
  
  private int blockSize100k;
  
  private boolean blockRandomised;
  
  private int bsBuff;
  
  private int bsLive;
  
  private final CRC crc = new CRC();
  
  private int nInUse;
  
  private InputStream in;
  
  private final boolean decompressConcatenated;
  
  private static final int EOF = 0;
  
  private static final int START_BLOCK_STATE = 1;
  
  private static final int RAND_PART_A_STATE = 2;
  
  private static final int RAND_PART_B_STATE = 3;
  
  private static final int RAND_PART_C_STATE = 4;
  
  private static final int NO_RAND_PART_A_STATE = 5;
  
  private static final int NO_RAND_PART_B_STATE = 6;
  
  private static final int NO_RAND_PART_C_STATE = 7;
  
  private int currentState = 1;
  
  private int storedBlockCRC;
  
  private int storedCombinedCRC;
  
  private int computedBlockCRC;
  
  private int computedCombinedCRC;
  
  private int su_count;
  
  private int su_ch2;
  
  private int su_chPrev;
  
  private int su_i2;
  
  private int su_j2;
  
  private int su_rNToGo;
  
  private int su_rTPos;
  
  private int su_tPos;
  
  private char su_z;
  
  private Data data;
  
  public BZip2CompressorInputStream(InputStream in) throws IOException {
    this(in, false);
  }
  
  public BZip2CompressorInputStream(InputStream in, boolean decompressConcatenated) throws IOException {
    this.in = in;
    this.decompressConcatenated = decompressConcatenated;
    init(true);
    initBlock();
  }
  
  public int read() throws IOException {
    if (this.in != null) {
      int r = read0();
      count((r < 0) ? -1 : 1);
      return r;
    } 
    throw new IOException("stream closed");
  }
  
  public int read(byte[] dest, int offs, int len) throws IOException {
    if (offs < 0)
      throw new IndexOutOfBoundsException("offs(" + offs + ") < 0."); 
    if (len < 0)
      throw new IndexOutOfBoundsException("len(" + len + ") < 0."); 
    if (offs + len > dest.length)
      throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length + ")."); 
    if (this.in == null)
      throw new IOException("stream closed"); 
    int hi = offs + len;
    int destOffs = offs;
    int b;
    while (destOffs < hi && (b = read0()) >= 0) {
      dest[destOffs++] = (byte)b;
      count(1);
    } 
    int c = (destOffs == offs) ? -1 : (destOffs - offs);
    return c;
  }
  
  private void makeMaps() {
    boolean[] inUse = this.data.inUse;
    byte[] seqToUnseq = this.data.seqToUnseq;
    int nInUseShadow = 0;
    for (int i = 0; i < 256; i++) {
      if (inUse[i])
        seqToUnseq[nInUseShadow++] = (byte)i; 
    } 
    this.nInUse = nInUseShadow;
  }
  
  private int read0() throws IOException {
    switch (this.currentState) {
      case 0:
        return -1;
      case 1:
        return setupBlock();
      case 2:
        throw new IllegalStateException();
      case 3:
        return setupRandPartB();
      case 4:
        return setupRandPartC();
      case 5:
        throw new IllegalStateException();
      case 6:
        return setupNoRandPartB();
      case 7:
        return setupNoRandPartC();
    } 
    throw new IllegalStateException();
  }
  
  private boolean init(boolean isFirstStream) throws IOException {
    if (null == this.in)
      throw new IOException("No InputStream"); 
    int magic0 = this.in.read();
    if (magic0 == -1 && !isFirstStream)
      return false; 
    int magic1 = this.in.read();
    int magic2 = this.in.read();
    if (magic0 != 66 || magic1 != 90 || magic2 != 104)
      throw new IOException(isFirstStream ? "Stream is not in the BZip2 format" : "Garbage after a valid BZip2 stream"); 
    int blockSize = this.in.read();
    if (blockSize < 49 || blockSize > 57)
      throw new IOException("BZip2 block size is invalid"); 
    this.blockSize100k = blockSize - 48;
    this.bsLive = 0;
    this.computedCombinedCRC = 0;
    return true;
  }
  
  private void initBlock() throws IOException {
    char magic0, magic1, magic2, magic3, magic4, magic5;
    while (true) {
      magic0 = bsGetUByte();
      magic1 = bsGetUByte();
      magic2 = bsGetUByte();
      magic3 = bsGetUByte();
      magic4 = bsGetUByte();
      magic5 = bsGetUByte();
      if (magic0 != '\027' || magic1 != 'r' || magic2 != 'E' || magic3 != '8' || magic4 != 'P' || magic5 != '')
        break; 
      if (complete())
        return; 
    } 
    if (magic0 != '1' || magic1 != 'A' || magic2 != 'Y' || magic3 != '&' || magic4 != 'S' || magic5 != 'Y') {
      this.currentState = 0;
      throw new IOException("bad block header");
    } 
    this.storedBlockCRC = bsGetInt();
    this.blockRandomised = (bsR(1) == 1);
    if (this.data == null)
      this.data = new Data(this.blockSize100k); 
    getAndMoveToFrontDecode();
    this.crc.initialiseCRC();
    this.currentState = 1;
  }
  
  private void endBlock() throws IOException {
    this.computedBlockCRC = this.crc.getFinalCRC();
    if (this.storedBlockCRC != this.computedBlockCRC) {
      this.computedCombinedCRC = this.storedCombinedCRC << 1 | this.storedCombinedCRC >>> 31;
      this.computedCombinedCRC ^= this.storedBlockCRC;
      throw new IOException("BZip2 CRC error");
    } 
    this.computedCombinedCRC = this.computedCombinedCRC << 1 | this.computedCombinedCRC >>> 31;
    this.computedCombinedCRC ^= this.computedBlockCRC;
  }
  
  private boolean complete() throws IOException {
    this.storedCombinedCRC = bsGetInt();
    this.currentState = 0;
    this.data = null;
    if (this.storedCombinedCRC != this.computedCombinedCRC)
      throw new IOException("BZip2 CRC error"); 
    return (!this.decompressConcatenated || !init(false));
  }
  
  public void close() throws IOException {
    InputStream inShadow = this.in;
    if (inShadow != null)
      try {
        if (inShadow != System.in)
          inShadow.close(); 
      } finally {
        this.data = null;
        this.in = null;
      }  
  }
  
  private int bsR(int n) throws IOException {
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    if (bsLiveShadow < n) {
      InputStream inShadow = this.in;
      do {
        int thech = inShadow.read();
        if (thech < 0)
          throw new IOException("unexpected end of stream"); 
        bsBuffShadow = bsBuffShadow << 8 | thech;
        bsLiveShadow += 8;
      } while (bsLiveShadow < n);
      this.bsBuff = bsBuffShadow;
    } 
    this.bsLive = bsLiveShadow - n;
    return bsBuffShadow >> bsLiveShadow - n & (1 << n) - 1;
  }
  
  private boolean bsGetBit() throws IOException {
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    if (bsLiveShadow < 1) {
      int thech = this.in.read();
      if (thech < 0)
        throw new IOException("unexpected end of stream"); 
      bsBuffShadow = bsBuffShadow << 8 | thech;
      bsLiveShadow += 8;
      this.bsBuff = bsBuffShadow;
    } 
    this.bsLive = bsLiveShadow - 1;
    return ((bsBuffShadow >> bsLiveShadow - 1 & 0x1) != 0);
  }
  
  private char bsGetUByte() throws IOException {
    return (char)bsR(8);
  }
  
  private int bsGetInt() throws IOException {
    return ((bsR(8) << 8 | bsR(8)) << 8 | bsR(8)) << 8 | bsR(8);
  }
  
  private static void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, char[] length, int minLen, int maxLen, int alphaSize) {
    int i;
    int pp;
    for (i = minLen, pp = 0; i <= maxLen; i++) {
      for (int k = 0; k < alphaSize; k++) {
        if (length[k] == i)
          perm[pp++] = k; 
      } 
    } 
    for (i = 23; --i > 0; ) {
      base[i] = 0;
      limit[i] = 0;
    } 
    for (i = 0; i < alphaSize; i++)
      base[length[i] + 1] = base[length[i] + 1] + 1; 
    int b;
    for (i = 1, b = base[0]; i < 23; i++) {
      b += base[i];
      base[i] = b;
    } 
    int vec;
    int j;
    for (i = minLen, vec = 0, j = base[i]; i <= maxLen; i++) {
      int nb = base[i + 1];
      vec += nb - j;
      j = nb;
      limit[i] = vec - 1;
      vec <<= 1;
    } 
    for (i = minLen + 1; i <= maxLen; i++)
      base[i] = (limit[i - 1] + 1 << 1) - base[i]; 
  }
  
  private void recvDecodingTables() throws IOException {
    Data dataShadow = this.data;
    boolean[] inUse = dataShadow.inUse;
    byte[] pos = dataShadow.recvDecodingTables_pos;
    byte[] selector = dataShadow.selector;
    byte[] selectorMtf = dataShadow.selectorMtf;
    int inUse16 = 0;
    int i;
    for (i = 0; i < 16; i++) {
      if (bsGetBit())
        inUse16 |= 1 << i; 
    } 
    for (i = 256; --i >= 0;)
      inUse[i] = false; 
    for (i = 0; i < 16; i++) {
      if ((inUse16 & 1 << i) != 0) {
        int i16 = i << 4;
        for (int m = 0; m < 16; m++) {
          if (bsGetBit())
            inUse[i16 + m] = true; 
        } 
      } 
    } 
    makeMaps();
    int alphaSize = this.nInUse + 2;
    int nGroups = bsR(3);
    int nSelectors = bsR(15);
    for (int k = 0; k < nSelectors; k++) {
      int m = 0;
      while (bsGetBit())
        m++; 
      selectorMtf[k] = (byte)m;
    } 
    for (int v = nGroups; --v >= 0;)
      pos[v] = (byte)v; 
    for (int j = 0; j < nSelectors; j++) {
      int m = selectorMtf[j] & 0xFF;
      byte tmp = pos[m];
      while (m > 0) {
        pos[m] = pos[m - 1];
        m--;
      } 
      pos[0] = tmp;
      selector[j] = tmp;
    } 
    char[][] len = dataShadow.temp_charArray2d;
    for (int t = 0; t < nGroups; t++) {
      int curr = bsR(5);
      char[] len_t = len[t];
      for (int m = 0; m < alphaSize; m++) {
        while (bsGetBit())
          curr += bsGetBit() ? -1 : 1; 
        len_t[m] = (char)curr;
      } 
    } 
    createHuffmanDecodingTables(alphaSize, nGroups);
  }
  
  private void createHuffmanDecodingTables(int alphaSize, int nGroups) {
    Data dataShadow = this.data;
    char[][] len = dataShadow.temp_charArray2d;
    int[] minLens = dataShadow.minLens;
    int[][] limit = dataShadow.limit;
    int[][] base = dataShadow.base;
    int[][] perm = dataShadow.perm;
    for (int t = 0; t < nGroups; t++) {
      int minLen = 32;
      int maxLen = 0;
      char[] len_t = len[t];
      for (int i = alphaSize; --i >= 0; ) {
        char lent = len_t[i];
        if (lent > maxLen)
          maxLen = lent; 
        if (lent < minLen)
          minLen = lent; 
      } 
      hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
      minLens[t] = minLen;
    } 
  }
  
  private void getAndMoveToFrontDecode() throws IOException {
    this.origPtr = bsR(24);
    recvDecodingTables();
    InputStream inShadow = this.in;
    Data dataShadow = this.data;
    byte[] ll8 = dataShadow.ll8;
    int[] unzftab = dataShadow.unzftab;
    byte[] selector = dataShadow.selector;
    byte[] seqToUnseq = dataShadow.seqToUnseq;
    char[] yy = dataShadow.getAndMoveToFrontDecode_yy;
    int[] minLens = dataShadow.minLens;
    int[][] limit = dataShadow.limit;
    int[][] base = dataShadow.base;
    int[][] perm = dataShadow.perm;
    int limitLast = this.blockSize100k * 100000;
    for (int i = 256; --i >= 0; ) {
      yy[i] = (char)i;
      unzftab[i] = 0;
    } 
    int groupNo = 0;
    int groupPos = 49;
    int eob = this.nInUse + 1;
    int nextSym = getAndMoveToFrontDecode0(0);
    int bsBuffShadow = this.bsBuff;
    int bsLiveShadow = this.bsLive;
    int lastShadow = -1;
    int zt = selector[groupNo] & 0xFF;
    int[] base_zt = base[zt];
    int[] limit_zt = limit[zt];
    int[] perm_zt = perm[zt];
    int minLens_zt = minLens[zt];
    while (nextSym != eob) {
      if (nextSym == 0 || nextSym == 1) {
        int s = -1;
        int n;
        for (n = 1;; n <<= 1) {
          if (nextSym == 0) {
            s += n;
          } else if (nextSym == 1) {
            s += n << 1;
          } else {
            break;
          } 
          if (groupPos == 0) {
            groupPos = 49;
            zt = selector[++groupNo] & 0xFF;
            base_zt = base[zt];
            limit_zt = limit[zt];
            perm_zt = perm[zt];
            minLens_zt = minLens[zt];
          } else {
            groupPos--;
          } 
          int j = minLens_zt;
          while (bsLiveShadow < j) {
            int thech = inShadow.read();
            if (thech >= 0) {
              bsBuffShadow = bsBuffShadow << 8 | thech;
              bsLiveShadow += 8;
              continue;
            } 
            throw new IOException("unexpected end of stream");
          } 
          int k = bsBuffShadow >> bsLiveShadow - j & (1 << j) - 1;
          bsLiveShadow -= j;
          while (k > limit_zt[j]) {
            j++;
            while (bsLiveShadow < 1) {
              int thech = inShadow.read();
              if (thech >= 0) {
                bsBuffShadow = bsBuffShadow << 8 | thech;
                bsLiveShadow += 8;
                continue;
              } 
              throw new IOException("unexpected end of stream");
            } 
            bsLiveShadow--;
            k = k << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
          } 
          nextSym = perm_zt[k - base_zt[j]];
        } 
        byte ch = seqToUnseq[yy[0]];
        unzftab[ch & 0xFF] = unzftab[ch & 0xFF] + s + 1;
        while (s-- >= 0)
          ll8[++lastShadow] = ch; 
        if (lastShadow >= limitLast)
          throw new IOException("block overrun"); 
        continue;
      } 
      if (++lastShadow >= limitLast)
        throw new IOException("block overrun"); 
      char tmp = yy[nextSym - 1];
      unzftab[seqToUnseq[tmp] & 0xFF] = unzftab[seqToUnseq[tmp] & 0xFF] + 1;
      ll8[lastShadow] = seqToUnseq[tmp];
      if (nextSym <= 16) {
        for (int j = nextSym - 1; j > 0;)
          yy[j] = yy[--j]; 
      } else {
        System.arraycopy(yy, 0, yy, 1, nextSym - 1);
      } 
      yy[0] = tmp;
      if (groupPos == 0) {
        groupPos = 49;
        zt = selector[++groupNo] & 0xFF;
        base_zt = base[zt];
        limit_zt = limit[zt];
        perm_zt = perm[zt];
        minLens_zt = minLens[zt];
      } else {
        groupPos--;
      } 
      int zn = minLens_zt;
      while (bsLiveShadow < zn) {
        int thech = inShadow.read();
        if (thech >= 0) {
          bsBuffShadow = bsBuffShadow << 8 | thech;
          bsLiveShadow += 8;
          continue;
        } 
        throw new IOException("unexpected end of stream");
      } 
      int zvec = bsBuffShadow >> bsLiveShadow - zn & (1 << zn) - 1;
      bsLiveShadow -= zn;
      while (zvec > limit_zt[zn]) {
        zn++;
        while (bsLiveShadow < 1) {
          int thech = inShadow.read();
          if (thech >= 0) {
            bsBuffShadow = bsBuffShadow << 8 | thech;
            bsLiveShadow += 8;
            continue;
          } 
          throw new IOException("unexpected end of stream");
        } 
        bsLiveShadow--;
        zvec = zvec << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
      } 
      nextSym = perm_zt[zvec - base_zt[zn]];
    } 
    this.last = lastShadow;
    this.bsLive = bsLiveShadow;
    this.bsBuff = bsBuffShadow;
  }
  
  private int getAndMoveToFrontDecode0(int groupNo) throws IOException {
    InputStream inShadow = this.in;
    Data dataShadow = this.data;
    int zt = dataShadow.selector[groupNo] & 0xFF;
    int[] limit_zt = dataShadow.limit[zt];
    int zn = dataShadow.minLens[zt];
    int zvec = bsR(zn);
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    while (zvec > limit_zt[zn]) {
      zn++;
      while (bsLiveShadow < 1) {
        int thech = inShadow.read();
        if (thech >= 0) {
          bsBuffShadow = bsBuffShadow << 8 | thech;
          bsLiveShadow += 8;
          continue;
        } 
        throw new IOException("unexpected end of stream");
      } 
      bsLiveShadow--;
      zvec = zvec << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
    } 
    this.bsLive = bsLiveShadow;
    this.bsBuff = bsBuffShadow;
    return dataShadow.perm[zt][zvec - dataShadow.base[zt][zn]];
  }
  
  private int setupBlock() throws IOException {
    if (this.currentState == 0 || this.data == null)
      return -1; 
    int[] cftab = this.data.cftab;
    int[] tt = this.data.initTT(this.last + 1);
    byte[] ll8 = this.data.ll8;
    cftab[0] = 0;
    System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);
    int i, c;
    for (i = 1, c = cftab[0]; i <= 256; i++) {
      c += cftab[i];
      cftab[i] = c;
    } 
    int lastShadow;
    for (i = 0, lastShadow = this.last; i <= lastShadow; i++) {
      cftab[ll8[i] & 0xFF] = cftab[ll8[i] & 0xFF] + 1;
      tt[cftab[ll8[i] & 0xFF]] = i;
    } 
    if (this.origPtr < 0 || this.origPtr >= tt.length)
      throw new IOException("stream corrupted"); 
    this.su_tPos = tt[this.origPtr];
    this.su_count = 0;
    this.su_i2 = 0;
    this.su_ch2 = 256;
    if (this.blockRandomised) {
      this.su_rNToGo = 0;
      this.su_rTPos = 0;
      return setupRandPartA();
    } 
    return setupNoRandPartA();
  }
  
  private int setupRandPartA() throws IOException {
    if (this.su_i2 <= this.last) {
      this.su_chPrev = this.su_ch2;
      int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
      this.su_tPos = this.data.tt[this.su_tPos];
      if (this.su_rNToGo == 0) {
        this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
        if (++this.su_rTPos == 512)
          this.su_rTPos = 0; 
      } else {
        this.su_rNToGo--;
      } 
      this.su_ch2 = su_ch2Shadow ^= (this.su_rNToGo == 1) ? 1 : 0;
      this.su_i2++;
      this.currentState = 3;
      this.crc.updateCRC(su_ch2Shadow);
      return su_ch2Shadow;
    } 
    endBlock();
    initBlock();
    return setupBlock();
  }
  
  private int setupNoRandPartA() throws IOException {
    if (this.su_i2 <= this.last) {
      this.su_chPrev = this.su_ch2;
      int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
      this.su_ch2 = su_ch2Shadow;
      this.su_tPos = this.data.tt[this.su_tPos];
      this.su_i2++;
      this.currentState = 6;
      this.crc.updateCRC(su_ch2Shadow);
      return su_ch2Shadow;
    } 
    this.currentState = 5;
    endBlock();
    initBlock();
    return setupBlock();
  }
  
  private int setupRandPartB() throws IOException {
    if (this.su_ch2 != this.su_chPrev) {
      this.currentState = 2;
      this.su_count = 1;
      return setupRandPartA();
    } 
    if (++this.su_count >= 4) {
      this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
      this.su_tPos = this.data.tt[this.su_tPos];
      if (this.su_rNToGo == 0) {
        this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
        if (++this.su_rTPos == 512)
          this.su_rTPos = 0; 
      } else {
        this.su_rNToGo--;
      } 
      this.su_j2 = 0;
      this.currentState = 4;
      if (this.su_rNToGo == 1)
        this.su_z = (char)(this.su_z ^ 0x1); 
      return setupRandPartC();
    } 
    this.currentState = 2;
    return setupRandPartA();
  }
  
  private int setupRandPartC() throws IOException {
    if (this.su_j2 < this.su_z) {
      this.crc.updateCRC(this.su_ch2);
      this.su_j2++;
      return this.su_ch2;
    } 
    this.currentState = 2;
    this.su_i2++;
    this.su_count = 0;
    return setupRandPartA();
  }
  
  private int setupNoRandPartB() throws IOException {
    if (this.su_ch2 != this.su_chPrev) {
      this.su_count = 1;
      return setupNoRandPartA();
    } 
    if (++this.su_count >= 4) {
      this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
      this.su_tPos = this.data.tt[this.su_tPos];
      this.su_j2 = 0;
      return setupNoRandPartC();
    } 
    return setupNoRandPartA();
  }
  
  private int setupNoRandPartC() throws IOException {
    if (this.su_j2 < this.su_z) {
      int su_ch2Shadow = this.su_ch2;
      this.crc.updateCRC(su_ch2Shadow);
      this.su_j2++;
      this.currentState = 7;
      return su_ch2Shadow;
    } 
    this.su_i2++;
    this.su_count = 0;
    return setupNoRandPartA();
  }
  
  private static final class Data {
    final boolean[] inUse = new boolean[256];
    
    final byte[] seqToUnseq = new byte[256];
    
    final byte[] selector = new byte[18002];
    
    final byte[] selectorMtf = new byte[18002];
    
    final int[] unzftab = new int[256];
    
    final int[][] limit = new int[6][258];
    
    final int[][] base = new int[6][258];
    
    final int[][] perm = new int[6][258];
    
    final int[] minLens = new int[6];
    
    final int[] cftab = new int[257];
    
    final char[] getAndMoveToFrontDecode_yy = new char[256];
    
    final char[][] temp_charArray2d = new char[6][258];
    
    final byte[] recvDecodingTables_pos = new byte[6];
    
    int[] tt;
    
    byte[] ll8;
    
    Data(int blockSize100k) {
      this.ll8 = new byte[blockSize100k * 100000];
    }
    
    int[] initTT(int length) {
      int[] ttShadow = this.tt;
      if (ttShadow == null || ttShadow.length < length)
        this.tt = ttShadow = new int[length]; 
      return ttShadow;
    }
  }
  
  public static boolean matches(byte[] signature, int length) {
    if (length < 3)
      return false; 
    if (signature[0] != 66)
      return false; 
    if (signature[1] != 90)
      return false; 
    if (signature[2] != 104)
      return false; 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\bzip2\BZip2CompressorInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */