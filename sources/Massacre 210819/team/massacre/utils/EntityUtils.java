package team.massacre.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

public final class EntityUtils {
   private EntityUtils() {
   }

   public static double getMovementSpeed(EntityLivingBase entity) {
      return MathUtil.distance(entity.prevPosX, entity.prevPosZ, entity.posX, entity.posZ);
   }

   public static Block getBlockUnder(EntityLivingBase entity, double offset) {
      return WorldUtils.getBlock(new BlockPos(entity.posX, entity.posY - offset, entity.posZ));
   }

   public static int getPotionModifier(EntityLivingBase entity, Potion potion) {
      PotionEffect effect = entity.getActivePotionEffect(potion.id);
      return effect != null ? effect.getAmplifier() + 1 : 0;
   }

   public static boolean isMoving(EntityLivingBase entity) {
      return MathUtil.distance(entity.prevPosX, entity.posY, entity.prevPosZ, entity.posX, entity.posY, entity.posZ) > 9.0E-4D;
   }

   public static boolean isOnGround(EntityLivingBase entity) {
      return entity.onGround && entity.isCollidedVertically;
   }

   public static List<EntityLivingBase> getLivingEntities(Predicate<EntityLivingBase> validator) {
      List<EntityLivingBase> entities = new ArrayList();
      Iterator var2 = WorldUtils.getWorld().loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)entity;
            if (validator.test(e)) {
               entities.add(e);
            }
         }
      }

      return entities;
   }
}
