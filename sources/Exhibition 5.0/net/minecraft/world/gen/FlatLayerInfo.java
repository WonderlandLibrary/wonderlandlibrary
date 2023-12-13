// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class FlatLayerInfo
{
    private final int field_175902_a;
    private IBlockState field_175901_b;
    private int layerCount;
    private int layerMinimumY;
    private static final String __OBFID = "CL_00000441";
    
    public FlatLayerInfo(final int p_i45467_1_, final Block p_i45467_2_) {
        this(3, p_i45467_1_, p_i45467_2_);
    }
    
    public FlatLayerInfo(final int p_i45627_1_, final int p_i45627_2_, final Block p_i45627_3_) {
        this.layerCount = 1;
        this.field_175902_a = p_i45627_1_;
        this.layerCount = p_i45627_2_;
        this.field_175901_b = p_i45627_3_.getDefaultState();
    }
    
    public FlatLayerInfo(final int p_i45628_1_, final int p_i45628_2_, final Block p_i45628_3_, final int p_i45628_4_) {
        this(p_i45628_1_, p_i45628_2_, p_i45628_3_);
        this.field_175901_b = p_i45628_3_.getStateFromMeta(p_i45628_4_);
    }
    
    public int getLayerCount() {
        return this.layerCount;
    }
    
    public IBlockState func_175900_c() {
        return this.field_175901_b;
    }
    
    private Block func_151536_b() {
        return this.field_175901_b.getBlock();
    }
    
    private int getFillBlockMeta() {
        return this.field_175901_b.getBlock().getMetaFromState(this.field_175901_b);
    }
    
    public int getMinY() {
        return this.layerMinimumY;
    }
    
    public void setMinY(final int p_82660_1_) {
        this.layerMinimumY = p_82660_1_;
    }
    
    @Override
    public String toString() {
        String var3;
        if (this.field_175902_a >= 3) {
            final ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.func_151536_b());
            var3 = ((var2 == null) ? "null" : var2.toString());
            if (this.layerCount > 1) {
                var3 = this.layerCount + "*" + var3;
            }
        }
        else {
            var3 = Integer.toString(Block.getIdFromBlock(this.func_151536_b()));
            if (this.layerCount > 1) {
                var3 = this.layerCount + "x" + var3;
            }
        }
        final int var4 = this.getFillBlockMeta();
        if (var4 > 0) {
            var3 = var3 + ":" + var4;
        }
        return var3;
    }
}
