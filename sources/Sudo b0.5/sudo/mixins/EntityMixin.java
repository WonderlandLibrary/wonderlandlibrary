package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sudo.module.ModuleManager;
import sudo.module.combat.Hitbox;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World world;

    @Shadow public abstract BlockPos getBlockPos();
    @Shadow protected abstract BlockPos getVelocityAffectingPos();
    
    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    private void onGetTargetingMargin(CallbackInfoReturnable<Float> info) {
        double v = ModuleManager.INSTANCE.getModule(Hitbox.class).size.getValue();
        if (v != 0 && ModuleManager.INSTANCE.getModule(Hitbox.class).isEnabled()) info.setReturnValue((float) v);
    }
}
