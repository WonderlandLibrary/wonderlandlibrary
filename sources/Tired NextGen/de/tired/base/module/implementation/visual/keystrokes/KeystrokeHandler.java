package de.tired.base.module.implementation.visual.keystrokes;

import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.util.animation.Easings;

import de.tired.util.render.RenderUtil;
import de.tired.util.render.StencilUtil;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.interfaces.IHook;

import java.awt.*;

public class KeystrokeHandler implements IHook {

    public int x, y, width, height;

    public KeystrokeKey key;

    private final Animation circleAnimation = new Animation();

    private final Animation alphaAnimation = new Animation();

    private final ColorAnimation reverseColorAnimation = new ColorAnimation();

    public KeystrokeHandler(KeystrokeKey keystrokeKey, int x, int y, int width, int height) {
        this.key = keystrokeKey;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //Draggable so we need to be able to change the x and y
    public void renderKey(int x, int y) {

        this.x = x;
        this.y = y;

        { // Animation code block
            this.circleAnimation.update();

            this.circleAnimation.animate(pressed() ? 18 : 0, .05, Easings.BOUNCE_IN);

            this.alphaAnimation.update();
            this.alphaAnimation.animate(pressed() ? 160 : 0, .05, Easings.BOUNCE_IN);

            final Color firstColor = ColorUtil.interpolateColorsBackAndForth(12, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);

            this.reverseColorAnimation.update();
            this.reverseColorAnimation.animate(pressed() ? firstColor : Color.WHITE, .2);

        }

        //Circle and rectangle code block
        {

            RenderUtil.instance.drawRect2(x, y, width, height, Integer.MIN_VALUE);
            if (circleAnimation.getValue() >= 1) {
                StencilUtil.initStencilToWrite();
                RenderUtil.instance.drawRect2(x, y, width, height, Integer.MIN_VALUE);
                StencilUtil.readStencilBuffer(1);

                RenderUtil.instance.drawCircle(x + width / 2f, y + height / 2f, circleAnimation.getValue(), new Color(20, 20, 20, (int) alphaAnimation.getValue()).getRGB());
                StencilUtil.uninitStencilBuffer();
            }
        }

        FontManager.raleWay20.drawCenteredString(key.key, x + width / 2f, y + height / 2f - 2, reverseColorAnimation.getColor().getRGB());
        if (circleAnimation.getValue() >= 1)
            FontManager.raleWay20.drawCenteredString(key.key, x + width / 2f, y + height / 2f - 1, new Color(reverseColorAnimation.getColor().getRed(), reverseColorAnimation.getColor().getGreen(), reverseColorAnimation.getColor().getBlue(), (int) alphaAnimation.getValue()).getRGB());

    }

    public boolean pressed() {
        switch (key) {
            case W:
                return MC.gameSettings.keyBindLeft.pressed;
            case A:
                return MC.gameSettings.keyBindBack.pressed;
            case D:
                return MC.gameSettings.keyBindJump.pressed;
            case S:
                return MC.gameSettings.keyBindRight.pressed;
        }
        return false;
    }

}
