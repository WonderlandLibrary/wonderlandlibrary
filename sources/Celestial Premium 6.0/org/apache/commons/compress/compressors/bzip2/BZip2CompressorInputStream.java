/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2Constants;
import org.apache.commons.compress.compressors.bzip2.CRC;
import org.apache.commons.compress.compressors.bzip2.Rand;

public class BZip2CompressorInputStream
extends CompressorInputStream
implements BZip2Constants {
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
        this.init(true);
        this.initBlock();
    }

    public int read() throws IOException {
        if (this.in != null) {
            int r = this.read0();
            this.count(r < 0 ? -1 : 1);
            return r;
        }
        throw new IOException("stream closed");
    }

    public int read(byte[] dest, int offs, int len) throws IOException {
        int b;
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > dest.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length + ").");
        }
        if (this.in == null) {
            throw new IOException("stream closed");
        }
        int hi = offs + len;
        int destOffs = offs;
        while (destOffs < hi && (b = this.read0()) >= 0) {
            dest[destOffs++] = (byte)b;
            this.count(1);
        }
        int c = destOffs == offs ? -1 : destOffs - offs;
        return c;
    }

    private void makeMaps() {
        boolean[] inUse = this.data.inUse;
        byte[] seqToUnseq = this.data.seqToUnseq;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; ++i) {
            if (!inUse[i]) continue;
            seqToUnseq[nInUseShadow++] = (byte)i;
        }
        this.nInUse = nInUseShadow;
    }

    private int read0() throws IOException {
        switch (this.currentState) {
            case 0: {
                return -1;
            }
            case 1: {
                return this.setupBlock();
            }
            case 2: {
                throw new IllegalStateException();
            }
            case 3: {
                return this.setupRandPartB();
            }
            case 4: {
                return this.setupRandPartC();
            }
            case 5: {
                throw new IllegalStateException();
            }
            case 6: {
                return this.setupNoRandPartB();
            }
            case 7: {
                return this.setupNoRandPartC();
            }
        }
        throw new IllegalStateException();
    }

    private boolean init(boolean isFirstStream) throws IOException {
        if (null == this.in) {
            throw new IOException("No InputStream");
        }
        int magic0 = this.in.read();
        if (magic0 == -1 && !isFirstStream) {
            return false;
        }
        int magic1 = this.in.read();
        int magic2 = this.in.read();
        if (magic0 != 66 || magic1 != 90 || magic2 != 104) {
            throw new IOException(isFirstStream ? "Stream is not in the BZip2 format" : "Garbage after a valid BZip2 stream");
        }
        int blockSize = this.in.read();
        if (blockSize < 49 || blockSize > 57) {
            throw new IOException("BZip2 block size is invalid");
        }
        this.blockSize100k = blockSize - 48;
        this.bsLive = 0;
        this.computedCombinedCRC = 0;
        return true;
    }

    private void initBlock() throws IOException {
        char magic5;
        char magic4;
        char magic3;
        char magic2;
        char magic1;
        char magic0;
        block3: {
            do {
                magic0 = this.bsGetUByte();
                magic1 = this.bsGetUByte();
                magic2 = this.bsGetUByte();
                magic3 = this.bsGetUByte();
                magic4 = this.bsGetUByte();
                magic5 = this.bsGetUByte();
                if (magic0 != '\u0017' || magic1 != 'r' || magic2 != 'E' || magic3 != '8' || magic4 != 'P' || magic5 != '\u0090') break block3;
            } while (!this.complete());
            return;
        }
        if (magic0 != '1' || magic1 != 'A' || magic2 != 'Y' || magic3 != '&' || magic4 != 'S' || magic5 != 'Y') {
            this.currentState = 0;
            throw new IOException("bad block header");
        }
        this.storedBlockCRC = this.bsGetInt();
        boolean bl = this.blockRandomised = this.bsR(1) == 1;
        if (this.data == null) {
            this.data = new Data(this.blockSize100k);
        }
        this.getAndMoveToFrontDecode();
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
        this.storedCombinedCRC = this.bsGetInt();
        this.currentState = 0;
        this.data = null;
        if (this.storedCombinedCRC != this.computedCombinedCRC) {
            throw new IOException("BZip2 CRC error");
        }
        return !this.decompressConcatenated || !this.init(false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void close() throws IOException {
        InputStream inShadow = this.in;
        if (inShadow != null) {
            try {
                if (inShadow != System.in) {
                    inShadow.close();
                }
            }
            finally {
                this.data = null;
                this.in = null;
            }
        }
    }

    private int bsR(int n) throws IOException {
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        if (bsLiveShadow < n) {
            InputStream inShadow = this.in;
            do {
                int thech;
                if ((thech = inShadow.read()) < 0) {
                    throw new IOException("unexpected end of stream");
                }
                bsBuffShadow = bsBuffShadow << 8 | thech;
            } while ((bsLiveShadow += 8) < n);
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
            if (thech < 0) {
                throw new IOException("unexpected end of stream");
            }
            bsBuffShadow = bsBuffShadow << 8 | thech;
            bsLiveShadow += 8;
            this.bsBuff = bsBuffShadow;
        }
        this.bsLive = bsLiveShadow - 1;
        return (bsBuffShadow >> bsLiveShadow - 1 & 1) != 0;
    }

    private char bsGetUByte() throws IOException {
        return (char)this.bsR(8);
    }

    private int bsGetInt() throws IOException {
        return ((this.bsR(8) << 8 | this.bsR(8)) << 8 | this.bsR(8)) << 8 | this.bsR(8);
    }

    private static void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, char[] length, int minLen, int maxLen, int alphaSize) {
        int i;
        int pp = 0;
        for (i = minLen; i <= maxLen; ++i) {
            for (int j = 0; j < alphaSize; ++j) {
                if (length[j] != i) continue;
                perm[pp++] = j;
            }
        }
        i = 23;
        while (--i > 0) {
            base[i] = 0;
            limit[i] = 0;
        }
        for (i = 0; i < alphaSize; ++i) {
            int n = length[i] + '\u0001';
            base[n] = base[n] + 1;
        }
        int b = base[0];
        for (i = 1; i < 23; ++i) {
            base[i] = b += base[i];
        }
        int vec = 0;
        int b2 = base[i];
        for (i = minLen; i <= maxLen; ++i) {
            int nb = base[i + 1];
            b2 = nb;
            limit[i] = (vec += nb - b2) - 1;
            vec <<= 1;
        }
        for (i = minLen + 1; i <= maxLen; ++i) {
            base[i] = (limit[i - 1] + 1 << 1) - base[i];
        }
    }

    private void recvDecodingTables() throws IOException {
        int i;
        int i2;
        Data dataShadow = this.data;
        boolean[] inUse = dataShadow.inUse;
        byte[] pos = dataShadow.recvDecodingTables_pos;
        byte[] selector = dataShadow.selector;
        byte[] selectorMtf = dataShadow.selectorMtf;
        int inUse16 = 0;
        for (i2 = 0; i2 < 16; ++i2) {
            if (!this.bsGetBit()) continue;
            inUse16 |= 1 << i2;
        }
        i2 = 256;
        while (--i2 >= 0) {
            inUse[i2] = false;
        }
        for (i2 = 0; i2 < 16; ++i2) {
            if ((inUse16 & 1 << i2) == 0) continue;
            int i16 = i2 << 4;
            for (int j = 0; j < 16; ++j) {
                if (!this.bsGetBit()) continue;
                inUse[i16 + j] = true;
            }
        }
        this.makeMaps();
        int alphaSize = this.nInUse + 2;
        int nGroups = this.bsR(3);
        int nSelectors = this.bsR(15);
        for (i = 0; i < nSelectors; ++i) {
            int j = 0;
            while (this.bsGetBit()) {
                ++j;
            }
            selectorMtf[i] = (byte)j;
        }
        int v = nGroups;
        while (--v >= 0) {
            pos[v] = (byte)v;
        }
        for (i = 0; i < nSelectors; ++i) {
            int v2;
            byte tmp = pos[v2];
            for (v2 = selectorMtf[i] & 0xFF; v2 > 0; --v2) {
                pos[v2] = pos[v2 - 1];
            }
            pos[0] = tmp;
            selector[i] = tmp;
        }
        char[][] len = dataShadow.temp_charArray2d;
        for (int t = 0; t < nGroups; ++t) {
            int curr = this.bsR(5);
            char[] len_t = len[t];
            for (int i3 = 0; i3 < alphaSize; ++i3) {
                while (this.bsGetBit()) {
                    curr += this.bsGetBit() ? -1 : 1;
                }
                len_t[i3] = (char)curr;
            }
        }
        this.createHuffmanDecodingTables(alphaSize, nGroups);
    }

    private void createHuffmanDecodingTables(int alphaSize, int nGroups) {
        Data dataShadow = this.data;
        char[][] len = dataShadow.temp_charArray2d;
        int[] minLens = dataShadow.minLens;
        int[][] limit = dataShadow.limit;
        int[][] base = dataShadow.base;
        int[][] perm = dataShadow.perm;
        for (int t = 0; t < nGroups; ++t) {
            int minLen = 32;
            int maxLen = 0;
            char[] len_t = len[t];
            int i = alphaSize;
            while (--i >= 0) {
                int lent = len_t[i];
                if (lent > maxLen) {
                    maxLen = lent;
                }
                if (lent >= minLen) continue;
                minLen = lent;
            }
            BZip2CompressorInputStream.hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
            minLens[t] = minLen;
        }
    }

    private void getAndMoveToFrontDecode() throws IOException {
        this.origPtr = this.bsR(24);
        this.recvDecodingTables();
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
        int i = 256;
        while (--i >= 0) {
            yy[i] = (char)i;
            unzftab[i] = 0;
        }
        int groupNo = 0;
        int groupPos = 49;
        int eob = this.nInUse + 1;
        int nextSym = this.getAndMoveToFrontDecode0(0);
        int bsBuffShadow = this.bsBuff;
        int bsLiveShadow = this.bsLive;
        int lastShadow = -1;
        int zt = selector[groupNo] & 0xFF;
        int[] base_zt = base[zt];
        int[] limit_zt = limit[zt];
        int[] perm_zt = perm[zt];
        int minLens_zt = minLens[zt];
        while (nextSym != eob) {
            int thech;
            if (nextSym == 0 || nextSym == 1) {
                int s = -1;
                int n = 1;
                while (true) {
                    if (nextSym == 0) {
                        s += n;
                    } else {
                        if (nextSym != 1) break;
                        s += n << 1;
                    }
                    if (groupPos == 0) {
                        groupPos = 49;
                        zt = selector[++groupNo] & 0xFF;
                        base_zt = base[zt];
                        limit_zt = limit[zt];
                        perm_zt = perm[zt];
                        minLens_zt = minLens[zt];
                    } else {
                        --groupPos;
                    }
                    int zn = minLens_zt;
                    while (bsLiveShadow < zn) {
                        thech = inShadow.read();
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
                        ++zn;
                        while (bsLiveShadow < 1) {
                            int thech2 = inShadow.read();
                            if (thech2 >= 0) {
                                bsBuffShadow = bsBuffShadow << 8 | thech2;
                                bsLiveShadow += 8;
                                continue;
                            }
                            throw new IOException("unexpected end of stream");
                        }
                        zvec = zvec << 1 | bsBuffShadow >> --bsLiveShadow & 1;
                    }
                    nextSym = perm_zt[zvec - base_zt[zn]];
                    n <<= 1;
                }
                byte ch = seqToUnseq[yy[0]];
                int n2 = ch & 0xFF;
                unzftab[n2] = unzftab[n2] + (s + 1);
                while (s-- >= 0) {
                    ll8[++lastShadow] = ch;
                }
                if (lastShadow < limitLast) continue;
                throw new IOException("block overrun");
            }
            if (++lastShadow >= limitLast) {
                throw new IOException("block overrun");
            }
            char tmp = yy[nextSym - 1];
            int n = seqToUnseq[tmp] & 0xFF;
            unzftab[n] = unzftab[n] + 1;
            ll8[lastShadow] = seqToUnseq[tmp];
            if (nextSym <= 16) {
                int j = nextSym - 1;
                while (j > 0) {
                    yy[j--] = yy[j];
                }
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
                --groupPos;
            }
            int zn = minLens_zt;
            while (bsLiveShadow < zn) {
                int thech3 = inShadow.read();
                if (thech3 >= 0) {
                    bsBuffShadow = bsBuffShadow << 8 | thech3;
                    bsLiveShadow += 8;
                    continue;
                }
                throw new IOException("unexpected end of stream");
            }
            int zvec = bsBuffShadow >> bsLiveShadow - zn & (1 << zn) - 1;
            bsLiveShadow -= zn;
            while (zvec > limit_zt[zn]) {
                ++zn;
                while (bsLiveShadow < 1) {
                    thech = inShadow.read();
                    if (thech >= 0) {
                        bsBuffShadow = bsBuffShadow << 8 | thech;
                        bsLiveShadow += 8;
                        continue;
                    }
                    throw new IOException("unexpected end of stream");
                }
                zvec = zvec << 1 | bsBuffShadow >> --bsLiveShadow & 1;
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
        int zvec = this.bsR(zn);
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        while (zvec > limit_zt[zn]) {
            ++zn;
            while (bsLiveShadow < 1) {
                int thech = inShadow.read();
                if (thech >= 0) {
                    bsBuffShadow = bsBuffShadow << 8 | thech;
                    bsLiveShadow += 8;
                    continue;
                }
                throw new IOException("unexpected end of stream");
            }
            zvec = zvec << 1 | bsBuffShadow >> --bsLiveShadow & 1;
        }
        this.bsLive = bsLiveShadow;
        this.bsBuff = bsBuffShadow;
        return dataShadow.perm[zt][zvec - dataShadow.base[zt][zn]];
    }

    private int setupBlock() throws IOException {
        int i;
        if (this.currentState == 0 || this.data == null) {
            return -1;
        }
        int[] cftab = this.data.cftab;
        int[] tt = this.data.initTT(this.last + 1);
        byte[] ll8 = this.data.ll8;
        cftab[0] = 0;
        System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);
        int c = cftab[0];
        for (i = 1; i <= 256; ++i) {
            cftab[i] = c += cftab[i];
        }
        i = 0;
        int lastShadow = this.last;
        while (i <= lastShadow) {
            int n = ll8[i] & 0xFF;
            int n2 = cftab[n];
            cftab[n] = n2 + 1;
            tt[n2] = i++;
        }
        if (this.origPtr < 0 || this.origPtr >= tt.length) {
            throw new IOException("stream corrupted");
        }
        this.su_tPos = tt[this.origPtr];
        this.su_count = 0;
        this.su_i2 = 0;
        this.su_ch2 = 256;
        if (this.blockRandomised) {
            this.su_rNToGo = 0;
            this.su_rTPos = 0;
            return this.setupRandPartA();
        }
        return this.setupNoRandPartA();
    }

    private int setupRandPartA() throws IOException {
        if (this.su_i2 <= this.last) {
            this.su_chPrev = this.su_ch2;
            int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
            this.su_tPos = this.data.tt[this.su_tPos];
            if (this.su_rNToGo == 0) {
                this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
                if (++this.su_rTPos == 512) {
                    this.su_rTPos = 0;
                }
            } else {
                --this.su_rNToGo;
            }
            this.su_ch2 = su_ch2Shadow ^= this.su_rNToGo == 1 ? 1 : 0;
            ++this.su_i2;
            this.currentState = 3;
            this.crc.updateCRC(su_ch2Shadow);
            return su_ch2Shadow;
        }
        this.endBlock();
        this.initBlock();
        return this.setupBlock();
    }

    private int setupNoRandPartA() throws IOException {
        if (this.su_i2 <= this.last) {
            int su_ch2Shadow;
            this.su_chPrev = this.su_ch2;
            this.su_ch2 = su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
            this.su_tPos = this.data.tt[this.su_tPos];
            ++this.su_i2;
            this.currentState = 6;
            this.crc.updateCRC(su_ch2Shadow);
            return su_ch2Shadow;
        }
        this.currentState = 5;
        this.endBlock();
        this.initBlock();
        return this.setupBlock();
    }

    private int setupRandPartB() throws IOException {
        if (this.su_ch2 != this.su_chPrev) {
            this.currentState = 2;
            this.su_count = 1;
            return this.setupRandPartA();
        }
        if (++this.su_count >= 4) {
            this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
            this.su_tPos = this.data.tt[this.su_tPos];
            if (this.su_rNToGo == 0) {
                this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
                if (++this.su_rTPos == 512) {
                    this.su_rTPos = 0;
                }
            } else {
                --this.su_rNToGo;
            }
            this.su_j2 = 0;
            this.currentState = 4;
            if (this.su_rNToGo == 1) {
                this.su_z = (char)(this.su_z ^ '\u0001');
            }
            return this.setupRandPartC();
        }
        this.currentState = 2;
        return this.setupRandPartA();
    }

    private int setupRandPartC() throws IOException {
        if (this.su_j2 < this.su_z) {
            this.crc.updateCRC(this.su_ch2);
            ++this.su_j2;
            return this.su_ch2;
        }
        this.currentState = 2;
        ++this.su_i2;
        this.su_count = 0;
        return this.setupRandPartA();
    }

    private int setupNoRandPartB() throws IOException {
        if (this.su_ch2 != this.su_chPrev) {
            this.su_count = 1;
            return this.setupNoRandPartA();
        }
        if (++this.su_count >= 4) {
            this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
            this.su_tPos = this.data.tt[this.su_tPos];
            this.su_j2 = 0;
            return this.setupNoRandPartC();
        }
        return this.setupNoRandPartA();
    }

    private int setupNoRandPartC() throws IOException {
        if (this.su_j2 < this.su_z) {
            int su_ch2Shadow = this.su_ch2;
            this.crc.updateCRC(su_ch2Shadow);
            ++this.su_j2;
            this.currentState = 7;
            return su_ch2Shadow;
        }
        ++this.su_i2;
        this.su_count = 0;
        return this.setupNoRandPartA();
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < 3) {
            return false;
        }
        if (signature[0] != 66) {
            return false;
        }
        if (signature[1] != 90) {
            return false;
        }
        return signature[2] == 104;
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
            if (ttShadow == null || ttShadow.length < length) {
                this.tt = ttShadow = new int[length];
            }
            return ttShadow;
        }
    }
}

