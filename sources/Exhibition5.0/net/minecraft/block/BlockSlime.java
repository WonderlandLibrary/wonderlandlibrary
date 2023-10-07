// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockSlime extends BlockBreakable
{
    private static final String __OBFID = "CL_00002063";
    
    public BlockSlime() {
        super(Material.clay, false);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.slipperiness = 0.8f;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
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
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final Entity entityIn) {
        if (Math.abs(entityIn.motionY) < 0.1 && !entityIn.isSneaking()) {
            final double var4 = 0.4 + Math.abs(entityIn.motionY) * 0.2;
            entityIn.motionX *= var4;
            entityIn.motionZ *= var4;
        }
        super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }
}
