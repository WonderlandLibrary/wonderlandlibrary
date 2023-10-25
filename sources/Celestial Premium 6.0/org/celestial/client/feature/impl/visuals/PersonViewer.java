/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class PersonViewer
extends Feature {
    public static NumberSetting viewerYaw;
    public static NumberSetting fovModifier;
    public static NumberSetting viewerPitch;

    public PersonViewer() {
        super("PersonViewer", "\u041f\u043e\u0432\u043e\u0437\u044f\u043b\u044f\u0435\u0442 \u0438\u0437\u043c\u0435\u043d\u044f\u0442\u044c \u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043a\u0430\u043c\u0435\u0440\u044b \u0432\u0442\u043e\u0440\u043e\u0433\u043e \u0438 \u0442\u0440\u0435\u0442\u044c\u0435\u0433\u043e \u043b\u0438\u0446\u0430", Type.Visuals);
        fovModifier = new NumberSetting("FOV Modifier", 4.0f, 1.0f, 50.0f, 1.0f, () -> true);
        viewerYaw = new NumberSetting("Viewer Yaw", 10.0f, -50.0f, 50.0f, 5.0f, () -> true);
        viewerPitch = new NumberSetting("Viewer Pitch", 10.0f, -50.0f, 50.0f, 5.0f, () -> true);
        this.addSettings(fovModifier, viewerYaw, viewerPitch);
    }
}

