// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.misc;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventWorld;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Fixes extends Module
{
    public final BooleanValue mouseDelayFix;
    public final BooleanValue hitDelayFix;
    public final BooleanValue memoryFix;
    public final TimeHelper timeHelper;
    
    public Fixes() {
        super("Fixes", new Color(169, 66, 237), Categorys.MISC);
        this.mouseDelayFix = new BooleanValue(1, "MouseDelayFix", this, true);
        this.hitDelayFix = new BooleanValue(2, "HitDelayFix", this, false);
        this.memoryFix = new BooleanValue(2, "MemoryFix", this, false);
        this.timeHelper = new TimeHelper();
    }
    
    @EventTarget
    public void onWorldChange(final EventWorld eventWorld) {
        System.gc();
    }
}
