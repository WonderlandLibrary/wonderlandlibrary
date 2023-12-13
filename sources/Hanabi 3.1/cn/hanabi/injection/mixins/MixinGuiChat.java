package cn.hanabi.injection.mixins;

import cn.hanabi.Hanabi;
import cn.hanabi.modules.ModManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin({GuiChat.class})
public abstract class MixinGuiChat extends MixinGuiScreen {
   @Shadow
   private boolean waitingOnAutocomplete;

   @Shadow
   public abstract void onAutocompleteResponse(String[] var1);

   @Inject(
      method = {"sendAutocompleteRequest"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V",
   shift = At.Shift.BEFORE
)},
      cancellable = true
   )
   private void autocomplete(String cmd, String p_146405_2_, @NotNull CallbackInfo ci) {
      if (cmd.startsWith(".") && !ModManager.getModule("NoCommand").isEnabled()) {
         String[] ls = (String[])Hanabi.INSTANCE.commandManager.autoComplete(cmd).toArray(new String[0]);
         if (ls.length == 0 || cmd.toLowerCase().endsWith(ls[ls.length - 1].toLowerCase())) {
            return;
         }

         this.waitingOnAutocomplete = true;
         this.onAutocompleteResponse(ls);
         ci.cancel();
      }

   }

   @Inject(
      method = {"drawScreen"},
      at = {@At("HEAD")}
   )
   public void mouse(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
      Hanabi.INSTANCE.hudWindowMgr.mouseCoordinateUpdate(mouseX, mouseY);
   }

   @Inject(
      method = {"updateScreen"},
      at = {@At("HEAD")}
   )
   public void updateScr(CallbackInfo info) {
      Hanabi.INSTANCE.hudWindowMgr.updateScreen();
   }

   @Inject(
      method = {"handleMouseInput"},
      at = {@At("RETURN")}
   )
   public void handleMouseInput(CallbackInfo info) {
      Hanabi.INSTANCE.hudWindowMgr.handleMouseInput(this.width, this.height);
   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At("HEAD")}
   )
   public void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo info) {
      Hanabi.INSTANCE.hudWindowMgr.mouseClick(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      Hanabi.INSTANCE.hudWindowMgr.mouseRelease(mouseX, mouseY, state);
      super.mouseReleased(mouseX, mouseY, state);
   }
}
