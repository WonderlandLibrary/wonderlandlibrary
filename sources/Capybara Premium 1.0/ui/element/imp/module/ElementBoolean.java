package fun.expensive.client.ui.element.imp.module;

import ru.alone.module.imp.render.HUD;
import ru.alone.setting.imp.SettingBoolean;
import ru.alone.ui.element.Element;
import ru.alone.utils.AnimationUtils;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.ui.element.imp.panel.ElementSettings;
import ru.alone.Alone;

import java.awt.*;

public class ElementBoolean extends Element {

    private ElementSettings settings;
    private SettingBoolean setting;
    private double animation;

    public ElementBoolean(ElementSettings settings, SettingBoolean setting) {
        this.settings = settings;
        this.setting = setting;
        this.setWidth(settings.getWidth());
        this.setHeight(15);
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        Fonts.Monstserrat.drawString(setting.getName(), (int) this.x, (int) this.y+5, new Color(0x7C7C7C).getRGB(), false);
        int checkbox_width = 15;
        int checkbox_height = 5;
        double max = setting.state ? 5 : checkbox_width;
        this.animation = AnimationUtils.animate(max, this.animation, 0.15);
        RenderUtils.drawRect(this.x-3 + this.width - checkbox_width, this.y + checkbox_height, this.x-3 + this.width - 4, this.y + checkbox_height + 3, new Color(0x2D2D2D).getRGB());
        RenderUtils.roundedBorder((float) this.x-3 + (float) this.width - (float) checkbox_width, (float) this.y + (float) checkbox_height, (float) this.x -3+ (float) this.width - 4, (float) this.y + (float) checkbox_height + 3, 1f, new Color(0x2D2D2D).getRGB());
        RenderUtils.drawCircle((float) this.x-3 + (float) this.width - (float) this.animation, (float) this.y + 6.5f, 0, 360, 3f, true, new Color(0x808080));
        HUD hud = (HUD) Alone.moduleManager.getModule(HUD.class);

        if (setting.state) {

               RenderUtils.drawCircle((float) this.x - 3 + (float) this.width - (float) this.animation, (float) this.y + 6.5f, 0, 360, 3f, true, new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue()));
               RenderUtils.drawBlurredShadowCircle((float) this.x - 3 + (float) this.width - (float) this.animation - 3.4f, (float) this.y + 3.8f, 6, 6, 8, new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue()));


        } else {

        }
        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (collided(x, y) && button == 0) {
            this.setting.state = !this.setting.state;
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }
}
