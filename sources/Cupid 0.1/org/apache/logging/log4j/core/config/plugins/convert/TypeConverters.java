package org.apache.logging.log4j.core.config.plugins.convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Provider;
import java.security.Security;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.CronExpression;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.LoaderUtil;

public final class TypeConverters {
  public static final String CATEGORY = "TypeConverter";
  
  @Plugin(name = "BigDecimal", category = "TypeConverter")
  public static class BigDecimalConverter implements TypeConverter<BigDecimal> {
    public BigDecimal convert(String s) {
      return new BigDecimal(s);
    }
  }
  
  @Plugin(name = "BigInteger", category = "TypeConverter")
  public static class BigIntegerConverter implements TypeConverter<BigInteger> {
    public BigInteger convert(String s) {
      return new BigInteger(s);
    }
  }
  
  @Plugin(name = "Boolean", category = "TypeConverter")
  public static class BooleanConverter implements TypeConverter<Boolean> {
    public Boolean convert(String s) {
      return Boolean.valueOf(s);
    }
  }
  
  @Plugin(name = "ByteArray", category = "TypeConverter")
  public static class ByteArrayConverter implements TypeConverter<byte[]> {
    private static final String PREFIX_0x = "0x";
    
    private static final String PREFIX_BASE64 = "Base64:";
    
    public byte[] convert(String value) {
      byte[] bytes;
      if (value == null || value.isEmpty()) {
        bytes = Constants.EMPTY_BYTE_ARRAY;
      } else if (value.startsWith("Base64:")) {
        String lexicalXSDBase64Binary = value.substring("Base64:".length());
        bytes = Base64Converter.parseBase64Binary(lexicalXSDBase64Binary);
      } else if (value.startsWith("0x")) {
        String lexicalXSDHexBinary = value.substring("0x".length());
        bytes = HexConverter.parseHexBinary(lexicalXSDHexBinary);
      } else {
        bytes = value.getBytes(Charset.defaultCharset());
      } 
      return bytes;
    }
  }
  
  @Plugin(name = "Byte", category = "TypeConverter")
  public static class ByteConverter implements TypeConverter<Byte> {
    public Byte convert(String s) {
      return Byte.valueOf(s);
    }
  }
  
  @Plugin(name = "Character", category = "TypeConverter")
  public static class CharacterConverter implements TypeConverter<Character> {
    public Character convert(String s) {
      if (s.length() != 1)
        throw new IllegalArgumentException("Character string must be of length 1: " + s); 
      return Character.valueOf(s.toCharArray()[0]);
    }
  }
  
  @Plugin(name = "CharacterArray", category = "TypeConverter")
  public static class CharArrayConverter implements TypeConverter<char[]> {
    public char[] convert(String s) {
      return s.toCharArray();
    }
  }
  
  @Plugin(name = "Charset", category = "TypeConverter")
  public static class CharsetConverter implements TypeConverter<Charset> {
    public Charset convert(String s) {
      return Charset.forName(s);
    }
  }
  
  @Plugin(name = "Class", category = "TypeConverter")
  public static class ClassConverter implements TypeConverter<Class<?>> {
    public Class<?> convert(String s) throws ClassNotFoundException {
      switch (s.toLowerCase()) {
        case "boolean":
          return boolean.class;
        case "byte":
          return byte.class;
        case "char":
          return char.class;
        case "double":
          return double.class;
        case "float":
          return float.class;
        case "int":
          return int.class;
        case "long":
          return long.class;
        case "short":
          return short.class;
        case "void":
          return void.class;
      } 
      return LoaderUtil.loadClass(s);
    }
  }
  
  @Plugin(name = "CronExpression", category = "TypeConverter")
  public static class CronExpressionConverter implements TypeConverter<CronExpression> {
    public CronExpression convert(String s) throws Exception {
      return new CronExpression(s);
    }
  }
  
  @Plugin(name = "Double", category = "TypeConverter")
  public static class DoubleConverter implements TypeConverter<Double> {
    public Double convert(String s) {
      return Double.valueOf(s);
    }
  }
  
