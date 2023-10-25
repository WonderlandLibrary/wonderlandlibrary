package org.yaml.snakeyaml.introspector;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.util.PlatformFeatureDetector;

public class PropertyUtils {
  private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap<>();
  
  private final Map<Class<?>, Set<Property>> readableProperties = new HashMap<>();
  
  private BeanAccess beanAccess = BeanAccess.DEFAULT;
  
  private boolean allowReadOnlyProperties = false;
  
  private boolean skipMissingProperties = false;
  
  private PlatformFeatureDetector platformFeatureDetector;
  
  private static final String TRANSIENT = "transient";
  
  public PropertyUtils() {
    this(new PlatformFeatureDetector());
  }
  
  PropertyUtils(PlatformFeatureDetector platformFeatureDetector) {
    this.platformFeatureDetector = platformFeatureDetector;
    if (platformFeatureDetector.isRunningOnAndroid())
      this.beanAccess = BeanAccess.FIELD; 
  }
  
  protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
    Class<?> c;
    if (this.propertiesCache.containsKey(type))
      return this.propertiesCache.get(type); 
    Map<String, Property> properties = new LinkedHashMap<>();
    boolean inaccessableFieldsExist = false;
    switch (bAccess) {
      case FIELD:
        for (c = type; c != null; c = c.getSuperclass()) {
          for (Field field : c.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !properties.containsKey(field.getName()))
              properties.put(field.getName(), new FieldProperty(field)); 
          } 
        } 
        break;
      default:
        try {
          for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
            Method readMethod = property.getReadMethod();
            if ((readMethod == null || !readMethod.getName().equals("getClass")) && !isTransient(property))
              properties.put(property.getName(), new MethodProperty(property)); 
          } 
        } catch (IntrospectionException e) {
          throw new YAMLException(e);
        } 
        for (c = type; c != null; c = c.getSuperclass()) {
          for (Field field : c.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers))
              if (Modifier.isPublic(modifiers)) {
                properties.put(field.getName(), new FieldProperty(field));
              } else {
                inaccessableFieldsExist = true;
              }  
          } 
        } 
        break;
    } 
    if (properties.isEmpty() && inaccessableFieldsExist)
      throw new YAMLException("No JavaBean properties found in " + type.getName()); 
    this.propertiesCache.put(type, properties);
    return properties;
  }
  
  private boolean isTransient(FeatureDescriptor fd) {
    return Boolean.TRUE.equals(fd.getValue("transient"));
  }
  
  public Set<Property> getProperties(Class<? extends Object> type) {
    return getProperties(type, this.beanAccess);
  }
  
  public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) {
    if (this.readableProperties.containsKey(type))
      return this.readableProperties.get(type); 
    Set<Property> properties = createPropertySet(type, bAccess);
    this.readableProperties.put(type, properties);
    return properties;
  }
  
  protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) {
    Set<Property> properties = new TreeSet<>();
    Collection<Property> props = getPropertiesMap(type, bAccess).values();
    for (Property property : props) {
      if (property.isReadable() && (this.allowReadOnlyProperties || property.isWritable()))
        properties.add(property); 
    } 
    return properties;
  }
  
  public Property getProperty(Class<? extends Object> type, String name) {
    return getProperty(type, name, this.beanAccess);
  }
  
  public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) {
    Map<String, Property> properties = getPropertiesMap(type, bAccess);
    Property property = properties.get(name);
    if (property == null && this.skipMissingProperties)
      property = new MissingProperty(name); 
    if (property == null)
      throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName()); 
    return property;
  }
  
  public void setBeanAccess(BeanAccess beanAccess) {
    if (this.platformFeatureDetector.isRunningOnAndroid() && beanAccess != BeanAccess.FIELD)
      throw new IllegalArgumentException("JVM is Android - only BeanAccess.FIELD is available"); 
    if (this.beanAccess != beanAccess) {
      this.beanAccess = beanAccess;
      this.propertiesCache.clear();
      this.readableProperties.clear();
    } 
  }
  
  public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
    if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
      this.allowReadOnlyProperties = allowReadOnlyProperties;
      this.readableProperties.clear();
    } 
  }
  
  public boolean isAllowReadOnlyProperties() {
    return this.allowReadOnlyProperties;
  }
  
  public void setSkipMissingProperties(boolean skipMissingProperties) {
    if (this.skipMissingProperties != skipMissingProperties) {
      this.skipMissingProperties = skipMissingProperties;
      this.readableProperties.clear();
    } 
  }
  
  public boolean isSkipMissingProperties() {
    return this.skipMissingProperties;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\introspector\PropertyUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */