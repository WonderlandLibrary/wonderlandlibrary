package org.newdawn.slick.util.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLElement {
   private Element dom;
   private XMLElementList children;
   private String name;

   XMLElement(Element var1) {
      this.dom = var1;
      this.name = this.dom.getTagName();
   }

   public String[] getAttributeNames() {
      NamedNodeMap var1 = this.dom.getAttributes();
      String[] var2 = new String[var1.getLength()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1.item(var3).getNodeName();
      }

      return var2;
   }

   public String getName() {
      return this.name;
   }

   public String getAttribute(String var1) {
      return this.dom.getAttribute(var1);
   }

   public String getAttribute(String var1, String var2) {
      String var3 = this.dom.getAttribute(var1);
      return var3 != null && var3.length() != 0 ? var3 : var2;
   }

   public int getIntAttribute(String var1) throws SlickXMLException {
      try {
         return Integer.parseInt(this.getAttribute(var1));
      } catch (NumberFormatException var3) {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1) + "' is not an integer", var3);
      }
   }

   public int getIntAttribute(String var1, int var2) throws SlickXMLException {
      try {
         return Integer.parseInt(this.getAttribute(var1, "" + var2));
      } catch (NumberFormatException var4) {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1, "" + var2) + "' is not an integer", var4);
      }
   }

   public double getDoubleAttribute(String var1) throws SlickXMLException {
      try {
         return Double.parseDouble(this.getAttribute(var1));
      } catch (NumberFormatException var3) {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1) + "' is not a double", var3);
      }
   }

   public double getDoubleAttribute(String var1, double var2) throws SlickXMLException {
      try {
         return Double.parseDouble(this.getAttribute(var1, "" + var2));
      } catch (NumberFormatException var5) {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1, "" + var2) + "' is not a double", var5);
      }
   }

   public boolean getBooleanAttribute(String var1) throws SlickXMLException {
      String var2 = this.getAttribute(var1);
      if (var2.equalsIgnoreCase("true")) {
         return true;
      } else if (var2.equalsIgnoreCase("false")) {
         return false;
      } else {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1) + "' is not a boolean");
      }
   }

   public boolean getBooleanAttribute(String var1, boolean var2) throws SlickXMLException {
      String var3 = this.getAttribute(var1, "" + var2);
      if (var3.equalsIgnoreCase("true")) {
         return true;
      } else if (var3.equalsIgnoreCase("false")) {
         return false;
      } else {
         throw new SlickXMLException("Value read: '" + this.getAttribute(var1, "" + var2) + "' is not a boolean");
      }
   }

   public String getContent() {
      String var1 = "";
      NodeList var2 = this.dom.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         if (var2.item(var3) instanceof Text) {
            var1 = var1 + var2.item(var3).getNodeValue();
         }
      }

      return var1;
   }

   public XMLElementList getChildren() {
      if (this.children != null) {
         return this.children;
      } else {
         NodeList var1 = this.dom.getChildNodes();
         this.children = new XMLElementList();

         for(int var2 = 0; var2 < var1.getLength(); ++var2) {
            if (var1.item(var2) instanceof Element) {
               this.children.add(new XMLElement((Element)var1.item(var2)));
            }
         }

         return this.children;
      }
   }

   public XMLElementList getChildrenByName(String var1) {
      XMLElementList var2 = new XMLElementList();
      XMLElementList var3 = this.getChildren();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         if (var3.get(var4).getName().equals(var1)) {
            var2.add(var3.get(var4));
         }
      }

      return var2;
   }

   public String toString() {
      String var1 = "[XML " + this.getName();
      String[] var2 = this.getAttributeNames();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1 = var1 + " " + var2[var3] + "=" + this.getAttribute(var2[var3]);
      }

      var1 = var1 + "]";
      return var1;
   }
}
