// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockPattern;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockSkull extends BlockContainer
{
    public static final PropertyDirection field_176418_a;
    public static final PropertyBool field_176417_b;
    private static final Predicate field_176419_M;
    private BlockPattern field_176420_N;
    private BlockPattern field_176421_O;
    private static final String __OBFID = "CL_00000307";
    
    protected BlockSkull() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSkull.field_176418_a, EnumFacing.NORTH).withProperty(BlockSkull.field_176417_b, false));
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        switch (SwitchEnumFacing.field_177063_a[((EnumFacing)access.getBlockState(pos).getValue(BlockSkull.field_176418_a)).ordinal()]) {
            default: {
                this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
                break;
            }
            case 2: {
                this.setBlockBounds(0.25f, 0.25f, 0.5f, 0.75f, 0.75f, 1.0f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.5f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
                break;
            }
            case 5: {
                this.setBlockBounds(0.0f, 0.25f, 0.25f, 0.5f, 0.75f, 0.75f);
                break;
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockSkull.field_176418_a, placer.func_174811_aO()).withProperty(BlockSkull.field_176417_b, false);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntitySkull();
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.skull;
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final TileEntity var3 = worldIn.getTileEntity(pos);
        return (var3 instanceof TileEntitySkull) ? ((TileEntitySkull)var3).getSkullType() : super.getDamageValue(worldIn, pos);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode) {
            state = state.withProperty(BlockSkull.field_176417_b, true);
            worldIn.setBlockState(pos, state, 4);
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (!(boolean)state.getValue(BlockSkull.field_176417_b)) {
                final TileEntity var4 = worldIn.getTileEntity(pos);
                if (var4 instanceof TileEntitySkull) {
                    final TileEntitySkull var5 = (TileEntitySkull)var4;
                    final ItemStack var6 = new ItemStack(Items.skull, 1, this.getDamageValue(worldIn, pos));
                    if (var5.getSkullType() == 3 && var5.getPlayerProfile() != null) {
                        var6.setTagCompound(new NBTTagCompound());
                        final NBTTagCompound var7 = new NBTTagCompound();
                        NBTUtil.writeGameProfile(var7, var5.getPlayerProfile());
                        var6.getTagCompound().setTag("SkullOwner", var7);
                    }
                    Block.spawnAsEntity(worldIn, pos, var6);
                }
            }
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.skull;
    }
    
    public boolean func_176415_b(final World worldIn, final BlockPos p_176415_2_, final ItemStack p_176415_3_) {
        return p_176415_3_.getMetadata() == 1 && p_176415_2_.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote && this.func_176414_j().func_177681_a(worldIn, p_176415_2_) != null;
    }
    
    public void func_180679_a(final World worldIn, final BlockPos p_180679_2_, final TileEntitySkull p_180679_3_) {
        if (p_180679_3_.getSkullType() == 1 && p_180679_2_.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
            final BlockPattern var4 = this.func_176416_l();
            final BlockPattern.PatternHelper var5 = var4.func_177681_a(worldIn, p_180679_2_);
            if (var5 != null) {
                for (int var6 = 0; var6 < 3; ++var6) {
                    final BlockWorldState var7 = var5.func_177670_a(var6, 0, 0);
                    worldIn.setBlockState(var7.getPos(), var7.func_177509_a().withProperty(BlockSkull.field_176417_b, true), 2);
                }
                for (int var6 = 0; var6 < var4.func_177684_c(); ++var6) {
                    for (int var8 = 0; var8 < var4.func_177685_b(); ++var8) {
                        final BlockWorldState var9 = var5.func_177670_a(var6, var8, 0);
                        worldIn.setBlockState(var9.getPos(), Blocks.air.getDefaultState(), 2);
                    }
                }
                final BlockPos var10 = var5.func_177670_a(1, 0, 0).getPos();
                final EntityWither var11 = new EntityWither(worldIn);
                final BlockPos var12 = var5.func_177670_a(1, 2, 0).getPos();
                var11.setLocationAndAngles(var12.getX() + 0.5, var12.getY() + 0.55, var12.getZ() + 0.5, (var5.func_177669_b().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f, 0.0f);
                var11.renderYawOffset = ((var5.func_177669_b().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f);
                var11.func_82206_m();
                for (final EntityPlayer var14 : worldIn.getEntitiesWithinAABB(EntityPlayer.class, var11.getEntityBoundingBox().expand(50.0, 50.0, 50.0))) {
                    var14.triggerAchievement(AchievementList.spawnWither);
                }
                worldIn.spawnEntityInWorld(var11);
                for (int var15 = 0; var15 < 120; ++var15) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, var10.getX() + worldIn.rand.nextDouble(), var10.getY() - 2 + worldIn.rand.nextDouble() * 3.9, var10.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (int var15 = 0; var15 < var4.func_177684_c(); ++var15) {
                    for (int var16 = 0; var16 < var4.func_177685_b(); ++var16) {
                        final BlockWorldState var17 = var5.func_177670_a(var15, var16, 0);
                        worldIn.func_175722_b(var17.getPos(), Blocks.air);
                    }
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSkull.field_176418_a, EnumFacing.getFront(meta & 0x7)).withProperty(BlockSkull.field_176417_b, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockSkull.field_176418_a)).getIndex();
        if (state.getValue(BlockSkull.field_176417_b)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSkull.field_176418_a, BlockSkull.field_176417_b });
    }
    
    protected BlockPattern func_176414_j() {
        if (this.field_176420_N == null) {
            this.field_176420_N = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176420_N;
    }
    
    protected BlockPattern func_176416_l() {
        if (this.field_176421_O == null) {
            this.field_176421_O = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', BlockSkull.field_176419_M).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176421_O;
    }
    
    static {
        field_176418_a = PropertyDirection.create("facing");
        field_176417_b = PropertyBool.create("nodrop");
        field_176419_M = (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002065";
            
            public boolean func_177062_a(final BlockWorldState p_177062_1_) {
                return p_177062_1_.func_177509_a().getBlock() == Blocks.skull && p_177062_1_.func_177507_b() instanceof TileEntitySkull && ((TileEntitySkull)p_177062_1_.func_177507_b()).getSkullType() == 1;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_177062_a((BlockWorldState)p_apply_1_);
            }
        };
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177063_a;
        private static final String __OBFID = "CL_00002064";
        
        static {
            field_177063_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.EAST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
        }
    }
}
