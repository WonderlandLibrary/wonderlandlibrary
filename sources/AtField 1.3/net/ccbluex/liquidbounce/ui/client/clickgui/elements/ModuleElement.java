/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class ModuleElement
extends ButtonElement {
    private boolean wasPressed;
    private final Module module;
    private boolean showSettings;
    private float settingsWidth = 0.0f;
    public int slowlySettingsYPos;
    public int slowlyFade;

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
            this.module.toggle();
            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
        }
        if (n3 == 1 && this.isHovering(n, n2) && this.isVisible()) {
            this.showSettings = !this.showSettings;
            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
        }
    }

    public ModuleElement(Module module) {
        super(null);
        this.displayName = module.getName();
        this.module = module;
    }

    public void setSettingsWidth(float f) {
        this.settingsWidth = f;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        LiquidBounce.clickGui.style.drawModuleElement(n, n2, this);
    }

    public void setShowSettings(boolean bl) {
        this.showSettings = bl;
    }

    public void updatePressed() {
        this.wasPressed = Mouse.isButtonDown((int)0);
    }

    public float getSettingsWidth() {
        return this.settingsWidth;
    }

    public boolean isShowSettings() {
        return this.showSettings;
    }

    public boolean isntPressed() {
        return !this.wasPressed;
    }
}

