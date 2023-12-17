// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.recipebook.RecipeList;
import java.util.Collection;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.StringUtils;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.pathfinding.Path;
import net.minecraft.client.gui.GuiScreenBook;
import java.io.IOException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import javax.annotation.Nullable;
import java.net.URISyntaxException;
import java.net.URI;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import java.io.UnsupportedEncodingException;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ITabCompleter;
import java.util.Arrays;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketKeepAlive;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventPlayer;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.StaffInfo;
import ru.tuskevich.Minced;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.gui.toasts.RecipeToast;
import java.util.function.Consumer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.stats.StatBase;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.world.GameType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.world.IInteractionObject;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.world.Explosion;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.init.Items;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.entity.EntityList;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.datasync.EntityDataManager;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SPacketJoinGame;
import com.google.common.collect.Maps;
import java.util.Random;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.play.INetHandlerPlayClient;

public class NetHandlerPlayClient implements INetHandlerPlayClient
{
    private static final Logger LOGGER;
    private final NetworkManager netManager;
    private final GameProfile profile;
    private final GuiScreen guiScreenServer;
    private Minecraft client;
    private WorldClient world;
    private boolean doneLoadingTerrain;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap;
    public int currentServerMaxPlayers;
    private boolean hasStatistics;
    private final ClientAdvancementManager advancementManager;
    private final Random avRandomizer;
    
    public NetHandlerPlayClient(final Minecraft mcIn, final GuiScreen p_i46300_2_, final NetworkManager networkManagerIn, final GameProfile profileIn) {
        this.playerInfoMap = (Map<UUID, NetworkPlayerInfo>)Maps.newHashMap();
        this.currentServerMaxPlayers = 20;
        this.avRandomizer = new Random();
        this.client = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = networkManagerIn;
        this.profile = profileIn;
        this.advancementManager = new ClientAdvancementManager(mcIn);
    }
    
    public void cleanup() {
        this.world = null;
    }
    
    @Override
    public void handleJoinGame(final SPacketJoinGame packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.playerController = new PlayerControllerMP(this.client, this);
        this.world = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.client.profiler);
        this.client.gameSettings.difficulty = packetIn.getDifficulty();
        this.client.loadWorld(this.world);
        final Minecraft client = this.client;
        Minecraft.player.dimension = packetIn.getDimension();
        this.client.displayGuiScreen(new GuiDownloadTerrain());
        final Minecraft client2 = this.client;
        Minecraft.player.setEntityId(packetIn.getPlayerId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        final Minecraft client3 = this.client;
        Minecraft.player.setReducedDebug(packetIn.isReducedDebugInfo());
        this.client.playerController.setGameType(packetIn.getGameType());
        this.client.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }
    
