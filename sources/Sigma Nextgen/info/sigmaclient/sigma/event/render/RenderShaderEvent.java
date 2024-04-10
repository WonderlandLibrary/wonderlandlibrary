package info.sigmaclient.sigma.event.render;

import info.sigmaclient.sigma.event.Event;

public class RenderShaderEvent extends Event {
    public float renderTime;
    public RenderShaderEvent(float renderTime){
        this.eventID = 4;
        this.renderTime = renderTime;
    }
}
