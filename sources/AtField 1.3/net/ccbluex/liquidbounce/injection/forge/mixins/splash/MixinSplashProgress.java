/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.client.SplashProgress
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.splash;

import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={SplashProgress.class}, remap=false)
public abstract class MixinSplashProgress {
    @Shadow(aliases={"SplashProgress"}, remap=false)
    private static boolean enabled;

    @Inject(method={"finish"}, at={@At(value="INVOKE", target="Lorg/lwjgl/opengl/Drawable;makeCurrent()V", shift=At.Shift.AFTER, remap=false, ordinal=0)}, remap=false, cancellable=true, require=1, allow=1)
    private static void finish(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }

    @Inject(method={"start"}, at={@At(value="FIELD", target="Lnet/minecraftforge/fml/client/SplashProgress;enabled:Z", opcode=178, remap=false, ordinal=0)}, remap=false, require=1, allow=1)
    private static void start(CallbackInfo callbackInfo) {
        enabled = true;
    }
}

