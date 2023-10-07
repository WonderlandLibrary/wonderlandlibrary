// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import java.util.HashMap;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class FastPlace extends Module
{
    private static final String KEY_TIMES = "CLICKSPEED";
    
    public FastPlace(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Integer>>)this.settings).put("CLICKSPEED", new Setting<Integer>("CLICKSPEED", 4, "Tick delay between clicks.", 1.0, 0.0, 20.0));
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        FastPlace.mc.rightClickDelayTimer = Math.min(FastPlace.mc.rightClickDelayTimer, 1);
    }
}
