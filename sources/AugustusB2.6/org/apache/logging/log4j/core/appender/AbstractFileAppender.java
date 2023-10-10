// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;

public abstract class AbstractFileAppender<M extends OutputStreamManager> extends AbstractOutputStreamAppender<M>
{
    private final String fileName;
    private final Advertiser advertiser;
    private final Object advertisement;
    
    private AbstractFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final M manager, final String filename, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        else {
            this.advertisement = null;
        }
        this.fileName = filename;
        this.advertiser = advertiser;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return true;
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B>
    {
        @PluginBuilderAttribute
        @Required
        private String fileName;
        @PluginBuilderAttribute
        private boolean append;
        @PluginBuilderAttribute
        private boolean locking;
        @PluginBuilderAttribute
        private boolean advertise;
        @PluginBuilderAttribute
        private String advertiseUri;
        @PluginBuilderAttribute
        private boolean createOnDemand;
        @PluginBuilderAttribute
        private String filePermissions;
        @PluginBuilderAttribute
        private String fileOwner;
        @PluginBuilderAttribute
        private String fileGroup;
        
        public Builder() {
            this.append = true;
        }
        
        public String getAdvertiseUri() {
            return this.advertiseUri;
        }
        
        public String getFileName() {
            return this.fileName;
        }
        
        public boolean isAdvertise() {
            return this.advertise;
        }
        
        public boolean isAppend() {
            return this.append;
        }
        
        public boolean isCreateOnDemand() {
            return this.createOnDemand;
        }
        
        public boolean isLocking() {
            return this.locking;
        }
        
        public String getFilePermissions() {
            return this.filePermissions;
        }
        
        public String getFileOwner() {
            return this.fileOwner;
        }
        
        public String getFileGroup() {
            return this.fileGroup;
        }
        
        public B withAdvertise(final boolean advertise) {
            this.advertise = advertise;
            return this.asBuilder();
        }
        
        public B withAdvertiseUri(final String advertiseUri) {
            this.advertiseUri = advertiseUri;
            return this.asBuilder();
        }
        
        public B withAppend(final boolean append) {
            this.append = append;
            return this.asBuilder();
        }
        
        public B withFileName(final String fileName) {
            this.fileName = fileName;
            return this.asBuilder();
        }
        
        public B withCreateOnDemand(final boolean createOnDemand) {
            this.createOnDemand = createOnDemand;
            return this.asBuilder();
        }
        
        public B withLocking(final boolean locking) {
            this.locking = locking;
            return this.asBuilder();
        }
        
        public B withFilePermissions(final String filePermissions) {
            this.filePermissions = filePermissions;
            return this.asBuilder();
        }
        
        public B withFileOwner(final String fileOwner) {
            this.fileOwner = fileOwner;
            return this.asBuilder();
        }
        
        public B withFileGroup(final String fileGroup) {
            this.fileGroup = fileGroup;
            return this.asBuilder();
        }
    }
}
