package org.apache.commons.codec.language;

import java.util.Locale;

public class Caverphone1 extends AbstractCaverphone {
   private static final String SIX_1 = "111111";

   public String encode(String var1) {
      if (var1 != null && var1.length() != 0) {
         String var2 = var1.toLowerCase(Locale.ENGLISH);
         var2 = var2.replaceAll("[^a-z]", "");
         var2 = var2.replaceAll("^cough", "cou2f");
         var2 = var2.replaceAll("^rough", "rou2f");
         var2 = var2.replaceAll("^tough", "tou2f");
         var2 = var2.replaceAll("^enough", "enou2f");
         var2 = var2.replaceAll("^gn", "2n");
         var2 = var2.replaceAll("mb$", "m2");
         var2 = var2.replaceAll("cq", "2q");
         var2 = var2.replaceAll("ci", "si");
         var2 = var2.replaceAll("ce", "se");
         var2 = var2.replaceAll("cy", "sy");
         var2 = var2.replaceAll("tch", "2ch");
         var2 = var2.replaceAll("c", "k");
         var2 = var2.replaceAll("q", "k");
         var2 = var2.replaceAll("x", "k");
         var2 = var2.replaceAll("v", "f");
         var2 = var2.replaceAll("dg", "2g");
         var2 = var2.replaceAll("tio", "sio");
         var2 = var2.replaceAll("tia", "sia");
         var2 = var2.replaceAll("d", "t");
         var2 = var2.replaceAll("ph", "fh");
         var2 = var2.replaceAll("b", "p");
         var2 = var2.replaceAll("sh", "s2");
         var2 = var2.replaceAll("z", "s");
         var2 = var2.replaceAll("^[aeiou]", "A");
         var2 = var2.replaceAll("[aeiou]", "3");
         var2 = var2.replaceAll("3gh3", "3kh3");
         var2 = var2.replaceAll("gh", "22");
         var2 = var2.replaceAll("g", "k");
         var2 = var2.replaceAll("s+", "S");
         var2 = var2.replaceAll("t+", "T");
         var2 = var2.replaceAll("p+", "P");
         var2 = var2.replaceAll("k+", "K");
         var2 = var2.replaceAll("f+", "F");
         var2 = var2.replaceAll("m+", "M");
         var2 = var2.replaceAll("n+", "N");
         var2 = var2.replaceAll("w3", "W3");
         var2 = var2.replaceAll("wy", "Wy");
         var2 = var2.replaceAll("wh3", "Wh3");
         var2 = var2.replaceAll("why", "Why");
         var2 = var2.replaceAll("w", "2");
         var2 = var2.replaceAll("^h", "A");
         var2 = var2.replaceAll("h", "2");
         var2 = var2.replaceAll("r3", "R3");
         var2 = var2.replaceAll("ry", "Ry");
         var2 = var2.replaceAll("r", "2");
         var2 = var2.replaceAll("l3", "L3");
         var2 = var2.replaceAll("ly", "Ly");
         var2 = var2.replaceAll("l", "2");
         var2 = var2.replaceAll("j", "y");
         var2 = var2.replaceAll("y3", "Y3");
         var2 = var2.replaceAll("y", "2");
         var2 = var2.replaceAll("2", "");
         var2 = var2.replaceAll("3", "");
         var2 = var2 + "111111";
         return var2.substring(0, 6);
      } else {
         return "111111";
      }
   }
}
