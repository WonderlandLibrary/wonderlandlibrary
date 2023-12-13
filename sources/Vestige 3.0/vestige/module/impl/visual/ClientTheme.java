package vestige.module.impl.visual;

import vestige.event.Listener;
import vestige.event.Priority;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.EventListenType;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.render.ColorUtil;

import java.awt.*;

public class ClientTheme extends Module {

    public final ModeSetting color = new ModeSetting("Color", "Blue", "White", "Blue", "Custom static", "Custom fade", "Custom 3 colors", "Rainbow");

    private final IntegerSetting red = new IntegerSetting("Red", () -> color.getMode().startsWith("Custom"), 0, 0, 255, 5);
    private final IntegerSetting green = new IntegerSetting("Green", () -> color.getMode().startsWith("Custom"), 0, 0, 255, 5);
    private final IntegerSetting blue = new IntegerSetting("Blue", () -> color.getMode().startsWith("Custom"), 255, 0, 255, 5);

    private final IntegerSetting red2 = new IntegerSetting("Red 2", () -> color.is("Custom fade") || color.is("Custom 3 colors"), 0, 0, 255, 5);
    private final IntegerSetting green2 = new IntegerSetting("Green 2", () -> color.is("Custom fade") || color.is("Custom 3 colors"), 255, 0, 255, 5);
    private final IntegerSetting blue2 = new IntegerSetting("Blue 2", () -> color.is("Custom fade") || color.is("Custom 3 colors"), 255, 0, 255, 5);

    private final IntegerSetting red3 = new IntegerSetting("Red 3", () -> color.is("Custom 3 colors"), 0, 0, 255, 5);
    private final IntegerSetting green3 = new IntegerSetting("Green 3", () -> color.is("Custom 3 colors"), 255, 0, 255, 5);
    private final IntegerSetting blue3 = new IntegerSetting("Blue 3", () -> color.is("Custom 3 colors"), 255, 0, 255, 5);

    private final DoubleSetting saturation = new DoubleSetting("Saturation", () -> color.is("Rainbow"), 0.9, 0.05, 1, 0.05);
    private final DoubleSetting brightness = new DoubleSetting("Brightness", () -> color.is("Rainbow"), 0.9, 0.05, 1, 0.05);

    private Color color1, color2, color3;

    private boolean colorsSet;

    public ClientTheme() {
        super("Client theme", Category.VISUAL);
        this.addSettings(color, red, green, blue, red2, green2, blue2, red3, green3, blue3, saturation, brightness);

        this.listenType = EventListenType.MANUAL;
        this.startListening();
    }

    @Override
    public void onEnable() {
        this.setEnabled(false);
    }

    @Listener(Priority.HIGHER)
    public void onRender(RenderEvent event) {
        setColors();
        colorsSet = true;
    }

    public int getColor(int offset) {
        if(!colorsSet) {
            setColors();
            colorsSet = true;
        }

        switch (color.getMode()) {
            case "White":
                return -1;
            case "Blue":
                return ColorUtil.getColor(new Color(5, 138, 255), new Color(0, 35, 206), 2500, offset);
            case "Custom static":
                return color1.getRGB();
            case "Custom fade":
                return ColorUtil.getColor(color1, color2, 2500, offset);
            case "Custom 3 colors":
                return ColorUtil.getColor(color1, color2, color3, 3000, offset);
            case "Rainbow":
                return ColorUtil.getRainbow(4500, (int) (offset * 0.65), (float) saturation.getValue(), (float) brightness.getValue());
        }

        return -1;
    }

    private void setColors() {
        color1 = new Color(red.getValue(), green.getValue(), blue.getValue());
        color2 = new Color(red2.getValue(), green2.getValue(), blue2.getValue());
        color3 = new Color(red3.getValue(), green3.getValue(), blue3.getValue());
    }

}
