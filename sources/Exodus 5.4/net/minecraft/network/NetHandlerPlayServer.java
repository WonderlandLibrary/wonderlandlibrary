/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.primitives.Doubles
 *  com.google.common.primitives.Floats
 *  com.google.common.util.concurrent.Futures
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer
implements INetHandlerPlayServer,
ITickable {
    private long lastSentPingPacket;
    private int networkTickCount;
    private int itemDropThreshold;
    private int chatSpamThresholdCount;
    public final NetworkManager netManager;
    private boolean field_147366_g;
    private double lastPosZ;
    private long lastPingTime;
    private int field_175090_f;
    private double lastPosX;
    private int floatingTickCount;
    public EntityPlayerMP playerEntity;
    private double lastPosY;
    private IntHashMap<Short> field_147372_n = new IntHashMap();
    private boolean hasMoved = true;
    private final MinecraftServer serverController;
    private int field_147378_h;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void processKeepAlive(C00PacketKeepAlive c00PacketKeepAlive) {
        if (c00PacketKeepAlive.getKey() == this.field_147378_h) {
            int n = (int)(this.currentTimeMillis() - this.lastPingTime);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + n) / 4;
        }
    }

    @Override
    public void processUseEntity(C02PacketUseEntity c02PacketUseEntity) {
        PacketThreadUtil.checkThreadAndEnqueue(c02PacketUseEntity, this, this.playerEntity.getServerForPlayer());
        WorldServer worldServer = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        Entity entity = c02PacketUseEntity.getEntityFromWorld(worldServer);
        this.playerEntity.markPlayerActive();
        if (entity != null) {
            boolean bl = this.playerEntity.canEntityBeSeen(entity);
            double d = 36.0;
            if (!bl) {
                d = 9.0;
            }
            if (this.playerEntity.getDistanceSqToEntity(entity) < d) {
                if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    this.playerEntity.interactWith(entity);
                } else if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
                    entity.interactAt(this.playerEntity, c02PacketUseEntity.getHitVec());
                } else if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.playerEntity) {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }
                    this.playerEntity.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }

    @Override
    public void processClientStatus(C16PacketClientStatus c16PacketClientStatus) {
        PacketThreadUtil.checkThreadAndEnqueue(c16PacketClientStatus, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        C16PacketClientStatus.EnumState enumState = c16PacketClientStatus.getStatus();
        switch (enumState) {
            case PERFORM_RESPAWN: {
                if (this.playerEntity.playerConqueredTheEnd) {
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
                    break;
                }
                if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
                    if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
                        this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                        this.serverController.deleteWorldAndStopServer();
                        break;
                    }
                    UserListBansEntry userListBansEntry = new UserListBansEntry(this.playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
                    this.serverController.getConfigurationManager().getBannedPlayers().addEntry(userListBansEntry);
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                    break;
                }
                if (this.playerEntity.getHealth() > 0.0f) {
                    return;
                }
                this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
                break;
            }
            case REQUEST_STATS: {
                this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
                break;
            }
            case OPEN_INVENTORY_ACHIEVEMENT: {
                this.playerEntity.triggerAchievement(AchievementList.openInventory);
            }
        }
    }

    @Override
    public void handleAnimation(C0APacketAnimation c0APacketAnimation) {
        PacketThreadUtil.checkThreadAndEnqueue(c0APacketAnimation, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        this.playerEntity.swingItem();
    }

    @Override
    public void processUpdateSign(C12PacketUpdateSign c12PacketUpdateSign) {
        PacketThreadUtil.checkThreadAndEnqueue(c12PacketUpdateSign, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        WorldServer worldServer = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        BlockPos blockPos = c12PacketUpdateSign.getPosition();
        if (worldServer.isBlockLoaded(blockPos)) {
            TileEntity tileEntity = worldServer.getTileEntity(blockPos);
            if (!(tileEntity instanceof TileEntitySign)) {
                return;
            }
            TileEntitySign tileEntitySign = (TileEntitySign)tileEntity;
            if (!tileEntitySign.getIsEditable() || tileEntitySign.getPlayer() != this.playerEntity) {
                this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }
            IChatComponent[] iChatComponentArray = c12PacketUpdateSign.getLines();
            int n = 0;
            while (n < iChatComponentArray.length) {
                tileEntitySign.signText[n] = new ChatComponentText(EnumChatFormatting.getTextWithoutFormattingCodes(iChatComponentArray[n].getUnformattedText()));
                ++n;
            }
            tileEntitySign.markDirty();
            worldServer.markBlockForUpdate(blockPos);
        }
    }

    @Override
    public void processCreativeInventoryAction(C10PacketCreativeInventoryAction c10PacketCreativeInventoryAction) {
        PacketThreadUtil.checkThreadAndEnqueue(c10PacketCreativeInventoryAction, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.theItemInWorldManager.isCreative()) {
            boolean bl;
            Object object;
            BlockPos blockPos;
            TileEntity tileEntity;
            NBTTagCompound nBTTagCompound;
            boolean bl2 = c10PacketCreativeInventoryAction.getSlotId() < 0;
            ItemStack itemStack = c10PacketCreativeInventoryAction.getStack();
            if (itemStack != null && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("BlockEntityTag", 10) && (nBTTagCompound = itemStack.getTagCompound().getCompoundTag("BlockEntityTag")).hasKey("x") && nBTTagCompound.hasKey("y") && nBTTagCompound.hasKey("z") && (tileEntity = this.playerEntity.worldObj.getTileEntity(blockPos = new BlockPos(nBTTagCompound.getInteger("x"), nBTTagCompound.getInteger("y"), nBTTagCompound.getInteger("z")))) != null) {
                object = new NBTTagCompound();
                tileEntity.writeToNBT((NBTTagCompound)object);
                ((NBTTagCompound)object).removeTag("x");
                ((NBTTagCompound)object).removeTag("y");
                ((NBTTagCompound)object).removeTag("z");
                itemStack.setTagInfo("BlockEntityTag", (NBTBase)object);
            }
            boolean bl3 = c10PacketCreativeInventoryAction.getSlotId() >= 1 && c10PacketCreativeInventoryAction.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
            boolean bl4 = itemStack == null || itemStack.getItem() != null;
            boolean bl5 = bl = itemStack == null || itemStack.getMetadata() >= 0 && itemStack.stackSize <= 64 && itemStack.stackSize > 0;
            if (bl3 && bl4 && bl) {
                if (itemStack == null) {
                    this.playerEntity.inventoryContainer.putStackInSlot(c10PacketCreativeInventoryAction.getSlotId(), null);
                } else {
                    this.playerEntity.inventoryContainer.putStackInSlot(c10PacketCreativeInventoryAction.getSlotId(), itemStack);
                }
                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            } else if (bl2 && bl4 && bl && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                object = this.playerEntity.dropPlayerItemWithRandomChoice(itemStack, true);
                if (object != null) {
                    ((EntityItem)object).setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    public NetHandlerPlayServer(MinecraftServer minecraftServer, NetworkManager networkManager, EntityPlayerMP entityPlayerMP) {
        this.serverController = minecraftServer;
        this.netManager = networkManager;
        networkManager.setNetHandler(this);
        this.playerEntity = entityPlayerMP;
        entityPlayerMP.playerNetServerHandler = this;
    }

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
        logger.info(String.valueOf(this.playerEntity.getName()) + " lost connection: " + iChatComponent);
        this.serverController.refreshStatusNextTick();
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("multiplayer.player.left", this.playerEntity.getDisplayName());
        chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.serverController.getConfigurationManager().sendChatMsg(chatComponentTranslation);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
        if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }

    @Override
    public void processEnchantItem(C11PacketEnchantItem c11PacketEnchantItem) {
        PacketThreadUtil.checkThreadAndEnqueue(c11PacketEnchantItem, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == c11PacketEnchantItem.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, c11PacketEnchantItem.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }

    @Override
    public void processInput(C0CPacketInput c0CPacketInput) {
        PacketThreadUtil.checkThreadAndEnqueue(c0CPacketInput, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.setEntityActionState(c0CPacketInput.getStrafeSpeed(), c0CPacketInput.getForwardSpeed(), c0CPacketInput.isJumping(), c0CPacketInput.isSneaking());
    }

    public void setPlayerLocation(double d, double d2, double d3, float f, float f2, Set<S08PacketPlayerPosLook.EnumFlags> set) {
        this.hasMoved = false;
        this.lastPosX = d;
        this.lastPosY = d2;
        this.lastPosZ = d3;
        if (set.contains((Object)S08PacketPlayerPosLook.EnumFlags.X)) {
            this.lastPosX += this.playerEntity.posX;
        }
        if (set.contains((Object)S08PacketPlayerPosLook.EnumFlags.Y)) {
            this.lastPosY += this.playerEntity.posY;
        }
        if (set.contains((Object)S08PacketPlayerPosLook.EnumFlags.Z)) {
            this.lastPosZ += this.playerEntity.posZ;
        }
        float f3 = f;
        float f4 = f2;
        if (set.contains((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f3 = f + this.playerEntity.rotationYaw;
        }
        if (set.contains((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f4 = f2 + this.playerEntity.rotationPitch;
        }
        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f3, f4);
        this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(d, d2, d3, f, f2, set));
    }

    public void kickPlayerFromServer(String string) {
        final ChatComponentText chatComponentText = new ChatComponentText(string);
        this.netManager.sendPacket(new S40PacketDisconnect(chatComponentText), (GenericFutureListener<? extends Future<? super Void>>)new GenericFutureListener<Future<? super Void>>(){

            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                NetHandlerPlayServer.this.netManager.closeChannel(chatComponentText);
            }
        }, new GenericFutureListener[0]);
        this.netManager.disableAutoRead();
        Futures.getUnchecked(this.serverController.addScheduledTask(new Runnable(){

            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        }));
    }

    @Override
    public void processPlayer(C03PacketPlayer c03PacketPlayer) {
        PacketThreadUtil.checkThreadAndEnqueue(c03PacketPlayer, this, this.playerEntity.getServerForPlayer());
        if (this.func_183006_b(c03PacketPlayer)) {
            this.kickPlayerFromServer("Invalid move packet received");
        } else {
            WorldServer worldServer = this.serverController.worldServerForDimension(this.playerEntity.dimension);
            this.field_147366_g = true;
            if (!this.playerEntity.playerConqueredTheEnd) {
                double d = this.playerEntity.posX;
                double d2 = this.playerEntity.posY;
                double d3 = this.playerEntity.posZ;
                double d4 = 0.0;
                double d5 = c03PacketPlayer.getPositionX() - this.lastPosX;
                double d6 = c03PacketPlayer.getPositionY() - this.lastPosY;
                double d7 = c03PacketPlayer.getPositionZ() - this.lastPosZ;
                if (c03PacketPlayer.isMoving()) {
                    d4 = d5 * d5 + d6 * d6 + d7 * d7;
                    if (!this.hasMoved && d4 < 0.25) {
                        this.hasMoved = true;
                    }
                }
                if (this.hasMoved) {
                    this.field_175090_f = this.networkTickCount;
                    if (this.playerEntity.ridingEntity != null) {
                        float f = this.playerEntity.rotationYaw;
                        float f2 = this.playerEntity.rotationPitch;
                        this.playerEntity.ridingEntity.updateRiderPosition();
                        double d8 = this.playerEntity.posX;
                        double d9 = this.playerEntity.posY;
                        double d10 = this.playerEntity.posZ;
                        if (c03PacketPlayer.getRotating()) {
                            f = c03PacketPlayer.getYaw();
                            f2 = c03PacketPlayer.getPitch();
                        }
                        this.playerEntity.onGround = c03PacketPlayer.isOnGround();
                        this.playerEntity.onUpdateEntity();
                        this.playerEntity.setPositionAndRotation(d8, d9, d10, f, f2);
                        if (this.playerEntity.ridingEntity != null) {
                            this.playerEntity.ridingEntity.updateRiderPosition();
                        }
                        this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                        if (this.playerEntity.ridingEntity != null) {
                            if (d4 > 4.0) {
                                Entity entity = this.playerEntity.ridingEntity;
                                this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(entity));
                                this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                            }
                            this.playerEntity.ridingEntity.isAirBorne = true;
                        }
                        if (this.hasMoved) {
                            this.lastPosX = this.playerEntity.posX;
                            this.lastPosY = this.playerEntity.posY;
                            this.lastPosZ = this.playerEntity.posZ;
                        }
                        worldServer.updateEntity(this.playerEntity);
                        return;
                    }
                    if (this.playerEntity.isPlayerSleeping()) {
                        this.playerEntity.onUpdateEntity();
                        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        worldServer.updateEntity(this.playerEntity);
                        return;
                    }
                    double d11 = this.playerEntity.posY;
                    this.lastPosX = this.playerEntity.posX;
                    this.lastPosY = this.playerEntity.posY;
                    this.lastPosZ = this.playerEntity.posZ;
                    double d12 = this.playerEntity.posX;
                    double d13 = this.playerEntity.posY;
                    double d14 = this.playerEntity.posZ;
                    float f = this.playerEntity.rotationYaw;
                    float f3 = this.playerEntity.rotationPitch;
                    if (c03PacketPlayer.isMoving() && c03PacketPlayer.getPositionY() == -999.0) {
                        c03PacketPlayer.setMoving(false);
                    }
                    if (c03PacketPlayer.isMoving()) {
                        d12 = c03PacketPlayer.getPositionX();
                        d13 = c03PacketPlayer.getPositionY();
                        d14 = c03PacketPlayer.getPositionZ();
                        if (Math.abs(c03PacketPlayer.getPositionX()) > 3.0E7 || Math.abs(c03PacketPlayer.getPositionZ()) > 3.0E7) {
                            this.kickPlayerFromServer("Illegal position");
                            return;
                        }
                    }
                    if (c03PacketPlayer.getRotating()) {
                        f = c03PacketPlayer.getYaw();
                        f3 = c03PacketPlayer.getPitch();
                    }
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f, f3);
                    if (!this.hasMoved) {
                        return;
                    }
                    double d15 = d12 - this.playerEntity.posX;
                    double d16 = d13 - this.playerEntity.posY;
                    double d17 = d14 - this.playerEntity.posZ;
                    double d18 = d15 * d15 + d16 * d16 + d17 * d17;
                    double d19 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
                    if (!(!(d18 - d19 > 100.0) || this.serverController.isSinglePlayer() && this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                        logger.warn(String.valueOf(this.playerEntity.getName()) + " moved too quickly! " + d15 + "," + d16 + "," + d17 + " (" + d15 + ", " + d16 + ", " + d17 + ")");
                        this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        return;
                    }
                    float f4 = 0.0625f;
                    boolean bl = worldServer.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f4, f4, f4)).isEmpty();
                    if (this.playerEntity.onGround && !c03PacketPlayer.isOnGround() && d16 > 0.0) {
                        this.playerEntity.jump();
                    }
                    this.playerEntity.moveEntity(d15, d16, d17);
                    this.playerEntity.onGround = c03PacketPlayer.isOnGround();
                    d15 = d12 - this.playerEntity.posX;
                    d16 = d13 - this.playerEntity.posY;
                    if (d16 > -0.5 || d16 < 0.5) {
                        d16 = 0.0;
                    }
                    d17 = d14 - this.playerEntity.posZ;
                    d18 = d15 * d15 + d16 * d16 + d17 * d17;
                    boolean bl2 = false;
                    if (d18 > 0.0625 && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
                        bl2 = true;
                        logger.warn(String.valueOf(this.playerEntity.getName()) + " moved wrongly!");
                    }
                    this.playerEntity.setPositionAndRotation(d12, d13, d14, f, f3);
                    this.playerEntity.addMovementStat(this.playerEntity.posX - d, this.playerEntity.posY - d2, this.playerEntity.posZ - d3);
                    if (!this.playerEntity.noClip) {
                        boolean bl3 = worldServer.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f4, f4, f4)).isEmpty();
                        if (bl && (bl2 || !bl3) && !this.playerEntity.isPlayerSleeping()) {
                            this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f, f3);
                            return;
                        }
                    }
                    AxisAlignedBB axisAlignedBB = this.playerEntity.getEntityBoundingBox().expand(f4, f4, f4).addCoord(0.0, -0.55, 0.0);
                    if (!(this.serverController.isFlightAllowed() || this.playerEntity.capabilities.allowFlying || worldServer.checkBlockCollision(axisAlignedBB))) {
                        if (d16 >= -0.03125) {
                            ++this.floatingTickCount;
                            if (this.floatingTickCount > 80) {
                                logger.warn(String.valueOf(this.playerEntity.getName()) + " was kicked for floating too long!");
                                this.kickPlayerFromServer("Flying is not enabled on this server");
                                return;
                            }
                        }
                    } else {
                        this.floatingTickCount = 0;
                    }
                    this.playerEntity.onGround = c03PacketPlayer.isOnGround();
                    this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                    this.playerEntity.handleFalling(this.playerEntity.posY - d11, c03PacketPlayer.isOnGround());
                } else if (this.networkTickCount - this.field_175090_f > 20) {
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                }
            }
        }
    }

    @Override
    public void processEntityAction(C0BPacketEntityAction c0BPacketEntityAction) {
        PacketThreadUtil.checkThreadAndEnqueue(c0BPacketEntityAction, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        switch (c0BPacketEntityAction.getAction()) {
            case START_SNEAKING: {
                this.playerEntity.setSneaking(true);
                break;
            }
            case STOP_SNEAKING: {
                this.playerEntity.setSneaking(false);
                break;
            }
            case START_SPRINTING: {
                this.playerEntity.setSprinting(true);
                break;
            }
            case STOP_SPRINTING: {
                this.playerEntity.setSprinting(false);
                break;
            }
            case STOP_SLEEPING: {
                this.playerEntity.wakeUpPlayer(false, true, true);
                this.hasMoved = false;
                break;
            }
            case RIDING_JUMP: {
                if (!(this.playerEntity.ridingEntity instanceof EntityHorse)) break;
                ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(c0BPacketEntityAction.getAuxData());
                break;
            }
            case OPEN_INVENTORY: {
                if (!(this.playerEntity.ridingEntity instanceof EntityHorse)) break;
                ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }

    @Override
    public void processPlayerDigging(C07PacketPlayerDigging c07PacketPlayerDigging) {
        PacketThreadUtil.checkThreadAndEnqueue(c07PacketPlayerDigging, this, this.playerEntity.getServerForPlayer());
        WorldServer worldServer = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        BlockPos blockPos = c07PacketPlayerDigging.getPosition();
        this.playerEntity.markPlayerActive();
        switch (c07PacketPlayerDigging.getStatus()) {
            case DROP_ITEM: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropOneItem(false);
                }
                return;
            }
            case DROP_ALL_ITEMS: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropOneItem(true);
                }
                return;
            }
            case RELEASE_USE_ITEM: {
                this.playerEntity.stopUsingItem();
                return;
            }
            case START_DESTROY_BLOCK: 
            case ABORT_DESTROY_BLOCK: 
            case STOP_DESTROY_BLOCK: {
                double d = this.playerEntity.posX - ((double)blockPos.getX() + 0.5);
                double d2 = this.playerEntity.posY - ((double)blockPos.getY() + 0.5) + 1.5;
                double d3 = this.playerEntity.posZ - ((double)blockPos.getZ() + 0.5);
                double d4 = d * d + d2 * d2 + d3 * d3;
                if (d4 > 36.0) {
                    return;
                }
                if (blockPos.getY() >= this.serverController.getBuildLimit()) {
                    return;
                }
                if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(worldServer, blockPos, this.playerEntity) && worldServer.getWorldBorder().contains(blockPos)) {
                        this.playerEntity.theItemInWorldManager.onBlockClicked(blockPos, c07PacketPlayerDigging.getFacing());
                    } else {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldServer, blockPos));
                    }
                } else {
                    if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.blockRemoving(blockPos);
                    } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.cancelDestroyingBlock();
                    }
                    if (worldServer.getBlockState(blockPos).getBlock().getMaterial() != Material.air) {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldServer, blockPos));
                    }
                }
                return;
            }
        }
        throw new IllegalArgumentException("Invalid player action");
    }

    @Override
    public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement) {
        Object object;
        PacketThreadUtil.checkThreadAndEnqueue(c08PacketPlayerBlockPlacement, this, this.playerEntity.getServerForPlayer());
        WorldServer worldServer = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        ItemStack itemStack = this.playerEntity.inventory.getCurrentItem();
        boolean bl = false;
        BlockPos blockPos = c08PacketPlayerBlockPlacement.getPosition();
        EnumFacing enumFacing = EnumFacing.getFront(c08PacketPlayerBlockPlacement.getPlacedBlockDirection());
        this.playerEntity.markPlayerActive();
        if (c08PacketPlayerBlockPlacement.getPlacedBlockDirection() == 255) {
            if (itemStack == null) {
                return;
            }
            this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, worldServer, itemStack);
        } else if (blockPos.getY() < this.serverController.getBuildLimit() - 1 || enumFacing != EnumFacing.UP && blockPos.getY() < this.serverController.getBuildLimit()) {
            if (this.hasMoved && this.playerEntity.getDistanceSq((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5) < 64.0 && !this.serverController.isBlockProtected(worldServer, blockPos, this.playerEntity) && worldServer.getWorldBorder().contains(blockPos)) {
                this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, worldServer, itemStack, blockPos, enumFacing, c08PacketPlayerBlockPlacement.getPlacedBlockOffsetX(), c08PacketPlayerBlockPlacement.getPlacedBlockOffsetY(), c08PacketPlayerBlockPlacement.getPlacedBlockOffsetZ());
            }
            bl = true;
        } else {
            object = new ChatComponentTranslation("build.tooHigh", this.serverController.getBuildLimit());
            ((ChatComponentStyle)object).getChatStyle().setColor(EnumChatFormatting.RED);
            this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat((IChatComponent)object));
            bl = true;
        }
        if (bl) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldServer, blockPos));
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldServer, blockPos.offset(enumFacing)));
        }
        if ((itemStack = this.playerEntity.inventory.getCurrentItem()) != null && itemStack.stackSize == 0) {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            itemStack = null;
        }
        if (itemStack == null || itemStack.getMaxItemUseDuration() == 0) {
            this.playerEntity.isChangingQuantityOnly = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            object = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.isChangingQuantityOnly = false;
            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), c08PacketPlayerBlockPlacement.getStack())) {
                this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, ((Slot)object).slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }

    @Override
    public void handleSpectate(C18PacketSpectate c18PacketSpectate) {
        PacketThreadUtil.checkThreadAndEnqueue(c18PacketSpectate, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.isSpectator()) {
            WorldServer worldServer;
            Entity entity = null;
            WorldServer[] worldServerArray = this.serverController.worldServers;
            int n = this.serverController.worldServers.length;
            int n2 = 0;
            while (n2 < n) {
                worldServer = worldServerArray[n2];
                if (worldServer != null && (entity = c18PacketSpectate.getEntity(worldServer)) != null) break;
                ++n2;
            }
            if (entity != null) {
                this.playerEntity.setSpectatingEntity(this.playerEntity);
                this.playerEntity.mountEntity(null);
                if (entity.worldObj != this.playerEntity.worldObj) {
                    worldServer = this.playerEntity.getServerForPlayer();
                    WorldServer worldServer2 = (WorldServer)entity.worldObj;
                    this.playerEntity.dimension = entity.dimension;
                    this.sendPacket(new S07PacketRespawn(this.playerEntity.dimension, worldServer.getDifficulty(), worldServer.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
                    worldServer.removePlayerEntityDangerously(this.playerEntity);
                    this.playerEntity.isDead = false;
                    this.playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    if (this.playerEntity.isEntityAlive()) {
                        worldServer.updateEntityWithOptionalForce(this.playerEntity, false);
                        worldServer2.spawnEntityInWorld(this.playerEntity);
                        worldServer2.updateEntityWithOptionalForce(this.playerEntity, false);
                    }
                    this.playerEntity.setWorld(worldServer2);
                    this.serverController.getConfigurationManager().preparePlayer(this.playerEntity, worldServer);
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    this.playerEntity.theItemInWorldManager.setWorld(worldServer2);
                    this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, worldServer2);
                    this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
                } else {
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
            }
        }
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    @Override
    public void processCloseWindow(C0DPacketCloseWindow c0DPacketCloseWindow) {
        PacketThreadUtil.checkThreadAndEnqueue(c0DPacketCloseWindow, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.closeContainer();
    }

    @Override
    public void processChatMessage(C01PacketChatMessage c01PacketChatMessage) {
        PacketThreadUtil.checkThreadAndEnqueue(c01PacketChatMessage, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(chatComponentTranslation));
        } else {
            this.playerEntity.markPlayerActive();
            String string = c01PacketChatMessage.getMessage();
            string = StringUtils.normalizeSpace((String)string);
            int n = 0;
            while (n < string.length()) {
                if (!ChatAllowedCharacters.isAllowedCharacter(string.charAt(n))) {
                    this.kickPlayerFromServer("Illegal characters in chat");
                    return;
                }
                ++n;
            }
            if (string.startsWith("/")) {
                this.handleSlashCommand(string);
            } else {
                ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.type.text", this.playerEntity.getDisplayName(), string);
                this.serverController.getConfigurationManager().sendChatMsgImpl(chatComponentTranslation, false);
            }
            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())) {
                this.kickPlayerFromServer("disconnect.spam");
            }
        }
    }

    @Override
    public void processPlayerAbilities(C13PacketPlayerAbilities c13PacketPlayerAbilities) {
        PacketThreadUtil.checkThreadAndEnqueue(c13PacketPlayerAbilities, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.capabilities.isFlying = c13PacketPlayerAbilities.isFlying() && this.playerEntity.capabilities.allowFlying;
    }

    @Override
    public void handleResourcePackStatus(C19PacketResourcePackStatus c19PacketResourcePackStatus) {
    }

    @Override
    public void processTabComplete(C14PacketTabComplete c14PacketTabComplete) {
        PacketThreadUtil.checkThreadAndEnqueue(c14PacketTabComplete, this, this.playerEntity.getServerForPlayer());
        ArrayList arrayList = Lists.newArrayList();
        for (String string : this.serverController.getTabCompletions(this.playerEntity, c14PacketTabComplete.getMessage(), c14PacketTabComplete.getTargetBlock())) {
            arrayList.add(string);
        }
        this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete(arrayList.toArray(new String[arrayList.size()])));
    }

    @Override
    public void update() {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");
        if ((long)this.networkTickCount - this.lastSentPingPacket > 40L) {
            this.lastSentPingPacket = this.networkTickCount;
            this.lastPingTime = this.currentTimeMillis();
            this.field_147378_h = (int)this.lastPingTime;
            this.sendPacket(new S00PacketKeepAlive(this.field_147378_h));
        }
        this.serverController.theProfiler.endSection();
        if (this.chatSpamThresholdCount > 0) {
            --this.chatSpamThresholdCount;
        }
        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > (long)(this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60)) {
            this.kickPlayerFromServer("You have been idle for too long!");
        }
    }

    private boolean func_183006_b(C03PacketPlayer c03PacketPlayer) {
        return !Doubles.isFinite((double)c03PacketPlayer.getPositionX()) || !Doubles.isFinite((double)c03PacketPlayer.getPositionY()) || !Doubles.isFinite((double)c03PacketPlayer.getPositionZ()) || !Floats.isFinite((float)c03PacketPlayer.getPitch()) || !Floats.isFinite((float)c03PacketPlayer.getYaw());
    }

    @Override
    public void processHeldItemChange(C09PacketHeldItemChange c09PacketHeldItemChange) {
        PacketThreadUtil.checkThreadAndEnqueue(c09PacketHeldItemChange, this, this.playerEntity.getServerForPlayer());
        if (c09PacketHeldItemChange.getSlotId() >= 0 && c09PacketHeldItemChange.getSlotId() < InventoryPlayer.getHotbarSize()) {
            this.playerEntity.inventory.currentItem = c09PacketHeldItemChange.getSlotId();
            this.playerEntity.markPlayerActive();
        } else {
            logger.warn(String.valueOf(this.playerEntity.getName()) + " tried to set an invalid carried item");
        }
    }

    public void setPlayerLocation(double d, double d2, double d3, float f, float f2) {
        this.setPlayerLocation(d, d2, d3, f, f2, Collections.emptySet());
    }

    @Override
    public void processVanilla250Packet(C17PacketCustomPayload c17PacketCustomPayload) {
        block44: {
            PacketThreadUtil.checkThreadAndEnqueue(c17PacketCustomPayload, this, this.playerEntity.getServerForPlayer());
            if ("MC|BEdit".equals(c17PacketCustomPayload.getChannelName())) {
                PacketBuffer packetBuffer;
                block40: {
                    ItemStack itemStack;
                    ItemStack itemStack2;
                    block41: {
                        packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)c17PacketCustomPayload.getBufferData()));
                        try {
                            itemStack2 = packetBuffer.readItemStackFromBuffer();
                            if (itemStack2 == null) break block40;
                            if (!ItemWritableBook.isNBTValid(itemStack2.getTagCompound())) {
                                throw new IOException("Invalid book tag!");
                            }
                            itemStack = this.playerEntity.inventory.getCurrentItem();
                            if (itemStack != null) break block41;
                            packetBuffer.release();
                        }
                        catch (Exception exception) {
                            logger.error("Couldn't handle book info", (Throwable)exception);
                            packetBuffer.release();
                            return;
                        }
                        return;
                    }
                    if (itemStack2.getItem() == Items.writable_book && itemStack2.getItem() == itemStack.getItem()) {
                        itemStack.setTagInfo("pages", itemStack2.getTagCompound().getTagList("pages", 8));
                    }
                    packetBuffer.release();
                    return;
                }
                packetBuffer.release();
                return;
            }
            if ("MC|BSign".equals(c17PacketCustomPayload.getChannelName())) {
                PacketBuffer packetBuffer;
                block42: {
                    ItemStack itemStack;
                    ItemStack itemStack3;
                    block43: {
                        packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)c17PacketCustomPayload.getBufferData()));
                        try {
                            itemStack3 = packetBuffer.readItemStackFromBuffer();
                            if (itemStack3 == null) break block42;
                            if (!ItemEditableBook.validBookTagContents(itemStack3.getTagCompound())) {
                                throw new IOException("Invalid book tag!");
                            }
                            itemStack = this.playerEntity.inventory.getCurrentItem();
                            if (itemStack != null) break block43;
                            packetBuffer.release();
                        }
                        catch (Exception exception) {
                            logger.error("Couldn't sign book", (Throwable)exception);
                            packetBuffer.release();
                            return;
                        }
                        return;
                    }
                    if (itemStack3.getItem() == Items.written_book && itemStack.getItem() == Items.writable_book) {
                        itemStack.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                        itemStack.setTagInfo("title", new NBTTagString(itemStack3.getTagCompound().getString("title")));
                        itemStack.setTagInfo("pages", itemStack3.getTagCompound().getTagList("pages", 8));
                        itemStack.setItem(Items.written_book);
                    }
                    packetBuffer.release();
                    return;
                }
                packetBuffer.release();
                return;
            }
            if ("MC|TrSel".equals(c17PacketCustomPayload.getChannelName())) {
                try {
                    int n = c17PacketCustomPayload.getBufferData().readInt();
                    Container container = this.playerEntity.openContainer;
                    if (container instanceof ContainerMerchant) {
                        ((ContainerMerchant)container).setCurrentRecipeIndex(n);
                    }
                }
                catch (Exception exception) {
                    logger.error("Couldn't select trade", (Throwable)exception);
                }
            } else if ("MC|AdvCdm".equals(c17PacketCustomPayload.getChannelName())) {
                if (!this.serverController.isCommandBlockEnabled()) {
                    this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
                } else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
                    PacketBuffer packetBuffer;
                    block45: {
                        packetBuffer = c17PacketCustomPayload.getBufferData();
                        try {
                            Object object;
                            byte by = packetBuffer.readByte();
                            CommandBlockLogic commandBlockLogic = null;
                            if (by == 0) {
                                object = this.playerEntity.worldObj.getTileEntity(new BlockPos(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readInt()));
                                if (object instanceof TileEntityCommandBlock) {
                                    commandBlockLogic = ((TileEntityCommandBlock)object).getCommandBlockLogic();
                                }
                            } else if (by == 1 && (object = this.playerEntity.worldObj.getEntityByID(packetBuffer.readInt())) instanceof EntityMinecartCommandBlock) {
                                commandBlockLogic = ((EntityMinecartCommandBlock)object).getCommandBlockLogic();
                            }
                            object = packetBuffer.readStringFromBuffer(packetBuffer.readableBytes());
                            boolean bl = packetBuffer.readBoolean();
                            if (commandBlockLogic == null) break block45;
                            commandBlockLogic.setCommand((String)object);
                            commandBlockLogic.setTrackOutput(bl);
                            if (!bl) {
                                commandBlockLogic.setLastOutput(null);
                            }
                            commandBlockLogic.updateCommand();
                            this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", object));
                        }
                        catch (Exception exception) {
                            logger.error("Couldn't set command block", (Throwable)exception);
                            packetBuffer.release();
                            break block44;
                        }
                    }
                    packetBuffer.release();
                } else {
                    this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
                }
            } else if ("MC|Beacon".equals(c17PacketCustomPayload.getChannelName())) {
                if (this.playerEntity.openContainer instanceof ContainerBeacon) {
                    try {
                        PacketBuffer packetBuffer = c17PacketCustomPayload.getBufferData();
                        int n = packetBuffer.readInt();
                        int n2 = packetBuffer.readInt();
                        ContainerBeacon containerBeacon = (ContainerBeacon)this.playerEntity.openContainer;
                        Slot slot = containerBeacon.getSlot(0);
                        if (slot.getHasStack()) {
                            slot.decrStackSize(1);
                            IInventory iInventory = containerBeacon.func_180611_e();
                            iInventory.setField(1, n);
                            iInventory.setField(2, n2);
                            iInventory.markDirty();
                        }
                    }
                    catch (Exception exception) {
                        logger.error("Couldn't set beacon", (Throwable)exception);
                    }
                }
            } else if ("MC|ItemName".equals(c17PacketCustomPayload.getChannelName()) && this.playerEntity.openContainer instanceof ContainerRepair) {
                ContainerRepair containerRepair = (ContainerRepair)this.playerEntity.openContainer;
                if (c17PacketCustomPayload.getBufferData() != null && c17PacketCustomPayload.getBufferData().readableBytes() >= 1) {
                    String string = ChatAllowedCharacters.filterAllowedCharacters(c17PacketCustomPayload.getBufferData().readStringFromBuffer(Short.MAX_VALUE));
                    if (string.length() <= 30) {
                        containerRepair.updateItemName(string);
                    }
                } else {
                    containerRepair.updateItemName("");
                }
            }
        }
    }

    @Override
    public void processClickWindow(C0EPacketClickWindow c0EPacketClickWindow) {
        PacketThreadUtil.checkThreadAndEnqueue(c0EPacketClickWindow, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == c0EPacketClickWindow.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            if (this.playerEntity.isSpectator()) {
                ArrayList arrayList = Lists.newArrayList();
                int n = 0;
                while (n < this.playerEntity.openContainer.inventorySlots.size()) {
                    arrayList.add(this.playerEntity.openContainer.inventorySlots.get(n).getStack());
                    ++n;
                }
                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, arrayList);
            } else {
                ItemStack itemStack = this.playerEntity.openContainer.slotClick(c0EPacketClickWindow.getSlotId(), c0EPacketClickWindow.getUsedButton(), c0EPacketClickWindow.getMode(), this.playerEntity);
                if (ItemStack.areItemStacksEqual(c0EPacketClickWindow.getClickedItem(), itemStack)) {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(c0EPacketClickWindow.getWindowId(), c0EPacketClickWindow.getActionNumber(), true));
                    this.playerEntity.isChangingQuantityOnly = true;
                    this.playerEntity.openContainer.detectAndSendChanges();
                    this.playerEntity.updateHeldItem();
                    this.playerEntity.isChangingQuantityOnly = false;
                } else {
                    this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, c0EPacketClickWindow.getActionNumber());
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(c0EPacketClickWindow.getWindowId(), c0EPacketClickWindow.getActionNumber(), false));
                    this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                    ArrayList arrayList = Lists.newArrayList();
                    int n = 0;
                    while (n < this.playerEntity.openContainer.inventorySlots.size()) {
                        arrayList.add(this.playerEntity.openContainer.inventorySlots.get(n).getStack());
                        ++n;
                    }
                    this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, arrayList);
                }
            }
        }
    }

    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }

    public void sendPacket(final Packet packet) {
        Object object;
        if (packet instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)packet;
            object = this.playerEntity.getChatVisibility();
            if (object == EntityPlayer.EnumChatVisibility.HIDDEN) {
                return;
            }
            if (object == EntityPlayer.EnumChatVisibility.SYSTEM && !s02PacketChat.isChat()) {
                return;
            }
        }
        try {
            this.netManager.sendPacket(packet);
        }
        catch (Throwable throwable) {
            object = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashReportCategory = ((CrashReport)object).makeCategory("Packet being sent");
            crashReportCategory.addCrashSectionCallable("Packet class", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return packet.getClass().getCanonicalName();
                }
            });
            throw new ReportedException((CrashReport)object);
        }
    }

    @Override
    public void processConfirmTransaction(C0FPacketConfirmTransaction c0FPacketConfirmTransaction) {
        PacketThreadUtil.checkThreadAndEnqueue(c0FPacketConfirmTransaction, this, this.playerEntity.getServerForPlayer());
        Short s = this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
        if (s != null && c0FPacketConfirmTransaction.getUid() == s.shortValue() && this.playerEntity.openContainer.windowId == c0FPacketConfirmTransaction.getWindowId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }

    @Override
    public void processClientSettings(C15PacketClientSettings c15PacketClientSettings) {
        PacketThreadUtil.checkThreadAndEnqueue(c15PacketClientSettings, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.handleClientSettings(c15PacketClientSettings);
    }

    private void handleSlashCommand(String string) {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, string);
    }
}

