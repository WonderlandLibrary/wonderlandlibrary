package dev.rise.ui.clickgui.impl.tecnio.elements.buttontype;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttons.BooleanButton;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttons.ModeButton;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttons.NumberButton;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;

@Getter
public class ModuleButton extends Button {

    private final Module module;
    private final Color color;

    private boolean extended;
    private float finalHeight;

    private final ArrayList<Button> buttons = new ArrayList<>();

    public ModuleButton(final float x, final float y, final float width, final float height, final Module mod, final Color col) {
        super(x, y, width, height);

        module = mod;
        color = col;

        final float startY = y + height;

        int count = 0;

        for (final Setting setting : module.getSettings()) {

            if (setting instanceof BooleanSetting) {
                buttons.add(new BooleanButton(this.getX(), startY + 18 * count, width, 12, (BooleanSetting) setting, new Color(Rise.CLIENT_THEME_COLOR)));
            } else if (setting instanceof ModeSetting) {
                buttons.add(new ModeButton(this.getX(), startY + 18 * count, width, 12, (ModeSetting) setting, new Color(Rise.CLIENT_THEME_COLOR)));
            } else if (setting instanceof NumberSetting) {
                buttons.add(new NumberButton(this.getX(), startY + 18 * count, width, 12, (NumberSetting) setting, new Color(Rise.CLIENT_THEME_COLOR)));
            }

            count++;
        }
    }

    @Override
    public void drawPanel(final int mouseX, final int mouseY) {
        RenderUtil.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), new Color(0xff181A17));

        if (!extended)
            RenderUtil.rect(this.getX() + 1, this.getY(), this.getWidth() - 3, this.getHeight(), module.isEnabled() ? new Color(Rise.CLIENT_THEME_COLOR) : new Color(0xff232623));
        else
            RenderUtil.rect(this.getX() + 2, this.getY(), this.getWidth() - 2, this.getHeight(), new Color(0xff181A17));

        CustomFont.drawCenteredString(module.getModuleInfo().name(), (this.getX() + (this.getWidth() / 2.0F)), this.getY() + this.getHeight() / 2 - 5, extended ? module.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.GENERAL).hashCode() : 0xffffffff : 0xffffffff);

        int count = 0;
        float offset = 0;

        if (extended) {
            final float startY = this.getY() + this.getHeight();

            for (int i = 0; i < buttons.size(); i++) {

                final Button pan = buttons.get(i);

                if (!module.getSettings().get(i).isHidden()) {
                    pan.setX(this.getX());
                    pan.setY(startY + pan.getHeight() * count);

                    pan.drawPanel(mouseX, mouseY);
                    ++count;

                    offset = pan.getHeight();
                }

            }
        }

        finalHeight = offset * count + this.getHeight();
    }

    @Override
    public void mouseAction(final int mouseX, final int mouseY, final boolean click, final int button) {
        if (isHovered(mouseX, mouseY) && click) {
            if (button == 0) module.toggleModule();
            else if (module.settings.size() > 0) extended = !extended;
        }
    }
}
