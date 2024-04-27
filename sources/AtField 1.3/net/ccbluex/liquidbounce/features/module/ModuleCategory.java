/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Main;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects.Drag;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.Scroll;

public enum ModuleCategory {
    COMBAT("Combat"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World"),
    MISC("Misc"),
    EXPLOIT("Exploit"),
    HYT("Hyt"),
    COLOR("Color"),
    ATFIELD("AtField"),
    AUTUMN("Autumn");

    public final boolean expanded;
    public final int posX;
    private final Drag drag;
    private final Scroll scroll = new Scroll();
    public final String namee;
    public int posY = 20;

    public Drag getDrag() {
        return this.drag;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private ModuleCategory() {
        void var3_1;
        this.namee = var3_1;
        this.posX = 40 + Main.categoryCount * 120;
        this.drag = new Drag(this.posX, this.posY);
        this.expanded = true;
        ++Main.categoryCount;
    }

    public Scroll getScroll() {
        return this.scroll;
    }

    public String getDisplayName() {
        return this.namee;
    }
}

