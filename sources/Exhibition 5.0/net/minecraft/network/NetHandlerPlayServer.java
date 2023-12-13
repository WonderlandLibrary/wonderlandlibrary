// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

import org.apache.logging.log4j.LogManager;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.init.Items;
import java.io.IOException;
import net.minecraft.item.ItemWritableBook;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C15PacketClientSettings;
import java.util.Iterator;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import java.util.ArrayList;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListBansEntry;
import java.util.Date;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import java.util.Set;
import java.util.Collections;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.play.client.C0CPacketInput;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NetHandlerPlayServer implements INetHandlerPlayServer, IUpdatePlayerListBox
{
    private static final Logger logger;
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private int field_175090_f;
    private int floatingTickCount;
    private boolean field_147366_g;
    private int field_147378_h;
    private long lastPingTime;
    private long lastSentPingPacket;
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private IntHashMap field_147372_n;
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean hasMoved;
    private static final String __OBFID = "CL_00001452";
    
    public NetHandlerPlayServer(final MinecraftServer server, final NetworkManager networkManagerIn, final EntityPlayerMP playerIn) {
        this.field_147372_n = new IntHashMap();
        this.hasMoved = true;
        this.serverController = server;
        (this.netManager = networkManagerIn).setNetHandler(this);
        this.playerEntity = playerIn;
        playerIn.playerNetServerHandler = this;
    }
    
    @Override
    public void update() {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");
        if (this.networkTickCount - this.lastSentPingPacket > 40L) {
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
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60) {
            this.kickPlayerFromServer("You have been idle for too long!");
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public void kickPlayerFromServer(final String reason) {
        final ChatComponentText var2 = new ChatComponentText(reason);
        this.netManager.sendPacket(new S40PacketDisconnect(var2), (GenericFutureListener)new GenericFutureListener() {
            private static final String __OBFID = "CL_00001453";
            
            public void operationComplete(final Future p_operationComplete_1_) {
                NetHandlerPlayServer.this.netManager.closeChannel(var2);
            }
        }, new GenericFutureListener[0]);
        this.netManager.disableAutoRead();
        Futures.getUnchecked((java.util.concurrent.Future)this.serverController.addScheduledTask(new Runnable() {
            private static final String __OBFID = "CL_00001454";
            
            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        }));
    }
    
    @Override
    public void processInput(final C0CPacketInput packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
    }
    
    @Override
    public void processPlayer(final C03PacketPlayer packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        this.field_147366_g = true;
        if (!this.playerEntity.playerConqueredTheEnd) {
            final double var3 = this.playerEntity.posX;
            final double var4 = this.playerEntity.posY;
            final double var5 = this.playerEntity.posZ;
            double var6 = 0.0;
            final double var7 = packetIn.getPositionX() - this.lastPosX;
            final double var8 = packetIn.getPositionY() - this.lastPosY;
            final double var9 = packetIn.getPositionZ() - this.lastPosZ;
            if (packetIn.isMoving()) {
                var6 = var7 * var7 + var8 * var8 + var9 * var9;
                if (!this.hasMoved && var6 < 0.25) {
                    this.hasMoved = true;
                }
            }
            if (this.hasMoved) {
                this.field_175090_f = this.networkTickCount;
                if (this.playerEntity.ridingEntity != null) {
                    float var10 = this.playerEntity.rotationYaw;
                    float var11 = this.playerEntity.rotationPitch;
                    this.playerEntity.ridingEntity.updateRiderPosition();
                    final double var12 = this.playerEntity.posX;
                    final double var13 = this.playerEntity.posY;
                    final double var14 = this.playerEntity.posZ;
                    if (packetIn.getRotating()) {
                        var10 = packetIn.getYaw();
                        var11 = packetIn.getPitch();
                    }
                    this.playerEntity.onGround = packetIn.isOnGround();
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(var12, var13, var14, var10, var11);
                    if (this.playerEntity.ridingEntity != null) {
                        this.playerEntity.ridingEntity.updateRiderPosition();
                    }
                    this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                    if (this.playerEntity.ridingEntity != null) {
                        if (var6 > 4.0) {
                            final Entity var15 = this.playerEntity.ridingEntity;
                            this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(var15));
                            this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        }
                        this.playerEntity.ridingEntity.isAirBorne = true;
                    }
                    if (this.hasMoved) {
                        this.lastPosX = this.playerEntity.posX;
                        this.lastPosY = this.playerEntity.posY;
                        this.lastPosZ = this.playerEntity.posZ;
                    }
                    var2.updateEntity(this.playerEntity);
                    return;
                }
                if (this.playerEntity.isPlayerSleeping()) {
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    var2.updateEntity(this.playerEntity);
                    return;
                }
                final double var16 = this.playerEntity.posY;
                this.lastPosX = this.playerEntity.posX;
                this.lastPosY = this.playerEntity.posY;
                this.lastPosZ = this.playerEntity.posZ;
                double var12 = this.playerEntity.posX;
                double var13 = this.playerEntity.posY;
                double var14 = this.playerEntity.posZ;
                float var17 = this.playerEntity.rotationYaw;
                float var18 = this.playerEntity.rotationPitch;
                if (packetIn.isMoving() && packetIn.getPositionY() == -999.0) {
                    packetIn.setMoving(false);
                }
                if (packetIn.isMoving()) {
                    var12 = packetIn.getPositionX();
                    var13 = packetIn.getPositionY();
                    var14 = packetIn.getPositionZ();
                    if (Math.abs(packetIn.getPositionX()) > 3.0E7 || Math.abs(packetIn.getPositionZ()) > 3.0E7) {
                        this.kickPlayerFromServer("Illegal position");
                        return;
                    }
                }
                if (packetIn.getRotating()) {
                    var17 = packetIn.getYaw();
                    var18 = packetIn.getPitch();
                }
                this.playerEntity.onUpdateEntity();
                this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var17, var18);
                if (!this.hasMoved) {
                    return;
                }
                double var19 = var12 - this.playerEntity.posX;
                double var20 = var13 - this.playerEntity.posY;
                double var21 = var14 - this.playerEntity.posZ;
                final double var22 = Math.min(Math.abs(var19), Math.abs(this.playerEntity.motionX));
                final double var23 = Math.min(Math.abs(var20), Math.abs(this.playerEntity.motionY));
                final double var24 = Math.min(Math.abs(var21), Math.abs(this.playerEntity.motionZ));
                double var25 = var22 * var22 + var23 * var23 + var24 * var24;
                if (var25 > 100.0 && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                    NetHandlerPlayServer.logger.warn(this.playerEntity.getName() + " moved too quickly! " + var19 + "," + var20 + "," + var21 + " (" + var22 + ", " + var23 + ", " + var24 + ")");
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    return;
                }
                final float var26 = 0.0625f;
                final boolean var27 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(var26, var26, var26)).isEmpty();
                if (this.playerEntity.onGround && !packetIn.isOnGround() && var20 > 0.0) {
                    this.playerEntity.jump();
                }
                this.playerEntity.moveEntity(var19, var20, var21);
                this.playerEntity.onGround = packetIn.isOnGround();
                final double var28 = var20;
                var19 = var12 - this.playerEntity.posX;
                var20 = var13 - this.playerEntity.posY;
                if (var20 > -0.5 || var20 < 0.5) {
                    var20 = 0.0;
                }
                var21 = var14 - this.playerEntity.posZ;
                var25 = var19 * var19 + var20 * var20 + var21 * var21;
                boolean var29 = false;
                if (var25 > 0.0625 && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
                    var29 = true;
                    NetHandlerPlayServer.logger.warn(this.playerEntity.getName() + " moved wrongly!");
                }
                this.playerEntity.setPositionAndRotation(var12, var13, var14, var17, var18);
                this.playerEntity.addMovementStat(this.playerEntity.posX - var3, this.playerEntity.posY - var4, this.playerEntity.posZ - var5);
                if (!this.playerEntity.noClip) {
                    final boolean var30 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(var26, var26, var26)).isEmpty();
                    if (var27 && (var29 || !var30) && !this.playerEntity.isPlayerSleeping()) {
                        this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, var17, var18);
                        return;
                    }
                }
                final AxisAlignedBB var31 = this.playerEntity.getEntityBoundingBox().expand(var26, var26, var26).addCoord(0.0, -0.55, 0.0);
                if (!this.serverController.isFlightAllowed() && !this.playerEntity.capabilities.allowFlying && !var2.checkBlockCollision(var31)) {
                    if (var28 >= -0.03125) {
                        ++this.floatingTickCount;
                        if (this.floatingTickCount > 80) {
                            NetHandlerPlayServer.logger.warn(this.playerEntity.getName() + " was kicked for floating too long!");
                            this.kickPlayerFromServer("Flying is not enabled on this server");
                            return;
                        }
                    }
                }
                else {
                    this.floatingTickCount = 0;
                }
                this.playerEntity.onGround = packetIn.isOnGround();
                this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                this.playerEntity.handleFalling(this.playerEntity.posY - var16, packetIn.isOnGround());
            }
            else if (this.networkTickCount - this.field_175090_f > 20) {
                this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            }
        }
    }
    
    public void setPlayerLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.func_175089_a(x, y, z, yaw, pitch, Collections.emptySet());
    }
    
    public void func_175089_a(final double p_175089_1_, final double p_175089_3_, final double p_175089_5_, final float p_175089_7_, final float p_175089_8_, final Set p_175089_9_) {
        this.hasMoved = false;
        this.lastPosX = p_175089_1_;
        this.lastPosY = p_175089_3_;
        this.lastPosZ = p_175089_5_;
        if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            this.lastPosX += this.playerEntity.posX;
        }
        if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            this.lastPosY += this.playerEntity.posY;
        }
        if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            this.lastPosZ += this.playerEntity.posZ;
        }
        float var10 = p_175089_7_;
        float var11 = p_175089_8_;
        if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            var10 = p_175089_7_ + this.playerEntity.rotationYaw;
        }
        if (p_175089_9_.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            var11 = p_175089_8_ + this.playerEntity.rotationPitch;
        }
        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var10, var11);
        this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(p_175089_1_, p_175089_3_, p_175089_5_, p_175089_7_, p_175089_8_, p_175089_9_));
    }
    
    @Override
    public void processPlayerDigging(final C07PacketPlayerDigging packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos var3 = packetIn.func_179715_a();
        this.playerEntity.markPlayerActive();
        switch (SwitchAction.field_180224_a[packetIn.func_180762_c().ordinal()]) {
            case 1: {
                if (!this.playerEntity.func_175149_v()) {
                    this.playerEntity.dropOneItem(false);
                }
            }
            case 2: {
                if (!this.playerEntity.func_175149_v()) {
                    this.playerEntity.dropOneItem(true);
                }
            }
            case 3: {
                this.playerEntity.stopUsingItem();
            }
            case 4:
            case 5:
            case 6: {
                final double var4 = this.playerEntity.posX - (var3.getX() + 0.5);
                final double var5 = this.playerEntity.posY - (var3.getY() + 0.5) + 1.5;
                final double var6 = this.playerEntity.posZ - (var3.getZ() + 0.5);
                final double var7 = var4 * var4 + var5 * var5 + var6 * var6;
                if (var7 > 36.0) {
                    return;
                }
                if (var3.getY() >= this.serverController.getBuildLimit()) {
                    return;
                }
                if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(var2, var3, this.playerEntity) && var2.getWorldBorder().contains(var3)) {
                        this.playerEntity.theItemInWorldManager.func_180784_a(var3, packetIn.func_179714_b());
                    }
                    else {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
                    }
                }
                else {
                    if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.func_180785_a(var3);
                    }
                    else if (packetIn.func_180762_c() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.func_180238_e();
                    }
                    if (var2.getBlockState(var3).getBlock().getMaterial() != Material.air) {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
                    }
                }
            }
            default: {
                throw new IllegalArgumentException("Invalid player action");
            }
        }
    }
    
    @Override
    public void processPlayerBlockPlacement(final C08PacketPlayerBlockPlacement packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        ItemStack var3 = this.playerEntity.inventory.getCurrentItem();
        boolean var4 = false;
        final BlockPos var5 = packetIn.getBlockPos();
        final EnumFacing var6 = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
        this.playerEntity.markPlayerActive();
        if (packetIn.getPlacedBlockDirection() == 255) {
            if (var3 == null) {
                return;
            }
            this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, var2, var3);
        }
        else if (var5.getY() >= this.serverController.getBuildLimit() - 1 && (var6 == EnumFacing.UP || var5.getY() >= this.serverController.getBuildLimit())) {
            final ChatComponentTranslation var7 = new ChatComponentTranslation("build.tooHigh", new Object[] { this.serverController.getBuildLimit() });
            var7.getChatStyle().setColor(EnumChatFormatting.RED);
            this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(var7));
            var4 = true;
        }
        else {
            if (this.hasMoved && this.playerEntity.getDistanceSq(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5) < 64.0 && !this.serverController.isBlockProtected(var2, var5, this.playerEntity) && var2.getWorldBorder().contains(var5)) {
                this.playerEntity.theItemInWorldManager.func_180236_a(this.playerEntity, var2, var3, var5, var6, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
            }
            var4 = true;
        }
        if (var4) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5));
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var5.offset(var6)));
        }
        var3 = this.playerEntity.inventory.getCurrentItem();
        if (var3 != null && var3.stackSize == 0) {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            var3 = null;
        }
        if (var3 == null || var3.getMaxItemUseDuration() == 0) {
            this.playerEntity.isChangingQuantityOnly = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            final Slot var8 = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.isChangingQuantityOnly = false;
            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack())) {
                this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, var8.slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }
    
    @Override
    public void func_175088_a(final C18PacketSpectate p_175088_1_) {
        PacketThreadUtil.func_180031_a(p_175088_1_, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.func_175149_v()) {
            Entity var2 = null;
            for (final WorldServer var6 : this.serverController.worldServers) {
                if (var6 != null) {
                    var2 = p_175088_1_.func_179727_a(var6);
                    if (var2 != null) {
                        break;
                    }
                }
            }
            if (var2 != null) {
                this.playerEntity.func_175399_e(this.playerEntity);
                this.playerEntity.mountEntity(null);
                if (var2.worldObj != this.playerEntity.worldObj) {
                    final WorldServer var7 = this.playerEntity.getServerForPlayer();
                    final WorldServer var8 = (WorldServer)var2.worldObj;
                    this.playerEntity.dimension = var2.dimension;
                    this.sendPacket(new S07PacketRespawn(this.playerEntity.dimension, var7.getDifficulty(), var7.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
                    var7.removePlayerEntityDangerously(this.playerEntity);
                    this.playerEntity.isDead = false;
                    this.playerEntity.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
                    if (this.playerEntity.isEntityAlive()) {
                        var7.updateEntityWithOptionalForce(this.playerEntity, false);
                        var8.spawnEntityInWorld(this.playerEntity);
                        var8.updateEntityWithOptionalForce(this.playerEntity, false);
                    }
                    this.playerEntity.setWorld(var8);
                    this.serverController.getConfigurationManager().func_72375_a(this.playerEntity, var7);
                    this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
                    this.playerEntity.theItemInWorldManager.setWorld(var8);
                    this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, var8);
                    this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
                }
                else {
                    this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
                }
            }
        }
    }
    
    @Override
    public void func_175086_a(final C19PacketResourcePackStatus p_175086_1_) {
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        NetHandlerPlayServer.logger.info(this.playerEntity.getName() + " lost connection: " + reason);
        this.serverController.refreshStatusNextTick();
        final ChatComponentTranslation var2 = new ChatComponentTranslation("multiplayer.player.left", new Object[] { this.playerEntity.getDisplayName() });
        var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.serverController.getConfigurationManager().sendChatMsg(var2);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
        if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            NetHandlerPlayServer.logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }
    
    public void sendPacket(final Packet packetIn) {
        if (packetIn instanceof S02PacketChat) {
            final S02PacketChat var2 = (S02PacketChat)packetIn;
            final EntityPlayer.EnumChatVisibility var3 = this.playerEntity.getChatVisibility();
            if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN) {
                return;
            }
            if (var3 == EntityPlayer.EnumChatVisibility.SYSTEM && !var2.isChat()) {
                return;
            }
        }
        try {
            this.netManager.sendPacket(packetIn);
        }
        catch (Throwable var5) {
            final CrashReport var4 = CrashReport.makeCrashReport(var5, "Sending packet");
            final CrashReportCategory var6 = var4.makeCategory("Packet being sent");
            var6.addCrashSectionCallable("Packet class", new Callable() {
                private static final String __OBFID = "CL_00002270";
                
                public String func_180225_a() {
                    return packetIn.getClass().getCanonicalName();
                }
                
                @Override
                public Object call() {
                    return this.func_180225_a();
                }
            });
            throw new ReportedException(var4);
        }
    }
    
    @Override
    public void processHeldItemChange(final C09PacketHeldItemChange packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize()) {
            this.playerEntity.inventory.currentItem = packetIn.getSlotId();
            this.playerEntity.markPlayerActive();
        }
        else {
            NetHandlerPlayServer.logger.warn(this.playerEntity.getName() + " tried to set an invalid carried item");
        }
    }
    
    @Override
    public void processChatMessage(final C01PacketChatMessage packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            final ChatComponentTranslation var4 = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            var4.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(var4));
        }
        else {
            this.playerEntity.markPlayerActive();
            String var5 = packetIn.getMessage();
            var5 = StringUtils.normalizeSpace(var5);
            for (int var6 = 0; var6 < var5.length(); ++var6) {
                if (!ChatAllowedCharacters.isAllowedCharacter(var5.charAt(var6))) {
                    this.kickPlayerFromServer("Illegal characters in chat");
                    return;
                }
            }
            if (var5.startsWith("/")) {
                this.handleSlashCommand(var5);
            }
            else {
                final ChatComponentTranslation var7 = new ChatComponentTranslation("chat.type.text", new Object[] { this.playerEntity.getDisplayName(), var5 });
                this.serverController.getConfigurationManager().sendChatMsgImpl(var7, false);
            }
            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())) {
                this.kickPlayerFromServer("disconnect.spam");
            }
        }
    }
    
    private void handleSlashCommand(final String command) {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
    }
    
    @Override
    public void func_175087_a(final C0APacketAnimation p_175087_1_) {
        PacketThreadUtil.func_180031_a(p_175087_1_, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        this.playerEntity.swingItem();
    }
    
    @Override
    public void processEntityAction(final C0BPacketEntityAction packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        switch (SwitchAction.field_180222_b[packetIn.getAction().ordinal()]) {
            case 1: {
                this.playerEntity.setSneaking(true);
                break;
            }
            case 2: {
                this.playerEntity.setSneaking(false);
                break;
            }
            case 3: {
                this.playerEntity.setSprinting(true);
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                this.playerEntity.wakeUpPlayer(false, true, true);
                this.hasMoved = false;
                break;
            }
            case 6: {
                if (this.playerEntity.ridingEntity instanceof EntityHorse) {
                    ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.func_149512_e());
                    break;
                }
                break;
            }
            case 7: {
                if (this.playerEntity.ridingEntity instanceof EntityHorse) {
                    ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
                    break;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }
    
    @Override
    public void processUseEntity(final C02PacketUseEntity packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final Entity var3 = packetIn.getEntityFromWorld(var2);
        this.playerEntity.markPlayerActive();
        if (var3 != null) {
            final boolean var4 = this.playerEntity.canEntityBeSeen(var3);
            double var5 = 36.0;
            if (!var4) {
                var5 = 9.0;
            }
            if (this.playerEntity.getDistanceSqToEntity(var3) < var5) {
                if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    this.playerEntity.interactWith(var3);
                }
                else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
                    var3.func_174825_a(this.playerEntity, packetIn.func_179712_b());
                }
                else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (var3 instanceof EntityItem || var3 instanceof EntityXPOrb || var3 instanceof EntityArrow || var3 == this.playerEntity) {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }
                    this.playerEntity.attackTargetEntityWithCurrentItem(var3);
                }
            }
        }
    }
    
    @Override
    public void processClientStatus(final C16PacketClientStatus packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        final C16PacketClientStatus.EnumState var2 = packetIn.getStatus();
        switch (SwitchAction.field_180223_c[var2.ordinal()]) {
            case 1: {
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
                    final UserListBansEntry var3 = new UserListBansEntry(this.playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
                    this.serverController.getConfigurationManager().getBannedPlayers().addEntry(var3);
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                    break;
                }
                else {
                    if (this.playerEntity.getHealth() > 0.0f) {
                        return;
                    }
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
                    break;
                }
                break;
            }
            case 2: {
                this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
                break;
            }
            case 3: {
                this.playerEntity.triggerAchievement(AchievementList.openInventory);
                break;
            }
        }
    }
    
    @Override
    public void processCloseWindow(final C0DPacketCloseWindow packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.closeContainer();
    }
    
    @Override
    public void processClickWindow(final C0EPacketClickWindow packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            if (this.playerEntity.func_175149_v()) {
                final ArrayList var2 = Lists.newArrayList();
                for (int var3 = 0; var3 < this.playerEntity.openContainer.inventorySlots.size(); ++var3) {
                    var2.add(this.playerEntity.openContainer.inventorySlots.get(var3).getStack());
                }
                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var2);
            }
            else {
                final ItemStack var4 = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), this.playerEntity);
                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), var4)) {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.playerEntity.isChangingQuantityOnly = true;
                    this.playerEntity.openContainer.detectAndSendChanges();
                    this.playerEntity.updateHeldItem();
                    this.playerEntity.isChangingQuantityOnly = false;
                }
                else {
                    this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, packetIn.getActionNumber());
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                    final ArrayList var5 = Lists.newArrayList();
                    for (int var6 = 0; var6 < this.playerEntity.openContainer.inventorySlots.size(); ++var6) {
                        var5.add(this.playerEntity.openContainer.inventorySlots.get(var6).getStack());
                    }
                    this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var5);
                }
            }
        }
    }
    
    @Override
    public void processEnchantItem(final C11PacketEnchantItem packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.func_175149_v()) {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }
    
    @Override
    public void processCreativeInventoryAction(final C10PacketCreativeInventoryAction packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.theItemInWorldManager.isCreative()) {
            final boolean var2 = packetIn.getSlotId() < 0;
            final ItemStack var3 = packetIn.getStack();
            if (var3 != null && var3.hasTagCompound() && var3.getTagCompound().hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound var4 = var3.getTagCompound().getCompoundTag("BlockEntityTag");
                if (var4.hasKey("x") && var4.hasKey("y") && var4.hasKey("z")) {
                    final BlockPos var5 = new BlockPos(var4.getInteger("x"), var4.getInteger("y"), var4.getInteger("z"));
                    final TileEntity var6 = this.playerEntity.worldObj.getTileEntity(var5);
                    if (var6 != null) {
                        final NBTTagCompound var7 = new NBTTagCompound();
                        var6.writeToNBT(var7);
                        var7.removeTag("x");
                        var7.removeTag("y");
                        var7.removeTag("z");
                        var3.setTagInfo("BlockEntityTag", var7);
                    }
                }
            }
            final boolean var8 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
            final boolean var9 = var3 == null || var3.getItem() != null;
            final boolean var10 = var3 == null || (var3.getMetadata() >= 0 && var3.stackSize <= 64 && var3.stackSize > 0);
            if (var8 && var9 && var10) {
                if (var3 == null) {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), null);
                }
                else {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), var3);
                }
                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            }
            else if (var2 && var9 && var10 && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                final EntityItem var11 = this.playerEntity.dropPlayerItemWithRandomChoice(var3, true);
                if (var11 != null) {
                    var11.setAgeToCreativeDespawnTime();
                }
            }
        }
    }
    
    @Override
    public void processConfirmTransaction(final C0FPacketConfirmTransaction packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final Short var2 = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
        if (var2 != null && packetIn.getUid() == var2 && this.playerEntity.openContainer.windowId == packetIn.getId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.func_175149_v()) {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }
    
    @Override
    public void processUpdateSign(final C12PacketUpdateSign packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        final WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos var3 = packetIn.func_179722_a();
        if (var2.isBlockLoaded(var3)) {
            final TileEntity var4 = var2.getTileEntity(var3);
            if (!(var4 instanceof TileEntitySign)) {
                return;
            }
            final TileEntitySign var5 = (TileEntitySign)var4;
            if (!var5.getIsEditable() || var5.func_145911_b() != this.playerEntity) {
                this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }
            System.arraycopy(packetIn.func_180768_b(), 0, var5.signText, 0, 4);
            var5.markDirty();
            var2.markBlockForUpdate(var3);
        }
    }
    
    @Override
    public void processKeepAlive(final C00PacketKeepAlive packetIn) {
        if (packetIn.getKey() == this.field_147378_h) {
            final int var2 = (int)(this.currentTimeMillis() - this.lastPingTime);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + var2) / 4;
        }
    }
    
    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }
    
    @Override
    public void processPlayerAbilities(final C13PacketPlayerAbilities packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.capabilities.isFlying = (packetIn.isFlying() && this.playerEntity.capabilities.allowFlying);
    }
    
    @Override
    public void processTabComplete(final C14PacketTabComplete packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        final ArrayList var2 = Lists.newArrayList();
        for (final String var4 : this.serverController.func_180506_a(this.playerEntity, packetIn.getMessage(), packetIn.func_179709_b())) {
            var2.add(var4);
        }
        this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete(var2.toArray(new String[var2.size()])));
    }
    
    @Override
    public void processClientSettings(final C15PacketClientSettings packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.handleClientSettings(packetIn);
    }
    
    @Override
    public void processVanilla250Packet(final C17PacketCustomPayload packetIn) {
        PacketThreadUtil.func_180031_a(packetIn, this, this.playerEntity.getServerForPlayer());
        if ("MC|BEdit".equals(packetIn.getChannelName())) {
            final PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)packetIn.getBufferData()));
            try {
                final ItemStack var3 = var2.readItemStackFromBuffer();
                if (var3 == null) {
                    return;
                }
                if (!ItemWritableBook.validBookPageTagContents(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                final ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
                if (var4 != null) {
                    if (var3.getItem() == Items.writable_book && var3.getItem() == var4.getItem()) {
                        var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                    }
                }
            }
            catch (Exception var5) {
                NetHandlerPlayServer.logger.error("Couldn't handle book info", (Throwable)var5);
            }
            finally {
                var2.release();
            }
            return;
        }
        if ("MC|BSign".equals(packetIn.getChannelName())) {
            final PacketBuffer var2 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)packetIn.getBufferData()));
            try {
                final ItemStack var3 = var2.readItemStackFromBuffer();
                if (var3 == null) {
                    return;
                }
                if (!ItemEditableBook.validBookTagContents(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                final ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
                if (var4 != null) {
                    if (var3.getItem() == Items.written_book && var4.getItem() == Items.writable_book) {
                        var4.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                        var4.setTagInfo("title", new NBTTagString(var3.getTagCompound().getString("title")));
                        var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                        var4.setItem(Items.written_book);
                    }
                }
            }
            catch (Exception var6) {
                NetHandlerPlayServer.logger.error("Couldn't sign book", (Throwable)var6);
            }
            finally {
                var2.release();
            }
            return;
        }
        if ("MC|TrSel".equals(packetIn.getChannelName())) {
            try {
                final int var7 = packetIn.getBufferData().readInt();
                final Container var8 = this.playerEntity.openContainer;
                if (var8 instanceof ContainerMerchant) {
                    ((ContainerMerchant)var8).setCurrentRecipeIndex(var7);
                }
            }
            catch (Exception var9) {
                NetHandlerPlayServer.logger.error("Couldn't select trade", (Throwable)var9);
            }
        }
        else if ("MC|AdvCdm".equals(packetIn.getChannelName())) {
            if (!this.serverController.isCommandBlockEnabled()) {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
            }
            else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
                final PacketBuffer var2 = packetIn.getBufferData();
                try {
                    final byte var10 = var2.readByte();
                    CommandBlockLogic var11 = null;
                    if (var10 == 0) {
                        final TileEntity var12 = this.playerEntity.worldObj.getTileEntity(new BlockPos(var2.readInt(), var2.readInt(), var2.readInt()));
                        if (var12 instanceof TileEntityCommandBlock) {
                            var11 = ((TileEntityCommandBlock)var12).getCommandBlockLogic();
                        }
                    }
                    else if (var10 == 1) {
                        final Entity var13 = this.playerEntity.worldObj.getEntityByID(var2.readInt());
                        if (var13 instanceof EntityMinecartCommandBlock) {
                            var11 = ((EntityMinecartCommandBlock)var13).func_145822_e();
                        }
                    }
                    final String var14 = var2.readStringFromBuffer(var2.readableBytes());
                    final boolean var15 = var2.readBoolean();
                    if (var11 != null) {
                        var11.setCommand(var14);
                        var11.func_175573_a(var15);
                        if (!var15) {
                            var11.func_145750_b(null);
                        }
                        var11.func_145756_e();
                        this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", new Object[] { var14 }));
                    }
                }
                catch (Exception var16) {
                    NetHandlerPlayServer.logger.error("Couldn't set command block", (Throwable)var16);
                }
                finally {
                    var2.release();
                }
            }
            else {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
            }
        }
        else if ("MC|Beacon".equals(packetIn.getChannelName())) {
            if (this.playerEntity.openContainer instanceof ContainerBeacon) {
                try {
                    final PacketBuffer var2 = packetIn.getBufferData();
                    final int var17 = var2.readInt();
                    final int var18 = var2.readInt();
                    final ContainerBeacon var19 = (ContainerBeacon)this.playerEntity.openContainer;
                    final Slot var20 = var19.getSlot(0);
                    if (var20.getHasStack()) {
                        var20.decrStackSize(1);
                        final IInventory var21 = var19.func_180611_e();
                        var21.setField(1, var17);
                        var21.setField(2, var18);
                        var21.markDirty();
                    }
                }
                catch (Exception var22) {
                    NetHandlerPlayServer.logger.error("Couldn't set beacon", (Throwable)var22);
                }
            }
        }
        else if ("MC|ItemName".equals(packetIn.getChannelName()) && this.playerEntity.openContainer instanceof ContainerRepair) {
            final ContainerRepair var23 = (ContainerRepair)this.playerEntity.openContainer;
            if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1) {
                final String var24 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
                if (var24.length() <= 30) {
                    var23.updateItemName(var24);
                }
            }
            else {
                var23.updateItemName("");
            }
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    static final class SwitchAction
    {
        static final int[] field_180224_a;
        static final int[] field_180222_b;
        static final int[] field_180223_c;
        private static final String __OBFID = "CL_00002269";
        
        static {
            field_180223_c = new int[C16PacketClientStatus.EnumState.values().length];
            try {
                SwitchAction.field_180223_c[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchAction.field_180223_c[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchAction.field_180223_c[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            field_180222_b = new int[C0BPacketEntityAction.Action.values().length];
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.START_SNEAKING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.STOP_SNEAKING.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.START_SPRINTING.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.STOP_SPRINTING.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.STOP_SLEEPING.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.RIDING_JUMP.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchAction.field_180222_b[C0BPacketEntityAction.Action.OPEN_INVENTORY.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            field_180224_a = new int[C07PacketPlayerDigging.Action.values().length];
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.DROP_ITEM.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.DROP_ALL_ITEMS.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError12) {}
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.RELEASE_USE_ITEM.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError13) {}
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.START_DESTROY_BLOCK.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError14) {}
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError15) {}
            try {
                SwitchAction.field_180224_a[C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError16) {}
        }
    }
}
