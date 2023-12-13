package org.newdawn.slick;

import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLPackedSheet {
   private Image image;
   private HashMap sprites = new HashMap();

   public XMLPackedSheet(String var1, String var2) throws SlickException {
      this.image = new Image(var1, false, 2);

      try {
         DocumentBuilder var3 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document var4 = var3.parse(ResourceLoader.getResourceAsStream(var2));
         NodeList var5 = var4.getElementsByTagName("sprite");

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            Element var7 = (Element)var5.item(var6);
            String var8 = var7.getAttribute("name");
            int var9 = Integer.parseInt(var7.getAttribute("x"));
            int var10 = Integer.parseInt(var7.getAttribute("y"));
            int var11 = Integer.parseInt(var7.getAttribute("width"));
            int var12 = Integer.parseInt(var7.getAttribute("height"));
            this.sprites.put(var8, this.image.getSubImage(var9, var10, var11, var12));
         }

      } catch (Exception var13) {
         throw new SlickException("Failed to parse sprite sheet XML", var13);
      }
   }

   public Image getSprite(String var1) {
      return (Image)this.sprites.get(var1);
   }
}
