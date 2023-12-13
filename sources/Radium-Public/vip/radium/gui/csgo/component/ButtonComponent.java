package vip.radium.gui.csgo.component;

public abstract class ButtonComponent extends Component {
    public ButtonComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY))
            onPress(button);

        super.onMouseClick(mouseX, mouseY, button);
    }

    public abstract void onPress(int mouseButton);
}
