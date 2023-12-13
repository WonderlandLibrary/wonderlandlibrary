// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

public abstract class BaseAttribute implements IAttribute
{
    private final IAttribute field_180373_a;
    private final String unlocalizedName;
    private final double defaultValue;
    private boolean shouldWatch;
    private static final String __OBFID = "CL_00001565";
    
    protected BaseAttribute(final IAttribute p_i45892_1_, final String p_i45892_2_, final double p_i45892_3_) {
        this.field_180373_a = p_i45892_1_;
        this.unlocalizedName = p_i45892_2_;
        this.defaultValue = p_i45892_3_;
        if (p_i45892_2_ == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }
    
    @Override
    public String getAttributeUnlocalizedName() {
        return this.unlocalizedName;
    }
    
    @Override
    public double getDefaultValue() {
        return this.defaultValue;
    }
    
    @Override
    public boolean getShouldWatch() {
        return this.shouldWatch;
    }
    
    public BaseAttribute setShouldWatch(final boolean p_111112_1_) {
        this.shouldWatch = p_111112_1_;
        return this;
    }
    
    @Override
    public IAttribute func_180372_d() {
        return this.field_180373_a;
    }
    
    @Override
    public int hashCode() {
        return this.unlocalizedName.hashCode();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof IAttribute && this.unlocalizedName.equals(((IAttribute)p_equals_1_).getAttributeUnlocalizedName());
    }
}
