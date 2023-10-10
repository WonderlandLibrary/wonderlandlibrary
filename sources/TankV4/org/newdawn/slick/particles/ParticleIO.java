package org.newdawn.slick.particles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParticleIO {
   public static ParticleSystem loadConfiguredSystem(String var0, Color var1) throws IOException {
      return loadConfiguredSystem(ResourceLoader.getResourceAsStream(var0), (ConfigurableEmitterFactory)null, (ParticleSystem)null, var1);
   }

   public static ParticleSystem loadConfiguredSystem(String var0) throws IOException {
      return loadConfiguredSystem(ResourceLoader.getResourceAsStream(var0), (ConfigurableEmitterFactory)null, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(File var0) throws IOException {
      return loadConfiguredSystem(new FileInputStream(var0), (ConfigurableEmitterFactory)null, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(InputStream var0, Color var1) throws IOException {
      return loadConfiguredSystem(var0, (ConfigurableEmitterFactory)null, (ParticleSystem)null, var1);
   }

   public static ParticleSystem loadConfiguredSystem(InputStream var0) throws IOException {
      return loadConfiguredSystem(var0, (ConfigurableEmitterFactory)null, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(String var0, ConfigurableEmitterFactory var1) throws IOException {
      return loadConfiguredSystem(ResourceLoader.getResourceAsStream(var0), var1, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(File var0, ConfigurableEmitterFactory var1) throws IOException {
      return loadConfiguredSystem(new FileInputStream(var0), var1, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(InputStream var0, ConfigurableEmitterFactory var1) throws IOException {
      return loadConfiguredSystem(var0, var1, (ParticleSystem)null, (Color)null);
   }

   public static ParticleSystem loadConfiguredSystem(InputStream var0, ConfigurableEmitterFactory var1, ParticleSystem var2, Color var3) throws IOException {
      if (var1 == null) {
         var1 = new ConfigurableEmitterFactory() {
            public ConfigurableEmitter createEmitter(String var1) {
               return new ConfigurableEmitter(var1);
            }
         };
      }

      try {
         DocumentBuilder var4 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document var5 = var4.parse(var0);
         Element var6 = var5.getDocumentElement();
         if (!var6.getNodeName().equals("system")) {
            throw new IOException("Not a particle system file");
         } else {
            if (var2 == null) {
               var2 = new ParticleSystem("org/newdawn/slick/data/particle.tga", 2000, var3);
            }

            boolean var7 = "true".equals(var6.getAttribute("additive"));
            if (var7) {
               var2.setBlendingMode(1);
            } else {
               var2.setBlendingMode(2);
            }

            boolean var8 = "true".equals(var6.getAttribute("points"));
            var2.setUsePoints(var8);
            NodeList var9 = var6.getElementsByTagName("emitter");

            for(int var10 = 0; var10 < var9.getLength(); ++var10) {
               Element var11 = (Element)var9.item(var10);
               ConfigurableEmitter var12 = var1.createEmitter("new");
               elementToEmitter(var11, var12);
               var2.addEmitter(var12);
            }

            var2.setRemoveCompletedEmitters(false);
            return var2;
         }
      } catch (IOException var13) {
         Log.error((Throwable)var13);
         throw var13;
      } catch (Exception var14) {
         Log.error((Throwable)var14);
         throw new IOException("Unable to load particle system config");
      }
   }

   public static void saveConfiguredSystem(File var0, ParticleSystem var1) throws IOException {
      saveConfiguredSystem((OutputStream)(new FileOutputStream(var0)), var1);
   }

   public static void saveConfiguredSystem(OutputStream var0, ParticleSystem var1) throws IOException {
      try {
         DocumentBuilder var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document var3 = var2.newDocument();
         Element var4 = var3.createElement("system");
         var4.setAttribute("additive", "" + (var1.getBlendingMode() == 1));
         var4.setAttribute("points", "" + var1.usePoints());
         var3.appendChild(var4);

         for(int var5 = 0; var5 < var1.getEmitterCount(); ++var5) {
            ParticleEmitter var6 = var1.getEmitter(var5);
            if (!(var6 instanceof ConfigurableEmitter)) {
               throw new RuntimeException("Only ConfigurableEmitter instances can be stored");
            }

            Element var7 = emitterToElement(var3, (ConfigurableEmitter)var6);
            var4.appendChild(var7);
         }

         StreamResult var10 = new StreamResult(new OutputStreamWriter(var0, "utf-8"));
         DOMSource var11 = new DOMSource(var3);
         TransformerFactory var12 = TransformerFactory.newInstance();
         Transformer var8 = var12.newTransformer();
         var8.setOutputProperty("indent", "yes");
         var8.transform(var11, var10);
      } catch (Exception var9) {
         Log.error((Throwable)var9);
         throw new IOException("Unable to save configured particle system");
      }
   }

   public static ConfigurableEmitter loadEmitter(String var0) throws IOException {
      return loadEmitter((InputStream)ResourceLoader.getResourceAsStream(var0), (ConfigurableEmitterFactory)null);
   }

   public static ConfigurableEmitter loadEmitter(File var0) throws IOException {
      return loadEmitter((InputStream)(new FileInputStream(var0)), (ConfigurableEmitterFactory)null);
   }

   public static ConfigurableEmitter loadEmitter(InputStream var0) throws IOException {
      return loadEmitter((InputStream)var0, (ConfigurableEmitterFactory)null);
   }

   public static ConfigurableEmitter loadEmitter(String var0, ConfigurableEmitterFactory var1) throws IOException {
      return loadEmitter(ResourceLoader.getResourceAsStream(var0), var1);
   }

   public static ConfigurableEmitter loadEmitter(File var0, ConfigurableEmitterFactory var1) throws IOException {
      return loadEmitter((InputStream)(new FileInputStream(var0)), var1);
   }

   public static ConfigurableEmitter loadEmitter(InputStream var0, ConfigurableEmitterFactory var1) throws IOException {
      if (var1 == null) {
         var1 = new ConfigurableEmitterFactory() {
            public ConfigurableEmitter createEmitter(String var1) {
               return new ConfigurableEmitter(var1);
            }
         };
      }

      try {
         DocumentBuilder var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document var3 = var2.parse(var0);
         if (!var3.getDocumentElement().getNodeName().equals("emitter")) {
            throw new IOException("Not a particle emitter file");
         } else {
            ConfigurableEmitter var4 = var1.createEmitter("new");
            elementToEmitter(var3.getDocumentElement(), var4);
            return var4;
         }
      } catch (IOException var5) {
         Log.error((Throwable)var5);
         throw var5;
      } catch (Exception var6) {
         Log.error((Throwable)var6);
         throw new IOException("Unable to load emitter");
      }
   }

   public static void saveEmitter(File var0, ConfigurableEmitter var1) throws IOException {
      saveEmitter((OutputStream)(new FileOutputStream(var0)), var1);
   }

   public static void saveEmitter(OutputStream var0, ConfigurableEmitter var1) throws IOException {
      try {
         DocumentBuilder var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document var3 = var2.newDocument();
         var3.appendChild(emitterToElement(var3, var1));
         StreamResult var4 = new StreamResult(new OutputStreamWriter(var0, "utf-8"));
         DOMSource var5 = new DOMSource(var3);
         TransformerFactory var6 = TransformerFactory.newInstance();
         Transformer var7 = var6.newTransformer();
         var7.setOutputProperty("indent", "yes");
         var7.transform(var5, var4);
      } catch (Exception var8) {
         Log.error((Throwable)var8);
         throw new IOException("Failed to save emitter");
      }
   }

   private static Element getFirstNamedElement(Element var0, String var1) {
      NodeList var2 = var0.getElementsByTagName(var1);
      return var2.getLength() == 0 ? null : (Element)var2.item(0);
   }

   private static void elementToEmitter(Element var0, ConfigurableEmitter var1) {
      var1.name = var0.getAttribute("name");
      var1.setImageName(var0.getAttribute("imageName"));
      String var2 = var0.getAttribute("renderType");
      var1.usePoints = 1;
      if (var2.equals("quads")) {
         var1.usePoints = 3;
      }

      if (var2.equals("points")) {
         var1.usePoints = 2;
      }

      String var3 = var0.getAttribute("useOriented");
      if (var3 != null) {
         var1.useOriented = "true".equals(var3);
      }

      String var4 = var0.getAttribute("useAdditive");
      if (var4 != null) {
         var1.useAdditive = "true".equals(var4);
      }

      parseRangeElement(getFirstNamedElement(var0, "spawnInterval"), var1.spawnInterval);
      parseRangeElement(getFirstNamedElement(var0, "spawnCount"), var1.spawnCount);
      parseRangeElement(getFirstNamedElement(var0, "initialLife"), var1.initialLife);
      parseRangeElement(getFirstNamedElement(var0, "initialSize"), var1.initialSize);
      parseRangeElement(getFirstNamedElement(var0, "xOffset"), var1.xOffset);
      parseRangeElement(getFirstNamedElement(var0, "yOffset"), var1.yOffset);
      parseRangeElement(getFirstNamedElement(var0, "initialDistance"), var1.initialDistance);
      parseRangeElement(getFirstNamedElement(var0, "speed"), var1.speed);
      parseRangeElement(getFirstNamedElement(var0, "length"), var1.length);
      parseRangeElement(getFirstNamedElement(var0, "emitCount"), var1.emitCount);
      parseValueElement(getFirstNamedElement(var0, "spread"), var1.spread);
      parseValueElement(getFirstNamedElement(var0, "angularOffset"), var1.angularOffset);
      parseValueElement(getFirstNamedElement(var0, "growthFactor"), var1.growthFactor);
      parseValueElement(getFirstNamedElement(var0, "gravityFactor"), var1.gravityFactor);
      parseValueElement(getFirstNamedElement(var0, "windFactor"), var1.windFactor);
      parseValueElement(getFirstNamedElement(var0, "startAlpha"), var1.startAlpha);
      parseValueElement(getFirstNamedElement(var0, "endAlpha"), var1.endAlpha);
      parseValueElement(getFirstNamedElement(var0, "alpha"), var1.alpha);
      parseValueElement(getFirstNamedElement(var0, "size"), var1.size);
      parseValueElement(getFirstNamedElement(var0, "velocity"), var1.velocity);
      parseValueElement(getFirstNamedElement(var0, "scaleY"), var1.scaleY);
      Element var5 = getFirstNamedElement(var0, "color");
      NodeList var6 = var5.getElementsByTagName("step");
      var1.colors.clear();

      for(int var7 = 0; var7 < var6.getLength(); ++var7) {
         Element var8 = (Element)var6.item(var7);
         float var9 = Float.parseFloat(var8.getAttribute("offset"));
         float var10 = Float.parseFloat(var8.getAttribute("r"));
         float var11 = Float.parseFloat(var8.getAttribute("g"));
         float var12 = Float.parseFloat(var8.getAttribute("b"));
         var1.addColorPoint(var9, new Color(var10, var11, var12, 1.0F));
      }

      var1.replay();
   }

   private static Element emitterToElement(Document var0, ConfigurableEmitter var1) {
      Element var2 = var0.createElement("emitter");
      var2.setAttribute("name", var1.name);
      var2.setAttribute("imageName", var1.imageName == null ? "" : var1.imageName);
      var2.setAttribute("useOriented", var1.useOriented ? "true" : "false");
      var2.setAttribute("useAdditive", var1.useAdditive ? "true" : "false");
      if (var1.usePoints == 1) {
         var2.setAttribute("renderType", "inherit");
      }

      if (var1.usePoints == 2) {
         var2.setAttribute("renderType", "points");
      }

      if (var1.usePoints == 3) {
         var2.setAttribute("renderType", "quads");
      }

      var2.appendChild(createRangeElement(var0, "spawnInterval", var1.spawnInterval));
      var2.appendChild(createRangeElement(var0, "spawnCount", var1.spawnCount));
      var2.appendChild(createRangeElement(var0, "initialLife", var1.initialLife));
      var2.appendChild(createRangeElement(var0, "initialSize", var1.initialSize));
      var2.appendChild(createRangeElement(var0, "xOffset", var1.xOffset));
      var2.appendChild(createRangeElement(var0, "yOffset", var1.yOffset));
      var2.appendChild(createRangeElement(var0, "initialDistance", var1.initialDistance));
      var2.appendChild(createRangeElement(var0, "speed", var1.speed));
      var2.appendChild(createRangeElement(var0, "length", var1.length));
      var2.appendChild(createRangeElement(var0, "emitCount", var1.emitCount));
      var2.appendChild(createValueElement(var0, "spread", var1.spread));
      var2.appendChild(createValueElement(var0, "angularOffset", var1.angularOffset));
      var2.appendChild(createValueElement(var0, "growthFactor", var1.growthFactor));
      var2.appendChild(createValueElement(var0, "gravityFactor", var1.gravityFactor));
      var2.appendChild(createValueElement(var0, "windFactor", var1.windFactor));
      var2.appendChild(createValueElement(var0, "startAlpha", var1.startAlpha));
      var2.appendChild(createValueElement(var0, "endAlpha", var1.endAlpha));
      var2.appendChild(createValueElement(var0, "alpha", var1.alpha));
      var2.appendChild(createValueElement(var0, "size", var1.size));
      var2.appendChild(createValueElement(var0, "velocity", var1.velocity));
      var2.appendChild(createValueElement(var0, "scaleY", var1.scaleY));
      Element var3 = var0.createElement("color");
      ArrayList var4 = var1.colors;

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         ConfigurableEmitter.ColorRecord var6 = (ConfigurableEmitter.ColorRecord)var4.get(var5);
         Element var7 = var0.createElement("step");
         var7.setAttribute("offset", "" + var6.pos);
         var7.setAttribute("r", "" + var6.col.r);
         var7.setAttribute("g", "" + var6.col.g);
         var7.setAttribute("b", "" + var6.col.b);
         var3.appendChild(var7);
      }

      var2.appendChild(var3);
      return var2;
   }

   private static Element createRangeElement(Document var0, String var1, ConfigurableEmitter.Range var2) {
      Element var3 = var0.createElement(var1);
      var3.setAttribute("min", "" + var2.getMin());
      var3.setAttribute("max", "" + var2.getMax());
      var3.setAttribute("enabled", "" + var2.isEnabled());
      return var3;
   }

   private static Element createValueElement(Document var0, String var1, ConfigurableEmitter.Value var2) {
      Element var3 = var0.createElement(var1);
      if (var2 instanceof ConfigurableEmitter.SimpleValue) {
         var3.setAttribute("type", "simple");
         var3.setAttribute("value", "" + var2.getValue(0.0F));
      } else if (var2 instanceof ConfigurableEmitter.RandomValue) {
         var3.setAttribute("type", "random");
         var3.setAttribute("value", "" + ((ConfigurableEmitter.RandomValue)var2).getValue());
      } else if (var2 instanceof ConfigurableEmitter.LinearInterpolator) {
         var3.setAttribute("type", "linear");
         var3.setAttribute("min", "" + ((ConfigurableEmitter.LinearInterpolator)var2).getMin());
         var3.setAttribute("max", "" + ((ConfigurableEmitter.LinearInterpolator)var2).getMax());
         var3.setAttribute("active", "" + ((ConfigurableEmitter.LinearInterpolator)var2).isActive());
         ArrayList var4 = ((ConfigurableEmitter.LinearInterpolator)var2).getCurve();

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            Vector2f var6 = (Vector2f)var4.get(var5);
            Element var7 = var0.createElement("point");
            var7.setAttribute("x", "" + var6.x);
            var7.setAttribute("y", "" + var6.y);
            var3.appendChild(var7);
         }
      } else {
         Log.warn("unkown value type ignored: " + var2.getClass());
      }

      return var3;
   }

   private static void parseRangeElement(Element var0, ConfigurableEmitter.Range var1) {
      if (var0 != null) {
         var1.setMin(Float.parseFloat(var0.getAttribute("min")));
         var1.setMax(Float.parseFloat(var0.getAttribute("max")));
         var1.setEnabled("true".equals(var0.getAttribute("enabled")));
      }
   }

   private static void parseValueElement(Element var0, ConfigurableEmitter.Value var1) {
      if (var0 != null) {
         String var2 = var0.getAttribute("type");
         String var3 = var0.getAttribute("value");
         if (var2 != null && var2.length() != 0) {
            if (var2.equals("simple")) {
               ((ConfigurableEmitter.SimpleValue)var1).setValue(Float.parseFloat(var3));
            } else if (var2.equals("random")) {
               ((ConfigurableEmitter.RandomValue)var1).setValue(Float.parseFloat(var3));
            } else if (var2.equals("linear")) {
               String var4 = var0.getAttribute("min");
               String var5 = var0.getAttribute("max");
               String var6 = var0.getAttribute("active");
               NodeList var7 = var0.getElementsByTagName("point");
               ArrayList var8 = new ArrayList();

               for(int var9 = 0; var9 < var7.getLength(); ++var9) {
                  Element var10 = (Element)var7.item(var9);
                  float var11 = Float.parseFloat(var10.getAttribute("x"));
                  float var12 = Float.parseFloat(var10.getAttribute("y"));
                  var8.add(new Vector2f(var11, var12));
               }

               ((ConfigurableEmitter.LinearInterpolator)var1).setCurve(var8);
               ((ConfigurableEmitter.LinearInterpolator)var1).setMin(Integer.parseInt(var4));
               ((ConfigurableEmitter.LinearInterpolator)var1).setMax(Integer.parseInt(var5));
               ((ConfigurableEmitter.LinearInterpolator)var1).setActive("true".equals(var6));
            } else {
               Log.warn("unkown type detected: " + var2);
            }
         } else if (var1 instanceof ConfigurableEmitter.SimpleValue) {
            ((ConfigurableEmitter.SimpleValue)var1).setValue(Float.parseFloat(var3));
         } else if (var1 instanceof ConfigurableEmitter.RandomValue) {
            ((ConfigurableEmitter.RandomValue)var1).setValue(Float.parseFloat(var3));
         } else {
            Log.warn("problems reading element, skipping: " + var0);
         }

      }
   }
}
