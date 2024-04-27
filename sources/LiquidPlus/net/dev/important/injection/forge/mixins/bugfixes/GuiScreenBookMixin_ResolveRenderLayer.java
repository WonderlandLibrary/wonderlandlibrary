/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiScreenBook
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreenBook.class})
public abstract class GuiScreenBookMixin_ResolveRenderLayer
extends GuiScreen {
    @Inject(method={"drawScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiScreenBook;handleComponentHover(Lnet/minecraft/util/IChatComponent;II)V")})
    private void patcher$callSuper(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    @Inject(method={"drawScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiScreenBook;handleComponentHover(Lnet/minecraft/util/IChatComponent;II)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void patcher$cancelFurtherRendering(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();
    }
}

