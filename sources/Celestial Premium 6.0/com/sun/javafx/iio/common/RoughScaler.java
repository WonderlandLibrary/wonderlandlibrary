/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.common.PushbroomScaler;
import java.nio.ByteBuffer;

public class RoughScaler
implements PushbroomScaler {
    protected int numBands;
    protected int destWidth;
    protected int destHeight;
    protected double scaleY;
    protected ByteBuffer destBuf;
    protected int[] colPositions;
    protected int sourceLine;
    protected int nextSourceLine;
    protected int destLine;

    public RoughScaler(int n, int n2, int n3, int n4, int n5) {
        if (n <= 0 || n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0) {
            throw new IllegalArgumentException();
        }
        this.numBands = n3;
        this.destWidth = n4;
        this.destHeight = n5;
        this.destBuf = ByteBuffer.wrap(new byte[n5 * n4 * n3]);
        double d = (double)n / (double)n4;
        this.scaleY = (double)n2 / (double)n5;
        this.colPositions = new int[n4];
        for (int i = 0; i < n4; ++i) {
            int n6 = (int)(((double)i + 0.5) * d);
            this.colPositions[i] = n6 * n3;
        }
        this.sourceLine = 0;
        this.destLine = 0;
        this.nextSourceLine = (int)(0.5 * this.scaleY);
    }

    @Override
    public ByteBuffer getDestination() {
        return this.destBuf;
    }

    @Override
    public boolean putSourceScanline(byte[] arrby, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("off < 0!");
        }
        if (this.destLine < this.destHeight) {
            if (this.sourceLine == this.nextSourceLine) {
                int n2;
                assert (this.destBuf.hasArray()) : "destBuf.hasArray() == false => destBuf is direct";
                byte[] arrby2 = this.destBuf.array();
                int n3 = n2 = this.destLine * this.destWidth * this.numBands;
                for (int i = 0; i < this.destWidth; ++i) {
                    int n4 = n + this.colPositions[i];
                    for (int j = 0; j < this.numBands; ++j) {
                        arrby2[n3++] = arrby[n4 + j];
                    }
                }
                while ((int)(((double)(++this.destLine) + 0.5) * this.scaleY) == this.sourceLine) {
                    System.arraycopy(arrby2, n2, arrby2, n3, this.destWidth * this.numBands);
                    n3 += this.destWidth * this.numBands;
                }
                this.nextSourceLine = (int)(((double)this.destLine + 0.5) * this.scaleY);
            }
            ++this.sourceLine;
        }
        return this.destLine == this.destHeight;
    }
}

