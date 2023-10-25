/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(name="Console", category="Core", elementType="appender", printObject=true)
public final class ConsoleAppender
extends AbstractOutputStreamAppender {
    private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
    private static ConsoleManagerFactory factory = new ConsoleManagerFactory();

    private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions) {
        super(name, layout, filter, ignoreExceptions, true, manager);
    }

    @PluginFactory
    public static ConsoleAppender createAppender(@PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="target") String t, @PluginAttribute(value="name") String name, @PluginAttribute(value="follow") String follow, @PluginAttribute(value="ignoreExceptions") String ignore) {
        if (name == null) {
            LOGGER.error("No name provided for ConsoleAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        boolean isFollow = Boolean.parseBoolean(follow);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        Target target = t == null ? Target.SYSTEM_OUT : Target.valueOf(t);
        return new ConsoleAppender(name, layout, filter, ConsoleAppender.getManager(isFollow, target, layout), ignoreExceptions);
    }

    private static OutputStreamManager getManager(boolean follow, Target target, Layout<? extends Serializable> layout) {
        String type = target.name();
        OutputStream os = ConsoleAppender.getOutputStream(follow, target);
        return OutputStreamManager.getManager(target.name() + "." + follow, new FactoryData(os, type, layout), factory);
    }

    private static OutputStream getOutputStream(boolean follow, Target target) {
        String enc = Charset.defaultCharset().name();
        PrintStream printStream = null;
        try {
            printStream = target == Target.SYSTEM_OUT ? (follow ? new PrintStream((OutputStream)new SystemOutStream(), true, enc) : System.out) : (follow ? new PrintStream((OutputStream)new SystemErrStream(), true, enc) : System.err);
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unsupported default encoding " + enc, ex);
        }
        PropertiesUtil propsUtil = PropertiesUtil.getProperties();
        if (!propsUtil.getStringProperty("os.name").startsWith("Windows") || propsUtil.getBooleanProperty("log4j.skipJansi")) {
            return printStream;
        }
        try {
            ClassLoader loader = Loader.getClassLoader();
            Class<?> clazz = loader.loadClass(JANSI_CLASS);
            Constructor<?> constructor = clazz.getConstructor(OutputStream.class);
            return (OutputStream)constructor.newInstance(printStream);
        }
        catch (ClassNotFoundException cnfe) {
            LOGGER.debug("Jansi is not installed, cannot find {}", JANSI_CLASS);
        }
        catch (NoSuchMethodException nsme) {
            LOGGER.warn("{} is missing the proper constructor", JANSI_CLASS);
        }
        catch (Exception ex) {
            LOGGER.warn("Unable to instantiate {}", JANSI_CLASS);
        }
        return printStream;
    }

    private static class ConsoleManagerFactory
    implements ManagerFactory<OutputStreamManager, FactoryData> {
        private ConsoleManagerFactory() {
        }

        @Override
        public OutputStreamManager createManager(String name, FactoryData data) {
            return new OutputStreamManager(data.os, data.type, data.layout);
        }
    }

    private static class FactoryData {
        private final OutputStream os;
        private final String type;
        private final Layout<? extends Serializable> layout;

        public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
            this.os = os;
            this.type = type;
            this.layout = layout;
        }
    }

    private static class SystemOutStream
    extends OutputStream {
        @Override
        public void close() {
        }

        @Override
        public void flush() {
            System.out.flush();
        }

        @Override
        public void write(byte[] b) throws IOException {
            System.out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            System.out.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            System.out.write(b);
        }
    }

    private static class SystemErrStream
    extends OutputStream {
        @Override
        public void close() {
        }

        @Override
        public void flush() {
            System.err.flush();
        }

        @Override
        public void write(byte[] b) throws IOException {
            System.err.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            System.err.write(b, off, len);
        }

        @Override
        public void write(int b) {
            System.err.write(b);
        }
    }

    public static enum Target {
        SYSTEM_OUT,
        SYSTEM_ERR;

    }
}

