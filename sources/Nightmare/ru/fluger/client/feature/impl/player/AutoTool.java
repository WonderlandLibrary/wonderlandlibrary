// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import java.util.Comparator;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import java.util.ArrayList;
import ru.fluger.client.feature.impl.Type;
import java.util.List;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class AutoTool extends Feature
{
    private BooleanSetting swapBack;
    private BooleanSetting saveItem;
    public BooleanSetting silentSwitch;
    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private aip swapedItem;
    private List<Integer> lastItem;
    
    public AutoTool() {
        super("AutoTool", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u043b\u0443\u0447\u0448\u0438\u0439 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442 \u0432 \u0440\u0443\u043a\u0438 \u043f\u0440\u0438 \u043b\u043e\u043c\u0430\u043d\u0438\u0438 \u0431\u043b\u043e\u043a\u0430", Type.Player);
        this.swapBack = new BooleanSetting("Swap Back", true, () -> true);
        this.saveItem = new BooleanSetting("Save Item", true, () -> true);
        this.silentSwitch = new BooleanSetting("Silent Switch", true, () -> true);
        this.swapedItem = null;
        this.lastItem = new ArrayList<Integer>();
        this.addSettings(this.swapBack, this.saveItem, this.silentSwitch);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate update) {
        aow hoverBlock = null;
        if (AutoTool.mc.s.a() == null) {
            return;
        }
        hoverBlock = AutoTool.mc.f.o(AutoTool.mc.s.a()).u();
        final List<Integer> bestItem = new ArrayList<Integer>();
        for (int i = 0; i < 9 && hoverBlock != null; ++i) {
            if (AutoTool.mc.h.bv.a(i).k() - AutoTool.mc.h.bv.a(i).i() > 1 || !this.saveItem.getCurrentValue()) {
                if (AutoTool.mc.h.getDigSpeed(AutoTool.mc.f.o(AutoTool.mc.s.a()), AutoTool.mc.h.bv.a(i)) > 1.0f) {
                    bestItem.add(i);
                }
            }
        }
        bestItem.sort(Comparator.comparingDouble(x -> -AutoTool.mc.h.getDigSpeed(AutoTool.mc.f.o(AutoTool.mc.s.a()), AutoTool.mc.h.bv.a(x))));
        if (!bestItem.isEmpty() && AutoTool.mc.t.ae.i) {
            if (AutoTool.mc.h.bv.i() != this.swapedItem) {
                this.lastItem.add(AutoTool.mc.h.bv.d);
                if (this.silentSwitch.getCurrentValue()) {
                    AutoTool.mc.h.d.a(new lv(bestItem.get(0)));
                }
                else {
                    AutoTool.mc.h.bv.d = bestItem.get(0);
                }
                this.itemIndex = bestItem.get(0);
                this.swap = true;
            }
            this.swapDelay = System.currentTimeMillis();
        }
        else if (this.swap && !this.lastItem.isEmpty() && System.currentTimeMillis() >= this.swapDelay + 300L && this.swapBack.getCurrentValue()) {
            if (this.silentSwitch.getCurrentValue()) {
                AutoTool.mc.h.d.a(new lv(this.lastItem.get(0)));
            }
            else {
                AutoTool.mc.h.bv.d = this.lastItem.get(0);
            }
            this.itemIndex = this.lastItem.get(0);
            this.lastItem.clear();
            this.swap = false;
        }
    }
}
