// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class AutoArmor extends Feature
{
    public static BooleanSetting openInventory;
    private final NumberSetting delay;
    public TimerHelper timerUtils;
    
    public AutoArmor() {
        super("AutoArmor", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043e\u0434\u0435\u0432\u0430\u0435\u0442 \u043b\u0443\u0447\u0448\u0443\u044e \u0431\u0440\u043e\u043d\u044e \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", Type.Combat);
        this.timerUtils = new TimerHelper();
        this.delay = new NumberSetting("Equip Delay", 1.0f, 0.0f, 10.0f, 0.5f, () -> true);
        AutoArmor.openInventory = new BooleanSetting("Open Inventory", true, () -> true);
        this.addSettings(this.delay, AutoArmor.openInventory);
    }
    
    public static boolean isNullOrEmpty(final aip stack) {
        return stack == null || stack.b();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + this.delay.getCurrentValue());
        if (!(AutoArmor.mc.m instanceof bmx) && AutoArmor.openInventory.getCurrentValue()) {
            return;
        }
        if (AutoArmor.mc.m instanceof bmg && !(AutoArmor.mc.m instanceof bmr)) {
            return;
        }
        final aec inventory = AutoArmor.mc.h.bv;
        final int[] bestArmorSlots = new int[4];
        final int[] bestArmorValues = new int[4];
        for (int type = 0; type < 4; ++type) {
            bestArmorSlots[type] = -1;
            final aip stack = inventory.g(type);
            if (!isNullOrEmpty(stack)) {
                if (stack.c() instanceof agv) {
                    final agv item = (agv)stack.c();
                    bestArmorValues[type] = this.getArmorValue(item, stack);
                }
            }
        }
        for (int slot = 0; slot < 36; ++slot) {
            final aip stack = inventory.a(slot);
            if (!isNullOrEmpty(stack)) {
                if (stack.c() instanceof agv) {
                    final agv item = (agv)stack.c();
                    final int armorType = item.c.b();
                    final int armorValue = this.getArmorValue(item, stack);
                    if (armorValue > bestArmorValues[armorType]) {
                        bestArmorSlots[armorType] = slot;
                        bestArmorValues[armorType] = armorValue;
                    }
                }
            }
        }
        final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for (final int i : types) {
            int j = bestArmorSlots[i];
            if (j != -1) {
                final aip oldArmor;
                if (!isNullOrEmpty(oldArmor = inventory.g(i)) && inventory.k() == -1) {
                    continue;
                }
                if (j < 9) {
                    j += 36;
                }
                if (!this.timerUtils.hasReached(this.delay.getCurrentValue() * 100.0f)) {
                    break;
                }
                if (!isNullOrEmpty(oldArmor)) {
                    AutoArmor.mc.c.a(0, 8 - i, 0, afw.b, AutoArmor.mc.h);
                }
                AutoArmor.mc.c.a(0, j, 0, afw.b, AutoArmor.mc.h);
                this.timerUtils.reset();
                break;
            }
        }
    }
    
    private int getArmorValue(final agv item, final aip stack) {
        final int armorPoints = item.d;
        int prtPoints = 0;
        final int armorToughness = (int)item.e;
        final int armorType = item.d().b(vl.d);
        final alk protection = alo.a;
        final int prtLvl = alm.a(protection, stack);
        final ur dmgSource = ur.a(AutoArmor.mc.h);
        prtPoints = protection.a(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}
