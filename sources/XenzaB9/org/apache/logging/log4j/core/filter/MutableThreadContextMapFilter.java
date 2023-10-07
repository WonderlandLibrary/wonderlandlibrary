// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.util.Iterator;
import java.io.InputStream;
import java.util.Map;
import org.apache.logging.log4j.core.filter.mutable.KeyValuePairConfig;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.util.internal.HttpInputStreamUtil;
import org.apache.logging.log4j.core.util.internal.Status;
import java.io.FileInputStream;
import java.io.File;
import java.net.URI;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.concurrent.ScheduledFuture;
import java.util.List;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.util.internal.LastModifiedSource;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.util.KeyValuePair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MutableThreadContextMapFilter", category = "Core", elementType = "filter", printObject = true)
@PluginAliases({ "MutableContextMapFilter" })
@PerformanceSensitive({ "allocation" })
public class MutableThreadContextMapFilter extends AbstractFilter
{
    private static final ObjectMapper MAPPER;
    private static final KeyValuePair[] EMPTY_ARRAY;
    private volatile Filter filter;
    private final long pollInterval;
    private final ConfigurationScheduler scheduler;
    private final LastModifiedSource source;
    private final AuthorizationProvider authorizationProvider;
    private final List<FilterConfigUpdateListener> listeners;
    private ScheduledFuture<?> future;
    
    private MutableThreadContextMapFilter(final Filter filter, final LastModifiedSource source, final long pollInterval, final AuthorizationProvider authorizationProvider, final Filter.Result onMatch, final Filter.Result onMismatch, final Configuration configuration) {
        super(onMatch, onMismatch);
        this.listeners = new ArrayList<FilterConfigUpdateListener>();
        this.future = null;
        this.filter = filter;
        this.pollInterval = pollInterval;
        this.source = source;
        this.scheduler = configuration.getScheduler();
        this.authorizationProvider = authorizationProvider;
    }
    
    @Override
    public void start() {
        if (this.pollInterval > 0L) {
            this.future = this.scheduler.scheduleWithFixedDelay(new FileMonitor(), 0L, this.pollInterval, TimeUnit.SECONDS);
            MutableThreadContextMapFilter.LOGGER.debug("Watching {} with poll interval {}", this.source.toString(), this.pollInterval);
        }
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.future.cancel(true);
        return super.stop(timeout, timeUnit);
    }
    
