package org.jboss.errai.reflections.serializers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jboss.errai.reflections.Reflections;
import org.jboss.errai.reflections.ReflectionsException;
import org.jboss.errai.reflections.util.ConfigurationBuilder;
import org.jboss.errai.reflections.util.Utils;

public class XmlSerializer implements Serializer {
   public Reflections read(InputStream var1) {
      Reflections var2 = new Reflections(this, new ConfigurationBuilder()) {
         final XmlSerializer this$0;

         {
            this.this$0 = var1;
         }
      };

      Document var3;
      try {
         var3 = (new SAXReader()).read(var1);
      } catch (DocumentException var16) {
         throw new RuntimeException(var16);
      }

      Iterator var4 = var3.getRootElement().elements().iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         Element var6 = (Element)var5;
         Iterator var7 = var6.elements().iterator();

         while(var7.hasNext()) {
            Object var8 = var7.next();
            Element var9 = (Element)var8;
            Element var10 = var9.element("key");
            Element var11 = var9.element("values");

            Element var14;
            Object var15;
            for(Iterator var12 = var11.elements().iterator(); var12.hasNext(); ((Multimap)var15).put(var10.getText(), var14.getText())) {
               Object var13 = var12.next();
               var14 = (Element)var13;
               var15 = (Multimap)var2.getStore().getStoreMap().get(var6.getName());
               if (var15 == null) {
                  var2.getStore().getStoreMap().put(var6.getName(), var15 = HashMultimap.create());
               }
            }
         }
      }

      return var2;
   }

   public File save(Reflections var1, String var2) {
      File var3 = Utils.prepareFile(var2);
      Document var4 = this.createDocument(var1);

      try {
         XMLWriter var5 = new XMLWriter(new FileOutputStream(var3), OutputFormat.createPrettyPrint());
         var5.write(var4);
         var5.close();
         return var3;
      } catch (IOException var6) {
         throw new ReflectionsException("could not save to file " + var2, var6);
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
      Map var2 = var1.getStore().getStoreMap();
      Document var3 = DocumentFactory.getInstance().createDocument();
      Element var4 = var3.addElement("Reflections");
      Iterator var5 = var2.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Element var7 = var4.addElement(var6);
         Iterator var8 = ((Multimap)var2.get(var6)).keySet().iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            Element var10 = var7.addElement("entry");
            var10.addElement("key").setText(var9);
            Element var11 = var10.addElement("values");
            Iterator var12 = ((Multimap)var2.get(var6)).get(var9).iterator();

            while(var12.hasNext()) {
               String var13 = (String)var12.next();
               var11.addElement("value").setText(var13);
            }
         }
      }

      return var3;
   }
}
