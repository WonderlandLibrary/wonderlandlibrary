/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.arithmo.module.impl.other;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.Timer;
import me.arithmo.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

public class AutoSwap
extends Module {
    public static int multiSwap;
    public static boolean isSwapping;
    public static boolean settingKey;
    public static boolean keysSet;
    private static final String MULTI = "MULTI";
    private static final String SINGLE = "SINGLE";
    public int multiKey;
    public int single;
    Timer timer = new Timer();

    public AutoSwap(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
        isSwapping = false;
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            if (settingKey && this.timer.delay(1000.0f) && !keysSet) {
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 press your key you'd like to set for Multi Swap.");
                keysSet = true;
            } else if (settingKey || !keysSet) {
                // empty if block
            }
            if (keysSet && Keyboard.getEventKey() != 0) {
                this.multiKey = Keyboard.getEventKey();
            }
        }
        if (!em.isPost() || !settingKey) {
            // empty if block
        }
    }
}

