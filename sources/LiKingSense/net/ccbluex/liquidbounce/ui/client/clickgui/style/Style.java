/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style;

import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public abstract class Style
extends MinecraftInstance {
    public abstract void drawPanel(int var1, int var2, Panel var3);

    public abstract void drawDescription(int var1, int var2, String var3);

    public abstract void drawButtonElement(int var1, int var2, ButtonElement var3);

    public abstract void drawModuleElement(int var1, int var2, ModuleElement var3);
}

