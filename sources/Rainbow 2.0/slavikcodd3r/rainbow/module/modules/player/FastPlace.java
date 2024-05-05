package slavikcodd3r.rainbow.module.modules.player;

import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod(displayName = "FastPlace")
public class FastPlace extends Module
{
    @Option.Op(name = "Half")
    private boolean half;
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            ClientUtils.mc().rightClickDelayTimer = Math.min(ClientUtils.mc().rightClickDelayTimer, this.half ? 2 : 1);
            if (this.half) {
            	this.setSuffix("Half");
            }
        }
    }
}
