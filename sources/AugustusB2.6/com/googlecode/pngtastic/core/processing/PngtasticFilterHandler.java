// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import com.googlecode.pngtastic.core.PngException;
import java.util.List;
import com.googlecode.pngtastic.core.PngFilterType;
import com.googlecode.pngtastic.core.Logger;

public final class PngtasticFilterHandler implements PngFilterHandler
{
    private final Logger log;
    
    public PngtasticFilterHandler(final Logger log) {
        this.log = log;
    }
    
    @Override
    public final void applyFiltering(final PngFilterType filterType, List<byte[]> var_2_18, final int sampleBitCount) {
        byte[] array = new byte[scanlines.get(0).length];
        var_2_18 = (List<byte[]>)scanlines.iterator();
        while (((Iterator)var_2_18).hasNext()) {
            final byte[] scanline = ((Iterator<byte[]>)var_2_18).next();
            if (filterType != null) {
                scanline[0] = filterType.getValue();
            }
            final byte[] previous = scanline.clone();
            try {
                final byte[] array2 = scanline;
                final byte[] previousLine = array;
                final byte[] array3 = array2;
                final PngFilterType forValue = PngFilterType.forValue(array3[0]);
                array3[0] = 0;
                final PngFilterType forValue2 = PngFilterType.forValue(previousLine[0]);
                previousLine[0] = 0;
                switch (forValue) {
                    case NONE: {
                        break;
                    }
                    case SUB: {
                        final byte[] array4 = array3.clone();
                        final int n = -(Math.max(1, sampleBitCount / 8) - 1);
                        for (int i = 1, n2 = n; i < array3.length; ++i, ++n2) {
                            array3[i] = (byte)(array4[i] - ((n2 < 0) ? 0 : array4[n2]));
                        }
                        break;
                    }
                    case UP: {
                        for (int j = 1; j < array3.length; ++j) {
                            array3[j] -= previousLine[j];
                        }
                        break;
                    }
                    case AVERAGE: {
                        final byte[] array5 = array3.clone();
                        final int n3 = -(Math.max(1, sampleBitCount / 8) - 1);
                        for (int k = 1, n4 = n3; k < array3.length; ++k, ++n4) {
                            array3[k] = (byte)(array5[k] - ((0xFF & array5[(n4 < 0) ? 0 : n4]) + (0xFF & previousLine[k])) / 2);
                        }
                        break;
                    }
                    case PAETH: {
                        final byte[] line = array3.clone();
                        final int n5 = -(Math.max(1, sampleBitCount / 8) - 1);
                        for (int l = 1, xp = n5; l < array3.length; ++l, ++xp) {
                            array3[l] = (byte)(line[l] - paethPredictor(line, previousLine, l, xp));
                        }
                        break;
                    }
                    default: {
                        throw new PngException("Unrecognized filter type " + forValue);
                    }
                }
                array3[0] = forValue.getValue();
                previousLine[0] = forValue2.getValue();
            }
            catch (PngException e) {
                this.log.error("Error during filtering: %s", e.getMessage());
            }
            array = previous;
        }
    }
    
    @Override
    public final void applyAdaptiveFiltering$6fb3b3b3(final List<byte[]> scanlines, final Map<PngFilterType, List<byte[]>> filteredScanLines, final int sampleSize) throws IOException {
        for (int s = 0; s < scanlines.size(); ++s) {
            long bestSum = Long.MAX_VALUE;
            PngFilterType bestFilterType = null;
            for (final Map.Entry<PngFilterType, List<byte[]>> entry : filteredScanLines.entrySet()) {
                long sum = 0L;
                final byte[] scanline = entry.getValue().get(s);
                for (int i = 1; i < scanline.length; ++i) {
                    sum += Math.abs(scanline[i]);
                }
                if (sum < bestSum) {
                    bestFilterType = entry.getKey();
                    bestSum = sum;
                }
            }
            if (bestFilterType != null) {
                scanlines.get(s)[0] = bestFilterType.getValue();
            }
        }
        this.applyFiltering(null, scanlines, sampleSize);
    }
    
    @Override
    public final void deFilter(final byte[] line, final byte[] previousLine, int sampleBitCount) throws PngException {
        final PngFilterType filterType = PngFilterType.forValue(line[0]);
        line[0] = 0;
        final PngFilterType previousFilterType = PngFilterType.forValue(previousLine[0]);
        previousLine[0] = 0;
        switch (filterType) {
            case SUB: {
                int x;
                for (sampleBitCount = -(Math.max(1, sampleBitCount / 8) - 1), x = 1, sampleBitCount = sampleBitCount; x < line.length; ++x, ++sampleBitCount) {
                    line[x] += (byte)((sampleBitCount < 0) ? 0 : line[sampleBitCount]);
                }
                break;
            }
            case UP: {
                for (sampleBitCount = 1; sampleBitCount < line.length; ++sampleBitCount) {
                    line[sampleBitCount] += previousLine[sampleBitCount];
                }
                break;
            }
            case AVERAGE: {
                int x;
                for (sampleBitCount = -(Math.max(1, sampleBitCount / 8) - 1), x = 1, sampleBitCount = sampleBitCount; x < line.length; ++x, ++sampleBitCount) {
                    line[x] += (byte)(((0xFF & ((sampleBitCount < 0) ? 0 : line[sampleBitCount])) + (0xFF & previousLine[x])) / 2);
                }
                break;
            }
            case PAETH: {
                int x;
                int result;
                for (sampleBitCount = -(Math.max(1, sampleBitCount / 8) - 1), x = 1, sampleBitCount = sampleBitCount; x < line.length; ++x, ++sampleBitCount) {
                    result = paethPredictor(line, previousLine, x, sampleBitCount);
                    line[x] += (byte)result;
                }
                break;
            }
        }
        line[0] = filterType.getValue();
        previousLine[0] = previousFilterType.getValue();
    }
    
    private static int paethPredictor(final byte[] line, final byte[] previousLine, int x, int xp) {
        final int a = 0xFF & ((xp < 0) ? 0 : line[xp]);
        x = (0xFF & previousLine[x]);
        final int c = 0xFF & ((xp < 0) ? 0 : previousLine[xp]);
        final int pa = ((xp = a + x - c) >= a) ? (xp - a) : (-(xp - a));
        final int pb = (xp >= x) ? (xp - x) : (-(xp - x));
        xp = ((xp >= c) ? (xp - c) : (-(xp - c)));
        if (pa <= pb && pa <= xp) {
            return a;
        }
        if (pb <= xp) {
            return x;
        }
        return c;
    }
}
