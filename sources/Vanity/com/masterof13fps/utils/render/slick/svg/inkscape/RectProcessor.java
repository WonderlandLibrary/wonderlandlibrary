package com.masterof13fps.utils.render.slick.svg.inkscape;

import com.masterof13fps.utils.render.slick.geom.Rectangle;
import com.masterof13fps.utils.render.slick.geom.Shape;
import com.masterof13fps.utils.render.slick.geom.Transform;
import com.masterof13fps.utils.render.slick.svg.Diagram;
import com.masterof13fps.utils.render.slick.svg.Figure;
import com.masterof13fps.utils.render.slick.svg.Loader;
import com.masterof13fps.utils.render.slick.svg.NonGeometricData;
import com.masterof13fps.utils.render.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * A processor for the <rect> element.
 *
 * @author kevin
 */
public class RectProcessor implements ElementProcessor {

	/**
	 * @see ElementProcessor#process(Loader, Element, Diagram, Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
		Transform transform = Util.getTransform(element);
	    transform = new Transform(t, transform); 
		
		float width = Float.parseFloat(element.getAttribute("width"));
		float height = Float.parseFloat(element.getAttribute("height"));
		float x = Float.parseFloat(element.getAttribute("x"));
		float y = Float.parseFloat(element.getAttribute("y"));
		
		Rectangle rect = new Rectangle(x,y,width+1,height+1);
		Shape shape = rect.transform(transform);
		
		NonGeometricData data = Util.getNonGeometricData(element);
		data.addAttribute("width", ""+width);
		data.addAttribute("height", ""+height);
		data.addAttribute("x", ""+x);
		data.addAttribute("y", ""+y);
		
		diagram.addFigure(new Figure(Figure.RECTANGLE, shape, data, transform));
	}

	/**
	 * @see ElementProcessor#handles(Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("rect")) {
			return true;
		}
		
		return false;
	}
}
