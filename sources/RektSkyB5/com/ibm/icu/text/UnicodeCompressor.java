/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.SCSU;

public final class UnicodeCompressor
implements SCSU {
    private static boolean[] sSingleTagTable = new boolean[]{false, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static boolean[] sUnicodeTagTable = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private int fCurrentWindow = 0;
    private int[] fOffsets = new int[8];
    private int fMode = 0;
    private int[] fIndexCount = new int[256];
    private int[] fTimeStamps = new int[8];
    private int fTimeStamp = 0;

    public UnicodeCompressor() {
        this.reset();
    }

    public static byte[] compress(String buffer) {
        return UnicodeCompressor.compress(buffer.toCharArray(), 0, buffer.length());
    }

    public static byte[] compress(char[] buffer, int start, int limit) {
        UnicodeCompressor comp = new UnicodeCompressor();
        int len = Math.max(4, 3 * (limit - start) + 1);
        byte[] temp = new byte[len];
        int byteCount = comp.compress(buffer, start, limit, null, temp, 0, len);
        byte[] result = new byte[byteCount];
        System.arraycopy(temp, 0, result, 0, byteCount);
        return result;
    }

    /*
     * Enabled aggressive block sorting
     */
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
        if (byteBuffer.length < 4 || byteBufferLimit - byteBufferStart < 4) {
            throw new IllegalArgumentException("byteBuffer.length < 4");
        }
        block4: while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
            block47: {
                block48: {
                    switch (this.fMode) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            break block48;
                        }
                    }
                    while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
                        curUC = charBuffer[ucPos++];
                        nextUC = ucPos < charBufferLimit ? charBuffer[ucPos] : -1;
                        if (curUC < 128) {
                            loByte = curUC & 0xFF;
                            if (sSingleTagTable[loByte]) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break block4;
                                }
                                byteBuffer[bytePos++] = 1;
                            }
                            byteBuffer[bytePos++] = (byte)loByte;
                            continue;
                        }
                        if (this.inDynamicWindow(curUC, this.fCurrentWindow)) {
                            byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[this.fCurrentWindow] + 128);
                            continue;
                        }
                        if (!UnicodeCompressor.isCompressible(curUC)) {
                            if (nextUC != -1 && UnicodeCompressor.isCompressible(nextUC)) {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break block4;
                                }
                                byteBuffer[bytePos++] = 14;
                                byteBuffer[bytePos++] = (byte)(curUC >>> 8);
                                byteBuffer[bytePos++] = (byte)(curUC & 0xFF);
                                continue;
                            }
                            if (bytePos + 3 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            byteBuffer[bytePos++] = 15;
                            hiByte = curUC >>> 8;
                            loByte = curUC & 0xFF;
                            if (sUnicodeTagTable[hiByte]) {
                                byteBuffer[bytePos++] = -16;
                            }
                            byteBuffer[bytePos++] = (byte)hiByte;
                            byteBuffer[bytePos++] = (byte)loByte;
                            this.fMode = 1;
                            break block47;
                        } else {
                            whichWindow = this.findDynamicWindow(curUC);
                            if (whichWindow != -1) {
                                forwardUC = ucPos + 1 < charBufferLimit ? charBuffer[ucPos + 1] : -1;
                                if (this.inDynamicWindow(nextUC, whichWindow) && this.inDynamicWindow(forwardUC, whichWindow)) {
                                    if (bytePos + 1 >= byteBufferLimit) {
                                        --ucPos;
                                        break block4;
                                    }
                                    byteBuffer[bytePos++] = (byte)(16 + whichWindow);
                                    byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                                    this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                    this.fCurrentWindow = whichWindow;
                                    continue;
                                }
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break block4;
                                }
                                byteBuffer[bytePos++] = (byte)(1 + whichWindow);
                                byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                                continue;
                            }
                            whichWindow = UnicodeCompressor.findStaticWindow(curUC);
                            if (whichWindow != -1 && !UnicodeCompressor.inStaticWindow(nextUC, whichWindow)) {
                                if (bytePos + 1 >= byteBufferLimit) {
                                    --ucPos;
                                    break block4;
                                }
                                byteBuffer[bytePos++] = (byte)(1 + whichWindow);
                                byteBuffer[bytePos++] = (byte)(curUC - sOffsets[whichWindow]);
                                continue;
                            }
                            int n2 = curIndex = UnicodeCompressor.makeIndex(curUC);
                            this.fIndexCount[n2] = this.fIndexCount[n2] + 1;
                            forwardUC = ucPos + 1 < charBufferLimit ? charBuffer[ucPos + 1] : -1;
                            if (this.fIndexCount[curIndex] > 1 || curIndex == UnicodeCompressor.makeIndex(nextUC) && curIndex == UnicodeCompressor.makeIndex(forwardUC)) {
                                if (bytePos + 2 >= byteBufferLimit) {
                                    --ucPos;
                                    break block4;
                                }
                                whichWindow = this.getLRDefinedWindow();
                                byteBuffer[bytePos++] = (byte)(24 + whichWindow);
                                byteBuffer[bytePos++] = (byte)curIndex;
                                byteBuffer[bytePos++] = (byte)(curUC - sOffsetTable[curIndex] + 128);
                                this.fOffsets[whichWindow] = sOffsetTable[curIndex];
                                this.fCurrentWindow = whichWindow;
                                this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                                continue;
                            }
                            if (bytePos + 3 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            byteBuffer[bytePos++] = 15;
                            hiByte = curUC >>> 8;
                            loByte = curUC & 0xFF;
                            if (sUnicodeTagTable[hiByte]) {
                                byteBuffer[bytePos++] = -16;
                            }
                            byteBuffer[bytePos++] = (byte)hiByte;
                            byteBuffer[bytePos++] = (byte)loByte;
                            this.fMode = 1;
                        }
                        break block47;
                    }
                    break block47;
                }
                while (ucPos < charBufferLimit && bytePos < byteBufferLimit) {
                    curUC = charBuffer[ucPos++];
                    nextUC = ucPos < charBufferLimit ? charBuffer[ucPos] : -1;
                    if (!UnicodeCompressor.isCompressible(curUC) || nextUC != -1 && !UnicodeCompressor.isCompressible(nextUC)) {
                        if (bytePos + 2 >= byteBufferLimit) {
                            --ucPos;
                            break block4;
                        }
                        hiByte = curUC >>> 8;
                        loByte = curUC & 0xFF;
                        if (sUnicodeTagTable[hiByte]) {
                            byteBuffer[bytePos++] = -16;
                        }
                        byteBuffer[bytePos++] = (byte)hiByte;
                        byteBuffer[bytePos++] = (byte)loByte;
                        continue;
                    }
                    if (curUC < 128) {
                        loByte = curUC & 0xFF;
                        if (nextUC != -1 && nextUC < 128 && !sSingleTagTable[loByte]) {
                            if (bytePos + 1 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            whichWindow = this.fCurrentWindow;
                            byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                            byteBuffer[bytePos++] = (byte)loByte;
                            this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                            this.fMode = 0;
                            break;
                        } else {
                            if (bytePos + 1 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            byteBuffer[bytePos++] = 0;
                            byteBuffer[bytePos++] = (byte)loByte;
                            continue;
                        }
                    }
                    whichWindow = this.findDynamicWindow(curUC);
                    if (whichWindow != -1) {
                        if (this.inDynamicWindow(nextUC, whichWindow)) {
                            if (bytePos + 1 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            byteBuffer[bytePos++] = (byte)(224 + whichWindow);
                            byteBuffer[bytePos++] = (byte)(curUC - this.fOffsets[whichWindow] + 128);
                            this.fTimeStamps[whichWindow] = ++this.fTimeStamp;
                            this.fCurrentWindow = whichWindow;
                            this.fMode = 0;
                            break;
                        } else {
                            if (bytePos + 2 >= byteBufferLimit) {
                                --ucPos;
                                break block4;
                            }
                            hiByte = curUC >>> 8;
                            loByte = curUC & 0xFF;
                            if (sUnicodeTagTable[hiByte]) {
                                byteBuffer[bytePos++] = -16;
                            }
                            byteBuffer[bytePos++] = (byte)hiByte;
                            byteBuffer[bytePos++] = (byte)loByte;
                            continue;
                        }
                    }
                    int n3 = curIndex = UnicodeCompressor.makeIndex(curUC);
                    this.fIndexCount[n3] = this.fIndexCount[n3] + 1;
                    forwardUC = ucPos + 1 < charBufferLimit ? charBuffer[ucPos + 1] : -1;
                    if (this.fIndexCount[curIndex] > 1 || curIndex == UnicodeCompressor.makeIndex(nextUC) && curIndex == UnicodeCompressor.makeIndex(forwardUC)) {
                        if (bytePos + 2 >= byteBufferLimit) {
                            --ucPos;
                            break block4;
                        }
                        whichWindow = this.getLRDefinedWindow();
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
                        --ucPos;
                        break block4;
                    }
                    hiByte = curUC >>> 8;
                    loByte = curUC & 0xFF;
                    if (sUnicodeTagTable[hiByte]) {
                        byteBuffer[bytePos++] = -16;
                    }
                    byteBuffer[bytePos++] = (byte)hiByte;
                    byteBuffer[bytePos++] = (byte)loByte;
                }
            }
        }
        if (charsRead != null) {
            charsRead[0] = ucPos - charBufferStart;
        }
        return bytePos - byteBufferStart;
    }

    public void reset() {
        int i2;
        this.fOffsets[0] = 128;
        this.fOffsets[1] = 192;
        this.fOffsets[2] = 1024;
        this.fOffsets[3] = 1536;
        this.fOffsets[4] = 2304;
        this.fOffsets[5] = 12352;
        this.fOffsets[6] = 12448;
        this.fOffsets[7] = 65280;
        for (i2 = 0; i2 < 8; ++i2) {
            this.fTimeStamps[i2] = 0;
        }
        for (i2 = 0; i2 <= 255; ++i2) {
            this.fIndexCount[i2] = 0;
        }
        this.fTimeStamp = 0;
        this.fCurrentWindow = 0;
        this.fMode = 0;
    }

    private static int makeIndex(int c2) {
        if (c2 >= 192 && c2 < 320) {
            return 249;
        }
        if (c2 >= 592 && c2 < 720) {
            return 250;
        }
        if (c2 >= 880 && c2 < 1008) {
            return 251;
        }
        if (c2 >= 1328 && c2 < 1424) {
            return 252;
        }
        if (c2 >= 12352 && c2 < 12448) {
            return 253;
        }
        if (c2 >= 12448 && c2 < 12576) {
            return 254;
        }
        if (c2 >= 65376 && c2 < 65439) {
            return 255;
        }
        if (c2 >= 128 && c2 < 13312) {
            return c2 / 128 & 0xFF;
        }
        if (c2 >= 57344 && c2 <= 65535) {
            return (c2 - 44032) / 128 & 0xFF;
        }
        return 0;
    }

    private boolean inDynamicWindow(int c2, int whichWindow) {
        return c2 >= this.fOffsets[whichWindow] && c2 < this.fOffsets[whichWindow] + 128;
    }

    private static boolean inStaticWindow(int c2, int whichWindow) {
        return c2 >= sOffsets[whichWindow] && c2 < sOffsets[whichWindow] + 128;
    }

    private static boolean isCompressible(int c2) {
        return c2 < 13312 || c2 >= 57344;
    }

    private int findDynamicWindow(int c2) {
        for (int i2 = 7; i2 >= 0; --i2) {
            if (!this.inDynamicWindow(c2, i2)) continue;
            int n2 = i2;
            this.fTimeStamps[n2] = this.fTimeStamps[n2] + 1;
            return i2;
        }
        return -1;
    }

    private static int findStaticWindow(int c2) {
        for (int i2 = 7; i2 >= 0; --i2) {
            if (!UnicodeCompressor.inStaticWindow(c2, i2)) continue;
            return i2;
        }
        return -1;
    }

    private int getLRDefinedWindow() {
        int leastRU = Integer.MAX_VALUE;
        int whichWindow = -1;
        for (int i2 = 7; i2 >= 0; --i2) {
            if (this.fTimeStamps[i2] >= leastRU) continue;
            leastRU = this.fTimeStamps[i2];
            whichWindow = i2;
        }
        return whichWindow;
    }
}

