/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.common.PushbroomScaler;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SmoothMinifier
implements PushbroomScaler {
    protected int sourceWidth;
    protected int sourceHeight;
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int boxHeight;
    protected byte[][] sourceData;
    protected int[] leftPoints;
    protected int[] rightPoints;
    protected int[] topPoints;
    protected int[] bottomPoints;
    protected int sourceLine;
    protected int sourceDataLine;
    protected int destLine;
    protected int[] tmpBuf;

    SmoothMinifier(int n, int n2, int n3, int n4, int n5) {
        int n6;
        int n7;
        if (n <= 0 || n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0 || n4 > n || n5 > n2) {
            throw new IllegalArgumentException();
        }
        this.sourceWidth = n;
        this.sourceHeight = n2;
        this.numBands = n3;
        this.destWidth = n4;
        this.destHeight = n5;
        this.destBuf = ByteBuffer.wrap(new byte[n5 * n4 * n3]);
        double d = (double)n / (double)n4;
        this.scaleY = (double)n2 / (double)n5;
        int n8 = (n + n4 - 1) / n4;
        this.boxHeight = (n2 + n5 - 1) / n5;
        int n9 = n8 / 2;
        int n10 = n8 - n9 - 1;
        int n11 = this.boxHeight / 2;
        int n12 = this.boxHeight - n11 - 1;
        this.sourceData = new byte[this.boxHeight][n4 * n3];
        this.leftPoints = new int[n4];
        this.rightPoints = new int[n4];
        for (n7 = 0; n7 < n4; ++n7) {
            n6 = (int)((double)n7 * d);
            this.leftPoints[n7] = n6 - n9;
            this.rightPoints[n7] = n6 + n10;
        }
        this.topPoints = new int[n5];
        this.bottomPoints = new int[n5];
        for (n7 = 0; n7 < n5; ++n7) {
            n6 = (int)((double)n7 * this.scaleY);
            this.topPoints[n7] = n6 - n11;
            this.bottomPoints[n7] = n6 + n12;
        }
        this.sourceLine = 0;
        this.sourceDataLine = 0;
        this.destLine = 0;
        this.tmpBuf = new int[n4 * n3];
    }

    @Override
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override
    public boolean putSourceScanline(byte[] arrby, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        if (n < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.numBands == 1) {
            n8 = arrby[n] & 0xFF;
            n7 = arrby[n + this.sourceWidth - 1] & 0xFF;
            for (n6 = 0; n6 < this.destWidth; ++n6) {
                n5 = 0;
                n4 = this.rightPoints[n6];
                for (n3 = this.leftPoints[n6]; n3 <= n4; ++n3) {
                    if (n3 < 0) {
                        n5 += n8;
                        continue;
                    }
                    if (n3 >= this.sourceWidth) {
                        n5 += n7;
                        continue;
                    }
                    n5 += arrby[n + n3] & 0xFF;
                }
                this.sourceData[this.sourceDataLine][n6] = (byte)(n5 /= n4 - this.leftPoints[n6] + 1);
            }
        } else {
            n8 = n + (this.sourceWidth - 1) * this.numBands;
            for (n7 = 0; n7 < this.destWidth; ++n7) {
                n6 = this.leftPoints[n7];
                n5 = this.rightPoints[n7];
                n4 = n5 - n6 + 1;
                n3 = n7 * this.numBands;
                for (n2 = 0; n2 < this.numBands; ++n2) {
                    int n9 = arrby[n + n2] & 0xFF;
                    int n10 = arrby[n8 + n2] & 0xFF;
                    int n11 = 0;
                    for (int i = n6; i <= n5; ++i) {
                        if (i < 0) {
                            n11 += n9;
                            continue;
                        }
                        if (i >= this.sourceWidth) {
                            n11 += n10;
                            continue;
                        }
                        n11 += arrby[n + i * this.numBands + n2] & 0xFF;
                    }
                    this.sourceData[this.sourceDataLine][n3 + n2] = (byte)(n11 /= n4);
                }
            }
        }
        if (this.sourceLine == this.bottomPoints[this.destLine] || this.destLine == this.destHeight - 1 && this.sourceLine == this.sourceHeight - 1) {
            assert (this.destBuf.hasArray()) : "destBuf.hasArray() == false => destBuf is direct";
            byte[] arrby2 = this.destBuf.array();
            n7 = this.destLine * this.destWidth * this.numBands;
            Arrays.fill(this.tmpBuf, 0);
            for (n6 = this.topPoints[this.destLine]; n6 <= this.bottomPoints[this.destLine]; ++n6) {
                n5 = 0;
                n5 = n6 < 0 ? 0 - this.sourceLine + this.sourceDataLine : (n6 >= this.sourceHeight ? (this.sourceHeight - 1 - this.sourceLine + this.sourceDataLine) % this.boxHeight : (n6 - this.sourceLine + this.sourceDataLine) % this.boxHeight);
                if (n5 < 0) {
                    n5 += this.boxHeight;
                }
                byte[] arrby3 = this.sourceData[n5];
                n3 = arrby3.length;
                for (n2 = 0; n2 < n3; ++n2) {
                    int n12 = n2;
                    this.tmpBuf[n12] = this.tmpBuf[n12] + (arrby3[n2] & 0xFF);
                }
            }
            n6 = this.tmpBuf.length;
            for (n5 = 0; n5 < n6; ++n5) {
                arrby2[n7 + n5] = (byte)(this.tmpBuf[n5] / this.boxHeight);
            }
            if (this.destLine < this.destHeight - 1) {
                ++this.destLine;
            }
        }
        if (++this.sourceLine != this.sourceHeight) {
            this.sourceDataLine = (this.sourceDataLine + 1) % this.boxHeight;
        }
        return this.destLine == this.destHeight;
    }
}

