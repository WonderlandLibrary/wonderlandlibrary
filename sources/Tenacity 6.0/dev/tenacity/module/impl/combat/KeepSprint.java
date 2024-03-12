// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import dev.tenacity.event.impl.player.KeepSprintEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public final class KeepSprint extends Module
{
    public KeepSprint() {
        super("KeepSprint", "Keep Sprint", Category.COMBAT, "Stops sprint reset after hitting");
    }
    
    @Override
    public void onKeepSprintEvent(final KeepSprintEvent event) {
        event.cancel();
    }
}
