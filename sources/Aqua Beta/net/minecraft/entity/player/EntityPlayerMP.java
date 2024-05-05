package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayerMP
extends EntityPlayer
implements ICrafting {
    private static final Logger logger = LogManager.getLogger();
    private String translator = "en_US";
    public NetHandlerPlayServer playerNetServerHandler;
    public final MinecraftServer mcServer;
    public final ItemInWorldManager theItemInWorldManager;
    public double managedPosX;
    public double managedPosZ;
    public final List<ChunkCoordIntPair> loadedChunks = Lists.newLinkedList();
    private final List<Integer> destroyedItemsNetCache = Lists.newLinkedList();
    private final StatisticsFile statsFile;
    private float combinedHealth = Float.MIN_VALUE;
    private float lastHealth = -1.0E8f;
    private int lastFoodLevel = -99999999;
    private boolean wasHungry = true;
    private int lastExperience = -99999999;
    private int respawnInvulnerabilityTicks = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    private Entity spectatingEntity = null;
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean playerConqueredTheEnd;

    public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager) {
        super((World)worldIn, profile);
        interactionManager.thisPlayerMP = this;
        this.theItemInWorldManager = interactionManager;
        BlockPos blockpos = worldIn.getSpawnPoint();
        if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE) {
            int i = Math.max((int)5, (int)(server.getSpawnProtectionSize() - 6));
            int j = MathHelper.floor_double((double)worldIn.getWorldBorder().getClosestDistance((double)blockpos.getX(), (double)blockpos.getZ()));
            if (j < i) {
                i = j;
            }
            if (j <= 1) {
                i = 1;
            }
            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2) - i, 0, this.rand.nextInt(i * 2) - i));
        }
        this.mcServer = server;
        this.statsFile = server.getConfigurationManager().getPlayerStatsFile((EntityPlayer)this);
        this.stepHeight = 0.0f;
        this.moveToBlockPosAndAngles(blockpos, 0.0f, 0.0f);
        while (!worldIn.getCollidingBoundingBoxes((Entity)this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0) {
            this.setPosition(this.posX, this.posY + 1.0, this.posZ);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("playerGameType", 99)) {
            if (MinecraftServer.getServer().getForceGamemode()) {
                this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
            } else {
                this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID((int)tagCompund.getInteger("playerGameType")));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
    }

    public void addExperienceLevel(int levels) {
        super.addExperienceLevel(levels);
        this.lastExperience = -1;
    }

    public void removeExperienceLevel(int levels) {
        super.removeExperienceLevel(levels);
        this.lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory() {
        this.openContainer.onCraftGuiOpened((ICrafting)this);
    }

    public void sendEnterCombat() {
        super.sendEnterCombat();
        this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
    }

    public void sendEndCombat() {
        super.sendEndCombat();
        this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
    }

    public void onUpdate() {
        Entity entity;
        this.theItemInWorldManager.updateBlockRemoving();
        --this.respawnInvulnerabilityTicks;
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }
        this.openContainer.detectAndSendChanges();
        if (!this.worldObj.isRemote && !this.openContainer.canInteractWith((EntityPlayer)this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        while (!this.destroyedItemsNetCache.isEmpty()) {
            int i = Math.min((int)this.destroyedItemsNetCache.size(), (int)Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator iterator = this.destroyedItemsNetCache.iterator();
            int j = 0;
            while (iterator.hasNext() && j < i) {
                aint[j++] = (Integer)iterator.next();
                iterator.remove();
            }
            this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(aint));
        }
        if (!this.loadedChunks.isEmpty()) {
            ArrayList list = Lists.newArrayList();
            Iterator iterator1 = this.loadedChunks.iterator();
            ArrayList list1 = Lists.newArrayList();
            while (iterator1.hasNext() && list.size() < 10) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator1.next();
                if (chunkcoordintpair != null) {
                    Chunk chunk;
                    if (!this.worldObj.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4)) || !(chunk = this.worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos)).isPopulated()) continue;
                    list.add((Object)chunk);
                    list1.addAll((Collection)((WorldServer)this.worldObj).getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
                    iterator1.remove();
                    continue;
                }
                iterator1.remove();
            }
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    this.playerNetServerHandler.sendPacket((Packet)new S21PacketChunkData((Chunk)list.get(0), true, 65535));
                } else {
                    this.playerNetServerHandler.sendPacket((Packet)new S26PacketMapChunkBulk((List)list));
                }
                for (TileEntity tileentity : list1) {
                    this.sendTileEntityUpdate(tileentity);
                }
                for (Chunk chunk1 : list) {
                    this.getServerForPlayer().getEntityTracker().func_85172_a(this, chunk1);
                }
            }
        }
        if ((entity = this.getSpectatingEntity()) != this) {
            if (!entity.isEntityAlive()) {
                this.setSpectatingEntity((Entity)this);
            } else {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
                if (this.isSneaking()) {
                    this.setSpectatingEntity((Entity)this);
                }
            }
        }
    }

    public void onUpdateEntity() {
        try {
            super.onUpdate();
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                Packet packet;
                ItemStack itemstack = this.inventory.getStackInSlot(i);
                if (itemstack == null || !itemstack.getItem().isMap() || (packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.worldObj, (EntityPlayer)this)) == null) continue;
                this.playerNetServerHandler.sendPacket(packet);
            }
            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0f != this.wasHungry) {
                this.playerNetServerHandler.sendPacket((Packet)new S06PacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                boolean bl = this.wasHungry = this.foodStats.getSaturationLevel() == 0.0f;
            }
            if (this.getHealth() + this.getAbsorptionAmount() != this.combinedHealth) {
                this.combinedHealth = this.getHealth() + this.getAbsorptionAmount();
                for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health)) {
                    this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).func_96651_a(Arrays.asList((Object[])new EntityPlayer[]{this}));
                }
            }
            if (this.experienceTotal != this.lastExperience) {
                this.lastExperience = this.experienceTotal;
                this.playerNetServerHandler.sendPacket((Packet)new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }
            if (this.ticksExisted % 20 * 5 == 0 && !this.getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes)) {
                this.updateBiomesExplored();
            }
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Ticking player");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected void updateBiomesExplored() {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double((double)this.posX), 0, MathHelper.floor_double((double)this.posZ)));
        String s = biomegenbase.biomeName;
        JsonSerializableSet jsonserializableset = (JsonSerializableSet)this.getStatFile().func_150870_b((StatBase)AchievementList.exploreAllBiomes);
        if (jsonserializableset == null) {
            jsonserializableset = (JsonSerializableSet)this.getStatFile().func_150872_a((StatBase)AchievementList.exploreAllBiomes, (IJsonSerializable)new JsonSerializableSet());
        }
        jsonserializableset.add((Object)s);
        if (this.getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()) {
            HashSet set = Sets.newHashSet((Iterable)BiomeGenBase.explorationBiomesList);
            for (String s1 : jsonserializableset) {
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    BiomeGenBase biomegenbase1 = (BiomeGenBase)iterator.next();
                    if (!biomegenbase1.biomeName.equals((Object)s1)) continue;
                    iterator.remove();
                }
                if (!set.isEmpty()) continue;
                break;
            }
            if (set.isEmpty()) {
                this.triggerAchievement((StatBase)AchievementList.exploreAllBiomes);
            }
        }
    }

    public void onDeath(DamageSource cause) {
        if (this.worldObj.getGameRules().getBoolean("showDeathMessages")) {
            Team team = this.getTeam();
            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers((EntityPlayer)this, this.getCombatTracker().getDeathMessage());
                } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer((EntityPlayer)this, this.getCombatTracker().getDeathMessage());
                }
            } else {
                this.mcServer.getConfigurationManager().sendChatMsg(this.getCombatTracker().getDeathMessage());
            }
        }
        if (!this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.dropAllItems();
        }
        for (ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
            Score score = this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective);
            score.func_96648_a();
        }
        EntityLivingBase entitylivingbase = this.getAttackingEntity();
        if (entitylivingbase != null) {
            EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get((Object)EntityList.getEntityID((Entity)entitylivingbase));
            if (entitylist$entityegginfo != null) {
                this.triggerAchievement(entitylist$entityegginfo.field_151513_e);
            }
            entitylivingbase.addToPlayerScore((Entity)this, this.scoreValue);
        }
        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
        this.getCombatTracker().reset();
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean flag;
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        boolean bl = flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals((Object)source.damageType);
        if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld) {
            return false;
        }
        if (source instanceof EntityDamageSource) {
            Entity entity = source.getEntity();
            if (entity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entity)) {
                return false;
            }
            if (entity instanceof EntityArrow) {
                EntityArrow entityarrow = (EntityArrow)entity;
                if (entityarrow.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entityarrow.shootingEntity)) {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean canAttackPlayer(EntityPlayer other) {
        return !this.canPlayersAttack() ? false : super.canAttackPlayer(other);
    }

    private boolean canPlayersAttack() {
        return this.mcServer.isPVPEnabled();
    }

    public void travelToDimension(int dimensionId) {
        if (this.dimension == 1 && dimensionId == 1) {
            this.triggerAchievement((StatBase)AchievementList.theEnd2);
            this.worldObj.removeEntity((Entity)this);
            this.playerConqueredTheEnd = true;
            this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(4, 0.0f));
        } else {
            if (this.dimension == 0 && dimensionId == 1) {
                this.triggerAchievement((StatBase)AchievementList.theEnd);
                BlockPos blockpos = this.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();
                if (blockpos != null) {
                    this.playerNetServerHandler.setPlayerLocation((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), 0.0f, 0.0f);
                }
                dimensionId = 1;
            } else {
                this.triggerAchievement((StatBase)AchievementList.portal);
            }
            this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
            this.lastExperience = -1;
            this.lastHealth = -1.0f;
            this.lastFoodLevel = -1;
        }
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return player.isSpectator() ? this.getSpectatingEntity() == this : (this.isSpectator() ? false : super.isSpectatedByPlayer(player));
    }

    private void sendTileEntityUpdate(TileEntity p_147097_1_) {
        Packet packet;
        if (p_147097_1_ != null && (packet = p_147097_1_.getDescriptionPacket()) != null) {
            this.playerNetServerHandler.sendPacket(packet);
        }
    }

    public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
        super.onItemPickup(p_71001_1_, p_71001_2_);
        this.openContainer.detectAndSendChanges();
    }

    public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation) {
        EntityPlayer.EnumStatus entityplayer$enumstatus = super.trySleep(bedLocation);
        if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
            S0APacketUseBed packet = new S0APacketUseBed((EntityPlayer)this, bedLocation);
            this.getServerForPlayer().getEntityTracker().sendToAllTrackingEntity((Entity)this, (Packet)packet);
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.playerNetServerHandler.sendPacket((Packet)packet);
        }
        return entityplayer$enumstatus;
    }

    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
        if (this.isPlayerSleeping()) {
            this.getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 2));
        }
        super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
        if (this.playerNetServerHandler != null) {
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public void mountEntity(Entity entityIn) {
        Entity entity = this.ridingEntity;
        super.mountEntity(entityIn);
        if (entityIn != entity) {
            this.playerNetServerHandler.sendPacket((Packet)new S1BPacketEntityAttach(0, (Entity)this, this.ridingEntity));
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
    }

    public void handleFalling(double p_71122_1_, boolean p_71122_3_) {
        Block block1;
        int k;
        int j;
        int i = MathHelper.floor_double((double)this.posX);
        BlockPos blockpos = new BlockPos(i, j = MathHelper.floor_double((double)(this.posY - (double)0.2f)), k = MathHelper.floor_double((double)this.posZ));
        Block block = this.worldObj.getBlockState(blockpos).getBlock();
        if (block.getMaterial() == Material.air && ((block1 = this.worldObj.getBlockState(blockpos.down()).getBlock()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
            blockpos = blockpos.down();
            block = this.worldObj.getBlockState(blockpos).getBlock();
        }
        super.updateFallState(p_71122_1_, p_71122_3_, block, blockpos);
    }

    public void openEditSign(TileEntitySign signTile) {
        signTile.setPlayer((EntityPlayer)this);
        this.playerNetServerHandler.sendPacket((Packet)new S36PacketSignEditorOpen(signTile.getPos()));
    }

    private void getNextWindowId() {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }

    public void displayGui(IInteractionObject guiOwner) {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
        this.openContainer = guiOwner.createContainer(this.inventory, (EntityPlayer)this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened((ICrafting)this);
    }

    public void displayGUIChest(IInventory chestInventory) {
        ILockableContainer ilockablecontainer;
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }
        if (chestInventory instanceof ILockableContainer && (ilockablecontainer = (ILockableContainer)chestInventory).isLocked() && !this.canOpen(ilockablecontainer.getLockCode()) && !this.isSpectator()) {
            this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat((IChatComponent)new ChatComponentTranslation("container.isLocked", new Object[]{chestInventory.getDisplayName()}), 2));
            this.playerNetServerHandler.sendPacket((Packet)new S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0f, 1.0f));
            return;
        }
        this.getNextWindowId();
        if (chestInventory instanceof IInteractionObject) {
            this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, (EntityPlayer)this);
        } else {
            this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            this.openContainer = new ContainerChest((IInventory)this.inventory, chestInventory, (EntityPlayer)this);
        }
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened((ICrafting)this);
    }

    public void displayVillagerTradeGui(IMerchant villager) {
        this.getNextWindowId();
        this.openContainer = new ContainerMerchant(this.inventory, villager, this.worldObj);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened((ICrafting)this);
        InventoryMerchant iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        IChatComponent ichatcomponent = villager.getDisplayName();
        this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = villager.getRecipes((EntityPlayer)this);
        if (merchantrecipelist != null) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }

    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
        this.openContainer = new ContainerHorseInventory((IInventory)this.inventory, horseInventory, horse, (EntityPlayer)this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened((ICrafting)this);
    }

    public void displayGUIBook(ItemStack bookStack) {
        Item item = bookStack.getItem();
        if (item == Items.written_book) {
            this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
        }
    }

    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting) && !this.isChangingQuantityOnly) {
            this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
        }
    }

    public void sendContainerToPlayer(Container p_71120_1_) {
        this.updateCraftingInventory(p_71120_1_, (List<ItemStack>)p_71120_1_.getInventory());
    }

    public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
        this.playerNetServerHandler.sendPacket((Packet)new S30PacketWindowItems(containerToSend.windowId, itemsList));
        this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }

    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
        this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }

    public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_) {
        for (int i = 0; i < p_175173_2_.getFieldCount(); ++i) {
            this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(p_175173_1_.windowId, i, p_175173_2_.getField(i)));
        }
    }

    public void closeScreen() {
        this.playerNetServerHandler.sendPacket((Packet)new S2EPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }

    public void updateHeldItem() {
        if (!this.isChangingQuantityOnly) {
            this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeContainer() {
        this.openContainer.onContainerClosed((EntityPlayer)this);
        this.openContainer = this.inventoryContainer;
    }

    public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean sneaking) {
        if (this.ridingEntity != null) {
            if (p_110430_1_ >= -1.0f && p_110430_1_ <= 1.0f) {
                this.moveStrafing = p_110430_1_;
            }
            if (p_110430_2_ >= -1.0f && p_110430_2_ <= 1.0f) {
                this.moveForward = p_110430_2_;
            }
            this.isJumping = p_110430_3_;
            this.setSneaking(sneaking);
        }
    }

    public void addStat(StatBase stat, int amount) {
        if (stat != null) {
            this.statsFile.increaseStat((EntityPlayer)this, stat, amount);
            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria())) {
                this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).increseScore(amount);
            }
            if (this.statsFile.func_150879_e()) {
                this.statsFile.func_150876_a(this);
            }
        }
    }

    public void func_175145_a(StatBase p_175145_1_) {
        if (p_175145_1_ != null) {
            this.statsFile.unlockAchievement((EntityPlayer)this, p_175145_1_, 0);
            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.getCriteria())) {
                this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).setScorePoints(0);
            }
            if (this.statsFile.func_150879_e()) {
                this.statsFile.func_150876_a(this);
            }
        }
    }

    public void mountEntityAndWakeUp() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity((Entity)this);
        }
        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }
    }

    public void setPlayerHealthUpdated() {
        this.lastHealth = -1.0E8f;
    }

    public void addChatComponentMessage(IChatComponent chatComponent) {
        this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(chatComponent));
    }

    protected void onItemUseFinish() {
        this.playerNetServerHandler.sendPacket((Packet)new S19PacketEntityStatus((Entity)this, 9));
        super.onItemUseFinish();
    }

    public void setItemInUse(ItemStack stack, int duration) {
        super.setItemInUse(stack, duration);
        if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT) {
            this.getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 3));
        }
    }

    public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
        super.clonePlayer(oldPlayer, respawnFromEnd);
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        this.destroyedItemsNetCache.addAll(((EntityPlayerMP)oldPlayer).destroyedItemsNetCache);
    }

    protected void onNewPotionEffect(PotionEffect id) {
        super.onNewPotionEffect(id);
        this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(this.getEntityId(), id));
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
        super.onChangedPotionEffect(id, p_70695_2_);
        this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(this.getEntityId(), id));
    }

    protected void onFinishedPotionEffect(PotionEffect effect) {
        super.onFinishedPotionEffect(effect);
        this.playerNetServerHandler.sendPacket((Packet)new S1EPacketRemoveEntityEffect(this.getEntityId(), effect));
    }

    public void setPositionAndUpdate(double x, double y, double z) {
        this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }

    public void onCriticalHit(Entity entityHit) {
        this.getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 4));
    }

    public void onEnchantmentCritical(Entity entityHit) {
        this.getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 5));
    }

    public void sendPlayerAbilities() {
        if (this.playerNetServerHandler != null) {
            this.playerNetServerHandler.sendPacket((Packet)new S39PacketPlayerAbilities(this.capabilities));
            this.updatePotionMetadata();
        }
    }

    public WorldServer getServerForPlayer() {
        return (WorldServer)this.worldObj;
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.theItemInWorldManager.setGameType(gameType);
        this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(3, (float)gameType.getID()));
        if (gameType == WorldSettings.GameType.SPECTATOR) {
            this.mountEntity(null);
        } else {
            this.setSpectatingEntity((Entity)this);
        }
        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }

    public boolean isSpectator() {
        return this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    public void addChatMessage(IChatComponent component) {
        this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(component));
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        if ("seed".equals((Object)commandName) && !this.mcServer.isDedicatedServer()) {
            return true;
        }
        if (!("tell".equals((Object)commandName) || "help".equals((Object)commandName) || "me".equals((Object)commandName) || "trigger".equals((Object)commandName))) {
            if (this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile())) {
                UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getConfigurationManager().getOppedPlayers().getEntry((Object)this.getGameProfile());
                return userlistopsentry != null ? userlistopsentry.getPermissionLevel() >= permLevel : this.mcServer.getOpPermissionLevel() >= permLevel;
            }
            return false;
        }
        return true;
    }

    public String getPlayerIP() {
        String s = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void handleClientSettings(C15PacketClientSettings packetIn) {
        this.translator = packetIn.getLang();
        this.chatVisibility = packetIn.getChatVisibility();
        this.chatColours = packetIn.isColorsEnabled();
        this.getDataWatcher().updateObject(10, (Object)((byte)packetIn.getModelPartFlags()));
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public void loadResourcePack(String url, String hash) {
        this.playerNetServerHandler.sendPacket((Packet)new S48PacketResourcePackSend(url, hash));
    }

    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }

    public void markPlayerActive() {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }

    public StatisticsFile getStatFile() {
        return this.statsFile;
    }

    public void removeEntity(Entity p_152339_1_) {
        if (p_152339_1_ instanceof EntityPlayer) {
            this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(new int[]{p_152339_1_.getEntityId()}));
        } else {
            this.destroyedItemsNetCache.add((Object)p_152339_1_.getEntityId());
        }
    }

    protected void updatePotionMetadata() {
        if (this.isSpectator()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        } else {
            super.updatePotionMetadata();
        }
        this.getServerForPlayer().getEntityTracker().func_180245_a(this);
    }

    public Entity getSpectatingEntity() {
        return this.spectatingEntity == null ? this : this.spectatingEntity;
    }

    public void setSpectatingEntity(Entity entityToSpectate) {
        Entity entity = this.getSpectatingEntity();
        EntityPlayerMP entityPlayerMP = this.spectatingEntity = entityToSpectate == null ? this : entityToSpectate;
        if (entity != this.spectatingEntity) {
            this.playerNetServerHandler.sendPacket((Packet)new S43PacketCamera(this.spectatingEntity));
            this.setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
        }
    }

    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR) {
            this.setSpectatingEntity(targetEntity);
        } else {
            super.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }

    public long getLastActiveTime() {
        return this.playerLastActiveTime;
    }

    public IChatComponent getTabListDisplayName() {
        return null;
    }
}
