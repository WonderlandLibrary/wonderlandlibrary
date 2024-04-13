package com.canon.majik.impl.modules.impl.misc;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.MultiTaskEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;

public class MultiTask extends Module {
    public MultiTask(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTask(MultiTaskEvent event){
        if(nullCheck()) return;
        event.setCancelled(true);
    }
}
