package com.masterof13fps.utils.render.slick.svg.inkscape;

import com.masterof13fps.utils.render.slick.geom.Shape;
import com.masterof13fps.utils.render.slick.geom.Transform;
import com.masterof13fps.utils.render.slick.svg.Diagram;
import com.masterof13fps.utils.render.slick.svg.Figure;
import com.masterof13fps.utils.render.slick.svg.Loader;
import com.masterof13fps.utils.render.slick.svg.NonGeometricData;
import com.masterof13fps.utils.render.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * Processor for the "use", a tag that allows references to other elements
 * and cloning.
 * 
 * @author kevin
 */
public class UseProcessor implements ElementProcessor {

	/**
	 * @see ElementProcessor#handles(Element)
	 */
	public boolean handles(Element element) {
		return element.getNodeName().equals("use");
	}

	/**
	 * @see ElementProcessor#process(Loader, Element, Diagram, Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram,
			Transform transform) throws ParsingException {

		String ref = element.getAttributeNS("http://www.w3.org/1999/xlink", "href");
		String href = Util.getAsReference(ref);
		
		Figure referenced = diagram.getFigureByID(href);
		if (referenced == null) {
			throw new ParsingException(element, "Unable to locate referenced element: "+href);
		}
		
		Transform local = Util.getTransform(element);
		Transform trans = local.concatenate(referenced.getTransform());
		
		NonGeometricData data = Util.getNonGeometricData(element);
		Shape shape = referenced.getShape().transform(trans);
		data.addAttribute(NonGeometricData.FILL, referenced.getData().getAttribute(NonGeometricData.FILL));
		data.addAttribute(NonGeometricData.STROKE, referenced.getData().getAttribute(NonGeometricData.STROKE));
		data.addAttribute(NonGeometricData.OPACITY, referenced.getData().getAttribute(NonGeometricData.OPACITY));
		data.addAttribute(NonGeometricData.STROKE_WIDTH, referenced.getData().getAttribute(NonGeometricData.STROKE_WIDTH));
		
		Figure figure = new Figure(referenced.getType(), shape, data, trans);
		diagram.addFigure(figure);
	}

}
