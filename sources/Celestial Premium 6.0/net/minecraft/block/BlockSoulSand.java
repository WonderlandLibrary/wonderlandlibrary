/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.player.NoSlowDown;

public class BlockSoulSand
extends Block {
    protected static final AxisAlignedBB SOUL_SAND_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0);

    public BlockSoulSand() {
        super(Material.SAND, MapColor.BROWN);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return SOUL_SAND_AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoSlowDown.class).getState() && NoSlowDown.soulSand.getCurrentValue()) {
            return;
        }
        entityIn.motionX *= 0.4;
        entityIn.motionZ *= 0.4;
    }
}

