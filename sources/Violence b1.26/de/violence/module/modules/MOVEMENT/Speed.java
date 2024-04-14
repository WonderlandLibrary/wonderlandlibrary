package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Line3D;
import de.violence.ui.Location3D;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Speed extends Module {
   public VSetting mode = new VSetting("Mode", this, Arrays.asList(new String[]{"Custom", "Fast", "Teleport", "YPort", "BAC", "Spartan Bhop", "Spartan YPort", "AAC 3.3.0 Fast", "AAC 3.3.2 YPort", "AAC 3.3.2 YPort Fast", "AAC 3.3.5 Lowhop", "NCP Bhop"}), "Custom", Arrays.asList(new String[]{"Timer-Speed-Custom", "Ground Multiplier-Speed-Custom", "Air Up Multiplier-Speed-Custom", "Air Down Multiplier-Speed-Custom", "Hop Speed Multiplier-Speed-Custom", "Hop Y Motion-Speed-Custom", "Multiply-Speed-Custom"}));
   public VSetting TimerSlider = new VSetting("Timer", this, 0.5D, 2.0D, 1.0D, false);
   int delay;
   double startY;
   boolean timerWasSettedByMe = false;
   public VSetting groundSpeedM = new VSetting("Ground Multiplier", this, 0.0D, 2.0D, 1.0D, false);
   public VSetting airUpSpeedM = new VSetting("Air Up Multiplier", this, 0.0D, 2.0D, 1.0D, false);
   public VSetting airDownSpeedM = new VSetting("Air Down Multiplier", this, 0.0D, 2.0D, 1.0D, false);
   public VSetting hopSpeedM = new VSetting("Hop Speed Multiplier", this, 0.0D, 3.0D, 1.0D, false);
   public VSetting hopYMotionM = new VSetting("Hop Y Motion", this, 0.0D, 0.42D, 0.0D, false);
   public VSetting multiply = new VSetting("Multiply", this, false);

   public Speed() {
      super("Speed", Category.MOVEMENT);
   }

   public void onDisable() {
      this.mc.thePlayer.capabilities.setPlayerWalkSpeed(0.1F);
      this.mc.timer.timerSpeed = 1.0F;
      this.mc.thePlayer.speedInAir = 0.02F;
      super.onDisable();
   }

   private boolean isMode(String mode) {
      return this.mode.getActiveMode().toLowerCase().replace(".", "").equalsIgnoreCase(mode.toLowerCase().replace(".", ""));
   }

   public void onWalkUpdate() {
      if(!this.isMode("custom") && !this.isMode("aac 3.3.8 strafe speed")) {
         this.mc.timer.timerSpeed = 1.0F;
      }

      if(this.isMode("spartan yport")) {
         BlockPos under = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
         if(this.mc.theWorld.getBlockState(under).getBlock() == Blocks.air) {
            this.mc.thePlayer.posY = (double)((int)this.mc.thePlayer.posY) - 0.8D;
         } else {
            this.mc.thePlayer.posY = (double)((int)this.mc.thePlayer.posY);
         }
      }

      super.onWalkUpdate();
   }

   public void onUpdate() {
      this.nameAddon = this.mode.getActiveMode();
      if(this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F) {
         if(this.timerWasSettedByMe) {
            this.timerWasSettedByMe = false;
            this.mc.timer.timerSpeed = 1.0F;
         }

         if(this.isMode("custom")) {
            if(this.TimerSlider.getCurrent() != 1.0D) {
               this.timerWasSettedByMe = true;
               this.mc.timer.timerSpeed = (float)this.TimerSlider.getCurrent();
            }

            this.costum();
         } else {
            if(this.TimerSlider.getCurrent() != 1.0D) {
               this.timerWasSettedByMe = true;
               this.mc.timer.timerSpeed = (float)this.TimerSlider.getCurrent();
            }

            if(this.isMode("teleport")) {
               this.onTeleport();
            } else if(this.isMode("normal")) {
               this.mc.thePlayer.capabilities.setPlayerWalkSpeed(0.08F);
               this.mc.thePlayer.motionX *= 1.0299999713897705D;
               this.mc.thePlayer.motionZ *= 1.0299999713897705D;
               if(this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.motionX *= 1.0399999618530273D;
                  this.mc.thePlayer.motionZ *= 1.0399999618530273D;
               }
            } else if(this.isMode("fast")) {
               if(this.mc.thePlayer.motionY > 0.0D) {
                  this.mc.thePlayer.motionX *= 1.2000000476837158D;
                  this.mc.thePlayer.motionZ *= 1.2000000476837158D;
               }
            } else if(this.isMode("yport")) {
               if(this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.jump();
                  this.mc.thePlayer.motionY = 0.0D;
                  this.portMove(this.mc.thePlayer.rotationYaw, 0.0F, 0.42F);
               }
            } else if(this.isMode("bac")) {
               BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.1D, this.mc.thePlayer.posZ);
               if(this.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
                  this.mc.timer.timerSpeed = 1.0F;
                  return;
               }

               if(this.mc.thePlayer.onGround) {
                  this.mc.timer.timerSpeed = 1.05F;
                  this.mc.thePlayer.motionY = 0.09000000357627869D;
                  this.move(this.mc.thePlayer.rotationYaw, 0.432F);
               } else if(this.mc.thePlayer.motionY < -0.07D) {
                  this.move(this.mc.thePlayer.rotationYaw, 0.432F);
               } else {
                  this.move(this.mc.thePlayer.rotationYaw, 0.333F);
               }
            } else if(this.isMode("aac 335 lowhop")) {
               this.onAAC317();
            } else if(this.isMode("spartan bhop")) {
               if(this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.jump();
                  this.move(this.mc.thePlayer.rotationYaw + (float)(this.mc.gameSettings.keyBindRight.isKeyDown()?30:(this.mc.gameSettings.keyBindLeft.isKeyDown()?-30:0)), 0.494F);
               }
            } else if(this.isMode("aac 330 fast")) {
               if(this.mc.gameSettings.keyBindForward.pressed) {
                  if(this.mc.thePlayer.onGround) {
                     this.mc.thePlayer.jump();
                  } else if(this.mc.thePlayer.motionY > 0.0D) {
                     this.move(this.mc.thePlayer.rotationYaw - 180.0F, 0.2F);
                  }
               }
            } else if(this.isMode("ncp bhop")) {
               this.doNCP();
            } else if(this.isMode("aac 332 fast")) {
               if(this.delay == 1) {
                  this.delay = 0;
                  this.move(this.mc.thePlayer.rotationYaw, -0.2F);
               }

               if(this.mc.thePlayer.motionY > 0.3D) {
                  this.portMove(this.mc.thePlayer.rotationYaw, 0.5F, 0.0F);
                  this.delay = 1;
               }
            } else if(this.isMode("aac 332 yport")) {
               this.onAAC332YPort();
            } else if(this.isMode("spartan yport")) {
               if(this.mc.thePlayer.motionY < 0.0D) {
                  --this.mc.thePlayer.motionY;
               }

               this.mc.timer.timerSpeed = 1.05F;
               this.timerWasSettedByMe = true;
               if(this.mc.thePlayer.onGround) {
                  this.move(this.mc.thePlayer.rotationYaw, 0.48F, 0.42F);
               }
            } else if(this.isMode("aac 332 yport fast")) {
               this.onAAC332YPortFast();
            }

            super.onUpdate();
         }
      }
   }

   private void onHypixel() {
   }

   private void onAAC317() {
      if(!this.mc.thePlayer.isCollidedHorizontally && !this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.gameSettings.keyBindForward.isKeyDown()) {
         if(this.mc.thePlayer.hurtTime <= 0) {
            if(this.mc.thePlayer.onGround) {
               this.mc.thePlayer.jump();
               this.mc.thePlayer.motionX *= 1.0099999904632568D;
               this.mc.thePlayer.motionZ *= 1.0099999904632568D;
               this.mc.thePlayer.motionY = 0.38510000705718994D;
            } else if(this.mc.thePlayer.motionY > 0.0D) {
               this.mc.thePlayer.motionY -= 0.014999999664723873D;
            } else {
               this.mc.thePlayer.motionY -= 0.01489999983459711D;
               BlockPos willBe = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.6D, this.mc.thePlayer.posZ);
               if(this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY() == this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0D && !this.mc.theWorld.getBlockState(willBe).getBlock().isTranslucent() && this.mc.theWorld.getBlockState(willBe).getBlock() != Blocks.water && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)) {
                  this.mc.thePlayer.motionY = -1.0D;
               }
            }

         }
      }
   }

   private void onTeleport() {
      ++this.delay;
      if(this.delay >= 1) {
         this.delay = 0;
         Location3D start = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
         Line3D line = new Line3D(start, (double)(-this.mc.thePlayer.rotationYaw), 0.0D, 8.0D);
         Iterator var4 = line.getPointsOn(2.0D).iterator();

         while(var4.hasNext()) {
            Location3D point = (Location3D)var4.next();
            this.mc.thePlayer.setPosition(point.getX(), point.getY(), point.getZ());
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(point.getX(), point.getY(), point.getZ(), false));
         }

         this.mc.thePlayer.setPosition(line.getEnd().getX(), line.getEnd().getY(), line.getEnd().getZ());
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(line.getEnd().getX(), line.getEnd().getY() - 1.0D, line.getEnd().getZ(), false));
      }
   }

   public void costum() {
      if(this.multiply.isToggled()) {
         if(!this.mc.gameSettings.keyBindForward.isKeyDown()) {
            return;
         }

         if(this.mc.thePlayer.onGround) {
            if(this.hopYMotionM.getCurrent() <= 0.0D && !this.mc.gameSettings.keyBindJump.isPressed()) {
               this.mc.thePlayer.motionX *= this.groundSpeedM.getCurrent();
               this.mc.thePlayer.motionZ *= this.groundSpeedM.getCurrent();
            } else {
               if(this.hopYMotionM.getCurrent() > 0.0D) {
                  this.mc.thePlayer.motionY = this.hopYMotionM.getCurrent();
               }

               this.mc.thePlayer.motionX *= this.hopSpeedM.getCurrent();
               this.mc.thePlayer.motionZ *= this.hopSpeedM.getCurrent();
            }
         } else if(this.mc.thePlayer.motionY > 0.0D) {
            this.mc.thePlayer.motionX *= this.airUpSpeedM.getCurrent();
            this.mc.thePlayer.motionZ *= this.airUpSpeedM.getCurrent();
         } else {
            this.mc.thePlayer.motionX *= this.airDownSpeedM.getCurrent();
            this.mc.thePlayer.motionZ *= this.airDownSpeedM.getCurrent();
         }
      } else {
         float direction = this.mc.thePlayer.rotationYaw;
         boolean move = false;
         boolean invert = false;
         if(this.mc.gameSettings.keyBindBack.isKeyDown()) {
            move = true;
            invert = true;
            direction += 180.0F;
         }

         if(this.mc.gameSettings.keyBindLeft.isKeyDown()) {
            move = true;
            if(invert) {
               direction += 90.0F;
            } else {
               direction -= 90.0F;
            }
         }

         if(this.mc.gameSettings.keyBindRight.isKeyDown()) {
            move = true;
            if(invert) {
               direction -= 90.0F;
            } else {
               direction += 90.0F;
            }
         }

         if(this.mc.gameSettings.keyBindForward.isKeyDown()) {
            move = true;
            direction = direction / 2.0F + this.mc.thePlayer.rotationYaw / 2.0F;
         } else if(this.mc.gameSettings.keyBindBack.isKeyDown()) {
            move = true;
            direction = direction / 2.0F + (this.mc.thePlayer.rotationYaw + 180.0F) / 2.0F;
         }

         if(move) {
            if(this.mc.thePlayer.onGround) {
               if(this.hopYMotionM.getCurrent() <= 0.0D && !this.mc.gameSettings.keyBindJump.isPressed()) {
                  this.move(direction, (float)this.groundSpeedM.getCurrent());
               } else {
                  if(this.hopYMotionM.getCurrent() > 0.0D) {
                     this.mc.thePlayer.motionY = this.hopYMotionM.getCurrent();
                  }

                  this.move(direction, (float)this.hopSpeedM.getCurrent());
               }
            } else if(this.mc.thePlayer.motionY > 0.0D) {
               this.move(direction, (float)this.airUpSpeedM.getCurrent());
            } else {
               this.move(direction, (float)this.airDownSpeedM.getCurrent());
            }
         }
      }

   }

   public void onAAC332YPortFast() {
      if(this.mc.thePlayer.hurtTime <= 0 && this.mc.thePlayer.motionY <= 0.0D) {
         if(!this.mc.gameSettings.keyBindJump.isKeyDown()) {
            if(this.mc.gameSettings.keyBindForward.isKeyDown()) {
               if(Math.sqrt(Math.abs(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX) + Math.abs(this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ)) >= 0.13D) {
                  if(this.delay > 0) {
                     --this.delay;
                  } else {
                     BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.5D, this.mc.thePlayer.posZ);
                     if(!this.mc.theWorld.getBlockState(pos).getBlock().isTranslucent() && this.mc.theWorld.getBlockState(pos).getBlock() != Blocks.water) {
                        if(this.mc.thePlayer.onGround) {
                           float f = this.mc.thePlayer.rotationYaw * 0.017453292F;
                           this.mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.2F);
                           this.mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.2F);
                           this.portMove(this.mc.thePlayer.rotationYaw, 0.0F, 0.42F);
                           this.mc.thePlayer.motionY = 0.0D;
                           this.mc.thePlayer.motionX *= 1.0169999599456787D;
                           this.mc.thePlayer.motionZ *= 1.0169999599456787D;
                        } else {
                           this.mc.thePlayer.motionY = -0.21D;
                           this.mc.thePlayer.motionX *= 1.0099999904632568D;
                           this.mc.thePlayer.motionZ *= 1.0099999904632568D;
                        }

                     } else {
                        this.delay = 10;
                     }
                  }
               }
            }
         }
      } else {
         if(this.delay < 5) {
            this.delay = 5;
         }

      }
   }

   public void onAAC332YPort() {
      if(this.mc.thePlayer.motionY <= 0.0D && this.mc.thePlayer.hurtTime <= 0) {
         if(this.startY > 0.0D) {
            --this.startY;
            if(this.startY > 10.0D) {
               this.startY = 0.0D;
            }

         } else if(this.mc.thePlayer.hurtTime <= 0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.5D, this.mc.thePlayer.posZ);
            if(!this.mc.theWorld.getBlockState(pos).getBlock().isTranslucent() && this.mc.theWorld.getBlockState(pos).getBlock() != Blocks.water) {
               if(Math.abs(this.mc.thePlayer.motionX) >= 0.05D || Math.abs(this.mc.thePlayer.motionZ) >= 0.05D) {
                  this.timerWasSettedByMe = true;
                  if(this.mc.thePlayer.onGround) {
                     ++this.delay;
                     if(this.delay > 2) {
                        this.portMove(this.mc.thePlayer.rotationYaw, 0.0F, 0.426F);
                        this.delay = 0;
                        this.mc.thePlayer.motionX *= 2.1700000762939453D;
                        this.mc.thePlayer.motionZ *= 2.1700000762939453D;
                     }
                  }

               }
            }
         }
      } else {
         this.startY = 10.0D;
      }
   }

   public void doNCP() {
      if(this.mc.gameSettings.keyBindForward.isKeyDown()) {
         this.mc.gameSettings.keyBindJump.pressed = false;
         if(this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
            this.mc.thePlayer.motionY = 0.39989998936653137D;
            this.mc.thePlayer.motionX *= 1.0720000267028809D;
            this.mc.thePlayer.motionZ *= 1.0720000267028809D;
         } else if(this.mc.thePlayer.motionY < 0.0D && this.mc.thePlayer.motionY > -0.6D) {
            this.mc.thePlayer.motionY -= 0.019999999552965164D;
            this.mc.thePlayer.jumpMovementFactor = 0.021F;
         }

         this.mc.timer.timerSpeed = 1.08F;
         this.timerWasSettedByMe = true;
      }
   }
}
