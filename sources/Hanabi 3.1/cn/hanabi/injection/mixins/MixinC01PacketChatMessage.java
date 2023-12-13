package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IC01PacketChatMessage;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({C01PacketChatMessage.class})
public class MixinC01PacketChatMessage implements IC01PacketChatMessage {
   @Shadow
   private String message;

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String s) {
      this.message = s;
   }
}
