/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class})
public abstract class GuiContainerMixin_SplitRemnants
extends GuiScreen {
    @Shadow
    private int field_146988_G;
    @Shadow
    private int field_146996_I;

    @Inject(method={"updateDragSplitting"}, at={@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;")}, cancellable=true)
    private void patcher$fixRemnants(CallbackInfo ci) {
        if (this.field_146988_G == 2) {
            this.field_146996_I = this.field_146297_k.field_71439_g.field_71071_by.func_70445_o().func_77976_d();
            ci.cancel();
        }
    }
}

