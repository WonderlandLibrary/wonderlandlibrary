package org.newdawn.slick.svg.inkscape;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Gradient;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DefsProcessor implements ElementProcessor {
   public boolean handles(Element var1) {
      return var1.getNodeName().equals("defs");
   }

   public void process(Loader var1, Element var2, Diagram var3, Transform var4) throws ParsingException {
      NodeList var5 = var2.getElementsByTagName("pattern");

      NodeList var8;
      Element var9;
      String var10;
      String var11;
      for(int var6 = 0; var6 < var5.getLength(); ++var6) {
         Element var7 = (Element)var5.item(var6);
         var8 = var7.getElementsByTagName("image");
         if (var8.getLength() == 0) {
            Log.warn("Pattern 1981 does not specify an image. Only image patterns are supported.");
         } else {
            var9 = (Element)var8.item(0);
            var10 = var7.getAttribute("id");
            var11 = var9.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            var3.addPatternDef(var10, var11);
         }
      }

      NodeList var22 = var2.getElementsByTagName("linearGradient");
      ArrayList var23 = new ArrayList();

      String var18;
      for(int var24 = 0; var24 < var22.getLength(); ++var24) {
         var9 = (Element)var22.item(var24);
         var10 = var9.getAttribute("id");
         Gradient var27 = new Gradient(var10, false);
         var27.setTransform(Util.getTransform(var9, "gradientTransform"));
         if (this.stringLength(var9.getAttribute("x1")) > 0) {
            var27.setX1(Float.parseFloat(var9.getAttribute("x1")));
         }

         if (this.stringLength(var9.getAttribute("x2")) > 0) {
            var27.setX2(Float.parseFloat(var9.getAttribute("x2")));
         }

         if (this.stringLength(var9.getAttribute("y1")) > 0) {
            var27.setY1(Float.parseFloat(var9.getAttribute("y1")));
         }

         if (this.stringLength(var9.getAttribute("y2")) > 0) {
            var27.setY2(Float.parseFloat(var9.getAttribute("y2")));
         }

         String var12 = var9.getAttributeNS("http://www.w3.org/1999/xlink", "href");
         if (this.stringLength(var12) > 0) {
            var27.reference(var12.substring(1));
            var23.add(var27);
         } else {
            NodeList var13 = var9.getElementsByTagName("stop");

            for(int var14 = 0; var14 < var13.getLength(); ++var14) {
               Element var15 = (Element)var13.item(var14);
               float var16 = Float.parseFloat(var15.getAttribute("offset"));
               String var17 = Util.extractStyle(var15.getAttribute("style"), "stop-color");
               var18 = Util.extractStyle(var15.getAttribute("style"), "stop-opacity");
               int var19 = Integer.parseInt(var17.substring(1), 16);
               Color var20 = new Color(var19);
               var20.a = Float.parseFloat(var18);
               var27.addStep(var16, var20);
            }

            var27.getImage();
         }

         var3.addGradient(var10, var27);
      }

      var8 = var2.getElementsByTagName("radialGradient");

      int var25;
      for(var25 = 0; var25 < var8.getLength(); ++var25) {
         Element var26 = (Element)var8.item(var25);
         var11 = var26.getAttribute("id");
         Gradient var28 = new Gradient(var11, true);
         var28.setTransform(Util.getTransform(var26, "gradientTransform"));
         if (this.stringLength(var26.getAttribute("cx")) > 0) {
            var28.setX1(Float.parseFloat(var26.getAttribute("cx")));
         }

         if (this.stringLength(var26.getAttribute("cy")) > 0) {
            var28.setY1(Float.parseFloat(var26.getAttribute("cy")));
         }

         if (this.stringLength(var26.getAttribute("fx")) > 0) {
            var28.setX2(Float.parseFloat(var26.getAttribute("fx")));
         }

         if (this.stringLength(var26.getAttribute("fy")) > 0) {
            var28.setY2(Float.parseFloat(var26.getAttribute("fy")));
         }

         if (this.stringLength(var26.getAttribute("r")) > 0) {
            var28.setR(Float.parseFloat(var26.getAttribute("r")));
         }

         String var29 = var26.getAttributeNS("http://www.w3.org/1999/xlink", "href");
         if (this.stringLength(var29) > 0) {
            var28.reference(var29.substring(1));
            var23.add(var28);
         } else {
            NodeList var30 = var26.getElementsByTagName("stop");

            for(int var31 = 0; var31 < var30.getLength(); ++var31) {
               Element var32 = (Element)var30.item(var31);
               float var33 = Float.parseFloat(var32.getAttribute("offset"));
               var18 = Util.extractStyle(var32.getAttribute("style"), "stop-color");
               String var34 = Util.extractStyle(var32.getAttribute("style"), "stop-opacity");
               int var35 = Integer.parseInt(var18.substring(1), 16);
               Color var21 = new Color(var35);
               var21.a = Float.parseFloat(var34);
               var28.addStep(var33, var21);
            }

            var28.getImage();
         }

         var3.addGradient(var11, var28);
      }

      for(var25 = 0; var25 < var23.size(); ++var25) {
         ((Gradient)var23.get(var25)).resolve(var3);
         ((Gradient)var23.get(var25)).getImage();
      }

   }

   private int stringLength(String var1) {
      return var1 == null ? 0 : var1.length();
   }
}
