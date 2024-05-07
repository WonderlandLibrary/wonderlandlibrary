package com.masterof13fps.utils.render.slick.svg;

import com.masterof13fps.utils.render.slick.geom.Transform;
import org.w3c.dom.Element;

/**
 * Description of a simple XML loader
 *
 * @author kevin
 */
public interface Loader {
	/**
	 * Load the children of a given element
	 * 
	 * @param element The element whose children should be loaded
	 * @param t The transform to apply to all the children
	 * @throws ParsingException Indicates a failure to read the XML
	 */
	public void loadChildren(Element element, Transform t) throws ParsingException;
}
