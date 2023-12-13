/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.svg.inkscape.ElementProcessor;
import org.newdawn.slick.svg.inkscape.Util;
import org.w3c.dom.Element;

public class RectProcessor
implements ElementProcessor {
    public void process(Loader loader, Element element, Diagram diagram, Transform t2) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t2, transform);
        float width = Float.parseFloat(element.getAttribute("width"));
        float height = Float.parseFloat(element.getAttribute("height"));
        float x2 = Float.parseFloat(element.getAttribute("x"));
        float y2 = Float.parseFloat(element.getAttribute("y"));
        Rectangle rect = new Rectangle(x2, y2, width + 1.0f, height + 1.0f);
        Shape shape = rect.transform(transform);
        NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("width", "" + width);
        data.addAttribute("height", "" + height);
        data.addAttribute("x", "" + x2);
        data.addAttribute("y", "" + y2);
        diagram.addFigure(new Figure(3, shape, data, transform));
    }

    public boolean handles(Element element) {
        return element.getNodeName().equals("rect");
    }
}

