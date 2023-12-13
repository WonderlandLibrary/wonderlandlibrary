package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Transform;
import org.w3c.dom.Element;

public interface Loader {
   void loadChildren(Element var1, Transform var2) throws ParsingException;
}
