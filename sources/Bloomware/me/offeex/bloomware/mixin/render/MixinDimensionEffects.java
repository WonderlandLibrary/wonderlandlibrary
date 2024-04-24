package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.module.visuals.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DimensionEffects.class)
public class MixinDimensionEffects {

    @Mixin(DimensionEffects.Overworld.class)
    private static class OverWorld {

        @ModifyVariable(method = "adjustFogColor", at = @At("HEAD"), index = 1, argsOnly = true)
        private Vec3d adjustFogColor(Vec3d value) {
            ColorMutable colorM = Environment.INSTANCE.getFogColor().getColor();
            return Environment.INSTANCE.getEnabled() && Environment.INSTANCE.getFog().getToggled()
                    ? new Vec3d(colorM.getRed() / 255f, colorM.getGreen() / 255f, colorM.getBlue() / 255f)
                    : value;
        }

    }
}
