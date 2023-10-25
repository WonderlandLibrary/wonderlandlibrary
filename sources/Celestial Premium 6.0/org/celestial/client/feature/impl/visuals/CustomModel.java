/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class CustomModel
extends Feature {
    public static ListSetting modelMode = new ListSetting("Model Mode", "Amogus", "Amogus", "Jeff Killer", "Crab", "Demon", "Red Panda", "Chinchilla", "Freddy Bear", "CupHead", "Sonic", "Crazy Rabbit", "None");
    public static BooleanSetting friendHighlight;
    public static ListSetting bodyColor;
    public static ListSetting eyeColor;
    public static ListSetting legsColor;
    public static ColorSetting bodyCustomColor;
    public static ColorSetting eyeCustomColor;
    public static ColorSetting legsCustomColor;
    public static BooleanSetting googlyEyes;
    public static NumberSetting googlyEyesSize;
    public static BooleanSetting onlyMe;
    public static BooleanSetting friends;

    public CustomModel() {
        super("CustomModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043c\u043e\u0434\u0435\u043b\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432", Type.Visuals);
        friendHighlight = new BooleanSetting("Friend Highlight", true, () -> CustomModel.modelMode.currentMode.equals("Amogus"));
        bodyColor = new ListSetting("Amogus Body Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        eyeColor = new ListSetting("Amogus Eye Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        legsColor = new ListSetting("Amogus Legs Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        bodyCustomColor = new ColorSetting("Amogus Body Color", Color.RED.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        eyeCustomColor = new ColorSetting("Amogus Eye Color", Color.CYAN.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        legsCustomColor = new ColorSetting("Amogus Legs Color", Color.RED.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        googlyEyes = new BooleanSetting("Googly Eyes", false, () -> CustomModel.modelMode.currentMode.equals("None"));
        googlyEyesSize = new NumberSetting("Google Eyes Size", 0.75f, 0.7f, 1.5f, 0.01f, () -> googlyEyes.getCurrentValue() && CustomModel.modelMode.currentMode.equals("None"));
        onlyMe = new BooleanSetting("Only Me", true, () -> true);
        friends = new BooleanSetting("Friends", false, () -> onlyMe.getCurrentValue());
        this.addSettings(modelMode, friendHighlight, bodyColor, bodyCustomColor, eyeColor, eyeCustomColor, legsColor, legsCustomColor, googlyEyes, googlyEyesSize, onlyMe, friends);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(modelMode.getCurrentMode());
    }
}

