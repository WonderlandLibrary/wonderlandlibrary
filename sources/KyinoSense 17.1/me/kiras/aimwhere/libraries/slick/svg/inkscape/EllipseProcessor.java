/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg.inkscape;

import me.kiras.aimwhere.libraries.slick.geom.Ellipse;
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

public class EllipseProcessor
implements ElementProcessor {
    @Override
    public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t, transform);
        float x = Util.getFloatAttribute(element, "cx");
        float y = Util.getFloatAttribute(element, "cy");
        float rx = Util.getFloatAttribute(element, "rx");
        float ry = Util.getFloatAttribute(element, "ry");
        Ellipse ellipse = new Ellipse(x, y, rx, ry);
        Shape shape = ellipse.transform(transform);
        NonGeometricData data = Util.getNonGeometricData(element);
        data.addAttribute("cx", "" + x);
        data.addAttribute("cy", "" + y);
        data.addAttribute("rx", "" + rx);
        data.addAttribute("ry", "" + ry);
        diagram.addFigure(new Figure(1, shape, data, transform));
    }

    @Override
    public boolean handles(Element element) {
        if (element.getNodeName().equals("ellipse")) {
            return true;
        }
        return element.getNodeName().equals("path") && "arc".equals(element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
    }
}

