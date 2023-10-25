package org.apache.logging.log4j.core.config.plugins.visitors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

public abstract class AbstractPluginVisitor<A extends Annotation> implements PluginVisitor<A> {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  protected final Class<A> clazz;
  
  protected A annotation;
  
  protected String[] aliases;
  
  protected Class<?> conversionType;
  
  protected StrSubstitutor substitutor;
  
  protected Member member;
  
  protected AbstractPluginVisitor(Class<A> clazz) {
    this.clazz = clazz;
  }
  
  public PluginVisitor<A> setAnnotation(Annotation anAnnotation) {
    Annotation a = Objects.<Annotation>requireNonNull(anAnnotation, "No annotation was provided");
    if (this.clazz.isInstance(a))
      this.annotation = (A)a; 
    return this;
  }
  
  public PluginVisitor<A> setAliases(String... someAliases) {
    this.aliases = someAliases;
    return this;
  }
  
  public PluginVisitor<A> setConversionType(Class<?> aConversionType) {
    this.conversionType = Objects.<Class<?>>requireNonNull(aConversionType, "No conversion type class was provided");
    return this;
  }
  
  public PluginVisitor<A> setStrSubstitutor(StrSubstitutor aSubstitutor) {
    this.substitutor = Objects.<StrSubstitutor>requireNonNull(aSubstitutor, "No StrSubstitutor was provided");
    return this;
  }
  
  public PluginVisitor<A> setMember(Member aMember) {
    this.member = aMember;
    return this;
  }
  
  protected static String removeAttributeValue(Map<String, String> attributes, String name, String... aliases) {
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key.equalsIgnoreCase(name)) {
        attributes.remove(key);
        return value;
      } 
      if (aliases != null)
        for (String alias : aliases) {
          if (key.equalsIgnoreCase(alias)) {
            attributes.remove(key);
            return value;
          } 
        }  
    } 
    return null;
  }
  
  protected Object convert(String value, Object defaultValue) {
    if (defaultValue instanceof String)
      return TypeConverters.convert(value, this.conversionType, Strings.trimToNull((String)defaultValue)); 
    return TypeConverters.convert(value, this.conversionType, defaultValue);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\AbstractPluginVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */