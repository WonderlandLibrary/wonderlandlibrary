/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import net.dev.important.patcher.screen.ResolutionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public class GuiScreenMixin_ResolveCrash {
    @Shadow
    public Minecraft field_146297_k;

    @Inject(method={"handleInput"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiScreen;handleKeyboardInput()V")}, cancellable=true)
    private void patcher$checkScreen(CallbackInfo ci) {
        if ((GuiScreen)this != this.field_146297_k.field_71462_r) {
            ResolutionHelper.setScaleOverride(-1);
            ci.cancel();
        }
    }
}

