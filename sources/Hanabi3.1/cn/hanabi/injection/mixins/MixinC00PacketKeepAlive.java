package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IC00PacketKeepAlive;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({C00PacketKeepAlive.class})
public class MixinC00PacketKeepAlive implements IC00PacketKeepAlive {
   @Shadow
   private int key;

   public int getKey() {
      return this.key;
   }

   public void setKey(int b) {
      this.key = b;
   }
}
