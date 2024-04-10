package info.sigmaclient.sigma.utils.render.swoothui;


import java.awt.*;

public class AnimatedColor {
    private final Color from;
    private final Color to;

    private int currentR;
    private int currentG;
    private int currentB;
    private int currentA;

    private Color target;

    /**
     * If you don't know, put 9 or 10
     */
    private final int delta;

    public AnimatedColor(final Color from, final Color to, final int delta) {
        this.from = from;
        this.to = to;
        this.delta = delta;
        this.target = this.from;
        this.currentR = this.from.getRed();
        this.currentG = this.from.getGreen();
        this.currentB = this.from.getBlue();
        this.currentA = this.from.getAlpha();
    }

    public void switchToFrom() {
        this.target = this.from;
    }

    public void switchToTo() {
        this.target = this.to;
    }

    public Color getColor() {
        // decrement R, G, B values by 10 until they reach the target

        if (this.currentR > this.target.getRed()) {
            this.currentR -= delta;
            if (this.currentR < this.target.getRed()) this.currentR = this.target.getRed();
        } else if (this.currentR < this.target.getRed()) {
            this.currentR += delta;
            if (this.currentR > this.target.getRed()) this.currentR = this.target.getRed();
        }

        if (this.currentG > this.target.getGreen()) {
            this.currentG -= delta;
            if (this.currentG < this.target.getGreen()) this.currentG = this.target.getGreen();
        } else if (this.currentG < this.target.getGreen()) {
            this.currentG += delta;
            if (this.currentG > this.target.getGreen()) this.currentG = this.target.getGreen();
        }

        if (this.currentB > this.target.getBlue()) {
            this.currentB -= delta;
            if (this.currentB < this.target.getBlue()) this.currentB = this.target.getBlue();
        } else if (this.currentB < this.target.getBlue()) {
            this.currentB += delta;
            if (this.currentB > this.target.getBlue()) this.currentB = this.target.getBlue();
        }

        if (this.currentA > this.target.getAlpha()) {
            this.currentA -= delta;
            if (this.currentA < this.target.getAlpha()) this.currentA = this.target.getAlpha();
        } else if (this.currentA < this.target.getAlpha()) {
            this.currentA += delta;
            if (this.currentA > this.target.getAlpha()) this.currentA = this.target.getAlpha();
        }

        return new Color(this.currentR, this.currentG, this.currentB, this.currentA);
    }
}
