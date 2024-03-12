// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class FlatLayerInfo
{
    private final int field_175902_a;
    private IBlockState layerMaterial;
    private int layerCount;
    private int layerMinimumY;
    
    public FlatLayerInfo(final int p_i45467_1_, final Block p_i45467_2_) {
        this(3, p_i45467_1_, p_i45467_2_);
    }
    
    public FlatLayerInfo(final int p_i45627_1_, final int height, final Block layerMaterialIn) {
        this.layerCount = 1;
        this.field_175902_a = p_i45627_1_;
        this.layerCount = height;
        this.layerMaterial = layerMaterialIn.getDefaultState();
    }
    
    public FlatLayerInfo(final int p_i45628_1_, final int p_i45628_2_, final Block p_i45628_3_, final int p_i45628_4_) {
        this(p_i45628_1_, p_i45628_2_, p_i45628_3_);
        this.layerMaterial = p_i45628_3_.getStateFromMeta(p_i45628_4_);
    }
    
    public int getLayerCount() {
        return this.layerCount;
    }
    
    public IBlockState getLayerMaterial() {
        return this.layerMaterial;
    }
    
    private Block getLayerMaterialBlock() {
        return this.layerMaterial.getBlock();
    }
    
    private int getFillBlockMeta() {
        return this.layerMaterial.getBlock().getMetaFromState(this.layerMaterial);
    }
    
    public int getMinY() {
        return this.layerMinimumY;
    }
    
    public void setMinY(final int minY) {
        this.layerMinimumY = minY;
    }
    
    @Override
    public String toString() {
        String s;
        if (this.field_175902_a >= 3) {
            final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(this.getLayerMaterialBlock());
            s = ((resourcelocation == null) ? "null" : resourcelocation.toString());
            if (this.layerCount > 1) {
                s = this.layerCount + "*" + s;
            }
        }
        else {
            s = Integer.toString(Block.getIdFromBlock(this.getLayerMaterialBlock()));
            if (this.layerCount > 1) {
                s = this.layerCount + "x" + s;
            }
        }
        final int i = this.getFillBlockMeta();
        if (i > 0) {
            s = s + ":" + i;
        }
        return s;
    }
}
