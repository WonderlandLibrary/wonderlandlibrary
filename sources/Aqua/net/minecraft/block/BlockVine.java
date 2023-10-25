package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine
extends Block {
    public static final PropertyBool UP = PropertyBool.create((String)"up");
    public static final PropertyBool NORTH = PropertyBool.create((String)"north");
    public static final PropertyBool EAST = PropertyBool.create((String)"east");
    public static final PropertyBool SOUTH = PropertyBool.create((String)"south");
    public static final PropertyBool WEST = PropertyBool.create((String)"west");
    public static final PropertyBool[] ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};

    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty((IProperty)UP, (Comparable)Boolean.valueOf((boolean)worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube()));
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        float f = 0.0625f;
        float f1 = 1.0f;
        float f2 = 1.0f;
        float f3 = 1.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        boolean flag = false;
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)WEST)).booleanValue()) {
            f4 = Math.max((float)f4, (float)0.0625f);
            f1 = 0.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            flag = true;
        }
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)EAST)).booleanValue()) {
            f1 = Math.min((float)f1, (float)0.9375f);
            f4 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            flag = true;
        }
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)NORTH)).booleanValue()) {
            f6 = Math.max((float)f6, (float)0.0625f);
            f3 = 0.0f;
            f1 = 0.0f;
            f4 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            flag = true;
        }
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)SOUTH)).booleanValue()) {
            f3 = Math.min((float)f3, (float)0.9375f);
            f6 = 1.0f;
            f1 = 0.0f;
            f4 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            flag = true;
        }
        if (!flag && this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock())) {
            f2 = Math.min((float)f2, (float)0.9375f);
            f5 = 1.0f;
            f1 = 0.0f;
            f4 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
        }
        this.setBlockBounds(f1, f2, f3, f4, f5, f6);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[side.ordinal()]) {
            case 1: {
                return this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                return this.canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
            }
        }
        return false;
    }

    private boolean canPlaceOn(Block blockIn) {
        return blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement();
    }

    private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
        IBlockState iblockstate = state;
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iblockstate1;
            PropertyBool propertybool = BlockVine.getPropertyFor(enumfacing);
            if (!((Boolean)state.getValue((IProperty)propertybool)).booleanValue() || this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()) || (iblockstate1 = worldIn.getBlockState(pos.up())).getBlock() == this && ((Boolean)iblockstate1.getValue((IProperty)propertybool)).booleanValue()) continue;
            state = state.withProperty((IProperty)propertybool, (Comparable)Boolean.valueOf((boolean)false));
        }
        if (BlockVine.getNumGrownFaces(state) == 0) {
            return false;
        }
        if (iblockstate != state) {
            worldIn.setBlockState(pos, state, 2);
        }
        return true;
    }

    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int getRenderColor(IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            int i = 4;
            int j = 5;
            boolean flag = false;
            block0: for (int k = -i; k <= i; ++k) {
                for (int l = -i; l <= i; ++l) {
                    for (int i1 = -1; i1 <= 1; ++i1) {
                        if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() != this || --j > 0) continue;
                        flag = true;
                        break block0;
                    }
                }
            }
            EnumFacing enumfacing1 = EnumFacing.random((Random)rand);
            BlockPos blockpos1 = pos.up();
            if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos1)) {
                if (!flag) {
                    IBlockState iblockstate2 = state;
                    for (EnumFacing enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
                        if (!rand.nextBoolean() && this.canPlaceOn(worldIn.getBlockState(blockpos1.offset(enumfacing3)).getBlock())) continue;
                        iblockstate2 = iblockstate2.withProperty((IProperty)BlockVine.getPropertyFor(enumfacing3), (Comparable)Boolean.valueOf((boolean)false));
                    }
                    if (((Boolean)iblockstate2.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)WEST)).booleanValue()) {
                        worldIn.setBlockState(blockpos1, iblockstate2, 2);
                    }
                }
            } else if (enumfacing1.getAxis().isHorizontal() && !((Boolean)state.getValue((IProperty)BlockVine.getPropertyFor(enumfacing1))).booleanValue()) {
                if (!flag) {
                    BlockPos blockpos3 = pos.offset(enumfacing1);
                    Block block1 = worldIn.getBlockState(blockpos3).getBlock();
                    if (block1.blockMaterial == Material.air) {
                        EnumFacing enumfacing2 = enumfacing1.rotateY();
                        EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                        boolean flag1 = (Boolean)state.getValue((IProperty)BlockVine.getPropertyFor(enumfacing2));
                        boolean flag2 = (Boolean)state.getValue((IProperty)BlockVine.getPropertyFor(enumfacing4));
                        BlockPos blockpos4 = blockpos3.offset(enumfacing2);
                        BlockPos blockpos = blockpos3.offset(enumfacing4);
                        if (flag1 && this.canPlaceOn(worldIn.getBlockState(blockpos4).getBlock())) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty((IProperty)BlockVine.getPropertyFor(enumfacing2), (Comparable)Boolean.valueOf((boolean)true)), 2);
                        } else if (flag2 && this.canPlaceOn(worldIn.getBlockState(blockpos).getBlock())) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty((IProperty)BlockVine.getPropertyFor(enumfacing4), (Comparable)Boolean.valueOf((boolean)true)), 2);
                        } else if (flag1 && worldIn.isAirBlock(blockpos4) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing2)).getBlock())) {
                            worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty((IProperty)BlockVine.getPropertyFor(enumfacing1.getOpposite()), (Comparable)Boolean.valueOf((boolean)true)), 2);
                        } else if (flag2 && worldIn.isAirBlock(blockpos) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing4)).getBlock())) {
                            worldIn.setBlockState(blockpos, this.getDefaultState().withProperty((IProperty)BlockVine.getPropertyFor(enumfacing1.getOpposite()), (Comparable)Boolean.valueOf((boolean)true)), 2);
                        } else if (this.canPlaceOn(worldIn.getBlockState(blockpos3.up()).getBlock())) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState(), 2);
                        }
                    } else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
                        worldIn.setBlockState(pos, state.withProperty((IProperty)BlockVine.getPropertyFor(enumfacing1), (Comparable)Boolean.valueOf((boolean)true)), 2);
                    }
                }
            } else if (pos.getY() > 1) {
                BlockPos blockpos2 = pos.down();
                IBlockState iblockstate = worldIn.getBlockState(blockpos2);
                Block block = iblockstate.getBlock();
                if (block.blockMaterial == Material.air) {
                    IBlockState iblockstate1 = state;
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        if (!rand.nextBoolean()) continue;
                        iblockstate1 = iblockstate1.withProperty((IProperty)BlockVine.getPropertyFor(enumfacing), (Comparable)Boolean.valueOf((boolean)false));
                    }
                    if (((Boolean)iblockstate1.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)WEST)).booleanValue()) {
                        worldIn.setBlockState(blockpos2, iblockstate1, 2);
                    }
                } else if (block == this) {
                    IBlockState iblockstate3 = iblockstate;
                    for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
                        PropertyBool propertybool = BlockVine.getPropertyFor(enumfacing5);
                        if (!rand.nextBoolean() || !((Boolean)state.getValue((IProperty)propertybool)).booleanValue()) continue;
                        iblockstate3 = iblockstate3.withProperty((IProperty)propertybool, (Comparable)Boolean.valueOf((boolean)true));
                    }
                    if (((Boolean)iblockstate3.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate3.getValue((IProperty)WEST)).booleanValue()) {
                        worldIn.setBlockState(blockpos2, iblockstate3, 2);
                    }
                }
            }
        }
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty((IProperty)UP, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)false));
        return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty)BlockVine.getPropertyFor(facing.getOpposite()), (Comparable)Boolean.valueOf((boolean)true)) : iblockstate;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
            BlockVine.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Blocks.vine, 1, 0));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf(((meta & 1) > 0 ? 1 : 0) != 0)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf(((meta & 2) > 0 ? 1 : 0) != 0)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf(((meta & 4) > 0 ? 1 : 0) != 0)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue()) {
            i |= 1;
        }
        if (((Boolean)state.getValue((IProperty)WEST)).booleanValue()) {
            i |= 2;
        }
        if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue()) {
            i |= 4;
        }
        if (((Boolean)state.getValue((IProperty)EAST)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{UP, NORTH, EAST, SOUTH, WEST});
    }

    public static PropertyBool getPropertyFor(EnumFacing side) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[side.ordinal()]) {
            case 1: {
                return UP;
            }
            case 2: {
                return NORTH;
            }
            case 3: {
                return SOUTH;
            }
            case 4: {
                return EAST;
            }
            case 5: {
                return WEST;
            }
        }
        throw new IllegalArgumentException(side + " is an invalid choice");
    }

    public static int getNumGrownFaces(IBlockState state) {
        int i = 0;
        for (PropertyBool propertybool : ALL_FACES) {
            if (!((Boolean)state.getValue((IProperty)propertybool)).booleanValue()) continue;
            ++i;
        }
        return i;
    }
}
