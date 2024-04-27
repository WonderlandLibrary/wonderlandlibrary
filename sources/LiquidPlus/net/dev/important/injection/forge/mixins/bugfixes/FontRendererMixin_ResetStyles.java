/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={FontRenderer.class})
public abstract class FontRendererMixin_ResetStyles {
    @Shadow
    protected abstract void func_78265_b();

    @Inject(method={"drawString(Ljava/lang/String;FFIZ)I"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I", ordinal=0, shift=At.Shift.AFTER)})
    private void patcher$resetStyle(CallbackInfoReturnable<Integer> ci) {
        this.func_78265_b();
    }
}

