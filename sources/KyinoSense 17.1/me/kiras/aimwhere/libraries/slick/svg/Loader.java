/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg;

import me.kiras.aimwhere.libraries.slick.geom.Transform;
import me.kiras.aimwhere.libraries.slick.svg.ParsingException;
import org.w3c.dom.Element;

public interface Loader {
    public void loadChildren(Element var1, Transform var2) throws ParsingException;
}

