/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockBone;
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
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockChorusPlant;
import net.minecraft.block.BlockClay;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.BlockConcretePowder;
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
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEndGateway;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockEndRod;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockFrostedIce;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGlazedTerracotta;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGrassPath;
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
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockMagma;
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
import net.minecraft.block.BlockObserver;
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
import net.minecraft.block.BlockPurpurSlab;
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
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSeaLantern;
import net.minecraft.block.BlockShulkerBox;
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
import net.minecraft.block.BlockStainedHardenedClay;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.BlockStructureVoid;
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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.player.EventCollide;
import org.celestial.client.event.events.impl.player.EventFullCube;
import org.celestial.client.feature.impl.misc.Baritone;
import org.celestial.client.feature.impl.player.AntiPush;
import org.celestial.client.feature.impl.player.ClipHelper;
import org.celestial.client.feature.impl.player.NoInteract;

public class Block {
    private static final ResourceLocation AIR_ID = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> REGISTRY = new RegistryNamespacedDefaultedByKey(AIR_ID);
    public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS = new ObjectIntIdentityMap();
    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    @Nullable
    public static final AxisAlignedBB NULL_AABB = null;
    private CreativeTabs displayOnCreativeTab;
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
    protected SoundType blockSoundType = SoundType.STONE;
    public float blockParticleGravity = 1.0f;
    protected final Material blockMaterial;
    protected final MapColor blockMapColor;
    public float slipperiness = 0.6f;
    protected final BlockStateContainer blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;

    public static int getIdFromBlock(Block blockIn) {
        return REGISTRY.getIDForObject(blockIn);
    }

    public static int getStateId(IBlockState state) {
        Block block = state.getBlock();
        return Block.getIdFromBlock(block) + (block.getMetaFromState(state) << 12);
    }

    public static Block getBlockById(int id) {
        return REGISTRY.getObjectById(id);
    }

    public static IBlockState getStateById(int id) {
        int i = id & 0xFFF;
        int j = id >> 12 & 0xF;
        return Block.getBlockById(i).getStateFromMeta(j);
    }

    public static Block getBlockFromItem(@Nullable Item itemIn) {
        return itemIn instanceof ItemBlock ? ((ItemBlock)itemIn).getBlock() : Blocks.AIR;
    }

    @Nullable
    public static Block getBlockFromName(String name) {
        ResourceLocation resourcelocation = new ResourceLocation(name);
        if (REGISTRY.containsKey(resourcelocation)) {
            return REGISTRY.getObject(resourcelocation);
        }
        try {
            return REGISTRY.getObjectById(Integer.parseInt(name));
        }
        catch (NumberFormatException var3) {
            return null;
        }
    }

