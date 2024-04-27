/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiLanguage
 *  net.minecraft.client.gui.GuiScreen
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={GuiLanguage.class})
public class GuiLanguageMixin_ResetUnicodeFont
extends GuiScreen {
    public void func_146281_b() {
        this.field_146297_k.field_71456_v.func_146158_b().func_146245_b();
    }
}

