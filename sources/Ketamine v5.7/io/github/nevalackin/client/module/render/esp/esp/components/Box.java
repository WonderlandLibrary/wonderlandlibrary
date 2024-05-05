package io.github.nevalackin.client.module.render.esp.esp.components;

public final class Box {

    private final double width;
    private final int colour;

    public Box(double width, int colour) {
        this.width = width;
        this.colour = colour;
    }

    public double getWidth() {
        return width;
    }

    public int getColour() {
        return colour;
    }

}
