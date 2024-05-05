// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class CustomModel extends Feature
{
    public static ListSetting modelMode;
    public static BooleanSetting friendHighlight;
    public static ListSetting bodyColor;
    public static ListSetting eyeColor;
    public static ListSetting legsColor;
    public static ColorSetting bodyCustomColor;
    public static ColorSetting eyeCustomColor;
    public static ColorSetting legsCustomColor;
    public static BooleanSetting onlyMe;
    public static BooleanSetting friends;
    
    public CustomModel() {
        super("CustomModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043c\u043e\u0434\u0435\u043b\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432", Type.Visuals);
        CustomModel.friendHighlight = new BooleanSetting("Friend Highlight", true, () -> CustomModel.modelMode.currentMode.equals("Amogus"));
        CustomModel.bodyColor = new ListSetting("Amogus Body Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), new String[] { "Custom", "Client", "Rainbow", "Astolfo" });
        CustomModel.eyeColor = new ListSetting("Amogus Eye Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), new String[] { "Custom", "Client", "Rainbow", "Astolfo" });
        CustomModel.legsColor = new ListSetting("Amogus Legs Color Mode", "Custom", () -> CustomModel.modelMode.currentMode.equals("Amogus"), new String[] { "Custom", "Client", "Rainbow", "Astolfo" });
        CustomModel.bodyCustomColor = new ColorSetting("Amogus Body Color", Color.RED.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        CustomModel.eyeCustomColor = new ColorSetting("Amogus Eye Color", Color.CYAN.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        CustomModel.legsCustomColor = new ColorSetting("Amogus Legs Color", Color.RED.getRGB(), () -> CustomModel.modelMode.currentMode.equals("Amogus") && CustomModel.bodyColor.currentMode.equals("Custom"));
        CustomModel.onlyMe = new BooleanSetting("Only Me", true, () -> true);
        CustomModel.friends = new BooleanSetting("Friends", false, () -> CustomModel.onlyMe.getCurrentValue());
        this.addSettings(CustomModel.modelMode, CustomModel.friendHighlight, CustomModel.bodyColor, CustomModel.bodyCustomColor, CustomModel.eyeColor, CustomModel.eyeCustomColor, CustomModel.legsColor, CustomModel.legsCustomColor, CustomModel.onlyMe, CustomModel.friends);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(CustomModel.modelMode.getCurrentMode());
    }
    
    static {
        CustomModel.modelMode = new ListSetting("Model Mode", "Amogus", () -> true, new String[] { "Amogus", "Jeff Killer", "Demon", "Rabbit" });
    }
}
