package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class RectProcessor implements ElementProcessor {
   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      float var6 = Float.parseFloat(var2.getAttribute("width"));
      float var7 = Float.parseFloat(var2.getAttribute("height"));
      float var8 = Float.parseFloat(var2.getAttribute("x"));
      float var9 = Float.parseFloat(var2.getAttribute("y"));
      Rectangle var10 = new Rectangle(var8, var9, var6 + 1.0F, var7 + 1.0F);
      Shape var11 = var10.transform(var5);
      NonGeometricData var12 = Util.getNonGeometricData(var2);
      var12.addAttribute("width", "" + var6);
      var12.addAttribute("height", "" + var7);
      var12.addAttribute("x", "" + var8);
      var12.addAttribute("y", "" + var9);
      var3.addFigure(new Figure(3, var11, var12, var5));
   }

   public boolean handles(Element var1) {
      return var1.getNodeName().equals("rect");
   }
}
