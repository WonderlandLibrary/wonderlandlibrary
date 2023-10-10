package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

@Module.Mod
public class Particulas extends Module {
   @Option.Op
   public boolean Coraçao;
   @Option.Op
   public boolean CRIT;
   @Option.Op
   public boolean Portal;
   @Option.Op
   public boolean Agua;
   @Option.Op
   public boolean Explosion;
   @Option.Op
   public boolean DripLava;
   @Option.Op
   public boolean Smoke;
   Minecraft mc = Minecraft.getMinecraft();
   @Option.Op
   public boolean Stone;
   @Option.Op
   public boolean Flamejante;
   @Option.Op
   public boolean Slime;
   @Option.Op
   public boolean SnowBall;
   @Option.Op
   public boolean CRITMAGIC;

   @EventTarget
   public void onUpdate(UpdateEvent var1) {
      if (this.Coraçao) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.HEART, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Explosion) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Stone) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Flamejante) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.FLAME, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.CRITMAGIC) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.SnowBall) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SNOWBALL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SNOWBALL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SNOWBALL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SNOWBALL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SNOWBALL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Agua) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_WATER, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_WATER, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_WATER, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_WATER, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_WATER, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.DripLava) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_LAVA, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_LAVA, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_LAVA, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_LAVA, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.DRIP_LAVA, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.CRIT) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.4D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.CRIT, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Smoke) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.SMOKE_LARGE, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Portal) {
         Minecraft.theWorld.spawnParticle(EnumParticleTypes.PORTAL, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2D, Minecraft.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      if (this.Slime) {
         for(int var2 = 1; var2 < 50; ++var2) {
            Minecraft.theWorld.spawnParticle(EnumParticleTypes.SLIME, Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5D, Minecraft.thePlayer.posZ, 1.0D, 0.0D, 0.0D, new int[0]);
         }
      }

   }
}
