/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.OutputStream;
import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import org.apache.logging.log4j.core.util.NullOutputStream;

@Plugin(name="OutputStream", category="Core", elementType="appender", printObject=true)
public final class OutputStreamAppender
extends AbstractOutputStreamAppender<OutputStreamManager> {
    private static OutputStreamManagerFactory factory = new OutputStreamManagerFactory();

    @PluginFactory
    public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, Filter filter, OutputStream target, String name, boolean follow, boolean ignore) {
        if (name == null) {
            LOGGER.error("No name provided for OutputStreamAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new OutputStreamAppender(name, (Layout<? extends Serializable>)layout, filter, OutputStreamAppender.getManager(target, follow, layout), ignore, null);
    }

    private static OutputStreamManager getManager(OutputStream target, boolean follow, Layout<? extends Serializable> layout) {
        OutputStream os = target == null ? NullOutputStream.getInstance() : new CloseShieldOutputStream(target);
        OutputStream targetRef = target == null ? os : target;
        String managerName = targetRef.getClass().getName() + "@" + Integer.toHexString(targetRef.hashCode()) + '.' + follow;
        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private OutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Property[] properties) {
        super(name, layout, filter, ignoreExceptions, true, properties, manager);
    }

    private static class OutputStreamManagerFactory
    implements ManagerFactory<OutputStreamManager, FactoryData> {
        private OutputStreamManagerFactory() {
        }

        @Override
        public OutputStreamManager createManager(String name, FactoryData data) {
            return new OutputStreamManager(data.os, data.name, data.layout, true);
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

    public static class Builder<B extends Builder<B>>
    extends AbstractOutputStreamAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender> {
        private boolean follow = false;
        private final boolean ignoreExceptions = true;
        private OutputStream target;

        @Override
        public OutputStreamAppender build() {
            Layout<Serializable> layout = this.getLayout();
            Layout<String> actualLayout = layout == null ? PatternLayout.createDefaultLayout() : layout;
            return new OutputStreamAppender(this.getName(), actualLayout, this.getFilter(), OutputStreamAppender.getManager(this.target, this.follow, actualLayout), true, this.getPropertyArray());
        }

        public B setFollow(boolean shouldFollow) {
            this.follow = shouldFollow;
            return (B)((Builder)this.asBuilder());
        }

        public B setTarget(OutputStream aTarget) {
            this.target = aTarget;
            return (B)((Builder)this.asBuilder());
        }
    }
}

