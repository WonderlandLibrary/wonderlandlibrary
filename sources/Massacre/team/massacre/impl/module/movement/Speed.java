package team.massacre.impl.module.movement;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.util.BlockPos;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventJump;
import team.massacre.impl.event.EventMove;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.FrictionUtil;
import team.massacre.utils.MovementUtils;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.baseHelper.ACType;

public class Speed extends Module {
   public boolean hasJumped;
   public boolean doSlow;
   public boolean spoofGround;
   public double movementSpeed;
   public double lastDist;
   private int verusStage;
   private int watchdogStage;
   public EnumProperty<Speed.Mode> mode;
   public EnumProperty<Speed.VerusMode> verusMode;
   public EnumProperty<Speed.WatchdogMode> watchdogMode;
   public Property<Boolean> dmgBoost;
   public Property<Boolean> fastFall;

   public Speed() {
      super("Speed", 0, Category.MOVEMENT);
      this.mode = new EnumProperty("Mode", Speed.Mode.Saico);
      this.verusMode = new EnumProperty("Verus Mode", Speed.VerusMode.Hop, () -> {
         return this.mode.getValue() == Speed.Mode.Verus;
      });
      this.watchdogMode = new EnumProperty("Watchdog Mode", Speed.WatchdogMode.Hop, () -> {
         return this.mode.getValue() == Speed.Mode.Watchdog;
      });
      this.dmgBoost = new Property("Damage Boost", true, () -> {
         return this.mode.getValue() == Speed.Mode.Watchdog;
      });
      this.fastFall = new Property("Fast Falling", true, () -> {
         return this.mode.getValue() == Speed.Mode.Watchdog;
      });
      this.addValues(new Property[]{this.mode, this.verusMode, this.watchdogMode, this.dmgBoost, this.fastFall});
   }

   @Handler
   public void bbb(EventJump event) {
      event.setCancelled(this.mode.getValue() == Speed.Mode.Watchdog && this.mc.gameSettings.keyBindJump.isKeyDown());
   }

   @Handler
   public void b(EventUpdate event) {
      EntityPlayerSP var10000;
      if (!this.mc.thePlayer.isMoving()) {
         var10000 = this.mc.thePlayer;
         var10000.motionX *= 0.12D;
         var10000 = this.mc.thePlayer;
         var10000.motionZ *= 0.12D;
      }

      switch((Speed.Mode)this.mode.getValue()) {
      case Verus:
         PacketUtil.sendPacketNoEvent(new C0CPacketInput());
         switch((Speed.VerusMode)this.verusMode.getValue()) {
         case Float:
            event.setOnGround(this.mc.thePlayer.onGround || this.spoofGround);
         }
      default:
         if (this.mode.getValue() == Speed.Mode.Watchdog && (Boolean)this.fastFall.getValue() && !(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ)).getBlock() instanceof BlockSlab) && this.mc.thePlayer.motionY < 0.1D && this.mc.thePlayer.motionY > -0.25D && (double)this.mc.thePlayer.fallDistance < 0.1D) {
            var10000 = this.mc.thePlayer;
            var10000.motionY -= 0.055D;
         }

