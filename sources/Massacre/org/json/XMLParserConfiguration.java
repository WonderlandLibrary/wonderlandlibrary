package org.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class XMLParserConfiguration {
   public static final XMLParserConfiguration ORIGINAL = new XMLParserConfiguration();
   public static final XMLParserConfiguration KEEP_STRINGS = (new XMLParserConfiguration()).withKeepStrings(true);
   private boolean keepStrings;
   private String cDataTagName;
   private boolean convertNilAttributeToNull;
   private Map<String, XMLXsiTypeConverter<?>> xsiTypeMap;

   public XMLParserConfiguration() {
      this.keepStrings = false;
      this.cDataTagName = "content";
      this.convertNilAttributeToNull = false;
      this.xsiTypeMap = Collections.emptyMap();
   }

   /** @deprecated */
   @Deprecated
   public XMLParserConfiguration(boolean keepStrings) {
      this(keepStrings, "content", false);
   }

   /** @deprecated */
   @Deprecated
   public XMLParserConfiguration(String cDataTagName) {
      this(false, cDataTagName, false);
   }

   /** @deprecated */
   @Deprecated
   public XMLParserConfiguration(boolean keepStrings, String cDataTagName) {
      this.keepStrings = keepStrings;
      this.cDataTagName = cDataTagName;
      this.convertNilAttributeToNull = false;
   }

   /** @deprecated */
   @Deprecated
   public XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull) {
      this.keepStrings = keepStrings;
      this.cDataTagName = cDataTagName;
      this.convertNilAttributeToNull = convertNilAttributeToNull;
   }

   private XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull, Map<String, XMLXsiTypeConverter<?>> xsiTypeMap) {
      this.keepStrings = keepStrings;
      this.cDataTagName = cDataTagName;
      this.convertNilAttributeToNull = convertNilAttributeToNull;
      this.xsiTypeMap = Collections.unmodifiableMap(xsiTypeMap);
   }

   protected XMLParserConfiguration clone() {
      return new XMLParserConfiguration(this.keepStrings, this.cDataTagName, this.convertNilAttributeToNull, this.xsiTypeMap);
   }

   public boolean isKeepStrings() {
      return this.keepStrings;
   }

   public XMLParserConfiguration withKeepStrings(boolean newVal) {
      XMLParserConfiguration newConfig = this.clone();
      newConfig.keepStrings = newVal;
      return newConfig;
   }

   public String getcDataTagName() {
      return this.cDataTagName;
   }

   public XMLParserConfiguration withcDataTagName(String newVal) {
      XMLParserConfiguration newConfig = this.clone();
      newConfig.cDataTagName = newVal;
      return newConfig;
   }

   public boolean isConvertNilAttributeToNull() {
      return this.convertNilAttributeToNull;
   }

   public XMLParserConfiguration withConvertNilAttributeToNull(boolean newVal) {
      XMLParserConfiguration newConfig = this.clone();
      newConfig.convertNilAttributeToNull = newVal;
      return newConfig;
   }

   public Map<String, XMLXsiTypeConverter<?>> getXsiTypeMap() {
      return this.xsiTypeMap;
   }

   public XMLParserConfiguration withXsiTypeMap(Map<String, XMLXsiTypeConverter<?>> xsiTypeMap) {
      XMLParserConfiguration newConfig = this.clone();
      Map<String, XMLXsiTypeConverter<?>> cloneXsiTypeMap = new HashMap(xsiTypeMap);
      newConfig.xsiTypeMap = Collections.unmodifiableMap(cloneXsiTypeMap);
      return newConfig;
   }
}
