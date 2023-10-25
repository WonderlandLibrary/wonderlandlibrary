package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

final class OSXHIDDevice {
  private static final Logger log = Logger.getLogger(OSXHIDDevice.class.getName());
  
  private static final int AXIS_DEFAULT_MIN_VALUE = 0;
  
  private static final int AXIS_DEFAULT_MAX_VALUE = 65536;
  
  private static final String kIOHIDTransportKey = "Transport";
  
  private static final String kIOHIDVendorIDKey = "VendorID";
  
  private static final String kIOHIDVendorIDSourceKey = "VendorIDSource";
  
  private static final String kIOHIDProductIDKey = "ProductID";
  
  private static final String kIOHIDVersionNumberKey = "VersionNumber";
  
  private static final String kIOHIDManufacturerKey = "Manufacturer";
  
  private static final String kIOHIDProductKey = "Product";
  
  private static final String kIOHIDSerialNumberKey = "SerialNumber";
  
  private static final String kIOHIDCountryCodeKey = "CountryCode";
  
  private static final String kIOHIDLocationIDKey = "LocationID";
  
  private static final String kIOHIDDeviceUsageKey = "DeviceUsage";
  
  private static final String kIOHIDDeviceUsagePageKey = "DeviceUsagePage";
  
  private static final String kIOHIDDeviceUsagePairsKey = "DeviceUsagePairs";
  
  private static final String kIOHIDPrimaryUsageKey = "PrimaryUsage";
  
  private static final String kIOHIDPrimaryUsagePageKey = "PrimaryUsagePage";
  
  private static final String kIOHIDMaxInputReportSizeKey = "MaxInputReportSize";
  
  private static final String kIOHIDMaxOutputReportSizeKey = "MaxOutputReportSize";
  
  private static final String kIOHIDMaxFeatureReportSizeKey = "MaxFeatureReportSize";
  
  private static final String kIOHIDElementKey = "Elements";
  
  private static final String kIOHIDElementCookieKey = "ElementCookie";
  
  private static final String kIOHIDElementTypeKey = "Type";
  
  private static final String kIOHIDElementCollectionTypeKey = "CollectionType";
  
  private static final String kIOHIDElementUsageKey = "Usage";
  
  private static final String kIOHIDElementUsagePageKey = "UsagePage";
  
  private static final String kIOHIDElementMinKey = "Min";
  
  private static final String kIOHIDElementMaxKey = "Max";
  
  private static final String kIOHIDElementScaledMinKey = "ScaledMin";
  
  private static final String kIOHIDElementScaledMaxKey = "ScaledMax";
  
  private static final String kIOHIDElementSizeKey = "Size";
  
  private static final String kIOHIDElementReportSizeKey = "ReportSize";
  
  private static final String kIOHIDElementReportCountKey = "ReportCount";
  
  private static final String kIOHIDElementReportIDKey = "ReportID";
  
  private static final String kIOHIDElementIsArrayKey = "IsArray";
  
  private static final String kIOHIDElementIsRelativeKey = "IsRelative";
  
  private static final String kIOHIDElementIsWrappingKey = "IsWrapping";
  
  private static final String kIOHIDElementIsNonLinearKey = "IsNonLinear";
  
  private static final String kIOHIDElementHasPreferredStateKey = "HasPreferredState";
  
  private static final String kIOHIDElementHasNullStateKey = "HasNullState";
  
  private static final String kIOHIDElementUnitKey = "Unit";
  
  private static final String kIOHIDElementUnitExponentKey = "UnitExponent";
  
  private static final String kIOHIDElementNameKey = "Name";
  
  private static final String kIOHIDElementValueLocationKey = "ValueLocation";
  
  private static final String kIOHIDElementDuplicateIndexKey = "DuplicateIndex";
  
  private static final String kIOHIDElementParentCollectionKey = "ParentCollection";
  
  private final long device_address;
  
  private final long device_interface_address;
  
  private final Map properties;
  
  private boolean released;
  
  public OSXHIDDevice(long device_address, long device_interface_address) throws IOException {
    this.device_address = device_address;
    this.device_interface_address = device_interface_address;
    this.properties = getDeviceProperties();
    open();
  }
  
  public final Controller.PortType getPortType() {
    String transport = (String)this.properties.get("Transport");
    if (transport == null)
      return Controller.PortType.UNKNOWN; 
    if (transport.equals("USB"))
      return Controller.PortType.USB; 
    return Controller.PortType.UNKNOWN;
  }
  
  public final String getProductName() {
    return (String)this.properties.get("Product");
  }
  
