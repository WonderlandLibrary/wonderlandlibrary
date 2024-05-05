/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg.inkscape;

import me.kiras.aimwhere.libraries.slick.geom.Rectangle;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.Transform;
import me.kiras.aimwhere.libraries.slick.svg.Diagram;
import me.kiras.aimwhere.libraries.slick.svg.Figure;
import me.kiras.aimwhere.libraries.slick.svg.Loader;
import me.kiras.aimwhere.libraries.slick.svg.NonGeometricData;
import me.kiras.aimwhere.libraries.slick.svg.ParsingException;
import me.kiras.aimwhere.libraries.slick.svg.inkscape.ElementProcessor;
import me.kiras.aimwhere.libraries.slick.svg.inkscape.Util;
import org.w3c.dom.Element;

public class RectProcessor
implements ElementProcessor {
    @Override
    public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        float width = Float.parseFloat(element.getAttribute("width"));
        float height = Float.parseFloat(element.getAttribute("height"));
        float x = Float.parseFloat(element.getAttribute("x"));
        float y = Float.parseFloat(element.getAttribute("y"));
        Rectangle rect = new Rectangle(x, y, width + 1.0f, height + 1.0f);
        Shape shape = rect.transform(transform);
        NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("width", "" + width);
        data.addAttribute("height", "" + height);
        data.addAttribute("x", "" + x);
        data.addAttribute("y", "" + y);
        diagram.addFigure(new Figure(3, shape, data, transform));
    }

    @Override
    public boolean handles(Element element) {
        return element.getNodeName().equals("rect");
    }
}

