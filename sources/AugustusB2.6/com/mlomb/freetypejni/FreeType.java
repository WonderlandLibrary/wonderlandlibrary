// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

import java.nio.ByteBuffer;

public class FreeType
{
    public static native long FT_Init_FreeType();
    
    public static native boolean FT_Done_FreeType(final long p0);
    
    public static native LibraryVersion FT_Library_Version(final long p0);
    
    public static native long FT_New_Memory_Face(final long p0, final ByteBuffer p1, final int p2, final long p3);
    
    public static native int FT_Face_Get_ascender(final long p0);
    
    public static native int FT_Face_Get_descender(final long p0);
    
    public static native long FT_Face_Get_face_flags(final long p0);
    
    public static native int FT_Face_Get_face_index(final long p0);
    
    public static native String FT_Face_Get_family_name(final long p0);
    
    public static native int FT_Face_Get_heigth(final long p0);
    
    public static native int FT_Face_Get_max_advance_height(final long p0);
    
    public static native int FT_Face_Get_max_advance_width(final long p0);
    
    public static native int FT_Face_Get_num_faces(final long p0);
    
    public static native int FT_Face_Get_num_glyphs(final long p0);
    
    public static native long FT_Face_Get_style_flags(final long p0);
    
    public static native String FT_Face_Get_style_name(final long p0);
    
    public static native int FT_Face_Get_underline_position(final long p0);
    
    public static native int FT_Face_Get_underline_thickness(final long p0);
    
    public static native int FT_Face_Get_units_per_EM(final long p0);
    
    public static native long FT_Face_Get_glyph(final long p0);
    
    public static native long FT_Face_Get_size(final long p0);
    
    public static native long FT_Get_Track_Kerning(final long p0, final long p1, final int p2);
    
    public static native Kerning FT_Face_Get_Kerning(final long p0, final char p1, final char p2, final int p3);
    
    public static native boolean FT_Done_Face(final long p0);
    
    public static native boolean FT_Reference_Face(final long p0);
    
    public static native boolean FT_HAS_KERNING(final long p0);
    
    public static native String FT_Get_Postscript_Name(final long p0);
    
    public static native boolean FT_Select_Charmap(final long p0, final int p1);
    
    public static native boolean FT_Set_Charmap(final long p0, final long p1);
    
    public static native boolean FT_Face_CheckTrueTypePatents(final long p0);
    
    public static native boolean FT_Face_SetUnpatentedHinting(final long p0, final boolean p1);
    
    public static native int[] FT_Get_First_Char(final long p0);
    
    public static native int FT_Get_Next_Char(final long p0, final long p1);
    
    public static native int FT_Get_Char_Index(final long p0, final int p1);
    
    public static native int FT_Get_Name_Index(final long p0, final String p1);
    
    public static native String FT_Get_Glyph_Name(final long p0, final int p1);
    
    public static native short FT_Get_FSType_Flags(final long p0);
    
    public static native boolean FT_Select_Size(final long p0, final int p1);
    
    public static native boolean FT_Load_Char(final long p0, final char p1, final int p2);
    
    public static native boolean FT_Request_Size(final long p0, final SizeRequest p1);
    
    public static native boolean FT_Set_Pixel_Sizes(final long p0, final float p1, final float p2);
    
    public static native boolean FT_Load_Glyph(final long p0, final int p1, final int p2);
    
    public static native boolean FT_Set_Char_Size(final long p0, final int p1, final int p2, final int p3, final int p4);
    
    public static native long FT_Size_Get_metrics(final long p0);
    
    public static native int FT_Size_Metrics_Get_ascender(final long p0);
    
    public static native int FT_Size_Metrics_Get_descender(final long p0);
    
    public static native int FT_Size_Metrics_Get_height(final long p0);
    
    public static native int FT_Size_Metrics_Get_max_advance(final long p0);
    
    public static native int FT_Size_Metrics_Get_x_ppem(final long p0);
    
    public static native int FT_Size_Metrics_Get_x_scale(final long p0);
    
    public static native int FT_Size_Metrics_Get_y_ppem(final long p0);
    
    public static native int FT_Size_Metrics_Get_y_scale(final long p0);
    
    public static native long FT_GlyphSlot_Get_linearHoriAdvance(final long p0);
    
    public static native long FT_GlyphSlot_Get_linearVertAdvance(final long p0);
    
    public static native GlyphSlot.Advance FT_GlyphSlot_Get_advance(final long p0);
    
    public static native int FT_GlyphSlot_Get_format(final long p0);
    
    public static native int FT_GlyphSlot_Get_bitmap_left(final long p0);
    
    public static native int FT_GlyphSlot_Get_bitmap_top(final long p0);
    
    public static native long FT_GlyphSlot_Get_bitmap(final long p0);
    
    public static native long FT_GlyphSlot_Get_metrics(final long p0);
    
    public static native boolean FT_Render_Glyph(final long p0, final int p1);
    
    public static native int FT_Glyph_Metrics_Get_width(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_height(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_horiAdvance(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_vertAdvance(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_horiBearingX(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_horiBearingY(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_vertBearingX(final long p0);
    
    public static native int FT_Glyph_Metrics_Get_vertBearingY(final long p0);
    
    public static native int FT_Bitmap_Get_width(final long p0);
    
    public static native int FT_Bitmap_Get_rows(final long p0);
    
    public static native int FT_Bitmap_Get_pitch(final long p0);
    
    public static native short FT_Bitmap_Get_num_grays(final long p0);
    
    public static native char FT_Bitmap_Get_palette_mode(final long p0);
    
    public static native char FT_Bitmap_Get_pixel_mode(final long p0);
    
    public static native ByteBuffer FT_Bitmap_Get_buffer(final long p0);
    
    public static native int FT_Get_Charmap_Index(final long p0);
    
    public static Library newLibrary() {
        final long ft_Init_FreeType = FT_Init_FreeType();
        if (ft_Init_FreeType <= 0L) {
            return null;
        }
        return new Library(ft_Init_FreeType);
    }
    
    static {
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
                throw new Exception("Operating system not supported.");
            }
            int i = 86;
            if (System.getProperty("os.arch").contains("64")) {
                i = 64;
            }
            System.loadLibrary("freetype-jni-" + i);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            System.err.println("Can't find the native file for FreeType-jni.");
            throw unsatisfiedLinkError;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
