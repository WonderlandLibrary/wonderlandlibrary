/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Writer;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractWriterAppender;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.WriterManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.CloseShieldWriter;

@Plugin(name="Writer", category="Core", elementType="appender", printObject=true)
public final class WriterAppender
extends AbstractWriterAppender<WriterManager> {
    private static WriterManagerFactory factory = new WriterManagerFactory();

    @PluginFactory
    public static WriterAppender createAppender(StringLayout layout, Filter filter, Writer target, String name, boolean follow, boolean ignore) {
        if (name == null) {
            LOGGER.error("No name provided for WriterAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WriterAppender(name, (StringLayout)layout, filter, WriterAppender.getManager(target, follow, layout), ignore, null);
    }

    private static WriterManager getManager(Writer target, boolean follow, StringLayout layout) {
        CloseShieldWriter writer = new CloseShieldWriter(target);
        String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
        return WriterManager.getManager(managerName, new FactoryData(writer, managerName, layout), factory);
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private WriterAppender(String name, StringLayout layout, Filter filter, WriterManager manager, boolean ignoreExceptions, Property[] properties) {
        super(name, layout, filter, ignoreExceptions, true, properties, manager);
    }

    private static class WriterManagerFactory
    implements ManagerFactory<WriterManager, FactoryData> {
        private WriterManagerFactory() {
        }

        @Override
        public WriterManager createManager(String name, FactoryData data) {
            return new WriterManager(data.writer, data.name, data.layout, true);
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

    public static class Builder<B extends Builder<B>>
    extends AbstractAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<WriterAppender> {
        private boolean follow = false;
        private Writer target;

        @Override
        public WriterAppender build() {
            StringLayout layout = (StringLayout)this.getLayout();
            Layout<String> actualLayout = layout != null ? layout : PatternLayout.createDefaultLayout();
            return new WriterAppender(this.getName(), (StringLayout)actualLayout, this.getFilter(), WriterAppender.getManager(this.target, this.follow, actualLayout), this.isIgnoreExceptions(), this.getPropertyArray());
        }

        public B setFollow(boolean shouldFollow) {
            this.follow = shouldFollow;
            return (B)((Builder)this.asBuilder());
        }

        public B setTarget(Writer aTarget) {
            this.target = aTarget;
            return (B)((Builder)this.asBuilder());
        }
    }
}

