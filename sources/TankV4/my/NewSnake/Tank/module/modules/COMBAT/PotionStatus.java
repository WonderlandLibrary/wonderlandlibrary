package my.NewSnake.Tank.module.modules.COMBAT;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Collection;
import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Module.Mod(
   displayName = "Potion Status"
)
public class PotionStatus extends Module {
   public Minecraft mc = Minecraft.getMinecraft();
   @Option.Op(
      min = 0.0D,
      max = 810.0D,
      increment = 0.25D
   )
   float y;
   @Option.Op(
      min = 0.0D,
      max = 810.0D,
      increment = 0.25D
   )
   float x;

   @EventTarget
   public void onRender(Render2DEvent var1) {
      int var2 = var1.getWidth() / 2 + 9;
      GL11.glPushMatrix();
      boolean var4 = true;
      boolean var5 = true;
      Minecraft.getMinecraft();
      Collection var6 = Minecraft.thePlayer.getActivePotionEffects();
      int var3 = 0;
      if (!var6.isEmpty()) {
         Minecraft.getMinecraft();
         Iterator var7 = Minecraft.thePlayer.getActivePotionEffects().iterator();

         while(var7.hasNext()) {
            Object var8 = var7.next();
            PotionEffect var9 = (PotionEffect)var8;
            Potion var10 = Potion.potionTypes[var9.getPotionID()];
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
            if (var10.hasStatusIcon()) {
               int var11 = var10.getStatusIconIndex();
               Gui.drawTexturedModalRect((int)this.x, (int)this.y + 18 * var3, var11 % 8 * 18, 198 + var11 / 8 * 18, 18, 18);
               ClientUtils.clientFont().drawStringWithShadow((var9.getDuration() <= 300 ? ChatFormatting.RED : ChatFormatting.WHITE) + Potion.getDurationString(var9), (double)(this.x - (float)ClientUtils.clientFont().getStringWidth(Potion.getDurationString(var9))) - 5.0D, (double)(this.y + (float)(18 * var3)) + 6.0D, 10);
               ++var3;
            }
         }
      }

      GL11.glPopMatrix();
   }
}
