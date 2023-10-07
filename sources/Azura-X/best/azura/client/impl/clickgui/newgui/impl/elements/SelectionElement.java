package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ComboValue;

public class SelectionElement extends SettingElement {

    private final ComboValue value;
    private long start;
    private double clickAnimation;
    private boolean extended;

    public SelectionElement(int x, int y, int width, int height, ComboValue value) {
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

    public ComboValue getValue() {
        return value;
    }
}
