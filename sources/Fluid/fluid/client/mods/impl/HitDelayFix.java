// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.impl.EventConnect;
import fluid.client.mods.GuiMod;

public class HitDelayFix extends GuiMod
{
    public HitDelayFix() {
        super("HitDelayFix", "Fixes the hitdelay");
        this.x = -100;
        this.drag.setxPosition(this.x);
        this.y = -100;
        this.drag.setyPosition(this.y);
        this.height = 0;
        this.drag.setHeight(this.height);
        this.width = 0;
        this.drag.setWidth(this.width);
    }
    
    @EventTarget
    public void onConnect(final EventConnect e) {
        if (e.ip.endsWith("hypixel.net")) {
            this.toggle();
        }
    }
}
