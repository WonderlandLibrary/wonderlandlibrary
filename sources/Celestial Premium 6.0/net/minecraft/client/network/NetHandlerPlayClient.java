/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.minecraft.client.network;

import baritone.events.events.baritoneOnly.EventBarChunkPost;
import baritone.events.events.baritoneOnly.EventBarChunkPre;
import baritone.events.events.baritoneOnly.EventBarDeath;
import baritone.events.events.network.EventPostSendPacket;
import baritone.events.events.network.EventSendPacket;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
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
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.toasts.RecipeToast;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.feature.impl.combat.Velocity;
import org.celestial.client.helpers.sound.SoundHelper;

public class NetHandlerPlayClient
implements INetHandlerPlayClient {
    private static final Logger LOGGER = LogManager.getLogger();
    private final NetworkManager netManager;
    private final GameProfile profile;
    private final GuiScreen guiScreenServer;
    private Minecraft gameController;
    public WorldClient clientWorldController;
    private boolean doneLoadingTerrain;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
    public int currentServerMaxPlayers = 20;
    private boolean hasStatistics;
    private final ClientAdvancementManager field_191983_k;
    private final Random avRandomizer = new Random();

    public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager networkManagerIn, GameProfile profileIn) {
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = networkManagerIn;
        this.profile = profileIn;
        this.field_191983_k = new ClientAdvancementManager(mcIn);
    }

    public void cleanup() {
        this.clientWorldController = null;
    }

    @Override
    public void handleJoinGame(SPacketJoinGame packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        this.gameController.player.dimension = packetIn.getDimension();
        this.gameController.displayGuiScreen(new GuiDownloadTerrain());
        this.gameController.player.setEntityId(packetIn.getPlayerId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        this.gameController.player.setReducedDebug(packetIn.isReducedDebugInfo());
        this.gameController.playerController.setGameType(packetIn.getGameType());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }

    @Override
    public void handleSpawnObject(SPacketSpawnObject packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        Entity entity = null;
        if (packetIn.getType() == 10) {
            entity = EntityMinecart.create(this.clientWorldController, d0, d1, d2, EntityMinecart.Type.getById(packetIn.getData()));
        } else if (packetIn.getType() == 90) {
            Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getData());
            if (entity1 instanceof EntityPlayer) {
                entity = new EntityFishHook(this.clientWorldController, (EntityPlayer)entity1, d0, d1, d2);
            }
            packetIn.setData(0);
        } else if (packetIn.getType() == 60) {
            entity = new EntityTippedArrow(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 91) {
            entity = new EntitySpectralArrow(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 61) {
            entity = new EntitySnowball(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 68) {
            entity = new EntityLlamaSpit(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
        } else if (packetIn.getType() == 71) {
            entity = new EntityItemFrame(this.clientWorldController, new BlockPos(d0, d1, d2), EnumFacing.byHorizontalIndex(packetIn.getData()));
            packetIn.setData(0);
        } else if (packetIn.getType() == 77) {
            entity = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2)));
            packetIn.setData(0);
        } else if (packetIn.getType() == 65) {
            entity = new EntityEnderPearl(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 72) {
            entity = new EntityEnderEye(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 76) {
            entity = new EntityFireworkRocket(this.clientWorldController, d0, d1, d2, ItemStack.field_190927_a);
        } else if (packetIn.getType() == 63) {
            entity = new EntityLargeFireball(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        } else if (packetIn.getType() == 93) {
            entity = new EntityDragonFireball(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        } else if (packetIn.getType() == 64) {
            entity = new EntitySmallFireball(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        } else if (packetIn.getType() == 66) {
            entity = new EntityWitherSkull(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        } else if (packetIn.getType() == 67) {
            entity = new EntityShulkerBullet(this.clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        } else if (packetIn.getType() == 62) {
            entity = new EntityEgg(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 79) {
            entity = new EntityEvokerFangs(this.clientWorldController, d0, d1, d2, 0.0f, 0, null);
        } else if (packetIn.getType() == 73) {
            entity = new EntityPotion(this.clientWorldController, d0, d1, d2, ItemStack.field_190927_a);
            packetIn.setData(0);
        } else if (packetIn.getType() == 75) {
            entity = new EntityExpBottle(this.clientWorldController, d0, d1, d2);
            packetIn.setData(0);
        } else if (packetIn.getType() == 1) {
            entity = new EntityBoat(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 50) {
            entity = new EntityTNTPrimed(this.clientWorldController, d0, d1, d2, null);
        } else if (packetIn.getType() == 78) {
            entity = new EntityArmorStand(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 51) {
            entity = new EntityEnderCrystal(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 2) {
            entity = new EntityItem(this.clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 70) {
            entity = new EntityFallingBlock(this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.getData() & 0xFFFF));
            packetIn.setData(0);
        } else if (packetIn.getType() == 3) {
            entity = new EntityAreaEffectCloud(this.clientWorldController, d0, d1, d2);
        }
        if (entity != null) {
            EntityTracker.updateServerPosition(entity, d0, d1, d2);
            entity.rotationPitch = (float)(packetIn.getPitch() * 360) / 256.0f;
            entity.rotationYaw = (float)(packetIn.getYaw() * 360) / 256.0f;
            Entity[] aentity = entity.getParts();
            if (aentity != null) {
                int i = packetIn.getEntityID() - entity.getEntityId();
                for (Entity entity2 : aentity) {
                    entity2.setEntityId(entity2.getEntityId() + i);
                }
            }
            entity.setEntityId(packetIn.getEntityID());
            entity.setUniqueId(packetIn.getUniqueId());
            this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
            if (packetIn.getData() > 0) {
                Entity entity3;
                if ((packetIn.getType() == 60 || packetIn.getType() == 91) && (entity3 = this.clientWorldController.getEntityByID(packetIn.getData() - 1)) instanceof EntityLivingBase && entity instanceof EntityArrow) {
                    ((EntityArrow)entity).shootingEntity = entity3;
                }
                entity.setVelocity((double)packetIn.getSpeedX() / 8000.0, (double)packetIn.getSpeedY() / 8000.0, (double)packetIn.getSpeedZ() / 8000.0);
            }
        }
    }

    @Override
    public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        EntityXPOrb entity = new EntityXPOrb(this.clientWorldController, d0, d1, d2, packetIn.getXPValue());
        EntityTracker.updateServerPosition(entity, d0, d1, d2);
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.setEntityId(packetIn.getEntityID());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
    }

    @Override
    public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        EntityLightningBolt entity = null;
        if (packetIn.getType() == 1) {
            entity = new EntityLightningBolt(this.clientWorldController, d0, d1, d2, false);
        }
        if (entity != null) {
            EntityTracker.updateServerPosition(entity, d0, d1, d2);
            entity.rotationYaw = 0.0f;
            entity.rotationPitch = 0.0f;
            entity.setEntityId(packetIn.getEntityId());
            this.clientWorldController.addWeatherEffect(entity);
        }
    }

    @Override
    public void handleSpawnPainting(SPacketSpawnPainting packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPainting entitypainting = new EntityPainting(this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        entitypainting.setUniqueId(packetIn.getUniqueId());
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
    }

    @Override
    public void handleEntityVelocity(SPacketEntityVelocity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (Velocity.velocityMode.currentMode.equals("Matrix") && Celestial.instance.featureManager.getFeatureByClass(Velocity.class).getState()) {
                if (packetIn.getEntityID() == Minecraft.getMinecraft().player.getEntityId()) {
                    entity.setVelocity(0.0, (double)packetIn.getMotionY() / 8000.0, (double)packetIn.getMotionZ() / 8000.0 / 2.5);
                    Minecraft.getMinecraft().player.onGround = true;
                }
            } else {
                entity.setVelocity((double)packetIn.getMotionX() / 8000.0, (double)packetIn.getMotionY() / 8000.0, (double)packetIn.getMotionZ() / 8000.0);
            }
        }
    }

    @Override
    public void handleEntityMetadata(SPacketEntityMetadata packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null && packetIn.getDataManagerEntries() != null) {
            entity.getDataManager().setEntryValues(packetIn.getDataManagerEntries());
        }
    }

    @Override
    public void handleSpawnPlayer(SPacketSpawnPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = (float)(packetIn.getYaw() * 360) / 256.0f;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.gameController.world, this.getPlayerInfo(packetIn.getUniqueId()).getGameProfile());
        entityotherplayermp.prevPosX = d0;
        entityotherplayermp.lastTickPosX = d0;
        entityotherplayermp.prevPosY = d1;
        entityotherplayermp.lastTickPosY = d1;
        entityotherplayermp.prevPosZ = d2;
        entityotherplayermp.lastTickPosZ = d2;
        EntityTracker.updateServerPosition(entityotherplayermp, d0, d1, d2);
        entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
        this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
        List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
        if (list != null) {
            entityotherplayermp.getDataManager().setEntryValues(list);
        }
    }

    @Override
    public void handleEntityTeleport(SPacketEntityTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            double d0 = packetIn.getX();
            double d1 = packetIn.getY();
            double d2 = packetIn.getZ();
            EntityTracker.updateServerPosition(entity, d0, d1, d2);
            if (!entity.canPassengerSteer()) {
                float f = (float)(packetIn.getYaw() * 360) / 256.0f;
                float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
                if (Math.abs(entity.posX - d0) < 0.03125 && Math.abs(entity.posY - d1) < 0.015625 && Math.abs(entity.posZ - d2) < 0.03125) {
                    entity.setPositionAndRotationDirect(entity.posX, entity.posY, entity.posZ, f, f1, 0, true);
                } else {
                    entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, true);
                }
                entity.onGround = packetIn.getOnGround();
            }
        }
    }

    @Override
    public void handleHeldItemChange(SPacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (InventoryPlayer.isHotbar(packetIn.getHeldItemHotbarIndex())) {
            this.gameController.player.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }

    @Override
    public void handleEntityMovement(SPacketEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            entity.serverPosX += (long)packetIn.getX();
            entity.serverPosY += (long)packetIn.getY();
            entity.serverPosZ += (long)packetIn.getZ();
            double d0 = (double)entity.serverPosX / 4096.0;
            double d1 = (double)entity.serverPosY / 4096.0;
            double d2 = (double)entity.serverPosZ / 4096.0;
            if (!entity.canPassengerSteer()) {
                float f = packetIn.isRotating() ? (float)(packetIn.getYaw() * 360) / 256.0f : entity.rotationYaw;
                float f1 = packetIn.isRotating() ? (float)(packetIn.getPitch() * 360) / 256.0f : entity.rotationPitch;
                entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, false);
                entity.onGround = packetIn.getOnGround();
            }
        }
    }

    @Override
    public void handleEntityHeadLook(SPacketEntityHeadLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            float f = (float)(packetIn.getYaw() * 360) / 256.0f;
            entity.setRotationYawHead(f);
        }
    }

    @Override
    public void handleDestroyEntities(SPacketDestroyEntities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
            this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }

    @Override
    public void handlePlayerPosLook(SPacketPlayerPosLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer = this.gameController.player;
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f1 = packetIn.getPitch();
        if (packetIn.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.X)) {
            d0 += entityplayer.posX;
        } else {
            entityplayer.motionX = 0.0;
        }
        if (packetIn.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Y)) {
            d1 += entityplayer.posY;
        } else {
            entityplayer.motionY = 0.0;
        }
        if (packetIn.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Z)) {
            d2 += entityplayer.posZ;
        } else {
            entityplayer.motionZ = 0.0;
        }
        if (packetIn.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f1 += entityplayer.rotationPitch;
        }
        if (packetIn.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityplayer.rotationYaw;
        }
        entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
        this.netManager.sendPacket(new CPacketConfirmTeleport(packetIn.getTeleportId()));
        this.netManager.sendPacket(new CPacketPlayer.PositionRotation(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            this.gameController.player.prevPosX = this.gameController.player.posX;
            this.gameController.player.prevPosY = this.gameController.player.posY;
            this.gameController.player.prevPosZ = this.gameController.player.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen(null);
        }
    }

    @Override
    public void handleMultiBlockChange(SPacketMultiBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        for (SPacketMultiBlockChange.BlockUpdateData spacketmultiblockchange$blockupdatedata : packetIn.getChangedBlocks()) {
            this.clientWorldController.invalidateRegionAndSetBlock(spacketmultiblockchange$blockupdatedata.getPos(), spacketmultiblockchange$blockupdatedata.getBlockState());
        }
    }

    @Override
    public void handleChunkData(SPacketChunkData packetIn) {
        EventManager.call(new EventBarChunkPre(packetIn));
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.doChunkLoad()) {
            this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }
        this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        Chunk chunk = this.clientWorldController.getChunk(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.fillChunk(packetIn.getReadBuffer(), packetIn.getExtractedSize(), packetIn.doChunkLoad());
        this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        if (!packetIn.doChunkLoad() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
        for (NBTTagCompound nbttagcompound : packetIn.getTileEntityTags()) {
            BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
            TileEntity tileentity = this.clientWorldController.getTileEntity(blockpos);
            if (tileentity == null) continue;
            tileentity.readFromNBT(nbttagcompound);
        }
        EventManager.call(new EventBarChunkPost(packetIn));
    }

    @Override
    public void processChunkUnload(SPacketUnloadChunk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.clientWorldController.doPreChunk(packetIn.getX(), packetIn.getZ(), false);
    }

    @Override
    public void handleBlockChange(SPacketBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }

    @Override
    public void handleDisconnect(SPacketDisconnect packetIn) {
        this.netManager.closeChannel(packetIn.getReason());
    }

    @Override
    public void onDisconnect(ITextComponent reason) {
        this.gameController.loadWorld(null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.gameController.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).getProxy(), "disconnect.lost", reason).getProxy());
            } else {
                this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        } else {
            this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }

    public void sendPacket(Packet<?> packetIn) {
        EventSendPacket event = new EventSendPacket(packetIn);
        EventManager.call(event);
        if (!event.isCancelled()) {
            this.netManager.sendPacket(packetIn);
        }
        EventPostSendPacket eventPost = new EventPostSendPacket(event.getPacket());
        EventManager.call(eventPost);
    }

    @Override
    public void handleCollectItem(SPacketCollectItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entitylivingbase == null) {
            entitylivingbase = this.gameController.player;
        }
        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1f, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.35f + 0.9f, false);
            } else {
                this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 1.4f + 2.0f, false);
            }
            if (entity instanceof EntityItem) {
                ((EntityItem)entity).getItem().func_190920_e(packetIn.func_191208_c());
            }
            this.gameController.effectRenderer.addEffect(new ParticleItemPickup((World)this.clientWorldController, entity, entitylivingbase, 0.5f));
            this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }

    @Override
    public void handleChat(SPacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.ingameGUI.func_191742_a(packetIn.func_192590_c(), packetIn.getChatComponent());
    }

    @Override
    public void handleAnimation(SPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingArm(EnumHand.MAIN_HAND);
            } else if (packetIn.getAnimationType() == 3) {
                EntityLivingBase entitylivingbase1 = (EntityLivingBase)entity;
                entitylivingbase1.swingArm(EnumHand.OFF_HAND);
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

    @Override
    public void handleUseBed(SPacketUseBed packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.getBedPosition());
    }

    @Override
    public void handleSpawnMob(SPacketSpawnMob packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = (float)(packetIn.getYaw() * 360) / 256.0f;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0f;
        EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), this.gameController.world);
        if (entitylivingbase != null) {
            EntityTracker.updateServerPosition(entitylivingbase, d0, d1, d2);
            entitylivingbase.renderYawOffset = (float)(packetIn.getHeadPitch() * 360) / 256.0f;
            entitylivingbase.rotationYawHead = (float)(packetIn.getHeadPitch() * 360) / 256.0f;
            Entity[] aentity = entitylivingbase.getParts();
            if (aentity != null) {
                int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
                for (Entity entity : aentity) {
                    entity.setEntityId(entity.getEntityId() + i);
                }
            }
            entitylivingbase.setEntityId(packetIn.getEntityID());
            entitylivingbase.setUniqueId(packetIn.getUniqueId());
            entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
            entitylivingbase.motionX = (float)packetIn.getVelocityX() / 8000.0f;
            entitylivingbase.motionY = (float)packetIn.getVelocityY() / 8000.0f;
            entitylivingbase.motionZ = (float)packetIn.getVelocityZ() / 8000.0f;
            this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
            List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
            if (list != null) {
                entitylivingbase.getDataManager().setEntryValues(list);
            }
        } else {
            LOGGER.warn("Skipping Entity with id {}", packetIn.getEntityType());
        }
    }

    @Override
    public void handleTimeUpdate(SPacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.world.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.world.setWorldTime(packetIn.getWorldTime());
    }

    @Override
    public void handleSpawnPosition(SPacketSpawnPosition packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.player.setSpawnPoint(packetIn.getSpawnPos(), true);
        this.gameController.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }

    @Override
    public void handleSetPassengers(SPacketSetPassengers packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity == null) {
            LOGGER.warn("Received passengers for unknown entity");
        } else {
            boolean flag = entity.isRidingOrBeingRiddenBy(this.gameController.player);
            entity.removePassengers();
            for (int i : packetIn.getPassengerIds()) {
                Entity entity1 = this.clientWorldController.getEntityByID(i);
                if (entity1 == null) continue;
                entity1.startRiding(entity, true);
                if (entity1 != this.gameController.player || flag) continue;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(this.gameController.gameSettings.keyBindSneak.getKeyCode())), false);
            }
        }
    }

    @Override
    public void handleEntityAttach(SPacketEntityAttach packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
        if (entity instanceof EntityLiving) {
            if (entity1 != null) {
                ((EntityLiving)entity).setLeashedToEntity(entity1, false);
            } else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }

    @Override
    public void handleEntityStatus(SPacketEntityStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            if (packetIn.getOpCode() == 21) {
                this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            } else if (packetIn.getOpCode() == 35) {
                int i = 40;
                this.gameController.effectRenderer.func_191271_a(entity, EnumParticleTypes.TOTEM, 30);
                this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.field_191263_gW, entity.getSoundCategory(), 1.0f, 1.0f, false);
                if (entity == this.gameController.player) {
                    this.gameController.entityRenderer.func_190565_a(new ItemStack(Items.TOTEM_OF_UNDYING));
                }
            } else {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }

    @Override
    public void handleUpdateHealth(SPacketUpdateHealth packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.player.setPlayerSPHealth(packetIn.getHealth());
        this.gameController.player.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        this.gameController.player.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    @Override
    public void handleSetExperience(SPacketSetExperience packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.player.setXPStats(packetIn.getExperienceBar(), packetIn.getTotalExperience(), packetIn.getLevel());
    }

    @Override
    public void handleRespawn(SPacketRespawn packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.getDimensionID() != this.gameController.player.dimension) {
            this.doneLoadingTerrain = false;
            Scoreboard scoreboard = this.clientWorldController.getScoreboard();
            this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.world.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
            this.clientWorldController.setWorldScoreboard(scoreboard);
            this.gameController.loadWorld(this.clientWorldController);
            this.gameController.player.dimension = packetIn.getDimensionID();
            this.gameController.displayGuiScreen(new GuiDownloadTerrain());
        }
        this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        this.gameController.playerController.setGameType(packetIn.getGameType());
    }

    @Override
    public void handleExplosion(SPacketExplosion packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Explosion explosion = new Explosion(this.gameController.world, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        this.gameController.player.motionX += (double)packetIn.getMotionX();
        this.gameController.player.motionY += (double)packetIn.getMotionY();
        this.gameController.player.motionZ += (double)packetIn.getMotionZ();
    }

    @Override
    public void handleOpenWindow(SPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.player;
        if ("minecraft:container".equals(packetIn.getGuiId())) {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("EntityHorse".equals(packetIn.getGuiId())) {
            Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
            if (entity instanceof AbstractHorse) {
                entityplayersp.openGuiHorseInventory((AbstractHorse)entity, new ContainerHorseChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        } else if (!packetIn.hasSlots()) {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else {
            ContainerLocalMenu iinventory = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(iinventory);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }

    @Override
    public void handleSetSlot(SPacketSetSlot packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer = this.gameController.player;
        ItemStack itemstack = packetIn.getStack();
        int i = packetIn.getSlot();
        this.gameController.func_193032_ao().func_193301_a(itemstack);
        if (packetIn.getWindowId() == -1) {
            entityplayer.inventory.setItemStack(itemstack);
        } else if (packetIn.getWindowId() == -2) {
            entityplayer.inventory.setInventorySlotContents(i, itemstack);
        } else {
            boolean flag = false;
            if (this.gameController.currentScreen instanceof GuiContainerCreative) {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
                boolean bl = flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex();
            }
            if (packetIn.getWindowId() == 0 && packetIn.getSlot() >= 36 && i < 45) {
                ItemStack itemstack1;
                if (!itemstack.isEmpty() && ((itemstack1 = entityplayer.inventoryContainer.getSlot(i).getStack()).isEmpty() || itemstack1.getCount() < itemstack.getCount())) {
                    itemstack.func_190915_d(5);
                }
                entityplayer.inventoryContainer.putStackInSlot(i, itemstack);
            } else if (!(packetIn.getWindowId() != entityplayer.openContainer.windowId || packetIn.getWindowId() == 0 && flag)) {
                entityplayer.openContainer.putStackInSlot(i, itemstack);
            }
        }
    }

    @Override
    public void handleConfirmTransaction(SPacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Container container = null;
        EntityPlayerSP entityplayer = this.gameController.player;
        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }
        if (container != null && !packetIn.wasAccepted()) {
            this.sendPacket(new CPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }

    @Override
    public void handleWindowItems(SPacketWindowItems packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer = this.gameController.player;
        if (packetIn.getWindowId() == 0) {
            entityplayer.inventoryContainer.func_190896_a(packetIn.getItemStacks());
        } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            entityplayer.openContainer.func_190896_a(packetIn.getItemStacks());
        }
    }

    @Override
    public void handleSignEditorOpen(SPacketSignEditorOpen packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
        if (!(tileentity instanceof TileEntitySign)) {
            tileentity = new TileEntitySign();
            tileentity.setWorldObj(this.clientWorldController);
            tileentity.setPos(packetIn.getSignPosition());
        }
        this.gameController.player.openEditSign((TileEntitySign)tileentity);
    }

    @Override
    public void handleUpdateTileEntity(SPacketUpdateTileEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (this.gameController.world.isBlockLoaded(packetIn.getPos())) {
            boolean flag;
            TileEntity tileentity = this.gameController.world.getTileEntity(packetIn.getPos());
            int i = packetIn.getTileEntityType();
            boolean bl = flag = i == 2 && tileentity instanceof TileEntityCommandBlock;
            if (i == 1 && tileentity instanceof TileEntityMobSpawner || flag || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner || i == 7 && tileentity instanceof TileEntityStructure || i == 8 && tileentity instanceof TileEntityEndGateway || i == 9 && tileentity instanceof TileEntitySign || i == 10 && tileentity instanceof TileEntityShulkerBox || i == 11 && tileentity instanceof TileEntityBed) {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
            if (flag && this.gameController.currentScreen instanceof GuiCommandBlock) {
                ((GuiCommandBlock)this.gameController.currentScreen).updateGui();
            }
        }
    }

    @Override
    public void handleWindowProperty(SPacketWindowProperty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer = this.gameController.player;
        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
            entityplayer.openContainer.updateProgressBar(packetIn.getProperty(), packetIn.getValue());
        }
    }

    @Override
    public void handleEntityEquipment(SPacketEntityEquipment packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setItemStackToSlot(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }

    @Override
    public void handleCloseWindow(SPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.player.closeScreenAndDropStack();
    }

    @Override
    public void handleBlockAction(SPacketBlockAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.world.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }

    @Override
    public void handleBlockBreakAnim(SPacketBlockBreakAnim packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }

    @Override
    public void handleChangeGameState(SPacketChangeGameState packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer = this.gameController.player;
        int i = packetIn.getGameState();
        float f = packetIn.getValue();
        int j = MathHelper.floor(f + 0.5f);
        if (i >= 0 && i < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[i] != null) {
            ((EntityPlayer)entityplayer).addChatComponentMessage(new TextComponentTranslation(SPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]), false);
        }
        if (i == 1) {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0f);
        } else if (i == 2) {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0f);
        } else if (i == 3) {
            this.gameController.playerController.setGameType(GameType.getByID(j));
        } else if (i == 4) {
            if (j == 0) {
                this.gameController.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
                this.gameController.displayGuiScreen(new GuiDownloadTerrain());
            } else if (j == 1) {
                this.gameController.displayGuiScreen(new GuiWinGame(true, () -> this.gameController.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN))));
            }
        } else if (i == 5) {
            GameSettings gamesettings = this.gameController.gameSettings;
            if (f == 0.0f) {
                this.gameController.displayGuiScreen(new GuiScreenDemo());
            } else if (f == 101.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.movement", GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())));
            } else if (f == 102.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.jump", GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())));
            } else if (f == 103.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.inventory", GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())));
            }
        } else if (i == 6) {
            this.clientWorldController.playSound(entityplayer, entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18f, 0.45f);
        } else if (i == 7) {
            this.clientWorldController.setRainStrength(f);
        } else if (i == 8) {
            this.clientWorldController.setThunderStrength(f);
        } else if (i == 10) {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.clientWorldController.playSound(entityplayer, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }

    @Override
    public void handleMaps(SPacketMaps packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        MapItemRenderer mapitemrenderer = this.gameController.entityRenderer.getMapItemRenderer();
        MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.world);
        if (mapdata == null) {
            MapData mapdata1;
            String s = "map_" + packetIn.getMapId();
            mapdata = new MapData(s);
            if (mapitemrenderer.func_191205_a(s) != null && (mapdata1 = mapitemrenderer.func_191207_a(mapitemrenderer.func_191205_a(s))) != null) {
                mapdata = mapdata1;
            }
            this.gameController.world.setItemData(s, mapdata);
        }
        packetIn.setMapdataTo(mapdata);
        mapitemrenderer.updateMapTexture(mapdata);
    }

    @Override
    public void handleEffect(SPacketEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.isSoundServerwide()) {
            this.gameController.world.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        } else {
            this.gameController.world.playEvent(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }

    @Override
    public void func_191981_a(SPacketAdvancementInfo p_191981_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_191981_1_, this, this.gameController);
        this.field_191983_k.func_192799_a(p_191981_1_);
    }

    @Override
    public void func_194022_a(SPacketSelectAdvancementsTab p_194022_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_194022_1_, this, this.gameController);
        ResourceLocation resourcelocation = p_194022_1_.func_194154_a();
        if (resourcelocation == null) {
            this.field_191983_k.func_194230_a(null, false);
        } else {
            Advancement advancement = this.field_191983_k.func_194229_a().func_192084_a(resourcelocation);
            this.field_191983_k.func_194230_a(advancement, false);
        }
    }

    @Override
    public void handleStatistics(SPacketStatistics packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        for (Map.Entry<StatBase, Integer> entry : packetIn.getStatisticMap().entrySet()) {
            StatBase statbase = entry.getKey();
            int k = entry.getValue();
            this.gameController.player.getStatFileWriter().unlockAchievement(this.gameController.player, statbase, k);
        }
        this.hasStatistics = true;
        if (this.gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)((Object)this.gameController.currentScreen)).func_193026_g();
        }
    }

    @Override
    public void func_191980_a(SPacketRecipeBook p_191980_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_191980_1_, this, this.gameController);
        RecipeBook recipebook = this.gameController.player.func_192035_E();
        recipebook.func_192813_a(p_191980_1_.func_192593_c());
        recipebook.func_192810_b(p_191980_1_.func_192594_d());
        SPacketRecipeBook.State spacketrecipebook$state = p_191980_1_.func_194151_e();
        switch (spacketrecipebook$state) {
            case REMOVE: {
                for (IRecipe irecipe : p_191980_1_.func_192595_a()) {
                    recipebook.func_193831_b(irecipe);
                }
                break;
            }
            case INIT: {
                p_191980_1_.func_192595_a().forEach(recipebook::func_194073_a);
                p_191980_1_.func_193644_b().forEach(recipebook::func_193825_e);
                break;
            }
            case ADD: {
                p_191980_1_.func_192595_a().forEach(p_194025_2_ -> {
                    recipebook.func_194073_a((IRecipe)p_194025_2_);
                    recipebook.func_193825_e((IRecipe)p_194025_2_);
                    RecipeToast.func_193665_a(this.gameController.getToastGui(), p_194025_2_);
                });
            }
        }
        RecipeBookClient.field_194087_f.forEach(p_194023_1_ -> p_194023_1_.func_194214_a(recipebook));
        if (this.gameController.currentScreen instanceof IRecipeShownListener) {
            ((IRecipeShownListener)((Object)this.gameController.currentScreen)).func_192043_J_();
        }
    }

    @Override
    public void handleEntityEffect(SPacketEntityEffect packetIn) {
        Potion potion;
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase && (potion = Potion.getPotionById(packetIn.getEffectId())) != null) {
            PotionEffect potioneffect = new PotionEffect(potion, packetIn.getDuration(), packetIn.getAmplifier(), packetIn.getIsAmbient(), packetIn.doesShowParticles());
            potioneffect.setPotionDurationMax(packetIn.isMaxDuration());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
        }
    }

    @Override
    public void handleCombatEvent(SPacketCombatEvent packetIn) {
        Entity entity;
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.eventType == SPacketCombatEvent.Event.ENTITY_DIED && (entity = this.clientWorldController.getEntityByID(packetIn.playerId)) == this.gameController.player) {
            EventManager.call(new EventBarDeath());
            this.gameController.displayGuiScreen(new GuiGameOver(packetIn.deathMessage));
            SoundHelper.playSound("wasted.wav", -25.0f, false);
        }
    }

    @Override
    public void handleServerDifficulty(SPacketServerDifficulty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        this.gameController.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }

    @Override
    public void handleCamera(SPacketCamera packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity != null) {
            this.gameController.setRenderViewEntity(entity);
        }
    }

    @Override
    public void handleWorldBorder(SPacketWorldBorder packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        packetIn.apply(this.clientWorldController.getWorldBorder());
    }

    @Override
    public void handleTitle(SPacketTitle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        SPacketTitle.Type spackettitle$type = packetIn.getType();
        String s = null;
        String s1 = null;
        String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";
        switch (spackettitle$type) {
            case TITLE: {
                s = s2;
                break;
            }
            case SUBTITLE: {
                s1 = s2;
                break;
            }
            case ACTIONBAR: {
                this.gameController.ingameGUI.setRecordPlaying(s2, false);
                return;
            }
            case RESET: {
                this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
                this.gameController.ingameGUI.setDefaultTitlesTimes();
                return;
            }
        }
        this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }

    @Override
    public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter packetIn) {
        this.gameController.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().isEmpty() ? null : packetIn.getHeader());
        this.gameController.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().isEmpty() ? null : packetIn.getFooter());
    }

    @Override
    public void handleRemoveEntityEffect(SPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.getEntity(this.clientWorldController);
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removeActivePotionEffect(packetIn.getPotion());
        }
    }

    @Override
    public void handlePlayerListItem(SPacketPlayerListItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        for (SPacketPlayerListItem.AddPlayerData spacketplayerlistitem$addplayerdata : packetIn.getEntries()) {
            if (packetIn.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(spacketplayerlistitem$addplayerdata.getProfile().getId());
                continue;
            }
            NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(spacketplayerlistitem$addplayerdata.getProfile().getId());
            if (packetIn.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                networkplayerinfo = new NetworkPlayerInfo(spacketplayerlistitem$addplayerdata);
                this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
            }
            if (networkplayerinfo == null) continue;
            switch (packetIn.getAction()) {
                case ADD_PLAYER: {
                    networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                    networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case UPDATE_GAME_MODE: {
                    networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                    break;
                }
                case UPDATE_LATENCY: {
                    networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    networkplayerinfo.setDisplayName(spacketplayerlistitem$addplayerdata.getDisplayName());
                }
            }
        }
    }

    @Override
    public void handleKeepAlive(SPacketKeepAlive packetIn) {
        this.sendPacket(new CPacketKeepAlive(packetIn.getId()));
    }

    @Override
    public void handlePlayerAbilities(SPacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayer1 = this.gameController.player;
        entityplayer1.capabilities.isFlying = packetIn.isFlying();
        entityplayer1.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayer1.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayer1.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayer1.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer1.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }

    @Override
    public void handleTabComplete(SPacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        String[] astring = packetIn.getMatches();
        Arrays.sort(astring);
        if (this.gameController.currentScreen instanceof ITabCompleter) {
            ((ITabCompleter)((Object)this.gameController.currentScreen)).setCompletions(astring);
        }
    }

    @Override
    public void handleSoundEffect(SPacketSoundEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.world.playSound(this.gameController.player, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSound(), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch());
    }

    @Override
    public void handleCustomSound(SPacketCustomSound packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(packetIn.getSoundName()), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch(), false, 0, ISound.AttenuationType.LINEAR, (float)packetIn.getX(), (float)packetIn.getY(), (float)packetIn.getZ()));
    }

    @Override
    public void handleResourcePack(SPacketResourcePackSend packetIn) {
        final String s = packetIn.getURL();
        final String s1 = packetIn.getHash();
        if (this.validateResourcePackUrl(s)) {
            if (s.startsWith("level://")) {
                try {
                    String s2 = URLDecoder.decode(s.substring("level://".length()), StandardCharsets.UTF_8.toString());
                    File file1 = new File(this.gameController.gameDir, "saves");
                    File file2 = new File(file1, s2);
                    if (file2.isFile()) {
                        this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                        Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), this.createDownloadCallback());
                        return;
                    }
                }
                catch (UnsupportedEncodingException s2) {
                    // empty catch block
                }
                this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            } else {
                ServerData serverdata = this.gameController.getCurrentServerData();
                if (serverdata != null && serverdata.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
                    this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                    Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), this.createDownloadCallback());
                } else if (serverdata != null && serverdata.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
                    this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                } else {
                    this.gameController.addScheduledTask(new Runnable(){

                        @Override
                        public void run() {
                            NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(){

                                @Override
                                public void confirmClicked(boolean result, int id) {
                                    NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
                                    ServerData serverdata1 = NetHandlerPlayClient.this.gameController.getCurrentServerData();
                                    if (result) {
                                        if (serverdata1 != null) {
                                            serverdata1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                        }
                                        NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                                        Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), NetHandlerPlayClient.this.createDownloadCallback());
                                    } else {
                                        if (serverdata1 != null) {
                                            serverdata1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                        }
                                        NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                                    }
                                    ServerList.saveSingleServer(serverdata1);
                                    NetHandlerPlayClient.this.gameController.displayGuiScreen(null);
                                }
                            }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                        }
                    });
                }
            }
        }
    }

    private boolean validateResourcePackUrl(String p_189688_1_) {
        try {
            URI uri = new URI(p_189688_1_);
            String s = uri.getScheme();
            boolean flag = "level".equals(s);
            if (!("http".equals(s) || "https".equals(s) || flag)) {
                throw new URISyntaxException(p_189688_1_, "Wrong protocol");
            }
            if (!flag || !p_189688_1_.contains("..") && p_189688_1_.endsWith("/resources.zip")) {
                return true;
            }
            throw new URISyntaxException(p_189688_1_, "Invalid levelstorage resourcepack path");
        }
        catch (URISyntaxException var5) {
            this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            return false;
        }
    }

    private FutureCallback<Object> createDownloadCallback() {
        return new FutureCallback<Object>(){

            @Override
            public void onSuccess(@Nullable Object p_onSuccess_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }

            @Override
            public void onFailure(Throwable p_onFailure_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        };
    }

    @Override
    public void handleUpdateEntityNBT(SPacketUpdateBossInfo packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.ingameGUI.getBossOverlay().read(packetIn);
    }

    @Override
    public void handleCooldown(SPacketCooldown packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.getTicks() == 0) {
            this.gameController.player.getCooldownTracker().removeCooldown(packetIn.getItem());
        } else {
            this.gameController.player.getCooldownTracker().setCooldown(packetIn.getItem(), packetIn.getTicks());
        }
    }

    @Override
    public void handleMoveVehicle(SPacketMoveVehicle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.gameController.player.getLowestRidingEntity();
        if (entity != this.gameController.player && entity.canPassengerSteer()) {
            entity.setPositionAndRotation(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getYaw(), packetIn.getPitch());
            this.netManager.sendPacket(new CPacketVehicleMove(entity));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void handleCustomPayload(SPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if ("MC|TrList".equals(packetIn.getChannelName())) {
            PacketBuffer packetbuffer = packetIn.getBufferData();
            try {
                int k = packetbuffer.readInt();
                GuiScreen guiscreen = this.gameController.currentScreen;
                if (guiscreen == null || !(guiscreen instanceof GuiMerchant) || k != this.gameController.player.openContainer.windowId) return;
                IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                imerchant.setRecipes(merchantrecipelist);
                return;
            }
            catch (IOException ioexception) {
                LOGGER.error("Couldn't load trade info", (Throwable)ioexception);
                return;
            }
            finally {
                packetbuffer.release();
            }
        } else if ("MC|Brand".equals(packetIn.getChannelName())) {
            this.gameController.player.setServerBrand(packetIn.getBufferData().readStringFromBuffer(32767));
            return;
        } else if ("MC|BOpen".equals(packetIn.getChannelName())) {
            ItemStack itemstack;
            EnumHand enumhand = packetIn.getBufferData().readEnumValue(EnumHand.class);
            ItemStack itemStack = itemstack = enumhand == EnumHand.OFF_HAND ? this.gameController.player.getHeldItemOffhand() : this.gameController.player.getHeldItemMainhand();
            if (itemstack.getItem() != Items.WRITTEN_BOOK) return;
            this.gameController.displayGuiScreen(new GuiScreenBook(this.gameController.player, itemstack, false));
            return;
        } else if ("MC|DebugPath".equals(packetIn.getChannelName())) {
            PacketBuffer packetbuffer1 = packetIn.getBufferData();
            int l = packetbuffer1.readInt();
            float f1 = packetbuffer1.readFloat();
            Path path = Path.read(packetbuffer1);
            ((DebugRendererPathfinding)this.gameController.debugRenderer.debugRendererPathfinding).addPath(l, path, f1);
            return;
        } else if ("MC|DebugNeighborsUpdate".equals(packetIn.getChannelName())) {
            PacketBuffer packetbuffer2 = packetIn.getBufferData();
            long i1 = packetbuffer2.readVarLong();
            BlockPos blockpos = packetbuffer2.readBlockPos();
            ((DebugRendererNeighborsUpdate)this.gameController.debugRenderer.field_191557_f).func_191553_a(i1, blockpos);
            return;
        } else {
            if (!"MC|StopSound".equals(packetIn.getChannelName())) return;
            PacketBuffer packetbuffer3 = packetIn.getBufferData();
            String s = packetbuffer3.readStringFromBuffer(32767);
            String s1 = packetbuffer3.readStringFromBuffer(256);
            this.gameController.getSoundHandler().stop(s1, SoundCategory.getByName(s));
        }
    }

    @Override
    public void handleScoreboardObjective(SPacketScoreboardObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.getAction() == 0) {
            ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.getObjectiveName(), IScoreCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.getObjectiveValue());
            scoreobjective.setRenderType(packetIn.getRenderType());
        } else {
            ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.getObjectiveName());
            if (packetIn.getAction() == 1) {
                scoreboard.removeObjective(scoreobjective1);
            } else if (packetIn.getAction() == 2) {
                scoreobjective1.setDisplayName(packetIn.getObjectiveValue());
                scoreobjective1.setRenderType(packetIn.getRenderType());
            }
        }
    }

    @Override
    public void handleUpdateScore(SPacketUpdateScore packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
        if (packetIn.getScoreAction() == SPacketUpdateScore.Action.CHANGE) {
            Score score = scoreboard.getOrCreateScore(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        } else if (packetIn.getScoreAction() == SPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
            } else if (scoreobjective != null) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }

    @Override
    public void handleDisplayObjective(SPacketDisplayObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        if (packetIn.getName().isEmpty()) {
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), null);
        } else {
            ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getName());
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), scoreobjective);
        }
    }

    @Override
    public void handleTeams(SPacketTeams packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam = packetIn.getAction() == 0 ? scoreboard.createTeam(packetIn.getName()) : scoreboard.getTeam(packetIn.getName());
        if (packetIn.getAction() == 0 || packetIn.getAction() == 2) {
            Team.CollisionRule team$collisionrule;
            scoreplayerteam.setTeamName(packetIn.getDisplayName());
            scoreplayerteam.setNamePrefix(packetIn.getPrefix());
            scoreplayerteam.setNameSuffix(packetIn.getSuffix());
            scoreplayerteam.setChatFormat(TextFormatting.fromColorIndex(packetIn.getColor()));
            scoreplayerteam.setFriendlyFlags(packetIn.getFriendlyFlags());
            Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(packetIn.getNameTagVisibility());
            if (team$enumvisible != null) {
                scoreplayerteam.setNameTagVisibility(team$enumvisible);
            }
            if ((team$collisionrule = Team.CollisionRule.getByName(packetIn.getCollisionRule())) != null) {
                scoreplayerteam.setCollisionRule(team$collisionrule);
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

    @Override
    public void handleParticles(SPacketParticles packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        if (packetIn.getParticleCount() == 0) {
            double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            double d4 = packetIn.getParticleSpeed() * packetIn.getZOffset();
            try {
                this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
            }
            catch (Throwable var17) {
                LOGGER.warn("Could not spawn particle effect {}", new Object[]{packetIn.getParticleType()});
            }
        } else {
            for (int k = 0; k < packetIn.getParticleCount(); ++k) {
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
                    LOGGER.warn("Could not spawn particle effect {}", new Object[]{packetIn.getParticleType()});
                    return;
                }
            }
        }
    }

    @Override
    public void handleEntityProperties(SPacketEntityProperties packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            AbstractAttributeMap abstractattributemap = ((EntityLivingBase)entity).getAttributeMap();
            for (SPacketEntityProperties.Snapshot spacketentityproperties$snapshot : packetIn.getSnapshots()) {
                IAttributeInstance iattributeinstance = abstractattributemap.getAttributeInstanceByName(spacketentityproperties$snapshot.getName());
                if (iattributeinstance == null) {
                    iattributeinstance = abstractattributemap.registerAttribute(new RangedAttribute(null, spacketentityproperties$snapshot.getName(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iattributeinstance.setBaseValue(spacketentityproperties$snapshot.getBaseValue());
                iattributeinstance.removeAllModifiers();
                for (AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers()) {
                    iattributeinstance.applyModifier(attributemodifier);
                }
            }
        }
    }

    @Override
    public void func_194307_a(SPacketPlaceGhostRecipe p_194307_1_) {
        PacketThreadUtil.checkThreadAndEnqueue(p_194307_1_, this, this.gameController);
        Container container = this.gameController.player.openContainer;
        if (container.windowId == p_194307_1_.func_194313_b() && container.getCanCraft(this.gameController.player) && this.gameController.currentScreen instanceof IRecipeShownListener) {
            GuiRecipeBook guirecipebook = ((IRecipeShownListener)((Object)this.gameController.currentScreen)).func_194310_f();
            guirecipebook.func_193951_a(p_194307_1_.func_194311_a(), container.inventorySlots);
        }
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }

    public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        return this.playerInfoMap.get(uniqueId);
    }

    @Nullable
    public NetworkPlayerInfo getPlayerInfo(String name) {
        for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
            if (!networkplayerinfo.getGameProfile().getName().equals(name)) continue;
            return networkplayerinfo;
        }
        return null;
    }

    public GameProfile getGameProfile() {
        return this.profile;
    }

    public ClientAdvancementManager func_191982_f() {
        return this.field_191983_k;
    }
}

