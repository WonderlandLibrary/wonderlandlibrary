// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import java.util.Iterator;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class AutoPotion extends Feature
{
    public TimerHelper timerHelper;
    public NumberSetting delay;
    public BooleanSetting onlyGround;
    public BooleanSetting strenght;
    public BooleanSetting speed;
    public BooleanSetting fire_resistance;
    aip held;
    
    public AutoPotion() {
        super("AutoPotion", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0440\u043e\u0441\u0430\u0435\u0442 Splash \u0437\u0435\u043b\u044c\u044f \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", Type.Combat);
        this.timerHelper = new TimerHelper();
        this.onlyGround = new BooleanSetting("Only Ground", true, () -> true);
        this.strenght = new BooleanSetting("Strenght", true, () -> true);
        this.speed = new BooleanSetting("Speed", true, () -> true);
        this.fire_resistance = new BooleanSetting("Fire Resistance", true, () -> true);
        this.delay = new NumberSetting("Throw Delay", 300.0f, 10.0f, 800.0f, 10.0f, () -> true);
        this.addSettings(this.delay, this.onlyGround, this.strenght, this.speed, this.fire_resistance);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion event) {
        if (AutoPotion.mc.m instanceof bmx) {
            return;
        }
        if (this.onlyGround.getCurrentValue() && !AutoPotion.mc.h.z) {
            return;
        }
        if (this.timerHelper.hasReached(this.delay.getCurrentValue())) {
            if (!AutoPotion.mc.h.a(uz.a(1)) && this.isPotionOnHotBar(Potions.SPEED)) {
                if (AutoPotion.mc.h.z) {
                    event.setPitch(90.0f);
                }
                else {
                    event.setPitch(-90.0f);
                }
                if (AutoPotion.mc.h.z) {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, 90.0f, AutoPotion.mc.h.z));
                }
                else {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, -90.0f, AutoPotion.mc.h.z));
                }
                if (AutoPotion.mc.h.z) {
                    if (event.getPitch() == 90.0f) {
                        this.throwPot(Potions.SPEED);
                    }
                }
                else if (event.getPitch() == -90.0f) {
                    this.throwPot(Potions.SPEED);
                }
            }
            if (!AutoPotion.mc.h.a(uz.a(5)) && this.isPotionOnHotBar(Potions.STRENGTH)) {
                if (AutoPotion.mc.h.z) {
                    event.setPitch(90.0f);
                }
                else {
                    event.setPitch(-90.0f);
                }
                if (AutoPotion.mc.h.z) {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, 90.0f, AutoPotion.mc.h.z));
                }
                else {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, -90.0f, AutoPotion.mc.h.z));
                }
                if (AutoPotion.mc.h.z) {
                    if (event.getPitch() == 90.0f) {
                        this.throwPot(Potions.STRENGTH);
                    }
                }
                else if (event.getPitch() == -90.0f) {
                    this.throwPot(Potions.STRENGTH);
                }
            }
            if (!AutoPotion.mc.h.a(uz.a(12)) && this.isPotionOnHotBar(Potions.FIRERES)) {
                if (AutoPotion.mc.h.z) {
                    event.setPitch(90.0f);
                }
                else {
                    event.setPitch(-90.0f);
                }
                if (AutoPotion.mc.h.z) {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, 90.0f, AutoPotion.mc.h.z));
                }
                else {
                    AutoPotion.mc.h.d.a(new lk.c(AutoPotion.mc.h.v, -90.0f, AutoPotion.mc.h.z));
                }
                if (AutoPotion.mc.h.z) {
                    if (event.getPitch() == 90.0f) {
                        this.throwPot(Potions.FIRERES);
                    }
                }
                else if (event.getPitch() == -90.0f) {
                    this.throwPot(Potions.FIRERES);
                }
            }
            this.timerHelper.reset();
        }
    }
    
    void throwPot(final Potions potion) {
        final int slot = this.getPotionSlot(potion);
        AutoPotion.mc.h.d.a(new lv(slot));
        AutoPotion.mc.c.e();
        AutoPotion.mc.h.d.a(new mb(ub.a));
        AutoPotion.mc.c.e();
        AutoPotion.mc.h.d.a(new lv(AutoPotion.mc.h.bv.d));
    }
    
    public int getPotionSlot(final Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (this.isStackPotion(AutoPotion.mc.h.bv.a(i), potion)) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean isPotionOnHotBar(final Potions potions) {
        for (int i = 0; i < 9; ++i) {
            if (this.isStackPotion(AutoPotion.mc.h.bv.a(i), potions)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isStackPotion(final aip stack, final Potions potion) {
        if (stack == null) {
            return false;
        }
        final ain item = stack.c();
        if (item == air.bI) {
            int id = 5;
            switch (potion) {
                case SPEED: {
                    id = 1;
                    break;
                }
                case FIRERES: {
                    id = 12;
                    break;
                }
            }
            for (final va effect : aki.a(stack)) {
                if (effect.a() == uz.a(id)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    enum Potions
    {
        STRENGTH, 
        SPEED, 
        FIRERES;
    }
}
