/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import markgg.modules.render.XRay;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockClay;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoubleStoneSlab;
import net.minecraft.block.BlockDoubleStoneSlabNew;
import net.minecraft.block.BlockDoubleWoodSlab;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockHalfStoneSlab;
import net.minecraft.block.BlockHalfStoneSlabNew;
import net.minecraft.block.BlockHalfWoodSlab;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.block.BlockHay;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockNetherBrick;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockPotato;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSeaLantern;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockSnowBlock;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.BlockYellowFlower;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block {
    private static final ResourceLocation AIR_ID = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey blockRegistry = new RegistryNamespacedDefaultedByKey(AIR_ID);
    public static final ObjectIntIdentityMap BLOCK_STATE_IDS = new ObjectIntIdentityMap();
    public static boolean isXRayEnabled;
    private CreativeTabs displayOnCreativeTab;
    public static final SoundType soundTypeStone;
    public static final SoundType soundTypeWood;
    public static final SoundType soundTypeGravel;
    public static final SoundType soundTypeGrass;
    public static final SoundType soundTypePiston;
    public static final SoundType soundTypeMetal;
    public static final SoundType soundTypeGlass;
    public static final SoundType soundTypeCloth;
    public static final SoundType soundTypeSand;
    public static final SoundType soundTypeSnow;
    public static final SoundType soundTypeLadder;
    public static final SoundType soundTypeAnvil;
    public static final SoundType SLIME_SOUND;
    protected boolean fullBlock;
    protected int lightOpacity;
    protected boolean translucent;
    protected int lightValue;
    protected boolean useNeighborBrightness;
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats = true;
    protected boolean needsRandomTick;
    protected boolean isBlockContainer;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public SoundType stepSound = soundTypeStone;
    public float blockParticleGravity = 1.0f;
    protected final Material blockMaterial;
    public float slipperiness = 0.6f;
    protected final BlockState blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;
    private static final String __OBFID = "CL_00000199";

    static {
        soundTypeStone = new SoundType("stone", 1.0f, 1.0f);
        soundTypeWood = new SoundType("wood", 1.0f, 1.0f);
        soundTypeGravel = new SoundType("gravel", 1.0f, 1.0f);
        soundTypeGrass = new SoundType("grass", 1.0f, 1.0f);
        soundTypePiston = new SoundType("stone", 1.0f, 1.0f);
        soundTypeMetal = new SoundType("stone", 1.0f, 1.5f);
        soundTypeGlass = new SoundType("stone", 1.0f, 1.0f){
            private static final String __OBFID = "CL_00000200";

            @Override
            public String getBreakSound() {
                return "dig.glass";
            }

            @Override
            public String getPlaceSound() {
                return "step.stone";
            }
        };
        soundTypeCloth = new SoundType("cloth", 1.0f, 1.0f);
        soundTypeSand = new SoundType("sand", 1.0f, 1.0f);
        soundTypeSnow = new SoundType("snow", 1.0f, 1.0f);
        soundTypeLadder = new SoundType("ladder", 1.0f, 1.0f){
            private static final String __OBFID = "CL_00000201";

            @Override
            public String getBreakSound() {
                return "dig.wood";
            }
        };
        soundTypeAnvil = new SoundType("anvil", 0.3f, 1.0f){
            private static final String __OBFID = "CL_00000202";

            @Override
            public String getBreakSound() {
                return "dig.stone";
            }

            @Override
            public String getPlaceSound() {
                return "random.anvil_land";
            }
        };
        SLIME_SOUND = new SoundType("slime", 1.0f, 1.0f){
            private static final String __OBFID = "CL_00002133";

            @Override
            public String getBreakSound() {
                return "mob.slime.big";
            }

            @Override
            public String getPlaceSound() {
                return "mob.slime.big";
            }

            @Override
            public String getStepSound() {
                return "mob.slime.small";
            }
        };
    }

    public static int getIdFromBlock(Block blockIn) {
        return blockRegistry.getIDForObject(blockIn);
    }

    public static int getStateId(IBlockState state) {
        return Block.getIdFromBlock(state.getBlock()) + (state.getBlock().getMetaFromState(state) << 12);
    }

    public static Block getBlockById(int id) {
        return (Block)blockRegistry.getObjectById(id);
    }

    public static IBlockState getStateById(int id) {
        int var1 = id & 0xFFF;
        int var2 = id >> 12 & 0xF;
        return Block.getBlockById(var1).getStateFromMeta(var2);
    }

    public static Block getBlockFromItem(Item itemIn) {
        return itemIn instanceof ItemBlock ? ((ItemBlock)itemIn).getBlock() : null;
    }

    public static Block getBlockFromName(String name) {
        ResourceLocation var1 = new ResourceLocation(name);
        if (blockRegistry.containsKey(var1)) {
            return (Block)blockRegistry.getObject(var1);
        }
        try {
            return (Block)blockRegistry.getObjectById(Integer.parseInt(name));
        }
        catch (NumberFormatException var3) {
            return null;
        }
    }

    public boolean isFullBlock() {
        return this.fullBlock;
    }

    public int getLightOpacity() {
        return this.lightOpacity;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public int getLightValue() {
        return this.lightValue;
    }

    public boolean getUseNeighborBrightness() {
        return this.useNeighborBrightness;
    }

    public Material getMaterial() {
        return this.blockMaterial;
    }

    public MapColor getMapColor(IBlockState state) {
        return this.getMaterial().getMaterialMapColor();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        if (state != null && !state.getPropertyNames().isEmpty()) {
            throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
        }
        return 0;
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    protected Block(Material materialIn) {
        this.blockMaterial = materialIn;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.fullBlock = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
        this.translucent = !materialIn.blocksLight();
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
    }

    protected Block setStepSound(SoundType sound) {
        this.stepSound = sound;
        return this;
    }

    protected Block setLightOpacity(int opacity) {
        this.lightOpacity = opacity;
        return this;
    }

    protected Block setLightLevel(float value) {
        this.lightValue = (int)(15.0f * value);
        return this;
    }

    protected Block setResistance(float resistance) {
        this.blockResistance = resistance * 3.0f;
        return this;
    }

    public boolean isSolidFullCube() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean isNormalCube() {
        return this.blockMaterial.isOpaque() && this.isFullCube() && !this.canProvidePower();
    }

    public boolean isVisuallyOpaque() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean isFullCube() {
        return true;
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return !this.blockMaterial.blocksMovement();
    }

    public int getRenderType() {
        return 3;
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return false;
    }

    protected Block setHardness(float hardness) {
        this.blockHardness = hardness;
        if (this.blockResistance < hardness * 5.0f) {
            this.blockResistance = hardness * 5.0f;
        }
        return this;
    }

    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0f);
        return this;
    }

    public float getBlockHardness(World worldIn, BlockPos pos) {
        return this.blockHardness;
    }

    protected Block setTickRandomly(boolean shouldTick) {
        this.needsRandomTick = shouldTick;
        return this;
    }

    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }

    public boolean hasTileEntity() {
        return this.isBlockContainer;
    }

    protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        if (isXRayEnabled) {
            return 1000000000;
        }
        Block var3 = worldIn.getBlockState(pos).getBlock();
        int var4 = worldIn.getCombinedLight(pos, var3.getLightValue());
        if (var4 == 0 && var3 instanceof BlockSlab) {
            pos = pos.offsetDown();
            var3 = worldIn.getBlockState(pos).getBlock();
            return worldIn.getCombinedLight(pos, var3.getLightValue());
        }
        return var4;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (XRay.isXrayBlock(this)) {
            return true;
        }
        if (isXRayEnabled) {
            return false;
        }
        return side == EnumFacing.DOWN && this.minY > 0.0 ? true : (side == EnumFacing.UP && this.maxY < 1.0 ? true : (side == EnumFacing.NORTH && this.minZ > 0.0 ? true : (side == EnumFacing.SOUTH && this.maxZ < 1.0 ? true : (side == EnumFacing.WEST && this.minX > 0.0 ? true : (side == EnumFacing.EAST && this.maxX < 1.0 ? true : !worldIn.getBlockState(pos).getBlock().isOpaqueCube())))));
    }

    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        AxisAlignedBB var7 = this.getCollisionBoundingBox(worldIn, pos, state);
        if (var7 != null && mask.intersectsWith(var7)) {
            list.add(var7);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return this.isCollidable();
    }

    public boolean isCollidable() {
        return true;
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        this.updateTick(worldIn, pos, state, random);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    }

    public int tickRate(World worldIn) {
        return 10;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        float var4 = this.getBlockHardness(worldIn, pos);
        return var4 < 0.0f ? 0.0f : (!playerIn.canHarvestBlock(this) ? playerIn.func_180471_a(this) / var4 / 100.0f : playerIn.func_180471_a(this) / var4 / 30.0f);
    }

    public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture) {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0f, forture);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int var6 = this.quantityDroppedWithBonus(fortune, worldIn.rand);
            for (int var7 = 0; var7 < var6; ++var7) {
                Item var8;
                if (!(worldIn.rand.nextFloat() <= chance) || (var8 = this.getItemDropped(state, worldIn.rand, fortune)) == null) continue;
                Block.spawnAsEntity(worldIn, pos, new ItemStack(var8, 1, this.damageDropped(state)));
            }
        }
    }

    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            float var3 = 0.5f;
            double var4 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0f - var3) * 0.5;
            double var6 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0f - var3) * 0.5;
            double var8 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0f - var3) * 0.5;
            EntityItem var10 = new EntityItem(worldIn, (double)pos.getX() + var4, (double)pos.getY() + var6, (double)pos.getZ() + var8, stack);
            var10.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(var10);
        }
    }

    protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
        if (!worldIn.isRemote) {
            while (amount > 0) {
                int var4 = EntityXPOrb.getXPSplit(amount);
                amount -= var4;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, var4));
            }
        }
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }

    public float getExplosionResistance(Entity exploder) {
        return this.blockResistance / 5.0f;
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        start = start.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
        end = end.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
        Vec3 var5 = start.getIntermediateWithXValue(end, this.minX);
        Vec3 var6 = start.getIntermediateWithXValue(end, this.maxX);
        Vec3 var7 = start.getIntermediateWithYValue(end, this.minY);
        Vec3 var8 = start.getIntermediateWithYValue(end, this.maxY);
        Vec3 var9 = start.getIntermediateWithZValue(end, this.minZ);
        Vec3 var10 = start.getIntermediateWithZValue(end, this.maxZ);
        if (!this.isVecInsideYZBounds(var5)) {
            var5 = null;
        }
        if (!this.isVecInsideYZBounds(var6)) {
            var6 = null;
        }
        if (!this.isVecInsideXZBounds(var7)) {
            var7 = null;
        }
        if (!this.isVecInsideXZBounds(var8)) {
            var8 = null;
        }
        if (!this.isVecInsideXYBounds(var9)) {
            var9 = null;
        }
        if (!this.isVecInsideXYBounds(var10)) {
            var10 = null;
        }
        Vec3 var11 = null;
        if (var5 != null && (var11 == null || start.squareDistanceTo(var5) < start.squareDistanceTo(var11))) {
            var11 = var5;
        }
        if (var6 != null && (var11 == null || start.squareDistanceTo(var6) < start.squareDistanceTo(var11))) {
            var11 = var6;
        }
        if (var7 != null && (var11 == null || start.squareDistanceTo(var7) < start.squareDistanceTo(var11))) {
            var11 = var7;
        }
        if (var8 != null && (var11 == null || start.squareDistanceTo(var8) < start.squareDistanceTo(var11))) {
            var11 = var8;
        }
        if (var9 != null && (var11 == null || start.squareDistanceTo(var9) < start.squareDistanceTo(var11))) {
            var11 = var9;
        }
        if (var10 != null && (var11 == null || start.squareDistanceTo(var10) < start.squareDistanceTo(var11))) {
            var11 = var10;
        }
        if (var11 == null) {
            return null;
        }
        EnumFacing var12 = null;
        if (var11 == var5) {
            var12 = EnumFacing.WEST;
        }
        if (var11 == var6) {
            var12 = EnumFacing.EAST;
        }
        if (var11 == var7) {
            var12 = EnumFacing.DOWN;
        }
        if (var11 == var8) {
            var12 = EnumFacing.UP;
        }
        if (var11 == var9) {
            var12 = EnumFacing.NORTH;
        }
        if (var11 == var10) {
            var12 = EnumFacing.SOUTH;
        }
        return new MovingObjectPosition(var11.addVector(pos.getX(), pos.getY(), pos.getZ()), var12, pos);
    }

    private boolean isVecInsideYZBounds(Vec3 point) {
        return point == null ? false : point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXZBounds(Vec3 point) {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    private boolean isVecInsideXYBounds(Vec3 point) {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY;
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.SOLID;
    }

    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
        return this.canPlaceBlockOnSide(worldIn, pos, side);
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial.isReplaceable();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    }

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
    }

    public final double getBlockBoundsMinX() {
        return this.minX;
    }

    public final double getBlockBoundsMaxX() {
        return this.maxX;
    }

    public final double getBlockBoundsMinY() {
        return this.minY;
    }

    public final double getBlockBoundsMaxY() {
        return this.maxY;
    }

    public final double getBlockBoundsMinZ() {
        return this.minZ;
    }

    public final double getBlockBoundsMaxZ() {
        return this.maxZ;
    }

    public int getBlockColor() {
        return 0xFFFFFF;
    }

    public int getRenderColor(IBlockState state) {
        return 0xFFFFFF;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return 0xFFFFFF;
    }

    public final int colorMultiplier(IBlockAccess worldIn, BlockPos pos) {
        return this.colorMultiplier(worldIn, pos, 0);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return 0;
    }

    public boolean canProvidePower() {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return 0;
    }

    public void setBlockBoundsForItemRender() {
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        playerIn.addExhaustion(0.025f);
        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(playerIn)) {
            ItemStack var7 = this.createStackedBlock(state);
            if (var7 != null) {
                Block.spawnAsEntity(worldIn, pos, var7);
            }
        } else {
            int var6 = EnchantmentHelper.getFortuneModifier(playerIn);
            this.dropBlockAsItem(worldIn, pos, state, var6);
        }
    }

    protected boolean canSilkHarvest() {
        return this.isFullCube() && !this.isBlockContainer;
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        int var2 = 0;
        Item var3 = Item.getItemFromBlock(this);
        if (var3 != null && var3.getHasSubtypes()) {
            var2 = this.getMetaFromState(state);
        }
        return new ItemStack(var3, 1, var2);
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    }

    public Block setUnlocalizedName(String name) {
        this.unlocalizedName = name;
        return this;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name");
    }

    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedName;
    }

    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        return false;
    }

    public boolean getEnableStats() {
        return this.enableStats;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    public int getMobilityFlag() {
        return this.blockMaterial.getMaterialMobility();
    }

    public float getAmbientOcclusionLightValue() {
        return this.isSolidFullCube() ? 0.2f : 1.0f;
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.fall(fallDistance, 1.0f);
    }

    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.motionY = 0.0;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(this);
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        return this.damageDropped(worldIn.getBlockState(pos));
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }

    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs tab) {
        this.displayOnCreativeTab = tab;
        return this;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
    }

    public void fillWithRain(World worldIn, BlockPos pos) {
    }

    public boolean isFlowerPot() {
        return false;
    }

    public boolean requiresUpdates() {
        return true;
    }

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return true;
    }

    public boolean isAssociatedBlock(Block other) {
        return this == other;
    }

    public static boolean isEqualTo(Block blockIn, Block other) {
        return blockIn != null && other != null ? (blockIn == other ? true : blockIn.isAssociatedBlock(other)) : false;
    }

    public boolean hasComparatorInputOverride() {
        return false;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return 0;
    }

    public IBlockState getStateForEntityRender(IBlockState state) {
        return state;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[0]);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    protected final void setDefaultState(IBlockState state) {
        this.defaultBlockState = state;
    }

    public final IBlockState getDefaultState() {
        return this.defaultBlockState;
    }

    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    public static void registerBlocks() {
        Block.registerBlock(0, AIR_ID, new BlockAir().setUnlocalizedName("air"));
        Block.registerBlock(1, "stone", new BlockStone().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stone"));
        Block.registerBlock(2, "grass", new BlockGrass().setHardness(0.6f).setStepSound(soundTypeGrass).setUnlocalizedName("grass"));
        Block.registerBlock(3, "dirt", new BlockDirt().setHardness(0.5f).setStepSound(soundTypeGravel).setUnlocalizedName("dirt"));
        Block var0 = new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(4, "cobblestone", var0);
        Block var1 = new BlockPlanks().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("wood");
        Block.registerBlock(5, "planks", var1);
        Block.registerBlock(6, "sapling", new BlockSapling().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("sapling"));
        Block.registerBlock(7, "bedrock", new Block(Material.rock).setBlockUnbreakable().setResistance(6000000.0f).setStepSound(soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.water).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(9, "water", new BlockStaticLiquid(Material.water).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.lava).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(11, "lava", new BlockStaticLiquid(Material.lava).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(12, "sand", new BlockSand().setHardness(0.5f).setStepSound(soundTypeSand).setUnlocalizedName("sand"));
        Block.registerBlock(13, "gravel", new BlockGravel().setHardness(0.6f).setStepSound(soundTypeGravel).setUnlocalizedName("gravel"));
        Block.registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreGold"));
        Block.registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreIron"));
        Block.registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreCoal"));
        Block.registerBlock(17, "log", new BlockOldLog().setUnlocalizedName("log"));
        Block.registerBlock(18, "leaves", new BlockOldLeaf().setUnlocalizedName("leaves"));
        Block.registerBlock(19, "sponge", new BlockSponge().setHardness(0.6f).setStepSound(soundTypeGrass).setUnlocalizedName("sponge"));
        Block.registerBlock(20, "glass", new BlockGlass(Material.glass, false).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("glass"));
        Block.registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreLapis"));
        Block.registerBlock(22, "lapis_block", new BlockCompressed(MapColor.lapisColor).setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("dispenser"));
        Block var2 = new BlockSandStone().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("sandStone");
        Block.registerBlock(24, "sandstone", var2);
        Block.registerBlock(25, "noteblock", new BlockNote().setHardness(0.8f).setUnlocalizedName("musicBlock"));
        Block.registerBlock(26, "bed", new BlockBed().setStepSound(soundTypeWood).setHardness(0.2f).setUnlocalizedName("bed").disableStats());
        Block.registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("goldenRail"));
        Block.registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("detectorRail"));
        Block.registerBlock(29, "sticky_piston", new BlockPistonBase(true).setUnlocalizedName("pistonStickyBase"));
        Block.registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0f).setUnlocalizedName("web"));
        Block.registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("tallgrass"));
        Block.registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("deadbush"));
        Block.registerBlock(33, "piston", new BlockPistonBase(false).setUnlocalizedName("pistonBase"));
        Block.registerBlock(34, "piston_head", (Block)new BlockPistonExtension());
        Block.registerBlock(35, "wool", new BlockColored(Material.cloth).setHardness(0.8f).setStepSound(soundTypeCloth).setUnlocalizedName("cloth"));
        Block.registerBlock(36, "piston_extension", (Block)new BlockPistonMoving());
        Block.registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("flower1"));
        Block.registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("flower2"));
        Block var3 = new BlockMushroom().setHardness(0.0f).setStepSound(soundTypeGrass).setLightLevel(0.125f).setUnlocalizedName("mushroom");
        Block.registerBlock(39, "brown_mushroom", var3);
        Block var4 = new BlockMushroom().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("mushroom");
        Block.registerBlock(40, "red_mushroom", var4);
        Block.registerBlock(41, "gold_block", new BlockCompressed(MapColor.goldColor).setHardness(3.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockGold"));
        Block.registerBlock(42, "iron_block", new BlockCompressed(MapColor.ironColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockIron"));
        Block.registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block.registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block var5 = new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(45, "brick_block", var5);
        Block.registerBlock(46, "tnt", new BlockTNT().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("tnt"));
        Block.registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5f).setStepSound(soundTypeWood).setUnlocalizedName("bookshelf"));
        Block.registerBlock(48, "mossy_cobblestone", new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0f).setResistance(2000.0f).setStepSound(soundTypePiston).setUnlocalizedName("obsidian"));
        Block.registerBlock(50, "torch", new BlockTorch().setHardness(0.0f).setLightLevel(0.9375f).setStepSound(soundTypeWood).setUnlocalizedName("torch"));
        Block.registerBlock(51, "fire", new BlockFire().setHardness(0.0f).setLightLevel(1.0f).setStepSound(soundTypeCloth).setUnlocalizedName("fire").disableStats());
        Block.registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0f).setStepSound(soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
        Block.registerBlock(53, "oak_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));
        Block.registerBlock(54, "chest", new BlockChest(0).setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("chest"));
        Block.registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0f).setStepSound(soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
        Block.registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreDiamond"));
        Block.registerBlock(57, "diamond_block", new BlockCompressed(MapColor.diamondColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockDiamond"));
        Block.registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("workbench"));
        Block.registerBlock(59, "wheat", new BlockCrops().setUnlocalizedName("crops"));
        Block var6 = new BlockFarmland().setHardness(0.6f).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
        Block.registerBlock(60, "farmland", var6);
        Block.registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5f).setStepSound(soundTypePiston).setLightLevel(0.875f).setUnlocalizedName("furnace"));
        Block.registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(64, "wooden_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorOak").disableStats());
        Block.registerBlock(65, "ladder", new BlockLadder().setHardness(0.4f).setStepSound(soundTypeLadder).setUnlocalizedName("ladder"));
        Block.registerBlock(66, "rail", new BlockRail().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("rail"));
        Block.registerBlock(67, "stone_stairs", new BlockStairs(var0.getDefaultState()).setUnlocalizedName("stairsStone"));
        Block.registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(69, "lever", new BlockLever().setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("lever"));
        Block.registerBlock(70, "stone_pressure_plate", new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS).setHardness(0.5f).setStepSound(soundTypePiston).setUnlocalizedName("pressurePlateStone"));
        Block.registerBlock(71, "iron_door", new BlockDoor(Material.iron).setHardness(5.0f).setStepSound(soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
        Block.registerBlock(72, "wooden_pressure_plate", new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING).setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("pressurePlateWood"));
        Block.registerBlock(73, "redstone_ore", new BlockRedstoneOre(false).setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(74, "lit_redstone_ore", new BlockRedstoneOre(true).setLightLevel(0.625f).setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone"));
        Block.registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("notGate"));
        Block.registerBlock(76, "redstone_torch", new BlockRedstoneTorch(true).setHardness(0.0f).setLightLevel(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5f).setStepSound(soundTypePiston).setUnlocalizedName("button"));
        Block.registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1f).setStepSound(soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
        Block.registerBlock(79, "ice", new BlockIce().setHardness(0.5f).setLightOpacity(3).setStepSound(soundTypeGlass).setUnlocalizedName("ice"));
        Block.registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2f).setStepSound(soundTypeSnow).setUnlocalizedName("snow"));
        Block.registerBlock(81, "cactus", new BlockCactus().setHardness(0.4f).setStepSound(soundTypeCloth).setUnlocalizedName("cactus"));
        Block.registerBlock(82, "clay", new BlockClay().setHardness(0.6f).setStepSound(soundTypeGravel).setUnlocalizedName("clay"));
        Block.registerBlock(83, "reeds", new BlockReed().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("reeds").disableStats());
        Block.registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("jukebox"));
        Block.registerBlock(85, "fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("fence"));
        Block var7 = new BlockPumpkin().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkin");
        Block.registerBlock(86, "pumpkin", var7);
        Block.registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4f).setStepSound(soundTypePiston).setUnlocalizedName("hellrock"));
        Block.registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5f).setStepSound(soundTypeSand).setUnlocalizedName("hellsand"));
        Block.registerBlock(89, "glowstone", new BlockGlowstone(Material.glass).setHardness(0.3f).setStepSound(soundTypeGlass).setLightLevel(1.0f).setUnlocalizedName("lightgem"));
        Block.registerBlock(90, "portal", new BlockPortal().setHardness(-1.0f).setStepSound(soundTypeGlass).setLightLevel(0.75f).setUnlocalizedName("portal"));
        Block.registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0f).setStepSound(soundTypeWood).setLightLevel(1.0f).setUnlocalizedName("litpumpkin"));
        Block.registerBlock(92, "cake", new BlockCake().setHardness(0.5f).setStepSound(soundTypeCloth).setUnlocalizedName("cake").disableStats());
        Block.registerBlock(93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(95, "stained_glass", new BlockStainedGlass(Material.glass).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("stainedGlass"));
        Block.registerBlock(96, "trapdoor", new BlockTrapDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
        Block.registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75f).setUnlocalizedName("monsterStoneEgg"));
        Block var8 = new BlockStoneBrick().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stonebricksmooth");
        Block.registerBlock(98, "stonebrick", var8);
        Block.registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.wood, var3).setHardness(0.2f).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.wood, var4).setHardness(0.2f).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(101, "iron_bars", new BlockPane(Material.iron, true).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("fenceIron"));
        Block.registerBlock(102, "glass_pane", new BlockPane(Material.glass, false).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("thinGlass"));
        Block var9 = new BlockMelon().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("melon");
        Block.registerBlock(103, "melon_block", var9);
        Block.registerBlock(104, "pumpkin_stem", new BlockStem(var7).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(105, "melon_stem", new BlockStem(var9).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(106, "vine", new BlockVine().setHardness(0.2f).setStepSound(soundTypeGrass).setUnlocalizedName("vine"));
        Block.registerBlock(107, "fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("fenceGate"));
        Block.registerBlock(108, "brick_stairs", new BlockStairs(var5.getDefaultState()).setUnlocalizedName("stairsBrick"));
        Block.registerBlock(109, "stone_brick_stairs", new BlockStairs(var8.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, (Comparable)((Object)BlockStoneBrick.EnumType.DEFAULT))).setUnlocalizedName("stairsStoneBrickSmooth"));
        Block.registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6f).setStepSound(soundTypeGrass).setUnlocalizedName("mycel"));
        Block.registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("waterlily"));
        Block var10 = new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(112, "nether_brick", var10);
        Block.registerBlock(113, "nether_brick_fence", new BlockFence(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherFence"));
        Block.registerBlock(114, "nether_brick_stairs", new BlockStairs(var10.getDefaultState()).setUnlocalizedName("stairsNetherBrick"));
        Block.registerBlock(115, "nether_wart", new BlockNetherWart().setUnlocalizedName("netherStalk"));
        Block.registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0f).setResistance(2000.0f).setUnlocalizedName("enchantmentTable"));
        Block.registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5f).setLightLevel(0.125f).setUnlocalizedName("brewingStand"));
        Block.registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0f).setUnlocalizedName("cauldron"));
        Block.registerBlock(119, "end_portal", new BlockEndPortal(Material.portal).setHardness(-1.0f).setResistance(6000000.0f));
        Block.registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setStepSound(soundTypeGlass).setLightLevel(0.125f).setHardness(-1.0f).setUnlocalizedName("endPortalFrame").setResistance(6000000.0f).setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(121, "end_stone", new Block(Material.rock).setHardness(3.0f).setResistance(15.0f).setStepSound(soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0f).setResistance(15.0f).setStepSound(soundTypePiston).setLightLevel(0.125f).setUnlocalizedName("dragonEgg"));
        Block.registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight"));
        Block.registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("cocoa"));
        Block.registerBlock(128, "sandstone_stairs", new BlockStairs(var2.getDefaultState().withProperty(BlockSandStone.field_176297_a, (Comparable)((Object)BlockSandStone.EnumType.SMOOTH))).setUnlocalizedName("stairsSandStone"));
        Block.registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreEmerald"));
        Block.registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5f).setResistance(1000.0f).setStepSound(soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5f));
        Block.registerBlock(131, "tripwire_hook", new BlockTripWireHook().setUnlocalizedName("tripWireSource"));
        Block.registerBlock(132, "tripwire", new BlockTripWire().setUnlocalizedName("tripWire"));
        Block.registerBlock(133, "emerald_block", new BlockCompressed(MapColor.emeraldColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockEmerald"));
        Block.registerBlock(134, "spruce_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.SPRUCE))).setUnlocalizedName("stairsWoodSpruce"));
        Block.registerBlock(135, "birch_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.BIRCH))).setUnlocalizedName("stairsWoodBirch"));
        Block.registerBlock(136, "jungle_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.JUNGLE))).setUnlocalizedName("stairsWoodJungle"));
        Block.registerBlock(137, "command_block", new BlockCommandBlock().setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("commandBlock"));
        Block.registerBlock(138, "beacon", new BlockBeacon().setUnlocalizedName("beacon").setLightLevel(1.0f));
        Block.registerBlock(139, "cobblestone_wall", new BlockWall(var0).setUnlocalizedName("cobbleWall"));
        Block.registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0f).setStepSound(soundTypeStone).setUnlocalizedName("flowerPot"));
        Block.registerBlock(141, "carrots", new BlockCarrot().setUnlocalizedName("carrots"));
        Block.registerBlock(142, "potatoes", new BlockPotato().setUnlocalizedName("potatoes"));
        Block.registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("button"));
        Block.registerBlock(144, "skull", new BlockSkull().setHardness(1.0f).setStepSound(soundTypePiston).setUnlocalizedName("skull"));
        Block.registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0f).setStepSound(soundTypeAnvil).setResistance(2000.0f).setUnlocalizedName("anvil"));
        Block.registerBlock(146, "trapped_chest", new BlockChest(1).setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("chestTrap"));
        Block.registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted("gold_block", Material.iron, 15).setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_light"));
        Block.registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted("iron_block", Material.iron, 150).setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
        Block.registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0f).setLightLevel(0.625f).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(151, "daylight_detector", (Block)new BlockDaylightDetector(false));
        Block.registerBlock(152, "redstone_block", new BlockCompressedPowered(MapColor.tntColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockRedstone"));
        Block.registerBlock(153, "quartz_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherquartz"));
        Block.registerBlock(154, "hopper", new BlockHopper().setHardness(3.0f).setResistance(8.0f).setStepSound(soundTypeMetal).setUnlocalizedName("hopper"));
        Block var11 = new BlockQuartz().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("quartzBlock");
        Block.registerBlock(155, "quartz_block", var11);
        Block.registerBlock(156, "quartz_stairs", new BlockStairs(var11.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, (Comparable)((Object)BlockQuartz.EnumType.DEFAULT))).setUnlocalizedName("stairsQuartz"));
        Block.registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("activatorRail"));
        Block.registerBlock(158, "dropper", new BlockDropper().setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("dropper"));
        Block.registerBlock(159, "stained_hardened_clay", new BlockColored(Material.rock).setHardness(1.25f).setResistance(7.0f).setStepSound(soundTypePiston).setUnlocalizedName("clayHardenedStained"));
        Block.registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
        Block.registerBlock(161, "leaves2", new BlockNewLeaf().setUnlocalizedName("leaves"));
        Block.registerBlock(162, "log2", new BlockNewLog().setUnlocalizedName("log"));
        Block.registerBlock(163, "acacia_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.ACACIA))).setUnlocalizedName("stairsWoodAcacia"));
        Block.registerBlock(164, "dark_oak_stairs", new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, (Comparable)((Object)BlockPlanks.EnumType.DARK_OAK))).setUnlocalizedName("stairsWoodDarkOak"));
        Block.registerBlock(165, "slime", new BlockSlime().setUnlocalizedName("slime").setStepSound(SLIME_SOUND));
        Block.registerBlock(166, "barrier", new BlockBarrier().setUnlocalizedName("barrier"));
        Block.registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.iron).setHardness(5.0f).setStepSound(soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
        Block.registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("prismarine"));
        Block.registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.glass).setHardness(0.3f).setStepSound(soundTypeGlass).setLightLevel(1.0f).setUnlocalizedName("seaLantern"));
        Block.registerBlock(170, "hay_block", new BlockHay().setHardness(0.5f).setStepSound(soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1f).setStepSound(soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        Block.registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25f).setResistance(7.0f).setStepSound(soundTypePiston).setUnlocalizedName("clayHardened"));
        Block.registerBlock(173, "coal_block", new Block(Material.rock).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5f).setStepSound(soundTypeGlass).setUnlocalizedName("icePacked"));
        Block.registerBlock(175, "double_plant", (Block)new BlockDoublePlant());
        Block.registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(178, "daylight_detector_inverted", (Block)new BlockDaylightDetector(true));
        Block var12 = new BlockRedSandstone().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("redSandStone");
        Block.registerBlock(179, "red_sandstone", var12);
        Block.registerBlock(180, "red_sandstone_stairs", new BlockStairs(var12.getDefaultState().withProperty(BlockRedSandstone.TYPE, (Comparable)((Object)BlockRedSandstone.EnumType.SMOOTH))).setUnlocalizedName("stairsRedSandStone"));
        Block.registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(183, "spruce_fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("spruceFenceGate"));
        Block.registerBlock(184, "birch_fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("birchFenceGate"));
        Block.registerBlock(185, "jungle_fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("jungleFenceGate"));
        Block.registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
        Block.registerBlock(187, "acacia_fence_gate", new BlockFenceGate().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
        Block.registerBlock(188, "spruce_fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("spruceFence"));
        Block.registerBlock(189, "birch_fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("birchFence"));
        Block.registerBlock(190, "jungle_fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("jungleFence"));
        Block.registerBlock(191, "dark_oak_fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFence"));
        Block.registerBlock(192, "acacia_fence", new BlockFence(Material.wood).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFence"));
        Block.registerBlock(193, "spruce_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
        Block.registerBlock(194, "birch_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
        Block.registerBlock(195, "jungle_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
        Block.registerBlock(196, "acacia_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
        Block.registerBlock(197, "dark_oak_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
        blockRegistry.validateKey();
        for (Block var14 : blockRegistry) {
            boolean var20;
            if (var14.blockMaterial == Material.air) {
                var14.useNeighborBrightness = false;
                continue;
            }
            boolean var15 = false;
            boolean var16 = var14 instanceof BlockStairs;
            boolean var17 = var14 instanceof BlockSlab;
            boolean var18 = var14 == var6;
            boolean var19 = var14.translucent;
            boolean bl = var20 = var14.lightOpacity == 0;
            if (var16 || var17 || var18 || var19 || var20) {
                var15 = true;
            }
            var14.useNeighborBrightness = var15;
        }
        for (Block var14 : blockRegistry) {
            for (IBlockState var22 : var14.getBlockState().getValidStates()) {
                int var23 = blockRegistry.getIDForObject(var14) << 4 | var14.getMetaFromState(var22);
                BLOCK_STATE_IDS.put(var22, var23);
            }
        }
    }

    private static void registerBlock(int id, ResourceLocation textualID, Block block_) {
        blockRegistry.register(id, textualID, block_);
    }

    private static void registerBlock(int id, String textualID, Block block_) {
        Block.registerBlock(id, new ResourceLocation(textualID), block_);
    }

    public static enum EnumOffsetType {
        NONE("NONE", 0),
        XZ("XZ", 1),
        XYZ("XYZ", 2);

        private static final EnumOffsetType[] $VALUES;
        private static final String __OBFID = "CL_00002132";

        static {
            $VALUES = new EnumOffsetType[]{NONE, XZ, XYZ};
        }

        private EnumOffsetType(String p_i45733_1_, int p_i45733_2_) {
        }
    }

    public static class SoundType {
        public final String soundName;
        public final float volume;
        public final float frequency;
        private static final String __OBFID = "CL_00000203";

        public SoundType(String name, float volume, float frequency) {
            this.soundName = name;
            this.volume = volume;
            this.frequency = frequency;
        }

        public float getVolume() {
            return this.volume;
        }

        public float getFrequency() {
            return this.frequency;
        }

        public String getBreakSound() {
            return "dig." + this.soundName;
        }

        public String getStepSound() {
            return "step." + this.soundName;
        }

        public String getPlaceSound() {
            return this.getBreakSound();
        }
    }
}

