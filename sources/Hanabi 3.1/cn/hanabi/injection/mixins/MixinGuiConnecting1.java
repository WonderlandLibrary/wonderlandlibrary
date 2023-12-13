package cn.hanabi.injection.mixins;

import me.yarukon.hud.window.HudWindowManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.client.multiplayer.GuiConnecting$1"}
)
public class MixinGuiConnecting1 {
   @Inject(
      method = {"run"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V",
   ordinal = 1,
   shift = At.Shift.AFTER
)}
   )
   private void connect(CallbackInfo ci) {
      HudWindowManager.startTime = System.currentTimeMillis();
   }
}
