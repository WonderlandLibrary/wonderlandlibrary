/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.xml;

import me.kiras.aimwhere.libraries.slick.tests.xml.GameData;
import me.kiras.aimwhere.libraries.slick.tests.xml.ItemContainer;
import me.kiras.aimwhere.libraries.slick.util.xml.ObjectTreeParser;
import me.kiras.aimwhere.libraries.slick.util.xml.SlickXMLException;

public class ObjectParserTest {
    public static void main(String[] argv) throws SlickXMLException {
        ObjectTreeParser parser = new ObjectTreeParser("me.kiras.aimwhere.libraries.slick.tests.xml");
        parser.addElementMapping("Bag", ItemContainer.class);
        GameData parsedData = (GameData)parser.parse("testdata/objxmltest.xml");
        parsedData.dump("");
    }
}

