package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventClientTick;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.manager.config.Cheats;

public class TimeChanger extends Cheats {

    public static Setting time;

    public TimeChanger() {
        super("TimeChanger", "Changes time client sided", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(time = new Setting("Time", this, 20000F, 20000F, 28000F, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        long timer = time.getValLong();
        mc.theWorld.setWorldTime(timer);
    }

    @EventTarget
    public void onTick(EventClientTick e) {
        long timer = time.getValLong();
        mc.theWorld.setWorldTime(timer);
    }
}