  @Plugin(name = "Duration", category = "TypeConverter")
  public static class DurationConverter implements TypeConverter<Duration> {
    public Duration convert(String s) {
      return Duration.parse(s);
    }
  }
  
  @Plugin(name = "File", category = "TypeConverter")
  public static class FileConverter implements TypeConverter<File> {
    public File convert(String s) {
      return new File(s);
    }
  }
  
  @Plugin(name = "Float", category = "TypeConverter")
  public static class FloatConverter implements TypeConverter<Float> {
    public Float convert(String s) {
      return Float.valueOf(s);
    }
  }
  
  @Plugin(name = "InetAddress", category = "TypeConverter")
  public static class InetAddressConverter implements TypeConverter<InetAddress> {
    public InetAddress convert(String s) throws Exception {
      return InetAddress.getByName(s);
    }
  }
  
  @Plugin(name = "Integer", category = "TypeConverter")
  public static class IntegerConverter implements TypeConverter<Integer> {
    public Integer convert(String s) {
      return Integer.valueOf(s);
    }
  }
  
  @Plugin(name = "Level", category = "TypeConverter")
  public static class LevelConverter implements TypeConverter<Level> {
    public Level convert(String s) {
      return Level.valueOf(s);
    }
  }
  
  @Plugin(name = "Long", category = "TypeConverter")
  public static class LongConverter implements TypeConverter<Long> {
    public Long convert(String s) {
      return Long.valueOf(s);
    }
  }
  
  @Plugin(name = "Path", category = "TypeConverter")
  public static class PathConverter implements TypeConverter<Path> {
    public Path convert(String s) throws Exception {
      return Paths.get(s, new String[0]);
    }
  }
  
  @Plugin(name = "Pattern", category = "TypeConverter")
  public static class PatternConverter implements TypeConverter<Pattern> {
    public Pattern convert(String s) {
      return Pattern.compile(s);
    }
  }
  
  @Plugin(name = "SecurityProvider", category = "TypeConverter")
  public static class SecurityProviderConverter implements TypeConverter<Provider> {
    public Provider convert(String s) {
      return Security.getProvider(s);
    }
  }
  
  @Plugin(name = "Short", category = "TypeConverter")
  public static class ShortConverter implements TypeConverter<Short> {
    public Short convert(String s) {
      return Short.valueOf(s);
    }
  }
  
  @Plugin(name = "String", category = "TypeConverter")
  public static class StringConverter implements TypeConverter<String> {
    public String convert(String s) {
      return s;
    }
  }
  
  @Plugin(name = "URI", category = "TypeConverter")
  public static class UriConverter implements TypeConverter<URI> {
    public URI convert(String s) throws URISyntaxException {
      return new URI(s);
    }
  }
  
  @Plugin(name = "URL", category = "TypeConverter")
  public static class UrlConverter implements TypeConverter<URL> {
    public URL convert(String s) throws MalformedURLException {
      return new URL(s);
    }
  }
  
  @Plugin(name = "UUID", category = "TypeConverter")
  public static class UuidConverter implements TypeConverter<UUID> {
    public UUID convert(String s) throws Exception {
      return UUID.fromString(s);
    }
  }
  
  public static <T> T convert(String s, Class<? extends T> clazz, Object defaultValue) {
    TypeConverter<T> converter = (TypeConverter)TypeConverterRegistry.getInstance().findCompatibleConverter(clazz);
    if (s == null)
      return parseDefaultValue(converter, defaultValue); 
    try {
      return converter.convert(s);
    } catch (Exception e) {
      LOGGER.warn("Error while converting string [{}] to type [{}]. Using default value [{}].", s, clazz, defaultValue, e);
      return parseDefaultValue(converter, defaultValue);
    } 
  }
  
  private static <T> T parseDefaultValue(TypeConverter<T> converter, Object defaultValue) {
    if (defaultValue == null)
      return null; 
    if (!(defaultValue instanceof String))
      return (T)defaultValue; 
    try {
      return converter.convert((String)defaultValue);
    } catch (Exception e) {
      LOGGER.debug("Can't parse default value [{}] for type [{}].", defaultValue, converter.getClass(), e);
      return null;
    } 
  }
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\TypeConverters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */