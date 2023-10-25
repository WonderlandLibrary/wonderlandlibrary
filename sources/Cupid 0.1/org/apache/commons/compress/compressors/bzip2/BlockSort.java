package org.apache.commons.compress.compressors.bzip2;

import java.util.BitSet;

class BlockSort {
  private static final int QSORT_STACK_SIZE = 1000;
  
  private static final int FALLBACK_QSORT_STACK_SIZE = 100;
  
  private static final int STACK_SIZE = 1000;
  
  private int workDone;
  
  private int workLimit;
  
  private boolean firstAttempt;
  
  private final int[] stack_ll = new int[1000];
  
  private final int[] stack_hh = new int[1000];
  
  private final int[] stack_dd = new int[1000];
  
  private final int[] mainSort_runningOrder = new int[256];
  
  private final int[] mainSort_copy = new int[256];
  
  private final boolean[] mainSort_bigDone = new boolean[256];
  
  private final int[] ftab = new int[65537];
  
  private final char[] quadrant;
  
  private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
  
  private int[] eclass;
  
  BlockSort(BZip2CompressorOutputStream.Data data) {
    this.quadrant = data.sfmap;
  }
  
  void blockSort(BZip2CompressorOutputStream.Data data, int last) {
    this.workLimit = 30 * last;
    this.workDone = 0;
    this.firstAttempt = true;
    if (last + 1 < 10000) {
      fallbackSort(data, last);
    } else {
      mainSort(data, last);
      if (this.firstAttempt && this.workDone > this.workLimit)
        fallbackSort(data, last); 
    } 
    int[] fmap = data.fmap;
    data.origPtr = -1;
    for (int i = 0; i <= last; i++) {
      if (fmap[i] == 0) {
        data.origPtr = i;
        break;
      } 
    } 
  }
  
  final void fallbackSort(BZip2CompressorOutputStream.Data data, int last) {
    data.block[0] = data.block[last + 1];
    fallbackSort(data.fmap, data.block, last + 1);
    int i;
    for (i = 0; i < last + 1; i++)
      data.fmap[i] = data.fmap[i] - 1; 
    for (i = 0; i < last + 1; i++) {
      if (data.fmap[i] == -1) {
        data.fmap[i] = last;
        break;
      } 
    } 
  }
  
  private void fallbackSimpleSort(int[] fmap, int[] eclass, int lo, int hi) {
    if (lo == hi)
      return; 
    if (hi - lo > 3)
      for (int j = hi - 4; j >= lo; j--) {
        int tmp = fmap[j];
        int ec_tmp = eclass[tmp];
        int k;
        for (k = j + 4; k <= hi && ec_tmp > eclass[fmap[k]]; 
          k += 4)
          fmap[k - 4] = fmap[k]; 
        fmap[k - 4] = tmp;
      }  
    for (int i = hi - 1; i >= lo; i--) {
      int tmp = fmap[i];
      int ec_tmp = eclass[tmp];
      int j;
      for (j = i + 1; j <= hi && ec_tmp > eclass[fmap[j]]; j++)
        fmap[j - 1] = fmap[j]; 
      fmap[j - 1] = tmp;
    } 
  }
  
  private void fswap(int[] fmap, int zz1, int zz2) {
    int zztmp = fmap[zz1];
    fmap[zz1] = fmap[zz2];
    fmap[zz2] = zztmp;
  }
  
  private void fvswap(int[] fmap, int yyp1, int yyp2, int yyn) {
    while (yyn > 0) {
      fswap(fmap, yyp1, yyp2);
      yyp1++;
      yyp2++;
      yyn--;
    } 
  }
  
  private int fmin(int a, int b) {
    return (a < b) ? a : b;
  }
  
  private void fpush(int sp, int lz, int hz) {
    this.stack_ll[sp] = lz;
    this.stack_hh[sp] = hz;
  }
  
  private int[] fpop(int sp) {
    return new int[] { this.stack_ll[sp], this.stack_hh[sp] };
  }
  
