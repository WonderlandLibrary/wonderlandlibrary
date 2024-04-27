/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.gui.client.clickgui.style;

import net.dev.important.gui.client.clickgui.Panel;
import net.dev.important.gui.client.clickgui.elements.ButtonElement;
import net.dev.important.gui.client.clickgui.elements.ModuleElement;
import net.dev.important.utils.MinecraftInstance;

public abstract class Style
extends MinecraftInstance {
    public abstract void drawPanel(int var1, int var2, Panel var3);

    public abstract void drawDescription(int var1, int var2, String var3);

    public abstract void drawButtonElement(int var1, int var2, ButtonElement var3);

    public abstract void drawModuleElement(int var1, int var2, ModuleElement var3);
}

