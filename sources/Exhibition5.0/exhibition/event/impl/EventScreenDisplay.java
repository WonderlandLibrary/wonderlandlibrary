// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.client.gui.GuiScreen;
import exhibition.event.Event;

public class EventScreenDisplay extends Event
{
    private GuiScreen screen;
    
    public void fire(final GuiScreen screen) {
        this.screen = screen;
        super.fire();
    }
    
    public GuiScreen getGuiScreen() {
        return this.screen;
    }
}
