// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.CloseShieldWriter;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.Writer;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Writer", category = "Core", elementType = "appender", printObject = true)
public final class WriterAppender extends AbstractWriterAppender<WriterManager>
{
    private static WriterManagerFactory factory;
    
    @PluginFactory
    public static WriterAppender createAppender(StringLayout layout, final Filter filter, final Writer target, final String name, final boolean follow, final boolean ignore) {
        if (name == null) {
            WriterAppender.LOGGER.error("No name provided for WriterAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WriterAppender(name, layout, filter, getManager(target, follow, layout), ignore, null);
    }
    
    private static WriterManager getManager(final Writer target, final boolean follow, final StringLayout layout) {
        final Writer writer = new CloseShieldWriter(target);
        final String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
        return WriterManager.getManager(managerName, new FactoryData(writer, managerName, layout), WriterAppender.factory);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private WriterAppender(final String name, final StringLayout layout, final Filter filter, final WriterManager manager, final boolean ignoreExceptions, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, true, properties, manager);
    }
    
    static {
        WriterAppender.factory = new WriterManagerFactory();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<WriterAppender>
    {
        private boolean follow;
        private Writer target;
        
        public Builder() {
            this.follow = false;
        }
        
        @Override
        public WriterAppender build() {
            final Layout<? extends Serializable> layout = this.getOrCreateLayout();
            if (!(layout instanceof StringLayout)) {
                WriterAppender.LOGGER.error("Layout must be a StringLayout to log to ServletContext");
                return null;
            }
            final StringLayout stringLayout = (StringLayout)layout;
            return new WriterAppender(this.getName(), stringLayout, this.getFilter(), getManager(this.target, this.follow, stringLayout), this.isIgnoreExceptions(), this.getPropertyArray(), null);
        }
        
        public B setFollow(final boolean shouldFollow) {
            this.follow = shouldFollow;
            return this.asBuilder();
        }
        
        public B setTarget(final Writer aTarget) {
            this.target = aTarget;
            return this.asBuilder();
        }
    }
    
    private static class FactoryData
    {
        private final StringLayout layout;
        private final String name;
        private final Writer writer;
        
        public FactoryData(final Writer writer, final String type, final StringLayout layout) {
            this.writer = writer;
            this.name = type;
            this.layout = layout;
        }
    }
    
    private static class WriterManagerFactory implements ManagerFactory<WriterManager, FactoryData>
    {
        @Override
        public WriterManager createManager(final String name, final FactoryData data) {
            return new WriterManager(data.writer, data.name, data.layout, true);
        }
    }
}
