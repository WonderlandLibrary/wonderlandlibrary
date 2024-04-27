/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiGameOver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiGameOver.class})
public class GuiGameOverMixin_ResolveButtonClick {
    @Shadow
    private int field_146347_a;

    @Inject(method={"initGui"}, at={@At(value="HEAD")})
    private void patcher$allowClickable(CallbackInfo ci) {
        this.field_146347_a = 0;
    }
}

