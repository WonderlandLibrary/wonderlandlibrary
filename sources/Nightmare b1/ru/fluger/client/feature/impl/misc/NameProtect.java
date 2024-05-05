// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.misc;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class NameProtect extends Feature
{
    public static BooleanSetting myName;
    public static BooleanSetting friends;
    public static BooleanSetting otherName;
    public static BooleanSetting tabSpoof;
    public static BooleanSetting scoreboardSpoof;
    
    public NameProtect() {
        super("NameProtect", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u043a\u0440\u044b\u0432\u0430\u0442\u044c \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u0441\u0435\u0431\u0435 \u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u0438\u0433\u0440\u043e\u043a\u0430\u0445", Type.Misc);
        this.addSettings(NameProtect.myName, NameProtect.otherName, NameProtect.friends, NameProtect.tabSpoof, NameProtect.scoreboardSpoof);
    }
    
    static {
        NameProtect.myName = new BooleanSetting("My Name", true, () -> true);
        NameProtect.friends = new BooleanSetting("Friends Spoof", true, () -> true);
        NameProtect.otherName = new BooleanSetting("Other Names", false, () -> true);
        NameProtect.tabSpoof = new BooleanSetting("Tab Spoof", false, () -> true);
        NameProtect.scoreboardSpoof = new BooleanSetting("Scoreboard Spoof", true, () -> true);
    }
}
