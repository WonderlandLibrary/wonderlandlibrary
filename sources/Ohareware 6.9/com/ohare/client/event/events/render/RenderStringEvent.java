package com.ohare.client.event.events.render;

import com.ohare.client.event.Event;

/**
 * made by Xen for OhareWare
 *
 * @since 6/11/2019
 **/
public class RenderStringEvent extends Event {
    private String text;

    public RenderStringEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
