// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.util.misc.ChatUtil;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoSay extends Module
{
    Timer timer;
    public static final String WORDS = "SAY";
    public final String DELAY = "DELAY";
    
    public AutoSay(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        ((HashMap<String, Setting<String>>)this.settings).put("SAY", new Setting<String>("SAY", "/warp", "Message to send."));
        ((HashMap<String, Setting<Integer>>)this.settings).put("DELAY", new Setting<Integer>("DELAY", 500, "MS delay between messages.", 100.0, 100.0, 10000.0));
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        final String message = ((HashMap<K, Setting<String>>)this.settings).get("SAY").getValue().toString();
        if (this.timer.delay(((HashMap<K, Setting<Number>>)this.settings).get("DELAY").getValue().longValue())) {
            ChatUtil.sendChat(message);
            this.timer.reset();
        }
    }
}
