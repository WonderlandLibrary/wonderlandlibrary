package de.tired.base.guis.newclickgui.section;


import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.RenderUtil;

import java.awt.*;

public class Section {

    public int x, y;

    private int mouseX, mouseY;

    private SectionType sectionType;

    public Section(SectionType sectionType, int x, int y) {
        this.x = x;
        this.y = y;
        this.sectionType = sectionType;
    }

    public void render(float x, float y, int mouseX, int mouseY) {
        RenderUtil.instance.drawCircle(x, y, 14, new Color(40, 40, 40).getRGB());

        FontManager.iconFont.drawString(sectionType.icon, x - 5, y - 1, -1);

    }

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

}
