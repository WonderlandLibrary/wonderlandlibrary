package my.NewSnake.Tank.module.modules.COMBAT;

import java.util.List;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

@Module.Mod
public class AimAssist extends Module {
   @Option.Op(
      min = 0.0D,
      max = 10.0D,
      increment = 1.0D
   )
   private double Range = 6.0D;

   @EventTarget
   public void onUpdate(UpdateEvent var1) {
      ClientUtils.mc();
      List var2 = Minecraft.theWorld.playerEntities;

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var10000 = ((EntityPlayer)var2.get(var3)).getName();
         ClientUtils.mc();
         if (var10000 != Minecraft.thePlayer.getName()) {
            EntityPlayer var4 = (EntityPlayer)var2.get(1);
            ClientUtils.mc();
            float var6 = Minecraft.thePlayer.getDistanceToEntity(var4);
            ClientUtils.mc();
            if (var6 > Minecraft.thePlayer.getDistanceToEntity((Entity)var2.get(var3))) {
               var4 = (EntityPlayer)var2.get(var3);
            }

            ClientUtils.mc();
            float var5 = Minecraft.thePlayer.getDistanceToEntity(var4);
            if ((double)var5 < this.Range) {
               ClientUtils.mc();
               if (Minecraft.thePlayer.canEntityBeSeen(var4)) {
                  faceEntity(var4);
                  FriendManager.isFriend(var4.getName());
               }
            }
         }
      }

   }

   public static synchronized void faceEntity(EntityLivingBase var0) {
      float[] var1 = getRotationsNeeded(var0);
      if (var1 != null) {
         ClientUtils.mc();
         Minecraft.thePlayer.rotationYaw = var1[0];
         ClientUtils.mc();
         Minecraft.thePlayer.rotationPitch = var1[1] + 1.0F;
      }

   }

   public static float[] getRotationsNeeded(Entity var0) {
      if (var0 == null) {
         return null;
      } else {
         double var10000 = var0.posX;
         ClientUtils.mc();
         double var1 = var10000 - Minecraft.thePlayer.posX;
         var10000 = var0.posZ;
         ClientUtils.mc();
         double var3 = var10000 - Minecraft.thePlayer.posZ;
         double var10001;
         double var5;
         if (var0 instanceof EntityLivingBase) {
            EntityLivingBase var7 = (EntityLivingBase)var0;
            var10000 = var7.posY + (double)var7.getEyeHeight();
            ClientUtils.mc();
            var10001 = Minecraft.thePlayer.posY;
            ClientUtils.mc();
            var5 = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         } else {
            var10000 = (var0.boundingBox.minY + var0.boundingBox.maxY) / 2.0D;
            ClientUtils.mc();
            var10001 = Minecraft.thePlayer.posY;
            ClientUtils.mc();
            var5 = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
         }

         double var12 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
         float var9 = (float)(Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
         float var10 = (float)(-(Math.atan2(var5, var12) * 180.0D / 3.141592653589793D));
         float[] var11 = new float[2];
         ClientUtils.mc();
         float var10003 = Minecraft.thePlayer.rotationYaw;
         ClientUtils.mc();
         var11[0] = var10003 + MathHelper.wrapAngleTo180_float(var9 - Minecraft.thePlayer.rotationYaw);
         ClientUtils.mc();
         var10003 = Minecraft.thePlayer.rotationPitch;
         ClientUtils.mc();
         var11[1] = var10003 + MathHelper.wrapAngleTo180_float(var10 - Minecraft.thePlayer.rotationPitch);
         return var11;
      }
   }
}
