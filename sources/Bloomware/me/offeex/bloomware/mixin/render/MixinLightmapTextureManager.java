package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.module.visuals.FullBright;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"), index = 2)
    private int fullBright(int value) {
        ColorMutable colorM = FullBright.INSTANCE.getColor().getColor();
        int argb = colorM.getArgb();
        int a = (argb >> 24) & 0xFF; // get pixel bytes in ARGB order
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb) & 0xFF;
        int abgr = (a << 24) | (b << 16) | (g << 8) | (r);

        return FullBright.INSTANCE.getEnabled() && FullBright.INSTANCE.getMode().like("Lightmap") ? abgr : value;
    }
}