  private final OSXHIDElement createElementFromElementProperties(Map element_properties) {
    long element_cookie = getLongFromProperties(element_properties, "ElementCookie");
    int element_type_id = getIntFromProperties(element_properties, "Type");
    ElementType element_type = ElementType.map(element_type_id);
    int min = (int)getLongFromProperties(element_properties, "Min", 0L);
    int max = (int)getLongFromProperties(element_properties, "Max", 65536L);
    UsagePair device_usage_pair = getUsagePair();
    boolean default_relative = (device_usage_pair != null && (device_usage_pair.getUsage() == GenericDesktopUsage.POINTER || device_usage_pair.getUsage() == GenericDesktopUsage.MOUSE));
    boolean is_relative = getBooleanFromProperties(element_properties, "IsRelative", default_relative);
    int usage = getIntFromProperties(element_properties, "Usage");
    int usage_page = getIntFromProperties(element_properties, "UsagePage");
    UsagePair usage_pair = createUsagePair(usage_page, usage);
    if (usage_pair == null || (element_type != ElementType.INPUT_MISC && element_type != ElementType.INPUT_BUTTON && element_type != ElementType.INPUT_AXIS))
      return null; 
    return new OSXHIDElement(this, usage_pair, element_cookie, element_type, min, max, is_relative);
  }
  
  private final void addElements(List elements, Map properties) {
    Object[] elements_properties = (Object[])properties.get("Elements");
    if (elements_properties == null)
      return; 
    for (int i = 0; i < elements_properties.length; i++) {
      Map element_properties = (Map)elements_properties[i];
      OSXHIDElement element = createElementFromElementProperties(element_properties);
      if (element != null)
        elements.add(element); 
      addElements(elements, element_properties);
    } 
  }
  
  public final List getElements() {
    List elements = new ArrayList();
    addElements(elements, this.properties);
    return elements;
  }
  
  private static final long getLongFromProperties(Map properties, String key, long default_value) {
    Long long_obj = (Long)properties.get(key);
    if (long_obj == null)
      return default_value; 
    return long_obj.longValue();
  }
  
  private static final boolean getBooleanFromProperties(Map properties, String key, boolean default_value) {
    return (getLongFromProperties(properties, key, default_value ? 1L : 0L) != 0L);
  }
  
  private static final int getIntFromProperties(Map properties, String key) {
    return (int)getLongFromProperties(properties, key);
  }
  
  private static final long getLongFromProperties(Map properties, String key) {
    Long long_obj = (Long)properties.get(key);
    return long_obj.longValue();
  }
  
  private static final UsagePair createUsagePair(int usage_page_id, int usage_id) {
    UsagePage usage_page = UsagePage.map(usage_page_id);
    if (usage_page != null) {
      Usage usage = usage_page.mapUsage(usage_id);
      if (usage != null)
        return new UsagePair(usage_page, usage); 
    } 
    return null;
  }
  
  public final UsagePair getUsagePair() {
    int usage_page_id = getIntFromProperties(this.properties, "PrimaryUsagePage");
    int usage_id = getIntFromProperties(this.properties, "PrimaryUsage");
    return createUsagePair(usage_page_id, usage_id);
  }
  
  private final void dumpProperties() {
    log.info(toString());
    dumpMap("", this.properties);
  }
  
  private static final void dumpArray(String prefix, Object[] array) {
    log.info(prefix + "{");
    for (int i = 0; i < array.length; i++) {
      dumpObject(prefix + "\t", array[i]);
      log.info(prefix + ",");
    } 
    log.info(prefix + "}");
  }
  
  private static final void dumpMap(String prefix, Map map) {
    Iterator keys = map.keySet().iterator();
    while (keys.hasNext()) {
      Object key = keys.next();
      Object value = map.get(key);
      dumpObject(prefix, key);
      dumpObject(prefix + "\t", value);
    } 
  }
  
  private static final void dumpObject(String prefix, Object obj) {
    if (obj instanceof Long) {
      Long l = (Long)obj;
      log.info(prefix + "0x" + Long.toHexString(l.longValue()));
    } else if (obj instanceof Map) {
      dumpMap(prefix, (Map)obj);
    } else if (obj.getClass().isArray()) {
      dumpArray(prefix, (Object[])obj);
    } else {
      log.info(prefix + obj);
    } 
  }
  
  private final Map getDeviceProperties() throws IOException {
    return nGetDeviceProperties(this.device_address);
  }
  
  public final synchronized void release() throws IOException {
    try {
      close();
    } finally {
      this.released = true;
      nReleaseDevice(this.device_address, this.device_interface_address);
    } 
  }
  
  public final synchronized void getElementValue(long element_cookie, OSXEvent event) throws IOException {
    checkReleased();
    nGetElementValue(this.device_interface_address, element_cookie, event);
  }
  
  public final synchronized OSXHIDQueue createQueue(int queue_depth) throws IOException {
    checkReleased();
    long queue_address = nCreateQueue(this.device_interface_address);
    return new OSXHIDQueue(queue_address, queue_depth);
  }
  
  private final void open() throws IOException {
    nOpen(this.device_interface_address);
  }
  
  private final void close() throws IOException {
    nClose(this.device_interface_address);
  }
  
  private final void checkReleased() throws IOException {
    if (this.released)
      throw new IOException(); 
  }
  
  private static final native Map nGetDeviceProperties(long paramLong) throws IOException;
  
  private static final native void nReleaseDevice(long paramLong1, long paramLong2);
  
  private static final native void nGetElementValue(long paramLong1, long paramLong2, OSXEvent paramOSXEvent) throws IOException;
  
  private static final native long nCreateQueue(long paramLong) throws IOException;
  
  private static final native void nOpen(long paramLong) throws IOException;
  
  private static final native void nClose(long paramLong) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\OSXHIDDevice.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */