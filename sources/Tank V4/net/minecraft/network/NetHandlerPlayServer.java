package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
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

public class NetHandlerPlayServer implements ITickable, INetHandlerPlayServer {
   private double lastPosY;
   private int floatingTickCount;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$client$C07PacketPlayerDigging$Action;
   private double lastPosZ;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$client$C0BPacketEntityAction$Action;
   private boolean field_147366_g;
   public EntityPlayerMP playerEntity;
   private final MinecraftServer serverController;
   private long lastPingTime;
   public final NetworkManager netManager;
   private int networkTickCount;
   private static final Logger logger = LogManager.getLogger();
   private double lastPosX;
   private int chatSpamThresholdCount;
   private boolean hasMoved = true;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$play$client$C16PacketClientStatus$EnumState;
   private int itemDropThreshold;
   private int field_175090_f;
   private IntHashMap field_147372_n = new IntHashMap();
   private int field_147378_h;
   private long lastSentPingPacket;

   public void processCloseWindow(C0DPacketCloseWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.closeContainer();
   }

   public void processConfirmTransaction(C0FPacketConfirmTransaction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      Short var2 = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
      if (var2 != null && var1.getUid() == var2 && this.playerEntity.openContainer.windowId == var1.getWindowId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
         this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
      }

   }

   public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
      ItemStack var3 = this.playerEntity.inventory.getCurrentItem();
      boolean var4 = false;
      BlockPos var5 = var1.getPosition();
      EnumFacing var6 = EnumFacing.getFront(var1.getPlacedBlockDirection());
      this.playerEntity.markPlayerActive();
      if (var1.getPlacedBlockDirection() == 255) {
         if (var3 == null) {
            return;
         }

         this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, var2, var3);
      } else if (var5.getY() >= this.serverController.getBuildLimit() - 1 && (var6 == EnumFacing.UP || var5.getY() >= this.serverController.getBuildLimit())) {
         ChatComponentTranslation var7 = new ChatComponentTranslation("build.tooHigh", new Object[]{this.serverController.getBuildLimit()});
         var7.getChatStyle().setColor(EnumChatFormatting.RED);
         this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(var7));
         var4 = true;
      } else {
         if (this.hasMoved && this.playerEntity.getDistanceSq((double)var5.getX() + 0.5D, (double)var5.getY() + 0.5D, (double)var5.getZ() + 0.5D) < 64.0D && !this.serverController.isBlockProtected(var2, var5, this.playerEntity) && var2.getWorldBorder().contains(var5)) {
            this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, var2, var3, var5, var6, var1.getPlacedBlockOffsetX(), var1.getPlacedBlockOffsetY(), var1.getPlacedBlockOffsetZ());
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
         Slot var8 = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
         this.playerEntity.openContainer.detectAndSendChanges();
         this.playerEntity.isChangingQuantityOnly = false;
         if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), var1.getStack())) {
            this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, var8.slotNumber, this.playerEntity.inventory.getCurrentItem()));
         }
      }

   }

   public void onDisconnect(IChatComponent var1) {
      logger.info(this.playerEntity.getName() + " lost connection: " + var1);
      this.serverController.refreshStatusNextTick();
      ChatComponentTranslation var2 = new ChatComponentTranslation("multiplayer.player.left", new Object[]{this.playerEntity.getDisplayName()});
      var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
      this.serverController.getConfigurationManager().sendChatMsg(var2);
      this.playerEntity.mountEntityAndWakeUp();
      this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
      if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
         logger.info("Stopping singleplayer server as player logged out");
         this.serverController.initiateShutdown();
      }

   }

   public void handleAnimation(C0APacketAnimation var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      this.playerEntity.swingItem();
   }

   public void handleSpectate(C18PacketSpectate var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      if (this.playerEntity.isSpectator()) {
         Entity var2 = null;
         WorldServer[] var6;
         int var5 = (var6 = this.serverController.worldServers).length;

         WorldServer var3;
         for(int var4 = 0; var4 < var5; ++var4) {
            var3 = var6[var4];
            if (var3 != null) {
               var2 = var1.getEntity(var3);
               if (var2 != null) {
                  break;
               }
            }
         }

         if (var2 != null) {
            this.playerEntity.setSpectatingEntity(this.playerEntity);
            this.playerEntity.mountEntity((Entity)null);
            if (var2.worldObj != this.playerEntity.worldObj) {
               var3 = this.playerEntity.getServerForPlayer();
               WorldServer var7 = (WorldServer)var2.worldObj;
               this.playerEntity.dimension = var2.dimension;
               this.sendPacket(new S07PacketRespawn(this.playerEntity.dimension, var3.getDifficulty(), var3.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
               var3.removePlayerEntityDangerously(this.playerEntity);
               this.playerEntity.isDead = false;
               this.playerEntity.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
               if (this.playerEntity.isEntityAlive()) {
                  var3.updateEntityWithOptionalForce(this.playerEntity, false);
                  var7.spawnEntityInWorld(this.playerEntity);
                  var7.updateEntityWithOptionalForce(this.playerEntity, false);
               }

               this.playerEntity.setWorld(var7);
               this.serverController.getConfigurationManager().preparePlayer(this.playerEntity, var3);
               this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
               this.playerEntity.theItemInWorldManager.setWorld(var7);
               this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, var7);
               this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
            } else {
               this.playerEntity.setPositionAndUpdate(var2.posX, var2.posY, var2.posZ);
            }
         }
      }

   }

   public void processUseEntity(C02PacketUseEntity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
      Entity var3 = var1.getEntityFromWorld(var2);
      this.playerEntity.markPlayerActive();
      if (var3 != null) {
         boolean var4 = this.playerEntity.canEntityBeSeen(var3);
         double var5 = 36.0D;
         if (!var4) {
            var5 = 9.0D;
         }

         if (this.playerEntity.getDistanceSqToEntity(var3) < var5) {
            if (var1.getAction() == C02PacketUseEntity.Action.INTERACT) {
               this.playerEntity.interactWith(var3);
            } else if (var1.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
               var3.interactAt(this.playerEntity, var1.getHitVec());
            } else if (var1.getAction() == C02PacketUseEntity.Action.ATTACK) {
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

   static int[] $SWITCH_TABLE$net$minecraft$network$play$client$C0BPacketEntityAction$Action() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$client$C0BPacketEntityAction$Action;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[C0BPacketEntityAction.Action.values().length];

         try {
            var0[C0BPacketEntityAction.Action.OPEN_INVENTORY.ordinal()] = 7;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[C0BPacketEntityAction.Action.RIDING_JUMP.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[C0BPacketEntityAction.Action.START_SNEAKING.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[C0BPacketEntityAction.Action.START_SPRINTING.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[C0BPacketEntityAction.Action.STOP_SLEEPING.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[C0BPacketEntityAction.Action.STOP_SNEAKING.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[C0BPacketEntityAction.Action.STOP_SPRINTING.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$client$C0BPacketEntityAction$Action = var0;
         return var0;
      }
   }

   public void setPlayerLocation(double var1, double var3, double var5, float var7, float var8, Set var9) {
      this.hasMoved = false;
      this.lastPosX = var1;
      this.lastPosY = var3;
      this.lastPosZ = var5;
      if (var9.contains(S08PacketPlayerPosLook.EnumFlags.X)) {
         this.lastPosX += this.playerEntity.posX;
      }

      if (var9.contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
         this.lastPosY += this.playerEntity.posY;
      }

      if (var9.contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
         this.lastPosZ += this.playerEntity.posZ;
      }

      float var10 = var7;
      float var11 = var8;
      if (var9.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
         var10 = var7 + this.playerEntity.rotationYaw;
      }

      if (var9.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
         var11 = var8 + this.playerEntity.rotationPitch;
      }

      this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var10, var11);
      this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(var1, var3, var5, var7, var8, var9));
   }

   static int[] $SWITCH_TABLE$net$minecraft$network$play$client$C07PacketPlayerDigging$Action() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$client$C07PacketPlayerDigging$Action;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[C07PacketPlayerDigging.Action.values().length];

         try {
            var0[C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[C07PacketPlayerDigging.Action.DROP_ALL_ITEMS.ordinal()] = 4;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[C07PacketPlayerDigging.Action.DROP_ITEM.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[C07PacketPlayerDigging.Action.RELEASE_USE_ITEM.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[C07PacketPlayerDigging.Action.START_DESTROY_BLOCK.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$client$C07PacketPlayerDigging$Action = var0;
         return var0;
      }
   }

   public void processEnchantItem(C11PacketEnchantItem var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      if (this.playerEntity.openContainer.windowId == var1.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
         this.playerEntity.openContainer.enchantItem(this.playerEntity, var1.getButton());
         this.playerEntity.openContainer.detectAndSendChanges();
      }

   }

   public NetHandlerPlayServer(MinecraftServer var1, NetworkManager var2, EntityPlayerMP var3) {
      this.serverController = var1;
      this.netManager = var2;
      var2.setNetHandler(this);
      this.playerEntity = var3;
      var3.playerNetServerHandler = this;
   }

   public void processPlayerAbilities(C13PacketPlayerAbilities var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.capabilities.isFlying = var1.isFlying() && this.playerEntity.capabilities.allowFlying;
   }

   public void processCreativeInventoryAction(C10PacketCreativeInventoryAction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      if (this.playerEntity.theItemInWorldManager.isCreative()) {
         boolean var2 = var1.getSlotId() < 0;
         ItemStack var3 = var1.getStack();
         if (var3 != null && var3.hasTagCompound() && var3.getTagCompound().hasKey("BlockEntityTag", 10)) {
            NBTTagCompound var4 = var3.getTagCompound().getCompoundTag("BlockEntityTag");
            if (var4.hasKey("x") && var4.hasKey("y") && var4.hasKey("z")) {
               BlockPos var5 = new BlockPos(var4.getInteger("x"), var4.getInteger("y"), var4.getInteger("z"));
               TileEntity var6 = this.playerEntity.worldObj.getTileEntity(var5);
               if (var6 != null) {
                  NBTTagCompound var7 = new NBTTagCompound();
                  var6.writeToNBT(var7);
                  var7.removeTag("x");
                  var7.removeTag("y");
                  var7.removeTag("z");
                  var3.setTagInfo("BlockEntityTag", var7);
               }
            }
         }

         boolean var8 = var1.getSlotId() >= 1 && var1.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
         boolean var9 = var3 == null || var3.getItem() != null;
         boolean var10 = var3 == null || var3.getMetadata() >= 0 && var3.stackSize <= 64 && var3.stackSize > 0;
         if (var8 && var9 && var10) {
            if (var3 == null) {
               this.playerEntity.inventoryContainer.putStackInSlot(var1.getSlotId(), (ItemStack)null);
            } else {
               this.playerEntity.inventoryContainer.putStackInSlot(var1.getSlotId(), var3);
            }

            this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
         } else if (var2 && var9 && var10 && this.itemDropThreshold < 200) {
            this.itemDropThreshold += 20;
            EntityItem var11 = this.playerEntity.dropPlayerItemWithRandomChoice(var3, true);
            if (var11 != null) {
               var11.setAgeToCreativeDespawnTime();
            }
         }
      }

   }

   public void processEntityAction(C0BPacketEntityAction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      switch($SWITCH_TABLE$net$minecraft$network$play$client$C0BPacketEntityAction$Action()[var1.getAction().ordinal()]) {
      case 1:
         this.playerEntity.setSneaking(true);
         break;
      case 2:
         this.playerEntity.setSneaking(false);
         break;
      case 3:
         this.playerEntity.wakeUpPlayer(false, true, true);
         this.hasMoved = false;
         break;
      case 4:
         this.playerEntity.setSprinting(true);
         break;
      case 5:
         this.playerEntity.setSprinting(false);
         break;
      case 6:
         if (this.playerEntity.ridingEntity instanceof EntityHorse) {
            ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(var1.getAuxData());
         }
         break;
      case 7:
         if (this.playerEntity.ridingEntity instanceof EntityHorse) {
            ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
         }
         break;
      default:
         throw new IllegalArgumentException("Invalid client command!");
      }

   }

   public void update() {
      this.field_147366_g = false;
      ++this.networkTickCount;
      this.serverController.theProfiler.startSection("keepAlive");
      if ((long)this.networkTickCount - this.lastSentPingPacket > 40L) {
         this.lastSentPingPacket = (long)this.networkTickCount;
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

   public NetworkManager getNetworkManager() {
      return this.netManager;
   }

   public void processClickWindow(C0EPacketClickWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      if (this.playerEntity.openContainer.windowId == var1.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
         if (this.playerEntity.isSpectator()) {
            ArrayList var2 = Lists.newArrayList();

            for(int var3 = 0; var3 < this.playerEntity.openContainer.inventorySlots.size(); ++var3) {
               var2.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var3)).getStack());
            }

            this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var2);
         } else {
            ItemStack var6 = this.playerEntity.openContainer.slotClick(var1.getSlotId(), var1.getUsedButton(), var1.getMode(), this.playerEntity);
            if (ItemStack.areItemStacksEqual(var1.getClickedItem(), var6)) {
               this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(var1.getWindowId(), var1.getActionNumber(), true));
               this.playerEntity.isChangingQuantityOnly = true;
               this.playerEntity.openContainer.detectAndSendChanges();
               this.playerEntity.updateHeldItem();
               this.playerEntity.isChangingQuantityOnly = false;
            } else {
               this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, var1.getActionNumber());
               this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(var1.getWindowId(), var1.getActionNumber(), false));
               this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
               ArrayList var7 = Lists.newArrayList();

               for(int var4 = 0; var4 < this.playerEntity.openContainer.inventorySlots.size(); ++var4) {
                  var7.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var4)).getStack());
               }

               this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var7);
            }
         }
      }

   }

   public void handleResourcePackStatus(C19PacketResourcePackStatus var1) {
   }

   public void processPlayerDigging(C07PacketPlayerDigging var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
      BlockPos var3 = var1.getPosition();
      this.playerEntity.markPlayerActive();
      switch($SWITCH_TABLE$net$minecraft$network$play$client$C07PacketPlayerDigging$Action()[var1.getStatus().ordinal()]) {
      case 1:
      case 2:
      case 3:
         double var4 = this.playerEntity.posX - ((double)var3.getX() + 0.5D);
         double var6 = this.playerEntity.posY - ((double)var3.getY() + 0.5D) + 1.5D;
         double var8 = this.playerEntity.posZ - ((double)var3.getZ() + 0.5D);
         double var10 = var4 * var4 + var6 * var6 + var8 * var8;
         if (var10 > 36.0D) {
            return;
         } else if (var3.getY() >= this.serverController.getBuildLimit()) {
            return;
         } else {
            if (var1.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
               if (!this.serverController.isBlockProtected(var2, var3, this.playerEntity) && var2.getWorldBorder().contains(var3)) {
                  this.playerEntity.theItemInWorldManager.onBlockClicked(var3, var1.getFacing());
               } else {
                  this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
               }
            } else {
               if (var1.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                  this.playerEntity.theItemInWorldManager.blockRemoving(var3);
               } else if (var1.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                  this.playerEntity.theItemInWorldManager.cancelDestroyingBlock();
               }

               if (var2.getBlockState(var3).getBlock().getMaterial() != Material.air) {
                  this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var2, var3));
               }
            }

            return;
         }
      case 4:
         if (!this.playerEntity.isSpectator()) {
            this.playerEntity.dropOneItem(true);
         }

         return;
      case 5:
         if (!this.playerEntity.isSpectator()) {
            this.playerEntity.dropOneItem(false);
         }

         return;
      case 6:
         this.playerEntity.stopUsingItem();
         return;
      default:
         throw new IllegalArgumentException("Invalid player action");
      }
   }

   public void processUpdateSign(C12PacketUpdateSign var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
      BlockPos var3 = var1.getPosition();
      if (var2.isBlockLoaded(var3)) {
         TileEntity var4 = var2.getTileEntity(var3);
         if (!(var4 instanceof TileEntitySign)) {
            return;
         }

         TileEntitySign var5 = (TileEntitySign)var4;
         if (!var5.getIsEditable() || var5.getPlayer() != this.playerEntity) {
            this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
            return;
         }

         IChatComponent[] var6 = var1.getLines();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var5.signText[var7] = new ChatComponentText(EnumChatFormatting.getTextWithoutFormattingCodes(var6[var7].getUnformattedText()));
         }

         var5.markDirty();
         var2.markBlockForUpdate(var3);
      }

   }

   public void processChatMessage(C01PacketChatMessage var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
         ChatComponentTranslation var2 = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
         var2.getChatStyle().setColor(EnumChatFormatting.RED);
         this.sendPacket(new S02PacketChat(var2));
      } else {
         this.playerEntity.markPlayerActive();
         String var4 = var1.getMessage();
         var4 = StringUtils.normalizeSpace(var4);

         for(int var3 = 0; var3 < var4.length(); ++var3) {
            if (!ChatAllowedCharacters.isAllowedCharacter(var4.charAt(var3))) {
               this.kickPlayerFromServer("Illegal characters in chat");
               return;
            }
         }

         if (var4.startsWith("/")) {
            this.handleSlashCommand(var4);
         } else {
            ChatComponentTranslation var5 = new ChatComponentTranslation("chat.type.text", new Object[]{this.playerEntity.getDisplayName(), var4});
            this.serverController.getConfigurationManager().sendChatMsgImpl(var5, false);
         }

         this.chatSpamThresholdCount += 20;
         if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())) {
            this.kickPlayerFromServer("disconnect.spam");
         }
      }

   }

   public void processInput(C0CPacketInput var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.setEntityActionState(var1.getStrafeSpeed(), var1.getForwardSpeed(), var1.isJumping(), var1.isSneaking());
   }

   public void processClientSettings(C15PacketClientSettings var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.handleClientSettings(var1);
   }

   public void processClientStatus(C16PacketClientStatus var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      this.playerEntity.markPlayerActive();
      C16PacketClientStatus.EnumState var2 = var1.getStatus();
      switch($SWITCH_TABLE$net$minecraft$network$play$client$C16PacketClientStatus$EnumState()[var2.ordinal()]) {
      case 1:
         if (this.playerEntity.playerConqueredTheEnd) {
            this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
         } else if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
            if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
               this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
               this.serverController.deleteWorldAndStopServer();
            } else {
               UserListBansEntry var3 = new UserListBansEntry(this.playerEntity.getGameProfile(), (Date)null, "(You just lost the game)", (Date)null, "Death in Hardcore");
               this.serverController.getConfigurationManager().getBannedPlayers().addEntry(var3);
               this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
            }
         } else {
            if (this.playerEntity.getHealth() > 0.0F) {
               return;
            }

            this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
         }
         break;
      case 2:
         this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
         break;
      case 3:
         this.playerEntity.triggerAchievement(AchievementList.openInventory);
      }

   }

   public void processVanilla250Packet(C17PacketCustomPayload var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      PacketBuffer var13;
      ItemStack var18;
      ItemStack var21;
      if ("MC|BEdit".equals(var1.getChannelName())) {
         var13 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)var1.getBufferData()));

         label110: {
            label109: {
               try {
                  var18 = var13.readItemStackFromBuffer();
                  if (var18 != null) {
                     if (!ItemWritableBook.isNBTValid(var18.getTagCompound())) {
                        throw new IOException("Invalid book tag!");
                     }

                     var21 = this.playerEntity.inventory.getCurrentItem();
                     if (var21 == null) {
                        break label110;
                     }

                     if (var18.getItem() == Items.writable_book && var18.getItem() == var21.getItem()) {
                        var21.setTagInfo("pages", var18.getTagCompound().getTagList("pages", 8));
                     }
                     break label109;
                  }
               } catch (Exception var10) {
                  logger.error((String)"Couldn't handle book info", (Throwable)var10);
                  var13.release();
                  return;
               }

               var13.release();
               return;
            }

            var13.release();
            return;
         }

         var13.release();
      } else if ("MC|BSign".equals(var1.getChannelName())) {
         var13 = new PacketBuffer(Unpooled.wrappedBuffer((ByteBuf)var1.getBufferData()));

         label128: {
            label127: {
               try {
                  var18 = var13.readItemStackFromBuffer();
                  if (var18 != null) {
                     if (!ItemEditableBook.validBookTagContents(var18.getTagCompound())) {
                        throw new IOException("Invalid book tag!");
                     }

                     var21 = this.playerEntity.inventory.getCurrentItem();
                     if (var21 == null) {
                        break label128;
                     }

                     if (var18.getItem() == Items.written_book && var21.getItem() == Items.writable_book) {
                        var21.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                        var21.setTagInfo("title", new NBTTagString(var18.getTagCompound().getString("title")));
                        var21.setTagInfo("pages", var18.getTagCompound().getTagList("pages", 8));
                        var21.setItem(Items.written_book);
                     }
                     break label127;
                  }
               } catch (Exception var11) {
                  logger.error((String)"Couldn't sign book", (Throwable)var11);
                  var13.release();
                  return;
               }

               var13.release();
               return;
            }

            var13.release();
            return;
         }

         var13.release();
      } else {
         if ("MC|TrSel".equals(var1.getChannelName())) {
            try {
               int var2 = var1.getBufferData().readInt();
               Container var3 = this.playerEntity.openContainer;
               if (var3 instanceof ContainerMerchant) {
                  ((ContainerMerchant)var3).setCurrentRecipeIndex(var2);
               }
            } catch (Exception var9) {
               logger.error((String)"Couldn't select trade", (Throwable)var9);
            }
         } else if ("MC|AdvCdm".equals(var1.getChannelName())) {
            if (!this.serverController.isCommandBlockEnabled()) {
               this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
            } else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
               var13 = var1.getBufferData();

               try {
                  byte var15 = var13.readByte();
                  CommandBlockLogic var4 = null;
                  if (var15 == 0) {
                     TileEntity var5 = this.playerEntity.worldObj.getTileEntity(new BlockPos(var13.readInt(), var13.readInt(), var13.readInt()));
                     if (var5 instanceof TileEntityCommandBlock) {
                        var4 = ((TileEntityCommandBlock)var5).getCommandBlockLogic();
                     }
                  } else if (var15 == 1) {
                     Entity var20 = this.playerEntity.worldObj.getEntityByID(var13.readInt());
                     if (var20 instanceof EntityMinecartCommandBlock) {
                        var4 = ((EntityMinecartCommandBlock)var20).getCommandBlockLogic();
                     }
                  }

                  String var22 = var13.readStringFromBuffer(var13.readableBytes());
                  boolean var6 = var13.readBoolean();
                  if (var4 != null) {
                     var4.setCommand(var22);
                     var4.setTrackOutput(var6);
                     if (!var6) {
                        var4.setLastOutput((IChatComponent)null);
                     }

                     var4.updateCommand();
                     this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", new Object[]{var22}));
                  }
               } catch (Exception var12) {
                  logger.error((String)"Couldn't set command block", (Throwable)var12);
                  var13.release();
                  return;
               }

               var13.release();
            } else {
               this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
            }
         } else if ("MC|Beacon".equals(var1.getChannelName())) {
            if (this.playerEntity.openContainer instanceof ContainerBeacon) {
               try {
                  var13 = var1.getBufferData();
                  int var16 = var13.readInt();
                  int var19 = var13.readInt();
                  ContainerBeacon var23 = (ContainerBeacon)this.playerEntity.openContainer;
                  Slot var24 = var23.getSlot(0);
                  if (var24.getHasStack()) {
                     var24.decrStackSize(1);
                     IInventory var7 = var23.func_180611_e();
                     var7.setField(1, var16);
                     var7.setField(2, var19);
                     var7.markDirty();
                  }
               } catch (Exception var8) {
                  logger.error((String)"Couldn't set beacon", (Throwable)var8);
               }
            }
         } else if ("MC|ItemName".equals(var1.getChannelName()) && this.playerEntity.openContainer instanceof ContainerRepair) {
            ContainerRepair var14 = (ContainerRepair)this.playerEntity.openContainer;
            if (var1.getBufferData() != null && var1.getBufferData().readableBytes() >= 1) {
               String var17 = ChatAllowedCharacters.filterAllowedCharacters(var1.getBufferData().readStringFromBuffer(32767));
               if (var17.length() <= 30) {
                  var14.updateItemName(var17);
               }
            } else {
               var14.updateItemName("");
            }
         }

      }
   }

   static int[] $SWITCH_TABLE$net$minecraft$network$play$client$C16PacketClientStatus$EnumState() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$play$client$C16PacketClientStatus$EnumState;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[C16PacketClientStatus.EnumState.values().length];

         try {
            var0[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$network$play$client$C16PacketClientStatus$EnumState = var0;
         return var0;
      }
   }

   private long currentTimeMillis() {
      return System.nanoTime() / 1000000L;
   }

   public void processPlayer(C03PacketPlayer var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      if (var1 != false) {
         this.kickPlayerFromServer("Invalid move packet received");
      } else {
         WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
         this.field_147366_g = true;
         if (!this.playerEntity.playerConqueredTheEnd) {
            double var3 = this.playerEntity.posX;
            double var5 = this.playerEntity.posY;
            double var7 = this.playerEntity.posZ;
            double var9 = 0.0D;
            double var11 = var1.getPositionX() - this.lastPosX;
            double var13 = var1.getPositionY() - this.lastPosY;
            double var15 = var1.getPositionZ() - this.lastPosZ;
            if (var1.isMoving()) {
               var9 = var11 * var11 + var13 * var13 + var15 * var15;
               if (!this.hasMoved && var9 < 0.25D) {
                  this.hasMoved = true;
               }
            }

            if (this.hasMoved) {
               this.field_175090_f = this.networkTickCount;
               double var19;
               double var21;
               double var23;
               if (this.playerEntity.ridingEntity != null) {
                  float var41 = this.playerEntity.rotationYaw;
                  float var18 = this.playerEntity.rotationPitch;
                  this.playerEntity.ridingEntity.updateRiderPosition();
                  var19 = this.playerEntity.posX;
                  var21 = this.playerEntity.posY;
                  var23 = this.playerEntity.posZ;
                  if (var1.getRotating()) {
                     var41 = var1.getYaw();
                     var18 = var1.getPitch();
                  }

                  this.playerEntity.onGround = var1.isOnGround();
                  this.playerEntity.onUpdateEntity();
                  this.playerEntity.setPositionAndRotation(var19, var21, var23, var41, var18);
                  if (this.playerEntity.ridingEntity != null) {
                     this.playerEntity.ridingEntity.updateRiderPosition();
                  }

                  this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                  if (this.playerEntity.ridingEntity != null) {
                     if (var9 > 4.0D) {
                        Entity var42 = this.playerEntity.ridingEntity;
                        this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(var42));
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

               double var17 = this.playerEntity.posY;
               this.lastPosX = this.playerEntity.posX;
               this.lastPosY = this.playerEntity.posY;
               this.lastPosZ = this.playerEntity.posZ;
               var19 = this.playerEntity.posX;
               var21 = this.playerEntity.posY;
               var23 = this.playerEntity.posZ;
               float var25 = this.playerEntity.rotationYaw;
               float var26 = this.playerEntity.rotationPitch;
               if (var1.isMoving() && var1.getPositionY() == -999.0D) {
                  var1.setMoving(false);
               }

               if (var1.isMoving()) {
                  var19 = var1.getPositionX();
                  var21 = var1.getPositionY();
                  var23 = var1.getPositionZ();
                  if (Math.abs(var1.getPositionX()) > 3.0E7D || Math.abs(var1.getPositionZ()) > 3.0E7D) {
                     this.kickPlayerFromServer("Illegal position");
                     return;
                  }
               }

               if (var1.getRotating()) {
                  var25 = var1.getYaw();
                  var26 = var1.getPitch();
               }

               this.playerEntity.onUpdateEntity();
               this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var25, var26);
               if (!this.hasMoved) {
                  return;
               }

               double var27 = var19 - this.playerEntity.posX;
               double var29 = var21 - this.playerEntity.posY;
               double var31 = var23 - this.playerEntity.posZ;
               double var33 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
               double var35 = var27 * var27 + var29 * var29 + var31 * var31;
               if (var35 - var33 > 100.0D && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                  logger.warn(this.playerEntity.getName() + " moved too quickly! " + var27 + "," + var29 + "," + var31 + " (" + var27 + ", " + var29 + ", " + var31 + ")");
                  this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                  return;
               }

               float var37 = 0.0625F;
               boolean var38 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract((double)var37, (double)var37, (double)var37)).isEmpty();
               if (this.playerEntity.onGround && !var1.isOnGround() && var29 > 0.0D) {
                  this.playerEntity.jump();
               }

               this.playerEntity.moveEntity(var27, var29, var31);
               this.playerEntity.onGround = var1.isOnGround();
               var27 = var19 - this.playerEntity.posX;
               var29 = var21 - this.playerEntity.posY;
               if (var29 > -0.5D || var29 < 0.5D) {
                  var29 = 0.0D;
               }

               var31 = var23 - this.playerEntity.posZ;
               var35 = var27 * var27 + var29 * var29 + var31 * var31;
               boolean var39 = false;
               if (var35 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
                  var39 = true;
                  logger.warn(this.playerEntity.getName() + " moved wrongly!");
               }

               this.playerEntity.setPositionAndRotation(var19, var21, var23, var25, var26);
               this.playerEntity.addMovementStat(this.playerEntity.posX - var3, this.playerEntity.posY - var5, this.playerEntity.posZ - var7);
               if (!this.playerEntity.noClip) {
                  boolean var40 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract((double)var37, (double)var37, (double)var37)).isEmpty();
                  if (var38 && (var39 || !var40) && !this.playerEntity.isPlayerSleeping()) {
                     this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, var25, var26);
                     return;
                  }
               }

               AxisAlignedBB var43 = this.playerEntity.getEntityBoundingBox().expand((double)var37, (double)var37, (double)var37).addCoord(0.0D, -0.55D, 0.0D);
               if (!this.serverController.isFlightAllowed() && !this.playerEntity.capabilities.allowFlying && !var2.checkBlockCollision(var43)) {
                  if (var29 >= -0.03125D) {
                     ++this.floatingTickCount;
                     if (this.floatingTickCount > 80) {
                        logger.warn(this.playerEntity.getName() + " was kicked for floating too long!");
                        this.kickPlayerFromServer("Flying is not enabled on this server");
                        return;
                     }
                  }
               } else {
                  this.floatingTickCount = 0;
               }

               this.playerEntity.onGround = var1.isOnGround();
               this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
               this.playerEntity.handleFalling(this.playerEntity.posY - var17, var1.isOnGround());
            } else if (this.networkTickCount - this.field_175090_f > 20) {
               this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            }
         }
      }

   }

   private void handleSlashCommand(String var1) {
      this.serverController.getCommandManager().executeCommand(this.playerEntity, var1);
   }

   public void sendPacket(Packet var1) {
      if (var1 instanceof S02PacketChat) {
         S02PacketChat var2 = (S02PacketChat)var1;
         EntityPlayer.EnumChatVisibility var3 = this.playerEntity.getChatVisibility();
         if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
         }

         if (var3 == EntityPlayer.EnumChatVisibility.SYSTEM && !var2.isChat()) {
            return;
         }
      }

      try {
         this.netManager.sendPacket(var1);
      } catch (Throwable var5) {
         CrashReport var6 = CrashReport.makeCrashReport(var5, "Sending packet");
         CrashReportCategory var4 = var6.makeCategory("Packet being sent");
         var4.addCrashSectionCallable("Packet class", new Callable(this, var1) {
            private final Packet val$packetIn;
            final NetHandlerPlayServer this$0;

            public String call() throws Exception {
               return this.val$packetIn.getClass().getCanonicalName();
            }

            public Object call() throws Exception {
               return this.call();
            }

            {
               this.this$0 = var1;
               this.val$packetIn = var2;
            }
         });
         throw new ReportedException(var6);
      }
   }

   public void kickPlayerFromServer(String var1) {
      ChatComponentText var2 = new ChatComponentText(var1);
      this.netManager.sendPacket(new S40PacketDisconnect(var2), new GenericFutureListener(this, var2) {
         private final ChatComponentText val$chatcomponenttext;
         final NetHandlerPlayServer this$0;

         public void operationComplete(Future var1) throws Exception {
            this.this$0.netManager.closeChannel(this.val$chatcomponenttext);
         }

         {
            this.this$0 = var1;
            this.val$chatcomponenttext = var2;
         }
      });
      this.netManager.disableAutoRead();
      Futures.getUnchecked(this.serverController.addScheduledTask(new Runnable(this) {
         final NetHandlerPlayServer this$0;

         public void run() {
            this.this$0.netManager.checkDisconnected();
         }

         {
            this.this$0 = var1;
         }
      }));
   }

   public void processTabComplete(C14PacketTabComplete var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = this.serverController.getTabCompletions(this.playerEntity, var1.getMessage(), var1.getTargetBlock()).iterator();

      while(var4.hasNext()) {
         String var3 = (String)var4.next();
         var2.add(var3);
      }

      this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete((String[])var2.toArray(new String[var2.size()])));
   }

   public void processKeepAlive(C00PacketKeepAlive var1) {
      if (var1.getKey() == this.field_147378_h) {
         int var2 = (int)(this.currentTimeMillis() - this.lastPingTime);
         this.playerEntity.ping = (this.playerEntity.ping * 3 + var2) / 4;
      }

   }

   public void setPlayerLocation(double var1, double var3, double var5, float var7, float var8) {
      this.setPlayerLocation(var1, var3, var5, var7, var8, Collections.emptySet());
   }

   public void processHeldItemChange(C09PacketHeldItemChange var1) {
      PacketThreadUtil.checkThreadAndEnqueue(var1, this, this.playerEntity.getServerForPlayer());
      if (var1.getSlotId() >= 0 && var1.getSlotId() < InventoryPlayer.getHotbarSize()) {
         this.playerEntity.inventory.currentItem = var1.getSlotId();
         this.playerEntity.markPlayerActive();
      } else {
         logger.warn(this.playerEntity.getName() + " tried to set an invalid carried item");
      }

   }
}
