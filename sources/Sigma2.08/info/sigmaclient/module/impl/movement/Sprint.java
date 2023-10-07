package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.PlayerUtil;

public class Sprint extends Module {
    public Sprint(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate)event;
        if (em.isPre() && canSprint()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    private boolean canSprint() {
        return PlayerUtil.isMoving() && mc.thePlayer.getFoodStats().getFoodLevel() > 6;
    }

}
