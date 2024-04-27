/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.Location;

public class Particles {
    public int ticks;
    public Location location;
    public String text;

    public Particles(Location location, String text) {
        this.location = location;
        this.text = text;
        this.ticks = 1;
    }
}

