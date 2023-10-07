// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockTNT extends Block
{
    public static final PropertyBool field_176246_a;
    private static final String __OBFID = "CL_00000324";
    
    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTNT.field_176246_a, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockDestroyedByExplosion(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        if (!worldIn.isRemote) {
            final EntityTNTPrimed var4 = new EntityTNTPrimed(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, explosionIn.getExplosivePlacedBy());
            var4.fuse = worldIn.rand.nextInt(var4.fuse / 4) + var4.fuse / 8;
            worldIn.spawnEntityInWorld(var4);
        }
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_180692_a(worldIn, pos, state, null);
    }
    
    public void func_180692_a(final World worldIn, final BlockPos p_180692_2_, final IBlockState p_180692_3_, final EntityLivingBase p_180692_4_) {
        if (!worldIn.isRemote && (boolean)p_180692_3_.getValue(BlockTNT.field_176246_a)) {
            final EntityTNTPrimed var5 = new EntityTNTPrimed(worldIn, p_180692_2_.getX() + 0.5f, p_180692_2_.getY() + 0.5f, p_180692_2_.getZ() + 0.5f, p_180692_4_);
            worldIn.spawnEntityInWorld(var5);
            worldIn.playSoundAtEntity(var5, "game.tnt.primed", 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (playerIn.getCurrentEquippedItem() != null) {
            final Item var9 = playerIn.getCurrentEquippedItem().getItem();
            if (var9 == Items.flint_and_steel || var9 == Items.fire_charge) {
                this.func_180692_a(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, true), playerIn);
                worldIn.setBlockToAir(pos);
                if (var9 == Items.flint_and_steel) {
                    playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
                }
                else if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack currentEquippedItem = playerIn.getCurrentEquippedItem();
                    --currentEquippedItem.stackSize;
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            final EntityArrow var5 = (EntityArrow)entityIn;
            if (var5.isBurning()) {
                this.func_180692_a(worldIn, pos, worldIn.getBlockState(pos).withProperty(BlockTNT.field_176246_a, true), (var5.shootingEntity instanceof EntityLivingBase) ? ((EntityLivingBase)var5.shootingEntity) : null);
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
        return this.getDefaultState().withProperty(BlockTNT.field_176246_a, (meta & 0x1) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue(BlockTNT.field_176246_a)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTNT.field_176246_a });
    }
    
    static {
        field_176246_a = PropertyBool.create("explode");
    }
}
