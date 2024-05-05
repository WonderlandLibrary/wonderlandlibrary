// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class Notifications extends Feature
{
    public static BooleanSetting state;
    public static BooleanSetting timePeriod;
    
    public Notifications() {
        super("Notifications", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0443\u044e \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u043c\u043e\u0434\u0443\u043b\u044f\u0445", Type.Hud);
        Notifications.state = new BooleanSetting("Module State", true, () -> true);
        Notifications.timePeriod = new BooleanSetting("Time Period", false, () -> true);
        this.addSettings(Notifications.state, Notifications.timePeriod);
    }
    
    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}
