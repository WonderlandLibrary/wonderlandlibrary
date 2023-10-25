package org.apache.logging.log4j.core.appender;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
public final class ConsoleAppender extends AbstractOutputStreamAppender<OutputStreamManager> {
  public static final String PLUGIN_NAME = "Console";
  
  private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
  
  private static ConsoleManagerFactory factory = new ConsoleManagerFactory();
  
  private static final Target DEFAULT_TARGET = Target.SYSTEM_OUT;
  
  private static final AtomicInteger COUNT = new AtomicInteger();
  
  private final Target target;
  
  public enum Target {
    SYSTEM_OUT {
      public Charset getDefaultCharset() {
        return getCharset("sun.stdout.encoding", Charset.defaultCharset());
      }
    },
    SYSTEM_ERR {
      public Charset getDefaultCharset() {
        return getCharset("sun.stderr.encoding", Charset.defaultCharset());
      }
    };
    
    protected Charset getCharset(String property, Charset defaultCharset) {
      return (new PropertiesUtil(PropertiesUtil.getSystemProperties())).getCharsetProperty(property, defaultCharset);
    }
    
    public abstract Charset getDefaultCharset();
  }
  
  private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Target target, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, true, properties, manager);
    this.target = target;
  }
  
  @Deprecated
  public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, String targetStr, String name, String follow, String ignore) {
    PatternLayout patternLayout;
    if (name == null) {
      LOGGER.error("No name provided for ConsoleAppender");
      return null;
    } 
    if (layout == null)
      patternLayout = PatternLayout.createDefaultLayout(); 
    boolean isFollow = Boolean.parseBoolean(follow);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    Target target = (targetStr == null) ? DEFAULT_TARGET : Target.valueOf(targetStr);
    return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, isFollow, false, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target, null);
  }
  
  @Deprecated
  public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, Target target, String name, boolean follow, boolean direct, boolean ignoreExceptions) {
    PatternLayout patternLayout;
    if (name == null) {
      LOGGER.error("No name provided for ConsoleAppender");
      return null;
    } 
    if (layout == null)
      patternLayout = PatternLayout.createDefaultLayout(); 
    target = (target == null) ? Target.SYSTEM_OUT : target;
    if (follow && direct) {
      LOGGER.error("Cannot use both follow and direct on ConsoleAppender");
      return null;
    } 
    return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, direct, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target, null);
  }
  
  public static ConsoleAppender createDefaultAppenderForLayout(Layout<? extends Serializable> layout) {
    return new ConsoleAppender("DefaultConsole-" + COUNT.incrementAndGet(), layout, null, 
        getDefaultManager(DEFAULT_TARGET, false, false, layout), true, DEFAULT_TARGET, null);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<ConsoleAppender> {
    @PluginBuilderAttribute
    @Required
    private ConsoleAppender.Target target = ConsoleAppender.DEFAULT_TARGET;
    
    @PluginBuilderAttribute
    private boolean follow;
    
    @PluginBuilderAttribute
    private boolean direct;
    
    public B setTarget(ConsoleAppender.Target aTarget) {
      this.target = aTarget;
      return (B)asBuilder();
    }
    
    public B setFollow(boolean shouldFollow) {
      this.follow = shouldFollow;
      return (B)asBuilder();
    }
    
    public B setDirect(boolean shouldDirect) {
      this.direct = shouldDirect;
      return (B)asBuilder();
    }
    
    public ConsoleAppender build() {
      if (this.follow && this.direct)
        throw new IllegalArgumentException("Cannot use both follow and direct on ConsoleAppender '" + getName() + "'"); 
      Layout<? extends Serializable> layout = getOrCreateLayout(this.target.getDefaultCharset());
      return new ConsoleAppender(getName(), layout, getFilter(), ConsoleAppender.getManager(this.target, this.follow, this.direct, layout), 
          isIgnoreExceptions(), this.target, getPropertyArray());
    }
  }
  
  private static OutputStreamManager getDefaultManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
    OutputStream os = getOutputStream(follow, direct, target);
    String managerName = target.name() + '.' + follow + '.' + direct + "-" + COUNT.get();
    return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
  }
  
  private static OutputStreamManager getManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
    OutputStream os = getOutputStream(follow, direct, target);
    String managerName = target.name() + '.' + follow + '.' + direct;
    return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
  }
  
  private static OutputStream getOutputStream(boolean follow, boolean direct, Target target) {
    CloseShieldOutputStream closeShieldOutputStream;
    String enc = Charset.defaultCharset().name();
    try {
      OutputStream outputStream = (target == Target.SYSTEM_OUT) ? (direct ? new FileOutputStream(FileDescriptor.out) : (follow ? new PrintStream(new SystemOutStream(), true, enc) : System.out)) : (direct ? new FileOutputStream(FileDescriptor.err) : (follow ? new PrintStream(new SystemErrStream(), true, enc) : System.err));
      closeShieldOutputStream = new CloseShieldOutputStream(outputStream);
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Unsupported default encoding " + enc, ex);
    } 
    PropertiesUtil propsUtil = PropertiesUtil.getProperties();
    if (!propsUtil.isOsWindows() || propsUtil.getBooleanProperty("log4j.skipJansi", true) || direct)
      return (OutputStream)closeShieldOutputStream; 
    try {
      Class<?> clazz = Loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
      Constructor<?> constructor = clazz.getConstructor(new Class[] { OutputStream.class });
      return (OutputStream)new CloseShieldOutputStream((OutputStream)constructor.newInstance(new Object[] { closeShieldOutputStream }));
    } catch (ClassNotFoundException cnfe) {
      LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
    } catch (NoSuchMethodException nsme) {
      LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
    } catch (Exception ex) {
      LOGGER.warn("Unable to instantiate {} due to {}", "org.fusesource.jansi.WindowsAnsiOutputStream", clean(Throwables.getRootCause(ex).toString()).trim());
    } 
    return (OutputStream)closeShieldOutputStream;
  }
  
  private static String clean(String string) {
    return string.replace(false, ' ');
  }
  
  private static class SystemErrStream extends OutputStream {
    public void close() {}
    
    public void flush() {
      System.err.flush();
    }
    
    public void write(byte[] b) throws IOException {
      System.err.write(b);
    }
    
    public void write(byte[] b, int off, int len) throws IOException {
      System.err.write(b, off, len);
    }
    
    public void write(int b) {
      System.err.write(b);
    }
  }
  
  private static class SystemOutStream extends OutputStream {
    public void close() {}
    
    public void flush() {
      System.out.flush();
    }
    
    public void write(byte[] b) throws IOException {
      System.out.write(b);
    }
    
    public void write(byte[] b, int off, int len) throws IOException {
      System.out.write(b, off, len);
    }
    
    public void write(int b) throws IOException {
      System.out.write(b);
    }
  }
  
  private static class FactoryData {
    private final OutputStream os;
    
    private final String name;
    
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
      this.os = os;
      this.name = type;
      this.layout = layout;
    }
  }
  
  private static class ConsoleManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData> {
    private ConsoleManagerFactory() {}
    
    public OutputStreamManager createManager(String name, ConsoleAppender.FactoryData data) {
      return new OutputStreamManager(data.os, data.name, data.layout, true);
    }
  }
  
  public Target getTarget() {
    return this.target;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\ConsoleAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */