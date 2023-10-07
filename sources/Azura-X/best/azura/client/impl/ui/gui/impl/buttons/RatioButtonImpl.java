package best.azura.client.impl.ui.gui.impl.buttons;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.impl.ui.font.FontRenderer;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;

import java.awt.*;

public class RatioButtonImpl implements Button {
    public int x, y;
    public String text = "";
    public boolean value = false, hovered;
    public double radius, animation = 0;
    private FontRenderer fontRenderer;
    public Color normalColor = new Color(70, 70, 70, 50),
            hoverColor = new Color(100, 100, 100, 70),
            electedColor = new Color(120, 120, 120, 50);

    public RatioButtonImpl(String text, int x, int y, double radius) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.fontRenderer = Fonts.INSTANCE.arial20;
    }

    public RatioButtonImpl(String text, int x, int y, double radius, FontRenderer fontRenderer) {
        this(text, x, y, radius);
        this.fontRenderer = fontRenderer;
    }


    @Override
    public void draw(int mouseX, int mouseY) {
        hovered = RenderUtil.INSTANCE.isHoveredScaled(x + fontRenderer.getStringWidth(text) * 1.25 - radius, y + 2 - radius, radius * 2, radius * 2, mouseX, mouseY, 1.0);
        RenderUtil.INSTANCE.drawCircle(x + fontRenderer.getStringWidth(text) * 1.25, y + 2, radius, value ? Color.GREEN : Color.RED);
        fontRenderer.drawString(text,x, y - fontRenderer.FONT_HEIGHT / 2f, -1);
    }

    @Override
    public void keyTyped(char typed, int keyCode) {

    }

    @Override
    public void mouseClicked() {
        if (hovered) value = !value;
    }
}
