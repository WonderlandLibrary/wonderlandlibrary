package org.newdawn.slick.svg.inkscape;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class PolygonProcessor implements ElementProcessor {
   private static int processPoly(Polygon var0, Element var1, StringTokenizer var2) throws ParsingException {
      int var3 = 0;
      new ArrayList();
      boolean var5 = false;
      boolean var6 = false;

      while(var2.hasMoreTokens()) {
         String var7 = var2.nextToken();
         if (!var7.equals("L")) {
            if (var7.equals("z")) {
               var6 = true;
               break;
            }

            if (var7.equals("M")) {
               if (var5) {
                  return 0;
               }

               var5 = true;
            } else {
               if (var7.equals("C")) {
                  return 0;
               }

               String var8 = var7;
               String var9 = var2.nextToken();

               try {
                  float var10 = Float.parseFloat(var8);
                  float var11 = Float.parseFloat(var9);
                  var0.addPoint(var10, var11);
                  ++var3;
               } catch (NumberFormatException var12) {
                  throw new ParsingException(var1.getAttribute("id"), "Invalid token in points list", var12);
               }
            }
         }
      }

      var0.setClosed(var6);
      return var3;
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      String var6 = var2.getAttribute("points");
      if (var2.getNodeName().equals("path")) {
         var6 = var2.getAttribute("d");
      }

      StringTokenizer var7 = new StringTokenizer(var6, ", ");
      Polygon var8 = new Polygon();
      int var9 = processPoly(var8, var2, var7);
      NonGeometricData var10 = Util.getNonGeometricData(var2);
      if (var9 > 3) {
         Shape var11 = var8.transform(var5);
         var3.addFigure(new Figure(5, var11, var10, var5));
      }

   }

   public boolean handles(Element var1) {
      if (var1.getNodeName().equals("polygon")) {
         return true;
      } else {
         return var1.getNodeName().equals("path") && !"arc".equals(var1.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
      }
   }
}
