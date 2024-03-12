// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.event.impl.render.HurtCamEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public class NoHurtCam extends Module
{
    public NoHurtCam() {
        super("NoHurtCam", "No Hurt Cam", Category.RENDER, "removes shaking after being hit");
    }
    
    @Override
    public void onHurtCamEvent(final HurtCamEvent e) {
        e.cancel();
    }
}
