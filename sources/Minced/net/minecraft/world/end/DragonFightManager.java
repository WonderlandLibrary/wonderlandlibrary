// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.end;

import com.google.common.base.Predicates;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.biome.BiomeEndDecorator;
import java.util.Set;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.boss.EntityDragon;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import java.util.Collections;
import java.util.Random;
import java.util.Collection;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import net.minecraft.nbt.NBTUtil;
import com.google.common.collect.Lists;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import net.minecraft.block.state.pattern.BlockPattern;
import java.util.List;
import net.minecraft.world.WorldServer;
import net.minecraft.world.BossInfoServer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;
import org.apache.logging.log4j.Logger;

public class DragonFightManager
{
    private static final Logger LOGGER;
    private static final Predicate<EntityPlayerMP> VALID_PLAYER;
    private final BossInfoServer bossInfo;
    private final WorldServer world;
    private final List<Integer> gateways;
    private final BlockPattern portalPattern;
    private int ticksSinceDragonSeen;
    private int aliveCrystals;
    private int ticksSinceCrystalsScanned;
    private int ticksSinceLastPlayerScan;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    private UUID dragonUniqueId;
    private boolean scanForLegacyFight;
    private BlockPos exitPortalLocation;
    private DragonSpawnManager respawnState;
    private int respawnStateTicks;
    private List<EntityEnderCrystal> crystals;
    
