package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventText;
import cn.hanabi.injection.interfaces.IFontRenderer;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({FontRenderer.class})
public abstract class MixinFontRenderer implements IFontRenderer {
   @Shadow
   private int[] colorCode;

   @ModifyVariable(
      method = {"renderString"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private String renderString(String string) {
      EventText textEvent = new EventText(string);
      EventManager.call(textEvent);
      return textEvent.getText();
   }

   @ModifyVariable(
      method = {"getStringWidth"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private String getStringWidth(String string) {
      EventText textEvent = new EventText(string);
      EventManager.call(textEvent);
      return textEvent.getText();
   }

   public int[] getColorCode() {
      return this.colorCode;
   }
}
