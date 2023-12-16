// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.texture;

public enum PixelType
{
    BYTE("BYTE", 0, 5120), 
    SHORT("SHORT", 1, 5122), 
    INT("INT", 2, 5124), 
    HALF_FLOAT("HALF_FLOAT", 3, 5131), 
    FLOAT("FLOAT", 4, 5126), 
    UNSIGNED_BYTE("UNSIGNED_BYTE", 5, 5121), 
    UNSIGNED_BYTE_3_3_2("UNSIGNED_BYTE_3_3_2", 6, 32818), 
    UNSIGNED_BYTE_2_3_3_REV("UNSIGNED_BYTE_2_3_3_REV", 7, 33634), 
    UNSIGNED_SHORT("UNSIGNED_SHORT", 8, 5123), 
    UNSIGNED_SHORT_5_6_5("UNSIGNED_SHORT_5_6_5", 9, 33635), 
    UNSIGNED_SHORT_5_6_5_REV("UNSIGNED_SHORT_5_6_5_REV", 10, 33636), 
    UNSIGNED_SHORT_4_4_4_4("UNSIGNED_SHORT_4_4_4_4", 11, 32819), 
    UNSIGNED_SHORT_4_4_4_4_REV("UNSIGNED_SHORT_4_4_4_4_REV", 12, 33637), 
    UNSIGNED_SHORT_5_5_5_1("UNSIGNED_SHORT_5_5_5_1", 13, 32820), 
    UNSIGNED_SHORT_1_5_5_5_REV("UNSIGNED_SHORT_1_5_5_5_REV", 14, 33638), 
    UNSIGNED_INT("UNSIGNED_INT", 15, 5125), 
    UNSIGNED_INT_8_8_8_8("UNSIGNED_INT_8_8_8_8", 16, 32821), 
    UNSIGNED_INT_8_8_8_8_REV("UNSIGNED_INT_8_8_8_8_REV", 17, 33639), 
    UNSIGNED_INT_10_10_10_2("UNSIGNED_INT_10_10_10_2", 18, 32822), 
    UNSIGNED_INT_2_10_10_10_REV("UNSIGNED_INT_2_10_10_10_REV", 19, 33640);
    
    private int id;
    
    private PixelType(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