    @Deprecated
    public boolean isFullyOpaque(IBlockState state) {
        return state.getMaterial().isOpaque() && state.isFullCube();
    }

    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return this.fullBlock;
    }

    @Deprecated
    public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
        return true;
    }

    @Deprecated
    public int getLightOpacity(IBlockState state) {
        return this.lightOpacity;
    }

    @Deprecated
    public boolean isTranslucent(IBlockState state) {
        return this.translucent;
    }

    @Deprecated
    public int getLightValue(IBlockState state) {
        return this.lightValue;
    }

    @Deprecated
    public boolean getUseNeighborBrightness(IBlockState state) {
        return this.useNeighborBrightness;
    }

    @Deprecated
    public Material getMaterial(IBlockState state) {
        return this.blockMaterial;
    }

    @Deprecated
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        return this.blockMapColor;
    }

    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        if (state.getPropertyKeys().isEmpty()) {
            return 0;
        }
        throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
    }

    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state;
    }

    @Deprecated
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state;
    }

    public Block(Material blockMaterialIn, MapColor blockMapColorIn) {
        this.blockMaterial = blockMaterialIn;
        this.blockMapColor = blockMapColorIn;
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
        this.fullBlock = this.getDefaultState().isOpaqueCube();
        this.lightOpacity = this.fullBlock ? 255 : 0;
        this.translucent = !blockMaterialIn.blocksLight();
    }

    protected Block(Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    protected Block setSoundType(SoundType sound) {
        this.blockSoundType = sound;
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

    protected static boolean func_193384_b(Block p_193384_0_) {
        return p_193384_0_ instanceof BlockShulkerBox || p_193384_0_ instanceof BlockLeaves || p_193384_0_ instanceof BlockTrapDoor || p_193384_0_ == Blocks.BEACON || p_193384_0_ == Blocks.CAULDRON || p_193384_0_ == Blocks.GLASS || p_193384_0_ == Blocks.GLOWSTONE || p_193384_0_ == Blocks.ICE || p_193384_0_ == Blocks.SEA_LANTERN || p_193384_0_ == Blocks.STAINED_GLASS;
    }

    protected static boolean func_193382_c(Block p_193382_0_) {
        return Block.func_193384_b(p_193382_0_) || p_193382_0_ == Blocks.PISTON || p_193382_0_ == Blocks.STICKY_PISTON || p_193382_0_ == Blocks.PISTON_HEAD;
    }

    @Deprecated
    public boolean isBlockNormalCube(IBlockState state) {
        return state.getMaterial().blocksMovement() && state.isFullCube();
    }

    @Deprecated
    public boolean isNormalCube(IBlockState state) {
        return state.getMaterial().isOpaque() && state.isFullCube() && !state.canProvidePower();
    }

    @Deprecated
    public boolean causesSuffocation(IBlockState p_176214_1_) {
        if (Celestial.instance.featureManager.getFeatureByClass(AntiPush.class).getState() && AntiPush.blocks.getCurrentValue()) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(ClipHelper.class).getState() && ClipHelper.disableBlockLight.getCurrentValue() && !Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return false;
        }
        return this.blockMaterial.blocksMovement() && this.getDefaultState().isFullCube();
    }

    @Deprecated
    public boolean isFullCube(IBlockState state) {
        EventFullCube eventFullCube = new EventFullCube();
        EventManager.call(eventFullCube);
        return !eventFullCube.isCancelled();
    }

    @Deprecated
    public boolean func_190946_v(IBlockState p_190946_1_) {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return !this.blockMaterial.blocksMovement();
    }

    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
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

    @Deprecated
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
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

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Deprecated
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        int i = source.getCombinedLight(pos, state.getLightValue());
        if (i == 0 && state.getBlock() instanceof BlockSlab) {
            pos = pos.down();
            state = source.getBlockState(pos);
            return source.getCombinedLight(pos, state.getLightValue());
        }
        return i;
    }

    @Deprecated
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(blockAccess, pos);
        switch (side) {
            case DOWN: {
                if (!(axisalignedbb.minY > 0.0)) break;
                return true;
            }
            case UP: {
                if (!(axisalignedbb.maxY < 1.0)) break;
                return true;
            }
            case NORTH: {
                if (!(axisalignedbb.minZ > 0.0)) break;
                return true;
            }
            case SOUTH: {
                if (!(axisalignedbb.maxZ < 1.0)) break;
                return true;
            }
            case WEST: {
                if (!(axisalignedbb.minX > 0.0)) break;
                return true;
            }
            case EAST: {
                if (!(axisalignedbb.maxX < 1.0)) break;
                return true;
            }
        }
        return !blockAccess.getBlockState(pos.offset(side)).isOpaqueCube();
    }

    @Deprecated
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.SOLID;
    }

    @Deprecated
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return state.getBoundingBox(worldIn, pos).offset(pos);
    }

    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        EventCollide eventCollide = new EventCollide(this.getCollisionBoundingBox(state, worldIn, pos), state.getBlock(), pos.getX(), pos.getY(), pos.getZ());
        EventManager.call(eventCollide);
        if (!eventCollide.isCancelled()) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
        }
    }

    protected static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox) {
        AxisAlignedBB axisalignedbb;
        if (blockBox != NULL_AABB && entityBox.intersectsWith(axisalignedbb = blockBox.offset(pos))) {
            collidingBoxes.add(axisalignedbb);
        }
    }

    @Deprecated
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getBoundingBox(worldIn, pos);
    }

    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        for (Block block : Objects.requireNonNull(NoInteract.getRightClickableBlocks())) {
            if (state.getBlock() != block || !Celestial.instance.featureManager.getFeatureByClass(NoInteract.class).getState()) continue;
            return false;
        }
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

    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
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

    @Deprecated
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
        float f = state.getBlockHardness(worldIn, pos);
        if (f < 0.0f) {
            return 0.0f;
        }
        return !player.canHarvestBlock(state) ? player.getDigSpeed(state) / f / 100.0f : player.getDigSpeed(state) / f / 30.0f;
    }

    public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int fortune) {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0f, fortune);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int i = this.quantityDroppedWithBonus(fortune, worldIn.rand);
            for (int j = 0; j < i; ++j) {
                Item item;
                if (!(worldIn.rand.nextFloat() <= chance) || (item = this.getItemDropped(state, worldIn.rand, fortune)) == Items.field_190931_a) continue;
                Block.spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
            }
        }
    }

    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean("doTileDrops")) {
            float f = 0.5f;
            double d0 = (double)(worldIn.rand.nextFloat() * 0.5f) + 0.25;
            double d1 = (double)(worldIn.rand.nextFloat() * 0.5f) + 0.25;
            double d2 = (double)(worldIn.rand.nextFloat() * 0.5f) + 0.25;
            EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(entityitem);
        }
    }

    protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
        if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
            while (amount > 0) {
                int i = EntityXPOrb.getXPSplit(amount);
                amount -= i;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, i));
            }
        }
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }

    public float getExplosionResistance(Entity exploder) {
        return this.blockResistance / 5.0f;
    }

    @Deprecated
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return this.rayTrace(pos, start, end, blockState.getBoundingBox(worldIn, pos));
    }

    @Nullable
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        Vec3d vec3d1;
        Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ()));
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.add(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial.isReplaceable();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        return false;
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    }

    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return motion;
    }

    @Deprecated
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 0;
    }

    @Deprecated
    public boolean canProvidePower(IBlockState state) {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    }

    @Deprecated
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 0;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005f);
        if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            ItemStack itemstack = this.getSilkTouchDrop(state);
            Block.spawnAsEntity(worldIn, pos, itemstack);
        } else {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItem(worldIn, pos, state, i);
        }
    }

    protected boolean canSilkHarvest() {
        return this.getDefaultState().isFullCube() && !this.isBlockContainer;
    }

    protected ItemStack getSilkTouchDrop(IBlockState state) {
        Item item = Item.getItemFromBlock(this);
        int i = 0;
        if (item.getHasSubtypes()) {
            i = this.getMetaFromState(state);
        }
        return new ItemStack(item, 1, i);
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    }

    public boolean canSpawnInBlock() {
        return !this.blockMaterial.isSolid() && !this.blockMaterial.isLiquid();
    }

    public Block setUnlocalizedName(String name) {
        this.unlocalizedName = name;
        return this;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
    }

    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedName;
    }

    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        return false;
    }

    public boolean getEnableStats() {
        return this.enableStats;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return this.blockMaterial.getMobilityFlag();
    }

    @Deprecated
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return state.isBlockNormalCube() ? 0.2f : 1.0f;
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.fall(fallDistance, 1.0f);
    }

    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.motionY = 0.0;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        tab.add(new ItemStack(this));
    }

    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs tab) {
        this.displayOnCreativeTab = tab;
        return this;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    }

    public void fillWithRain(World worldIn, BlockPos pos) {
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
        if (blockIn != null && other != null) {
            return blockIn == other ? true : blockIn.isAssociatedBlock(other);
        }
        return false;
    }

    @Deprecated
    public boolean hasComparatorInputOverride(IBlockState state) {
        return false;
    }

    @Deprecated
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[0]);
    }

    public BlockStateContainer getBlockState() {
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

    @Deprecated
    public Vec3d func_190949_e(IBlockState p_190949_1_, IBlockAccess p_190949_2_, BlockPos p_190949_3_) {
        EnumOffsetType block$enumoffsettype = this.getOffsetType();
        if (block$enumoffsettype == EnumOffsetType.NONE) {
            return Vec3d.ZERO;
        }
        long i = MathHelper.getCoordinateRandom(p_190949_3_.getX(), 0, p_190949_3_.getZ());
        return new Vec3d(((double)((float)(i >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5, block$enumoffsettype == EnumOffsetType.XYZ ? ((double)((float)(i >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2 : 0.0, ((double)((float)(i >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5);
    }

    public SoundType getSoundType() {
        return this.blockSoundType;
    }

    public String toString() {
        return "Block{" + REGISTRY.getNameForObject(this) + "}";
    }

    public void func_190948_a(ItemStack p_190948_1_, @Nullable World p_190948_2_, List<String> p_190948_3_, ITooltipFlag p_190948_4_) {
    }

    public static void registerBlocks() {
        Block.registerBlock(0, AIR_ID, new BlockAir().setUnlocalizedName("air"));
        Block.registerBlock(1, "stone", new BlockStone().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stone"));
        Block.registerBlock(2, "grass", new BlockGrass().setHardness(0.6f).setSoundType(SoundType.PLANT).setUnlocalizedName("grass"));
        Block.registerBlock(3, "dirt", new BlockDirt().setHardness(0.5f).setSoundType(SoundType.GROUND).setUnlocalizedName("dirt"));
        Block block = new Block(Material.ROCK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        Block.registerBlock(4, "cobblestone", block);
        Block block1 = new BlockPlanks().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("wood");
        Block.registerBlock(5, "planks", block1);
        Block.registerBlock(6, "sapling", new BlockSapling().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("sapling"));
        Block.registerBlock(7, "bedrock", new BlockEmptyDrops(Material.ROCK).setBlockUnbreakable().setResistance(6000000.0f).setSoundType(SoundType.STONE).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.WATER).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(9, "water", new BlockStaticLiquid(Material.WATER).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.LAVA).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(11, "lava", new BlockStaticLiquid(Material.LAVA).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(12, "sand", new BlockSand().setHardness(0.5f).setSoundType(SoundType.SAND).setUnlocalizedName("sand"));
        Block.registerBlock(13, "gravel", new BlockGravel().setHardness(0.6f).setSoundType(SoundType.GROUND).setUnlocalizedName("gravel"));
        Block.registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreGold"));
        Block.registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreIron"));
        Block.registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreCoal"));
        Block.registerBlock(17, "log", new BlockOldLog().setUnlocalizedName("log"));
        Block.registerBlock(18, "leaves", new BlockOldLeaf().setUnlocalizedName("leaves"));
        Block.registerBlock(19, "sponge", new BlockSponge().setHardness(0.6f).setSoundType(SoundType.PLANT).setUnlocalizedName("sponge"));
        Block.registerBlock(20, "glass", new BlockGlass(Material.GLASS, false).setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("glass"));
        Block.registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreLapis"));
        Block.registerBlock(22, "lapis_block", new Block(Material.IRON, MapColor.LAPIS).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5f).setSoundType(SoundType.STONE).setUnlocalizedName("dispenser"));
        Block block2 = new BlockSandStone().setSoundType(SoundType.STONE).setHardness(0.8f).setUnlocalizedName("sandStone");
        Block.registerBlock(24, "sandstone", block2);
        Block.registerBlock(25, "noteblock", new BlockNote().setSoundType(SoundType.WOOD).setHardness(0.8f).setUnlocalizedName("musicBlock"));
        Block.registerBlock(26, "bed", new BlockBed().setSoundType(SoundType.WOOD).setHardness(0.2f).setUnlocalizedName("bed").disableStats());
        Block.registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7f).setSoundType(SoundType.METAL).setUnlocalizedName("goldenRail"));
        Block.registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7f).setSoundType(SoundType.METAL).setUnlocalizedName("detectorRail"));
        Block.registerBlock(29, "sticky_piston", new BlockPistonBase(true).setUnlocalizedName("pistonStickyBase"));
        Block.registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0f).setUnlocalizedName("web"));
        Block.registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("tallgrass"));
        Block.registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("deadbush"));
        Block.registerBlock(33, "piston", new BlockPistonBase(false).setUnlocalizedName("pistonBase"));
        Block.registerBlock(34, "piston_head", new BlockPistonExtension().setUnlocalizedName("pistonBase"));
        Block.registerBlock(35, "wool", new BlockColored(Material.CLOTH).setHardness(0.8f).setSoundType(SoundType.CLOTH).setUnlocalizedName("cloth"));
        Block.registerBlock(36, "piston_extension", (Block)new BlockPistonMoving());
        Block.registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("flower1"));
        Block.registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("flower2"));
        Block block3 = new BlockMushroom().setHardness(0.0f).setSoundType(SoundType.PLANT).setLightLevel(0.125f).setUnlocalizedName("mushroom");
        Block.registerBlock(39, "brown_mushroom", block3);
        Block block4 = new BlockMushroom().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("mushroom");
        Block.registerBlock(40, "red_mushroom", block4);
        Block.registerBlock(41, "gold_block", new Block(Material.IRON, MapColor.GOLD).setHardness(3.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(42, "iron_block", new Block(Material.IRON, MapColor.IRON).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab"));
        Block.registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab"));
        Block block5 = new Block(Material.ROCK, MapColor.RED).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        Block.registerBlock(45, "brick_block", block5);
        Block.registerBlock(46, "tnt", new BlockTNT().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("tnt"));
        Block.registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("bookshelf"));
        Block.registerBlock(48, "mossy_cobblestone", new Block(Material.ROCK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0f).setResistance(2000.0f).setSoundType(SoundType.STONE).setUnlocalizedName("obsidian"));
        Block.registerBlock(50, "torch", new BlockTorch().setHardness(0.0f).setLightLevel(0.9375f).setSoundType(SoundType.WOOD).setUnlocalizedName("torch"));
        Block.registerBlock(51, "fire", new BlockFire().setHardness(0.0f).setLightLevel(1.0f).setSoundType(SoundType.CLOTH).setUnlocalizedName("fire").disableStats());
        Block.registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0f).setSoundType(SoundType.METAL).setUnlocalizedName("mobSpawner").disableStats());
        Block.registerBlock(53, "oak_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setUnlocalizedName("stairsWood"));
        Block.registerBlock(54, "chest", new BlockChest(BlockChest.Type.BASIC).setHardness(2.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("chest"));
        Block.registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0f).setSoundType(SoundType.STONE).setUnlocalizedName("redstoneDust").disableStats());
        Block.registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreDiamond"));
        Block.registerBlock(57, "diamond_block", new Block(Material.IRON, MapColor.DIAMOND).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("workbench"));
        Block.registerBlock(59, "wheat", new BlockCrops().setUnlocalizedName("crops"));
        Block block6 = new BlockFarmland().setHardness(0.6f).setSoundType(SoundType.GROUND).setUnlocalizedName("farmland");
        Block.registerBlock(60, "farmland", block6);
        Block.registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5f).setSoundType(SoundType.STONE).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.DECORATIONS));
        Block.registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5f).setSoundType(SoundType.STONE).setLightLevel(0.875f).setUnlocalizedName("furnace"));
        Block.registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(64, "wooden_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorOak").disableStats());
        Block.registerBlock(65, "ladder", new BlockLadder().setHardness(0.4f).setSoundType(SoundType.LADDER).setUnlocalizedName("ladder"));
        Block.registerBlock(66, "rail", new BlockRail().setHardness(0.7f).setSoundType(SoundType.METAL).setUnlocalizedName("rail"));
        Block.registerBlock(67, "stone_stairs", new BlockStairs(block.getDefaultState()).setUnlocalizedName("stairsStone"));
        Block.registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(69, "lever", new BlockLever().setHardness(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("lever"));
        Block.registerBlock(70, "stone_pressure_plate", new BlockPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS).setHardness(0.5f).setSoundType(SoundType.STONE).setUnlocalizedName("pressurePlateStone"));
        Block.registerBlock(71, "iron_door", new BlockDoor(Material.IRON).setHardness(5.0f).setSoundType(SoundType.METAL).setUnlocalizedName("doorIron").disableStats());
        Block.registerBlock(72, "wooden_pressure_plate", new BlockPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING).setHardness(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("pressurePlateWood"));
        Block.registerBlock(73, "redstone_ore", new BlockRedstoneOre(false).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(74, "lit_redstone_ore", new BlockRedstoneOre(true).setLightLevel(0.625f).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreRedstone"));
        Block.registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("notGate"));
        Block.registerBlock(76, "redstone_torch", new BlockRedstoneTorch(true).setHardness(0.0f).setLightLevel(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.REDSTONE));
        Block.registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5f).setSoundType(SoundType.STONE).setUnlocalizedName("button"));
        Block.registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1f).setSoundType(SoundType.SNOW).setUnlocalizedName("snow").setLightOpacity(0));
        Block.registerBlock(79, "ice", new BlockIce().setHardness(0.5f).setLightOpacity(3).setSoundType(SoundType.GLASS).setUnlocalizedName("ice"));
        Block.registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2f).setSoundType(SoundType.SNOW).setUnlocalizedName("snow"));
        Block.registerBlock(81, "cactus", new BlockCactus().setHardness(0.4f).setSoundType(SoundType.CLOTH).setUnlocalizedName("cactus"));
        Block.registerBlock(82, "clay", new BlockClay().setHardness(0.6f).setSoundType(SoundType.GROUND).setUnlocalizedName("clay"));
        Block.registerBlock(83, "reeds", new BlockReed().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("reeds").disableStats());
        Block.registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("jukebox"));
        Block.registerBlock(85, "fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("fence"));
        Block block7 = new BlockPumpkin().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkin");
        Block.registerBlock(86, "pumpkin", block7);
        Block.registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4f).setSoundType(SoundType.STONE).setUnlocalizedName("hellrock"));
        Block.registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5f).setSoundType(SoundType.SAND).setUnlocalizedName("hellsand"));
        Block.registerBlock(89, "glowstone", new BlockGlowstone(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setLightLevel(1.0f).setUnlocalizedName("lightgem"));
        Block.registerBlock(90, "portal", new BlockPortal().setHardness(-1.0f).setSoundType(SoundType.GLASS).setLightLevel(0.75f).setUnlocalizedName("portal"));
        Block.registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0f).setSoundType(SoundType.WOOD).setLightLevel(1.0f).setUnlocalizedName("litpumpkin"));
        Block.registerBlock(92, "cake", new BlockCake().setHardness(0.5f).setSoundType(SoundType.CLOTH).setUnlocalizedName("cake").disableStats());
        Block.registerBlock(93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(95, "stained_glass", new BlockStainedGlass(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("stainedGlass"));
        Block.registerBlock(96, "trapdoor", new BlockTrapDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("trapdoor").disableStats());
        Block.registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75f).setUnlocalizedName("monsterStoneEgg"));
        Block block8 = new BlockStoneBrick().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stonebricksmooth");
        Block.registerBlock(98, "stonebrick", block8);
        Block.registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.WOOD, MapColor.DIRT, block3).setHardness(0.2f).setSoundType(SoundType.WOOD).setUnlocalizedName("mushroom"));
        Block.registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.WOOD, MapColor.RED, block4).setHardness(0.2f).setSoundType(SoundType.WOOD).setUnlocalizedName("mushroom"));
        Block.registerBlock(101, "iron_bars", new BlockPane(Material.IRON, true).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("fenceIron"));
        Block.registerBlock(102, "glass_pane", new BlockPane(Material.GLASS, false).setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("thinGlass"));
        Block block9 = new BlockMelon().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("melon");
        Block.registerBlock(103, "melon_block", block9);
        Block.registerBlock(104, "pumpkin_stem", new BlockStem(block7).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(105, "melon_stem", new BlockStem(block9).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(106, "vine", new BlockVine().setHardness(0.2f).setSoundType(SoundType.PLANT).setUnlocalizedName("vine"));
        Block.registerBlock(107, "fence_gate", new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("fenceGate"));
        Block.registerBlock(108, "brick_stairs", new BlockStairs(block5.getDefaultState()).setUnlocalizedName("stairsBrick"));
        Block.registerBlock(109, "stone_brick_stairs", new BlockStairs(block8.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)).setUnlocalizedName("stairsStoneBrickSmooth"));
        Block.registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6f).setSoundType(SoundType.PLANT).setUnlocalizedName("mycel"));
        Block.registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0f).setSoundType(SoundType.PLANT).setUnlocalizedName("waterlily"));
        Block block10 = new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        Block.registerBlock(112, "nether_brick", block10);
        Block.registerBlock(113, "nether_brick_fence", new BlockFence(Material.ROCK, MapColor.NETHERRACK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("netherFence"));
        Block.registerBlock(114, "nether_brick_stairs", new BlockStairs(block10.getDefaultState()).setUnlocalizedName("stairsNetherBrick"));
        Block.registerBlock(115, "nether_wart", new BlockNetherWart().setUnlocalizedName("netherStalk"));
        Block.registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0f).setResistance(2000.0f).setUnlocalizedName("enchantmentTable"));
        Block.registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5f).setLightLevel(0.125f).setUnlocalizedName("brewingStand"));
        Block.registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0f).setUnlocalizedName("cauldron"));
        Block.registerBlock(119, "end_portal", new BlockEndPortal(Material.PORTAL).setHardness(-1.0f).setResistance(6000000.0f));
        Block.registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setSoundType(SoundType.GLASS).setLightLevel(0.125f).setHardness(-1.0f).setUnlocalizedName("endPortalFrame").setResistance(6000000.0f).setCreativeTab(CreativeTabs.DECORATIONS));
        Block.registerBlock(121, "end_stone", new Block(Material.ROCK, MapColor.SAND).setHardness(3.0f).setResistance(15.0f).setSoundType(SoundType.STONE).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0f).setResistance(15.0f).setSoundType(SoundType.STONE).setLightLevel(0.125f).setUnlocalizedName("dragonEgg"));
        Block.registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.REDSTONE));
        Block.registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("redstoneLight"));
        Block.registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("woodSlab"));
        Block.registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("woodSlab"));
        Block.registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("cocoa"));
        Block.registerBlock(128, "sandstone_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setUnlocalizedName("stairsSandStone"));
        Block.registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("oreEmerald"));
        Block.registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5f).setResistance(1000.0f).setSoundType(SoundType.STONE).setUnlocalizedName("enderChest").setLightLevel(0.5f));
        Block.registerBlock(131, "tripwire_hook", new BlockTripWireHook().setUnlocalizedName("tripWireSource"));
        Block.registerBlock(132, "tripwire", new BlockTripWire().setUnlocalizedName("tripWire"));
        Block.registerBlock(133, "emerald_block", new Block(Material.IRON, MapColor.EMERALD).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(134, "spruce_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setUnlocalizedName("stairsWoodSpruce"));
        Block.registerBlock(135, "birch_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setUnlocalizedName("stairsWoodBirch"));
        Block.registerBlock(136, "jungle_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setUnlocalizedName("stairsWoodJungle"));
        Block.registerBlock(137, "command_block", new BlockCommandBlock(MapColor.BROWN).setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("commandBlock"));
        Block.registerBlock(138, "beacon", new BlockBeacon().setUnlocalizedName("beacon").setLightLevel(1.0f));
        Block.registerBlock(139, "cobblestone_wall", new BlockWall(block).setUnlocalizedName("cobbleWall"));
        Block.registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0f).setSoundType(SoundType.STONE).setUnlocalizedName("flowerPot"));
        Block.registerBlock(141, "carrots", new BlockCarrot().setUnlocalizedName("carrots"));
        Block.registerBlock(142, "potatoes", new BlockPotato().setUnlocalizedName("potatoes"));
        Block.registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("button"));
        Block.registerBlock(144, "skull", new BlockSkull().setHardness(1.0f).setSoundType(SoundType.STONE).setUnlocalizedName("skull"));
        Block.registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0f).setSoundType(SoundType.ANVIL).setResistance(2000.0f).setUnlocalizedName("anvil"));
        Block.registerBlock(146, "trapped_chest", new BlockChest(BlockChest.Type.TRAP).setHardness(2.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("chestTrap"));
        Block.registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.IRON, 15, MapColor.GOLD).setHardness(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("weightedPlate_light"));
        Block.registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.IRON, 150).setHardness(0.5f).setSoundType(SoundType.WOOD).setUnlocalizedName("weightedPlate_heavy"));
        Block.registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0f).setLightLevel(0.625f).setSoundType(SoundType.WOOD).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(151, "daylight_detector", (Block)new BlockDaylightDetector(false));
        Block.registerBlock(152, "redstone_block", new BlockCompressedPowered(Material.IRON, MapColor.TNT).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.REDSTONE));
        Block.registerBlock(153, "quartz_ore", new BlockOre(MapColor.NETHERRACK).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setUnlocalizedName("netherquartz"));
        Block.registerBlock(154, "hopper", new BlockHopper().setHardness(3.0f).setResistance(8.0f).setSoundType(SoundType.METAL).setUnlocalizedName("hopper"));
        Block block11 = new BlockQuartz().setSoundType(SoundType.STONE).setHardness(0.8f).setUnlocalizedName("quartzBlock");
        Block.registerBlock(155, "quartz_block", block11);
        Block.registerBlock(156, "quartz_stairs", new BlockStairs(block11.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setUnlocalizedName("stairsQuartz"));
        Block.registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7f).setSoundType(SoundType.METAL).setUnlocalizedName("activatorRail"));
        Block.registerBlock(158, "dropper", new BlockDropper().setHardness(3.5f).setSoundType(SoundType.STONE).setUnlocalizedName("dropper"));
        Block.registerBlock(159, "stained_hardened_clay", new BlockStainedHardenedClay().setHardness(1.25f).setResistance(7.0f).setSoundType(SoundType.STONE).setUnlocalizedName("clayHardenedStained"));
        Block.registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3f).setSoundType(SoundType.GLASS).setUnlocalizedName("thinStainedGlass"));
        Block.registerBlock(161, "leaves2", new BlockNewLeaf().setUnlocalizedName("leaves"));
        Block.registerBlock(162, "log2", new BlockNewLog().setUnlocalizedName("log"));
        Block.registerBlock(163, "acacia_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setUnlocalizedName("stairsWoodAcacia"));
        Block.registerBlock(164, "dark_oak_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setUnlocalizedName("stairsWoodDarkOak"));
        Block.registerBlock(165, "slime", new BlockSlime().setUnlocalizedName("slime").setSoundType(SoundType.SLIME));
        Block.registerBlock(166, "barrier", new BlockBarrier().setUnlocalizedName("barrier"));
        Block.registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.IRON).setHardness(5.0f).setSoundType(SoundType.METAL).setUnlocalizedName("ironTrapdoor").disableStats());
        Block.registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("prismarine"));
        Block.registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setLightLevel(1.0f).setUnlocalizedName("seaLantern"));
        Block.registerBlock(170, "hay_block", new BlockHay().setHardness(0.5f).setSoundType(SoundType.PLANT).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1f).setSoundType(SoundType.CLOTH).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        Block.registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25f).setResistance(7.0f).setSoundType(SoundType.STONE).setUnlocalizedName("clayHardened"));
        Block.registerBlock(173, "coal_block", new Block(Material.ROCK, MapColor.BLACK).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5f).setSoundType(SoundType.GLASS).setUnlocalizedName("icePacked"));
        Block.registerBlock(175, "double_plant", (Block)new BlockDoublePlant());
        Block.registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(178, "daylight_detector_inverted", (Block)new BlockDaylightDetector(true));
        Block block12 = new BlockRedSandstone().setSoundType(SoundType.STONE).setHardness(0.8f).setUnlocalizedName("redSandStone");
        Block.registerBlock(179, "red_sandstone", block12);
        Block.registerBlock(180, "red_sandstone_stairs", new BlockStairs(block12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)).setUnlocalizedName("stairsRedSandStone"));
        Block.registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(183, "spruce_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("spruceFenceGate"));
        Block.registerBlock(184, "birch_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("birchFenceGate"));
        Block.registerBlock(185, "jungle_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.JUNGLE).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("jungleFenceGate"));
        Block.registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("darkOakFenceGate"));
        Block.registerBlock(187, "acacia_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.ACACIA).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("acaciaFenceGate"));
        Block.registerBlock(188, "spruce_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("spruceFence"));
        Block.registerBlock(189, "birch_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("birchFence"));
        Block.registerBlock(190, "jungle_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("jungleFence"));
        Block.registerBlock(191, "dark_oak_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("darkOakFence"));
        Block.registerBlock(192, "acacia_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("acaciaFence"));
        Block.registerBlock(193, "spruce_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorSpruce").disableStats());
        Block.registerBlock(194, "birch_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorBirch").disableStats());
        Block.registerBlock(195, "jungle_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorJungle").disableStats());
        Block.registerBlock(196, "acacia_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorAcacia").disableStats());
        Block.registerBlock(197, "dark_oak_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("doorDarkOak").disableStats());
        Block.registerBlock(198, "end_rod", new BlockEndRod().setHardness(0.0f).setLightLevel(0.9375f).setSoundType(SoundType.WOOD).setUnlocalizedName("endRod"));
        Block.registerBlock(199, "chorus_plant", new BlockChorusPlant().setHardness(0.4f).setSoundType(SoundType.WOOD).setUnlocalizedName("chorusPlant"));
        Block.registerBlock(200, "chorus_flower", new BlockChorusFlower().setHardness(0.4f).setSoundType(SoundType.WOOD).setUnlocalizedName("chorusFlower"));
        Block block13 = new Block(Material.ROCK, MapColor.MAGENTA).setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("purpurBlock");
        Block.registerBlock(201, "purpur_block", block13);
        Block.registerBlock(202, "purpur_pillar", new BlockRotatedPillar(Material.ROCK, MapColor.MAGENTA).setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("purpurPillar"));
        Block.registerBlock(203, "purpur_stairs", new BlockStairs(block13.getDefaultState()).setUnlocalizedName("stairsPurpur"));
        Block.registerBlock(204, "purpur_double_slab", new BlockPurpurSlab.Double().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("purpurSlab"));
        Block.registerBlock(205, "purpur_slab", new BlockPurpurSlab.Half().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("purpurSlab"));
        Block.registerBlock(206, "end_bricks", new Block(Material.ROCK, MapColor.SAND).setSoundType(SoundType.STONE).setHardness(0.8f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("endBricks"));
        Block.registerBlock(207, "beetroots", new BlockBeetroot().setUnlocalizedName("beetroots"));
        Block block14 = new BlockGrassPath().setHardness(0.65f).setSoundType(SoundType.PLANT).setUnlocalizedName("grassPath").disableStats();
        Block.registerBlock(208, "grass_path", block14);
        Block.registerBlock(209, "end_gateway", new BlockEndGateway(Material.PORTAL).setHardness(-1.0f).setResistance(6000000.0f));
        Block.registerBlock(210, "repeating_command_block", new BlockCommandBlock(MapColor.PURPLE).setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("repeatingCommandBlock"));
        Block.registerBlock(211, "chain_command_block", new BlockCommandBlock(MapColor.GREEN).setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("chainCommandBlock"));
        Block.registerBlock(212, "frosted_ice", new BlockFrostedIce().setHardness(0.5f).setLightOpacity(3).setSoundType(SoundType.GLASS).setUnlocalizedName("frostedIce"));
        Block.registerBlock(213, "magma", new BlockMagma().setHardness(0.5f).setSoundType(SoundType.STONE).setUnlocalizedName("magma"));
        Block.registerBlock(214, "nether_wart_block", new Block(Material.GRASS, MapColor.RED).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(1.0f).setSoundType(SoundType.WOOD).setUnlocalizedName("netherWartBlock"));
        Block.registerBlock(215, "red_nether_brick", new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setUnlocalizedName("redNetherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        Block.registerBlock(216, "bone_block", new BlockBone().setUnlocalizedName("boneBlock"));
        Block.registerBlock(217, "structure_void", new BlockStructureVoid().setUnlocalizedName("structureVoid"));
        Block.registerBlock(218, "observer", new BlockObserver().setHardness(3.0f).setUnlocalizedName("observer"));
        Block.registerBlock(219, "white_shulker_box", new BlockShulkerBox(EnumDyeColor.WHITE).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxWhite"));
        Block.registerBlock(220, "orange_shulker_box", new BlockShulkerBox(EnumDyeColor.ORANGE).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxOrange"));
        Block.registerBlock(221, "magenta_shulker_box", new BlockShulkerBox(EnumDyeColor.MAGENTA).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxMagenta"));
        Block.registerBlock(222, "light_blue_shulker_box", new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxLightBlue"));
        Block.registerBlock(223, "yellow_shulker_box", new BlockShulkerBox(EnumDyeColor.YELLOW).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxYellow"));
        Block.registerBlock(224, "lime_shulker_box", new BlockShulkerBox(EnumDyeColor.LIME).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxLime"));
        Block.registerBlock(225, "pink_shulker_box", new BlockShulkerBox(EnumDyeColor.PINK).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxPink"));
        Block.registerBlock(226, "gray_shulker_box", new BlockShulkerBox(EnumDyeColor.GRAY).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxGray"));
        Block.registerBlock(227, "silver_shulker_box", new BlockShulkerBox(EnumDyeColor.SILVER).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxSilver"));
        Block.registerBlock(228, "cyan_shulker_box", new BlockShulkerBox(EnumDyeColor.CYAN).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxCyan"));
        Block.registerBlock(229, "purple_shulker_box", new BlockShulkerBox(EnumDyeColor.PURPLE).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxPurple"));
        Block.registerBlock(230, "blue_shulker_box", new BlockShulkerBox(EnumDyeColor.BLUE).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBlue"));
        Block.registerBlock(231, "brown_shulker_box", new BlockShulkerBox(EnumDyeColor.BROWN).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBrown"));
        Block.registerBlock(232, "green_shulker_box", new BlockShulkerBox(EnumDyeColor.GREEN).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxGreen"));
        Block.registerBlock(233, "red_shulker_box", new BlockShulkerBox(EnumDyeColor.RED).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxRed"));
        Block.registerBlock(234, "black_shulker_box", new BlockShulkerBox(EnumDyeColor.BLACK).setHardness(2.0f).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBlack"));
        Block.registerBlock(235, "white_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.WHITE));
        Block.registerBlock(236, "orange_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
        Block.registerBlock(237, "magenta_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
        Block.registerBlock(238, "light_blue_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
        Block.registerBlock(239, "yellow_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
        Block.registerBlock(240, "lime_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.LIME));
        Block.registerBlock(241, "pink_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.PINK));
        Block.registerBlock(242, "gray_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.GRAY));
        Block.registerBlock(243, "silver_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.SILVER));
        Block.registerBlock(244, "cyan_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.CYAN));
        Block.registerBlock(245, "purple_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
        Block.registerBlock(246, "blue_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.BLUE));
        Block.registerBlock(247, "brown_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.BROWN));
        Block.registerBlock(248, "green_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.GREEN));
        Block.registerBlock(249, "red_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.RED));
        Block.registerBlock(250, "black_glazed_terracotta", (Block)new BlockGlazedTerracotta(EnumDyeColor.BLACK));
        Block.registerBlock(251, "concrete", new BlockColored(Material.ROCK).setHardness(1.8f).setSoundType(SoundType.STONE).setUnlocalizedName("concrete"));
        Block.registerBlock(252, "concrete_powder", new BlockConcretePowder().setHardness(0.5f).setSoundType(SoundType.SAND).setUnlocalizedName("concretePowder"));
        Block.registerBlock(255, "structure_block", new BlockStructure().setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("structureBlock"));
        REGISTRY.validateKey();
        for (Block block15 : REGISTRY) {
            boolean flag5;
            if (block15.blockMaterial == Material.AIR) {
                block15.useNeighborBrightness = false;
                continue;
            }
            boolean flag = false;
            boolean flag1 = block15 instanceof BlockStairs;
            boolean flag2 = block15 instanceof BlockSlab;
            boolean flag3 = block15 == block6 || block15 == block14;
            boolean flag4 = block15.translucent;
            boolean bl = flag5 = block15.lightOpacity == 0;
            if (flag1 || flag2 || flag3 || flag4 || flag5) {
                flag = true;
            }
            block15.useNeighborBrightness = flag;
        }
        HashSet<Block> set = Sets.newHashSet(REGISTRY.getObject(new ResourceLocation("tripwire")));
        for (Block block16 : REGISTRY) {
            if (set.contains(block16)) {
                for (int i = 0; i < 15; ++i) {
                    int j = REGISTRY.getIDForObject(block16) << 4 | i;
                    BLOCK_STATE_IDS.put(block16.getStateFromMeta(i), j);
                }
                continue;
            }
            for (IBlockState iblockstate : block16.getBlockState().getValidStates()) {
                int k = REGISTRY.getIDForObject(block16) << 4 | block16.getMetaFromState(iblockstate);
                BLOCK_STATE_IDS.put(iblockstate, k);
            }
        }
    }

    private static void registerBlock(int id, ResourceLocation textualID, Block block_) {
        REGISTRY.register(id, textualID, block_);
    }

    private static void registerBlock(int id, String textualID, Block block_) {
        Block.registerBlock(id, new ResourceLocation(textualID), block_);
    }

    public boolean isLiquid() {
        return this.blockMaterial.isLiquid();
    }

    public static enum EnumOffsetType {
        NONE,
        XZ,
        XYZ;

    }
}

