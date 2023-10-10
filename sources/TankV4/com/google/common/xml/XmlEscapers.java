package com.google.common.xml;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

@Beta
@GwtCompatible
public class XmlEscapers {
   private static final char MIN_ASCII_CONTROL_CHAR = '\u0000';
   private static final char MAX_ASCII_CONTROL_CHAR = '\u001f';
   private static final Escaper XML_ESCAPER;
   private static final Escaper XML_CONTENT_ESCAPER;
   private static final Escaper XML_ATTRIBUTE_ESCAPER;

   private XmlEscapers() {
   }

   public static Escaper xmlContentEscaper() {
      return XML_CONTENT_ESCAPER;
   }

   public static Escaper xmlAttributeEscaper() {
      return XML_ATTRIBUTE_ESCAPER;
   }

   static {
      Escapers.Builder var0 = Escapers.builder();
      var0.setSafeRange('\u0000', '\uffff');
      var0.setUnsafeReplacement("");

      for(char var1 = 0; var1 <= 31; ++var1) {
         if (var1 != '\t' && var1 != '\n' && var1 != '\r') {
            var0.addEscape(var1, "");
         }
      }

      var0.addEscape('&', "&amp;");
      var0.addEscape('<', "&lt;");
      var0.addEscape('>', "&gt;");
      XML_CONTENT_ESCAPER = var0.build();
      var0.addEscape('\'', "&apos;");
      var0.addEscape('"', "&quot;");
      XML_ESCAPER = var0.build();
      var0.addEscape('\t', "&#x9;");
      var0.addEscape('\n', "&#xA;");
      var0.addEscape('\r', "&#xD;");
      XML_ATTRIBUTE_ESCAPER = var0.build();
   }
}
