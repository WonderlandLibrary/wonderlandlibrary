package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.UpdateActionEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.MoveUtil;
import rip.autumn.utils.PlayerUtils;

@Label("Speed")
@Category(ModuleCategory.MOVEMENT)
public final class SpeedMod extends Module {
   public final EnumOption mode;
   public final DoubleOption bhopSpeed;

   public SpeedMod() {
      this.mode = new EnumOption("Mode", Mode.VANILLA);
      this.bhopSpeed = new DoubleOption("Speed", 0.5D, () -> this.mode.getValue() == Mode.VANILLA, 0.1D, 3.0D, 0.05D);
      this.setMode(this.mode);
      this.addOptions(this.mode, this.bhopSpeed);
   }

   public void onDisable() {
      mc.thePlayer.stepHeight = 0.625F;
      mc.timer.timerSpeed = 1.0F;
   }

   @Listener(UpdateActionEvent.class)
   public void onActionUpdate(UpdateActionEvent event) {
      if (event.isSneakState()) {
         event.setSneakState(false);
      }
   }

   @Listener(MotionUpdateEvent.class)
   public void onMotionUpdate(MotionUpdateEvent event) {
      if (event.isPre()) {
         if (PlayerUtils.isInLiquid()) {
            return;
         }

         switch (mode.getValue().toString()) {
            case "VANILLA":
               MoveUtil.setSpeed((Double) bhopSpeed.getValue());
               if(isMoving() && getGround()) {
                  mc.thePlayer.jump();
               } else if(!isMoving()) {
                  mc.thePlayer.motionX = 0.0;
                  mc.thePlayer.motionZ = 0.0;
               }
               break;
            case "VERUS":
               if (isMoving()) {
                  if (getGround()) {
                     mc.thePlayer.jump();
                     MoveUtil.setSpeed(0.48);
                  } else {
                     MoveUtil.setSpeed(MoveUtil.getSpeed());
                  }
               } else {
                  MoveUtil.setSpeed(0);
               }
               break;
         }

      }

   }

   private enum Mode {
      VANILLA,
      VERUS;
   }
}
