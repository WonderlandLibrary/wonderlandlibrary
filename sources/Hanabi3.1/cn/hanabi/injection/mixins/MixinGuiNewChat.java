package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventChat;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiNewChat.class})
public class MixinGuiNewChat {
   @Inject(
      method = {"printChatMessageWithOptionalDeletion"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void eventchat(IChatComponent p_146234_1_, int p_146234_2_, CallbackInfo ci) {
      EventChat event = new EventChat(p_146234_1_.getUnformattedText(), p_146234_1_);
      EventManager.call(event);
      if (event.cancelled) {
         ci.cancel();
      }

   }
}
