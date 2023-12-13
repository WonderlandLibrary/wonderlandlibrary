package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.svg.NonGeometricData;
import org.w3c.dom.Element;

public class InkscapeNonGeometricData extends NonGeometricData {
   private Element element;

   public InkscapeNonGeometricData(String var1, Element var2) {
      super(var1);
      this.element = var2;
   }

   public String getAttribute(String var1) {
      String var2 = super.getAttribute(var1);
      if (var2 == null) {
         var2 = this.element.getAttribute(var1);
      }

      return var2;
   }

   public Element getElement() {
      return this.element;
   }
}
