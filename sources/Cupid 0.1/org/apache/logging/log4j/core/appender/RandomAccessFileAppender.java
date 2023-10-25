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

@Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
public final class RandomAccessFileAppender extends AbstractOutputStreamAppender<RandomAccessFileManager> {
  private final String fileName;
  
  private Object advertisement;
  
  private final Advertiser advertiser;
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RandomAccessFileAppender> {
    @PluginBuilderAttribute("fileName")
    private String fileName;
    
    @PluginBuilderAttribute("append")
    private boolean append = true;
    
    @PluginBuilderAttribute("advertise")
    private boolean advertise;
    
    @PluginBuilderAttribute("advertiseURI")
    private String advertiseURI;
    
    public Builder() {
      withBufferSize(262144);
    }
    
    public RandomAccessFileAppender build() {
      String name = getName();
      if (name == null) {
        RandomAccessFileAppender.LOGGER.error("No name provided for RandomAccessFileAppender");
        return null;
      } 
      if (this.fileName == null) {
        RandomAccessFileAppender.LOGGER.error("No filename provided for RandomAccessFileAppender with name {}", name);
        return null;
      } 
      Layout<? extends Serializable> layout = getOrCreateLayout();
      boolean immediateFlush = isImmediateFlush();
      RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(this.fileName, this.append, immediateFlush, 
          getBufferSize(), this.advertiseURI, layout, null);
      if (manager == null)
        return null; 
      return new RandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), immediateFlush, this.advertise ? 
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
    
    public B setAdvertise(boolean advertise) {
      this.advertise = advertise;
      return (B)asBuilder();
    }
    
    public B setAdvertiseURI(String advertiseURI) {
      this.advertiseURI = advertiseURI;
      return (B)asBuilder();
    }
  }
  
  private RandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RandomAccessFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
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
  
  public int getBufferSize() {
    return getManager().getBufferSize();
  }
  
  @Deprecated
  public static <B extends Builder<B>> RandomAccessFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String bufferSizeStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration configuration) {
    boolean isAppend = Booleans.parseBoolean(append, true);
    boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    boolean isAdvertise = Boolean.parseBoolean(advertise);
    int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
    return ((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder()
      .setAdvertise(isAdvertise)
      .setAdvertiseURI(advertiseURI)
      .setAppend(isAppend)
      .withBufferSize(bufferSize))
      .setConfiguration(configuration))
      .setFileName(fileName).setFilter(filter)).setIgnoreExceptions(ignoreExceptions))
      .withImmediateFlush(isFlush)).setLayout(layout)).setName(name))
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */