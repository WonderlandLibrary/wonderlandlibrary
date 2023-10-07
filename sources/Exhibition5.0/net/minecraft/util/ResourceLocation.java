// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;
    private static final String __OBFID = "CL_00001082";
    
    protected ResourceLocation(final int p_i45928_1_, final String... p_i45928_2_) {
        this.resourceDomain = (StringUtils.isEmpty((CharSequence)p_i45928_2_[0]) ? "minecraft" : p_i45928_2_[0].toLowerCase());
        Validate.notNull((Object)(this.resourcePath = p_i45928_2_[1]));
    }
    
    public ResourceLocation(final String p_i1293_1_) {
        this(0, func_177516_a(p_i1293_1_));
    }
    
    public ResourceLocation(final String p_i1292_1_, final String p_i1292_2_) {
        this(0, new String[] { p_i1292_1_, p_i1292_2_ });
    }
    
    protected static String[] func_177516_a(final String p_177516_0_) {
        final String[] var1 = { null, p_177516_0_ };
        final int var2 = p_177516_0_.indexOf(58);
        if (var2 >= 0) {
            var1[1] = p_177516_0_.substring(var2 + 1, p_177516_0_.length());
            if (var2 > 1) {
                var1[0] = p_177516_0_.substring(0, var2);
            }
        }
        return var1;
    }
    
    public String getResourcePath() {
        return this.resourcePath;
    }
    
    public String getResourceDomain() {
        return this.resourceDomain;
    }
    
    @Override
    public String toString() {
        return this.resourceDomain + ':' + this.resourcePath;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ResourceLocation)) {
            return false;
        }
        final ResourceLocation var2 = (ResourceLocation)p_equals_1_;
        return this.resourceDomain.equals(var2.resourceDomain) && this.resourcePath.equals(var2.resourcePath);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
