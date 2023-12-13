package org.newdawn.slick.tests.xml;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

public class XMLTest {
   private static void fail(String var0) {
      throw new RuntimeException(var0);
   }

   private static void assertNotNull(Object var0) {
      if (var0 == null) {
         throw new RuntimeException("TEST FAILS: " + var0 + " must not be null");
      }
   }

   private static void assertEquals(float var0, float var1) {
      if (var0 != var1) {
         throw new RuntimeException("TEST FAILS: " + var0 + " should be " + var1);
      }
   }

   private static void assertEquals(int var0, int var1) {
      if (var0 != var1) {
         throw new RuntimeException("TEST FAILS: " + var0 + " should be " + var1);
      }
   }

   private static void assertEquals(Object var0, Object var1) {
      if (!var0.equals(var1)) {
         throw new RuntimeException("TEST FAILS: " + var0 + " should be " + var1);
      }
   }

   public static void main(String[] var0) throws SlickException {
      XMLParser var1 = new XMLParser();
      XMLElement var2 = var1.parse("testdata/test.xml");
      assertEquals(var2.getName(), "testRoot");
      System.out.println(var2);
      assertNotNull(var2.getChildrenByName("simple").get(0).getContent());
      System.out.println(var2.getChildrenByName("simple").get(0).getContent());
      XMLElement var3 = var2.getChildrenByName("parent").get(0);
      assertEquals(var3.getChildrenByName("grandchild").size(), 0);
      assertEquals(var3.getChildrenByName("child").size(), 2);
      assertEquals(var3.getChildrenByName("child").get(0).getChildren().size(), 2);
      XMLElement var4 = var3.getChildrenByName("child").get(0).getChildren().get(0);
      String var5 = var4.getAttribute("name");
      String var6 = var4.getAttribute("nothere", "defaultValue");
      int var7 = var4.getIntAttribute("age");
      assertEquals(var5, "bob");
      assertEquals(var6, "defaultValue");
      assertEquals(var7, 1);
      XMLElement var8 = var2.getChildrenByName("other").get(0);
      float var9 = (float)var8.getDoubleAttribute("x");
      float var10 = (float)var8.getDoubleAttribute("y", 1.0D);
      float var11 = (float)var8.getDoubleAttribute("z", 83.0D);
      assertEquals(var9, 5.3F);
      assertEquals(var10, 5.4F);
      assertEquals(var11, 83.0F);

      try {
         var11 = (float)var8.getDoubleAttribute("z");
         fail("Attribute z as a double should fail");
      } catch (SlickException var13) {
      }

   }
}
