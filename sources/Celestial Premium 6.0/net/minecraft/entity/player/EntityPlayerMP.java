/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.CooldownTrackerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayerMP
extends EntityPlayer
implements IContainerListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private String language = "en_US";
    public NetHandlerPlayServer connection;
    public final MinecraftServer mcServer;
    public final PlayerInteractionManager interactionManager;
    public double managedPosX;
    public double managedPosZ;
    private final List<Integer> entityRemoveQueue = Lists.newLinkedList();
    private final PlayerAdvancements field_192042_bX;
    private final StatisticsManagerServer statsFile;
    private float lastHealthScore = Float.MIN_VALUE;
    private int lastFoodScore = Integer.MIN_VALUE;
    private int lastAirScore = Integer.MIN_VALUE;
    private int lastArmorScore = Integer.MIN_VALUE;
    private int lastLevelScore = Integer.MIN_VALUE;
    private int lastExperienceScore = Integer.MIN_VALUE;
    private float lastHealth = -1.0E8f;
    private int lastFoodLevel = -99999999;
    private boolean wasHungry = true;
    private int lastExperience = -99999999;
    private int respawnInvulnerabilityTicks = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    private Entity spectatingEntity;
    private boolean invulnerableDimensionChange;
    private boolean field_192040_cp;
    private final RecipeBookServer field_192041_cq = new RecipeBookServer();
    private Vec3d field_193107_ct;
    private int field_193108_cu;
    private boolean field_193109_cv;
    private Vec3d field_193110_cw;
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean playerConqueredTheEnd;

    public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn) {
        super(worldIn, profile);
        interactionManagerIn.thisPlayerMP = this;
        this.interactionManager = interactionManagerIn;
        BlockPos blockpos = worldIn.getSpawnPoint();
        if (worldIn.provider.func_191066_m() && worldIn.getWorldInfo().getGameType() != GameType.ADVENTURE) {
            int i = Math.max(0, server.getSpawnRadius(worldIn));
            int j = MathHelper.floor(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
            if (j < i) {
                i = j;
            }
            if (j <= 1) {
                i = 1;
            }
            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2 + 1) - i, 0, this.rand.nextInt(i * 2 + 1) - i));
        }
        this.mcServer = server;
        this.statsFile = server.getPlayerList().getPlayerStatsFile(this);
        this.field_192042_bX = server.getPlayerList().func_192054_h(this);
        this.stepHeight = 1.0f;
        this.moveToBlockPosAndAngles(blockpos, 0.0f, 0.0f);
        while (!worldIn.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0) {
            this.setPosition(this.posX, this.posY + 1.0, this.posZ);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("playerGameType", 99)) {
            if (this.getServer().getForceGamemode()) {
                this.interactionManager.setGameType(this.getServer().getGameType());
            } else {
                this.interactionManager.setGameType(GameType.getByID(compound.getInteger("playerGameType")));
            }
        }
        if (compound.hasKey("enteredNetherPosition", 10)) {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("enteredNetherPosition");
            this.field_193110_cw = new Vec3d(nbttagcompound.getDouble("x"), nbttagcompound.getDouble("y"), nbttagcompound.getDouble("z"));
        }
        this.field_192040_cp = compound.getBoolean("seenCredits");
        if (compound.hasKey("recipeBook", 10)) {
            this.field_192041_cq.func_192825_a(compound.getCompoundTag("recipeBook"));
        }
    }

    public static void func_191522_a(DataFixer p_191522_0_) {
        p_191522_0_.registerWalker(FixTypes.PLAYER, new IDataWalker(){

            @Override
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
                NBTTagCompound nbttagcompound;
                if (compound.hasKey("RootVehicle", 10) && (nbttagcompound = compound.getCompoundTag("RootVehicle")).hasKey("Entity", 10)) {
                    nbttagcompound.setTag("Entity", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("Entity"), versionIn));
                }
                return compound;
            }
        });
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("playerGameType", this.interactionManager.getGameType().getID());
        compound.setBoolean("seenCredits", this.field_192040_cp);
        if (this.field_193110_cw != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setDouble("x", this.field_193110_cw.x);
            nbttagcompound.setDouble("y", this.field_193110_cw.y);
            nbttagcompound.setDouble("z", this.field_193110_cw.z);
            compound.setTag("enteredNetherPosition", nbttagcompound);
        }
        Entity entity1 = this.getLowestRidingEntity();
        Entity entity = this.getRidingEntity();
        if (entity != null && entity1 != this && entity1.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            entity1.writeToNBTOptional(nbttagcompound2);
            nbttagcompound1.setUniqueId("Attach", entity.getUniqueID());
            nbttagcompound1.setTag("Entity", nbttagcompound2);
            compound.setTag("RootVehicle", nbttagcompound1);
        }
        compound.setTag("recipeBook", this.field_192041_cq.func_192824_e());
    }

    @Override
    public void addExperienceLevel(int levels) {
        super.addExperienceLevel(levels);
        this.lastExperience = -1;
    }

    @Override
    public void func_192024_a(ItemStack p_192024_1_, int p_192024_2_) {
        super.func_192024_a(p_192024_1_, p_192024_2_);
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
    protected void func_191955_a(IBlockState p_191955_1_) {
        CriteriaTriggers.field_192124_d.func_192193_a(this, p_191955_1_);
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
            int i = Math.min(this.entityRemoveQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator<Integer> iterator = this.entityRemoveQueue.iterator();
            int j = 0;
            while (iterator.hasNext() && j < i) {
                aint[j++] = iterator.next();
                iterator.remove();
            }
            this.connection.sendPacket(new SPacketDestroyEntities(aint));
        }
        Entity entity = this.getSpectatingEntity();
        if (entity != this) {
            if (entity.isEntityAlive()) {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.mcServer.getPlayerList().serverUpdateMovingPlayer(this);
                if (this.isSneaking()) {
                    this.setSpectatingEntity(this);
                }
            } else {
                this.setSpectatingEntity(this);
            }
        }
        CriteriaTriggers.field_193135_v.func_193182_a(this);
        if (this.field_193107_ct != null) {
            CriteriaTriggers.field_193133_t.func_193162_a(this, this.field_193107_ct, this.ticksExisted - this.field_193108_cu);
        }
        this.field_192042_bX.func_192741_b(this);
    }

    public void onUpdateEntity() {
        try {
            super.onUpdate();
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                Packet<?> packet;
                ItemStack itemstack = this.inventory.getStackInSlot(i);
                if (itemstack.isEmpty() || !itemstack.getItem().isMap() || (packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.world, this)) == null) continue;
                this.connection.sendPacket(packet);
            }
            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0f != this.wasHungry) {
                this.connection.sendPacket(new SPacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                boolean bl = this.wasHungry = this.foodStats.getSaturationLevel() == 0.0f;
            }
            if (this.getHealth() + this.getAbsorptionAmount() != this.lastHealthScore) {
                this.lastHealthScore = this.getHealth() + this.getAbsorptionAmount();
                this.updateScorePoints(IScoreCriteria.HEALTH, MathHelper.ceil(this.lastHealthScore));
            }
            if (this.foodStats.getFoodLevel() != this.lastFoodScore) {
                this.lastFoodScore = this.foodStats.getFoodLevel();
                this.updateScorePoints(IScoreCriteria.FOOD, MathHelper.ceil(this.lastFoodScore));
            }
            if (this.getAir() != this.lastAirScore) {
                this.lastAirScore = this.getAir();
                this.updateScorePoints(IScoreCriteria.AIR, MathHelper.ceil(this.lastAirScore));
            }
            if (this.getTotalArmorValue() != this.lastArmorScore) {
                this.lastArmorScore = this.getTotalArmorValue();
                this.updateScorePoints(IScoreCriteria.ARMOR, MathHelper.ceil(this.lastArmorScore));
            }
            if (this.experienceTotal != this.lastExperienceScore) {
                this.lastExperienceScore = this.experienceTotal;
                this.updateScorePoints(IScoreCriteria.XP, MathHelper.ceil(this.lastExperienceScore));
            }
            if (this.experienceLevel != this.lastLevelScore) {
                this.lastLevelScore = this.experienceLevel;
                this.updateScorePoints(IScoreCriteria.LEVEL, MathHelper.ceil(this.lastLevelScore));
            }
            if (this.experienceTotal != this.lastExperience) {
                this.lastExperience = this.experienceTotal;
                this.connection.sendPacket(new SPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }
            if (this.ticksExisted % 20 == 0) {
                CriteriaTriggers.field_192135_o.func_192215_a(this);
            }
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    private void updateScorePoints(IScoreCriteria criteria, int points) {
        for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(criteria)) {
            Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.setScorePoints(points);
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        boolean flag = this.world.getGameRules().getBoolean("showDeathMessages");
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED, flag));
        if (flag) {
            Team team = this.getTeam();
            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    this.mcServer.getPlayerList().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
                } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    this.mcServer.getPlayerList().sendMessageToTeamOrAllPlayers(this, this.getCombatTracker().getDeathMessage());
                }
            } else {
                this.mcServer.getPlayerList().sendChatMsg(this.getCombatTracker().getDeathMessage());
            }
        }
        this.func_192030_dh();
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.func_190776_cN();
            this.inventory.dropAllItems();
        }
        for (ScoreObjective scoreobjective : this.world.getScoreboard().getObjectivesFromCriteria(IScoreCriteria.DEATH_COUNT)) {
            Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.incrementScore();
        }
        EntityLivingBase entitylivingbase = this.getAttackingEntity();
        if (entitylivingbase != null) {
            EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.func_191301_a(entitylivingbase));
            if (entitylist$entityegginfo != null) {
                this.addStat(entitylist$entityegginfo.entityKilledByStat);
            }
            entitylivingbase.func_191956_a(this, this.scoreValue, cause);
        }
        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().reset();
    }

    @Override
    public void func_191956_a(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_) {
        if (p_191956_1_ != this) {
            super.func_191956_a(p_191956_1_, p_191956_2_, p_191956_3_);
            this.addScore(p_191956_2_);
            Collection<ScoreObjective> collection = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TOTAL_KILL_COUNT);
            if (p_191956_1_ instanceof EntityPlayer) {
                this.addStat(StatList.PLAYER_KILLS);
                collection.addAll(this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.PLAYER_KILL_COUNT));
            } else {
                this.addStat(StatList.MOB_KILLS);
            }
            collection.addAll(this.func_192038_E(p_191956_1_));
            for (ScoreObjective scoreobjective : collection) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).incrementScore();
            }
            CriteriaTriggers.field_192122_b.func_192211_a(this, p_191956_1_, p_191956_3_);
        }
    }

    private Collection<ScoreObjective> func_192038_E(Entity p_192038_1_) {
        int j;
        ScorePlayerTeam scoreplayerteam1;
        int i;
        String s = p_192038_1_ instanceof EntityPlayer ? p_192038_1_.getName() : p_192038_1_.getCachedUniqueIdString();
        ScorePlayerTeam scoreplayerteam = this.getWorldScoreboard().getPlayersTeam(this.getName());
        if (scoreplayerteam != null && (i = scoreplayerteam.getChatFormat().getColorIndex()) >= 0 && i < IScoreCriteria.KILLED_BY_TEAM.length) {
            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.KILLED_BY_TEAM[i])) {
                Score score = this.getWorldScoreboard().getOrCreateScore(s, scoreobjective);
                score.incrementScore();
            }
        }
        if ((scoreplayerteam1 = this.getWorldScoreboard().getPlayersTeam(s)) != null && (j = scoreplayerteam1.getChatFormat().getColorIndex()) >= 0 && j < IScoreCriteria.TEAM_KILL.length) {
            return this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TEAM_KILL[j]);
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean flag;
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        boolean bl = flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);
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

    @Override
    public boolean canAttackPlayer(EntityPlayer other) {
        return !this.canPlayersAttack() ? false : super.canAttackPlayer(other);
    }

    private boolean canPlayersAttack() {
        return this.mcServer.isPVPEnabled();
    }

    @Override
    @Nullable
    public Entity changeDimension(int dimensionIn) {
        this.invulnerableDimensionChange = true;
        if (this.dimension == 0 && dimensionIn == -1) {
            this.field_193110_cw = new Vec3d(this.posX, this.posY, this.posZ);
        } else if (this.dimension != -1 && dimensionIn != 0) {
            this.field_193110_cw = null;
        }
        if (this.dimension == 1 && dimensionIn == 1) {
            this.world.removeEntity(this);
            if (!this.playerConqueredTheEnd) {
                this.playerConqueredTheEnd = true;
                this.connection.sendPacket(new SPacketChangeGameState(4, this.field_192040_cp ? 0.0f : 1.0f));
                this.field_192040_cp = true;
            }
            return this;
        }
        if (this.dimension == 0 && dimensionIn == 1) {
            dimensionIn = 1;
        }
        this.mcServer.getPlayerList().changePlayerDimension(this, dimensionIn);
        this.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        return this;
    }

    @Override
    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        if (player.isSpectator()) {
            return this.getSpectatingEntity() == this;
        }
        return this.isSpectator() ? false : super.isSpectatedByPlayer(player);
    }

    private void sendTileEntityUpdate(TileEntity p_147097_1_) {
        SPacketUpdateTileEntity spacketupdatetileentity;
        if (p_147097_1_ != null && (spacketupdatetileentity = p_147097_1_.getUpdatePacket()) != null) {
            this.connection.sendPacket(spacketupdatetileentity);
        }
    }

    @Override
    public void onItemPickup(Entity entityIn, int quantity) {
        super.onItemPickup(entityIn, quantity);
        this.openContainer.detectAndSendChanges();
    }

    @Override
    public EntityPlayer.SleepResult trySleep(BlockPos bedLocation) {
        EntityPlayer.SleepResult entityplayer$sleepresult = super.trySleep(bedLocation);
        if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
            this.addStat(StatList.SLEEP_IN_BED);
            SPacketUseBed packet = new SPacketUseBed(this, bedLocation);
            this.getServerWorld().getEntityTracker().sendToAllTrackingEntity(this, packet);
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.connection.sendPacket(packet);
            CriteriaTriggers.field_192136_p.func_192215_a(this);
        }
        return entityplayer$sleepresult;
    }

    @Override
    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
        if (this.isPlayerSleeping()) {
            this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(this, 2));
        }
        super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
        if (this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        Entity entity = this.getRidingEntity();
        if (!super.startRiding(entityIn, force)) {
            return false;
        }
        Entity entity1 = this.getRidingEntity();
        if (entity1 != entity && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
        return true;
    }

    @Override
    public void dismountRidingEntity() {
        Entity entity = this.getRidingEntity();
        super.dismountRidingEntity();
        Entity entity1 = this.getRidingEntity();
        if (entity1 != entity && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source) || this.isInvulnerableDimensionChange();
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    protected void frostWalk(BlockPos pos) {
        if (!this.isSpectator()) {
            super.frostWalk(pos);
        }
    }

    public void handleFalling(double y, boolean onGroundIn) {
        BlockPos blockpos1;
        IBlockState iblockstate1;
        Block block;
        int k;
        int j;
        int i = MathHelper.floor(this.posX);
        BlockPos blockpos = new BlockPos(i, j = MathHelper.floor(this.posY - (double)0.2f), k = MathHelper.floor(this.posZ));
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        if (iblockstate.getMaterial() == Material.AIR && ((block = (iblockstate1 = this.world.getBlockState(blockpos1 = blockpos.down())).getBlock()) instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)) {
            blockpos = blockpos1;
            iblockstate = iblockstate1;
        }
        super.updateFallState(y, onGroundIn, iblockstate, blockpos);
    }

    @Override
    public void openEditSign(TileEntitySign signTile) {
        signTile.setPlayer(this);
        this.connection.sendPacket(new SPacketSignEditorOpen(signTile.getPos()));
    }

    private void getNextWindowId() {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }

    @Override
    public void displayGui(IInteractionObject guiOwner) {
        if (guiOwner instanceof ILootContainer && ((ILootContainer)((Object)guiOwner)).getLootTable() != null && this.isSpectator()) {
            this.addChatComponentMessage(new TextComponentTranslation("container.spectatorCantOpen", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)), true);
        } else {
            this.getNextWindowId();
            this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
            this.openContainer = guiOwner.createContainer(this.inventory, this);
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }

    @Override
    public void displayGUIChest(IInventory chestInventory) {
        if (chestInventory instanceof ILootContainer && ((ILootContainer)((Object)chestInventory)).getLootTable() != null && this.isSpectator()) {
            this.addChatComponentMessage(new TextComponentTranslation("container.spectatorCantOpen", new Object[0]).setStyle(new Style().setColor(TextFormatting.RED)), true);
        } else {
            ILockableContainer ilockablecontainer;
            if (this.openContainer != this.inventoryContainer) {
                this.closeScreen();
            }
            if (chestInventory instanceof ILockableContainer && (ilockablecontainer = (ILockableContainer)chestInventory).isLocked() && !this.canOpen(ilockablecontainer.getLockCode()) && !this.isSpectator()) {
                this.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", chestInventory.getDisplayName()), ChatType.GAME_INFO));
                this.connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0f, 1.0f));
                return;
            }
            this.getNextWindowId();
            if (chestInventory instanceof IInteractionObject) {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, ((IInteractionObject)((Object)chestInventory)).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = ((IInteractionObject)((Object)chestInventory)).createContainer(this.inventory, this);
            } else {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = new ContainerChest(this.inventory, chestInventory, this);
            }
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }

    @Override
    public void displayVillagerTradeGui(IMerchant villager) {
        this.getNextWindowId();
        this.openContainer = new ContainerMerchant(this.inventory, villager, this.world);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
        InventoryMerchant iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        ITextComponent itextcomponent = villager.getDisplayName();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", itextcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
        if (merchantrecipelist != null) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }

    @Override
    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
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
    public void openBook(ItemStack stack, EnumHand hand) {
        Item item = stack.getItem();
        if (item == Items.WRITTEN_BOOK) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeEnumValue(hand);
            this.connection.sendPacket(new SPacketCustomPayload("MC|BOpen", packetbuffer));
        }
    }

    @Override
    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
        commandBlock.setSendToClient(true);
        this.sendTileEntityUpdate(commandBlock);
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting)) {
            if (containerToSend == this.inventoryContainer) {
                CriteriaTriggers.field_192125_e.func_192208_a(this, this.inventory);
            }
            if (!this.isChangingQuantityOnly) {
                this.connection.sendPacket(new SPacketSetSlot(containerToSend.windowId, slotInd, stack));
            }
        }
    }

    public void sendContainerToPlayer(Container containerIn) {
        this.updateCraftingInventory(containerIn, containerIn.getInventory());
    }

    @Override
    public void updateCraftingInventory(Container containerToSend, NonNullList<ItemStack> itemsList) {
        this.connection.sendPacket(new SPacketWindowItems(containerToSend.windowId, itemsList));
        this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }

    @Override
    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
        this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory) {
        for (int i = 0; i < inventory.getFieldCount(); ++i) {
            this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, i, inventory.getField(i)));
        }
    }

    @Override
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

    public void setEntityActionState(float strafe, float forward, boolean jumping, boolean sneaking) {
        if (this.isRiding()) {
            if (strafe >= -1.0f && strafe <= 1.0f) {
                this.moveStrafing = strafe;
            }
            if (forward >= -1.0f && forward <= 1.0f) {
                this.field_191988_bg = forward;
            }
            this.isJumping = jumping;
            this.setSneaking(sneaking);
        }
    }

    @Override
    public void addStat(StatBase stat, int amount) {
        if (stat != null) {
            this.statsFile.increaseStat(this, stat, amount);
            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria())) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).increaseScore(amount);
            }
        }
    }

    @Override
    public void takeStat(StatBase stat) {
        if (stat != null) {
            this.statsFile.unlockAchievement(this, stat, 0);
            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria())) {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).setScorePoints(0);
            }
        }
    }

    @Override
    public void func_192021_a(List<IRecipe> p_192021_1_) {
        this.field_192041_cq.func_193835_a(p_192021_1_, this);
    }

    @Override
    public void func_193102_a(ResourceLocation[] p_193102_1_) {
        ArrayList<IRecipe> list = Lists.newArrayList();
        for (ResourceLocation resourcelocation : p_193102_1_) {
            list.add(CraftingManager.func_193373_a(resourcelocation));
        }
        this.func_192021_a(list);
    }

    @Override
    public void func_192022_b(List<IRecipe> p_192022_1_) {
        this.field_192041_cq.func_193834_b(p_192022_1_, this);
    }

    public void mountEntityAndWakeUp() {
        this.field_193109_cv = true;
        this.removePassengers();
        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }
    }

    public boolean func_193105_t() {
        return this.field_193109_cv;
    }

    public void setPlayerHealthUpdated() {
        this.lastHealth = -1.0E8f;
    }

    @Override
    public void addChatComponentMessage(ITextComponent chatComponent, boolean p_146105_2_) {
        this.connection.sendPacket(new SPacketChat(chatComponent, p_146105_2_ ? ChatType.GAME_INFO : ChatType.CHAT));
    }

    @Override
    protected void onItemUseFinish() {
        if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
            this.connection.sendPacket(new SPacketEntityStatus(this, 9));
            super.onItemUseFinish();
        }
    }

    public void func_193104_a(EntityPlayerMP p_193104_1_, boolean p_193104_2_) {
        if (p_193104_2_) {
            this.inventory.copyInventory(p_193104_1_.inventory);
            this.setHealth(p_193104_1_.getHealth());
            this.foodStats = p_193104_1_.foodStats;
            this.experienceLevel = p_193104_1_.experienceLevel;
            this.experienceTotal = p_193104_1_.experienceTotal;
            this.experience = p_193104_1_.experience;
            this.setScore(p_193104_1_.getScore());
            this.lastPortalPos = p_193104_1_.lastPortalPos;
            this.lastPortalVec = p_193104_1_.lastPortalVec;
            this.teleportDirection = p_193104_1_.teleportDirection;
        } else if (this.world.getGameRules().getBoolean("keepInventory") || p_193104_1_.isSpectator()) {
            this.inventory.copyInventory(p_193104_1_.inventory);
            this.experienceLevel = p_193104_1_.experienceLevel;
            this.experienceTotal = p_193104_1_.experienceTotal;
            this.experience = p_193104_1_.experience;
            this.setScore(p_193104_1_.getScore());
        }
        this.xpSeed = p_193104_1_.xpSeed;
        this.theInventoryEnderChest = p_193104_1_.theInventoryEnderChest;
        this.getDataManager().set(PLAYER_MODEL_FLAG, p_193104_1_.getDataManager().get(PLAYER_MODEL_FLAG));
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        this.field_192041_cq.func_193824_a(p_193104_1_.field_192041_cq);
        this.entityRemoveQueue.addAll(p_193104_1_.entityRemoveQueue);
        this.field_192040_cp = p_193104_1_.field_192040_cp;
        this.field_193110_cw = p_193104_1_.field_193110_cw;
        this.func_192029_h(p_193104_1_.func_192023_dk());
        this.func_192031_i(p_193104_1_.func_192025_dl());
    }

    @Override
    protected void onNewPotionEffect(PotionEffect id) {
        super.onNewPotionEffect(id);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));
        if (id.getPotion() == MobEffects.LEVITATION) {
            this.field_193108_cu = this.ticksExisted;
            this.field_193107_ct = new Vec3d(this.posX, this.posY, this.posZ);
        }
        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    @Override
    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
        super.onChangedPotionEffect(id, p_70695_2_);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));
        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    @Override
    protected void onFinishedPotionEffect(PotionEffect effect) {
        super.onFinishedPotionEffect(effect);
        this.connection.sendPacket(new SPacketRemoveEntityEffect(this.getEntityId(), effect.getPotion()));
        if (effect.getPotion() == MobEffects.LEVITATION) {
            this.field_193107_ct = null;
        }
        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    @Override
    public void setPositionAndUpdate(double x, double y, double z) {
        this.connection.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }

    @Override
    public void onCriticalHit(Entity entityHit) {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 4));
    }

    @Override
    public void onEnchantmentCritical(Entity entityHit) {
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
    public void setGameType(GameType gameType) {
        this.interactionManager.setGameType(gameType);
        this.connection.sendPacket(new SPacketChangeGameState(3, gameType.getID()));
        if (gameType == GameType.SPECTATOR) {
            this.func_192030_dh();
            this.dismountRidingEntity();
        } else {
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
    public void addChatMessage(ITextComponent component) {
        this.connection.sendPacket(new SPacketChat(component));
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer()) {
            return true;
        }
        if (!("tell".equals(commandName) || "help".equals(commandName) || "me".equals(commandName) || "trigger".equals(commandName))) {
            if (this.mcServer.getPlayerList().canSendCommands(this.getGameProfile())) {
                UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getPlayerList().getOppedPlayers().getEntry(this.getGameProfile());
                if (userlistopsentry != null) {
                    return userlistopsentry.getPermissionLevel() >= permLevel;
                }
                return this.mcServer.getOpPermissionLevel() >= permLevel;
            }
            return false;
        }
        return true;
    }

    public String getPlayerIP() {
        String s = this.connection.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void handleClientSettings(CPacketClientSettings packetIn) {
        this.language = packetIn.getLang();
        this.chatVisibility = packetIn.getChatVisibility();
        this.chatColours = packetIn.isColorsEnabled();
        this.getDataManager().set(PLAYER_MODEL_FLAG, (byte)packetIn.getModelPartFlags());
        this.getDataManager().set(MAIN_HAND, (byte)(packetIn.getMainHand() != EnumHandSide.LEFT ? 1 : 0));
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public void loadResourcePack(String url, String hash) {
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

    public RecipeBookServer func_192037_E() {
        return this.field_192041_cq;
    }

    public void removeEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer) {
            this.connection.sendPacket(new SPacketDestroyEntities(entityIn.getEntityId()));
        } else {
            this.entityRemoveQueue.add(entityIn.getEntityId());
        }
    }

    public void addEntity(Entity entityIn) {
        this.entityRemoveQueue.remove((Object)entityIn.getEntityId());
    }

    @Override
    protected void updatePotionMetadata() {
        if (this.isSpectator()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        } else {
            super.updatePotionMetadata();
        }
        this.getServerWorld().getEntityTracker().updateVisibility(this);
    }

    public Entity getSpectatingEntity() {
        return this.spectatingEntity == null ? this : this.spectatingEntity;
    }

    public void setSpectatingEntity(Entity entityToSpectate) {
        Entity entity = this.getSpectatingEntity();
        Entity entity2 = this.spectatingEntity = entityToSpectate == null ? this : entityToSpectate;
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
    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (this.interactionManager.getGameType() == GameType.SPECTATOR) {
            this.setSpectatingEntity(targetEntity);
        } else {
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
    public void swingArm(EnumHand hand) {
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

    public PlayerAdvancements func_192039_O() {
        return this.field_192042_bX;
    }

    @Nullable
    public Vec3d func_193106_Q() {
        return this.field_193110_cw;
    }
}

