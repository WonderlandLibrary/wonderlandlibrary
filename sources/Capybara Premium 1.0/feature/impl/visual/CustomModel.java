package fun.expensive.client.feature.impl.visual;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class CustomModel extends Feature {
    public static ListSetting modelMode = new ListSetting("Model Mode", "Amogus", () -> true, "Amogus", "Jeff Killer", "Demon", "Rabbit");
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
        super("CustomModel", "ѕозвол€ет редактировать модель игроков", FeatureCategory.Visuals);
        friendHighlight = new BooleanSetting("Friend Highlight", true, () -> {
            return modelMode.currentMode.equals("Amogus");
        });
        bodyColor = new ListSetting("Amogus Body Color Mode", "Custom", () -> {
            return modelMode.currentMode.equals("Amogus");
        }, new String[]{"Custom", "Client", "Rainbow", "Astolfo"});
        eyeColor = new ListSetting("Amogus Eye Color Mode", "Custom", () -> {
            return modelMode.currentMode.equals("Amogus");
        }, new String[]{"Custom", "Client", "Rainbow", "Astolfo"});
        legsColor = new ListSetting("Amogus Legs Color Mode", "Custom", () -> {
            return modelMode.currentMode.equals("Amogus");
        }, new String[]{"Custom", "Client", "Rainbow", "Astolfo"});
        bodyCustomColor = new ColorSetting("Amogus Body Color", Color.RED.getRGB(), () -> {
            return modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom");
        });
        eyeCustomColor = new ColorSetting("Amogus Eye Color", Color.CYAN.getRGB(), () -> {
            return modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom");
        });
        legsCustomColor = new ColorSetting("Amogus Legs Color", Color.RED.getRGB(), () -> {
            return modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom");
        });
        onlyMe = new BooleanSetting("Only Me", true, () -> {
            return true;
        });
        friends = new BooleanSetting("Friends", false, () -> {
            return onlyMe.getBoolValue();
        });
        addSettings(modelMode, friendHighlight, bodyColor, bodyCustomColor, eyeColor, eyeCustomColor, legsColor, legsCustomColor, onlyMe, friends);
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(modelMode.getCurrentMode());
    }
}
