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

    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (!worldIn.isBlockPowered(pos)) return;
        this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, true));
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isBlockPowered(pos)) return;
        this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, true));
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (worldIn.isRemote) return;
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (float)pos.getX() + 0.5f, pos.getY(), (float)pos.getZ() + 0.5f, explosionIn.getExplosivePlacedBy());
        entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
        worldIn.spawnEntityInWorld(entitytntprimed);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }

    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        if (worldIn.isRemote) return;
        if (state.getValue(EXPLODE) == false) return;
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (float)pos.getX() + 0.5f, pos.getY(), (float)pos.getZ() + 0.5f, igniter);
        worldIn.spawnEntityInWorld(entitytntprimed);
        worldIn.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.getCurrentEquippedItem() == null) return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        Item item = playerIn.getCurrentEquippedItem().getItem();
        if (item != Items.flint_and_steel) {
            if (item != Items.fire_charge) return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
        this.explode(worldIn, pos, state.withProperty(EXPLODE, true), playerIn);
        worldIn.setBlockToAir(pos);
        if (item == Items.flint_and_steel) {
            playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
            return true;
        }
        if (playerIn.capabilities.isCreativeMode) return true;
        --playerIn.getCurrentEquippedItem().stackSize;
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        if (!(entityIn instanceof EntityArrow)) return;
        EntityArrow entityarrow = (EntityArrow)entityIn;
        if (!entityarrow.isBurning()) return;
        this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, true), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState();
        if ((meta & 1) > 0) {
            bl = true;
            return iBlockState.withProperty(EXPLODE, bl);
        }
        bl = false;
        return iBlockState.withProperty(EXPLODE, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(EXPLODE) == false) return 0;
        return 1;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, EXPLODE);
    }
}

