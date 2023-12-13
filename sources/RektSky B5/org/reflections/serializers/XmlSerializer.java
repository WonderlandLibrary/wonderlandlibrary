/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.dom4j.Document
 *  org.dom4j.DocumentException
 *  org.dom4j.DocumentFactory
 *  org.dom4j.Element
 *  org.dom4j.io.OutputFormat
 *  org.dom4j.io.SAXReader
 *  org.dom4j.io.XMLWriter
 */
package org.reflections.serializers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
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
import org.reflections.serializers.Serializer;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.Utils;

public class XmlSerializer
implements Serializer {
    @Override
    public Reflections read(InputStream inputStream) {
        Reflections reflections;
        try {
            Constructor constructor = Reflections.class.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            reflections = (Reflections)constructor.newInstance(new Object[0]);
        }
        catch (Exception e2) {
            reflections = new Reflections(new ConfigurationBuilder());
        }
        try {
            Document document = new SAXReader().read(inputStream);
            for (Object e1 : document.getRootElement().elements()) {
                Element index = (Element)e1;
                for (Object e2 : index.elements()) {
                    Element entry = (Element)e2;
                    Element key = entry.element("key");
                    Element values = entry.element("values");
                    for (Object o3 : values.elements()) {
                        Element value = (Element)o3;
                        reflections.getStore().put(index.getName(), key.getText(), value.getText());
                    }
                }
            }
        }
        catch (DocumentException e3) {
            throw new ReflectionsException("could not read.", e3);
        }
        catch (Throwable e4) {
            throw new RuntimeException("Could not read. Make sure relevant dependencies exist on classpath.", e4);
        }
        return reflections;
    }

    @Override
    public File save(Reflections reflections, String filename) {
        File file = Utils.prepareFile(filename);
        try {
            Document document = this.createDocument(reflections);
            XMLWriter xmlWriter = new XMLWriter((OutputStream)new FileOutputStream(file), OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
        }
        catch (IOException e2) {
            throw new ReflectionsException("could not save to file " + filename, e2);
        }
        catch (Throwable e3) {
            throw new RuntimeException("Could not save to file " + filename + ". Make sure relevant dependencies exist on classpath.", e3);
        }
        return file;
    }

    @Override
    public String toString(Reflections reflections) {
        Document document = this.createDocument(reflections);
        try {
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter((Writer)writer, OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        }
        catch (IOException e2) {
            throw new RuntimeException();
        }
    }

    private Document createDocument(Reflections reflections) {
        Store map = reflections.getStore();
        Document document = DocumentFactory.getInstance().createDocument();
        Element root = document.addElement("Reflections");
        for (String indexName : map.keySet()) {
            Element indexElement = root.addElement(indexName);
            for (String key : map.keys(indexName)) {
                Element entryElement = indexElement.addElement("entry");
                entryElement.addElement("key").setText(key);
                Element valuesElement = entryElement.addElement("values");
                for (String value : map.get(indexName, key)) {
                    valuesElement.addElement("value").setText(value);
                }
            }
        }
        return document;
    }
}

