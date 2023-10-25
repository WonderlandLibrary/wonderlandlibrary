/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
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
    protected boolean enableStats = true;
    public static final SoundType SLIME_SOUND;
    public static final SoundType soundTypeGrass;
    protected boolean isBlockContainer;
    public static final SoundType soundTypeAnvil;
    public static final SoundType soundTypePiston;
    private CreativeTabs displayOnCreativeTab;
    public SoundType stepSound = soundTypeStone;
    protected int lightValue;
    protected int lightOpacity;
    public static final SoundType soundTypeGlass;
    protected final Material blockMaterial;
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blockRegistry;
    public static final SoundType soundTypeGravel;
    protected boolean fullBlock;
    public static final SoundType soundTypeSnow;
    public static final SoundType soundTypeStone;
    protected double maxY;
    protected boolean useNeighborBrightness;
    public float slipperiness = 0.6f;
    public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS;
    protected double maxX;
    protected double maxZ;
    public static final SoundType soundTypeCloth;
    public static final SoundType soundTypeSand;
    protected float blockResistance;
    protected double minY;
    public static final SoundType soundTypeWood;
    public static final SoundType soundTypeMetal;
    public float blockParticleGravity = 1.0f;
    protected boolean needsRandomTick;
    protected final MapColor field_181083_K;
    private IBlockState defaultBlockState;
    public static final SoundType soundTypeLadder;
    protected double minX;
    protected double minZ;
    protected boolean translucent;
    protected float blockHardness;
    private static final ResourceLocation AIR_ID;
    private String unlocalizedName;
    protected final BlockState blockState;

    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
    }

    public int tickRate(World world) {
        return 10;
    }

    public static IBlockState getStateById(int n) {
        int n2 = n & 0xFFF;
        int n3 = n >> 12 & 0xF;
        return Block.getBlockById(n2).getStateFromMeta(n3);
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
    }

    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        entityPlayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        entityPlayer.addExhaustion(0.025f);
        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(entityPlayer)) {
            ItemStack itemStack = this.createStackedBlock(iBlockState);
            if (itemStack != null) {
                Block.spawnAsEntity(world, blockPos, itemStack);
            }
        } else {
            int n = EnchantmentHelper.getFortuneModifier(entityPlayer);
            this.dropBlockAsItem(world, blockPos, iBlockState, n);
        }
    }

    public Vec3 modifyAcceleration(World world, BlockPos blockPos, Entity entity, Vec3 vec3) {
        return vec3;
    }

    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockState;
    }

    public final double getBlockBoundsMaxY() {
        return this.maxY;
    }

    public boolean canReplace(World world, BlockPos blockPos, EnumFacing enumFacing, ItemStack itemStack) {
        return this.canPlaceBlockOnSide(world, blockPos, enumFacing);
    }

    public int getMetaFromState(IBlockState iBlockState) {
        if (iBlockState != null && !iBlockState.getPropertyNames().isEmpty()) {
            throw new IllegalArgumentException("Don't know how to convert " + iBlockState + " back into data...");
        }
        return 0;
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean isNormalCube() {
        return this.blockMaterial.isOpaque() && this.isFullCube() && !this.canProvidePower();
    }

    public boolean onBlockEventReceived(World world, BlockPos blockPos, IBlockState iBlockState, int n, int n2) {
        return false;
    }

    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }

    public final double getBlockBoundsMaxX() {
        return this.maxX;
    }

    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0f);
        return this;
    }

    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
    }

    public int damageDropped(IBlockState iBlockState) {
        return 0;
    }

    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return 0xFFFFFF;
    }

    public final double getBlockBoundsMinY() {
        return this.minY;
    }

    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        AxisAlignedBB axisAlignedBB2 = this.getCollisionBoundingBox(world, blockPos, iBlockState);
        if (axisAlignedBB2 != null && axisAlignedBB.intersectsWith(axisAlignedBB2)) {
            list.add(axisAlignedBB2);
        }
    }

    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return 0;
    }

    public boolean isFlowerPot() {
        return false;
    }

    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(this);
    }

    public void setBlockBoundsForItemRender() {
    }

    public boolean func_181623_g() {
        return !this.blockMaterial.isSolid() && !this.blockMaterial.isLiquid();
    }

    public int getMixedBrightnessForBlock(IBlockAccess iBlockAccess, BlockPos blockPos) {
        Block block = iBlockAccess.getBlockState(blockPos).getBlock();
        int n = iBlockAccess.getCombinedLight(blockPos, block.getLightValue());
        if (n == 0 && block instanceof BlockSlab) {
            blockPos = blockPos.down();
            block = iBlockAccess.getBlockState(blockPos).getBlock();
            return iBlockAccess.getCombinedLight(blockPos, block.getLightValue());
        }
        return n;
    }

    public Material getMaterial() {
        return this.blockMaterial;
    }

    private boolean isVecInsideYZBounds(Vec3 vec3) {
        return vec3 == null ? false : vec3.yCoord >= this.minY && vec3.yCoord <= this.maxY && vec3.zCoord >= this.minZ && vec3.zCoord <= this.maxZ;
    }

    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
    }

    public static int getStateId(IBlockState iBlockState) {
        Block block = iBlockState.getBlock();
        return Block.getIdFromBlock(block) + (block.getMetaFromState(iBlockState) << 12);
    }

    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
    }

    public float getExplosionResistance(Entity entity) {
        return this.blockResistance / 5.0f;
    }

    protected Block setResistance(float f) {
        this.blockResistance = f * 3.0f;
        return this;
    }

    protected Block setStepSound(SoundType soundType) {
        this.stepSound = soundType;
        return this;
    }

    public static Block getBlockFromName(String string) {
        ResourceLocation resourceLocation = new ResourceLocation(string);
        if (blockRegistry.containsKey(resourceLocation)) {
            return blockRegistry.getObject(resourceLocation);
        }
        try {
            return blockRegistry.getObjectById(Integer.parseInt(string));
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }

    public int getLightValue() {
        return this.lightValue;
    }

    protected Block setTickRandomly(boolean bl) {
        this.needsRandomTick = bl;
        return this;
    }

    public float getBlockHardness(World world, BlockPos blockPos) {
        return this.blockHardness;
    }

    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedName;
    }

    protected Block setLightLevel(float f) {
        this.lightValue = (int)(15.0f * f);
        return this;
    }

    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
    }

    public boolean isFullBlock() {
        return this.fullBlock;
    }

    public final double getBlockBoundsMinX() {
        return this.minX;
    }

    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return 0;
    }

    public int getLightOpacity() {
        return this.lightOpacity;
    }

    public static void spawnAsEntity(World world, BlockPos blockPos, ItemStack itemStack) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            float f = 0.5f;
            double d = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            double d3 = (double)(world.rand.nextFloat() * f) + (double)(1.0f - f) * 0.5;
            EntityItem entityItem = new EntityItem(world, (double)blockPos.getX() + d, (double)blockPos.getY() + d2, (double)blockPos.getZ() + d3, itemStack);
            entityItem.setDefaultPickupDelay();
            world.spawnEntityInWorld(entityItem);
        }
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[0]);
    }

    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    public boolean isVisuallyOpaque() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return world.getBlockState((BlockPos)blockPos).getBlock().blockMaterial.isReplaceable();
    }

    private boolean isVecInsideXYBounds(Vec3 vec3) {
        return vec3 == null ? false : vec3.xCoord >= this.minX && vec3.xCoord <= this.maxX && vec3.yCoord >= this.minY && vec3.yCoord <= this.maxY;
    }

    static {
        AIR_ID = new ResourceLocation("air");
        blockRegistry = new RegistryNamespacedDefaultedByKey(AIR_ID);
        BLOCK_STATE_IDS = new ObjectIntIdentityMap();
        soundTypeStone = new SoundType("stone", 1.0f, 1.0f);
        soundTypeWood = new SoundType("wood", 1.0f, 1.0f);
        soundTypeGravel = new SoundType("gravel", 1.0f, 1.0f);
        soundTypeGrass = new SoundType("grass", 1.0f, 1.0f);
        soundTypePiston = new SoundType("stone", 1.0f, 1.0f);
        soundTypeMetal = new SoundType("stone", 1.0f, 1.5f);
        soundTypeGlass = new SoundType("stone", 1.0f, 1.0f){

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

            @Override
            public String getBreakSound() {
                return "dig.wood";
            }
        };
        soundTypeAnvil = new SoundType("anvil", 0.3f, 1.0f){

            @Override
            public String getPlaceSound() {
                return "random.anvil_land";
            }

            @Override
            public String getBreakSound() {
                return "dig.stone";
            }
        };
        SLIME_SOUND = new SoundType("slime", 1.0f, 1.0f){

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

    public boolean hasTileEntity() {
        return this.isBlockContainer;
    }

    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        float f = this.getBlockHardness(world, blockPos);
        return f < 0.0f ? 0.0f : (!entityPlayer.canHarvestBlock(this) ? entityPlayer.getToolDigEfficiency(this) / f / 100.0f : entityPlayer.getToolDigEfficiency(this) / f / 30.0f);
    }

    public final double getBlockBoundsMaxZ() {
        return this.maxZ;
    }

    public boolean canProvidePower() {
        return false;
    }

    public static void registerBlocks() {
        int n;
        Block.registerBlock(0, AIR_ID, new BlockAir().setUnlocalizedName("air"));
        Block.registerBlock(1, "stone", new BlockStone().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stone"));
        Block.registerBlock(2, "grass", new BlockGrass().setHardness(0.6f).setStepSound(soundTypeGrass).setUnlocalizedName("grass"));
        Block.registerBlock(3, "dirt", new BlockDirt().setHardness(0.5f).setStepSound(soundTypeGravel).setUnlocalizedName("dirt"));
        Block block = new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(4, "cobblestone", block);
        Block block2 = new BlockPlanks().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("wood");
        Block.registerBlock(5, "planks", block2);
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
        Block.registerBlock(22, "lapis_block", new Block(Material.iron, MapColor.lapisColor).setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("dispenser"));
        Block block3 = new BlockSandStone().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("sandStone");
        Block.registerBlock(24, "sandstone", block3);
        Block.registerBlock(25, "noteblock", new BlockNote().setHardness(0.8f).setUnlocalizedName("musicBlock"));
        Block.registerBlock(26, "bed", new BlockBed().setStepSound(soundTypeWood).setHardness(0.2f).setUnlocalizedName("bed").disableStats());
        Block.registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("goldenRail"));
        Block.registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("detectorRail"));
        Block.registerBlock(29, "sticky_piston", new BlockPistonBase(true).setUnlocalizedName("pistonStickyBase"));
        Block.registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0f).setUnlocalizedName("web"));
        Block.registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("tallgrass"));
        Block.registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("deadbush"));
        Block.registerBlock(33, "piston", new BlockPistonBase(false).setUnlocalizedName("pistonBase"));
        Block.registerBlock(34, "piston_head", new BlockPistonExtension().setUnlocalizedName("pistonBase"));
        Block.registerBlock(35, "wool", new BlockColored(Material.cloth).setHardness(0.8f).setStepSound(soundTypeCloth).setUnlocalizedName("cloth"));
        Block.registerBlock(36, "piston_extension", (Block)new BlockPistonMoving());
        Block.registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("flower1"));
        Block.registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("flower2"));
        Block block4 = new BlockMushroom().setHardness(0.0f).setStepSound(soundTypeGrass).setLightLevel(0.125f).setUnlocalizedName("mushroom");
        Block.registerBlock(39, "brown_mushroom", block4);
        Block block5 = new BlockMushroom().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("mushroom");
        Block.registerBlock(40, "red_mushroom", block5);
        Block.registerBlock(41, "gold_block", new Block(Material.iron, MapColor.goldColor).setHardness(3.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(42, "iron_block", new Block(Material.iron, MapColor.ironColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block.registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block block6 = new Block(Material.rock, MapColor.redColor).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(45, "brick_block", block6);
        Block.registerBlock(46, "tnt", new BlockTNT().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("tnt"));
        Block.registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5f).setStepSound(soundTypeWood).setUnlocalizedName("bookshelf"));
        Block.registerBlock(48, "mossy_cobblestone", new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0f).setResistance(2000.0f).setStepSound(soundTypePiston).setUnlocalizedName("obsidian"));
        Block.registerBlock(50, "torch", new BlockTorch().setHardness(0.0f).setLightLevel(0.9375f).setStepSound(soundTypeWood).setUnlocalizedName("torch"));
        Block.registerBlock(51, "fire", new BlockFire().setHardness(0.0f).setLightLevel(1.0f).setStepSound(soundTypeCloth).setUnlocalizedName("fire").disableStats());
        Block.registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0f).setStepSound(soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
        Block.registerBlock(53, "oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setUnlocalizedName("stairsWood"));
        Block.registerBlock(54, "chest", new BlockChest(0).setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("chest"));
        Block.registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0f).setStepSound(soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
        Block.registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreDiamond"));
        Block.registerBlock(57, "diamond_block", new Block(Material.iron, MapColor.diamondColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("workbench"));
        Block.registerBlock(59, "wheat", new BlockCrops().setUnlocalizedName("crops"));
        Block block7 = new BlockFarmland().setHardness(0.6f).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
        Block.registerBlock(60, "farmland", block7);
        Block.registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5f).setStepSound(soundTypePiston).setLightLevel(0.875f).setUnlocalizedName("furnace"));
        Block.registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(64, "wooden_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorOak").disableStats());
        Block.registerBlock(65, "ladder", new BlockLadder().setHardness(0.4f).setStepSound(soundTypeLadder).setUnlocalizedName("ladder"));
        Block.registerBlock(66, "rail", new BlockRail().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("rail"));
        Block.registerBlock(67, "stone_stairs", new BlockStairs(block.getDefaultState()).setUnlocalizedName("stairsStone"));
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
        Block.registerBlock(85, "fence", new BlockFence(Material.wood, BlockPlanks.EnumType.OAK.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("fence"));
        Block block8 = new BlockPumpkin().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkin");
        Block.registerBlock(86, "pumpkin", block8);
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
        Block block9 = new BlockStoneBrick().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stonebricksmooth");
        Block.registerBlock(98, "stonebrick", block9);
        Block.registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.dirtColor, block4).setHardness(0.2f).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.redColor, block5).setHardness(0.2f).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(101, "iron_bars", new BlockPane(Material.iron, true).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("fenceIron"));
        Block.registerBlock(102, "glass_pane", new BlockPane(Material.glass, false).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("thinGlass"));
        Block block10 = new BlockMelon().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("melon");
        Block.registerBlock(103, "melon_block", block10);
        Block.registerBlock(104, "pumpkin_stem", new BlockStem(block8).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(105, "melon_stem", new BlockStem(block10).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(106, "vine", new BlockVine().setHardness(0.2f).setStepSound(soundTypeGrass).setUnlocalizedName("vine"));
        Block.registerBlock(107, "fence_gate", new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("fenceGate"));
        Block.registerBlock(108, "brick_stairs", new BlockStairs(block6.getDefaultState()).setUnlocalizedName("stairsBrick"));
        Block.registerBlock(109, "stone_brick_stairs", new BlockStairs(block9.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)).setUnlocalizedName("stairsStoneBrickSmooth"));
        Block.registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6f).setStepSound(soundTypeGrass).setUnlocalizedName("mycel"));
        Block.registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0f).setStepSound(soundTypeGrass).setUnlocalizedName("waterlily"));
        Block block11 = new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(112, "nether_brick", block11);
        Block.registerBlock(113, "nether_brick_fence", new BlockFence(Material.rock, MapColor.netherrackColor).setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherFence"));
        Block.registerBlock(114, "nether_brick_stairs", new BlockStairs(block11.getDefaultState()).setUnlocalizedName("stairsNetherBrick"));
        Block.registerBlock(115, "nether_wart", new BlockNetherWart().setUnlocalizedName("netherStalk"));
        Block.registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0f).setResistance(2000.0f).setUnlocalizedName("enchantmentTable"));
        Block.registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5f).setLightLevel(0.125f).setUnlocalizedName("brewingStand"));
        Block.registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0f).setUnlocalizedName("cauldron"));
        Block.registerBlock(119, "end_portal", new BlockEndPortal(Material.portal).setHardness(-1.0f).setResistance(6000000.0f));
        Block.registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setStepSound(soundTypeGlass).setLightLevel(0.125f).setHardness(-1.0f).setUnlocalizedName("endPortalFrame").setResistance(6000000.0f).setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(121, "end_stone", new Block(Material.rock, MapColor.sandColor).setHardness(3.0f).setResistance(15.0f).setStepSound(soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0f).setResistance(15.0f).setStepSound(soundTypePiston).setLightLevel(0.125f).setUnlocalizedName("dragonEgg"));
        Block.registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight"));
        Block.registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("cocoa"));
        Block.registerBlock(128, "sandstone_stairs", new BlockStairs(block3.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setUnlocalizedName("stairsSandStone"));
        Block.registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("oreEmerald"));
        Block.registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5f).setResistance(1000.0f).setStepSound(soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5f));
        Block.registerBlock(131, "tripwire_hook", new BlockTripWireHook().setUnlocalizedName("tripWireSource"));
        Block.registerBlock(132, "tripwire", new BlockTripWire().setUnlocalizedName("tripWire"));
        Block.registerBlock(133, "emerald_block", new Block(Material.iron, MapColor.emeraldColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(134, "spruce_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setUnlocalizedName("stairsWoodSpruce"));
        Block.registerBlock(135, "birch_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setUnlocalizedName("stairsWoodBirch"));
        Block.registerBlock(136, "jungle_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setUnlocalizedName("stairsWoodJungle"));
        Block.registerBlock(137, "command_block", new BlockCommandBlock().setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("commandBlock"));
        Block.registerBlock(138, "beacon", new BlockBeacon().setUnlocalizedName("beacon").setLightLevel(1.0f));
        Block.registerBlock(139, "cobblestone_wall", new BlockWall(block).setUnlocalizedName("cobbleWall"));
        Block.registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0f).setStepSound(soundTypeStone).setUnlocalizedName("flowerPot"));
        Block.registerBlock(141, "carrots", new BlockCarrot().setUnlocalizedName("carrots"));
        Block.registerBlock(142, "potatoes", new BlockPotato().setUnlocalizedName("potatoes"));
        Block.registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("button"));
        Block.registerBlock(144, "skull", new BlockSkull().setHardness(1.0f).setStepSound(soundTypePiston).setUnlocalizedName("skull"));
        Block.registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0f).setStepSound(soundTypeAnvil).setResistance(2000.0f).setUnlocalizedName("anvil"));
        Block.registerBlock(146, "trapped_chest", new BlockChest(1).setHardness(2.5f).setStepSound(soundTypeWood).setUnlocalizedName("chestTrap"));
        Block.registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 15, MapColor.goldColor).setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_light"));
        Block.registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 150).setHardness(0.5f).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
        Block.registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0f).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0f).setLightLevel(0.625f).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(151, "daylight_detector", (Block)new BlockDaylightDetector(false));
        Block.registerBlock(152, "redstone_block", new BlockCompressedPowered(Material.iron, MapColor.tntColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypeMetal).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(153, "quartz_ore", new BlockOre(MapColor.netherrackColor).setHardness(3.0f).setResistance(5.0f).setStepSound(soundTypePiston).setUnlocalizedName("netherquartz"));
        Block.registerBlock(154, "hopper", new BlockHopper().setHardness(3.0f).setResistance(8.0f).setStepSound(soundTypeMetal).setUnlocalizedName("hopper"));
        Block block12 = new BlockQuartz().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("quartzBlock");
        Block.registerBlock(155, "quartz_block", block12);
        Block.registerBlock(156, "quartz_stairs", new BlockStairs(block12.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setUnlocalizedName("stairsQuartz"));
        Block.registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(soundTypeMetal).setUnlocalizedName("activatorRail"));
        Block.registerBlock(158, "dropper", new BlockDropper().setHardness(3.5f).setStepSound(soundTypePiston).setUnlocalizedName("dropper"));
        Block.registerBlock(159, "stained_hardened_clay", new BlockColored(Material.rock).setHardness(1.25f).setResistance(7.0f).setStepSound(soundTypePiston).setUnlocalizedName("clayHardenedStained"));
        Block.registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3f).setStepSound(soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
        Block.registerBlock(161, "leaves2", new BlockNewLeaf().setUnlocalizedName("leaves"));
        Block.registerBlock(162, "log2", new BlockNewLog().setUnlocalizedName("log"));
        Block.registerBlock(163, "acacia_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setUnlocalizedName("stairsWoodAcacia"));
        Block.registerBlock(164, "dark_oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setUnlocalizedName("stairsWoodDarkOak"));
        Block.registerBlock(165, "slime", new BlockSlime().setUnlocalizedName("slime").setStepSound(SLIME_SOUND));
        Block.registerBlock(166, "barrier", new BlockBarrier().setUnlocalizedName("barrier"));
        Block.registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.iron).setHardness(5.0f).setStepSound(soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
        Block.registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("prismarine"));
        Block.registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.glass).setHardness(0.3f).setStepSound(soundTypeGlass).setLightLevel(1.0f).setUnlocalizedName("seaLantern"));
        Block.registerBlock(170, "hay_block", new BlockHay().setHardness(0.5f).setStepSound(soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1f).setStepSound(soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        Block.registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25f).setResistance(7.0f).setStepSound(soundTypePiston).setUnlocalizedName("clayHardened"));
        Block.registerBlock(173, "coal_block", new Block(Material.rock, MapColor.blackColor).setHardness(5.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5f).setStepSound(soundTypeGlass).setUnlocalizedName("icePacked"));
        Block.registerBlock(175, "double_plant", (Block)new BlockDoublePlant());
        Block.registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0f).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(178, "daylight_detector_inverted", (Block)new BlockDaylightDetector(true));
        Block block13 = new BlockRedSandstone().setStepSound(soundTypePiston).setHardness(0.8f).setUnlocalizedName("redSandStone");
        Block.registerBlock(179, "red_sandstone", block13);
        Block.registerBlock(180, "red_sandstone_stairs", new BlockStairs(block13.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)).setUnlocalizedName("stairsRedSandStone"));
        Block.registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(183, "spruce_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("spruceFenceGate"));
        Block.registerBlock(184, "birch_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("birchFenceGate"));
        Block.registerBlock(185, "jungle_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.JUNGLE).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("jungleFenceGate"));
        Block.registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
        Block.registerBlock(187, "acacia_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.ACACIA).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
        Block.registerBlock(188, "spruce_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.SPRUCE.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("spruceFence"));
        Block.registerBlock(189, "birch_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.BIRCH.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("birchFence"));
        Block.registerBlock(190, "jungle_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.JUNGLE.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("jungleFence"));
        Block.registerBlock(191, "dark_oak_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.DARK_OAK.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFence"));
        Block.registerBlock(192, "acacia_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.ACACIA.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFence"));
        Block.registerBlock(193, "spruce_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
        Block.registerBlock(194, "birch_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
        Block.registerBlock(195, "jungle_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
        Block.registerBlock(196, "acacia_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
        Block.registerBlock(197, "dark_oak_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
        blockRegistry.validateKey();
        for (Block block14 : blockRegistry) {
            boolean bl;
            if (block14.blockMaterial == Material.air) {
                block14.useNeighborBrightness = false;
                continue;
            }
            boolean bl2 = false;
            boolean bl3 = block14 instanceof BlockStairs;
            n = block14 instanceof BlockSlab;
            boolean bl4 = block14 == block7;
            boolean bl5 = block14.translucent;
            boolean bl6 = bl = block14.lightOpacity == 0;
            if (bl3 || n != 0 || bl4 || bl5 || bl) {
                bl2 = true;
            }
            block14.useNeighborBrightness = bl2;
        }
        for (Block block14 : blockRegistry) {
            for (IBlockState iBlockState : block14.getBlockState().getValidStates()) {
                n = blockRegistry.getIDForObject(block14) << 4 | block14.getMetaFromState(iBlockState);
                BLOCK_STATE_IDS.put(iBlockState, n);
            }
        }
    }

    public boolean isReplaceable(World world, BlockPos blockPos) {
        return false;
    }

    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(this);
    }

    protected Block setHardness(float f) {
        this.blockHardness = f;
        if (this.blockResistance < f * 5.0f) {
            this.blockResistance = f * 5.0f;
        }
        return this;
    }

    public boolean isBlockNormalCube() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean getUseNeighborBrightness() {
        return this.useNeighborBrightness;
    }

    public final int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return this.colorMultiplier(iBlockAccess, blockPos, 0);
    }

    public void fillWithRain(World world, BlockPos blockPos) {
    }

    public boolean isAssociatedBlock(Block block) {
        return this == block;
    }

    public boolean isCollidable() {
        return true;
    }

    public int getDamageValue(World world, BlockPos blockPos) {
        return this.damageDropped(world.getBlockState(blockPos));
    }

    public boolean isBlockSolid(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return iBlockAccess.getBlockState(blockPos).getBlock().getMaterial().isSolid();
    }

    public void onLanded(World world, Entity entity) {
        entity.motionY = 0.0;
    }

    protected final void setDefaultState(IBlockState iBlockState) {
        this.defaultBlockState = iBlockState;
    }

    public static int getIdFromBlock(Block block) {
        return blockRegistry.getIDForObject(block);
    }

    public boolean hasComparatorInputOverride() {
        return false;
    }

    private static void registerBlock(int n, String string, Block block) {
        Block.registerBlock(n, new ResourceLocation(string), block);
    }

    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
    }

    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getStateFromMeta(n);
    }

    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        if (!world.isRemote) {
            int n2 = this.quantityDroppedWithBonus(n, world.rand);
            int n3 = 0;
            while (n3 < n2) {
                Item item;
                if (world.rand.nextFloat() <= f && (item = this.getItemDropped(iBlockState, world.rand, n)) != null) {
                    Block.spawnAsEntity(world, blockPos, new ItemStack(item, 1, this.damageDropped(iBlockState)));
                }
                ++n3;
            }
        }
    }

    public void randomTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        this.updateTick(world, blockPos, iBlockState, random);
    }

    private boolean isVecInsideXZBounds(Vec3 vec3) {
        return vec3 == null ? false : vec3.xCoord >= this.minX && vec3.xCoord <= this.maxX && vec3.zCoord >= this.minZ && vec3.zCoord <= this.maxZ;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return 0;
    }

    public int getRenderColor(IBlockState iBlockState) {
        return 0xFFFFFF;
    }

    public void onFallenUpon(World world, BlockPos blockPos, Entity entity, float f) {
        entity.fall(f, 1.0f);
    }

    protected boolean canSilkHarvest() {
        return this.isFullCube() && !this.isBlockContainer;
    }

    public Block setCreativeTab(CreativeTabs creativeTabs) {
        this.displayOnCreativeTab = creativeTabs;
        return this;
    }

    public int getMobilityFlag() {
        return this.blockMaterial.getMaterialMobility();
    }

    public int getRenderType() {
        return 3;
    }

    protected void dropXpOnBlockBreak(World world, BlockPos blockPos, int n) {
        if (!world.isRemote) {
            while (n > 0) {
                int n2 = EntityXPOrb.getXPSplit(n);
                n -= n2;
                world.spawnEntityInWorld(new EntityXPOrb(world, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, n2));
            }
        }
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public static boolean isEqualTo(Block block, Block block2) {
        return block != null && block2 != null ? (block == block2 ? true : block.isAssociatedBlock(block2)) : false;
    }

    public int getBlockColor() {
        return 0xFFFFFF;
    }

    public final IBlockState getDefaultState() {
        return this.defaultBlockState;
    }

    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        return new AxisAlignedBB((double)blockPos.getX() + this.minX, (double)blockPos.getY() + this.minY, (double)blockPos.getZ() + this.minZ, (double)blockPos.getX() + this.maxX, (double)blockPos.getY() + this.maxY, (double)blockPos.getZ() + this.maxZ);
    }

    public Block(Material material, MapColor mapColor) {
        this.blockMaterial = material;
        this.field_181083_K = mapColor;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.fullBlock = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
        this.translucent = !material.blocksLight();
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
    }

    public boolean canCollideCheck(IBlockState iBlockState, boolean bl) {
        return this.isCollidable();
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
    }

    public String toString() {
        return "Block{" + blockRegistry.getNameForObject(this) + "}";
    }

    protected final void setBlockBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        this.minX = f;
        this.minY = f2;
        this.minZ = f3;
        this.maxX = f4;
        this.maxY = f5;
        this.maxZ = f6;
    }

    public boolean getEnableStats() {
        return this.enableStats;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.SOLID;
    }

    protected Block setLightOpacity(int n) {
        this.lightOpacity = n;
        return this;
    }

    private static void registerBlock(int n, ResourceLocation resourceLocation, Block block) {
        blockRegistry.register(n, resourceLocation, block);
    }

    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN && this.minY > 0.0 ? true : (enumFacing == EnumFacing.UP && this.maxY < 1.0 ? true : (enumFacing == EnumFacing.NORTH && this.minZ > 0.0 ? true : (enumFacing == EnumFacing.SOUTH && this.maxZ < 1.0 ? true : (enumFacing == EnumFacing.WEST && this.minX > 0.0 ? true : (enumFacing == EnumFacing.EAST && this.maxX < 1.0 ? true : !iBlockAccess.getBlockState(blockPos).getBlock().isOpaqueCube())))));
    }

    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    public IBlockState getStateForEntityRender(IBlockState iBlockState) {
        return iBlockState;
    }

    public Block setUnlocalizedName(String string) {
        this.unlocalizedName = string;
        return this;
    }

    public final void dropBlockAsItem(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        this.dropBlockAsItemWithChance(world, blockPos, iBlockState, 1.0f, n);
    }

    public int quantityDroppedWithBonus(int n, Random random) {
        return this.quantityDropped(random);
    }

    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        return false;
    }

    public boolean isPassable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return !this.blockMaterial.blocksMovement();
    }

    public MapColor getMapColor(IBlockState iBlockState) {
        return this.field_181083_K;
    }

    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 vec3, Vec3 vec32) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        vec3 = vec3.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        vec32 = vec32.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        Vec3 vec33 = vec3.getIntermediateWithXValue(vec32, this.minX);
        Vec3 vec34 = vec3.getIntermediateWithXValue(vec32, this.maxX);
        Vec3 vec35 = vec3.getIntermediateWithYValue(vec32, this.minY);
        Vec3 vec36 = vec3.getIntermediateWithYValue(vec32, this.maxY);
        Vec3 vec37 = vec3.getIntermediateWithZValue(vec32, this.minZ);
        Vec3 vec38 = vec3.getIntermediateWithZValue(vec32, this.maxZ);
        if (!this.isVecInsideYZBounds(vec33)) {
            vec33 = null;
        }
        if (!this.isVecInsideYZBounds(vec34)) {
            vec34 = null;
        }
        if (!this.isVecInsideXZBounds(vec35)) {
            vec35 = null;
        }
        if (!this.isVecInsideXZBounds(vec36)) {
            vec36 = null;
        }
        if (!this.isVecInsideXYBounds(vec37)) {
            vec37 = null;
        }
        if (!this.isVecInsideXYBounds(vec38)) {
            vec38 = null;
        }
        Vec3 vec39 = null;
        if (vec33 != null && (vec39 == null || vec3.squareDistanceTo(vec33) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec33;
        }
        if (vec34 != null && (vec39 == null || vec3.squareDistanceTo(vec34) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec34;
        }
        if (vec35 != null && (vec39 == null || vec3.squareDistanceTo(vec35) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec35;
        }
        if (vec36 != null && (vec39 == null || vec3.squareDistanceTo(vec36) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec36;
        }
        if (vec37 != null && (vec39 == null || vec3.squareDistanceTo(vec37) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec37;
        }
        if (vec38 != null && (vec39 == null || vec3.squareDistanceTo(vec38) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec38;
        }
        if (vec39 == null) {
            return null;
        }
        EnumFacing enumFacing = null;
        if (vec39 == vec33) {
            enumFacing = EnumFacing.WEST;
        }
        if (vec39 == vec34) {
            enumFacing = EnumFacing.EAST;
        }
        if (vec39 == vec35) {
            enumFacing = EnumFacing.DOWN;
        }
        if (vec39 == vec36) {
            enumFacing = EnumFacing.UP;
        }
        if (vec39 == vec37) {
            enumFacing = EnumFacing.NORTH;
        }
        if (vec39 == vec38) {
            enumFacing = EnumFacing.SOUTH;
        }
        return new MovingObjectPosition(vec39.addVector(blockPos.getX(), blockPos.getY(), blockPos.getZ()), enumFacing, blockPos);
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public boolean requiresUpdates() {
        return true;
    }

    public static Block getBlockById(int n) {
        return blockRegistry.getObjectById(n);
    }

    public static Block getBlockFromItem(Item item) {
        return item instanceof ItemBlock ? ((ItemBlock)item).getBlock() : null;
    }

    public boolean isFullCube() {
        return true;
    }

    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
    }

    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState();
    }

    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        int n = 0;
        Item item = Item.getItemFromBlock(this);
        if (item != null && item.getHasSubtypes()) {
            n = this.getMetaFromState(iBlockState);
        }
        return new ItemStack(item, 1, n);
    }

    public final double getBlockBoundsMinZ() {
        return this.minZ;
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return this.canPlaceBlockAt(world, blockPos);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name");
    }

    public float getAmbientOcclusionLightValue() {
        return this.isBlockNormalCube() ? 0.2f : 1.0f;
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return new AxisAlignedBB((double)blockPos.getX() + this.minX, (double)blockPos.getY() + this.minY, (double)blockPos.getZ() + this.minZ, (double)blockPos.getX() + this.maxX, (double)blockPos.getY() + this.maxY, (double)blockPos.getZ() + this.maxZ);
    }

    protected Block(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public boolean canDropFromExplosion(Explosion explosion) {
        return true;
    }

    public static class SoundType {
        public final float volume;
        public final float frequency;
        public final String soundName;

        public float getVolume() {
            return this.volume;
        }

        public String getBreakSound() {
            return "dig." + this.soundName;
        }

        public String getStepSound() {
            return "step." + this.soundName;
        }

        public float getFrequency() {
            return this.frequency;
        }

        public SoundType(String string, float f, float f2) {
            this.soundName = string;
            this.volume = f;
            this.frequency = f2;
        }

        public String getPlaceSound() {
            return this.getBreakSound();
        }
    }

    public static enum EnumOffsetType {
        NONE,
        XZ,
        XYZ;

    }
}

