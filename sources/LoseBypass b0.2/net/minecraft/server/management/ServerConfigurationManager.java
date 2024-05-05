/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.Unpooled
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
import java.util.Iterator;
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
    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private final MinecraftServer mcServer;
    private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
    private final UserListBans bannedPlayers = new UserListBans(FILE_PLAYERBANS);
    private final BanList bannedIPs = new BanList(FILE_IPBANS);
    private final UserListOps ops = new UserListOps(FILE_OPS);
    private final UserListWhitelist whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
    private final Map<UUID, StatisticsFile> playerStatFiles = Maps.newHashMap();
    private IPlayerFileData playerNBTManagerObj;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;

    public ServerConfigurationManager(MinecraftServer server) {
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
        GameProfile gameprofile = playerIn.getGameProfile();
        PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
        GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
        playerprofilecache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
        String s1 = "local";
        if (netManager.getRemoteAddress() != null) {
            s1 = netManager.getRemoteAddress().toString();
        }
        logger.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        WorldInfo worldinfo = worldserver.getWorldInfo();
        BlockPos blockpos = worldserver.getSpawnPoint();
        this.setPlayerGameTypeBasedOnOther(playerIn, null, worldserver);
        NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
        nethandlerplayserver.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionId(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new S3FPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver.sendPacket(new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new S05PacketSpawnPosition(blockpos));
        nethandlerplayserver.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().sendAchievements(playerIn);
        this.sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation chatcomponenttranslation = !playerIn.getName().equalsIgnoreCase(s) ? new ChatComponentTranslation("multiplayer.player.joined.renamed", playerIn.getDisplayName(), s) : new ChatComponentTranslation("multiplayer.player.joined", playerIn.getDisplayName());
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(chatcomponenttranslation);
        this.playerLoggedIn(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);
        if (this.mcServer.getResourcePackUrl().length() > 0) {
            playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }
        Iterator<PotionEffect> iterator = playerIn.getActivePotionEffects().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                playerIn.addSelfToInternalCraftingInventory();
                if (nbttagcompound == null) return;
                if (!nbttagcompound.hasKey("Riding", 10)) return;
                Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldserver);
                if (entity == null) return;
                entity.forceSpawn = true;
                worldserver.spawnEntityInWorld(entity);
                playerIn.mountEntity(entity);
                entity.forceSpawn = false;
                return;
            }
            PotionEffect potioneffect = iterator.next();
            nethandlerplayserver.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }
    }

    protected void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn) {
        HashSet<ScoreObjective> set = Sets.newHashSet();
        for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams()) {
            playerIn.playerNetServerHandler.sendPacket(new S3EPacketTeams(scoreplayerteam, 0));
        }
        int i = 0;
        while (i < 19) {
            ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);
            if (scoreobjective != null && !set.contains(scoreobjective)) {
                for (Packet packet : scoreboardIn.func_96550_d(scoreobjective)) {
                    playerIn.playerNetServerHandler.sendPacket(packet);
                }
                set.add(scoreobjective);
            }
            ++i;
        }
    }

    public void setPlayerManager(WorldServer[] worldServers) {
        this.playerNBTManagerObj = worldServers[0].getSaveHandler().getPlayerNBTManager();
        worldServers[0].getWorldBorder().addListener(new IBorderListener(){

            @Override
            public void onSizeChanged(WorldBorder border, double newSize) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
            }

            @Override
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
            }

            @Override
            public void onCenterChanged(WorldBorder border, double x, double z) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
            }

            @Override
            public void onWarningTimeChanged(WorldBorder border, int newTime) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
            }

            @Override
            public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }

            @Override
            public void onDamageAmountChanged(WorldBorder border, double newAmount) {
            }

            @Override
            public void onDamageBufferChanged(WorldBorder border, double newSize) {
            }
        });
    }

    public void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
        WorldServer worldserver = playerIn.getServerForPlayer();
        if (worldIn != null) {
            worldIn.getPlayerManager().removePlayer(playerIn);
        }
        worldserver.getPlayerManager().addPlayer(playerIn);
        worldserver.theChunkProviderServer.loadChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
    }

    public int getEntityViewDistance() {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }

    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn) {
        NBTTagCompound nbttagcompound = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        if (!playerIn.getName().equals(this.mcServer.getServerOwner())) return this.playerNBTManagerObj.readPlayerData(playerIn);
        if (nbttagcompound == null) return this.playerNBTManagerObj.readPlayerData(playerIn);
        playerIn.readFromNBT(nbttagcompound);
        NBTTagCompound nbttagcompound1 = nbttagcompound;
        logger.debug("loading single player");
        return nbttagcompound1;
    }

    protected void writePlayerData(EntityPlayerMP playerIn) {
        this.playerNBTManagerObj.writePlayerData(playerIn);
        StatisticsFile statisticsfile = this.playerStatFiles.get(playerIn.getUniqueID());
        if (statisticsfile == null) return;
        statisticsfile.saveStatFile();
    }

    public void playerLoggedIn(EntityPlayerMP playerIn) {
        this.playerEntityList.add(playerIn);
        this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, playerIn));
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        worldserver.spawnEntityInWorld(playerIn);
        this.preparePlayer(playerIn, null);
        int i = 0;
        while (i < this.playerEntityList.size()) {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, entityplayermp));
            ++i;
        }
    }

    public void serverUpdateMountedMovingPlayer(EntityPlayerMP playerIn) {
        playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
    }

    public void playerLoggedOut(EntityPlayerMP playerIn) {
        playerIn.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(playerIn);
        WorldServer worldserver = playerIn.getServerForPlayer();
        if (playerIn.ridingEntity != null) {
            worldserver.removePlayerEntityDangerously(playerIn.ridingEntity);
            logger.debug("removing player mount");
        }
        worldserver.removeEntity(playerIn);
        worldserver.getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        UUID uuid = playerIn.getUniqueID();
        EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);
        if (entityplayermp == playerIn) {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
        }
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, playerIn));
    }

    public String allowUserToConnect(SocketAddress address, GameProfile profile) {
        if (this.bannedPlayers.isBanned(profile)) {
            UserListBansEntry userlistbansentry = (UserListBansEntry)this.bannedPlayers.getEntry(profile);
            String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();
            if (userlistbansentry.getBanEndDate() == null) return s1;
            return s1 + "\nYour ban will be removed on " + dateFormat.format(userlistbansentry.getBanEndDate());
        }
        if (!this.canJoin(profile)) {
            return "You are not white-listed on this server!";
        }
        if (this.bannedIPs.isBanned(address)) {
            IPBanEntry ipbanentry = this.bannedIPs.getBanEntry(address);
            String s = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
            if (ipbanentry.getBanEndDate() == null) return s;
            return s + "\nYour ban will be removed on " + dateFormat.format(ipbanentry.getBanEndDate());
        }
        if (this.playerEntityList.size() < this.maxPlayers) return null;
        if (this.func_183023_f(profile)) return null;
        return "The server is full!";
    }

    public EntityPlayerMP createPlayerForUser(GameProfile profile) {
        ItemInWorldManager iteminworldmanager;
        UUID uuid = EntityPlayer.getUUID(profile);
        ArrayList<Object> list = Lists.newArrayList();
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (!entityplayermp.getUniqueID().equals(uuid)) continue;
            list.add(entityplayermp);
        }
        EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());
        if (entityplayermp2 != null && !list.contains(entityplayermp2)) {
            list.add(entityplayermp2);
        }
        for (EntityPlayerMP entityPlayerMP : list) {
            entityPlayerMP.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }
        if (this.mcServer.isDemo()) {
            iteminworldmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
            return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, iteminworldmanager);
        }
        iteminworldmanager = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, iteminworldmanager);
    }

    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd) {
        playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
        playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
        BlockPos blockpos = playerIn.getBedLocation();
        boolean flag = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        ItemInWorldManager iteminworldmanager = this.mcServer.isDemo() ? new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension)) : new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), iteminworldmanager);
        entityplayermp.playerNetServerHandler = playerIn.playerNetServerHandler;
        entityplayermp.clonePlayer(playerIn, conqueredEnd);
        entityplayermp.setEntityId(playerIn.getEntityId());
        entityplayermp.func_174817_o(playerIn);
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);
        if (blockpos != null) {
            BlockPos blockpos1 = EntityPlayer.getBedSpawnLocation(this.mcServer.worldServerForDimension(playerIn.dimension), blockpos, flag);
            if (blockpos1 != null) {
                entityplayermp.setLocationAndAngles((float)blockpos1.getX() + 0.5f, (float)blockpos1.getY() + 0.1f, (float)blockpos1.getZ() + 0.5f, 0.0f, 0.0f);
                entityplayermp.setSpawnPoint(blockpos, flag);
            } else {
                entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0f));
            }
        }
        worldserver.theChunkProviderServer.loadChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);
        while (!worldserver.getCollidingBoundingBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty() && entityplayermp.posY < 256.0) {
            entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0, entityplayermp.posZ);
        }
        entityplayermp.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityplayermp.dimension, entityplayermp.worldObj.getDifficulty(), entityplayermp.worldObj.getWorldInfo().getTerrainType(), entityplayermp.theItemInWorldManager.getGameType()));
        BlockPos blockpos2 = worldserver.getSpawnPoint();
        entityplayermp.playerNetServerHandler.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockpos2));
        entityplayermp.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
        worldserver.getPlayerManager().addPlayer(entityplayermp);
        worldserver.spawnEntityInWorld(entityplayermp);
        this.playerEntityList.add(entityplayermp);
        this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
        entityplayermp.addSelfToInternalCraftingInventory();
        entityplayermp.setHealth(entityplayermp.getHealth());
        return entityplayermp;
    }

    public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension) {
        int i = playerIn.dimension;
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.dimension = dimension;
        WorldServer worldserver1 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(playerIn);
        playerIn.isDead = false;
        this.transferEntityToWorld(playerIn, i, worldserver, worldserver1);
        this.preparePlayer(playerIn, worldserver);
        playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        playerIn.theItemInWorldManager.setWorld(worldserver1);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver1);
        this.syncPlayerInventory(playerIn);
        Iterator<PotionEffect> iterator = playerIn.getActivePotionEffects().iterator();
        while (iterator.hasNext()) {
            PotionEffect potioneffect = iterator.next();
            playerIn.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }
    }

    public void transferEntityToWorld(Entity entityIn, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_) {
        double d0 = entityIn.posX;
        double d1 = entityIn.posZ;
        double d2 = 8.0;
        float f = entityIn.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");
        if (entityIn.dimension == -1) {
            d0 = MathHelper.clamp_double(d0 / d2, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            d1 = MathHelper.clamp_double(d1 / d2, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        } else if (entityIn.dimension == 0) {
            d0 = MathHelper.clamp_double(d0 * d2, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            d1 = MathHelper.clamp_double(d1 * d2, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        } else {
            BlockPos blockpos = p_82448_2_ == 1 ? p_82448_4_.getSpawnPoint() : p_82448_4_.getSpawnCoordinate();
            d0 = blockpos.getX();
            entityIn.posY = blockpos.getY();
            d1 = blockpos.getZ();
            entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0f, 0.0f);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        p_82448_3_.theProfiler.endSection();
        if (p_82448_2_ != 1) {
            p_82448_3_.theProfiler.startSection("placing");
            d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
            d1 = MathHelper.clamp_int((int)d1, -29999872, 29999872);
            if (entityIn.isEntityAlive()) {
                entityIn.setLocationAndAngles(d0, entityIn.posY, d1, entityIn.rotationYaw, entityIn.rotationPitch);
                p_82448_4_.getDefaultTeleporter().placeInPortal(entityIn, f);
                p_82448_4_.spawnEntityInWorld(entityIn);
                p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
            }
            p_82448_3_.theProfiler.endSection();
        }
        entityIn.setWorld(p_82448_4_);
    }

    public void onTick() {
        if (++this.playerPingIndex <= 600) return;
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
        this.playerPingIndex = 0;
    }

    public void sendPacketToAllPlayers(Packet packetIn) {
        int i = 0;
        while (i < this.playerEntityList.size()) {
            this.playerEntityList.get((int)i).playerNetServerHandler.sendPacket(packetIn);
            ++i;
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension) {
        int i = 0;
        while (i < this.playerEntityList.size()) {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.dimension == dimension) {
                entityplayermp.playerNetServerHandler.sendPacket(packetIn);
            }
            ++i;
        }
    }

    public void sendMessageToAllTeamMembers(EntityPlayer player, IChatComponent message) {
        Team team = player.getTeam();
        if (team == null) return;
        Iterator<String> iterator = team.getMembershipCollection().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);
            if (entityplayermp == null || entityplayermp == player) continue;
            entityplayermp.addChatMessage(message);
        }
    }

    public void sendMessageToTeamOrEvryPlayer(EntityPlayer player, IChatComponent message) {
        Team team = player.getTeam();
        if (team == null) {
            this.sendChatMsg(message);
            return;
        }
        int i = 0;
        while (i < this.playerEntityList.size()) {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp.getTeam() != team) {
                entityplayermp.addChatMessage(message);
            }
            ++i;
        }
    }

    public String func_181058_b(boolean p_181058_1_) {
        String s = "";
        ArrayList<EntityPlayerMP> list = Lists.newArrayList(this.playerEntityList);
        int i = 0;
        while (i < list.size()) {
            if (i > 0) {
                s = s + ", ";
            }
            s = s + ((EntityPlayerMP)list.get(i)).getName();
            if (p_181058_1_) {
                s = s + " (" + ((EntityPlayerMP)list.get(i)).getUniqueID().toString() + ")";
            }
            ++i;
        }
        return s;
    }

    public String[] getAllUsernames() {
        String[] astring = new String[this.playerEntityList.size()];
        int i = 0;
        while (i < this.playerEntityList.size()) {
            astring[i] = this.playerEntityList.get(i).getName();
            ++i;
        }
        return astring;
    }

    public GameProfile[] getAllProfiles() {
        GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];
        int i = 0;
        while (i < this.playerEntityList.size()) {
            agameprofile[i] = this.playerEntityList.get(i).getGameProfile();
            ++i;
        }
        return agameprofile;
    }

    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }

    public BanList getBannedIPs() {
        return this.bannedIPs;
    }

    public void addOp(GameProfile profile) {
        this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel(), this.ops.func_183026_b(profile)));
    }

    public void removeOp(GameProfile profile) {
        this.ops.removeEntry(profile);
    }

    public boolean canJoin(GameProfile profile) {
        if (!this.whiteListEnforced) return true;
        if (this.ops.hasEntry(profile)) return true;
        if (this.whiteListedPlayers.hasEntry(profile)) return true;
        return false;
    }

    public boolean canSendCommands(GameProfile profile) {
        if (this.ops.hasEntry(profile)) return true;
        if (this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed()) {
            if (this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName())) return true;
        }
        if (this.commandsAllowedForAll) return true;
        return false;
    }

    public EntityPlayerMP getPlayerByUsername(String username) {
        EntityPlayerMP entityplayermp;
        Iterator<EntityPlayerMP> iterator = this.playerEntityList.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!(entityplayermp = iterator.next()).getName().equalsIgnoreCase(username));
        return entityplayermp;
    }

    public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn) {
        this.sendToAllNearExcept(null, x, y, z, radius, dimension, packetIn);
    }

    public void sendToAllNearExcept(EntityPlayer p_148543_1_, double x, double y, double z, double radius, int dimension, Packet p_148543_11_) {
        int i = 0;
        while (i < this.playerEntityList.size()) {
            double d2;
            double d1;
            double d0;
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            if (entityplayermp != p_148543_1_ && entityplayermp.dimension == dimension && (d0 = x - entityplayermp.posX) * d0 + (d1 = y - entityplayermp.posY) * d1 + (d2 = z - entityplayermp.posZ) * d2 < radius * radius) {
                entityplayermp.playerNetServerHandler.sendPacket(p_148543_11_);
            }
            ++i;
        }
    }

    public void saveAllPlayerData() {
        int i = 0;
        while (i < this.playerEntityList.size()) {
            this.writePlayerData(this.playerEntityList.get(i));
            ++i;
        }
    }

    public void addWhitelistedPlayer(GameProfile profile) {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
    }

    public void removePlayerFromWhitelist(GameProfile profile) {
        this.whiteListedPlayers.removeEntry(profile);
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

    public void loadWhiteList() {
    }

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn) {
        WorldBorder worldborder = this.mcServer.worldServers[0].getWorldBorder();
        playerIn.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE));
        playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
        if (!worldIn.isRaining()) return;
        playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0f));
        playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0f)));
        playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getThunderStrength(1.0f)));
    }

    public void syncPlayerInventory(EntityPlayerMP playerIn) {
        playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
        playerIn.setPlayerHealthUpdated();
        playerIn.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
    }

    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String[] getAvailablePlayerDat() {
        return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public void setWhiteListEnabled(boolean whitelistEnabled) {
        this.whiteListEnforced = whitelistEnabled;
    }

    public List<EntityPlayerMP> getPlayersMatchingAddress(String address) {
        ArrayList<EntityPlayerMP> list = Lists.newArrayList();
        Iterator<EntityPlayerMP> iterator = this.playerEntityList.iterator();
        while (iterator.hasNext()) {
            EntityPlayerMP entityplayermp = iterator.next();
            if (!entityplayermp.getPlayerIP().equals(address)) continue;
            list.add(entityplayermp);
        }
        return list;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance() {
        return this.mcServer;
    }

    public NBTTagCompound getHostPlayerData() {
        return null;
    }

    public void setGameType(WorldSettings.GameType p_152604_1_) {
        this.gameType = p_152604_1_;
    }

    private void setPlayerGameTypeBasedOnOther(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World worldIn) {
        if (p_72381_2_ != null) {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        } else if (this.gameType != null) {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }
        p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }

    public void setCommandsAllowedForAll(boolean p_72387_1_) {
        this.commandsAllowedForAll = p_72387_1_;
    }

    public void removeAllPlayers() {
        int i = 0;
        while (i < this.playerEntityList.size()) {
            this.playerEntityList.get((int)i).playerNetServerHandler.kickPlayerFromServer("Server closed");
            ++i;
        }
    }

    public void sendChatMsgImpl(IChatComponent component, boolean isChat) {
        this.mcServer.addChatMessage(component);
        byte b0 = (byte)(isChat ? 1 : 0);
        this.sendPacketToAllPlayers(new S02PacketChat(component, b0));
    }

    public void sendChatMsg(IChatComponent component) {
        this.sendChatMsgImpl(component, true);
    }

    public StatisticsFile getPlayerStatsFile(EntityPlayer playerIn) {
        File file3;
        UUID uuid = playerIn.getUniqueID();
        StatisticsFile statisticsfile = uuid == null ? null : this.playerStatFiles.get(uuid);
        if (statisticsfile != null) return statisticsfile;
        File file1 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
        File file2 = new File(file1, uuid.toString() + ".json");
        if (!file2.exists() && (file3 = new File(file1, playerIn.getName() + ".json")).exists() && file3.isFile()) {
            file3.renameTo(file2);
        }
        statisticsfile = new StatisticsFile(this.mcServer, file2);
        statisticsfile.readStatFile();
        this.playerStatFiles.put(uuid, statisticsfile);
        return statisticsfile;
    }

    public void setViewDistance(int distance) {
        this.viewDistance = distance;
        if (this.mcServer.worldServers == null) return;
        WorldServer[] worldServerArray = this.mcServer.worldServers;
        int n = worldServerArray.length;
        int n2 = 0;
        while (n2 < n) {
            WorldServer worldserver = worldServerArray[n2];
            if (worldserver != null) {
                worldserver.getPlayerManager().setPlayerViewRadius(distance);
            }
            ++n2;
        }
    }

    public List<EntityPlayerMP> func_181057_v() {
        return this.playerEntityList;
    }

    public EntityPlayerMP getPlayerByUUID(UUID playerUUID) {
        return this.uuidToPlayerMap.get(playerUUID);
    }

    public boolean func_183023_f(GameProfile p_183023_1_) {
        return false;
    }
}