  private void fallbackQSort3(int[] fmap, int[] eclass, int loSt, int hiSt) {
    long r = 0L;
    int sp = 0;
    fpush(sp++, loSt, hiSt);
    while (sp > 0) {
      long med;
      int[] s = fpop(--sp);
      int lo = s[0], hi = s[1];
      if (hi - lo < 10) {
        fallbackSimpleSort(fmap, eclass, lo, hi);
        continue;
      } 
      r = (r * 7621L + 1L) % 32768L;
      long r3 = r % 3L;
      if (r3 == 0L) {
        med = eclass[fmap[lo]];
      } else if (r3 == 1L) {
        med = eclass[fmap[lo + hi >>> 1]];
      } else {
        med = eclass[fmap[hi]];
      } 
      int ltLo = lo, unLo = ltLo;
      int gtHi = hi, unHi = gtHi;
      while (true) {
        if (unLo <= unHi) {
          int i = eclass[fmap[unLo]] - (int)med;
          if (i == 0) {
            fswap(fmap, unLo, ltLo);
            ltLo++;
            unLo++;
            continue;
          } 
          if (i <= 0) {
            unLo++;
            continue;
          } 
        } 
        while (unLo <= unHi) {
          int i = eclass[fmap[unHi]] - (int)med;
          if (i == 0) {
            fswap(fmap, unHi, gtHi);
            gtHi--;
            unHi--;
            continue;
          } 
          if (i < 0)
            break; 
          unHi--;
        } 
        if (unLo > unHi)
          break; 
        fswap(fmap, unLo, unHi);
        unLo++;
        unHi--;
      } 
      if (gtHi < ltLo)
        continue; 
      int n = fmin(ltLo - lo, unLo - ltLo);
      fvswap(fmap, lo, unLo - n, n);
      int m = fmin(hi - gtHi, gtHi - unHi);
      fvswap(fmap, unHi + 1, hi - m + 1, m);
      n = lo + unLo - ltLo - 1;
      m = hi - gtHi - unHi + 1;
      if (n - lo > hi - m) {
        fpush(sp++, lo, n);
        fpush(sp++, m, hi);
        continue;
      } 
      fpush(sp++, m, hi);
      fpush(sp++, lo, n);
    } 
  }
  
  private int[] getEclass() {
    return (this.eclass == null) ? (this.eclass = new int[this.quadrant.length / 2]) : this.eclass;
  }
  
  final void fallbackSort(int[] fmap, byte[] block, int nblock) {
    int nNotDone, ftab[] = new int[257];
    int[] eclass = getEclass();
    int i;
    for (i = 0; i < nblock; i++)
      eclass[i] = 0; 
    for (i = 0; i < nblock; i++)
      ftab[block[i] & 0xFF] = ftab[block[i] & 0xFF] + 1; 
    for (i = 1; i < 257; i++)
      ftab[i] = ftab[i] + ftab[i - 1]; 
    for (i = 0; i < nblock; i++) {
      int j = block[i] & 0xFF;
      int k = ftab[j] - 1;
      ftab[j] = k;
      fmap[k] = i;
    } 
    int nBhtab = 64 + nblock;
    BitSet bhtab = new BitSet(nBhtab);
    for (i = 0; i < 256; i++)
      bhtab.set(ftab[i]); 
    for (i = 0; i < 32; i++) {
      bhtab.set(nblock + 2 * i);
      bhtab.clear(nblock + 2 * i + 1);
    } 
    int H = 1;
    do {
      int j = 0;
      for (i = 0; i < nblock; i++) {
        if (bhtab.get(i))
          j = i; 
        int k = fmap[i] - H;
        if (k < 0)
          k += nblock; 
        eclass[k] = j;
      } 
      nNotDone = 0;
      int r = -1;
      while (true) {
        int k = r + 1;
        k = bhtab.nextClearBit(k);
        int l = k - 1;
        if (l >= nblock)
          break; 
        k = bhtab.nextSetBit(k + 1);
        r = k - 1;
        if (r >= nblock)
          break; 
        if (r > l) {
          nNotDone += r - l + 1;
          fallbackQSort3(fmap, eclass, l, r);
          int cc = -1;
          for (i = l; i <= r; i++) {
            int cc1 = eclass[fmap[i]];
            if (cc != cc1) {
              bhtab.set(i);
              cc = cc1;
            } 
          } 
        } 
      } 
      H *= 2;
    } while (H <= nblock && nNotDone != 0);
  }
  
  private static final int[] INCS = new int[] { 
      1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 
      88573, 265720, 797161, 2391484 };
  
  private static final int SMALL_THRESH = 20;
  
