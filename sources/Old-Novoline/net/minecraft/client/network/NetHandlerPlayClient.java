package net.minecraft.client.network;

import cc.novoline.Novoline;
import cc.novoline.modules.combat.Velocity;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.*;
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
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.*;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
//import net.skidunion.irc.entities.MinecraftServerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static net.minecraft.client.Minecraft.getInstance;

public class NetHandlerPlayClient implements INetHandlerPlayClient {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
    /**
     * The NetworkManager instance used to communicate with the server (used only by handlePlayerPosLook to update
     * positioning and handleJoinGame to inform the server of the client distribution/mods)
     */
    private final NetworkManager netManager;
    private final GameProfile profile;
    /**
     * Seems to be either null (integrated server) or an instance of either GuiMultiplayer (when connecting to a server)
     * or GuiScreenReamlsTOS (when connecting to MCO server)
     */
    private final GuiScreen guiScreenServer;
    /**
     * Just an ordinary random number generator, used to randomize audio pitch of item/orb pickup and randomize both
     * particlespawn offset and velocity
     */
    private final Random avRandomizer = new Random();
    public int currentServerMaxPlayers = 20;

    /**
     * Reference to the Minecraft instance, which many handler methods operate on
     */
    private Minecraft gameController;
    /**
     * Reference to the current ClientWorld instance, which many handler methods operate on
     */
    private WorldClient clientWorldController;
    /**
     * True if the client has finished downloading terrain and may spawn. Set upon receipt of S08PacketPlayerPosLook,
     * reset upon respawning
     */
    private boolean doneLoadingTerrain;
    private boolean field_147308_k = false;

