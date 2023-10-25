package org.apache.logging.log4j.core.appender;

import java.io.OutputStream;
import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import org.apache.logging.log4j.core.util.NullOutputStream;

@Plugin(name = "OutputStream", category = "Core", elementType = "appender", printObject = true)
public final class OutputStreamAppender extends AbstractOutputStreamAppender<OutputStreamManager> {
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender> {
    private boolean follow = false;
    
    private final boolean ignoreExceptions = true;
    
    private OutputStream target;
    
    public OutputStreamAppender build() {
      Layout<? extends Serializable> layout = getLayout();
      Layout<? extends Serializable> actualLayout = (layout == null) ? (Layout<? extends Serializable>)PatternLayout.createDefaultLayout() : layout;
      return new OutputStreamAppender(getName(), actualLayout, getFilter(), OutputStreamAppender.getManager(this.target, this.follow, actualLayout), true, 
          getPropertyArray());
    }
    
    public B setFollow(boolean shouldFollow) {
      this.follow = shouldFollow;
      return (B)asBuilder();
    }
    
    public B setTarget(OutputStream aTarget) {
      this.target = aTarget;
      return (B)asBuilder();
    }
  }
  
  private static class FactoryData {
    private final Layout<? extends Serializable> layout;
    
    private final String name;
    
    private final OutputStream os;
    
    public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
      this.os = os;
      this.name = type;
      this.layout = layout;
    }
  }
  
  private static class OutputStreamManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData> {
    private OutputStreamManagerFactory() {}
    
    public OutputStreamManager createManager(String name, OutputStreamAppender.FactoryData data) {
      return new OutputStreamManager(data.os, data.name, data.layout, true);
    }
  }
  
  private static OutputStreamManagerFactory factory = new OutputStreamManagerFactory();
  
  @PluginFactory
  public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, Filter filter, OutputStream target, String name, boolean follow, boolean ignore) {
    PatternLayout patternLayout;
    if (name == null) {
      LOGGER.error("No name provided for OutputStreamAppender");
      return null;
    } 
    if (layout == null)
      patternLayout = PatternLayout.createDefaultLayout(); 
    return new OutputStreamAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, (Layout<? extends Serializable>)patternLayout), ignore, null);
  }
  
  private static OutputStreamManager getManager(OutputStream target, boolean follow, Layout<? extends Serializable> layout) {
    OutputStream os = (target == null) ? (OutputStream)NullOutputStream.getInstance() : (OutputStream)new CloseShieldOutputStream(target);
    OutputStream targetRef = (target == null) ? os : target;
    String managerName = targetRef.getClass().getName() + "@" + Integer.toHexString(targetRef.hashCode()) + '.' + follow;
    return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private OutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, true, properties, manager);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\OutputStreamAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */