// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.module.Category;
import dev.tenacity.event.impl.player.SafeWalkEvent;
import dev.tenacity.module.Module;

public final class SafeWalk extends Module
{
    @Override
    public void onSafeWalkEvent(final SafeWalkEvent e) {
        if (SafeWalk.mc.thePlayer == null) {
            return;
        }
        e.setSafe(true);
    }
    
    public SafeWalk() {
        super("SafeWalk", "Safe Walk", Category.PLAYER, "prevents walking off blocks");
    }
}