    public DragonFightManager(final WorldServer worldIn, final NBTTagCompound compound) {
        this.bossInfo = (BossInfoServer)new BossInfoServer(new TextComponentTranslation("entity.EnderDragon.name", new Object[0]), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS).setPlayEndBossMusic(true).setCreateFog(true);
        this.gateways = (List<Integer>)Lists.newArrayList();
        this.scanForLegacyFight = true;
        this.world = worldIn;
        if (compound.hasKey("DragonKilled", 99)) {
            if (compound.hasUniqueId("DragonUUID")) {
                this.dragonUniqueId = compound.getUniqueId("DragonUUID");
            }
            this.dragonKilled = compound.getBoolean("DragonKilled");
            this.previouslyKilled = compound.getBoolean("PreviouslyKilled");
            if (compound.getBoolean("IsRespawning")) {
                this.respawnState = DragonSpawnManager.START;
            }
            if (compound.hasKey("ExitPortalLocation", 10)) {
                this.exitPortalLocation = NBTUtil.getPosFromTag(compound.getCompoundTag("ExitPortalLocation"));
            }
        }
        else {
            this.dragonKilled = true;
            this.previouslyKilled = true;
        }
        if (compound.hasKey("Gateways", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Gateways", 3);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.gateways.add(nbttaglist.getIntAt(i));
            }
        }
        else {
            this.gateways.addAll((Collection<? extends Integer>)ContiguousSet.create(Range.closedOpen((Comparable)0, (Comparable)20), DiscreteDomain.integers()));
            Collections.shuffle(this.gateways, new Random(worldIn.getSeed()));
        }
        this.portalPattern = FactoryBlockPattern.start().aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockMatcher.forBlock(Blocks.BEDROCK))).build();
    }
    
    public NBTTagCompound getCompound() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.dragonUniqueId != null) {
            nbttagcompound.setUniqueId("DragonUUID", this.dragonUniqueId);
        }
        nbttagcompound.setBoolean("DragonKilled", this.dragonKilled);
        nbttagcompound.setBoolean("PreviouslyKilled", this.previouslyKilled);
        if (this.exitPortalLocation != null) {
            nbttagcompound.setTag("ExitPortalLocation", NBTUtil.createPosTag(this.exitPortalLocation));
        }
        final NBTTagList nbttaglist = new NBTTagList();
        for (final int i : this.gateways) {
            nbttaglist.appendTag(new NBTTagInt(i));
        }
        nbttagcompound.setTag("Gateways", nbttaglist);
        return nbttagcompound;
    }
    
    public void tick() {
        this.bossInfo.setVisible(!this.dragonKilled);
        if (++this.ticksSinceLastPlayerScan >= 20) {
            this.updatePlayers();
            this.ticksSinceLastPlayerScan = 0;
        }
        if (!this.bossInfo.getPlayers().isEmpty()) {
            if (this.scanForLegacyFight) {
                DragonFightManager.LOGGER.info("Scanning for legacy world dragon fight...");
                this.loadChunks();
                this.scanForLegacyFight = false;
                final boolean flag = this.hasDragonBeenKilled();
                if (flag) {
                    DragonFightManager.LOGGER.info("Found that the dragon has been killed in this world already.");
                    this.previouslyKilled = true;
                }
                else {
                    DragonFightManager.LOGGER.info("Found that the dragon has not yet been killed in this world.");
                    this.generatePortal(this.previouslyKilled = false);
                }
                final List<EntityDragon> list = this.world.getEntities((Class<? extends EntityDragon>)EntityDragon.class, (com.google.common.base.Predicate<? super EntityDragon>)EntitySelectors.IS_ALIVE);
                if (list.isEmpty()) {
                    this.dragonKilled = true;
                }
                else {
                    final EntityDragon entitydragon = list.get(0);
                    this.dragonUniqueId = entitydragon.getUniqueID();
                    DragonFightManager.LOGGER.info("Found that there's a dragon still alive ({})", (Object)entitydragon);
                    this.dragonKilled = false;
                    if (!flag) {
                        DragonFightManager.LOGGER.info("But we didn't have a portal, let's remove it.");
                        entitydragon.setDead();
                        this.dragonUniqueId = null;
                    }
                }
                if (!this.previouslyKilled && this.dragonKilled) {
                    this.dragonKilled = false;
                }
            }
            if (this.respawnState != null) {
                if (this.crystals == null) {
                    this.respawnState = null;
                    this.respawnDragon();
                }
                this.respawnState.process(this.world, this, this.crystals, this.respawnStateTicks++, this.exitPortalLocation);
            }
            if (!this.dragonKilled) {
                if (this.dragonUniqueId == null || ++this.ticksSinceDragonSeen >= 1200) {
                    this.loadChunks();
                    final List<EntityDragon> list2 = this.world.getEntities((Class<? extends EntityDragon>)EntityDragon.class, (com.google.common.base.Predicate<? super EntityDragon>)EntitySelectors.IS_ALIVE);
                    if (list2.isEmpty()) {
                        DragonFightManager.LOGGER.debug("Haven't seen the dragon, respawning it");
                        this.createNewDragon();
                    }
                    else {
                        DragonFightManager.LOGGER.debug("Haven't seen our dragon, but found another one to use.");
                        this.dragonUniqueId = list2.get(0).getUniqueID();
                    }
                    this.ticksSinceDragonSeen = 0;
                }
                if (++this.ticksSinceCrystalsScanned >= 100) {
                    this.findAliveCrystals();
                    this.ticksSinceCrystalsScanned = 0;
                }
            }
        }
    }
    
    protected void setRespawnState(final DragonSpawnManager state) {
        if (this.respawnState == null) {
            throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
        }
        this.respawnStateTicks = 0;
        if (state == DragonSpawnManager.END) {
            this.respawnState = null;
            this.dragonKilled = false;
            final EntityDragon entitydragon = this.createNewDragon();
            for (final EntityPlayerMP entityplayermp : this.bossInfo.getPlayers()) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitydragon);
            }
        }
        else {
            this.respawnState = state;
        }
    }
    
    private boolean hasDragonBeenKilled() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = -8; j <= 8; ++j) {
                final Chunk chunk = this.world.getChunk(i, j);
                for (final TileEntity tileentity : chunk.getTileEntityMap().values()) {
                    if (tileentity instanceof TileEntityEndPortal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Nullable
    private BlockPattern.PatternHelper findExitPortal() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = -8; j <= 8; ++j) {
                final Chunk chunk = this.world.getChunk(i, j);
                for (final TileEntity tileentity : chunk.getTileEntityMap().values()) {
                    if (tileentity instanceof TileEntityEndPortal) {
                        final BlockPattern.PatternHelper blockpattern$patternhelper = this.portalPattern.match(this.world, tileentity.getPos());
                        if (blockpattern$patternhelper != null) {
                            final BlockPos blockpos = blockpattern$patternhelper.translateOffset(3, 3, 3).getPos();
                            if (this.exitPortalLocation == null && blockpos.getX() == 0 && blockpos.getZ() == 0) {
                                this.exitPortalLocation = blockpos;
                            }
                            return blockpattern$patternhelper;
                        }
                        continue;
                    }
                }
            }
        }
        int l;
        for (int k = l = this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION).getY(); l >= 0; --l) {
            final BlockPattern.PatternHelper blockpattern$patternhelper2 = this.portalPattern.match(this.world, new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX(), l, WorldGenEndPodium.END_PODIUM_LOCATION.getZ()));
            if (blockpattern$patternhelper2 != null) {
                if (this.exitPortalLocation == null) {
                    this.exitPortalLocation = blockpattern$patternhelper2.translateOffset(3, 3, 3).getPos();
                }
                return blockpattern$patternhelper2;
            }
        }
        return null;
    }
    
    private void loadChunks() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = -8; j <= 8; ++j) {
                this.world.getChunk(i, j);
            }
        }
    }
    
    private void updatePlayers() {
        final Set<EntityPlayerMP> set = (Set<EntityPlayerMP>)Sets.newHashSet();
        for (final EntityPlayerMP entityplayermp : this.world.getPlayers((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, (com.google.common.base.Predicate<? super EntityPlayerMP>)DragonFightManager.VALID_PLAYER)) {
            this.bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }
        final Set<EntityPlayerMP> set2 = (Set<EntityPlayerMP>)Sets.newHashSet((Iterable)this.bossInfo.getPlayers());
        set2.removeAll(set);
        for (final EntityPlayerMP entityplayermp2 : set2) {
            this.bossInfo.removePlayer(entityplayermp2);
        }
    }
    
    private void findAliveCrystals() {
        this.ticksSinceCrystalsScanned = 0;
        this.aliveCrystals = 0;
        for (final WorldGenSpikes.EndSpike worldgenspikes$endspike : BiomeEndDecorator.getSpikesForWorld(this.world)) {
            this.aliveCrystals += this.world.getEntitiesWithinAABB((Class<? extends Entity>)EntityEnderCrystal.class, worldgenspikes$endspike.getTopBoundingBox()).size();
        }
        DragonFightManager.LOGGER.debug("Found {} end crystals still alive", (Object)this.aliveCrystals);
    }
    
    public void processDragonDeath(final EntityDragon dragon) {
        if (dragon.getUniqueID().equals(this.dragonUniqueId)) {
            this.bossInfo.setPercent(0.0f);
            this.bossInfo.setVisible(false);
            this.generatePortal(true);
            this.spawnNewGateway();
            if (!this.previouslyKilled) {
                this.world.setBlockState(this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.getDefaultState());
            }
            this.previouslyKilled = true;
            this.dragonKilled = true;
        }
    }
    
    private void spawnNewGateway() {
        if (!this.gateways.isEmpty()) {
            final int i = this.gateways.remove(this.gateways.size() - 1);
            final int j = (int)(96.0 * Math.cos(2.0 * (-3.141592653589793 + 0.15707963267948966 * i)));
            final int k = (int)(96.0 * Math.sin(2.0 * (-3.141592653589793 + 0.15707963267948966 * i)));
            this.generateGateway(new BlockPos(j, 75, k));
        }
    }
    
    private void generateGateway(final BlockPos pos) {
        this.world.playEvent(3000, pos, 0);
        new WorldGenEndGateway().generate(this.world, new Random(), pos);
    }
    
    private void generatePortal(final boolean active) {
        final WorldGenEndPodium worldgenendpodium = new WorldGenEndPodium(active);
        if (this.exitPortalLocation == null) {
            this.exitPortalLocation = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION).down();
            while (this.world.getBlockState(this.exitPortalLocation).getBlock() == Blocks.BEDROCK && this.exitPortalLocation.getY() > this.world.getSeaLevel()) {
                this.exitPortalLocation = this.exitPortalLocation.down();
            }
        }
        worldgenendpodium.generate(this.world, new Random(), this.exitPortalLocation);
    }
    
    private EntityDragon createNewDragon() {
        this.world.getChunk(new BlockPos(0, 128, 0));
        final EntityDragon entitydragon = new EntityDragon(this.world);
        entitydragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        entitydragon.setLocationAndAngles(0.0, 128.0, 0.0, this.world.rand.nextFloat() * 360.0f, 0.0f);
        this.world.spawnEntity(entitydragon);
        this.dragonUniqueId = entitydragon.getUniqueID();
        return entitydragon;
    }
    
    public void dragonUpdate(final EntityDragon dragonIn) {
        if (dragonIn.getUniqueID().equals(this.dragonUniqueId)) {
            this.bossInfo.setPercent(dragonIn.getHealth() / dragonIn.getMaxHealth());
            this.ticksSinceDragonSeen = 0;
            if (dragonIn.hasCustomName()) {
                this.bossInfo.setName(dragonIn.getDisplayName());
            }
        }
    }
    
    public int getNumAliveCrystals() {
        return this.aliveCrystals;
    }
    
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final DamageSource dmgSrc) {
        if (this.respawnState != null && this.crystals.contains(crystal)) {
            DragonFightManager.LOGGER.debug("Aborting respawn sequence");
            this.respawnState = null;
            this.respawnStateTicks = 0;
            this.resetSpikeCrystals();
            this.generatePortal(true);
        }
        else {
            this.findAliveCrystals();
            final Entity entity = this.world.getEntityFromUuid(this.dragonUniqueId);
            if (entity instanceof EntityDragon) {
                ((EntityDragon)entity).onCrystalDestroyed(crystal, new BlockPos(crystal), dmgSrc);
            }
        }
    }
    
    public boolean hasPreviouslyKilledDragon() {
        return this.previouslyKilled;
    }
    
    public void respawnDragon() {
        if (this.dragonKilled && this.respawnState == null) {
            BlockPos blockpos = this.exitPortalLocation;
            if (blockpos == null) {
                DragonFightManager.LOGGER.debug("Tried to respawn, but need to find the portal first.");
                final BlockPattern.PatternHelper blockpattern$patternhelper = this.findExitPortal();
                if (blockpattern$patternhelper == null) {
                    DragonFightManager.LOGGER.debug("Couldn't find a portal, so we made one.");
                    this.generatePortal(true);
                }
                else {
                    DragonFightManager.LOGGER.debug("Found the exit portal & temporarily using it.");
                }
                blockpos = this.exitPortalLocation;
            }
            final List<EntityEnderCrystal> list1 = (List<EntityEnderCrystal>)Lists.newArrayList();
            final BlockPos blockpos2 = blockpos.up(1);
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                final List<EntityEnderCrystal> list2 = this.world.getEntitiesWithinAABB((Class<? extends EntityEnderCrystal>)EntityEnderCrystal.class, new AxisAlignedBB(blockpos2.offset(enumfacing, 2)));
                if (list2.isEmpty()) {
                    return;
                }
                list1.addAll(list2);
            }
            DragonFightManager.LOGGER.debug("Found all crystals, respawning dragon.");
            this.respawnDragon(list1);
        }
    }
    
    private void respawnDragon(final List<EntityEnderCrystal> crystalsIn) {
        if (this.dragonKilled && this.respawnState == null) {
            for (BlockPattern.PatternHelper blockpattern$patternhelper = this.findExitPortal(); blockpattern$patternhelper != null; blockpattern$patternhelper = this.findExitPortal()) {
                for (int i = 0; i < this.portalPattern.getPalmLength(); ++i) {
                    for (int j = 0; j < this.portalPattern.getThumbLength(); ++j) {
                        for (int k = 0; k < this.portalPattern.getFingerLength(); ++k) {
                            final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, k);
                            if (blockworldstate.getBlockState().getBlock() == Blocks.BEDROCK || blockworldstate.getBlockState().getBlock() == Blocks.END_PORTAL) {
                                this.world.setBlockState(blockworldstate.getPos(), Blocks.END_STONE.getDefaultState());
                            }
                        }
                    }
                }
            }
            this.respawnState = DragonSpawnManager.START;
            this.respawnStateTicks = 0;
            this.generatePortal(false);
            this.crystals = crystalsIn;
        }
    }
    
    public void resetSpikeCrystals() {
        for (final WorldGenSpikes.EndSpike worldgenspikes$endspike : BiomeEndDecorator.getSpikesForWorld(this.world)) {
            for (final EntityEnderCrystal entityendercrystal : this.world.getEntitiesWithinAABB((Class<? extends EntityEnderCrystal>)EntityEnderCrystal.class, worldgenspikes$endspike.getTopBoundingBox())) {
                entityendercrystal.setEntityInvulnerable(false);
                entityendercrystal.setBeamTarget(null);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        VALID_PLAYER = Predicates.and((Predicate)EntitySelectors.IS_ALIVE, (Predicate)EntitySelectors.withinRange(0.0, 128.0, 0.0, 192.0));
    }
}
