package com.canon.majik.impl.modules.impl.render;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;

public class FullBright extends Module {
    public FullBright(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        mc.gameSettings.gammaSetting = 10000f;
    }
}
