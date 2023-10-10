package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityCritFX extends EntitySmokeFX {
   protected EntityCritFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12, 2.5F);
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityCritFX(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
