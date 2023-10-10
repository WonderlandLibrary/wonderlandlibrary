package org.newdawn.slick.svg.inkscape;

import java.util.StringTokenizer;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class Util {
   public static final String INKSCAPE = "http://www.inkscape.org/namespaces/inkscape";
   public static final String SODIPODI = "http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd";
   public static final String XLINK = "http://www.w3.org/1999/xlink";

   static NonGeometricData getNonGeometricData(Element var0) {
      String var1 = getMetaData(var0);
      InkscapeNonGeometricData var2 = new InkscapeNonGeometricData(var1, var0);
      var2.addAttribute("id", var0.getAttribute("id"));
      var2.addAttribute("fill", getStyle(var0, "fill"));
      var2.addAttribute("stroke", getStyle(var0, "stroke"));
      var2.addAttribute("opacity", getStyle(var0, "opacity"));
      var2.addAttribute("stroke-dasharray", getStyle(var0, "stroke-dasharray"));
      var2.addAttribute("stroke-dashoffset", getStyle(var0, "stroke-dashoffset"));
      var2.addAttribute("stroke-miterlimit", getStyle(var0, "stroke-miterlimit"));
      var2.addAttribute("stroke-opacity", getStyle(var0, "stroke-opacity"));
      var2.addAttribute("stroke-width", getStyle(var0, "stroke-width"));
      return var2;
   }

   static String getMetaData(Element var0) {
      String var1 = var0.getAttributeNS("http://www.inkscape.org/namespaces/inkscape", "label");
      return var1 != null && !var1.equals("") ? var1 : var0.getAttribute("id");
   }

   static String getStyle(Element var0, String var1) {
      String var2 = var0.getAttribute(var1);
      if (var2 != null && var2.length() > 0) {
         return var2;
      } else {
         String var3 = var0.getAttribute("style");
         return extractStyle(var3, var1);
      }
   }

   static String extractStyle(String var0, String var1) {
      if (var0 == null) {
         return "";
      } else {
         StringTokenizer var2 = new StringTokenizer(var0, ";");

         String var3;
         String var4;
         do {
            if (!var2.hasMoreTokens()) {
               return "";
            }

            var3 = var2.nextToken();
            var4 = var3.substring(0, var3.indexOf(58));
         } while(!var4.equals(var1));

         return var3.substring(var3.indexOf(58) + 1);
      }
   }

   static Transform getTransform(Element var0) {
      return getTransform(var0, "transform");
   }

   static Transform getTransform(Element var0, String var1) {
      String var2 = var0.getAttribute(var1);
      if (var2 == null) {
         return new Transform();
      } else if (var2.equals("")) {
         return new Transform();
      } else if (var2.startsWith("translate")) {
         var2 = var2.substring(0, var2.length() - 1);
         var2 = var2.substring(10);
         StringTokenizer var7 = new StringTokenizer(var2, ", ");
         float var8 = Float.parseFloat(var7.nextToken());
         float var9 = Float.parseFloat(var7.nextToken());
         return Transform.createTranslateTransform(var8, var9);
      } else if (!var2.startsWith("matrix")) {
         return new Transform();
      } else {
         float[] var3 = new float[6];
         var2 = var2.substring(0, var2.length() - 1);
         var2 = var2.substring(7);
         StringTokenizer var4 = new StringTokenizer(var2, ", ");
         float[] var5 = new float[6];

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = Float.parseFloat(var4.nextToken());
         }

         var3[0] = var5[0];
         var3[1] = var5[2];
         var3[2] = var5[4];
         var3[3] = var5[1];
         var3[4] = var5[3];
         var3[5] = var5[5];
         return new Transform(var3);
      }
   }

   static float getFloatAttribute(Element var0, String var1) throws ParsingException {
      String var2 = var0.getAttribute(var1);
      if (var2 == null || var2.equals("")) {
         var2 = var0.getAttributeNS("http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd", var1);
      }

      try {
         return Float.parseFloat(var2);
      } catch (NumberFormatException var4) {
         throw new ParsingException(var0, "Invalid value for: " + var1, var4);
      }
   }

   public static String getAsReference(String var0) {
      if (var0.length() < 2) {
         return "";
      } else {
         var0 = var0.substring(1, var0.length());
         return var0;
      }
   }
}
