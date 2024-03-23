/*
what fucking screenshare only checks your latest log - thnkscj
*/
package dev.zprestige.prestige.client.bypass;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.RunEvent;
import dev.zprestige.prestige.client.util.impl.TimerUtil;

import java.util.ArrayList;
import java.util.List;

public class ScreenshareBypass {

    public ArrayList<Bypass> bypasses = new ArrayList();
    public TimerUtil timer = new TimerUtil();

    public ScreenshareBypass() {
        Prestige.Companion.getEventBus().registerListener(this);
    }

    public void setList(List list) {
        this.bypasses = new ArrayList(list);
    }

    @EventListener
    public void event(RunEvent event) {
        if (timer.delay(10000)) {
            bypasses.forEach(Bypass::run);
            timer.reset();
        }
    }
}
