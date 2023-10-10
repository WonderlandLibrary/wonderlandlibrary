// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.io.UnsupportedEncodingException;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.nio.charset.Charset;
import java.io.OutputStream;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
public final class ConsoleAppender extends AbstractOutputStreamAppender<OutputStreamManager>
{
    public static final String PLUGIN_NAME = "Console";
    private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
    private static ConsoleManagerFactory factory;
    private static final Target DEFAULT_TARGET;
    private static final AtomicInteger COUNT;
    private final Target target;
    
    private ConsoleAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final OutputStreamManager manager, final boolean ignoreExceptions, final Target target, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, true, properties, manager);
        this.target = target;
    }
    
    @Deprecated
    public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, final Filter filter, final String targetStr, final String name, final String follow, final String ignore) {
        if (name == null) {
            ConsoleAppender.LOGGER.error("No name provided for ConsoleAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        final boolean isFollow = Boolean.parseBoolean(follow);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final Target target = (targetStr == null) ? ConsoleAppender.DEFAULT_TARGET : Target.valueOf(targetStr);
        return new ConsoleAppender(name, layout, filter, getManager(target, isFollow, false, layout), ignoreExceptions, target, null);
    }
    
    @Deprecated
    public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, final Filter filter, Target target, final String name, final boolean follow, final boolean direct, final boolean ignoreExceptions) {
        if (name == null) {
            ConsoleAppender.LOGGER.error("No name provided for ConsoleAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        target = ((target == null) ? Target.SYSTEM_OUT : target);
        if (follow && direct) {
            ConsoleAppender.LOGGER.error("Cannot use both follow and direct on ConsoleAppender");
            return null;
        }
        return new ConsoleAppender(name, layout, filter, getManager(target, follow, direct, layout), ignoreExceptions, target, null);
    }
    
    public static ConsoleAppender createDefaultAppenderForLayout(final Layout<? extends Serializable> layout) {
        return new ConsoleAppender("DefaultConsole-" + ConsoleAppender.COUNT.incrementAndGet(), layout, null, getDefaultManager(ConsoleAppender.DEFAULT_TARGET, false, false, layout), true, ConsoleAppender.DEFAULT_TARGET, null);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private static OutputStreamManager getDefaultManager(final Target target, final boolean follow, final boolean direct, final Layout<? extends Serializable> layout) {
        final OutputStream os = getOutputStream(follow, direct, target);
        final String managerName = target.name() + '.' + follow + '.' + direct + "-" + ConsoleAppender.COUNT.get();
        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), ConsoleAppender.factory);
    }
    
    private static OutputStreamManager getManager(final Target target, final boolean follow, final boolean direct, final Layout<? extends Serializable> layout) {
        final OutputStream os = getOutputStream(follow, direct, target);
        final String managerName = target.name() + '.' + follow + '.' + direct;
        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), ConsoleAppender.factory);
    }
    
    private static OutputStream getOutputStream(final boolean follow, final boolean direct, final Target target) {
        final String enc = Charset.defaultCharset().name();
        OutputStream outputStream;
        try {
            OutputStream outputStream2;
            if (target == Target.SYSTEM_OUT) {
                if (direct) {
                    outputStream2 = new FileOutputStream(FileDescriptor.out);
                }
                else if (follow) {
                    final SystemOutStream out;
                    outputStream2 = new PrintStream(out, true, enc);
                    out = new SystemOutStream();
                }
                else {
                    outputStream2 = System.out;
                }
            }
            else if (direct) {
                outputStream2 = new FileOutputStream(FileDescriptor.err);
            }
            else if (follow) {
                final SystemErrStream out2;
                outputStream2 = new PrintStream(out2, true, enc);
                out2 = new SystemErrStream();
            }
            else {
                outputStream2 = System.err;
            }
            outputStream = outputStream2;
            outputStream = new CloseShieldOutputStream(outputStream);
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unsupported default encoding " + enc, ex);
        }
        final PropertiesUtil propsUtil = PropertiesUtil.getProperties();
        if (!propsUtil.isOsWindows() || propsUtil.getBooleanProperty("log4j.skipJansi", true) || direct) {
            return outputStream;
        }
        try {
            final Class<?> clazz = Loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
            final Constructor<?> constructor = clazz.getConstructor(OutputStream.class);
            return new CloseShieldOutputStream((OutputStream)constructor.newInstance(outputStream));
        }
        catch (ClassNotFoundException cnfe) {
            ConsoleAppender.LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
        }
        catch (NoSuchMethodException nsme) {
            ConsoleAppender.LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
        }
        catch (Exception ex2) {
            ConsoleAppender.LOGGER.warn("Unable to instantiate {} due to {}", "org.fusesource.jansi.WindowsAnsiOutputStream", clean(Throwables.getRootCause(ex2).toString()).trim());
        }
        return outputStream;
    }
    
    private static String clean(final String string) {
        return string.replace('\0', ' ');
    }
    
    public Target getTarget() {
        return this.target;
    }
    
    static {
        ConsoleAppender.factory = new ConsoleManagerFactory();
        DEFAULT_TARGET = Target.SYSTEM_OUT;
        COUNT = new AtomicInteger();
    }
    
    public enum Target
    {
        SYSTEM_OUT {
            @Override
            public Charset getDefaultCharset() {
                return this.getCharset("sun.stdout.encoding", Charset.defaultCharset());
            }
        }, 
        SYSTEM_ERR {
            @Override
            public Charset getDefaultCharset() {
                return this.getCharset("sun.stderr.encoding", Charset.defaultCharset());
            }
        };
        
        public abstract Charset getDefaultCharset();
        
        protected Charset getCharset(final String property, final Charset defaultCharset) {
            return new PropertiesUtil(PropertiesUtil.getSystemProperties()).getCharsetProperty(property, defaultCharset);
        }
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<ConsoleAppender>
    {
        @PluginBuilderAttribute
        @Required
        private Target target;
        @PluginBuilderAttribute
        private boolean follow;
        @PluginBuilderAttribute
        private boolean direct;
        
        public Builder() {
            this.target = ConsoleAppender.DEFAULT_TARGET;
        }
        
        public B setTarget(final Target aTarget) {
            this.target = aTarget;
            return this.asBuilder();
        }
        
        public B setFollow(final boolean shouldFollow) {
            this.follow = shouldFollow;
            return this.asBuilder();
        }
        
        public B setDirect(final boolean shouldDirect) {
            this.direct = shouldDirect;
            return this.asBuilder();
        }
        
        @Override
        public ConsoleAppender build() {
            if (this.follow && this.direct) {
                throw new IllegalArgumentException("Cannot use both follow and direct on ConsoleAppender '" + this.getName() + "'");
            }
            final Layout<? extends Serializable> layout = this.getOrCreateLayout(this.target.getDefaultCharset());
            return new ConsoleAppender(this.getName(), layout, this.getFilter(), getManager(this.target, this.follow, this.direct, layout), this.isIgnoreExceptions(), this.target, this.getPropertyArray(), null);
        }
    }
    
    private static class SystemErrStream extends OutputStream
    {
        public SystemErrStream() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
            System.err.flush();
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            System.err.write(b);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.err.write(b, off, len);
        }
        
        @Override
        public void write(final int b) {
            System.err.write(b);
        }
    }
    
    private static class SystemOutStream extends OutputStream
    {
        public SystemOutStream() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
            System.out.flush();
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            System.out.write(b);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.out.write(b, off, len);
        }
        
        @Override
        public void write(final int b) throws IOException {
            System.out.write(b);
        }
    }
    
    private static class FactoryData
    {
        private final OutputStream os;
        private final String name;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final OutputStream os, final String type, final Layout<? extends Serializable> layout) {
            this.os = os;
            this.name = type;
            this.layout = layout;
        }
    }
    
    private static class ConsoleManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData>
    {
        @Override
        public OutputStreamManager createManager(final String name, final FactoryData data) {
            return new OutputStreamManager(data.os, data.name, data.layout, true);
        }
    }
}
