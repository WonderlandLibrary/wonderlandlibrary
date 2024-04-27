/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_ToggleGLErrorChecking {
    @Shadow
    private boolean field_175619_R;

    @Inject(method={"startGame"}, at={@At(value="TAIL")})
    private void patcher$disableGlErrorChecking(CallbackInfo ci) {
        this.field_175619_R = false;
    }
}

