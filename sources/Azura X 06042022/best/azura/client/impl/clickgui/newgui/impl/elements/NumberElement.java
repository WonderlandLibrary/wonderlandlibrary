package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.impl.value.NumberValue;

public class NumberElement extends SettingElement {

    private final NumberValue<?> value;
    private boolean dragging = false;

    public NumberElement(int x, int y, int width, int height, NumberValue<?> value) {
        super(x, y, width, height, value);
        this.value = value;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(this.hovered && animation >= 0.95 && visibilityAnimation >= 0.95) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}