    @Override
    public void handleSpawnObject(final SPacketSpawnObject packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final double d0 = packetIn.getX();
        final double d2 = packetIn.getY();
        final double d3 = packetIn.getZ();
        Entity entity = null;
        if (packetIn.getType() == 10) {
            entity = EntityMinecart.create(this.world, d0, d2, d3, EntityMinecart.Type.getById(packetIn.getData()));
        }
        else if (packetIn.getType() == 90) {
            final Entity entity2 = this.world.getEntityByID(packetIn.getData());
            if (entity2 instanceof EntityPlayer) {
                entity = new EntityFishHook(this.world, (EntityPlayer)entity2, d0, d2, d3);
            }
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 60) {
            entity = new EntityTippedArrow(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 91) {
            entity = new EntitySpectralArrow(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 61) {
            entity = new EntitySnowball(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 68) {
            entity = new EntityLlamaSpit(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
        }
        else if (packetIn.getType() == 71) {
            entity = new EntityItemFrame(this.world, new BlockPos(d0, d2, d3), EnumFacing.byHorizontalIndex(packetIn.getData()));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 77) {
            entity = new EntityLeashKnot(this.world, new BlockPos(MathHelper.floor(d0), MathHelper.floor(d2), MathHelper.floor(d3)));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 65) {
            entity = new EntityEnderPearl(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 72) {
            entity = new EntityEnderEye(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 76) {
            entity = new EntityFireworkRocket(this.world, d0, d2, d3, ItemStack.EMPTY);
        }
        else if (packetIn.getType() == 63) {
            entity = new EntityLargeFireball(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 93) {
            entity = new EntityDragonFireball(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 64) {
            entity = new EntitySmallFireball(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 66) {
            entity = new EntityWitherSkull(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 67) {
            entity = new EntityShulkerBullet(this.world, d0, d2, d3, packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 62) {
            entity = new EntityEgg(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 79) {
            entity = new EntityEvokerFangs(this.world, d0, d2, d3, 0.0f, 0, null);
        }
        else if (packetIn.getType() == 73) {
            entity = new EntityPotion(this.world, d0, d2, d3, ItemStack.EMPTY);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 75) {
            entity = new EntityExpBottle(this.world, d0, d2, d3);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 1) {
            entity = new EntityBoat(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 50) {
            entity = new EntityTNTPrimed(this.world, d0, d2, d3, null);
        }
        else if (packetIn.getType() == 78) {
            entity = new EntityArmorStand(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 51) {
            entity = new EntityEnderCrystal(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 2) {
            entity = new EntityItem(this.world, d0, d2, d3);
        }
        else if (packetIn.getType() == 70) {
            entity = new EntityFallingBlock(this.world, d0, d2, d3, Block.getStateById(packetIn.getData() & 0xFFFF));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 3) {
            entity = new EntityAreaEffectCloud(this.world, d0, d2, d3);
        }
        if (entity != null) {
            EntityTracker.updateServerPosition(entity, d0, d2, d3);
            entity.rotationPitch = packetIn.getPitch() * 360 / 256.0f;
            entity.rotationYaw = packetIn.getYaw() * 360 / 256.0f;
            final Entity[] aentity = entity.getParts();
            if (aentity != null) {
                final int i = packetIn.getEntityID() - entity.getEntityId();
                for (final Entity entity3 : aentity) {
                    entity3.setEntityId(entity3.getEntityId() + i);
                }
            }
            entity.setEntityId(packetIn.getEntityID());
            entity.setUniqueId(packetIn.getUniqueId());
            this.world.addEntityToWorld(packetIn.getEntityID(), entity);
            if (packetIn.getData() > 0) {
                if (packetIn.getType() == 60 || packetIn.getType() == 91) {
                    final Entity entity4 = this.world.getEntityByID(packetIn.getData() - 1);
                    if (entity4 instanceof EntityLivingBase && entity instanceof EntityArrow) {
                        ((EntityArrow)entity).shootingEntity = entity4;
                    }
                }
                entity.setVelocity(packetIn.getSpeedX() / 8000.0, packetIn.getSpeedY() / 8000.0, packetIn.getSpeedZ() / 8000.0);
            }
        }
    }
    
    @Override
    public void handleSpawnExperienceOrb(final SPacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final double d0 = packetIn.getX();
        final double d2 = packetIn.getY();
        final double d3 = packetIn.getZ();
        final Entity entity = new EntityXPOrb(this.world, d0, d2, d3, packetIn.getXPValue());
        EntityTracker.updateServerPosition(entity, d0, d2, d3);
        entity.rotationYaw = 0.0f;
        entity.rotationPitch = 0.0f;
        entity.setEntityId(packetIn.getEntityID());
        this.world.addEntityToWorld(packetIn.getEntityID(), entity);
    }
    
    @Override
    public void handleSpawnGlobalEntity(final SPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final double d0 = packetIn.getX();
        final double d2 = packetIn.getY();
        final double d3 = packetIn.getZ();
        Entity entity = null;
        if (packetIn.getType() == 1) {
            entity = new EntityLightningBolt(this.world, d0, d2, d3, false);
        }
        if (entity != null) {
            EntityTracker.updateServerPosition(entity, d0, d2, d3);
            entity.rotationYaw = 0.0f;
            entity.rotationPitch = 0.0f;
            entity.setEntityId(packetIn.getEntityId());
            this.world.addWeatherEffect(entity);
        }
    }
    
    @Override
    public void handleSpawnPainting(final SPacketSpawnPainting packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final EntityPainting entitypainting = new EntityPainting(this.world, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        entitypainting.setUniqueId(packetIn.getUniqueId());
        this.world.addEntityToWorld(packetIn.getEntityID(), entitypainting);
    }
    
    @Override
    public void handleEntityVelocity(final SPacketEntityVelocity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setVelocity(packetIn.getMotionX() / 8000.0, packetIn.getMotionY() / 8000.0, packetIn.getMotionZ() / 8000.0);
        }
    }
    
    @Override
    public void handleEntityMetadata(final SPacketEntityMetadata packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity != null && packetIn.getDataManagerEntries() != null) {
            entity.getDataManager().setEntryValues(packetIn.getDataManagerEntries());
        }
    }
    
    @Override
    public void handleSpawnPlayer(final SPacketSpawnPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final double d0 = packetIn.getX();
        final double d2 = packetIn.getY();
        final double d3 = packetIn.getZ();
        final float f = packetIn.getYaw() * 360 / 256.0f;
        final float f2 = packetIn.getPitch() * 360 / 256.0f;
        final EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.client.world, this.getPlayerInfo(packetIn.getUniqueId()).getGameProfile());
        entityotherplayermp.prevPosX = d0;
        entityotherplayermp.lastTickPosX = d0;
        entityotherplayermp.prevPosY = d2;
        entityotherplayermp.lastTickPosY = d2;
        entityotherplayermp.prevPosZ = d3;
        EntityTracker.updateServerPosition(entityotherplayermp, d0, d2, entityotherplayermp.lastTickPosZ = d3);
        entityotherplayermp.setPositionAndRotation(d0, d2, d3, f, f2);
        this.world.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
        final List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
        if (list != null) {
            entityotherplayermp.getDataManager().setEntryValues(list);
        }
    }
    
    @Override
    public void handleEntityTeleport(final SPacketEntityTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            final double d0 = packetIn.getX();
            final double d2 = packetIn.getY();
            final double d3 = packetIn.getZ();
            EntityTracker.updateServerPosition(entity, d0, d2, d3);
            if (!entity.canPassengerSteer()) {
                final float f = packetIn.getYaw() * 360 / 256.0f;
                final float f2 = packetIn.getPitch() * 360 / 256.0f;
                if (Math.abs(entity.posX - d0) < 0.03125 && Math.abs(entity.posY - d2) < 0.015625 && Math.abs(entity.posZ - d3) < 0.03125) {
                    entity.setPositionAndRotationDirect(entity.posX, entity.posY, entity.posZ, f, f2, 0, true);
                }
                else {
                    entity.setPositionAndRotationDirect(d0, d2, d3, f, f2, 3, true);
                }
                entity.onGround = packetIn.getOnGround();
            }
        }
    }
    
    @Override
    public void handleHeldItemChange(final SPacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (InventoryPlayer.isHotbar(packetIn.getHeldItemHotbarIndex())) {
            final Minecraft client = this.client;
            Minecraft.player.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }
    
    @Override
    public void handleEntityMovement(final SPacketEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = packetIn.getEntity(this.world);
        if (entity != null) {
            final Entity entity2 = entity;
            entity2.serverPosX += packetIn.getX();
            final Entity entity3 = entity;
            entity3.serverPosY += packetIn.getY();
            final Entity entity4 = entity;
            entity4.serverPosZ += packetIn.getZ();
            final double d0 = entity.serverPosX / 4096.0;
            final double d2 = entity.serverPosY / 4096.0;
            final double d3 = entity.serverPosZ / 4096.0;
            if (!entity.canPassengerSteer()) {
                final float f = packetIn.isRotating() ? (packetIn.getYaw() * 360 / 256.0f) : entity.rotationYaw;
                final float f2 = packetIn.isRotating() ? (packetIn.getPitch() * 360 / 256.0f) : entity.rotationPitch;
                entity.setPositionAndRotationDirect(d0, d2, d3, f, f2, 3, false);
                entity.onGround = packetIn.getOnGround();
            }
        }
    }
    
    @Override
    public void handleEntityHeadLook(final SPacketEntityHeadLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = packetIn.getEntity(this.world);
        if (entity != null) {
            final float f = packetIn.getYaw() * 360 / 256.0f;
            entity.setRotationYawHead(f);
        }
    }
    
    @Override
    public void handleDestroyEntities(final SPacketDestroyEntities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
            this.world.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }
    
    @Override
    public void handlePlayerPosLook(final SPacketPlayerPosLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        double d0 = packetIn.getX();
        double d2 = packetIn.getY();
        double d3 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f2 = packetIn.getPitch();
        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X)) {
            d0 += entityplayer.posX;
        }
        else {
            entityplayer.motionX = 0.0;
        }
        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
            d2 += entityplayer.posY;
        }
        else {
            entityplayer.motionY = 0.0;
        }
        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
            d3 += entityplayer.posZ;
        }
        else {
            entityplayer.motionZ = 0.0;
        }
        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 += entityplayer.rotationPitch;
        }
        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityplayer.rotationYaw;
        }
        entityplayer.setPositionAndRotation(d0, d2, d3, f, f2);
        this.netManager.sendPacket(new CPacketConfirmTeleport(packetIn.getTeleportId()));
        this.netManager.sendPacket(new CPacketPlayer.PositionRotation(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            final Minecraft client2 = this.client;
            final EntityPlayerSP player = Minecraft.player;
            final Minecraft client3 = this.client;
            player.prevPosX = Minecraft.player.posX;
            final Minecraft client4 = this.client;
            final EntityPlayerSP player2 = Minecraft.player;
            final Minecraft client5 = this.client;
            player2.prevPosY = Minecraft.player.posY;
            final Minecraft client6 = this.client;
            final EntityPlayerSP player3 = Minecraft.player;
            final Minecraft client7 = this.client;
            player3.prevPosZ = Minecraft.player.posZ;
            this.doneLoadingTerrain = true;
            this.client.displayGuiScreen(null);
        }
    }
    
    @Override
    public void handleMultiBlockChange(final SPacketMultiBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        for (final SPacketMultiBlockChange.BlockUpdateData spacketmultiblockchange$blockupdatedata : packetIn.getChangedBlocks()) {
            this.world.invalidateRegionAndSetBlock(spacketmultiblockchange$blockupdatedata.getPos(), spacketmultiblockchange$blockupdatedata.getBlockState());
        }
    }
    
    @Override
    public void handleChunkData(final SPacketChunkData packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (packetIn.isFullChunk()) {
            this.world.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }
        this.world.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        final Chunk chunk = this.world.getChunk(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.read(packetIn.getReadBuffer(), packetIn.getExtractedSize(), packetIn.isFullChunk());
        this.world.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        if (!packetIn.isFullChunk() || !(this.world.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
        for (final NBTTagCompound nbttagcompound : packetIn.getTileEntityTags()) {
            final BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
            final TileEntity tileentity = this.world.getTileEntity(blockpos);
            if (tileentity != null) {
                tileentity.readFromNBT(nbttagcompound);
            }
        }
    }
    
    @Override
    public void processChunkUnload(final SPacketUnloadChunk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.world.doPreChunk(packetIn.getX(), packetIn.getZ(), false);
    }
    
    @Override
    public void handleBlockChange(final SPacketBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.world.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }
    
    @Override
    public void handleDisconnect(final SPacketDisconnect packetIn) {
        this.netManager.closeChannel(packetIn.getReason());
    }
    
    @Override
    public void onDisconnect(final ITextComponent reason) {
        this.client.loadWorld(null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.client.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).getProxy(), "disconnect.lost", reason).getProxy());
            }
            else {
                this.client.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        }
        else {
            this.client.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }
    
    public void sendPacket(final Packet<?> packetIn) {
        this.netManager.sendPacket(packetIn);
    }
    
    @Override
    public void handleCollectItem(final SPacketCollectItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.world.getEntityByID(packetIn.getEntityID());
        if (entitylivingbase == null) {
            final Minecraft client = this.client;
            entitylivingbase = Minecraft.player;
        }
        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                this.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1f, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.35f + 0.9f, false);
            }
            else {
                this.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 1.4f + 2.0f, false);
            }
            if (entity instanceof EntityItem) {
                ((EntityItem)entity).getItem().setCount(packetIn.getAmount());
            }
            this.client.effectRenderer.addEffect(new ParticleItemPickup(this.world, entity, entitylivingbase, 0.5f));
            this.world.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }
    
    @Override
    public void handleChat(final SPacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.ingameGUI.addChatMessage(packetIn.getType(), packetIn.getChatComponent());
    }
    
    @Override
    public void handleAnimation(final SPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingArm(EnumHand.MAIN_HAND);
            }
            else if (packetIn.getAnimationType() == 3) {
                final EntityLivingBase entitylivingbase2 = (EntityLivingBase)entity;
                entitylivingbase2.swingArm(EnumHand.OFF_HAND);
            }
            else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            }
            else if (packetIn.getAnimationType() == 2) {
                final EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            }
            else if (packetIn.getAnimationType() == 4) {
                this.client.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            }
            else if (packetIn.getAnimationType() == 5) {
                this.client.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
    
    @Override
    public void handleUseBed(final SPacketUseBed packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        packetIn.getPlayer(this.world).trySleep(packetIn.getBedPosition());
    }
    
    @Override
    public void handleSpawnMob(final SPacketSpawnMob packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final double d0 = packetIn.getX();
        final double d2 = packetIn.getY();
        final double d3 = packetIn.getZ();
        final float f = packetIn.getYaw() * 360 / 256.0f;
        final float f2 = packetIn.getPitch() * 360 / 256.0f;
        final EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), this.client.world);
        if (entitylivingbase != null) {
            EntityTracker.updateServerPosition(entitylivingbase, d0, d2, d3);
            entitylivingbase.renderYawOffset = packetIn.getHeadPitch() * 360 / 256.0f;
            entitylivingbase.rotationYawHead = packetIn.getHeadPitch() * 360 / 256.0f;
            final Entity[] aentity = entitylivingbase.getParts();
            if (aentity != null) {
                final int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
                for (final Entity entity : aentity) {
                    entity.setEntityId(entity.getEntityId() + i);
                }
            }
            entitylivingbase.setEntityId(packetIn.getEntityID());
            entitylivingbase.setUniqueId(packetIn.getUniqueId());
            entitylivingbase.setPositionAndRotation(d0, d2, d3, f, f2);
            entitylivingbase.motionX = packetIn.getVelocityX() / 8000.0f;
            entitylivingbase.motionY = packetIn.getVelocityY() / 8000.0f;
            entitylivingbase.motionZ = packetIn.getVelocityZ() / 8000.0f;
            this.world.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
            final List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
            if (list != null) {
                entitylivingbase.getDataManager().setEntryValues(list);
            }
        }
        else {
            NetHandlerPlayClient.LOGGER.warn("Skipping Entity with id {}", (Object)packetIn.getEntityType());
        }
    }
    
    @Override
    public void handleTimeUpdate(final SPacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.world.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.client.world.setWorldTime(packetIn.getWorldTime());
    }
    
    @Override
    public void handleSpawnPosition(final SPacketSpawnPosition packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        Minecraft.player.setSpawnPoint(packetIn.getSpawnPos(), true);
        this.client.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }
    
    @Override
    public void handleSetPassengers(final SPacketSetPassengers packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity == null) {
            NetHandlerPlayClient.LOGGER.warn("Received passengers for unknown entity");
        }
        else {
            final Entity entity3 = entity;
            final Minecraft client = this.client;
            final boolean flag = entity3.isRidingOrBeingRiddenBy(Minecraft.player);
            entity.removePassengers();
            for (final int i : packetIn.getPassengerIds()) {
                final Entity entity2 = this.world.getEntityByID(i);
                if (entity2 != null) {
                    entity2.startRiding(entity, true);
                    final Entity entity4 = entity2;
                    final Minecraft client2 = this.client;
                    if (entity4 == Minecraft.player && !flag) {
                        this.client.ingameGUI.setOverlayMessage(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(this.client.gameSettings.keyBindSneak.getKeyCode())), false);
                    }
                }
            }
        }
    }
    
    @Override
    public void handleEntityAttach(final SPacketEntityAttach packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        final Entity entity2 = this.world.getEntityByID(packetIn.getVehicleEntityId());
        if (entity instanceof EntityLiving) {
            if (entity2 != null) {
                ((EntityLiving)entity).setLeashHolder(entity2, false);
            }
            else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }
    
    @Override
    public void handleEntityStatus(final SPacketEntityStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = packetIn.getEntity(this.world);
        if (entity != null) {
            if (packetIn.getOpCode() == 21) {
                this.client.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            }
            else if (packetIn.getOpCode() == 35) {
                final int i = 40;
                this.client.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
                this.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
                final Entity entity2 = entity;
                final Minecraft client = this.client;
                if (entity2 == Minecraft.player) {
                    this.client.entityRenderer.displayItemActivation(new ItemStack(Items.TOTEM_OF_UNDYING));
                }
            }
            else {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }
    
    @Override
    public void handleUpdateHealth(final SPacketUpdateHealth packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        Minecraft.player.setPlayerSPHealth(packetIn.getHealth());
        final Minecraft client2 = this.client;
        Minecraft.player.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        final Minecraft client3 = this.client;
        Minecraft.player.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }
    
    @Override
    public void handleSetExperience(final SPacketSetExperience packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        Minecraft.player.setXPStats(packetIn.getExperienceBar(), packetIn.getTotalExperience(), packetIn.getLevel());
    }
    
    @Override
    public void handleRespawn(final SPacketRespawn packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final int dimensionID = packetIn.getDimensionID();
        final Minecraft client = this.client;
        if (dimensionID != Minecraft.player.dimension) {
            this.doneLoadingTerrain = false;
            final Scoreboard scoreboard = this.world.getScoreboard();
            (this.world = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.client.world.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.client.profiler)).setWorldScoreboard(scoreboard);
            this.client.loadWorld(this.world);
            final Minecraft client2 = this.client;
            Minecraft.player.dimension = packetIn.getDimensionID();
            this.client.displayGuiScreen(new GuiDownloadTerrain());
        }
        this.client.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        this.client.playerController.setGameType(packetIn.getGameType());
    }
    
    @Override
    public void handleExplosion(final SPacketExplosion packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Explosion explosion = new Explosion(this.client.world, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        final Minecraft client = this.client;
        final EntityPlayerSP player = Minecraft.player;
        player.motionX += packetIn.getMotionX();
        final Minecraft client2 = this.client;
        final EntityPlayerSP player2 = Minecraft.player;
        player2.motionY += packetIn.getMotionY();
        final Minecraft client3 = this.client;
        final EntityPlayerSP player3 = Minecraft.player;
        player3.motionZ += packetIn.getMotionZ();
    }
    
    @Override
    public void handleOpenWindow(final SPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayerSP entityplayersp = Minecraft.player;
        if ("minecraft:container".equals(packetIn.getGuiId())) {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("minecraft:villager".equals(packetIn.getGuiId())) {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("EntityHorse".equals(packetIn.getGuiId())) {
            final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
            if (entity instanceof AbstractHorse) {
                entityplayersp.openGuiHorseInventory((AbstractHorse)entity, new ContainerHorseChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        }
        else if (!packetIn.hasSlots()) {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else {
            final IInventory iinventory = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(iinventory);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }
    
    @Override
    public void handleSetSlot(final SPacketSetSlot packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        final ItemStack itemstack = packetIn.getStack();
        final int i = packetIn.getSlot();
        this.client.getTutorial().handleSetSlot(itemstack);
        if (packetIn.getWindowId() == -1) {
            entityplayer.inventory.setItemStack(itemstack);
        }
        else if (packetIn.getWindowId() == -2) {
            entityplayer.inventory.setInventorySlotContents(i, itemstack);
        }
        else {
            boolean flag = false;
            if (this.client.currentScreen instanceof GuiContainerCreative) {
                final GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.client.currentScreen;
                flag = (guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex());
            }
            if (packetIn.getWindowId() == 0 && packetIn.getSlot() >= 36 && i < 45) {
                if (!itemstack.isEmpty()) {
                    final ItemStack itemstack2 = entityplayer.inventoryContainer.getSlot(i).getStack();
                    if (itemstack2.isEmpty() || itemstack2.getCount() < itemstack.getCount()) {
                        itemstack.setAnimationsToGo(5);
                    }
                }
                entityplayer.inventoryContainer.putStackInSlot(i, itemstack);
            }
            else if (packetIn.getWindowId() == entityplayer.openContainer.windowId && (packetIn.getWindowId() != 0 || !flag)) {
                entityplayer.openContainer.putStackInSlot(i, itemstack);
            }
        }
    }
    
    @Override
    public void handleConfirmTransaction(final SPacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        Container container = null;
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        }
        else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }
        if (container != null && !packetIn.wasAccepted()) {
            this.sendPacket(new CPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }
    
    @Override
    public void handleWindowItems(final SPacketWindowItems packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        if (packetIn.getWindowId() == 0) {
            entityplayer.inventoryContainer.setAll(packetIn.getItemStacks());
        }
        else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            entityplayer.openContainer.setAll(packetIn.getItemStacks());
        }
    }
    
    @Override
    public void handleSignEditorOpen(final SPacketSignEditorOpen packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        TileEntity tileentity = this.world.getTileEntity(packetIn.getSignPosition());
        if (!(tileentity instanceof TileEntitySign)) {
            tileentity = new TileEntitySign();
            tileentity.setWorld(this.world);
            tileentity.setPos(packetIn.getSignPosition());
        }
        final Minecraft client = this.client;
        Minecraft.player.openEditSign((TileEntitySign)tileentity);
    }
    
    @Override
    public void handleUpdateTileEntity(final SPacketUpdateTileEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (this.client.world.isBlockLoaded(packetIn.getPos())) {
            final TileEntity tileentity = this.client.world.getTileEntity(packetIn.getPos());
            final int i = packetIn.getTileEntityType();
            final boolean flag = i == 2 && tileentity instanceof TileEntityCommandBlock;
            if ((i == 1 && tileentity instanceof TileEntityMobSpawner) || flag || (i == 3 && tileentity instanceof TileEntityBeacon) || (i == 4 && tileentity instanceof TileEntitySkull) || (i == 5 && tileentity instanceof TileEntityFlowerPot) || (i == 6 && tileentity instanceof TileEntityBanner) || (i == 7 && tileentity instanceof TileEntityStructure) || (i == 8 && tileentity instanceof TileEntityEndGateway) || (i == 9 && tileentity instanceof TileEntitySign) || (i == 10 && tileentity instanceof TileEntityShulkerBox) || (i == 11 && tileentity instanceof TileEntityBed)) {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
            if (flag && this.client.currentScreen instanceof GuiCommandBlock) {
                ((GuiCommandBlock)this.client.currentScreen).updateGui();
            }
        }
    }
    
    @Override
    public void handleWindowProperty(final SPacketWindowProperty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
            entityplayer.openContainer.updateProgressBar(packetIn.getProperty(), packetIn.getValue());
        }
    }
    
    @Override
    public void handleEntityEquipment(final SPacketEntityEquipment packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityID());
        if (entity != null) {
            entity.setItemStackToSlot(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }
    
    @Override
    public void handleCloseWindow(final SPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        Minecraft.player.closeScreenAndDropStack();
    }
    
    @Override
    public void handleBlockAction(final SPacketBlockAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.world.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }
    
    @Override
    public void handleBlockBreakAnim(final SPacketBlockBreakAnim packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }
    
    @Override
    public void handleChangeGameState(final SPacketChangeGameState packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer = Minecraft.player;
        final int i = packetIn.getGameState();
        final float f = packetIn.getValue();
        final int j = MathHelper.floor(f + 0.5f);
        if (i >= 0 && i < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[i] != null) {
            entityplayer.sendStatusMessage(new TextComponentTranslation(SPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]), false);
        }
        if (i == 1) {
            this.world.getWorldInfo().setRaining(true);
            this.world.setRainStrength(0.0f);
        }
        else if (i == 2) {
            this.world.getWorldInfo().setRaining(false);
            this.world.setRainStrength(1.0f);
        }
        else if (i == 3) {
            this.client.playerController.setGameType(GameType.getByID(j));
        }
        else if (i == 4) {
            if (j == 0) {
                final Minecraft client2 = this.client;
                Minecraft.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
                this.client.displayGuiScreen(new GuiDownloadTerrain());
            }
            else if (j == 1) {
                final Minecraft client3;
                this.client.displayGuiScreen(new GuiWinGame(true, () -> {
                    client3 = this.client;
                    Minecraft.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
                }));
            }
        }
        else if (i == 5) {
            final GameSettings gamesettings = this.client.gameSettings;
            if (f == 0.0f) {
                this.client.displayGuiScreen(new GuiScreenDemo());
            }
            else if (f == 101.0f) {
                this.client.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
            }
            else if (f == 102.0f) {
                this.client.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
            }
            else if (f == 103.0f) {
                this.client.ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
            }
        }
        else if (i == 6) {
            this.world.playSound(entityplayer, entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18f, 0.45f);
        }
        else if (i == 7) {
            this.world.setRainStrength(f);
        }
        else if (i == 8) {
            this.world.setThunderStrength(f);
        }
        else if (i == 10) {
            this.world.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.world.playSound(entityplayer, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void handleMaps(final SPacketMaps packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final MapItemRenderer mapitemrenderer = this.client.entityRenderer.getMapItemRenderer();
        MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.client.world);
        if (mapdata == null) {
            final String s = "map_" + packetIn.getMapId();
            mapdata = new MapData(s);
            if (mapitemrenderer.getMapInstanceIfExists(s) != null) {
                final MapData mapdata2 = mapitemrenderer.getData(mapitemrenderer.getMapInstanceIfExists(s));
                if (mapdata2 != null) {
                    mapdata = mapdata2;
                }
            }
            this.client.world.setData(s, mapdata);
        }
        packetIn.setMapdataTo(mapdata);
        mapitemrenderer.updateMapTexture(mapdata);
    }
    
    @Override
    public void handleEffect(final SPacketEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (packetIn.isSoundServerwide()) {
            this.client.world.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
        else {
            this.client.world.playEvent(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }
    
    @Override
    public void handleAdvancementInfo(final SPacketAdvancementInfo packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.advancementManager.read(packetIn);
    }
    
    @Override
    public void handleSelectAdvancementsTab(final SPacketSelectAdvancementsTab packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final ResourceLocation resourcelocation = packetIn.getTab();
        if (resourcelocation == null) {
            this.advancementManager.setSelectedTab(null, false);
        }
        else {
            final Advancement advancement = this.advancementManager.getAdvancementList().getAdvancement(resourcelocation);
            this.advancementManager.setSelectedTab(advancement, false);
        }
    }
    
    @Override
    public void handleStatistics(final SPacketStatistics packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        for (final Map.Entry<StatBase, Integer> entry : packetIn.getStatisticMap().entrySet()) {
            final StatBase statbase = entry.getKey();
            final int k = entry.getValue();
            final Minecraft client = this.client;
            final StatisticsManager statFileWriter = Minecraft.player.getStatFileWriter();
            final Minecraft client2 = this.client;
            statFileWriter.unlockAchievement(Minecraft.player, statbase, k);
        }
        this.hasStatistics = true;
        if (this.client.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)this.client.currentScreen).onStatsUpdated();
        }
    }
    
    @Override
    public void handleRecipeBook(final SPacketRecipeBook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final RecipeBook recipebook = Minecraft.player.getRecipeBook();
        recipebook.setGuiOpen(packetIn.isGuiOpen());
        recipebook.setFilteringCraftable(packetIn.isFilteringCraftable());
        final SPacketRecipeBook.State spacketrecipebook$state = packetIn.getState();
        switch (spacketrecipebook$state) {
            case REMOVE: {
                for (final IRecipe irecipe : packetIn.getRecipes()) {
                    recipebook.lock(irecipe);
                }
                break;
            }
            case INIT: {
                packetIn.getRecipes().forEach(recipebook::unlock);
                packetIn.getDisplayedRecipes().forEach(recipebook::markNew);
                break;
            }
            case ADD: {
                final RecipeBook recipeBook;
                packetIn.getRecipes().forEach(p_194025_2_ -> {
                    recipeBook.unlock(p_194025_2_);
                    recipeBook.markNew(p_194025_2_);
                    RecipeToast.addOrUpdate(this.client.getToastGui(), p_194025_2_);
                    return;
                });
                break;
            }
        }
        RecipeBookClient.ALL_RECIPES.forEach(p_194023_1_ -> p_194023_1_.updateKnownRecipes(recipebook));
        if (this.client.currentScreen instanceof IRecipeShownListener) {
            ((IRecipeShownListener)this.client.currentScreen).recipesUpdated();
        }
    }
    
    @Override
    public void handleEntityEffect(final SPacketEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity instanceof EntityLivingBase) {
            final Potion potion = Potion.getPotionById(packetIn.getEffectId());
            if (potion != null) {
                final PotionEffect potioneffect = new PotionEffect(potion, packetIn.getDuration(), packetIn.getAmplifier(), packetIn.getIsAmbient(), packetIn.doesShowParticles());
                potioneffect.setPotionDurationMax(packetIn.isMaxDuration());
                ((EntityLivingBase)entity).addPotionEffect(potioneffect);
            }
        }
    }
    
    @Override
    public void handleCombatEvent(final SPacketCombatEvent packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (packetIn.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
            final Entity entityByID;
            final Entity entity = entityByID = this.world.getEntityByID(packetIn.playerId);
            final Minecraft client = this.client;
            if (entityByID == Minecraft.player) {
                this.client.displayGuiScreen(new GuiGameOver(packetIn.deathMessage));
            }
        }
    }
    
    @Override
    public void handleServerDifficulty(final SPacketServerDifficulty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        this.client.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }
    
    @Override
    public void handleCamera(final SPacketCamera packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = packetIn.getEntity(this.world);
        if (entity != null) {
            this.client.setRenderViewEntity(entity);
        }
    }
    
    @Override
    public void handleWorldBorder(final SPacketWorldBorder packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        packetIn.apply(this.world.getWorldBorder());
    }
    
    @Override
    public void handleTitle(final SPacketTitle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final SPacketTitle.Type spackettitle$type = packetIn.getType();
        String s = null;
        String s2 = null;
        final String s3 = (packetIn.getMessage() != null) ? packetIn.getMessage().getFormattedText() : "";
        switch (spackettitle$type) {
            case TITLE: {
                s = s3;
                break;
            }
            case SUBTITLE: {
                s2 = s3;
                break;
            }
            case ACTIONBAR: {
                this.client.ingameGUI.setOverlayMessage(s3, false);
                return;
            }
            case RESET: {
                this.client.ingameGUI.displayTitle("", "", -1, -1, -1);
                this.client.ingameGUI.setDefaultTitlesTimes();
                return;
            }
        }
        this.client.ingameGUI.displayTitle(s, s2, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }
    
    @Override
    public void handlePlayerListHeaderFooter(final SPacketPlayerListHeaderFooter packetIn) {
        this.client.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().isEmpty() ? null : packetIn.getHeader());
        this.client.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().isEmpty() ? null : packetIn.getFooter());
    }
    
    @Override
    public void handleRemoveEntityEffect(final SPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = packetIn.getEntity(this.world);
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removeActivePotionEffect(packetIn.getPotion());
        }
    }
    
    @Override
    public void handlePlayerListItem(final SPacketPlayerListItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        for (final SPacketPlayerListItem.AddPlayerData spacketplayerlistitem$addplayerdata : packetIn.getEntries()) {
            if (Minced.getInstance().manager.getModule(StaffInfo.class).state) {
                try {
                    EventManager.call(new EventPlayer(spacketplayerlistitem$addplayerdata, packetIn.getAction()));
                }
                catch (Exception ex) {}
            }
            if (packetIn.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(spacketplayerlistitem$addplayerdata.getProfile().getId());
            }
            else {
                NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(spacketplayerlistitem$addplayerdata.getProfile().getId());
                if (packetIn.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                    networkplayerinfo = new NetworkPlayerInfo(spacketplayerlistitem$addplayerdata);
                    this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
                }
                if (networkplayerinfo == null) {
                    continue;
                }
                switch (packetIn.getAction()) {
                    case ADD_PLAYER: {
                        networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                        networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                        continue;
                    }
                    case UPDATE_GAME_MODE: {
                        networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                        continue;
                    }
                    case UPDATE_LATENCY: {
                        networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                        continue;
                    }
                    case UPDATE_DISPLAY_NAME: {
                        networkplayerinfo.setDisplayName(spacketplayerlistitem$addplayerdata.getDisplayName());
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void handleKeepAlive(final SPacketKeepAlive packetIn) {
        this.sendPacket(new CPacketKeepAlive(packetIn.getId()));
    }
    
    @Override
    public void handlePlayerAbilities(final SPacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final EntityPlayer entityplayer1 = Minecraft.player;
        entityplayer1.capabilities.isFlying = packetIn.isFlying();
        entityplayer1.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayer1.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayer1.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayer1.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer1.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }
    
    @Override
    public void handleTabComplete(final SPacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final String[] astring = packetIn.getMatches();
        Arrays.sort(astring);
        if (this.client.currentScreen instanceof ITabCompleter) {
            ((ITabCompleter)this.client.currentScreen).setCompletions(astring);
        }
    }
    
    @Override
    public void handleSoundEffect(final SPacketSoundEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final WorldClient world = this.client.world;
        final Minecraft client = this.client;
        world.playSound(Minecraft.player, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSound(), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch());
    }
    
    @Override
    public void handleCustomSound(final SPacketCustomSound packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(packetIn.getSoundName()), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch(), false, 0, ISound.AttenuationType.LINEAR, (float)packetIn.getX(), (float)packetIn.getY(), (float)packetIn.getZ()));
    }
    
    @Override
    public void handleResourcePack(final SPacketResourcePackSend packetIn) {
        final String s = packetIn.getURL();
        final String s2 = packetIn.getHash();
        if (this.validateResourcePackUrl(s)) {
            if (s.startsWith("level://")) {
                try {
                    final String s3 = URLDecoder.decode(s.substring("level://".length()), StandardCharsets.UTF_8.toString());
                    final File file1 = new File(this.client.gameDir, "saves");
                    final File file2 = new File(file1, s3);
                    if (file2.isFile()) {
                        this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                        Futures.addCallback((ListenableFuture)this.client.getResourcePackRepository().setServerResourcePack(file2), (FutureCallback)this.createDownloadCallback());
                        return;
                    }
                }
                catch (UnsupportedEncodingException ex) {}
                this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
            else {
                final ServerData serverdata = this.client.getCurrentServerData();
                if (serverdata != null && serverdata.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
                    this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                    Futures.addCallback((ListenableFuture)this.client.getResourcePackRepository().downloadResourcePack(s, s2), (FutureCallback)this.createDownloadCallback());
                }
                else if (serverdata != null && serverdata.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
                    this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                }
                else {
                    this.client.addScheduledTask(new Runnable() {
                        @Override
                        public void run() {
                            NetHandlerPlayClient.this.client.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                                @Override
                                public void confirmClicked(final boolean result, final int id) {
                                    NetHandlerPlayClient.this.client = Minecraft.getMinecraft();
                                    final ServerData serverdata1 = NetHandlerPlayClient.this.client.getCurrentServerData();
                                    if (result) {
                                        if (serverdata1 != null) {
                                            serverdata1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                        }
                                        NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                                        Futures.addCallback((ListenableFuture)NetHandlerPlayClient.this.client.getResourcePackRepository().downloadResourcePack(s, s2), NetHandlerPlayClient.this.createDownloadCallback());
                                    }
                                    else {
                                        if (serverdata1 != null) {
                                            serverdata1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                        }
                                        NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                                    }
                                    ServerList.saveSingleServer(serverdata1);
                                    NetHandlerPlayClient.this.client.displayGuiScreen(null);
                                }
                            }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                        }
                    });
                }
            }
        }
    }
    
    private boolean validateResourcePackUrl(final String url) {
        try {
            final URI uri = new URI(url);
            final String s = uri.getScheme();
            final boolean flag = "level".equals(s);
            if (!"http".equals(s) && !"https".equals(s) && !flag) {
                throw new URISyntaxException(url, "Wrong protocol");
            }
            if (!flag || (!url.contains("..") && url.endsWith("/resources.zip"))) {
                return true;
            }
            throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
        }
        catch (URISyntaxException var5) {
            this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            return false;
        }
    }
    
    private FutureCallback<Object> createDownloadCallback() {
        return (FutureCallback<Object>)new FutureCallback<Object>() {
            public void onSuccess(@Nullable final Object p_onSuccess_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }
            
            public void onFailure(final Throwable p_onFailure_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        };
    }
    
    @Override
    public void handleUpdateBossInfo(final SPacketUpdateBossInfo packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        this.client.ingameGUI.getBossOverlay().read(packetIn);
    }
    
    @Override
    public void handleCooldown(final SPacketCooldown packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (packetIn.getTicks() == 0) {
            final Minecraft client = this.client;
            Minecraft.player.getCooldownTracker().removeCooldown(packetIn.getItem());
        }
        else {
            final Minecraft client2 = this.client;
            Minecraft.player.getCooldownTracker().setCooldown(packetIn.getItem(), packetIn.getTicks());
        }
    }
    
    @Override
    public void handleMoveVehicle(final SPacketMoveVehicle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Minecraft client = this.client;
        final Entity lowestRidingEntity;
        final Entity entity = lowestRidingEntity = Minecraft.player.getLowestRidingEntity();
        final Minecraft client2 = this.client;
        if (lowestRidingEntity != Minecraft.player && entity.canPassengerSteer()) {
            entity.setPositionAndRotation(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getYaw(), packetIn.getPitch());
            this.netManager.sendPacket(new CPacketVehicleMove(entity));
        }
    }
    
    @Override
    public void handleCustomPayload(final SPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if ("MC|TrList".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer = packetIn.getBufferData();
            try {
                final int k = packetbuffer.readInt();
                final GuiScreen guiscreen = this.client.currentScreen;
                if (guiscreen != null && guiscreen instanceof GuiMerchant) {
                    final int n = k;
                    final Minecraft client = this.client;
                    if (n == Minecraft.player.openContainer.windowId) {
                        final IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                        final MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                        imerchant.setRecipes(merchantrecipelist);
                    }
                }
            }
            catch (IOException ioexception) {
                NetHandlerPlayClient.LOGGER.error("Couldn't load trade info", (Throwable)ioexception);
            }
            finally {
                packetbuffer.release();
            }
        }
        else if ("MC|Brand".equals(packetIn.getChannelName())) {
            final Minecraft client2 = this.client;
            Minecraft.player.setServerBrand(packetIn.getBufferData().readString(32767));
        }
        else if ("MC|BOpen".equals(packetIn.getChannelName())) {
            final EnumHand enumhand = packetIn.getBufferData().readEnumValue(EnumHand.class);
            ItemStack itemStack;
            if (enumhand == EnumHand.OFF_HAND) {
                final Minecraft client3 = this.client;
                itemStack = Minecraft.player.getHeldItemOffhand();
            }
            else {
                final Minecraft client4 = this.client;
                itemStack = Minecraft.player.getHeldItemMainhand();
            }
            final ItemStack itemstack = itemStack;
            if (itemstack.getItem() == Items.WRITTEN_BOOK) {
                final Minecraft client5 = this.client;
                final Minecraft client6 = this.client;
                client5.displayGuiScreen(new GuiScreenBook(Minecraft.player, itemstack, false));
            }
        }
        else if ("MC|DebugPath".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer2 = packetIn.getBufferData();
            final int l = packetbuffer2.readInt();
            final float f1 = packetbuffer2.readFloat();
            final Path path = Path.read(packetbuffer2);
            ((DebugRendererPathfinding)this.client.debugRenderer.pathfinding).addPath(l, path, f1);
        }
        else if ("MC|DebugNeighborsUpdate".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer3 = packetIn.getBufferData();
            final long i1 = packetbuffer3.readVarLong();
            final BlockPos blockpos = packetbuffer3.readBlockPos();
            ((DebugRendererNeighborsUpdate)this.client.debugRenderer.neighborsUpdate).addUpdate(i1, blockpos);
        }
        else if ("MC|StopSound".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer4 = packetIn.getBufferData();
            final String s = packetbuffer4.readString(32767);
            final String s2 = packetbuffer4.readString(256);
            this.client.getSoundHandler().stop(s2, SoundCategory.getByName(s));
        }
    }
    
    @Override
    public void handleScoreboardObjective(final SPacketScoreboardObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Scoreboard scoreboard = this.world.getScoreboard();
        if (packetIn.getAction() == 0) {
            final ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.getObjectiveName(), IScoreCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.getObjectiveValue());
            scoreobjective.setRenderType(packetIn.getRenderType());
        }
        else {
            final ScoreObjective scoreobjective2 = scoreboard.getObjective(packetIn.getObjectiveName());
            if (packetIn.getAction() == 1) {
                scoreboard.removeObjective(scoreobjective2);
            }
            else if (packetIn.getAction() == 2) {
                scoreobjective2.setDisplayName(packetIn.getObjectiveValue());
                scoreobjective2.setRenderType(packetIn.getRenderType());
            }
        }
    }
    
    @Override
    public void handleUpdateScore(final SPacketUpdateScore packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Scoreboard scoreboard = this.world.getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
        if (packetIn.getScoreAction() == SPacketUpdateScore.Action.CHANGE) {
            final Score score = scoreboard.getOrCreateScore(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        }
        else if (packetIn.getScoreAction() == SPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
            }
            else if (scoreobjective != null) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }
    
    @Override
    public void handleDisplayObjective(final SPacketDisplayObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Scoreboard scoreboard = this.world.getScoreboard();
        if (packetIn.getName().isEmpty()) {
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), null);
        }
        else {
            final ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getName());
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), scoreobjective);
        }
    }
    
    @Override
    public void handleTeams(final SPacketTeams packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Scoreboard scoreboard = this.world.getScoreboard();
        ScorePlayerTeam scoreplayerteam;
        if (packetIn.getAction() == 0) {
            scoreplayerteam = scoreboard.createTeam(packetIn.getName());
        }
        else {
            scoreplayerteam = scoreboard.getTeam(packetIn.getName());
        }
        if (packetIn.getAction() == 0 || packetIn.getAction() == 2) {
            scoreplayerteam.setDisplayName(packetIn.getDisplayName());
            scoreplayerteam.setPrefix(packetIn.getPrefix());
            scoreplayerteam.setSuffix(packetIn.getSuffix());
            scoreplayerteam.setColor(TextFormatting.fromColorIndex(packetIn.getColor()));
            scoreplayerteam.setFriendlyFlags(packetIn.getFriendlyFlags());
            final Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(packetIn.getNameTagVisibility());
            if (team$enumvisible != null) {
                scoreplayerteam.setNameTagVisibility(team$enumvisible);
            }
            final Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(packetIn.getCollisionRule());
            if (team$collisionrule != null) {
                scoreplayerteam.setCollisionRule(team$collisionrule);
            }
        }
        if (packetIn.getAction() == 0 || packetIn.getAction() == 3) {
            for (final String s : packetIn.getPlayers()) {
                scoreboard.addPlayerToTeam(s, packetIn.getName());
            }
        }
        if (packetIn.getAction() == 4) {
            for (final String s2 : packetIn.getPlayers()) {
                scoreboard.removePlayerFromTeam(s2, scoreplayerteam);
            }
        }
        if (packetIn.getAction() == 1) {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }
    
    @Override
    public void handleParticles(final SPacketParticles packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        if (packetIn.getParticleCount() == 0) {
            final double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            final double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            final double d3 = packetIn.getParticleSpeed() * packetIn.getZOffset();
            try {
                this.world.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d3, packetIn.getParticleArgs());
            }
            catch (Throwable var17) {
                NetHandlerPlayClient.LOGGER.warn("Could not spawn particle effect {}", (Object)packetIn.getParticleType());
            }
        }
        else {
            for (int k = 0; k < packetIn.getParticleCount(); ++k) {
                final double d4 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
                final double d5 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
                final double d6 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
                final double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                final double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                final double d9 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
                try {
                    this.world.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d4, packetIn.getYCoordinate() + d5, packetIn.getZCoordinate() + d6, d7, d8, d9, packetIn.getParticleArgs());
                }
                catch (Throwable var18) {
                    NetHandlerPlayClient.LOGGER.warn("Could not spawn particle effect {}", (Object)packetIn.getParticleType());
                    return;
                }
            }
        }
    }
    
    @Override
    public void handleEntityProperties(final SPacketEntityProperties packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)packetIn, this, this.client);
        final Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            final AbstractAttributeMap abstractattributemap = ((EntityLivingBase)entity).getAttributeMap();
            for (final SPacketEntityProperties.Snapshot spacketentityproperties$snapshot : packetIn.getSnapshots()) {
                IAttributeInstance iattributeinstance = abstractattributemap.getAttributeInstanceByName(spacketentityproperties$snapshot.getName());
                if (iattributeinstance == null) {
                    iattributeinstance = abstractattributemap.registerAttribute(new RangedAttribute(null, spacketentityproperties$snapshot.getName(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iattributeinstance.setBaseValue(spacketentityproperties$snapshot.getBaseValue());
                iattributeinstance.removeAllModifiers();
                for (final AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers()) {
                    iattributeinstance.applyModifier(attributemodifier);
                }
            }
        }
    }
    
    @Override
    public void func_194307_a(final SPacketPlaceGhostRecipe p_194307_1_) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayClient>)p_194307_1_, this, this.client);
        final Minecraft client = this.client;
        final Container container = Minecraft.player.openContainer;
        if (container.windowId == p_194307_1_.func_194313_b()) {
            final Container container2 = container;
            final Minecraft client2 = this.client;
            if (container2.getCanCraft(Minecraft.player) && this.client.currentScreen instanceof IRecipeShownListener) {
                final GuiRecipeBook guirecipebook = ((IRecipeShownListener)this.client.currentScreen).func_194310_f();
                guirecipebook.setupGhostRecipe(p_194307_1_.func_194311_a(), container.inventorySlots);
            }
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }
    
    public NetworkPlayerInfo getPlayerInfo(final UUID uniqueId) {
        return this.playerInfoMap.get(uniqueId);
    }
    
    @Nullable
    public NetworkPlayerInfo getPlayerInfo(final String name) {
        for (final NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
            if (networkplayerinfo.getGameProfile().getName().equals(name)) {
                return networkplayerinfo;
            }
        }
        return null;
    }
    
    public GameProfile getGameProfile() {
        return this.profile;
    }
    
    public ClientAdvancementManager getAdvancementManager() {
        return this.advancementManager;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
