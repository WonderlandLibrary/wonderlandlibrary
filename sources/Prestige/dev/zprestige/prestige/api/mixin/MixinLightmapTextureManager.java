package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.LightEvent;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={LightmapTextureManager.class})
public class MixinLightmapTextureManager {
    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    void update(Args args) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new LightEvent().invoke()) {
            args.set(2, -1);
        }
    }
}
