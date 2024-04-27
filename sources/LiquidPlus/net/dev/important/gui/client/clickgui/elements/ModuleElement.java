/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.gui.client.clickgui.elements;

import net.dev.important.Client;
import net.dev.important.gui.client.clickgui.elements.ButtonElement;
import net.dev.important.modules.module.Module;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class ModuleElement
extends ButtonElement {
    private final Module module;
    private boolean showSettings;
    private float settingsWidth = 0.0f;
    private boolean wasPressed;
    public int slowlySettingsYPos;
    public int slowlyFade;

    public ModuleElement(Module module2) {
        super(null);
        this.displayName = module2.getName();
        this.module = module2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float button) {
        Client.clickGui.style.drawModuleElement(mouseX, mouseY, this);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
            this.module.toggle();
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
            this.showSettings = !this.showSettings;
            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
        }
    }

    public Module getModule() {
        return this.module;
    }

    public boolean isShowSettings() {
        return this.showSettings;
    }

    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    public boolean isntPressed() {
        return !this.wasPressed;
    }

    public void updatePressed() {
        this.wasPressed = Mouse.isButtonDown((int)0);
    }

    public float getSettingsWidth() {
        return this.settingsWidth;
    }

    public void setSettingsWidth(float settingsWidth) {
        this.settingsWidth = settingsWidth;
    }
}

