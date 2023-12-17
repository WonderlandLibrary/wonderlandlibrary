// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockSlime extends BlockBreakable
{
    public BlockSlime() {
        super(Material.CLAY, false, MapColor.GRASS);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.slipperiness = 0.8f;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else {
            entityIn.fall(fallDistance, 0.0f);
        }
    }
    
    @Override
    public void onLanded(final World worldIn, final Entity entityIn) {
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.motionY < 0.0) {
            entityIn.motionY = -entityIn.motionY;
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8;
            }
        }
    }
    
    @Override
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
        if (Math.abs(entityIn.motionY) < 0.1 && !entityIn.isSneaking()) {
            final double d0 = 0.4 + Math.abs(entityIn.motionY) * 0.2;
            entityIn.motionX *= d0;
            entityIn.motionZ *= d0;
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
