// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.NonNullList;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import java.util.Iterator;
import ru.tuskevich.modules.impl.PLAYER.NoInteract;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import java.util.List;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.MOVEMENT.NoClip;
import ru.tuskevich.Minced;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;

public class Block
{
    private static final ResourceLocation AIR_ID;
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> REGISTRY;
    public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS;
    public static final AxisAlignedBB FULL_BLOCK_AABB;
    @Nullable
    public static final AxisAlignedBB NULL_AABB;
    private CreativeTabs displayOnCreativeTab;
    protected boolean fullBlock;
    protected int lightOpacity;
    protected boolean translucent;
    protected int lightValue;
    protected boolean useNeighborBrightness;
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats;
    protected boolean needsRandomTick;
    protected boolean hasTileEntity;
    protected SoundType blockSoundType;
    public float blockParticleGravity;
    protected final Material material;
    protected final MapColor blockMapColor;
    public float slipperiness;
    protected final BlockStateContainer blockState;
    private IBlockState defaultBlockState;
    private String translationKey;
    
    public static int getIdFromBlock(final Block blockIn) {
        return Block.REGISTRY.getIDForObject(blockIn);
    }
    
    public static int getStateId(final IBlockState state) {
        final Block block = state.getBlock();
        return getIdFromBlock(block) + (block.getMetaFromState(state) << 12);
    }
    
    public static Block getBlockById(final int id) {
        return Block.REGISTRY.getObjectById(id);
    }
    
    public static IBlockState getStateById(final int id) {
        final int i = id & 0xFFF;
        final int j = id >> 12 & 0xF;
        return getBlockById(i).getStateFromMeta(j);
    }
    
    public static Block getBlockFromItem(@Nullable final Item itemIn) {
        return (itemIn instanceof ItemBlock) ? ((ItemBlock)itemIn).getBlock() : Blocks.AIR;
    }
    
    @Nullable
    public static Block getBlockFromName(final String name) {
        final ResourceLocation resourcelocation = new ResourceLocation(name);
        if (Block.REGISTRY.containsKey(resourcelocation)) {
            return Block.REGISTRY.getObject(resourcelocation);
        }
        try {
            return Block.REGISTRY.getObjectById(Integer.parseInt(name));
        }
        catch (NumberFormatException var3) {
            return null;
        }
    }
    
    @Deprecated
    public boolean isTopSolid(final IBlockState state) {
        return state.getMaterial().isOpaque() && state.isFullCube();
    }
    
    @Deprecated
    public boolean isFullBlock(final IBlockState state) {
        return !Minced.getInstance().manager.getModule(NoClip.class).state;
    }
    
    @Deprecated
    public boolean canEntitySpawn(final IBlockState state, final Entity entityIn) {
        return true;
    }
    
    @Deprecated
    public int getLightOpacity(final IBlockState state) {
        return this.lightOpacity;
    }
    
    @Deprecated
    public boolean isTranslucent(final IBlockState state) {
        return this.translucent;
    }
    
    @Deprecated
    public int getLightValue(final IBlockState state) {
        return this.lightValue;
    }
    
    @Deprecated
    public boolean getUseNeighborBrightness(final IBlockState state) {
        return this.useNeighborBrightness;
    }
    
