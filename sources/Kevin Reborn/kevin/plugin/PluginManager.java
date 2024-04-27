package kevin.plugin;

import kevin.hud.element.elements.ConnectNotificationType;
import kevin.hud.element.elements.Notification;
import kevin.main.KevinClient;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class PluginManager {
    static final SensitiveUtils sensitiveUtils = new SensitiveUtils();
    static final Pattern FILE_REG = Pattern.compile("\\.jar$");
    static final HashMap<String, Plugin> loopUpMap = new HashMap<>();
    public static void initialize() {
        File plugins = KevinClient.fileManager.plugins;
        int i = 0;
        for (File file : Objects.requireNonNull(plugins.listFiles())) {
            if (file == null) continue;
            if (file.isDirectory()) continue;
            if (!FILE_REG.matcher(file.getName()).find()) continue;
            try {
                load(file);
                ++i;
            } catch (IOException e) {
                Minecraft.logger.error("IO EXCEPTION", e);
            }
        }
        if (i == 0) {
            Minecraft.logger.info("[PluginManager] No plugin was loaded");
        } else {
            Minecraft.logger.info(String.format("[PluginManager] Loaded %s plugin(s)", i));
        }
        for (Plugin value : loopUpMap.values()) {
            try {
                Minecraft.logger.info("[PluginManager] Enabling " + value.getDescription().getFullName());
                value.onLoad();
            } catch (Throwable e) {
                Minecraft.logger.error(String.format("Exception occurred while enabling %s (is it up to date?)", value.getDescription().getFullName()), e);
            }
        }
    }

    public static void load(File file) throws IOException {
        if (!sensitiveUtils.getCallerClass().getName().startsWith("kevin.")) throw new RuntimeException("Method invoke access denied");
        try (JarFile jarFile = new JarFile(file)) {
            if (jarFile.getJarEntry("Updater.class") != null) {
                Minecraft.logger.warn(String.format("[PluginManager] Skyrage virus found in %s", file.getAbsolutePath()));
                return;
            }
            JarEntry entry = jarFile.getJarEntry("plugin.properties");
            InputStream inputStream = jarFile.getInputStream(entry);
            Properties properties = new Properties();
            properties.load(inputStream);
            try {
                PluginDescription description = new PluginDescription(properties);
                if (loopUpMap.containsKey(description.name.toLowerCase())) {
                    Minecraft.logger.warn(String.format("[PluginManager] Plugin with same name was found: %s", description.name));
                    return;
                }
                Minecraft.logger.info(String.format("[PluginManager] Loading plugin %s", description.name));
                PluginClassLoader loader = new PluginClassLoader(new URL[]{file.toURI().toURL()}, jarFile, description);
                Plugin plugin = loader.getPlugin();
                loopUpMap.put(description.name.toLowerCase(), plugin);
            } catch (RuntimeException e) {
                Minecraft.logger.error(String.format("[PluginManager] Could not load plugin at %s", file.getAbsolutePath()), e);
                KevinClient.hud.addNotification(new Notification(String.format("Could not load plugin at %s", file.getAbsolutePath()), "PluginManager", ConnectNotificationType.Error));
            } catch (InvalidDescriptionException e) {
                Minecraft.logger.error(String.format("[PluginManager] Exception occurred while parsing description at %s", file.getAbsolutePath()), e);
            }
        }
    }
}
