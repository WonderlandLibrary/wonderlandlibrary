// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockTNT extends Block
{
    public static final PropertyBool EXPLODE;
    
    public BlockTNT() {
        super(Material.TNT);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onPlayerDestroy(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (worldIn.isBlockPowered(pos)) {
            this.onPlayerDestroy(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onExplosionDestroy(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        if (!worldIn.isRemote) {
            final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, explosionIn.getExplosivePlacedBy());
            entitytntprimed.setFuse((short)(worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            worldIn.spawnEntity(entitytntprimed);
        }
    }
    
    @Override
    public void onPlayerDestroy(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }
    
    public void explode(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase igniter) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockTNT.EXPLODE)) {
            final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, igniter);
            worldIn.spawnEntity(entitytntprimed);
            worldIn.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = playerIn.getHeldItem(hand);
        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE)) {
            this.explode(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
                itemstack.damageItem(1, playerIn);
            }
            else if (!playerIn.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            final EntityArrow entityarrow = (EntityArrow)entityIn;
            if (entityarrow.isBurning()) {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true), (entityarrow.shootingEntity instanceof EntityLivingBase) ? ((EntityLivingBase)entityarrow.shootingEntity) : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public boolean canDropFromExplosion(final Explosion explosionIn) {
        return false;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, (meta & 0x1) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockTNT.EXPLODE)) ? 1 : 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTNT.EXPLODE });
    }
    
    static {
        EXPLODE = PropertyBool.create("explode");
    }
}
