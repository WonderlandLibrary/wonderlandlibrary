package de.violence.module.modules.WORLD;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.ui.Category;
import de.violence.ui.BlickWinkel3D;
import de.violence.ui.Location3D;
import de.violence.utils.RotationHandler;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Scaffold extends Module {
   public VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"AAC", "AAC Fast", "Sneak"}), "AAC");
   private VSetting bSilent = new VSetting("Silent", this, false);
   private VSetting bPushback = new VSetting("Push back", this, false, Arrays.asList(new String[]{"Push back Speed-Scaffold"}));
   private VSetting sPushback = new VSetting("Push back Speed", this, 0.1D, 0.5D, 0.1D, false);
   private float lastYaw;
   private BlockPos lastPos;
   int look = 0;
   boolean wasSprinting = false;
   boolean sneakWasSetted = false;
   int lastRot = 0;

   public Scaffold() {
      super("Scaffold", Category.WORLD);
   }

   public void checkSprint() {
      this.mc.thePlayer.setSprinting(false);
      this.mc.gameSettings.keyBindSprint.pressed = false;
      this.wasSprinting = false;
   }

   private int findBlocks() {
      for(int i = 0; i < 9; ++i) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + i).getHasStack() && this.mc.thePlayer.inventoryContainer.getSlot(36 + i).getStack().getItem() instanceof ItemBlock) {
            return 36 + i;
         }
      }

      return -1;
   }

   public void onEnable() {
      this.lastYaw = this.mc.thePlayer.rotationYaw;
      super.onEnable();
   }

   public void onDisable() {
      this.checkSprint();
      this.lastPos = null;
      if(this.bSilent.isToggled()) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
      }

      if(this.sneakWasSetted) {
         this.sneakWasSetted = false;
         this.mc.gameSettings.keyBindSneak.pressed = false;
      }

      super.onDisable();
   }

   private boolean isMode(String mode) {
      return this.mMode.getActiveMode().toLowerCase().replace(".", "").equalsIgnoreCase(mode.toLowerCase().replace(".", ""));
   }

   public void onFrameRender() {
      double var10000 = this.mc.thePlayer.posX;
      Minecraft.getMinecraft().getRenderManager();
      double x = var10000 - RenderManager.renderPosX;
      var10000 = this.mc.thePlayer.posY - 0.5D;
      Minecraft.getMinecraft().getRenderManager();
      double y = var10000 - RenderManager.renderPosY;
      var10000 = this.mc.thePlayer.posZ;
      Minecraft.getMinecraft().getRenderManager();
      double z = var10000 - RenderManager.renderPosZ;
      GL11.glEnable(3042);
      GL11.glLineWidth(1.0F);
      GL11.glColor4f(0.2F, 0.3F, 1.0F, 1.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      RenderGlobal.func_181561_a(new AxisAlignedBB(x - 1.0D, y - 0.5D, z - 1.0D, x + 1.0D, y + 0.5D, z + 1.0D));
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      super.onFrameRender();
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      super.onUpdate();
      if(Killaura.shouldAction && this.bSilent.isToggled()) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
      }

      if(Math.abs(this.mc.thePlayer.rotationYaw - this.lastYaw) > 15.0F) {
         this.lastYaw = this.mc.thePlayer.rotationYaw;
         this.mc.thePlayer.motionX = 0.0D;
         this.mc.thePlayer.motionZ = 0.0D;
      } else {
         ItemStack itemStack = this.mc.thePlayer.getCurrentEquippedItem();
         if(this.bSilent.isToggled() && this.findBlocks() != -1) {
            itemStack = this.mc.thePlayer.inventoryContainer.getSlot(this.findBlocks()).getStack();
         }

         try {
            if(itemStack == null) {
               return;
            }

            if(!(itemStack.getItem() instanceof ItemBlock)) {
               return;
            }

            BlockPos place = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.5D, this.mc.thePlayer.posZ);
            BlockPos at = this.at(place);
            this.checkSprint();
            int faceing = this.lastRot;
            boolean shouldLook = this.shouldLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
            float nextYaw = this.mc.thePlayer.rotationYaw;
            if(this.sneakWasSetted) {
               this.sneakWasSetted = false;
               this.mc.gameSettings.keyBindSneak.pressed = false;
            }

            this.mc.thePlayer.setSprinting(false);
            if(this.isMode("aac") || this.isMode("aac fast") || this.isMode("sneak")) {
               this.mc.gameSettings.keyBindSprint.pressed = false;
               this.mc.thePlayer.setSprinting(false);
               this.wasSprinting = true;
            }

            Location3D end = null;
            Location3D start = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
            if(faceing == 4) {
               start = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, (double)at.getZ() + 0.5D);
               end = new Location3D((double)at.getX(), (double)at.getY() + 0.5D, (double)at.getZ() + 0.5D);
            } else if(faceing == 5) {
               start = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, (double)at.getZ() + 0.5D);
               end = new Location3D((double)(at.getX() + 1), (double)at.getY() + 0.5D, (double)at.getZ() + 0.5D);
            } else if(faceing == 2) {
               start = new Location3D((double)at.getX() + 0.5D, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
               end = new Location3D((double)at.getX() + 0.5D, (double)at.getY() + 0.5D, (double)at.getZ());
            } else if(faceing == 3) {
               start = new Location3D((double)at.getX() + 0.5D, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
               end = new Location3D((double)at.getX() + 0.5D, (double)at.getY() + 0.5D, (double)(at.getZ() + 1));
            }

            BlickWinkel3D v = new BlickWinkel3D(start, end);
            RotationHandler.server_pitch = (float)(81.3D + (Math.random() < 0.1D?Math.random() / 5.0D:0.0D));
            RotationHandler.server_yaw = (float)v.getYaw();
            nextYaw = (float)v.getYaw();
            this.look = 5;
            if(this.isMode("aac fast")) {
               this.mc.thePlayer.motionX *= 1.0099999904632568D;
               this.mc.thePlayer.motionZ *= 1.0099999904632568D;
            } else if(this.isMode("aac")) {
               this.mc.thePlayer.motionX /= 1.15D;
               this.mc.thePlayer.motionZ /= 1.15D;
            }

            if(this.look > 0) {
               RotationHandler.server_pitch = 81.4F;
               RotationHandler.server_yaw = nextYaw;
               --this.look;
            }

            if(this.shouldPlace(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ)) {
               new Vec3((double)place.getX(), (double)place.getY(), (double)place.getZ());
               if(this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.motionY = Math.random() / 25.0D;
               }

               this.portMove(this.mc.thePlayer.rotationYaw, 0.1F, 0.0F);
               if(this.bSilent.isToggled() && this.findBlocks() != -1) {
                  this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.findBlocks() - 36));
               }

               this.mc.thePlayer.swingItem();
               IBlockState ibl = ((ItemBlock)itemStack.getItem()).getBlock().getDefaultState();
               this.mc.theWorld.setBlockState(place, ibl);
               C08PacketPlayerBlockPlacement bl = new C08PacketPlayerBlockPlacement(at, faceing, itemStack, 0.0F, 0.0F, 0.0F);
               if(this.bPushback.isToggled()) {
                  this.move(this.mc.thePlayer.rotationYaw, (float)(-this.sPushback.getCurrent()));
               }

               this.mc.thePlayer.sendQueue.addToSendQueue(bl);
               if(this.isMode("sneak")) {
                  this.sneakWasSetted = true;
                  this.mc.gameSettings.keyBindSneak.pressed = true;
               }
            }
         } catch (Exception var12) {
            ;
         }

         this.lastYaw = this.mc.thePlayer.rotationYaw;
         this.lastPos = this.mc.thePlayer.getPosition();
      }
   }

   public BlockPos at(BlockPos want) {
      BlockPos o1 = new BlockPos(want.getX() + 1, want.getY(), want.getZ());
      BlockPos o2 = new BlockPos(want.getX() - 1, want.getY(), want.getZ());
      BlockPos o3 = new BlockPos(want.getX(), want.getY(), want.getZ() + 1);
      BlockPos o4 = new BlockPos(want.getX(), want.getY(), want.getZ() - 1);
      if(this.mc.theWorld.getBlockState(o1).getBlock() != Blocks.air) {
         this.lastRot = 4;
         return o1;
      } else if(this.mc.theWorld.getBlockState(o2).getBlock() != Blocks.air) {
         this.lastRot = 5;
         return o2;
      } else if(this.mc.theWorld.getBlockState(o3).getBlock() != Blocks.air) {
         this.lastRot = 2;
         return o3;
      } else if(this.mc.theWorld.getBlockState(o4).getBlock() != Blocks.air) {
         this.lastRot = 3;
         return o4;
      } else {
         return null;
      }
   }

   public boolean shouldPlace(double x, double y, double z) {
      BlockPos p1 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
      BlockPos p2 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
      BlockPos p3 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
      BlockPos p4 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
      return this.mc.thePlayer.worldObj.getBlockState(p1).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p2).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p3).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p4).getBlock() == Blocks.air;
   }

   public boolean shouldLook(double x, double y, double z) {
      BlockPos p1 = new BlockPos(x - 0.029999999329447746D, y - 0.5D, z - 0.029999999329447746D);
      BlockPos p2 = new BlockPos(x - 0.029999999329447746D, y - 0.5D, z + 0.029999999329447746D);
      BlockPos p3 = new BlockPos(x + 0.029999999329447746D, y - 0.5D, z + 0.029999999329447746D);
      BlockPos p4 = new BlockPos(x + 0.029999999329447746D, y - 0.5D, z - 0.029999999329447746D);
      return this.mc.thePlayer.worldObj.getBlockState(p1).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p2).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p3).getBlock() == Blocks.air && this.mc.thePlayer.worldObj.getBlockState(p4).getBlock() == Blocks.air;
   }
}
