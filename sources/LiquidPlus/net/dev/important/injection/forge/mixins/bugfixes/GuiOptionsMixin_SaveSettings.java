/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={GuiOptions.class})
public class GuiOptionsMixin_SaveSettings
extends GuiScreen {
    public void func_146281_b() {
        this.field_146297_k.field_71474_y.func_74303_b();
    }
}

