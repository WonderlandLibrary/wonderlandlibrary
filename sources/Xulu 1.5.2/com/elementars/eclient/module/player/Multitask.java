// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.AllowInteractEvent;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class Multitask extends Module
{
    public Multitask() {
        super("Multitask", "Credit to luchadora client", 0, Category.PLAYER, true);
    }
    
    @EventTarget
    public void onUseItem(final AllowInteractEvent event) {
        event.usingItem = false;
    }
}
