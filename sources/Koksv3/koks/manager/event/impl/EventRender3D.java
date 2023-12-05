package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 06:04
 */
public class EventRender3D extends Event {

    float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}