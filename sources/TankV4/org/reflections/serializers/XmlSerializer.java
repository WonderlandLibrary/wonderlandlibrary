package org.reflections.serializers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.Utils;

public class XmlSerializer implements Serializer {
   public Reflections read(InputStream var1) {
      Reflections var2;
      try {
         Constructor var3 = Reflections.class.getDeclaredConstructor();
         var3.setAccessible(true);
         var2 = (Reflections)var3.newInstance();
      } catch (Exception var15) {
         var2 = new Reflections(new ConfigurationBuilder());
      }

      try {
         Document var18 = (new SAXReader()).read(var1);
         Iterator var4 = var18.getRootElement().elements().iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            Element var6 = (Element)var5;
            Iterator var7 = var6.elements().iterator();

            while(var7.hasNext()) {
               Object var8 = var7.next();
               Element var9 = (Element)var8;
               Element var10 = var9.element("key");
               Element var11 = var9.element("values");
               Iterator var12 = var11.elements().iterator();

               while(var12.hasNext()) {
                  Object var13 = var12.next();
                  Element var14 = (Element)var13;
                  var2.getStore().getOrCreate(var6.getName()).put(var10.getText(), var14.getText());
               }
            }
         }

         return var2;
      } catch (DocumentException var16) {
         throw new ReflectionsException("could not read.", var16);
      } catch (Throwable var17) {
         throw new RuntimeException("Could not read. Make sure relevant dependencies exist on classpath.", var17);
      }
   }

   public File save(Reflections var1, String var2) {
      File var3 = Utils.prepareFile(var2);

      try {
         Document var4 = this.createDocument(var1);
         XMLWriter var5 = new XMLWriter(new FileOutputStream(var3), OutputFormat.createPrettyPrint());
         var5.write(var4);
         var5.close();
         return var3;
      } catch (IOException var6) {
         throw new ReflectionsException("could not save to file " + var2, var6);
      } catch (Throwable var7) {
         throw new RuntimeException("Could not save to file " + var2 + ". Make sure relevant dependencies exist on classpath.", var7);
      }
   }

   public String toString(Reflections var1) {
      Document var2 = this.createDocument(var1);

      try {
         StringWriter var3 = new StringWriter();
         XMLWriter var4 = new XMLWriter(var3, OutputFormat.createPrettyPrint());
         var4.write(var2);
         var4.close();
         return var3.toString();
      } catch (IOException var5) {
         throw new RuntimeException();
      }
   }

   private Document createDocument(Reflections var1) {
      Store var2 = var1.getStore();
      Document var3 = DocumentFactory.getInstance().createDocument();
      Element var4 = var3.addElement("Reflections");
      Iterator var5 = var2.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Element var7 = var4.addElement(var6);
         Iterator var8 = var2.get(var6).keySet().iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            Element var10 = var7.addElement("entry");
            var10.addElement("key").setText(var9);
            Element var11 = var10.addElement("values");
            Iterator var12 = var2.get(var6).get(var9).iterator();

            while(var12.hasNext()) {
               String var13 = (String)var12.next();
               var11.addElement("value").setText(var13);
            }
         }
      }

      return var3;
   }
}
