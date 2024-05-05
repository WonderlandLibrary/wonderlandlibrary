package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ColorValue;

public class ColorElement extends SettingElement {

    private final ColorValue value;
    private boolean extended;
    private double clickAnimation;
    private long clickStart;
    private boolean draggingBrightSat = false, draggingHue = false;

    public ColorElement(int x, int y, int width, int height, ColorValue value) {
        super(x, y, width, height, value);
        this.value = value;
    }
    

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        draggingBrightSat = false;
        draggingHue = false;
    }

    public ColorValue getValue() {
        return value;
    }
}