    public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_) {
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = p_i46300_3_;
        this.profile = p_i46300_4_;
    }

    public static NetworkPlayerInfo getPlayerInfo(UUID uuid) {
        return playerInfoMap.get(uuid);
    }

    /**
     * Clears the WorldClient instance associated with this NetHandlerPlayClient
     */
    public void cleanup() {
        this.clientWorldController = null;
    }

    /**
     * Registers some server properties (gametype,hardcore-mode,terraintype,difficulty,player limit), creates a new
     * WorldClient and sets the player initial dimension
     */
    @Override
    public void handleJoinGame(S01PacketJoinGame packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.playerController = new PlayerControllerMP(gameController, this);
        this.clientWorldController = new WorldClient(this,
                new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(),
                        packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), gameController.mcProfiler);
        gameController.gameSettings.difficulty = packetIn.getDifficulty();
        gameController.loadWorld(clientWorldController);
        gameController.player.dimension = packetIn.getDimension();
        gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        gameController.player.setEntityId(packetIn.getEntityId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        gameController.player.setReducedDebug(packetIn.isReducedDebugInfo());
        gameController.playerController.setGameType(packetIn.getGameType());
        gameController.gameSettings.sendSettingsToServer();
        netManager.sendPacket(new C17PacketCustomPayload("MC|Brand",
                new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));

        // Отправление обновления IRC
        //REMOVED SKIDUNION
//        if (Novoline.getInstance().getIRC().isAuthenticated()) {
//            Novoline.getInstance().getIRC().update(new MinecraftServerEntity(
//                    getInstance().session.getUsername(),
//                    getInstance().getCurrentServerData() == null ? "Singleplayer" : getInstance().getCurrentServerData().serverIP
//            )).queue();
//        }
    }

    /**
     * Spawns an instance of the objecttype indicated by the packet and sets its position and momentum
     */
    @Override
    public void handleSpawnObject(S0EPacketSpawnObject packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = (double) packetIn.getX() / 32.0D;
        double d1 = (double) packetIn.getY() / 32.0D;
        double d2 = (double) packetIn.getZ() / 32.0D;
        Entity entity = null;

        if (packetIn.getType() == 10) {
            entity = EntityMinecart.func_180458_a(clientWorldController, d0, d1, d2,
                    EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
        } else if (packetIn.getType() == 90) {
            Entity entity1 = clientWorldController.getEntityByID(packetIn.func_149009_m());

            if (entity1 instanceof EntityPlayer) {
                entity = new EntityFishHook(clientWorldController, d0, d1, d2, (EntityPlayer) entity1);
            }

            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 60) {
            entity = new EntityArrow(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 61) {
            entity = new EntitySnowball(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 71) {
            entity = new EntityItemFrame(clientWorldController,
                    new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)),
                    EnumFacing.getHorizontal(packetIn.func_149009_m()));
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 77) {
            entity = new EntityLeashKnot(clientWorldController,
                    new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1),
                            MathHelper.floor_double(d2)));
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 65) {
            entity = new EntityEnderPearl(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 72) {
            entity = new EntityEnderEye(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 76) {
            entity = new EntityFireworkRocket(clientWorldController, d0, d1, d2, null);
        } else if (packetIn.getType() == 63) {
            entity = new EntityLargeFireball(clientWorldController, d0, d1, d2,
                    (double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
                    (double) packetIn.getSpeedZ() / 8000.0D);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 64) {
            entity = new EntitySmallFireball(clientWorldController, d0, d1, d2,
                    (double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
                    (double) packetIn.getSpeedZ() / 8000.0D);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 66) {
            entity = new EntityWitherSkull(clientWorldController, d0, d1, d2,
                    (double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
                    (double) packetIn.getSpeedZ() / 8000.0D);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 62) {
            entity = new EntityEgg(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 73) {
            entity = new EntityPotion(clientWorldController, d0, d1, d2, packetIn.func_149009_m());
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 75) {
            entity = new EntityExpBottle(clientWorldController, d0, d1, d2);
            packetIn.func_149002_g(0);
        } else if (packetIn.getType() == 1) {
            entity = new EntityBoat(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 50) {
            entity = new EntityTNTPrimed(clientWorldController, d0, d1, d2, null);
        } else if (packetIn.getType() == 78) {
            entity = new EntityArmorStand(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 51) {
            entity = new EntityEnderCrystal(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 2) {
            entity = new EntityItem(clientWorldController, d0, d1, d2);
        } else if (packetIn.getType() == 70) {
            entity = new EntityFallingBlock(clientWorldController, d0, d1, d2,
                    Block.getStateById(packetIn.func_149009_m() & 65535));
            packetIn.func_149002_g(0);
        }

        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            entity.rotationPitch = (float) (packetIn.getPitch() * 360) / 256.0F;
            entity.rotationYaw = (float) (packetIn.getYaw() * 360) / 256.0F;

            Entity[] entityParts = entity.getParts();

            if (entityParts != null) {
                int i = packetIn.getEntityID() - entity.getEntityID();

                for (Entity entityPart : entityParts) {
                    entityPart.setEntityId(entityPart.getEntityID() + i);
                }
            }

            entity.setEntityId(packetIn.getEntityID());
            clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);

            if (packetIn.func_149009_m() > 0) {
                if (packetIn.getType() == 60) {
                    Entity entity2 = clientWorldController.getEntityByID(packetIn.func_149009_m());

                    if (entity2 instanceof EntityLivingBase && entity instanceof EntityArrow) {
                        ((EntityArrow) entity).shootingEntity = entity2;
                    }
                }

                entity.setVelocity((double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
                        (double) packetIn.getSpeedZ() / 8000.0D);
            }
        }
    }

    /**
     * Spawns an experience orb and sets its value (amount of XP)
     */
    @Override
    public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = new EntityXPOrb(clientWorldController, (double) packetIn.getX() / 32.0D,
                (double) packetIn.getY() / 32.0D, (double) packetIn.getZ() / 32.0D, packetIn.getXPValue());
        entity.serverPosX = packetIn.getX();
        entity.serverPosY = packetIn.getY();
        entity.serverPosZ = packetIn.getZ();
        entity.rotationYaw = 0.0F;
        entity.rotationPitch = 0.0F;
        entity.setEntityId(packetIn.getEntityID());
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
    }

    /**
     * Handles globally visible entities. Used in vanilla for lightning bolts
     */
    @Override
    public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = (double) packetIn.func_149051_d() / 32.0D;
        double d1 = (double) packetIn.func_149050_e() / 32.0D;
        double d2 = (double) packetIn.func_149049_f() / 32.0D;
        Entity entity = null;

        if (packetIn.func_149053_g() == 1) {
            entity = new EntityLightningBolt(clientWorldController, d0, d1, d2);
        }

        if (entity != null) {
            entity.serverPosX = packetIn.func_149051_d();
            entity.serverPosY = packetIn.func_149050_e();
            entity.serverPosZ = packetIn.func_149049_f();
            entity.rotationYaw = 0.0F;
            entity.rotationPitch = 0.0F;
            entity.setEntityId(packetIn.func_149052_c());

            clientWorldController.addWeatherEffect(entity);
        }
    }

    /**
     * Handles the spawning of a painting object
     */
    @Override
    public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPainting entitypainting = new EntityPainting(clientWorldController, packetIn.getPosition(),
                packetIn.getFacing(), packetIn.getTitle());
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
    }

    /**
     * Sets the velocity of the specified entity to the specified value
     */
    @Override
    public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity == null) {
            return;
        }

        entity.setVelocity((double) packetIn.getMotionX() / 8500, (double) packetIn.getMotionY() / 8500,
                (double) packetIn.getMotionZ() / 8500);
    }

    /**
     * Invoked when the server registers new proximate objects in your watchlist or when objects in your watchlist have
     * changed -> Registers any changes locally
     */
    @Override
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null && packetIn.func_149376_c() != null) {
            entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }

    /**
     * Handles the creation of a nearby player entity, sets the position and held item
     */
    @Override
    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = (double) packetIn.getX() / 32.0D;
        double d1 = (double) packetIn.getY() / 32.0D;
        double d2 = (double) packetIn.getZ() / 32.0D;
        float f = (float) (packetIn.getYaw() * 360) / 256.0F;
        float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;

        NetworkPlayerInfo playerInfo = getPlayerInfo(packetIn.getPlayer());
        if (playerInfo == null) return;

        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(gameController.world,
                playerInfo.getGameProfile());
        entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = entityotherplayermp.serverPosX = packetIn
                .getX();
        entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = entityotherplayermp.serverPosY = packetIn
                .getY();
        entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = entityotherplayermp.serverPosZ = packetIn
                .getZ();
        int i = packetIn.getCurrentItemID();

        if (i == 0) {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
        } else {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(
                    Item.getItemById(i), 1, 0);
        }

        entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
        List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();

        if (list != null) {
            entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    /**
     * Updates an entity's position and rotation as specified by the packet
     */
    @Override
    public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            double d0 = (double) entity.serverPosX / 32.0D;
            double d1 = (double) entity.serverPosY / 32.0D;
            double d2 = (double) entity.serverPosZ / 32.0D;
            float f = (float) (packetIn.getYaw() * 360) / 256.0F;
            float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;

            if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
            } else {
                entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
            }

            entity.onGround = packetIn.getOnGround();
        }
    }

    /**
     * Updates which hotbar slot of the player is currently selected
     */
    @Override
    public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getIndex() >= 0 && packetIn.getIndex() < InventoryPlayer.getHotbarSize()) {
            gameController.player.inventory.currentItem = packetIn.getIndex();
        }
    }

    /**
     * Updates the specified entity's position by the specified relative moment and absolute rotation. Note that
     * subclassing of the packet allows for the specification of a subset of this data (e.g. only rel. position, abs.
     * rotation or both).
     */
    @Override
    public void handleEntityMovement(S14PacketEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null) {
            entity.serverPosX += packetIn.getX();
            entity.serverPosY += packetIn.getY();
            entity.serverPosZ += packetIn.getZ();
            double d0 = (double) entity.serverPosX / 32.0D;
            double d1 = (double) entity.serverPosY / 32.0D;
            double d2 = (double) entity.serverPosZ / 32.0D;
            float f =
                    packetIn.func_149060_h() ? (float) (packetIn.getYaw() * 360) / 256.0F : entity.rotationYaw;
            float f1 =
                    packetIn.func_149060_h() ? (float) (packetIn.getPitch() * 360) / 256.0F : entity.rotationPitch;
            entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
            entity.onGround = packetIn.isOnGround();
        }
    }

    /**
     * Updates the direction in which the specified entity is looking, normally this head rotation is independent of the
     * rotation of the entity itself
     */
    @Override
    public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null) {
            float f = (float) (packetIn.getYaw() * 360) / 256.0F;
            entity.setRotationYawHead(f);
        }
    }

    /**
     * Locally eliminates the entities. Invoked by the server when the items are in fact destroyed, or the player is no
     * longer registered as required to monitor them. The latter  happens when distance between the player and item
     * increases beyond a certain treshold (typically the viewing distance)
     */
    @Override
    public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (int i = 0; i < packetIn.getEntityIDs().length; ++i) {
            clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }

    /**
     * Handles changes in player positioning and rotation such as when travelling to a new dimension, (re)spawning,
     * mounting horses etc. Seems to immediately reply to the server with the clients post-processing perspective on the
     * player positioning
     */
    @Override
    public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f1 = packetIn.getPitch();

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            d0 += entityplayer.posX;
        } else {
            entityplayer.motionX = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            d1 += entityplayer.posY;
        } else {
            entityplayer.motionY = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            d2 += entityplayer.posZ;
        } else {
            entityplayer.motionZ = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f1 += entityplayer.rotationPitch;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f += entityplayer.rotationYaw;
        }

        entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
        netManager.sendPacket(
                new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY,
                        entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));

        if (!doneLoadingTerrain) {
            gameController.player.prevPosX = gameController.player.posX;
            gameController.player.prevPosY = gameController.player.posY;
            gameController.player.prevPosZ = gameController.player.posZ;
            this.doneLoadingTerrain = true;
            gameController.displayGuiScreen(null);
        }
    }

    /**
     * Received from the servers PlayerManager if between 1 and 64 blocks in a chunk are changed. If only one block
     * requires an update, the server sends S23PacketBlockChange and if 64 or more blocks are changed, the server sends
     * S21PacketChunkData
     */
    @Override
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (S22PacketMultiBlockChange.BlockUpdateData s22packetmultiblockchange$blockupdatedata : packetIn
                .getChangedBlocks()) {
            clientWorldController.invalidateRegionAndSetBlock(s22packetmultiblockchange$blockupdatedata.getPos(),
                    s22packetmultiblockchange$blockupdatedata.getBlockState());
        }
    }

    /**
     * Updates the specified chunk with the supplied data, marks it for re-rendering and lighting recalculation
     */
    @Override
    public void handleChunkData(S21PacketChunkData packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.func_149274_i()) {
            if (packetIn.getExtractedSize() == 0) {
                clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), false);
                return;
            }

            clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }

        clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4,
                (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        Chunk chunk = clientWorldController
                .getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i());
        clientWorldController
                .markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4,
                        (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);

        if (!packetIn.func_149274_i() || !(clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
    }

    /**
     * Updates the block and metadata and generates a blockupdate (and notify the clients)
     */
    @Override
    public void handleBlockChange(S23PacketBlockChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }

    /**
     * Closes the network channel
     */
    @Override
    public void handleDisconnect(S40PacketDisconnect packetIn) {
        netManager.closeChannel(packetIn.getReason());
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    @Override
    public void onDisconnect(IChatComponent reason) {
        gameController.loadWorld(null);

        if (guiScreenServer != null) {
            if (guiScreenServer instanceof GuiScreenRealmsProxy) {
                gameController.displayGuiScreen(
                        new DisconnectedRealmsScreen(((GuiScreenRealmsProxy) guiScreenServer).func_154321_a(),
                                "disconnect.lost", reason).getProxy());
            } else {
                gameController
                        .displayGuiScreen(new GuiDisconnected(guiScreenServer, "disconnect.lost", reason));
            }
        } else {
            gameController.displayGuiScreen(
                    new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }

    public void sendPacket(Packet p_147297_1_) {
        netManager.sendPacket(p_147297_1_);
    }

    public void sendPacketNoEvent(Packet p_147297_1_) {
        netManager.sendPacketNoEvent(p_147297_1_);
    }

    @Override
    public void handleCollectItem(S0DPacketCollectItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase) clientWorldController
                .getEntityByID(packetIn.getEntityID());

        if (entitylivingbase == null) {
            entitylivingbase = gameController.player;
        }

        if (entity != null) {
            if (entity instanceof EntityXPOrb) {
                clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F,
                        ((avRandomizer.nextFloat() - avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            } else {
                clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F,
                        ((avRandomizer.nextFloat() - avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            gameController.effectRenderer
                    .addEffect(new EntityPickupFX(clientWorldController, entity, entitylivingbase, 0.5F));
            clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }

    /**
     * Prints a chatmessage in the chat GUI
     */
    @Override
    public void handleChat(S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getType() == 2) {
            gameController.ingameGUI.setRecordPlaying(packetIn.getChatComponent(), false);
        } else {
            gameController.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
        }
    }

    /**
     * Renders a specified animation: Waking up a player, a living entity swinging its currently held item, being hurt
     * or receiving a critical hit by normal or magical means
     */
    @Override
    public void handleAnimation(S0BPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                entitylivingbase.swingItem();
            } else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            } else if (packetIn.getAnimationType() == 2) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                entityplayer.wakeUpPlayer(false, false, false);
            } else if (packetIn.getAnimationType() == 4) {
                gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            } else if (packetIn.getAnimationType() == 5) {
                gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    /**
     * Retrieves the player identified by the packet, puts him to sleep if possible (and flags whether all players are
     * asleep)
     */
    @Override
    public void handleUseBed(S0APacketUseBed packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        packetIn.getPlayer(clientWorldController).trySleep(packetIn.getBedPosition());
    }

    /**
     * Spawns the mob entity at the specified location, with the specified rotation, momentum and type. Updates the
     * entities Datawatchers with the entity metadata specified in the packet
     */
    @Override
    public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = (double) packetIn.getX() / 32.0D;
        double d1 = (double) packetIn.getY() / 32.0D;
        double d2 = (double) packetIn.getZ() / 32.0D;
        float f = (float) (packetIn.getYaw() * 360) / 256.0F;
        float f1 = (float) (packetIn.getPitch() * 360) / 256.0F;
        EntityLivingBase entitylivingbase = (EntityLivingBase) EntityList
                .createEntityByID(packetIn.getEntityType(), gameController.world);
        entitylivingbase.serverPosX = packetIn.getX();
        entitylivingbase.serverPosY = packetIn.getY();
        entitylivingbase.serverPosZ = packetIn.getZ();
        entitylivingbase.renderYawOffset = entitylivingbase.rotationYawHead = (float) (packetIn
                .getHeadPitch() * 360) / 256.0F;
        Entity[] aentity = entitylivingbase.getParts();

        if (aentity != null) {
            int i = packetIn.getEntityID() - entitylivingbase.getEntityID();

            for (Entity entity : aentity) {
                entity.setEntityId(entity.getEntityID() + i);
            }
        }

        entitylivingbase.setEntityId(packetIn.getEntityID());
        entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
        entitylivingbase.motionX = (float) packetIn.getVelocityX() / 8000.0F;
        entitylivingbase.motionY = (float) packetIn.getVelocityY() / 8000.0F;
        entitylivingbase.motionZ = (float) packetIn.getVelocityZ() / 8000.0F;
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
        List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();

        if (list != null) {
            entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    @Override
    public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.world.setTotalWorldTime(packetIn.getTotalWorldTime());
        gameController.world.setWorldTime(packetIn.getWorldTime());
    }

    @Override
    public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.player.setSpawnPoint(packetIn.getSpawnPos(), true);
        gameController.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }

    @Override
    public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());
        Entity entity1 = clientWorldController.getEntityByID(packetIn.getVehicleEntityId());

        if (packetIn.getLeash() == 0) {
            boolean flag = false;

            if (packetIn.getEntityId() == gameController.player.getEntityID()) {
                entity = gameController.player;

                if (entity1 instanceof EntityBoat) {
                    ((EntityBoat) entity1).setIsBoatEmpty(false);
                }

                flag = entity.ridingEntity == null && entity1 != null;
            } else if (entity1 instanceof EntityBoat) {
                ((EntityBoat) entity1).setIsBoatEmpty(true);
            }

            if (entity == null) {
                return;
            }

            entity.mountEntity(entity1);

            if (flag) {
                GameSettings gamesettings = gameController.gameSettings;
                gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard",
                        GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode())), false);
            }
        } else if (packetIn.getLeash() == 1 && entity instanceof EntityLiving) {
            if (entity1 != null) {
                ((EntityLiving) entity).setLeashedToEntity(entity1, false);
            } else {
                ((EntityLiving) entity).clearLeashed(false, false);
            }
        }
    }

    @Override
    public void handleMoveVehicle(SPacketMoveVehicle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = gameController.player.ridingEntity;

        if (entity != gameController.player && ((EntityHorse) entity).isHorseSaddled()) {
            entity.setPositionAndRotation(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getYaw(), packetIn.getPitch());
            netManager.sendPacket(new CPacketVehicleMove(entity));
        }
    }

    /**
     * Invokes the entities' handleUpdateHealth method which is implemented in LivingBase (hurt/death),
     * MinecartMobSpawner (spawn delay), FireworkRocket & MinecartTNT (explosion), IronGolem (throwing,...), Witch
     * (spawn particles), Zombie (villager transformation), Animal (breeding mode particles), Horse (breeding/smoke
     * particles), Sheep (...), Tameable (...), Villager (particles for breeding mode, angry and happy), Wolf (...)
     */
    @Override
    public void handleEntityStatus(S19PacketEntityStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null) {
            if (packetIn.getOpCode() == 21) {
                gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian) entity));
            } else {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }

    @Override
    public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        gameController.player.setPlayerSPHealth(packetIn.getHealth());
        gameController.player.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        gameController.player.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    @Override
    public void handleSetExperience(S1FPacketSetExperience packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.player.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
    }

    @Override
    public void handleRespawn(S07PacketRespawn packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getDimensionID() != gameController.player.dimension) {
            this.doneLoadingTerrain = false;
            Scoreboard scoreboard = clientWorldController.getScoreboard();
            this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false,
                    gameController.world.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()),
                    packetIn.getDimensionID(), packetIn.getDifficulty(), gameController.mcProfiler);
            clientWorldController.setWorldScoreboard(scoreboard);
            gameController.loadWorld(clientWorldController);
            gameController.player.dimension = packetIn.getDimensionID();
            gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        }

        gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        gameController.playerController.setGameType(packetIn.getGameType());
    }

    /**
     * Initiates a new explosion (sound, particles, drop spawn) for the affected blocks indicated by the packet.
     */
    @Override
    public void handleExplosion(S27PacketExplosion packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Explosion explosion = new Explosion(gameController.world, null, packetIn.getX(),
                packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);

        if (Novoline.getInstance().getModuleManager().getModule(Velocity.class).isEnabled()) {
            Velocity velocity = Novoline.getInstance().getModuleManager().getModule(Velocity.class);
            velocity.handleExplosion(gameController, packetIn);
        } else {
            gameController.player.motionX += packetIn.getMotionX();
            gameController.player.motionY += packetIn.getMotionY();
            gameController.player.motionZ += packetIn.getMotionZ();
        }
    }

    /**
     * Displays a GUI by ID. In order starting from id 0: Chest, Workbench, Furnace, Dispenser, Enchanting table,
     * Brewing stand, Villager merchant, Beacon, Anvil, Hopper, Dropper, Horse
     */
    @Override
    public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayerSP entityplayersp = gameController.player;
        if ("minecraft:container".equals(packetIn.getGuiId())) {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else if ("EntityHorse".equals(packetIn.getGuiId())) {
            Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

            if (entity instanceof EntityHorse) {
                entityplayersp.displayGUIHorse((EntityHorse) entity,
                        new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        } else if (!packetIn.hasSlots()) {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        } else {
            ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(),
                    packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(containerlocalmenu);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }

    /**
     * Handles pickin up an ItemStack or dropping one in your inventory or an open (non-creative) container
     */
    @Override
    public void handleSetSlot(S2FPacketSetSlot packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;

        if (packetIn.getWindowID() == -1) {
            entityplayer.inventory.setItemStack(packetIn.getItem());
        } else {
            boolean flag = false;

            if (gameController.currentScreen instanceof GuiContainerCreative) {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative) gameController.currentScreen;
                flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
            }

            if (packetIn.getWindowID() == 0 && packetIn.getSlot() >= 36 && packetIn.getSlot() < 45) {
                ItemStack itemstack = entityplayer.inventoryContainer.getSlot(packetIn.getSlot())
                        .getStack();

                if (packetIn.getItem() != null && (itemstack == null || itemstack.stackSize < packetIn
                        .getItem().stackSize)) {
                    packetIn.getItem().animationsToGo = 5;
                }

                entityplayer.inventoryContainer.putStackInSlot(packetIn.getSlot(), packetIn.getItem());
            } else if (packetIn.getWindowID() == entityplayer.openContainer.windowId && (packetIn
                    .getWindowID() != 0 || !flag)) {
                entityplayer.openContainer.putStackInSlot(packetIn.getSlot(), packetIn.getItem());
            }
        }
    }

    /**
     * Verifies that the server and client are synchronized with respect to the inventory/container opened by the player
     * and confirms if it is the case.
     */
    @Override
    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Container container = null;
        EntityPlayer entityplayer = gameController.player;

        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }

        if (container != null && !packetIn.isAccepted()) {
            sendPacket(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }

    /**
     * Handles the placement of a specified ItemStack in a specified container/inventory slot
     */
    @Override
    public void handleWindowItems(S30PacketWindowItems packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;

        if (packetIn.getWindowID() == 0) {
            entityplayer.inventoryContainer.putStacksInSlots(packetIn.getItemStacks());
        } else if (packetIn.getWindowID() == entityplayer.openContainer.windowId) {
            entityplayer.openContainer.putStacksInSlots(packetIn.getItemStacks());
        }
    }

    /**
     * Creates a sign in the specified location if it didn't exist and opens the GUI to edit its text
     */
    @Override
    public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        TileEntity tileentity = clientWorldController.getTileEntity(packetIn.getSignPosition());

        if (!(tileentity instanceof TileEntitySign)) {
            tileentity = new TileEntitySign();
            tileentity.setWorldObj(clientWorldController);
            tileentity.setPos(packetIn.getSignPosition());
        }

        gameController.player.openEditSign((TileEntitySign) tileentity);
    }

    /**
     * Updates a specified sign with the specified text lines
     */
    @Override
    public void handleUpdateSign(S33PacketUpdateSign packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        boolean flag = false;

        if (gameController.world.isBlockLoaded(packetIn.getPos())) {
            TileEntity tileentity = gameController.world.getTileEntity(packetIn.getPos());

            if (tileentity instanceof TileEntitySign) {
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                if (tileentitysign.getIsEditable()) {
                    System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
                    tileentitysign.markDirty();
                }

                flag = true;
            }
        }

        if (!flag && gameController.player != null) {
            gameController.player.addChatMessage(new ChatComponentText(
                    "Unable to locate sign at " + packetIn.getPos().getX() + ", " + packetIn.getPos()
                            .getY() + ", " + packetIn.getPos().getZ()));
        }
    }

    /**
     * Updates the NBTTagCompound metadata of instances of the following entitytypes: Mob spawners, command blocks,
     * beacons, skulls, flowerpot
     */
    @Override
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (gameController.world.isBlockLoaded(packetIn.getPos())) {
            TileEntity tileentity = gameController.world.getTileEntity(packetIn.getPos());
            int i = packetIn.getTileEntityType();

            if (i == 1 && tileentity instanceof TileEntityMobSpawner || i == 2 && tileentity instanceof TileEntityCommandBlock || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner) {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
        }
    }

    /**
     * Sets the progressbar of the opened window to the specified value
     */
    @Override
    public void handleWindowProperty(S31PacketWindowProperty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;

        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId()) {
            entityplayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
        }
    }

    @Override
    public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null) {
            entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }

    /**
     * Resets the ItemStack held in hand and closes the window that is opened
     */
    @Override
    public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.player.closeScreenAndDropStack();
    }

    /**
     * Triggers Block.onBlockEventReceived, which is implemented in BlockPistonBase for extension/retraction, BlockNote
     * for setting the instrument (including audiovisual feedback) and in BlockContainer to set the number of players
     * accessing a (Ender)Chest
     */
    @Override
    public void handleBlockAction(S24PacketBlockAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.world
                .addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(),
                        packetIn.getData2());
    }

    /**
     * Updates all registered IWorldAccess instances with destroyBlockInWorldPartially
     */
    @Override
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }

    @Override
    public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (int i = 0; i < packetIn.getChunkCount(); ++i) {
            int j = packetIn.getChunkX(i);
            int k = packetIn.getChunkZ(i);
            clientWorldController.doPreChunk(j, k, true);
            clientWorldController
                    .invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            Chunk chunk = clientWorldController.getChunkFromChunkCoords(j, k);
            chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
            clientWorldController
                    .markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);

            if (!(clientWorldController.provider instanceof WorldProviderSurface)) {
                chunk.resetRelightChecks();
            }
        }
    }

    @Override
    public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;
        int i = packetIn.getGameState();
        float f = packetIn.func_149137_d();
        int j = MathHelper.floor_float(f + 0.5F);

        if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null) {
            entityplayer.addChatMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i]));
        }

        if (i == 1) {
            clientWorldController.getWorldInfo().setRaining(true);
            clientWorldController.setRainStrength(0.0F);
        } else if (i == 2) {
            clientWorldController.getWorldInfo().setRaining(false);
            clientWorldController.setRainStrength(1.0F);
        } else if (i == 3) {
            gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
        } else if (i == 4) {
            gameController.displayGuiScreen(new GuiWinGame());
        } else if (i == 5) {
            GameSettings gamesettings = gameController.gameSettings;

            if (f == 0.0F) {
                gameController.displayGuiScreen(new GuiScreenDemo());
            } else if (f == 101.0F) {
                gameController.ingameGUI.getChatGUI().printChatMessage(
                        new ChatComponentTranslation("demo.help.movement",
                                GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()),
                                GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()),
                                GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()),
                                GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())));
            } else if (f == 102.0F) {
                gameController.ingameGUI.getChatGUI().printChatMessage(
                        new ChatComponentTranslation("demo.help.jump",
                                GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())));
            } else if (f == 103.0F) {
                gameController.ingameGUI.getChatGUI().printChatMessage(
                        new ChatComponentTranslation("demo.help.inventory",
                                GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())));
            }
        } else if (i == 6) {
            clientWorldController
                    .playSound(entityplayer.posX, entityplayer.posY + (double) entityplayer.getEyeHeight(),
                            entityplayer.posZ, "random.successful_hit", 0.18F, 0.45F, false);
        } else if (i == 7) {
            clientWorldController.setRainStrength(f);
        } else if (i == 8) {
            clientWorldController.setThunderStrength(f);
        } else if (i == 10) {
            clientWorldController
                    .spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY,
                            entityplayer.posZ, 0.0D, 0.0D, 0.0D);
            clientWorldController
                    .playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "mob.guardian.curse", 1.0F,
                            1.0F, false);
        }
    }

    /**
     * Updates the worlds MapStorage with the specified MapData for the specified map-identifier and invokes a
     * MapItemRenderer for it
     */
    @Override
    public void handleMaps(S34PacketMaps packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), gameController.world);
        packetIn.setMapdataTo(mapdata);
        gameController.entityRenderer.getMapItemRenderer().updateMapTexture(mapdata);
    }

    @Override
    public void handleEffect(S28PacketEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.isSoundServerwide()) {
            gameController.world
                    .playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        } else {
            gameController.world
                    .playAuxSFX(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }

    /**
     * Updates the players statistics or achievements
     */
    @Override
    public void handleStatistics(S37PacketStatistics packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        boolean flag = false;

        for (Entry<StatBase, Integer> entry : packetIn.func_148974_c().entrySet()) {
            StatBase statbase = entry.getKey();
            int i = entry.getValue();

            if (statbase.isAchievement() && i > 0) {
                if (field_147308_k && gameController.player.getStatFileWriter().readStat(statbase) == 0) {
                    Achievement achievement = (Achievement) statbase;
                    gameController.guiAchievement.displayAchievement(achievement);
                    gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);

                    if (statbase == AchievementList.openInventory) {
                        gameController.gameSettings.showInventoryAchievementHint = false;
                        gameController.gameSettings.saveOptions();
                    }
                }

                flag = true;
            }

            gameController.player.getStatFileWriter()
                    .unlockAchievement(gameController.player, statbase, i);
        }

        if (!field_147308_k && !flag && gameController.gameSettings.showInventoryAchievementHint) {
            gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }

        this.field_147308_k = true;

        if (gameController.currentScreen instanceof IProgressMeter) {
            ((IProgressMeter) gameController.currentScreen).doneLoading();
        }
    }

    @Override
    public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity instanceof EntityLivingBase) {
            PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(),
                    packetIn.getAmplifier(), false, packetIn.func_179707_f());
            potioneffect.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase) entity).addPotionEffect(potioneffect);
        }
    }

    @Override
    public void handleCombatEvent(S42PacketCombatEvent packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.field_179775_c);
        EntityLivingBase entitylivingbase = entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;

        if (packetIn.eventType == S42PacketCombatEvent.Event.END_COMBAT) {
            long i = 1000L * packetIn.field_179772_d / 20;
            MetadataCombat metadatacombat = new MetadataCombat(gameController.player, entitylivingbase);
            gameController.getTwitchStream().func_176026_a(metadatacombat, -i, 0L);
        } else if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED) {
            Entity entity1 = clientWorldController.getEntityByID(packetIn.field_179774_b);

            if (entity1 instanceof EntityPlayer) {
                MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityPlayer) entity1,
                        entitylivingbase);
                metadataplayerdeath.func_152807_a(packetIn.deathMessage);
                gameController.getTwitchStream().func_152911_a(metadataplayerdeath, 0L);
            }
        }
    }

    @Override
    public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        gameController.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }

    @Override
    public void handleCamera(S43PacketCamera packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null) {
            gameController.setRenderViewEntity(entity);
        }
    }

    @Override
    public void handleWorldBorder(S44PacketWorldBorder packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        packetIn.func_179788_a(clientWorldController.getWorldBorder());
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public void handleTitle(S45PacketTitle packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        S45PacketTitle.Type s45packettitle$type = packetIn.getType();
        String s = null;
        String s1 = null;
        String s2 = packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : "";

        switch (s45packettitle$type) {
            case TITLE:
                s = s2;
                break;

            case SUBTITLE:
                s1 = s2;
                break;

            case RESET:
                gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
                gameController.ingameGUI.func_175177_a();
                return;
        }

        gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }

    @Override
    public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
        if (!netManager.isLocalChannel()) {
            netManager.setCompressionTreshold(packetIn.getCompressionLevel());
        }
    }

    @Override
    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {
        gameController.ingameGUI.getTabList()
                .setHeader(packetIn.getHeader().getFormattedText().isEmpty() ? null : packetIn.getHeader());
        gameController.ingameGUI.getTabList()
                .setFooter(packetIn.getFooter().getFormattedText().isEmpty() ? null : packetIn.getFooter());
    }

    @Override
    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).removePotionEffectClient(packetIn.getEffectId());
        }
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : packetIn.playersDataList()) {
            if (packetIn.getAction() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                playerInfoMap.remove(s38packetplayerlistitem$addplayerdata.getProfile().getId());
            } else {
                NetworkPlayerInfo networkplayerinfo = playerInfoMap
                        .get(s38packetplayerlistitem$addplayerdata.getProfile().getId());

                if (packetIn.getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                    networkplayerinfo = new NetworkPlayerInfo(s38packetplayerlistitem$addplayerdata);
                    playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
                }

                if (networkplayerinfo != null) {
                    switch (packetIn.getAction()) {
                        case ADD_PLAYER:
                            networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                            networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                            break;

                        case UPDATE_GAME_MODE:
                            networkplayerinfo.setGameType(s38packetplayerlistitem$addplayerdata.getGameMode());
                            break;

                        case UPDATE_LATENCY:
                            networkplayerinfo.setResponseTime(s38packetplayerlistitem$addplayerdata.getPing());
                            break;

                        case UPDATE_DISPLAY_NAME:
                            networkplayerinfo.setDisplayName(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    }
                }
            }
        }
    }

    @Override
    public void handleKeepAlive(S00PacketKeepAlive packetIn) {
        sendPacket(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }

    @Override
    public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = gameController.player;
        entityplayer.abilities.setFlying(packetIn.isFlying());
        entityplayer.abilities.setCreative(packetIn.isCreative());
        entityplayer.abilities.setDisabledDamage(packetIn.isDisabledDamage());
        entityplayer.abilities.setAllowFlying(packetIn.isAllowFlying());
        entityplayer.abilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer.abilities.setWalkSpeed(packetIn.getWalkSpeed());
    }

    /**
     * Displays the available command-completion options the server knows of
     */
    @Override
    public void handleTabComplete(S3APacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        String[] astring = packetIn.func_149630_c();

        if (gameController.currentScreen instanceof GuiChat) {
            GuiChat guichat = (GuiChat) gameController.currentScreen;
            guichat.onAutocompleteResponse(astring);
        }
    }

    @Override
    public void handleSoundEffect(S29PacketSoundEffect packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        gameController.world
                .playSound(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSoundName(),
                        packetIn.getVolume(), packetIn.getPitch(), false);
    }

    @Override
    public void handleResourcePack(S48PacketResourcePackSend packetIn) {
        String s = packetIn.getURL();
        String s1 = packetIn.getHash();

        if (s.startsWith("level://")) {
            String s2 = s.substring("level://".length());
            File file1 = new File(gameController.mcDataDir, "saves");
            File file2 = new File(file1, s2);

            if (file2.isFile()) {
                netManager
                        .sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(gameController.getResourcePackRepository().setResourcePackInstance(file2),
                        new FutureCallback<Object>() {

                            @Override
                            public void onSuccess(Object p_onSuccess_1_) {
                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                            }

                            @Override
                            public void onFailure(Throwable p_onFailure_1_) {
                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                            }
                        });
            } else {
                netManager.sendPacket(
                        new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        } else {
            if (gameController.getCurrentServerData() != null && gameController.getCurrentServerData()
                    .getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
                netManager
                        .sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(gameController.getResourcePackRepository().downloadResourcePack(s, s1),
                        new FutureCallback<Object>() {

                            @Override
                            public void onSuccess(Object p_onSuccess_1_) {
                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                            }

                            @Override
                            public void onFailure(Throwable p_onFailure_1_) {
                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                            }
                        });
            } else if (gameController.getCurrentServerData() != null && gameController.getCurrentServerData()
                    .getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
                netManager
                        .sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
            } else {
                gameController.addScheduledTask(
                        () -> gameController.displayGuiScreen(new GuiYesNo((result, id) -> {
                            NetHandlerPlayClient.this.gameController = getInstance();

                            if (result) {
                                if (gameController.getCurrentServerData() != null) {
                                    gameController.getCurrentServerData()
                                            .setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                }

                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.ACCEPTED));
                                Futures.addCallback(gameController.getResourcePackRepository()
                                        .downloadResourcePack(s, s1), new FutureCallback<Object>() {

                                    @Override
                                    public void onSuccess(Object p_onSuccess_1_) {
                                        netManager.sendPacket(
                                                new C19PacketResourcePackStatus(s1,
                                                        C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                    }

                                    @Override
                                    public void onFailure(Throwable p_onFailure_1_) {
                                        netManager.sendPacket(
                                                new C19PacketResourcePackStatus(s1,
                                                        C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                    }
                                });
                            } else {
                                if (gameController.getCurrentServerData() != null) {
                                    gameController.getCurrentServerData()
                                            .setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                }

                                netManager.sendPacket(new C19PacketResourcePackStatus(s1,
                                        C19PacketResourcePackStatus.Action.DECLINED));
                            }

                            ServerList.func_147414_b(gameController.getCurrentServerData());
                            gameController.displayGuiScreen(null);
                        }, I18n.format("multiplayer.texturePrompt.line1"),
                                I18n.format("multiplayer.texturePrompt.line2"), 0)));
            }
        }
    }

    @Override
    public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null) {
            entity.clientUpdateEntityNBT(packetIn.getTagCompound());
        }
    }

    /**
     * Handles packets that have room for a channel specification. Vanilla implemented channels are "MC|TrList" to
     * acquire a MerchantRecipeList trades for a villager merchant, "MC|Brand" which sets the server brand? on the
     * player instance and finally "MC|RPack" which the server uses to communicate the identifier of the default server
     * resourcepack for the client to load.
     */
    @Override
    public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if ("MC|TrList".equals(packetIn.getChannelName())) {
            PacketBuffer packetbuffer = packetIn.getBufferData();

            try {
                int i = packetbuffer.readInt();
                GuiScreen guiscreen = gameController.currentScreen;

                if (guiscreen instanceof GuiMerchant && i == gameController.player.openContainer.windowId) {
                    IMerchant imerchant = ((GuiMerchant) guiscreen).getMerchant();
                    MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                    imerchant.setRecipes(merchantrecipelist);
                }
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't load trade info", ioexception);
            } finally {
                packetbuffer.release();
            }
        } else if ("MC|Brand".equals(packetIn.getChannelName())) {
            gameController.player.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
        } else if ("MC|BOpen".equals(packetIn.getChannelName())) {
            ItemStack itemstack = gameController.player.getCurrentEquippedItem();

            if (itemstack != null && itemstack.getItem() == Items.written_book) {
                gameController
                        .displayGuiScreen(new GuiScreenBook(gameController.player, itemstack, false));
            }
        }
    }

    /**
     * May create a scoreboard objective, remove an objective from the scoreboard or update an objectives' displayname
     */
    @Override
    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();

        if (packetIn.func_149338_e() == 0) {
            ScoreObjective scoreobjective = scoreboard
                    .addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
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

    /**
     * Either updates the score with a specified value or removes the score for an objective
     */
    @Override
    public void handleUpdateScore(S3CPacketUpdateScore packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());

        if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.CHANGE) {
            Score score = scoreboard.getValueFromObjective(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        } else if (packetIn.getScoreAction() == S3CPacketUpdateScore.Action.REMOVE) {
            if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
            } else if (scoreobjective != null) {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }

    /**
     * Removes or sets the ScoreObjective to be displayed at a particular scoreboard position (list, sidebar, below
     * name)
     */
    @Override
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();

        if (packetIn.getServerName().isEmpty()) {
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), null);
        } else {
            ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getServerName());
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
        }
    }

    /**
     * Updates a team managed by the scoreboard: Create/Remove the team registration, Register/Remove the player-team-
     * memberships, Set team displayname/prefix/suffix and/or whether friendly fire is enabled
     */
    @Override
    public void handleTeams(S3EPacketTeams packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam;

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
            Team.EnumVisible visible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());

            if (visible != null) {
                scoreplayerteam.setNameTagVisibility(visible);
            }
        }

        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3) {
            for (String s : packetIn.func_149310_g()) {
                scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());
            }
        }

        if (packetIn.func_149307_h() == 4) {
            for (String s1 : packetIn.func_149310_g()) {
                scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
            }
        }

        if (packetIn.func_149307_h() == 1) {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }

    /**
     * Spawns a specified number of particles at the specified location with a randomized displacement according to
     * specified bounds
     */
    @Override
    public void handleParticles(S2APacketParticles packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getParticleCount() == 0) {
            double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            double d4 = packetIn.getParticleSpeed() * packetIn.getZOffset();

            try {
                clientWorldController
                        .spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(),
                                packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4,
                                packetIn.getParticleArgs());
            } catch (Throwable var17) {
                LOGGER.warn("Could not spawn particle effect " + packetIn.getParticleType());
            }
        } else {
            for (int i = 0; i < packetIn.getParticleCount(); ++i) {
                double d1 = avRandomizer.nextGaussian() * (double) packetIn.getXOffset();
                double d3 = avRandomizer.nextGaussian() * (double) packetIn.getYOffset();
                double d5 = avRandomizer.nextGaussian() * (double) packetIn.getZOffset();
                double d6 = avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();
                double d7 = avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();
                double d8 = avRandomizer.nextGaussian() * (double) packetIn.getParticleSpeed();

                try {
                    clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(),
                            packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3,
                            packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
                } catch (Throwable var16) {
                    LOGGER.warn("Could not spawn particle effect " + packetIn.getParticleType());
                    return;
                }
            }
        }
    }

    /**
     * Updates en entity's attributes and their respective modifiers, which are used for speed bonusses (player
     * sprinting, animals fleeing, baby speed), weapon/tool attackDamage, hostiles followRange randomization, zombie
     * maxHealth and knockback resistance as well as reinforcement spawning chance.
     */
    @Override
    public void handleEntityProperties(S20PacketEntityProperties packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException(
                        "Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            } else {
                BaseAttributeMap baseattributemap = ((EntityLivingBase) entity).getAttributeMap();

                for (S20PacketEntityProperties.Snapshot snapshot : packetIn.func_149441_d()) {
                    IAttributeInstance attributeInstance = baseattributemap
                            .getAttributeInstanceByName(snapshot.func_151409_a());

                    if (attributeInstance == null) {
                        attributeInstance = baseattributemap.registerAttribute(
                                new RangedAttribute(null, snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D,
                                        Double.MAX_VALUE));
                    }

                    attributeInstance.setBaseValue(snapshot.func_151410_b());
                    attributeInstance.removeAllModifiers();

                    for (AttributeModifier attributemodifier : snapshot.func_151408_c()) {
                        attributeInstance.applyModifier(attributemodifier);
                    }
                }
            }
        }
    }

    /**
     * Returns this the NetworkManager instance registered with this NetworkHandlerPlayClient
     */
    public NetworkManager getNetworkManager() {
        return netManager;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return playerInfoMap.values();
    }

    /**
     * Gets the client's description information about another player on the server.
     */
    public NetworkPlayerInfo getPlayerInfo(String name) {
        for (NetworkPlayerInfo playerInfo : playerInfoMap.values()) {
            if (playerInfo.getGameProfile().getName().equals(name)) {
                return playerInfo;
            }
        }

        return null;
    }

    public GameProfile getGameProfile() {
        return profile;
    }
}
