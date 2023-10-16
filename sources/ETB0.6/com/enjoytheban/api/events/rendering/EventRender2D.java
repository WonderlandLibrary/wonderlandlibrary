package com.enjoytheban.api.events.rendering;

import com.enjoytheban.api.Event;

public class EventRender2D extends Event { 
	
	/**
	 * @author Purity
	 * @desc A 2D Render Event for Render related things
	 * @called GuiIngame func_175180_a
	 */
	
	//new variable called partial ticks (p_175180_1_ without mappings)
	private float partialTicks;
	/***
	 * issa constructor
	 * @param partialTicks
	 */
    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    //returns partialticks
    public float getPartialTicks() {
        return partialTicks;
    }

    //sets partical ticks
    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}