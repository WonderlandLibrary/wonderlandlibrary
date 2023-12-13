package cn.hanabi.injection.mixins;

import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.world.UhcHelper;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MovementInputFromOptions.class})
public class MixinMovementInputFromOptions extends MixinMovementInput {
   @Inject(
      method = {"updatePlayerMoveState"},
      at = {@At("RETURN")}
   )
   public void updatePlayerMoveState(CallbackInfo info) {
      if (((UhcHelper)ModManager.getModule(UhcHelper.class)).isEnabled() && (Boolean)((UhcHelper)ModManager.getModule(UhcHelper.class)).sneakily.getValue() && this.sneak) {
         this.moveStrafe = (float)((double)this.moveStrafe / 0.3);
         this.moveForward = (float)((double)this.moveForward / 0.3);
      }

   }
}
