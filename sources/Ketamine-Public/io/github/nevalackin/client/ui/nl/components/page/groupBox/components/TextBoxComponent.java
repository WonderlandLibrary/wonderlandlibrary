package io.github.nevalackin.client.ui.nl.components.page.groupBox.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TextBoxComponent extends Component implements Animated {

    // TODO :: TextBoxComponent

    private final Function<String, Boolean> setter;
    private final Supplier<String> getter;

    private boolean active;
    private double progress;

    private String text;

    public TextBoxComponent(Component parent, Function<String, Boolean> setter, Supplier<String> getter, double y, double width, double height) {
        super(parent, 0, y, width, height);

        this.setter = setter;
        this.getter = getter;

        this.text = getter.get();
    }

    @Override
    public double getX() {
        return super.getX() + this.getParent().getWidth() - this.getWidth();
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        if (!this.active) this.text = getter.get();

        this.progress = DrawUtil.animateProgress(this.progress, this.active ? 1.0 : 0.0, 3);

        // Draw background
        {
            DrawUtil.glDrawFilledQuad(x, y, width, height, this.getTheme().getComponentBackgroundColour());

            DrawUtil.glDrawOutlinedQuad(x, y, width, height, 1.f,
                                        ColourUtil.fadeBetween(this.getTheme().getComponentOutlineColour(), this.getTheme().getMainColour(), DrawUtil.bezierBlendAnimation(this.progress)));
        }

        // Draw text
        {
            final double textScale = 0.22;
            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

            fontRenderer.draw(this.text, x + width / 2.0 - fontRenderer.getWidth(this.text, textScale) / 2.0,
                              y + height / 2.0 - fontRenderer.getHeight(this.text, textScale) / 2.0,
                              textScale, this.getTheme().getTextColour());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) this.active = true;
    }

    @Override
    public void onMouseRelease(int button) {

    }

    @Override
    public void onKeyPress(int keyCode) {
        if (this.active) {
            switch (keyCode) {
                case Keyboard.KEY_ESCAPE:
                    this.active = false;
                    break;
                case Keyboard.KEY_BACK:
                    if (!this.text.isEmpty())
                        this.text = this.text.substring(0, this.text.length() - 1);
                    break;
                case Keyboard.KEY_DELETE:
                    this.text = "";
                    break;
                case Keyboard.KEY_RETURN:
                    this.setter.apply(this.text);
                    break;
                case Keyboard.KEY_MINUS:
                case Keyboard.KEY_0:
                case Keyboard.KEY_1:
                case Keyboard.KEY_2:
                case Keyboard.KEY_3:
                case Keyboard.KEY_4:
                case Keyboard.KEY_5:
                case Keyboard.KEY_6:
                case Keyboard.KEY_7:
                case Keyboard.KEY_8:
                case Keyboard.KEY_9:
                case Keyboard.KEY_PERIOD:
                    this.text += keyCode;
                    break;
            }
        }
    }

    @Override
    public void resetAnimationState() {
        this.progress = 0.0;
    }
}
