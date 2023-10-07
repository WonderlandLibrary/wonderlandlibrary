// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.material;

public class MaterialPortal extends Material
{
    private static final String __OBFID = "CL_00000545";
    
    public MaterialPortal(final MapColor p_i2118_1_) {
        super(p_i2118_1_);
    }
    
    @Override
    public boolean isSolid() {
        return false;
    }
    
    @Override
    public boolean blocksLight() {
        return false;
    }
    
    @Override
    public boolean blocksMovement() {
        return false;
    }
}
