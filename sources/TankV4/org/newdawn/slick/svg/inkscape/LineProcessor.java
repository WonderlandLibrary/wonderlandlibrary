package org.newdawn.slick.svg.inkscape;

import java.util.StringTokenizer;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class LineProcessor implements ElementProcessor {
   private static int processPoly(Polygon var0, Element var1, StringTokenizer var2) throws ParsingException {
      int var3 = 0;

      while(var2.hasMoreTokens()) {
         String var4 = var2.nextToken();
         if (!var4.equals("L")) {
            if (var4.equals("z")) {
               break;
            }

            if (!var4.equals("M")) {
               if (var4.equals("C")) {
                  return 0;
               }

               String var5 = var4;
               String var6 = var2.nextToken();

               try {
                  float var7 = Float.parseFloat(var5);
                  float var8 = Float.parseFloat(var6);
                  var0.addPoint(var7, var8);
                  ++var3;
               } catch (NumberFormatException var9) {
                  throw new ParsingException(var1.getAttribute("id"), "Invalid token in points list", var9);
               }
            }
         }
      }

      return var3;
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      float var6;
      float var7;
      float var8;
      float var9;
      if (var2.getNodeName().equals("line")) {
         var6 = Float.parseFloat(var2.getAttribute("x1"));
         var8 = Float.parseFloat(var2.getAttribute("x2"));
         var7 = Float.parseFloat(var2.getAttribute("y1"));
         var9 = Float.parseFloat(var2.getAttribute("y2"));
      } else {
         String var10 = var2.getAttribute("d");
         StringTokenizer var11 = new StringTokenizer(var10, ", ");
         Polygon var12 = new Polygon();
         if (processPoly(var12, var2, var11) != 2) {
            return;
         }

         var6 = var12.getPoint(0)[0];
         var7 = var12.getPoint(0)[1];
         var8 = var12.getPoint(1)[0];
         var9 = var12.getPoint(1)[1];
      }

      float[] var14 = new float[]{var6, var7, var8, var9};
      float[] var15 = new float[4];
      var5.transform(var14, 0, var15, 0, 2);
      Line var16 = new Line(var15[0], var15[1], var15[2], var15[3]);
      NonGeometricData var13 = Util.getNonGeometricData(var2);
      var13.addAttribute("x1", "" + var6);
      var13.addAttribute("x2", "" + var8);
      var13.addAttribute("y1", "" + var7);
      var13.addAttribute("y2", "" + var9);
      var3.addFigure(new Figure(2, var16, var13, var5));
   }

   public boolean handles(Element var1) {
      if (var1.getNodeName().equals("line")) {
         return true;
      } else {
         return var1.getNodeName().equals("path") && !"arc".equals(var1.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
      }
   }
}
