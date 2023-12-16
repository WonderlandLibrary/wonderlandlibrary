// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import fluid.client.mods.GuiMod;

public class Crosshair extends GuiMod
{
    public Crosshair() {
        super("Crosshair", "Makes your crosshair red if target isnt null");
        this.x = -10;
        this.drag.setxPosition(this.x);
        this.y = -10;
        this.drag.setyPosition(this.y);
        this.height = this.fr.FONT_HEIGHT;
        this.drag.setHeight(this.height);
        this.width = 0;
        this.drag.setWidth(this.width);
    }
}
