/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.svg.inkscape;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.newdawn.slick.geom.Path;
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

public class PathProcessor
implements ElementProcessor {
    private static Path processPoly(Element element, StringTokenizer tokens) throws ParsingException {
        boolean count = false;
        ArrayList pts = new ArrayList();
        boolean moved = false;
        boolean reasonToBePath = false;
        Path path = null;
        while (tokens.hasMoreTokens()) {
            try {
                float y2;
                float x2;
                String nextToken = tokens.nextToken();
                if (nextToken.equals("L")) {
                    x2 = Float.parseFloat(tokens.nextToken());
                    y2 = Float.parseFloat(tokens.nextToken());
                    path.lineTo(x2, y2);
                    continue;
                }
                if (nextToken.equals("z")) {
                    path.close();
                    continue;
                }
                if (nextToken.equals("M")) {
                    if (!moved) {
                        moved = true;
                        x2 = Float.parseFloat(tokens.nextToken());
                        y2 = Float.parseFloat(tokens.nextToken());
                        path = new Path(x2, y2);
                        continue;
                    }
                    reasonToBePath = true;
                    x2 = Float.parseFloat(tokens.nextToken());
                    y2 = Float.parseFloat(tokens.nextToken());
                    path.startHole(x2, y2);
                    continue;
                }
                if (!nextToken.equals("C")) continue;
                reasonToBePath = true;
                float cx1 = Float.parseFloat(tokens.nextToken());
                float cy1 = Float.parseFloat(tokens.nextToken());
                float cx2 = Float.parseFloat(tokens.nextToken());
                float cy2 = Float.parseFloat(tokens.nextToken());
                float x3 = Float.parseFloat(tokens.nextToken());
                float y3 = Float.parseFloat(tokens.nextToken());
                path.curveTo(x3, y3, cx1, cy1, cx2, cy2);
            }
            catch (NumberFormatException e2) {
                throw new ParsingException(element.getAttribute("id"), "Invalid token in points list", (Throwable)e2);
            }
        }
        if (!reasonToBePath) {
            return null;
        }
        return path;
    }

    public void process(Loader loader, Element element, Diagram diagram, Transform t2) throws ParsingException {
        Transform transform = Util.getTransform(element);
        transform = new Transform(t2, transform);
        String points = element.getAttribute("points");
        if (element.getNodeName().equals("path")) {
            points = element.getAttribute("d");
        }
        StringTokenizer tokens = new StringTokenizer(points, ", ");
        Path path = PathProcessor.processPoly(element, tokens);
        NonGeometricData data = Util.getNonGeometricData(element);
        if (path != null) {
            Shape shape = path.transform(transform);
            diagram.addFigure(new Figure(4, shape, data, transform));
        }
    }

    public boolean handles(Element element) {
        return element.getNodeName().equals("path") && !"arc".equals(element.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
    }
}

