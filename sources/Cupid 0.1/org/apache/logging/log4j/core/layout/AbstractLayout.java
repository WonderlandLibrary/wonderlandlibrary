package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLayout<T extends Serializable> implements Layout<T> {
  public static abstract class Builder<B extends Builder<B>> {
    @PluginConfiguration
    private Configuration configuration;
    
    @PluginBuilderAttribute
    private byte[] footer;
    
    @PluginBuilderAttribute
    private byte[] header;
    
    public B asBuilder() {
      return (B)this;
    }
    
    public Configuration getConfiguration() {
      return this.configuration;
    }
    
    public byte[] getFooter() {
      return this.footer;
    }
    
    public byte[] getHeader() {
      return this.header;
    }
    
    public B setConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return asBuilder();
    }
    
    public B setFooter(byte[] footer) {
      this.footer = footer;
      return asBuilder();
    }
    
    public B setHeader(byte[] header) {
      this.header = header;
      return asBuilder();
    }
  }
  
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  protected final Configuration configuration;
  
  protected long eventCount;
  
  protected final byte[] footer;
  
  protected final byte[] header;
  
  @Deprecated
  public AbstractLayout(byte[] header, byte[] footer) {
    this(null, header, footer);
  }
  
  public AbstractLayout(Configuration configuration, byte[] header, byte[] footer) {
    this.configuration = configuration;
    this.header = header;
    this.footer = footer;
  }
  
  public Configuration getConfiguration() {
    return this.configuration;
  }
  
  public Map<String, String> getContentFormat() {
    return new HashMap<>();
  }
  
  public byte[] getFooter() {
    return this.footer;
  }
  
  public byte[] getHeader() {
    return this.header;
  }
  
  protected void markEvent() {
    this.eventCount++;
  }
  
  public void encode(LogEvent event, ByteBufferDestination destination) {
    byte[] data = toByteArray(event);
    destination.writeBytes(data, 0, data.length);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\AbstractLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */