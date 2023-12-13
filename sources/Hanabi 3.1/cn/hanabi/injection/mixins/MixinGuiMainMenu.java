package cn.hanabi.injection.mixins;

import me.yarukon.mainmenu.GuiCustomMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiMainMenu.class})
public class MixinGuiMainMenu {
   @Inject(
      method = {"initGui"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onInit(CallbackInfo ci) {
      Minecraft.getMinecraft().displayGuiScreen(new GuiCustomMainMenu());
      ci.cancel();
   }
}
