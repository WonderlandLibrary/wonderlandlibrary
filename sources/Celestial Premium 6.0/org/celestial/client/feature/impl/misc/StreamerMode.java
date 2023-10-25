/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class StreamerMode
extends Feature {
    public static BooleanSetting ownName;
    public static BooleanSetting otherNames;
    public static BooleanSetting friendNames;
    public static BooleanSetting skinSpoof;
    public static BooleanSetting tabSpoof;
    public static BooleanSetting scoreBoardSpoof;
    public static BooleanSetting warpSpoof;
    public static BooleanSetting authSpoof;

    public StreamerMode() {
        super("StreamerMode", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u043a\u0440\u044b\u0432\u0430\u0442\u044c \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u0441\u0435\u0431\u0435 \u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u0438\u0433\u0440\u043e\u043a\u0430\u0445 \u043d\u0430 \u0432\u0438\u0434\u0435\u043e \u0438\u043b\u0438 \u0441\u0442\u0440\u0438\u043c\u0435", Type.Misc);
        ownName = new BooleanSetting("Own Name", true, () -> true);
        otherNames = new BooleanSetting("Other Names", true, () -> true);
        friendNames = new BooleanSetting("Friend Names", false, () -> true);
        tabSpoof = new BooleanSetting("Tab Spoof", true, () -> true);
        skinSpoof = new BooleanSetting("Skin Spoof", true, () -> true);
        scoreBoardSpoof = new BooleanSetting("ScoreBoard Spoof", true, () -> true);
        warpSpoof = new BooleanSetting("Warp Spoof", true, () -> true);
        authSpoof = new BooleanSetting("Auth Spoof", true, () -> true);
        this.addSettings(ownName, otherNames, friendNames, tabSpoof, skinSpoof, scoreBoardSpoof, warpSpoof, authSpoof);
    }
}

