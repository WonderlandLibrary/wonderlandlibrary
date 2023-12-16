// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import com.darkmagician6.eventapi.events.impl.EventRenderDummy;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.impl.EventRender;
import fluid.client.mods.GuiMod;

public class TestMod extends GuiMod
{
    public TestMod() {
        super("TestMod", "TestMod");
        this.x = 50;
        this.drag.setxPosition(this.x);
        this.y = 50;
        this.drag.setyPosition(this.y);
        this.height = this.fr.FONT_HEIGHT;
        this.drag.setHeight(this.height);
        this.width = this.fr.getStringWidth("test");
        this.drag.setWidth(this.width);
    }
    
    @EventTarget
    public void onRender(final EventRender e) {
        this.fr.drawStringWithShadow("test", (float)this.getX(), (float)this.getY(), -1);
    }
    
    @EventTarget
    public void onDummy(final EventRenderDummy e) {
        this.fr.drawStringWithShadow("test", (float)this.getX(), (float)this.getY(), -1);
        this.drag.draw(e.x, e.y);
    }
}
