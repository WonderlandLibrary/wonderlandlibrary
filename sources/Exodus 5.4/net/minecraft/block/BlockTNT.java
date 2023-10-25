/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT
extends Block {
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, EXPLODE);
    }

    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (world.isBlockPowered(blockPos)) {
            this.onBlockDestroyedByPlayer(world, blockPos, iBlockState.withProperty(EXPLODE, true));
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        EntityArrow entityArrow;
        if (!world.isRemote && entity instanceof EntityArrow && (entityArrow = (EntityArrow)entity).isBurning()) {
            this.explode(world, blockPos, world.getBlockState(blockPos).withProperty(EXPLODE, true), entityArrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityArrow.shootingEntity : null);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
        if (!world.isRemote) {
            EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(world, (float)blockPos.getX() + 0.5f, blockPos.getY(), (float)blockPos.getZ() + 0.5f, explosion.getExplosivePlacedBy());
            entityTNTPrimed.fuse = world.rand.nextInt(entityTNTPrimed.fuse / 4) + entityTNTPrimed.fuse / 8;
            world.spawnEntityInWorld(entityTNTPrimed);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.onBlockAdded(world, blockPos, iBlockState);
        if (world.isBlockPowered(blockPos)) {
            this.onBlockDestroyedByPlayer(world, blockPos, iBlockState.withProperty(EXPLODE, true));
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(EXPLODE) != false ? 1 : 0;
    }

    public void explode(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase) {
        if (!world.isRemote && iBlockState.getValue(EXPLODE).booleanValue()) {
            EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(world, (float)blockPos.getX() + 0.5f, blockPos.getY(), (float)blockPos.getZ() + 0.5f, entityLivingBase);
            world.spawnEntityInWorld(entityTNTPrimed);
            world.playSoundAtEntity(entityTNTPrimed, "game.tnt.primed", 1.0f, 1.0f);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(EXPLODE, (n & 1) > 0);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.explode(world, blockPos, iBlockState, null);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        Item item;
        if (entityPlayer.getCurrentEquippedItem() != null && ((item = entityPlayer.getCurrentEquippedItem().getItem()) == Items.flint_and_steel || item == Items.fire_charge)) {
            this.explode(world, blockPos, iBlockState.withProperty(EXPLODE, true), entityPlayer);
            world.setBlockToAir(blockPos);
            if (item == Items.flint_and_steel) {
                entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
            } else if (!entityPlayer.capabilities.isCreativeMode) {
                --entityPlayer.getCurrentEquippedItem().stackSize;
            }
            return true;
        }
        return super.onBlockActivated(world, blockPos, iBlockState, entityPlayer, enumFacing, f, f2, f3);
    }
}

