/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLPackedSheet {
    private Image image;
    private HashMap sprites = new HashMap();

    public XMLPackedSheet(String imageRef, String xmlRef) throws SlickException {
        this.image = new Image(imageRef, false, 2);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(ResourceLoader.getResourceAsStream(xmlRef));
            NodeList list = doc.getElementsByTagName("sprite");
            for (int i2 = 0; i2 < list.getLength(); ++i2) {
                Element element = (Element)list.item(i2);
                String name = element.getAttribute("name");
                int x2 = Integer.parseInt(element.getAttribute("x"));
                int y2 = Integer.parseInt(element.getAttribute("y"));
                int width = Integer.parseInt(element.getAttribute("width"));
                int height = Integer.parseInt(element.getAttribute("height"));
                this.sprites.put(name, this.image.getSubImage(x2, y2, width, height));
            }
        }
        catch (Exception e2) {
            throw new SlickException("Failed to parse sprite sheet XML", e2);
        }
    }

    public Image getSprite(String name) {
        return (Image)this.sprites.get(name);
    }
}

