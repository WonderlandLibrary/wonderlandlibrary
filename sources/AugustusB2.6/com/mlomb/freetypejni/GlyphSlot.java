// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class GlyphSlot extends Utils.Pointer
{
    public GlyphSlot(final long n) {
        super(n);
    }
    
    public Bitmap getBitmap() {
        final long ft_GlyphSlot_Get_bitmap = FreeType.FT_GlyphSlot_Get_bitmap(this.pointer);
        if (ft_GlyphSlot_Get_bitmap <= 0L) {
            return null;
        }
        return new Bitmap(ft_GlyphSlot_Get_bitmap);
    }
    
    public long getLinearHoriAdvance() {
        return FreeType.FT_GlyphSlot_Get_linearHoriAdvance(this.pointer);
    }
    
    public long getLinearVertAdvance() {
        return FreeType.FT_GlyphSlot_Get_linearVertAdvance(this.pointer);
    }
    
    public Advance getAdvance() {
        return FreeType.FT_GlyphSlot_Get_advance(this.pointer);
    }
    
    public int getFormat() {
        return FreeType.FT_GlyphSlot_Get_format(this.pointer);
    }
    
    public int getBitmapLeft() {
        return FreeType.FT_GlyphSlot_Get_bitmap_left(this.pointer);
    }
    
    public int getBitmapTop() {
        return FreeType.FT_GlyphSlot_Get_bitmap_top(this.pointer);
    }
    
    public GlyphMetrics getMetrics() {
        final long ft_GlyphSlot_Get_metrics = FreeType.FT_GlyphSlot_Get_metrics(this.pointer);
        if (ft_GlyphSlot_Get_metrics <= 0L) {
            return null;
        }
        return new GlyphMetrics(ft_GlyphSlot_Get_metrics);
    }
    
    public boolean renderGlyph(final FreeTypeConstants.FT_Render_Mode ft_Render_Mode) {
        return FreeType.FT_Render_Glyph(this.pointer, ft_Render_Mode.ordinal());
    }
    
    public static class Advance
    {
        private final int x;
        private final int y;
        
        public Advance(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }
    }
}
