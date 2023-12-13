// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Phase extends Module
{
    private final TimeHelper tickTimeHelper;
    
    public Phase() {
        super("Phase", new Color(45, 196, 148), Categorys.PLAYER);
        this.tickTimeHelper = new TimeHelper();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        Phase.mc.thePlayer.motionY = 0.0;
        Phase.mc.thePlayer.onGround = true;
    }
}
