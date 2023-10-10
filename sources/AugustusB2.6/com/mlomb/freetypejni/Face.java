// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

import java.nio.ByteBuffer;

public class Face extends Utils.Pointer
{
    private ByteBuffer data;
    
    public Face(final long n) {
        super(n);
    }
    
    public Face(final long n, final ByteBuffer data) {
        super(n);
        this.data = data;
    }
    
    public boolean delete() {
        if (this.data != null) {
            Utils.deleteBuffer(this.data);
        }
        return FreeType.FT_Done_Face(this.pointer);
    }
    
    public int getAscender() {
        return FreeType.FT_Face_Get_ascender(this.pointer);
    }
    
    public int getDescender() {
        return FreeType.FT_Face_Get_descender(this.pointer);
    }
    
    public long getFaceFlags() {
        return FreeType.FT_Face_Get_face_flags(this.pointer);
    }
    
    public int getFaceIndex() {
        return FreeType.FT_Face_Get_face_index(this.pointer);
    }
    
    public String getFamilyName() {
        return FreeType.FT_Face_Get_family_name(this.pointer);
    }
    
    public int getHeight() {
        return FreeType.FT_Face_Get_heigth(this.pointer);
    }
    
    public int getMaxAdvanceHeight() {
        return FreeType.FT_Face_Get_max_advance_height(this.pointer);
    }
    
    public int getMaxAdvanceWidth() {
        return FreeType.FT_Face_Get_max_advance_width(this.pointer);
    }
    
    public int getNumFaces() {
        return FreeType.FT_Face_Get_num_faces(this.pointer);
    }
    
    public int getNumGlyphs() {
        return FreeType.FT_Face_Get_num_glyphs(this.pointer);
    }
    
    public long getStyleFlags() {
        return FreeType.FT_Face_Get_style_flags(this.pointer);
    }
    
    public String getStyleName() {
        return FreeType.FT_Face_Get_style_name(this.pointer);
    }
    
    public int getUnderlinePosition() {
        return FreeType.FT_Face_Get_underline_position(this.pointer);
    }
    
    public int getUnderlineThickness() {
        return FreeType.FT_Face_Get_underline_thickness(this.pointer);
    }
    
    public int getUnitsPerEM() {
        return FreeType.FT_Face_Get_units_per_EM(this.pointer);
    }
    
    public int getCharIndex(final int n) {
        return FreeType.FT_Get_Char_Index(this.pointer, n);
    }
    
    public boolean hasKerning() {
        return FreeType.FT_HAS_KERNING(this.pointer);
    }
    
    public boolean selectSize(final int n) {
        return FreeType.FT_Select_Size(this.pointer, n);
    }
    
    public boolean setCharSize(final int n, final int n2, final int n3, final int n4) {
        return FreeType.FT_Set_Char_Size(this.pointer, n, n2, n3, n4);
    }
    
    public boolean loadGlyph(final int n, final int n2) {
        return FreeType.FT_Load_Glyph(this.pointer, n, n2);
    }
    
    public boolean loadChar(final char c, final int n) {
        return FreeType.FT_Load_Char(this.pointer, c, n);
    }
    
    public Kerning getKerning(final char c, final char c2) {
        return this.getKerning(c, c2, FreeTypeConstants.FT_Kerning_Mode.FT_KERNING_DEFAULT);
    }
    
    public Kerning getKerning(final char c, final char c2, final FreeTypeConstants.FT_Kerning_Mode ft_Kerning_Mode) {
        return FreeType.FT_Face_Get_Kerning(this.pointer, c, c2, ft_Kerning_Mode.ordinal());
    }
    
    public boolean setPixelSizes(final float n, final float n2) {
        return FreeType.FT_Set_Pixel_Sizes(this.pointer, n, n2);
    }
    
    public GlyphSlot getGlyphSlot() {
        final long ft_Face_Get_glyph = FreeType.FT_Face_Get_glyph(this.pointer);
        if (ft_Face_Get_glyph <= 0L) {
            return null;
        }
        return new GlyphSlot(ft_Face_Get_glyph);
    }
    
    public Size getSize() {
        final long ft_Face_Get_size = FreeType.FT_Face_Get_size(this.pointer);
        if (ft_Face_Get_size <= 0L) {
            return null;
        }
        return new Size(ft_Face_Get_size);
    }
    
    public boolean checkTrueTypePatents() {
        return FreeType.FT_Face_CheckTrueTypePatents(this.pointer);
    }
    
    public boolean setUnpatentedHinting(final boolean b) {
        return FreeType.FT_Face_SetUnpatentedHinting(this.pointer, b);
    }
    
    public boolean referenceFace() {
        return FreeType.FT_Reference_Face(this.pointer);
    }
    
    public boolean requestSize(final SizeRequest sizeRequest) {
        return FreeType.FT_Request_Size(this.pointer, sizeRequest);
    }
    
    public int[] getFirstChar() {
        return FreeType.FT_Get_First_Char(this.pointer);
    }
    
    public int getFirstCharAsCharcode() {
        return this.getFirstChar()[0];
    }
    
    public int getFirstCharAsGlyphIndex() {
        return this.getFirstChar()[1];
    }
    
    public int getNextChar(final long n) {
        return FreeType.FT_Get_Next_Char(this.pointer, n);
    }
    
    public int getGlyphIndexByName(final String s) {
        return FreeType.FT_Get_Name_Index(this.pointer, s);
    }
    
    public long getTrackKerning(final int n, final int n2) {
        return FreeType.FT_Get_Track_Kerning(this.pointer, n, n2);
    }
    
    public String getGlyphName(final int n) {
        return FreeType.FT_Get_Glyph_Name(this.pointer, n);
    }
    
    public String getPostscriptName() {
        return FreeType.FT_Get_Postscript_Name(this.pointer);
    }
    
    public boolean selectCharmap(final int n) {
        return FreeType.FT_Select_Charmap(this.pointer, n);
    }
    
    public boolean setCharmap(final CharMap charMap) {
        return FreeType.FT_Set_Charmap(this.pointer, charMap.getPointer());
    }
    
    public short getFSTypeFlags() {
        return FreeType.FT_Get_FSType_Flags(this.pointer);
    }
}
