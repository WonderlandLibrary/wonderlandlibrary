// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.client.gui.GuiScreen;
import moonsense.event.SCEvent;

public class SCGuiChangedEvent extends SCEvent
{
    public final GuiScreen theGUI;
    public final GuiScreen thePreviousGUI;
    
    public SCGuiChangedEvent(final GuiScreen theGUI, final GuiScreen thePreviousGUI) {
        this.theGUI = theGUI;
        this.thePreviousGUI = thePreviousGUI;
    }
}
