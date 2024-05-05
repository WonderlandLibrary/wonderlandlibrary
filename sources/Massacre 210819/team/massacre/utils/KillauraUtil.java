package team.massacre.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class KillauraUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static Entity getTarget(boolean players, boolean mobs, boolean animals, boolean villagers, boolean invisibles, boolean others, double reach) {
      Iterator var8 = mc.theWorld.loadedEntityList.iterator();

      EntityLivingBase o;
      do {
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           do {
                              Object obj;
                              do {
                                 do {
                                    do {
                                       if (!var8.hasNext()) {
                                          return null;
                                       }

                                       obj = var8.next();
                                    } while(!(obj instanceof EntityLivingBase));
                                 } while((double)(o = (EntityLivingBase)obj).getDistanceToEntity(mc.thePlayer) > reach);
                              } while(o == mc.thePlayer);
                           } while(o.isDead);
                        } while(!(o instanceof EntityLivingBase));
                     } while(o instanceof EntityPlayer && !players);
                  } while(o instanceof EntityMob && !mobs);
               } while(o instanceof EntityAnimal && !animals);
            } while(o instanceof EntityVillager && !villagers);
         } while(o.isInvisible() && !invisibles);
      } while(!others && o instanceof EntityPlayer && o instanceof EntityMob && o instanceof EntityAnimal && o instanceof EntityVillager && o.isInvisible());

      return o;
   }

   public static List<EntityLivingBase> getTargets(boolean teams, int maxTargets, boolean mobs, boolean players, double range, boolean invis) {
      ArrayList<EntityLivingBase> list = new ArrayList();
      Iterator var8 = mc.theWorld.loadedEntityList.iterator();

      while(true) {
         Object obj;
         EntityLivingBase o;
         do {
            do {
               do {
                  if (!var8.hasNext()) {
                     return list;
                  }

                  obj = var8.next();
               } while(!(obj instanceof EntityLivingBase));
            } while((double)(o = (EntityLivingBase)obj).getDistanceToEntity(mc.thePlayer) > (((EntityLivingBase)obj).posY > mc.thePlayer.posY + 1.0D ? range + 2.0D : range));
         } while(o.isInvisible() && !invis);

         if (!o.isDead && o.getHealth() != 0.0F && o != mc.thePlayer && obj instanceof EntityPlayer && list.size() < maxTargets) {
            list.add(o);
         }
      }
   }

   private static MovingObjectPosition tracePathD(World w, double posX, double posY, double posZ, double v, double v1, double v2, float borderSize, HashSet<Entity> exclude) {
      return tracePath(w, (float)posX, (float)posY, (float)posZ, (float)v, (float)v1, (float)v2, borderSize, exclude);
   }

   public static MovingObjectPosition rayCast(EntityPlayerSP player, double x, double y, double z) {
      HashSet<Entity> excluded = new HashSet();
      excluded.add(player);
      return tracePathD(player.worldObj, player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, x, y, z, 1.0F, excluded);
   }

   private static MovingObjectPosition tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, HashSet<Entity> excluded) {
      Vec3 startVec = new Vec3((double)x, (double)y, (double)z);
      Vec3 endVec = new Vec3((double)tx, (double)ty, (double)tz);
      float minX = x < tx ? x : tx;
      float minY = y < ty ? y : ty;
      float minZ = z < tz ? z : tz;
      float maxX = x > tx ? x : tx;
      float maxY = y > ty ? y : ty;
      float maxZ = z > tz ? z : tz;
      AxisAlignedBB bb = (new AxisAlignedBB((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ)).expand((double)borderSize, (double)borderSize, (double)borderSize);
      ArrayList<Entity> allEntities = (ArrayList)world.getEntitiesWithinAABBExcludingEntity((Entity)null, bb);
      MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
      startVec = new Vec3((double)x, (double)y, (double)z);
      endVec = new Vec3((double)tx, (double)ty, (double)tz);
      Entity closestHitEntity = null;
      float closestHit = Float.POSITIVE_INFINITY;
      Iterator var23 = allEntities.iterator();

      while(true) {
         float currentHit;
         Entity ent;
         do {
            MovingObjectPosition intercept;
            do {
               float entBorder;
               AxisAlignedBB entityBb;
               do {
                  do {
                     do {
                        if (!var23.hasNext()) {
                           if (closestHitEntity != null) {
                              blockHit = new MovingObjectPosition(closestHitEntity);
                           }

                           return blockHit;
                        }

                        ent = (Entity)var23.next();
                     } while(!ent.canBeCollidedWith());
                  } while(excluded.contains(ent));

                  entBorder = ent.getCollisionBorderSize();
                  entityBb = ent.getEntityBoundingBox();
               } while(entityBb == null);

               entityBb = entityBb.expand((double)entBorder, (double)entBorder, (double)entBorder);
               intercept = entityBb.calculateIntercept(startVec, endVec);
            } while(intercept == null);

            currentHit = (float)intercept.hitVec.distanceTo(startVec);
         } while(currentHit >= closestHit && currentHit != 0.0F);

         closestHit = currentHit;
         closestHitEntity = ent;
      }
   }
}
