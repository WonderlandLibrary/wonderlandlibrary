// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import java.util.Iterator;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.settings.Setting;
import java.util.ArrayList;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.misc.TimerHelper;
import java.util.List;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class AutoTotem extends Feature
{
    private final NumberSetting health;
    private final BooleanSetting countTotem;
    private final BooleanSetting checkCrystal;
    private final BooleanSetting checkTnt;
    private final NumberSetting radiusTnt;
    private final BooleanSetting inventoryOnly;
    private final NumberSetting radiusCrystal;
    private final NumberSetting swapBackDelay;
    private final NumberSetting fallDistance;
    private final BooleanSetting switchBack;
    private final BooleanSetting checkFall;
    private final List<Integer> lastItem;
    private final TimerHelper timerHelper;
    private boolean swap;
    
    public AutoTotem() {
        super("AutoTotem", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u0432 \u0440\u0443\u043a\u0443 \u0442\u043e\u0442\u0435\u043c \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435", Type.Combat);
        this.checkTnt = new BooleanSetting("Check Tnt", true, () -> true);
        this.radiusTnt = new NumberSetting("Distance to Tnt", 6.0f, 1.0f, 8.0f, 1.0f, () -> true);
        this.switchBack = new BooleanSetting("Swap Back", "\u0412\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0435\u0442 \u043f\u0440\u043e\u0448\u043b\u044b\u0439 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u043f\u043e\u0441\u043b\u0435 \u0441\u043d\u043e\u0441\u0430 \u0442\u043e\u0442\u0435\u043c\u0430.", true, () -> true);
        this.lastItem = new ArrayList<Integer>();
        this.timerHelper = new TimerHelper();
        this.swap = false;
        this.health = new NumberSetting("Health Amount", 3.5f, 1.0f, 20.0f, 0.5f, () -> true);
        this.inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        this.swapBackDelay = new NumberSetting("Swap back delay", "\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u043c\u0435\u0436\u0434\u0443 \u0441\u0432\u0430\u043f\u043e\u043c \u043f\u0440\u043e\u0448\u043b\u043e\u0433\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430 \u0438 \u0442\u043e\u0442\u0435\u043c\u0430", 100.0f, 10.0f, 500.0f, 5.0f, this.switchBack::getCurrentValue);
        this.countTotem = new BooleanSetting("Count Totem", true, () -> true);
        this.checkFall = new BooleanSetting("Check Fall", true, () -> true);
        this.fallDistance = new NumberSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, this.checkFall::getCurrentValue);
        this.checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        this.radiusCrystal = new NumberSetting("Distance to Crystal", 6.0f, 1.0f, 8.0f, 1.0f, this.checkCrystal::getCurrentValue);
        this.addSettings(this.switchBack, this.swapBackDelay, this.health, this.inventoryOnly, this.countTotem, this.checkFall, this.fallDistance, this.checkCrystal, this.radiusCrystal, this.checkTnt, this.radiusTnt);
    }
    
    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.h.bv.w_(); ++i) {
            final aip stack = AutoTotem.mc.h.bv.a(i);
            if (stack.c() == air.Totem) {
                ++count;
            }
        }
        return count;
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (this.fountTotemCount() > 0 && this.countTotem.getCurrentValue()) {
            AutoTotem.mc.ubuntuFontRender16.drawStringWithShadow(this.fountTotemCount() + "", event.getResolution().a() / 2.0f + 19.0f, event.getResolution().b() / 2.0f, -1);
            for (int i = 0; i < AutoTotem.mc.h.bv.w_(); ++i) {
                final aip stack = AutoTotem.mc.h.bv.a(i);
                if (stack.c() == air.Totem) {
                    bus.G();
                    bus.l();
                    AutoTotem.mc.ad().b(stack, event.getResolution().a() / 2 + 4, event.getResolution().b() / 2 - 7);
                    bus.H();
                }
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.inventoryOnly.getCurrentValue() && !(AutoTotem.mc.m instanceof bmx)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (AutoTotem.mc.h.bv.a(i).c() == air.Totem && tIndex == -1) {
                tIndex = i;
            }
            if (AutoTotem.mc.h.bv.a(i).c() == air.Totem) {
                ++totemCount;
            }
        }
        if ((AutoTotem.mc.h.cd() < this.health.getCurrentValue() || this.checkCrystal() || this.checkTNT() || this.checkFall(this.fallDistance.getCurrentValue())) && totemCount != 0 && tIndex != -1) {
            if (AutoTotem.mc.h.cp().c() != air.Totem) {
                AutoTotem.mc.c.a(0, (tIndex < 9) ? (tIndex + 36) : tIndex, 1, afw.a, AutoTotem.mc.h);
                AutoTotem.mc.c.a(0, 45, 0, afw.a, AutoTotem.mc.h);
                AutoTotem.mc.c.a(0, (tIndex < 9) ? (tIndex + 36) : tIndex, 0, afw.a, AutoTotem.mc.h);
                this.swap = true;
                this.lastItem.add(tIndex);
            }
        }
        else if (this.switchBack.getCurrentValue() && (this.swap || totemCount == 0) && this.lastItem.size() > 0) {
            if (!(AutoTotem.mc.h.bv.a(this.lastItem.get(0)).c() instanceof agt) && this.timerHelper.hasReached(this.swapBackDelay.getCurrentValue())) {
                AutoTotem.mc.c.a(0, (this.lastItem.get(0) < 9) ? (this.lastItem.get(0) + 36) : ((int)this.lastItem.get(0)), 0, afw.a, AutoTotem.mc.h);
                AutoTotem.mc.c.a(0, 45, 0, afw.a, AutoTotem.mc.h);
                AutoTotem.mc.c.a(0, (this.lastItem.get(0) < 9) ? (this.lastItem.get(0) + 36) : ((int)this.lastItem.get(0)), 0, afw.a, AutoTotem.mc.h);
                this.timerHelper.reset();
            }
            this.swap = false;
            this.lastItem.clear();
        }
    }
    
    private boolean checkFall(final float fallDist) {
        return this.checkFall.getCurrentValue() && AutoTotem.mc.h.L > fallDist;
    }
    
    private boolean checkTNT() {
        if (!this.checkTnt.getCurrentValue()) {
            return false;
        }
        for (final vg entity : AutoTotem.mc.f.e) {
            if (entity instanceof acm && AutoTotem.mc.h.g(entity) <= this.radiusTnt.getCurrentValue()) {
                return true;
            }
            if (!(entity instanceof afm)) {
                continue;
            }
            if (AutoTotem.mc.h.g(entity) > this.radiusTnt.getCurrentValue()) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    private boolean checkCrystal() {
        if (!this.checkCrystal.getCurrentValue()) {
            return false;
        }
        for (final vg entity : AutoTotem.mc.f.e) {
            if (entity instanceof abc) {
                if (AutoTotem.mc.h.g(entity) > this.radiusCrystal.getCurrentValue()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
}
