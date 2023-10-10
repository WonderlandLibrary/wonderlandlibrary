// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class GlyphMetrics extends Utils.Pointer
{
    public GlyphMetrics(final long n) {
        super(n);
    }
    
    public int getWidth() {
        return FreeType.FT_Glyph_Metrics_Get_width(this.pointer);
    }
    
    public int getHeight() {
        return FreeType.FT_Glyph_Metrics_Get_height(this.pointer);
    }
    
    public int getHoriAdvance() {
        return FreeType.FT_Glyph_Metrics_Get_horiAdvance(this.pointer);
    }
    
    public int getVertAdvance() {
        return FreeType.FT_Glyph_Metrics_Get_vertAdvance(this.pointer);
    }
    
    public int getHoriBearingX() {
        return FreeType.FT_Glyph_Metrics_Get_horiBearingX(this.pointer);
    }
    
    public int getHoriBearingY() {
        return FreeType.FT_Glyph_Metrics_Get_horiBearingY(this.pointer);
    }
    
    public int getVertBearingX() {
        return FreeType.FT_Glyph_Metrics_Get_vertBearingX(this.pointer);
    }
    
    public int getVertBearingY() {
        return FreeType.FT_Glyph_Metrics_Get_vertBearingY(this.pointer);
    }
}
