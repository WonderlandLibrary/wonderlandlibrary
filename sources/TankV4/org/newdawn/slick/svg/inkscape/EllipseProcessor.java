package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class EllipseProcessor implements ElementProcessor {
   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      float var6 = Util.getFloatAttribute(var2, "cx");
      float var7 = Util.getFloatAttribute(var2, "cy");
      float var8 = Util.getFloatAttribute(var2, "rx");
      float var9 = Util.getFloatAttribute(var2, "ry");
      Ellipse var10 = new Ellipse(var6, var7, var8, var9);
      Shape var11 = var10.transform(var5);
      NonGeometricData var12 = Util.getNonGeometricData(var2);
      var12.addAttribute("cx", "" + var6);
      var12.addAttribute("cy", "" + var7);
      var12.addAttribute("rx", "" + var8);
      var12.addAttribute("ry", "" + var9);
      var3.addFigure(new Figure(1, var11, var12, var5));
   }

   public boolean handles(Element var1) {
      if (var1.getNodeName().equals("ellipse")) {
         return true;
      } else {
         return var1.getNodeName().equals("path") && "arc".equals(var1.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
      }
   }
}
