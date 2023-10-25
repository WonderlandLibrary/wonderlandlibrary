package ru.salam4ik.bot.bot.network;

import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import io.netty.util.internal.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
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
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.lwjgl.input.Keyboard;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.entity.BotController;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.world.BotWorld;
import wtf.evolution.Main;
import wtf.evolution.bot.ProxyS;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.notifications.NotificationType;

import static ru.salam4ik.bot.bot.BotStarter.mc;

public class BotPlayClient
        implements INetHandlerPlayClient {
    public boolean left;
    private BotController botController;
    public final MovementInput movementInput;
    private BotPlayer bot;
    private ArrayList<String> solved = new ArrayList<>();
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap;
    private BotNetwork netManager;
    public boolean right;
    private final GameProfile profile;
    private BotWorld world;
    public boolean backward;
    public boolean forward;
    private boolean doneLoadingTerrain;
    public boolean jump;
    public boolean sneak;

    @Override
    public void handleSpawnMob(SPacketSpawnMob sPacketSpawnMob) {
        double d = sPacketSpawnMob.getX();
        double d2 = sPacketSpawnMob.getY();
        double d3 = sPacketSpawnMob.getZ();
        float f = (float)(sPacketSpawnMob.getYaw() * 360) / 256.0f;
        float f2 = (float)(sPacketSpawnMob.getPitch() * 360) / 256.0f;
        EntityLivingBase entityLivingBase = (EntityLivingBase)EntityList.createEntityByID(sPacketSpawnMob.getEntityType(), this.world);
        if (entityLivingBase != null) {
            EntityTracker.updateServerPosition(entityLivingBase, d, d2, d3);
            entityLivingBase.renderYawOffset = (float)(sPacketSpawnMob.getHeadPitch() * 360) / 256.0f;
            entityLivingBase.rotationYawHead = (float)(sPacketSpawnMob.getHeadPitch() * 360) / 256.0f;
            Entity[] entityArray = entityLivingBase.getParts();
            if (entityArray != null) {
                int n = sPacketSpawnMob.getEntityID() - entityLivingBase.getEntityId();
                for (Entity entity : entityArray) {
                    entity.setEntityId(entity.getEntityId() + n);
                }
            }
            entityLivingBase.setEntityId(sPacketSpawnMob.getEntityID());
            entityLivingBase.setUniqueId(sPacketSpawnMob.getUniqueId());
            entityLivingBase.setPositionAndRotation(d, d2, d3, f, f2);
            entityLivingBase.motionX = (float)sPacketSpawnMob.getVelocityX() / 8000.0f;
            entityLivingBase.motionY = (float)sPacketSpawnMob.getVelocityY() / 8000.0f;
            entityLivingBase.motionZ = (float)sPacketSpawnMob.getVelocityZ() / 8000.0f;
            this.world.addEntityToWorld(sPacketSpawnMob.getEntityID(), entityLivingBase);
            List<EntityDataManager.DataEntry<?>> list = sPacketSpawnMob.getDataManagerEntries();
            if (list != null) {
                entityLivingBase.getDataManager().setEntryValues(list);
            }
        }
    }

    @Override
    public void handleEffect(SPacketEffect sPacketEffect) {
        if (sPacketEffect.isSoundServerwide()) {
            this.world.playBroadcastSound(sPacketEffect.getSoundType(), sPacketEffect.getSoundPos(), sPacketEffect.getSoundData());
        } else {
            this.world.playEvent(sPacketEffect.getSoundType(), sPacketEffect.getSoundPos(), sPacketEffect.getSoundData());
        }
    }

    @Override
    public void handleServerDifficulty(SPacketServerDifficulty sPacketServerDifficulty) {
        this.world.getWorldInfo().setDifficulty(sPacketServerDifficulty.getDifficulty());
        this.world.getWorldInfo().setDifficultyLocked(sPacketServerDifficulty.isDifficultyLocked());
    }

    @Override
    public void handleEntityMetadata(SPacketEntityMetadata sPacketEntityMetadata) {
        Entity entity = this.world.getEntityByID(sPacketEntityMetadata.getEntityId());
        if (entity != null && sPacketEntityMetadata.getDataManagerEntries() != null) {
            entity.getDataManager().setEntryValues(sPacketEntityMetadata.getDataManagerEntries());
        }
    }

    public BotPlayer getBot() {
        return this.bot;
    }

    @Override
    public void handlePlayerPosLook(SPacketPlayerPosLook sPacketPlayerPosLook) {
        BotPlayer botPlayer = this.bot;
        double d = sPacketPlayerPosLook.getX();
        double d2 = sPacketPlayerPosLook.getY();
        double d3 = sPacketPlayerPosLook.getZ();
        float f = sPacketPlayerPosLook.getYaw();
        float f2 = sPacketPlayerPosLook.getPitch();
        if (sPacketPlayerPosLook.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.X)) {
            d += botPlayer.posX;
        } else {
            botPlayer.motionX = 0.0;
        }
        if (sPacketPlayerPosLook.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Y)) {
            d2 += botPlayer.posY;
        } else {
            botPlayer.motionY = 0.0;
        }
        if (sPacketPlayerPosLook.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Z)) {
            d3 += botPlayer.posZ;
        } else {
            botPlayer.motionZ = 0.0;
        }
        if (sPacketPlayerPosLook.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 += botPlayer.rotationPitch;
        }
        if (sPacketPlayerPosLook.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += botPlayer.rotationYaw;
        }
        botPlayer.setPositionAndRotation(d, d2, d3, f, f2);
        this.netManager.sendPacket(new CPacketConfirmTeleport(sPacketPlayerPosLook.getTeleportId()));
        this.netManager.sendPacket(new CPacketPlayer.PositionRotation(botPlayer.posX, botPlayer.getEntityBoundingBox().minY, botPlayer.posZ, botPlayer.rotationYaw, botPlayer.rotationPitch, false));
        if (!this.doneLoadingTerrain) {
            this.bot.prevPosX = this.bot.posX;
            this.bot.prevPosY = this.bot.posY;
            this.bot.prevPosZ = this.bot.posZ;
            this.doneLoadingTerrain = true;
        }
    }

    @Override
    public void handleBlockBreakAnim(SPacketBlockBreakAnim sPacketBlockBreakAnim) {
        world.sendBlockBreakProgress(sPacketBlockBreakAnim.getBreakerId(), sPacketBlockBreakAnim.getPosition(), sPacketBlockBreakAnim.getProgress());
    }

    @Override
    public void handleEntityMovement(SPacketEntity sPacketEntity) {
        Entity entity = sPacketEntity.getEntity(this.world);
        if (entity != null) {
            entity.serverPosX += (long)sPacketEntity.getX();
            entity.serverPosY += (long)sPacketEntity.getY();
            entity.serverPosZ += (long)sPacketEntity.getZ();
            double d = (double)entity.serverPosX / 4096.0;
            double d2 = (double)entity.serverPosY / 4096.0;
            double d3 = (double)entity.serverPosZ / 4096.0;
            if (!entity.canPassengerSteer()) {
                float f = sPacketEntity.isRotating() ? (float)(sPacketEntity.getYaw() * 360) / 256.0f : entity.rotationYaw;
                float f2 = sPacketEntity.isRotating() ? (float)(sPacketEntity.getPitch() * 360) / 256.0f : entity.rotationPitch;
                entity.setPositionAndRotationDirect(d, d2, d3, f, f2, 3, false);
                entity.onGround = sPacketEntity.getOnGround();
            }
        }
    }

    @Override
    public void handleJoinGame(SPacketJoinGame sPacketJoinGame) {
        this.botController = new BotController(this);
        this.world = new BotWorld(this, new WorldSettings(0L, sPacketJoinGame.getGameType(), true, sPacketJoinGame.isHardcoreMode(), sPacketJoinGame.getWorldType()), sPacketJoinGame.getDimension(), sPacketJoinGame.getDifficulty());
        this.loadWorld(this.world);
        this.bot.dimension = sPacketJoinGame.getDimension();
        this.bot.setEntityId(sPacketJoinGame.getPlayerId());
        this.bot.setReducedDebug(sPacketJoinGame.isReducedDebugInfo());
        this.botController.setGameType(sPacketJoinGame.getGameType());
        this.sendPacket(new CPacketClientSettings("ru_RU", 4, EntityPlayer.EnumChatVisibility.FULL, true, 0, EnumHandSide.RIGHT));
        this.netManager.sendPacket(new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
        this.world.setBot(this.bot);
        Main.notify.call(bot.getName(), "Connected!", NotificationType.INFO);
        Bot.bots.add(new Bot(this.netManager, this, this.botController, this.bot, this.world));

        this.netManager.connection = true;
    }

    @Override
    public void handleCooldown(SPacketCooldown sPacketCooldown) {
        if (sPacketCooldown.getTicks() == 0) {
            this.bot.getCooldownTracker().removeCooldown(sPacketCooldown.getItem());
        } else {
            this.bot.getCooldownTracker().setCooldown(sPacketCooldown.getItem(), sPacketCooldown.getTicks());
        }
    }

    @Override
    public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter sPacketPlayerListHeaderFooter) {
    }


    @Override
    public void handleTabComplete(SPacketTabComplete sPacketTabComplete) {
    }

    @Override
    public void handleWorldBorder(SPacketWorldBorder sPacketWorldBorder) {
        sPacketWorldBorder.apply(this.world.getWorldBorder());
    }

    @Override
    public void handleConfirmTransaction(SPacketConfirmTransaction sPacketConfirmTransaction) {
        Container container = null;
        BotPlayer botPlayer = this.bot;
        if (sPacketConfirmTransaction.getWindowId() == 0) {
            container = botPlayer.inventoryContainer;
        } else if (sPacketConfirmTransaction.getWindowId() == botPlayer.openContainer.windowId) {
            container = botPlayer.openContainer;
        }
        if (container != null && !sPacketConfirmTransaction.wasAccepted()) {
            this.sendPacket(new CPacketConfirmTransaction(sPacketConfirmTransaction.getWindowId(), sPacketConfirmTransaction.getActionNumber(), true));
        }
    }

    @Override
    public void handleCollectItem(SPacketCollectItem sPacketCollectItem) {
    }

    @Override
    public void handleTeams(SPacketTeams sPacketTeams) {
    }

    @Override
    public void handleChangeGameState(SPacketChangeGameState sPacketChangeGameState) {
        BotPlayer botPlayer = this.bot;
        int n = sPacketChangeGameState.getGameState();
        float f = sPacketChangeGameState.getValue();
        int n2 = MathHelper.floor(f + 0.5f);
        if (n >= 0 && n < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[n] != null) {

        }
        if (n == 1) {
            this.world.getWorldInfo().setRaining(true);
            this.world.setRainStrength(0.0f);
        } else if (n == 2) {
            this.world.getWorldInfo().setRaining(false);
            this.world.setRainStrength(1.0f);
        } else if (n == 3) {
            this.botController.setGameType(GameType.getByID(n2));
        } else if (n == 4) {
            if (n2 == 0) {
                this.bot.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            }
        } else if (n == 7) {
            this.world.setRainStrength(f);
        } else if (n == 8) {
            this.world.setThunderStrength(f);
        }
    }

    @Override
    public void handleHeldItemChange(SPacketHeldItemChange sPacketHeldItemChange) {
        if (InventoryPlayer.isHotbar(sPacketHeldItemChange.getHeldItemHotbarIndex())) {
            this.bot.inventory.currentItem = sPacketHeldItemChange.getHeldItemHotbarIndex();
        }
    }

    @Override
    public void handleEntityAttach(SPacketEntityAttach sPacketEntityAttach) {
        Entity entity = this.world.getEntityByID(sPacketEntityAttach.getEntityId());
        Entity entity2 = this.world.getEntityByID(sPacketEntityAttach.getVehicleEntityId());
        if (entity instanceof EntityLiving) {
            if (entity2 != null) {
                ((EntityLiving)entity).setLeashHolder(entity2, false);
            } else {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }

    @Override
    public void handleParticles(SPacketParticles sPacketParticles) {
    }



    @Override
    public void handleCombatEvent(SPacketCombatEvent sPacketCombatEvent) {
        Entity entity;
        if (sPacketCombatEvent.eventType == SPacketCombatEvent.Event.ENTITY_DIED && (entity = this.world.getEntityByID(sPacketCombatEvent.playerId)) == this.bot) {
            this.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
        }
    }

    public BotPlayClient(BotNetwork botNetwork, GameProfile gameProfile) {
        this.playerInfoMap = Maps.newHashMap();
        this.movementInput = new MovementInput(){

            @Override
            public void updatePlayerMoveState() {
                this.moveForward = 0.0f;
                this.moveStrafe = 0.0f;
                this.jump = false;
                this.sneak = false;
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                    if ((Keyboard.isKeyDown(72) || Keyboard.isKeyDown(200) || BotPlayClient.this.forward)) {
                        this.moveForward += 1.0f;
                    }
                    if ((Keyboard.isKeyDown(76) || Keyboard.isKeyDown(208) || BotPlayClient.this.backward)) {
                        this.moveForward -= 1.0f;
                    }
                    if ((Keyboard.isKeyDown(75) || Keyboard.isKeyDown(203) || BotPlayClient.this.left)) {
                        this.moveStrafe += 1.0f;
                    }
                    if ((Keyboard.isKeyDown(77) || Keyboard.isKeyDown(205) || BotPlayClient.this.right)) {
                        this.moveStrafe -= 1.0f;
                    }
                    if ((Keyboard.isKeyDown(79) || BotPlayClient.this.jump) || bot.isInWater()) {
                        this.jump = true;
                    }
                    if ((Keyboard.isKeyDown(81) || BotPlayClient.this.sneak)) {
                        this.sneak = true;
                        this.moveStrafe = (float) ((double) this.moveStrafe * 0.3);
                        this.moveForward = (float) ((double) this.moveForward * 0.3);
                    }
                }
            }
        };
        this.netManager = botNetwork;
        this.profile = gameProfile;
    }

    @Override
    public void onDisconnect(ITextComponent iTextComponent) {
        Bot.bots.remove(this.getBotAboba());
        this.netManager.closeChannel();
        new Thread(() -> {
            ProxyS.Proxy proxy = getBotAboba().getNetManager().p;
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), getGameProfile().getName());
            try {
                BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
                botNetwork.setNetHandler(new BotLoginClient(botNetwork));
                botNetwork.sendPacket(new C00Handshake(GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
                botNetwork.sendPacket(new CPacketLoginStart(gameProfile));
            }
            catch (Exception exception) {

            }
        }).start();
    }

    public void sendPacket(Packet<?> packet) {
        this.netManager.sendPacket(packet);
    }

    @Override
    public void handleResourcePack(SPacketResourcePackSend sPacketResourcePackSend) {
    }

    @Override
    public void handleUpdateBossInfo(SPacketUpdateBossInfo packetIn) {

    }

    private void loadWorld(BotWorld botWorld) {
        this.world = botWorld;
        this.bot = new BotPlayer(this);
        this.botController.flipPlayer(this.bot);
        this.bot.preparePlayerToSpawn();
        this.world.spawnEntity(this.bot);
        this.botController.setPlayerCapabilities(this.bot);
        this.bot.movementInput = this.movementInput;
        this.world.setBot(this.bot);
    }

    @Override
    public void handleSetExperience(SPacketSetExperience sPacketSetExperience) {
        this.bot.setXPStats(sPacketSetExperience.getExperienceBar(), sPacketSetExperience.getTotalExperience(), sPacketSetExperience.getLevel());
    }

    @Override
    public void handleKeepAlive(SPacketKeepAlive sPacketKeepAlive) {
        this.sendPacket(new CPacketKeepAlive(sPacketKeepAlive.getId()));
    }

    public BotNetwork getNetworkManager() {
        return this.netManager;
    }

    @Override
    public void handleOpenWindow(SPacketOpenWindow sPacketOpenWindow) {
        PacketThreadUtil.checkThreadAndEnqueue(sPacketOpenWindow, this, Minecraft.getMinecraft());
        this.bot.currentContainerName = sPacketOpenWindow.getWindowTitle().getUnformattedText();
        if ("minecraft:container".equals(sPacketOpenWindow.getGuiId())) {
            this.bot.displayGUIChest(new InventoryBasic(sPacketOpenWindow.getWindowTitle(), sPacketOpenWindow.getSlotCount()));
            this.bot.openContainer.windowId = sPacketOpenWindow.getWindowId();
        } else if ("minecraft:villager".equals(sPacketOpenWindow.getGuiId())) {
            this.bot.displayVillagerTradeGui(new NpcMerchant(this.bot, sPacketOpenWindow.getWindowTitle()));
            this.bot.openContainer.windowId = sPacketOpenWindow.getWindowId();

        } else if ("EntityHorse".equals(sPacketOpenWindow.getGuiId())) {
            Entity entity = this.world.getEntityByID(sPacketOpenWindow.getEntityId());
            if (entity instanceof AbstractHorse) {
                this.bot.openContainer.windowId = sPacketOpenWindow.getWindowId();

            }
        } else if (!sPacketOpenWindow.hasSlots()) {
            this.bot.displayGui(new LocalBlockIntercommunication(sPacketOpenWindow.getGuiId(), sPacketOpenWindow.getWindowTitle()));
            this.bot.openContainer.windowId = sPacketOpenWindow.getWindowId();
            this.bot.openContainer.name = sPacketOpenWindow.getWindowTitle().getUnformattedText();
        } else {
            ContainerLocalMenu containerLocalMenu = new ContainerLocalMenu(sPacketOpenWindow.getGuiId(), sPacketOpenWindow.getWindowTitle(), sPacketOpenWindow.getSlotCount());
            this.bot.displayGUIChest(containerLocalMenu);
            this.bot.openContainer.windowId = sPacketOpenWindow.getWindowId();
            this.bot.openContainer.name = sPacketOpenWindow.getWindowTitle().getUnformattedText();
        }
    }

    @Override
    public void handleWindowProperty(SPacketWindowProperty sPacketWindowProperty) {
        BotPlayer botPlayer = this.bot;
        if (botPlayer.openContainer != null && botPlayer.openContainer.windowId == sPacketWindowProperty.getWindowId()) {
            botPlayer.openContainer.updateProgressBar(sPacketWindowProperty.getProperty(), sPacketWindowProperty.getValue());
        }
    }

    @Override
    public void handleBlockAction(SPacketBlockAction sPacketBlockAction) {
        this.world.addBlockEvent(sPacketBlockAction.getBlockPosition(), sPacketBlockAction.getBlockType(), sPacketBlockAction.getData1(), sPacketBlockAction.getData2());
    }

    public BotController getController() {
        return this.botController;
    }

    @Override
    public void handleTitle(SPacketTitle sPacketTitle) {
    }

    public GameProfile getGameProfile() {
        return this.profile;
    }

    public NetworkPlayerInfo getPlayerInfo(UUID uUID) {
        return this.playerInfoMap.get(uUID);
    }

    @Override
    public void handleEntityVelocity(SPacketEntityVelocity sPacketEntityVelocity) {
        Entity entity = this.world.getEntityByID(sPacketEntityVelocity.getEntityID());
        if (entity != null) {
            entity.setVelocity((double)sPacketEntityVelocity.getMotionX() / 8000.0, (double)sPacketEntityVelocity.getMotionY() / 8000.0, (double)sPacketEntityVelocity.getMotionZ() / 8000.0);
        }
    }

    @Override
    public void handleEntityEffect(SPacketEntityEffect sPacketEntityEffect) {
        Potion potion;
        Entity entity = this.world.getEntityByID(sPacketEntityEffect.getEntityId());
        if (entity instanceof EntityLivingBase && (potion = Potion.getPotionById(sPacketEntityEffect.getEffectId())) != null) {
            PotionEffect potionEffect = new PotionEffect(potion, sPacketEntityEffect.getDuration(), sPacketEntityEffect.getAmplifier(), sPacketEntityEffect.getIsAmbient(), sPacketEntityEffect.doesShowParticles());
            potionEffect.setPotionDurationMax(sPacketEntityEffect.isMaxDuration());
            ((EntityLivingBase)entity).addPotionEffect(potionEffect);
        }
    }

    @Override
    public void handleEntityHeadLook(SPacketEntityHeadLook sPacketEntityHeadLook) {
        Entity entity = sPacketEntityHeadLook.getEntity(this.world);
        if (entity != null) {
            float f = (float)(sPacketEntityHeadLook.getYaw() * 360) / 256.0f;
            entity.setRotationYawHead(f);
        }
    }

    @Override
    public void handleStatistics(SPacketStatistics sPacketStatistics) {
    }

    @Override
    public void handleRecipeBook(SPacketRecipeBook packetIn) {

    }

    @Override
    public void handleEntityTeleport(SPacketEntityTeleport sPacketEntityTeleport) {
        Entity entity = this.world.getEntityByID(sPacketEntityTeleport.getEntityId());
        if (entity != null) {
            double d = sPacketEntityTeleport.getX();
            double d2 = sPacketEntityTeleport.getY();
            double d3 = sPacketEntityTeleport.getZ();
            EntityTracker.updateServerPosition(entity, d, d2, d3);
            if (!entity.canPassengerSteer()) {
                float f = (float)(sPacketEntityTeleport.getYaw() * 360) / 256.0f;
                float f2 = (float)(sPacketEntityTeleport.getPitch() * 360) / 256.0f;
                if (Math.abs(entity.posX - d) < 0.03125 && Math.abs(entity.posY - d2) < 0.015625 && Math.abs(entity.posZ - d3) < 0.03125) {
                    entity.setPositionAndRotationDirect(entity.posX, entity.posY, entity.posZ, f, f2, 0, true);
                } else {
                    entity.setPositionAndRotationDirect(d, d2, d3, f, f2, 3, true);
                }
                entity.onGround = sPacketEntityTeleport.getOnGround();
            }
        }
    }

    @Override
    public void handleWindowItems(SPacketWindowItems sPacketWindowItems) {
        BotPlayer botPlayer = this.bot;
        if (sPacketWindowItems.getWindowId() == 0) {
            botPlayer.inventoryContainer.setAll(sPacketWindowItems.getItemStacks());
        } else if (sPacketWindowItems.getWindowId() == botPlayer.openContainer.windowId) {
            botPlayer.openContainer.setAll(sPacketWindowItems.getItemStacks());
        }
    }

    @Override
    public void handleSignEditorOpen(SPacketSignEditorOpen sPacketSignEditorOpen) {
        TileEntity tileEntity = this.world.getTileEntity(sPacketSignEditorOpen.getSignPosition());
        if (!(tileEntity instanceof TileEntitySign)) {
            tileEntity = new TileEntitySign();
            tileEntity.setWorld(this.world);
            tileEntity.setPos(sPacketSignEditorOpen.getSignPosition());
        }
        this.bot.openEditSign((TileEntitySign)tileEntity);
    }

    @Override
    public void handleSetPassengers(SPacketSetPassengers sPacketSetPassengers) {
        Entity entity = this.world.getEntityByID(sPacketSetPassengers.getEntityId());
        if (entity != null) {
            entity.removePassengers();
            for (int n : sPacketSetPassengers.getPassengerIds()) {
                Entity entity2 = this.world.getEntityByID(n);
                if (entity2 == null) continue;
                entity2.startRiding(entity, true);
            }
        }
    }

    private void setDimensionAndSpawnPlayer(int n) {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        this.world.removeEntity(this.bot);
        this.bot = new BotPlayer(this);
        this.bot.getDataManager().setEntryValues(Objects.requireNonNull(this.bot.getDataManager().getAll()));
        this.bot.dimension = n;
        this.bot.preparePlayerToSpawn();
        this.world.spawnEntity(this.bot);
        this.botController.flipPlayer(this.bot);
        try {
            this.bot.movementInput = this.movementInput;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.botController.setPlayerCapabilities(this.bot);
        this.bot.setReducedDebug(this.bot.hasReducedDebug());
        this.world.setBot(this.bot);
    }

    @Override
    public void handleSoundEffect(SPacketSoundEffect sPacketSoundEffect) {
    }

    @Override
    public void handleMultiBlockChange(SPacketMultiBlockChange sPacketMultiBlockChange) {
        for (SPacketMultiBlockChange.BlockUpdateData blockUpdateData : sPacketMultiBlockChange.getChangedBlocks()) {
            this.world.invalidateRegionAndSetBlock(blockUpdateData.getPos(), blockUpdateData.getBlockState());
        }
    }

    @Override
    public void handleCustomPayload(SPacketCustomPayload sPacketCustomPayload) {
        if ("MC|Brand".equals(sPacketCustomPayload.getChannelName())) {
            this.bot.setServerBrand(sPacketCustomPayload.getBufferData().readString(Short.MAX_VALUE));
        }
    }

    @Override
    public void handleUpdateTileEntity(SPacketUpdateTileEntity sPacketUpdateTileEntity) {
        if (this.world.isBlockLoaded(sPacketUpdateTileEntity.getPos())) {
            boolean bl;
            TileEntity tileEntity = this.world.getTileEntity(sPacketUpdateTileEntity.getPos());
            int n = sPacketUpdateTileEntity.getTileEntityType();
            boolean bl2 = bl = n == 2 && tileEntity instanceof TileEntityCommandBlock;
            if (n == 1 && tileEntity instanceof TileEntityMobSpawner || bl || n == 3 && tileEntity instanceof TileEntityBeacon || n == 4 && tileEntity instanceof TileEntitySkull || n == 5 && tileEntity instanceof TileEntityFlowerPot || n == 6 && tileEntity instanceof TileEntityBanner || n == 7 && tileEntity instanceof TileEntityStructure || n == 8 && tileEntity instanceof TileEntityEndGateway || n == 9 && tileEntity instanceof TileEntitySign || n == 10 && tileEntity instanceof TileEntityShulkerBox || n == 11 && tileEntity instanceof TileEntityBed) {
                tileEntity.readFromNBT(sPacketUpdateTileEntity.getNbtCompound());
            }
        }
    }

    @Override
    public void handleDisplayObjective(SPacketDisplayObjective sPacketDisplayObjective) {
    }

    @Override
    public void func_194307_a(SPacketPlaceGhostRecipe sPacketPlaceGhostRecipe) {
    }

    @Override
    public void handleCamera(SPacketCamera sPacketCamera) {
    }

    @Override
    public void handleDestroyEntities(SPacketDestroyEntities sPacketDestroyEntities) {
        for (int i = 0; i < sPacketDestroyEntities.getEntityIDs().length; ++i) {
            this.world.removeEntityFromWorld(sPacketDestroyEntities.getEntityIDs()[i]);
        }
    }

    @Override
    public void handleChunkData(SPacketChunkData sPacketChunkData) {
        if (sPacketChunkData.isFullChunk()) {
            this.world.doPreChunk(sPacketChunkData.getChunkX(), sPacketChunkData.getChunkZ(), true);
        }
        this.world.invalidateBlockReceiveRegion(sPacketChunkData.getChunkX() << 4, 0, sPacketChunkData.getChunkZ() << 4, (sPacketChunkData.getChunkX() << 4) + 15, 256, (sPacketChunkData.getChunkZ() << 4) + 15);
        Chunk chunk = this.world.getChunk(sPacketChunkData.getChunkX(), sPacketChunkData.getChunkZ());
        chunk.read(sPacketChunkData.getReadBuffer(), sPacketChunkData.getExtractedSize(), sPacketChunkData.isFullChunk());
        this.world.markBlockRangeForRenderUpdate(sPacketChunkData.getChunkX() << 4, 0, sPacketChunkData.getChunkZ() << 4, (sPacketChunkData.getChunkX() << 4) + 15, 256, (sPacketChunkData.getChunkZ() << 4) + 15);
        if (!sPacketChunkData.isFullChunk() || !(this.world.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
        for (NBTTagCompound nBTTagCompound : sPacketChunkData.getTileEntityTags()) {
            BlockPos blockPos = new BlockPos(nBTTagCompound.getInteger("x"), nBTTagCompound.getInteger("y"), nBTTagCompound.getInteger("z"));
            TileEntity tileEntity = this.world.getTileEntity(blockPos);
            if (tileEntity == null) continue;
            tileEntity.readFromNBT(nBTTagCompound);
        }
    }

    @Override
    public void handlePlayerAbilities(SPacketPlayerAbilities sPacketPlayerAbilities) {
        BotPlayer botPlayer = this.bot;
        botPlayer.capabilities.isFlying = sPacketPlayerAbilities.isFlying();
        botPlayer.capabilities.isCreativeMode = sPacketPlayerAbilities.isCreativeMode();
        botPlayer.capabilities.disableDamage = sPacketPlayerAbilities.isInvulnerable();
        botPlayer.capabilities.allowFlying = sPacketPlayerAbilities.isAllowFlying();
        botPlayer.capabilities.setFlySpeed(sPacketPlayerAbilities.getFlySpeed());
        botPlayer.capabilities.setPlayerWalkSpeed(sPacketPlayerAbilities.getWalkSpeed());
    }

    @Override
    public void handleAnimation(SPacketAnimation sPacketAnimation) {
    }

    @Override
    public void handleSpawnObject(SPacketSpawnObject sPacketSpawnObject) {

    }

    @Override
    public void handleScoreboardObjective(SPacketScoreboardObjective sPacketScoreboardObjective) {
    }

    @Override
    public void handleMaps(SPacketMaps sPacketMaps) {
        if (reg) return;
           Object object;
           MapData mapData = ItemMap.loadMapData(sPacketMaps.getMapId(), this.bot.world);
           if (mapData == null) {
               object = "map_" + sPacketMaps.getMapId();
               mapData = new MapData((String)object);
               this.bot.world.setData((String)object, mapData);
           }
           sPacketMaps.setMapdataTo(mapData);
           object = new BufferedImage(128, 128, 2);
           byte[] byArray = mapData.colors;
           for (int i = 0; i < 128; ++i) {
               for (int j = 0; j < 128; ++j) {
                   byte by = byArray[i + j * 128];
                   int n = by >>> 2 & 0x1F;
                   byte by2 = (byte)(by & 3);
                   BasicColor basicColor = BasicColor.colors.get(n);
                   if (basicColor == null) {
                       basicColor = BasicColor.TRANSPARENT;
                   }
                   ((BufferedImage)object).setRGB(i, j, basicColor.shaded(by2));
               }
           }
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           try {
               ImageIO.write((RenderedImage)object, "png", byteArrayOutputStream);
               String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
               new Thread(() -> {
                   try {
                       sendPost(base64);
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
               }).start();
           }
           catch (Exception exception) {
               exception.printStackTrace();
           }

    }

    @Override
    public void handleEntityProperties(SPacketEntityProperties sPacketEntityProperties) {
        Entity entity = this.world.getEntityByID(sPacketEntityProperties.getEntityId());
        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException(String.valueOf(new StringBuilder().append("Server tried to update attributes of a non-living entity (actually: ").append(entity).append(")")));
            }
            AbstractAttributeMap abstractAttributeMap = ((EntityLivingBase)entity).getAttributeMap();
            for (SPacketEntityProperties.Snapshot snapshot : sPacketEntityProperties.getSnapshots()) {
                IAttributeInstance iAttributeInstance = abstractAttributeMap.getAttributeInstanceByName(snapshot.getName());
                if (iAttributeInstance == null) {
                    iAttributeInstance = abstractAttributeMap.registerAttribute(new RangedAttribute(null, snapshot.getName(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
                }
                iAttributeInstance.setBaseValue(snapshot.getBaseValue());
                iAttributeInstance.removeAllModifiers();
                for (AttributeModifier attributeModifier : snapshot.getModifiers()) {
                    iAttributeInstance.applyModifier(attributeModifier);
                }
            }
        }
    }



    @Override
    public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb sPacketSpawnExperienceOrb) {

    }

    @Override
    public void handleBlockChange(SPacketBlockChange sPacketBlockChange) {
        this.world.invalidateRegionAndSetBlock(sPacketBlockChange.getBlockPosition(), sPacketBlockChange.getBlockState());
    }

    private Bot getBotAboba() {
        Bot bot = null;
        for (Bot bot2 : Bot.bots) {
            if (!bot2.getBot().getDisplayName().getUnformattedComponentText().equalsIgnoreCase(this.bot.getDisplayName().getUnformattedText())) continue;
            bot = bot2;
        }
        return bot;
    }

    public void cleanup() {
        this.world = null;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return this.playerInfoMap.values();
    }

    @Override
    public void handleSpawnPlayer(SPacketSpawnPlayer sPacketSpawnPlayer) {
        double d = sPacketSpawnPlayer.getX();
        double d2 = sPacketSpawnPlayer.getY();
        double d3 = sPacketSpawnPlayer.getZ();
        float f = (float)(sPacketSpawnPlayer.getYaw() * 360) / 256.0f;
        float f2 = (float)(sPacketSpawnPlayer.getPitch() * 360) / 256.0f;
        EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(this.world, this.getPlayerInfo(sPacketSpawnPlayer.getUniqueId()).getGameProfile());
        entityOtherPlayerMP.prevPosX = d;
        entityOtherPlayerMP.lastTickPosX = d;
        entityOtherPlayerMP.prevPosY = d2;
        entityOtherPlayerMP.lastTickPosY = d2;
        entityOtherPlayerMP.prevPosZ = d3;
        entityOtherPlayerMP.lastTickPosZ = d3;
        EntityTracker.updateServerPosition(entityOtherPlayerMP, d, d2, d3);
        entityOtherPlayerMP.setPositionAndRotation(d, d2, d3, f, f2);
        this.world.addEntityToWorld(sPacketSpawnPlayer.getEntityID(), entityOtherPlayerMP);
        List<EntityDataManager.DataEntry<?>> list = sPacketSpawnPlayer.getDataManagerEntries();
        if (list != null) {
            entityOtherPlayerMP.getDataManager().setEntryValues(list);
        }
    }

    @Override
    public void processChunkUnload(SPacketUnloadChunk sPacketUnloadChunk) {
        this.world.doPreChunk(sPacketUnloadChunk.getX(), sPacketUnloadChunk.getZ(), false);
    }

    @Override
    public void handleUpdateScore(SPacketUpdateScore sPacketUpdateScore) {
    }

    @Override
    public void handleUseBed(SPacketUseBed sPacketUseBed) {
        sPacketUseBed.getPlayer(this.world).trySleep(sPacketUseBed.getBedPosition());
    }

    @Override
    public void handleSpawnPosition(SPacketSpawnPosition sPacketSpawnPosition) {
        this.bot.setSpawnPoint(sPacketSpawnPosition.getSpawnPos(), true);
        this.world.getWorldInfo().setSpawn(sPacketSpawnPosition.getSpawnPos());
    }

    @Override
    public void handleRespawn(SPacketRespawn sPacketRespawn) {
        Bot.bots.removeIf(bot -> bot.getConnection().equals(this));
        int n = this.bot.getEntityId();
        String string = this.bot.getServerBrand();
        if (sPacketRespawn.getDimensionID() != this.bot.dimension) {
            this.doneLoadingTerrain = false;
            this.world = new BotWorld(this, new WorldSettings(0L, sPacketRespawn.getGameType(), false, this.world.getWorldInfo().isHardcoreModeEnabled(), sPacketRespawn.getWorldType()), sPacketRespawn.getDimensionID(), sPacketRespawn.getDifficulty());
            this.loadWorld(this.world);
            this.bot.dimension = sPacketRespawn.getDimensionID();
        }
        this.setDimensionAndSpawnPlayer(sPacketRespawn.getDimensionID());
        this.botController.setGameType(sPacketRespawn.getGameType());
        this.bot.setEntityId(n);
        this.bot.setServerBrand(string);
        this.world.setBot(this.bot);
        Bot.bots.add(new Bot(this.netManager, this, this.botController, this.bot, this.world));
    }

    @Override
    public void handleSpawnPainting(SPacketSpawnPainting sPacketSpawnPainting) {
        EntityPainting entityPainting = new EntityPainting(this.world, sPacketSpawnPainting.getPosition(), sPacketSpawnPainting.getFacing(), sPacketSpawnPainting.getTitle());
        entityPainting.setUniqueId(sPacketSpawnPainting.getUniqueId());
        this.world.addEntityToWorld(sPacketSpawnPainting.getEntityID(), entityPainting);
    }

    @Override
    public void handleDisconnect(SPacketDisconnect sPacketDisconnect) {
        ChatUtil.print(getBotAboba().getBot().getName() + " | " + sPacketDisconnect.getReason().getFormattedText());
        Bot.bots.remove(getBotAboba());
        this.netManager.closeChannel();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ProxyS.Proxy proxy = this.netManager.p;
            GameProfile gameProfile = new GameProfile(getGameProfile().getId(), getGameProfile().getName());
            try {
                netManager = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
                netManager.setNetHandler(new BotLoginClient(netManager));
                netManager.sendPacket(new C00Handshake(GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
                Thread.sleep(300);
                netManager.sendPacket(new CPacketLoginStart(gameProfile));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();

    }

    @Override
    public void handleSetSlot(SPacketSetSlot sPacketSetSlot) {
        BotPlayer botPlayer = this.bot;
        ItemStack itemstack = sPacketSetSlot.getStack();
        int i = sPacketSetSlot.getSlot();
        mc.getTutorial().handleSetSlot(itemstack);

        if (sPacketSetSlot.getWindowId() == -1)
        {
            bot.inventory.setItemStack(itemstack);
        }
        else if (sPacketSetSlot.getWindowId() == -2)
        {
            bot.inventory.setInventorySlotContents(i, itemstack);
        }
        else
        {
            boolean flag = false;

            if (mc.currentScreen instanceof GuiContainerCreative)
            {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative)mc.currentScreen;
                flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex();
            }

            if (sPacketSetSlot.getWindowId() == 0 && sPacketSetSlot.getSlot() >= 36 && i < 45)
            {
                if (!itemstack.isEmpty())
                {
                    ItemStack itemstack1 = bot.inventoryContainer.getSlot(i).getStack();

                    if (itemstack1.isEmpty() || itemstack1.getCount() < itemstack.getCount())
                    {
                        itemstack.setAnimationsToGo(5);
                    }
                }

                bot.inventoryContainer.putStackInSlot(i, itemstack);
            }
            else if (sPacketSetSlot.getWindowId() == bot.openContainer.windowId && (sPacketSetSlot.getWindowId() != 0 || !flag))
            {
                bot.openContainer.putStackInSlot(i, itemstack);
            }
        }
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    public static String stripColor(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public boolean reg;


    @Override
    public void handleChat(SPacketChat sPacketChat) {
        String string = stripColor(sPacketChat.getChatComponent().getFormattedText());
        if (!infinitySolved)
            getInfinitySolve(getBotAboba(), string);
    if (string.contains("/reg") && !reg) {
        reg = true;
        new Thread(() -> {
            String pass = RandomStringUtils.randomAlphabetic(wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(6, 8)).toLowerCase();
            this.bot.sendChatMessage(String.format("/register %s %s", pass, pass));
            ChatUtil.print("§b" + bot.getName() + " §dSuccessfully registered!");

        }).start();

        }
    }

    public boolean infinitySolved;

    public void getInfinitySolve(Bot bot, String message) {
        new Thread(() -> {
            String[] solve1 = new String[]{
                    "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳"
            };


            String[] solve2 = new String[]{
                    "⑴", "⑵", "⑶", "⑷", "⑸", "⑹", "⑺", "⑻", "⑼", "⑽", "⑾", "⑿", "⒀", "⒁", "⒂", "⒃", "⒄", "⒅", "⒆", "⒇"
            };
            String[] solve3 = new String[]{
                    "⒈", "⒉", "⒊", "⒋", "⒌", "⒍", "⒎", "⒏", "⒐", "⒑", "⒒", "⒓", "⒔", "⒕", "⒖", "⒗", "⒘", "⒙", "⒚", "⒛"
            };
            String[] solve4 = new String[]{
                    "ноль", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"
            };


            String[] solve5 = new String[]{
                    "⓫", "⓬", "⓭", "⓮", "⓯", "⓰", "⓱", "⓲", "⓳", "⓴"
            };
            if (message.contains("Решите пример: ")) {

                String text = message.replaceAll("Решите пример: ", "");

                text = text.replaceAll("\\.", "");
                text = text.replaceAll("!", "");
                text = text.replaceAll(" ", "");
                text = text.replaceAll("_", "");
                text = text.replaceAll("-", "");
                text = text.replaceAll("_", "");
                String[] split1 = text.split("\\+");
                int solve = 0;
                for (String s : solve1) {
                    solve++;
                    if (split1[1].equalsIgnoreCase(s)) {
                        text = text.replaceAll(s, String.valueOf(solve));
                    }
                }
                solve = 0;
                for (String s : solve2) {
                    solve++;
                    if (split1[1].equalsIgnoreCase(s)) {
                        text = text.replaceAll(s, String.valueOf(solve));
                    }
                }
                solve = 0;
                for (String s : solve3) {
                    solve++;
                    if (split1[1].equalsIgnoreCase(s)) {
                        text = text.replaceAll(s, String.valueOf(solve));
                    }
                }
                solve = -1;
                for (String s : solve4) {
                    solve++;
                    if (split1[1].equalsIgnoreCase(s)) {
                        text = text.replaceAll(s, String.valueOf(solve));
                    }

                }
                solve = 10;
                for (String s : solve5) {
                    solve++;
                    if (split1[1].equalsIgnoreCase(s)) {
                        text = text.replaceAll(s, String.valueOf(solve));
                    }
                }
                Main.notify.call("Solved", bot.getBot().getName() + " " + text , NotificationType.INFO);
                String[] split = text.split("\\+");
                System.out.println(message);

                bot.getBot().sendChatMessage(String.valueOf(Integer.parseInt(split[0]) + Integer.parseInt(split[1])));
                infinitySolved = true;
            }
        }).start();

    }


    @Override
    public void handleCustomSound(SPacketCustomSound sPacketCustomSound) {
    }

    public BotWorld getWorld() {
        return this.world;
    }

    @Override
    public void handleMoveVehicle(SPacketMoveVehicle sPacketMoveVehicle) {
        Entity entity = this.bot.getLowestRidingEntity();
        if (entity != this.bot && entity.canPassengerSteer()) {
            entity.setPositionAndRotation(sPacketMoveVehicle.getX(), sPacketMoveVehicle.getY(), sPacketMoveVehicle.getZ(), sPacketMoveVehicle.getYaw(), sPacketMoveVehicle.getPitch());
            this.netManager.sendPacket(new CPacketVehicleMove(entity));
        }
    }

    @Override
    public void handleAdvancementInfo(SPacketAdvancementInfo packetIn) {

    }

    @Override
    public void handleSelectAdvancementsTab(SPacketSelectAdvancementsTab packetIn) {

    }



    @Override
    public void handleCloseWindow(SPacketCloseWindow sPacketCloseWindow) {
        this.bot.closeScreenAndDropStack();
    }

    @Override
    public void handleUpdateHealth(SPacketUpdateHealth sPacketUpdateHealth) {
        this.bot.setPlayerSPHealth(sPacketUpdateHealth.getHealth());
        this.bot.getFoodStats().setFoodLevel(sPacketUpdateHealth.getFoodLevel());
        this.bot.getFoodStats().setFoodSaturationLevel(sPacketUpdateHealth.getSaturationLevel());
    }

    @Override
    public void handleExplosion(SPacketExplosion sPacketExplosion) {
        Explosion explosion = new Explosion(this.world, null, sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ(), sPacketExplosion.getStrength(), sPacketExplosion.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        this.bot.motionX += (double)sPacketExplosion.getMotionX();
        this.bot.motionY += (double)sPacketExplosion.getMotionY();
        this.bot.motionZ += (double)sPacketExplosion.getMotionZ();
    }

    @Override
    public void handleRemoveEntityEffect(SPacketRemoveEntityEffect sPacketRemoveEntityEffect) {
        Entity entity = sPacketRemoveEntityEffect.getEntity(this.world);
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removeActivePotionEffect(sPacketRemoveEntityEffect.getPotion());
        }
    }

    @Override
    public void handleEntityStatus(SPacketEntityStatus sPacketEntityStatus) {
        Entity entity = sPacketEntityStatus.getEntity(this.world);
        if (entity != null && sPacketEntityStatus.getOpCode() != 21 && sPacketEntityStatus.getOpCode() != 35) {
            entity.handleStatusUpdate(sPacketEntityStatus.getOpCode());
        }
    }


    @Override
    public void handlePlayerListItem(SPacketPlayerListItem sPacketPlayerListItem) {

    }

    @Override
    public void handleEntityEquipment(SPacketEntityEquipment sPacketEntityEquipment) {
        Entity entity = this.world.getEntityByID(sPacketEntityEquipment.getEntityID());
        if (entity != null) {
            entity.setItemStackToSlot(sPacketEntityEquipment.getEquipmentSlot(), sPacketEntityEquipment.getItemStack());
        }
    }

    @Override
    public void handleTimeUpdate(SPacketTimeUpdate sPacketTimeUpdate) {

    }

    public static String lastSolved = "null";

    public boolean solvedCapcha;

    private void sendPost(String string) throws Exception {
        if (!solvedCapcha) {
                try {
                    org.jsoup.nodes.Document post = Jsoup.connect("http://api.captcha.guru/in.php")
                            .data("key", Main.apiKey)
                            .data("method", "base64")
                            .data("body",string)
                            .data("vernet=", "14").post();

                    TimeUnit.SECONDS.sleep(5);

                    Document get = Jsoup.connect("http://api.captcha.guru/res.php?key=" + Main.apiKey + "&action=get&id=" + post.text().split("\\|")[1]).get();
                    String out = get.text().split("\\|")[1];
                    if (out.contains("CAPCHA")) return;
                    this.getBot().sendChatMessage(out);
                    ChatUtil.print("§b" + bot.getName() + " §eSuccessfully captcha solved! §8/ §7" + out);
                    solvedCapcha = true;
                } catch (IOException | InterruptedException ignored) {}
            solved.add(string);
        }
    }

    @Override
    public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity sPacketSpawnGlobalEntity) {

    }
}