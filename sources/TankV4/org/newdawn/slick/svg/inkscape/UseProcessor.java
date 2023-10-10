package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class UseProcessor implements ElementProcessor {
   public boolean handles(Element var1) {
      return var1.getNodeName().equals("use");
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      String var5 = var2.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      String var6 = Util.getAsReference(var5);
      Figure var7 = var3.getFigureByID(var6);
      if (var7 == null) {
         throw new ParsingException(var2, "Unable to locate referenced element: " + var6);
      } else {
         Transform var8 = Util.getTransform(var2);
         Transform var9 = var8.concatenate(var7.getTransform());
         NonGeometricData var10 = Util.getNonGeometricData(var2);
         Shape var11 = var7.getShape().transform(var9);
         var10.addAttribute("fill", var7.getData().getAttribute("fill"));
         var10.addAttribute("stroke", var7.getData().getAttribute("stroke"));
         var10.addAttribute("opacity", var7.getData().getAttribute("opacity"));
         var10.addAttribute("stroke-width", var7.getData().getAttribute("stroke-width"));
         Figure var12 = new Figure(var7.getType(), var11, var10, var9);
         var3.addFigure(var12);
      }
   }
}
