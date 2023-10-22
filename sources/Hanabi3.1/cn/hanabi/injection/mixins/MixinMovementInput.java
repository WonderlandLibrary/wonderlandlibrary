package cn.hanabi.injection.mixins;

import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({MovementInput.class})
public class MixinMovementInput {
   @Shadow
   public float moveStrafe;
   @Shadow
   public float moveForward;
   @Shadow
   public boolean jump;
   @Shadow
   public boolean sneak;
}
