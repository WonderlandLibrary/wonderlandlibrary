/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg.inkscape;

import me.kiras.aimwhere.libraries.slick.geom.Transform;
import me.kiras.aimwhere.libraries.slick.svg.Diagram;
import me.kiras.aimwhere.libraries.slick.svg.Loader;
import me.kiras.aimwhere.libraries.slick.svg.ParsingException;
import org.w3c.dom.Element;

public interface ElementProcessor {
    public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException;

    public boolean handles(Element var1);
}

