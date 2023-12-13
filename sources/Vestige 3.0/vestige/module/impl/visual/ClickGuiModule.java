package vestige.module.impl.visual;

import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.ui.click.dropdown.DropdownClickGUI;
import vestige.util.render.ColorUtil;

import java.awt.*;

public class ClickGuiModule extends Module {

    private DropdownClickGUI dropdownClickGUI;

    private final ModeSetting color = new ModeSetting("Color", "Client theme", "Client theme", "Custom static", "Custom fade", "Custom 3 colors", "Rainbow");

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

    public final BooleanSetting boxOnHover = new BooleanSetting("Box on hover", false);
    public final BooleanSetting boxOnSettings = new BooleanSetting("Box on settings", () -> boxOnHover.isEnabled(), false);

    private Color color1, color2, color3;

    private ClientTheme theme;

    public ClickGuiModule() {
        super("ClickGUI", Category.VISUAL);
        this.setKey(Keyboard.KEY_RSHIFT);
        this.addSettings(color, red, green, blue, red2, green2, blue2, red3, green3, blue3, saturation, brightness, boxOnHover, boxOnSettings);
    }

    @Override
    public void onEnable() {
        if (dropdownClickGUI == null) {
            dropdownClickGUI = new DropdownClickGUI(this);
        }

        mc.displayGuiScreen(dropdownClickGUI);

        setColors();
    }

    @Override
    public void onClientStarted() {
        theme = Vestige.instance.getModuleManager().getModule(ClientTheme.class);
    }

    @Listener
    public void onRender(RenderEvent event) {
        setColors();
    }

    public int getColor(int offset) {
        switch (color.getMode()) {
            case "Client theme":
                return theme.getColor(offset);
            case "Custom static":
                return color1.getRGB();
            case "Custom fade":
                return ColorUtil.getColor(color1, color2, 2500, offset);
            case "Custom 3 colors":
                return ColorUtil.getColor(color1, color2, color3, 3000, offset);
            case "Rainbow":
                return ColorUtil.getRainbow(4500, (int) (offset * 0.85), (float) saturation.getValue(), (float) brightness.getValue());
        }

        return -1;
    }

    private void setColors() {
        color1 = new Color(red.getValue(), green.getValue(), blue.getValue());
        color2 = new Color(red2.getValue(), green2.getValue(), blue2.getValue());
        color3 = new Color(red3.getValue(), green3.getValue(), blue3.getValue());
    }

}
