/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.mojang.authlib.GameProfile
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.management;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListOps;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.server.management.UserListWhitelist;
import net.minecraft.server.management.UserListWhitelistEntry;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerConfigurationManager {
    private boolean whiteListEnforced;
    public static final File FILE_WHITELIST;
    private final UserListBans bannedPlayers;
    private final UserListOps ops;
    private final BanList bannedIPs;
    private final UserListWhitelist whiteListedPlayers;
    public static final File FILE_IPBANS;
    private int viewDistance;
    private IPlayerFileData playerNBTManagerObj;
    private final Map<UUID, StatisticsFile> playerStatFiles;
    public static final File FILE_PLAYERBANS;
    private final MinecraftServer mcServer;
    private WorldSettings.GameType gameType;
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap;
    private static final SimpleDateFormat dateFormat;
    private static final Logger logger;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    public static final File FILE_OPS;
    protected int maxPlayers;
    private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();

    public void setPlayerManager(WorldServer[] worldServerArray) {
        this.playerNBTManagerObj = worldServerArray[0].getSaveHandler().getPlayerNBTManager();
        worldServerArray[0].getWorldBorder().addListener(new IBorderListener(){

            @Override
            public void onWarningDistanceChanged(WorldBorder worldBorder, int n) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }

            @Override
            public void onDamageAmountChanged(WorldBorder worldBorder, double d) {
            }

            @Override
            public void onSizeChanged(WorldBorder worldBorder, double d) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.SET_SIZE));
            }

            @Override
            public void onCenterChanged(WorldBorder worldBorder, double d, double d2) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.SET_CENTER));
            }

            @Override
            public void onWarningTimeChanged(WorldBorder worldBorder, int n) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.SET_WARNING_TIME));
            }

            @Override
            public void onTransitionStarted(WorldBorder worldBorder, double d, double d2, long l) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.LERP_SIZE));
            }

            @Override
            public void onDamageBufferChanged(WorldBorder worldBorder, double d) {
            }
        });
    }

    public String[] getOppedPlayerNames() {
        return this.ops.getKeys();
    }

    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP entityPlayerMP, int n, boolean bl) {
        BlockPos blockPos;
        entityPlayerMP.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(entityPlayerMP);
        entityPlayerMP.getServerForPlayer().getEntityTracker().untrackEntity(entityPlayerMP);
        entityPlayerMP.getServerForPlayer().getPlayerManager().removePlayer(entityPlayerMP);
        this.playerEntityList.remove(entityPlayerMP);
        this.mcServer.worldServerForDimension(entityPlayerMP.dimension).removePlayerEntityDangerously(entityPlayerMP);
        BlockPos blockPos2 = entityPlayerMP.getBedLocation();
        boolean bl2 = entityPlayerMP.isSpawnForced();
        entityPlayerMP.dimension = n;
        ItemInWorldManager itemInWorldManager = this.mcServer.isDemo() ? new DemoWorldManager(this.mcServer.worldServerForDimension(entityPlayerMP.dimension)) : new ItemInWorldManager(this.mcServer.worldServerForDimension(entityPlayerMP.dimension));
        EntityPlayerMP entityPlayerMP2 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(entityPlayerMP.dimension), entityPlayerMP.getGameProfile(), itemInWorldManager);
        entityPlayerMP2.playerNetServerHandler = entityPlayerMP.playerNetServerHandler;
        entityPlayerMP2.clonePlayer(entityPlayerMP, bl);
        entityPlayerMP2.setEntityId(entityPlayerMP.getEntityId());
        entityPlayerMP2.func_174817_o(entityPlayerMP);
        WorldServer worldServer = this.mcServer.worldServerForDimension(entityPlayerMP.dimension);
        this.setPlayerGameTypeBasedOnOther(entityPlayerMP2, entityPlayerMP, worldServer);
        if (blockPos2 != null) {
            blockPos = EntityPlayer.getBedSpawnLocation(this.mcServer.worldServerForDimension(entityPlayerMP.dimension), blockPos2, bl2);
            if (blockPos != null) {
                entityPlayerMP2.setLocationAndAngles((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.1f, (float)blockPos.getZ() + 0.5f, 0.0f, 0.0f);
                entityPlayerMP2.setSpawnPoint(blockPos2, bl2);
            } else {
                entityPlayerMP2.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0f));
            }
        }
        worldServer.theChunkProviderServer.loadChunk((int)entityPlayerMP2.posX >> 4, (int)entityPlayerMP2.posZ >> 4);
        while (!worldServer.getCollidingBoundingBoxes(entityPlayerMP2, entityPlayerMP2.getEntityBoundingBox()).isEmpty() && entityPlayerMP2.posY < 256.0) {
            entityPlayerMP2.setPosition(entityPlayerMP2.posX, entityPlayerMP2.posY + 1.0, entityPlayerMP2.posZ);
        }
        entityPlayerMP2.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityPlayerMP2.dimension, entityPlayerMP2.worldObj.getDifficulty(), entityPlayerMP2.worldObj.getWorldInfo().getTerrainType(), entityPlayerMP2.theItemInWorldManager.getGameType()));
        blockPos = worldServer.getSpawnPoint();
        entityPlayerMP2.playerNetServerHandler.setPlayerLocation(entityPlayerMP2.posX, entityPlayerMP2.posY, entityPlayerMP2.posZ, entityPlayerMP2.rotationYaw, entityPlayerMP2.rotationPitch);
        entityPlayerMP2.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockPos));
        entityPlayerMP2.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityPlayerMP2.experience, entityPlayerMP2.experienceTotal, entityPlayerMP2.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityPlayerMP2, worldServer);
        worldServer.getPlayerManager().addPlayer(entityPlayerMP2);
        worldServer.spawnEntityInWorld(entityPlayerMP2);
        this.playerEntityList.add(entityPlayerMP2);
        this.uuidToPlayerMap.put(entityPlayerMP2.getUniqueID(), entityPlayerMP2);
        entityPlayerMP2.addSelfToInternalCraftingInventory();
        entityPlayerMP2.setHealth(entityPlayerMP2.getHealth());
        return entityPlayerMP2;
    }

    public void setViewDistance(int n) {
        this.viewDistance = n;
        if (this.mcServer.worldServers != null) {
            WorldServer[] worldServerArray = this.mcServer.worldServers;
            int n2 = this.mcServer.worldServers.length;
            int n3 = 0;
            while (n3 < n2) {
                WorldServer worldServer = worldServerArray[n3];
                if (worldServer != null) {
                    worldServer.getPlayerManager().setPlayerViewRadius(n);
                }
                ++n3;
            }
        }
    }

    public EntityPlayerMP getPlayerByUsername(String string) {
        for (EntityPlayerMP entityPlayerMP : this.playerEntityList) {
            if (!entityPlayerMP.getName().equalsIgnoreCase(string)) continue;
            return entityPlayerMP;
        }
        return null;
    }

    static {
        FILE_PLAYERBANS = new File("banned-players.json");
        FILE_IPBANS = new File("banned-ips.json");
        FILE_OPS = new File("ops.json");
        FILE_WHITELIST = new File("whitelist.json");
        logger = LogManager.getLogger();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }

    public EntityPlayerMP getPlayerByUUID(UUID uUID) {
        return this.uuidToPlayerMap.get(uUID);
    }

    public List<EntityPlayerMP> func_181057_v() {
        return this.playerEntityList;
    }

    public void setWhiteListEnabled(boolean bl) {
        this.whiteListEnforced = bl;
    }

    public UserListOps getOppedPlayers() {
        return this.ops;
    }

    public GameProfile[] getAllProfiles() {
        GameProfile[] gameProfileArray = new GameProfile[this.playerEntityList.size()];
        int n = 0;
        while (n < this.playerEntityList.size()) {
            gameProfileArray[n] = this.playerEntityList.get(n).getGameProfile();
            ++n;
        }
        return gameProfileArray;
    }

    public void serverUpdateMountedMovingPlayer(EntityPlayerMP entityPlayerMP) {
        entityPlayerMP.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(entityPlayerMP);
    }

    public void addOp(GameProfile gameProfile) {
        this.ops.addEntry(new UserListOpsEntry(gameProfile, this.mcServer.getOpPermissionLevel(), this.ops.func_183026_b(gameProfile)));
    }

    public void playerLoggedOut(EntityPlayerMP entityPlayerMP) {
        entityPlayerMP.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(entityPlayerMP);
        WorldServer worldServer = entityPlayerMP.getServerForPlayer();
        if (entityPlayerMP.ridingEntity != null) {
            worldServer.removePlayerEntityDangerously(entityPlayerMP.ridingEntity);
            logger.debug("removing player mount");
        }
        worldServer.removeEntity(entityPlayerMP);
        worldServer.getPlayerManager().removePlayer(entityPlayerMP);
        this.playerEntityList.remove(entityPlayerMP);
        UUID uUID = entityPlayerMP.getUniqueID();
        EntityPlayerMP entityPlayerMP2 = this.uuidToPlayerMap.get(uUID);
        if (entityPlayerMP2 == entityPlayerMP) {
            this.uuidToPlayerMap.remove(uUID);
            this.playerStatFiles.remove(uUID);
        }
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, entityPlayerMP));
    }

    protected void sendScoreboard(ServerScoreboard serverScoreboard, EntityPlayerMP entityPlayerMP) {
        HashSet hashSet = Sets.newHashSet();
        for (ScorePlayerTeam scorePlayerTeam : serverScoreboard.getTeams()) {
            entityPlayerMP.playerNetServerHandler.sendPacket(new S3EPacketTeams(scorePlayerTeam, 0));
        }
        int n = 0;
        while (n < 19) {
            ScoreObjective scoreObjective = serverScoreboard.getObjectiveInDisplaySlot(n);
            if (scoreObjective != null && !hashSet.contains(scoreObjective)) {
                for (Packet packet : serverScoreboard.func_96550_d(scoreObjective)) {
                    entityPlayerMP.playerNetServerHandler.sendPacket(packet);
                }
                hashSet.add(scoreObjective);
            }
            ++n;
        }
    }

    private void setPlayerGameTypeBasedOnOther(EntityPlayerMP entityPlayerMP, EntityPlayerMP entityPlayerMP2, World world) {
        if (entityPlayerMP2 != null) {
            entityPlayerMP.theItemInWorldManager.setGameType(entityPlayerMP2.theItemInWorldManager.getGameType());
        } else if (this.gameType != null) {
            entityPlayerMP.theItemInWorldManager.setGameType(this.gameType);
        }
        entityPlayerMP.theItemInWorldManager.initializeGameType(world.getWorldInfo().getGameType());
    }

    public void setCommandsAllowedForAll(boolean bl) {
        this.commandsAllowedForAll = bl;
    }

    public void playerLoggedIn(EntityPlayerMP entityPlayerMP) {
        this.playerEntityList.add(entityPlayerMP);
        this.uuidToPlayerMap.put(entityPlayerMP.getUniqueID(), entityPlayerMP);
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, entityPlayerMP));
        WorldServer worldServer = this.mcServer.worldServerForDimension(entityPlayerMP.dimension);
        worldServer.spawnEntityInWorld(entityPlayerMP);
        this.preparePlayer(entityPlayerMP, null);
        int n = 0;
        while (n < this.playerEntityList.size()) {
            EntityPlayerMP entityPlayerMP2 = this.playerEntityList.get(n);
            entityPlayerMP.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, entityPlayerMP2));
            ++n;
        }
    }

    public void sendToAllNear(double d, double d2, double d3, double d4, int n, Packet packet) {
        this.sendToAllNearExcept(null, d, d2, d3, d4, n, packet);
    }

    public String[] getAllUsernames() {
        String[] stringArray = new String[this.playerEntityList.size()];
        int n = 0;
        while (n < this.playerEntityList.size()) {
            stringArray[n] = this.playerEntityList.get(n).getName();
            ++n;
        }
        return stringArray;
    }

    public void syncPlayerInventory(EntityPlayerMP entityPlayerMP) {
        entityPlayerMP.sendContainerToPlayer(entityPlayerMP.inventoryContainer);
        entityPlayerMP.setPlayerHealthUpdated();
        entityPlayerMP.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(entityPlayerMP.inventory.currentItem));
    }

    public void sendChatMsg(IChatComponent iChatComponent) {
        this.sendChatMsgImpl(iChatComponent, true);
    }

    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }

    public void saveAllPlayerData() {
        int n = 0;
        while (n < this.playerEntityList.size()) {
            this.writePlayerData(this.playerEntityList.get(n));
            ++n;
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet packet, int n) {
        int n2 = 0;
        while (n2 < this.playerEntityList.size()) {
            EntityPlayerMP entityPlayerMP = this.playerEntityList.get(n2);
            if (entityPlayerMP.dimension == n) {
                entityPlayerMP.playerNetServerHandler.sendPacket(packet);
            }
            ++n2;
        }
    }

    public void sendToAllNearExcept(EntityPlayer entityPlayer, double d, double d2, double d3, double d4, int n, Packet packet) {
        int n2 = 0;
        while (n2 < this.playerEntityList.size()) {
            double d5;
            double d6;
            double d7;
            EntityPlayerMP entityPlayerMP = this.playerEntityList.get(n2);
            if (entityPlayerMP != entityPlayer && entityPlayerMP.dimension == n && (d7 = d - entityPlayerMP.posX) * d7 + (d6 = d2 - entityPlayerMP.posY) * d6 + (d5 = d3 - entityPlayerMP.posZ) * d5 < d4 * d4) {
                entityPlayerMP.playerNetServerHandler.sendPacket(packet);
            }
            ++n2;
        }
    }

    public void transferEntityToWorld(Entity entity, int n, WorldServer worldServer, WorldServer worldServer2) {
        double d = entity.posX;
        double d2 = entity.posZ;
        double d3 = 8.0;
        float f = entity.rotationYaw;
        worldServer.theProfiler.startSection("moving");
        if (entity.dimension == -1) {
            d = MathHelper.clamp_double(d / d3, worldServer2.getWorldBorder().minX() + 16.0, worldServer2.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp_double(d2 / d3, worldServer2.getWorldBorder().minZ() + 16.0, worldServer2.getWorldBorder().maxZ() - 16.0);
            entity.setLocationAndAngles(d, entity.posY, d2, entity.rotationYaw, entity.rotationPitch);
            if (entity.isEntityAlive()) {
                worldServer.updateEntityWithOptionalForce(entity, false);
            }
        } else if (entity.dimension == 0) {
            d = MathHelper.clamp_double(d * d3, worldServer2.getWorldBorder().minX() + 16.0, worldServer2.getWorldBorder().maxX() - 16.0);
            d2 = MathHelper.clamp_double(d2 * d3, worldServer2.getWorldBorder().minZ() + 16.0, worldServer2.getWorldBorder().maxZ() - 16.0);
            entity.setLocationAndAngles(d, entity.posY, d2, entity.rotationYaw, entity.rotationPitch);
            if (entity.isEntityAlive()) {
                worldServer.updateEntityWithOptionalForce(entity, false);
            }
        } else {
            BlockPos blockPos = n == 1 ? worldServer2.getSpawnPoint() : worldServer2.getSpawnCoordinate();
            d = blockPos.getX();
            entity.posY = blockPos.getY();
            d2 = blockPos.getZ();
            entity.setLocationAndAngles(d, entity.posY, d2, 90.0f, 0.0f);
            if (entity.isEntityAlive()) {
                worldServer.updateEntityWithOptionalForce(entity, false);
            }
        }
        worldServer.theProfiler.endSection();
        if (n != 1) {
            worldServer.theProfiler.startSection("placing");
            d = MathHelper.clamp_int((int)d, -29999872, 29999872);
            d2 = MathHelper.clamp_int((int)d2, -29999872, 29999872);
            if (entity.isEntityAlive()) {
                entity.setLocationAndAngles(d, entity.posY, d2, entity.rotationYaw, entity.rotationPitch);
                worldServer2.getDefaultTeleporter().placeInPortal(entity, f);
                worldServer2.spawnEntityInWorld(entity);
                worldServer2.updateEntityWithOptionalForce(entity, false);
            }
            worldServer.theProfiler.endSection();
        }
        entity.setWorld(worldServer2);
    }

    public void removeOp(GameProfile gameProfile) {
        this.ops.removeEntry(gameProfile);
    }

    public String allowUserToConnect(SocketAddress socketAddress, GameProfile gameProfile) {
        if (this.bannedPlayers.isBanned(gameProfile)) {
            UserListBansEntry userListBansEntry = (UserListBansEntry)this.bannedPlayers.getEntry(gameProfile);
            String string = "You are banned from this server!\nReason: " + userListBansEntry.getBanReason();
            if (userListBansEntry.getBanEndDate() != null) {
                string = String.valueOf(string) + "\nYour ban will be removed on " + dateFormat.format(userListBansEntry.getBanEndDate());
            }
            return string;
        }
        if (!this.canJoin(gameProfile)) {
            return "You are not white-listed on this server!";
        }
        if (this.bannedIPs.isBanned(socketAddress)) {
            IPBanEntry iPBanEntry = this.bannedIPs.getBanEntry(socketAddress);
            String string = "Your IP address is banned from this server!\nReason: " + iPBanEntry.getBanReason();
            if (iPBanEntry.getBanEndDate() != null) {
                string = String.valueOf(string) + "\nYour ban will be removed on " + dateFormat.format(iPBanEntry.getBanEndDate());
            }
            return string;
        }
        return this.playerEntityList.size() >= this.maxPlayers && !this.func_183023_f(gameProfile) ? "The server is full!" : null;
    }

    public void sendMessageToTeamOrEvryPlayer(EntityPlayer entityPlayer, IChatComponent iChatComponent) {
        Team team = entityPlayer.getTeam();
        if (team == null) {
            this.sendChatMsg(iChatComponent);
        } else {
            int n = 0;
            while (n < this.playerEntityList.size()) {
                EntityPlayerMP entityPlayerMP = this.playerEntityList.get(n);
                if (entityPlayerMP.getTeam() != team) {
                    entityPlayerMP.addChatMessage(iChatComponent);
                }
                ++n;
            }
        }
    }

    public void removePlayerFromWhitelist(GameProfile gameProfile) {
        this.whiteListedPlayers.removeEntry(gameProfile);
    }

    public UserListWhitelist getWhitelistedPlayers() {
        return this.whiteListedPlayers;
    }

    public NBTTagCompound getHostPlayerData() {
        return null;
    }

    public void loadWhiteList() {
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public void sendChatMsgImpl(IChatComponent iChatComponent, boolean bl) {
        this.mcServer.addChatMessage(iChatComponent);
        byte by = (byte)(bl ? 1 : 0);
        this.sendPacketToAllPlayers(new S02PacketChat(iChatComponent, by));
    }

    public String[] getWhitelistedPlayerNames() {
        return this.whiteListedPlayers.getKeys();
    }

    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP entityPlayerMP) {
        NBTTagCompound nBTTagCompound;
        NBTTagCompound nBTTagCompound2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        if (entityPlayerMP.getName().equals(this.mcServer.getServerOwner()) && nBTTagCompound2 != null) {
            entityPlayerMP.readFromNBT(nBTTagCompound2);
            nBTTagCompound = nBTTagCompound2;
            logger.debug("loading single player");
        } else {
            nBTTagCompound = this.playerNBTManagerObj.readPlayerData(entityPlayerMP);
        }
        return nBTTagCompound;
    }

    public boolean func_183023_f(GameProfile gameProfile) {
        return false;
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.gameType = gameType;
    }

    protected void writePlayerData(EntityPlayerMP entityPlayerMP) {
        this.playerNBTManagerObj.writePlayerData(entityPlayerMP);
        StatisticsFile statisticsFile = this.playerStatFiles.get(entityPlayerMP.getUniqueID());
        if (statisticsFile != null) {
            statisticsFile.saveStatFile();
        }
    }

    public void addWhitelistedPlayer(GameProfile gameProfile) {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(gameProfile));
    }

    public EntityPlayerMP createPlayerForUser(GameProfile gameProfile) {
        Object object2;
        UUID uUID = EntityPlayer.getUUID(gameProfile);
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < this.playerEntityList.size()) {
            object2 = this.playerEntityList.get(n);
            if (((Entity)object2).getUniqueID().equals(uUID)) {
                arrayList.add(object2);
            }
            ++n;
        }
        EntityPlayerMP entityPlayerMP = this.uuidToPlayerMap.get(gameProfile.getId());
        if (entityPlayerMP != null && !arrayList.contains(entityPlayerMP)) {
            arrayList.add(entityPlayerMP);
        }
        for (Object object2 : arrayList) {
            ((EntityPlayerMP)object2).playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }
        object2 = this.mcServer.isDemo() ? new DemoWorldManager(this.mcServer.worldServerForDimension(0)) : new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), gameProfile, (ItemInWorldManager)object2);
    }

    public void sendMessageToAllTeamMembers(EntityPlayer entityPlayer, IChatComponent iChatComponent) {
        Team team = entityPlayer.getTeam();
        if (team != null) {
            for (String string : team.getMembershipCollection()) {
                EntityPlayerMP entityPlayerMP = this.getPlayerByUsername(string);
                if (entityPlayerMP == null || entityPlayerMP == entityPlayer) continue;
                entityPlayerMP.addChatMessage(iChatComponent);
            }
        }
    }

    public void sendPacketToAllPlayers(Packet packet) {
        int n = 0;
        while (n < this.playerEntityList.size()) {
            this.playerEntityList.get((int)n).playerNetServerHandler.sendPacket(packet);
            ++n;
        }
    }

    public boolean canJoin(GameProfile gameProfile) {
        return !this.whiteListEnforced || this.ops.hasEntry(gameProfile) || this.whiteListedPlayers.hasEntry(gameProfile);
    }

    public String func_181058_b(boolean bl) {
        String string = "";
        ArrayList arrayList = Lists.newArrayList(this.playerEntityList);
        int n = 0;
        while (n < arrayList.size()) {
            if (n > 0) {
                string = String.valueOf(string) + ", ";
            }
            string = String.valueOf(string) + ((EntityPlayerMP)arrayList.get(n)).getName();
            if (bl) {
                string = String.valueOf(string) + " (" + ((EntityPlayerMP)arrayList.get(n)).getUniqueID().toString() + ")";
            }
            ++n;
        }
        return string;
    }

    public List<EntityPlayerMP> getPlayersMatchingAddress(String string) {
        ArrayList arrayList = Lists.newArrayList();
        for (EntityPlayerMP entityPlayerMP : this.playerEntityList) {
            if (!entityPlayerMP.getPlayerIP().equals(string)) continue;
            arrayList.add(entityPlayerMP);
        }
        return arrayList;
    }

    public int getEntityViewDistance() {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public boolean canSendCommands(GameProfile gameProfile) {
        return this.ops.hasEntry(gameProfile) || this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(gameProfile.getName()) || this.commandsAllowedForAll;
    }

    public StatisticsFile getPlayerStatsFile(EntityPlayer entityPlayer) {
        StatisticsFile statisticsFile;
        UUID uUID = entityPlayer.getUniqueID();
        StatisticsFile statisticsFile2 = statisticsFile = uUID == null ? null : this.playerStatFiles.get(uUID);
        if (statisticsFile == null) {
            File file;
            File file2 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            File file3 = new File(file2, String.valueOf(uUID.toString()) + ".json");
            if (!file3.exists() && (file = new File(file2, String.valueOf(entityPlayer.getName()) + ".json")).exists() && file.isFile()) {
                file.renameTo(file3);
            }
            statisticsFile = new StatisticsFile(this.mcServer, file3);
            statisticsFile.readStatFile();
            this.playerStatFiles.put(uUID, statisticsFile);
        }
        return statisticsFile;
    }

    public void transferPlayerToDimension(EntityPlayerMP entityPlayerMP, int n) {
        int n2 = entityPlayerMP.dimension;
        WorldServer worldServer = this.mcServer.worldServerForDimension(entityPlayerMP.dimension);
        entityPlayerMP.dimension = n;
        WorldServer worldServer2 = this.mcServer.worldServerForDimension(entityPlayerMP.dimension);
        entityPlayerMP.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityPlayerMP.dimension, entityPlayerMP.worldObj.getDifficulty(), entityPlayerMP.worldObj.getWorldInfo().getTerrainType(), entityPlayerMP.theItemInWorldManager.getGameType()));
        worldServer.removePlayerEntityDangerously(entityPlayerMP);
        entityPlayerMP.isDead = false;
        this.transferEntityToWorld(entityPlayerMP, n2, worldServer, worldServer2);
        this.preparePlayer(entityPlayerMP, worldServer);
        entityPlayerMP.playerNetServerHandler.setPlayerLocation(entityPlayerMP.posX, entityPlayerMP.posY, entityPlayerMP.posZ, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        entityPlayerMP.theItemInWorldManager.setWorld(worldServer2);
        this.updateTimeAndWeatherForPlayer(entityPlayerMP, worldServer2);
        this.syncPlayerInventory(entityPlayerMP);
        for (PotionEffect potionEffect : entityPlayerMP.getActivePotionEffects()) {
            entityPlayerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(entityPlayerMP.getEntityId(), potionEffect));
        }
    }

    public BanList getBannedIPs() {
        return this.bannedIPs;
    }

    public MinecraftServer getServerInstance() {
        return this.mcServer;
    }

    public void removeAllPlayers() {
        int n = 0;
        while (n < this.playerEntityList.size()) {
            this.playerEntityList.get((int)n).playerNetServerHandler.kickPlayerFromServer("Server closed");
            ++n;
        }
    }

    public void preparePlayer(EntityPlayerMP entityPlayerMP, WorldServer worldServer) {
        WorldServer worldServer2 = entityPlayerMP.getServerForPlayer();
        if (worldServer != null) {
            worldServer.getPlayerManager().removePlayer(entityPlayerMP);
        }
        worldServer2.getPlayerManager().addPlayer(entityPlayerMP);
        worldServer2.theChunkProviderServer.loadChunk((int)entityPlayerMP.posX >> 4, (int)entityPlayerMP.posZ >> 4);
    }

    public void initializeConnectionToPlayer(NetworkManager networkManager, EntityPlayerMP entityPlayerMP) {
        Entity entity;
        GameProfile gameProfile = entityPlayerMP.getGameProfile();
        PlayerProfileCache playerProfileCache = this.mcServer.getPlayerProfileCache();
        GameProfile gameProfile2 = playerProfileCache.getProfileByUUID(gameProfile.getId());
        String string = gameProfile2 == null ? gameProfile.getName() : gameProfile2.getName();
        playerProfileCache.addEntry(gameProfile);
        NBTTagCompound nBTTagCompound = this.readPlayerDataFromFile(entityPlayerMP);
        entityPlayerMP.setWorld(this.mcServer.worldServerForDimension(entityPlayerMP.dimension));
        entityPlayerMP.theItemInWorldManager.setWorld((WorldServer)entityPlayerMP.worldObj);
        String string2 = "local";
        if (networkManager.getRemoteAddress() != null) {
            string2 = networkManager.getRemoteAddress().toString();
        }
        logger.info(String.valueOf(entityPlayerMP.getName()) + "[" + string2 + "] logged in with entity id " + entityPlayerMP.getEntityId() + " at (" + entityPlayerMP.posX + ", " + entityPlayerMP.posY + ", " + entityPlayerMP.posZ + ")");
        WorldServer worldServer = this.mcServer.worldServerForDimension(entityPlayerMP.dimension);
        WorldInfo worldInfo = worldServer.getWorldInfo();
        BlockPos blockPos = worldServer.getSpawnPoint();
        this.setPlayerGameTypeBasedOnOther(entityPlayerMP, null, worldServer);
        NetHandlerPlayServer netHandlerPlayServer = new NetHandlerPlayServer(this.mcServer, networkManager, entityPlayerMP);
        netHandlerPlayServer.sendPacket(new S01PacketJoinGame(entityPlayerMP.getEntityId(), entityPlayerMP.theItemInWorldManager.getGameType(), worldInfo.isHardcoreModeEnabled(), worldServer.provider.getDimensionId(), worldServer.getDifficulty(), this.getMaxPlayers(), worldInfo.getTerrainType(), worldServer.getGameRules().getBoolean("reducedDebugInfo")));
        netHandlerPlayServer.sendPacket(new S3FPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
        netHandlerPlayServer.sendPacket(new S41PacketServerDifficulty(worldInfo.getDifficulty(), worldInfo.isDifficultyLocked()));
        netHandlerPlayServer.sendPacket(new S05PacketSpawnPosition(blockPos));
        netHandlerPlayServer.sendPacket(new S39PacketPlayerAbilities(entityPlayerMP.capabilities));
        netHandlerPlayServer.sendPacket(new S09PacketHeldItemChange(entityPlayerMP.inventory.currentItem));
        entityPlayerMP.getStatFile().func_150877_d();
        entityPlayerMP.getStatFile().sendAchievements(entityPlayerMP);
        this.sendScoreboard((ServerScoreboard)worldServer.getScoreboard(), entityPlayerMP);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation chatComponentTranslation = !entityPlayerMP.getName().equalsIgnoreCase(string) ? new ChatComponentTranslation("multiplayer.player.joined.renamed", entityPlayerMP.getDisplayName(), string) : new ChatComponentTranslation("multiplayer.player.joined", entityPlayerMP.getDisplayName());
        chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(chatComponentTranslation);
        this.playerLoggedIn(entityPlayerMP);
        netHandlerPlayServer.setPlayerLocation(entityPlayerMP.posX, entityPlayerMP.posY, entityPlayerMP.posZ, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
        this.updateTimeAndWeatherForPlayer(entityPlayerMP, worldServer);
        if (this.mcServer.getResourcePackUrl().length() > 0) {
            entityPlayerMP.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }
        for (PotionEffect object2 : entityPlayerMP.getActivePotionEffects()) {
            netHandlerPlayServer.sendPacket(new S1DPacketEntityEffect(entityPlayerMP.getEntityId(), object2));
        }
        entityPlayerMP.addSelfToInternalCraftingInventory();
        if (nBTTagCompound != null && nBTTagCompound.hasKey("Riding", 10) && (entity = EntityList.createEntityFromNBT(nBTTagCompound.getCompoundTag("Riding"), worldServer)) != null) {
            entity.forceSpawn = true;
            worldServer.spawnEntityInWorld(entity);
            entityPlayerMP.mountEntity(entity);
            entity.forceSpawn = false;
        }
    }

    public void onTick() {
        if (++this.playerPingIndex > 600) {
            this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
            this.playerPingIndex = 0;
        }
    }

    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }

    public String[] getAvailablePlayerDat() {
        return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP entityPlayerMP, WorldServer worldServer) {
        WorldBorder worldBorder = this.mcServer.worldServers[0].getWorldBorder();
        entityPlayerMP.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(worldBorder, S44PacketWorldBorder.Action.INITIALIZE));
        entityPlayerMP.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldServer.getTotalWorldTime(), worldServer.getWorldTime(), worldServer.getGameRules().getBoolean("doDaylightCycle")));
        if (worldServer.isRaining()) {
            entityPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0f));
            entityPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldServer.getRainStrength(1.0f)));
            entityPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldServer.getThunderStrength(1.0f)));
        }
    }

    public ServerConfigurationManager(MinecraftServer minecraftServer) {
        this.uuidToPlayerMap = Maps.newHashMap();
        this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
        this.bannedIPs = new BanList(FILE_IPBANS);
        this.ops = new UserListOps(FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
        this.playerStatFiles = Maps.newHashMap();
        this.mcServer = minecraftServer;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }
}

