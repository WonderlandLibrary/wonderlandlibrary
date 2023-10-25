package org.apache.logging.log4j.core.appender;

import java.io.Writer;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.CloseShieldWriter;

@Plugin(name = "Writer", category = "Core", elementType = "appender", printObject = true)
public final class WriterAppender extends AbstractWriterAppender<WriterManager> {
  public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<WriterAppender> {
    private boolean follow = false;
    
    private Writer target;
    
    public WriterAppender build() {
      StringLayout layout = (StringLayout)getLayout();
      StringLayout actualLayout = (layout != null) ? layout : (StringLayout)PatternLayout.createDefaultLayout();
      return new WriterAppender(getName(), actualLayout, getFilter(), WriterAppender.getManager(this.target, this.follow, actualLayout), 
          isIgnoreExceptions(), getPropertyArray());
    }
    
    public B setFollow(boolean shouldFollow) {
      this.follow = shouldFollow;
      return (B)asBuilder();
    }
    
    public B setTarget(Writer aTarget) {
      this.target = aTarget;
      return (B)asBuilder();
    }
  }
  
  private static class FactoryData {
    private final StringLayout layout;
    
    private final String name;
    
    private final Writer writer;
    
    public FactoryData(Writer writer, String type, StringLayout layout) {
      this.writer = writer;
      this.name = type;
      this.layout = layout;
    }
  }
  
  private static class WriterManagerFactory implements ManagerFactory<WriterManager, FactoryData> {
    private WriterManagerFactory() {}
    
    public WriterManager createManager(String name, WriterAppender.FactoryData data) {
      return new WriterManager(data.writer, data.name, data.layout, true);
    }
  }
  
  private static WriterManagerFactory factory = new WriterManagerFactory();
  
  @PluginFactory
  public static WriterAppender createAppender(StringLayout layout, Filter filter, Writer target, String name, boolean follow, boolean ignore) {
    PatternLayout patternLayout;
    if (name == null) {
      LOGGER.error("No name provided for WriterAppender");
      return null;
    } 
    if (layout == null)
      patternLayout = PatternLayout.createDefaultLayout(); 
    return new WriterAppender(name, (StringLayout)patternLayout, filter, getManager(target, follow, (StringLayout)patternLayout), ignore, null);
  }
  
  private static WriterManager getManager(Writer target, boolean follow, StringLayout layout) {
    CloseShieldWriter closeShieldWriter = new CloseShieldWriter(target);
    String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
    return WriterManager.getManager(managerName, new FactoryData((Writer)closeShieldWriter, managerName, layout), factory);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private WriterAppender(String name, StringLayout layout, Filter filter, WriterManager manager, boolean ignoreExceptions, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, true, properties, manager);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\WriterAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */