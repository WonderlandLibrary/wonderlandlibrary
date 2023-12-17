// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketChangeGameState;
import java.net.SocketAddress;
import net.minecraft.stats.StatList;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.DimensionType;
import net.minecraft.advancements.CriteriaTriggers;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.Set;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Sets;
import java.util.Iterator;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.network.NetworkManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.GameType;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.stats.StatisticsManagerServer;
import java.util.UUID;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import java.io.File;

public abstract class PlayerList
{
    public static final File FILE_PLAYERBANS;
    public static final File FILE_IPBANS;
    public static final File FILE_OPS;
    public static final File FILE_WHITELIST;
    private static final Logger LOGGER;
    private static final SimpleDateFormat DATE_FORMAT;
    private final MinecraftServer server;
    private final List<EntityPlayerMP> playerEntityList;
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap;
    private final UserListBans bannedPlayers;
    private final UserListIPBans bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map<UUID, StatisticsManagerServer> playerStatFiles;
    private final Map<UUID, PlayerAdvancements> advancements;
    private IPlayerFileData playerDataManager;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    
    public PlayerList(final MinecraftServer server) {
        this.playerEntityList = (List<EntityPlayerMP>)Lists.newArrayList();
        this.uuidToPlayerMap = (Map<UUID, EntityPlayerMP>)Maps.newHashMap();
        this.bannedPlayers = new UserListBans(PlayerList.FILE_PLAYERBANS);
        this.bannedIPs = new UserListIPBans(PlayerList.FILE_IPBANS);
        this.ops = new UserListOps(PlayerList.FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(PlayerList.FILE_WHITELIST);
        this.playerStatFiles = (Map<UUID, StatisticsManagerServer>)Maps.newHashMap();
        this.advancements = (Map<UUID, PlayerAdvancements>)Maps.newHashMap();
        this.server = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }
    
    public void initializeConnectionToPlayer(final NetworkManager netManager, final EntityPlayerMP playerIn) {
        final GameProfile gameprofile = playerIn.getGameProfile();
        final PlayerProfileCache playerprofilecache = this.server.getPlayerProfileCache();
        final GameProfile gameprofile2 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        final String s = (gameprofile2 == null) ? gameprofile.getName() : gameprofile2.getName();
        playerprofilecache.addEntry(gameprofile);
        final NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.server.getWorld(playerIn.dimension));
        playerIn.interactionManager.setWorld((WorldServer)playerIn.world);
        String s2 = "local";
        if (netManager.getRemoteAddress() != null) {
            s2 = netManager.getRemoteAddress().toString();
        }
        PlayerList.LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", (Object)playerIn.getName(), (Object)s2, (Object)playerIn.getEntityId(), (Object)playerIn.posX, (Object)playerIn.posY, (Object)playerIn.posZ);
        final WorldServer worldserver = this.server.getWorld(playerIn.dimension);
        final WorldInfo worldinfo = worldserver.getWorldInfo();
        this.setPlayerGameTypeBasedOnOther(playerIn, null, worldserver);
        final NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.server, netManager, playerIn);
        nethandlerplayserver.sendPacket(new SPacketJoinGame(playerIn.getEntityId(), playerIn.interactionManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionType().getId(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new SPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver.sendPacket(new SPacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new SPacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
        this.updatePermissionLevel(playerIn);
        playerIn.getStatFile().markAllDirty();
        playerIn.getRecipeBook().init(playerIn);
        this.sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.server.refreshStatusNextTick();
        TextComponentTranslation textcomponenttranslation;
        if (playerIn.getName().equalsIgnoreCase(s)) {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined", new Object[] { playerIn.getDisplayName() });
        }
        else {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined.renamed", new Object[] { playerIn.getDisplayName(), s });
        }
        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.sendMessage(textcomponenttranslation);
        this.playerLoggedIn(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);
        if (!this.server.getResourcePackUrl().isEmpty()) {
            playerIn.loadResourcePack(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
        }
        for (final PotionEffect potioneffect : playerIn.getActivePotionEffects()) {
            nethandlerplayserver.sendPacket(new SPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }
        if (nbttagcompound != null && nbttagcompound.hasKey("RootVehicle", 10)) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("RootVehicle");
            final Entity entity1 = AnvilChunkLoader.readWorldEntity(nbttagcompound2.getCompoundTag("Entity"), worldserver, true);
            if (entity1 != null) {
                final UUID uuid = nbttagcompound2.getUniqueId("Attach");
                if (entity1.getUniqueID().equals(uuid)) {
                    playerIn.startRiding(entity1, true);
                }
                else {
                    for (final Entity entity2 : entity1.getRecursivePassengers()) {
                        if (entity2.getUniqueID().equals(uuid)) {
                            playerIn.startRiding(entity2, true);
                            break;
                        }
                    }
                }
                if (!playerIn.isRiding()) {
                    PlayerList.LOGGER.warn("Couldn't reattach entity to player");
                    worldserver.removeEntityDangerously(entity1);
                    for (final Entity entity3 : entity1.getRecursivePassengers()) {
                        worldserver.removeEntityDangerously(entity3);
                    }
                }
            }
        }
        playerIn.addSelfToInternalCraftingInventory();
    }
    
    protected void sendScoreboard(final ServerScoreboard scoreboardIn, final EntityPlayerMP playerIn) {
        final Set<ScoreObjective> set = (Set<ScoreObjective>)Sets.newHashSet();
        for (final ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams()) {
            playerIn.connection.sendPacket(new SPacketTeams(scoreplayerteam, 0));
        }
        for (int i = 0; i < 19; ++i) {
            final ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
            if (scoreobjective != null && !set.contains(scoreobjective)) {
                for (final Packet<?> packet : scoreboardIn.getCreatePackets(scoreobjective)) {
                    playerIn.connection.sendPacket(packet);
                }
                set.add(scoreobjective);
            }
        }
    }
    
    public void setPlayerManager(final WorldServer[] worldServers) {
        this.playerDataManager = worldServers[0].getSaveHandler().getPlayerNBTManager();
        worldServers[0].getWorldBorder().addListener(new IBorderListener() {
            @Override
            public void onSizeChanged(final WorldBorder border, final double newSize) {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_SIZE));
            }
            
            @Override
            public void onTransitionStarted(final WorldBorder border, final double oldSize, final double newSize, final long time) {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.LERP_SIZE));
            }
            
            @Override
            public void onCenterChanged(final WorldBorder border, final double x, final double z) {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_CENTER));
            }
            
            @Override
            public void onWarningTimeChanged(final WorldBorder border, final int newTime) {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_TIME));
            }
            
            @Override
            public void onWarningDistanceChanged(final WorldBorder border, final int newDistance) {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }
            
            @Override
            public void onDamageAmountChanged(final WorldBorder border, final double newAmount) {
            }
            
            @Override
            public void onDamageBufferChanged(final WorldBorder border, final double newSize) {
            }
        });
    }
    
    public void preparePlayer(final EntityPlayerMP playerIn, @Nullable final WorldServer worldIn) {
        final WorldServer worldserver = playerIn.getServerWorld();
        if (worldIn != null) {
            worldIn.getPlayerChunkMap().removePlayer(playerIn);
        }
        worldserver.getPlayerChunkMap().addPlayer(playerIn);
        worldserver.getChunkProvider().provideChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
        if (worldIn != null) {
            CriteriaTriggers.CHANGED_DIMENSION.trigger(playerIn, worldIn.provider.getDimensionType(), worldserver.provider.getDimensionType());
            if (worldIn.provider.getDimensionType() == DimensionType.NETHER && playerIn.world.provider.getDimensionType() == DimensionType.OVERWORLD && playerIn.getEnteredNetherPosition() != null) {
                CriteriaTriggers.NETHER_TRAVEL.trigger(playerIn, playerIn.getEnteredNetherPosition());
            }
        }
    }
    
    public int getEntityViewDistance() {
        return PlayerChunkMap.getFurthestViewableBlock(this.getViewDistance());
    }
    
    @Nullable
    public NBTTagCompound readPlayerDataFromFile(final EntityPlayerMP playerIn) {
        final NBTTagCompound nbttagcompound = this.server.worlds[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound nbttagcompound2;
        if (playerIn.getName().equals(this.server.getServerOwner()) && nbttagcompound != null) {
            nbttagcompound2 = nbttagcompound;
            playerIn.readFromNBT(nbttagcompound);
            PlayerList.LOGGER.debug("loading single player");
        }
        else {
            nbttagcompound2 = this.playerDataManager.readPlayerData(playerIn);
        }
        return nbttagcompound2;
    }
    
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        this.playerDataManager.writePlayerData(playerIn);
        final StatisticsManagerServer statisticsmanagerserver = this.playerStatFiles.get(playerIn.getUniqueID());
        if (statisticsmanagerserver != null) {
            statisticsmanagerserver.saveStatFile();
        }
        final PlayerAdvancements playeradvancements = this.advancements.get(playerIn.getUniqueID());
        if (playeradvancements != null) {
            playeradvancements.save();
        }
    }
    
    public void playerLoggedIn(final EntityPlayerMP playerIn) {
        this.playerEntityList.add(playerIn);
        this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { playerIn }));
        final WorldServer worldserver = this.server.getWorld(playerIn.dimension);
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            playerIn.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { this.playerEntityList.get(i) }));
        }
        worldserver.spawnEntity(playerIn);
        this.preparePlayer(playerIn, null);
    }
    
    public void serverUpdateMovingPlayer(final EntityPlayerMP playerIn) {
        playerIn.getServerWorld().getPlayerChunkMap().updateMovingPlayer(playerIn);
    }
    
    public void playerLoggedOut(final EntityPlayerMP playerIn) {
        final WorldServer worldserver = playerIn.getServerWorld();
        playerIn.addStat(StatList.LEAVE_GAME);
        this.writePlayerData(playerIn);
        if (playerIn.isRiding()) {
            final Entity entity = playerIn.getLowestRidingEntity();
            if (entity.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
                PlayerList.LOGGER.debug("Removing player mount");
                playerIn.dismountRidingEntity();
                worldserver.removeEntityDangerously(entity);
                for (final Entity entity2 : entity.getRecursivePassengers()) {
                    worldserver.removeEntityDangerously(entity2);
                }
                worldserver.getChunk(playerIn.chunkCoordX, playerIn.chunkCoordZ).markDirty();
            }
        }
        worldserver.removeEntity(playerIn);
        worldserver.getPlayerChunkMap().removePlayer(playerIn);
        playerIn.getAdvancements().dispose();
        this.playerEntityList.remove(playerIn);
        final UUID uuid = playerIn.getUniqueID();
        final EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);
        if (entityplayermp == playerIn) {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
            this.advancements.remove(uuid);
        }
        this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { playerIn }));
    }
    
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        if (this.bannedPlayers.isBanned(profile)) {
            final UserListBansEntry userlistbansentry = this.bannedPlayers.getEntry(profile);
            String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();
            if (userlistbansentry.getBanEndDate() != null) {
                s1 = s1 + "\nYour ban will be removed on " + PlayerList.DATE_FORMAT.format(userlistbansentry.getBanEndDate());
            }
            return s1;
        }
        if (!this.canJoin(profile)) {
            return "You are not white-listed on this server!";
        }
        if (this.bannedIPs.isBanned(address)) {
            final UserListIPBansEntry userlistipbansentry = this.bannedIPs.getBanEntry(address);
            String s2 = "Your IP address is banned from this server!\nReason: " + userlistipbansentry.getBanReason();
            if (userlistipbansentry.getBanEndDate() != null) {
                s2 = s2 + "\nYour ban will be removed on " + PlayerList.DATE_FORMAT.format(userlistipbansentry.getBanEndDate());
            }
            return s2;
        }
        return (this.playerEntityList.size() >= this.maxPlayers && !this.bypassesPlayerLimit(profile)) ? "The server is full!" : null;
    }
    
    public EntityPlayerMP createPlayerForUser(final GameProfile profile) {
        final UUID uuid = EntityPlayer.getUUID(profile);
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.getUniqueID().equals(uuid)) {
                list.add(entityplayermp);
            }
        }
        final EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());
        if (entityplayermp2 != null && !list.contains(entityplayermp2)) {
            list.add(entityplayermp2);
        }
        for (final EntityPlayerMP entityplayermp3 : list) {
            entityplayermp3.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login", new Object[0]));
        }
        PlayerInteractionManager playerinteractionmanager;
        if (this.server.isDemo()) {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.server.getWorld(0));
        }
        else {
            playerinteractionmanager = new PlayerInteractionManager(this.server.getWorld(0));
        }
        return new EntityPlayerMP(this.server, this.server.getWorld(0), profile, playerinteractionmanager);
    }
    
    public EntityPlayerMP recreatePlayerEntity(final EntityPlayerMP playerIn, final int dimension, final boolean conqueredEnd) {
        playerIn.getServerWorld().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerWorld().getEntityTracker().untrack(playerIn);
        playerIn.getServerWorld().getPlayerChunkMap().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.server.getWorld(playerIn.dimension).removeEntityDangerously(playerIn);
        final BlockPos blockpos = playerIn.getBedLocation();
        final boolean flag = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        PlayerInteractionManager playerinteractionmanager;
        if (this.server.isDemo()) {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.server.getWorld(playerIn.dimension));
        }
        else {
            playerinteractionmanager = new PlayerInteractionManager(this.server.getWorld(playerIn.dimension));
        }
        final EntityPlayerMP entityplayermp = new EntityPlayerMP(this.server, this.server.getWorld(playerIn.dimension), playerIn.getGameProfile(), playerinteractionmanager);
        entityplayermp.connection = playerIn.connection;
        entityplayermp.copyFrom(playerIn, conqueredEnd);
        entityplayermp.setEntityId(playerIn.getEntityId());
        entityplayermp.setCommandStats(playerIn);
        entityplayermp.setPrimaryHand(playerIn.getPrimaryHand());
        for (final String s : playerIn.getTags()) {
            entityplayermp.addTag(s);
        }
        final WorldServer worldserver = this.server.getWorld(playerIn.dimension);
        this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);
        if (blockpos != null) {
            final BlockPos blockpos2 = EntityPlayer.getBedSpawnLocation(this.server.getWorld(playerIn.dimension), blockpos, flag);
            if (blockpos2 != null) {
                entityplayermp.setLocationAndAngles(blockpos2.getX() + 0.5f, blockpos2.getY() + 0.1f, blockpos2.getZ() + 0.5f, 0.0f, 0.0f);
                entityplayermp.setSpawnPoint(blockpos, flag);
            }
            else {
                entityplayermp.connection.sendPacket(new SPacketChangeGameState(0, 0.0f));
            }
        }
        worldserver.getChunkProvider().provideChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
        while (!worldserver.getCollisionBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty() && entityplayermp.posY < 256.0) {
            entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0, entityplayermp.posZ);
        }
        entityplayermp.connection.sendPacket(new SPacketRespawn(entityplayermp.dimension, entityplayermp.world.getDifficulty(), entityplayermp.world.getWorldInfo().getTerrainType(), entityplayermp.interactionManager.getGameType()));
        final BlockPos blockpos3 = worldserver.getSpawnPoint();
        entityplayermp.connection.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.connection.sendPacket(new SPacketSpawnPosition(blockpos3));
        entityplayermp.connection.sendPacket(new SPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
        this.updatePermissionLevel(entityplayermp);
        worldserver.getPlayerChunkMap().addPlayer(entityplayermp);
        worldserver.spawnEntity(entityplayermp);
        this.playerEntityList.add(entityplayermp);
        this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
        entityplayermp.addSelfToInternalCraftingInventory();
        entityplayermp.setHealth(entityplayermp.getHealth());
        return entityplayermp;
    }
    
    public void updatePermissionLevel(final EntityPlayerMP player) {
        final GameProfile gameprofile = player.getGameProfile();
        int i = this.canSendCommands(gameprofile) ? this.ops.getPermissionLevel(gameprofile) : 0;
        i = ((this.server.isSinglePlayer() && this.server.worlds[0].getWorldInfo().areCommandsAllowed()) ? 4 : i);
        i = (this.commandsAllowedForAll ? 4 : i);
        this.sendPlayerPermissionLevel(player, i);
    }
    
    public void changePlayerDimension(final EntityPlayerMP player, final int dimensionIn) {
        final int i = player.dimension;
        final WorldServer worldserver = this.server.getWorld(player.dimension);
        player.dimension = dimensionIn;
        final WorldServer worldserver2 = this.server.getWorld(player.dimension);
        player.connection.sendPacket(new SPacketRespawn(player.dimension, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
        this.updatePermissionLevel(player);
        worldserver.removeEntityDangerously(player);
        player.isDead = false;
        this.transferEntityToWorld(player, i, worldserver, worldserver2);
        this.preparePlayer(player, worldserver);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldserver2);
        player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
        this.updateTimeAndWeatherForPlayer(player, worldserver2);
        this.syncPlayerInventory(player);
        for (final PotionEffect potioneffect : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }
    }
    
    public void transferEntityToWorld(final Entity entityIn, final int lastDimension, final WorldServer oldWorldIn, final WorldServer toWorldIn) {
        double d0 = entityIn.posX;
        double d2 = entityIn.posZ;
        final double d3 = 8.0;
        final float f = entityIn.rotationYaw;
        oldWorldIn.profiler.startSection("moving");
        if (entityIn.dimension == -1) {
            d0 = MathHelper.clamp(d0 / 8.0, toWorldIn.getWorldBorder().minX() + 16.0, toWorldIn.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp(d2 / 8.0, toWorldIn.getWorldBorder().minZ() + 16.0, toWorldIn.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else if (entityIn.dimension == 0) {
            d0 = MathHelper.clamp(d0 * 8.0, toWorldIn.getWorldBorder().minX() + 16.0, toWorldIn.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp(d2 * 8.0, toWorldIn.getWorldBorder().minZ() + 16.0, toWorldIn.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else {
            BlockPos blockpos;
            if (lastDimension == 1) {
                blockpos = toWorldIn.getSpawnPoint();
            }
            else {
                blockpos = toWorldIn.getSpawnCoordinate();
            }
            d0 = blockpos.getX();
            entityIn.posY = blockpos.getY();
            d2 = blockpos.getZ();
            entityIn.setLocationAndAngles(d0, entityIn.posY, d2, 90.0f, 0.0f);
            if (entityIn.isEntityAlive()) {
                oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        oldWorldIn.profiler.endSection();
        if (lastDimension != 1) {
            oldWorldIn.profiler.startSection("placing");
            d0 = MathHelper.clamp((int)d0, -29999872, 29999872);
            d2 = MathHelper.clamp((int)d2, -29999872, 29999872);
            if (entityIn.isEntityAlive()) {
                entityIn.setLocationAndAngles(d0, entityIn.posY, d2, entityIn.rotationYaw, entityIn.rotationPitch);
                toWorldIn.getDefaultTeleporter().placeInPortal(entityIn, f);
                toWorldIn.spawnEntity(entityIn);
                toWorldIn.updateEntityWithOptionalForce(entityIn, false);
            }
            oldWorldIn.profiler.endSection();
        }
        entityIn.setWorld(toWorldIn);
    }
    
    public void onTick() {
        if (++this.playerPingIndex > 600) {
            this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
            this.playerPingIndex = 0;
        }
    }
    
    public void sendPacketToAllPlayers(final Packet<?> packetIn) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.playerEntityList.get(i).connection.sendPacket(packetIn);
        }
    }
    
    public void sendPacketToAllPlayersInDimension(final Packet<?> packetIn, final int dimension) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.dimension == dimension) {
                entityplayermp.connection.sendPacket(packetIn);
            }
        }
    }
    
    public void sendMessageToAllTeamMembers(final EntityPlayer player, final ITextComponent message) {
        final Team team = player.getTeam();
        if (team != null) {
            for (final String s : team.getMembershipCollection()) {
                final EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);
                if (entityplayermp != null && entityplayermp != player) {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }
    
    public void sendMessageToTeamOrAllPlayers(final EntityPlayer player, final ITextComponent message) {
        final Team team = player.getTeam();
        if (team == null) {
            this.sendMessage(message);
        }
        else {
            for (int i = 0; i < this.playerEntityList.size(); ++i) {
                final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
                if (entityplayermp.getTeam() != team) {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }
    
    public String getFormattedListOfPlayers(final boolean includeUUIDs) {
        String s = "";
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList((Iterable)this.playerEntityList);
        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                s += ", ";
            }
            s += list.get(i).getName();
            if (includeUUIDs) {
                s = s + " (" + list.get(i).getCachedUniqueIdString() + ")";
            }
        }
        return s;
    }
    
    public String[] getOnlinePlayerNames() {
        final String[] astring = new String[this.playerEntityList.size()];
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            astring[i] = this.playerEntityList.get(i).getName();
        }
        return astring;
    }
    
    public GameProfile[] getOnlinePlayerProfiles() {
        final GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            agameprofile[i] = this.playerEntityList.get(i).getGameProfile();
        }
        return agameprofile;
    }
    
    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }
    
    public UserListIPBans getBannedIPs() {
        return this.bannedIPs;
    }
    
    public void addOp(final GameProfile profile) {
        final int i = this.server.getOpPermissionLevel();
        ((UserList<K, UserListOpsEntry>)this.ops).addEntry(new UserListOpsEntry(profile, this.server.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(profile)));
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), i);
    }
    
    public void removeOp(final GameProfile profile) {
        ((UserList<GameProfile, V>)this.ops).removeEntry(profile);
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), 0);
    }
    
    private void sendPlayerPermissionLevel(final EntityPlayerMP player, final int permLevel) {
        if (player != null && player.connection != null) {
            byte b0;
            if (permLevel <= 0) {
                b0 = 24;
            }
            else if (permLevel >= 4) {
                b0 = 28;
            }
            else {
                b0 = (byte)(24 + permLevel);
            }
            player.connection.sendPacket(new SPacketEntityStatus(player, b0));
        }
    }
    
    public boolean canJoin(final GameProfile profile) {
        return !this.whiteListEnforced || ((UserList<GameProfile, V>)this.ops).hasEntry(profile) || ((UserList<GameProfile, V>)this.whiteListedPlayers).hasEntry(profile);
    }
    
    public boolean canSendCommands(final GameProfile profile) {
        return ((UserList<GameProfile, V>)this.ops).hasEntry(profile) || (this.server.isSinglePlayer() && this.server.worlds[0].getWorldInfo().areCommandsAllowed() && this.server.getServerOwner().equalsIgnoreCase(profile.getName())) || this.commandsAllowedForAll;
    }
    
    @Nullable
    public EntityPlayerMP getPlayerByUsername(final String username) {
        for (final EntityPlayerMP entityplayermp : this.playerEntityList) {
            if (entityplayermp.getName().equalsIgnoreCase(username)) {
                return entityplayermp;
            }
        }
        return null;
    }
    
    public void sendToAllNearExcept(@Nullable final EntityPlayer except, final double x, final double y, final double z, final double radius, final int dimension, final Packet<?> packetIn) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp != except && entityplayermp.dimension == dimension) {
                final double d0 = x - entityplayermp.posX;
                final double d2 = y - entityplayermp.posY;
                final double d3 = z - entityplayermp.posZ;
                if (d0 * d0 + d2 * d2 + d3 * d3 < radius * radius) {
                    entityplayermp.connection.sendPacket(packetIn);
                }
            }
        }
    }
    
    public void saveAllPlayerData() {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.writePlayerData(this.playerEntityList.get(i));
        }
    }
    
    public void addWhitelistedPlayer(final GameProfile profile) {
        ((UserList<K, UserListWhitelistEntry>)this.whiteListedPlayers).addEntry(new UserListWhitelistEntry(profile));
    }
    
    public void removePlayerFromWhitelist(final GameProfile profile) {
        ((UserList<GameProfile, V>)this.whiteListedPlayers).removeEntry(profile);
    }
    
    public UserListWhitelist getWhitelistedPlayers() {
        return this.whiteListedPlayers;
    }
    
    public String[] getWhitelistedPlayerNames() {
        return this.whiteListedPlayers.getKeys();
    }
    
    public UserListOps getOppedPlayers() {
        return this.ops;
    }
    
    public String[] getOppedPlayerNames() {
        return this.ops.getKeys();
    }
    
    public void reloadWhitelist() {
    }
    
    public void updateTimeAndWeatherForPlayer(final EntityPlayerMP playerIn, final WorldServer worldIn) {
        final WorldBorder worldborder = this.server.worlds[0].getWorldBorder();
        playerIn.connection.sendPacket(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
        playerIn.connection.sendPacket(new SPacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
        final BlockPos blockpos = worldIn.getSpawnPoint();
        playerIn.connection.sendPacket(new SPacketSpawnPosition(blockpos));
        if (worldIn.isRaining()) {
            playerIn.connection.sendPacket(new SPacketChangeGameState(1, 0.0f));
            playerIn.connection.sendPacket(new SPacketChangeGameState(7, worldIn.getRainStrength(1.0f)));
            playerIn.connection.sendPacket(new SPacketChangeGameState(8, worldIn.getThunderStrength(1.0f)));
        }
    }
    
    public void syncPlayerInventory(final EntityPlayerMP playerIn) {
        playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
        playerIn.setPlayerHealthUpdated();
        playerIn.connection.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
    }
    
    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public String[] getAvailablePlayerDat() {
        return this.server.worlds[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }
    
    public void setWhiteListEnabled(final boolean whitelistEnabled) {
        this.whiteListEnforced = whitelistEnabled;
    }
    
    public List<EntityPlayerMP> getPlayersMatchingAddress(final String address) {
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (final EntityPlayerMP entityplayermp : this.playerEntityList) {
            if (entityplayermp.getPlayerIP().equals(address)) {
                list.add(entityplayermp);
            }
        }
        return list;
    }
    
    public int getViewDistance() {
        return this.viewDistance;
    }
    
    public MinecraftServer getServerInstance() {
        return this.server;
    }
    
    public NBTTagCompound getHostPlayerData() {
        return null;
    }
    
    public void setGameType(final GameType gameModeIn) {
        this.gameType = gameModeIn;
    }
    
    private void setPlayerGameTypeBasedOnOther(final EntityPlayerMP target, final EntityPlayerMP source, final World worldIn) {
        if (source != null) {
            target.interactionManager.setGameType(source.interactionManager.getGameType());
        }
        else if (this.gameType != null) {
            target.interactionManager.setGameType(this.gameType);
        }
        target.interactionManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }
    
    public void setCommandsAllowedForAll(final boolean p_72387_1_) {
        this.commandsAllowedForAll = p_72387_1_;
    }
    
    public void removeAllPlayers() {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.playerEntityList.get(i).connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.server_shutdown", new Object[0]));
        }
    }
    
    public void sendMessage(final ITextComponent component, final boolean isSystem) {
        this.server.sendMessage(component);
        final ChatType chattype = isSystem ? ChatType.SYSTEM : ChatType.CHAT;
        this.sendPacketToAllPlayers(new SPacketChat(component, chattype));
    }
    
    public void sendMessage(final ITextComponent component) {
        this.sendMessage(component, true);
    }
    
    public StatisticsManagerServer getPlayerStatsFile(final EntityPlayer playerIn) {
        final UUID uuid = playerIn.getUniqueID();
        StatisticsManagerServer statisticsmanagerserver = (uuid == null) ? null : this.playerStatFiles.get(uuid);
        if (statisticsmanagerserver == null) {
            final File file1 = new File(this.server.getWorld(0).getSaveHandler().getWorldDirectory(), "stats");
            final File file2 = new File(file1, uuid + ".json");
            if (!file2.exists()) {
                final File file3 = new File(file1, playerIn.getName() + ".json");
                if (file3.exists() && file3.isFile()) {
                    file3.renameTo(file2);
                }
            }
            statisticsmanagerserver = new StatisticsManagerServer(this.server, file2);
            statisticsmanagerserver.readStatFile();
            this.playerStatFiles.put(uuid, statisticsmanagerserver);
        }
        return statisticsmanagerserver;
    }
    
    public PlayerAdvancements getPlayerAdvancements(final EntityPlayerMP p_192054_1_) {
        final UUID uuid = p_192054_1_.getUniqueID();
        PlayerAdvancements playeradvancements = this.advancements.get(uuid);
        if (playeradvancements == null) {
            final File file1 = new File(this.server.getWorld(0).getSaveHandler().getWorldDirectory(), "advancements");
            final File file2 = new File(file1, uuid + ".json");
            playeradvancements = new PlayerAdvancements(this.server, file2, p_192054_1_);
            this.advancements.put(uuid, playeradvancements);
        }
        playeradvancements.setPlayer(p_192054_1_);
        return playeradvancements;
    }
    
    public void setViewDistance(final int distance) {
        this.viewDistance = distance;
        if (this.server.worlds != null) {
            for (final WorldServer worldserver : this.server.worlds) {
                if (worldserver != null) {
                    worldserver.getPlayerChunkMap().setPlayerViewRadius(distance);
                    worldserver.getEntityTracker().setViewDistance(distance);
                }
            }
        }
    }
    
    public List<EntityPlayerMP> getPlayers() {
        return this.playerEntityList;
    }
    
    public EntityPlayerMP getPlayerByUUID(final UUID playerUUID) {
        return this.uuidToPlayerMap.get(playerUUID);
    }
    
    public boolean bypassesPlayerLimit(final GameProfile profile) {
        return false;
    }
    
    public void reloadResources() {
        for (final PlayerAdvancements playeradvancements : this.advancements.values()) {
            playeradvancements.reload();
        }
    }
    
    static {
        FILE_PLAYERBANS = new File("banned-players.json");
        FILE_IPBANS = new File("banned-ips.json");
        FILE_OPS = new File("ops.json");
        FILE_WHITELIST = new File("whitelist.json");
        LOGGER = LogManager.getLogger();
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }
}
