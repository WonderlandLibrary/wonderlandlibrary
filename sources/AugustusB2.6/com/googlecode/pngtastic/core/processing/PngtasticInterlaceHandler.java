// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import com.googlecode.pngtastic.core.PngException;
import java.util.ArrayList;
import java.util.List;
import com.googlecode.pngtastic.core.Logger;

public final class PngtasticInterlaceHandler implements PngInterlaceHandler
{
    private final Logger log;
    private PngFilterHandler pngFilterHandler;
    private static final int[] interlaceColumnFrequency;
    private static final int[] interlaceColumnOffset;
    private static final int[] interlaceRowFrequency;
    private static final int[] interlaceRowOffset;
    
    public PngtasticInterlaceHandler(final Logger log, final PngFilterHandler pngFilterHandler) {
        this.log = log;
        this.pngFilterHandler = pngFilterHandler;
    }
    
    @Override
    public final List<byte[]> deInterlace(final int width, final int height, final int sampleBitCount, final PngByteArrayOutputStream inflatedImageData) {
        this.log.debug("Deinterlacing", new Object[0]);
        final List<byte[]> results = new ArrayList<byte[]>();
        final int sampleSize = Math.max(1, sampleBitCount / 8);
        final byte[][] rows = new byte[height][Math.ceil(width * sampleBitCount / 8.0).intValue() + 1];
        int subImageOffset = 0;
        for (int pass = 0; pass < 7; ++pass) {
            final int subImageRows = (height - PngtasticInterlaceHandler.interlaceRowOffset[pass] + (PngtasticInterlaceHandler.interlaceRowFrequency[pass] - 1)) / PngtasticInterlaceHandler.interlaceRowFrequency[pass];
            final int rowLength = Math.ceil((width - PngtasticInterlaceHandler.interlaceColumnOffset[pass] + (PngtasticInterlaceHandler.interlaceColumnFrequency[pass] - 1)) / PngtasticInterlaceHandler.interlaceColumnFrequency[pass] * sampleBitCount / 8.0).intValue() + 1;
            final int cf = PngtasticInterlaceHandler.interlaceColumnFrequency[pass] * sampleSize;
            final int co = PngtasticInterlaceHandler.interlaceColumnOffset[pass] * sampleSize;
            final int rf = PngtasticInterlaceHandler.interlaceRowFrequency[pass];
            final int ro = PngtasticInterlaceHandler.interlaceRowOffset[pass];
            byte[] array = new byte[rowLength];
            int offset = 0;
            for (int i = 0; i < subImageRows; ++i) {
                offset = subImageOffset + i * rowLength;
                final byte[] row = new byte[rowLength];
                System.arraycopy(inflatedImageData.get(), offset, row, 0, rowLength);
                try {
                    this.pngFilterHandler.deFilter(row, array, sampleBitCount);
                }
                catch (PngException e) {
                    this.log.error("Error: %s", e.getMessage());
                }
                for (int samples = (row.length - 1) / sampleSize, sample = 0; sample < samples; ++sample) {
                    for (int b = 0; b < sampleSize; ++b) {
                        rows[i * rf + ro][sample * cf + co + b + 1] = row[sample * sampleSize + b + 1];
                    }
                }
                array = row.clone();
            }
            subImageOffset = offset + rowLength;
        }
        for (int j = 0; j < rows.length; ++j) {
            results.add(rows[j]);
        }
        return results;
    }
    
    static {
        interlaceColumnFrequency = new int[] { 8, 8, 4, 4, 2, 2, 1 };
        interlaceColumnOffset = new int[] { 0, 4, 0, 2, 0, 1, 0 };
        interlaceRowFrequency = new int[] { 8, 8, 8, 4, 4, 2, 2 };
        interlaceRowOffset = new int[] { 0, 0, 4, 0, 2, 0, 1 };
    }
}
