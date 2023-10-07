/**
 * Time: 12:26:49 AM
 * Date: Jan 2, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;

/**
 * @author cool1
 */
public class NoFall extends Module {

    /**
     * @param data
     */
    public NoFall(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate)event;
            if(em.isPre() && mc.thePlayer.fallDistance >= 1) {
                em.setGround(true);
            }
        }
    }

}
