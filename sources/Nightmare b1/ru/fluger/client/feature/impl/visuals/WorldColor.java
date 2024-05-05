// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class WorldColor extends Feature
{
    public static ListSetting worldColorMode;
    public static ColorSetting lightMapColor;
    
    public WorldColor() {
        super("WorldColor", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0446\u0432\u0435\u0442 \u0432\u043e\u043a\u0440\u0443\u0433", Type.Visuals);
        WorldColor.lightMapColor = new ColorSetting("LightMap Color", Color.WHITE.getRGB(), () -> WorldColor.worldColorMode.currentMode.equals("Custom"));
        this.addSettings(WorldColor.worldColorMode, WorldColor.lightMapColor);
    }
    
    static {
        WorldColor.worldColorMode = new ListSetting("LightMap Mode", "Custom", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
    }
}
