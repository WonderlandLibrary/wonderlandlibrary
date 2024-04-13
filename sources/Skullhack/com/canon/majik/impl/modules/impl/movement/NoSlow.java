package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.MoveEvent;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;

public class NoSlow extends Module {
    public NoSlow(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onMove(MoveEvent event){
        if(nullCheck()) return;
        float[] direction = !PlayerUtils.isMoving() ? new float[]{0.0f, 0.0f} : PlayerUtils.strafe(PlayerUtils.getBaseMoveSpeed());
        event.setX(direction[0]);
        event.setZ(direction[1]);
    }
}
