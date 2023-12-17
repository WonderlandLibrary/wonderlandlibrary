// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.util.EnumHandSide;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.StatBase;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.NonNullList;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.IInteractionObject;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Collection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityList;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.item.ItemMapBase;
import java.util.Iterator;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.util.CooldownTrackerServer;
import net.minecraft.util.CooldownTracker;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.entity.Entity;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.List;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.NetHandlerPlayServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.inventory.IContainerListener;

public class EntityPlayerMP extends EntityPlayer implements IContainerListener
{
    private static final Logger LOGGER;
    private String language;
    public NetHandlerPlayServer connection;
    public final MinecraftServer server;
    public final PlayerInteractionManager interactionManager;
    public double managedPosX;
    public double managedPosZ;
    private final List<Integer> entityRemoveQueue;
    private final PlayerAdvancements advancements;
    private final StatisticsManagerServer statsFile;
    private float lastHealthScore;
    private int lastFoodScore;
    private int lastAirScore;
    private int lastArmorScore;
    private int lastLevelScore;
    private int lastExperienceScore;
    private float lastHealth;
    private int lastFoodLevel;
    private boolean wasHungry;
    private int lastExperience;
    private int respawnInvulnerabilityTicks;
    private EnumChatVisibility chatVisibility;
    private boolean chatColours;
    private long playerLastActiveTime;
    private Entity spectatingEntity;
    private boolean invulnerableDimensionChange;
    private boolean seenCredits;
    private final RecipeBookServer recipeBook;
    private Vec3d levitationStartPos;
    private int levitatingSince;
    private boolean disconnected;
    private Vec3d enteredNetherPosition;
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean queuedEndExit;
    
