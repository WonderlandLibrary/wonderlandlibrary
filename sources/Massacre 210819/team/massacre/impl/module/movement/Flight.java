package team.massacre.impl.module.movement;

import net.minecraft.potion.Potion;
import org.apache.commons.lang3.RandomUtils;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventMove;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.MovementUtils;

public class Flight extends Module {
   public boolean hasJumped;
   public boolean noMoreJumpLOL;
   public boolean doSlow;
   public boolean canFly;
   public boolean adjustSpeed;
   public double speed;
   public double startY;
   public EnumProperty<Flight.AirpodFlightMode> mode;
   public int enabledTicks;
   public int stage;

   public Flight() {
      super("Flight", 0, Category.MOVEMENT);
      this.mode = new EnumProperty("Mode", Flight.AirpodFlightMode.Collide);
      this.addValues(new Property[]{this.mode});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((Flight.AirpodFlightMode)this.mode.getValue()).name());
      switch((Flight.AirpodFlightMode)this.mode.getValue()) {
      case Collide:
         double y = (double)Math.round(this.mc.thePlayer.posY / 0.015625D) * 0.015625D;
         this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, y, this.mc.thePlayer.posZ);
         eventUpdate.setOnGround(true);
         switch(this.stage) {
         case 0:
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688697815D, this.mc.thePlayer.posZ);
            this.stage = 1;
            break;
         case 1:
            this.mc.thePlayer.motionY = 0.0D;
            this.stage = 0;
            break;
         default:
            eventUpdate.setOnGround(false);
            if (this.mc.thePlayer.posY <= this.startY) {
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.startY, this.mc.thePlayer.posZ);
               eventUpdate.setPosY(this.startY);
            }
         }
      case Redesky:
      default:
      }
   }

   @Handler
   public void onMove(EventMove eventMove) {
      double random = RandomUtils.nextDouble(2.146D, 2.149998999999999D);
      switch((Flight.AirpodFlightMode)this.mode.getValue()) {
      case Collide:
         if (this.mc.thePlayer.isMoving()) {
            if (!this.doSlow) {
               this.speed = MovementUtils.getBaseMoveSpeed() * 20.0D;
               this.doSlow = true;
            } else {
               this.speed -= this.speed / 35.0D;
               this.doSlow = false;
            }
         }

         MovementUtils.setSpeed(eventMove, this.speed);
         break;
      case Redesky:
         boolean hasspeed = this.mc.thePlayer.isPotionActive(Potion.moveSpeed);
         double boost = MovementUtils.getBaseMoveSpeed() * 6.5D / (hasspeed ? 1.6D : 1.25D);
         switch(this.enabledTicks) {
         case 0:
            if (this.mc.thePlayer.hurtTime != 0 && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
               this.speed = 0.635D;
            }
            break;
         case 1:
            if (this.mc.thePlayer.onGround) {
               eventMove.setY(this.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815D));
            }

            this.mc.timer.timerSpeed = 0.99F;
            this.speed *= 2.0D;
            break;
         case 2:
            this.speed = boost;
            break;
         default:
            eventMove.setY(this.mc.thePlayer.motionY = this.mc.thePlayer.ticksExisted % 4 == 0 ? -0.0010000000474974513D : 0.0010000000474974513D);
            this.speed -= this.speed / 159.0D;
         }

         this.speed = Math.max(this.speed, MovementUtils.getBaseMoveSpeed());
         ++this.enabledTicks;
         MovementUtils.setSpeed(eventMove, this.speed);
      case Ghostly:
      }

   }

   public void onDisable() {
      super.onDisable();
      this.noMoreJumpLOL = false;
      this.speed = 0.0D;
      this.mc.thePlayer.capabilities.isFlying = false;
      this.mc.timer.timerSpeed = 1.0F;
      this.hasJumped = false;
   }

   public void onEnable() {
      super.onEnable();
      switch((Flight.AirpodFlightMode)this.mode.getValue()) {
      case Collide:
         this.startY = this.mc.thePlayer.posY;
         this.stage = 0;
      case Redesky:
      default:
         this.enabledTicks = 0;
         this.noMoreJumpLOL = false;
         this.adjustSpeed = false;
         this.canFly = true;
      }
   }

   public static enum AirpodFlightMode {
      Redesky,
      Ghostly,
      Collide;
   }
}
