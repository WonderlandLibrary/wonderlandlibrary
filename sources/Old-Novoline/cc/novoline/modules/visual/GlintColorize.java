package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.ColorProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GlintColorize extends AbstractModule {

    private int offset = 0;

    @Property("glint-color")
    private final ColorProperty color = PropertyFactory.createColor(0xFF8A8AFF);
    @Property("rainbow")
    private final BooleanProperty rainbow = PropertyFactory.booleanFalse();

    public GlintColorize(@NonNull ModuleManager novoline) {
        super(novoline, "GlintColorize", "Glint Colorize", Keyboard.KEY_NONE, EnumModuleType.VISUALS);
        Manager.put(new Setting("GLINT_COLOR", "Glint color", SettingType.COLOR_PICKER, this, color, null, () -> !rainbow.get()));
        Manager.put(new Setting("GLINT_RAINBOW", "Rainbow", SettingType.CHECKBOX, this, rainbow));
    }

    @EventTarget
    public void onTick(TickUpdateEvent e) {
        if (rainbow.get()) {
            offset++;
            if (offset > 255) offset = 0;
        } else if (offset != 0) offset = 0;
    }

    public int glintColor() {
        float[] colorPicker = this.color.getHSB();
        float hue = rainbow.get() ? offset / 255.0F : colorPicker[0];
        final Color color = Color.getHSBColor(hue, colorPicker[1], colorPicker[2]);
        return color.getRGB();
    }

    public ColorProperty getColor() {
        return color;
    }

    public BooleanProperty getRainbow() {
        return rainbow;
    }

    public int getOffset() {
        return offset;
    }
}
