/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.tabs;

import com.wallhacks.losebypass.utils.ColorAnimation;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;

public abstract class ClickGuiTab {
    public ColorAnimation colorAnimation = new ColorAnimation(new Color(0x959595), 0.02f);
    public String name;
    public ResourceLocation icon;

    public ClickGuiTab(String name, ResourceLocation icon) {
        this.name = name;
        this.icon = icon;
    }

    public abstract void drawTab(int var1, int var2, int var3, int var4, int var5, double var6);

    public abstract void keyTyped(char var1, int var2);

    public void init() {
    }
}