  private static final int DEPTH_THRESH = 10;
  
  private static final int WORK_FACTOR = 30;
  
  private static final int SETMASK = 2097152;
  
  private static final int CLEARMASK = -2097153;
  
  private boolean mainSimpleSort(BZip2CompressorOutputStream.Data dataShadow, int lo, int hi, int d, int lastShadow) {
    int bigN = hi - lo + 1;
    if (bigN < 2)
      return (this.firstAttempt && this.workDone > this.workLimit); 
    int hp = 0;
    while (INCS[hp] < bigN)
      hp++; 
    int[] fmap = dataShadow.fmap;
    char[] quadrant = this.quadrant;
    byte[] block = dataShadow.block;
    int lastPlus1 = lastShadow + 1;
    boolean firstAttemptShadow = this.firstAttempt;
    int workLimitShadow = this.workLimit;
    int workDoneShadow = this.workDone;
    label97: while (--hp >= 0) {
      int h = INCS[hp];
      int mj = lo + h - 1;
      for (int i = lo + h; i <= hi; ) {
        for (int k = 3; i <= hi && --k >= 0; i++) {
          int v = fmap[i];
          int vd = v + d;
          int j = i;
          boolean onceRunned = false;
          int a = 0;
          while (true) {
            if (onceRunned) {
              fmap[j] = a;
              if ((j -= h) <= mj)
                break; 
            } else {
              onceRunned = true;
            } 
            a = fmap[j - h];
            int i1 = a + d;
            int i2 = vd;
            if (block[i1 + 1] == block[i2 + 1]) {
              if (block[i1 + 2] == block[i2 + 2]) {
                if (block[i1 + 3] == block[i2 + 3]) {
                  if (block[i1 + 4] == block[i2 + 4]) {
                    if (block[i1 + 5] == block[i2 + 5]) {
                      i1 += 6;
                      i2 += 6;
                      if (block[i1] == block[i2]) {
                        int x = lastShadow;
                        while (x > 0) {
                          x -= 4;
                          if (block[i1 + 1] == block[i2 + 1]) {
                            if (quadrant[i1] == quadrant[i2]) {
                              if (block[i1 + 2] == block[i2 + 2]) {
                                if (quadrant[i1 + 1] == quadrant[i2 + 1]) {
                                  if (block[i1 + 3] == block[i2 + 3]) {
                                    if (quadrant[i1 + 2] == quadrant[i2 + 2]) {
                                      if (block[i1 + 4] == block[i2 + 4]) {
                                        if (quadrant[i1 + 3] == quadrant[i2 + 3]) {
                                          i1 += 4;
                                          if (i1 >= lastPlus1)
                                            i1 -= lastPlus1; 
                                          i2 += 4;
                                          if (i2 >= lastPlus1)
                                            i2 -= lastPlus1; 
                                          workDoneShadow++;
                                          continue;
                                        } 
                                        if (quadrant[i1 + 3] > quadrant[i2 + 3])
                                          continue; 
                                        break;
                                      } 
                                      if ((block[i1 + 4] & 0xFF) > (block[i2 + 4] & 0xFF))
                                        continue; 
                                      break;
                                    } 
                                    if (quadrant[i1 + 2] > quadrant[i2 + 2])
                                      continue; 
                                    break;
                                  } 
                                  if ((block[i1 + 3] & 0xFF) > (block[i2 + 3] & 0xFF))
                                    continue; 
                                  break;
                                } 
                                if (quadrant[i1 + 1] > quadrant[i2 + 1])
                                  continue; 
                                break;
                              } 
                              if ((block[i1 + 2] & 0xFF) > (block[i2 + 2] & 0xFF))
                                continue; 
                              break;
                            } 
                            if (quadrant[i1] > quadrant[i2])
                              continue; 
                            break;
                          } 
                          if ((block[i1 + 1] & 0xFF) > (block[i2 + 1] & 0xFF));
                        } 
                        break;
                      } 
                      if ((block[i1] & 0xFF) > (block[i2] & 0xFF))
                        continue; 
                      break;
                    } 
                    if ((block[i1 + 5] & 0xFF) > (block[i2 + 5] & 0xFF))
                      continue; 
                    break;
                  } 
                  if ((block[i1 + 4] & 0xFF) > (block[i2 + 4] & 0xFF))
                    continue; 
                  break;
                } 
                if ((block[i1 + 3] & 0xFF) > (block[i2 + 3] & 0xFF))
                  continue; 
                break;
              } 
              if ((block[i1 + 2] & 0xFF) > (block[i2 + 2] & 0xFF))
                continue; 
              break;
            } 
            if ((block[i1 + 1] & 0xFF) > (block[i2 + 1] & 0xFF))
              continue; 
            break;
          } 
          fmap[j] = v;
        } 
        if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow)
          break label97; 
      } 
    } 
    this.workDone = workDoneShadow;
    return (firstAttemptShadow && workDoneShadow > workLimitShadow);
  }
  
  private static void vswap(int[] fmap, int p1, int p2, int n) {
    n += p1;
    while (p1 < n) {
      int t = fmap[p1];
      fmap[p1++] = fmap[p2];
      fmap[p2++] = t;
    } 
  }
  
  private static byte med3(byte a, byte b, byte c) {
    return (a < b) ? ((b < c) ? b : ((a < c) ? c : a)) : ((b > c) ? b : ((a > c) ? c : a));
  }
  
  private void mainQSort3(BZip2CompressorOutputStream.Data dataShadow, int loSt, int hiSt, int dSt, int last) {
    int[] stack_ll = this.stack_ll;
    int[] stack_hh = this.stack_hh;
    int[] stack_dd = this.stack_dd;
    int[] fmap = dataShadow.fmap;
    byte[] block = dataShadow.block;
    stack_ll[0] = loSt;
    stack_hh[0] = hiSt;
    stack_dd[0] = dSt;
    for (int sp = 1; --sp >= 0; ) {
      int lo = stack_ll[sp];
      int hi = stack_hh[sp];
      int d = stack_dd[sp];
      if (hi - lo < 20 || d > 10) {
        if (mainSimpleSort(dataShadow, lo, hi, d, last))
          return; 
        continue;
      } 
      int d1 = d + 1;
      int med = med3(block[fmap[lo] + d1], block[fmap[hi] + d1], block[fmap[lo + hi >>> 1] + d1]) & 0xFF;
      int unLo = lo;
      int unHi = hi;
      int ltLo = lo;
      int gtHi = hi;
      while (true) {
        if (unLo <= unHi) {
          int i = (block[fmap[unLo] + d1] & 0xFF) - med;
          if (i == 0) {
            int temp = fmap[unLo];
            fmap[unLo++] = fmap[ltLo];
            fmap[ltLo++] = temp;
            continue;
          } 
          if (i < 0) {
            unLo++;
            continue;
          } 
        } 
        while (unLo <= unHi) {
          int i = (block[fmap[unHi] + d1] & 0xFF) - med;
          if (i == 0) {
            int temp = fmap[unHi];
            fmap[unHi--] = fmap[gtHi];
            fmap[gtHi--] = temp;
            continue;
          } 
          if (i > 0)
            unHi--; 
        } 
        if (unLo <= unHi) {
          int temp = fmap[unLo];
          fmap[unLo++] = fmap[unHi];
          fmap[unHi--] = temp;
          continue;
        } 
        break;
      } 
      if (gtHi < ltLo) {
        stack_ll[sp] = lo;
        stack_hh[sp] = hi;
        stack_dd[sp] = d1;
        sp++;
        continue;
      } 
      int n = (ltLo - lo < unLo - ltLo) ? (ltLo - lo) : (unLo - ltLo);
      vswap(fmap, lo, unLo - n, n);
      int m = (hi - gtHi < gtHi - unHi) ? (hi - gtHi) : (gtHi - unHi);
      vswap(fmap, unLo, hi - m + 1, m);
      n = lo + unLo - ltLo - 1;
      m = hi - gtHi - unHi + 1;
      stack_ll[sp] = lo;
      stack_hh[sp] = n;
      stack_dd[sp] = d;
      sp++;
      stack_ll[sp] = n + 1;
      stack_hh[sp] = m - 1;
      stack_dd[sp] = d1;
      sp++;
      stack_ll[sp] = m;
      stack_hh[sp] = hi;
      stack_dd[sp] = d;
      sp++;
    } 
  }
  
  final void mainSort(BZip2CompressorOutputStream.Data dataShadow, int lastShadow) {
    int[] runningOrder = this.mainSort_runningOrder;
    int[] copy = this.mainSort_copy;
    boolean[] bigDone = this.mainSort_bigDone;
    int[] ftab = this.ftab;
    byte[] block = dataShadow.block;
    int[] fmap = dataShadow.fmap;
    char[] quadrant = this.quadrant;
    int workLimitShadow = this.workLimit;
    boolean firstAttemptShadow = this.firstAttempt;
    int i;
    for (i = 65537; --i >= 0;)
      ftab[i] = 0; 
    for (i = 0; i < 20; i++)
      block[lastShadow + i + 2] = block[i % (lastShadow + 1) + 1]; 
    for (i = lastShadow + 20 + 1; --i >= 0;)
      quadrant[i] = Character.MIN_VALUE; 
    block[0] = block[lastShadow + 1];
    int c1 = block[0] & 0xFF;
    int k;
    for (k = 0; k <= lastShadow; k++) {
      int c2 = block[k + 1] & 0xFF;
      ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] + 1;
      c1 = c2;
    } 
    for (k = 1; k <= 65536; k++)
      ftab[k] = ftab[k] + ftab[k - 1]; 
    c1 = block[1] & 0xFF;
    for (k = 0; k < lastShadow; k++) {
      int c2 = block[k + 2] & 0xFF;
      ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] - 1;
      fmap[ftab[(c1 << 8) + c2] - 1] = k;
      c1 = c2;
    } 
    ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] = ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1;
    fmap[ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1] = lastShadow;
    for (k = 256; --k >= 0; ) {
      bigDone[k] = false;
      runningOrder[k] = k;
    } 
    for (int h = 364; h != 1; ) {
      h /= 3;
      for (int m = h; m <= 255; m++) {
        int vv = runningOrder[m];
        int a = ftab[vv + 1 << 8] - ftab[vv << 8];
        int b = h - 1;
        int n = m;
        int ro;
        for (ro = runningOrder[n - h]; ftab[ro + 1 << 8] - ftab[ro << 8] > a; ro = runningOrder[n - h]) {
          runningOrder[n] = ro;
          n -= h;
          if (n <= b)
            break; 
        } 
        runningOrder[n] = vv;
      } 
    } 
    for (int j = 0; j <= 255; j++) {
      int ss = runningOrder[j];
      int m;
      for (m = 0; m <= 255; m++) {
        int sb = (ss << 8) + m;
        int ftab_sb = ftab[sb];
        if ((ftab_sb & 0x200000) != 2097152) {
          int lo = ftab_sb & 0xFFDFFFFF;
          int hi = (ftab[sb + 1] & 0xFFDFFFFF) - 1;
          if (hi > lo) {
            mainQSort3(dataShadow, lo, hi, 2, lastShadow);
            if (firstAttemptShadow && this.workDone > workLimitShadow)
              return; 
          } 
          ftab[sb] = ftab_sb | 0x200000;
        } 
      } 
      for (m = 0; m <= 255; m++)
        copy[m] = ftab[(m << 8) + ss] & 0xFFDFFFFF; 
      int hj;
      for (m = ftab[ss << 8] & 0xFFDFFFFF, hj = ftab[ss + 1 << 8] & 0xFFDFFFFF; m < hj; m++) {
        int fmap_j = fmap[m];
        c1 = block[fmap_j] & 0xFF;
        if (!bigDone[c1]) {
          fmap[copy[c1]] = (fmap_j == 0) ? lastShadow : (fmap_j - 1);
          copy[c1] = copy[c1] + 1;
        } 
      } 
      for (m = 256; --m >= 0;)
        ftab[(m << 8) + ss] = ftab[(m << 8) + ss] | 0x200000; 
      bigDone[ss] = true;
      if (j < 255) {
        int bbStart = ftab[ss << 8] & 0xFFDFFFFF;
        int bbSize = (ftab[ss + 1 << 8] & 0xFFDFFFFF) - bbStart;
        int shifts = 0;
        while (bbSize >> shifts > 65534)
          shifts++; 
        for (int n = 0; n < bbSize; n++) {
          int a2update = fmap[bbStart + n];
          char qVal = (char)(n >> shifts);
          quadrant[a2update] = qVal;
          if (a2update < 20)
            quadrant[a2update + lastShadow + 1] = qVal; 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\bzip2\BlockSort.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */