/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class ClientSounds
extends Feature {
    public static ListSetting soundMode;
    public static NumberSetting volume;
    public static NumberSetting pitch;

    public ClientSounds() {
        super("ClientSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a\u0438 \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f/\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u043c\u043e\u0434\u0443\u043b\u044f", Type.Misc);
        soundMode = new ListSetting("Sound Mode", "Wav", "Wav", "Button");
        volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);
        pitch = new NumberSetting("Pitch", 2.0f, 0.5f, 2.0f, 0.1f, () -> ClientSounds.soundMode.currentMode.equals("Button"));
        this.addSettings(soundMode, volume, pitch);
    }
}

