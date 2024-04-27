package kevin.plugin;

import java.util.Properties;

public class PluginDescription {
    public final String name;
    public final String main;
    public final String description;
    public final String version;
    public final String author;
    public PluginDescription(Properties properties) throws InvalidDescriptionException {
        name = properties.getProperty("name");
        main = properties.getProperty("main");
        description = properties.getProperty("description", "");
        version = properties.getProperty("version");
        author = properties.getProperty("author", "");
        if (name == null || version == null) {
            throw new InvalidDescriptionException("name or version could not be null");
        }
        if (name.contains(" ") || version.contains(" ")) {
            throw new InvalidDescriptionException("name or version could not contain space");
        }
        if (main == null) {
            throw new InvalidDescriptionException("main could not be null");
        }
        if (main.startsWith("kevin.") || main.startsWith("net.minecraft.")) {
            throw new InvalidDescriptionException("restricted package: " + main);
        }
        if (main.contains(" ")) {
            throw new InvalidDescriptionException("main package could not contain space");
        }
    }

    public String getFullName() {
        return name + "-" + version;
    }
}
