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
import org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "RollingFile", category = "Core", elementType = "appender", printObject = true)
public final class RollingFileAppender extends AbstractOutputStreamAppender<RollingFileManager> {
  public static final String PLUGIN_NAME = "RollingFile";
  
  private static final int DEFAULT_BUFFER_SIZE = 8192;
  
  private final String fileName;
  
  private final String filePattern;
  
  private Object advertisement;
  
  private final Advertiser advertiser;
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RollingFileAppender> {
    @PluginBuilderAttribute
    private String fileName;
    
    @PluginBuilderAttribute
    @Required
    private String filePattern;
    
    @PluginBuilderAttribute
    private boolean append = true;
    
    @PluginBuilderAttribute
    private boolean locking;
    
    @PluginElement("Policy")
    @Required
    private TriggeringPolicy policy;
    
    @PluginElement("Strategy")
    private RolloverStrategy strategy;
    
    @PluginBuilderAttribute
    private boolean advertise;
    
    @PluginBuilderAttribute
    private String advertiseUri;
    
    @PluginBuilderAttribute
    private boolean createOnDemand;
    
    @PluginBuilderAttribute
    private String filePermissions;
    
    @PluginBuilderAttribute
    private String fileOwner;
    
    @PluginBuilderAttribute
    private String fileGroup;
    
    public RollingFileAppender build() {
      boolean isBufferedIo = isBufferedIo();
      int bufferSize = getBufferSize();
      if (getName() == null) {
        RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No name provided.", getName());
        return null;
      } 
      if (!isBufferedIo && bufferSize > 0)
        RollingFileAppender.LOGGER.warn("RollingFileAppender '{}': The bufferSize is set to {} but bufferedIO is not true", getName(), Integer.valueOf(bufferSize)); 
      if (this.filePattern == null) {
        RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No file name pattern provided.", getName());
        return null;
      } 
      if (this.policy == null) {
        RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No TriggeringPolicy provided.", getName());
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
      } else if (this.fileName == null && !(this.strategy instanceof DirectFileRolloverStrategy)) {
        RollingFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a {} must be configured", getName(), DirectFileRolloverStrategy.class.getSimpleName());
        return null;
      } 
      Layout<? extends Serializable> layout = getOrCreateLayout();
      RollingFileManager manager = RollingFileManager.getFileManager(this.fileName, this.filePattern, this.append, isBufferedIo, this.policy, this.strategy, this.advertiseUri, layout, bufferSize, 
          isImmediateFlush(), this.createOnDemand, this.filePermissions, this.fileOwner, this.fileGroup, 
          getConfiguration());
      if (manager == null)
        return null; 
      manager.initialize();
      return new RollingFileAppender(getName(), layout, getFilter(), manager, this.fileName, this.filePattern, 
          isIgnoreExceptions(), (!isBufferedIo || isImmediateFlush()), this.advertise ? 
          getConfiguration().getAdvertiser() : null, getPropertyArray());
    }
    
    public String getAdvertiseUri() {
      return this.advertiseUri;
    }
    
    public String getFileName() {
      return this.fileName;
    }
    
    public boolean isAdvertise() {
      return this.advertise;
    }
    
    public boolean isAppend() {
      return this.append;
    }
    
    public boolean isCreateOnDemand() {
      return this.createOnDemand;
    }
    
    public boolean isLocking() {
      return this.locking;
    }
    
    public String getFilePermissions() {
      return this.filePermissions;
    }
    
    public String getFileOwner() {
      return this.fileOwner;
    }
    
    public String getFileGroup() {
      return this.fileGroup;
    }
    
    public B withAdvertise(boolean advertise) {
      this.advertise = advertise;
      return (B)asBuilder();
    }
    
    public B withAdvertiseUri(String advertiseUri) {
      this.advertiseUri = advertiseUri;
      return (B)asBuilder();
    }
    
    public B withAppend(boolean append) {
      this.append = append;
      return (B)asBuilder();
    }
    
    public B withFileName(String fileName) {
      this.fileName = fileName;
      return (B)asBuilder();
    }
    
    public B withCreateOnDemand(boolean createOnDemand) {
      this.createOnDemand = createOnDemand;
      return (B)asBuilder();
    }
    
    public B withLocking(boolean locking) {
      this.locking = locking;
      return (B)asBuilder();
    }
    
    public String getFilePattern() {
      return this.filePattern;
    }
    
    public TriggeringPolicy getPolicy() {
      return this.policy;
    }
    
    public RolloverStrategy getStrategy() {
      return this.strategy;
    }
    
    public B withFilePattern(String filePattern) {
      this.filePattern = filePattern;
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
  
  private RollingFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    if (advertiser != null) {
      Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    } 
    this.fileName = fileName;
    this.filePattern = filePattern;
    this.advertiser = advertiser;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(timeout, timeUnit, false);
    if (this.advertiser != null)
      this.advertiser.unadvertise(this.advertisement); 
    setStopped();
    return stopped;
  }
  
  public void append(LogEvent event) {
    getManager().checkRollover(event);
    super.append(event);
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public String getFilePattern() {
    return this.filePattern;
  }
  
  public <T extends TriggeringPolicy> T getTriggeringPolicy() {
    return (T)getManager().getTriggeringPolicy();
  }
  
  @Deprecated
  public static <B extends Builder<B>> RollingFileAppender createAppender(String fileName, String filePattern, String append, String name, String bufferedIO, String bufferSizeStr, String immediateFlush, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignore, String advertise, String advertiseUri, Configuration config) {
    int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
    return ((Builder<B>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder()
      .withAdvertise(Boolean.parseBoolean(advertise))
      .withAdvertiseUri(advertiseUri)
      .withAppend(Booleans.parseBoolean(append, true))
      .withBufferedIo(Booleans.parseBoolean(bufferedIO, true)))
      .withBufferSize(bufferSize))
      .setConfiguration(config))
      .withFileName(fileName)
      .withFilePattern(filePattern).setFilter(filter)).setIgnoreExceptions(Booleans.parseBoolean(ignore, true)))
      .withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).setLayout(layout))
      .withCreateOnDemand(false)
      .withLocking(false).setName(name))
      .withPolicy(policy)
      .withStrategy(strategy)
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\RollingFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */