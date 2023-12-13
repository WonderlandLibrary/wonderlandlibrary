package cn.hanabi.injection.mixins;

import net.minecraft.client.resources.DefaultPlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DefaultPlayerSkin.class})
public class MixinDefaultPlayerSkin {
   @Inject(
      method = {"getDefaultSkin"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getDefaultSkin(CallbackInfoReturnable callbackInfoReturnable) {
   }
}
