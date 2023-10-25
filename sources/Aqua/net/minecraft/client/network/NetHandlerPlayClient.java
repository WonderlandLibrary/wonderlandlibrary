package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import events.Event;
import events.EventType;
import events.listeners.EventBacktrack;
import events.listeners.EventPacket;
import events.listeners.EventPacketNofall;
import events.listeners.EventTimerDisabler;
import intent.AquaDev.aqua.Aqua;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.audio.ISound;
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
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
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
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayClient
implements INetHandlerPlayClient {
    private static final Logger logger = LogManager.getLogger();
    private final NetworkManager netManager;
    private final GameProfile profile;
    private final GuiScreen guiScreenServer;
    private Minecraft gameController;
    private WorldClient clientWorldController;
    private boolean doneLoadingTerrain;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
    public int currentServerMaxPlayers = 20;
    private boolean field_147308_k = false;
    private final Random avRandomizer = new Random();

    public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_) {
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = p_i46300_3_;
        this.profile = p_i46300_4_;
    }

    public void cleanup() {
        this.clientWorldController = null;
    }

    public void handleJoinGame(S01PacketJoinGame packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        this.gameController.thePlayer.dimension = packetIn.getDimension();
        this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain(this));
        this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
        this.gameController.playerController.setGameType(packetIn.getGameType());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket((Packet)new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }

    public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        double d0 = (double)packetIn.getX() / 32.0;
        double d1 = (double)packetIn.getY() / 32.0;
        double d2 = (double)packetIn.getZ() / 32.0;
        EntityMinecart entity = null;
        if (packetIn.getType() == 10) {
            entity = EntityMinecart.getMinecart((World)this.clientWorldController, (double)d0, (double)d1, (double)d2, (EntityMinecart.EnumMinecartType)EntityMinecart.EnumMinecartType.byNetworkID((int)packetIn.func_149009_m()));
        } else if (packetIn.getType() == 90) {
            Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
            if (entity1 instanceof EntityPlayer) {
                entity = new EntityFishHook((World)this.clientWorldController, d0, d1, d2, (EntityPlayer)entity1);
            }
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 60) {
            entity = new EntityArrow((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 61) {
            entity = new EntitySnowball((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 71) {
            entity = new EntityItemFrame((World)this.clientWorldController, new BlockPos(MathHelper.floor_double((double)d0), MathHelper.floor_double((double)d1), MathHelper.floor_double((double)d2)), EnumFacing.getHorizontal((int)packetIn.func_149009_m()));
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 77) {
            entity = new EntityLeashKnot((World)this.clientWorldController, new BlockPos(MathHelper.floor_double((double)d0), MathHelper.floor_double((double)d1), MathHelper.floor_double((double)d2)));
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 65) {
            entity = new EntityEnderPearl((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 72) {
            entity = new EntityEnderEye((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 76) {
            entity = new EntityFireworkRocket((World)this.clientWorldController, d0, d1, d2, (ItemStack)null);
        } else if (packetIn.getType() == 63) {
            entity = new EntityLargeFireball((World)this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 64) {
            entity = new EntitySmallFireball((World)this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 66) {
            entity = new EntityWitherSkull((World)this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 62) {
            entity = new EntityEgg((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 73) {
            entity = new EntityPotion((World)this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 75) {
            entity = new EntityExpBottle((World)this.clientWorldController, d0, d1, d2);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 1) {
            entity = new EntityBoat((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 50) {
            entity = new EntityTNTPrimed((World)this.clientWorldController, d0, d1, d2, (EntityLivingBase)null);
        } else if (packetIn.getType() == 78) {
            entity = new EntityArmorStand((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 51) {
            entity = new EntityEnderCrystal((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 2) {
            entity = new EntityItem((World)this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 70) {
            entity = new EntityFallingBlock((World)this.clientWorldController, d0, d1, d2, Block.getStateById((int)(packetIn.func_149009_m() & 0xFFFF)));
            packetIn.func_149002_g(0);
        }
        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            entity.rotationPitch = (float)(packetIn.getPitch() * 360) / 256.0f;
            entity.rotationYaw = (float)(packetIn.getYaw() * 360) / 256.0f;
            Entity[] aentity = entity.getParts();
            if (aentity != null) {
                int i = packetIn.getEntityID() - entity.getEntityId();
                for (int j = 0; j < aentity.length; ++j) {
                    aentity[j].setEntityId(aentity[j].getEntityId() + i);
                }
            }
            entity.setEntityId(packetIn.getEntityID());
            this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entity);
            if (packetIn.func_149009_m() > 0) {
                Entity entity2;
                if (packetIn.getType() == 60 && (entity2 = this.clientWorldController.getEntityByID(packetIn.func_149009_m())) instanceof EntityLivingBase && entity instanceof EntityArrow) {
                    ((EntityArrow)entity).shootingEntity = entity2;
                }
                entity.setVelocity((double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            }
        }
    }

    public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityXPOrb entity = new EntityXPOrb((World)this.clientWorldController, (double)packetIn.getX() / 32.0, (double)packetIn.getY() / 32.0, (double)packetIn.getZ() / 32.0, packetIn.getXPValue());
        entity.serverPosX = packetIn.getX();
        entity.serverPosY = packetIn.getY();
        entity.serverPosZ = packetIn.getZ();
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.setEntityId(packetIn.getEntityID());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entity);
    }

    public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        double d0 = (double)packetIn.func_149051_d() / 32.0;
        double d1 = (double)packetIn.func_149050_e() / 32.0;
        double d2 = (double)packetIn.func_149049_f() / 32.0;
        EntityLightningBolt entity = null;
        if (packetIn.func_149053_g() == 1) {
            entity = new EntityLightningBolt((World)this.clientWorldController, d0, d1, d2);
        }
        if (entity != null) {
            entity.serverPosX = packetIn.func_149051_d();
            entity.serverPosY = packetIn.func_149050_e();
            entity.serverPosZ = packetIn.func_149049_f();
            entity.rotationYaw = 0.0f;
            entity.rotationPitch = 0.0f;
            entity.setEntityId(packetIn.func_149052_c());
            this.clientWorldController.addWeatherEffect((Entity)entity);
        }
    }

    public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPainting entitypainting = new EntityPainting((World)this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitypainting);
    }

    public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setVelocity((double)packetIn.getMotionX() / 8000.0, (double)packetIn.getMotionY() / 8000.0, (double)packetIn.getMotionZ() / 8000.0);
        }
    }

    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null && packetIn.func_149376_c() != null) {
            entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }

    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        double d0 = (double)packetIn.getX() / 32.0;
        double d1 = (double)packetIn.getY() / 32.0;
        double d2 = (double)packetIn.getZ() / 32.0;
        float f = (float)(packetIn.getYaw() * 360) / 256.0f;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP((World)this.gameController.theWorld, this.getPlayerInfo(packetIn.getPlayer()).getGameProfile());
        entityotherplayermp.serverPosX = packetIn.getX();
        entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (double)entityotherplayermp.serverPosX;
        entityotherplayermp.serverPosY = packetIn.getY();
        entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (double)entityotherplayermp.serverPosY;
        entityotherplayermp.serverPosZ = packetIn.getZ();
        entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (double)entityotherplayermp.serverPosZ;
        int i = packetIn.getCurrentItemID();
        entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = i == 0 ? null : new ItemStack(Item.getItemById((int)i), 1, 0);
        entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityotherplayermp);
        List list = packetIn.func_148944_c();
        if (list != null) {
            entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            double d0 = (double)entity.serverPosX / 32.0;
            double d1 = (double)entity.serverPosY / 32.0;
            double d2 = (double)entity.serverPosZ / 32.0;
            float f = (float)(packetIn.getYaw() * 360) / 256.0f;
            float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
            if (Math.abs((double)(entity.posX - d0)) < 0.03125 && Math.abs((double)(entity.posY - d1)) < 0.015625 && Math.abs((double)(entity.posZ - d2)) < 0.03125) {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
            } else {
                entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
            }
            entity.onGround = packetIn.getOnGround();
        }
    }

    public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
            this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }

    public void handleEntityMovement(S14PacketEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = packetIn.getEntity((World)this.clientWorldController);
        if (entity != null) {
            entity.serverPosX += packetIn.func_149062_c();
            entity.serverPosY += packetIn.func_149061_d();
            entity.serverPosZ += packetIn.func_149064_e();
            double d0 = (double)entity.serverPosX / 32.0;
            double d1 = (double)entity.serverPosY / 32.0;
            double d2 = (double)entity.serverPosZ / 32.0;
            float f = packetIn.func_149060_h() ? (float)(packetIn.func_149066_f() * 360) / 256.0f : entity.rotationYaw;
            float f1 = packetIn.func_149060_h() ? (float)(packetIn.func_149063_g() * 360) / 256.0f : entity.rotationPitch;
            entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
            entity.onGround = packetIn.getOnGround();
        }
    }

    public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = packetIn.getEntity((World)this.clientWorldController);
        if (entity != null) {
            float f = (float)(packetIn.getYaw() * 360) / 256.0f;
            entity.setRotationYawHead(f);
        }
    }

    public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
            this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }

    public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f1 = packetIn.getPitch();
        if (packetIn.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.X)) {
            d0 += entityplayer.posX;
        } else {
            entityplayer.motionX = 0.0;
        }
        if (packetIn.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Y)) {
            d1 += entityplayer.posY;
        } else {
            entityplayer.motionY = 0.0;
        }
        if (packetIn.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Z)) {
            d2 += entityplayer.posZ;
        } else {
            entityplayer.motionZ = 0.0;
        }
        if (packetIn.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f1 += entityplayer.rotationPitch;
        }
        if (packetIn.func_179834_f().contains((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityplayer.rotationYaw;
        }
        entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
        this.netManager.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
            this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
            this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen((GuiScreen)null);
        }
    }

    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        for (S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata : packetIn.getChangedBlocks()) {
            this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState());
        }
    }

    public void handleChunkData(S21PacketChunkData packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.func_149274_i()) {
            if (packetIn.getExtractedSize() == 0) {
                this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
                return;
            }
            this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }
        this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.fillChunk(packetIn.getExtractedDataBytes(), packetIn.getExtractedSize(), packetIn.func_149274_i());
        this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
    }

    public void handleBlockChange(S23PacketBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }

    public void handleDisconnect(S40PacketDisconnect packetIn) {
        this.netManager.closeChannel(packetIn.getReason());
    }

    public void onDisconnect(IChatComponent reason) {
        this.gameController.loadWorld((WorldClient)null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.gameController.displayGuiScreen((GuiScreen)new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason).getProxy());
            } else {
                this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        } else {
            this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), "disconnect.lost", reason));
        }
    }

    public void addToSendQueue(Packet packet) {
        try {
            EventPacketNofall eventPacket1 = new EventPacketNofall(EventPacketNofall.Action.SEND, packet, null, null);
            eventPacket1.setType(EventType.PRE);
            Aqua.INSTANCE.onEvent((Event)eventPacket1);
            EventTimerDisabler eventTimerDisabler = new EventTimerDisabler(EventTimerDisabler.Action.SEND, packet, null, null);
            eventTimerDisabler.setType(EventType.PRE);
            Aqua.INSTANCE.onEvent((Event)eventTimerDisabler);
            EventBacktrack eventBacktrack = new EventBacktrack(EventBacktrack.Action.SEND, packet, null, null);
            eventBacktrack.setType(EventType.PRE);
            EventPacket eventPacket = new EventPacket(EventPacket.Action.SEND, packet, null, null);
            eventPacket.setType(EventType.PRE);
            if (Aqua.moduleManager.getModuleByName("Backtrack").isToggled()) {
                Aqua.INSTANCE.onEvent((Event)eventPacket);
            }
            if (!eventBacktrack.isCancelled()) {
                this.netManager.sendPacket(packet);
            }
        }
        catch (Exception e) {
            this.netManager.sendPacket(packet);
        }
    }

    public void handleCollectItem(S0DPacketCollectItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entitylivingbase == null) {
            entitylivingbase = this.gameController.thePlayer;
        }
        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            } else {
                this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            this.gameController.effectRenderer.addEffect((EntityFX)new EntityPickupFX((World)this.clientWorldController, entity, (Entity)entitylivingbase, 0.5f));
            this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }

    public void handleChat(S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.getType() == 2) {
            this.gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
        } else {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
        }
    }

    public void handleAnimation(S0BPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingItem();
            } else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            } else if (packetIn.getAnimationType() == 2) {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            } else if (packetIn.getAnimationType() == 4) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            } else if (packetIn.getAnimationType() == 5) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    public void handleUseBed(S0APacketUseBed packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        packetIn.getPlayer((World)this.clientWorldController).trySleep(packetIn.getBedPosition());
    }

    public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        double d0 = (double)packetIn.getX() / 32.0;
        double d1 = (double)packetIn.getY() / 32.0;
        double d2 = (double)packetIn.getZ() / 32.0;
        float f = (float)(packetIn.getYaw() * 360) / 256.0f;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
        EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID((int)packetIn.getEntityType(), (World)this.gameController.theWorld);
        entitylivingbase.serverPosX = packetIn.getX();
        entitylivingbase.serverPosY = packetIn.getY();
        entitylivingbase.serverPosZ = packetIn.getZ();
        entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (float)(packetIn.getHeadPitch() * 360) / 256.0f;
        Entity[] aentity = entitylivingbase.getParts();
        if (aentity != null) {
            int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
            for (int j = 0; j < aentity.length; ++j) {
                aentity[j].setEntityId(aentity[j].getEntityId() + i);
            }
        }
        entitylivingbase.setEntityId(packetIn.getEntityID());
        entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
        entitylivingbase.motionX = (float)packetIn.getVelocityX() / 8000.0f;
        entitylivingbase.motionY = (float)packetIn.getVelocityY() / 8000.0f;
        entitylivingbase.motionZ = (float)packetIn.getVelocityZ() / 8000.0f;
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitylivingbase);
        List list = packetIn.func_149027_c();
        if (list != null) {
            entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
    }

    public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.thePlayer.setSpawnPoint(packetIn.getSpawnPos(), true);
        this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }

    public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
        if (packetIn.getLeash() == 0) {
            boolean flag = false;
            if (packetIn.getEntityId() == this.gameController.thePlayer.getEntityId()) {
                entity = this.gameController.thePlayer;
                if (entity1 instanceof EntityBoat) {
                    ((EntityBoat)entity1).setIsBoatEmpty(false);
                }
                flag = entity.ridingEntity == null && entity1 != null;
            } else if (entity1 instanceof EntityBoat) {
                ((EntityBoat)entity1).setIsBoatEmpty(true);
            }
            if (entity == null) {
                return;
            }
            entity.mountEntity(entity1);
            if (flag) {
                GameSettings gamesettings = this.gameController.gameSettings;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format((String)"mount.onboard", (Object[])new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindSneak.getKeyCode())}), false);
            }
        } else if (packetIn.getLeash() == 1 && entity instanceof EntityLiving) {
            if (entity1 != null) {
                ((EntityLiving)entity).setLeashedToEntity(entity1, false);
            } else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }

    public void handleEntityStatus(S19PacketEntityStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = packetIn.getEntity((World)this.clientWorldController);
        if (entity != null) {
            if (packetIn.getOpCode() == 21) {
                this.gameController.getSoundHandler().playSound((ISound)new GuardianSound((EntityGuardian)entity));
            } else {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }

    public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
        this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    public void handleSetExperience(S1FPacketSetExperience packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
    }

    public void handleRespawn(S07PacketRespawn packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.getDimensionID() != this.gameController.thePlayer.dimension) {
            this.doneLoadingTerrain = false;
            Scoreboard scoreboard = this.clientWorldController.getScoreboard();
            this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
            this.clientWorldController.setWorldScoreboard(scoreboard);
            this.gameController.loadWorld(this.clientWorldController);
            this.gameController.thePlayer.dimension = packetIn.getDimensionID();
            this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain(this));
        }
        this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        this.gameController.playerController.setGameType(packetIn.getGameType());
    }

    public void handleExplosion(S27PacketExplosion packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Explosion explosion = new Explosion((World)this.gameController.theWorld, (Entity)null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        this.gameController.thePlayer.motionX += (double)packetIn.func_149149_c();
        this.gameController.thePlayer.motionY += (double)packetIn.func_149144_d();
        this.gameController.thePlayer.motionZ += (double)packetIn.func_149147_e();
    }

    public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;
        if ("minecraft:container".equals((Object)packetIn.getGuiId())) {
            entityplayersp.displayGUIChest((IInventory)new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("minecraft:villager".equals((Object)packetIn.getGuiId())) {
            entityplayersp.displayVillagerTradeGui((IMerchant)new NpcMerchant((EntityPlayer)entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("EntityHorse".equals((Object)packetIn.getGuiId())) {
            Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
            if (entity instanceof EntityHorse) {
                entityplayersp.displayGUIHorse((EntityHorse)entity, (IInventory)new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        } else if (!packetIn.hasSlots()) {
            entityplayersp.displayGui((IInteractionObject)new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else {
            ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest((IInventory)containerlocalmenu);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }

    public void handleSetSlot(S2FPacketSetSlot packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        if (packetIn.func_149175_c() == -1) {
            entityplayer.inventory.setItemStack(packetIn.func_149174_e());
        } else {
            boolean flag = false;
            if (this.gameController.currentScreen instanceof GuiContainerCreative) {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
                boolean bl = flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
            }
            if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
                ItemStack itemstack = entityplayer.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
                if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < packetIn.func_149174_e().stackSize)) {
                    packetIn.func_149174_e().animationsToGo = 5;
                }
                entityplayer.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            } else if (!(packetIn.func_149175_c() != entityplayer.openContainer.windowId || packetIn.func_149175_c() == 0 && flag)) {
                entityplayer.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
        }
    }

    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Container container = null;
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }
        if (container != null && !packetIn.func_148888_e()) {
            this.addToSendQueue((Packet)new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }

    public void handleWindowItems(S30PacketWindowItems packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        if (packetIn.func_148911_c() == 0) {
            entityplayer.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
        } else if (packetIn.func_148911_c() == entityplayer.openContainer.windowId) {
            entityplayer.openContainer.putStacksInSlots(packetIn.getItemStacks());
        }
    }

    public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
        if (!(tileentity instanceof TileEntitySign)) {
            tileentity = new TileEntitySign();
            tileentity.setWorldObj((World)this.clientWorldController);
            tileentity.setPos(packetIn.getSignPosition());
        }
        this.gameController.thePlayer.openEditSign((TileEntitySign)tileentity);
    }

    public void handleUpdateSign(S33PacketUpdateSign packetIn) {
        TileEntity tileentity;
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        boolean flag = false;
        if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos()) && (tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos())) instanceof TileEntitySign) {
            TileEntitySign tileentitysign = (TileEntitySign)tileentity;
            if (tileentitysign.getIsEditable()) {
                System.arraycopy((Object)packetIn.getLines(), (int)0, (Object)tileentitysign.signText, (int)0, (int)4);
                tileentitysign.markDirty();
            }
            flag = true;
        }
        if (!flag && this.gameController.thePlayer != null) {
            this.gameController.thePlayer.addChatMessage((IChatComponent)new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ()));
        }
    }

    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
            TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
            int i = packetIn.getTileEntityType();
            if (i == 1 && tileentity instanceof TileEntityMobSpawner || i == 2 && tileentity instanceof TileEntityCommandBlock || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner) {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
        }
    }

    public void handleWindowProperty(S31PacketWindowProperty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
            entityplayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
        }
    }

    public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }

    public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.thePlayer.closeScreenAndDropStack();
    }

    public void handleBlockAction(S24PacketBlockAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.theWorld.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }

    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.theWorld.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }

    public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        for (int i = 0; i < packetIn.getChunkCount(); ++i) {
            int j = packetIn.getChunkX(i);
            int k = packetIn.getChunkZ(i);
            this.clientWorldController.doPreChunk(j, k, true);
            this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
            chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
            this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            if (this.clientWorldController.provider instanceof WorldProviderSurface) continue;
            chunk.resetRelightChecks();
        }
    }

    public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        int i = packetIn.getGameState();
        float f = packetIn.func_149137_d();
        int j = MathHelper.floor_float((float)(f + 0.5f));
        if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null) {
            entityplayer.addChatComponentMessage((IChatComponent)new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
        }
        if (i == 1) {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0f);
        } else if (i == 2) {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0f);
        } else if (i == 3) {
            this.gameController.playerController.setGameType(WorldSettings.GameType.getByID((int)j));
        } else if (i == 4) {
            this.gameController.displayGuiScreen((GuiScreen)new GuiWinGame());
        } else if (i == 5) {
            GameSettings gamesettings = this.gameController.gameSettings;
            if (f == 0.0f) {
                this.gameController.displayGuiScreen((GuiScreen)new GuiScreenDemo());
            } else if (f == 101.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.movement", new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString((int)gamesettings.keyBindRight.getKeyCode())}));
            } else if (f == 102.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.jump", new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindJump.getKeyCode())}));
            } else if (f == 103.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.inventory", new Object[]{GameSettings.getKeyDisplayString((int)gamesettings.keyBindInventory.getKeyCode())}));
            }
        } else if (i == 6) {
            this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ, "random.successful_hit", 0.18f, 0.45f, false);
        } else if (i == 7) {
            this.clientWorldController.setRainStrength(f);
        } else if (i == 8) {
            this.clientWorldController.setThunderStrength(f);
        } else if (i == 10) {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.clientWorldController.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "mob.guardian.curse", 1.0f, 1.0f, false);
        }
    }

    public void handleMaps(S34PacketMaps packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        MapData mapdata = ItemMap.loadMapData((int)packetIn.getMapId(), (World)this.gameController.theWorld);
        packetIn.setMapdataTo(mapdata);
        this.gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
    }

    public void handleEffect(S28PacketEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.isSoundServerwide()) {
            this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        } else {
            this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }

    public void handleStatistics(S37PacketStatistics packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        boolean flag = false;
        for (Map.Entry entry : packetIn.func_148974_c().entrySet()) {
            StatBase statbase = (StatBase)entry.getKey();
            int i = (Integer)entry.getValue();
            if (statbase.isAchievement() && i > 0) {
                if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
                    Achievement achievement = (Achievement)statbase;
                    this.gameController.guiAchievement.displayAchievement(achievement);
                    if (statbase == AchievementList.openInventory) {
                        this.gameController.gameSettings.showInventoryAchievementHint = false;
                        this.gameController.gameSettings.saveOptions();
                    }
                }
                flag = true;
            }
            this.gameController.thePlayer.getStatFileWriter().unlockAchievement((EntityPlayer)this.gameController.thePlayer, statbase, i);
        }
        if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint) {
            this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }
        this.field_147308_k = true;
        if (this.gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)this.gameController.currentScreen).doneLoading();
        }
    }

    public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase) {
            PotionEffect potioneffect = new PotionEffect((int)packetIn.getEffectId(), packetIn.getDuration(), (int)packetIn.getAmplifier(), false, packetIn.func_179707_f());
            potioneffect.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
        }
    }

    public void handleCombatEvent(S42PacketCombatEvent packetIn) {
        Entity entity1;
        EntityLivingBase entitylivingbase;
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
        EntityLivingBase entityLivingBase = entitylivingbase = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;
        if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
            long l = 1000 * packetIn.field_179772_d / 20;
        } else if (packetIn.eventType != S42PacketCombatEvent.Event.ENTITY_DIED || (entity1 = this.clientWorldController.getEntityByID(packetIn.field_179774_b)) instanceof EntityPlayer) {
            // empty if block
        }
    }

    public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }

    public void handleCamera(S43PacketCamera packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = packetIn.getEntity((World)this.clientWorldController);
        if (entity != null) {
            this.gameController.setRenderViewEntity(entity);
        }
    }

    public void handleWorldBorder(S44PacketWorldBorder packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
    }

    public void handleTitle(S45PacketTitle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        S45PacketTitle.Type s45packettitle$type = packetIn.getType();
        String s = null;
        String s1 = null;
        String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";
        switch (4.$SwitchMap$net$minecraft$network$play$server$S45PacketTitle$Type[s45packettitle$type.ordinal()]) {
            case 1: {
                s = s2;
                break;
            }
            case 2: {
                s1 = s2;
                break;
            }
            case 3: {
                this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
                this.gameController.ingameGUI.setDefaultTitlesTimes();
                return;
            }
        }
        this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }

    public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
        if (!this.netManager.isLocalChannel()) {
            this.netManager.setCompressionTreshold(packetIn.getThreshold());
        }
    }

    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
        this.gameController.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().length() == 0 ? null : packetIn.getHeader());
        this.gameController.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().length() == 0 ? null : packetIn.getFooter());
    }

    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
        }
    }

    public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        for (S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.getEntries()) {
            if (packetIn.getAction() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove((Object)s38packetplayerlistitem$addplayerdata.getProfile().getId());
                continue;
            }
            NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)this.playerInfoMap.get((Object)s38packetplayerlistitem$addplayerdata.getProfile().getId());
            if (packetIn.getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
                this.playerInfoMap.put((Object)networkplayerinfo.getGameProfile().getId(), (Object)networkplayerinfo);
            }
            if (networkplayerinfo == null) continue;
            switch (4.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[packetIn.getAction().ordinal()]) {
                case 1: {
                    networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                    networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case 2: {
                    networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                    break;
                }
                case 3: {
                    networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case 4: {
                    networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
                }
            }
        }
    }

    public void handleKeepAlive(S00PacketKeepAlive packetIn) {
        this.addToSendQueue((Packet)new C00PacketKeepAlive(packetIn.func_149134_c()));
    }

    public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        EntityPlayerSP entityplayer = this.gameController.thePlayer;
        entityplayer.capabilities.isFlying = packetIn.isFlying();
        entityplayer.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayer.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayer.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayer.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }

    public void handleTabComplete(S3APacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        String[] astring = packetIn.func_149630_c();
        if (this.gameController.currentScreen instanceof GuiChat) {
            GuiChat guichat = (GuiChat)this.gameController.currentScreen;
            guichat.onAutocompleteResponse(astring);
        }
    }

    public void handleSoundEffect(S29PacketSoundEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(), packetIn.getVolume(), packetIn.getPitch(), false);
    }

    public void handleResourcePack(S48PacketResourcePackSend packetIn) {
        String s = packetIn.getURL();
        String s1 = packetIn.getHash();
        if (s.startsWith("level://")) {
            File file1 = new File(this.gameController.mcDataDir, "saves");
            String s2 = s.substring("level://".length());
            File file2 = new File(file1, s2);
            if (file2.isFile()) {
                this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback((ListenableFuture)this.gameController.getResourcePackRepository().setResourcePackInstance(file2), (FutureCallback)new /* Unavailable Anonymous Inner Class!! */);
            } else {
                this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
            this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback((ListenableFuture)this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), (FutureCallback)new /* Unavailable Anonymous Inner Class!! */);
        } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
            this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
        } else {
            this.gameController.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */);
        }
    }

    public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = packetIn.getEntity((World)this.clientWorldController);
        if (entity != null) {
            entity.clientUpdateEntityNBT(packetIn.getTagCompound());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if ("MC|TrList".equals((Object)packetIn.getChannelName())) {
            PacketBuffer packetbuffer = packetIn.getBufferData();
            try {
                int i = packetbuffer.readInt();
                GuiScreen guiscreen = this.gameController.currentScreen;
                if (guiscreen == null || !(guiscreen instanceof GuiMerchant) || i != this.gameController.thePlayer.openContainer.windowId) return;
                IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf((PacketBuffer)packetbuffer);
                imerchant.setRecipes(merchantrecipelist);
                return;
            }
            catch (IOException ioexception) {
                logger.error("Couldn't load trade info", (Throwable)ioexception);
                return;
            }
            finally {
                packetbuffer.release();
            }
        } else if ("MC|Brand".equals((Object)packetIn.getChannelName())) {
            this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(Short.MAX_VALUE));
            return;
        } else {
            ItemStack itemstack;
            if (!"MC|BOpen".equals((Object)packetIn.getChannelName()) || (itemstack = this.gameController.thePlayer.getCurrentEquippedItem()) == null || itemstack.getItem() != Items.written_book) return;
            this.gameController.displayGuiScreen((GuiScreen)new GuiScreenBook((EntityPlayer)this.gameController.thePlayer, itemstack, false));
        }
    }

    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.func_149338_e() == 0) {
            ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.func_149337_d());
            scoreobjective.setRenderType(packetIn.func_179817_d());
        } else {
            ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.func_149339_c());
            if (packetIn.func_149338_e() == 1) {
                scoreboard.removeObjective(scoreobjective1);
            } else if (packetIn.func_149338_e() == 2) {
                scoreobjective1.setDisplayName(packetIn.func_149337_d());
                scoreobjective1.setRenderType(packetIn.func_179817_d());
            }
        }
    }

    public void handleUpdateScore(S3CPacketUpdateScore packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
        if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
            Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        } else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty((String)packetIn.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), (ScoreObjective)null);
            } else if (scoreobjective != null) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }

    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.func_149370_d().length() == 0) {
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), (ScoreObjective)null);
        } else {
            ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
        }
    }

    public void handleTeams(S3EPacketTeams packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam = packetIn.getAction() == 0 ? scoreboard.createTeam(packetIn.getName()) : scoreboard.getTeam(packetIn.getName());
        if (packetIn.getAction() == 0 || packetIn.getAction() == 2) {
            scoreplayerteam.setTeamName(packetIn.getDisplayName());
            scoreplayerteam.setNamePrefix(packetIn.getPrefix());
            scoreplayerteam.setNameSuffix(packetIn.getSuffix());
            scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a((int)packetIn.getColor()));
            scoreplayerteam.func_98298_a(packetIn.getFriendlyFlags());
            Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a((String)packetIn.getNameTagVisibility());
            if (team$enumvisible != null) {
                scoreplayerteam.setNameTagVisibility(team$enumvisible);
            }
        }
        if (packetIn.getAction() == 0 || packetIn.getAction() == 3) {
            for (String s : packetIn.getPlayers()) {
                scoreboard.addPlayerToTeam(s, packetIn.getName());
            }
        }
        if (packetIn.getAction() == 4) {
            for (String s1 : packetIn.getPlayers()) {
                scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
            }
        }
        if (packetIn.getAction() == 1) {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }

    public void handleParticles(S2APacketParticles packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        if (packetIn.getParticleCount() == 0) {
            double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            double d4 = packetIn.getParticleSpeed() * packetIn.getZOffset();
            try {
                this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
            }
            catch (Throwable var17) {
                logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
            }
        } else {
            for (int i = 0; i < packetIn.getParticleCount(); ++i) {
                double d1 = this.avRandomizer.nextGaussian() * (double)packetIn.getXOffset();
                double d3 = this.avRandomizer.nextGaussian() * (double)packetIn.getYOffset();
                double d5 = this.avRandomizer.nextGaussian() * (double)packetIn.getZOffset();
                double d6 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d7 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d8 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                try {
                    this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
                    continue;
                }
                catch (Throwable var16) {
                    logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
                    return;
                }
            }
        }
    }

    public void handleEntityProperties(S20PacketEntityProperties packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
            for (S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : packetIn.func_149441_d()) {
                IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
                if (iattributeinstance == null) {
                    iattributeinstance = baseattributemap.registerAttribute((IAttribute)new RangedAttribute((IAttribute)null, s20packetentityproperties$snapshot.func_151409_a(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
                iattributeinstance.removeAllModifiers();
                for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c()) {
                    iattributeinstance.applyModifier(attributemodifier);
                }
            }
        }
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }

    public NetworkPlayerInfo getPlayerInfo(UUID p_175102_1_) {
        return (NetworkPlayerInfo)this.playerInfoMap.get((Object)p_175102_1_);
    }

    public NetworkPlayerInfo getPlayerInfo(String p_175104_1_) {
        for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
            if (!networkplayerinfo.getGameProfile().getName().equals((Object)p_175104_1_)) continue;
            return networkplayerinfo;
        }
        return null;
    }

    public GameProfile getGameProfile() {
        return this.profile;
    }

    static /* synthetic */ NetworkManager access$000(NetHandlerPlayClient x0) {
        return x0.netManager;
    }

    static /* synthetic */ Minecraft access$102(NetHandlerPlayClient x0, Minecraft x1) {
        x0.gameController = x1;
        return x0.gameController;
    }

    static /* synthetic */ Minecraft access$100(NetHandlerPlayClient x0) {
        return x0.gameController;
    }
}
