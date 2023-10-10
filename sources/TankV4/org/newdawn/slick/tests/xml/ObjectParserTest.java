package org.newdawn.slick.tests.xml;

import org.newdawn.slick.util.xml.ObjectTreeParser;
import org.newdawn.slick.util.xml.SlickXMLException;

public class ObjectParserTest {
   static Class class$org$newdawn$slick$tests$xml$ItemContainer;

   public static void main(String[] var0) throws SlickXMLException {
      ObjectTreeParser var1 = new ObjectTreeParser("org.newdawn.slick.tests.xml");
      var1.addElementMapping("Bag", class$org$newdawn$slick$tests$xml$ItemContainer == null ? (class$org$newdawn$slick$tests$xml$ItemContainer = class$("org.newdawn.slick.tests.xml.ItemContainer")) : class$org$newdawn$slick$tests$xml$ItemContainer);
      GameData var2 = (GameData)var1.parse("testdata/objxmltest.xml");
      var2.dump("");
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }
}
