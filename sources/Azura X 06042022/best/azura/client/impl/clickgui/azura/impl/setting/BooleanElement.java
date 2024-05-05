package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;

import java.awt.*;

public class BooleanElement extends SettingElement {

    private final BooleanValue value;
    private double clickAnimation = 0;
    private long start;

    public BooleanElement(int x, int y, int width, int height, BooleanValue value) {
        super(x, y, width, height, value);
        this.value = value;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        this.height = this.visibilityAnimation == 0 ? 0 : (int) (30*this.visibilityAnimation);
        if(this.visibilityAnimation == 0) return;

        if(!value.getObject()) {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/200f);
            clickAnimation = -1 * Math.pow(anim-1, 6) + 1;
            clickAnimation = 1 - clickAnimation;
        } else {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/200f);
            clickAnimation = -1 * Math.pow(anim-1, 6) + 1;
        }

        this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);

        double opacity = visibilityAnimation == 1 ? animation : visibilityAnimation;
        Fonts.INSTANCE.arial15.drawString(value.getName(), x+30, y+15-Fonts.INSTANCE.arial15.FONT_HEIGHT/2.0,
                new Color(255, 255, 255, (int) (255*(visibilityAnimation == 1 ? animation : visibilityAnimation))).getRGB());
        RenderUtil.INSTANCE.drawRoundedRect(x+width-60, y+height/2-8, 30, 16, 6, RenderUtil.INSTANCE.modifiedAlpha(new Color(20, 20, 20), (int) (255*opacity)));

        RenderUtil.INSTANCE.drawCircle(x+width-53+clickAnimation*15, y+height/2,  5, RenderUtil.INSTANCE.modifiedAlpha(ColorUtil.colorFade(new Color(40, 40, 40), ClickGUI.color.getObject().getColor(), clickAnimation), (int) (255*opacity)));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(this.hovered && this.animation >= 0.99) {
            this.value.setObject(!this.value.getObject());
            this.start = System.currentTimeMillis();
        }
    }

    public BooleanValue getValue() {
        return value;
    }
}
