// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.texture;

public enum InternalFormat
{
    R8("R8", 0, 33321), 
    RG8("RG8", 1, 33323), 
    RGB8("RGB8", 2, 32849), 
    RGBA8("RGBA8", 3, 32856), 
    R8_SNORM("R8_SNORM", 4, 36756), 
    RG8_SNORM("RG8_SNORM", 5, 36757), 
    RGB8_SNORM("RGB8_SNORM", 6, 36758), 
    RGBA8_SNORM("RGBA8_SNORM", 7, 36759), 
    R16("R16", 8, 33322), 
    RG16("RG16", 9, 33324), 
    RGB16("RGB16", 10, 32852), 
    RGBA16("RGBA16", 11, 32859), 
    R16_SNORM("R16_SNORM", 12, 36760), 
    RG16_SNORM("RG16_SNORM", 13, 36761), 
    RGB16_SNORM("RGB16_SNORM", 14, 36762), 
    RGBA16_SNORM("RGBA16_SNORM", 15, 36763), 
    R16F("R16F", 16, 33325), 
    RG16F("RG16F", 17, 33327), 
    RGB16F("RGB16F", 18, 34843), 
    RGBA16F("RGBA16F", 19, 34842), 
    R32F("R32F", 20, 33326), 
    RG32F("RG32F", 21, 33328), 
    RGB32F("RGB32F", 22, 34837), 
    RGBA32F("RGBA32F", 23, 34836), 
    R32I("R32I", 24, 33333), 
    RG32I("RG32I", 25, 33339), 
    RGB32I("RGB32I", 26, 36227), 
    RGBA32I("RGBA32I", 27, 36226), 
    R32UI("R32UI", 28, 33334), 
    RG32UI("RG32UI", 29, 33340), 
    RGB32UI("RGB32UI", 30, 36209), 
    RGBA32UI("RGBA32UI", 31, 36208), 
    R3_G3_B2("R3_G3_B2", 32, 10768), 
    RGB5_A1("RGB5_A1", 33, 32855), 
    RGB10_A2("RGB10_A2", 34, 32857), 
    R11F_G11F_B10F("R11F_G11F_B10F", 35, 35898), 
    RGB9_E5("RGB9_E5", 36, 35901);
    
    private int id;
    
    private InternalFormat(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
