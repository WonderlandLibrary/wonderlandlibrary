// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import java.util.Collection;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.world.demo.DemoWorldManager;
import java.net.SocketAddress;
import net.minecraft.stats.StatList;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.HashSet;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityList;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.IPlayerFileData;
import java.util.Map;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import java.io.File;

public abstract class ServerConfigurationManager
{
    public static final File FILE_PLAYERBANS;
    public static final File FILE_IPBANS;
    public static final File FILE_OPS;
    public static final File FILE_WHITELIST;
    private static final Logger logger;
    private static final SimpleDateFormat dateFormat;
    private final MinecraftServer mcServer;
    public final List playerEntityList;
    public final Map field_177454_f;
    private final UserListBans bannedPlayers;
    private final BanList bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map playerStatFiles;
    private IPlayerFileData playerNBTManagerObj;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    private static final String __OBFID = "CL_00001423";
    
    public ServerConfigurationManager(final MinecraftServer server) {
        this.playerEntityList = Lists.newArrayList();
        this.field_177454_f = Maps.newHashMap();
        this.bannedPlayers = new UserListBans(ServerConfigurationManager.FILE_PLAYERBANS);
        this.bannedIPs = new BanList(ServerConfigurationManager.FILE_IPBANS);
        this.ops = new UserListOps(ServerConfigurationManager.FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(ServerConfigurationManager.FILE_WHITELIST);
        this.playerStatFiles = Maps.newHashMap();
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }
    
    public void initializeConnectionToPlayer(final NetworkManager netManager, final EntityPlayerMP playerIn) {
        final GameProfile var3 = playerIn.getGameProfile();
        final PlayerProfileCache var4 = this.mcServer.getPlayerProfileCache();
        final GameProfile var5 = var4.func_152652_a(var3.getId());
        final String var6 = (var5 == null) ? var3.getName() : var5.getName();
        var4.func_152649_a(var3);
        final NBTTagCompound var7 = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
        String var8 = "local";
        if (netManager.getRemoteAddress() != null) {
            var8 = netManager.getRemoteAddress().toString();
        }
        ServerConfigurationManager.logger.info(playerIn.getName() + "[" + var8 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        final WorldServer var9 = this.mcServer.worldServerForDimension(playerIn.dimension);
        final WorldInfo var10 = var9.getWorldInfo();
        final BlockPos var11 = var9.getSpawnPoint();
        this.func_72381_a(playerIn, null, var9);
        final NetHandlerPlayServer var12 = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);
        var12.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), var10.isHardcoreModeEnabled(), var9.provider.getDimensionId(), var9.getDifficulty(), this.getMaxPlayers(), var10.getTerrainType(), var9.getGameRules().getGameRuleBooleanValue("reducedDebugInfo")));
        var12.sendPacket(new S3FPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
        var12.sendPacket(new S41PacketServerDifficulty(var10.getDifficulty(), var10.isDifficultyLocked()));
        var12.sendPacket(new S05PacketSpawnPosition(var11));
        var12.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
        var12.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().func_150884_b(playerIn);
        this.func_96456_a((ServerScoreboard)var9.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation var13;
        if (!playerIn.getName().equalsIgnoreCase(var6)) {
            var13 = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] { playerIn.getDisplayName(), var6 });
        }
        else {
            var13 = new ChatComponentTranslation("multiplayer.player.joined", new Object[] { playerIn.getDisplayName() });
        }
        var13.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(var13);
        this.playerLoggedIn(playerIn);
        var12.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, var9);
        if (this.mcServer.getResourcePackUrl().length() > 0) {
            playerIn.func_175397_a(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }
        for (final PotionEffect var15 : playerIn.getActivePotionEffects()) {
            var12.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), var15));
        }
        playerIn.addSelfToInternalCraftingInventory();
        if (var7 != null && var7.hasKey("Riding", 10)) {
            final Entity var16 = EntityList.createEntityFromNBT(var7.getCompoundTag("Riding"), var9);
            if (var16 != null) {
                var16.forceSpawn = true;
                var9.spawnEntityInWorld(var16);
                playerIn.mountEntity(var16);
                var16.forceSpawn = false;
            }
        }
    }
    
    protected void func_96456_a(final ServerScoreboard scoreboardIn, final EntityPlayerMP playerIn) {
        final HashSet var3 = Sets.newHashSet();
        for (final ScorePlayerTeam var5 : scoreboardIn.getTeams()) {
            playerIn.playerNetServerHandler.sendPacket(new S3EPacketTeams(var5, 0));
        }
        for (int var6 = 0; var6 < 19; ++var6) {
            final ScoreObjective var7 = scoreboardIn.getObjectiveInDisplaySlot(var6);
            if (var7 != null && !var3.contains(var7)) {
                final List var8 = scoreboardIn.func_96550_d(var7);
                for (final Packet var10 : var8) {
                    playerIn.playerNetServerHandler.sendPacket(var10);
                }
                var3.add(var7);
            }
        }
    }
    
    public void setPlayerManager(final WorldServer[] p_72364_1_) {
        this.playerNBTManagerObj = p_72364_1_[0].getSaveHandler().getPlayerNBTManager();
        p_72364_1_[0].getWorldBorder().addListener(new IBorderListener() {
            private static final String __OBFID = "CL_00002267";
            
            @Override
            public void onSizeChanged(final WorldBorder border, final double newSize) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
            }
            
            @Override
            public void func_177692_a(final WorldBorder border, final double p_177692_2_, final double p_177692_4_, final long p_177692_6_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
            }
            
            @Override
            public void onCenterChanged(final WorldBorder border, final double x, final double z) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
            }
            
            @Override
            public void onWarningTimeChanged(final WorldBorder border, final int p_177691_2_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
            }
            
            @Override
            public void onWarningDistanceChanged(final WorldBorder border, final int p_177690_2_) {
                ServerConfigurationManager.this.sendPacketToAllPlayers(new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }
            
            @Override
            public void func_177696_b(final WorldBorder border, final double p_177696_2_) {
            }
            
            @Override
            public void func_177695_c(final WorldBorder border, final double p_177695_2_) {
            }
        });
    }
    
    public void func_72375_a(final EntityPlayerMP playerIn, final WorldServer worldIn) {
        final WorldServer var3 = playerIn.getServerForPlayer();
        if (worldIn != null) {
            worldIn.getPlayerManager().removePlayer(playerIn);
        }
        var3.getPlayerManager().addPlayer(playerIn);
        var3.theChunkProviderServer.loadChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
    }
    
    public int getEntityViewDistance() {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }
    
    public NBTTagCompound readPlayerDataFromFile(final EntityPlayerMP playerIn) {
        final NBTTagCompound var2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound var3;
        if (playerIn.getName().equals(this.mcServer.getServerOwner()) && var2 != null) {
            playerIn.readFromNBT(var2);
            var3 = var2;
            ServerConfigurationManager.logger.debug("loading single player");
        }
        else {
            var3 = this.playerNBTManagerObj.readPlayerData(playerIn);
        }
        return var3;
    }
    
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        this.playerNBTManagerObj.writePlayerData(playerIn);
        final StatisticsFile var2 = this.playerStatFiles.get(playerIn.getUniqueID());
        if (var2 != null) {
            var2.func_150883_b();
        }
    }
    
    public void playerLoggedIn(final EntityPlayerMP playerIn) {
        this.playerEntityList.add(playerIn);
        this.field_177454_f.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { playerIn }));
        final WorldServer var2 = this.mcServer.worldServerForDimension(playerIn.dimension);
        var2.spawnEntityInWorld(playerIn);
        this.func_72375_a(playerIn, null);
        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
            final EntityPlayerMP var4 = this.playerEntityList.get(var3);
            playerIn.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { var4 }));
        }
    }
    
    public void serverUpdateMountedMovingPlayer(final EntityPlayerMP playerIn) {
        playerIn.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(playerIn);
    }
    
    public void playerLoggedOut(final EntityPlayerMP playerIn) {
        playerIn.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(playerIn);
        final WorldServer var2 = playerIn.getServerForPlayer();
        if (playerIn.ridingEntity != null) {
            var2.removePlayerEntityDangerously(playerIn.ridingEntity);
            ServerConfigurationManager.logger.debug("removing player mount");
        }
        var2.removeEntity(playerIn);
        var2.getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.field_177454_f.remove(playerIn.getUniqueID());
        this.playerStatFiles.remove(playerIn.getUniqueID());
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { playerIn }));
    }
    
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        if (this.bannedPlayers.isBanned(profile)) {
            final UserListBansEntry var5 = (UserListBansEntry)this.bannedPlayers.getEntry(profile);
            String var6 = "You are banned from this server!\nReason: " + var5.getBanReason();
            if (var5.getBanEndDate() != null) {
                var6 = var6 + "\nYour ban will be removed on " + ServerConfigurationManager.dateFormat.format(var5.getBanEndDate());
            }
            return var6;
        }
        if (!this.canJoin(profile)) {
            return "You are not white-listed on this server!";
        }
        if (this.bannedIPs.isBanned(address)) {
            final IPBanEntry var7 = this.bannedIPs.getBanEntry(address);
            String var6 = "Your IP address is banned from this server!\nReason: " + var7.getBanReason();
            if (var7.getBanEndDate() != null) {
                var6 = var6 + "\nYour ban will be removed on " + ServerConfigurationManager.dateFormat.format(var7.getBanEndDate());
            }
            return var6;
        }
        return (this.playerEntityList.size() >= this.maxPlayers) ? "The server is full!" : null;
    }
    
    public EntityPlayerMP createPlayerForUser(final GameProfile profile) {
        final UUID var2 = EntityPlayer.getUUID(profile);
        final ArrayList var3 = Lists.newArrayList();
        for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
            final EntityPlayerMP var5 = this.playerEntityList.get(var4);
            if (var5.getUniqueID().equals(var2)) {
                var3.add(var5);
            }
        }
        final Iterator var6 = var3.iterator();
        while (var6.hasNext()) {
            final EntityPlayerMP var5 = var6.next();
            var5.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }
        Object var7;
        if (this.mcServer.isDemo()) {
            var7 = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
        }
        else {
            var7 = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        }
        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, (ItemInWorldManager)var7);
    }
    
    public EntityPlayerMP recreatePlayerEntity(final EntityPlayerMP playerIn, final int dimension, final boolean conqueredEnd) {
        playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
        playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.mcServer.worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
        final BlockPos var4 = playerIn.func_180470_cg();
        final boolean var5 = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        Object var6;
        if (this.mcServer.isDemo()) {
            var6 = new DemoWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        }
        else {
            var6 = new ItemInWorldManager(this.mcServer.worldServerForDimension(playerIn.dimension));
        }
        final EntityPlayerMP var7 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(playerIn.dimension), playerIn.getGameProfile(), (ItemInWorldManager)var6);
        var7.playerNetServerHandler = playerIn.playerNetServerHandler;
        var7.clonePlayer(playerIn, conqueredEnd);
        var7.setEntityId(playerIn.getEntityId());
        var7.func_174817_o(playerIn);
        final WorldServer var8 = this.mcServer.worldServerForDimension(playerIn.dimension);
        this.func_72381_a(var7, playerIn, var8);
        if (var4 != null) {
            final BlockPos var9 = EntityPlayer.func_180467_a(this.mcServer.worldServerForDimension(playerIn.dimension), var4, var5);
            if (var9 != null) {
                var7.setLocationAndAngles(var9.getX() + 0.5f, var9.getY() + 0.1f, var9.getZ() + 0.5f, 0.0f, 0.0f);
                var7.func_180473_a(var4, var5);
            }
            else {
                var7.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0f));
            }
        }
        var8.theChunkProviderServer.loadChunk((int)var7.posX >> 4, (int)var7.posZ >> 4);
        while (!var8.getCollidingBoundingBoxes(var7, var7.getEntityBoundingBox()).isEmpty() && var7.posY < 256.0) {
            var7.setPosition(var7.posX, var7.posY + 1.0, var7.posZ);
        }
        var7.playerNetServerHandler.sendPacket(new S07PacketRespawn(var7.dimension, var7.worldObj.getDifficulty(), var7.worldObj.getWorldInfo().getTerrainType(), var7.theItemInWorldManager.getGameType()));
        final BlockPos var9 = var8.getSpawnPoint();
        var7.playerNetServerHandler.setPlayerLocation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
        var7.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(var9));
        var7.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(var7.experience, var7.experienceTotal, var7.experienceLevel));
        this.updateTimeAndWeatherForPlayer(var7, var8);
        var8.getPlayerManager().addPlayer(var7);
        var8.spawnEntityInWorld(var7);
        this.playerEntityList.add(var7);
        this.field_177454_f.put(var7.getUniqueID(), var7);
        var7.addSelfToInternalCraftingInventory();
        var7.setHealth(var7.getHealth());
        return var7;
    }
    
    public void transferPlayerToDimension(final EntityPlayerMP playerIn, final int dimension) {
        final int var3 = playerIn.dimension;
        final WorldServer var4 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.dimension = dimension;
        final WorldServer var5 = this.mcServer.worldServerForDimension(playerIn.dimension);
        playerIn.playerNetServerHandler.sendPacket(new S07PacketRespawn(playerIn.dimension, playerIn.worldObj.getDifficulty(), playerIn.worldObj.getWorldInfo().getTerrainType(), playerIn.theItemInWorldManager.getGameType()));
        var4.removePlayerEntityDangerously(playerIn);
        playerIn.isDead = false;
        this.transferEntityToWorld(playerIn, var3, var4, var5);
        this.func_72375_a(playerIn, var4);
        playerIn.playerNetServerHandler.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        playerIn.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(playerIn, var5);
        this.syncPlayerInventory(playerIn);
        for (final PotionEffect var7 : playerIn.getActivePotionEffects()) {
            playerIn.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), var7));
        }
    }
    
    public void transferEntityToWorld(final Entity entityIn, final int p_82448_2_, final WorldServer p_82448_3_, final WorldServer p_82448_4_) {
        double var5 = entityIn.posX;
        double var6 = entityIn.posZ;
        final double var7 = 8.0;
        final float var8 = entityIn.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");
        if (entityIn.dimension == -1) {
            var5 = MathHelper.clamp_double(var5 / var7, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            var6 = MathHelper.clamp_double(var6 / var7, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(var5, entityIn.posY, var6, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else if (entityIn.dimension == 0) {
            var5 = MathHelper.clamp_double(var5 * var7, p_82448_4_.getWorldBorder().minX() + 16.0, p_82448_4_.getWorldBorder().maxX() - 16.0);
            var6 = MathHelper.clamp_double(var6 * var7, p_82448_4_.getWorldBorder().minZ() + 16.0, p_82448_4_.getWorldBorder().maxZ() - 16.0);
            entityIn.setLocationAndAngles(var5, entityIn.posY, var6, entityIn.rotationYaw, entityIn.rotationPitch);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else {
            BlockPos var9;
            if (p_82448_2_ == 1) {
                var9 = p_82448_4_.getSpawnPoint();
            }
            else {
                var9 = p_82448_4_.func_180504_m();
            }
            var5 = var9.getX();
            entityIn.posY = var9.getY();
            var6 = var9.getZ();
            entityIn.setLocationAndAngles(var5, entityIn.posY, var6, 90.0f, 0.0f);
            if (entityIn.isEntityAlive()) {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        p_82448_3_.theProfiler.endSection();
        if (p_82448_2_ != 1) {
            p_82448_3_.theProfiler.startSection("placing");
            var5 = MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var6 = MathHelper.clamp_int((int)var6, -29999872, 29999872);
            if (entityIn.isEntityAlive()) {
                entityIn.setLocationAndAngles(var5, entityIn.posY, var6, entityIn.rotationYaw, entityIn.rotationPitch);
                p_82448_4_.getDefaultTeleporter().func_180266_a(entityIn, var8);
                p_82448_4_.spawnEntityInWorld(entityIn);
                p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
            }
            p_82448_3_.theProfiler.endSection();
        }
        entityIn.setWorld(p_82448_4_);
    }
    
    public void onTick() {
        if (++this.playerPingIndex > 600) {
            this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
            this.playerPingIndex = 0;
        }
    }
    
    public void sendPacketToAllPlayers(final Packet packetIn) {
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            this.playerEntityList.get(var2).playerNetServerHandler.sendPacket(packetIn);
        }
    }
    
    public void sendPacketToAllPlayersInDimension(final Packet packetIn, final int dimension) {
        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
            final EntityPlayerMP var4 = this.playerEntityList.get(var3);
            if (var4.dimension == dimension) {
                var4.playerNetServerHandler.sendPacket(packetIn);
            }
        }
    }
    
    public void func_177453_a(final EntityPlayer p_177453_1_, final IChatComponent p_177453_2_) {
        final Team var3 = p_177453_1_.getTeam();
        if (var3 != null) {
            final Collection var4 = var3.getMembershipCollection();
            for (final String var6 : var4) {
                final EntityPlayerMP var7 = this.getPlayerByUsername(var6);
                if (var7 != null && var7 != p_177453_1_) {
                    var7.addChatMessage(p_177453_2_);
                }
            }
        }
    }
    
    public void func_177452_b(final EntityPlayer p_177452_1_, final IChatComponent p_177452_2_) {
        final Team var3 = p_177452_1_.getTeam();
        if (var3 == null) {
            this.sendChatMsg(p_177452_2_);
        }
        else {
            for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
                final EntityPlayerMP var5 = this.playerEntityList.get(var4);
                if (var5.getTeam() != var3) {
                    var5.addChatMessage(p_177452_2_);
                }
            }
        }
    }
    
    public String func_180602_f() {
        String var1 = "";
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            if (var2 > 0) {
                var1 += ", ";
            }
            var1 += this.playerEntityList.get(var2).getName();
        }
        return var1;
    }
    
    public String[] getAllUsernames() {
        final String[] var1 = new String[this.playerEntityList.size()];
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            var1[var2] = this.playerEntityList.get(var2).getName();
        }
        return var1;
    }
    
    public GameProfile[] getAllProfiles() {
        final GameProfile[] var1 = new GameProfile[this.playerEntityList.size()];
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
            var1[var2] = this.playerEntityList.get(var2).getGameProfile();
        }
        return var1;
    }
    
    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }
    
    public BanList getBannedIPs() {
        return this.bannedIPs;
    }
    
    public void addOp(final GameProfile profile) {
        this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel()));
    }
    
    public void removeOp(final GameProfile profile) {
        this.ops.removeEntry(profile);
    }
    
    public boolean canJoin(final GameProfile profile) {
        return !this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile);
    }
    
    public boolean canSendCommands(final GameProfile profile) {
        return this.ops.hasEntry(profile) || (this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName())) || this.commandsAllowedForAll;
    }
    
    public EntityPlayerMP getPlayerByUsername(final String username) {
        for (final EntityPlayerMP var3 : this.playerEntityList) {
            if (var3.getName().equalsIgnoreCase(username)) {
                return var3;
            }
        }
        return null;
    }
    
    public void sendToAllNear(final double x, final double y, final double z, final double radius, final int dimension, final Packet packetIn) {
        this.sendToAllNearExcept(null, x, y, z, radius, dimension, packetIn);
    }
    
    public void sendToAllNearExcept(final EntityPlayer p_148543_1_, final double x, final double y, final double z, final double radius, final int dimension, final Packet p_148543_11_) {
        for (int var12 = 0; var12 < this.playerEntityList.size(); ++var12) {
            final EntityPlayerMP var13 = this.playerEntityList.get(var12);
            if (var13 != p_148543_1_ && var13.dimension == dimension) {
                final double var14 = x - var13.posX;
                final double var15 = y - var13.posY;
                final double var16 = z - var13.posZ;
                if (var14 * var14 + var15 * var15 + var16 * var16 < radius * radius) {
                    var13.playerNetServerHandler.sendPacket(p_148543_11_);
                }
            }
        }
    }
    
    public void saveAllPlayerData() {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
            this.writePlayerData(this.playerEntityList.get(var1));
        }
    }
    
    public void addWhitelistedPlayer(final GameProfile profile) {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
    }
    
    public void removePlayerFromWhitelist(final GameProfile profile) {
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
    
    public void updateTimeAndWeatherForPlayer(final EntityPlayerMP playerIn, final WorldServer worldIn) {
        final WorldBorder var3 = this.mcServer.worldServers[0].getWorldBorder();
        playerIn.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(var3, S44PacketWorldBorder.Action.INITIALIZE));
        playerIn.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getGameRuleBooleanValue("doDaylightCycle")));
        if (worldIn.isRaining()) {
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0f));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0f)));
            playerIn.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getWeightedThunderStrength(1.0f)));
        }
    }
    
    public void syncPlayerInventory(final EntityPlayerMP playerIn) {
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
    
    public void setWhiteListEnabled(final boolean whitelistEnabled) {
        this.whiteListEnforced = whitelistEnabled;
    }
    
    public List getPlayersMatchingAddress(final String address) {
        final ArrayList var2 = Lists.newArrayList();
        for (final EntityPlayerMP var4 : this.playerEntityList) {
            if (var4.getPlayerIP().equals(address)) {
                var2.add(var4);
            }
        }
        return var2;
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
    
    public void func_152604_a(final WorldSettings.GameType p_152604_1_) {
        this.gameType = p_152604_1_;
    }
    
    private void func_72381_a(final EntityPlayerMP p_72381_1_, final EntityPlayerMP p_72381_2_, final World worldIn) {
        if (p_72381_2_ != null) {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        }
        else if (this.gameType != null) {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }
        p_72381_1_.theItemInWorldManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }
    
    public void setCommandsAllowedForAll(final boolean p_72387_1_) {
        this.commandsAllowedForAll = p_72387_1_;
    }
    
    public void removeAllPlayers() {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
            this.playerEntityList.get(var1).playerNetServerHandler.kickPlayerFromServer("Server closed");
        }
    }
    
    public void sendChatMsgImpl(final IChatComponent component, final boolean isChat) {
        this.mcServer.addChatMessage(component);
        final int var3 = isChat ? 1 : 0;
        this.sendPacketToAllPlayers(new S02PacketChat(component, (byte)var3));
    }
    
    public void sendChatMsg(final IChatComponent component) {
        this.sendChatMsgImpl(component, true);
    }
    
    public StatisticsFile getPlayerStatsFile(final EntityPlayer playerIn) {
        final UUID var2 = playerIn.getUniqueID();
        StatisticsFile var3 = (var2 == null) ? null : this.playerStatFiles.get(var2);
        if (var3 == null) {
            final File var4 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            final File var5 = new File(var4, var2.toString() + ".json");
            if (!var5.exists()) {
                final File var6 = new File(var4, playerIn.getName() + ".json");
                if (var6.exists() && var6.isFile()) {
                    var6.renameTo(var5);
                }
            }
            var3 = new StatisticsFile(this.mcServer, var5);
            var3.func_150882_a();
            this.playerStatFiles.put(var2, var3);
        }
        return var3;
    }
    
    public void setViewDistance(final int distance) {
        this.viewDistance = distance;
        if (this.mcServer.worldServers != null) {
            for (final WorldServer var5 : this.mcServer.worldServers) {
                if (var5 != null) {
                    var5.getPlayerManager().func_152622_a(distance);
                }
            }
        }
    }
    
    public EntityPlayerMP func_177451_a(final UUID p_177451_1_) {
        return this.field_177454_f.get(p_177451_1_);
    }
    
    static {
        FILE_PLAYERBANS = new File("banned-players.json");
        FILE_IPBANS = new File("banned-ips.json");
        FILE_OPS = new File("ops.json");
        FILE_WHITELIST = new File("whitelist.json");
        logger = LogManager.getLogger();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    }
}
