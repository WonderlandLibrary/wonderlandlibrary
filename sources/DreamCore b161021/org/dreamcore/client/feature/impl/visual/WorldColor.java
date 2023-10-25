package org.dreamcore.client.feature.impl.visual;

import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ColorSetting;

import java.awt.*;

public class WorldColor extends Feature {

    public static ColorSetting worldColor = new ColorSetting("World Color", Color.RED.getRGB(), () -> true);

    public WorldColor() {
        super("WorldColor", "Меняет цвет игры", Type.Visuals);
        addSettings(worldColor);
    }
}
