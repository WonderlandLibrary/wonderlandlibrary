// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.material;

public class MaterialLogic extends Material
{
    private static final String __OBFID = "CL_00000539";
    
    public MaterialLogic(final MapColor p_i2112_1_) {
        super(p_i2112_1_);
        this.setAdventureModeExempt();
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
