// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import moonsense.event.SCEventCancellable;

public class SCGuiScreenPreRenderEvent extends SCEventCancellable
{
    public final int mx;
    public final int my;
    public final float partialTicks;
    
    public SCGuiScreenPreRenderEvent(final int mx, final int my, final float partialTicks) {
        this.mx = mx;
        this.my = my;
        this.partialTicks = partialTicks;
    }
}
