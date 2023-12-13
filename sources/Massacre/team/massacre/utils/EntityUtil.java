package team.massacre.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWaterMob;

public class EntityUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static EntityLivingBase getMouseOverEntity() {
      EntityLivingBase re = null;
      if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase && !(mc.objectMouseOver.entityHit instanceof EntityArmorStand)) {
         re = (EntityLivingBase)mc.objectMouseOver.entityHit;
         if (!check(re)) {
            re = null;
         }
      }

      return re;
   }

   public static boolean check(EntityLivingBase in) {
      return !(in instanceof EntityAmbientCreature) && !(in instanceof EntityAgeable) && !(in instanceof EntityTameable) && !(in instanceof EntityWaterMob) && !in.isDead && in.getHealth() > 0.0F;
   }
}
