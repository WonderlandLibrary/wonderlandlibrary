/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.util.concurrent.FutureCallback
 *  com.google.common.util.concurrent.Futures
 *  com.mojang.authlib.GameProfile
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventPacket;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayClient
implements INetHandlerPlayClient {
    private final Random avRandomizer;
    public int currentServerMaxPlayers = 20;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
    private boolean field_147308_k = false;
    private final NetworkManager netManager;
    private static final Logger logger = LogManager.getLogger();
    private final GameProfile profile;
    private WorldClient clientWorldController;
    private final GuiScreen guiScreenServer;
    private Minecraft gameController;
    public boolean doneLoadingTerrain;

    @Override
    public void handleSpawnPlayer(S0CPacketSpawnPlayer s0CPacketSpawnPlayer) {
        PacketThreadUtil.checkThreadAndEnqueue(s0CPacketSpawnPlayer, this, this.gameController);
        double d = (double)s0CPacketSpawnPlayer.getX() / 32.0;
        double d2 = (double)s0CPacketSpawnPlayer.getY() / 32.0;
        double d3 = (double)s0CPacketSpawnPlayer.getZ() / 32.0;
        float f = (float)(s0CPacketSpawnPlayer.getYaw() * 360) / 256.0f;
        float f2 = (float)(s0CPacketSpawnPlayer.getPitch() * 360) / 256.0f;
        EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(Minecraft.theWorld, this.getPlayerInfo(s0CPacketSpawnPlayer.getPlayer()).getGameProfile());
        entityOtherPlayerMP.serverPosX = s0CPacketSpawnPlayer.getX();
        entityOtherPlayerMP.prevPosX = entityOtherPlayerMP.lastTickPosX = (double)entityOtherPlayerMP.serverPosX;
        entityOtherPlayerMP.serverPosY = s0CPacketSpawnPlayer.getY();
        entityOtherPlayerMP.prevPosY = entityOtherPlayerMP.lastTickPosY = (double)entityOtherPlayerMP.serverPosY;
        entityOtherPlayerMP.serverPosZ = s0CPacketSpawnPlayer.getZ();
        entityOtherPlayerMP.prevPosZ = entityOtherPlayerMP.lastTickPosZ = (double)entityOtherPlayerMP.serverPosZ;
        int n = s0CPacketSpawnPlayer.getCurrentItemID();
        entityOtherPlayerMP.inventory.mainInventory[entityOtherPlayerMP.inventory.currentItem] = n == 0 ? null : new ItemStack(Item.getItemById(n), 1, 0);
        entityOtherPlayerMP.setPositionAndRotation(d, d2, d3, f, f2);
        this.clientWorldController.addEntityToWorld(s0CPacketSpawnPlayer.getEntityID(), entityOtherPlayerMP);
        List<DataWatcher.WatchableObject> list = s0CPacketSpawnPlayer.func_148944_c();
        if (list != null) {
            entityOtherPlayerMP.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    @Override
    public void handleMultiBlockChange(S22PacketMultiBlockChange s22PacketMultiBlockChange) {
        PacketThreadUtil.checkThreadAndEnqueue(s22PacketMultiBlockChange, this, this.gameController);
        S22PacketMultiBlockChange.BlockUpdateData[] blockUpdateDataArray = s22PacketMultiBlockChange.getChangedBlocks();
        int n = blockUpdateDataArray.length;
        int n2 = 0;
        while (n2 < n) {
            S22PacketMultiBlockChange.BlockUpdateData blockUpdateData = blockUpdateDataArray[n2];
            this.clientWorldController.invalidateRegionAndSetBlock(blockUpdateData.getPos(), blockUpdateData.getBlockState());
            ++n2;
        }
    }

    @Override
    public void handleWorldBorder(S44PacketWorldBorder s44PacketWorldBorder) {
        PacketThreadUtil.checkThreadAndEnqueue(s44PacketWorldBorder, this, this.gameController);
        s44PacketWorldBorder.func_179788_a(this.clientWorldController.getWorldBorder());
    }

    @Override
    public void handleConfirmTransaction(S32PacketConfirmTransaction s32PacketConfirmTransaction) {
        PacketThreadUtil.checkThreadAndEnqueue(s32PacketConfirmTransaction, this, this.gameController);
        Container container = null;
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if (s32PacketConfirmTransaction.getWindowId() == 0) {
            container = entityPlayerSP.inventoryContainer;
        } else if (s32PacketConfirmTransaction.getWindowId() == entityPlayerSP.openContainer.windowId) {
            container = entityPlayerSP.openContainer;
        }
        if (container != null && !s32PacketConfirmTransaction.func_148888_e()) {
            this.addToSendQueue(new C0FPacketConfirmTransaction(s32PacketConfirmTransaction.getWindowId(), s32PacketConfirmTransaction.getActionNumber(), true));
        }
    }

    @Override
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim s25PacketBlockBreakAnim) {
        PacketThreadUtil.checkThreadAndEnqueue(s25PacketBlockBreakAnim, this, this.gameController);
        Minecraft.theWorld.sendBlockBreakProgress(s25PacketBlockBreakAnim.getBreakerId(), s25PacketBlockBreakAnim.getPosition(), s25PacketBlockBreakAnim.getProgress());
    }

    @Override
    public void handleRespawn(S07PacketRespawn s07PacketRespawn) {
        PacketThreadUtil.checkThreadAndEnqueue(s07PacketRespawn, this, this.gameController);
        if (s07PacketRespawn.getDimensionID() != Minecraft.thePlayer.dimension) {
            this.doneLoadingTerrain = false;
            Scoreboard scoreboard = this.clientWorldController.getScoreboard();
            this.clientWorldController = new WorldClient(this, new WorldSettings(0L, s07PacketRespawn.getGameType(), false, Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled(), s07PacketRespawn.getWorldType()), s07PacketRespawn.getDimensionID(), s07PacketRespawn.getDifficulty(), this.gameController.mcProfiler);
            this.clientWorldController.setWorldScoreboard(scoreboard);
            this.gameController.loadWorld(this.clientWorldController);
            Minecraft.thePlayer.dimension = s07PacketRespawn.getDimensionID();
            this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        }
        this.gameController.setDimensionAndSpawnPlayer(s07PacketRespawn.getDimensionID());
        Minecraft.playerController.setGameType(s07PacketRespawn.getGameType());
    }

    public NetHandlerPlayClient(Minecraft minecraft, GuiScreen guiScreen, NetworkManager networkManager, GameProfile gameProfile) {
        this.avRandomizer = new Random();
        this.gameController = minecraft;
        this.guiScreenServer = guiScreen;
        this.netManager = networkManager;
        this.profile = gameProfile;
    }

    public void cleanup() {
        this.clientWorldController = null;
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }

    @Override
    public void handleEntityMovement(S14PacketEntity s14PacketEntity) {
        PacketThreadUtil.checkThreadAndEnqueue(s14PacketEntity, this, this.gameController);
        Entity entity = s14PacketEntity.getEntity(this.clientWorldController);
        if (entity != null) {
            entity.serverPosX += s14PacketEntity.func_149062_c();
            entity.serverPosY += s14PacketEntity.func_149061_d();
            entity.serverPosZ += s14PacketEntity.func_149064_e();
            double d = (double)entity.serverPosX / 32.0;
            double d2 = (double)entity.serverPosY / 32.0;
            double d3 = (double)entity.serverPosZ / 32.0;
            float f = s14PacketEntity.func_149060_h() ? (float)(s14PacketEntity.func_149066_f() * 360) / 256.0f : entity.rotationYaw;
            float f2 = s14PacketEntity.func_149060_h() ? (float)(s14PacketEntity.func_149063_g() * 360) / 256.0f : entity.rotationPitch;
            entity.setPositionAndRotation2(d, d2, d3, f, f2, 3, false);
            entity.onGround = s14PacketEntity.getOnGround();
        }
    }

    public void addToSendQueueSilent(Packet<?> packet) {
        this.netManager.sendPacket(packet);
    }

    @Override
    public void handleUseBed(S0APacketUseBed s0APacketUseBed) {
        PacketThreadUtil.checkThreadAndEnqueue(s0APacketUseBed, this, this.gameController);
        s0APacketUseBed.getPlayer(this.clientWorldController).trySleep(s0APacketUseBed.getBedPosition());
    }

    @Override
    public void handleStatistics(S37PacketStatistics s37PacketStatistics) {
        PacketThreadUtil.checkThreadAndEnqueue(s37PacketStatistics, this, this.gameController);
        boolean bl = false;
        for (Map.Entry<StatBase, Integer> entry : s37PacketStatistics.func_148974_c().entrySet()) {
            StatBase statBase = entry.getKey();
            int n = entry.getValue();
            if (statBase.isAchievement() && n > 0) {
                if (this.field_147308_k && Minecraft.thePlayer.getStatFileWriter().readStat(statBase) == 0) {
                    Achievement achievement = (Achievement)statBase;
                    this.gameController.guiAchievement.displayAchievement(achievement);
                    this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);
                    if (statBase == AchievementList.openInventory) {
                        Minecraft.gameSettings.showInventoryAchievementHint = false;
                        Minecraft.gameSettings.saveOptions();
                    }
                }
                bl = true;
            }
            Minecraft.thePlayer.getStatFileWriter().unlockAchievement(Minecraft.thePlayer, statBase, n);
        }
        if (!this.field_147308_k && !bl && Minecraft.gameSettings.showInventoryAchievementHint) {
            this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }
        this.field_147308_k = true;
        if (this.gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)((Object)this.gameController.currentScreen)).doneLoading();
        }
    }

    @Override
    public void handleTimeUpdate(S03PacketTimeUpdate s03PacketTimeUpdate) {
        PacketThreadUtil.checkThreadAndEnqueue(s03PacketTimeUpdate, this, this.gameController);
        Minecraft.theWorld.setTotalWorldTime(s03PacketTimeUpdate.getTotalWorldTime());
        Minecraft.theWorld.setWorldTime(s03PacketTimeUpdate.getWorldTime());
    }

    @Override
    public void handleOpenWindow(S2DPacketOpenWindow s2DPacketOpenWindow) {
        PacketThreadUtil.checkThreadAndEnqueue(s2DPacketOpenWindow, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if ("minecraft:container".equals(s2DPacketOpenWindow.getGuiId())) {
            entityPlayerSP.displayGUIChest(new InventoryBasic(s2DPacketOpenWindow.getWindowTitle(), s2DPacketOpenWindow.getSlotCount()));
            entityPlayerSP.openContainer.windowId = s2DPacketOpenWindow.getWindowId();
        } else if ("minecraft:villager".equals(s2DPacketOpenWindow.getGuiId())) {
            entityPlayerSP.displayVillagerTradeGui(new NpcMerchant(entityPlayerSP, s2DPacketOpenWindow.getWindowTitle()));
            entityPlayerSP.openContainer.windowId = s2DPacketOpenWindow.getWindowId();
        } else if ("EntityHorse".equals(s2DPacketOpenWindow.getGuiId())) {
            Entity entity = this.clientWorldController.getEntityByID(s2DPacketOpenWindow.getEntityId());
            if (entity instanceof EntityHorse) {
                entityPlayerSP.displayGUIHorse((EntityHorse)entity, new AnimalChest(s2DPacketOpenWindow.getWindowTitle(), s2DPacketOpenWindow.getSlotCount()));
                entityPlayerSP.openContainer.windowId = s2DPacketOpenWindow.getWindowId();
            }
        } else if (!s2DPacketOpenWindow.hasSlots()) {
            entityPlayerSP.displayGui(new LocalBlockIntercommunication(s2DPacketOpenWindow.getGuiId(), s2DPacketOpenWindow.getWindowTitle()));
            entityPlayerSP.openContainer.windowId = s2DPacketOpenWindow.getWindowId();
        } else {
            ContainerLocalMenu containerLocalMenu = new ContainerLocalMenu(s2DPacketOpenWindow.getGuiId(), s2DPacketOpenWindow.getWindowTitle(), s2DPacketOpenWindow.getSlotCount());
            entityPlayerSP.displayGUIChest(containerLocalMenu);
            entityPlayerSP.openContainer.windowId = s2DPacketOpenWindow.getWindowId();
        }
    }

    @Override
    public void handleSetSlot(S2FPacketSetSlot s2FPacketSetSlot) {
        PacketThreadUtil.checkThreadAndEnqueue(s2FPacketSetSlot, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if (s2FPacketSetSlot.func_149175_c() == -1) {
            entityPlayerSP.inventory.setItemStack(s2FPacketSetSlot.func_149174_e());
        } else {
            Object object;
            boolean bl = false;
            if (this.gameController.currentScreen instanceof GuiContainerCreative) {
                object = (GuiContainerCreative)this.gameController.currentScreen;
                boolean bl2 = bl = ((GuiContainerCreative)object).getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
            }
            if (s2FPacketSetSlot.func_149175_c() == 0 && s2FPacketSetSlot.func_149173_d() >= 36 && s2FPacketSetSlot.func_149173_d() < 45) {
                object = entityPlayerSP.inventoryContainer.getSlot(s2FPacketSetSlot.func_149173_d()).getStack();
                if (s2FPacketSetSlot.func_149174_e() != null && (object == null || ((ItemStack)object).stackSize < s2FPacketSetSlot.func_149174_e().stackSize)) {
                    s2FPacketSetSlot.func_149174_e().animationsToGo = 5;
                }
                entityPlayerSP.inventoryContainer.putStackInSlot(s2FPacketSetSlot.func_149173_d(), s2FPacketSetSlot.func_149174_e());
            } else if (!(s2FPacketSetSlot.func_149175_c() != entityPlayerSP.openContainer.windowId || s2FPacketSetSlot.func_149175_c() == 0 && bl)) {
                entityPlayerSP.openContainer.putStackInSlot(s2FPacketSetSlot.func_149173_d(), s2FPacketSetSlot.func_149174_e());
            }
        }
    }

    @Override
    public void handleSpawnObject(S0EPacketSpawnObject s0EPacketSpawnObject) {
        Entity[] entityArray;
        PacketThreadUtil.checkThreadAndEnqueue(s0EPacketSpawnObject, this, this.gameController);
        double d = (double)s0EPacketSpawnObject.getX() / 32.0;
        double d2 = (double)s0EPacketSpawnObject.getY() / 32.0;
        double d3 = (double)s0EPacketSpawnObject.getZ() / 32.0;
        Entity entity = null;
        if (s0EPacketSpawnObject.getType() == 10) {
            entity = EntityMinecart.func_180458_a(this.clientWorldController, d, d2, d3, EntityMinecart.EnumMinecartType.byNetworkID(s0EPacketSpawnObject.func_149009_m()));
        } else if (s0EPacketSpawnObject.getType() == 90) {
            entityArray = this.clientWorldController.getEntityByID(s0EPacketSpawnObject.func_149009_m());
            if (entityArray instanceof EntityPlayer) {
                entity = new EntityFishHook(this.clientWorldController, d, d2, d3, (EntityPlayer)entityArray);
            }
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 60) {
            entity = new EntityArrow(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 61) {
            entity = new EntitySnowball(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 71) {
            entity = new EntityItemFrame(this.clientWorldController, new BlockPos(MathHelper.floor_double(d), MathHelper.floor_double(d2), MathHelper.floor_double(d3)), EnumFacing.getHorizontal(s0EPacketSpawnObject.func_149009_m()));
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 77) {
            entity = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(d), MathHelper.floor_double(d2), MathHelper.floor_double(d3)));
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 65) {
            entity = new EntityEnderPearl(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 72) {
            entity = new EntityEnderEye(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 76) {
            entity = new EntityFireworkRocket(this.clientWorldController, d, d2, d3, null);
        } else if (s0EPacketSpawnObject.getType() == 63) {
            entity = new EntityLargeFireball(this.clientWorldController, d, d2, d3, (double)s0EPacketSpawnObject.getSpeedX() / 8000.0, (double)s0EPacketSpawnObject.getSpeedY() / 8000.0, (double)s0EPacketSpawnObject.getSpeedZ() / 8000.0);
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 64) {
            entity = new EntitySmallFireball(this.clientWorldController, d, d2, d3, (double)s0EPacketSpawnObject.getSpeedX() / 8000.0, (double)s0EPacketSpawnObject.getSpeedY() / 8000.0, (double)s0EPacketSpawnObject.getSpeedZ() / 8000.0);
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 66) {
            entity = new EntityWitherSkull(this.clientWorldController, d, d2, d3, (double)s0EPacketSpawnObject.getSpeedX() / 8000.0, (double)s0EPacketSpawnObject.getSpeedY() / 8000.0, (double)s0EPacketSpawnObject.getSpeedZ() / 8000.0);
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 62) {
            entity = new EntityEgg(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 73) {
            entity = new EntityPotion((World)this.clientWorldController, d, d2, d3, s0EPacketSpawnObject.func_149009_m());
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 75) {
            entity = new EntityExpBottle(this.clientWorldController, d, d2, d3);
            s0EPacketSpawnObject.func_149002_g(0);
        } else if (s0EPacketSpawnObject.getType() == 1) {
            entity = new EntityBoat(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 50) {
            entity = new EntityTNTPrimed(this.clientWorldController, d, d2, d3, null);
        } else if (s0EPacketSpawnObject.getType() == 78) {
            entity = new EntityArmorStand(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 51) {
            entity = new EntityEnderCrystal(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 2) {
            entity = new EntityItem(this.clientWorldController, d, d2, d3);
        } else if (s0EPacketSpawnObject.getType() == 70) {
            entity = new EntityFallingBlock(this.clientWorldController, d, d2, d3, Block.getStateById(s0EPacketSpawnObject.func_149009_m() & 0xFFFF));
            s0EPacketSpawnObject.func_149002_g(0);
        }
        if (entity != null) {
            entity.serverPosX = s0EPacketSpawnObject.getX();
            entity.serverPosY = s0EPacketSpawnObject.getY();
            entity.serverPosZ = s0EPacketSpawnObject.getZ();
            entity.rotationPitch = (float)(s0EPacketSpawnObject.getPitch() * 360) / 256.0f;
            entity.rotationYaw = (float)(s0EPacketSpawnObject.getYaw() * 360) / 256.0f;
            entityArray = entity.getParts();
            if (entityArray != null) {
                int n = s0EPacketSpawnObject.getEntityID() - entity.getEntityId();
                int n2 = 0;
                while (n2 < entityArray.length) {
                    entityArray[n2].setEntityId(entityArray[n2].getEntityId() + n);
                    ++n2;
                }
            }
            entity.setEntityId(s0EPacketSpawnObject.getEntityID());
            this.clientWorldController.addEntityToWorld(s0EPacketSpawnObject.getEntityID(), entity);
            if (s0EPacketSpawnObject.func_149009_m() > 0) {
                Entity entity2;
                if (s0EPacketSpawnObject.getType() == 60 && (entity2 = this.clientWorldController.getEntityByID(s0EPacketSpawnObject.func_149009_m())) instanceof EntityLivingBase && entity instanceof EntityArrow) {
                    ((EntityArrow)entity).shootingEntity = entity2;
                }
                entity.setVelocity((double)s0EPacketSpawnObject.getSpeedX() / 8000.0, (double)s0EPacketSpawnObject.getSpeedY() / 8000.0, (double)s0EPacketSpawnObject.getSpeedZ() / 8000.0);
            }
        }
    }

    @Override
    public void handleCollectItem(S0DPacketCollectItem s0DPacketCollectItem) {
        PacketThreadUtil.checkThreadAndEnqueue(s0DPacketCollectItem, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s0DPacketCollectItem.getCollectedItemEntityID());
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.clientWorldController.getEntityByID(s0DPacketCollectItem.getEntityID());
        if (entityLivingBase == null) {
            entityLivingBase = Minecraft.thePlayer;
        }
        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            } else {
                this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            this.gameController.effectRenderer.addEffect(new EntityPickupFX((World)this.clientWorldController, entity, entityLivingBase, 0.5f));
            this.clientWorldController.removeEntityFromWorld(s0DPacketCollectItem.getCollectedItemEntityID());
        }
    }

    @Override
    public void handleChat(S02PacketChat s02PacketChat) {
        PacketThreadUtil.checkThreadAndEnqueue(s02PacketChat, this, this.gameController);
        if (s02PacketChat.getType() == 2) {
            this.gameController.ingameGUI.setRecordPlaying(s02PacketChat.getChatComponent(), false);
        } else {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(s02PacketChat.getChatComponent());
        }
    }

    @Override
    public void handleSpawnPainting(S10PacketSpawnPainting s10PacketSpawnPainting) {
        PacketThreadUtil.checkThreadAndEnqueue(s10PacketSpawnPainting, this, this.gameController);
        EntityPainting entityPainting = new EntityPainting(this.clientWorldController, s10PacketSpawnPainting.getPosition(), s10PacketSpawnPainting.getFacing(), s10PacketSpawnPainting.getTitle());
        this.clientWorldController.addEntityToWorld(s10PacketSpawnPainting.getEntityID(), entityPainting);
    }

    @Override
    public void handleExplosion(S27PacketExplosion s27PacketExplosion) {
        PacketThreadUtil.checkThreadAndEnqueue(s27PacketExplosion, this, this.gameController);
        Explosion explosion = new Explosion(Minecraft.theWorld, null, s27PacketExplosion.getX(), s27PacketExplosion.getY(), s27PacketExplosion.getZ(), s27PacketExplosion.getStrength(), s27PacketExplosion.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        Minecraft.thePlayer.motionX += (double)s27PacketExplosion.func_149149_c();
        Minecraft.thePlayer.motionY += (double)s27PacketExplosion.func_149144_d();
        Minecraft.thePlayer.motionZ += (double)s27PacketExplosion.func_149147_e();
    }

    @Override
    public void handleMapChunkBulk(S26PacketMapChunkBulk s26PacketMapChunkBulk) {
        PacketThreadUtil.checkThreadAndEnqueue(s26PacketMapChunkBulk, this, this.gameController);
        int n = 0;
        while (n < s26PacketMapChunkBulk.getChunkCount()) {
            int n2 = s26PacketMapChunkBulk.getChunkX(n);
            int n3 = s26PacketMapChunkBulk.getChunkZ(n);
            this.clientWorldController.doPreChunk(n2, n3, true);
            this.clientWorldController.invalidateBlockReceiveRegion(n2 << 4, 0, n3 << 4, (n2 << 4) + 15, 256, (n3 << 4) + 15);
            Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(n2, n3);
            chunk.fillChunk(s26PacketMapChunkBulk.getChunkBytes(n), s26PacketMapChunkBulk.getChunkSize(n), true);
            this.clientWorldController.markBlockRangeForRenderUpdate(n2 << 4, 0, n3 << 4, (n2 << 4) + 15, 256, (n3 << 4) + 15);
            if (!(this.clientWorldController.provider instanceof WorldProviderSurface)) {
                chunk.resetRelightChecks();
            }
            ++n;
        }
    }

    @Override
    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect s1EPacketRemoveEntityEffect) {
        PacketThreadUtil.checkThreadAndEnqueue(s1EPacketRemoveEntityEffect, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s1EPacketRemoveEntityEffect.getEntityId());
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removePotionEffectClient(s1EPacketRemoveEntityEffect.getEffectId());
        }
    }

    @Override
    public void handleEffect(S28PacketEffect s28PacketEffect) {
        PacketThreadUtil.checkThreadAndEnqueue(s28PacketEffect, this, this.gameController);
        if (s28PacketEffect.isSoundServerwide()) {
            Minecraft.theWorld.playBroadcastSound(s28PacketEffect.getSoundType(), s28PacketEffect.getSoundPos(), s28PacketEffect.getSoundData());
        } else {
            Minecraft.theWorld.playAuxSFX(s28PacketEffect.getSoundType(), s28PacketEffect.getSoundPos(), s28PacketEffect.getSoundData());
        }
    }

    @Override
    public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity s2CPacketSpawnGlobalEntity) {
        PacketThreadUtil.checkThreadAndEnqueue(s2CPacketSpawnGlobalEntity, this, this.gameController);
        double d = (double)s2CPacketSpawnGlobalEntity.func_149051_d() / 32.0;
        double d2 = (double)s2CPacketSpawnGlobalEntity.func_149050_e() / 32.0;
        double d3 = (double)s2CPacketSpawnGlobalEntity.func_149049_f() / 32.0;
        EntityLightningBolt entityLightningBolt = null;
        if (s2CPacketSpawnGlobalEntity.func_149053_g() == 1) {
            entityLightningBolt = new EntityLightningBolt(this.clientWorldController, d, d2, d3);
        }
        if (entityLightningBolt != null) {
            entityLightningBolt.serverPosX = s2CPacketSpawnGlobalEntity.func_149051_d();
            entityLightningBolt.serverPosY = s2CPacketSpawnGlobalEntity.func_149050_e();
            entityLightningBolt.serverPosZ = s2CPacketSpawnGlobalEntity.func_149049_f();
            entityLightningBolt.rotationYaw = 0.0f;
            entityLightningBolt.rotationPitch = 0.0f;
            entityLightningBolt.setEntityId(s2CPacketSpawnGlobalEntity.func_149052_c());
            this.clientWorldController.addWeatherEffect(entityLightningBolt);
        }
    }

    @Override
    public void handleUpdateHealth(S06PacketUpdateHealth s06PacketUpdateHealth) {
        PacketThreadUtil.checkThreadAndEnqueue(s06PacketUpdateHealth, this, this.gameController);
        Minecraft.thePlayer.setPlayerSPHealth(s06PacketUpdateHealth.getHealth());
        Minecraft.thePlayer.getFoodStats().setFoodLevel(s06PacketUpdateHealth.getFoodLevel());
        Minecraft.thePlayer.getFoodStats().setFoodSaturationLevel(s06PacketUpdateHealth.getSaturationLevel());
    }

    @Override
    public void handleChunkData(S21PacketChunkData s21PacketChunkData) {
        PacketThreadUtil.checkThreadAndEnqueue(s21PacketChunkData, this, this.gameController);
        if (s21PacketChunkData.func_149274_i()) {
            if (s21PacketChunkData.getExtractedSize() == 0) {
                this.clientWorldController.doPreChunk(s21PacketChunkData.getChunkX(), s21PacketChunkData.getChunkZ(), false);
                return;
            }
            this.clientWorldController.doPreChunk(s21PacketChunkData.getChunkX(), s21PacketChunkData.getChunkZ(), true);
        }
        this.clientWorldController.invalidateBlockReceiveRegion(s21PacketChunkData.getChunkX() << 4, 0, s21PacketChunkData.getChunkZ() << 4, (s21PacketChunkData.getChunkX() << 4) + 15, 256, (s21PacketChunkData.getChunkZ() << 4) + 15);
        Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(s21PacketChunkData.getChunkX(), s21PacketChunkData.getChunkZ());
        chunk.fillChunk(s21PacketChunkData.func_149272_d(), s21PacketChunkData.getExtractedSize(), s21PacketChunkData.func_149274_i());
        this.clientWorldController.markBlockRangeForRenderUpdate(s21PacketChunkData.getChunkX() << 4, 0, s21PacketChunkData.getChunkZ() << 4, (s21PacketChunkData.getChunkX() << 4) + 15, 256, (s21PacketChunkData.getChunkZ() << 4) + 15);
        if (!s21PacketChunkData.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
    }

    @Override
    public void handleEntityStatus(S19PacketEntityStatus s19PacketEntityStatus) {
        PacketThreadUtil.checkThreadAndEnqueue(s19PacketEntityStatus, this, this.gameController);
        Entity entity = s19PacketEntityStatus.getEntity(this.clientWorldController);
        if (entity != null) {
            if (s19PacketEntityStatus.getOpCode() == 21) {
                this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            } else {
                entity.handleStatusUpdate(s19PacketEntityStatus.getOpCode());
            }
        }
    }

    @Override
    public void handleUpdateSign(S33PacketUpdateSign s33PacketUpdateSign) {
        TileEntity tileEntity;
        PacketThreadUtil.checkThreadAndEnqueue(s33PacketUpdateSign, this, this.gameController);
        boolean bl = false;
        if (Minecraft.theWorld.isBlockLoaded(s33PacketUpdateSign.getPos()) && (tileEntity = Minecraft.theWorld.getTileEntity(s33PacketUpdateSign.getPos())) instanceof TileEntitySign) {
            TileEntitySign tileEntitySign = (TileEntitySign)tileEntity;
            if (tileEntitySign.getIsEditable()) {
                System.arraycopy(s33PacketUpdateSign.getLines(), 0, tileEntitySign.signText, 0, 4);
                tileEntitySign.markDirty();
            }
            bl = true;
        }
        if (!bl && Minecraft.thePlayer != null) {
            Minecraft.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at " + s33PacketUpdateSign.getPos().getX() + ", " + s33PacketUpdateSign.getPos().getY() + ", " + s33PacketUpdateSign.getPos().getZ()));
        }
    }

    @Override
    public void handleSpawnMob(S0FPacketSpawnMob s0FPacketSpawnMob) {
        PacketThreadUtil.checkThreadAndEnqueue(s0FPacketSpawnMob, this, this.gameController);
        double d = (double)s0FPacketSpawnMob.getX() / 32.0;
        double d2 = (double)s0FPacketSpawnMob.getY() / 32.0;
        double d3 = (double)s0FPacketSpawnMob.getZ() / 32.0;
        float f = (float)(s0FPacketSpawnMob.getYaw() * 360) / 256.0f;
        float f2 = (float)(s0FPacketSpawnMob.getPitch() * 360) / 256.0f;
        EntityLivingBase entityLivingBase = (EntityLivingBase)EntityList.createEntityByID(s0FPacketSpawnMob.getEntityType(), Minecraft.theWorld);
        entityLivingBase.serverPosX = s0FPacketSpawnMob.getX();
        entityLivingBase.serverPosY = s0FPacketSpawnMob.getY();
        entityLivingBase.serverPosZ = s0FPacketSpawnMob.getZ();
        entityLivingBase.renderYawOffset = entityLivingBase.rotationYawHead = (float)(s0FPacketSpawnMob.getHeadPitch() * 360) / 256.0f;
        Entity[] entityArray = entityLivingBase.getParts();
        if (entityArray != null) {
            int n = s0FPacketSpawnMob.getEntityID() - entityLivingBase.getEntityId();
            int n2 = 0;
            while (n2 < entityArray.length) {
                entityArray[n2].setEntityId(entityArray[n2].getEntityId() + n);
                ++n2;
            }
        }
        entityLivingBase.setEntityId(s0FPacketSpawnMob.getEntityID());
        entityLivingBase.setPositionAndRotation(d, d2, d3, f, f2);
        entityLivingBase.motionX = (float)s0FPacketSpawnMob.getVelocityX() / 8000.0f;
        entityLivingBase.motionY = (float)s0FPacketSpawnMob.getVelocityY() / 8000.0f;
        entityLivingBase.motionZ = (float)s0FPacketSpawnMob.getVelocityZ() / 8000.0f;
        this.clientWorldController.addEntityToWorld(s0FPacketSpawnMob.getEntityID(), entityLivingBase);
        List<DataWatcher.WatchableObject> list = s0FPacketSpawnMob.func_149027_c();
        if (list != null) {
            entityLivingBase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    @Override
    public void handleEntityEffect(S1DPacketEntityEffect s1DPacketEntityEffect) {
        PacketThreadUtil.checkThreadAndEnqueue(s1DPacketEntityEffect, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s1DPacketEntityEffect.getEntityId());
        if (entity instanceof EntityLivingBase) {
            PotionEffect potionEffect = new PotionEffect(s1DPacketEntityEffect.getEffectId(), s1DPacketEntityEffect.getDuration(), s1DPacketEntityEffect.getAmplifier(), false, s1DPacketEntityEffect.func_179707_f());
            potionEffect.setPotionDurationMax(s1DPacketEntityEffect.func_149429_c());
            ((EntityLivingBase)entity).addPotionEffect(potionEffect);
        }
    }

    public void addToSendQueueNoEvent(Packet packet) {
        this.netManager.sendPacket(packet);
    }

    @Override
    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter s47PacketPlayerListHeaderFooter) {
        this.gameController.ingameGUI.getTabList().setHeader(s47PacketPlayerListHeaderFooter.getHeader().getFormattedText().length() == 0 ? null : s47PacketPlayerListHeaderFooter.getHeader());
        this.gameController.ingameGUI.getTabList().setFooter(s47PacketPlayerListHeaderFooter.getFooter().getFormattedText().length() == 0 ? null : s47PacketPlayerListHeaderFooter.getFooter());
    }

    @Override
    public void handleTitle(S45PacketTitle s45PacketTitle) {
        PacketThreadUtil.checkThreadAndEnqueue(s45PacketTitle, this, this.gameController);
        S45PacketTitle.Type type = s45PacketTitle.getType();
        String string = null;
        String string2 = null;
        String string3 = s45PacketTitle.getMessage() != null ? s45PacketTitle.getMessage().getFormattedText() : "";
        switch (type) {
            case TITLE: {
                string = string3;
                break;
            }
            case SUBTITLE: {
                string2 = string3;
                break;
            }
            case RESET: {
                this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
                this.gameController.ingameGUI.func_175177_a();
                return;
            }
        }
        this.gameController.ingameGUI.displayTitle(string, string2, s45PacketTitle.getFadeInTime(), s45PacketTitle.getDisplayTime(), s45PacketTitle.getFadeOutTime());
    }

    @Override
    public void handleTeams(S3EPacketTeams s3EPacketTeams) {
        PacketThreadUtil.checkThreadAndEnqueue(s3EPacketTeams, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScorePlayerTeam scorePlayerTeam = s3EPacketTeams.func_149307_h() == 0 ? scoreboard.createTeam(s3EPacketTeams.func_149312_c()) : scoreboard.getTeam(s3EPacketTeams.func_149312_c());
        if (s3EPacketTeams.func_149307_h() == 0 || s3EPacketTeams.func_149307_h() == 2) {
            scorePlayerTeam.setTeamName(s3EPacketTeams.func_149306_d());
            scorePlayerTeam.setNamePrefix(s3EPacketTeams.func_149311_e());
            scorePlayerTeam.setNameSuffix(s3EPacketTeams.func_149309_f());
            scorePlayerTeam.setChatFormat(EnumChatFormatting.func_175744_a(s3EPacketTeams.func_179813_h()));
            scorePlayerTeam.func_98298_a(s3EPacketTeams.func_149308_i());
            Object object = Team.EnumVisible.func_178824_a(s3EPacketTeams.func_179814_i());
            if (object != null) {
                scorePlayerTeam.setNameTagVisibility((Team.EnumVisible)((Object)object));
            }
        }
        if (s3EPacketTeams.func_149307_h() == 0 || s3EPacketTeams.func_149307_h() == 3) {
            for (Object object : s3EPacketTeams.func_149310_g()) {
                scoreboard.addPlayerToTeam((String)object, s3EPacketTeams.func_149312_c());
            }
        }
        if (s3EPacketTeams.func_149307_h() == 4) {
            for (Object object : s3EPacketTeams.func_149310_g()) {
                scoreboard.removePlayerFromTeam((String)object, scorePlayerTeam);
            }
        }
        if (s3EPacketTeams.func_149307_h() == 1) {
            scoreboard.removeTeam(scorePlayerTeam);
        }
    }

    @Override
    public void handleKeepAlive(S00PacketKeepAlive s00PacketKeepAlive) {
        this.addToSendQueue(new C00PacketKeepAlive(s00PacketKeepAlive.func_149134_c()));
    }

    @Override
    public void handleBlockChange(S23PacketBlockChange s23PacketBlockChange) {
        PacketThreadUtil.checkThreadAndEnqueue(s23PacketBlockChange, this, this.gameController);
        this.clientWorldController.invalidateRegionAndSetBlock(s23PacketBlockChange.getBlockPosition(), s23PacketBlockChange.getBlockState());
    }

    @Override
    public void handleCamera(S43PacketCamera s43PacketCamera) {
        PacketThreadUtil.checkThreadAndEnqueue(s43PacketCamera, this, this.gameController);
        Entity entity = s43PacketCamera.getEntity(this.clientWorldController);
        if (entity != null) {
            this.gameController.setRenderViewEntity(entity);
        }
    }

    @Override
    public void handleJoinGame(S01PacketJoinGame s01PacketJoinGame) {
        PacketThreadUtil.checkThreadAndEnqueue(s01PacketJoinGame, this, this.gameController);
        Minecraft.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, s01PacketJoinGame.getGameType(), false, s01PacketJoinGame.isHardcoreMode(), s01PacketJoinGame.getWorldType()), s01PacketJoinGame.getDimension(), s01PacketJoinGame.getDifficulty(), this.gameController.mcProfiler);
        Minecraft.gameSettings.difficulty = s01PacketJoinGame.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        Minecraft.thePlayer.dimension = s01PacketJoinGame.getDimension();
        this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        Minecraft.thePlayer.setEntityId(s01PacketJoinGame.getEntityId());
        this.currentServerMaxPlayers = s01PacketJoinGame.getMaxPlayers();
        Minecraft.thePlayer.setReducedDebug(s01PacketJoinGame.isReducedDebugInfo());
        Minecraft.playerController.setGameType(s01PacketJoinGame.getGameType());
        Minecraft.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }

    @Override
    public void handleWindowItems(S30PacketWindowItems s30PacketWindowItems) {
        PacketThreadUtil.checkThreadAndEnqueue(s30PacketWindowItems, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if (s30PacketWindowItems.func_148911_c() == 0) {
            entityPlayerSP.inventoryContainer.putStacksInSlots(s30PacketWindowItems.getItemStacks());
        } else if (s30PacketWindowItems.func_148911_c() == entityPlayerSP.openContainer.windowId) {
            entityPlayerSP.openContainer.putStacksInSlots(s30PacketWindowItems.getItemStacks());
        }
    }

    @Override
    public void handleTabComplete(S3APacketTabComplete s3APacketTabComplete) {
        PacketThreadUtil.checkThreadAndEnqueue(s3APacketTabComplete, this, this.gameController);
        String[] stringArray = s3APacketTabComplete.func_149630_c();
        if (this.gameController.currentScreen instanceof GuiChat) {
            GuiChat guiChat = (GuiChat)this.gameController.currentScreen;
            guiChat.onAutocompleteResponse(stringArray);
        }
    }

    @Override
    public void handleParticles(S2APacketParticles s2APacketParticles) {
        PacketThreadUtil.checkThreadAndEnqueue(s2APacketParticles, this, this.gameController);
        if (s2APacketParticles.getParticleCount() == 0) {
            double d = s2APacketParticles.getParticleSpeed() * s2APacketParticles.getXOffset();
            double d2 = s2APacketParticles.getParticleSpeed() * s2APacketParticles.getYOffset();
            double d3 = s2APacketParticles.getParticleSpeed() * s2APacketParticles.getZOffset();
            try {
                this.clientWorldController.spawnParticle(s2APacketParticles.getParticleType(), s2APacketParticles.isLongDistance(), s2APacketParticles.getXCoordinate(), s2APacketParticles.getYCoordinate(), s2APacketParticles.getZCoordinate(), d, d2, d3, s2APacketParticles.getParticleArgs());
            }
            catch (Throwable throwable) {
                logger.warn("Could not spawn particle effect " + (Object)((Object)s2APacketParticles.getParticleType()));
            }
        } else {
            int n = 0;
            while (n < s2APacketParticles.getParticleCount()) {
                double d = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getXOffset();
                double d4 = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getYOffset();
                double d5 = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getZOffset();
                double d6 = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getParticleSpeed();
                double d7 = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getParticleSpeed();
                double d8 = this.avRandomizer.nextGaussian() * (double)s2APacketParticles.getParticleSpeed();
                try {
                    this.clientWorldController.spawnParticle(s2APacketParticles.getParticleType(), s2APacketParticles.isLongDistance(), s2APacketParticles.getXCoordinate() + d, s2APacketParticles.getYCoordinate() + d4, s2APacketParticles.getZCoordinate() + d5, d6, d7, d8, s2APacketParticles.getParticleArgs());
                }
                catch (Throwable throwable) {
                    logger.warn("Could not spawn particle effect " + (Object)((Object)s2APacketParticles.getParticleType()));
                    return;
                }
                ++n;
            }
        }
    }

    @Override
    public void handleSpawnPosition(S05PacketSpawnPosition s05PacketSpawnPosition) {
        PacketThreadUtil.checkThreadAndEnqueue(s05PacketSpawnPosition, this, this.gameController);
        Minecraft.thePlayer.setSpawnPoint(s05PacketSpawnPosition.getSpawnPos(), true);
        Minecraft.theWorld.getWorldInfo().setSpawn(s05PacketSpawnPosition.getSpawnPos());
    }

    @Override
    public void handleSoundEffect(S29PacketSoundEffect s29PacketSoundEffect) {
        PacketThreadUtil.checkThreadAndEnqueue(s29PacketSoundEffect, this, this.gameController);
        Minecraft.theWorld.playSound(s29PacketSoundEffect.getX(), s29PacketSoundEffect.getY(), s29PacketSoundEffect.getZ(), s29PacketSoundEffect.getSoundName(), s29PacketSoundEffect.getVolume(), s29PacketSoundEffect.getPitch(), false);
    }

    @Override
    public void handleEntityNBT(S49PacketUpdateEntityNBT s49PacketUpdateEntityNBT) {
        PacketThreadUtil.checkThreadAndEnqueue(s49PacketUpdateEntityNBT, this, this.gameController);
        Entity entity = s49PacketUpdateEntityNBT.getEntity(this.clientWorldController);
        if (entity != null) {
            entity.clientUpdateEntityNBT(s49PacketUpdateEntityNBT.getTagCompound());
        }
    }

    public GameProfile getGameProfile() {
        return this.profile;
    }

    @Override
    public void handleCustomPayload(S3FPacketCustomPayload s3FPacketCustomPayload) {
        block8: {
            ItemStack itemStack;
            PacketThreadUtil.checkThreadAndEnqueue(s3FPacketCustomPayload, this, this.gameController);
            if ("MC|TrList".equals(s3FPacketCustomPayload.getChannelName())) {
                PacketBuffer packetBuffer;
                block7: {
                    packetBuffer = s3FPacketCustomPayload.getBufferData();
                    try {
                        int n = packetBuffer.readInt();
                        GuiScreen guiScreen = this.gameController.currentScreen;
                        if (guiScreen == null || !(guiScreen instanceof GuiMerchant) || n != Minecraft.thePlayer.openContainer.windowId) break block7;
                        IMerchant iMerchant = ((GuiMerchant)guiScreen).getMerchant();
                        MerchantRecipeList merchantRecipeList = MerchantRecipeList.readFromBuf(packetBuffer);
                        iMerchant.setRecipes(merchantRecipeList);
                    }
                    catch (IOException iOException) {
                        logger.error("Couldn't load trade info", (Throwable)iOException);
                        packetBuffer.release();
                        break block8;
                    }
                }
                packetBuffer.release();
            } else if ("MC|Brand".equals(s3FPacketCustomPayload.getChannelName())) {
                Minecraft.thePlayer.setClientBrand(s3FPacketCustomPayload.getBufferData().readStringFromBuffer(Short.MAX_VALUE));
            } else if ("MC|BOpen".equals(s3FPacketCustomPayload.getChannelName()) && (itemStack = Minecraft.thePlayer.getCurrentEquippedItem()) != null && itemStack.getItem() == Items.written_book) {
                this.gameController.displayGuiScreen(new GuiScreenBook(Minecraft.thePlayer, itemStack, false));
            }
        }
    }

    @Override
    public void handleEntityHeadLook(S19PacketEntityHeadLook s19PacketEntityHeadLook) {
        PacketThreadUtil.checkThreadAndEnqueue(s19PacketEntityHeadLook, this, this.gameController);
        Entity entity = s19PacketEntityHeadLook.getEntity(this.clientWorldController);
        if (entity != null) {
            float f = (float)(s19PacketEntityHeadLook.getYaw() * 360) / 256.0f;
            entity.setRotationYawHead(f);
        }
    }

    public NetworkPlayerInfo getPlayerInfo(String string) {
        for (NetworkPlayerInfo networkPlayerInfo : this.playerInfoMap.values()) {
            if (!networkPlayerInfo.getGameProfile().getName().equals(string)) continue;
            return networkPlayerInfo;
        }
        return null;
    }

    public NetworkPlayerInfo getPlayerInfo(UUID uUID) {
        return this.playerInfoMap.get(uUID);
    }

    @Override
    public void handleSetExperience(S1FPacketSetExperience s1FPacketSetExperience) {
        PacketThreadUtil.checkThreadAndEnqueue(s1FPacketSetExperience, this, this.gameController);
        Minecraft.thePlayer.setXPStats(s1FPacketSetExperience.func_149397_c(), s1FPacketSetExperience.getTotalExperience(), s1FPacketSetExperience.getLevel());
    }

    @Override
    public void handlePlayerAbilities(S39PacketPlayerAbilities s39PacketPlayerAbilities) {
        PacketThreadUtil.checkThreadAndEnqueue(s39PacketPlayerAbilities, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        entityPlayerSP.capabilities.isFlying = s39PacketPlayerAbilities.isFlying();
        entityPlayerSP.capabilities.isCreativeMode = s39PacketPlayerAbilities.isCreativeMode();
        entityPlayerSP.capabilities.disableDamage = s39PacketPlayerAbilities.isInvulnerable();
        entityPlayerSP.capabilities.allowFlying = s39PacketPlayerAbilities.isAllowFlying();
        entityPlayerSP.capabilities.setFlySpeed(s39PacketPlayerAbilities.getFlySpeed());
        entityPlayerSP.capabilities.setPlayerWalkSpeed(s39PacketPlayerAbilities.getWalkSpeed());
    }

    @Override
    public void handleEntityVelocity(S12PacketEntityVelocity s12PacketEntityVelocity) {
        PacketThreadUtil.checkThreadAndEnqueue(s12PacketEntityVelocity, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s12PacketEntityVelocity.getEntityID());
        if (entity != null) {
            entity.setVelocity((double)s12PacketEntityVelocity.getMotionX() / 8000.0, (double)s12PacketEntityVelocity.getMotionY() / 8000.0, (double)s12PacketEntityVelocity.getMotionZ() / 8000.0);
        }
    }

    @Override
    public void handleEntityAttach(S1BPacketEntityAttach s1BPacketEntityAttach) {
        PacketThreadUtil.checkThreadAndEnqueue(s1BPacketEntityAttach, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s1BPacketEntityAttach.getEntityId());
        Entity entity2 = this.clientWorldController.getEntityByID(s1BPacketEntityAttach.getVehicleEntityId());
        if (s1BPacketEntityAttach.getLeash() == 0) {
            boolean bl = false;
            if (s1BPacketEntityAttach.getEntityId() == Minecraft.thePlayer.getEntityId()) {
                entity = Minecraft.thePlayer;
                if (entity2 instanceof EntityBoat) {
                    ((EntityBoat)entity2).setIsBoatEmpty(false);
                }
                bl = entity.ridingEntity == null && entity2 != null;
            } else if (entity2 instanceof EntityBoat) {
                ((EntityBoat)entity2).setIsBoatEmpty(true);
            }
            if (entity == null) {
                return;
            }
            entity.mountEntity(entity2);
            if (bl) {
                GameSettings gameSettings = Minecraft.gameSettings;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(gameSettings.keyBindSneak.getKeyCode())), false);
            }
        } else if (s1BPacketEntityAttach.getLeash() == 1 && entity instanceof EntityLiving) {
            if (entity2 != null) {
                ((EntityLiving)entity).setLeashedToEntity(entity2, false);
            } else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }

    @Override
    public void handleSignEditorOpen(S36PacketSignEditorOpen s36PacketSignEditorOpen) {
        PacketThreadUtil.checkThreadAndEnqueue(s36PacketSignEditorOpen, this, this.gameController);
        TileEntity tileEntity = this.clientWorldController.getTileEntity(s36PacketSignEditorOpen.getSignPosition());
        if (!(tileEntity instanceof TileEntitySign)) {
            tileEntity = new TileEntitySign();
            tileEntity.setWorldObj(this.clientWorldController);
            tileEntity.setPos(s36PacketSignEditorOpen.getSignPosition());
        }
        Minecraft.thePlayer.openEditSign((TileEntitySign)tileEntity);
    }

    @Override
    public void handleSetCompressionLevel(S46PacketSetCompressionLevel s46PacketSetCompressionLevel) {
        if (!this.netManager.isLocalChannel()) {
            this.netManager.setCompressionTreshold(s46PacketSetCompressionLevel.func_179760_a());
        }
    }

    @Override
    public void handleUpdateScore(S3CPacketUpdateScore s3CPacketUpdateScore) {
        PacketThreadUtil.checkThreadAndEnqueue(s3CPacketUpdateScore, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScoreObjective scoreObjective = scoreboard.getObjective(s3CPacketUpdateScore.getObjectiveName());
        if (s3CPacketUpdateScore.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
            Score score = scoreboard.getValueFromObjective(s3CPacketUpdateScore.getPlayerName(), scoreObjective);
            score.setScorePoints(s3CPacketUpdateScore.getScoreValue());
        } else if (s3CPacketUpdateScore.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(s3CPacketUpdateScore.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(s3CPacketUpdateScore.getPlayerName(), null);
            } else if (scoreObjective != null) {
                scoreboard.removeObjectiveFromEntity(s3CPacketUpdateScore.getPlayerName(), scoreObjective);
            }
        }
    }

    @Override
    public void handlePlayerListItem(S38PacketPlayerListItem s38PacketPlayerListItem) {
        PacketThreadUtil.checkThreadAndEnqueue(s38PacketPlayerListItem, this, this.gameController);
        for (S38PacketPlayerListItem.AddPlayerData addPlayerData : s38PacketPlayerListItem.func_179767_a()) {
            if (s38PacketPlayerListItem.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(addPlayerData.getProfile().getId());
                continue;
            }
            NetworkPlayerInfo networkPlayerInfo = this.playerInfoMap.get(addPlayerData.getProfile().getId());
            if (s38PacketPlayerListItem.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                networkPlayerInfo = new NetworkPlayerInfo(addPlayerData);
                this.playerInfoMap.put(networkPlayerInfo.getGameProfile().getId(), networkPlayerInfo);
            }
            if (networkPlayerInfo == null) continue;
            switch (s38PacketPlayerListItem.func_179768_b()) {
                case ADD_PLAYER: {
                    networkPlayerInfo.setGameType(addPlayerData.getGameMode());
                    networkPlayerInfo.setResponseTime(addPlayerData.getPing());
                    break;
                }
                case UPDATE_GAME_MODE: {
                    networkPlayerInfo.setGameType(addPlayerData.getGameMode());
                    break;
                }
                case UPDATE_LATENCY: {
                    networkPlayerInfo.setResponseTime(addPlayerData.getPing());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    networkPlayerInfo.setDisplayName(addPlayerData.getDisplayName());
                }
            }
        }
    }

    @Override
    public void handleEntityMetadata(S1CPacketEntityMetadata s1CPacketEntityMetadata) {
        PacketThreadUtil.checkThreadAndEnqueue(s1CPacketEntityMetadata, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s1CPacketEntityMetadata.getEntityId());
        if (entity != null && s1CPacketEntityMetadata.func_149376_c() != null) {
            entity.getDataWatcher().updateWatchedObjectsFromList(s1CPacketEntityMetadata.func_149376_c());
        }
    }

    @Override
    public void handleResourcePack(S48PacketResourcePackSend s48PacketResourcePackSend) {
        final String string = s48PacketResourcePackSend.getURL();
        final String string2 = s48PacketResourcePackSend.getHash();
        if (string.startsWith("level://")) {
            File file = new File(this.gameController.mcDataDir, "saves");
            String string3 = string.substring(8);
            File file2 = new File(file, string3);
            if (file2.isFile()) {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), (FutureCallback)new FutureCallback<Object>(){

                    public void onSuccess(Object object) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                    }

                    public void onFailure(Throwable throwable) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                    }
                });
            } else {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(string, string2), (FutureCallback)new FutureCallback<Object>(){

                public void onFailure(Throwable throwable) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                }

                public void onSuccess(Object object) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                }
            });
        } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.DECLINED));
        } else {
            this.gameController.addScheduledTask(new Runnable(){

                @Override
                public void run() {
                    NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(){

                        @Override
                        public void confirmClicked(boolean bl, int n) {
                            NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
                            if (bl) {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.ACCEPTED));
                                Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(string, string2), (FutureCallback)new FutureCallback<Object>(){

                                    public void onFailure(Throwable throwable) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                    }

                                    public void onSuccess(Object object) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                    }
                                });
                            } else {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(string2, C19PacketResourcePackStatus.Action.DECLINED));
                            }
                            ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
                            NetHandlerPlayClient.this.gameController.displayGuiScreen(null);
                        }
                    }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                }
            });
        }
    }

    @Override
    public void handleDestroyEntities(S13PacketDestroyEntities s13PacketDestroyEntities) {
        PacketThreadUtil.checkThreadAndEnqueue(s13PacketDestroyEntities, this, this.gameController);
        int n = 0;
        while (n < s13PacketDestroyEntities.getEntityIDs().length) {
            this.clientWorldController.removeEntityFromWorld(s13PacketDestroyEntities.getEntityIDs()[n]);
            ++n;
        }
    }

    @Override
    public void handleServerDifficulty(S41PacketServerDifficulty s41PacketServerDifficulty) {
        PacketThreadUtil.checkThreadAndEnqueue(s41PacketServerDifficulty, this, this.gameController);
        Minecraft.theWorld.getWorldInfo().setDifficulty(s41PacketServerDifficulty.getDifficulty());
        Minecraft.theWorld.getWorldInfo().setDifficultyLocked(s41PacketServerDifficulty.isDifficultyLocked());
    }

    @Override
    public void handleCombatEvent(S42PacketCombatEvent s42PacketCombatEvent) {
        Entity entity;
        EntityLivingBase entityLivingBase;
        PacketThreadUtil.checkThreadAndEnqueue(s42PacketCombatEvent, this, this.gameController);
        Entity entity2 = this.clientWorldController.getEntityByID(s42PacketCombatEvent.field_179775_c);
        EntityLivingBase entityLivingBase2 = entityLivingBase = entity2 instanceof EntityLivingBase ? (EntityLivingBase)entity2 : null;
        if (s42PacketCombatEvent.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
            long l = 1000 * s42PacketCombatEvent.field_179772_d / 20;
            MetadataCombat metadataCombat = new MetadataCombat(Minecraft.thePlayer, entityLivingBase);
            this.gameController.getTwitchStream().func_176026_a(metadataCombat, 0L - l, 0L);
        } else if (s42PacketCombatEvent.eventType == S42PacketCombatEvent.Event.ENTITY_DIED && (entity = this.clientWorldController.getEntityByID(s42PacketCombatEvent.field_179774_b)) instanceof EntityPlayer) {
            MetadataPlayerDeath metadataPlayerDeath = new MetadataPlayerDeath((EntityPlayer)entity, entityLivingBase);
            metadataPlayerDeath.func_152807_a(s42PacketCombatEvent.deathMessage);
            this.gameController.getTwitchStream().func_152911_a(metadataPlayerDeath, 0L);
        }
    }

    @Override
    public void handleCloseWindow(S2EPacketCloseWindow s2EPacketCloseWindow) {
        PacketThreadUtil.checkThreadAndEnqueue(s2EPacketCloseWindow, this, this.gameController);
        Minecraft.thePlayer.closeScreenAndDropStack();
    }

    @Override
    public void handleHeldItemChange(S09PacketHeldItemChange s09PacketHeldItemChange) {
        PacketThreadUtil.checkThreadAndEnqueue(s09PacketHeldItemChange, this, this.gameController);
        if (s09PacketHeldItemChange.getHeldItemHotbarIndex() >= 0 && s09PacketHeldItemChange.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
            Minecraft.thePlayer.inventory.currentItem = s09PacketHeldItemChange.getHeldItemHotbarIndex();
        }
    }

    @Override
    public void handleEntityEquipment(S04PacketEntityEquipment s04PacketEntityEquipment) {
        PacketThreadUtil.checkThreadAndEnqueue(s04PacketEntityEquipment, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s04PacketEntityEquipment.getEntityID());
        if (entity != null) {
            entity.setCurrentItemOrArmor(s04PacketEntityEquipment.getEquipmentSlot(), s04PacketEntityEquipment.getItemStack());
        }
    }

    @Override
    public void handleDisconnect(S40PacketDisconnect s40PacketDisconnect) {
        this.netManager.closeChannel(s40PacketDisconnect.getReason());
    }

    @Override
    public void handleEntityTeleport(S18PacketEntityTeleport s18PacketEntityTeleport) {
        PacketThreadUtil.checkThreadAndEnqueue(s18PacketEntityTeleport, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s18PacketEntityTeleport.getEntityId());
        if (entity != null) {
            entity.serverPosX = s18PacketEntityTeleport.getX();
            entity.serverPosY = s18PacketEntityTeleport.getY();
            entity.serverPosZ = s18PacketEntityTeleport.getZ();
            double d = (double)entity.serverPosX / 32.0;
            double d2 = (double)entity.serverPosY / 32.0;
            double d3 = (double)entity.serverPosZ / 32.0;
            float f = (float)(s18PacketEntityTeleport.getYaw() * 360) / 256.0f;
            float f2 = (float)(s18PacketEntityTeleport.getPitch() * 360) / 256.0f;
            if (Math.abs(entity.posX - d) < 0.03125 && Math.abs(entity.posY - d2) < 0.015625 && Math.abs(entity.posZ - d3) < 0.03125) {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f2, 3, true);
            } else {
                entity.setPositionAndRotation2(d, d2, d3, f, f2, 3, true);
            }
            entity.onGround = s18PacketEntityTeleport.getOnGround();
        }
    }

    @Override
    public void handleScoreboardObjective(S3BPacketScoreboardObjective s3BPacketScoreboardObjective) {
        PacketThreadUtil.checkThreadAndEnqueue(s3BPacketScoreboardObjective, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (s3BPacketScoreboardObjective.func_149338_e() == 0) {
            ScoreObjective scoreObjective = scoreboard.addScoreObjective(s3BPacketScoreboardObjective.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
            scoreObjective.setDisplayName(s3BPacketScoreboardObjective.func_149337_d());
            scoreObjective.setRenderType(s3BPacketScoreboardObjective.func_179817_d());
        } else {
            ScoreObjective scoreObjective = scoreboard.getObjective(s3BPacketScoreboardObjective.func_149339_c());
            if (s3BPacketScoreboardObjective.func_149338_e() == 1) {
                scoreboard.removeObjective(scoreObjective);
            } else if (s3BPacketScoreboardObjective.func_149338_e() == 2) {
                scoreObjective.setDisplayName(s3BPacketScoreboardObjective.func_149337_d());
                scoreObjective.setRenderType(s3BPacketScoreboardObjective.func_179817_d());
            }
        }
    }

    @Override
    public void handlePlayerPosLook(S08PacketPlayerPosLook s08PacketPlayerPosLook) {
        PacketThreadUtil.checkThreadAndEnqueue(s08PacketPlayerPosLook, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        double d = S08PacketPlayerPosLook.getX();
        double d2 = S08PacketPlayerPosLook.getY();
        double d3 = s08PacketPlayerPosLook.getZ();
        float f = s08PacketPlayerPosLook.getYaw();
        float f2 = s08PacketPlayerPosLook.getPitch();
        if (s08PacketPlayerPosLook.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.X)) {
            d += entityPlayerSP.posX;
        } else {
            entityPlayerSP.motionX = 0.0;
        }
        if (s08PacketPlayerPosLook.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Y)) {
            d2 += entityPlayerSP.posY;
        } else {
            entityPlayerSP.motionY = 0.0;
        }
        if (s08PacketPlayerPosLook.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Z)) {
            d3 += entityPlayerSP.posZ;
        } else {
            entityPlayerSP.motionZ = 0.0;
        }
        if (s08PacketPlayerPosLook.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 += entityPlayerSP.rotationPitch;
        }
        if (s08PacketPlayerPosLook.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityPlayerSP.rotationYaw;
        }
        entityPlayerSP.setPositionAndRotation(d, d2, d3, f, f2);
        this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(entityPlayerSP.posX, entityPlayerSP.getEntityBoundingBox().minY, entityPlayerSP.posZ, entityPlayerSP.rotationYaw, entityPlayerSP.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            Minecraft.thePlayer.prevPosX = Minecraft.thePlayer.posX;
            Minecraft.thePlayer.prevPosY = Minecraft.thePlayer.posY;
            Minecraft.thePlayer.prevPosZ = Minecraft.thePlayer.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen(null);
        }
    }

    public void addToSendQueue(Packet packet) {
        EventPacket eventPacket = new EventPacket(packet, false);
        eventPacket.call();
        Exodus.onEvent(eventPacket);
        if (eventPacket.isCancelled()) {
            return;
        }
        this.netManager.sendPacket(packet);
    }

    @Override
    public void handleBlockAction(S24PacketBlockAction s24PacketBlockAction) {
        PacketThreadUtil.checkThreadAndEnqueue(s24PacketBlockAction, this, this.gameController);
        Minecraft.theWorld.addBlockEvent(s24PacketBlockAction.getBlockPosition(), s24PacketBlockAction.getBlockType(), s24PacketBlockAction.getData1(), s24PacketBlockAction.getData2());
    }

    @Override
    public void handleEntityProperties(S20PacketEntityProperties s20PacketEntityProperties) {
        PacketThreadUtil.checkThreadAndEnqueue(s20PacketEntityProperties, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s20PacketEntityProperties.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            BaseAttributeMap baseAttributeMap = ((EntityLivingBase)entity).getAttributeMap();
            for (S20PacketEntityProperties.Snapshot snapshot : s20PacketEntityProperties.func_149441_d()) {
                IAttributeInstance iAttributeInstance = baseAttributeMap.getAttributeInstanceByName(snapshot.func_151409_a());
                if (iAttributeInstance == null) {
                    iAttributeInstance = baseAttributeMap.registerAttribute(new RangedAttribute(null, snapshot.func_151409_a(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iAttributeInstance.setBaseValue(snapshot.func_151410_b());
                iAttributeInstance.removeAllModifiers();
                for (AttributeModifier attributeModifier : snapshot.func_151408_c()) {
                    iAttributeInstance.applyModifier(attributeModifier);
                }
            }
        }
    }

    @Override
    public void handleChangeGameState(S2BPacketChangeGameState s2BPacketChangeGameState) {
        PacketThreadUtil.checkThreadAndEnqueue(s2BPacketChangeGameState, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        int n = s2BPacketChangeGameState.getGameState();
        float f = s2BPacketChangeGameState.func_149137_d();
        int n2 = MathHelper.floor_float(f + 0.5f);
        if (n >= 0 && n < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[n] != null) {
            ((EntityPlayer)entityPlayerSP).addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[n], new Object[0]));
        }
        if (n == 1) {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0f);
        } else if (n == 2) {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0f);
        } else if (n == 3) {
            Minecraft.playerController.setGameType(WorldSettings.GameType.getByID(n2));
        } else if (n == 4) {
            this.gameController.displayGuiScreen(new GuiWinGame());
        } else if (n == 5) {
            GameSettings gameSettings = Minecraft.gameSettings;
            if (f == 0.0f) {
                this.gameController.displayGuiScreen(new GuiScreenDemo());
            } else if (f == 101.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", GameSettings.getKeyDisplayString(gameSettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gameSettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gameSettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gameSettings.keyBindRight.getKeyCode())));
            } else if (f == 102.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", GameSettings.getKeyDisplayString(gameSettings.keyBindJump.getKeyCode())));
            } else if (f == 103.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", GameSettings.getKeyDisplayString(gameSettings.keyBindInventory.getKeyCode())));
            }
        } else if (n == 6) {
            this.clientWorldController.playSound(entityPlayerSP.posX, entityPlayerSP.posY + (double)entityPlayerSP.getEyeHeight(), entityPlayerSP.posZ, "random.successful_hit", 0.18f, 0.45f, false);
        } else if (n == 7) {
            this.clientWorldController.setRainStrength(f);
        } else if (n == 8) {
            this.clientWorldController.setThunderStrength(f);
        } else if (n == 10) {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityPlayerSP.posX, entityPlayerSP.posY, entityPlayerSP.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.clientWorldController.playSound(entityPlayerSP.posX, entityPlayerSP.posY, entityPlayerSP.posZ, "mob.guardian.curse", 1.0f, 1.0f, false);
        }
    }

    @Override
    public void handleAnimation(S0BPacketAnimation s0BPacketAnimation) {
        PacketThreadUtil.checkThreadAndEnqueue(s0BPacketAnimation, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(s0BPacketAnimation.getEntityID());
        if (entity != null) {
            if (s0BPacketAnimation.getAnimationType() == 0) {
                EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                entityLivingBase.swingItem();
            } else if (s0BPacketAnimation.getAnimationType() == 1) {
                entity.performHurtAnimation();
            } else if (s0BPacketAnimation.getAnimationType() == 2) {
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                entityPlayer.wakeUpPlayer(false, false, false);
            } else if (s0BPacketAnimation.getAnimationType() == 4) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            } else if (s0BPacketAnimation.getAnimationType() == 5) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    @Override
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity s35PacketUpdateTileEntity) {
        PacketThreadUtil.checkThreadAndEnqueue(s35PacketUpdateTileEntity, this, this.gameController);
        if (Minecraft.theWorld.isBlockLoaded(s35PacketUpdateTileEntity.getPos())) {
            TileEntity tileEntity = Minecraft.theWorld.getTileEntity(s35PacketUpdateTileEntity.getPos());
            int n = s35PacketUpdateTileEntity.getTileEntityType();
            if (n == 1 && tileEntity instanceof TileEntityMobSpawner || n == 2 && tileEntity instanceof TileEntityCommandBlock || n == 3 && tileEntity instanceof TileEntityBeacon || n == 4 && tileEntity instanceof TileEntitySkull || n == 5 && tileEntity instanceof TileEntityFlowerPot || n == 6 && tileEntity instanceof TileEntityBanner) {
                tileEntity.readFromNBT(s35PacketUpdateTileEntity.getNbtCompound());
            }
        }
    }

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
        this.gameController.loadWorld(null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.gameController.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", iChatComponent).getProxy());
            } else {
                this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", iChatComponent));
            }
        } else {
            this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", iChatComponent));
        }
    }

    @Override
    public void handleMaps(S34PacketMaps s34PacketMaps) {
        PacketThreadUtil.checkThreadAndEnqueue(s34PacketMaps, this, this.gameController);
        MapData mapData = ItemMap.loadMapData(s34PacketMaps.getMapId(), Minecraft.theWorld);
        s34PacketMaps.setMapdataTo(mapData);
        this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapData);
    }

    @Override
    public void handleWindowProperty(S31PacketWindowProperty s31PacketWindowProperty) {
        PacketThreadUtil.checkThreadAndEnqueue(s31PacketWindowProperty, this, this.gameController);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if (entityPlayerSP.openContainer != null && entityPlayerSP.openContainer.windowId == s31PacketWindowProperty.getWindowId()) {
            entityPlayerSP.openContainer.updateProgressBar(s31PacketWindowProperty.getVarIndex(), s31PacketWindowProperty.getVarValue());
        }
    }

    @Override
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard s3DPacketDisplayScoreboard) {
        PacketThreadUtil.checkThreadAndEnqueue(s3DPacketDisplayScoreboard, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (s3DPacketDisplayScoreboard.func_149370_d().length() == 0) {
            scoreboard.setObjectiveInDisplaySlot(s3DPacketDisplayScoreboard.func_149371_c(), null);
        } else {
            ScoreObjective scoreObjective = scoreboard.getObjective(s3DPacketDisplayScoreboard.func_149370_d());
            scoreboard.setObjectiveInDisplaySlot(s3DPacketDisplayScoreboard.func_149371_c(), scoreObjective);
        }
    }

    @Override
    public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb s11PacketSpawnExperienceOrb) {
        PacketThreadUtil.checkThreadAndEnqueue(s11PacketSpawnExperienceOrb, this, this.gameController);
        EntityXPOrb entityXPOrb = new EntityXPOrb(this.clientWorldController, (double)s11PacketSpawnExperienceOrb.getX() / 32.0, (double)s11PacketSpawnExperienceOrb.getY() / 32.0, (double)s11PacketSpawnExperienceOrb.getZ() / 32.0, s11PacketSpawnExperienceOrb.getXPValue());
        entityXPOrb.serverPosX = s11PacketSpawnExperienceOrb.getX();
        entityXPOrb.serverPosY = s11PacketSpawnExperienceOrb.getY();
        entityXPOrb.serverPosZ = s11PacketSpawnExperienceOrb.getZ();
        entityXPOrb.rotationYaw = 0.0f;
        entityXPOrb.rotationPitch = 0.0f;
        entityXPOrb.setEntityId(s11PacketSpawnExperienceOrb.getEntityID());
        this.clientWorldController.addEntityToWorld(s11PacketSpawnExperienceOrb.getEntityID(), entityXPOrb);
    }
}

