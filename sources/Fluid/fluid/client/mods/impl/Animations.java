// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import fluid.client.mods.GuiMod;

public class Animations extends GuiMod
{
    public Animations() {
        super("Animations", "Uses 1.7 animations for sword");
        this.x = -100;
        this.drag.setxPosition(this.x);
        this.y = -100;
        this.drag.setyPosition(this.y);
        this.height = 0;
        this.drag.setHeight(this.height);
        this.width = 0;
        this.drag.setWidth(this.width);
    }
}
