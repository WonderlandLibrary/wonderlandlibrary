package com.masterof13fps.utils.render.slick.svg.inkscape;

import com.masterof13fps.utils.render.slick.geom.Transform;
import com.masterof13fps.utils.render.slick.svg.Diagram;
import com.masterof13fps.utils.render.slick.svg.Loader;
import com.masterof13fps.utils.render.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * TODO: Document this class
 *
 * @author kevin
 */
public class GroupProcessor implements ElementProcessor {

	/**
	 * @see ElementProcessor#handles(Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("g")) {
			return true;
		}
		return false;
	}

	/**O
	 * @see ElementProcessor#process(Loader, Element, Diagram, Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
		Transform transform = Util.getTransform(element);
		transform = new Transform(t, transform);
		
		loader.loadChildren(element, transform);
	}

}
