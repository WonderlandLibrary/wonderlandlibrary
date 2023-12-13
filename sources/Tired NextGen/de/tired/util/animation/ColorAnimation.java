package de.tired.util.animation;


import java.awt.Color;

public class ColorAnimation {
    private final Animation redPart = new Animation();
    private final Animation greenPart = new Animation();
    private final Animation bluePart = new Animation();
    private final Animation alphaPart = new Animation();

    public ColorAnimation() {
    }

    public void update() {
        this.redPart.update();
        this.greenPart.update();
        this.bluePart.update();
        this.alphaPart.update();
    }

    public boolean isAlive() {
        return this.redPart.isAlive() && this.greenPart.isAlive() && this.bluePart.isAlive();
    }

    public void animate(Color color, double duration) {
        this.animate(color, duration, false);
    }

    public void animate(Color color, double duration, boolean safe) {
        this.redPart.animate((double)color.getRed(), duration, safe);
        this.greenPart.animate((double)color.getGreen(), duration, safe);
        this.bluePart.animate((double)color.getBlue(), duration, safe);
        this.alphaPart.animate((double)color.getAlpha(), duration, safe);
    }

    public Color getColor() {
        return new Color((int)this.redPart.getValue(), (int)this.greenPart.getValue(), (int)this.bluePart.getValue(), (int)this.alphaPart.getValue());
    }
}