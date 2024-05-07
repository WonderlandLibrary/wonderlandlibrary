package com.masterof13fps.utils.render.slick.svg.inkscape;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.masterof13fps.utils.render.slick.geom.Polygon;
import com.masterof13fps.utils.render.slick.geom.Shape;
import com.masterof13fps.utils.render.slick.geom.Transform;
import com.masterof13fps.utils.render.slick.svg.Diagram;
import com.masterof13fps.utils.render.slick.svg.Figure;
import com.masterof13fps.utils.render.slick.svg.Loader;
import com.masterof13fps.utils.render.slick.svg.NonGeometricData;
import com.masterof13fps.utils.render.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * A processor for the <polygon> and <path> elements marked as not an arc.
 *
 * @author kevin
 */
public class PolygonProcessor implements ElementProcessor {

	/**
	 * Process the points in a polygon definition
	 * 
	 * @param poly The polygon being built
	 * @param element The XML element being read
	 * @param tokens The tokens representing the path
	 * @return The number of points found
	 * @throws ParsingException Indicates an invalid token in the path
	 */
	private static int processPoly(Polygon poly, Element element, StringTokenizer tokens) throws ParsingException {
		int count = 0;
		
		ArrayList pts = new ArrayList();
		boolean moved = false;
		boolean closed = false;
		
		while (tokens.hasMoreTokens()) {
			String nextToken = tokens.nextToken();
			if (nextToken.equals("L")) {
				continue;
			}
			if (nextToken.equals("z")) {
				closed = true;
				break;
			}
			if (nextToken.equals("M")) {
				if (!moved) {
					moved = true;
					continue;
				}
				
				return 0;
			}
			if (nextToken.equals("C")) {
				return 0;
			}
			
			String tokenX = nextToken;
			String tokenY = tokens.nextToken();
			
			try {
				float x = Float.parseFloat(tokenX);
				float y = Float.parseFloat(tokenY);
				
				poly.addPoint(x,y);
				count++;
			} catch (NumberFormatException e) {
				throw new ParsingException(element.getAttribute("id"), "Invalid token in points list", e);
			}
		}
		
		poly.setClosed(closed);
		return count;
	}


	/**
	 * @see ElementProcessor#process(Loader, Element, Diagram, Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
		Transform transform = Util.getTransform(element);
		transform = new Transform(t, transform); 
		
		String points = element.getAttribute("points");
		if (element.getNodeName().equals("path")) {
			points = element.getAttribute("d");
		}
		
		StringTokenizer tokens = new StringTokenizer(points, ", ");
		Polygon poly = new Polygon();
		int count = processPoly(poly, element, tokens);
		
		NonGeometricData data = Util.getNonGeometricData(element);
		if (count > 3) {
			Shape shape = poly.transform(transform);
			
			diagram.addFigure(new Figure(Figure.POLYGON, shape, data, transform));
		} 
	}

	/**
	 * @see ElementProcessor#handles(Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("polygon")) {
			return true;
		}
		
		if (element.getNodeName().equals("path")) {
			if (!"arc".equals(element.getAttributeNS(Util.SODIPODI, "type"))) {
				return true;
			}
		}
		
		return false;
	}
}
