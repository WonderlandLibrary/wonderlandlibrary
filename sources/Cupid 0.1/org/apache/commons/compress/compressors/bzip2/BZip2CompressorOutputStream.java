package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class BZip2CompressorOutputStream extends CompressorOutputStream implements BZip2Constants {
  public static final int MIN_BLOCKSIZE = 1;
  
  public static final int MAX_BLOCKSIZE = 9;
  
  private static final int GREATER_ICOST = 15;
  
  private static final int LESSER_ICOST = 0;
  
  private int last;
  
  private final int blockSize100k;
  
  private int bsBuff;
  
  private int bsLive;
  
  private static void hbMakeCodeLengths(byte[] len, int[] freq, Data dat, int alphaSize, int maxLen) {
    int[] heap = dat.heap;
    int[] weight = dat.weight;
    int[] parent = dat.parent;
    for (int i = alphaSize; --i >= 0;)
      weight[i + 1] = ((freq[i] == 0) ? 1 : freq[i]) << 8; 
    for (boolean tooLong = true; tooLong; ) {
      tooLong = false;
      int nNodes = alphaSize;
      int nHeap = 0;
      heap[0] = 0;
      weight[0] = 0;
      parent[0] = -2;
      int j;
      for (j = 1; j <= alphaSize; j++) {
        parent[j] = -1;
        nHeap++;
        heap[nHeap] = j;
        int zz = nHeap;
        int tmp = heap[zz];
        while (weight[tmp] < weight[heap[zz >> 1]]) {
          heap[zz] = heap[zz >> 1];
          zz >>= 1;
        } 
        heap[zz] = tmp;
      } 
      while (nHeap > 1) {
        int n1 = heap[1];
        heap[1] = heap[nHeap];
        nHeap--;
        int yy = 0;
        int zz = 1;
        int tmp = heap[1];
        while (true) {
          yy = zz << 1;
          if (yy > nHeap)
            break; 
          if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
            yy++; 
          if (weight[tmp] < weight[heap[yy]])
            break; 
          heap[zz] = heap[yy];
          zz = yy;
        } 
        heap[zz] = tmp;
        int n2 = heap[1];
        heap[1] = heap[nHeap];
        nHeap--;
        yy = 0;
        zz = 1;
        tmp = heap[1];
        while (true) {
          yy = zz << 1;
          if (yy > nHeap)
            break; 
          if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
            yy++; 
          if (weight[tmp] < weight[heap[yy]])
            break; 
          heap[zz] = heap[yy];
          zz = yy;
        } 
        heap[zz] = tmp;
        nNodes++;
        parent[n2] = nNodes;
        parent[n1] = nNodes;
        int weight_n1 = weight[n1];
        int weight_n2 = weight[n2];
        weight[nNodes] = (weight_n1 & 0xFFFFFF00) + (weight_n2 & 0xFFFFFF00) | 1 + (((weight_n1 & 0xFF) > (weight_n2 & 0xFF)) ? (weight_n1 & 0xFF) : (weight_n2 & 0xFF));
        parent[nNodes] = -1;
        nHeap++;
        heap[nHeap] = nNodes;
        tmp = 0;
        zz = nHeap;
        tmp = heap[zz];
        int weight_tmp = weight[tmp];
        while (weight_tmp < weight[heap[zz >> 1]]) {
          heap[zz] = heap[zz >> 1];
          zz >>= 1;
        } 
        heap[zz] = tmp;
      } 
      for (j = 1; j <= alphaSize; j++) {
        int m = 0;
        int k = j;
        int parent_k;
        while ((parent_k = parent[k]) >= 0) {
          k = parent_k;
          m++;
        } 
        len[j - 1] = (byte)m;
        if (m > maxLen)
          tooLong = true; 
      } 
      if (tooLong)
        for (j = 1; j < alphaSize; j++) {
          int k = weight[j] >> 8;
          k = 1 + (k >> 1);
          weight[j] = k << 8;
        }  
    } 
  }
  
  private final CRC crc = new CRC();
  
  private int nInUse;
  
  private int nMTF;
  
  private int currentChar = -1;
  
  private int runLength = 0;
  
  private int blockCRC;
  
  private int combinedCRC;
  
  private final int allowableBlockSize;
  
  private Data data;
  
  private BlockSort blockSorter;
  
  private OutputStream out;
  
  public static int chooseBlockSize(long inputLength) {
    return (inputLength > 0L) ? (int)Math.min(inputLength / 132000L + 1L, 9L) : 9;
  }
  
  public BZip2CompressorOutputStream(OutputStream out) throws IOException {
    this(out, 9);
  }
  
  public BZip2CompressorOutputStream(OutputStream out, int blockSize) throws IOException {
    if (blockSize < 1)
      throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1"); 
    if (blockSize > 9)
      throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9"); 
    this.blockSize100k = blockSize;
    this.out = out;
    this.allowableBlockSize = this.blockSize100k * 100000 - 20;
    init();
  }
  
  public void write(int b) throws IOException {
    if (this.out != null) {
      write0(b);
    } else {
      throw new IOException("closed");
    } 
  }
  
  private void writeRun() throws IOException {
    int lastShadow = this.last;
    if (lastShadow < this.allowableBlockSize) {
      int currentCharShadow = this.currentChar;
      Data dataShadow = this.data;
      dataShadow.inUse[currentCharShadow] = true;
      byte ch = (byte)currentCharShadow;
      int runLengthShadow = this.runLength;
      this.crc.updateCRC(currentCharShadow, runLengthShadow);
      switch (runLengthShadow) {
        case 1:
          dataShadow.block[lastShadow + 2] = ch;
          this.last = lastShadow + 1;
          return;
        case 2:
          dataShadow.block[lastShadow + 2] = ch;
          dataShadow.block[lastShadow + 3] = ch;
          this.last = lastShadow + 2;
          return;
        case 3:
          block = dataShadow.block;
          block[lastShadow + 2] = ch;
          block[lastShadow + 3] = ch;
          block[lastShadow + 4] = ch;
          this.last = lastShadow + 3;
          return;
      } 
      runLengthShadow -= 4;
      dataShadow.inUse[runLengthShadow] = true;
      byte[] block = dataShadow.block;
      block[lastShadow + 2] = ch;
      block[lastShadow + 3] = ch;
      block[lastShadow + 4] = ch;
      block[lastShadow + 5] = ch;
      block[lastShadow + 6] = (byte)runLengthShadow;
      this.last = lastShadow + 5;
    } else {
      endBlock();
      initBlock();
      writeRun();
    } 
  }
  
  protected void finalize() throws Throwable {
    finish();
    super.finalize();
  }
  
  public void finish() throws IOException {
    if (this.out != null)
      try {
        if (this.runLength > 0)
          writeRun(); 
        this.currentChar = -1;
        endBlock();
        endCompression();
      } finally {
        this.out = null;
        this.data = null;
        this.blockSorter = null;
      }  
  }
  
  public void close() throws IOException {
    if (this.out != null) {
      OutputStream outShadow = this.out;
      finish();
      outShadow.close();
    } 
  }
  
  public void flush() throws IOException {
    OutputStream outShadow = this.out;
    if (outShadow != null)
      outShadow.flush(); 
  }
  
  private void init() throws IOException {
    bsPutUByte(66);
    bsPutUByte(90);
    this.data = new Data(this.blockSize100k);
    this.blockSorter = new BlockSort(this.data);
    bsPutUByte(104);
    bsPutUByte(48 + this.blockSize100k);
    this.combinedCRC = 0;
    initBlock();
  }
  
  private void initBlock() {
    this.crc.initialiseCRC();
    this.last = -1;
    boolean[] inUse = this.data.inUse;
    for (int i = 256; --i >= 0;)
      inUse[i] = false; 
  }
  
  private void endBlock() throws IOException {
    this.blockCRC = this.crc.getFinalCRC();
    this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
    this.combinedCRC ^= this.blockCRC;
    if (this.last == -1)
      return; 
    blockSort();
    bsPutUByte(49);
    bsPutUByte(65);
    bsPutUByte(89);
    bsPutUByte(38);
    bsPutUByte(83);
    bsPutUByte(89);
    bsPutInt(this.blockCRC);
    bsW(1, 0);
    moveToFrontCodeAndSend();
  }
  
  private void endCompression() throws IOException {
    bsPutUByte(23);
    bsPutUByte(114);
    bsPutUByte(69);
    bsPutUByte(56);
    bsPutUByte(80);
    bsPutUByte(144);
    bsPutInt(this.combinedCRC);
    bsFinishedWithStream();
  }
  
  public final int getBlockSize() {
    return this.blockSize100k;
  }
  
  public void write(byte[] buf, int offs, int len) throws IOException {
    if (offs < 0)
      throw new IndexOutOfBoundsException("offs(" + offs + ") < 0."); 
    if (len < 0)
      throw new IndexOutOfBoundsException("len(" + len + ") < 0."); 
    if (offs + len > buf.length)
      throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ")."); 
    if (this.out == null)
      throw new IOException("stream closed"); 
    for (int hi = offs + len; offs < hi;)
      write0(buf[offs++]); 
  }
  
  private void write0(int b) throws IOException {
    if (this.currentChar != -1) {
      b &= 0xFF;
      if (this.currentChar == b) {
        if (++this.runLength > 254) {
          writeRun();
          this.currentChar = -1;
          this.runLength = 0;
        } 
      } else {
        writeRun();
        this.runLength = 1;
        this.currentChar = b;
      } 
    } else {
      this.currentChar = b & 0xFF;
      this.runLength++;
    } 
  }
  
  private static void hbAssignCodes(int[] code, byte[] length, int minLen, int maxLen, int alphaSize) {
    int vec = 0;
    for (int n = minLen; n <= maxLen; n++) {
      for (int i = 0; i < alphaSize; i++) {
        if ((length[i] & 0xFF) == n) {
          code[i] = vec;
          vec++;
        } 
      } 
      vec <<= 1;
    } 
  }
  
  private void bsFinishedWithStream() throws IOException {
    while (this.bsLive > 0) {
      int ch = this.bsBuff >> 24;
      this.out.write(ch);
      this.bsBuff <<= 8;
      this.bsLive -= 8;
    } 
  }
  
  private void bsW(int n, int v) throws IOException {
    OutputStream outShadow = this.out;
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    while (bsLiveShadow >= 8) {
      outShadow.write(bsBuffShadow >> 24);
      bsBuffShadow <<= 8;
      bsLiveShadow -= 8;
    } 
    this.bsBuff = bsBuffShadow | v << 32 - bsLiveShadow - n;
    this.bsLive = bsLiveShadow + n;
  }
  
  private void bsPutUByte(int c) throws IOException {
    bsW(8, c);
  }
  
  private void bsPutInt(int u) throws IOException {
    bsW(8, u >> 24 & 0xFF);
    bsW(8, u >> 16 & 0xFF);
    bsW(8, u >> 8 & 0xFF);
    bsW(8, u & 0xFF);
  }
  
  private void sendMTFValues() throws IOException {
    byte[][] len = this.data.sendMTFValues_len;
    int alphaSize = this.nInUse + 2;
    for (int t = 6; --t >= 0; ) {
      byte[] len_t = len[t];
      for (int v = alphaSize; --v >= 0;)
        len_t[v] = 15; 
    } 
    int nGroups = (this.nMTF < 200) ? 2 : ((this.nMTF < 600) ? 3 : ((this.nMTF < 1200) ? 4 : ((this.nMTF < 2400) ? 5 : 6)));
    sendMTFValues0(nGroups, alphaSize);
    int nSelectors = sendMTFValues1(nGroups, alphaSize);
    sendMTFValues2(nGroups, nSelectors);
    sendMTFValues3(nGroups, alphaSize);
    sendMTFValues4();
    sendMTFValues5(nGroups, nSelectors);
    sendMTFValues6(nGroups, alphaSize);
    sendMTFValues7();
  }
  
  private void sendMTFValues0(int nGroups, int alphaSize) {
    byte[][] len = this.data.sendMTFValues_len;
    int[] mtfFreq = this.data.mtfFreq;
    int remF = this.nMTF;
    int gs = 0;
    for (int nPart = nGroups; nPart > 0; nPart--) {
      int tFreq = remF / nPart;
      int ge = gs - 1;
      int aFreq = 0;
      for (int a = alphaSize - 1; aFreq < tFreq && ge < a;)
        aFreq += mtfFreq[++ge]; 
      if (ge > gs && nPart != nGroups && nPart != 1 && (nGroups - nPart & 0x1) != 0)
        aFreq -= mtfFreq[ge--]; 
      byte[] len_np = len[nPart - 1];
      for (int v = alphaSize; --v >= 0; ) {
        if (v >= gs && v <= ge) {
          len_np[v] = 0;
          continue;
        } 
        len_np[v] = 15;
      } 
      gs = ge + 1;
      remF -= aFreq;
    } 
  }
  
  private int sendMTFValues1(int nGroups, int alphaSize) {
    Data dataShadow = this.data;
    int[][] rfreq = dataShadow.sendMTFValues_rfreq;
    int[] fave = dataShadow.sendMTFValues_fave;
    short[] cost = dataShadow.sendMTFValues_cost;
    char[] sfmap = dataShadow.sfmap;
    byte[] selector = dataShadow.selector;
    byte[][] len = dataShadow.sendMTFValues_len;
    byte[] len_0 = len[0];
    byte[] len_1 = len[1];
    byte[] len_2 = len[2];
    byte[] len_3 = len[3];
    byte[] len_4 = len[4];
    byte[] len_5 = len[5];
    int nMTFShadow = this.nMTF;
    int nSelectors = 0;
    for (int iter = 0; iter < 4; iter++) {
      for (int i = nGroups; --i >= 0; ) {
        fave[i] = 0;
        int[] rfreqt = rfreq[i];
        for (int j = alphaSize; --j >= 0;)
          rfreqt[j] = 0; 
      } 
      nSelectors = 0;
      int gs;
      for (gs = 0; gs < this.nMTF; ) {
        int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
        if (nGroups == 6) {
          short cost0 = 0;
          short cost1 = 0;
          short cost2 = 0;
          short cost3 = 0;
          short cost4 = 0;
          short cost5 = 0;
          for (int m = gs; m <= ge; m++) {
            int icv = sfmap[m];
            cost0 = (short)(cost0 + (len_0[icv] & 0xFF));
            cost1 = (short)(cost1 + (len_1[icv] & 0xFF));
            cost2 = (short)(cost2 + (len_2[icv] & 0xFF));
            cost3 = (short)(cost3 + (len_3[icv] & 0xFF));
            cost4 = (short)(cost4 + (len_4[icv] & 0xFF));
            cost5 = (short)(cost5 + (len_5[icv] & 0xFF));
          } 
          cost[0] = cost0;
          cost[1] = cost1;
          cost[2] = cost2;
          cost[3] = cost3;
          cost[4] = cost4;
          cost[5] = cost5;
        } else {
          for (int n = nGroups; --n >= 0;)
            cost[n] = 0; 
          for (int m = gs; m <= ge; m++) {
            int icv = sfmap[m];
            for (int i1 = nGroups; --i1 >= 0;)
              cost[i1] = (short)(cost[i1] + (len[i1][icv] & 0xFF)); 
          } 
        } 
        int bt = -1;
        for (int j = nGroups, bc = 999999999; --j >= 0; ) {
          int cost_t = cost[j];
          if (cost_t < bc) {
            bc = cost_t;
            bt = j;
          } 
        } 
        fave[bt] = fave[bt] + 1;
        selector[nSelectors] = (byte)bt;
        nSelectors++;
        int[] rfreq_bt = rfreq[bt];
        for (int k = gs; k <= ge; k++)
          rfreq_bt[sfmap[k]] = rfreq_bt[sfmap[k]] + 1; 
        gs = ge + 1;
      } 
      for (int t = 0; t < nGroups; t++)
        hbMakeCodeLengths(len[t], rfreq[t], this.data, alphaSize, 20); 
    } 
    return nSelectors;
  }
  
  private void sendMTFValues2(int nGroups, int nSelectors) {
    Data dataShadow = this.data;
    byte[] pos = dataShadow.sendMTFValues2_pos;
    int i;
    for (i = nGroups; --i >= 0;)
      pos[i] = (byte)i; 
    for (i = 0; i < nSelectors; i++) {
      byte ll_i = dataShadow.selector[i];
      byte tmp = pos[0];
      int j = 0;
      while (ll_i != tmp) {
        j++;
        byte tmp2 = tmp;
        tmp = pos[j];
        pos[j] = tmp2;
      } 
      pos[0] = tmp;
      dataShadow.selectorMtf[i] = (byte)j;
    } 
  }
  
  private void sendMTFValues3(int nGroups, int alphaSize) {
    int[][] code = this.data.sendMTFValues_code;
    byte[][] len = this.data.sendMTFValues_len;
    for (int t = 0; t < nGroups; t++) {
      int minLen = 32;
      int maxLen = 0;
      byte[] len_t = len[t];
      for (int i = alphaSize; --i >= 0; ) {
        int l = len_t[i] & 0xFF;
        if (l > maxLen)
          maxLen = l; 
        if (l < minLen)
          minLen = l; 
      } 
      hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
    } 
  }
  
  private void sendMTFValues4() throws IOException {
    boolean[] inUse = this.data.inUse;
    boolean[] inUse16 = this.data.sentMTFValues4_inUse16;
    int i;
    for (i = 16; --i >= 0; ) {
      inUse16[i] = false;
      int i16 = i * 16;
      for (int k = 16; --k >= 0;) {
        if (inUse[i16 + k])
          inUse16[i] = true; 
      } 
    } 
    for (i = 0; i < 16; i++)
      bsW(1, inUse16[i] ? 1 : 0); 
    OutputStream outShadow = this.out;
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    for (int j = 0; j < 16; j++) {
      if (inUse16[j]) {
        int i16 = j * 16;
        for (int k = 0; k < 16; k++) {
          while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
          } 
          if (inUse[i16 + k])
            bsBuffShadow |= 1 << 32 - bsLiveShadow - 1; 
          bsLiveShadow++;
        } 
      } 
    } 
    this.bsBuff = bsBuffShadow;
    this.bsLive = bsLiveShadow;
  }
  
  private void sendMTFValues5(int nGroups, int nSelectors) throws IOException {
    bsW(3, nGroups);
    bsW(15, nSelectors);
    OutputStream outShadow = this.out;
    byte[] selectorMtf = this.data.selectorMtf;
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    for (int i = 0; i < nSelectors; i++) {
      for (int j = 0, hj = selectorMtf[i] & 0xFF; j < hj; j++) {
        while (bsLiveShadow >= 8) {
          outShadow.write(bsBuffShadow >> 24);
          bsBuffShadow <<= 8;
          bsLiveShadow -= 8;
        } 
        bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
        bsLiveShadow++;
      } 
      while (bsLiveShadow >= 8) {
        outShadow.write(bsBuffShadow >> 24);
        bsBuffShadow <<= 8;
        bsLiveShadow -= 8;
      } 
      bsLiveShadow++;
    } 
    this.bsBuff = bsBuffShadow;
    this.bsLive = bsLiveShadow;
  }
  
  private void sendMTFValues6(int nGroups, int alphaSize) throws IOException {
    byte[][] len = this.data.sendMTFValues_len;
    OutputStream outShadow = this.out;
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    for (int t = 0; t < nGroups; t++) {
      byte[] len_t = len[t];
      int curr = len_t[0] & 0xFF;
      while (bsLiveShadow >= 8) {
        outShadow.write(bsBuffShadow >> 24);
        bsBuffShadow <<= 8;
        bsLiveShadow -= 8;
      } 
      bsBuffShadow |= curr << 32 - bsLiveShadow - 5;
      bsLiveShadow += 5;
      for (int i = 0; i < alphaSize; i++) {
        int lti = len_t[i] & 0xFF;
        while (curr < lti) {
          while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
          } 
          bsBuffShadow |= 2 << 32 - bsLiveShadow - 2;
          bsLiveShadow += 2;
          curr++;
        } 
        while (curr > lti) {
          while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
          } 
          bsBuffShadow |= 3 << 32 - bsLiveShadow - 2;
          bsLiveShadow += 2;
          curr--;
        } 
        while (bsLiveShadow >= 8) {
          outShadow.write(bsBuffShadow >> 24);
          bsBuffShadow <<= 8;
          bsLiveShadow -= 8;
        } 
        bsLiveShadow++;
      } 
    } 
    this.bsBuff = bsBuffShadow;
    this.bsLive = bsLiveShadow;
  }
  
  private void sendMTFValues7() throws IOException {
    Data dataShadow = this.data;
    byte[][] len = dataShadow.sendMTFValues_len;
    int[][] code = dataShadow.sendMTFValues_code;
    OutputStream outShadow = this.out;
    byte[] selector = dataShadow.selector;
    char[] sfmap = dataShadow.sfmap;
    int nMTFShadow = this.nMTF;
    int selCtr = 0;
    int bsLiveShadow = this.bsLive;
    int bsBuffShadow = this.bsBuff;
    for (int gs = 0; gs < nMTFShadow; ) {
      int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
      int selector_selCtr = selector[selCtr] & 0xFF;
      int[] code_selCtr = code[selector_selCtr];
      byte[] len_selCtr = len[selector_selCtr];
      while (gs <= ge) {
        int sfmap_i = sfmap[gs];
        while (bsLiveShadow >= 8) {
          outShadow.write(bsBuffShadow >> 24);
          bsBuffShadow <<= 8;
          bsLiveShadow -= 8;
        } 
        int n = len_selCtr[sfmap_i] & 0xFF;
        bsBuffShadow |= code_selCtr[sfmap_i] << 32 - bsLiveShadow - n;
        bsLiveShadow += n;
        gs++;
      } 
      gs = ge + 1;
      selCtr++;
    } 
    this.bsBuff = bsBuffShadow;
    this.bsLive = bsLiveShadow;
  }
  
  private void moveToFrontCodeAndSend() throws IOException {
    bsW(24, this.data.origPtr);
    generateMTFValues();
    sendMTFValues();
  }
  
  private void blockSort() {
    this.blockSorter.blockSort(this.data, this.last);
  }
  
  private void generateMTFValues() {
    int lastShadow = this.last;
    Data dataShadow = this.data;
    boolean[] inUse = dataShadow.inUse;
    byte[] block = dataShadow.block;
    int[] fmap = dataShadow.fmap;
    char[] sfmap = dataShadow.sfmap;
    int[] mtfFreq = dataShadow.mtfFreq;
    byte[] unseqToSeq = dataShadow.unseqToSeq;
    byte[] yy = dataShadow.generateMTFValues_yy;
    int nInUseShadow = 0;
    for (int i = 0; i < 256; i++) {
      if (inUse[i]) {
        unseqToSeq[i] = (byte)nInUseShadow;
        nInUseShadow++;
      } 
    } 
    this.nInUse = nInUseShadow;
    int eob = nInUseShadow + 1;
    int j;
    for (j = eob; j >= 0; j--)
      mtfFreq[j] = 0; 
    for (j = nInUseShadow; --j >= 0;)
      yy[j] = (byte)j; 
    int wr = 0;
    int zPend = 0;
    for (int k = 0; k <= lastShadow; k++) {
      byte ll_i = unseqToSeq[block[fmap[k]] & 0xFF];
      byte tmp = yy[0];
      int m = 0;
      while (ll_i != tmp) {
        m++;
        byte tmp2 = tmp;
        tmp = yy[m];
        yy[m] = tmp2;
      } 
      yy[0] = tmp;
      if (m == 0) {
        zPend++;
      } else {
        if (zPend > 0) {
          zPend--;
          while (true) {
            if ((zPend & 0x1) == 0) {
              sfmap[wr] = Character.MIN_VALUE;
              wr++;
              mtfFreq[0] = mtfFreq[0] + 1;
            } else {
              sfmap[wr] = '\001';
              wr++;
              mtfFreq[1] = mtfFreq[1] + 1;
            } 
            if (zPend >= 2) {
              zPend = zPend - 2 >> 1;
              continue;
            } 
            break;
          } 
          zPend = 0;
        } 
        sfmap[wr] = (char)(m + 1);
        wr++;
        mtfFreq[m + 1] = mtfFreq[m + 1] + 1;
      } 
    } 
    if (zPend > 0) {
      zPend--;
      while (true) {
        if ((zPend & 0x1) == 0) {
          sfmap[wr] = Character.MIN_VALUE;
          wr++;
          mtfFreq[0] = mtfFreq[0] + 1;
        } else {
          sfmap[wr] = '\001';
          wr++;
          mtfFreq[1] = mtfFreq[1] + 1;
        } 
        if (zPend >= 2) {
          zPend = zPend - 2 >> 1;
          continue;
        } 
        break;
      } 
    } 
    sfmap[wr] = (char)eob;
    mtfFreq[eob] = mtfFreq[eob] + 1;
    this.nMTF = wr + 1;
  }
  
  static final class Data {
    final boolean[] inUse = new boolean[256];
    
    final byte[] unseqToSeq = new byte[256];
    
    final int[] mtfFreq = new int[258];
    
    final byte[] selector = new byte[18002];
    
    final byte[] selectorMtf = new byte[18002];
    
    final byte[] generateMTFValues_yy = new byte[256];
    
    final byte[][] sendMTFValues_len = new byte[6][258];
    
    final int[][] sendMTFValues_rfreq = new int[6][258];
    
    final int[] sendMTFValues_fave = new int[6];
    
    final short[] sendMTFValues_cost = new short[6];
    
    final int[][] sendMTFValues_code = new int[6][258];
    
    final byte[] sendMTFValues2_pos = new byte[6];
    
    final boolean[] sentMTFValues4_inUse16 = new boolean[16];
    
    final int[] heap = new int[260];
    
    final int[] weight = new int[516];
    
    final int[] parent = new int[516];
    
    final byte[] block;
    
    final int[] fmap;
    
    final char[] sfmap;
    
    int origPtr;
    
    Data(int blockSize100k) {
      int n = blockSize100k * 100000;
      this.block = new byte[n + 1 + 20];
      this.fmap = new int[n];
      this.sfmap = new char[2 * n];
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\bzip2\BZip2CompressorOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */