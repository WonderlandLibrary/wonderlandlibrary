// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;

public class MapGenMineshaft extends MapGenStructure
{
    private double field_82673_e;
    private static final String __OBFID = "CL_00000443";
    
    public MapGenMineshaft() {
        this.field_82673_e = 0.004;
    }
    
    @Override
    public String getStructureName() {
        return "Mineshaft";
    }
    
    public MapGenMineshaft(final Map p_i2034_1_) {
        this.field_82673_e = 0.004;
        for (final Map.Entry var3 : p_i2034_1_.entrySet()) {
            if (var3.getKey().equals("chance")) {
                this.field_82673_e = MathHelper.parseDoubleWithDefault(var3.getValue(), this.field_82673_e);
            }
        }
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int p_75047_1_, final int p_75047_2_) {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(p_75047_1_), Math.abs(p_75047_2_));
    }
    
    @Override
    protected StructureStart getStructureStart(final int p_75049_1_, final int p_75049_2_) {
        return new StructureMineshaftStart(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }
}
