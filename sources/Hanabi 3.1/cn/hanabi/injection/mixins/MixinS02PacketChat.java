package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS02PacketChat;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({S02PacketChat.class})
public class MixinS02PacketChat implements IS02PacketChat {
   @Shadow
   private IChatComponent chatComponent;

   public IChatComponent getChatComponent() {
      return this.chatComponent;
   }

   public void setChatComponent(IChatComponent i) {
      this.chatComponent = i;
   }
}
