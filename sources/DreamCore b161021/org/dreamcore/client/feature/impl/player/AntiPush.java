package org.dreamcore.client.feature.impl.player;

import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;

public class AntiPush extends Feature {

    public static BooleanSetting water;
    public static BooleanSetting players;
    public static BooleanSetting blocks;

    public AntiPush() {
        super("AntiPush", "Убирает отталкивание от игроков, воды и блоков", Type.Player);
        players = new BooleanSetting("Entity", true, () -> true);
        water = new BooleanSetting("Liquid", true, () -> true);
        blocks = new BooleanSetting("Blocks", true, () -> true);
        addSettings(players, water, blocks);
    }
}
