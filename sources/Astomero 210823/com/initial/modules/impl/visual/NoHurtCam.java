package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.*;
import com.initial.events.impl.*;

public class NoHurtCam extends Module
{
    public NoHurtCam() {
        super("NoHurtCam", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void onEvent(final EventUpdate e) {
        this.setDisplayName("No Hurt Cam");
    }
    
    @EventTarget
    public void Hurtcam(final EventHurt event) {
        event.setCancelled(true);
    }
}
