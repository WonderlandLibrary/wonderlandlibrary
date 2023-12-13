// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class RegistryDefaulted extends RegistrySimple
{
    private final Object defaultObject;
    private static final String __OBFID = "CL_00001198";
    
    public RegistryDefaulted(final Object p_i1366_1_) {
        this.defaultObject = p_i1366_1_;
    }
    
    @Override
    public Object getObject(final Object p_82594_1_) {
        final Object var2 = super.getObject(p_82594_1_);
        return (var2 == null) ? this.defaultObject : var2;
    }
}
