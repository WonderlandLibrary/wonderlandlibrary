package com.ohare.client.module.modules.other;

import java.awt.Color;

import com.ohare.client.event.events.game.TickEvent;
import com.ohare.client.module.Module;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.entity.item.EntityItem;

/**
 * made by Xen for OhareWare
 *
 * @since 6/10/2019
 **/
public class NoRender extends Module {

    public NoRender() {
        super("NoRender", Category.OTHER, new Color(0).getRGB());
        setDescription("Dont render items");
        setRenderlabel("No Render");
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.theWorld != null) {
            mc.theWorld.loadedEntityList.forEach(e -> {
            	if (e instanceof EntityItem) {
            		mc.theWorld.removeEntity(e);
            	}
            });
        }
    }
}
