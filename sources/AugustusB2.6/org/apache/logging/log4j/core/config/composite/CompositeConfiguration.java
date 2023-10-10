// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import java.net.URI;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.util.ArrayList;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.Source;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import org.apache.logging.log4j.core.util.Patterns;
import java.util.Map;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.util.List;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.AbstractConfiguration;

public class CompositeConfiguration extends AbstractConfiguration implements Reconfigurable
{
    public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
    private static final String[] VERBOSE_CLASSES;
    private final List<? extends AbstractConfiguration> configurations;
    private MergeStrategy mergeStrategy;
    
    public CompositeConfiguration(final List<? extends AbstractConfiguration> configurations) {
        super(((AbstractConfiguration)configurations.get(0)).getLoggerContext(), ConfigurationSource.COMPOSITE_SOURCE);
        this.rootNode = ((AbstractConfiguration)configurations.get(0)).getRootNode();
        this.configurations = configurations;
        final String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty("log4j.mergeStrategy", DefaultMergeStrategy.class.getName());
        try {
            this.mergeStrategy = Loader.newInstanceOf(mergeStrategyClassName);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex2;
            this.mergeStrategy = new DefaultMergeStrategy();
        }
        for (final AbstractConfiguration config : configurations) {
            this.mergeStrategy.mergeRootProperties(this.rootNode, config);
        }
        final StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(CompositeConfiguration.VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
        for (final Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
            final String key = entry.getKey();
            final String value = this.getStrSubstitutor().replace(entry.getValue());
            if ("status".equalsIgnoreCase(key)) {
                statusConfig.withStatus(value.toUpperCase());
            }
            else if ("dest".equalsIgnoreCase(key)) {
                statusConfig.withDestination(value);
            }
            else if ("shutdownHook".equalsIgnoreCase(key)) {
                this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
            }
            else if ("shutdownTimeout".equalsIgnoreCase(key)) {
                this.shutdownTimeoutMillis = Long.parseLong(value);
            }
            else if ("verbose".equalsIgnoreCase(key)) {
                statusConfig.withVerbosity(value);
            }
            else if ("packages".equalsIgnoreCase(key)) {
                this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
            }
            else {
                if (!"name".equalsIgnoreCase(key)) {
                    continue;
                }
                this.setName(value);
            }
        }
        statusConfig.initialize();
    }
    
    @Override
    public void setup() {
        final AbstractConfiguration targetConfiguration = (AbstractConfiguration)this.configurations.get(0);
        this.staffChildConfiguration(targetConfiguration);
        final WatchManager watchManager = this.getWatchManager();
        final WatchManager targetWatchManager = targetConfiguration.getWatchManager();
        if (targetWatchManager.getIntervalSeconds() > 0) {
            watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
            final Map<Source, Watcher> watchers = targetWatchManager.getConfigurationWatchers();
            for (final Map.Entry<Source, Watcher> entry : watchers.entrySet()) {
                watchManager.watch(entry.getKey(), entry.getValue().newWatcher(this, this.listeners, entry.getValue().getLastModified()));
            }
        }
        for (final AbstractConfiguration sourceConfiguration : this.configurations.subList(1, this.configurations.size())) {
            this.staffChildConfiguration(sourceConfiguration);
            final Node sourceRoot = sourceConfiguration.getRootNode();
            this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, this.getPluginManager());
            if (CompositeConfiguration.LOGGER.isEnabled(Level.ALL)) {
                final StringBuilder sb = new StringBuilder();
                this.printNodes("", this.rootNode, sb);
                System.out.println(sb.toString());
            }
            final int monitorInterval = sourceConfiguration.getWatchManager().getIntervalSeconds();
            if (monitorInterval > 0) {
                final int currentInterval = watchManager.getIntervalSeconds();
                if (currentInterval <= 0 || monitorInterval < currentInterval) {
                    watchManager.setIntervalSeconds(monitorInterval);
                }
                final WatchManager sourceWatchManager = sourceConfiguration.getWatchManager();
                final Map<Source, Watcher> watchers2 = sourceWatchManager.getConfigurationWatchers();
                for (final Map.Entry<Source, Watcher> entry2 : watchers2.entrySet()) {
                    watchManager.watch(entry2.getKey(), entry2.getValue().newWatcher(this, this.listeners, entry2.getValue().getLastModified()));
                }
            }
        }
    }
    
    @Override
    public Configuration reconfigure() {
        CompositeConfiguration.LOGGER.debug("Reconfiguring composite configuration");
        final List<AbstractConfiguration> configs = new ArrayList<AbstractConfiguration>();
        final ConfigurationFactory factory = ConfigurationFactory.getInstance();
        for (final AbstractConfiguration config : this.configurations) {
            final ConfigurationSource source = config.getConfigurationSource();
            final URI sourceURI = source.getURI();
            Configuration currentConfig = config;
            if (sourceURI == null) {
                CompositeConfiguration.LOGGER.warn("Unable to determine URI for configuration {}, changes to it will be ignored", config.getName());
            }
            else {
                currentConfig = factory.getConfiguration(this.getLoggerContext(), config.getName(), sourceURI);
                if (currentConfig == null) {
                    CompositeConfiguration.LOGGER.warn("Unable to reload configuration {}, changes to it will be ignored", config.getName());
                }
            }
            configs.add((AbstractConfiguration)currentConfig);
        }
        return new CompositeConfiguration(configs);
    }
    
    private void staffChildConfiguration(final AbstractConfiguration childConfiguration) {
        childConfiguration.setPluginManager(this.pluginManager);
        childConfiguration.setScriptManager(this.scriptManager);
        childConfiguration.setup();
    }
    
    private void printNodes(final String indent, final Node node, final StringBuilder sb) {
        sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
        sb.append(indent).append(node.getAttributes().toString()).append("\n");
        for (final Node child : node.getChildren()) {
            this.printNodes(indent + "  ", child, sb);
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode()) + " [configurations=" + this.configurations + ", mergeStrategy=" + this.mergeStrategy + ", rootNode=" + this.rootNode + ", listeners=" + this.listeners + ", pluginPackages=" + this.pluginPackages + ", pluginManager=" + this.pluginManager + ", isShutdownHookEnabled=" + this.isShutdownHookEnabled + ", shutdownTimeoutMillis=" + this.shutdownTimeoutMillis + ", scriptManager=" + this.scriptManager + "]";
    }
    
    static {
        VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
    }
}
