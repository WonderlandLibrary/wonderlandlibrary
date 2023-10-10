// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.tools.texturepacker;

import java.util.NoSuchElementException;
import java.awt.image.BufferedImage;

public final class ColorBleedEffect
{
    private static final int[] offsets;
    
    public final BufferedImage processImage(final BufferedImage image, final int maxIterations) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        BufferedImage processedImage;
        if (image.getType() == 2) {
            processedImage = image;
        }
        else {
            processedImage = new BufferedImage(width, height, 2);
        }
        final int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        final Mask mask = new Mask(rgb);
        for (int iterations = 0, lastPending = -1; mask.pendingSize > 0 && mask.pendingSize != lastPending && iterations < maxIterations; ++iterations) {
            lastPending = mask.pendingSize;
            executeIteration(rgb, mask, width, height);
        }
        processedImage.setRGB(0, 0, width, height, rgb, 0, width);
        return processedImage;
    }
    
    private static void executeIteration(final int[] rgb, final Mask mask, final int width, final int height) {
        final Mask.MaskIterator iterator = mask.new MaskIterator();
        while (iterator.hasNext()) {
            final int pixelIndex;
            final int x = (pixelIndex = iterator.next()) % width;
            final int y = pixelIndex / width;
            int r = 0;
            int g = 0;
            int b = 0;
            int count = 0;
            for (int i = 0; i < 16; i += 2) {
                final int column = x + ColorBleedEffect.offsets[i];
                final int row = y + ColorBleedEffect.offsets[i + 1];
                if (column >= 0 && column < width && row >= 0 && row < height) {
                    final int currentPixelIndex = row * width + column;
                    if (!mask.blank[currentPixelIndex]) {
                        final int argb = rgb[currentPixelIndex];
                        r += (argb >> 16 & 0xFF);
                        g += (argb >> 8 & 0xFF);
                        b += (argb & 0xFF);
                        ++count;
                    }
                }
            }
            if (count != 0) {
                final int n = pixelIndex;
                final int n2 = r / count;
                final int n3 = g / count;
                final int j = b / count;
                final int k = n3;
                final int l = n2;
                if (l < 0 || l > 255 || k < 0 || k > 255 || j < 0 || j > 255) {
                    throw new IllegalArgumentException("Invalid RGBA: " + l + ", " + k + "," + j + ",0");
                }
                rgb[n] = (0x0 | (l & 0xFF) << 16 | (k & 0xFF) << 8 | (j & 0xFF));
                iterator.markAsInProgress();
            }
        }
        iterator.reset();
    }
    
    static {
        offsets = new int[] { -1, -1, 0, -1, 1, -1, -1, 0, 1, 0, -1, 1, 0, 1, 1, 1 };
    }
    
    static final class Mask
    {
        final boolean[] blank;
        final int[] pending;
        final int[] changing;
        int pendingSize;
        int changingSize;
        
        Mask(final int[] rgb) {
            final int n = rgb.length;
            this.blank = new boolean[n];
            this.pending = new int[n];
            this.changing = new int[n];
            for (int i = 0; i < n; ++i) {
                if (rgb[i] >>> 24 == 0) {
                    this.blank[i] = true;
                    this.pending[this.pendingSize] = i;
                    ++this.pendingSize;
                }
            }
        }
        
        final class MaskIterator
        {
            private int index;
            
            final boolean hasNext() {
                return this.index < Mask.this.pendingSize;
            }
            
            final int next() {
                if (this.index >= Mask.this.pendingSize) {
                    throw new NoSuchElementException(String.valueOf(this.index));
                }
                return Mask.this.pending[this.index++];
            }
            
            final void markAsInProgress() {
                --this.index;
                final int[] changing = Mask.this.changing;
                final int changingSize = Mask.this.changingSize;
                final Mask this$0 = Mask.this;
                final int index = this.index;
                final Mask mask = this$0;
                if (index >= mask.pendingSize) {
                    throw new IndexOutOfBoundsException(String.valueOf(index));
                }
                final int n = mask.pending[index];
                final Mask mask2 = mask;
                --mask2.pendingSize;
                final int n2 = index;
                final int[] pending = mask.pending;
                pending[n2] = pending[mask.pendingSize];
                changing[changingSize] = n;
                final Mask this$2 = Mask.this;
                ++this$2.changingSize;
            }
            
            final void reset() {
                this.index = 0;
                for (int i = 0, n = Mask.this.changingSize; i < n; ++i) {
                    Mask.this.blank[Mask.this.changing[i]] = false;
                }
                Mask.this.changingSize = 0;
            }
        }
    }
}
