package team.massacre.impl.module.playr;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventUpdate;
import team.massacre.impl.event.ServerPosLookEvent;
import team.massacre.utils.RotationUtil;

public class NoRotate extends Module {
   public int ticksSinceFlag;
   public float yaw;
   public float pitch;

   public NoRotate() {
      super("No Rotate", 0, Category.PLAYER);
   }

   @Handler
   private void a(EventUpdate event) {
      if (event.isPre() && this.ticksSinceFlag <= 4) {
         ++this.ticksSinceFlag;
         float[] rotations = RotationUtil.getRotations(new float[]{this.yaw, this.pitch}, 45.0F, new float[]{event.getYaw(), event.getPitch()});
         this.yaw = rotations[0];
         this.pitch = rotations[1];
         event.setYaw(rotations[0]);
         event.setPitch(rotations[1]);
      }

   }

   @Handler
   private void v(ServerPosLookEvent event) {
      this.yaw = event.getYaw();
      this.pitch = event.getPitch();
      this.ticksSinceFlag = 0;
      event.setYaw(this.mc.thePlayer.rotationYaw);
      event.setPitch(this.mc.thePlayer.rotationPitch);
   }
}
