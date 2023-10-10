package net.minecraft.client.entity;

import my.NewSnake.event.events.InsideBlockRenderEvent;
import my.NewSnake.event.events.ItemSlowEvent;
import my.NewSnake.event.events.MoveEvent;
import my.NewSnake.event.events.PushOutOfBlocksEvent;
import my.NewSnake.event.events.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP extends AbstractClientPlayer {
   public float prevRenderArmPitch;
   public int sprintingTicksLeft;
   public MovementInput movementInput;
   public double lastReportedPosY;
   public float prevRenderArmYaw;
   public float renderYawHead;
   private double lastReportedPosZ;
   private boolean serverSneakState;
   public float renderArmPitch;
   private float lastReportedPitch;
   private final StatFileWriter statWriter;
   private float horseJumpPower;
   public float prevTimeInPortal;
   private int horseJumpPowerCounter;
   public float rotationPitchHead;
   private int positionUpdateTicks;
   private double lastReportedPosX;
   public float renderPitchHead;
   private boolean hasValidHealth;
   private float lastReportedYaw;
   public float timeInPortal;
   private String clientBrand;
   public final NetHandlerPlayClient sendQueue;
   public float renderArmYaw;
   private boolean serverSprintState;
   protected int sprintToggleTimer;
   protected Minecraft mc;

   public EntityItem dropOneItem(boolean var1) {
      C07PacketPlayerDigging.Action var2 = var1 ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
      this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
      return null;
   }

   public void closeScreenAndDropStack() {
      this.inventory.setItemStack((ItemStack)null);
      super.closeScreen();
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   public boolean isEntityInsideOpaqueBlock() {
      InsideBlockRenderEvent var1 = new InsideBlockRenderEvent();
      var1.call();
      return var1.isCancelled() ? false : super.isEntityInsideOpaqueBlock();
   }

   public void onLivingUpdate() {
      if (this.sprintingTicksLeft > 0) {
         --this.sprintingTicksLeft;
         if (this.sprintingTicksLeft == 0) {
            this.setSprinting(false);
         }
      }

      if (this.sprintToggleTimer > 0) {
         --this.sprintToggleTimer;
      }

      this.prevTimeInPortal = this.timeInPortal;
      if (this.inPortal) {
         if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
            this.mc.displayGuiScreen((GuiScreen)null);
         }

         if (this.timeInPortal == 0.0F) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
         }

         this.timeInPortal += 0.0125F;
         if (this.timeInPortal >= 1.0F) {
            this.timeInPortal = 1.0F;
         }

         this.inPortal = false;
      } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
         this.timeInPortal += 0.006666667F;
         if (this.timeInPortal > 1.0F) {
            this.timeInPortal = 1.0F;
         }
      } else {
         if (this.timeInPortal > 0.0F) {
            this.timeInPortal -= 0.05F;
         }

         if (this.timeInPortal < 0.0F) {
            this.timeInPortal = 0.0F;
         }
      }

      if (this.timeUntilPortal > 0) {
         --this.timeUntilPortal;
      }

      boolean var1 = this.movementInput.jump;
      boolean var2 = this.movementInput.sneak;
      float var3 = 0.8F;
      boolean var4 = this.movementInput.moveForward >= var3;
      this.movementInput.updatePlayerMoveState();
      ItemSlowEvent var5 = new ItemSlowEvent();
      var5.call();
      if (!var5.isCancelled() && this.isUsingItem() && !this.isRiding()) {
         MovementInput var10000 = this.movementInput;
         var10000.moveStrafe *= 0.2F;
         var10000 = this.movementInput;
         var10000.moveForward *= 0.2F;
         this.sprintToggleTimer = 0;
      }

      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      boolean var6 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
      if (this.onGround && !var2 && !var4 && this.movementInput.moveForward >= var3 && !this.isSprinting() && var6 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
         if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.sprintToggleTimer = 7;
         } else {
            this.setSprinting(true);
         }
      }

      if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var6 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
         this.setSprinting(true);
      }

      if (this.isSprinting() && (this.movementInput.moveForward < var3 || this.isCollidedHorizontally || !var6)) {
         this.setSprinting(false);
      }

      if (this.capabilities.allowFlying) {
         if (Minecraft.playerController.isSpectatorMode()) {
            if (!this.capabilities.isFlying) {
               this.capabilities.isFlying = true;
               this.sendPlayerAbilities();
            }
         } else if (!var1 && this.movementInput.jump) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.sendPlayerAbilities();
               this.flyToggleTimer = 0;
            }
         }
      }

      if (this.capabilities.isFlying && this != false) {
         if (this.movementInput.sneak) {
            this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
         }

         if (this.movementInput.jump) {
            this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
         }
      }

      if (this != false) {
         if (this.horseJumpPowerCounter < 0) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter == 0) {
               this.horseJumpPower = 0.0F;
            }
         }

         if (var1 && !this.movementInput.jump) {
            this.horseJumpPowerCounter = -10;
            this.sendHorseJump();
         } else if (!var1 && this.movementInput.jump) {
            this.horseJumpPowerCounter = 0;
            this.horseJumpPower = 0.0F;
         } else if (var1) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter < 10) {
               this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
            } else {
               this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
            }
         }
      } else {
         this.horseJumpPower = 0.0F;
      }

      super.onLivingUpdate();
      if (this.onGround && this.capabilities.isFlying && !Minecraft.playerController.isSpectatorMode()) {
         this.capabilities.isFlying = false;
         this.sendPlayerAbilities();
      }

   }

   public void setXPStats(float var1, int var2, int var3) {
      this.experience = var1;
      this.experienceTotal = var2;
      this.experienceLevel = var3;
   }

   public boolean isUser() {
      return true;
   }

   public float getDirection() {
      float var1 = this.rotationYaw;
      if (this.moveForward < 0.0F) {
         var1 += 180.0F;
      }

      float var2 = 1.0F;
      if (this.moveForward < 0.0F) {
         var2 = -0.5F;
      } else if (this.moveForward > 0.0F) {
         var2 = 0.5F;
      }

      if (this.moveStrafing > 0.0F) {
         var1 -= 90.0F * var2;
      } else if (this.moveStrafing < 0.0F) {
         var1 += 90.0F * var2;
      }

      var1 *= 0.017453292F;
      return var1;
   }

   public void setPlayerSPHealth(float var1) {
      if (this.hasValidHealth) {
         float var2 = this.getHealth() - var1;
         if (var2 <= 0.0F) {
            this.setHealth(var1);
            if (var2 < 0.0F) {
               this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
         } else {
            this.lastDamage = var2;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.generic, var2);
            this.hurtTime = this.maxHurtTime = 10;
         }
      } else {
         this.setHealth(var1);
         this.hasValidHealth = true;
      }

   }

   public void swingItem() {
      super.swingItem();
      this.sendQueue.addToSendQueue(new C0APacketAnimation());
   }

   public void openEditSign(TileEntitySign var1) {
      this.mc.displayGuiScreen(new GuiEditSign(var1));
   }

   public void addChatMessage(IChatComponent var1) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(var1);
   }

   public void displayGUIBook(ItemStack var1) {
      Item var2 = var1.getItem();
      if (var2 == Items.writable_book) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, var1, true));
      }

   }

   public void displayVillagerTradeGui(IMerchant var1) {
      this.mc.displayGuiScreen(new GuiMerchant(this.inventory, var1, this.worldObj));
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   public void sendChatMessage(String var1) {
      this.sendQueue.addToSendQueue(new C01PacketChatMessage(var1));
   }

   protected void joinEntityItemWithWorld(EntityItem var1) {
   }

   public StatFileWriter getStatFileWriter() {
      return this.statWriter;
   }

   public final boolean isMoving() {
      return Minecraft.thePlayer.moveForward != 0.0F || Minecraft.thePlayer.moveStrafing != 0.0F;
   }

   public float getHorseJumpPower() {
      return this.horseJumpPower;
   }

   public void sendHorseInventory() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
   }

   public void setSprinting(boolean var1) {
      super.setSprinting(var1);
      this.sprintingTicksLeft = var1 ? 600 : 0;
   }

   public void addStat(StatBase var1, int var2) {
      if (var1 != null && var1.isIndependent) {
         super.addStat(var1, var2);
      }

   }

   protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
      PushOutOfBlocksEvent var7 = new PushOutOfBlocksEvent();
      var7.call();
      if (!this.noClip && !var7.isCancelled()) {
         BlockPos var8 = new BlockPos(var1, var3, var5);
         double var9 = var1 - (double)var8.getX();
         double var11 = var5 - (double)var8.getZ();
         if (var8 != false) {
            byte var13 = -1;
            double var14 = 9999.0D;
            if (var8.west() != false && var9 < var14) {
               var14 = var9;
               var13 = 0;
            }

            if (var8.east() != false && 1.0D - var9 < var14) {
               var14 = 1.0D - var9;
               var13 = 1;
            }

            if (var8.north() != false && var11 < var14) {
               var14 = var11;
               var13 = 4;
            }

            if (var8.south() != false && 1.0D - var11 < var14) {
               var14 = 1.0D - var11;
               var13 = 5;
            }

            float var16 = 0.1F;
            if (var13 == 0) {
               this.motionX = (double)(-var16);
            }

            if (var13 == 1) {
               this.motionX = (double)var16;
            }

            if (var13 == 4) {
               this.motionZ = (double)(-var16);
            }

            if (var13 == 5) {
               this.motionZ = (double)var16;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void onCriticalHit(Entity var1) {
      this.mc.effectRenderer.emitParticleAtEntity(var1, EnumParticleTypes.CRIT);
   }

   public void mountEntity(Entity var1) {
      super.mountEntity(var1);
      if (var1 instanceof EntityMinecart) {
         this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)var1));
      }

   }

   public void heal(float var1) {
   }

   public void displayGui(IInteractionObject var1) {
      String var2 = var1.getGuiID();
      if ("minecraft:crafting_table".equals(var2)) {
         this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
      } else if ("minecraft:enchanting_table".equals(var2)) {
         this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, var1));
      } else if ("minecraft:anvil".equals(var2)) {
         this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
      }

   }

   public void closeScreen() {
      this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
      this.closeScreenAndDropStack();
   }

   public void onUpdateWalkingPlayer() {
      UpdateEvent var1 = new UpdateEvent(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
      var1.call();
      if (var1.isCancelled()) {
         UpdateEvent var25 = new UpdateEvent(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
         var25.call();
      } else {
         boolean var2 = this.isSprinting();
         if (var2 != this.serverSprintState) {
            if (var2) {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = var2;
         }

         boolean var3 = this.isSneaking();
         if (var3 != this.serverSneakState) {
            if (var3) {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = var3;
         }

         if (this != false) {
            double var4 = var1.getX();
            double var6 = var1.getY();
            double var8 = var1.getZ();
            float var10 = var1.getYaw();
            float var11 = var1.getPitch();
            boolean var12 = var1.isOnground();
            double var13 = var4 - this.lastReportedPosX;
            double var15 = var6 - this.lastReportedPosY;
            double var17 = var8 - this.lastReportedPosZ;
            double var19 = (double)(var10 - this.lastReportedYaw);
            double var21 = (double)(var11 - this.lastReportedPitch);
            boolean var23 = var13 * var13 + var15 * var15 + var17 * var17 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean var24 = var19 != 0.0D || var21 != 0.0D;
            if (var23 && var24) {
               this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(var4, var6, var8, var10, var11, var12));
            } else if (var23) {
               this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(var4, var6, var8, var12));
            } else if (var24) {
               this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(var10, var11, var12));
            } else {
               this.sendQueue.addToSendQueue(new C03PacketPlayer(var12));
            }

            ++this.positionUpdateTicks;
            if (var23) {
               this.lastReportedPosX = var4;
               this.lastReportedPosY = var6;
               this.lastReportedPosZ = var8;
               this.positionUpdateTicks = 0;
            }

            if (var24) {
               this.lastReportedYaw = var10;
               this.lastReportedPitch = var11;
            }
         }

         (new UpdateEvent()).call();
      }
   }

   public void displayGUIChest(IInventory var1) {
      String var2 = var1 instanceof IInteractionObject ? ((IInteractionObject)var1).getGuiID() : "minecraft:container";
      if ("minecraft:chest".equals(var2)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
      } else if ("minecraft:hopper".equals(var2)) {
         this.mc.displayGuiScreen(new GuiHopper(this.inventory, var1));
      } else if ("minecraft:furnace".equals(var2)) {
         this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
      } else if ("minecraft:brewing_stand".equals(var2)) {
         this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, var1));
      } else if ("minecraft:beacon".equals(var2)) {
         this.mc.displayGuiScreen(new GuiBeacon(this.inventory, var1));
      } else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
      } else {
         this.mc.displayGuiScreen(new GuiDispenser(this.inventory, var1));
      }

   }

   protected void damageEntity(DamageSource var1, float var2) {
      if (!this.isEntityInvulnerable(var1)) {
         this.setHealth(this.getHealth() - var2);
      }

   }

   public void sendPlayerAbilities() {
      this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
   }

   public EntityPlayerSP(Minecraft var1, World var2, NetHandlerPlayClient var3, StatFileWriter var4) {
      super(var2, var3.getGameProfile());
      this.sendQueue = var3;
      this.statWriter = var4;
      this.mc = var1;
      this.dimension = 0;
   }

   public boolean isServerWorld() {
      return true;
   }

   public void playSound(String var1, float var2, float var3) {
      this.worldObj.playSound(this.posX, this.posY, this.posZ, var1, var2, var3, false);
   }

   public void setClientBrand(String var1) {
      this.clientBrand = var1;
   }

   public boolean isSneaking() {
      boolean var1 = this.movementInput != null ? this.movementInput.sneak : false;
      return var1 && !this.sleeping;
   }

   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
   }

   public void updateEntityActionState() {
      // $FF: Couldn't be decompiled
   }

   public void displayGUIHorse(EntityHorse var1, IInventory var2) {
      this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, var2, var1));
   }

   public void addChatComponentMessage(IChatComponent var1) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(var1);
   }

   public void openEditCommandBlock(CommandBlockLogic var1) {
      this.mc.displayGuiScreen(new GuiCommandBlock(var1));
   }

   protected void sendHorseJump() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
   }

   public void onEnchantmentCritical(Entity var1) {
      this.mc.effectRenderer.emitParticleAtEntity(var1, EnumParticleTypes.CRIT_MAGIC);
   }

   public boolean canCommandSenderUseCommand(int var1, String var2) {
      return var1 <= 0;
   }

   public void moveEntity(double var1, double var3, double var5) {
      MoveEvent var7 = new MoveEvent(var1, var3, var5);
      var7.call();
      super.moveEntity(var7.getX(), var7.getY(), var7.getZ());
   }

   public void respawnPlayer() {
      this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
   }

   public void onUpdate() {
      if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
         super.onUpdate();
         if (this.isRiding()) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
            this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
         } else {
            this.onUpdateWalkingPlayer();
         }
      }

   }

   public String getClientBrand() {
      return this.clientBrand;
   }
}
