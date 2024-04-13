package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.MoveEvent;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;

public class FastStop extends Module {
    public FastStop(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onMove(MoveEvent event){
        if(!PlayerUtils.isMoving()){
            event.setX(0);
            event.setZ(0);
        }
    }
}
