package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyBool EXPLODE = PropertyBool.create((String)"explode");

    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf((boolean)true)));
            worldIn.setBlockToAir(pos);
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf((boolean)true)));
            worldIn.setBlockToAir(pos);
        }
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5f), (double)pos.getY(), (double)((float)pos.getZ() + 0.5f), explosionIn.getExplosivePlacedBy());
            entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
            worldIn.spawnEntityInWorld((Entity)entitytntprimed);
        }
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }

    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)EXPLODE)).booleanValue()) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5f), (double)pos.getY(), (double)((float)pos.getZ() + 0.5f), igniter);
            worldIn.spawnEntityInWorld((Entity)entitytntprimed);
            worldIn.playSoundAtEntity((Entity)entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        Item item;
        if (playerIn.getCurrentEquippedItem() != null && ((item = playerIn.getCurrentEquippedItem().getItem()) == Items.flint_and_steel || item == Items.fire_charge)) {
            this.explode(worldIn, pos, state.withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf((boolean)true)), (EntityLivingBase)playerIn);
            worldIn.setBlockToAir(pos);
            if (item == Items.flint_and_steel) {
                playerIn.getCurrentEquippedItem().damageItem(1, (EntityLivingBase)playerIn);
            } else if (!playerIn.capabilities.isCreativeMode) {
                --playerIn.getCurrentEquippedItem().stackSize;
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        EntityArrow entityarrow;
        if (!worldIn.isRemote && entityIn instanceof EntityArrow && (entityarrow = (EntityArrow)entityIn).isBurning()) {
            this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf((boolean)true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
            worldIn.setBlockToAir(pos);
        }
    }

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)EXPLODE, (Comparable)Boolean.valueOf(((meta & 1) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue((IProperty)EXPLODE) != false ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{EXPLODE});
    }
}
