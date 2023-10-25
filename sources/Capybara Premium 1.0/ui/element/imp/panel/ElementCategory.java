package fun.expensive.client.ui.element.imp.panel;


import ru.alone.module.imp.render.UI;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.Alone;
import ru.alone.module.Module;
import ru.alone.ui.element.Element;
import ru.alone.ui.element.imp.module.ElementModule;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ElementCategory extends Element {

    public Module.Category category;
    public ElementFlow flow;
    public List<ElementModule> elementModuleList = new ArrayList<ElementModule>();

    public ElementCategory(ElementFlow flow, Module.Category category) {
        this.flow = flow;
        this.category = category;
        this.setWidth(125);
        this.setHeight(160);
        Alone.moduleManager.getModules().stream().filter(m -> m.getCategory().equals(category) && !(m instanceof UI)).forEach(m -> elementModuleList.add(new ElementModule(m, this)));
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {

        int title_y = (int) this.y;
        int title_height = 15;

        RenderUtils.drawRect(this.x, title_y, this.x + this.width, title_y + title_height, new Color(0x171717).getRGB());
        RenderUtils.roundedBorder((float) this.x, title_y, (float) this.x + (float) this.width, title_y + title_height, 1, new Color(0x171717).getRGB());
        RenderUtils.drawRect(this.x, this.y + title_height, this.x + this.width, this.y + title_height + this.height, new Color(0x151515).getRGB());

        Fonts.Monstserrat17.drawString(category.name(), (int) this.x + (int) this.width / 2 - Fonts.Monstserrat17.getStringWidth(category.name()) / 2, (int) this.y + 5, 0x7C7C7C, false);
        if (category.name() == "Combat") {
            Fonts.Icon.drawString("D", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        if (category.name() == "Movement") {
            Fonts.Icon.drawString("A", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        if (category.name() == "Render") {
            Fonts.Icon.drawString("C", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        if (category.name() == "Player") {
            Fonts.Icon.drawString("B", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        if (category.name() == "Misc") {
            Fonts.Icon.drawString("G", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        if (category.name() == "Exploit") {
            Fonts.Icon.drawString("J", (float) (this.x + 6), (float) (this.y + 3.5f), 0x7C7C7C);
        }
        int offsetY = 20;

        for (ElementModule elementModule : elementModuleList) {
            elementModule.x = this.x;
            elementModule.y = this.y + offsetY;
            offsetY += elementModule.getHeight();
        }
        elementModuleList.forEach(e -> e.render(width, height, x, y, ticks));
        super.render(width, height, x, y, ticks);
    }


    @Override
    public void mouseClicked(int x, int y, int button) {
        elementModuleList.forEach(e -> e.mouseClicked(x, y, button));
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }
}
