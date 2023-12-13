package wtf.diablo.events.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import wtf.diablo.events.Event;

@Getter@Setter
public class OverlayEvent extends Event {
    private ScaledResolution sr;

    public OverlayEvent(ScaledResolution sr){
        this.sr = sr;
    }

    public ScaledResolution getScaledResolution(){
        return sr;
    }
}
