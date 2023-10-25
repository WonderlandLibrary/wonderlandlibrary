package fun.expensive.client.ui.element.imp.module;

import net.minecraft.util.math.MathHelper;
import ru.alone.setting.imp.SettingNumeric;
import ru.alone.ui.element.Element;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.ui.element.imp.panel.ElementSettings;

import java.awt.*;

public class ElementSlider extends Element {

    private double selected;
    private boolean dragging = false;
    private ElementSettings settings;
    private SettingNumeric setting;

    private double animation;

    public ElementSlider(ElementSettings settings, SettingNumeric setting){
        this.settings = settings;
        this.setting = setting;
        this.selected = MathHelper.clamp(setting.value / setting.max, 0.0, 1.0);
        this.setWidth(settings.getWidth());
    }

    double slider_height = 3; double slider_x = 2; double slider_y = 15;

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        x-=this.x;
        setWidth(60);
        setHeight(20);

        double slider_width = getWidth();

        String title = setting.getName() + ": ";

        float text_width = Fonts.Monstserrat.getStringWidth(title) + 2;

        if(animation != selected){
            if(animation > selected){
                animation-=0.002;
            }else if(animation < selected){
                animation+=0.002;
            }
        }

        RenderUtils.drawRect(this.x + slider_x, this.y + slider_y, this.x + slider_x + slider_width * this.animation, this.y + slider_y + slider_height, new Color(0x777777).getRGB());

        RenderUtils.roundedBorder((float)this.x  +(float) slider_x, (float)this.y + (float)slider_y, (float)this.x + (float)slider_x + (float)slider_width, (float)this.y + (float)slider_y + (float)slider_height, 0.6f,1, new Color(0x777777).getRGB());
        RenderUtils.drawCircle((float)this.x + (float)slider_x + (float)slider_width * (float)this.animation, (float)this.y + (float)slider_y + 1.5f, 0, 360,3f,true, new Color(0x808080));

        Fonts.Monstserrat17.drawString(title, (float)this.x, (float)this.y+5, 0x7C7C7C, false);

        String displayValue = setting.value + "/" + setting.max;

        Fonts.Monstserrat.drawString(displayValue, (float)this.x  + Fonts.Monstserrat17.getStringWidth(title) + 10, (float)this.y + 5.5, 0x7C7C7C, false);

        double clamp = MathHelper.clamp((double)x / getWidth(), 0.0, 1.0);
        if (dragging) {
            this.selected = clamp;
            double current = setting.min + clamp * (setting.max - setting.min);
            double round = 10;
            setting.value = (double)Math.round(current * round) / round;
        }

        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (button == 0 && x >= this.x + this.slider_x && x <= this.x + slider_x + this.width && y >= this.y + this.slider_y && y <= this.y + this.slider_y + this.slider_height) {
            dragging = true;
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        this.dragging = false;
        super.mouseRealesed(x, y, button);
    }
}
