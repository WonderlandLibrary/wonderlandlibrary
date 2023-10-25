package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "RollingRandomAccessFile", category = "Core", elementType = "appender", printObject = true)
public final class RollingRandomAccessFileAppender extends AbstractOutputStreamAppender<RollingRandomAccessFileManager> {
  private final String fileName;
  
  private final String filePattern;
  
  private final Object advertisement;
  
  private final Advertiser advertiser;
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RollingRandomAccessFileAppender> {
    @PluginBuilderAttribute("fileName")
    private String fileName;
    
    @PluginBuilderAttribute("filePattern")
    private String filePattern;
    
    @PluginBuilderAttribute("append")
    private boolean append;
    
    @PluginElement("Policy")
    private TriggeringPolicy policy;
    
    @PluginElement("Strategy")
    private RolloverStrategy strategy;
    
    @PluginBuilderAttribute("advertise")
    private boolean advertise;
    
    @PluginBuilderAttribute("advertiseURI")
    private String advertiseURI;
    
    @PluginBuilderAttribute
    private String filePermissions;
    
    @PluginBuilderAttribute
    private String fileOwner;
    
    @PluginBuilderAttribute
    private String fileGroup;
    
    public Builder() {
      this.append = true;
      withBufferSize(262144);
      setIgnoreExceptions(true);
      withImmediateFlush(true);
    }
    
    public RollingRandomAccessFileAppender build() {
      String name = getName();
      if (name == null) {
        RollingRandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
        return null;
      } 
      if (this.strategy == null) {
        if (this.fileName != null) {
          this
            
            .strategy = (RolloverStrategy)DefaultRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
        } else {
          this
            
            .strategy = (RolloverStrategy)DirectWriteRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
        } 
      } else if (this.fileName == null && !(this.strategy instanceof org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy)) {
        RollingRandomAccessFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a DirectFileRolloverStrategy must be configured");
        return null;
      } 
      if (this.filePattern == null) {
        RollingRandomAccessFileAppender.LOGGER.error("No filename pattern provided for FileAppender with name " + name);
        return null;
      } 
      if (this.policy == null) {
        RollingRandomAccessFileAppender.LOGGER.error("A TriggeringPolicy must be provided");
        return null;
      } 
      Layout<? extends Serializable> layout = getOrCreateLayout();
      boolean immediateFlush = isImmediateFlush();
      int bufferSize = getBufferSize();
      RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(this.fileName, this.filePattern, this.append, immediateFlush, bufferSize, this.policy, this.strategy, this.advertiseURI, layout, this.filePermissions, this.fileOwner, this.fileGroup, 
          
          getConfiguration());
      if (manager == null)
        return null; 
      manager.initialize();
      return new RollingRandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, this.filePattern, 
          isIgnoreExceptions(), immediateFlush, bufferSize, this.advertise ? 
          getConfiguration().getAdvertiser() : null, getPropertyArray());
    }
    
    public B withFileName(String fileName) {
      this.fileName = fileName;
      return (B)asBuilder();
    }
    
    public B withFilePattern(String filePattern) {
      this.filePattern = filePattern;
      return (B)asBuilder();
    }
    
    public B withAppend(boolean append) {
      this.append = append;
      return (B)asBuilder();
    }
    
    public B withPolicy(TriggeringPolicy policy) {
      this.policy = policy;
      return (B)asBuilder();
    }
    
    public B withStrategy(RolloverStrategy strategy) {
      this.strategy = strategy;
      return (B)asBuilder();
    }
    
    public B withAdvertise(boolean advertise) {
      this.advertise = advertise;
      return (B)asBuilder();
    }
    
    public B withAdvertiseURI(String advertiseURI) {
      this.advertiseURI = advertiseURI;
      return (B)asBuilder();
    }
    
    public B withFilePermissions(String filePermissions) {
      this.filePermissions = filePermissions;
      return (B)asBuilder();
    }
    
    public B withFileOwner(String fileOwner) {
      this.fileOwner = fileOwner;
      return (B)asBuilder();
    }
    
    public B withFileGroup(String fileGroup) {
      this.fileGroup = fileGroup;
      return (B)asBuilder();
    }
  }
  
  private RollingRandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingRandomAccessFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, int bufferSize, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    if (advertiser != null) {
      Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    } else {
      this.advertisement = null;
    } 
    this.fileName = fileName;
    this.filePattern = filePattern;
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
  
  public void append(LogEvent event) {
    RollingRandomAccessFileManager manager = getManager();
    manager.checkRollover(event);
    super.append(event);
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public String getFilePattern() {
    return this.filePattern;
  }
  
  public int getBufferSize() {
    return getManager().getBufferSize();
  }
  
  @Deprecated
  public static <B extends Builder<B>> RollingRandomAccessFileAppender createAppender(String fileName, String filePattern, String append, String name, String immediateFlush, String bufferSizeStr, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignoreExceptions, String advertise, String advertiseURI, Configuration configuration) {
    boolean isAppend = Booleans.parseBoolean(append, true);
    boolean isIgnoreExceptions = Booleans.parseBoolean(ignoreExceptions, true);
    boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, true);
    boolean isAdvertise = Boolean.parseBoolean(advertise);
    int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
    return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder()
      .withAdvertise(isAdvertise)
      .withAdvertiseURI(advertiseURI)
      .withAppend(isAppend)
      .withBufferSize(bufferSize))
      .setConfiguration(configuration))
      .withFileName(fileName)
      .withFilePattern(filePattern).setFilter(filter)).setIgnoreExceptions(isIgnoreExceptions))
      .withImmediateFlush(isImmediateFlush)).setLayout(layout)).setName(name))
      .withPolicy(policy)
      .withStrategy(strategy)
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\RollingRandomAccessFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */