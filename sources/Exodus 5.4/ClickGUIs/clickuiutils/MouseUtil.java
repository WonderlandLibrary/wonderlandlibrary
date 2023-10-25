/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package ClickGUIs.clickuiutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public enum MouseUtil {
    INSTANCE;


    public int getMouseX() {
        return Mouse.getX() * this.sr().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    private ScaledResolution sr() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public int getMouseY() {
        int n = this.sr().getScaledHeight();
        int n2 = Mouse.getY() * this.sr().getScaledHeight();
        Minecraft.getMinecraft();
        return n - n2 / Minecraft.displayHeight - 1;
    }

    public boolean isHovered(float f, float f2, float f3, float f4) {
        return (float)this.getMouseX() > f && (float)this.getMouseX() < f3 && (float)this.getMouseY() > f2 && (float)this.getMouseY() < f4;
    }
}

