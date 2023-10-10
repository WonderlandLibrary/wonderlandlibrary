package my.NewSnake.Tank.module.modules.RENDER;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;

@Module.Mod
public class Personagem extends Module {
   @Option.Op(
      min = 0.0D,
      max = 100.0D,
      increment = 5.0D
   )
   public double Personagem;
   protected static Minecraft mc = Minecraft.getMinecraft();
   @Option.Op(
      min = 0.0D,
      max = 400.0D,
      increment = 10.0D
   )
   public double y;
   @Option.Op(
      min = 0.0D,
      max = 400.0D,
      increment = 10.0D
   )
   public double x;

   @EventTarget
   public void onRender2D(Render2DEvent var1) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int var2 = (int)this.x;
      int var3 = (int)this.y;
      int var4 = (int)this.Personagem;
      ClientUtils.mc();
      float var5 = Minecraft.thePlayer.rotationYaw * -1.0F;
      ClientUtils.mc();
      float var6 = Minecraft.thePlayer.rotationPitch * -1.0F;
      ClientUtils.mc();
      GuiInventory.drawEntityOnScreen((float)var2, (double)var3, var4, var5, var6, Minecraft.thePlayer);
   }
}
