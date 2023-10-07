// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoEat extends Module
{
    Timer timer;
    
    public AutoEat(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (AutoEat.mc.thePlayer.getFoodStats().getFoodLevel() < 20 && this.timer.delay(2000.0f)) {
            AutoEat.mc.thePlayer.sendChatMessage("/eat");
            this.timer.reset();
        }
        if (this.timer.delay(60000.0f)) {
            AutoEat.mc.thePlayer.sendChatMessage("/eat");
            this.timer.reset();
        }
    }
}
