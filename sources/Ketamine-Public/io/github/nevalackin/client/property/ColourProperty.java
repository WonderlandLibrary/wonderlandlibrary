package io.github.nevalackin.client.property;

import io.github.nevalackin.client.property.Property;

import java.awt.*;

public final class ColourProperty extends Property<Integer> {

    private Color colour;

    public ColourProperty(final String name, final int colour, Dependency dependency) {
        super(name, colour, dependency);
    }

    public ColourProperty(final String name, final int colour) {
        super(name, colour, null);
    }

    @Override
    public void setValue(final Integer colour) {
        super.setValue(colour);

        this.colour = new Color(colour); // Create Color object
    }

    public void setValue(final Color colour) {
        this.colour = colour;
        super.setValue(colour.getRGB());
    }

    public Color getColour() {
        return colour;
    }
}
