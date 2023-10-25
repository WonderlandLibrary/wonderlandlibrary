package org.yaml.snakeyaml;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class TypeDescription {
  private static final Logger log = Logger.getLogger(TypeDescription.class.getPackage().getName());
  
  private final Class<? extends Object> type;
  
  private Class<?> impl;
  
  private Tag tag;
  
  private transient Set<Property> dumpProperties;
  
  private transient PropertyUtils propertyUtils;
  
  private transient boolean delegatesChecked;
  
  private Map<String, PropertySubstitute> properties = Collections.emptyMap();
  
  protected Set<String> excludes = Collections.emptySet();
  
  protected String[] includes = null;
  
  protected BeanAccess beanAccess;
  
  public TypeDescription(Class<? extends Object> clazz, Tag tag) {
    this(clazz, tag, null);
  }
  
  public TypeDescription(Class<? extends Object> clazz, Tag tag, Class<?> impl) {
    this.type = clazz;
    this.tag = tag;
    this.impl = impl;
    this.beanAccess = null;
  }
  
  public TypeDescription(Class<? extends Object> clazz, String tag) {
    this(clazz, new Tag(tag), null);
  }
  
  public TypeDescription(Class<? extends Object> clazz) {
    this(clazz, (Tag)null, null);
  }
  
  public TypeDescription(Class<? extends Object> clazz, Class<?> impl) {
    this(clazz, null, impl);
  }
  
  public Tag getTag() {
    return this.tag;
  }
  
  @Deprecated
  public void setTag(Tag tag) {
    this.tag = tag;
  }
  
  @Deprecated
  public void setTag(String tag) {
    setTag(new Tag(tag));
  }
  
  public Class<? extends Object> getType() {
    return this.type;
  }
  
  @Deprecated
  public void putListPropertyType(String property, Class<? extends Object> type) {
    addPropertyParameters(property, new Class[] { type });
  }
  
  @Deprecated
  public Class<? extends Object> getListPropertyType(String property) {
    if (this.properties.containsKey(property)) {
      Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
      if (typeArguments != null && typeArguments.length > 0)
        return (Class)typeArguments[0]; 
    } 
    return null;
  }
  
  @Deprecated
  public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value) {
    addPropertyParameters(property, new Class[] { key, value });
  }
  
  @Deprecated
  public Class<? extends Object> getMapKeyType(String property) {
    if (this.properties.containsKey(property)) {
      Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
      if (typeArguments != null && typeArguments.length > 0)
        return (Class)typeArguments[0]; 
    } 
    return null;
  }
  
  @Deprecated
  public Class<? extends Object> getMapValueType(String property) {
    if (this.properties.containsKey(property)) {
      Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
      if (typeArguments != null && typeArguments.length > 1)
        return (Class)typeArguments[1]; 
    } 
    return null;
  }
  
  public void addPropertyParameters(String pName, Class<?>... classes) {
    if (!this.properties.containsKey(pName)) {
      substituteProperty(pName, null, null, null, classes);
    } else {
      PropertySubstitute pr = this.properties.get(pName);
      pr.setActualTypeArguments(classes);
    } 
  }
  
  public String toString() {
    return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
  }
  
  private void checkDelegates() {
    Collection<PropertySubstitute> values = this.properties.values();
    for (PropertySubstitute p : values) {
      try {
        p.setDelegate(discoverProperty(p.getName()));
      } catch (YAMLException e) {}
    } 
    this.delegatesChecked = true;
  }
  
  private Property discoverProperty(String name) {
    if (this.propertyUtils != null) {
      if (this.beanAccess == null)
        return this.propertyUtils.getProperty(this.type, name); 
      return this.propertyUtils.getProperty(this.type, name, this.beanAccess);
    } 
    return null;
  }
  
  public Property getProperty(String name) {
    if (!this.delegatesChecked)
      checkDelegates(); 
    return this.properties.containsKey(name) ? (Property)this.properties.get(name) : discoverProperty(name);
  }
  
  public void substituteProperty(String pName, Class<?> pType, String getter, String setter, Class<?>... argParams) {
    substituteProperty(new PropertySubstitute(pName, pType, getter, setter, argParams));
  }
  
  public void substituteProperty(PropertySubstitute substitute) {
    if (Collections.EMPTY_MAP == this.properties)
      this.properties = new LinkedHashMap<>(); 
    substitute.setTargetType(this.type);
    this.properties.put(substitute.getName(), substitute);
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    this.propertyUtils = propertyUtils;
  }
  
  public void setIncludes(String... propNames) {
    this.includes = (propNames != null && propNames.length > 0) ? propNames : null;
  }
  
  public void setExcludes(String... propNames) {
    if (propNames != null && propNames.length > 0) {
      this.excludes = new HashSet<>();
      for (String name : propNames)
        this.excludes.add(name); 
    } else {
      this.excludes = Collections.emptySet();
    } 
  }
  
  public Set<Property> getProperties() {
    if (this.dumpProperties != null)
      return this.dumpProperties; 
    if (this.propertyUtils != null) {
      if (this.includes != null) {
        this.dumpProperties = new LinkedHashSet<>();
        for (String propertyName : this.includes) {
          if (!this.excludes.contains(propertyName))
            this.dumpProperties.add(getProperty(propertyName)); 
        } 
        return this.dumpProperties;
      } 
      Set<Property> readableProps = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
      if (this.properties.isEmpty()) {
        if (this.excludes.isEmpty())
          return this.dumpProperties = readableProps; 
        this.dumpProperties = new LinkedHashSet<>();
        for (Property property : readableProps) {
          if (!this.excludes.contains(property.getName()))
            this.dumpProperties.add(property); 
        } 
        return this.dumpProperties;
      } 
      if (!this.delegatesChecked)
        checkDelegates(); 
      this.dumpProperties = new LinkedHashSet<>();
      for (Property property : this.properties.values()) {
        if (!this.excludes.contains(property.getName()) && property.isReadable())
          this.dumpProperties.add(property); 
      } 
      for (Property property : readableProps) {
        if (!this.excludes.contains(property.getName()))
          this.dumpProperties.add(property); 
      } 
      return this.dumpProperties;
    } 
    return null;
  }
  
  public boolean setupPropertyType(String key, Node valueNode) {
    return false;
  }
  
  public boolean setProperty(Object targetBean, String propertyName, Object value) throws Exception {
    return false;
  }
  
  public Object newInstance(Node node) {
    if (this.impl != null)
      try {
        Constructor<?> c = this.impl.getDeclaredConstructor(new Class[0]);
        c.setAccessible(true);
        return c.newInstance(new Object[0]);
      } catch (Exception e) {
        log.fine(e.getLocalizedMessage());
        this.impl = null;
      }  
    return null;
  }
  
  public Object newInstance(String propertyName, Node node) {
    return null;
  }
  
  public Object finalizeConstruction(Object obj) {
    return obj;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\TypeDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */