package fun.expensive.client.ui.element.imp.panel;

import net.minecraft.client.renderer.texture.DynamicTexture;
import ru.alone.Alone;
import ru.alone.setting.imp.SettingBoolean;
import ru.alone.setting.imp.SettingColor;
import ru.alone.setting.imp.SettingEnum;
import ru.alone.setting.imp.SettingNumeric;
import ru.alone.ui.element.Element;
import ru.alone.ui.element.imp.module.*;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.module.Module;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ElementSettings extends Element {

    public ElementFlow flow;
    public Module module;

    private List<Element> elements = new ArrayList<Element>();

    public ElementSettings(ElementFlow flow, Module module){
        this.setWidth(120);
        this.setHeight(180);
        this.module = module;
        this.flow = flow;

        module.getSettings().stream().filter(o -> o instanceof SettingBoolean).map(o -> (SettingBoolean)o).forEach(o -> elements.add(new ElementBoolean(this, o)));
        module.getSettings().stream().filter(o -> o instanceof SettingNumeric).map(o -> (SettingNumeric)o).forEach(o -> elements.add(new ElementSlider(this, o)));
        module.getSettings().stream().filter(o -> o instanceof SettingColor).map(o -> (SettingColor)o).forEach(o -> elements.add(new ElementColorPicker(this, o)));
        module.getSettings().stream().filter(o -> o instanceof SettingEnum).map(o -> (SettingEnum)o).forEach(o -> elements.add(new ElementEnum(this, o)));

        elements.add(new ElementBind(this));
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {

        int title_y = (int)this.y;
        int title_height = 15;

        RenderUtils.drawRect(this.x, title_y, this.x + this.width, title_y + title_height, new Color(0x171717).getRGB());
        RenderUtils.roundedBorder((float)this.x, title_y, (float)this.x +(float) this.width, title_y + title_height,1, new Color(0x171717).getRGB());
        String title = module.getName();
        Fonts.Monstserrat.drawString(title, (int)this.x + (int)this.width / 2 - Fonts.Monstserrat.getStringWidth(title) / 2, (int)this.y+5, 0x7C7C7C, false);

        RenderUtils.drawRect(this.x, this.y + title_height, this.x + this.width, this.y + title_height + this.height,  new Color(0x151515).getRGB());

        Alone.exitTexture.bind((int)this.x + (int)this.width - 20, (int)this.y - 2);


        int offset = 0;
        for(Element element : elements){
            element.x = this.x + 2;
            element.y = this.y + title_height + 2 + offset;
            offset+=element.getHeight();
        }

        elements.forEach(e -> e.render(width, height, x, y, ticks));

        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {

        if(x >= this.x + (float)this.width - 12 && x <= this.x + (float)this.width - 2 && y >= this.y + 2 && y <= this.y + 12 && button == 0){
            flow.settings = null;
        }

        elements.forEach(e -> e.mouseClicked(x, y, button));
        super.mouseClicked(x, y, button);
    }

    @Override
    public void keypressed(char c, int key) {
        elements.forEach(e -> e.keypressed(c, key));
        super.keypressed(c, key);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        elements.forEach(e -> e.mouseRealesed(x, y, button));
        super.mouseRealesed(x, y, button);
    }

}
