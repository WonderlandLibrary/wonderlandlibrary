/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.composite;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.composite.DefaultMergeStrategy;
import org.apache.logging.log4j.core.config.composite.MergeStrategy;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.util.PropertiesUtil;

public class CompositeConfiguration
extends AbstractConfiguration
implements Reconfigurable {
    public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
    private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
    private final List<? extends AbstractConfiguration> configurations;
    private MergeStrategy mergeStrategy;

    public CompositeConfiguration(List<? extends AbstractConfiguration> configurations) {
        super(configurations.get(0).getLoggerContext(), ConfigurationSource.COMPOSITE_SOURCE);
        this.rootNode = configurations.get(0).getRootNode();
        this.configurations = configurations;
        String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty(MERGE_STRATEGY_PROPERTY, DefaultMergeStrategy.class.getName());
        try {
            this.mergeStrategy = (MergeStrategy)Loader.newInstanceOf(mergeStrategyClassName);
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            this.mergeStrategy = new DefaultMergeStrategy();
        }
        for (AbstractConfiguration abstractConfiguration : configurations) {
            this.mergeStrategy.mergeRootProperties(this.rootNode, abstractConfiguration);
        }
        StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
        for (Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = this.getStrSubstitutor().replace(entry.getValue());
            if ("status".equalsIgnoreCase(key)) {
                statusConfig.withStatus(value.toUpperCase());
                continue;
            }
            if ("dest".equalsIgnoreCase(key)) {
                statusConfig.withDestination(value);
                continue;
            }
            if ("shutdownHook".equalsIgnoreCase(key)) {
                this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
                continue;
            }
            if ("shutdownTimeout".equalsIgnoreCase(key)) {
                this.shutdownTimeoutMillis = Long.parseLong(value);
                continue;
            }
            if ("verbose".equalsIgnoreCase(key)) {
                statusConfig.withVerbosity(value);
                continue;
            }
            if ("packages".equalsIgnoreCase(key)) {
                this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR)));
                continue;
            }
            if (!"name".equalsIgnoreCase(key)) continue;
            this.setName(value);
        }
        statusConfig.initialize();
    }

    @Override
    public void setup() {
        AbstractConfiguration targetConfiguration = this.configurations.get(0);
        this.staffChildConfiguration(targetConfiguration);
        WatchManager watchManager = this.getWatchManager();
        WatchManager targetWatchManager = targetConfiguration.getWatchManager();
        if (targetWatchManager.getIntervalSeconds() > 0) {
            watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
            Map<Source, Watcher> watchers = targetWatchManager.getConfigurationWatchers();
            for (Map.Entry<Source, Watcher> entry : watchers.entrySet()) {
                watchManager.watch(entry.getKey(), entry.getValue().newWatcher(this, this.listeners, entry.getValue().getLastModified()));
            }
        }
        for (AbstractConfiguration abstractConfiguration : this.configurations.subList(1, this.configurations.size())) {
            int monitorInterval;
            this.staffChildConfiguration(abstractConfiguration);
            Node sourceRoot = abstractConfiguration.getRootNode();
            this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, this.getPluginManager());
            if (LOGGER.isEnabled(Level.ALL)) {
                StringBuilder sb = new StringBuilder();
                this.printNodes("", this.rootNode, sb);
                System.out.println(sb.toString());
            }
            if ((monitorInterval = abstractConfiguration.getWatchManager().getIntervalSeconds()) <= 0) continue;
            int currentInterval = watchManager.getIntervalSeconds();
            if (currentInterval <= 0 || monitorInterval < currentInterval) {
                watchManager.setIntervalSeconds(monitorInterval);
            }
            WatchManager sourceWatchManager = abstractConfiguration.getWatchManager();
            Map<Source, Watcher> watchers = sourceWatchManager.getConfigurationWatchers();
            for (Map.Entry<Source, Watcher> entry : watchers.entrySet()) {
                watchManager.watch(entry.getKey(), entry.getValue().newWatcher(this, this.listeners, entry.getValue().getLastModified()));
            }
        }
    }

    @Override
    public Configuration reconfigure() {
        LOGGER.debug("Reconfiguring composite configuration");
        ArrayList<AbstractConfiguration> configs = new ArrayList<AbstractConfiguration>();
        ConfigurationFactory factory = ConfigurationFactory.getInstance();
        for (AbstractConfiguration abstractConfiguration : this.configurations) {
            ConfigurationSource source = abstractConfiguration.getConfigurationSource();
            URI sourceURI = source.getURI();
            Configuration currentConfig = abstractConfiguration;
            if (sourceURI == null) {
                LOGGER.warn("Unable to determine URI for configuration {}, changes to it will be ignored", abstractConfiguration.getName());
            } else {
                currentConfig = factory.getConfiguration(this.getLoggerContext(), abstractConfiguration.getName(), sourceURI);
                if (currentConfig == null) {
                    LOGGER.warn("Unable to reload configuration {}, changes to it will be ignored", abstractConfiguration.getName());
                }
            }
            configs.add((AbstractConfiguration)currentConfig);
        }
        return new CompositeConfiguration(configs);
    }

    private void staffChildConfiguration(AbstractConfiguration childConfiguration) {
        childConfiguration.setPluginManager(this.pluginManager);
        childConfiguration.setScriptManager(this.scriptManager);
        childConfiguration.setup();
    }

    private void printNodes(String indent, Node node, StringBuilder sb) {
        sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
        sb.append(indent).append(node.getAttributes().toString()).append("\n");
        for (Node child : node.getChildren()) {
            this.printNodes(indent + "  ", child, sb);
        }
    }

    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode()) + " [configurations=" + this.configurations + ", mergeStrategy=" + this.mergeStrategy + ", rootNode=" + this.rootNode + ", listeners=" + this.listeners + ", pluginPackages=" + this.pluginPackages + ", pluginManager=" + this.pluginManager + ", isShutdownHookEnabled=" + this.isShutdownHookEnabled + ", shutdownTimeoutMillis=" + this.shutdownTimeoutMillis + ", scriptManager=" + this.scriptManager + "]";
    }
}

