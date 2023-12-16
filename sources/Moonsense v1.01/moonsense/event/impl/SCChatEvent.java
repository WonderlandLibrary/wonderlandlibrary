// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.util.IChatComponent;
import moonsense.event.SCEventCancellable;

public class SCChatEvent extends SCEventCancellable
{
    public final IChatComponent component;
    public final int lineNumber;
    
    public SCChatEvent(final IChatComponent component, final int lineNumber) {
        this.component = component;
        this.lineNumber = lineNumber;
    }
}
