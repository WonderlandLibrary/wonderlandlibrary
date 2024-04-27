/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiIngame
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value={GuiIngame.class})
public class GuiIngameMixin_ScoreboardTextTransparency {
    @ModifyConstant(method={"renderScoreboard"}, constant={@Constant(intValue=0x20FFFFFF)})
    private int patcher$fixTextBlending(int original) {
        return -1;
    }
}

