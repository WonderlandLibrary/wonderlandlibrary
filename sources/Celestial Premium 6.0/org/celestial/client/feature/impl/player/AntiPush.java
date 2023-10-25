/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class AntiPush
extends Feature {
    public static BooleanSetting water;
    public static BooleanSetting players;
    public static BooleanSetting blocks;

    public AntiPush() {
        super("AntiPush", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043e\u0442\u0442\u0430\u043b\u043a\u0438\u0432\u0430\u043d\u0438\u0435 \u043e\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u0432\u043e\u0434\u044b \u0438 \u0431\u043b\u043e\u043a\u043e\u0432", Type.Player);
        players = new BooleanSetting("Entity", true, () -> true);
        water = new BooleanSetting("Liquid", true, () -> true);
        blocks = new BooleanSetting("Blocks", true, () -> true);
        this.addSettings(players, water, blocks);
    }
}

