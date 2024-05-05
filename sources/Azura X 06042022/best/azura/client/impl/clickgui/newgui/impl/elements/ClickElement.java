package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.impl.value.ClickValue;

public class ClickElement extends SettingElement {
    private final ClickValue value;

    public ClickElement(int x, int y, int width, int height, ClickValue value) {
        super(x, y, width, height, value);
        this.value = value;
    }

    @Override
    public void render(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(this.hovered && this.animation >= 0.99) {
            this.value.getObject().run();
        }
    }

    public ClickValue getValue() {
        return value;
    }
}
