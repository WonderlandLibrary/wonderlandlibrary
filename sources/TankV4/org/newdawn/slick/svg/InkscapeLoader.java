package org.newdawn.slick.svg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.inkscape.DefsProcessor;
import org.newdawn.slick.svg.inkscape.ElementProcessor;
import org.newdawn.slick.svg.inkscape.EllipseProcessor;
import org.newdawn.slick.svg.inkscape.GroupProcessor;
import org.newdawn.slick.svg.inkscape.LineProcessor;
import org.newdawn.slick.svg.inkscape.PathProcessor;
import org.newdawn.slick.svg.inkscape.PolygonProcessor;
import org.newdawn.slick.svg.inkscape.RectProcessor;
import org.newdawn.slick.svg.inkscape.UseProcessor;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class InkscapeLoader implements Loader {
   public static int RADIAL_TRIANGULATION_LEVEL = 1;
   private static ArrayList processors = new ArrayList();
   private Diagram diagram;

   public static void addElementProcessor(ElementProcessor var0) {
      processors.add(var0);
   }

   public static Diagram load(String var0, boolean var1) throws SlickException {
      return load(ResourceLoader.getResourceAsStream(var0), var1);
   }

   public static Diagram load(String var0) throws SlickException {
      return load(ResourceLoader.getResourceAsStream(var0), false);
   }

   public static Diagram load(InputStream var0, boolean var1) throws SlickException {
      return (new InkscapeLoader()).loadDiagram(var0, var1);
   }

   private InkscapeLoader() {
   }

   private Diagram loadDiagram(InputStream var1) throws SlickException {
      return this.loadDiagram(var1, false);
   }

   private Diagram loadDiagram(InputStream var1, boolean var2) throws SlickException {
      try {
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         var3.setValidating(false);
         var3.setNamespaceAware(true);
         DocumentBuilder var4 = var3.newDocumentBuilder();
         var4.setEntityResolver(new EntityResolver(this) {
            private final InkscapeLoader this$0;

            {
               this.this$0 = var1;
            }

            public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
               return new InputSource(new ByteArrayInputStream(new byte[0]));
            }
         });
         Document var5 = var4.parse(var1);
         Element var6 = var5.getDocumentElement();

         String var7;
         for(var7 = var6.getAttribute("width"); Character.isLetter(var7.charAt(var7.length() - 1)); var7 = var7.substring(0, var7.length() - 1)) {
         }

         String var8;
         for(var8 = var6.getAttribute("height"); Character.isLetter(var8.charAt(var8.length() - 1)); var8 = var8.substring(0, var8.length() - 1)) {
         }

         float var9 = Float.parseFloat(var7);
         float var10 = Float.parseFloat(var8);
         this.diagram = new Diagram(var9, var10);
         if (!var2) {
            var10 = 0.0F;
         }

         this.loadChildren(var6, Transform.createTranslateTransform(0.0F, -var10));
         return this.diagram;
      } catch (Exception var11) {
         throw new SlickException("Failed to load inkscape document", var11);
      }
   }

   public void loadChildren(Element var1, Transform var2) throws ParsingException {
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         if (var3.item(var4) instanceof Element) {
            this.loadElement((Element)var3.item(var4), var2);
         }
      }

   }

   private void loadElement(Element var1, Transform var2) throws ParsingException {
      for(int var3 = 0; var3 < processors.size(); ++var3) {
         ElementProcessor var4 = (ElementProcessor)processors.get(var3);
         if (var4.handles(var1)) {
            var4.process(this, var1, this.diagram, var2);
         }
      }

   }

   static {
      addElementProcessor(new RectProcessor());
      addElementProcessor(new EllipseProcessor());
      addElementProcessor(new PolygonProcessor());
      addElementProcessor(new PathProcessor());
      addElementProcessor(new LineProcessor());
      addElementProcessor(new GroupProcessor());
      addElementProcessor(new DefsProcessor());
      addElementProcessor(new UseProcessor());
   }
}
