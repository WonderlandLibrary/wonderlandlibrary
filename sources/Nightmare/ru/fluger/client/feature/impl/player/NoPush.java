// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class NoPush extends Feature
{
    public static BooleanSetting water;
    public static BooleanSetting players;
    public static BooleanSetting blocks;
    
    public NoPush() {
        super("NoPush", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043e\u0442\u0442\u0430\u043b\u043a\u0438\u0432\u0430\u043d\u0438\u0435 \u043e\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u0432\u043e\u0434\u044b \u0438 \u0431\u043b\u043e\u043a\u043e\u0432", Type.Player);
        this.addSettings(NoPush.players, NoPush.water, NoPush.blocks);
    }
    
    static {
        NoPush.water = new BooleanSetting("Water", true, () -> true);
        NoPush.players = new BooleanSetting("Entity", true, () -> true);
        NoPush.blocks = new BooleanSetting("Blocks", true, () -> true);
    }
}
