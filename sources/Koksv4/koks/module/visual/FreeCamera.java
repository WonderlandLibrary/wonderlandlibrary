package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.EntityRendererEvent;
import koks.event.MoveEntityEvent;
import koks.event.UpdateMotionEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "FreeCamera", description = "You can look around with your camera", category = Module.Category.VISUAL)
public class FreeCamera extends Module{

    EntityOtherPlayerMP livingBase;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof UpdateMotionEvent updateMotionEvent) {
            updateMotionEvent.setCurrentView(true);
        }
        if(event instanceof final MoveEntityEvent moveEntityEvent) {
            moveEntityEvent.setEntity(livingBase);
        }
        if(event instanceof EntityRendererEvent entityRendererEvent) {
            entityRendererEvent.setEntity(livingBase);
        }
    }

    @Override
    public void onEnable() {
        livingBase = createCopy(getPlayer());
        livingBase.setInvisible(true);
        getPlayer().setInvisible(false);

        getWorld().spawnEntityInWorld(livingBase);
        mc.renderViewEntity = livingBase;
    }

    @Override
    public void onDisable() {
        getWorld().removeEntity(livingBase);
        mc.renderViewEntity = getPlayer();
    }
}
