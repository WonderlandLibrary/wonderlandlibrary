package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class GroupProcessor implements ElementProcessor {
   public boolean handles(Element var1) {
      return var1.getNodeName().equals("g");
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      var1.loadChildren(var2, var5);
   }
}
