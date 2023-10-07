package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ClickValue;

import java.awt.*;

public class ClickElement extends SettingElement {
    private final ClickValue value;

    public ClickElement(int x, int y, int width, int height, ClickValue value) {
        super(x, y, width, height, value);
        this.value = value;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        this.height = this.visibilityAnimation == 0 ? 0 : (int) (30*this.visibilityAnimation);
        if(this.visibilityAnimation == 0) return;

        this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
        Fonts.INSTANCE.arial15.drawString(value.getName(), x+30, y+15-Fonts.INSTANCE.arial15.FONT_HEIGHT/2.0,
                new Color(255, 255, 255, (int) (255*(visibilityAnimation == 1 ? animation : visibilityAnimation))).getRGB());

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
