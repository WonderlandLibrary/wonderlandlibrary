// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.util.render.CompassUtil;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Compass extends Module
{
    public Compass() {
        super("Compass", 0, "Displays a ingame compass", Category.RENDER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender2D) {
            final CompassUtil cpass = new CompassUtil(325.0f, 325.0f, 1.0f, 2, true);
            final ScaledResolution sc = new ScaledResolution(Compass.mc);
            cpass.draw(sc);
        }
    }
}
