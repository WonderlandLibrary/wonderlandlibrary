// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.network;

import org.apache.logging.log4j.LogManager;
import java.util.UUID;
import java.util.Collection;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.StringUtils;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import java.io.IOException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import java.io.File;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import java.util.Iterator;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.stats.AchievementList;
import net.minecraft.client.stream.Metadata;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.world.IInteractionObject;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.world.Explosion;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.entity.EntityList;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.client.gui.GuiMultiplayer;
import exhibition.gui.screen.impl.mainmenu.ClientMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S01PacketJoinGame;
import com.google.common.collect.Maps;
import java.util.Random;
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
    private static final Logger logger;
    private final NetworkManager netManager;
    private final GameProfile field_175107_d;
    private final GuiScreen guiScreenServer;
    private Minecraft gameController;
    private WorldClient clientWorldController;
    private boolean doneLoadingTerrain;
    private final Map playerInfoMap;
    public int currentServerMaxPlayers;
    private boolean field_147308_k;
    private final Random avRandomizer;
    private static final String __OBFID = "CL_00000878";
    
    public NetHandlerPlayClient(final Minecraft mcIn, final GuiScreen p_i46300_2_, final NetworkManager p_i46300_3_, final GameProfile p_i46300_4_) {
        this.playerInfoMap = Maps.newHashMap();
        this.currentServerMaxPlayers = 20;
        this.field_147308_k = false;
        this.avRandomizer = new Random();
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = p_i46300_3_;
        this.field_175107_d = p_i46300_4_;
    }
    
    public void cleanup() {
        this.clientWorldController = null;
    }
    
    @Override
    public void handleJoinGame(final S01PacketJoinGame packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.func_149198_e(), false, packetIn.func_149195_d(), packetIn.func_149196_i()), packetIn.func_149194_f(), packetIn.func_149192_g(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.func_149192_g();
        this.gameController.loadWorld(this.clientWorldController);
        this.gameController.thePlayer.dimension = packetIn.func_149194_f();
        this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        this.gameController.thePlayer.setEntityId(packetIn.func_149197_c());
        this.currentServerMaxPlayers = packetIn.func_149193_h();
        this.gameController.thePlayer.func_175150_k(packetIn.func_179744_h());
        this.gameController.playerController.setGameType(packetIn.func_149198_e());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
    }
    
    @Override
    public void handleSpawnObject(final S0EPacketSpawnObject packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final double var2 = packetIn.func_148997_d() / 32.0;
        final double var3 = packetIn.func_148998_e() / 32.0;
        final double var4 = packetIn.func_148994_f() / 32.0;
        Object var5 = null;
        if (packetIn.func_148993_l() == 10) {
            var5 = EntityMinecart.func_180458_a(this.clientWorldController, var2, var3, var4, EntityMinecart.EnumMinecartType.func_180038_a(packetIn.func_149009_m()));
        }
        else if (packetIn.func_148993_l() == 90) {
            final Entity var6 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
            if (var6 instanceof EntityPlayer) {
                var5 = new EntityFishHook(this.clientWorldController, var2, var3, var4, (EntityPlayer)var6);
            }
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 60) {
            var5 = new EntityArrow(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 61) {
            var5 = new EntitySnowball(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 71) {
            var5 = new EntityItemFrame(this.clientWorldController, new BlockPos(MathHelper.floor_double(var2), MathHelper.floor_double(var3), MathHelper.floor_double(var4)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 77) {
            var5 = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(var2), MathHelper.floor_double(var3), MathHelper.floor_double(var4)));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 65) {
            var5 = new EntityEnderPearl(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 72) {
            var5 = new EntityEnderEye(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 76) {
            var5 = new EntityFireworkRocket(this.clientWorldController, var2, var3, var4, null);
        }
        else if (packetIn.func_148993_l() == 63) {
            var5 = new EntityLargeFireball(this.clientWorldController, var2, var3, var4, packetIn.func_149010_g() / 8000.0, packetIn.func_149004_h() / 8000.0, packetIn.func_148999_i() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 64) {
            var5 = new EntitySmallFireball(this.clientWorldController, var2, var3, var4, packetIn.func_149010_g() / 8000.0, packetIn.func_149004_h() / 8000.0, packetIn.func_148999_i() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 66) {
            var5 = new EntityWitherSkull(this.clientWorldController, var2, var3, var4, packetIn.func_149010_g() / 8000.0, packetIn.func_149004_h() / 8000.0, packetIn.func_148999_i() / 8000.0);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 62) {
            var5 = new EntityEgg(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 73) {
            var5 = new EntityPotion(this.clientWorldController, var2, var3, var4, packetIn.func_149009_m());
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 75) {
            var5 = new EntityExpBottle(this.clientWorldController, var2, var3, var4);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 1) {
            var5 = new EntityBoat(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 50) {
            var5 = new EntityTNTPrimed(this.clientWorldController, var2, var3, var4, null);
        }
        else if (packetIn.func_148993_l() == 78) {
            var5 = new EntityArmorStand(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 51) {
            var5 = new EntityEnderCrystal(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 2) {
            var5 = new EntityItem(this.clientWorldController, var2, var3, var4);
        }
        else if (packetIn.func_148993_l() == 70) {
            var5 = new EntityFallingBlock(this.clientWorldController, var2, var3, var4, Block.getStateById(packetIn.func_149009_m() & 0xFFFF));
            packetIn.func_149002_g(0);
        }
        if (var5 != null) {
            ((Entity)var5).serverPosX = packetIn.func_148997_d();
            ((Entity)var5).serverPosY = packetIn.func_148998_e();
            ((Entity)var5).serverPosZ = packetIn.func_148994_f();
            ((Entity)var5).rotationPitch = packetIn.func_149008_j() * 360 / 256.0f;
            ((Entity)var5).rotationYaw = packetIn.func_149006_k() * 360 / 256.0f;
            final Entity[] var7 = ((Entity)var5).getParts();
            if (var7 != null) {
                final int var8 = packetIn.func_149001_c() - ((Entity)var5).getEntityId();
                for (int var9 = 0; var9 < var7.length; ++var9) {
                    var7[var9].setEntityId(var7[var9].getEntityId() + var8);
                }
            }
            ((Entity)var5).setEntityId(packetIn.func_149001_c());
            this.clientWorldController.addEntityToWorld(packetIn.func_149001_c(), (Entity)var5);
            if (packetIn.func_149009_m() > 0) {
                if (packetIn.func_148993_l() == 60) {
                    final Entity var10 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());
                    if (var10 instanceof EntityLivingBase && var5 instanceof EntityArrow) {
                        ((EntityArrow)var5).shootingEntity = var10;
                    }
                }
                ((Entity)var5).setVelocity(packetIn.func_149010_g() / 8000.0, packetIn.func_149004_h() / 8000.0, packetIn.func_148999_i() / 8000.0);
            }
        }
    }
    
    @Override
    public void handleSpawnExperienceOrb(final S11PacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityXPOrb var2 = new EntityXPOrb(this.clientWorldController, packetIn.func_148984_d(), packetIn.func_148983_e(), packetIn.func_148982_f(), packetIn.func_148986_g());
        var2.serverPosX = packetIn.func_148984_d();
        var2.serverPosY = packetIn.func_148983_e();
        var2.serverPosZ = packetIn.func_148982_f();
        var2.rotationYaw = 0.0f;
        var2.rotationPitch = 0.0f;
        var2.setEntityId(packetIn.func_148985_c());
        this.clientWorldController.addEntityToWorld(packetIn.func_148985_c(), var2);
    }
    
    @Override
    public void handleSpawnGlobalEntity(final S2CPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final double var2 = packetIn.func_149051_d() / 32.0;
        final double var3 = packetIn.func_149050_e() / 32.0;
        final double var4 = packetIn.func_149049_f() / 32.0;
        EntityLightningBolt var5 = null;
        if (packetIn.func_149053_g() == 1) {
            var5 = new EntityLightningBolt(this.clientWorldController, var2, var3, var4);
        }
        if (var5 != null) {
            var5.serverPosX = packetIn.func_149051_d();
            var5.serverPosY = packetIn.func_149050_e();
            var5.serverPosZ = packetIn.func_149049_f();
            var5.rotationYaw = 0.0f;
            var5.rotationPitch = 0.0f;
            var5.setEntityId(packetIn.func_149052_c());
            this.clientWorldController.addWeatherEffect(var5);
        }
    }
    
    @Override
    public void handleSpawnPainting(final S10PacketSpawnPainting packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPainting var2 = new EntityPainting(this.clientWorldController, packetIn.func_179837_b(), packetIn.func_179836_c(), packetIn.func_148961_h());
        this.clientWorldController.addEntityToWorld(packetIn.func_148965_c(), var2);
    }
    
    @Override
    public void handleEntityVelocity(final S12PacketEntityVelocity packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        if (var2 != null) {
            var2.setVelocity(packetIn.getX() / 8000.0, packetIn.getY() / 8000.0, packetIn.getZ() / 8000.0);
        }
    }
    
    @Override
    public void handleEntityMetadata(final S1CPacketEntityMetadata packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149375_d());
        if (var2 != null && packetIn.func_149376_c() != null) {
            var2.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }
    
    @Override
    public void handleSpawnPlayer(final S0CPacketSpawnPlayer packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final double var2 = packetIn.func_148942_f() / 32.0;
        final double var3 = packetIn.func_148949_g() / 32.0;
        final double var4 = packetIn.func_148946_h() / 32.0;
        final float var5 = packetIn.func_148941_i() * 360 / 256.0f;
        final float var6 = packetIn.func_148945_j() * 360 / 256.0f;
        final EntityOtherPlayerMP entityOtherPlayerMP3;
        final EntityOtherPlayerMP entityOtherPlayerMP2;
        final EntityOtherPlayerMP entityOtherPlayerMP;
        final EntityOtherPlayerMP var7 = entityOtherPlayerMP = (entityOtherPlayerMP2 = (entityOtherPlayerMP3 = new EntityOtherPlayerMP(this.gameController.theWorld, this.func_175102_a(packetIn.func_179819_c()).getGameProfile())));
        final int func_148942_f = packetIn.func_148942_f();
        entityOtherPlayerMP.serverPosX = func_148942_f;
        final double n = func_148942_f;
        entityOtherPlayerMP2.lastTickPosX = n;
        entityOtherPlayerMP3.prevPosX = n;
        final EntityOtherPlayerMP entityOtherPlayerMP4 = var7;
        final EntityOtherPlayerMP entityOtherPlayerMP5 = var7;
        final EntityOtherPlayerMP entityOtherPlayerMP6 = var7;
        final int func_148949_g = packetIn.func_148949_g();
        entityOtherPlayerMP6.serverPosY = func_148949_g;
        final double n2 = func_148949_g;
        entityOtherPlayerMP5.lastTickPosY = n2;
        entityOtherPlayerMP4.prevPosY = n2;
        final EntityOtherPlayerMP entityOtherPlayerMP7 = var7;
        final EntityOtherPlayerMP entityOtherPlayerMP8 = var7;
        final EntityOtherPlayerMP entityOtherPlayerMP9 = var7;
        final int func_148946_h = packetIn.func_148946_h();
        entityOtherPlayerMP9.serverPosZ = func_148946_h;
        final double n3 = func_148946_h;
        entityOtherPlayerMP8.lastTickPosZ = n3;
        entityOtherPlayerMP7.prevPosZ = n3;
        final int var8 = packetIn.func_148947_k();
        if (var8 == 0) {
            var7.inventory.mainInventory[var7.inventory.currentItem] = null;
        }
        else {
            var7.inventory.mainInventory[var7.inventory.currentItem] = new ItemStack(Item.getItemById(var8), 1, 0);
        }
        var7.setPositionAndRotation(var2, var3, var4, var5, var6);
        this.clientWorldController.addEntityToWorld(packetIn.func_148943_d(), var7);
        final List var9 = packetIn.func_148944_c();
        if (var9 != null) {
            var7.getDataWatcher().updateWatchedObjectsFromList(var9);
        }
    }
    
    @Override
    public void handleEntityTeleport(final S18PacketEntityTeleport packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149451_c());
        if (var2 != null) {
            var2.serverPosX = packetIn.func_149449_d();
            var2.serverPosY = packetIn.func_149448_e();
            var2.serverPosZ = packetIn.func_149446_f();
            final double var3 = var2.serverPosX / 32.0;
            final double var4 = var2.serverPosY / 32.0 + 0.015625;
            final double var5 = var2.serverPosZ / 32.0;
            final float var6 = packetIn.func_149450_g() * 360 / 256.0f;
            final float var7 = packetIn.func_149447_h() * 360 / 256.0f;
            if (Math.abs(var2.posX - var3) < 0.03125 && Math.abs(var2.posY - var4) < 0.015625 && Math.abs(var2.posZ - var5) < 0.03125) {
                var2.func_180426_a(var2.posX, var2.posY, var2.posZ, var6, var7, 3, true);
            }
            else {
                var2.func_180426_a(var3, var4, var5, var6, var7, 3, true);
            }
            var2.onGround = packetIn.func_179697_g();
        }
    }
    
    @Override
    public void handleHeldItemChange(final S09PacketHeldItemChange packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.func_149385_c() >= 0 && packetIn.func_149385_c() < InventoryPlayer.getHotbarSize()) {
            this.gameController.thePlayer.inventory.currentItem = packetIn.func_149385_c();
        }
    }
    
    @Override
    public void handleEntityMovement(final S14PacketEntity packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = packetIn.func_149065_a(this.clientWorldController);
        if (var2 != null) {
            final Entity entity = var2;
            entity.serverPosX += packetIn.func_149062_c();
            final Entity entity2 = var2;
            entity2.serverPosY += packetIn.func_149061_d();
            final Entity entity3 = var2;
            entity3.serverPosZ += packetIn.func_149064_e();
            final double var3 = var2.serverPosX / 32.0;
            final double var4 = var2.serverPosY / 32.0;
            final double var5 = var2.serverPosZ / 32.0;
            final float var6 = packetIn.func_149060_h() ? (packetIn.func_149066_f() * 360 / 256.0f) : var2.rotationYaw;
            final float var7 = packetIn.func_149060_h() ? (packetIn.func_149063_g() * 360 / 256.0f) : var2.rotationPitch;
            var2.func_180426_a(var3, var4, var5, var6, var7, 3, false);
            var2.onGround = packetIn.func_179742_g();
        }
    }
    
    @Override
    public void handleEntityHeadLook(final S19PacketEntityHeadLook packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = packetIn.func_149381_a(this.clientWorldController);
        if (var2 != null) {
            final float var3 = packetIn.func_149380_c() * 360 / 256.0f;
            var2.setRotationYawHead(var3);
        }
    }
    
    @Override
    public void handleDestroyEntities(final S13PacketDestroyEntities packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        for (int var2 = 0; var2 < packetIn.func_149098_c().length; ++var2) {
            this.clientWorldController.removeEntityFromWorld(packetIn.func_149098_c()[var2]);
        }
    }
    
    @Override
    public void handlePlayerPosLook(final S08PacketPlayerPosLook packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        double var3 = packetIn.func_148932_c();
        double var4 = packetIn.func_148928_d();
        double var5 = packetIn.func_148933_e();
        float var6 = packetIn.func_148931_f();
        float var7 = packetIn.func_148930_g();
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            var3 += var2.posX;
        }
        else {
            var2.motionX = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            var4 += var2.posY;
        }
        else {
            var2.motionY = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            var5 += var2.posZ;
        }
        else {
            var2.motionZ = 0.0;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            var7 += var2.rotationPitch;
        }
        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            var6 += var2.rotationYaw;
        }
        var2.setPositionAndRotation(var3, var4, var5, var6, var7);
        this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(var2.posX, var2.getEntityBoundingBox().minY, var2.posZ, var2.rotationYaw, var2.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
            this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
            this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen(null);
        }
    }
    
    @Override
    public void handleMultiBlockChange(final S22PacketMultiBlockChange packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        for (final S22PacketMultiBlockChange.BlockUpdateData var5 : packetIn.func_179844_a()) {
            this.clientWorldController.func_180503_b(var5.func_180090_a(), var5.func_180088_c());
        }
    }
    
    @Override
    public void handleChunkData(final S21PacketChunkData packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.func_149274_i()) {
            if (packetIn.func_149276_g() == 0) {
                this.clientWorldController.doPreChunk(packetIn.func_149273_e(), packetIn.func_149271_f(), false);
                return;
            }
            this.clientWorldController.doPreChunk(packetIn.func_149273_e(), packetIn.func_149271_f(), true);
        }
        this.clientWorldController.invalidateBlockReceiveRegion(packetIn.func_149273_e() << 4, 0, packetIn.func_149271_f() << 4, (packetIn.func_149273_e() << 4) + 15, 256, (packetIn.func_149271_f() << 4) + 15);
        final Chunk var2 = this.clientWorldController.getChunkFromChunkCoords(packetIn.func_149273_e(), packetIn.func_149271_f());
        var2.func_177439_a(packetIn.func_149272_d(), packetIn.func_149276_g(), packetIn.func_149274_i());
        this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.func_149273_e() << 4, 0, packetIn.func_149271_f() << 4, (packetIn.func_149273_e() << 4) + 15, 256, (packetIn.func_149271_f() << 4) + 15);
        if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface)) {
            var2.resetRelightChecks();
        }
    }
    
    @Override
    public void handleBlockChange(final S23PacketBlockChange packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.clientWorldController.func_180503_b(packetIn.func_179827_b(), packetIn.func_180728_a());
    }
    
    @Override
    public void handleDisconnect(final S40PacketDisconnect packetIn) {
        this.netManager.closeChannel(packetIn.func_149165_c());
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        this.gameController.loadWorld(null);
        if (this.guiScreenServer != null) {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
                this.gameController.displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason).getProxy());
            }
            else {
                this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        }
        else {
            this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new ClientMainMenu()), "disconnect.lost", reason));
        }
    }
    
    public void addToSendQueue(final Packet p_147297_1_) {
        this.netManager.sendPacket(p_147297_1_);
    }
    
    @Override
    public void handleCollectItem(final S0DPacketCollectItem packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149354_c());
        Object var3 = this.clientWorldController.getEntityByID(packetIn.func_149353_d());
        if (var3 == null) {
            var3 = this.gameController.thePlayer;
        }
        if (var2 != null) {
            if (var2 instanceof EntityXPOrb) {
                this.clientWorldController.playSoundAtEntity(var2, "random.orb", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            else {
                this.clientWorldController.playSoundAtEntity(var2, "random.pop", 0.2f, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
            this.gameController.effectRenderer.addEffect(new EntityPickupFX(this.clientWorldController, var2, (Entity)var3, 0.5f));
            this.clientWorldController.removeEntityFromWorld(packetIn.func_149354_c());
        }
    }
    
    @Override
    public void handleChat(final S02PacketChat packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.func_179841_c() == 2) {
            this.gameController.ingameGUI.func_175188_a(packetIn.func_148915_c(), false);
        }
        else {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.func_148915_c());
        }
    }
    
    @Override
    public void handleAnimation(final S0BPacketAnimation packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_148978_c());
        if (var2 != null) {
            if (packetIn.func_148977_d() == 0) {
                final EntityLivingBase var3 = (EntityLivingBase)var2;
                var3.swingItem();
            }
            else if (packetIn.func_148977_d() == 1) {
                var2.performHurtAnimation();
            }
            else if (packetIn.func_148977_d() == 2) {
                final EntityPlayer var4 = (EntityPlayer)var2;
                var4.wakeUpPlayer(false, false, false);
            }
            else if (packetIn.func_148977_d() == 4) {
                this.gameController.effectRenderer.func_178926_a(var2, EnumParticleTypes.CRIT);
            }
            else if (packetIn.func_148977_d() == 5) {
                this.gameController.effectRenderer.func_178926_a(var2, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
    
    @Override
    public void handleUseBed(final S0APacketUseBed packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        packetIn.getPlayer(this.clientWorldController).func_180469_a(packetIn.func_179798_a());
    }
    
    @Override
    public void handleSpawnMob(final S0FPacketSpawnMob packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final double var2 = packetIn.func_149023_f() / 32.0;
        final double var3 = packetIn.func_149034_g() / 32.0;
        final double var4 = packetIn.func_149029_h() / 32.0;
        final float var5 = packetIn.func_149028_l() * 360 / 256.0f;
        final float var6 = packetIn.func_149030_m() * 360 / 256.0f;
        final EntityLivingBase var7 = (EntityLivingBase)EntityList.createEntityByID(packetIn.func_149025_e(), this.gameController.theWorld);
        var7.serverPosX = packetIn.func_149023_f();
        var7.serverPosY = packetIn.func_149034_g();
        var7.serverPosZ = packetIn.func_149029_h();
        var7.rotationYawHead = packetIn.func_149032_n() * 360 / 256.0f;
        final Entity[] var8 = var7.getParts();
        if (var8 != null) {
            final int var9 = packetIn.func_149024_d() - var7.getEntityId();
            for (int var10 = 0; var10 < var8.length; ++var10) {
                var8[var10].setEntityId(var8[var10].getEntityId() + var9);
            }
        }
        var7.setEntityId(packetIn.func_149024_d());
        var7.setPositionAndRotation(var2, var3, var4, var5, var6);
        var7.motionX = packetIn.func_149026_i() / 8000.0f;
        var7.motionY = packetIn.func_149033_j() / 8000.0f;
        var7.motionZ = packetIn.func_149031_k() / 8000.0f;
        this.clientWorldController.addEntityToWorld(packetIn.func_149024_d(), var7);
        final List var11 = packetIn.func_149027_c();
        if (var11 != null) {
            var7.getDataWatcher().updateWatchedObjectsFromList(var11);
        }
    }
    
    @Override
    public void handleTimeUpdate(final S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.theWorld.func_82738_a(packetIn.func_149366_c());
        this.gameController.theWorld.setWorldTime(packetIn.func_149365_d());
    }
    
    @Override
    public void handleSpawnPosition(final S05PacketSpawnPosition packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.thePlayer.func_180473_a(packetIn.func_179800_a(), true);
        this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.func_179800_a());
    }
    
    @Override
    public void handleEntityAttach(final S1BPacketEntityAttach packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        Object var2 = this.clientWorldController.getEntityByID(packetIn.func_149403_d());
        final Entity var3 = this.clientWorldController.getEntityByID(packetIn.func_149402_e());
        if (packetIn.func_149404_c() == 0) {
            boolean var4 = false;
            if (packetIn.func_149403_d() == this.gameController.thePlayer.getEntityId()) {
                var2 = this.gameController.thePlayer;
                if (var3 instanceof EntityBoat) {
                    ((EntityBoat)var3).setIsBoatEmpty(false);
                }
                var4 = (((Entity)var2).ridingEntity == null && var3 != null);
            }
            else if (var3 instanceof EntityBoat) {
                ((EntityBoat)var3).setIsBoatEmpty(true);
            }
            if (var2 == null) {
                return;
            }
            ((Entity)var2).mountEntity(var3);
            if (var4) {
                final GameSettings var5 = this.gameController.gameSettings;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(var5.keyBindSneak.getKeyCode())), false);
            }
        }
        else if (packetIn.func_149404_c() == 1 && var2 instanceof EntityLiving) {
            if (var3 != null) {
                ((EntityLiving)var2).setLeashedToEntity(var3, false);
            }
            else {
                ((EntityLiving)var2).clearLeashed(false, false);
            }
        }
    }
    
    @Override
    public void handleEntityStatus(final S19PacketEntityStatus packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = packetIn.func_149161_a(this.clientWorldController);
        if (var2 != null) {
            if (packetIn.func_149160_c() == 21) {
                this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)var2));
            }
            else {
                var2.handleHealthUpdate(packetIn.func_149160_c());
            }
        }
    }
    
    @Override
    public void handleUpdateHealth(final S06PacketUpdateHealth packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
        this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }
    
    @Override
    public void handleSetExperience(final S1FPacketSetExperience packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.func_149396_d(), packetIn.func_149395_e());
    }
    
    @Override
    public void handleRespawn(final S07PacketRespawn packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.func_149082_c() != this.gameController.thePlayer.dimension) {
            this.doneLoadingTerrain = false;
            final Scoreboard var2 = this.clientWorldController.getScoreboard();
            (this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.func_149083_e(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.func_149080_f()), packetIn.func_149082_c(), packetIn.func_149081_d(), this.gameController.mcProfiler)).setWorldScoreboard(var2);
            this.gameController.loadWorld(this.clientWorldController);
            this.gameController.thePlayer.dimension = packetIn.func_149082_c();
            this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        }
        this.gameController.setDimensionAndSpawnPlayer(packetIn.func_149082_c());
        this.gameController.playerController.setGameType(packetIn.func_149083_e());
    }
    
    @Override
    public void handleExplosion(final S27PacketExplosion packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Explosion var2 = new Explosion(this.gameController.theWorld, null, packetIn.func_149148_f(), packetIn.func_149143_g(), packetIn.func_149145_h(), packetIn.func_149146_i(), packetIn.func_149150_j());
        var2.doExplosionB(true);
        final EntityPlayerSP thePlayer = this.gameController.thePlayer;
        thePlayer.motionX += packetIn.func_149149_c();
        final EntityPlayerSP thePlayer2 = this.gameController.thePlayer;
        thePlayer2.motionY += packetIn.func_149144_d();
        final EntityPlayerSP thePlayer3 = this.gameController.thePlayer;
        thePlayer3.motionZ += packetIn.func_149147_e();
    }
    
    @Override
    public void handleOpenWindow(final S2DPacketOpenWindow packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        if ("minecraft:container".equals(packetIn.func_148902_e())) {
            var2.displayGUIChest(new InventoryBasic(packetIn.func_179840_c(), packetIn.func_148898_f()));
            var2.openContainer.windowId = packetIn.func_148901_c();
        }
        else if ("minecraft:villager".equals(packetIn.func_148902_e())) {
            var2.displayVillagerTradeGui(new NpcMerchant(var2, packetIn.func_179840_c()));
            var2.openContainer.windowId = packetIn.func_148901_c();
        }
        else if ("EntityHorse".equals(packetIn.func_148902_e())) {
            final Entity var3 = this.clientWorldController.getEntityByID(packetIn.func_148897_h());
            if (var3 instanceof EntityHorse) {
                var2.displayGUIHorse((EntityHorse)var3, new AnimalChest(packetIn.func_179840_c(), packetIn.func_148898_f()));
                var2.openContainer.windowId = packetIn.func_148901_c();
            }
        }
        else if (!packetIn.func_148900_g()) {
            var2.displayGui(new LocalBlockIntercommunication(packetIn.func_148902_e(), packetIn.func_179840_c()));
            var2.openContainer.windowId = packetIn.func_148901_c();
        }
        else {
            final ContainerLocalMenu var4 = new ContainerLocalMenu(packetIn.func_148902_e(), packetIn.func_179840_c(), packetIn.func_148898_f());
            var2.displayGUIChest(var4);
            var2.openContainer.windowId = packetIn.func_148901_c();
        }
    }
    
    @Override
    public void handleSetSlot(final S2FPacketSetSlot packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        if (packetIn.func_149175_c() == -1) {
            var2.inventory.setItemStack(packetIn.func_149174_e());
        }
        else {
            boolean var3 = false;
            if (this.gameController.currentScreen instanceof GuiContainerCreative) {
                final GuiContainerCreative var4 = (GuiContainerCreative)this.gameController.currentScreen;
                var3 = (var4.func_147056_g() != CreativeTabs.tabInventory.getTabIndex());
            }
            if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45) {
                final ItemStack var5 = var2.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();
                if (packetIn.func_149174_e() != null && (var5 == null || var5.stackSize < packetIn.func_149174_e().stackSize)) {
                    packetIn.func_149174_e().animationsToGo = 5;
                }
                var2.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
            else if (packetIn.func_149175_c() == var2.openContainer.windowId && (packetIn.func_149175_c() != 0 || !var3)) {
                var2.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
        }
    }
    
    @Override
    public void handleConfirmTransaction(final S32PacketConfirmTransaction packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        Container var2 = null;
        final EntityPlayerSP var3 = this.gameController.thePlayer;
        if (packetIn.func_148889_c() == 0) {
            var2 = var3.inventoryContainer;
        }
        else if (packetIn.func_148889_c() == var3.openContainer.windowId) {
            var2 = var3.openContainer;
        }
        if (var2 != null && !packetIn.func_148888_e()) {
            this.addToSendQueue(new C0FPacketConfirmTransaction(packetIn.func_148889_c(), packetIn.func_148890_d(), true));
        }
    }
    
    @Override
    public void handleWindowItems(final S30PacketWindowItems packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        if (packetIn.func_148911_c() == 0) {
            var2.inventoryContainer.putStacksInSlots(packetIn.func_148910_d());
        }
        else if (packetIn.func_148911_c() == var2.openContainer.windowId) {
            var2.openContainer.putStacksInSlots(packetIn.func_148910_d());
        }
    }
    
    @Override
    public void handleSignEditorOpen(final S36PacketSignEditorOpen packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        Object var2 = this.clientWorldController.getTileEntity(packetIn.func_179777_a());
        if (!(var2 instanceof TileEntitySign)) {
            var2 = new TileEntitySign();
            ((TileEntity)var2).setWorldObj(this.clientWorldController);
            ((TileEntity)var2).setPos(packetIn.func_179777_a());
        }
        this.gameController.thePlayer.func_175141_a((TileEntitySign)var2);
    }
    
    @Override
    public void handleUpdateSign(final S33PacketUpdateSign packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        boolean var2 = false;
        if (this.gameController.theWorld.isBlockLoaded(packetIn.func_179704_a())) {
            final TileEntity var3 = this.gameController.theWorld.getTileEntity(packetIn.func_179704_a());
            if (var3 instanceof TileEntitySign) {
                final TileEntitySign var4 = (TileEntitySign)var3;
                if (var4.getIsEditable()) {
                    System.arraycopy(packetIn.func_180753_b(), 0, var4.signText, 0, 4);
                    var4.markDirty();
                }
                var2 = true;
            }
        }
        if (!var2 && this.gameController.thePlayer != null) {
            this.gameController.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at " + packetIn.func_179704_a().getX() + ", " + packetIn.func_179704_a().getY() + ", " + packetIn.func_179704_a().getZ()));
        }
    }
    
    @Override
    public void handleUpdateTileEntity(final S35PacketUpdateTileEntity packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (this.gameController.theWorld.isBlockLoaded(packetIn.func_179823_a())) {
            final TileEntity var2 = this.gameController.theWorld.getTileEntity(packetIn.func_179823_a());
            final int var3 = packetIn.getTileEntityType();
            if ((var3 == 1 && var2 instanceof TileEntityMobSpawner) || (var3 == 2 && var2 instanceof TileEntityCommandBlock) || (var3 == 3 && var2 instanceof TileEntityBeacon) || (var3 == 4 && var2 instanceof TileEntitySkull) || (var3 == 5 && var2 instanceof TileEntityFlowerPot) || (var3 == 6 && var2 instanceof TileEntityBanner)) {
                var2.readFromNBT(packetIn.getNbtCompound());
            }
        }
    }
    
    @Override
    public void handleWindowProperty(final S31PacketWindowProperty packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        if (var2.openContainer != null && var2.openContainer.windowId == packetIn.func_149182_c()) {
            var2.openContainer.updateProgressBar(packetIn.func_149181_d(), packetIn.func_149180_e());
        }
    }
    
    @Override
    public void handleEntityEquipment(final S04PacketEntityEquipment packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149389_d());
        if (var2 != null) {
            var2.setCurrentItemOrArmor(packetIn.func_149388_e(), packetIn.func_149390_c());
        }
    }
    
    @Override
    public void handleCloseWindow(final S2EPacketCloseWindow packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.thePlayer.func_175159_q();
    }
    
    @Override
    public void handleBlockAction(final S24PacketBlockAction packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.theWorld.addBlockEvent(packetIn.func_179825_a(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }
    
    @Override
    public void handleBlockBreakAnim(final S25PacketBlockBreakAnim packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.theWorld.sendBlockBreakProgress(packetIn.func_148845_c(), packetIn.func_179821_b(), packetIn.func_148846_g());
    }
    
    @Override
    public void handleMapChunkBulk(final S26PacketMapChunkBulk packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        for (int var2 = 0; var2 < packetIn.func_149254_d(); ++var2) {
            final int var3 = packetIn.func_149255_a(var2);
            final int var4 = packetIn.func_149253_b(var2);
            this.clientWorldController.doPreChunk(var3, var4, true);
            this.clientWorldController.invalidateBlockReceiveRegion(var3 << 4, 0, var4 << 4, (var3 << 4) + 15, 256, (var4 << 4) + 15);
            final Chunk var5 = this.clientWorldController.getChunkFromChunkCoords(var3, var4);
            var5.func_177439_a(packetIn.func_149256_c(var2), packetIn.func_179754_d(var2), true);
            this.clientWorldController.markBlockRangeForRenderUpdate(var3 << 4, 0, var4 << 4, (var3 << 4) + 15, 256, (var4 << 4) + 15);
            if (!(this.clientWorldController.provider instanceof WorldProviderSurface)) {
                var5.resetRelightChecks();
            }
        }
    }
    
    @Override
    public void handleChangeGameState(final S2BPacketChangeGameState packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        final int var3 = packetIn.func_149138_c();
        final float var4 = packetIn.func_149137_d();
        final int var5 = MathHelper.floor_float(var4 + 0.5f);
        if (var3 >= 0 && var3 < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[var3] != null) {
            var2.addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[var3], new Object[0]));
        }
        if (var3 == 1) {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0f);
        }
        else if (var3 == 2) {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0f);
        }
        else if (var3 == 3) {
            this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(var5));
        }
        else if (var3 == 4) {
            this.gameController.displayGuiScreen(new GuiWinGame());
        }
        else if (var3 == 5) {
            final GameSettings var6 = this.gameController.gameSettings;
            if (var4 == 0.0f) {
                this.gameController.displayGuiScreen(new GuiScreenDemo());
            }
            else if (var4 == 101.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(var6.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindRight.getKeyCode()) }));
            }
            else if (var4 == 102.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(var6.keyBindJump.getKeyCode()) }));
            }
            else if (var4 == 103.0f) {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(var6.keyBindInventory.getKeyCode()) }));
            }
        }
        else if (var3 == 6) {
            this.clientWorldController.playSound(var2.posX, var2.posY + var2.getEyeHeight(), var2.posZ, "random.successful_hit", 0.18f, 0.45f, false);
        }
        else if (var3 == 7) {
            this.clientWorldController.setRainStrength(var4);
        }
        else if (var3 == 8) {
            this.clientWorldController.setThunderStrength(var4);
        }
        else if (var3 == 10) {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, var2.posX, var2.posY, var2.posZ, 0.0, 0.0, 0.0, new int[0]);
            this.clientWorldController.playSound(var2.posX, var2.posY, var2.posZ, "mob.guardian.curse", 1.0f, 1.0f, false);
        }
    }
    
    @Override
    public void handleMaps(final S34PacketMaps packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final MapData var2 = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
        packetIn.func_179734_a(var2);
        this.gameController.entityRenderer.getMapItemRenderer().func_148246_a(var2);
    }
    
    @Override
    public void handleEffect(final S28PacketEffect packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.isSoundServerwide()) {
            this.gameController.theWorld.func_175669_a(packetIn.getSoundType(), packetIn.func_179746_d(), packetIn.getSoundData());
        }
        else {
            this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.func_179746_d(), packetIn.getSoundData());
        }
    }
    
    @Override
    public void handleStatistics(final S37PacketStatistics packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        boolean var2 = false;
        for (final Map.Entry var4 : packetIn.func_148974_c().entrySet()) {
            final StatBase var5 = var4.getKey();
            final int var6 = var4.getValue();
            if (var5.isAchievement() && var6 > 0) {
                if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().writeStat(var5) == 0) {
                    final Achievement var7 = (Achievement)var5;
                    this.gameController.guiAchievement.displayAchievement(var7);
                    this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(var7), 0L);
                    if (var5 == AchievementList.openInventory) {
                        this.gameController.gameSettings.showInventoryAchievementHint = false;
                        this.gameController.gameSettings.saveOptions();
                    }
                }
                var2 = true;
            }
            this.gameController.thePlayer.getStatFileWriter().func_150873_a(this.gameController.thePlayer, var5, var6);
        }
        if (!this.field_147308_k && !var2 && this.gameController.gameSettings.showInventoryAchievementHint) {
            this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }
        this.field_147308_k = true;
        if (this.gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter)this.gameController.currentScreen).doneLoading();
        }
    }
    
    @Override
    public void handleEntityEffect(final S1DPacketEntityEffect packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149426_d());
        if (var2 instanceof EntityLivingBase) {
            final PotionEffect var3 = new PotionEffect(packetIn.func_149427_e(), packetIn.func_180755_e(), packetIn.func_149428_f(), false, packetIn.func_179707_f());
            var3.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase)var2).addPotionEffect(var3);
        }
    }
    
    @Override
    public void func_175098_a(final S42PacketCombatEvent p_175098_1_) {
        PacketThreadUtil.func_180031_a(p_175098_1_, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(p_175098_1_.field_179775_c);
        final EntityLivingBase var3 = (var2 instanceof EntityLivingBase) ? ((EntityLivingBase)var2) : null;
        if (p_175098_1_.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT) {
            final long var4 = 1000 * p_175098_1_.field_179772_d / 20;
            final MetadataCombat var5 = new MetadataCombat(this.gameController.thePlayer, var3);
            this.gameController.getTwitchStream().func_176026_a(var5, 0L - var4, 0L);
        }
        else if (p_175098_1_.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED) {
            final Entity var6 = this.clientWorldController.getEntityByID(p_175098_1_.field_179774_b);
            if (var6 instanceof EntityPlayer) {
                final MetadataPlayerDeath var7 = new MetadataPlayerDeath((EntityLivingBase)var6, var3);
                var7.func_152807_a(p_175098_1_.field_179773_e);
                this.gameController.getTwitchStream().func_152911_a(var7, 0L);
            }
        }
    }
    
    @Override
    public void func_175101_a(final S41PacketServerDifficulty p_175101_1_) {
        PacketThreadUtil.func_180031_a(p_175101_1_, this, this.gameController);
        this.gameController.theWorld.getWorldInfo().setDifficulty(p_175101_1_.func_179831_b());
        this.gameController.theWorld.getWorldInfo().setDifficultyLocked(p_175101_1_.func_179830_a());
    }
    
    @Override
    public void func_175094_a(final S43PacketCamera p_175094_1_) {
        PacketThreadUtil.func_180031_a(p_175094_1_, this, this.gameController);
        final Entity var2 = p_175094_1_.func_179780_a(this.clientWorldController);
        if (var2 != null) {
            this.gameController.func_175607_a(var2);
        }
    }
    
    @Override
    public void func_175093_a(final S44PacketWorldBorder p_175093_1_) {
        PacketThreadUtil.func_180031_a(p_175093_1_, this, this.gameController);
        p_175093_1_.func_179788_a(this.clientWorldController.getWorldBorder());
    }
    
    @Override
    public void func_175099_a(final S45PacketTitle p_175099_1_) {
        PacketThreadUtil.func_180031_a(p_175099_1_, this, this.gameController);
        final S45PacketTitle.Type var2 = p_175099_1_.func_179807_a();
        String var3 = null;
        String var4 = null;
        final String var5 = (p_175099_1_.func_179805_b() != null) ? p_175099_1_.func_179805_b().getFormattedText() : "";
        switch (SwitchAction.field_178885_a[var2.ordinal()]) {
            case 1: {
                var3 = var5;
                break;
            }
            case 2: {
                var4 = var5;
                break;
            }
            case 3: {
                this.gameController.ingameGUI.func_175178_a("", "", -1, -1, -1);
                this.gameController.ingameGUI.func_175177_a();
                return;
            }
        }
        this.gameController.ingameGUI.func_175178_a(var3, var4, p_175099_1_.func_179806_c(), p_175099_1_.func_179804_d(), p_175099_1_.func_179803_e());
    }
    
    @Override
    public void func_175100_a(final S46PacketSetCompressionLevel p_175100_1_) {
        if (!this.netManager.isLocalChannel()) {
            this.netManager.setCompressionTreshold(p_175100_1_.func_179760_a());
        }
    }
    
    @Override
    public void func_175096_a(final S47PacketPlayerListHeaderFooter p_175096_1_) {
        this.gameController.ingameGUI.getTabList().setHeader((p_175096_1_.func_179700_a().getFormattedText().length() == 0) ? null : p_175096_1_.func_179700_a());
        this.gameController.ingameGUI.getTabList().setFooter((p_175096_1_.func_179701_b().getFormattedText().length() == 0) ? null : p_175096_1_.func_179701_b());
    }
    
    @Override
    public void handleRemoveEntityEffect(final S1EPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149076_c());
        if (var2 instanceof EntityLivingBase) {
            ((EntityLivingBase)var2).removePotionEffectClient(packetIn.func_149075_d());
        }
    }
    
    @Override
    public void handlePlayerListItem(final S38PacketPlayerListItem packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        for (final S38PacketPlayerListItem.AddPlayerData var3 : packetIn.func_179767_a()) {
            if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                this.playerInfoMap.remove(var3.func_179962_a().getId());
            }
            else {
                NetworkPlayerInfo var4 = this.playerInfoMap.get(var3.func_179962_a().getId());
                if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                    var4 = new NetworkPlayerInfo(var3);
                    this.playerInfoMap.put(var4.getGameProfile().getId(), var4);
                }
                if (var4 == null) {
                    continue;
                }
                switch (SwitchAction.field_178884_b[packetIn.func_179768_b().ordinal()]) {
                    case 1: {
                        var4.setGameType(var3.func_179960_c());
                        var4.setResponseTime(var3.func_179963_b());
                        continue;
                    }
                    case 2: {
                        var4.setGameType(var3.func_179960_c());
                        continue;
                    }
                    case 3: {
                        var4.setResponseTime(var3.func_179963_b());
                        continue;
                    }
                    case 4: {
                        var4.setDisplayName(var3.func_179961_d());
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void handleKeepAlive(final S00PacketKeepAlive packetIn) {
        this.addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }
    
    @Override
    public void handlePlayerAbilities(final S39PacketPlayerAbilities packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final EntityPlayerSP var2 = this.gameController.thePlayer;
        var2.capabilities.isFlying = packetIn.isFlying();
        var2.capabilities.isCreativeMode = packetIn.isCreativeMode();
        var2.capabilities.disableDamage = packetIn.isInvulnerable();
        var2.capabilities.allowFlying = packetIn.isAllowFlying();
        var2.capabilities.setFlySpeed(packetIn.getFlySpeed());
        var2.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }
    
    @Override
    public void handleTabComplete(final S3APacketTabComplete packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final String[] var2 = packetIn.func_149630_c();
        if (this.gameController.currentScreen instanceof GuiChat) {
            final GuiChat var3 = (GuiChat)this.gameController.currentScreen;
            var3.onAutocompleteResponse(var2);
        }
    }
    
    @Override
    public void handleSoundEffect(final S29PacketSoundEffect packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        this.gameController.theWorld.playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getName(), packetIn.func_149208_g(), packetIn.func_149209_h(), false);
    }
    
    @Override
    public void func_175095_a(final S48PacketResourcePackSend p_175095_1_) {
        final String var2 = p_175095_1_.func_179783_a();
        final String var3 = p_175095_1_.func_179784_b();
        if (var2.startsWith("level://")) {
            final String var4 = var2.substring("level://".length());
            final File var5 = new File(this.gameController.mcDataDir, "saves");
            final File var6 = new File(var5, var4);
            if (var6.isFile()) {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(this.gameController.getResourcePackRepository().func_177319_a(var6), (FutureCallback)new FutureCallback() {
                    private static final String __OBFID = "CL_00000879";
                    
                    public void onSuccess(final Object p_onSuccess_1_) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                    }
                    
                    public void onFailure(final Throwable p_onFailure_1_) {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                    }
                });
            }
            else {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        }
        else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback(this.gameController.getResourcePackRepository().func_180601_a(var2, var3), (FutureCallback)new FutureCallback() {
                private static final String __OBFID = "CL_00002624";
                
                public void onSuccess(final Object p_onSuccess_1_) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                }
                
                public void onFailure(final Throwable p_onFailure_1_) {
                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                }
            });
        }
        else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
            this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.DECLINED));
        }
        else {
            this.gameController.addScheduledTask(new Runnable() {
                private static final String __OBFID = "CL_00002623";
                
                @Override
                public void run() {
                    NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                        private static final String __OBFID = "CL_00002622";
                        
                        @Override
                        public void confirmClicked(final boolean result, final int id) {
                            NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
                            if (result) {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.ACCEPTED));
                                Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().func_180601_a(var2, var3), (FutureCallback)new FutureCallback() {
                                    private static final String __OBFID = "CL_00002621";
                                    
                                    public void onSuccess(final Object p_onSuccess_1_) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                    }
                                    
                                    public void onFailure(final Throwable p_onFailure_1_) {
                                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                    }
                                });
                            }
                            else {
                                if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
                                    NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                }
                                NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(var3, C19PacketResourcePackStatus.Action.DECLINED));
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
    public void func_175097_a(final S49PacketUpdateEntityNBT p_175097_1_) {
        PacketThreadUtil.func_180031_a(p_175097_1_, this, this.gameController);
        final Entity var2 = p_175097_1_.func_179764_a(this.clientWorldController);
        if (var2 != null) {
            var2.func_174834_g(p_175097_1_.func_179763_a());
        }
    }
    
    @Override
    public void handleCustomPayload(final S3FPacketCustomPayload packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if ("MC|TrList".equals(packetIn.getChannelName())) {
            final PacketBuffer var2 = packetIn.getBufferData();
            try {
                final int var3 = var2.readInt();
                final GuiScreen var4 = this.gameController.currentScreen;
                if (var4 != null && var4 instanceof GuiMerchant && var3 == this.gameController.thePlayer.openContainer.windowId) {
                    final IMerchant var5 = ((GuiMerchant)var4).getMerchant();
                    final MerchantRecipeList var6 = MerchantRecipeList.func_151390_b(var2);
                    var5.setRecipes(var6);
                }
            }
            catch (IOException var7) {
                NetHandlerPlayClient.logger.error("Couldn't load trade info", (Throwable)var7);
            }
            finally {
                var2.release();
            }
        }
        else if ("MC|Brand".equals(packetIn.getChannelName())) {
            this.gameController.thePlayer.func_175158_f(packetIn.getBufferData().readStringFromBuffer(32767));
        }
        else if ("MC|BOpen".equals(packetIn.getChannelName())) {
            final ItemStack var8 = this.gameController.thePlayer.getCurrentEquippedItem();
            if (var8 != null && var8.getItem() == Items.written_book) {
                this.gameController.displayGuiScreen(new GuiScreenBook(this.gameController.thePlayer, var8, false));
            }
        }
    }
    
    @Override
    public void handleScoreboardObjective(final S3BPacketScoreboardObjective packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Scoreboard var2 = this.clientWorldController.getScoreboard();
        if (packetIn.func_149338_e() == 0) {
            final ScoreObjective var3 = var2.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
            var3.setDisplayName(packetIn.func_149337_d());
            var3.func_178767_a(packetIn.func_179817_d());
        }
        else {
            final ScoreObjective var3 = var2.getObjective(packetIn.func_149339_c());
            if (packetIn.func_149338_e() == 1) {
                var2.func_96519_k(var3);
            }
            else if (packetIn.func_149338_e() == 2) {
                var3.setDisplayName(packetIn.func_149337_d());
                var3.func_178767_a(packetIn.func_179817_d());
            }
        }
    }
    
    @Override
    public void handleUpdateScore(final S3CPacketUpdateScore packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Scoreboard var2 = this.clientWorldController.getScoreboard();
        final ScoreObjective var3 = var2.getObjective(packetIn.func_149321_d());
        if (packetIn.func_180751_d() == S3CPacketUpdateScore.Action.CHANGE) {
            final Score var4 = var2.getValueFromObjective(packetIn.func_149324_c(), var3);
            var4.setScorePoints(packetIn.func_149323_e());
        }
        else if (packetIn.func_180751_d() == S3CPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(packetIn.func_149321_d())) {
                var2.func_178822_d(packetIn.func_149324_c(), null);
            }
            else if (var3 != null) {
                var2.func_178822_d(packetIn.func_149324_c(), var3);
            }
        }
    }
    
    @Override
    public void handleDisplayScoreboard(final S3DPacketDisplayScoreboard packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Scoreboard var2 = this.clientWorldController.getScoreboard();
        if (packetIn.func_149370_d().length() == 0) {
            var2.setObjectiveInDisplaySlot(packetIn.func_149371_c(), null);
        }
        else {
            final ScoreObjective var3 = var2.getObjective(packetIn.func_149370_d());
            var2.setObjectiveInDisplaySlot(packetIn.func_149371_c(), var3);
        }
    }
    
    @Override
    public void handleTeams(final S3EPacketTeams packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Scoreboard var2 = this.clientWorldController.getScoreboard();
        ScorePlayerTeam var3;
        if (packetIn.func_149307_h() == 0) {
            var3 = var2.createTeam(packetIn.func_149312_c());
        }
        else {
            var3 = var2.getTeam(packetIn.func_149312_c());
        }
        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2) {
            var3.setTeamName(packetIn.func_149306_d());
            var3.setNamePrefix(packetIn.func_149311_e());
            var3.setNameSuffix(packetIn.func_149309_f());
            var3.func_178774_a(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
            var3.func_98298_a(packetIn.func_149308_i());
            final Team.EnumVisible var4 = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());
            if (var4 != null) {
                var3.func_178772_a(var4);
            }
        }
        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3) {
            for (final String var6 : packetIn.func_149310_g()) {
                var2.func_151392_a(var6, packetIn.func_149312_c());
            }
        }
        if (packetIn.func_149307_h() == 4) {
            for (final String var6 : packetIn.func_149310_g()) {
                var2.removePlayerFromTeam(var6, var3);
            }
        }
        if (packetIn.func_149307_h() == 1) {
            var2.removeTeam(var3);
        }
    }
    
    @Override
    public void handleParticles(final S2APacketParticles packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        if (packetIn.func_149222_k() == 0) {
            final double var2 = packetIn.func_149227_j() * packetIn.func_149221_g();
            final double var3 = packetIn.func_149227_j() * packetIn.func_149224_h();
            final double var4 = packetIn.func_149227_j() * packetIn.func_149223_i();
            try {
                this.clientWorldController.spawnParticle(packetIn.func_179749_a(), packetIn.func_179750_b(), packetIn.func_149220_d(), packetIn.func_149226_e(), packetIn.func_149225_f(), var2, var3, var4, packetIn.func_179748_k());
            }
            catch (Throwable var12) {
                NetHandlerPlayClient.logger.warn("Could not spawn particle effect " + packetIn.func_179749_a());
            }
        }
        else {
            for (int var5 = 0; var5 < packetIn.func_149222_k(); ++var5) {
                final double var6 = this.avRandomizer.nextGaussian() * packetIn.func_149221_g();
                final double var7 = this.avRandomizer.nextGaussian() * packetIn.func_149224_h();
                final double var8 = this.avRandomizer.nextGaussian() * packetIn.func_149223_i();
                final double var9 = this.avRandomizer.nextGaussian() * packetIn.func_149227_j();
                final double var10 = this.avRandomizer.nextGaussian() * packetIn.func_149227_j();
                final double var11 = this.avRandomizer.nextGaussian() * packetIn.func_149227_j();
                try {
                    this.clientWorldController.spawnParticle(packetIn.func_179749_a(), packetIn.func_179750_b(), packetIn.func_149220_d() + var6, packetIn.func_149226_e() + var7, packetIn.func_149225_f() + var8, var9, var10, var11, packetIn.func_179748_k());
                }
                catch (Throwable var13) {
                    NetHandlerPlayClient.logger.warn("Could not spawn particle effect " + packetIn.func_179749_a());
                    return;
                }
            }
        }
    }
    
    @Override
    public void handleEntityProperties(final S20PacketEntityProperties packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.gameController);
        final Entity var2 = this.clientWorldController.getEntityByID(packetIn.func_149442_c());
        if (var2 != null) {
            if (!(var2 instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + var2 + ")");
            }
            final BaseAttributeMap var3 = ((EntityLivingBase)var2).getAttributeMap();
            for (final S20PacketEntityProperties.Snapshot var5 : packetIn.func_149441_d()) {
                IAttributeInstance var6 = var3.getAttributeInstanceByName(var5.func_151409_a());
                if (var6 == null) {
                    var6 = var3.registerAttribute(new RangedAttribute(null, var5.func_151409_a(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                var6.setBaseValue(var5.func_151410_b());
                var6.removeAllModifiers();
                for (final AttributeModifier var8 : var5.func_151408_c()) {
                    var6.applyModifier(var8);
                }
            }
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public Collection func_175106_d() {
        return this.playerInfoMap.values();
    }
    
    public NetworkPlayerInfo func_175102_a(final UUID p_175102_1_) {
        return this.playerInfoMap.get(p_175102_1_);
    }
    
    public NetworkPlayerInfo func_175104_a(final String p_175104_1_) {
        for (final NetworkPlayerInfo var3 : this.playerInfoMap.values()) {
            if (var3.getGameProfile().getName().equals(p_175104_1_)) {
                return var3;
            }
        }
        return null;
    }
    
    public GameProfile func_175105_e() {
        return this.field_175107_d;
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    static final class SwitchAction
    {
        static final int[] field_178885_a;
        static final int[] field_178884_b;
        private static final String __OBFID = "CL_00002620";
        
        static {
            field_178884_b = new int[S38PacketPlayerListItem.Action.values().length];
            try {
                SwitchAction.field_178884_b[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchAction.field_178884_b[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchAction.field_178884_b[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchAction.field_178884_b[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            field_178885_a = new int[S45PacketTitle.Type.values().length];
            try {
                SwitchAction.field_178885_a[S45PacketTitle.Type.TITLE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchAction.field_178885_a[S45PacketTitle.Type.SUBTITLE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchAction.field_178885_a[S45PacketTitle.Type.RESET.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
        }
    }
}
