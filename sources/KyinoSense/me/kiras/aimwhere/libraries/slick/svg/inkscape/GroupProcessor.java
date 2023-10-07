/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg.inkscape;

import me.kiras.aimwhere.libraries.slick.geom.Transform;
import me.kiras.aimwhere.libraries.slick.svg.Diagram;
import me.kiras.aimwhere.libraries.slick.svg.Loader;
import me.kiras.aimwhere.libraries.slick.svg.ParsingException;
import me.kiras.aimwhere.libraries.slick.svg.inkscape.ElementProcessor;
import me.kiras.aimwhere.libraries.slick.svg.inkscape.Util;
import org.w3c.dom.Element;

public class GroupProcessor
implements ElementProcessor {
    @Override
    public boolean handles(Element element) {
        return element.getNodeName().equals("g");
    }

    @Override
    public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        loader.loadChildren(element, transform);
    }
}

