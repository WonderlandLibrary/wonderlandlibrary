package info.sigmaclient.sigma.gui.altmanager;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.profiler.*;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;
import net.minecraft.block.material.*;
import java.util.*;
import javax.annotation.*;
import net.minecraft.nbt.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.DefaultBiomeMagnifier;
import net.minecraft.world.biome.IBiomeMagnifier;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import net.minecraft.world.chunk.storage.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.*;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.function.*;
import java.util.stream.Stream;

import static net.minecraft.world.Difficulty.HARD;

public class FakeWorld extends ClientWorld {
    public FakeWorld(final FakeNetHandlerPlayClient netHandler) {
        super(
                netHandler,
                new ClientWorldInfo(HARD, false, false),
                RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("overworld")),
                new DimensionType(
                        OptionalLong.of(0),
                        false,
                        false,
                        false,
                        false,
                        0,
                        false,
                        false,
                        false,
                        false,
                        false,
                        0,
                        DefaultBiomeMagnifier.INSTANCE
                        ,
                        new ResourceLocation(""), new ResourceLocation(""), 0
                ),
                0,
                Minecraft.getInstance()::getProfiler,
                Minecraft.getInstance().worldRenderer,
                false,
                0
        );
//        Minecraft.getInstance().loadWorld(this);
    }


    protected boolean isChunkLoaded(final int i, final int i1, final boolean b) {
        return true;
    }

    public BlockPos getTopSolidOrLiquidBlock(final BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }

    public boolean isAirBlock(final BlockPos pos) {
        return true;
    }


    public boolean setBlockState(final BlockPos pos, final BlockState state) {
        return true;
    }

    public boolean setBlockToAir(final BlockPos pos) {
        return true;
    }

    public void markChunkDirty(final BlockPos pos, final TileEntity unusedTileEntity) {
    }

    public void notifyBlockUpdate(final BlockPos pos, final BlockState oldState, final BlockState newState, final int flags) {
    }

    public boolean destroyBlock(final BlockPos pos, final boolean dropBlock) {
        return true;
    }

    public void notifyNeighborsOfStateExcept(final BlockPos pos, final Block blockType, final Direction skipSide) {
    }

    public void notifyNeighborsRespectDebug(final BlockPos pos, final Block blockType, final boolean p_175722_3_) {
    }

    public void markAndNotifyBlock(final BlockPos pos, final Chunk chunk, final BlockState BlockState, final BlockState newState, final int flags) {
    }

    public void markBlocksDirtyVertical(final int par1, final int par2, final int par3, final int par4) {
    }

    public void markBlockRangeForRenderUpdate(final int p_147458_1_, final int p_147458_2_, final int p_147458_3_, final int p_147458_4_, final int p_147458_5_, final int p_147458_6_) {
    }

    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        return true;
    }

    public boolean getLightFromNeighbors(final BlockPos pos) {
        return false;
    }

    public int getLight(final BlockPos pos, final boolean checkNeighbors) {
        return 0;
    }

    public int getLight(final BlockPos pos) {
        return 0;
    }

    public boolean canBlockSeeSky(final BlockPos pos) {
        return true;
    }

    public BlockPos getHeight(final BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }


    public float getSunBrightness(final float p_72971_1_) {
        return 1.0f;
    }


    public boolean isDaytime() {
        return true;
    }


    public void onEntityAdded(final Entity par1Entity) {
    }

    public void onEntityRemoved(final Entity par1Entity) {
    }

    public void removeEntity(final Entity par1Entity) {
    }

    public void removeEntityDangerously(final Entity entityIn) {
    }

    public int calculateSkylightSubtracted(final float par1) {
        return 0;
    }

    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }

    public void updateEntities() {
    }

    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        if (forceUpdate) {
            ++entityIn.ticksExisted;
        }
    }

    public boolean checkNoEntityCollision(final AxisAlignedBB bb) {
        return true;
    }

    public boolean checkNoEntityCollision(final AxisAlignedBB bb, final Entity entityIn) {
        return true;
    }

    public boolean checkBlockCollision(final AxisAlignedBB bb) {
        return true;
    }

    public boolean containsAnyLiquid(final AxisAlignedBB bb) {
        return true;
    }

    public boolean handleMaterialAcceleration(final AxisAlignedBB par1AxisAlignedBB, final Material par2Material, final Entity par3Entity) {
        return true;
    }

    public boolean isMaterialInBB(final AxisAlignedBB par1AxisAlignedBB, final Material par2Material) {
        return true;
    }

    public TileEntity getTileEntity(final BlockPos pos) {
        return null;
    }


    public String getDebugLoadedEntities() {
        return "";
    }


    public String getProviderName() {
        return "";
    }

    public void setTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
    }

    public void removeTileEntity(final BlockPos pos) {
    }

    public void markTileEntityForRemoval(final TileEntity p_147457_1_) {
    }

    public boolean isBlockNormalCube(final BlockPos pos, final boolean _default) {
        return true;
    }

    public void tick() {
    }

    protected void updateWeather() {
    }

    public void updateWeatherBody() {
    }

    public boolean canBlockFreezeWater(final BlockPos pos) {
        return true;
    }

    public boolean canBlockFreezeNoWater(final BlockPos pos) {
        return true;
    }

    public boolean canBlockFreeze(final BlockPos pos, final boolean noWaterAdj) {
        return true;
    }

    public boolean canBlockFreezeBody(final BlockPos pos, final boolean noWaterAdj) {
        return true;
    }

    public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
        return true;
    }

    public boolean canSnowAtBody(final BlockPos pos, final boolean checkLight) {
        return true;
    }

    public boolean tickUpdates(final boolean par1) {
        return true;
    }

    public List getPendingBlockUpdates(final Chunk par1Chunk, final boolean par2) {
        return null;
    }

    public Entity findNearestEntityWithinAABB(final Class par1Class, final AxisAlignedBB par2AxisAlignedBB, final Entity par3Entity) {
        return null;
    }


    public int countEntities(final Class par1Class) {
        return 0;
    }

    public int getStrongPower(final BlockPos pos) {
        return 0;
    }

    public int getStrongPower(final BlockPos pos, final Direction direction) {
        return 0;
    }

    public boolean isSidePowered(final BlockPos pos, final Direction side) {
        return true;
    }

    public int getRedstonePower(final BlockPos pos, final Direction facing) {
        return 0;
    }

    public boolean isBlockPowered(final BlockPos pos) {
        return true;
    }

    public int isBlockIndirectlyGettingPowered(final BlockPos pos) {
        return 0;
    }

    public void checkSessionLock() {
    }

    public long getSeed() {
        return 1L;
    }

    public long getTotalWorldTime() {
        return 1L;
    }

    public long getWorldTime() {
        return 1L;
    }

    public void setWorldTime(final long par1) {
    }

    public BlockPos getSpawnPoint() {
        return new BlockPos(0, 64, 0);
    }


    public void joinEntityInSurroundings(final Entity par1Entity) {
    }

    public boolean canSeeSky(final BlockPos pos) {
        return true;
    }

    public void setEntityState(final Entity par1Entity, final byte par2) {
    }

    public float getThunderStrength(final float delta) {
        return 0.0f;
    }

    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
    }

    public void updateAllPlayersSleepingFlag() {
    }

    public boolean isRainingAt(final BlockPos strikePosition) {
        return true;
    }


    public void setThunderStrength(final float p_147442_1_) {
    }

    public float getRainStrength(final float par1) {
        return 0.0f;
    }


    public void setRainStrength(final float par1) {
    }

    public boolean isThundering() {
        return true;
    }

    public boolean isRaining() {
        return true;
    }

    public boolean isBlockinHighHumidity(final BlockPos pos) {
        return true;
    }

    public void setItemData(final String dataID, final WorldSavedData worldSavedDataIn) {
    }

    public void playBroadcastSound(final int p_175669_1_, final BlockPos pos, final int p_175669_3_) {
    }

    @Override
    public Iterable<Entity> getLoadedEntityList() {
        return super.getLoadedEntityList();
    }

    public FakeWorld(ClientPlayNetHandler p_i242067_1_, ClientWorldInfo p_i242067_2_, RegistryKey<World> p_i242067_3_, DimensionType p_i242067_4_, int p_i242067_5_, Supplier<IProfiler> p_i242067_6_, WorldRenderer p_i242067_7_, boolean p_i242067_8_, long p_i242067_9_) {
        super(p_i242067_1_, p_i242067_2_, p_i242067_3_, p_i242067_4_, p_i242067_5_, p_i242067_6_, p_i242067_7_, p_i242067_8_, p_i242067_9_);
    }

    @Override
    public DimensionRenderInfo func_239132_a_() {
        return super.func_239132_a_();
    }

    @Override
    public void tick(BooleanSupplier hasTimeLeft) {
        super.tick(hasTimeLeft);
    }

    @Override
    public void func_239134_a_(long p_239134_1_) {
        super.func_239134_a_(p_239134_1_);
    }

    @Override
    public void setDayTime(long time) {
        super.setDayTime(time);
    }

    @Override
    public Iterable<Entity> getAllEntities() {
        return super.getAllEntities();
    }

    @Override
    public void tickEntities() {
        super.tickEntities();
    }

    @Override
    public void updateEntity(Entity entityIn) {
        super.updateEntity(entityIn);
    }

    @Override
    public void updateEntityRidden(Entity p_217420_1_, Entity p_217420_2_) {
        super.updateEntityRidden(p_217420_1_, p_217420_2_);
    }

    @Override
    public void onChunkUnloaded(Chunk chunkIn) {
        super.onChunkUnloaded(chunkIn);
    }

    @Override
    public void onChunkLoaded(int chunkX, int chunkZ) {
        super.onChunkLoaded(chunkX, chunkZ);
    }

    @Override
    public void clearColorCaches() {
        super.clearColorCaches();
    }

    @Override
    public boolean chunkExists(int chunkX, int chunkZ) {
        return true;
    }

    @Override
    public void addPlayer(int playerId, AbstractClientPlayerEntity playerEntityIn) {
        super.addPlayer(playerId, playerEntityIn);
    }

    @Override
    public void addEntity(int entityIdIn, Entity entityToSpawn) {
        super.addEntity(entityIdIn, entityToSpawn);
    }

    @Override
    public void removeEntityFromWorld(int eid) {
        super.removeEntityFromWorld(eid);
    }

    @Override
    public void addEntitiesToChunk(Chunk chunkIn) {
        super.addEntitiesToChunk(chunkIn);
    }

    @Nullable
    @Override
    public Entity getEntityByID(int id) {
        return super.getEntityByID(id);
    }

    @Override
    public void invalidateRegionAndSetBlock(BlockPos pos, BlockState state) {
        super.invalidateRegionAndSetBlock(pos, state);
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        super.sendQuittingDisconnectingPacket();
    }

    @Override
    public void animateTick(int posX, int posY, int posZ) {
        super.animateTick(posX, posY, posZ);
    }

    @Override
    public void animateTick(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.Mutable pos) {
        super.animateTick(x, y, z, offset, random, holdingBarrier, pos);
    }

    @Override
    public void removeAllEntities() {
        super.removeAllEntities();
    }

    @Override
    public CrashReportCategory fillCrashReport(CrashReport report) {
        return super.fillCrashReport(report);
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        super.playSound(player, x, y, z, soundIn, category, volume, pitch);
    }

    @Override
    public void playMovingSound(@Nullable PlayerEntity playerIn, Entity entityIn, SoundEvent eventIn, SoundCategory categoryIn, float volume, float pitch) {
        super.playMovingSound(playerIn, entityIn, eventIn, categoryIn, volume, pitch);
    }

    @Override
    public void playSound(BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        super.playSound(pos, soundIn, category, volume, pitch, distanceDelay);
    }

    @Override
    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        super.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
    }

    @Override
    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable CompoundNBT compound) {
        super.makeFireworks(x, y, z, motionX, motionY, motionZ, compound);
    }

    @Override
    public void sendPacketToServer(IPacket<?> packetIn) {
        super.sendPacketToServer(packetIn);
    }

    @Override
    public RecipeManager getRecipeManager() {
        return super.getRecipeManager();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboardIn) {
        super.setScoreboard(scoreboardIn);
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return super.getPendingBlockTicks();
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return super.getPendingFluidTicks();
    }

    @Override
    public ClientChunkProvider getChunkProvider() {
        return super.getChunkProvider();
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        return true;
    }

    @Override
    public boolean isPlayerUpdate() {
        return true;
    }

    @Nullable
    @Override
    public MapData getMapData(String mapName) {
        return super.getMapData(mapName);
    }

    @Override
    public void registerMapData(MapData mapDataIn) {
        super.registerMapData(mapDataIn);
    }

    @Override
    public int getNextMapId() {
        return 0;
    }

    @Override
    public Scoreboard getScoreboard() {
        return super.getScoreboard();
    }

    @Override
    public ITagCollectionSupplier getTags() {
        return super.getTags();
    }

    @Override
    public DynamicRegistries func_241828_r() {
        return super.func_241828_r();
    }

    @Override
    public void markBlockRangeForRenderUpdate(BlockPos blockPosIn, BlockState oldState, BlockState newState) {
        super.markBlockRangeForRenderUpdate(blockPosIn, oldState, newState);
    }

    @Override
    public void markSurroundingsForRerender(int sectionX, int sectionY, int sectionZ) {
        super.markSurroundingsForRerender(sectionX, sectionY, sectionZ);
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        super.sendBlockBreakProgress(breakerId, pos, progress);
    }

    @Override
    public void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void addParticle(IParticleData particleData, boolean forceAlwaysRender, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super.addParticle(particleData, forceAlwaysRender, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void addOptionalParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super.addOptionalParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void addOptionalParticle(IParticleData particleData, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super.addOptionalParticle(particleData, ignoreRange, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public List<AbstractClientPlayerEntity> getPlayers() {
        return super.getPlayers();
    }

    @Override
    public Biome getNoiseBiomeRaw(int x, int y, int z) {
        return super.getNoiseBiomeRaw(x, y, z);
    }

    @Override
    public Vector3d getSkyColor(BlockPos blockPosIn, float partialTicks) {
        return super.getSkyColor(blockPosIn, partialTicks);
    }

    @Override
    public Vector3d getCloudColor(float partialTicks) {
        return super.getCloudColor(partialTicks);
    }

    @Override
    public float getStarBrightness(float partialTicks) {
        return super.getStarBrightness(partialTicks);
    }

    @Override
    public int getTimeLightningFlash() {
        return 0;
    }

    @Override
    public void setTimeLightningFlash(int timeFlashIn) {
        super.setTimeLightningFlash(timeFlashIn);
    }

    @Override
    public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
        return super.func_230487_a_(p_230487_1_, p_230487_2_);
    }

    @Override
    public int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn) {
        return 0;
    }

    @Override
    public int getBlockColorRaw(BlockPos blockPosIn, ColorResolver colorResolverIn) {
        return 0;
    }

    @Override
    public BlockPos func_239140_u_() {
        return super.func_239140_u_();
    }

    @Override
    public float func_243489_v() {
        return super.func_243489_v();
    }

    @Override
    public void func_239136_a_(BlockPos p_239136_1_, float p_239136_2_) {
        super.func_239136_a_(p_239136_1_, p_239136_2_);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public ClientWorldInfo getWorldInfo() {
        return super.getWorldInfo();
    }

    @Override
    public Stream<VoxelShape> func_230318_c_(@Nullable Entity p_230318_1_, AxisAlignedBB p_230318_2_, Predicate<Entity> p_230318_3_) {
        return super.func_230318_c_(p_230318_1_, p_230318_2_, p_230318_3_);
    }

    @Override
    public boolean checkNoEntityCollision(@Nullable Entity entityIn, VoxelShape shape) {
        return true;
    }

    @Override
    public BlockPos getHeight(Heightmap.Type heightmapType, BlockPos pos) {
        return super.getHeight(heightmapType, pos);
    }

    @Override
    public Optional<RegistryKey<Biome>> func_242406_i(BlockPos p_242406_1_) {
        return super.func_242406_i(p_242406_1_);
    }

    @Override
    public int getLightFor(LightType lightTypeIn, BlockPos blockPosIn) {
        return 0;
    }

    @Override
    public int getLightSubtracted(BlockPos blockPosIn, int amount) {
        return 0;
    }

    @Override
    public int getLightValue(BlockPos pos) {
        return 0;
    }

    @Override
    public int getMaxLightLevel() {
        return 0;
    }

    @Override
    public Stream<BlockState> func_234853_a_(AxisAlignedBB p_234853_1_) {
        return super.func_234853_a_(p_234853_1_);
    }

    @Override
    public BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
        return super.rayTraceBlocks(context);
    }

    @Nullable
    @Override
    public BlockRayTraceResult rayTraceBlocks(Vector3d startVec, Vector3d endVec, BlockPos pos, VoxelShape shape, BlockState state) {
        return super.rayTraceBlocks(startVec, endVec, pos, shape, state);
    }

    @Override
    public double func_242402_a(VoxelShape p_242402_1_, Supplier<VoxelShape> p_242402_2_) {
        return super.func_242402_a(p_242402_1_, p_242402_2_);
    }

    @Override
    public double func_242403_h(BlockPos p_242403_1_) {
        return super.func_242403_h(p_242403_1_);
    }

    @Override
    public boolean placedBlockCollides(BlockState state, BlockPos pos, ISelectionContext context) {
        return true;
    }

    @Override
    public boolean checkNoEntityCollision(Entity entity) {
        return true;
    }

    @Override
    public boolean hasNoCollisions(AxisAlignedBB aabb) {
        return true;
    }

    @Override
    public boolean hasNoCollisions(Entity entity) {
        return true;
    }

    @Override
    public boolean hasNoCollisions(Entity entity, AxisAlignedBB aabb) {
        return true;
    }

    @Override
    public boolean hasNoCollisions(@Nullable Entity entity, AxisAlignedBB aabb, Predicate<Entity> entityPredicate) {
        return true;
    }

    @Override
    public Stream<VoxelShape> func_234867_d_(@Nullable Entity entity, AxisAlignedBB aabb, Predicate<Entity> entityPredicate) {
        return super.func_234867_d_(entity, aabb, entityPredicate);
    }

    @Override
    public Stream<VoxelShape> getCollisionShapes(@Nullable Entity entity, AxisAlignedBB aabb) {
        return super.getCollisionShapes(entity, aabb);
    }

    @Override
    public boolean func_242405_a(@Nullable Entity p_242405_1_, AxisAlignedBB p_242405_2_, BiPredicate<BlockState, BlockPos> p_242405_3_) {
        return true;
    }

    @Override
    public Stream<VoxelShape> func_241457_a_(@Nullable Entity entity, AxisAlignedBB aabb, BiPredicate<BlockState, BlockPos> statePosPredicate) {
        return super.func_241457_a_(entity, aabb, statePosPredicate);
    }

    @Override
    public float getMoonFactor() {
        return super.getMoonFactor();
    }

    @Override
    public float func_242415_f(float p_242415_1_) {
        return super.func_242415_f(p_242415_1_);
    }

    @Override
    public int getMoonPhase() {
        return 0;
    }

    @Override
    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb) {
        return super.getEntitiesWithinAABBExcludingEntity(entityIn, bb);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> p_217357_1_, AxisAlignedBB p_217357_2_) {
        return super.getEntitiesWithinAABB(p_217357_1_, p_217357_2_);
    }

    @Override
    public <T extends Entity> List<T> getLoadedEntitiesWithinAABB(Class<? extends T> p_225317_1_, AxisAlignedBB p_225317_2_) {
        return super.getLoadedEntitiesWithinAABB(p_225317_1_, p_225317_2_);
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, @Nullable Predicate<Entity> predicate) {
        return super.getClosestPlayer(x, y, z, distance, predicate);
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(Entity entityIn, double distance) {
        return super.getClosestPlayer(entityIn, distance);
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, boolean creativePlayers) {
        return super.getClosestPlayer(x, y, z, distance, creativePlayers);
    }

    @Override
    public boolean isPlayerWithin(double x, double y, double z, double distance) {
        return true;
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target) {
        return super.getClosestPlayer(predicate, target);
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target, double p_217372_3_, double p_217372_5_, double p_217372_7_) {
        return super.getClosestPlayer(predicate, target, p_217372_3_, p_217372_5_, p_217372_7_);
    }

    @Nullable
    @Override
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, double x, double y, double z) {
        return super.getClosestPlayer(predicate, x, y, z);
    }

    @Nullable
    @Override
    public <T extends LivingEntity> T getClosestEntityWithinAABB(Class<? extends T> entityClazz, EntityPredicate p_217360_2_, @Nullable LivingEntity target, double x, double y, double z, AxisAlignedBB boundingBox) {
        return super.getClosestEntityWithinAABB(entityClazz, p_217360_2_, target, x, y, z, boundingBox);
    }

    @Nullable
    @Override
    public <T extends LivingEntity> T func_225318_b(Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, @Nullable LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_) {
        return super.func_225318_b(p_225318_1_, p_225318_2_, p_225318_3_, p_225318_4_, p_225318_6_, p_225318_8_, p_225318_10_);
    }

    @Nullable
    @Override
    public <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, EntityPredicate predicate, @Nullable LivingEntity target, double x, double y, double z) {
        return super.getClosestEntity(entities, predicate, target, x, y, z);
    }

    @Override
    public List<PlayerEntity> getTargettablePlayersWithinAABB(EntityPredicate predicate, LivingEntity target, AxisAlignedBB box) {
        return super.getTargettablePlayersWithinAABB(predicate, target, box);
    }

    @Override
    public <T extends LivingEntity> List<T> getTargettableEntitiesWithinAABB(Class<? extends T> p_217374_1_, EntityPredicate p_217374_2_, LivingEntity p_217374_3_, AxisAlignedBB p_217374_4_) {
        return super.getTargettableEntitiesWithinAABB(p_217374_1_, p_217374_2_, p_217374_3_, p_217374_4_);
    }

    @Nullable
    @Override
    public PlayerEntity getPlayerByUuid(UUID uniqueIdIn) {
        return super.getPlayerByUuid(uniqueIdIn);
    }

    @Override
    public long func_241851_ab() {
        return super.func_241851_ab();
    }

    @Override
    public Difficulty getDifficulty() {
        return super.getDifficulty();
    }

    @Override
    public void func_230547_a_(BlockPos p_230547_1_, Block p_230547_2_) {
        super.func_230547_a_(p_230547_1_, p_230547_2_);
    }

    @Override
    public int func_234938_ad_() {
        return 0;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return super.getBiome(pos);
    }

    @Override
    public Stream<BlockState> getStatesInArea(AxisAlignedBB aabb) {
        return super.getStatesInArea(aabb);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return super.getNoiseBiome(x, y, z);
    }

    @Override
    public float getBrightness(BlockPos pos) {
        return super.getBrightness(pos);
    }

    @Override
    public IChunk getChunk(BlockPos pos) {
        return super.getChunk(pos);
    }

    @Override
    public IChunk getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus) {
        return super.getChunk(chunkX, chunkZ, requiredStatus);
    }

    @Override
    public boolean hasWater(BlockPos pos) {
        return true;
    }

    @Override
    public int getNeighborAwareLightSubtracted(BlockPos pos, int amount) {
        return 0;
    }

    @Override
    public boolean isBlockLoaded(BlockPos pos) {
        return true;
    }

    @Override
    public boolean isAreaLoaded(BlockPos from, BlockPos to) {
        return true;
    }

    @Override
    public boolean isAreaLoaded(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        return true;
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, @Nullable Entity entity) {
        return true;
    }

    @Override
    public boolean addEntity(Entity entityIn) {
        return true;
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return super.getServer();
    }

    @Override
    public Chunk getChunkAt(BlockPos pos) {
        return super.getChunkAt(pos);
    }

    @Override
    public Chunk getChunk(int chunkX, int chunkZ) {
        return super.getChunk(chunkX, chunkZ);
    }

    @Override
    public IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull) {
        return super.getChunk(x, z, requiredStatus, nonnull);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        return true;
    }

    @Override
    public void onBlockStateChange(BlockPos pos, BlockState blockStateIn, BlockState newState) {
        super.onBlockStateChange(pos, blockStateIn, newState);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        return true;
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, @Nullable Entity entity, int recursionLeft) {
        return true;
    }

    @Override
    public void notifyNeighborsOfStateChange(BlockPos pos, Block blockIn) {
        super.notifyNeighborsOfStateChange(pos, blockIn);
    }

    @Override
    public void neighborChanged(BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(pos, blockIn, fromPos);
    }

    @Override
    public int getHeight(Heightmap.Type heightmapType, int x, int z) {
        return 0;
    }

    @Override
    public WorldLightManager getLightManager() {
        return super.getLightManager();
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return super.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return super.getFluidState(pos);
    }

    @Override
    public boolean isNightTime() {
        return true;
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        super.playSound(player, pos, soundIn, category, volume, pitch);
    }

    @Override
    public float getCelestialAngleRadians(float partialTicks) {
        return super.getCelestialAngleRadians(partialTicks);
    }

    @Override
    public void addTileEntities(Collection<TileEntity> tileEntityCollection) {
        super.addTileEntities(tileEntityCollection);
    }

    @Override
    public void tickBlockEntities() {
        super.tickBlockEntities();
    }

    @Override
    public void guardEntityTick(Consumer<Entity> consumerEntity, Entity entityIn) {
        super.guardEntityTick(consumerEntity, entityIn);
    }

    @Override
    public Explosion createExplosion(@Nullable Entity entityIn, double xIn, double yIn, double zIn, float explosionRadius, Explosion.Mode modeIn) {
        return super.createExplosion(entityIn, xIn, yIn, zIn, explosionRadius, modeIn);
    }

    @Override
    public Explosion createExplosion(@Nullable Entity entityIn, double xIn, double yIn, double zIn, float explosionRadius, boolean causesFire, Explosion.Mode modeIn) {
        return super.createExplosion(entityIn, xIn, yIn, zIn, explosionRadius, causesFire, modeIn);
    }

    @Override
    public Explosion createExplosion(@Nullable Entity exploder, @Nullable DamageSource damageSource, @Nullable ExplosionContext context, double x, double y, double z, float size, boolean causesFire, Explosion.Mode mode) {
        return super.createExplosion(exploder, damageSource, context, x, y, z, size, causesFire, mode);
    }

    @Override
    public boolean isBlockPresent(BlockPos pos) {
        return true;
    }

    @Override
    public boolean isDirectionSolid(BlockPos pos, Entity entity, Direction direction) {
        return true;
    }

    @Override
    public boolean isTopSolid(BlockPos pos, Entity entityIn) {
        return true;
    }

    @Override
    public void calculateInitialSkylight() {
        super.calculateInitialSkylight();
    }

    @Override
    public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
        super.setAllowedSpawnTypes(hostile, peaceful);
    }

    @Override
    protected void calculateInitialWeather() {
        super.calculateInitialWeather();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Nullable
    @Override
    public IBlockReader getBlockReader(int chunkX, int chunkZ) {
        return super.getBlockReader(chunkX, chunkZ);
    }

    @Override
    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        return super.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(@Nullable EntityType<T> type, AxisAlignedBB boundingBox, Predicate<? super T> predicate) {
        return super.getEntitiesWithinAABB(type, boundingBox, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
        return super.getEntitiesWithinAABB(clazz, aabb, filter);
    }

    @Override
    public <T extends Entity> List<T> getLoadedEntitiesWithinAABB(Class<? extends T> p_225316_1_, AxisAlignedBB p_225316_2_, @Nullable Predicate<? super T> p_225316_3_) {
        return super.getLoadedEntitiesWithinAABB(p_225316_1_, p_225316_2_, p_225316_3_);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getRedstonePowerFromNeighbors(BlockPos pos) {
        return 0;
    }

    @Override
    public long getGameTime() {
        return super.getGameTime();
    }

    @Override
    public long getDayTime() {
        return super.getDayTime();
    }

    @Override
    public boolean isBlockModifiable(PlayerEntity player, BlockPos pos) {
        return true;
    }

    @Override
    public GameRules getGameRules() {
        return super.getGameRules();
    }

    @Override
    public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {
        super.updateComparatorOutputLevel(pos, blockIn);
    }

    @Override
    public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
        return super.getDifficultyForLocation(pos);
    }

    @Override
    public int getSkylightSubtracted() {
        return 0;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return super.getWorldBorder();
    }

    @Override
    public DimensionType getDimensionType() {
        return super.getDimensionType();
    }

    @Override
    public RegistryKey<World> getDimensionKey() {
        return super.getDimensionKey();
    }

    @Override
    public Random getRandom() {
        return super.getRandom();
    }

    @Override
    public boolean hasBlockState(BlockPos pos, Predicate<BlockState> state) {
        return true;
    }

    @Override
    public BlockPos getBlockRandomPos(int x, int y, int z, int yMask) {
        return super.getBlockRandomPos(x, y, z, yMask);
    }

    @Override
    public boolean isSaveDisabled() {
        return true;
    }

    @Override
    public IProfiler getProfiler() {
        return super.getProfiler();
    }

    @Override
    public Supplier<IProfiler> getWorldProfiler() {
        return super.getWorldProfiler();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return super.getBiomeManager();
    }

    public void playEvent(final PlayerEntity player, final int type, final BlockPos pos, final int data) {
    }

    public void playEvent(final int type, final BlockPos pos, final int data) {
    }

    public int getHeight() {
        return 0;
    }

    public int getActualHeight() {
        return 0;
    }


    public boolean addTileEntity(final TileEntity tile) {
        return true;
    }


    public boolean isSideSolid(final BlockPos pos, final Direction side) {
        return true;
    }

    public boolean isSideSolid(final BlockPos pos, final Direction side, final boolean _default) {
        return true;
    }

    @Override
    public int getCountLoadedEntities() {
        return 0;
    }


    public Chunk getChunkFromChunkCoords(final int par1, final int par2) {
        return null;
    }
}