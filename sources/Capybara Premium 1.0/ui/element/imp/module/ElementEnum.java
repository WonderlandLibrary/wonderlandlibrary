package fun.expensive.client.ui.element.imp.module;

import ru.alone.setting.imp.SettingEnum;
import ru.alone.ui.element.Element;
import ru.alone.ui.element.imp.panel.ElementSettings;
import ru.alone.utils.other.font.Fonts;

import java.awt.*;

public class ElementEnum extends Element {

    private ElementSettings settings;
    private SettingEnum setting;
    private double animation;

    public ElementEnum(ElementSettings settings, SettingEnum setting) {
        this.settings = settings;
        this.setting = setting;
        this.setWidth(settings.getWidth());
        this.setHeight(15);
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        Fonts.Monstserrat.drawString(setting.getName(), (int) this.x, (int) this.y+5, new Color(0x7C7C7C).getRGB(), false);
        Fonts.Monstserrat.drawString(setting.getEnumValue(), (int) this.x+this.width-5-Fonts.Monstserrat.getStringWidth(setting.getEnumValue()), (int) this.y+5, new Color(0x7C7C7C).getRGB(), false);

        super.render(width, height, x, y, ticks);
    }


    @Override
    public void mouseClicked(int x, int y, int button) {
        {
            if (collided(x, y)) {
                if (button == 0) {
                    int i = 0;
                    int enumIndex = 0;
                    for (String enumName : setting.getEnumValues()) {
                        if (enumName.equals(setting.getEnumValue())) enumIndex = i;
                        i++;
                    }
                    if (enumIndex == setting.getEnumValues().size() - 1) {
                        setting.setEnumValue(setting.getEnumValues().get(0));
                    } else {
                        enumIndex++;
                        i = 0;
                        for (String enumName : setting.getEnumValues()) {
                            if (i == enumIndex) setting.setEnumValue(enumName);
                            i++;
                        }
                    }
                } else if (button == 1) {
                    int i = 0;
                    int enumIndex = 0;
                    for (String enumName : setting.getEnumValues()) {
                        if (enumName.equals(setting.getEnumValue())) enumIndex = i;
                        i++;
                    }
                    if (enumIndex == 0) {
                        setting.setEnumValue(setting.getEnumValues().get(setting.getEnumValues().size() - 1));
                    } else {
                        enumIndex--;
                        i = 0;
                        for (String enumName : setting.getEnumValues()) {
                            if (i == enumIndex) setting.setEnumValue(enumName);
                            i++;
                        }
                    }
                }
            }
        }
        super.mouseClicked(x, y, button);
    }
    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }
}