         if (event.isPre()) {
            double x = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
            double z = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(x * x + z * z);
            if (this.mode.getValue() == Speed.Mode.Watchdog) {
               double baseMoveSpeed;
               switch((Speed.WatchdogMode)this.watchdogMode.getValue()) {
               case Hop:
                  if (this.mc.thePlayer.isMoving()) {
                     baseMoveSpeed = MovementUtils.getBaseSpeed(ACType.HYPIXEL);
                     double groundSpeed = (Boolean)this.dmgBoost.getValue() && this.mc.thePlayer.hurtTime > 5 ? baseMoveSpeed * 2.249D : baseMoveSpeed * 1.69D;
                     double airSpeed = (Boolean)this.dmgBoost.getValue() && this.mc.thePlayer.hurtTime > 5 ? 0.3D : 0.76D;
                     float frictionSpeed = (Boolean)this.dmgBoost.getValue() && this.mc.thePlayer.hurtTime > 5 ? 99.85665F : ((Boolean)this.fastFall.getValue() ? 97.5F : 99.5F);
                     if (this.mc.thePlayer.onGround && !this.doSlow) {
                        this.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815D);
                        MovementUtils.setSpeed(groundSpeed);
                        this.doSlow = true;
                     } else if (this.doSlow) {
                        this.doSlow = false;
                        double bunnySlope = airSpeed * (this.lastDist - baseMoveSpeed);
                        MovementUtils.setSpeed(this.lastDist - bunnySlope);
                     } else {
                        MovementUtils.setSpeed((double)FrictionUtil.applyCustomFriction((float)this.mc.thePlayer.getSpeed(), frictionSpeed));
                     }
                  }
                  break;
               case Ground:
                  if (this.mc.thePlayer.isMoving()) {
                     baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                     if (this.mc.thePlayer.onGround) {
                        var10000 = this.mc.thePlayer;
                        var10000.motionY += 0.0624D;
                        this.movementSpeed = 0.22100000083446503D;
                     }

                     MovementUtils.setSpeed(this.movementSpeed = Math.max(this.movementSpeed, baseMoveSpeed));
                  }
               }
            }
         }

      }
   }

   @Handler
   public void onMove(EventMove event) {
      this.setSuffix(((Speed.Mode)this.mode.getValue()).name());
      switch((Speed.Mode)this.mode.getValue()) {
      case Verus:
         PacketUtil.sendPacketNoEvent(new C0CPacketInput());
         switch((Speed.VerusMode)this.verusMode.getValue()) {
         case Float:
            if (this.mc.thePlayer.isMovingOnGround()) {
               this.movementSpeed = 0.612D;
               event.setY(0.41999998688697815D);
               this.spoofGround = true;
               this.verusStage = 0;
            } else if (this.verusStage <= 9) {
               this.movementSpeed += 0.1D;
               event.setY(0.0D);
               ++this.verusStage;
            } else {
               this.movementSpeed = 0.24D;
               this.spoofGround = false;
            }

            this.mc.thePlayer.motionY = event.getY();
            MovementUtils.setSpeed(event, this.movementSpeed - 1.0E-4D);
            break;
         case Hop:
            if (this.mc.thePlayer.isMovingOnGround()) {
               this.movementSpeed = 0.612D;
               event.setY(this.mc.thePlayer.motionY = 0.41999998688697815D);
            } else {
               this.movementSpeed = 0.36D;
            }

            MovementUtils.setSpeed(event, this.movementSpeed - 1.0E-4D);
            break;
         case YPort:
            if (this.mc.thePlayer.isMovingOnGround()) {
               this.movementSpeed = 0.612D;
               event.setY(this.mc.thePlayer.motionY = 0.41999998688697815D);
               if (!this.mc.thePlayer.movementInput.jump) {
                  this.spoofGround = true;
               }
            } else if (this.spoofGround) {
               this.movementSpeed = 0.55D;
               event.setY(this.mc.thePlayer.motionY = 0.0D);
               this.spoofGround = false;
            }

            MovementUtils.setSpeed(event, this.movementSpeed - 1.0E-4D);
            break;
         case Descend:
            if (this.mc.thePlayer.isMovingOnGround()) {
               this.spoofGround = true;
               this.movementSpeed = 0.612D;
               event.setY(this.mc.thePlayer.motionY = 0.41999998688697815D);
               this.verusStage = 0;
            } else if (this.verusStage <= 9) {
               this.spoofGround = true;
               this.movementSpeed += 0.05D;
               event.setY(this.mc.thePlayer.motionY = 0.0D);
               ++this.verusStage;
            } else {
               this.movementSpeed = 0.24D;
               this.spoofGround = false;
               this.verusStage = 0;
            }

            this.mc.thePlayer.motionY = event.getY();
            MovementUtils.setSpeed(event, this.movementSpeed - 1.0E-4D);
            break;
         case Old:
            if (this.mc.thePlayer.isMovingOnGround()) {
               this.movementSpeed = 0.612D;
               event.setY(this.mc.thePlayer.motionY = 0.41999998688697815D);
               this.spoofGround = true;
            } else {
               this.movementSpeed *= 1.0536D;
            }
         }

         MovementUtils.setSpeed(event, MovementUtils.getBaseSpeed(ACType.VERUS) - 0.24D + this.movementSpeed);
      case Watchdog:
      default:
         break;
      case Saico:
         if (this.mc.thePlayer.isMoving() && !this.mc.thePlayer.isInWater()) {
            double baseSpeed = MovementUtils.getBaseMoveSpeed();
            if (this.mc.thePlayer.onGround) {
               this.mc.timer.timerSpeed = 1.0F;
               event.setY(this.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.4099999964237213D));
               this.movementSpeed = baseSpeed * 2.07D - 1.0E-4D;
               this.doSlow = true;
            } else if (!this.doSlow && !this.mc.thePlayer.isCollidedHorizontally) {
               this.movementSpeed = MovementUtils.calculateFriction(this.movementSpeed, this.lastDist, baseSpeed);
            } else {
               double e = 0.66D * (this.lastDist - baseSpeed);
               this.movementSpeed = this.lastDist - e;
               this.doSlow = false;
            }

            if (this.mc.thePlayer.isCollidedHorizontally || !this.mc.thePlayer.isMoving()) {
               this.movementSpeed = baseSpeed;
            }

            MovementUtils.setSpeed(event, Math.max(this.movementSpeed, baseSpeed));
         }
         break;
      case Ghostly:
         if (this.mc.thePlayer.isMoving()) {
            if (!this.doSlow) {
               this.movementSpeed = MovementUtils.getBaseMoveSpeed() * 7.1D;
               this.doSlow = true;
            } else {
               this.movementSpeed -= this.movementSpeed / 25.0D;
               this.doSlow = false;
            }
         }

         MovementUtils.setSpeed(event, this.movementSpeed);
      }

   }

   public void onDisable() {
      super.onDisable();
      EntityPlayerSP var10000 = this.mc.thePlayer;
      var10000.motionX *= 0.0D;
      var10000 = this.mc.thePlayer;
      var10000.motionZ *= 0.0D;
   }

   public void onEnable() {
      super.onEnable();
      this.lastDist = 0.0D;
      this.spoofGround = false;
      this.verusStage = 0;
      this.movementSpeed = 0.0D;
   }

   public static enum WatchdogMode {
      Hop,
      Ground;
   }

   public static enum VerusMode {
      Hop,
      YPort,
      Old,
      Descend,
      Float;
   }

   public static enum Mode {
      Watchdog,
      Ghostly,
      Saico,
      Verus;
   }
}
