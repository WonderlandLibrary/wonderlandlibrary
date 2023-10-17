package de.tired.util.render;

import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import org.lwjgl.input.Mouse;

public class ScrollHandler {

    public float maxScroll = Integer.MAX_VALUE, minScroll = 0, rawScroll;
    private float scroll;

    private final Animation scrollAnimation = new Animation();

    public void handleScroll() {

        scroll = Math.abs(rawScroll);
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);

        scrollAnimation.update();
        scrollAnimation.animate(rawScroll, .2, Easings.NONE);

        this.scroll = scrollAnimation.getValue();

    }

    public void setMaxScroll(float maxScroll) {
        this.maxScroll = maxScroll;
    }

    public float getScroll() {
        return scroll;
    }


}