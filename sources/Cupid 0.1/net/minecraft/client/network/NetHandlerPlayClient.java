package net.minecraft.client.network;

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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.impl.network.PacketEvent;
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
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.Metadata;
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
import net.minecraft.tileentity.TileEntitySign;
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
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayClient implements INetHandlerPlayClient {
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
    this.netManager.sendPacket((Packet)new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
  }
  
  public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
    EntityFallingBlock entityFallingBlock;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX() / 32.0D;
    double d1 = packetIn.getY() / 32.0D;
    double d2 = packetIn.getZ() / 32.0D;
    Entity entity = null;
    if (packetIn.getType() == 10) {
      EntityMinecart entityMinecart = EntityMinecart.func_180458_a((World)this.clientWorldController, d0, d1, d2, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
    } else if (packetIn.getType() == 90) {
      Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
      if (entity1 instanceof EntityPlayer)
        EntityFishHook entityFishHook = new EntityFishHook((World)this.clientWorldController, d0, d1, d2, (EntityPlayer)entity1); 
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 60) {
      EntityArrow entityArrow = new EntityArrow((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 61) {
      EntitySnowball entitySnowball = new EntitySnowball((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 71) {
      EntityItemFrame entityItemFrame = new EntityItemFrame((World)this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 77) {
      EntityLeashKnot entityLeashKnot = new EntityLeashKnot((World)this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 65) {
      EntityEnderPearl entityEnderPearl = new EntityEnderPearl((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 72) {
      EntityEnderEye entityEnderEye = new EntityEnderEye((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 76) {
      EntityFireworkRocket entityFireworkRocket = new EntityFireworkRocket((World)this.clientWorldController, d0, d1, d2, (ItemStack)null);
    } else if (packetIn.getType() == 63) {
      EntityLargeFireball entityLargeFireball = new EntityLargeFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 64) {
      EntitySmallFireball entitySmallFireball = new EntitySmallFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 66) {
      EntityWitherSkull entityWitherSkull = new EntityWitherSkull((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 62) {
      EntityEgg entityEgg = new EntityEgg((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 73) {
      EntityPotion entityPotion = new EntityPotion((World)this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 75) {
      EntityExpBottle entityExpBottle = new EntityExpBottle((World)this.clientWorldController, d0, d1, d2);
      packetIn.func_149002_g(0);
    } else if (packetIn.getType() == 1) {
      EntityBoat entityBoat = new EntityBoat((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 50) {
      EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed((World)this.clientWorldController, d0, d1, d2, (EntityLivingBase)null);
    } else if (packetIn.getType() == 78) {
      EntityArmorStand entityArmorStand = new EntityArmorStand((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 51) {
      EntityEnderCrystal entityEnderCrystal = new EntityEnderCrystal((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 2) {
      EntityItem entityItem = new EntityItem((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 70) {
      entityFallingBlock = new EntityFallingBlock((World)this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.func_149009_m() & 0xFFFF));
      packetIn.func_149002_g(0);
    } 
    if (entityFallingBlock != null) {
      ((Entity)entityFallingBlock).serverPosX = packetIn.getX();
      ((Entity)entityFallingBlock).serverPosY = packetIn.getY();
      ((Entity)entityFallingBlock).serverPosZ = packetIn.getZ();
      ((Entity)entityFallingBlock).rotationPitch = (packetIn.getPitch() * 360) / 256.0F;
      ((Entity)entityFallingBlock).rotationYaw = (packetIn.getYaw() * 360) / 256.0F;
      Entity[] aentity = entityFallingBlock.getParts();
      if (aentity != null) {
        int i = packetIn.getEntityID() - entityFallingBlock.getEntityId();
        for (int j = 0; j < aentity.length; j++)
          aentity[j].setEntityId(aentity[j].getEntityId() + i); 
      } 
      entityFallingBlock.setEntityId(packetIn.getEntityID());
      this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityFallingBlock);
      if (packetIn.func_149009_m() > 0) {
        if (packetIn.getType() == 60) {
          Entity entity2 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
          if (entity2 instanceof EntityLivingBase && entityFallingBlock instanceof EntityArrow)
            ((EntityArrow)entityFallingBlock).shootingEntity = entity2; 
        } 
        entityFallingBlock.setVelocity(packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      } 
    } 
  }
  
  public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityXPOrb entityXPOrb = new EntityXPOrb((World)this.clientWorldController, packetIn.getX() / 32.0D, packetIn.getY() / 32.0D, packetIn.getZ() / 32.0D, packetIn.getXPValue());
    ((Entity)entityXPOrb).serverPosX = packetIn.getX();
    ((Entity)entityXPOrb).serverPosY = packetIn.getY();
    ((Entity)entityXPOrb).serverPosZ = packetIn.getZ();
    ((Entity)entityXPOrb).rotationYaw = 0.0F;
    ((Entity)entityXPOrb).rotationPitch = 0.0F;
    entityXPOrb.setEntityId(packetIn.getEntityID());
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityXPOrb);
  }
  
  public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
    EntityLightningBolt entityLightningBolt;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.func_149051_d() / 32.0D;
    double d1 = packetIn.func_149050_e() / 32.0D;
    double d2 = packetIn.func_149049_f() / 32.0D;
    Entity entity = null;
    if (packetIn.func_149053_g() == 1)
      entityLightningBolt = new EntityLightningBolt((World)this.clientWorldController, d0, d1, d2); 
    if (entityLightningBolt != null) {
      ((Entity)entityLightningBolt).serverPosX = packetIn.func_149051_d();
      ((Entity)entityLightningBolt).serverPosY = packetIn.func_149050_e();
      ((Entity)entityLightningBolt).serverPosZ = packetIn.func_149049_f();
      ((Entity)entityLightningBolt).rotationYaw = 0.0F;
      ((Entity)entityLightningBolt).rotationPitch = 0.0F;
      entityLightningBolt.setEntityId(packetIn.func_149052_c());
      this.clientWorldController.addWeatherEffect((Entity)entityLightningBolt);
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
    if (entity != null)
      entity.setVelocity(packetIn.getMotionX() / 8000.0D, packetIn.getMotionY() / 8000.0D, packetIn.getMotionZ() / 8000.0D); 
  }
  
  public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity != null && packetIn.func_149376_c() != null)
      entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c()); 
  }
  
  public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX() / 32.0D;
    double d1 = packetIn.getY() / 32.0D;
    double d2 = packetIn.getZ() / 32.0D;
    float f = (packetIn.getYaw() * 360) / 256.0F;
    float f1 = (packetIn.getPitch() * 360) / 256.0F;
    EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP((World)this.gameController.theWorld, getPlayerInfo(packetIn.getPlayer()).getGameProfile());
    entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (entityotherplayermp.serverPosX = packetIn.getX());
    entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (entityotherplayermp.serverPosY = packetIn.getY());
    entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (entityotherplayermp.serverPosZ = packetIn.getZ());
    int i = packetIn.getCurrentItemID();
    if (i == 0) {
      entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
    } else {
      entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(Item.getItemById(i), 1, 0);
    } 
    entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityotherplayermp);
    List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();
    if (list != null)
      entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list); 
  }
  
  public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity != null) {
      entity.serverPosX = packetIn.getX();
      entity.serverPosY = packetIn.getY();
      entity.serverPosZ = packetIn.getZ();
      double d0 = entity.serverPosX / 32.0D;
      double d1 = entity.serverPosY / 32.0D;
      double d2 = entity.serverPosZ / 32.0D;
      float f = (packetIn.getYaw() * 360) / 256.0F;
      float f1 = (packetIn.getPitch() * 360) / 256.0F;
      if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
        entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
      } else {
        entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
      } 
      entity.onGround = packetIn.getOnGround();
    } 
  }
  
  public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize())
      this.gameController.thePlayer.inventory.currentItem = packetIn.getHeldItemHotbarIndex(); 
  }
  
  public void handleEntityMovement(S14PacketEntity packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null) {
      entity.serverPosX += packetIn.func_149062_c();
      entity.serverPosY += packetIn.func_149061_d();
      entity.serverPosZ += packetIn.func_149064_e();
      double d0 = entity.serverPosX / 32.0D;
      double d1 = entity.serverPosY / 32.0D;
      double d2 = entity.serverPosZ / 32.0D;
      float f = packetIn.func_149060_h() ? ((packetIn.func_149066_f() * 360) / 256.0F) : entity.rotationYaw;
      float f1 = packetIn.func_149060_h() ? ((packetIn.func_149063_g() * 360) / 256.0F) : entity.rotationPitch;
      entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
      entity.onGround = packetIn.getOnGround();
    } 
  }
  
  public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null) {
      float f = (packetIn.getYaw() * 360) / 256.0F;
      entity.setRotationYawHead(f);
    } 
  }
  
  public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    for (int i = 0; i < (packetIn.getEntityIDs()).length; i++)
      this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]); 
  }
  
  public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    float f = packetIn.getYaw();
    float f1 = packetIn.getPitch();
    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
      d0 += ((EntityPlayer)entityPlayerSP).posX;
    } else {
      ((EntityPlayer)entityPlayerSP).motionX = 0.0D;
    } 
    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
      d1 += ((EntityPlayer)entityPlayerSP).posY;
    } else {
      ((EntityPlayer)entityPlayerSP).motionY = 0.0D;
    } 
    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
      d2 += ((EntityPlayer)entityPlayerSP).posZ;
    } else {
      ((EntityPlayer)entityPlayerSP).motionZ = 0.0D;
    } 
    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
      f1 += ((EntityPlayer)entityPlayerSP).rotationPitch; 
    if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
      f += ((EntityPlayer)entityPlayerSP).rotationYaw; 
    entityPlayerSP.setPositionAndRotation(d0, d1, d2, f, f1);
    this.netManager.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((EntityPlayer)entityPlayerSP).posX, (entityPlayerSP.getEntityBoundingBox()).minY, ((EntityPlayer)entityPlayerSP).posZ, ((EntityPlayer)entityPlayerSP).rotationYaw, ((EntityPlayer)entityPlayerSP).rotationPitch, false));
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
    for (S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata : packetIn.getChangedBlocks())
      this.clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(), s22packetmultiblockchange$blockupdatedata.getBlockState()); 
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
    chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
    this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
    if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof net.minecraft.world.WorldProviderSurface))
      chunk.resetRelightChecks(); 
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
        this.gameController.displayGuiScreen((GuiScreen)(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason)).getProxy());
      } else {
        this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
      } 
    } else {
      this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), "disconnect.lost", reason));
    } 
  }
  
  public void addToSendQueue(Packet packet) {
    PacketEvent packetEvent = (PacketEvent)Lite.EVENT_BUS.call((new PacketEvent(packet))
        
        .setDirection(Event.Direction.OUT));
    if (!packetEvent.isCancelled())
      this.netManager.sendPacket(packet); 
  }
  
  public void handleCollectItem(S0DPacketCollectItem packetIn) {
    EntityPlayerSP entityPlayerSP;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
    EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
    if (entitylivingbase == null)
      entityPlayerSP = this.gameController.thePlayer; 
    if (entity != null) {
      if (entity instanceof EntityXPOrb) {
        this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else {
        this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } 
      this.gameController.effectRenderer.addEffect((EntityFX)new EntityPickupFX((World)this.clientWorldController, entity, (Entity)entityPlayerSP, 0.5F));
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
    if (entity != null)
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
  
  public void handleUseBed(S0APacketUseBed packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    packetIn.getPlayer((World)this.clientWorldController).trySleep(packetIn.getBedPosition());
  }
  
  public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX() / 32.0D;
    double d1 = packetIn.getY() / 32.0D;
    double d2 = packetIn.getZ() / 32.0D;
    float f = (packetIn.getYaw() * 360) / 256.0F;
    float f1 = (packetIn.getPitch() * 360) / 256.0F;
    EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), (World)this.gameController.theWorld);
    entitylivingbase.serverPosX = packetIn.getX();
    entitylivingbase.serverPosY = packetIn.getY();
    entitylivingbase.serverPosZ = packetIn.getZ();
    entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (packetIn.getHeadPitch() * 360) / 256.0F;
    Entity[] aentity = entitylivingbase.getParts();
    if (aentity != null) {
      int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
      for (int j = 0; j < aentity.length; j++)
        aentity[j].setEntityId(aentity[j].getEntityId() + i); 
    } 
    entitylivingbase.setEntityId(packetIn.getEntityID());
    entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
    entitylivingbase.motionX = (packetIn.getVelocityX() / 8000.0F);
    entitylivingbase.motionY = (packetIn.getVelocityY() / 8000.0F);
    entitylivingbase.motionZ = (packetIn.getVelocityZ() / 8000.0F);
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitylivingbase);
    List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();
    if (list != null)
      entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list); 
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
    EntityPlayerSP entityPlayerSP;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
    if (packetIn.getLeash() == 0) {
      boolean flag = false;
      if (packetIn.getEntityId() == this.gameController.thePlayer.getEntityId()) {
        entityPlayerSP = this.gameController.thePlayer;
        if (entity1 instanceof EntityBoat)
          ((EntityBoat)entity1).setIsBoatEmpty(false); 
        flag = (((Entity)entityPlayerSP).ridingEntity == null && entity1 != null);
      } else if (entity1 instanceof EntityBoat) {
        ((EntityBoat)entity1).setIsBoatEmpty(true);
      } 
      if (entityPlayerSP == null)
        return; 
      entityPlayerSP.mountEntity(entity1);
      if (flag) {
        GameSettings gamesettings = this.gameController.gameSettings;
        this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode()) }), false);
      } 
    } else if (packetIn.getLeash() == 1 && entityPlayerSP instanceof EntityLiving) {
      if (entity1 != null) {
        ((EntityLiving)entityPlayerSP).setLeashedToEntity(entity1, false);
      } else {
        ((EntityLiving)entityPlayerSP).clearLeashed(false, false);
      } 
    } 
  }
  
  public void handleEntityStatus(S19PacketEntityStatus packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null)
      if (packetIn.getOpCode() == 21) {
        this.gameController.getSoundHandler().playSound((ISound)new GuardianSound((EntityGuardian)entity));
      } else {
        entity.handleStatusUpdate(packetIn.getOpCode());
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
    this.gameController.thePlayer.motionX += packetIn.func_149149_c();
    this.gameController.thePlayer.motionY += packetIn.func_149144_d();
    this.gameController.thePlayer.motionZ += packetIn.func_149147_e();
  }
  
  public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityplayersp = this.gameController.thePlayer;
    if ("minecraft:container".equals(packetIn.getGuiId())) {
      entityplayersp.displayGUIChest((IInventory)new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
      entityplayersp.displayVillagerTradeGui((IMerchant)new NpcMerchant((EntityPlayer)entityplayersp, packetIn.getWindowTitle()));
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } else if ("EntityHorse".equals(packetIn.getGuiId())) {
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
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    if (packetIn.func_149175_c() == -1) {
      ((EntityPlayer)entityPlayerSP).inventory.setItemStack(packetIn.func_149174_e());
    } else {
      boolean flag = false;
      if (this.gameController.currentScreen instanceof GuiContainerCreative) {
        GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
        flag = (guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex());
      } 
      if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
        ItemStack itemstack = ((EntityPlayer)entityPlayerSP).inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
        if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < (packetIn.func_149174_e()).stackSize))
          (packetIn.func_149174_e()).animationsToGo = 5; 
        ((EntityPlayer)entityPlayerSP).inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
      } else if (packetIn.func_149175_c() == ((EntityPlayer)entityPlayerSP).openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag)) {
        ((EntityPlayer)entityPlayerSP).openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
      } 
    } 
  }
  
  public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Container container = null;
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    if (packetIn.getWindowId() == 0) {
      container = ((EntityPlayer)entityPlayerSP).inventoryContainer;
    } else if (packetIn.getWindowId() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
      container = ((EntityPlayer)entityPlayerSP).openContainer;
    } 
    if (container != null && !packetIn.func_148888_e())
      addToSendQueue((Packet)new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true)); 
  }
  
  public void handleWindowItems(S30PacketWindowItems packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    if (packetIn.func_148911_c() == 0) {
      ((EntityPlayer)entityPlayerSP).inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
    } else if (packetIn.func_148911_c() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
      ((EntityPlayer)entityPlayerSP).openContainer.putStacksInSlots(packetIn.getItemStacks());
    } 
  }
  
  public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
    TileEntitySign tileEntitySign;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
    if (!(tileentity instanceof TileEntitySign)) {
      tileEntitySign = new TileEntitySign();
      tileEntitySign.setWorldObj((World)this.clientWorldController);
      tileEntitySign.setPos(packetIn.getSignPosition());
    } 
    this.gameController.thePlayer.openEditSign(tileEntitySign);
  }
  
  public void handleUpdateSign(S33PacketUpdateSign packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    boolean flag = false;
    if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
      TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
      if (tileentity instanceof TileEntitySign) {
        TileEntitySign tileentitysign = (TileEntitySign)tileentity;
        if (tileentitysign.getIsEditable()) {
          System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
          tileentitysign.markDirty();
        } 
        flag = true;
      } 
    } 
    if (!flag && this.gameController.thePlayer != null)
      this.gameController.thePlayer.addChatMessage((IChatComponent)new ChatComponentText("Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos().getY() + ", " + packetIn.getPos().getZ())); 
  }
  
  public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
      TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
      int i = packetIn.getTileEntityType();
      if ((i == 1 && tileentity instanceof net.minecraft.tileentity.TileEntityMobSpawner) || (i == 2 && tileentity instanceof net.minecraft.tileentity.TileEntityCommandBlock) || (i == 3 && tileentity instanceof net.minecraft.tileentity.TileEntityBeacon) || (i == 4 && tileentity instanceof net.minecraft.tileentity.TileEntitySkull) || (i == 5 && tileentity instanceof net.minecraft.tileentity.TileEntityFlowerPot) || (i == 6 && tileentity instanceof net.minecraft.tileentity.TileEntityBanner))
        tileentity.readFromNBT(packetIn.getNbtCompound()); 
    } 
  }
  
  public void handleWindowProperty(S31PacketWindowProperty packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    if (((EntityPlayer)entityPlayerSP).openContainer != null && ((EntityPlayer)entityPlayerSP).openContainer.windowId == packetIn.getWindowId())
      ((EntityPlayer)entityPlayerSP).openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue()); 
  }
  
  public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
    if (entity != null)
      entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack()); 
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
    for (int i = 0; i < packetIn.getChunkCount(); i++) {
      int j = packetIn.getChunkX(i);
      int k = packetIn.getChunkZ(i);
      this.clientWorldController.doPreChunk(j, k, true);
      this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
      Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
      chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
      this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
      if (!(this.clientWorldController.provider instanceof net.minecraft.world.WorldProviderSurface))
        chunk.resetRelightChecks(); 
    } 
  }
  
  public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    int i = packetIn.getGameState();
    float f = packetIn.func_149137_d();
    int j = MathHelper.floor_float(f + 0.5F);
    if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null)
      entityPlayerSP.addChatComponentMessage((IChatComponent)new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0])); 
    if (i == 1) {
      this.clientWorldController.getWorldInfo().setRaining(true);
      this.clientWorldController.setRainStrength(0.0F);
    } else if (i == 2) {
      this.clientWorldController.getWorldInfo().setRaining(false);
      this.clientWorldController.setRainStrength(1.0F);
    } else if (i == 3) {
      this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
    } else if (i == 4) {
      this.gameController.displayGuiScreen((GuiScreen)new GuiWinGame());
    } else if (i == 5) {
      GameSettings gamesettings = this.gameController.gameSettings;
      if (f == 0.0F) {
        this.gameController.displayGuiScreen((GuiScreen)new GuiScreenDemo());
      } else if (f == 101.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
      } else if (f == 102.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
      } else if (f == 103.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
      } 
    } else if (i == 6) {
      this.clientWorldController.playSound(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ, "random.successful_hit", 0.18F, 0.45F, false);
    } else if (i == 7) {
      this.clientWorldController.setRainStrength(f);
    } else if (i == 8) {
      this.clientWorldController.setThunderStrength(f);
    } else if (i == 10) {
      this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, ((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      this.clientWorldController.playSound(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, "mob.guardian.curse", 1.0F, 1.0F, false);
    } 
  }
  
  public void handleMaps(S34PacketMaps packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), (World)this.gameController.theWorld);
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
    for (Map.Entry<StatBase, Integer> entry : (Iterable<Map.Entry<StatBase, Integer>>)packetIn.func_148974_c().entrySet()) {
      StatBase statbase = entry.getKey();
      int i = ((Integer)entry.getValue()).intValue();
      if (statbase.isAchievement() && i > 0) {
        if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0) {
          Achievement achievement = (Achievement)statbase;
          this.gameController.guiAchievement.displayAchievement(achievement);
          this.gameController.getTwitchStream().func_152911_a((Metadata)new MetadataAchievement(achievement), 0L);
          if (statbase == AchievementList.openInventory) {
            this.gameController.gameSettings.showInventoryAchievementHint = false;
            this.gameController.gameSettings.saveOptions();
          } 
        } 
        flag = true;
      } 
      this.gameController.thePlayer.getStatFileWriter().unlockAchievement((EntityPlayer)this.gameController.thePlayer, statbase, i);
    } 
    if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint)
      this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory); 
    this.field_147308_k = true;
    if (this.gameController.currentScreen instanceof IProgressMeter)
      ((IProgressMeter)this.gameController.currentScreen).doneLoading(); 
  }
  
  public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity instanceof EntityLivingBase) {
      PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
      potioneffect.setPotionDurationMax(packetIn.func_149429_c());
      ((EntityLivingBase)entity).addPotionEffect(potioneffect);
    } 
  }
  
  public void handleCombatEvent(S42PacketCombatEvent packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
    EntityLivingBase entitylivingbase = (entity instanceof EntityLivingBase) ? (EntityLivingBase)entity : null;
    if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
      long i = (1000 * packetIn.field_179772_d / 20);
      MetadataCombat metadatacombat = new MetadataCombat((EntityLivingBase)this.gameController.thePlayer, entitylivingbase);
      this.gameController.getTwitchStream().func_176026_a((Metadata)metadatacombat, 0L - i, 0L);
    } else if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
      Entity entity1 = this.clientWorldController.getEntityByID(packetIn.field_179774_b);
      if (entity1 instanceof EntityPlayer) {
        MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityLivingBase)entity1, entitylivingbase);
        metadataplayerdeath.func_152807_a(packetIn.deathMessage);
        this.gameController.getTwitchStream().func_152911_a((Metadata)metadataplayerdeath, 0L);
      } 
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
    if (entity != null)
      this.gameController.setRenderViewEntity(entity); 
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
    String s2 = (packetIn.getMessage() != null) ? packetIn.getMessage().getFormattedText() : "";
    switch (s45packettitle$type) {
      case ADD_PLAYER:
        s = s2;
        break;
      case UPDATE_GAME_MODE:
        s1 = s2;
        break;
      case UPDATE_LATENCY:
        this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
        this.gameController.ingameGUI.func_175177_a();
        return;
    } 
    this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
  }
  
  public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
    if (!this.netManager.isLocalChannel())
      this.netManager.setCompressionTreshold(packetIn.func_179760_a()); 
  }
  
  public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
    this.gameController.ingameGUI.getTabList().setHeader((packetIn.getHeader().getFormattedText().length() == 0) ? null : packetIn.getHeader());
    this.gameController.ingameGUI.getTabList().setFooter((packetIn.getFooter().getFormattedText().length() == 0) ? null : packetIn.getFooter());
  }
  
  public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity instanceof EntityLivingBase)
      ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId()); 
  }
  
  public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    for (S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.func_179767_a()) {
      if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
        this.playerInfoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
        continue;
      } 
      NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(s38packetplayerlistitem$addplayerdata.getProfile().getId());
      if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
        networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
        this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
      } 
      if (networkplayerinfo != null)
        switch (packetIn.func_179768_b()) {
          case ADD_PLAYER:
            networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
            networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
          case UPDATE_GAME_MODE:
            networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
          case UPDATE_LATENCY:
            networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
          case UPDATE_DISPLAY_NAME:
            networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
        }  
    } 
  }
  
  public void handleKeepAlive(S00PacketKeepAlive packetIn) {
    addToSendQueue((Packet)new C00PacketKeepAlive(packetIn.func_149134_c()));
  }
  
  public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.thePlayer;
    ((EntityPlayer)entityPlayerSP).capabilities.isFlying = packetIn.isFlying();
    ((EntityPlayer)entityPlayerSP).capabilities.isCreativeMode = packetIn.isCreativeMode();
    ((EntityPlayer)entityPlayerSP).capabilities.disableDamage = packetIn.isInvulnerable();
    ((EntityPlayer)entityPlayerSP).capabilities.allowFlying = packetIn.isAllowFlying();
    ((EntityPlayer)entityPlayerSP).capabilities.setFlySpeed(packetIn.getFlySpeed());
    ((EntityPlayer)entityPlayerSP).capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
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
  
  public boolean validateResourcePackUrl(String url, String hash) {
    try {
      URI uri = new URI(url);
      String scheme = uri.getScheme();
      boolean isLevelProtocol = "level".equals(scheme);
      if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
        getNetworkManager().sendPacket((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
        throw new URISyntaxException(url, "Wrong protocol");
      } 
      url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());
      if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
        Minecraft.logger.warn("The current server has attempted to be malicious but we have stopped them.");
        throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
      } 
      return true;
    } catch (URISyntaxException e) {
      return false;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    } 
  }
  
  public void handleResourcePack(S48PacketResourcePackSend packetIn) {
    final String s = packetIn.getURL();
    final String s1 = packetIn.getHash();
    if (!validateResourcePackUrl(s, s1))
      return; 
    if (s.startsWith("level://")) {
      String s2 = s.substring("level://".length());
      File file1 = new File(this.gameController.mcDataDir, "saves");
      File file2 = new File(file1, s2);
      if (file2.isFile()) {
        this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
        Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), new FutureCallback<Object>() {
              public void onSuccess(Object p_onSuccess_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
              }
              
              public void onFailure(Throwable p_onFailure_1_) {
                NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
              }
            });
      } else {
        this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
      } 
    } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
      this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
      Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback<Object>() {
            public void onSuccess(Object p_onSuccess_1_) {
              NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }
            
            public void onFailure(Throwable p_onFailure_1_) {
              NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
          });
    } else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
      this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
    } else {
      this.gameController.addScheduledTask(new Runnable() {
            public void run() {
              NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)new GuiYesNo(new GuiYesNoCallback() {
                      public void confirmClicked(boolean result, int id) {
                        NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
                        if (result) {
                          if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
                            NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED); 
                          NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                          Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), new FutureCallback<Object>() {
                                public void onSuccess(Object p_onSuccess_1_) {
                                  NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                }
                                
                                public void onFailure(Throwable p_onFailure_1_) {
                                  NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                }
                              });
                        } else {
                          if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
                            NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED); 
                          NetHandlerPlayClient.this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
                        } 
                        ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
                        NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)null);
                      }
                    }I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
            }
          });
    } 
  }
  
  public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null)
      entity.clientUpdateEntityNBT(packetIn.getTagCompound()); 
  }
  
  public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if ("MC|TrList".equals(packetIn.getChannelName())) {
      PacketBuffer packetbuffer = packetIn.getBufferData();
      try {
        int i = packetbuffer.readInt();
        GuiScreen guiscreen = this.gameController.currentScreen;
        if (guiscreen != null && guiscreen instanceof GuiMerchant && i == this.gameController.thePlayer.openContainer.windowId) {
          IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
          MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
          imerchant.setRecipes(merchantrecipelist);
        } 
      } catch (IOException ioexception) {
        logger.error("Couldn't load trade info", ioexception);
      } finally {
        packetbuffer.release();
      } 
    } else if ("MC|Brand".equals(packetIn.getChannelName())) {
      this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
    } else if ("MC|BOpen".equals(packetIn.getChannelName())) {
      ItemStack itemstack = this.gameController.thePlayer.getCurrentEquippedItem();
      if (itemstack != null && itemstack.getItem() == Items.written_book)
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
      if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
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
    ScorePlayerTeam scoreplayerteam;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Scoreboard scoreboard = this.clientWorldController.getScoreboard();
    if (packetIn.func_149307_h() == 0) {
      scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
    } else {
      scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
    } 
    if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2) {
      scoreplayerteam.setTeamName(packetIn.func_149306_d());
      scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
      scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
      scoreplayerteam.setChatFormat(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
      scoreplayerteam.func_98298_a(packetIn.func_149308_i());
      Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
      if (team$enumvisible != null)
        scoreplayerteam.setNameTagVisibility(team$enumvisible); 
    } 
    if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3)
      for (String s : packetIn.func_149310_g())
        scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());  
    if (packetIn.func_149307_h() == 4)
      for (String s1 : packetIn.func_149310_g())
        scoreboard.removePlayerFromTeam(s1, scoreplayerteam);  
    if (packetIn.func_149307_h() == 1)
      scoreboard.removeTeam(scoreplayerteam); 
  }
  
  public void handleParticles(S2APacketParticles packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.getParticleCount() == 0) {
      double d0 = (packetIn.getParticleSpeed() * packetIn.getXOffset());
      double d2 = (packetIn.getParticleSpeed() * packetIn.getYOffset());
      double d4 = (packetIn.getParticleSpeed() * packetIn.getZOffset());
      try {
        this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
      } catch (Throwable var17) {
        logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
      } 
    } else {
      for (int i = 0; i < packetIn.getParticleCount(); i++) {
        double d1 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
        double d3 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
        double d5 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
        double d6 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        try {
          this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
        } catch (Throwable var16) {
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
      if (!(entity instanceof EntityLivingBase))
        throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")"); 
      BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
      for (S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : packetIn.func_149441_d()) {
        IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());
        if (iattributeinstance == null)
          iattributeinstance = baseattributemap.registerAttribute((IAttribute)new RangedAttribute((IAttribute)null, s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE)); 
        iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
        iattributeinstance.removeAllModifiers();
        for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c())
          iattributeinstance.applyModifier(attributemodifier); 
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
    return this.playerInfoMap.get(p_175102_1_);
  }
  
  public NetworkPlayerInfo getPlayerInfo(String p_175104_1_) {
    for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
      if (networkplayerinfo.getGameProfile().getName().equals(p_175104_1_))
        return networkplayerinfo; 
    } 
    return null;
  }
  
  public GameProfile getGameProfile() {
    return this.profile;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\network\NetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */