/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class ConfigurationFactory {
    public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
    public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected static final String TEST_PREFIX = "log4j2-test";
    protected static final String DEFAULT_PREFIX = "log4j2";
    private static final String CLASS_LOADER_SCHEME = "classloader";
    private static final int CLASS_LOADER_SCHEME_LENGTH = "classloader".length() + 1;
    private static final String CLASS_PATH_SCHEME = "classpath";
    private static final int CLASS_PATH_SCHEME_LENGTH = "classpath".length() + 1;
    private static volatile List<ConfigurationFactory> factories = null;
    private static ConfigurationFactory configFactory = new Factory();
    protected final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ConfigurationFactory getInstance() {
        if (factories != null) return configFactory;
        String string = TEST_PREFIX;
        synchronized (TEST_PREFIX) {
            if (factories != null) return configFactory;
            ArrayList<ConfigurationFactory> list = new ArrayList<ConfigurationFactory>();
            String factoryClass = PropertiesUtil.getProperties().getStringProperty(CONFIGURATION_FACTORY_PROPERTY);
            if (factoryClass != null) {
                ConfigurationFactory.addFactory(list, factoryClass);
            }
            PluginManager manager = new PluginManager("ConfigurationFactory");
            manager.collectPlugins();
            Map<String, PluginType<?>> plugins = manager.getPlugins();
            TreeSet<WeightedFactory> ordered = new TreeSet<WeightedFactory>();
            for (PluginType<?> type : plugins.values()) {
                try {
                    Class<ConfigurationFactory> clazz = type.getPluginClass();
                    Order order = clazz.getAnnotation(Order.class);
                    if (order == null) continue;
                    int weight = order.value();
                    ordered.add(new WeightedFactory(weight, clazz));
                }
                catch (Exception ex) {
                    LOGGER.warn("Unable to add class " + type.getPluginClass());
                }
            }
            for (WeightedFactory wf : ordered) {
                ConfigurationFactory.addFactory(list, wf.factoryClass);
            }
            factories = Collections.unmodifiableList(list);
            // ** MonitorExit[var0] (shouldn't be in output)
            return configFactory;
        }
    }

    private static void addFactory(List<ConfigurationFactory> list, String factoryClass) {
        try {
            ConfigurationFactory.addFactory(list, Class.forName(factoryClass));
        }
        catch (ClassNotFoundException ex) {
            LOGGER.error("Unable to load class " + factoryClass, (Throwable)ex);
        }
        catch (Exception ex) {
            LOGGER.error("Unable to load class " + factoryClass, (Throwable)ex);
        }
    }

    private static void addFactory(List<ConfigurationFactory> list, Class<ConfigurationFactory> factoryClass) {
        try {
            list.add(factoryClass.newInstance());
        }
        catch (Exception ex) {
            LOGGER.error("Unable to create instance of " + factoryClass.getName(), (Throwable)ex);
        }
    }

    public static void setConfigurationFactory(ConfigurationFactory factory) {
        configFactory = factory;
    }

    public static void resetConfigurationFactory() {
        configFactory = new Factory();
    }

    public static void removeConfigurationFactory(ConfigurationFactory factory) {
        if (configFactory == factory) {
            configFactory = new Factory();
        }
    }

    protected abstract String[] getSupportedTypes();

    protected boolean isActive() {
        return true;
    }

    public abstract Configuration getConfiguration(ConfigurationSource var1);

    public Configuration getConfiguration(String name, URI configLocation) {
        ConfigurationSource source;
        if (!this.isActive()) {
            return null;
        }
        if (configLocation != null && (source = this.getInputFromURI(configLocation)) != null) {
            return this.getConfiguration(source);
        }
        return null;
    }

    protected ConfigurationSource getInputFromURI(URI configLocation) {
        ClassLoader loader;
        String path;
        ConfigurationSource source;
        boolean isClassPathScheme;
        String scheme;
        File configFile = FileUtils.fileFromURI(configLocation);
        if (configFile != null && configFile.exists() && configFile.canRead()) {
            try {
                return new ConfigurationSource((InputStream)new FileInputStream(configFile), configFile);
            }
            catch (FileNotFoundException ex) {
                LOGGER.error("Cannot locate file " + configLocation.getPath(), (Throwable)ex);
            }
        }
        boolean isClassLoaderScheme = (scheme = configLocation.getScheme()) != null && scheme.equals("classloader");
        boolean bl = isClassPathScheme = scheme != null && !isClassLoaderScheme && scheme.equals("classpath");
        if ((scheme == null || isClassLoaderScheme || isClassPathScheme) && (source = this.getInputFromResource(path = isClassLoaderScheme ? configLocation.toString().substring(CLASS_LOADER_SCHEME_LENGTH) : (isClassPathScheme ? configLocation.toString().substring(CLASS_PATH_SCHEME_LENGTH) : configLocation.getPath()), loader = this.getClass().getClassLoader())) != null) {
            return source;
        }
        try {
            return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.getPath());
        }
        catch (MalformedURLException ex) {
            LOGGER.error("Invalid URL " + configLocation.toString(), (Throwable)ex);
        }
        catch (IOException ex) {
            LOGGER.error("Unable to access " + configLocation.toString(), (Throwable)ex);
        }
        catch (Exception ex) {
            LOGGER.error("Unable to access " + configLocation.toString(), (Throwable)ex);
        }
        return null;
    }

    protected ConfigurationSource getInputFromString(String config, ClassLoader loader) {
        try {
            URL url = new URL(config);
            return new ConfigurationSource(url.openStream(), FileUtils.fileFromURI(url.toURI()));
        }
        catch (Exception ex) {
            ConfigurationSource source = this.getInputFromResource(config, loader);
            if (source == null) {
                try {
                    File file = new File(config);
                    return new ConfigurationSource((InputStream)new FileInputStream(file), file);
                }
                catch (FileNotFoundException fnfe) {
                    // empty catch block
                }
            }
            return source;
        }
    }

    protected ConfigurationSource getInputFromResource(String resource, ClassLoader loader) {
        URL url = Loader.getResource(resource, loader);
        if (url == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = url.openStream();
        }
        catch (IOException ioe) {
            return null;
        }
        if (is == null) {
            return null;
        }
        if (FileUtils.isFile(url)) {
            try {
                return new ConfigurationSource(is, FileUtils.fileFromURI(url.toURI()));
            }
            catch (URISyntaxException ex) {
                // empty catch block
            }
        }
        return new ConfigurationSource(is, resource);
    }

    public static class ConfigurationSource {
        private File file;
        private String location;
        private InputStream stream;

        public ConfigurationSource() {
        }

        public ConfigurationSource(InputStream stream) {
            this.stream = stream;
            this.file = null;
            this.location = null;
        }

        public ConfigurationSource(InputStream stream, File file) {
            this.stream = stream;
            this.file = file;
            this.location = file.getAbsolutePath();
        }

        public ConfigurationSource(InputStream stream, String location) {
            this.stream = stream;
            this.location = location;
            this.file = null;
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public InputStream getInputStream() {
            return this.stream;
        }

        public void setInputStream(InputStream stream) {
            this.stream = stream;
        }
    }

    private static class Factory
    extends ConfigurationFactory {
        private Factory() {
        }

        @Override
        public Configuration getConfiguration(String name, URI configLocation) {
            Object config;
            if (configLocation == null) {
                config = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY));
                if (config != null) {
                    ConfigurationSource source = null;
                    try {
                        source = this.getInputFromURI(new URI((String)config));
                    }
                    catch (Exception ex) {
                        // empty catch block
                    }
                    if (source == null) {
                        ClassLoader loader = this.getClass().getClassLoader();
                        source = this.getInputFromString((String)config, loader);
                    }
                    if (source != null) {
                        for (ConfigurationFactory factory : factories) {
                            String[] types = factory.getSupportedTypes();
                            if (types == null) continue;
                            for (String type : types) {
                                Configuration c;
                                if (!type.equals("*") && !((String)config).endsWith(type) || (c = factory.getConfiguration(source)) == null) continue;
                                return c;
                            }
                        }
                    }
                }
            } else {
                for (ConfigurationFactory factory : factories) {
                    String[] types = factory.getSupportedTypes();
                    if (types == null) continue;
                    for (String type : types) {
                        Configuration config2;
                        if (!type.equals("*") && !configLocation.toString().endsWith(type) || (config2 = factory.getConfiguration(name, configLocation)) == null) continue;
                        return config2;
                    }
                }
            }
            if ((config = this.getConfiguration(true, name)) == null && (config = this.getConfiguration(true, null)) == null && (config = this.getConfiguration(false, name)) == null) {
                config = this.getConfiguration(false, null);
            }
            return config != null ? config : new DefaultConfiguration();
        }

        private Configuration getConfiguration(boolean isTest, String name) {
            boolean named = name != null && name.length() > 0;
            ClassLoader loader = this.getClass().getClassLoader();
            for (ConfigurationFactory factory : factories) {
                String prefix = isTest ? ConfigurationFactory.TEST_PREFIX : ConfigurationFactory.DEFAULT_PREFIX;
                String[] types = factory.getSupportedTypes();
                if (types == null) continue;
                for (String suffix : types) {
                    String configName;
                    ConfigurationSource source;
                    if (suffix.equals("*") || (source = this.getInputFromResource(configName = named ? prefix + name + suffix : prefix + suffix, loader)) == null) continue;
                    return factory.getConfiguration(source);
                }
            }
            return null;
        }

        @Override
        public String[] getSupportedTypes() {
            return null;
        }

        @Override
        public Configuration getConfiguration(ConfigurationSource source) {
            if (source != null) {
                String config = source.getLocation();
                for (ConfigurationFactory factory : factories) {
                    String[] types = factory.getSupportedTypes();
                    if (types == null) continue;
                    for (String type : types) {
                        if (!type.equals("*") && (config == null || !config.endsWith(type))) continue;
                        Configuration c = factory.getConfiguration(source);
                        if (c != null) {
                            return c;
                        }
                        LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
                        return null;
                    }
                }
            }
            LOGGER.error("Cannot process configuration, input source is null");
            return null;
        }
    }

    private static class WeightedFactory
    implements Comparable<WeightedFactory> {
        private final int weight;
        private final Class<ConfigurationFactory> factoryClass;

        public WeightedFactory(int weight, Class<ConfigurationFactory> clazz) {
            this.weight = weight;
            this.factoryClass = clazz;
        }

        @Override
        public int compareTo(WeightedFactory wf) {
            int w = wf.weight;
            if (this.weight == w) {
                return 0;
            }
            if (this.weight > w) {
                return -1;
            }
            return 1;
        }
    }
}

