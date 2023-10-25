package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "MemoryMappedFile", category = "Core", elementType = "appender", printObject = true)
public final class MemoryMappedFileAppender extends AbstractOutputStreamAppender<MemoryMappedFileManager> {
  private static final int BIT_POSITION_1GB = 30;
  
  private static final int MAX_REGION_LENGTH = 1073741824;
  
  private static final int MIN_REGION_LENGTH = 256;
  
  private final String fileName;
  
  private Object advertisement;
  
  private final Advertiser advertiser;
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<MemoryMappedFileAppender> {
    @PluginBuilderAttribute("fileName")
    private String fileName;
    
    @PluginBuilderAttribute("append")
    private boolean append = true;
    
    @PluginBuilderAttribute("regionLength")
    private int regionLength = 33554432;
    
    @PluginBuilderAttribute("advertise")
    private boolean advertise;
    
    @PluginBuilderAttribute("advertiseURI")
    private String advertiseURI;
    
    public MemoryMappedFileAppender build() {
      String name = getName();
      int actualRegionLength = MemoryMappedFileAppender.determineValidRegionLength(name, this.regionLength);
      if (name == null) {
        MemoryMappedFileAppender.LOGGER.error("No name provided for MemoryMappedFileAppender");
        return null;
      } 
      if (this.fileName == null) {
        MemoryMappedFileAppender.LOGGER.error("No filename provided for MemoryMappedFileAppender with name " + name);
        return null;
      } 
      Layout<? extends Serializable> layout = getOrCreateLayout();
      MemoryMappedFileManager manager = MemoryMappedFileManager.getFileManager(this.fileName, this.append, isImmediateFlush(), actualRegionLength, this.advertiseURI, layout);
      if (manager == null)
        return null; 
      return new MemoryMappedFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), false, this.advertise ? 
          getConfiguration().getAdvertiser() : null, getPropertyArray());
    }
    
    public B setFileName(String fileName) {
      this.fileName = fileName;
      return (B)asBuilder();
    }
    
    public B setAppend(boolean append) {
      this.append = append;
      return (B)asBuilder();
    }
    
    public B setRegionLength(int regionLength) {
      this.regionLength = regionLength;
      return (B)asBuilder();
    }
    
    public B setAdvertise(boolean advertise) {
      this.advertise = advertise;
      return (B)asBuilder();
    }
    
    public B setAdvertiseURI(String advertiseURI) {
      this.advertiseURI = advertiseURI;
      return (B)asBuilder();
    }
  }
  
  private MemoryMappedFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, MemoryMappedFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    if (advertiser != null) {
      Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
      configuration.putAll(manager.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    } 
    this.fileName = filename;
    this.advertiser = advertiser;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    stop(timeout, timeUnit, false);
    if (this.advertiser != null)
      this.advertiser.unadvertise(this.advertisement); 
    setStopped();
    return true;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public int getRegionLength() {
    return getManager().getRegionLength();
  }
  
  @Deprecated
  public static <B extends Builder<B>> MemoryMappedFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String regionLengthStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration config) {
    boolean isAppend = Booleans.parseBoolean(append, true);
    boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, false);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    boolean isAdvertise = Boolean.parseBoolean(advertise);
    int regionLength = Integers.parseInt(regionLengthStr, 33554432);
    return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)newBuilder()
      .setAdvertise(isAdvertise)
      .setAdvertiseURI(advertiseURI)
      .setAppend(isAppend)
      .setConfiguration(config))
      .setFileName(fileName).setFilter(filter)).setIgnoreExceptions(ignoreExceptions))
      .withImmediateFlush(isImmediateFlush)).setLayout(layout)).setName(name))
      .setRegionLength(regionLength)
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private static int determineValidRegionLength(String name, int regionLength) {
    if (regionLength > 1073741824) {
      LOGGER.info("MemoryMappedAppender[{}] Reduced region length from {} to max length: {}", name, Integer.valueOf(regionLength), 
          Integer.valueOf(1073741824));
      return 1073741824;
    } 
    if (regionLength < 256) {
      LOGGER.info("MemoryMappedAppender[{}] Expanded region length from {} to min length: {}", name, Integer.valueOf(regionLength), 
          Integer.valueOf(256));
      return 256;
    } 
    int result = Integers.ceilingNextPowerOfTwo(regionLength);
    if (regionLength != result)
      LOGGER.info("MemoryMappedAppender[{}] Rounded up region length from {} to next power of two: {}", name, 
          Integer.valueOf(regionLength), Integer.valueOf(result)); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */