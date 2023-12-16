// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.client.Minecraft;
import moonsense.event.SCEvent;

public class SCUpdateEvent extends SCEvent
{
    @Override
    public SCEvent call() {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) {
            return this;
        }
        return super.call();
    }
}
