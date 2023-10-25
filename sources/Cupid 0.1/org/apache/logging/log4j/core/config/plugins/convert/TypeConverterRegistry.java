package org.apache.logging.log4j.core.config.plugins.convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.apache.logging.log4j.core.util.TypeUtil;
import org.apache.logging.log4j.status.StatusLogger;

public class TypeConverterRegistry {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static volatile TypeConverterRegistry INSTANCE;
  
  private static final Object INSTANCE_LOCK = new Object();
  
  private final ConcurrentMap<Type, TypeConverter<?>> registry = new ConcurrentHashMap<>();
  
  public static TypeConverterRegistry getInstance() {
    TypeConverterRegistry result = INSTANCE;
    if (result == null)
      synchronized (INSTANCE_LOCK) {
        result = INSTANCE;
        if (result == null)
          INSTANCE = result = new TypeConverterRegistry(); 
      }  
    return result;
  }
  
  public TypeConverter<?> findCompatibleConverter(Type type) {
    Objects.requireNonNull(type, "No type was provided");
    TypeConverter<?> primary = this.registry.get(type);
    if (primary != null)
      return primary; 
    if (type instanceof Class) {
      Class<?> clazz = (Class)type;
      if (clazz.isEnum()) {
        EnumConverter<? extends Enum> converter = new EnumConverter<>(clazz.asSubclass(Enum.class));
        synchronized (INSTANCE_LOCK) {
          return registerConverter(type, converter);
        } 
      } 
    } 
    for (Map.Entry<Type, TypeConverter<?>> entry : this.registry.entrySet()) {
      Type key = entry.getKey();
      if (TypeUtil.isAssignable(type, key)) {
        LOGGER.debug("Found compatible TypeConverter<{}> for type [{}].", key, type);
        TypeConverter<?> value = entry.getValue();
        synchronized (INSTANCE_LOCK) {
          return registerConverter(type, value);
        } 
      } 
    } 
    throw new UnknownFormatConversionException(type.toString());
  }
  
  private TypeConverterRegistry() {
    LOGGER.trace("TypeConverterRegistry initializing.");
    PluginManager manager = new PluginManager("TypeConverter");
    manager.collectPlugins();
    loadKnownTypeConverters(manager.getPlugins().values());
    registerPrimitiveTypes();
  }
  
  private void loadKnownTypeConverters(Collection<PluginType<?>> knownTypes) {
    for (PluginType<?> knownType : knownTypes) {
      Class<?> clazz = knownType.getPluginClass();
      if (TypeConverter.class.isAssignableFrom(clazz)) {
        Class<? extends TypeConverter> pluginClass = clazz.asSubclass(TypeConverter.class);
        Type conversionType = getTypeConverterSupportedType(pluginClass);
        TypeConverter<?> converter = (TypeConverter)ReflectionUtil.instantiate(pluginClass);
        registerConverter(conversionType, converter);
      } 
    } 
  }
  
  private TypeConverter<?> registerConverter(Type conversionType, TypeConverter<?> converter) {
    TypeConverter<?> conflictingConverter = this.registry.get(conversionType);
    if (conflictingConverter != null) {
      boolean overridable;
      if (converter instanceof Comparable) {
        Comparable<TypeConverter<?>> comparableConverter = (Comparable)converter;
        overridable = (comparableConverter.compareTo(conflictingConverter) < 0);
      } else if (conflictingConverter instanceof Comparable) {
        Comparable<TypeConverter<?>> comparableConflictingConverter = (Comparable)conflictingConverter;
        overridable = (comparableConflictingConverter.compareTo(converter) > 0);
      } else {
        overridable = false;
      } 
      if (overridable) {
        LOGGER.debug("Replacing TypeConverter [{}] for type [{}] with [{}] after comparison.", conflictingConverter, conversionType, converter);
        this.registry.put(conversionType, converter);
        return converter;
      } 
      LOGGER.warn("Ignoring TypeConverter [{}] for type [{}] that conflicts with [{}], since they are not comparable.", converter, conversionType, conflictingConverter);
      return conflictingConverter;
    } 
    this.registry.put(conversionType, converter);
    return converter;
  }
  
  private static Type getTypeConverterSupportedType(Class<? extends TypeConverter> typeConverterClass) {
    for (Type type : typeConverterClass.getGenericInterfaces()) {
      if (type instanceof ParameterizedType) {
        ParameterizedType pType = (ParameterizedType)type;
        if (TypeConverter.class.equals(pType.getRawType()))
          return pType.getActualTypeArguments()[0]; 
      } 
    } 
    return void.class;
  }
  
  private void registerPrimitiveTypes() {
    registerTypeAlias(Boolean.class, boolean.class);
    registerTypeAlias(Byte.class, byte.class);
    registerTypeAlias(Character.class, char.class);
    registerTypeAlias(Double.class, double.class);
    registerTypeAlias(Float.class, float.class);
    registerTypeAlias(Integer.class, int.class);
    registerTypeAlias(Long.class, long.class);
    registerTypeAlias(Short.class, short.class);
  }
  
  private void registerTypeAlias(Type knownType, Type aliasType) {
    this.registry.putIfAbsent(aliasType, this.registry.get(knownType));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\TypeConverterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */