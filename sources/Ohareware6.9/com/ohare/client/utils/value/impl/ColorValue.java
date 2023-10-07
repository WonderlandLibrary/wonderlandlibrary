package com.ohare.client.utils.value.impl;

import com.ohare.client.utils.value.Value;

import java.awt.*;

/**
 * made by Xen for OhareWare
 *
 * @since 7/26/2019
 **/
public class ColorValue extends Value<Color> {

    public ColorValue(String label, Color value) {
        super(label, value);
    }

    @Override
    public void setValue(String value) {
        setValue(new Color(Integer.parseInt(value)));
    }

    @Override
    public void setValue(Color value) {
        super.setValue(value);
    }

    /**
     * Sets the value from a hexadecimal.
     *
     * @param hex The hexadecimal to add.
     */
    public void setValue(int hex) {
        setValue(new Color(hex));
    }

    /**
     * Sets the value from an array of red green and blue values.
     *
     * @param red   The red value of the color.
     * @param green The green value of the color.
     * @param blue  The blue value of the color.
     */
    public void setValue(int red, int green, int blue) {
        setValue(new Color(red, green, blue));
    }

}