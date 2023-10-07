// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

public class Language implements Comparable
{
    private final String languageCode;
    private final String region;
    private final String name;
    private final boolean bidirectional;
    private static final String __OBFID = "CL_00001095";
    
    public Language(final String p_i1303_1_, final String p_i1303_2_, final String p_i1303_3_, final boolean p_i1303_4_) {
        this.languageCode = p_i1303_1_;
        this.region = p_i1303_2_;
        this.name = p_i1303_3_;
        this.bidirectional = p_i1303_4_;
    }
    
    public String getLanguageCode() {
        return this.languageCode;
    }
    
    public boolean isBidirectional() {
        return this.bidirectional;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.region);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return this == p_equals_1_ || (p_equals_1_ instanceof Language && this.languageCode.equals(((Language)p_equals_1_).languageCode));
    }
    
    @Override
    public int hashCode() {
        return this.languageCode.hashCode();
    }
    
    public int compareTo(final Language p_compareTo_1_) {
        return this.languageCode.compareTo(p_compareTo_1_.languageCode);
    }
    
    @Override
    public int compareTo(final Object p_compareTo_1_) {
        return this.compareTo((Language)p_compareTo_1_);
    }
}
