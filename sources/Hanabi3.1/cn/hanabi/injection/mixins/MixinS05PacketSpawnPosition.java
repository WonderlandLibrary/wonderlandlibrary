package cn.hanabi.injection.mixins;

import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({S05PacketSpawnPosition.class})
public class MixinS05PacketSpawnPosition {
   @Inject(
      method = {"processPacket"},
      at = {@At("RETURN")}
   )
   public void processPacket(INetHandlerPlayClient p_processPacket_1_, CallbackInfo callbackInfo) {
   }
}