    public EntityPlayerMP(final MinecraftServer server, final WorldServer worldIn, final GameProfile profile, final PlayerInteractionManager interactionManagerIn) {
        super(worldIn, profile);
        this.language = "en_US";
        this.entityRemoveQueue = (List<Integer>)Lists.newLinkedList();
        this.lastHealthScore = Float.MIN_VALUE;
        this.lastFoodScore = Integer.MIN_VALUE;
        this.lastAirScore = Integer.MIN_VALUE;
        this.lastArmorScore = Integer.MIN_VALUE;
        this.lastLevelScore = Integer.MIN_VALUE;
        this.lastExperienceScore = Integer.MIN_VALUE;
        this.lastHealth = -1.0E8f;
        this.lastFoodLevel = -99999999;
        this.wasHungry = true;
        this.lastExperience = -99999999;
        this.respawnInvulnerabilityTicks = 60;
        this.chatColours = true;
        this.playerLastActiveTime = System.currentTimeMillis();
        this.recipeBook = new RecipeBookServer();
        interactionManagerIn.player = this;
        this.interactionManager = interactionManagerIn;
        BlockPos blockpos = worldIn.getSpawnPoint();
        if (worldIn.provider.hasSkyLight() && worldIn.getWorldInfo().getGameType() != GameType.ADVENTURE) {
            int i = Math.max(0, server.getSpawnRadius(worldIn));
            final int j = MathHelper.floor(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
            if (j < i) {
                i = j;
            }
            if (j <= 1) {
                i = 1;
            }
            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2 + 1) - i, 0, this.rand.nextInt(i * 2 + 1) - i));
        }
        this.server = server;
        this.statsFile = server.getPlayerList().getPlayerStatsFile(this);
        this.advancements = server.getPlayerList().getPlayerAdvancements(this);
        this.stepHeight = 1.0f;
        this.moveToBlockPosAndAngles(blockpos, 0.0f, 0.0f);
        while (!worldIn.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0) {
            this.setPosition(this.posX, this.posY + 1.0, this.posZ);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("playerGameType", 99)) {
            if (this.getServer().getForceGamemode()) {
                this.interactionManager.setGameType(this.getServer().getGameType());
            }
            else {
                this.interactionManager.setGameType(GameType.getByID(compound.getInteger("playerGameType")));
            }
        }
        if (compound.hasKey("enteredNetherPosition", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("enteredNetherPosition");
            this.enteredNetherPosition = new Vec3d(nbttagcompound.getDouble("x"), nbttagcompound.getDouble("y"), nbttagcompound.getDouble("z"));
        }
        this.seenCredits = compound.getBoolean("seenCredits");
        if (compound.hasKey("recipeBook", 10)) {
            this.recipeBook.read(compound.getCompoundTag("recipeBook"));
        }
    }
    
    public static void registerFixesPlayerMP(final DataFixer p_191522_0_) {
        p_191522_0_.registerWalker(FixTypes.PLAYER, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (compound.hasKey("RootVehicle", 10)) {
                    final NBTTagCompound nbttagcompound = compound.getCompoundTag("RootVehicle");
                    if (nbttagcompound.hasKey("Entity", 10)) {
                        nbttagcompound.setTag("Entity", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("Entity"), versionIn));
                    }
                }
                return compound;
            }
        });
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("playerGameType", this.interactionManager.getGameType().getID());
        compound.setBoolean("seenCredits", this.seenCredits);
        if (this.enteredNetherPosition != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setDouble("x", this.enteredNetherPosition.x);
            nbttagcompound.setDouble("y", this.enteredNetherPosition.y);
            nbttagcompound.setDouble("z", this.enteredNetherPosition.z);
            compound.setTag("enteredNetherPosition", nbttagcompound);
        }
        final Entity entity1 = this.getLowestRidingEntity();
        final Entity entity2 = this.getRidingEntity();
        if (entity2 != null && entity1 != this && entity1.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            entity1.writeToNBTOptional(nbttagcompound3);
            nbttagcompound2.setUniqueId("Attach", entity2.getUniqueID());
            nbttagcompound2.setTag("Entity", nbttagcompound3);
            compound.setTag("RootVehicle", nbttagcompound2);
        }
        compound.setTag("recipeBook", this.recipeBook.write());
    }
    
    @Override
    public void addExperienceLevel(final int levels) {
        super.addExperienceLevel(levels);
        this.lastExperience = -1;
    }
    
    @Override
    public void onEnchant(final ItemStack enchantedItem, final int cost) {
        super.onEnchant(enchantedItem, cost);
        this.lastExperience = -1;
    }
    
    public void addSelfToInternalCraftingInventory() {
        this.openContainer.addListener(this);
    }
    
    @Override
    public void sendEnterCombat() {
        super.sendEnterCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTER_COMBAT));
    }
    
    @Override
    public void sendEndCombat() {
        super.sendEndCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.END_COMBAT));
    }
    
    @Override
    protected void onInsideBlock(final IBlockState p_191955_1_) {
        CriteriaTriggers.ENTER_BLOCK.trigger(this, p_191955_1_);
    }
    
    @Override
    protected CooldownTracker createCooldownTracker() {
        return new CooldownTrackerServer(this);
    }
    
    @Override
    public void onUpdate() {
        this.interactionManager.updateBlockRemoving();
        --this.respawnInvulnerabilityTicks;
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }
        this.openContainer.detectAndSendChanges();
        if (!this.world.isRemote && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        while (!this.entityRemoveQueue.isEmpty()) {
            final int i = Math.min(this.entityRemoveQueue.size(), Integer.MAX_VALUE);
            final int[] aint = new int[i];
            final Iterator<Integer> iterator = this.entityRemoveQueue.iterator();
            int j = 0;
            while (iterator.hasNext() && j < i) {
                aint[j++] = iterator.next();
                iterator.remove();
            }
            this.connection.sendPacket(new SPacketDestroyEntities(aint));
        }
        final Entity entity = this.getSpectatingEntity();
        if (entity != this) {
            if (entity.isEntityAlive()) {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.server.getPlayerList().serverUpdateMovingPlayer(this);
                if (this.isSneaking()) {
                    this.setSpectatingEntity(this);
                }
            }
            else {
                this.setSpectatingEntity(this);
            }
        }
        CriteriaTriggers.TICK.trigger(this);
        if (this.levitationStartPos != null) {
            CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.ticksExisted - this.levitatingSince);
        }
        this.advancements.flushDirty(this);
    }
    
    public void onUpdateEntity() {
        try {
            super.onUpdate();
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.inventory.getStackInSlot(i);
                if (!itemstack.isEmpty() && itemstack.getItem().isMap()) {
                    final Packet<?> packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.world, this);
                    if (packet != null) {
                        this.connection.sendPacket(packet);
                    }
                }
            }
            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0f != this.wasHungry) {
                this.connection.sendPacket(new SPacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                this.wasHungry = (this.foodStats.getSaturationLevel() == 0.0f);
            }
            if (this.getHealth() + this.getAbsorptionAmount() != this.lastHealthScore) {
                this.lastHealthScore = this.getHealth() + this.getAbsorptionAmount();
                this.updateScorePoints(IScoreCriteria.HEALTH, MathHelper.ceil(this.lastHealthScore));
            }
            if (this.foodStats.getFoodLevel() != this.lastFoodScore) {
                this.lastFoodScore = this.foodStats.getFoodLevel();
                this.updateScorePoints(IScoreCriteria.FOOD, MathHelper.ceil((float)this.lastFoodScore));
            }
            if (this.getAir() != this.lastAirScore) {
                this.lastAirScore = this.getAir();
                this.updateScorePoints(IScoreCriteria.AIR, MathHelper.ceil((float)this.lastAirScore));
            }
            if (this.getTotalArmorValue() != this.lastArmorScore) {
                this.lastArmorScore = this.getTotalArmorValue();
                this.updateScorePoints(IScoreCriteria.ARMOR, MathHelper.ceil((float)this.lastArmorScore));
            }
            if (this.experienceTotal != this.lastExperienceScore) {
                this.lastExperienceScore = this.experienceTotal;
                this.updateScorePoints(IScoreCriteria.XP, MathHelper.ceil((float)this.lastExperienceScore));
            }
            if (this.experienceLevel != this.lastLevelScore) {
                this.lastLevelScore = this.experienceLevel;
                this.updateScorePoints(IScoreCriteria.LEVEL, MathHelper.ceil((float)this.lastLevelScore));
            }
            if (this.experienceTotal != this.lastExperience) {
                this.lastExperience = this.experienceTotal;
                this.connection.sendPacket(new SPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }
            if (this.ticksExisted % 20 == 0) {
                CriteriaTriggers.LOCATION.trigger(this);
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    private void updateScorePoints(final IScoreCriteria criteria, final int points) {
        for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(criteria)) {
            final Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.setScorePoints(points);
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        final boolean flag = this.world.getGameRules().getBoolean("showDeathMessages");
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED, flag));
        if (flag) {
            final Team team = this.getTeam();
            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    this.server.getPlayerList().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
                }
                else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    this.server.getPlayerList().sendMessageToTeamOrAllPlayers(this, this.getCombatTracker().getDeathMessage());
                }
            }
            else {
                this.server.getPlayerList().sendMessage(this.getCombatTracker().getDeathMessage());
            }
        }
        this.spawnShoulderEntities();
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAllItems();
        }
        for (final ScoreObjective scoreobjective : this.world.getScoreboard().getObjectivesFromCriteria(IScoreCriteria.DEATH_COUNT)) {
            final Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.incrementScore();
        }
        final EntityLivingBase entitylivingbase = this.getAttackingEntity();
        if (entitylivingbase != null) {
            final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.getKey(entitylivingbase));
            if (entitylist$entityegginfo != null) {
                this.addStat(entitylist$entityegginfo.entityKilledByStat);
            }
            entitylivingbase.awardKillScore(this, this.scoreValue, cause);
        }
        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().reset();
    }
    
    @Override
    public void awardKillScore(final Entity p_191956_1_, final int p_191956_2_, final DamageSource p_191956_3_) {
        if (p_191956_1_ != this) {
            super.awardKillScore(p_191956_1_, p_191956_2_, p_191956_3_);
            this.addScore(p_191956_2_);
            final Collection<ScoreObjective> collection = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TOTAL_KILL_COUNT);
            if (p_191956_1_ instanceof EntityPlayer) {
                this.addStat(StatList.PLAYER_KILLS);
                collection.addAll(this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.PLAYER_KILL_COUNT));
            }
            else {
                this.addStat(StatList.MOB_KILLS);
            }
            collection.addAll(this.awardTeamKillScores(p_191956_1_));
            for (final ScoreObjective scoreobjective : collection) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).incrementScore();
            }
            CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, p_191956_1_, p_191956_3_);
        }
    }
    
    private Collection<ScoreObjective> awardTeamKillScores(final Entity p_192038_1_) {
        final String s = (p_192038_1_ instanceof EntityPlayer) ? p_192038_1_.getName() : p_192038_1_.getCachedUniqueIdString();
        final ScorePlayerTeam scoreplayerteam = this.getWorldScoreboard().getPlayersTeam(this.getName());
        if (scoreplayerteam != null) {
            final int i = scoreplayerteam.getColor().getColorIndex();
            if (i >= 0 && i < IScoreCriteria.KILLED_BY_TEAM.length) {
                for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.KILLED_BY_TEAM[i])) {
                    final Score score = this.getWorldScoreboard().getOrCreateScore(s, scoreobjective);
                    score.incrementScore();
                }
            }
        }
        final ScorePlayerTeam scoreplayerteam2 = this.getWorldScoreboard().getPlayersTeam(s);
        if (scoreplayerteam2 != null) {
            final int j = scoreplayerteam2.getColor().getColorIndex();
            if (j >= 0 && j < IScoreCriteria.TEAM_KILL.length) {
                return this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TEAM_KILL[j]);
            }
        }
        return (Collection<ScoreObjective>)Lists.newArrayList();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final boolean flag = this.server.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);
        if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (source instanceof EntityDamageSource) {
            final Entity entity = source.getTrueSource();
            if (entity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entity)) {
                return false;
            }
            if (entity instanceof EntityArrow) {
                final EntityArrow entityarrow = (EntityArrow)entity;
                if (entityarrow.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entityarrow.shootingEntity)) {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public boolean canAttackPlayer(final EntityPlayer other) {
        return this.canPlayersAttack() && super.canAttackPlayer(other);
    }
    
    private boolean canPlayersAttack() {
        return this.server.isPVPEnabled();
    }
    
    @Nullable
    @Override
    public Entity changeDimension(int dimensionIn) {
        this.invulnerableDimensionChange = true;
        if (this.dimension == 0 && dimensionIn == -1) {
            this.enteredNetherPosition = new Vec3d(this.posX, this.posY, this.posZ);
        }
        else if (this.dimension != -1 && dimensionIn != 0) {
            this.enteredNetherPosition = null;
        }
        if (this.dimension == 1 && dimensionIn == 1) {
            this.world.removeEntity(this);
            if (!this.queuedEndExit) {
                this.queuedEndExit = true;
                this.connection.sendPacket(new SPacketChangeGameState(4, this.seenCredits ? 0.0f : 1.0f));
                this.seenCredits = true;
            }
            return this;
        }
        if (this.dimension == 0 && dimensionIn == 1) {
            dimensionIn = 1;
        }
        this.server.getPlayerList().changePlayerDimension(this, dimensionIn);
        this.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        return this;
    }
    
    @Override
    public boolean isSpectatedByPlayer(final EntityPlayerMP player) {
        if (player.isSpectator()) {
            return this.getSpectatingEntity() == this;
        }
        return !this.isSpectator() && super.isSpectatedByPlayer(player);
    }
    
    private void sendTileEntityUpdate(final TileEntity p_147097_1_) {
        if (p_147097_1_ != null) {
            final SPacketUpdateTileEntity spacketupdatetileentity = p_147097_1_.getUpdatePacket();
            if (spacketupdatetileentity != null) {
                this.connection.sendPacket(spacketupdatetileentity);
            }
        }
    }
    
    @Override
    public void onItemPickup(final Entity entityIn, final int quantity) {
        super.onItemPickup(entityIn, quantity);
        this.openContainer.detectAndSendChanges();
    }
    
    @Override
    public SleepResult trySleep(final BlockPos bedLocation) {
        final SleepResult entityplayer$sleepresult = super.trySleep(bedLocation);
        if (entityplayer$sleepresult == SleepResult.OK) {
            this.addStat(StatList.SLEEP_IN_BED);
            final Packet<?> packet = new SPacketUseBed(this, bedLocation);
            this.getServerWorld().getEntityTracker().sendToTracking(this, packet);
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.connection.sendPacket(packet);
            CriteriaTriggers.SLEPT_IN_BED.trigger(this);
        }
        return entityplayer$sleepresult;
    }
    
    @Override
    public void wakeUpPlayer(final boolean immediately, final boolean updateWorldFlag, final boolean setSpawn) {
        if (this.isPlayerSleeping()) {
            this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(this, 2));
        }
        super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
        if (this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    @Override
    public boolean startRiding(final Entity entityIn, final boolean force) {
        final Entity entity = this.getRidingEntity();
        if (!super.startRiding(entityIn, force)) {
            return false;
        }
        final Entity entity2 = this.getRidingEntity();
        if (entity2 != entity && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
        return true;
    }
    
    @Override
    public void dismountRidingEntity() {
        final Entity entity = this.getRidingEntity();
        super.dismountRidingEntity();
        final Entity entity2 = this.getRidingEntity();
        if (entity2 != entity && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    @Override
    public boolean isEntityInvulnerable(final DamageSource source) {
        return super.isEntityInvulnerable(source) || this.isInvulnerableDimensionChange();
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
    }
    
    @Override
    protected void frostWalk(final BlockPos pos) {
        if (!this.isSpectator()) {
            super.frostWalk(pos);
        }
    }
    
    public void handleFalling(final double y, final boolean onGroundIn) {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.posY - 0.20000000298023224);
        final int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        if (iblockstate.getMaterial() == Material.AIR) {
            final BlockPos blockpos2 = blockpos.down();
            final IBlockState iblockstate2 = this.world.getBlockState(blockpos2);
            final Block block = iblockstate2.getBlock();
            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                blockpos = blockpos2;
                iblockstate = iblockstate2;
            }
        }
        super.updateFallState(y, onGroundIn, iblockstate, blockpos);
    }
    
    @Override
    public void openEditSign(final TileEntitySign signTile) {
        signTile.setPlayer(this);
        this.connection.sendPacket(new SPacketSignEditorOpen(signTile.getPos()));
    }
    
    private void getNextWindowId() {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }
    
    @Override
    public void displayGui(final IInteractionObject guiOwner) {
        if (guiOwner instanceof ILootContainer && ((ILootContainer)guiOwner).getLootTable() != null && this.isSpectator()) {
            this.sendStatusMessage(new TextComponentTranslation("container.spectatorCantOpen", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)), true);
        }
        else {
            this.getNextWindowId();
            this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
            this.openContainer = guiOwner.createContainer(this.inventory, this);
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }
    
    @Override
    public void displayGUIChest(final IInventory chestInventory) {
        if (chestInventory instanceof ILootContainer && ((ILootContainer)chestInventory).getLootTable() != null && this.isSpectator()) {
            this.sendStatusMessage(new TextComponentTranslation("container.spectatorCantOpen", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)), true);
        }
        else {
            if (this.openContainer != this.inventoryContainer) {
                this.closeScreen();
            }
            if (chestInventory instanceof ILockableContainer) {
                final ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;
                if (ilockablecontainer.isLocked() && !this.canOpen(ilockablecontainer.getLockCode()) && !this.isSpectator()) {
                    this.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), ChatType.GAME_INFO));
                    this.connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0f, 1.0f));
                    return;
                }
            }
            this.getNextWindowId();
            if (chestInventory instanceof IInteractionObject) {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, this);
            }
            else {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = new ContainerChest(this.inventory, chestInventory, this);
            }
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }
    
    @Override
    public void displayVillagerTradeGui(final IMerchant villager) {
        this.getNextWindowId();
        this.openContainer = new ContainerMerchant(this.inventory, villager, this.world);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
        final IInventory iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        final ITextComponent itextcomponent = villager.getDisplayName();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", itextcomponent, iinventory.getSizeInventory()));
        final MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
        if (merchantrecipelist != null) {
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }
    
    @Override
    public void openGuiHorseInventory(final AbstractHorse horse, final IInventory inventoryIn) {
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }
        this.getNextWindowId();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "EntityHorse", inventoryIn.getDisplayName(), inventoryIn.getSizeInventory(), horse.getEntityId()));
        this.openContainer = new ContainerHorseInventory(this.inventory, inventoryIn, horse, this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
    }
    
    @Override
    public void openBook(final ItemStack stack, final EnumHand hand) {
        final Item item = stack.getItem();
        if (item == Items.WRITTEN_BOOK) {
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeEnumValue(hand);
            this.connection.sendPacket(new SPacketCustomPayload("MC|BOpen", packetbuffer));
        }
    }
    
    @Override
    public void displayGuiCommandBlock(final TileEntityCommandBlock commandBlock) {
        commandBlock.setSendToClient(true);
        this.sendTileEntityUpdate(commandBlock);
    }
    
    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
        if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting)) {
            if (containerToSend == this.inventoryContainer) {
                CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory);
            }
            if (!this.isChangingQuantityOnly) {
                this.connection.sendPacket(new SPacketSetSlot(containerToSend.windowId, slotInd, stack));
            }
        }
    }
    
    public void sendContainerToPlayer(final Container containerIn) {
        this.sendAllContents(containerIn, containerIn.getInventory());
    }
    
    @Override
    public void sendAllContents(final Container containerToSend, final NonNullList<ItemStack> itemsList) {
        this.connection.sendPacket(new SPacketWindowItems(containerToSend.windowId, itemsList));
        this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }
    
    @Override
    public void sendWindowProperty(final Container containerIn, final int varToUpdate, final int newValue) {
        this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }
    
    @Override
    public void sendAllWindowProperties(final Container containerIn, final IInventory inventory) {
        for (int i = 0; i < inventory.getFieldCount(); ++i) {
            this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, i, inventory.getField(i)));
        }
    }
    
    public void closeScreen() {
        this.connection.sendPacket(new SPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }
    
    public void updateHeldItem() {
        if (!this.isChangingQuantityOnly) {
            this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }
    
    public void closeContainer() {
        this.openContainer.onContainerClosed(this);
        this.openContainer = this.inventoryContainer;
    }
    
    public void setEntityActionState(final float strafe, final float forward, final boolean jumping, final boolean sneaking) {
        if (this.isRiding()) {
            if (strafe >= -1.0f && strafe <= 1.0f) {
                this.moveStrafing = strafe;
            }
            if (forward >= -1.0f && forward <= 1.0f) {
                this.moveForward = forward;
            }
            this.isJumping = jumping;
            this.setSneaking(sneaking);
        }
    }
    
    @Override
    public void addStat(final StatBase stat, final int amount) {
        if (stat != null) {
            this.statsFile.increaseStat(this, stat, amount);
            for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria())) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).increaseScore(amount);
            }
        }
    }
    
    @Override
    public void takeStat(final StatBase stat) {
        if (stat != null) {
            this.statsFile.unlockAchievement(this, stat, 0);
            for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria())) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).setScorePoints(0);
            }
        }
    }
    
    @Override
    public void unlockRecipes(final List<IRecipe> p_192021_1_) {
        this.recipeBook.add(p_192021_1_, this);
    }
    
    @Override
    public void unlockRecipes(final ResourceLocation[] p_193102_1_) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (final ResourceLocation resourcelocation : p_193102_1_) {
            list.add(CraftingManager.getRecipe(resourcelocation));
        }
        this.unlockRecipes(list);
    }
    
    @Override
    public void resetRecipes(final List<IRecipe> p_192022_1_) {
        this.recipeBook.remove(p_192022_1_, this);
    }
    
    public void mountEntityAndWakeUp() {
        this.disconnected = true;
        this.removePassengers();
        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }
    }
    
    public boolean hasDisconnected() {
        return this.disconnected;
    }
    
    public void setPlayerHealthUpdated() {
        this.lastHealth = -1.0E8f;
    }
    
    @Override
    public void sendStatusMessage(final ITextComponent chatComponent, final boolean actionBar) {
        this.connection.sendPacket(new SPacketChat(chatComponent, actionBar ? ChatType.GAME_INFO : ChatType.CHAT));
    }
    
    @Override
    protected void onItemUseFinish() {
        if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
            this.connection.sendPacket(new SPacketEntityStatus(this, (byte)9));
            super.onItemUseFinish();
        }
    }
    
    public void copyFrom(final EntityPlayerMP that, final boolean keepEverything) {
        if (keepEverything) {
            this.inventory.copyInventory(that.inventory);
            this.setHealth(that.getHealth());
            this.foodStats = that.foodStats;
            this.experienceLevel = that.experienceLevel;
            this.experienceTotal = that.experienceTotal;
            this.experience = that.experience;
            this.setScore(that.getScore());
            this.lastPortalPos = that.lastPortalPos;
            this.lastPortalVec = that.lastPortalVec;
            this.teleportDirection = that.teleportDirection;
        }
        else if (this.world.getGameRules().getBoolean("keepInventory") || that.isSpectator()) {
            this.inventory.copyInventory(that.inventory);
            this.experienceLevel = that.experienceLevel;
            this.experienceTotal = that.experienceTotal;
            this.experience = that.experience;
            this.setScore(that.getScore());
        }
        this.xpSeed = that.xpSeed;
        this.enderChest = that.enderChest;
        this.getDataManager().set(EntityPlayerMP.PLAYER_MODEL_FLAG, (Byte)that.getDataManager().get((DataParameter<T>)EntityPlayerMP.PLAYER_MODEL_FLAG));
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        this.recipeBook.copyFrom(that.recipeBook);
        this.entityRemoveQueue.addAll(that.entityRemoveQueue);
        this.seenCredits = that.seenCredits;
        this.enteredNetherPosition = that.enteredNetherPosition;
        this.setLeftShoulderEntity(that.getLeftShoulderEntity());
        this.setRightShoulderEntity(that.getRightShoulderEntity());
    }
    
    @Override
    protected void onNewPotionEffect(final PotionEffect id) {
        super.onNewPotionEffect(id);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));
        if (id.getPotion() == MobEffects.LEVITATION) {
            this.levitatingSince = this.ticksExisted;
            this.levitationStartPos = new Vec3d(this.posX, this.posY, this.posZ);
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    @Override
    protected void onChangedPotionEffect(final PotionEffect id, final boolean p_70695_2_) {
        super.onChangedPotionEffect(id, p_70695_2_);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    @Override
    protected void onFinishedPotionEffect(final PotionEffect effect) {
        super.onFinishedPotionEffect(effect);
        this.connection.sendPacket(new SPacketRemoveEntityEffect(this.getEntityId(), effect.getPotion()));
        if (effect.getPotion() == MobEffects.LEVITATION) {
            this.levitationStartPos = null;
        }
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }
    
    @Override
    public void setPositionAndUpdate(final double x, final double y, final double z) {
        this.connection.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }
    
    @Override
    public void onCriticalHit(final Entity entityHit) {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 4));
    }
    
    @Override
    public void onEnchantmentCritical(final Entity entityHit) {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 5));
    }
    
    @Override
    public void sendPlayerAbilities() {
        if (this.connection != null) {
            this.connection.sendPacket(new SPacketPlayerAbilities(this.capabilities));
            this.updatePotionMetadata();
        }
    }
    
    public WorldServer getServerWorld() {
        return (WorldServer)this.world;
    }
    
    @Override
    public void setGameType(final GameType gameType) {
        this.interactionManager.setGameType(gameType);
        this.connection.sendPacket(new SPacketChangeGameState(3, (float)gameType.getID()));
        if (gameType == GameType.SPECTATOR) {
            this.spawnShoulderEntities();
            this.dismountRidingEntity();
        }
        else {
            this.setSpectatingEntity(this);
        }
        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }
    
    @Override
    public boolean isSpectator() {
        return this.interactionManager.getGameType() == GameType.SPECTATOR;
    }
    
    @Override
    public boolean isCreative() {
        return this.interactionManager.getGameType() == GameType.CREATIVE;
    }
    
    @Override
    public void sendMessage(final ITextComponent component) {
        this.connection.sendPacket(new SPacketChat(component));
    }
    
    @Override
    public boolean canUseCommand(final int permLevel, final String commandName) {
        if ("seed".equals(commandName) && !this.server.isDedicatedServer()) {
            return true;
        }
        if ("tell".equals(commandName) || "help".equals(commandName) || "me".equals(commandName) || "trigger".equals(commandName)) {
            return true;
        }
        if (!this.server.getPlayerList().canSendCommands(this.getGameProfile())) {
            return false;
        }
        final UserListOpsEntry userlistopsentry = this.server.getPlayerList().getOppedPlayers().getEntry(this.getGameProfile());
        if (userlistopsentry != null) {
            return userlistopsentry.getPermissionLevel() >= permLevel;
        }
        return this.server.getOpPermissionLevel() >= permLevel;
    }
    
    public String getPlayerIP() {
        String s = this.connection.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }
    
    public void handleClientSettings(final CPacketClientSettings packetIn) {
        this.language = packetIn.getLang();
        this.chatVisibility = packetIn.getChatVisibility();
        this.chatColours = packetIn.isColorsEnabled();
        this.getDataManager().set(EntityPlayerMP.PLAYER_MODEL_FLAG, (byte)packetIn.getModelPartFlags());
        this.getDataManager().set(EntityPlayerMP.MAIN_HAND, (byte)((packetIn.getMainHand() != EnumHandSide.LEFT) ? 1 : 0));
    }
    
    public EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }
    
    public void loadResourcePack(final String url, final String hash) {
        this.connection.sendPacket(new SPacketResourcePackSend(url, hash));
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }
    
    public void markPlayerActive() {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }
    
    public StatisticsManagerServer getStatFile() {
        return this.statsFile;
    }
    
    public RecipeBookServer getRecipeBook() {
        return this.recipeBook;
    }
    
    public void removeEntity(final Entity entityIn) {
        if (entityIn instanceof EntityPlayer) {
            this.connection.sendPacket(new SPacketDestroyEntities(new int[] { entityIn.getEntityId() }));
        }
        else {
            this.entityRemoveQueue.add(entityIn.getEntityId());
        }
    }
    
    public void addEntity(final Entity entityIn) {
        this.entityRemoveQueue.remove((Object)entityIn.getEntityId());
    }
    
    @Override
    protected void updatePotionMetadata() {
        if (this.isSpectator()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        }
        else {
            super.updatePotionMetadata();
        }
        this.getServerWorld().getEntityTracker().updateVisibility(this);
    }
    
    public Entity getSpectatingEntity() {
        return (this.spectatingEntity == null) ? this : this.spectatingEntity;
    }
    
    public void setSpectatingEntity(final Entity entityToSpectate) {
        final Entity entity = this.getSpectatingEntity();
        this.spectatingEntity = ((entityToSpectate == null) ? this : entityToSpectate);
        if (entity != this.spectatingEntity) {
            this.connection.sendPacket(new SPacketCamera(this.spectatingEntity));
            this.setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
        }
    }
    
    @Override
    protected void decrementTimeUntilPortal() {
        if (this.timeUntilPortal > 0 && !this.invulnerableDimensionChange) {
            --this.timeUntilPortal;
        }
    }
    
    @Override
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (this.interactionManager.getGameType() == GameType.SPECTATOR) {
            this.setSpectatingEntity(targetEntity);
        }
        else {
            super.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }
    
    public long getLastActiveTime() {
        return this.playerLastActiveTime;
    }
    
    @Nullable
    public ITextComponent getTabListDisplayName() {
        return null;
    }
    
    @Override
    public void swingArm(final EnumHand hand) {
        super.swingArm(hand);
        this.resetCooldown();
    }
    
    public boolean isInvulnerableDimensionChange() {
        return this.invulnerableDimensionChange;
    }
    
    public void clearInvulnerableDimensionChange() {
        this.invulnerableDimensionChange = false;
    }
    
    public void setElytraFlying() {
        this.setFlag(7, true);
    }
    
    public void clearElytraFlying() {
        this.setFlag(7, true);
        this.setFlag(7, false);
    }
    
    public PlayerAdvancements getAdvancements() {
        return this.advancements;
    }
    
    @Nullable
    public Vec3d getEnteredNetherPosition() {
        return this.enteredNetherPosition;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
