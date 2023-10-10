// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.DumperOptions;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.io.File;
import org.yaml.snakeyaml.Yaml;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;

public abstract class Config implements ConfigurationProvider
{
    private static final ThreadLocal<Yaml> YAML;
    private final CommentStore commentStore;
    private final File configFile;
    private Map<String, Object> config;
    
    public Config(final File configFile) {
        this.commentStore = new CommentStore('.', 2);
        this.configFile = configFile;
    }
    
    public abstract URL getDefaultConfigURL();
    
    public synchronized Map<String, Object> loadConfig(final File location) {
        final List<String> unsupported = this.getUnsupportedOptions();
        final URL jarConfigFile = this.getDefaultConfigURL();
        try {
            this.commentStore.storeComments(jarConfigFile.openStream());
            for (final String option : unsupported) {
                final List<String> comments = this.commentStore.header(option);
                if (comments != null) {
                    comments.clear();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> config = null;
        if (location.exists()) {
            try {
                final FileInputStream input = new FileInputStream(location);
                try {
                    config = Config.YAML.get().load(input);
                    input.close();
                }
                catch (Throwable t) {
                    try {
                        input.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                    throw t;
                }
            }
            catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }
            catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (config == null) {
            config = new HashMap<String, Object>();
        }
        Map<String, Object> defaults = config;
        try {
            final InputStream stream = jarConfigFile.openStream();
            try {
                defaults = Config.YAML.get().load(stream);
                for (final String option2 : unsupported) {
                    defaults.remove(option2);
                }
                for (final Map.Entry<String, Object> entry : config.entrySet()) {
                    if (defaults.containsKey(entry.getKey()) && !unsupported.contains(entry.getKey())) {
                        defaults.put(entry.getKey(), entry.getValue());
                    }
                }
                if (stream != null) {
                    stream.close();
                }
            }
            catch (Throwable t2) {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (Throwable exception2) {
                        t2.addSuppressed(exception2);
                    }
                }
                throw t2;
            }
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        this.handleConfig(defaults);
        this.saveConfig(location, defaults);
        return defaults;
    }
    
    protected abstract void handleConfig(final Map<String, Object> p0);
    
    public synchronized void saveConfig(final File location, final Map<String, Object> config) {
        try {
            this.commentStore.writeComments(Config.YAML.get().dump(config), location);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public abstract List<String> getUnsupportedOptions();
    
    @Override
    public void set(final String path, final Object value) {
        this.config.put(path, value);
    }
    
    @Override
    public void saveConfig() {
        this.configFile.getParentFile().mkdirs();
        this.saveConfig(this.configFile, this.config);
    }
    
    @Override
    public void reloadConfig() {
        this.configFile.getParentFile().mkdirs();
        this.config = new ConcurrentSkipListMap<String, Object>(this.loadConfig(this.configFile));
    }
    
    @Override
    public Map<String, Object> getValues() {
        return this.config;
    }
    
    public <T> T get(final String key, final Class<T> clazz, final T def) {
        final Object o = this.config.get(key);
        if (o != null) {
            return (T)o;
        }
        return def;
    }
    
    public boolean getBoolean(final String key, final boolean def) {
        final Object o = this.config.get(key);
        if (o != null) {
            return (boolean)o;
        }
        return def;
    }
    
    public String getString(final String key, final String def) {
        final Object o = this.config.get(key);
        if (o != null) {
            return (String)o;
        }
        return def;
    }
    
    public int getInt(final String key, final int def) {
        final Object o = this.config.get(key);
        if (o == null) {
            return def;
        }
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        return def;
    }
    
    public double getDouble(final String key, final double def) {
        final Object o = this.config.get(key);
        if (o == null) {
            return def;
        }
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        return def;
    }
    
    public List<Integer> getIntegerList(final String key) {
        final Object o = this.config.get(key);
        return (o != null) ? ((List)o) : new ArrayList<Integer>();
    }
    
    public List<String> getStringList(final String key) {
        final Object o = this.config.get(key);
        return (o != null) ? ((List)o) : new ArrayList<String>();
    }
    
    public JsonElement getSerializedComponent(final String key) {
        final Object o = this.config.get(key);
        if (o != null && !((String)o).isEmpty()) {
            return GsonComponentSerializer.gson().serializeToTree(LegacyComponentSerializer.legacySection().deserialize((String)o));
        }
        return null;
    }
    
    static {
        final DumperOptions options;
        final Object o;
        YAML = ThreadLocal.withInitial(() -> {
            options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(false);
            options.setIndent(2);
            new Yaml(new YamlConstructor(), new Representer(), options);
            return o;
        });
    }
}
