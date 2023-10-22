package com.sun.javafx.font;

import java.util.HashMap;

class WindowsFontMap {
   static HashMap platformFontMap;

   static HashMap populateHardcodedFileNameMap() {
      HashMap var0 = new HashMap();
      FamilyDescription var1 = new FamilyDescription();
      var1.familyName = "Segoe UI";
      var1.plainFullName = "Segoe UI";
      var1.plainFileName = "segoeui.ttf";
      var1.boldFullName = "Segoe UI Bold";
      var1.boldFileName = "segoeuib.ttf";
      var1.italicFullName = "Segoe UI Italic";
      var1.italicFileName = "segoeuii.ttf";
      var1.boldItalicFullName = "Segoe UI Bold Italic";
      var1.boldItalicFileName = "segoeuiz.ttf";
      var0.put("segoe", var1);
      var1 = new FamilyDescription();
      var1.familyName = "Tahoma";
      var1.plainFullName = "Tahoma";
      var1.plainFileName = "tahoma.ttf";
      var1.boldFullName = "Tahoma Bold";
      var1.boldFileName = "tahomabd.ttf";
      var0.put("tahoma", var1);
      var1 = new FamilyDescription();
      var1.familyName = "Verdana";
      var1.plainFullName = "Verdana";
      var1.plainFileName = "verdana.TTF";
      var1.boldFullName = "Verdana Bold";
      var1.boldFileName = "verdanab.TTF";
      var1.italicFullName = "Verdana Italic";
      var1.italicFileName = "verdanai.TTF";
      var1.boldItalicFullName = "Verdana Bold Italic";
      var1.boldItalicFileName = "verdanaz.TTF";
      var0.put("verdana", var1);
      var1 = new FamilyDescription();
      var1.familyName = "Arial";
      var1.plainFullName = "Arial";
      var1.plainFileName = "ARIAL.TTF";
      var1.boldFullName = "Arial Bold";
      var1.boldFileName = "ARIALBD.TTF";
      var1.italicFullName = "Arial Italic";
      var1.italicFileName = "ARIALI.TTF";
      var1.boldItalicFullName = "Arial Bold Italic";
      var1.boldItalicFileName = "ARIALBI.TTF";
      var0.put("arial", var1);
      var1 = new FamilyDescription();
      var1.familyName = "Times New Roman";
      var1.plainFullName = "Times New Roman";
      var1.plainFileName = "times.ttf";
      var1.boldFullName = "Times New Roman Bold";
      var1.boldFileName = "timesbd.ttf";
      var1.italicFullName = "Times New Roman Italic";
      var1.italicFileName = "timesi.ttf";
      var1.boldItalicFullName = "Times New Roman Bold Italic";
      var1.boldItalicFileName = "timesbi.ttf";
      var0.put("times", var1);
      var1 = new FamilyDescription();
      var1.familyName = "Courier New";
      var1.plainFullName = "Courier New";
      var1.plainFileName = "cour.ttf";
      var1.boldFullName = "Courier New Bold";
      var1.boldFileName = "courbd.ttf";
      var1.italicFullName = "Courier New Italic";
      var1.italicFileName = "couri.ttf";
      var1.boldItalicFullName = "Courier New Bold Italic";
      var1.boldItalicFileName = "courbi.ttf";
      var0.put("courier", var1);
      return var0;
   }

   static String getPathName(String var0) {
      return PrismFontFactory.getPathNameWindows(var0);
   }

   static String findFontFile(String var0, int var1) {
      if (platformFontMap == null) {
         platformFontMap = populateHardcodedFileNameMap();
      }

      if (platformFontMap != null && platformFontMap.size() != 0) {
         int var2 = var0.indexOf(32);
         String var3 = var0;
         if (var2 > 0) {
            var3 = var0.substring(0, var2);
         }

         FamilyDescription var4 = (FamilyDescription)platformFontMap.get(var3);
         if (var4 == null) {
            return null;
         } else {
            String var5 = null;
            if (var1 < 0) {
               if (var0.equalsIgnoreCase(var4.plainFullName)) {
                  var5 = var4.plainFileName;
               } else if (var0.equalsIgnoreCase(var4.boldFullName)) {
                  var5 = var4.boldFileName;
               } else if (var0.equalsIgnoreCase(var4.italicFullName)) {
                  var5 = var4.italicFileName;
               } else if (var0.equalsIgnoreCase(var4.boldItalicFullName)) {
                  var5 = var4.boldItalicFileName;
               }

               return var5 != null ? getPathName(var5) : null;
            } else if (!var0.equalsIgnoreCase(var4.familyName)) {
               return null;
            } else {
               switch (var1) {
                  case 0:
                     var5 = var4.plainFileName;
                     break;
                  case 1:
                     var5 = var4.boldFileName;
                     if (var5 == null) {
                        var5 = var4.plainFileName;
                     }
                     break;
                  case 2:
                     var5 = var4.italicFileName;
                     if (var5 == null) {
                        var5 = var4.plainFileName;
                     }
                     break;
                  case 3:
                     var5 = var4.boldItalicFileName;
                     if (var5 == null) {
                        var5 = var4.italicFileName;
                     }

                     if (var5 == null) {
                        var5 = var4.boldFileName;
                     }

                     if (var5 == null) {
                        var5 = var4.plainFileName;
                     }
               }

               return var5 != null ? getPathName(var5) : null;
            }
         }
      } else {
         return null;
      }
   }

   static class FamilyDescription {
      public String familyName;
      public String plainFullName;
      public String boldFullName;
      public String italicFullName;
      public String boldItalicFullName;
      public String plainFileName;
      public String boldFileName;
      public String italicFileName;
      public String boldItalicFileName;
   }
}
