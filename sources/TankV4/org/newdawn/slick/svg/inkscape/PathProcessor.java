package org.newdawn.slick.svg.inkscape;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class PathProcessor implements ElementProcessor {
   private static Path processPoly(Element var0, StringTokenizer var1) throws ParsingException {
      boolean var2 = false;
      new ArrayList();
      boolean var4 = false;
      boolean var5 = false;
      Path var6 = null;

      while(var1.hasMoreTokens()) {
         try {
            String var7 = var1.nextToken();
            float var8;
            float var9;
            if (var7.equals("L")) {
               var8 = Float.parseFloat(var1.nextToken());
               var9 = Float.parseFloat(var1.nextToken());
               var6.lineTo(var8, var9);
            } else if (var7.equals("z")) {
               var6.close();
            } else if (var7.equals("M")) {
               if (!var4) {
                  var4 = true;
                  var8 = Float.parseFloat(var1.nextToken());
                  var9 = Float.parseFloat(var1.nextToken());
                  var6 = new Path(var8, var9);
               } else {
                  var5 = true;
                  var8 = Float.parseFloat(var1.nextToken());
                  var9 = Float.parseFloat(var1.nextToken());
                  var6.startHole(var8, var9);
               }
            } else if (var7.equals("C")) {
               var5 = true;
               var8 = Float.parseFloat(var1.nextToken());
               var9 = Float.parseFloat(var1.nextToken());
               float var10 = Float.parseFloat(var1.nextToken());
               float var11 = Float.parseFloat(var1.nextToken());
               float var12 = Float.parseFloat(var1.nextToken());
               float var13 = Float.parseFloat(var1.nextToken());
               var6.curveTo(var12, var13, var8, var9, var10, var11);
            }
         } catch (NumberFormatException var14) {
            throw new ParsingException(var0.getAttribute("id"), "Invalid token in points list", var14);
         }
      }

      if (!var5) {
         return null;
      } else {
         return var6;
      }
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      Transform var5 = Util.getTransform(var2);
      var5 = new Transform(var4, var5);
      String var6 = var2.getAttribute("points");
      if (var2.getNodeName().equals("path")) {
         var6 = var2.getAttribute("d");
      }

      StringTokenizer var7 = new StringTokenizer(var6, ", ");
      Path var8 = processPoly(var2, var7);
      NonGeometricData var9 = Util.getNonGeometricData(var2);
      if (var8 != null) {
         Shape var10 = var8.transform(var5);
         var3.addFigure(new Figure(4, var10, var9, var5));
      }

   }

   public boolean handles(Element var1) {
      return var1.getNodeName().equals("path") && !"arc".equals(var1.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", "type"));
   }
}
