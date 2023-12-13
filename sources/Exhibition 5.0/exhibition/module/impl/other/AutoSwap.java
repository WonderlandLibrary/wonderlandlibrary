// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import org.lwjgl.input.Keyboard;
import exhibition.util.misc.ChatUtil;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoSwap extends Module
{
    public static int multiSwap;
    public static boolean isSwapping;
    public static boolean settingKey;
    public static boolean keysSet;
    private static final String MULTI = "MULTI";
    private static final String SINGLE = "SINGLE";
    public int multiKey;
    public int single;
    Timer timer;
    
    public AutoSwap(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @Override
    public void onEnable() {
        AutoSwap.isSwapping = false;
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            if (AutoSwap.settingKey && this.timer.delay(1000.0f) && !AutoSwap.keysSet) {
                ChatUtil.printChat("§4[§cE§4]§8 press your key you'd like to set for Multi Swap.");
                AutoSwap.keysSet = true;
            }
            else if (AutoSwap.settingKey || !AutoSwap.keysSet) {}
            if (AutoSwap.keysSet && Keyboard.getEventKey() != 0) {
                this.multiKey = Keyboard.getEventKey();
            }
        }
        if (!em.isPost() || !AutoSwap.settingKey) {}
    }
}
