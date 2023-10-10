// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import org.apache.logging.log4j.core.util.NullOutputStream;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.OutputStream;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "OutputStream", category = "Core", elementType = "appender", printObject = true)
public final class OutputStreamAppender extends AbstractOutputStreamAppender<OutputStreamManager>
{
    private static OutputStreamManagerFactory factory;
    
    @PluginFactory
    public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, final Filter filter, final OutputStream target, final String name, final boolean follow, final boolean ignore) {
        if (name == null) {
            OutputStreamAppender.LOGGER.error("No name provided for OutputStreamAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new OutputStreamAppender(name, layout, filter, getManager(target, follow, layout), ignore, null);
    }
    
    private static OutputStreamManager getManager(final OutputStream target, final boolean follow, final Layout<? extends Serializable> layout) {
        final OutputStream os = (target == null) ? NullOutputStream.getInstance() : new CloseShieldOutputStream(target);
        final OutputStream targetRef = (target == null) ? os : target;
        final String managerName = targetRef.getClass().getName() + "@" + Integer.toHexString(targetRef.hashCode()) + '.' + follow;
        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), OutputStreamAppender.factory);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private OutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final OutputStreamManager manager, final boolean ignoreExceptions, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, true, properties, manager);
    }
    
    static {
        OutputStreamAppender.factory = new OutputStreamManagerFactory();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender>
    {
        private boolean follow;
        private final boolean ignoreExceptions = true;
        private OutputStream target;
        
        public Builder() {
            this.follow = false;
        }
        
        @Override
        public OutputStreamAppender build() {
            final Layout<? extends Serializable> layout = this.getLayout();
            final Layout<? extends Serializable> actualLayout = (layout == null) ? PatternLayout.createDefaultLayout() : layout;
            return new OutputStreamAppender(this.getName(), actualLayout, this.getFilter(), getManager(this.target, this.follow, actualLayout), true, this.getPropertyArray(), null);
        }
        
        public B setFollow(final boolean shouldFollow) {
            this.follow = shouldFollow;
            return this.asBuilder();
        }
        
        public B setTarget(final OutputStream aTarget) {
            this.target = aTarget;
            return this.asBuilder();
        }
    }
    
    private static class FactoryData
    {
        private final Layout<? extends Serializable> layout;
        private final String name;
        private final OutputStream os;
        
        public FactoryData(final OutputStream os, final String type, final Layout<? extends Serializable> layout) {
            this.os = os;
            this.name = type;
            this.layout = layout;
        }
    }
    
    private static class OutputStreamManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData>
    {
        @Override
        public OutputStreamManager createManager(final String name, final FactoryData data) {
            return new OutputStreamManager(data.os, data.name, data.layout, true);
        }
    }
}
