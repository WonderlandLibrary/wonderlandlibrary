/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiVideoSettings
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.dev.important.patcher.ducks.GameSettingsExt;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={GuiVideoSettings.class})
public abstract class GuiVideoSettingsMixin_MipmapSlider
extends GuiScreen {
    public void func_146281_b() {
        super.func_146281_b();
        ((GameSettingsExt)this.field_146297_k.field_71474_y).patcher$onSettingsGuiClosed();
    }
}

