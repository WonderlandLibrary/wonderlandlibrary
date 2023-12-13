package cn.hanabi.injection.mixins;

import cn.hanabi.Hanabi;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.HUD;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin({GuiIngame.class})
public class MixinGuiIngame {
   @Inject(
      method = {"renderTooltip"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
      EventManager.call(new EventRender2D(partialTicks));
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      if (ModManager.getModule("HUD").isEnabled() && (Boolean)((HUD)ModManager.getModule("HUD")).hotbar.getValueState()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderScoreboard"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void customBoard(ScoreObjective so, ScaledResolution sr, CallbackInfo info) {
      if (Hanabi.INSTANCE.customScoreboard) {
         info.cancel();
      }

   }
}
