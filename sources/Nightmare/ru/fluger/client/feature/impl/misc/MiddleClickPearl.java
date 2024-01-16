// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.misc;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.input.EventMouse;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class MiddleClickPearl extends Feature
{
    public MiddleClickPearl() {
        super("MiddleClickPearl", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043a\u0438\u0434\u0430\u0435\u0442 \u044d\u043d\u0434\u0435\u0440-\u043f\u0435\u0440\u043b \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u043a\u043e\u043b\u0435\u0441\u043e \u043c\u044b\u0448\u0438", Type.Misc);
    }
    
    @EventTarget
    public void onMouseEvent(final EventMouse event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; ++i) {
                final aip itemStack = MiddleClickPearl.mc.h.bv.a(i);
                if (itemStack.c() == air.bC) {
                    MiddleClickPearl.mc.h.d.a(new lv(i));
                    MiddleClickPearl.mc.h.d.a(new mb(ub.a));
                    MiddleClickPearl.mc.h.d.a(new lv(MiddleClickPearl.mc.h.bv.d));
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        MiddleClickPearl.mc.h.d.a(new lv(MiddleClickPearl.mc.h.bv.d));
        super.onDisable();
    }
}
