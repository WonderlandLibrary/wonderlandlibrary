package team.massacre.impl.module.playr;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.EntityUtil;
import team.massacre.utils.RotationUtil;

public class AimAssist extends Module {
   public static EntityLivingBase toAtk;
   public Property<Boolean> instant = new Property("Instant", false);
   public Property<Boolean> onClick = new Property("Aim On Click", true);
   public Property<Boolean> lockEntity = new Property("Lock Entity", true);
   public DoubleProperty aimRange;
   float lastYaw;
   float lastPitch;

   public AimAssist() {
      super("Aim Assist", 0, Category.PLAYER);
      this.aimRange = new DoubleProperty("Range", 4.0D, 1.0D, 7.0D, 0.1D, Representation.DISTANCE);
      this.addValues(new Property[]{this.instant, this.onClick, this.lockEntity, this.aimRange});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      if ((Boolean)this.instant.getValue()) {
         this.setSuffix("Instant");
      } else {
         this.hasSuffix = false;
      }

      EntityLivingBase curEnt = EntityUtil.getMouseOverEntity();
      if (toAtk != null) {
         try {
            if (this.mc.thePlayer.inventory.getCurrentItem().getItem().getUnlocalizedName().toLowerCase().contains("tnt")) {
               return;
            }
         } catch (Exception var6) {
         }

         float[] rot = RotationUtil.faceEntity(toAtk, (Boolean)this.instant.getValue());
         if (rot != null) {
            float yaw = rot[0];
            float pitch = rot[1];
            pitch = pitch > 60.0F ? 60.0F : pitch;
            pitch = pitch < -60.0F ? -60.0F : pitch;
            if ((Boolean)this.onClick.getValue()) {
               if (this.mc.gameSettings.keyBindAttack.pressed) {
                  if (this.mc.thePlayer.rotationYaw != this.lastYaw) {
                     this.mc.thePlayer.rotationYaw = yaw;
                  }

                  if (this.mc.thePlayer.rotationPitch != this.lastPitch) {
                     this.mc.thePlayer.rotationPitch = pitch;
                  }
               }
            } else {
               this.mc.thePlayer.rotationYaw = yaw;
               this.mc.thePlayer.rotationPitch = pitch;
            }
         }

         this.lastYaw = this.mc.thePlayer.rotationYaw;
         this.lastPitch = this.mc.thePlayer.rotationPitch;

         try {
            if (toAtk.getHealth() < 0.5F || (double)this.mc.thePlayer.getDistanceToEntity(toAtk) > (Double)this.aimRange.getValue() || toAtk.isInvisible() || toAtk.isDead || toAtk.deathTime != 0 || toAtk instanceof EntityArmorStand) {
               toAtk = null;
            }
         } catch (Exception var7) {
         }
      }

      if (curEnt != null) {
         if ((Boolean)this.lockEntity.getValue()) {
            toAtk = curEnt;
         } else {
            if (toAtk == null) {
               toAtk = curEnt;
            }

         }
      }
   }
}
