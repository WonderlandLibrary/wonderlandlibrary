package my.NewSnake.Tank.module.modules.COMBAT;

import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@Module.Mod
public class Aimbot extends Module {
   private double range = 6.0D;
   private double Angle = 9999.0D;
   private EntityLivingBase target;
   private double speed = 1.0D;

   public void onEnable() {
      this.target = null;
      super.enable();
   }

   @EventTarget(3)
   public void onUpdate(UpdateEvent var1) {
      if (var1.getState().equals(Event.State.PRE) && ClientUtils.player().isEntityAlive()) {
         Iterator var3 = ClientUtils.world().loadedEntityList.iterator();

         while(true) {
            Object var2;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var2 = var3.next();
            } while(!(var2 instanceof EntityLivingBase));

            if ((Entity)var2 != false && ((EntityLivingBase)var2).isEntityAlive()) {
               this.target = (EntityLivingBase)var2;
            } else {
               this.target = null;
            }

            if (this.target != null) {
               EntityPlayerSP var7 = ClientUtils.player();
               var7.rotationPitch += (float)((double)this.getPitchChange(this.target) / this.speed);
               EntityPlayerSP var11 = ClientUtils.player();
               var11.rotationYaw += (float)((double)this.getYawChange(this.target) / this.speed);
            }
         }
      }
   }

   public float getPitchChange(Entity var1) {
      double var10000 = var1.posX;
      ClientUtils.mc();
      double var2 = var10000 - Minecraft.thePlayer.posX;
      var10000 = var1.posZ;
      ClientUtils.mc();
      double var4 = var10000 - Minecraft.thePlayer.posZ;
      var10000 = var1.posY - 2.2D + (double)var1.getEyeHeight();
      ClientUtils.mc();
      double var6 = var10000 - Minecraft.thePlayer.posY;
      double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4);
      double var10 = -Math.toDegrees(Math.atan(var6 / var8));
      ClientUtils.mc();
      return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)var10) - 2.5F;
   }

   public float getYawChange(Entity var1) {
      double var10000 = var1.posX;
      ClientUtils.mc();
      double var2 = var10000 - Minecraft.thePlayer.posX;
      var10000 = var1.posZ;
      ClientUtils.mc();
      double var4 = var10000 - Minecraft.thePlayer.posZ;
      double var6 = 0.0D;
      if (var4 < 0.0D && var2 < 0.0D) {
         var6 = 90.0D + Math.toDegrees(Math.atan(var4 / var2));
      } else if (var4 < 0.0D && var2 > 0.0D) {
         var6 = -90.0D + Math.toDegrees(Math.atan(var4 / var2));
      } else {
         var6 = Math.toDegrees(-Math.atan(var2 / var4));
      }

      ClientUtils.mc();
      return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)var6));
   }
}
