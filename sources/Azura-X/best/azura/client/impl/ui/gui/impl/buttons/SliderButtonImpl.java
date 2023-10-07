package best.azura.client.impl.ui.gui.impl.buttons;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.util.render.RenderUtil;

import java.awt.*;

public class SliderButtonImpl implements Button {
    public int x, y, width, height;
    public String text = "";
    private boolean value = false, hovered;
    public double roundness = 0, animation = 0;
    public Color normalColor = new Color(70, 70, 70, 50),
            hoverColor = new Color(100, 100, 100, 70),
            electedColor = new Color(120, 120, 120, 50);

    public SliderButtonImpl(String text, int x, int y, int width, int height, double roundness) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.roundness = roundness;
    }


    @Override
    public void draw(int mouseX, int mouseY) {
        hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
        RenderUtil.INSTANCE.drawRoundedRect(x, y, width, height, roundness , value ? Color.GREEN : Color.RED);
        RenderUtil.INSTANCE.drawCircle(value ? x + width : x, y + (height / 2.0), roundness, value ? Color.GREEN : Color.RED);
        RenderUtil.INSTANCE.drawCircle(value ? x + width : x, y + (height / 2.0), roundness, value ? Color.GREEN : Color.RED);

        // TODO add text render.
    }

    @Override
    public void keyTyped(char typed, int keyCode) {

    }

    @Override
    public void mouseClicked() {
        if (hovered) value = !value;
    }
}
