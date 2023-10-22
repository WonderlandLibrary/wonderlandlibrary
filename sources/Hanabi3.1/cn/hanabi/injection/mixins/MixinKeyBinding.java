package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({KeyBinding.class})
public class MixinKeyBinding implements IKeyBinding {
   @Shadow
   private boolean pressed;

   public boolean getPress() {
      return this.pressed;
   }

   public void setPress(Boolean b) {
      this.pressed = b;
   }
}