    @Deprecated
    public Material getMaterial(final IBlockState state) {
        return this.material;
    }
    
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return this.blockMapColor;
    }
    
    @Deprecated
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState();
    }
    
    public int getMetaFromState(final IBlockState state) {
        if (state.getPropertyKeys().isEmpty()) {
            return 0;
        }
        throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
    }
    
    @Deprecated
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state;
    }
    
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state;
    }
    
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state;
    }
    
    public Block(final Material blockMaterialIn, final MapColor blockMapColorIn) {
        this.enableStats = true;
        this.blockSoundType = SoundType.STONE;
        this.blockParticleGravity = 1.0f;
        this.slipperiness = 0.6f;
        this.material = blockMaterialIn;
        this.blockMapColor = blockMapColorIn;
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
        this.fullBlock = this.getDefaultState().isOpaqueCube();
        this.lightOpacity = (this.fullBlock ? 255 : 0);
        this.translucent = !blockMaterialIn.blocksLight();
    }
    
    protected Block(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected Block setSoundType(final SoundType sound) {
        this.blockSoundType = sound;
        return this;
    }
    
    protected Block setLightOpacity(final int opacity) {
        this.lightOpacity = opacity;
        return this;
    }
    
    protected Block setLightLevel(final float value) {
        this.lightValue = (int)(15.0f * value);
        return this;
    }
    
    protected Block setResistance(final float resistance) {
        this.blockResistance = resistance * 3.0f;
        return this;
    }
    
    protected static boolean isExceptionBlockForAttaching(final Block attachBlock) {
        return attachBlock instanceof BlockShulkerBox || attachBlock instanceof BlockLeaves || attachBlock instanceof BlockTrapDoor || attachBlock == Blocks.BEACON || attachBlock == Blocks.CAULDRON || attachBlock == Blocks.GLASS || attachBlock == Blocks.GLOWSTONE || attachBlock == Blocks.ICE || attachBlock == Blocks.SEA_LANTERN || attachBlock == Blocks.STAINED_GLASS;
    }
    
    protected static boolean isExceptBlockForAttachWithPiston(final Block attachBlock) {
        return isExceptionBlockForAttaching(attachBlock) || attachBlock == Blocks.PISTON || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
    }
    
    @Deprecated
    public boolean isBlockNormalCube(final IBlockState state) {
        return state.getMaterial().blocksMovement() && state.isFullCube();
    }
    
    @Deprecated
    public boolean isNormalCube(final IBlockState state) {
        return state.getMaterial().isOpaque() && state.isFullCube() && !state.canProvidePower();
    }
    
    @Deprecated
    public boolean causesSuffocation(final IBlockState state) {
        return this.material.blocksMovement() && this.getDefaultState().isFullCube();
    }
    
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return true;
    }
    
    @Deprecated
    public boolean hasCustomBreakingProgress(final IBlockState state) {
        return false;
    }
    
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return !this.material.blocksMovement();
    }
    
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }
    
    protected Block setHardness(final float hardness) {
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
    public float getBlockHardness(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return this.blockHardness;
    }
    
    protected Block setTickRandomly(final boolean shouldTick) {
        this.needsRandomTick = shouldTick;
        return this;
    }
    
    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }
    
    public boolean hasTileEntity() {
        return this.hasTileEntity;
    }
    
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return Block.FULL_BLOCK_AABB;
    }
    
    @Deprecated
    public int getPackedLightmapCoords(IBlockState state, final IBlockAccess source, BlockPos pos) {
        final int i = source.getCombinedLight(pos, state.getLightValue());
        if (i == 0 && state.getBlock() instanceof BlockSlab) {
            pos = pos.down();
            state = source.getBlockState(pos);
            return source.getCombinedLight(pos, state.getLightValue());
        }
        return i;
    }
    
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        final AxisAlignedBB axisalignedbb = blockState.getBoundingBox(blockAccess, pos);
        switch (side) {
            case DOWN: {
                if (axisalignedbb.minY > 0.0) {
                    return true;
                }
                break;
            }
            case UP: {
                if (axisalignedbb.maxY < 1.0) {
                    return true;
                }
                break;
            }
            case NORTH: {
                if (axisalignedbb.minZ > 0.0) {
                    return true;
                }
                break;
            }
            case SOUTH: {
                if (axisalignedbb.maxZ < 1.0) {
                    return true;
                }
                break;
            }
            case WEST: {
                if (axisalignedbb.minX > 0.0) {
                    return true;
                }
                break;
            }
            case EAST: {
                if (axisalignedbb.maxX < 1.0) {
                    return true;
                }
                break;
            }
        }
        return !blockAccess.getBlockState(pos.offset(side)).isOpaqueCube();
    }
    
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.SOLID;
    }
    
    @Deprecated
    public AxisAlignedBB getSelectedBoundingBox(final IBlockState state, final World worldIn, final BlockPos pos) {
        return state.getBoundingBox(worldIn, pos).offset(pos);
    }
    
    @Deprecated
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
    }
    
    @Deprecated
    protected static void addCollisionBoxToList(final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final AxisAlignedBB blockBox) {
        if (blockBox != Block.NULL_AABB) {
            final AxisAlignedBB axisalignedbb = blockBox.offset(pos);
            if (entityBox.intersects(axisalignedbb)) {
                collidingBoxes.add(axisalignedbb);
            }
        }
    }
    
    @Deprecated
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return blockState.getBoundingBox(worldIn, pos);
    }
    
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return true;
    }
    
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        if (Minced.getInstance().manager.getModule(KillAura.class).state && KillAura.target != null) {
            return false;
        }
        for (final Block block : NoInteract.getBlocks()) {
            if (state.getBlock() == block) {
                if (!Minced.getInstance().manager.getModule(NoInteract.class).state) {
                    continue;
                }
                return false;
            }
        }
        return this.isCollidable();
    }
    
    public boolean isCollidable() {
        return true;
    }
    
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
        this.updateTick(worldIn, pos, state, random);
    }
    
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
    }
    
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
    }
    
    public void onPlayerDestroy(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    @Deprecated
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
    }
    
    public int tickRate(final World worldIn) {
        return 10;
    }
    
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    public int quantityDropped(final Random random) {
        return 1;
    }
    
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this);
    }
    
    @Deprecated
    public float getPlayerRelativeBlockHardness(final IBlockState state, final EntityPlayer player, final World worldIn, final BlockPos pos) {
        final float f = state.getBlockHardness(worldIn, pos);
        if (f < 0.0f) {
            return 0.0f;
        }
        return player.canHarvestBlock(state) ? (player.getDigSpeed(state) / f / 30.0f) : (player.getDigSpeed(state) / f / 100.0f);
    }
    
    public final void dropBlockAsItem(final World worldIn, final BlockPos pos, final IBlockState state, final int fortune) {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0f, fortune);
    }
    
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            for (int i = this.quantityDroppedWithBonus(fortune, worldIn.rand), j = 0; j < i; ++j) {
                if (worldIn.rand.nextFloat() <= chance) {
                    final Item item = this.getItemDropped(state, worldIn.rand, fortune);
                    if (item != Items.AIR) {
                        spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
                    }
                }
            }
        }
    }
    
    public static void spawnAsEntity(final World worldIn, final BlockPos pos, final ItemStack stack) {
        if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean("doTileDrops")) {
            final float f = 0.5f;
            final double d0 = worldIn.rand.nextFloat() * 0.5f + 0.25;
            final double d2 = worldIn.rand.nextFloat() * 0.5f + 0.25;
            final double d3 = worldIn.rand.nextFloat() * 0.5f + 0.25;
            final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d2, pos.getZ() + d3, stack);
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntity(entityitem);
        }
    }
    
    protected void dropXpOnBlockBreak(final World worldIn, final BlockPos pos, int amount) {
        if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
            while (amount > 0) {
                final int i = EntityXPOrb.getXPSplit(amount);
                amount -= i;
                worldIn.spawnEntity(new EntityXPOrb(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i));
            }
        }
    }
    
    public int damageDropped(final IBlockState state) {
        return 0;
    }
    
    public float getExplosionResistance(final Entity exploder) {
        return this.blockResistance / 5.0f;
    }
    
    @Deprecated
    @Nullable
    public RayTraceResult collisionRayTrace(final IBlockState blockState, final World worldIn, final BlockPos pos, final Vec3d start, final Vec3d end) {
        return this.rayTrace(pos, start, end, blockState.getBoundingBox(worldIn, pos));
    }
    
    @Nullable
    protected RayTraceResult rayTrace(final BlockPos pos, final Vec3d start, final Vec3d end, final AxisAlignedBB boundingBox) {
        final Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        final Vec3d vec3d2 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
        final RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d2);
        return (raytraceresult == null) ? null : new RayTraceResult(raytraceresult.hitVec.add(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }
    
    public void onExplosionDestroy(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
    }
    
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return this.canPlaceBlockAt(worldIn, pos);
    }
    
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().material.isReplaceable();
    }
    
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        return false;
    }
    
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
    }
    
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }
    
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
    }
    
    public Vec3d modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3d motion) {
        return motion;
    }
    
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return 0;
    }
    
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return false;
    }
    
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
    }
    
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return 0;
    }
    
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005f);
        if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            final ItemStack itemstack = this.getSilkTouchDrop(state);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            final int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItem(worldIn, pos, state, i);
        }
    }
    
    protected boolean canSilkHarvest() {
        return this.getDefaultState().isFullCube() && !this.hasTileEntity;
    }
    
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        final Item item = Item.getItemFromBlock(this);
        int i = 0;
        if (item.getHasSubtypes()) {
            i = this.getMetaFromState(state);
        }
        return new ItemStack(item, 1, i);
    }
    
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return this.quantityDropped(random);
    }
    
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    }
    
    public boolean canSpawnInBlock() {
        return !this.material.isSolid() && !this.material.isLiquid();
    }
    
    public Block setTranslationKey(final String key) {
        this.translationKey = key;
        return this;
    }
    
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + ".name");
    }
    
    public String getTranslationKey() {
        return "tile." + this.translationKey;
    }
    
    @Deprecated
    public boolean eventReceived(final IBlockState state, final World worldIn, final BlockPos pos, final int id, final int param) {
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
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return this.material.getPushReaction();
    }
    
    @Deprecated
    public float getAmbientOcclusionLightValue(final IBlockState state) {
        return state.isBlockNormalCube() ? 0.2f : 1.0f;
    }
    
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        entityIn.fall(fallDistance, 1.0f);
    }
    
    public void onLanded(final World worldIn, final Entity entityIn) {
        entityIn.motionY = 0.0;
    }
    
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
    }
    
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
    
    public CreativeTabs getCreativeTab() {
        return this.displayOnCreativeTab;
    }
    
    public Block setCreativeTab(final CreativeTabs tab) {
        this.displayOnCreativeTab = tab;
        return this;
    }
    
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
    }
    
    public void fillWithRain(final World worldIn, final BlockPos pos) {
    }
    
    public boolean requiresUpdates() {
        return true;
    }
    
    public boolean canDropFromExplosion(final Explosion explosionIn) {
        return true;
    }
    
    public boolean isAssociatedBlock(final Block other) {
        return this == other;
    }
    
    public static boolean isEqualTo(final Block blockIn, final Block other) {
        return blockIn != null && other != null && (blockIn == other || blockIn.isAssociatedBlock(other));
    }
    
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return false;
    }
    
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return 0;
    }
    
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[0]);
    }
    
    public BlockStateContainer getBlockState() {
        return this.blockState;
    }
    
    protected final void setDefaultState(final IBlockState state) {
        this.defaultBlockState = state;
    }
    
    public final IBlockState getDefaultState() {
        return this.defaultBlockState;
    }
    
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }
    
    @Deprecated
    public Vec3d getOffset(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final EnumOffsetType block$enumoffsettype = this.getOffsetType();
        if (block$enumoffsettype == EnumOffsetType.NONE) {
            return Vec3d.ZERO;
        }
        final long i = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
        return new Vec3d(((i >> 16 & 0xFL) / 15.0f - 0.5) * 0.5, (block$enumoffsettype == EnumOffsetType.XYZ) ? (((i >> 20 & 0xFL) / 15.0f - 1.0) * 0.2) : 0.0, ((i >> 24 & 0xFL) / 15.0f - 0.5) * 0.5);
    }
    
    public SoundType getSoundType() {
        return this.blockSoundType;
    }
    
    @Override
    public String toString() {
        return "Block{" + Block.REGISTRY.getNameForObject(this) + "}";
    }
    
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    }
    
    public static void registerBlocks() {
        registerBlock(0, Block.AIR_ID, new BlockAir().setTranslationKey("air"));
        registerBlock(1, "stone", new BlockStone().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stone"));
        registerBlock(2, "grass", new BlockGrass().setHardness(0.6f).setSoundType(SoundType.PLANT).setTranslationKey("grass"));
        registerBlock(3, "dirt", new BlockDirt().setHardness(0.5f).setSoundType(SoundType.GROUND).setTranslationKey("dirt"));
        final Block block = new Block(Material.ROCK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stonebrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        registerBlock(4, "cobblestone", block);
        final Block block2 = new BlockPlanks().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("wood");
        registerBlock(5, "planks", block2);
        registerBlock(6, "sapling", new BlockSapling().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("sapling"));
        registerBlock(7, "bedrock", new BlockEmptyDrops(Material.ROCK).setBlockUnbreakable().setResistance(6000000.0f).setSoundType(SoundType.STONE).setTranslationKey("bedrock").disableStats().setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.WATER).setHardness(100.0f).setLightOpacity(3).setTranslationKey("water").disableStats());
        registerBlock(9, "water", new BlockStaticLiquid(Material.WATER).setHardness(100.0f).setLightOpacity(3).setTranslationKey("water").disableStats());
        registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.LAVA).setHardness(100.0f).setLightLevel(1.0f).setTranslationKey("lava").disableStats());
        registerBlock(11, "lava", new BlockStaticLiquid(Material.LAVA).setHardness(100.0f).setLightLevel(1.0f).setTranslationKey("lava").disableStats());
        registerBlock(12, "sand", new BlockSand().setHardness(0.5f).setSoundType(SoundType.SAND).setTranslationKey("sand"));
        registerBlock(13, "gravel", new BlockGravel().setHardness(0.6f).setSoundType(SoundType.GROUND).setTranslationKey("gravel"));
        registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreGold"));
        registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreIron"));
        registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreCoal"));
        registerBlock(17, "log", new BlockOldLog().setTranslationKey("log"));
        registerBlock(18, "leaves", new BlockOldLeaf().setTranslationKey("leaves"));
        registerBlock(19, "sponge", new BlockSponge().setHardness(0.6f).setSoundType(SoundType.PLANT).setTranslationKey("sponge"));
        registerBlock(20, "glass", new BlockGlass(Material.GLASS, false).setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("glass"));
        registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreLapis"));
        registerBlock(22, "lapis_block", new Block(Material.IRON, MapColor.LAPIS).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("blockLapis").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5f).setSoundType(SoundType.STONE).setTranslationKey("dispenser"));
        final Block block3 = new BlockSandStone().setSoundType(SoundType.STONE).setHardness(0.8f).setTranslationKey("sandStone");
        registerBlock(24, "sandstone", block3);
        registerBlock(25, "noteblock", new BlockNote().setSoundType(SoundType.WOOD).setHardness(0.8f).setTranslationKey("musicBlock"));
        registerBlock(26, "bed", new BlockBed().setSoundType(SoundType.WOOD).setHardness(0.2f).setTranslationKey("bed").disableStats());
        registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7f).setSoundType(SoundType.METAL).setTranslationKey("goldenRail"));
        registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7f).setSoundType(SoundType.METAL).setTranslationKey("detectorRail"));
        registerBlock(29, "sticky_piston", new BlockPistonBase(true).setTranslationKey("pistonStickyBase"));
        registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0f).setTranslationKey("web"));
        registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("tallgrass"));
        registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("deadbush"));
        registerBlock(33, "piston", new BlockPistonBase(false).setTranslationKey("pistonBase"));
        registerBlock(34, "piston_head", new BlockPistonExtension().setTranslationKey("pistonBase"));
        registerBlock(35, "wool", new BlockColored(Material.CLOTH).setHardness(0.8f).setSoundType(SoundType.CLOTH).setTranslationKey("cloth"));
        registerBlock(36, "piston_extension", new BlockPistonMoving());
        registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("flower1"));
        registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("flower2"));
        final Block block4 = new BlockMushroom().setHardness(0.0f).setSoundType(SoundType.PLANT).setLightLevel(0.125f).setTranslationKey("mushroom");
        registerBlock(39, "brown_mushroom", block4);
        final Block block5 = new BlockMushroom().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("mushroom");
        registerBlock(40, "red_mushroom", block5);
        registerBlock(41, "gold_block", new Block(Material.IRON, MapColor.GOLD).setHardness(3.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("blockGold").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(42, "iron_block", new Block(Material.IRON, MapColor.IRON).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("blockIron").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab"));
        registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab"));
        final Block block6 = new Block(Material.ROCK, MapColor.RED).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("brick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        registerBlock(45, "brick_block", block6);
        registerBlock(46, "tnt", new BlockTNT().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("tnt"));
        registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5f).setSoundType(SoundType.WOOD).setTranslationKey("bookshelf"));
        registerBlock(48, "mossy_cobblestone", new Block(Material.ROCK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stoneMoss").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0f).setResistance(2000.0f).setSoundType(SoundType.STONE).setTranslationKey("obsidian"));
        registerBlock(50, "torch", new BlockTorch().setHardness(0.0f).setLightLevel(0.9375f).setSoundType(SoundType.WOOD).setTranslationKey("torch"));
        registerBlock(51, "fire", new BlockFire().setHardness(0.0f).setLightLevel(1.0f).setSoundType(SoundType.CLOTH).setTranslationKey("fire").disableStats());
        registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0f).setSoundType(SoundType.METAL).setTranslationKey("mobSpawner").disableStats());
        registerBlock(53, "oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setTranslationKey("stairsWood"));
        registerBlock(54, "chest", new BlockChest(BlockChest.Type.BASIC).setHardness(2.5f).setSoundType(SoundType.WOOD).setTranslationKey("chest"));
        registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0f).setSoundType(SoundType.STONE).setTranslationKey("redstoneDust").disableStats());
        registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreDiamond"));
        registerBlock(57, "diamond_block", new Block(Material.IRON, MapColor.DIAMOND).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("blockDiamond").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5f).setSoundType(SoundType.WOOD).setTranslationKey("workbench"));
        registerBlock(59, "wheat", new BlockCrops().setTranslationKey("crops"));
        final Block block7 = new BlockFarmland().setHardness(0.6f).setSoundType(SoundType.GROUND).setTranslationKey("farmland");
        registerBlock(60, "farmland", block7);
        registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5f).setSoundType(SoundType.STONE).setTranslationKey("furnace").setCreativeTab(CreativeTabs.DECORATIONS));
        registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5f).setSoundType(SoundType.STONE).setLightLevel(0.875f).setTranslationKey("furnace"));
        registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("sign").disableStats());
        registerBlock(64, "wooden_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorOak").disableStats());
        registerBlock(65, "ladder", new BlockLadder().setHardness(0.4f).setSoundType(SoundType.LADDER).setTranslationKey("ladder"));
        registerBlock(66, "rail", new BlockRail().setHardness(0.7f).setSoundType(SoundType.METAL).setTranslationKey("rail"));
        registerBlock(67, "stone_stairs", new BlockStairs(block.getDefaultState()).setTranslationKey("stairsStone"));
        registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("sign").disableStats());
        registerBlock(69, "lever", new BlockLever().setHardness(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("lever"));
        registerBlock(70, "stone_pressure_plate", new BlockPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS).setHardness(0.5f).setSoundType(SoundType.STONE).setTranslationKey("pressurePlateStone"));
        registerBlock(71, "iron_door", new BlockDoor(Material.IRON).setHardness(5.0f).setSoundType(SoundType.METAL).setTranslationKey("doorIron").disableStats());
        registerBlock(72, "wooden_pressure_plate", new BlockPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING).setHardness(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("pressurePlateWood"));
        registerBlock(73, "redstone_ore", new BlockRedstoneOre(false).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreRedstone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(74, "lit_redstone_ore", new BlockRedstoneOre(true).setLightLevel(0.625f).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreRedstone"));
        registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("notGate"));
        registerBlock(76, "redstone_torch", new BlockRedstoneTorch(true).setHardness(0.0f).setLightLevel(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("notGate").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5f).setSoundType(SoundType.STONE).setTranslationKey("button"));
        registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1f).setSoundType(SoundType.SNOW).setTranslationKey("snow").setLightOpacity(0));
        registerBlock(79, "ice", new BlockIce().setHardness(0.5f).setLightOpacity(3).setSoundType(SoundType.GLASS).setTranslationKey("ice"));
        registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2f).setSoundType(SoundType.SNOW).setTranslationKey("snow"));
        registerBlock(81, "cactus", new BlockCactus().setHardness(0.4f).setSoundType(SoundType.CLOTH).setTranslationKey("cactus"));
        registerBlock(82, "clay", new BlockClay().setHardness(0.6f).setSoundType(SoundType.GROUND).setTranslationKey("clay"));
        registerBlock(83, "reeds", new BlockReed().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("reeds").disableStats());
        registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("jukebox"));
        registerBlock(85, "fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("fence"));
        final Block block8 = new BlockPumpkin().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("pumpkin");
        registerBlock(86, "pumpkin", block8);
        registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4f).setSoundType(SoundType.STONE).setTranslationKey("hellrock"));
        registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5f).setSoundType(SoundType.SAND).setTranslationKey("hellsand"));
        registerBlock(89, "glowstone", new BlockGlowstone(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setLightLevel(1.0f).setTranslationKey("lightgem"));
        registerBlock(90, "portal", new BlockPortal().setHardness(-1.0f).setSoundType(SoundType.GLASS).setLightLevel(0.75f).setTranslationKey("portal"));
        registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0f).setSoundType(SoundType.WOOD).setLightLevel(1.0f).setTranslationKey("litpumpkin"));
        registerBlock(92, "cake", new BlockCake().setHardness(0.5f).setSoundType(SoundType.CLOTH).setTranslationKey("cake").disableStats());
        registerBlock(93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("diode").disableStats());
        registerBlock(94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("diode").disableStats());
        registerBlock(95, "stained_glass", new BlockStainedGlass(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("stainedGlass"));
        registerBlock(96, "trapdoor", new BlockTrapDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("trapdoor").disableStats());
        registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75f).setTranslationKey("monsterStoneEgg"));
        final Block block9 = new BlockStoneBrick().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stonebricksmooth");
        registerBlock(98, "stonebrick", block9);
        registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.WOOD, MapColor.DIRT, block4).setHardness(0.2f).setSoundType(SoundType.WOOD).setTranslationKey("mushroom"));
        registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.WOOD, MapColor.RED, block5).setHardness(0.2f).setSoundType(SoundType.WOOD).setTranslationKey("mushroom"));
        registerBlock(101, "iron_bars", new BlockPane(Material.IRON, true).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("fenceIron"));
        registerBlock(102, "glass_pane", new BlockPane(Material.GLASS, false).setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("thinGlass"));
        final Block block10 = new BlockMelon().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("melon");
        registerBlock(103, "melon_block", block10);
        registerBlock(104, "pumpkin_stem", new BlockStem(block8).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("pumpkinStem"));
        registerBlock(105, "melon_stem", new BlockStem(block10).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("pumpkinStem"));
        registerBlock(106, "vine", new BlockVine().setHardness(0.2f).setSoundType(SoundType.PLANT).setTranslationKey("vine"));
        registerBlock(107, "fence_gate", new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("fenceGate"));
        registerBlock(108, "brick_stairs", new BlockStairs(block6.getDefaultState()).setTranslationKey("stairsBrick"));
        registerBlock(109, "stone_brick_stairs", new BlockStairs(block9.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)).setTranslationKey("stairsStoneBrickSmooth"));
        registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6f).setSoundType(SoundType.PLANT).setTranslationKey("mycel"));
        registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0f).setSoundType(SoundType.PLANT).setTranslationKey("waterlily"));
        final Block block11 = new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("netherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        registerBlock(112, "nether_brick", block11);
        registerBlock(113, "nether_brick_fence", new BlockFence(Material.ROCK, MapColor.NETHERRACK).setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("netherFence"));
        registerBlock(114, "nether_brick_stairs", new BlockStairs(block11.getDefaultState()).setTranslationKey("stairsNetherBrick"));
        registerBlock(115, "nether_wart", new BlockNetherWart().setTranslationKey("netherStalk"));
        registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0f).setResistance(2000.0f).setTranslationKey("enchantmentTable"));
        registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5f).setLightLevel(0.125f).setTranslationKey("brewingStand"));
        registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0f).setTranslationKey("cauldron"));
        registerBlock(119, "end_portal", new BlockEndPortal(Material.PORTAL).setHardness(-1.0f).setResistance(6000000.0f));
        registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setSoundType(SoundType.GLASS).setLightLevel(0.125f).setHardness(-1.0f).setTranslationKey("endPortalFrame").setResistance(6000000.0f).setCreativeTab(CreativeTabs.DECORATIONS));
        registerBlock(121, "end_stone", new Block(Material.ROCK, MapColor.SAND).setHardness(3.0f).setResistance(15.0f).setSoundType(SoundType.STONE).setTranslationKey("whiteStone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0f).setResistance(15.0f).setSoundType(SoundType.STONE).setLightLevel(0.125f).setTranslationKey("dragonEgg"));
        registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("redstoneLight").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("redstoneLight"));
        registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("woodSlab"));
        registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("woodSlab"));
        registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("cocoa"));
        registerBlock(128, "sandstone_stairs", new BlockStairs(block3.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setTranslationKey("stairsSandStone"));
        registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("oreEmerald"));
        registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5f).setResistance(1000.0f).setSoundType(SoundType.STONE).setTranslationKey("enderChest").setLightLevel(0.5f));
        registerBlock(131, "tripwire_hook", new BlockTripWireHook().setTranslationKey("tripWireSource"));
        registerBlock(132, "tripwire", new BlockTripWire().setTranslationKey("tripWire"));
        registerBlock(133, "emerald_block", new Block(Material.IRON, MapColor.EMERALD).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("blockEmerald").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(134, "spruce_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setTranslationKey("stairsWoodSpruce"));
        registerBlock(135, "birch_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setTranslationKey("stairsWoodBirch"));
        registerBlock(136, "jungle_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setTranslationKey("stairsWoodJungle"));
        registerBlock(137, "command_block", new BlockCommandBlock(MapColor.BROWN).setBlockUnbreakable().setResistance(6000000.0f).setTranslationKey("commandBlock"));
        registerBlock(138, "beacon", new BlockBeacon().setTranslationKey("beacon").setLightLevel(1.0f));
        registerBlock(139, "cobblestone_wall", new BlockWall(block).setTranslationKey("cobbleWall"));
        registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0f).setSoundType(SoundType.STONE).setTranslationKey("flowerPot"));
        registerBlock(141, "carrots", new BlockCarrot().setTranslationKey("carrots"));
        registerBlock(142, "potatoes", new BlockPotato().setTranslationKey("potatoes"));
        registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("button"));
        registerBlock(144, "skull", new BlockSkull().setHardness(1.0f).setSoundType(SoundType.STONE).setTranslationKey("skull"));
        registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0f).setSoundType(SoundType.ANVIL).setResistance(2000.0f).setTranslationKey("anvil"));
        registerBlock(146, "trapped_chest", new BlockChest(BlockChest.Type.TRAP).setHardness(2.5f).setSoundType(SoundType.WOOD).setTranslationKey("chestTrap"));
        registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.IRON, 15, MapColor.GOLD).setHardness(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("weightedPlate_light"));
        registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.IRON, 150).setHardness(0.5f).setSoundType(SoundType.WOOD).setTranslationKey("weightedPlate_heavy"));
        registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0f).setSoundType(SoundType.WOOD).setTranslationKey("comparator").disableStats());
        registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0f).setLightLevel(0.625f).setSoundType(SoundType.WOOD).setTranslationKey("comparator").disableStats());
        registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
        registerBlock(152, "redstone_block", new BlockCompressedPowered(Material.IRON, MapColor.TNT).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setTranslationKey("blockRedstone").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(153, "quartz_ore", new BlockOre(MapColor.NETHERRACK).setHardness(3.0f).setResistance(5.0f).setSoundType(SoundType.STONE).setTranslationKey("netherquartz"));
        registerBlock(154, "hopper", new BlockHopper().setHardness(3.0f).setResistance(8.0f).setSoundType(SoundType.METAL).setTranslationKey("hopper"));
        final Block block12 = new BlockQuartz().setSoundType(SoundType.STONE).setHardness(0.8f).setTranslationKey("quartzBlock");
        registerBlock(155, "quartz_block", block12);
        registerBlock(156, "quartz_stairs", new BlockStairs(block12.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setTranslationKey("stairsQuartz"));
        registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7f).setSoundType(SoundType.METAL).setTranslationKey("activatorRail"));
        registerBlock(158, "dropper", new BlockDropper().setHardness(3.5f).setSoundType(SoundType.STONE).setTranslationKey("dropper"));
        registerBlock(159, "stained_hardened_clay", new BlockStainedHardenedClay().setHardness(1.25f).setResistance(7.0f).setSoundType(SoundType.STONE).setTranslationKey("clayHardenedStained"));
        registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3f).setSoundType(SoundType.GLASS).setTranslationKey("thinStainedGlass"));
        registerBlock(161, "leaves2", new BlockNewLeaf().setTranslationKey("leaves"));
        registerBlock(162, "log2", new BlockNewLog().setTranslationKey("log"));
        registerBlock(163, "acacia_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setTranslationKey("stairsWoodAcacia"));
        registerBlock(164, "dark_oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setTranslationKey("stairsWoodDarkOak"));
        registerBlock(165, "slime", new BlockSlime().setTranslationKey("slime").setSoundType(SoundType.SLIME));
        registerBlock(166, "barrier", new BlockBarrier().setTranslationKey("barrier"));
        registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.IRON).setHardness(5.0f).setSoundType(SoundType.METAL).setTranslationKey("ironTrapdoor").disableStats());
        registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("prismarine"));
        registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.GLASS).setHardness(0.3f).setSoundType(SoundType.GLASS).setLightLevel(1.0f).setTranslationKey("seaLantern"));
        registerBlock(170, "hay_block", new BlockHay().setHardness(0.5f).setSoundType(SoundType.PLANT).setTranslationKey("hayBlock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1f).setSoundType(SoundType.CLOTH).setTranslationKey("woolCarpet").setLightOpacity(0));
        registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25f).setResistance(7.0f).setSoundType(SoundType.STONE).setTranslationKey("clayHardened"));
        registerBlock(173, "coal_block", new Block(Material.ROCK, MapColor.BLACK).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("blockCoal").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5f).setSoundType(SoundType.GLASS).setTranslationKey("icePacked"));
        registerBlock(175, "double_plant", new BlockDoublePlant());
        registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("banner").disableStats());
        registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("banner").disableStats());
        registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        final Block block13 = new BlockRedSandstone().setSoundType(SoundType.STONE).setHardness(0.8f).setTranslationKey("redSandStone");
        registerBlock(179, "red_sandstone", block13);
        registerBlock(180, "red_sandstone_stairs", new BlockStairs(block13.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)).setTranslationKey("stairsRedSandStone"));
        registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab2"));
        registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab2"));
        registerBlock(183, "spruce_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("spruceFenceGate"));
        registerBlock(184, "birch_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("birchFenceGate"));
        registerBlock(185, "jungle_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.JUNGLE).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("jungleFenceGate"));
        registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("darkOakFenceGate"));
        registerBlock(187, "acacia_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.ACACIA).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("acaciaFenceGate"));
        registerBlock(188, "spruce_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("spruceFence"));
        registerBlock(189, "birch_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("birchFence"));
        registerBlock(190, "jungle_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("jungleFence"));
        registerBlock(191, "dark_oak_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("darkOakFence"));
        registerBlock(192, "acacia_fence", new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.getMapColor()).setHardness(2.0f).setResistance(5.0f).setSoundType(SoundType.WOOD).setTranslationKey("acaciaFence"));
        registerBlock(193, "spruce_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorSpruce").disableStats());
        registerBlock(194, "birch_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorBirch").disableStats());
        registerBlock(195, "jungle_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorJungle").disableStats());
        registerBlock(196, "acacia_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorAcacia").disableStats());
        registerBlock(197, "dark_oak_door", new BlockDoor(Material.WOOD).setHardness(3.0f).setSoundType(SoundType.WOOD).setTranslationKey("doorDarkOak").disableStats());
        registerBlock(198, "end_rod", new BlockEndRod().setHardness(0.0f).setLightLevel(0.9375f).setSoundType(SoundType.WOOD).setTranslationKey("endRod"));
        registerBlock(199, "chorus_plant", new BlockChorusPlant().setHardness(0.4f).setSoundType(SoundType.WOOD).setTranslationKey("chorusPlant"));
        registerBlock(200, "chorus_flower", new BlockChorusFlower().setHardness(0.4f).setSoundType(SoundType.WOOD).setTranslationKey("chorusFlower"));
        final Block block14 = new Block(Material.ROCK, MapColor.MAGENTA).setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setTranslationKey("purpurBlock");
        registerBlock(201, "purpur_block", block14);
        registerBlock(202, "purpur_pillar", new BlockRotatedPillar(Material.ROCK, MapColor.MAGENTA).setHardness(1.5f).setResistance(10.0f).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setTranslationKey("purpurPillar"));
        registerBlock(203, "purpur_stairs", new BlockStairs(block14.getDefaultState()).setTranslationKey("stairsPurpur"));
        registerBlock(204, "purpur_double_slab", new BlockPurpurSlab.Double().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("purpurSlab"));
        registerBlock(205, "purpur_slab", new BlockPurpurSlab.Half().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("purpurSlab"));
        registerBlock(206, "end_bricks", new Block(Material.ROCK, MapColor.SAND).setSoundType(SoundType.STONE).setHardness(0.8f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setTranslationKey("endBricks"));
        registerBlock(207, "beetroots", new BlockBeetroot().setTranslationKey("beetroots"));
        final Block block15 = new BlockGrassPath().setHardness(0.65f).setSoundType(SoundType.PLANT).setTranslationKey("grassPath").disableStats();
        registerBlock(208, "grass_path", block15);
        registerBlock(209, "end_gateway", new BlockEndGateway(Material.PORTAL).setHardness(-1.0f).setResistance(6000000.0f));
        registerBlock(210, "repeating_command_block", new BlockCommandBlock(MapColor.PURPLE).setBlockUnbreakable().setResistance(6000000.0f).setTranslationKey("repeatingCommandBlock"));
        registerBlock(211, "chain_command_block", new BlockCommandBlock(MapColor.GREEN).setBlockUnbreakable().setResistance(6000000.0f).setTranslationKey("chainCommandBlock"));
        registerBlock(212, "frosted_ice", new BlockFrostedIce().setHardness(0.5f).setLightOpacity(3).setSoundType(SoundType.GLASS).setTranslationKey("frostedIce"));
        registerBlock(213, "magma", new BlockMagma().setHardness(0.5f).setSoundType(SoundType.STONE).setTranslationKey("magma"));
        registerBlock(214, "nether_wart_block", new Block(Material.GRASS, MapColor.RED).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(1.0f).setSoundType(SoundType.WOOD).setTranslationKey("netherWartBlock"));
        registerBlock(215, "red_nether_brick", new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setSoundType(SoundType.STONE).setTranslationKey("redNetherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(216, "bone_block", new BlockBone().setTranslationKey("boneBlock"));
        registerBlock(217, "structure_void", new BlockStructureVoid().setTranslationKey("structureVoid"));
        registerBlock(218, "observer", new BlockObserver().setHardness(3.0f).setTranslationKey("observer"));
        registerBlock(219, "white_shulker_box", new BlockShulkerBox(EnumDyeColor.WHITE).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxWhite"));
        registerBlock(220, "orange_shulker_box", new BlockShulkerBox(EnumDyeColor.ORANGE).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxOrange"));
        registerBlock(221, "magenta_shulker_box", new BlockShulkerBox(EnumDyeColor.MAGENTA).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxMagenta"));
        registerBlock(222, "light_blue_shulker_box", new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxLightBlue"));
        registerBlock(223, "yellow_shulker_box", new BlockShulkerBox(EnumDyeColor.YELLOW).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxYellow"));
        registerBlock(224, "lime_shulker_box", new BlockShulkerBox(EnumDyeColor.LIME).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxLime"));
        registerBlock(225, "pink_shulker_box", new BlockShulkerBox(EnumDyeColor.PINK).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxPink"));
        registerBlock(226, "gray_shulker_box", new BlockShulkerBox(EnumDyeColor.GRAY).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxGray"));
        registerBlock(227, "silver_shulker_box", new BlockShulkerBox(EnumDyeColor.SILVER).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxSilver"));
        registerBlock(228, "cyan_shulker_box", new BlockShulkerBox(EnumDyeColor.CYAN).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxCyan"));
        registerBlock(229, "purple_shulker_box", new BlockShulkerBox(EnumDyeColor.PURPLE).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxPurple"));
        registerBlock(230, "blue_shulker_box", new BlockShulkerBox(EnumDyeColor.BLUE).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBlue"));
        registerBlock(231, "brown_shulker_box", new BlockShulkerBox(EnumDyeColor.BROWN).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBrown"));
        registerBlock(232, "green_shulker_box", new BlockShulkerBox(EnumDyeColor.GREEN).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxGreen"));
        registerBlock(233, "red_shulker_box", new BlockShulkerBox(EnumDyeColor.RED).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxRed"));
        registerBlock(234, "black_shulker_box", new BlockShulkerBox(EnumDyeColor.BLACK).setHardness(2.0f).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBlack"));
        registerBlock(235, "white_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.WHITE));
        registerBlock(236, "orange_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
        registerBlock(237, "magenta_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
        registerBlock(238, "light_blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
        registerBlock(239, "yellow_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
        registerBlock(240, "lime_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIME));
        registerBlock(241, "pink_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PINK));
        registerBlock(242, "gray_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GRAY));
        registerBlock(243, "silver_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.SILVER));
        registerBlock(244, "cyan_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.CYAN));
        registerBlock(245, "purple_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
        registerBlock(246, "blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLUE));
        registerBlock(247, "brown_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BROWN));
        registerBlock(248, "green_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GREEN));
        registerBlock(249, "red_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.RED));
        registerBlock(250, "black_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLACK));
        registerBlock(251, "concrete", new BlockColored(Material.ROCK).setHardness(1.8f).setSoundType(SoundType.STONE).setTranslationKey("concrete"));
        registerBlock(252, "concrete_powder", new BlockConcretePowder().setHardness(0.5f).setSoundType(SoundType.SAND).setTranslationKey("concretePowder"));
        registerBlock(255, "structure_block", new BlockStructure().setBlockUnbreakable().setResistance(6000000.0f).setTranslationKey("structureBlock"));
        Block.REGISTRY.validateKey();
        for (final Block block16 : Block.REGISTRY) {
            if (block16.material == Material.AIR) {
                block16.useNeighborBrightness = false;
            }
            else {
                boolean flag = false;
                final boolean flag2 = block16 instanceof BlockStairs;
                final boolean flag3 = block16 instanceof BlockSlab;
                final boolean flag4 = block16 == block7 || block16 == block15;
                final boolean flag5 = block16.translucent;
                final boolean flag6 = block16.lightOpacity == 0;
                if (flag2 || flag3 || flag4 || flag5 || flag6) {
                    flag = true;
                }
                block16.useNeighborBrightness = flag;
            }
        }
        final Set<Block> set = (Set<Block>)Sets.newHashSet((Object[])new Block[] { Block.REGISTRY.getObject(new ResourceLocation("tripwire")) });
        for (final Block block17 : Block.REGISTRY) {
            if (set.contains(block17)) {
                for (int i = 0; i < 15; ++i) {
                    final int j = Block.REGISTRY.getIDForObject(block17) << 4 | i;
                    Block.BLOCK_STATE_IDS.put(block17.getStateFromMeta(i), j);
                }
            }
            else {
                for (final IBlockState iblockstate : block17.getBlockState().getValidStates()) {
                    final int k = Block.REGISTRY.getIDForObject(block17) << 4 | block17.getMetaFromState(iblockstate);
                    Block.BLOCK_STATE_IDS.put(iblockstate, k);
                }
            }
        }
    }
    
    private static void registerBlock(final int id, final ResourceLocation textualID, final Block block_) {
        Block.REGISTRY.register(id, textualID, block_);
    }
    
    private static void registerBlock(final int id, final String textualID, final Block block_) {
        registerBlock(id, new ResourceLocation(textualID), block_);
    }
    
    static {
        AIR_ID = new ResourceLocation("air");
        REGISTRY = new RegistryNamespacedDefaultedByKey<ResourceLocation, Block>(Block.AIR_ID);
        BLOCK_STATE_IDS = new ObjectIntIdentityMap<IBlockState>();
        FULL_BLOCK_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        NULL_AABB = null;
    }
    
    public enum EnumOffsetType
    {
        NONE, 
        XZ, 
        XYZ;
    }
}
