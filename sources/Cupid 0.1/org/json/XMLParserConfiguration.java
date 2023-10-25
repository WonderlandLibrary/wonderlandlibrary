package org.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XMLParserConfiguration {
  public static final XMLParserConfiguration ORIGINAL = new XMLParserConfiguration();
  
  public static final XMLParserConfiguration KEEP_STRINGS = (new XMLParserConfiguration())
    .withKeepStrings(true);
  
  private boolean keepStrings;
  
  private String cDataTagName;
  
  private boolean convertNilAttributeToNull;
  
  private Map<String, XMLXsiTypeConverter<?>> xsiTypeMap;
  
  private Set<String> forceList;
  
  public XMLParserConfiguration() {
    this.keepStrings = false;
    this.cDataTagName = "content";
    this.convertNilAttributeToNull = false;
    this.xsiTypeMap = Collections.emptyMap();
    this.forceList = Collections.emptySet();
  }
  
  @Deprecated
  public XMLParserConfiguration(boolean keepStrings) {
    this(keepStrings, "content", false);
  }
  
  @Deprecated
  public XMLParserConfiguration(String cDataTagName) {
    this(false, cDataTagName, false);
  }
  
  @Deprecated
  public XMLParserConfiguration(boolean keepStrings, String cDataTagName) {
    this.keepStrings = keepStrings;
    this.cDataTagName = cDataTagName;
    this.convertNilAttributeToNull = false;
  }
  
  @Deprecated
  public XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull) {
    this.keepStrings = keepStrings;
    this.cDataTagName = cDataTagName;
    this.convertNilAttributeToNull = convertNilAttributeToNull;
  }
  
  private XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull, Map<String, XMLXsiTypeConverter<?>> xsiTypeMap, Set<String> forceList) {
    this.keepStrings = keepStrings;
    this.cDataTagName = cDataTagName;
    this.convertNilAttributeToNull = convertNilAttributeToNull;
    this.xsiTypeMap = Collections.unmodifiableMap(xsiTypeMap);
    this.forceList = Collections.unmodifiableSet(forceList);
  }
  
  protected XMLParserConfiguration clone() {
    return new XMLParserConfiguration(this.keepStrings, this.cDataTagName, this.convertNilAttributeToNull, this.xsiTypeMap, this.forceList);
  }
  
  public boolean isKeepStrings() {
    return this.keepStrings;
  }
  
  public XMLParserConfiguration withKeepStrings(boolean newVal) {
    XMLParserConfiguration newConfig = clone();
    newConfig.keepStrings = newVal;
    return newConfig;
  }
  
  public String getcDataTagName() {
    return this.cDataTagName;
  }
  
  public XMLParserConfiguration withcDataTagName(String newVal) {
    XMLParserConfiguration newConfig = clone();
    newConfig.cDataTagName = newVal;
    return newConfig;
  }
  
  public boolean isConvertNilAttributeToNull() {
    return this.convertNilAttributeToNull;
  }
  
  public XMLParserConfiguration withConvertNilAttributeToNull(boolean newVal) {
    XMLParserConfiguration newConfig = clone();
    newConfig.convertNilAttributeToNull = newVal;
    return newConfig;
  }
  
  public Map<String, XMLXsiTypeConverter<?>> getXsiTypeMap() {
    return this.xsiTypeMap;
  }
  
  public XMLParserConfiguration withXsiTypeMap(Map<String, XMLXsiTypeConverter<?>> xsiTypeMap) {
    XMLParserConfiguration newConfig = clone();
    Map<String, XMLXsiTypeConverter<?>> cloneXsiTypeMap = new HashMap<String, XMLXsiTypeConverter<?>>(xsiTypeMap);
    newConfig.xsiTypeMap = Collections.unmodifiableMap(cloneXsiTypeMap);
    return newConfig;
  }
  
  public Set<String> getForceList() {
    return this.forceList;
  }
  
  public XMLParserConfiguration withForceList(Set<String> forceList) {
    XMLParserConfiguration newConfig = clone();
    Set<String> cloneForceList = new HashSet<String>(forceList);
    newConfig.forceList = Collections.unmodifiableSet(cloneForceList);
    return newConfig;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\XMLParserConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */