package my.NewSnake.Tank.module.modules.WORLD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

@Module.Mod
public class Snow extends Module {
   public static transient ArrayList snowflakes = new ArrayList();
   public static transient float lastPitch = 0.0F;
   public static transient int snowflakeAmount = 1000;
   public static transient float lastYaw = 0.0F;

   @EventTarget
   public void TEST(Render2DEvent var1) {
      ScaledResolution var2 = new ScaledResolution(mc);
      Minecraft var10002 = mc;
      double var3 = (double)(360.0F * ((lastYaw - Minecraft.thePlayer.rotationYaw) / mc.gameSettings.fovSetting));
      if (Double.isInfinite(var3)) {
         var3 = 0.0D;
      }

      var10002 = mc;
      double var5 = (double)(360.0F * ((lastPitch - Minecraft.thePlayer.rotationPitch) / mc.gameSettings.fovSetting));
      if (Double.isInfinite(var5)) {
         var5 = 0.0D;
      }

      ArrayList var7 = new ArrayList();
      Iterator var9 = snowflakes.iterator();

      Snow.Snowflake var8;
      while(var9.hasNext()) {
         var8 = (Snow.Snowflake)var9.next();
         var8.x += var3;
         var8.y += (double)(new Random()).nextInt(5) + var5;
         if (var8.x > 0.0D && var8.x < var2.getScaledWidth_double() && var8.y > 0.0D && var8.y < var2.getScaledHeight_double()) {
            Gui.drawRect(var8.x, var8.y, var8.x + 1.0D + (new Random()).nextDouble() + (double)(new Random()).nextInt(1), var8.y + 1.0D + (new Random()).nextDouble() + (double)(new Random()).nextInt(1), -1);
         }

         if (var8.x < -200.0D || var8.x > var2.getScaledWidth_double() + 200.0D || var8.y < -300.0D || var8.y > var2.getScaledHeight_double()) {
            var7.add(var8);
         }
      }

      var9 = var7.iterator();

      while(var9.hasNext()) {
         var8 = (Snow.Snowflake)var9.next();
         snowflakes.remove(var8);
      }

      if (snowflakes.size() < snowflakeAmount) {
         for(int var11 = snowflakes.size(); var11 < snowflakeAmount; ++var11) {
            snowflakes.add(new Snow.Snowflake(this, (double)((new Random()).nextInt(var2.getScaledWidth() + 400) - 200), (double)((new Random()).nextInt(300) * -1)));
         }
      }

      Minecraft var10000 = mc;
      lastYaw = Minecraft.thePlayer.rotationYaw;
      var10000 = mc;
      lastPitch = Minecraft.thePlayer.rotationPitch;
   }

   public class Snowflake {
      final Snow this$0;
      public double x;
      public double y;

      public Snowflake(Snow var1, double var2, double var4) {
         this.this$0 = var1;
         this.x = 0.0D;
         this.y = 0.0D;
         this.x = var2;
         this.y = var4;
      }
   }
}
