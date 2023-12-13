package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
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
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.*;
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
import net.minecraft.util.*;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
    /**
     * The NetServerHandler assigned to this player by the ServerConfigurationManager.
     */
    public NetHandlerPlayServer playerNetServerHandler;

    /**
     * Reference to the MinecraftServer object.
     */
    public final MinecraftServer server;

    /**
     * The ItemInWorldManager belonging to this player
     */
    public final ItemInWorldManager theItemInWorldManager;

    /**
     * player X position as seen by PlayerManager
     */
    public double managedPosX;

    /**
     * player Z position as seen by PlayerManager
     */
    public double managedPosZ;
    public final List<ChunkCoordIntPair> loadedChunks = Lists.newLinkedList();
    private final List<Integer> destroyedItemsNetCache = Lists.newLinkedList();
    private final StatisticsFile statsFile;

    /**
     * the total health of the player, includes actual health and absorption health. Updated every tick.
     */
    private float combinedHealth = Float.MIN_VALUE;

    /**
     * amount of health the client was last set to
     */
    private float lastHealth = -1.0E8F;

    /**
     * set to foodStats.GetFoodLevel
     */
    private int lastFoodLevel = -99999999;

    /**
     * set to foodStats.getSaturationLevel() == 0.0F each tick
     */
    private boolean wasHungry = true;

    /**
     * Amount of experience the client was last set to
     */
    private int lastExperience = -99999999;
    private int respawnInvulnerabilityTicks = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private long playerLastActiveTime = System.currentTimeMillis();

    /**
     * The entity the player is currently spectating through.
     */
    private Entity spectatingEntity = null;

    /**
     * The currently in use window ID. Incremented every time a window is opened.
     */
    private int currentWindowId;

    /**
     * set to true when player is moving quantity of items from one inventory to another(crafting) but item in either
     * slot is not changed
     */
    public boolean isChangingQuantityOnly;
    public int ping;

    /**
     * Set when a player beats the ender dragon, used to respawn the player at the spawn point while retaining inventory
     * and XP
     */
    public boolean playerConqueredTheEnd;

    public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager) {
        super(worldIn, profile);
        interactionManager.thisPlayerMP = this;
        theItemInWorldManager = interactionManager;
        BlockPos blockpos = worldIn.getSpawnPoint();

        if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE) {
            int i = Math.max(5, server.getSpawnProtectionSize() - 6);
            int j = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));

            if (j < i)
                i = j;

            if (j <= 1)
                i = 1;

            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(rand.nextInt(i * 2) - i, 0, rand.nextInt(i * 2) - i));
        }

        this.server = server;
        statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
        stepHeight = 0.0F;
        moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);

        while (!worldIn.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty() && posY < 255.0D)
            setPosition(posX, posY + 1.0D, posZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("playerGameType", 99)) {
            if (MinecraftServer.getServer().getForceGamemode())
                theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
            else
                theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("playerGameType", theItemInWorldManager.getGameType().getID());
    }

    /**
     * Add experience levels to this player.
     */
    public void addExperienceLevel(int levels) {
        super.addExperienceLevel(levels);
        lastExperience = -1;
    }

    public void removeExperienceLevel(int levels) {
        super.removeExperienceLevel(levels);
        lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory() {
        openContainer.onCraftGuiOpened(this);
    }

    /**
     * Sends an ENTER_COMBAT packet to the client
     */
    public void sendEnterCombat() {
        super.sendEnterCombat();
        playerNetServerHandler.sendPacket(new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
    }

    /**
     * Sends an END_COMBAT packet to the client
     */
    public void sendEndCombat() {
        super.sendEndCombat();
        playerNetServerHandler.sendPacket(new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        theItemInWorldManager.updateBlockRemoving();
        --respawnInvulnerabilityTicks;

        if (hurtResistantTime > 0)
            --hurtResistantTime;

        openContainer.detectAndSendChanges();

        if (!world.isRemote && !openContainer.canInteractWith(this)) {
            closeScreen();
            openContainer = inventoryContainer;
        }

        while (!destroyedItemsNetCache.isEmpty()) {
            int i = Math.min(destroyedItemsNetCache.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator<Integer> iterator = destroyedItemsNetCache.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = iterator.next();
                iterator.remove();
            }

            playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(aint));
        }

        if (!loadedChunks.isEmpty()) {
            List<Chunk> list = Lists.newArrayList();
            Iterator<ChunkCoordIntPair> iterator1 = loadedChunks.iterator();
            List<TileEntity> list1 = Lists.newArrayList();

            while (iterator1.hasNext() && list.size() < 10) {
                ChunkCoordIntPair chunkcoordintpair = iterator1.next();

                if (chunkcoordintpair != null) {
                    if (world.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4))) {
                        Chunk chunk = world.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);

                        if (chunk.isPopulated()) {
                            list.add(chunk);
                            list1.addAll((getServerForPlayer().getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16)));
                            iterator1.remove();
                        }
                    }
                } else
                    iterator1.remove();
            }

            if (!list.isEmpty()) {
                if (list.size() == 1)
                    playerNetServerHandler.sendPacket(new S21PacketChunkData(list.get(0), true, 65535));
                else
                    playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(list));

                for (TileEntity tileentity : list1)
                    sendTileEntityUpdate(tileentity);

                for (Chunk chunk1 : list)
                    getServerForPlayer().getEntityTracker().func_85172_a(this, chunk1);
            }
        }

        Entity entity = getSpectatingEntity();

        if (entity != this) {
            if (!entity.isEntityAlive()) {
                setSpectatingEntity(this);
            } else {
                setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                server.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
                if (isSneaking()) setSpectatingEntity(this);
            }
        }
    }

    public void onUpdateEntity() {
        try {
            super.onUpdate();

            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = inventory.getStackInSlot(i);

                if (itemstack != null && itemstack.getItem().isMap()) {
                    Packet<?> packet = ((ItemMapBase) itemstack.getItem()).createMapDataPacket(itemstack, world, this);
                    if (packet != null) playerNetServerHandler.sendPacket(packet);
                }
            }

            if (getHealth() != lastHealth || lastFoodLevel != foodStats.getFoodLevel() || foodStats.getSaturationLevel() == 0.0F != wasHungry) {
                playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(getHealth(), foodStats.getFoodLevel(), foodStats.getSaturationLevel()));
                lastHealth = getHealth();
                lastFoodLevel = foodStats.getFoodLevel();
                wasHungry = foodStats.getSaturationLevel() == 0.0F;
            }

            if (getHealth() + getAbsorptionAmount() != combinedHealth) {
                combinedHealth = getHealth() + getAbsorptionAmount();

                for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health))
                    getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).func_96651_a(Collections.singletonList(this));
            }

            if (experienceTotal != lastExperience) {
                lastExperience = experienceTotal;
                playerNetServerHandler.sendPacket(new S1FPacketSetExperience(experience, experienceTotal, experienceLevel));
            }

            if (ticksExisted % 20 * 5 == 0 && !getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes))
                updateBiomesExplored();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Updates all biomes that have been explored by this player and triggers Adventuring Time if player qualifies.
     */
    protected void updateBiomesExplored() {
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(posX), 0, MathHelper.floor_double(posZ)));
        String s = biomegenbase.biomeName;
        JsonSerializableSet jsonserializableset = getStatFile().func_150870_b(AchievementList.exploreAllBiomes);

        if (jsonserializableset == null)
            jsonserializableset = getStatFile().func_150872_a(AchievementList.exploreAllBiomes, new JsonSerializableSet());

        jsonserializableset.add(s);

        if (getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()) {
            Set<BiomeGenBase> set = Sets.newHashSet(BiomeGenBase.explorationBiomesList);

            for (String s1 : jsonserializableset) {
                set.removeIf(biomegenbase1 -> biomegenbase1.biomeName.equals(s1));
                if (set.isEmpty()) break;
            }

            if (set.isEmpty()) triggerAchievement(AchievementList.exploreAllBiomes);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause) {
        if (world.getGameRules().getBoolean("showDeathMessages")) {
            Team team = getTeam();

            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
                    server.getConfigurationManager().sendMessageToAllTeamMembers(this, getCombatTracker().getDeathMessage());
                else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM)
                    server.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, getCombatTracker().getDeathMessage());
            } else
                server.getConfigurationManager().sendChatMsg(getCombatTracker().getDeathMessage());
        }

        if (!world.getGameRules().getBoolean("keepInventory"))
            inventory.dropAllItems();

        for (ScoreObjective scoreobjective : world.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
            Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
            score.func_96648_a();
        }

        EntityLivingBase entitylivingbase = func_94060_bK();

        if (entitylivingbase != null) {
            EntityList.EntityEggInfo eggInfo = EntityList.entityEggs.get(EntityList.getEntityID(entitylivingbase));
            if (eggInfo != null) triggerAchievement(eggInfo.field_151513_e);
            entitylivingbase.addToPlayerScore(this, scoreValue);
        }

        triggerAchievement(StatList.deathsStat);
        func_175145_a(StatList.timeSinceDeathStat);
        getCombatTracker().reset();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source))
            return false;
        else {
            boolean flag = server.isDedicatedServer() && canPlayersAttack() && "fall".equals(source.damageType);

            if (!flag && respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld)
                return false;
            else {
                if (source instanceof EntityDamageSource) {
                    Entity entity = source.getEntity();

                    if (entity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer) entity))
                        return false;

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shootingEntity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer) entityarrow.shootingEntity))
                            return false;
                    }
                }

                return super.attackEntityFrom(source, amount);
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer other) {
        return canPlayersAttack() && super.canAttackPlayer(other);
    }

    /**
     * Returns if other players can attack this player
     */
    private boolean canPlayersAttack() {
        return server.isPVPEnabled();
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId) {
        if (dimension == 1 && dimensionId == 1) {
            triggerAchievement(AchievementList.theEnd2);
            world.removeEntity(this);
            playerConqueredTheEnd = true;
            playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
        } else {
            if (dimension == 0 && dimensionId == 1) {
                triggerAchievement(AchievementList.theEnd);
                BlockPos blockpos = server.worldServerForDimension(dimensionId).getSpawnCoordinate();

                if (blockpos != null)
                    playerNetServerHandler.setPlayerLocation(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F);

                dimensionId = 1;
            } else
                triggerAchievement(AchievementList.portal);

            server.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
            lastExperience = -1;
            lastHealth = -1.0F;
            lastFoodLevel = -1;
        }
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return player.isSpectator() ? getSpectatingEntity() == this : (!isSpectator() && super.isSpectatedByPlayer(player));
    }

    private void sendTileEntityUpdate(TileEntity tileEntity) {
        if (tileEntity != null) {
            Packet<?> packet = tileEntity.getDescriptionPacket();
            if (packet != null)
                playerNetServerHandler.sendPacket(packet);
        }
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
        super.onItemPickup(p_71001_1_, p_71001_2_);
        openContainer.detectAndSendChanges();
    }

    public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation) {
        EntityPlayer.EnumStatus enumStatus = super.trySleep(bedLocation);

        if (enumStatus == EntityPlayer.EnumStatus.OK) {
            Packet<?> packet = new S0APacketUseBed(this, bedLocation);
            getServerForPlayer().getEntityTracker().sendToAllTrackingEntity(this, packet);
            playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
            playerNetServerHandler.sendPacket(packet);
        }

        return enumStatus;
    }

    /**
     * Wake up the player if they're sleeping.
     */
    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn) {
        if (isPlayerSleeping())
            getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));

        super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);

        if (playerNetServerHandler != null)
            playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn) {
        Entity entity = ridingEntity;
        super.mountEntity(entityIn);

        if (entityIn != entity) {
            playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, ridingEntity));
            playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
    }

    /**
     * process player falling based on movement packet
     */
    public void handleFalling(double p_71122_1_, boolean p_71122_3_) {
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY - 0.20000000298023224D);
        int z = MathHelper.floor_double(posZ);
        BlockPos pos = new BlockPos(x, y, z);
        Block block = world.getBlockState(pos).getBlock();

        if (block.getMaterial() == Material.air) {
            Block block1 = world.getBlockState(pos.down()).getBlock();

            if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
                pos = pos.down();
                block = world.getBlockState(pos).getBlock();
            }
        }

        super.updateFallState(p_71122_1_, p_71122_3_, block, pos);
    }

    public void openEditSign(TileEntitySign signTile) {
        signTile.setPlayer(this);
        playerNetServerHandler.sendPacket(new S36PacketSignEditorOpen(signTile.getPos()));
    }

    /**
     * get the next window id to use
     */
    private void getNextWindowId() {
        currentWindowId = currentWindowId % 100 + 1;
    }

    public void displayGui(IInteractionObject guiOwner) {
        getNextWindowId();
        playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
        openContainer = guiOwner.createContainer(inventory, this);
        openContainer.windowId = currentWindowId;
        openContainer.onCraftGuiOpened(this);
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory chestInventory) {
        if (openContainer != inventoryContainer)
            closeScreen();

        if (chestInventory instanceof ILockableContainer) {
            ILockableContainer ilockablecontainer = (ILockableContainer) chestInventory;

            if (ilockablecontainer.isLocked() && !canOpen(ilockablecontainer.getLockCode()) && !isSpectator()) {
                playerNetServerHandler.sendPacket(new S02PacketChat(new ChatComponentTranslation("container.isLocked", chestInventory.getDisplayName()), (byte) 2));
                playerNetServerHandler.sendPacket(new S29PacketSoundEffect("random.door_close", posX, posY, posZ, 1.0F, 1.0F));
                return;
            }
        }

        getNextWindowId();

        if (chestInventory instanceof IInteractionObject) {
            playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, ((IInteractionObject) chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            openContainer = ((IInteractionObject) chestInventory).createContainer(inventory, this);
        } else {
            playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            openContainer = new ContainerChest(inventory, chestInventory, this);
        }

        openContainer.windowId = currentWindowId;
        openContainer.onCraftGuiOpened(this);
    }

    public void displayVillagerTradeGui(IMerchant villager) {
        getNextWindowId();
        openContainer = new ContainerMerchant(inventory, villager, world);
        openContainer.windowId = currentWindowId;
        openContainer.onCraftGuiOpened(this);
        IInventory iinventory = ((ContainerMerchant) openContainer).getMerchantInventory();
        IChatComponent ichatcomponent = villager.getDisplayName();
        playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, "minecraft:villager", ichatcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = villager.getRecipes(this);

        if (merchantrecipelist != null) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }

    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
        if (openContainer != inventoryContainer)
            closeScreen();

        getNextWindowId();
        playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
        openContainer = new ContainerHorseInventory(inventory, horseInventory, horse, this);
        openContainer.windowId = currentWindowId;
        openContainer.onCraftGuiOpened(this);
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    public void displayGUIBook(ItemStack bookStack) {
        if (bookStack.getItem() == Items.written_book)
            playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (containerToSend.getSlot(slotInd) instanceof SlotCrafting || isChangingQuantityOnly)
            return;
        playerNetServerHandler.sendPacket(new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
    }

    public void sendContainerToPlayer(Container container) {
        updateCraftingInventory(container, container.getInventory());
    }

    /**
     * update the crafting window inventory with the items in the list
     */
    public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
        playerNetServerHandler.sendPacket(new S30PacketWindowItems(containerToSend.windowId, itemsList));
        playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, inventory.getItemStack()));
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
        playerNetServerHandler.sendPacket(new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }

    public void func_175173_a(Container container, IInventory inventory) {
        for (int i = 0; i < inventory.getFieldCount(); ++i)
            playerNetServerHandler.sendPacket(new S31PacketWindowProperty(container.windowId, i, inventory.getField(i)));
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen() {
        playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(openContainer.windowId));
        closeContainer();
    }

    /**
     * updates item held by mouse
     */
    public void updateHeldItem() {
        if (!isChangingQuantityOnly)
            playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, inventory.getItemStack()));
    }

    /**
     * Closes the container the player currently has open.
     */
    public void closeContainer() {
        openContainer.onContainerClosed(this);
        openContainer = inventoryContainer;
    }

    public void setEntityActionState(float strafe, float forward, boolean jumping, boolean sneaking) {
        if (ridingEntity != null) {
            if (strafe >= -1.0F && strafe <= 1.0F)
                moveStrafing = strafe;

            if (forward >= -1.0F && forward <= 1.0F)
                moveForward = forward;

            isJumping = jumping;
            setSneaking(sneaking);
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase stat, int amount) {
        if (stat == null)
            return;
        statsFile.increaseStat(this, stat, amount);

        for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(stat.func_150952_k()))
            getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).increseScore(amount);

        if (statsFile.func_150879_e())
            statsFile.func_150876_a(this);
    }

    public void func_175145_a(StatBase p_175145_1_) {
        if (p_175145_1_ != null) {
            statsFile.unlockAchievement(this, p_175145_1_, 0);

            for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.func_150952_k()))
                getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).setScorePoints(0);

            if (statsFile.func_150879_e())
                statsFile.func_150876_a(this);
        }
    }

    public void mountEntityAndWakeUp() {
        if (riddenByEntity != null)
            riddenByEntity.mountEntity(this);

        if (sleeping)
            wakeUpPlayer(true, false, false);
    }

    /**
     * this function is called when a players inventory is sent to him, lastHealth is updated on any dimension
     * transitions, then reset.
     */
    public void setPlayerHealthUpdated() {
        lastHealth = -1.0E8F;
    }

    public void addChatComponentMessage(IChatComponent chatComponent) {
        playerNetServerHandler.sendPacket(new S02PacketChat(chatComponent));
    }

    /**
     * Used for when item use count runs out, ie: eating completed
     */
    protected void onItemUseFinish() {
        playerNetServerHandler.sendPacket(new S19PacketEntityStatus(this, (byte) 9));
        super.onItemUseFinish();
    }

    /**
     * sets the itemInUse when the use item button is clicked. Args: itemstack, int maxItemUseDuration
     */
    public void setItemInUse(ItemStack stack, int duration) {
        super.setItemInUse(stack, duration);

        if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT)
            getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
    }

    /**
     * Copies the values from the given player into this player if boolean par2 is true. Always clones Ender Chest
     * Inventory.
     */
    public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
        super.clonePlayer(oldPlayer, respawnFromEnd);
        lastExperience = -1;
        lastHealth = -1.0F;
        lastFoodLevel = -1;
        destroyedItemsNetCache.addAll(((EntityPlayerMP) oldPlayer).destroyedItemsNetCache);
    }

    protected void onNewPotionEffect(PotionEffect id) {
        super.onNewPotionEffect(id);
        playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), id));
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
        super.onChangedPotionEffect(id, p_70695_2_);
        playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), id));
    }

    protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
        super.onFinishedPotionEffect(p_70688_1_);
        playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(getEntityId(), p_70688_1_));
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double x, double y, double z) {
        playerNetServerHandler.setPlayerLocation(x, y, z, rotationYaw, rotationPitch);
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity entityHit) {
        getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 4));
    }

    public void onEnchantmentCritical(Entity entityHit) {
        getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 5));
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
        if (playerNetServerHandler != null) {
            playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities(capabilities));
            updatePotionMetadata();
        }
    }

    public WorldServer getServerForPlayer() {
        return (WorldServer) world;
    }

    /**
     * Sets the player's game mode and sends it to them.
     */
    public void setGameType(WorldSettings.GameType gameType) {
        theItemInWorldManager.setGameType(gameType);
        playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, (float) gameType.getID()));

        if (gameType == WorldSettings.GameType.SPECTATOR)
            mountEntity(null);
        else
            setSpectatingEntity(this);

        sendPlayerAbilities();
        markPotionsDirty();
    }

    /**
     * Returns true if the player is in spectator mode.
     */
    public boolean isSpectator() {
        return theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(IChatComponent component) {
        playerNetServerHandler.sendPacket(new S02PacketChat(component));
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        if ("seed".equals(commandName) && !server.isDedicatedServer())
            return true;
        else if (!"tell".equals(commandName) && !"help".equals(commandName) && !"me".equals(commandName) && !"trigger".equals(commandName)) {
            if (server.getConfigurationManager().canSendCommands(getGameProfile())) {
                UserListOpsEntry opsList = server.getConfigurationManager().getOppedPlayers().getEntry(getGameProfile());
                return opsList != null ? opsList.getPermissionLevel() >= permLevel : server.getOpPermissionLevel() >= permLevel;
            } else
                return false;
        } else
            return true;
    }

    /**
     * Gets the player's IP address. Used in /banip.
     */
    public String getPlayerIP() {
        String s = playerNetServerHandler.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void handleClientSettings(C15PacketClientSettings packetIn) {
        chatVisibility = packetIn.getChatVisibility();
        getDataWatcher().updateObject(10, (byte) packetIn.getModelPartFlags());
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return chatVisibility;
    }

    public void loadResourcePack(String url, String hash) {
        playerNetServerHandler.sendPacket(new S48PacketResourcePackSend(url, hash));
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return new BlockPos(posX, posY + 0.5D, posZ);
    }

    public void markPlayerActive() {
        playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }

    /**
     * Gets the stats file for reading achievements
     */
    public StatisticsFile getStatFile() {
        return statsFile;
    }

    /**
     * Sends a packet to the player to remove an entity.
     */
    public void removeEntity(Entity p_152339_1_) {
        if (p_152339_1_ instanceof EntityPlayer)
            playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(p_152339_1_.getEntityId()));
        else
            destroyedItemsNetCache.add(p_152339_1_.getEntityId());
    }

    /**
     * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
     * ambience, and invisibility metadata values
     */
    protected void updatePotionMetadata() {
        if (isSpectator()) {
            resetPotionEffectMetadata();
            setInvisible(true);
        } else
            super.updatePotionMetadata();

        getServerForPlayer().getEntityTracker().func_180245_a(this);
    }

    public Entity getSpectatingEntity() {
        return spectatingEntity == null ? this : spectatingEntity;
    }

    public void setSpectatingEntity(Entity entityToSpectate) {
        Entity entity = getSpectatingEntity();
        spectatingEntity = entityToSpectate == null ? this : entityToSpectate;

        if (entity != spectatingEntity) {
            playerNetServerHandler.sendPacket(new S43PacketCamera(spectatingEntity));
            setPositionAndUpdate(spectatingEntity.posX, spectatingEntity.posY, spectatingEntity.posZ);
        }
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR)
            setSpectatingEntity(targetEntity);
        else
            super.attackTargetEntityWithCurrentItem(targetEntity);
    }

    public long getLastActiveTime() {
        return playerLastActiveTime;
    }

    /**
     * Returns null which indicates the tab list should just display the player's name, return a different value to
     * display the specified text instead of the player's name
     */
    public IChatComponent getTabListDisplayName() {
        return null;
    }
}