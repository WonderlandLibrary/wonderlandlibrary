package kevin.plugin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class PluginClassLoader extends URLClassLoader {
    protected Plugin plugin;
    protected Logger logger;
    PluginClassLoader(URL[] urls, JarFile jar, PluginDescription desc) {
        super(urls, PluginClassLoader.class.getClassLoader());

        logger = LogManager.getLogger(desc.name);

        try {
            Class<?> main = Class.forName(desc.main, true, this);

            Class<? extends Plugin> pluginClz = main.asSubclass(Plugin.class);

            plugin = pluginClz.getDeclaredConstructor().newInstance();
            plugin.initialize(logger, desc);
        } catch (LinkageError | ClassNotFoundException e) {
            throw new RuntimeException(String.format("Plugin %s main class not found", desc.name), e);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Plugin %s don't have public constructor", desc.name), e);
        } catch (InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(String.format("Error occurred (%s)", desc.name), e);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
