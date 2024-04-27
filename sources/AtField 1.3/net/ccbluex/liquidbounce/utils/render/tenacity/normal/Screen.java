/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.tenacity.normal;

import net.ccbluex.liquidbounce.utils.render.tenacity.normal.Utils;

public interface Screen
extends Utils {
    public void mouseClicked(int var1, int var2, int var3);

    public void drawScreen(int var1, int var2);

    public void keyTyped(char var1, int var2);

    public void initGui();

    public void mouseReleased(int var1, int var2, int var3);
}

