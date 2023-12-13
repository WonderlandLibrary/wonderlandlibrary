package org.newdawn.slick.util.xml;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;

public class XMLParser {
   private static DocumentBuilderFactory factory;

   public XMLElement parse(String var1) throws SlickException {
      return this.parse(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public XMLElement parse(String var1, InputStream var2) throws SlickXMLException {
      try {
         if (factory == null) {
            factory = DocumentBuilderFactory.newInstance();
         }

         DocumentBuilder var3 = factory.newDocumentBuilder();
         Document var4 = var3.parse(var2);
         return new XMLElement(var4.getDocumentElement());
      } catch (Exception var5) {
         throw new SlickXMLException("Failed to parse document: " + var1, var5);
      }
   }
}
