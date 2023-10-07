/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.svg.inkscape;

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

public class UseProcessor
implements ElementProcessor {
    @Override
    public boolean handles(Element element) {
        return element.getNodeName().equals("use");
    }

    @Override
    public void process(Loader loader, Element element, Diagram diagram, Transform transform) throws ParsingException {
        String ref = element.getAttributeNS("http://www.w3.org/1999/xlink", "href");
        String href = Util.getAsReference(ref);
        Figure referenced = diagram.getFigureByID(href);
        if (referenced == null) {
            throw new ParsingException(element, "Unable to locate referenced element: " + href);
        }
        Transform local = Util.getTransform(element);
        Transform trans = local.concatenate(referenced.getTransform());
        NonGeometricData data = Util.getNonGeometricData(element);
        Shape shape = referenced.getShape().transform(trans);
        data.addAttribute("fill", referenced.getData().getAttribute("fill"));
        data.addAttribute("stroke", referenced.getData().getAttribute("stroke"));
        data.addAttribute("opacity", referenced.getData().getAttribute("opacity"));
        data.addAttribute("stroke-width", referenced.getData().getAttribute("stroke-width"));
        Figure figure = new Figure(referenced.getType(), shape, data, trans);
        diagram.addFigure(figure);
    }
}

