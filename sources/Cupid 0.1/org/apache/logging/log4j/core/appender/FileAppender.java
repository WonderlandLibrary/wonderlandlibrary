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
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "File", category = "Core", elementType = "appender", printObject = true)
public final class FileAppender extends AbstractOutputStreamAppender<FileManager> {
  public static final String PLUGIN_NAME = "File";
  
  private static final int DEFAULT_BUFFER_SIZE = 8192;
  
  private final String fileName;
  
  private final Advertiser advertiser;
  
  private final Object advertisement;
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<FileAppender> {
    @PluginBuilderAttribute
    @Required
    private String fileName;
    
    @PluginBuilderAttribute
    private boolean append = true;
    
    @PluginBuilderAttribute
    private boolean locking;
    
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
    
    public FileAppender build() {
      boolean bufferedIo = isBufferedIo();
      int bufferSize = getBufferSize();
      if (this.locking && bufferedIo) {
        FileAppender.LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for {}", this.fileName);
        bufferedIo = false;
      } 
      if (!bufferedIo && bufferSize > 0)
        FileAppender.LOGGER.warn("The bufferSize is set to {} but bufferedIo is false: {}", Integer.valueOf(bufferSize), Boolean.valueOf(bufferedIo)); 
      Layout<? extends Serializable> layout = getOrCreateLayout();
      FileManager manager = FileManager.getFileManager(this.fileName, this.append, this.locking, bufferedIo, this.createOnDemand, this.advertiseUri, layout, bufferSize, this.filePermissions, this.fileOwner, this.fileGroup, 
          getConfiguration());
      if (manager == null)
        return null; 
      return new FileAppender(getName(), layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), (!bufferedIo || 
          isImmediateFlush()), this.advertise ? getConfiguration().getAdvertiser() : null, 
          getPropertyArray());
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
  
  @Deprecated
  public static <B extends Builder<B>> FileAppender createAppender(String fileName, String append, String locking, String name, String immediateFlush, String ignoreExceptions, String bufferedIo, String bufferSizeStr, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseUri, Configuration config) {
    return ((Builder)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder()
      .withAdvertise(Boolean.parseBoolean(advertise))
      .withAdvertiseUri(advertiseUri)
      .withAppend(Booleans.parseBoolean(append, true))
      .withBufferedIo(Booleans.parseBoolean(bufferedIo, true)))
      .withBufferSize(Integers.parseInt(bufferSizeStr, 8192)))
      .setConfiguration(config))
      .withFileName(fileName).setFilter(filter)).setIgnoreExceptions(Booleans.parseBoolean(ignoreExceptions, true)))
      .withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).setLayout(layout))
      .withLocking(Boolean.parseBoolean(locking)).setName(name))
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private FileAppender(String name, Layout<? extends Serializable> layout, Filter filter, FileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    if (advertiser != null) {
      Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
      configuration.putAll(manager.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    } else {
      this.advertisement = null;
    } 
    this.fileName = filename;
    this.advertiser = advertiser;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    stop(timeout, timeUnit, false);
    if (this.advertiser != null)
      this.advertiser.unadvertise(this.advertisement); 
    setStopped();
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\FileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */