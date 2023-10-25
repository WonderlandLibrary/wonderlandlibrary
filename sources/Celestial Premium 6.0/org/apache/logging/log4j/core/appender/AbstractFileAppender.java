/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.Advertiser;

public abstract class AbstractFileAppender<M extends OutputStreamManager>
extends AbstractOutputStreamAppender<M> {
    private final String fileName;
    private final Advertiser advertiser;
    private final Object advertisement;

    private AbstractFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, M manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            HashMap<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(((AbstractManager)manager).getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        } else {
            this.advertisement = null;
        }
        this.fileName = filename;
        this.advertiser = advertiser;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return true;
    }

    public static abstract class Builder<B extends Builder<B>>
    extends AbstractOutputStreamAppender.Builder<B> {
        @PluginBuilderAttribute
        @Required
        private String fileName;
        @PluginBuilderAttribute
        private boolean append = true;
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

        public B withAdvertise(boolean advertise) {
            this.advertise = advertise;
            return (B)((Builder)this.asBuilder());
        }

        public B withAdvertiseUri(String advertiseUri) {
            this.advertiseUri = advertiseUri;
            return (B)((Builder)this.asBuilder());
        }

        public B withAppend(boolean append) {
            this.append = append;
            return (B)((Builder)this.asBuilder());
        }

        public B withFileName(String fileName) {
            this.fileName = fileName;
            return (B)((Builder)this.asBuilder());
        }

        public B withCreateOnDemand(boolean createOnDemand) {
            this.createOnDemand = createOnDemand;
            return (B)((Builder)this.asBuilder());
        }

        public B withLocking(boolean locking) {
            this.locking = locking;
            return (B)((Builder)this.asBuilder());
        }

        public B withFilePermissions(String filePermissions) {
            this.filePermissions = filePermissions;
            return (B)((Builder)this.asBuilder());
        }

        public B withFileOwner(String fileOwner) {
            this.fileOwner = fileOwner;
            return (B)((Builder)this.asBuilder());
        }

        public B withFileGroup(String fileGroup) {
            this.fileGroup = fileGroup;
            return (B)((Builder)this.asBuilder());
        }
    }
}

