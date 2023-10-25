/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.player.NoSlowDown;

public class BlockSlime
extends BlockBreakable {
    public BlockSlime() {
        super(Material.CLAY, false, MapColor.GRASS);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.slipperiness = 0.8f;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.fall(fallDistance, 0.0f);
        }
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0.0) {
            entityIn.motionY = -entityIn.motionY;
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8;
            }
        }
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoSlowDown.class).getState() && NoSlowDown.slimeBlock.getCurrentValue()) {
            return;
        }
        if (Math.abs(entityIn.motionY) < 0.1 && !entityIn.isSneaking()) {
            double d0 = 0.4 + Math.abs(entityIn.motionY) * 0.2;
            entityIn.motionX *= d0;
            entityIn.motionZ *= d0;
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}

