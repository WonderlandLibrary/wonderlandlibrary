// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class FreeTypeConstants
{
    public static final int FT_LOAD_DEFAULT = 0;
    public static final int FT_LOAD_NO_SCALE = 1;
    public static final int FT_LOAD_NO_HINTING = 2;
    public static final int FT_LOAD_RENDER = 4;
    public static final int FT_LOAD_NO_BITMAP = 8;
    public static final int FT_LOAD_VERTICAL_LAYOUT = 16;
    public static final int FT_LOAD_FORCE_AUTOHINT = 32;
    public static final int FT_LOAD_CROP_BITMAP = 64;
    public static final int FT_LOAD_PEDANTIC = 128;
    public static final int FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH = 512;
    public static final int FT_LOAD_NO_RECURSE = 1024;
    public static final int FT_LOAD_IGNORE_TRANSFORM = 2048;
    public static final int FT_LOAD_MONOCHROME = 4096;
    public static final int FT_LOAD_LINEAR_DESIGN = 8192;
    public static final int FT_LOAD_NO_AUTOHINT = 32768;
    public static final int FT_LOAD_COLOR = 1048576;
    public static final int FT_LOAD_COMPUTE_METRICS = 2097152;
    public static final int FT_FSTYPE_INSTALLABLE_EMBEDDING = 0;
    public static final int FT_FSTYPE_RESTRICTED_LICENSE_EMBEDDING = 2;
    public static final int FT_FSTYPE_PREVIEW_AND_PRINT_EMBEDDING = 4;
    public static final int FT_FSTYPE_EDITABLE_EMBEDDING = 8;
    public static final int FT_FSTYPE_NO_SUBSETTING = 256;
    public static final int FT_FSTYPE_BITMAP_EMBEDDING_ONLY = 512;
    public static final int FT_ENCODING_NONE = 0;
    public static final int FT_ENCODING_MS_SYMBOL = 1937337698;
    public static final int FT_ENCODING_UNICODE = 1970170211;
    public static final int FT_ENCODING_SJIS = 1936353651;
    public static final int FT_ENCODING_GB2312 = 1734484000;
    public static final int FT_ENCODING_BIG5 = 1651074869;
    public static final int FT_ENCODING_WANSUNG = 2002873971;
    public static final int FT_ENCODING_JOHAB = 1785686113;
    public static final int FT_ENCODING_ADOBE_STANDARD = 1094995778;
    public static final int FT_ENCODING_ADOBE_EXPERT = 1094992453;
    public static final int FT_ENCODING_ADOBE_CUSTOM = 1094992451;
    public static final int FT_ENCODING_ADOBE_LATIN_1 = 1818326065;
    public static final int FT_ENCODING_OLD_LATIN_2 = 1818326066;
    public static final int FT_ENCODING_APPLE_ROMAN = 1634889070;
    
    public enum FT_Render_Mode
    {
        FT_RENDER_MODE_NORMAL, 
        FT_RENDER_MODE_LIGHT, 
        FT_RENDER_MODE_MONO, 
        FT_RENDER_MODE_LCD, 
        FT_RENDER_MODE_LCD_V, 
        FT_RENDER_MODE_MAX;
    }
    
    public enum FT_Size_Request_Type
    {
        FT_SIZE_REQUEST_TYPE_NOMINAL, 
        FT_SIZE_REQUEST_TYPE_REAL_DIM, 
        FT_SIZE_REQUEST_TYPE_BBOX, 
        FT_SIZE_REQUEST_TYPE_CELL, 
        FT_SIZE_REQUEST_TYPE_SCALES, 
        FT_SIZE_REQUEST_TYPE_MAX;
    }
    
    public enum FT_Kerning_Mode
    {
        FT_KERNING_DEFAULT, 
        FT_KERNING_UNFITTED, 
        FT_KERNING_UNSCALED;
    }
}
