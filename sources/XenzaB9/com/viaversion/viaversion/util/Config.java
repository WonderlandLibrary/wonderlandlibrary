// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.DumperOptions;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.Via;
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
    
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }
    
    public Map<String, Object> loadConfig(final File location) {
        return this.loadConfig(location, this.getDefaultConfigURL());
    }
    
    public synchronized Map<String, Object> loadConfig(final File location, final URL jarConfigFile) {
        final List<String> unsupported = this.getUnsupportedOptions();
        try {
            this.commentStore.storeComments(jarConfigFile.openStream());
            for (final String option : unsupported) {
                final List<String> comments = this.commentStore.header(option);
                if (comments != null) {
                    comments.clear();
                }
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> config = null;
        if (location.exists()) {
            try (final FileInputStream input = new FileInputStream(location)) {
                config = Config.YAML.get().load(input);
            }
            catch (final FileNotFoundException e2) {
                e2.printStackTrace();
            }
            catch (final IOException e3) {
                e3.printStackTrace();
            }
        }
        if (config == null) {
            config = new HashMap<String, Object>();
        }
        Map<String, Object> defaults = config;
        try (final InputStream stream = jarConfigFile.openStream()) {
            defaults = Config.YAML.get().load(stream);
            for (final String option2 : unsupported) {
                defaults.remove(option2);
            }
            for (final Map.Entry<String, Object> entry : config.entrySet()) {
                if (defaults.containsKey(entry.getKey()) && !unsupported.contains(entry.getKey())) {
                    defaults.put(entry.getKey(), entry.getValue());
                }
            }
        }
        catch (final IOException e4) {
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
        catch (final IOException e) {
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
    
    public void saveConfig(final File file) {
        this.saveConfig(file, this.config);
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
    
    public <T> List<T> getListSafe(final String key, final Class<T> type, final String invalidValueMessage) {
        final Object o = this.config.get(key);
        if (o instanceof List) {
            final List<?> list = (List<?>)o;
            final List<T> filteredValues = new ArrayList<T>();
            for (final Object o2 : list) {
                if (type.isInstance(o2)) {
                    filteredValues.add(type.cast(o2));
                }
                else {
                    if (invalidValueMessage == null) {
                        continue;
                    }
                    Via.getPlatform().getLogger().warning(String.format(invalidValueMessage, o2));
                }
            }
            return filteredValues;
        }
        return new ArrayList<T>();
    }
    
    public JsonElement getSerializedComponent(final String key) {
        final Object o = this.config.get(key);
        if (o != null && !((String)o).isEmpty()) {
            return GsonComponentSerializer.gson().serializeToTree(LegacyComponentSerializer.legacySection().deserialize((String)o));
        }
        return null;
    }
    
    static {
        YAML = ThreadLocal.withInitial(() -> {
            final DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(false);
            options.setIndent(2);
            new Yaml(new YamlConstructor(), new Representer(), options);
            return;
        });
    }
}
