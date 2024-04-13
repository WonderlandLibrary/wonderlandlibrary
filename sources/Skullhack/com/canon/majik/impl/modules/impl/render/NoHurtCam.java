package com.canon.majik.impl.modules.impl.render;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;

public class NoHurtCam extends Module {
    public NoHurtCam(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
    }
}