    public void registerListener(final FilterConfigUpdateListener listener) {
        this.listeners.add(listener);
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter.filter(event);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter.filter(logger, level, marker, msg, t);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter.filter(logger, level, marker, msg, t);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter.filter(logger, level, marker, msg, params);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter.filter(logger, level, marker, msg, p0);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter.filter(logger, level, marker, msg, p0, p1);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter.filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    private static LastModifiedSource getSource(final String configLocation) {
        LastModifiedSource source = null;
        try {
            final URI uri = new URI(configLocation);
            if (uri.getScheme() != null) {
                source = new LastModifiedSource(new URI(configLocation));
            }
            else {
                source = new LastModifiedSource(new File(configLocation));
            }
        }
        catch (final Exception ex) {
            source = new LastModifiedSource(new File(configLocation));
        }
        return source;
    }
    
    private static ConfigResult getConfig(final LastModifiedSource source, final AuthorizationProvider authorizationProvider) {
        final File inputFile = source.getFile();
        InputStream inputStream = null;
        HttpInputStreamUtil.Result result = null;
        final long lastModified = source.getLastModified();
        if (inputFile != null && inputFile.exists()) {
            try {
                final long modified = inputFile.lastModified();
                if (modified > lastModified) {
                    source.setLastModified(modified);
                    inputStream = new FileInputStream(inputFile);
                    result = new HttpInputStreamUtil.Result(Status.SUCCESS);
                }
                else {
                    result = new HttpInputStreamUtil.Result(Status.NOT_MODIFIED);
                }
            }
            catch (final Exception ex) {
                result = new HttpInputStreamUtil.Result(Status.ERROR);
            }
        }
        else if (source.getURI() != null) {
            try {
                result = HttpInputStreamUtil.getInputStream(source, authorizationProvider);
                inputStream = result.getInputStream();
            }
            catch (final ConfigurationException ex2) {
                result = new HttpInputStreamUtil.Result(Status.ERROR);
            }
        }
        else {
            result = new HttpInputStreamUtil.Result(Status.NOT_FOUND);
        }
        final ConfigResult configResult = new ConfigResult();
        if (result.getStatus() == Status.SUCCESS) {
            MutableThreadContextMapFilter.LOGGER.debug("Processing Debug key/value pairs from: {}", source.toString());
            try {
                final KeyValuePairConfig keyValuePairConfig = (KeyValuePairConfig)MutableThreadContextMapFilter.MAPPER.readValue(inputStream, (Class)KeyValuePairConfig.class);
                if (keyValuePairConfig != null) {
                    final Map<String, String[]> configs = keyValuePairConfig.getConfigs();
                    if (configs != null && configs.size() > 0) {
                        final List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
                        for (final Map.Entry<String, String[]> entry : configs.entrySet()) {
                            final String key = entry.getKey();
                            for (final String value : entry.getValue()) {
                                if (value != null) {
                                    pairs.add(new KeyValuePair(key, value));
                                }
                                else {
                                    MutableThreadContextMapFilter.LOGGER.warn("Ignoring null value for {}", key);
                                }
                            }
                        }
                        if (pairs.size() > 0) {
                            configResult.pairs = pairs.toArray(MutableThreadContextMapFilter.EMPTY_ARRAY);
                            configResult.status = Status.SUCCESS;
                        }
                        else {
                            configResult.status = Status.EMPTY;
                        }
                    }
                    else {
                        MutableThreadContextMapFilter.LOGGER.debug("No configuration data in {}", source.toString());
                        configResult.status = Status.EMPTY;
                    }
                }
                else {
                    MutableThreadContextMapFilter.LOGGER.warn("No configs element in MutableThreadContextMapFilter configuration");
                    configResult.status = Status.ERROR;
                }
            }
            catch (final Exception ex3) {
                MutableThreadContextMapFilter.LOGGER.warn("Invalid key/value pair configuration, input ignored: {}", ex3.getMessage());
                configResult.status = Status.ERROR;
            }
        }
        else {
            configResult.status = result.getStatus();
        }
        return configResult;
    }
    
    static {
        MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        EMPTY_ARRAY = new KeyValuePair[0];
    }
    
    public static class Builder extends AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<MutableThreadContextMapFilter>
    {
        @PluginBuilderAttribute
        private String configLocation;
        @PluginBuilderAttribute
        private long pollInterval;
        @PluginConfiguration
        private Configuration configuration;
        
        public Builder setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder setPollInterval(final long pollInterval) {
            this.pollInterval = pollInterval;
            return this;
        }
        
        public Builder setConfigLocation(final String configLocation) {
            this.configLocation = configLocation;
            return this;
        }
        
        @Override
        public MutableThreadContextMapFilter build() {
            final LastModifiedSource source = getSource(this.configLocation);
            if (source == null) {
                return new MutableThreadContextMapFilter(new NoOpFilter(), null, 0L, null, this.getOnMatch(), this.getOnMismatch(), this.configuration, null);
            }
            final AuthorizationProvider authorizationProvider = ConfigurationFactory.authorizationProvider(PropertiesUtil.getProperties());
            Filter filter;
            if (this.pollInterval <= 0L) {
                final ConfigResult result = getConfig(source, authorizationProvider);
                if (result.status == Status.SUCCESS) {
                    if (result.pairs.length > 0) {
                        filter = ThreadContextMapFilter.createFilter(result.pairs, "or", this.getOnMatch(), this.getOnMismatch());
                    }
                    else {
                        filter = new NoOpFilter();
                    }
                }
                else if (result.status == Status.NOT_FOUND || result.status == Status.EMPTY) {
                    filter = new NoOpFilter();
                }
                else {
                    MutableThreadContextMapFilter.LOGGER.warn("Unexpected response returned on initial call: {}", result.status);
                    filter = new NoOpFilter();
                }
            }
            else {
                filter = new NoOpFilter();
            }
            if (this.pollInterval > 0L) {
                this.configuration.getScheduler().incrementScheduledItems();
            }
            return new MutableThreadContextMapFilter(filter, source, this.pollInterval, authorizationProvider, this.getOnMatch(), this.getOnMismatch(), this.configuration, null);
        }
    }
    
    private class FileMonitor implements Runnable
    {
        @Override
        public void run() {
            final ConfigResult result = getConfig(MutableThreadContextMapFilter.this.source, MutableThreadContextMapFilter.this.authorizationProvider);
            if (result.status == Status.SUCCESS) {
                MutableThreadContextMapFilter.this.filter = ThreadContextMapFilter.createFilter(result.pairs, "or", MutableThreadContextMapFilter.this.getOnMatch(), MutableThreadContextMapFilter.this.getOnMismatch());
                MutableThreadContextMapFilter.LOGGER.info("Filter configuration was updated: {}", MutableThreadContextMapFilter.this.filter.toString());
                for (final FilterConfigUpdateListener listener : MutableThreadContextMapFilter.this.listeners) {
                    listener.onEvent();
                }
            }
            else if (result.status == Status.NOT_FOUND) {
                if (!(MutableThreadContextMapFilter.this.filter instanceof NoOpFilter)) {
                    MutableThreadContextMapFilter.LOGGER.info("Filter configuration was removed");
                    MutableThreadContextMapFilter.this.filter = new NoOpFilter();
                    for (final FilterConfigUpdateListener listener : MutableThreadContextMapFilter.this.listeners) {
                        listener.onEvent();
                    }
                }
            }
            else if (result.status == Status.EMPTY) {
                MutableThreadContextMapFilter.LOGGER.debug("Filter configuration is empty");
                MutableThreadContextMapFilter.this.filter = new NoOpFilter();
            }
        }
    }
    
    private static class NoOpFilter extends AbstractFilter
    {
        public NoOpFilter() {
            super(Filter.Result.NEUTRAL, Filter.Result.NEUTRAL);
        }
    }
    
    private static class ConfigResult extends HttpInputStreamUtil.Result
    {
        public KeyValuePair[] pairs;
        public Status status;
    }
    
    public interface FilterConfigUpdateListener
    {
        void onEvent();
    }
}
