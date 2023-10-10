package my.NewSnake.Tank.module.modules.RENDER;

import java.awt.Color;
import java.util.Objects;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

@Module.Mod
public class MeuPing extends Module {
   public Minecraft mc = Minecraft.getMinecraft();
   @Option.Op(
      min = 50.0D,
      max = 700.0D,
      increment = 1.0D
   )
   private double y;
   @Option.Op
   private boolean Ping;
   @Option.Op(
      min = 50.0D,
      max = 700.0D,
      increment = 1.0D
   )
   private double x;

   @EventTarget
   public void PingHUD(Render2DEvent var1) {
      float var10000 = (float)(var1.getWidth() / 3 + (int)this.x);
      var10000 = (float)(var1.getHeight() / 3 + (int)this.y);
      if (this.Ping) {
         NetworkPlayerInfo var4 = ClientUtils.mc().getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID());
         String var5 = "Ping: " + (Objects.isNull(var4) ? "0ms" : var4.getResponseTime() + "ms");
         Minecraft.fontRendererObj.drawStringWithShadow(var5, (float)(this.x + 46.5D), (float)(this.y + 28.0D), (new Color(255, 255, 255)).getRGB());
      }

   }
}
