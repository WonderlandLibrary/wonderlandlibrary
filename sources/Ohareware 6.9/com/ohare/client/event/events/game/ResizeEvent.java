package com.ohare.client.event.events.game;

import com.ohare.client.event.Event;

import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for Client
 *
 * @since 5/25/2019
 **/
public class ResizeEvent extends Event {
    private ScaledResolution sr;

    public ResizeEvent(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getSr() {
        return sr;
    }
}
