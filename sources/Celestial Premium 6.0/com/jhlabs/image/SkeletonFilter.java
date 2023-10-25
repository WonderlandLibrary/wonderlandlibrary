/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.BinaryFilter;
import java.awt.Rectangle;

public class SkeletonFilter
extends BinaryFilter {
    private static final byte[] skeletonTable;

    static {
        byte[] arrby = new byte[256];
        arrby[3] = 1;
        arrby[6] = 1;
        arrby[7] = 3;
        arrby[10] = 3;
        arrby[11] = 1;
        arrby[12] = 1;
        arrby[14] = 1;
        arrby[15] = 3;
        arrby[24] = 2;
        arrby[26] = 2;
        arrby[28] = 3;
        arrby[30] = 3;
        arrby[31] = 3;
        arrby[40] = 3;
        arrby[48] = 2;
        arrby[56] = 2;
        arrby[60] = 3;
        arrby[62] = 2;
        arrby[63] = 2;
        arrby[96] = 2;
        arrby[104] = 2;
        arrby[108] = 2;
        arrby[112] = 3;
        arrby[120] = 3;
        arrby[124] = 3;
        arrby[126] = 2;
        arrby[129] = 1;
        arrby[130] = 3;
        arrby[131] = 1;
        arrby[134] = 1;
        arrby[135] = 3;
        arrby[143] = 1;
        arrby[159] = 1;
        arrby[160] = 3;
        arrby[161] = 1;
        arrby[176] = 2;
        arrby[192] = 2;
        arrby[193] = 3;
        arrby[194] = 1;
        arrby[195] = 3;
        arrby[198] = 1;
        arrby[199] = 3;
        arrby[207] = 1;
        arrby[224] = 2;
        arrby[225] = 3;
        arrby[227] = 1;
        arrby[231] = 1;
        arrby[240] = 3;
        arrby[241] = 3;
        arrby[243] = 1;
        arrby[248] = 2;
        arrby[249] = 2;
        arrby[252] = 2;
        skeletonTable = arrby;
    }

    public SkeletonFilter() {
        this.newColor = -1;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] outPixels = new int[width * height];
        int count = 0;
        int black = -16777216;
        int white = -1;
        for (int i = 0; i < this.iterations; ++i) {
            count = 0;
            for (int pass = 0; pass < 2; ++pass) {
                for (int y = 1; y < height - 1; ++y) {
                    int offset = y * width + 1;
                    for (int x = 1; x < width - 1; ++x) {
                        int pixel = inPixels[offset];
                        if (pixel == black) {
                            int tableIndex = 0;
                            if (inPixels[offset - width - 1] == black) {
                                tableIndex |= 1;
                            }
                            if (inPixels[offset - width] == black) {
                                tableIndex |= 2;
                            }
                            if (inPixels[offset - width + 1] == black) {
                                tableIndex |= 4;
                            }
                            if (inPixels[offset + 1] == black) {
                                tableIndex |= 8;
                            }
                            if (inPixels[offset + width + 1] == black) {
                                tableIndex |= 0x10;
                            }
                            if (inPixels[offset + width] == black) {
                                tableIndex |= 0x20;
                            }
                            if (inPixels[offset + width - 1] == black) {
                                tableIndex |= 0x40;
                            }
                            if (inPixels[offset - 1] == black) {
                                tableIndex |= 0x80;
                            }
                            byte code = skeletonTable[tableIndex];
                            if (pass == 1) {
                                if (code == 2 || code == 3) {
                                    pixel = this.colormap != null ? this.colormap.getColor((float)i / (float)this.iterations) : this.newColor;
                                    ++count;
                                }
                            } else if (code == 1 || code == 3) {
                                pixel = this.colormap != null ? this.colormap.getColor((float)i / (float)this.iterations) : this.newColor;
                                ++count;
                            }
                        }
                        outPixels[offset++] = pixel;
                    }
                }
                if (pass != 0) continue;
                inPixels = outPixels;
                outPixels = new int[width * height];
            }
            if (count == 0) break;
        }
        return outPixels;
    }

    public String toString() {
        return "Binary/Skeletonize...";
    }
}

