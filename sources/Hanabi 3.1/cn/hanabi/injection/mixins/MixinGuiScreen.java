package cn.hanabi.injection.mixins;

import cn.hanabi.Hanabi;
import cn.hanabi.modules.ModManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin({GuiScreen.class})
public abstract class MixinGuiScreen {
   @Shadow
   public Minecraft mc;
   @Shadow
   public int width;
   @Shadow
   public int height;

   @Inject(
      method = {"sendChatMessage(Ljava/lang/String;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChat(String msg, boolean addToChat, @NotNull CallbackInfo ci) {
      if (msg.startsWith(".") && msg.length() > 1 && !ModManager.getModule("NoCommand").isEnabled()) {
         if (Hanabi.INSTANCE.commandManager.executeCommand(msg)) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
         }

         ci.cancel();
      }

   }

   @Shadow
   protected void mouseReleased(int mouseX, int mouseY, int state) {
   }
}
