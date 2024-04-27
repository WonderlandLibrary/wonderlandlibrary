/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.apache.commons.lang3.SystemUtils
 *  org.lwjgl.opengl.Display
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_FixScreenState {
    @Shadow
    private boolean field_71431_Q;

    @Inject(method={"toggleFullscreen"}, at={@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;setFullscreen(Z)V", remap=false)})
    private void patcher$resolveScreenState(CallbackInfo ci) {
        if (!this.field_71431_Q && SystemUtils.IS_OS_WINDOWS) {
            Display.setResizable((boolean)false);
            Display.setResizable((boolean)true);
        }
    }
}

