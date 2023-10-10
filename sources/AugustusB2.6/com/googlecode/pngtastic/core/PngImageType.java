// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core;

public enum PngImageType
{
    GREYSCALE(0), 
    TRUECOLOR(2), 
    INDEXED_COLOR(3), 
    GREYSCALE_ALPHA(4), 
    TRUECOLOR_ALPHA(6);
    
    private int colorType;
    
    private PngImageType(final int colorType) {
        this.colorType = colorType;
    }
    
    public static PngImageType forColorType(final int colorType) {
        switch (colorType) {
            case 0: {
                return PngImageType.GREYSCALE;
            }
            case 2: {
                return PngImageType.TRUECOLOR;
            }
            case 3: {
                return PngImageType.INDEXED_COLOR;
            }
            case 4: {
                return PngImageType.GREYSCALE_ALPHA;
            }
            case 6: {
                return PngImageType.TRUECOLOR_ALPHA;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public final int channelCount() {
        switch (this.colorType) {
            case 0:
            case 3: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 2: {
                return 3;
            }
            case 6: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
