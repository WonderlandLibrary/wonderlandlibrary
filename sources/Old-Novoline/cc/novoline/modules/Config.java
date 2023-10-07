package cc.novoline.modules;

import cc.novoline.modules.configurations.property.Property;
import cc.novoline.modules.configurations.property.mapper.PropertyMapperFactory;
import cc.novoline.modules.exceptions.LoadConfigException;
import cc.novoline.modules.serializers.PropertySerializer;
import com.google.common.reflect.TypeToken;
import com.typesafe.config.ConfigException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.concurrent.ThreadLocalRandom;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author xDelsy
 */
public final class Config {

    private static final Logger LOGGER = LogManager.getLogger();

    /* fields */
    private final Path file;
    private final HoconConfigurationLoader loader;
    private ConfigurationNode rootNode;

    /* constructors */
    private Config(@NotNull Path path) {
        this.file = path;
        this.loader = HoconConfigurationLoader.builder().setDefaultOptions(defaultOptions()).setPath(file).build();
    }

    private @NotNull ConfigurationOptions defaultOptions() {
        ConfigurationOptions options = ConfigurationOptions.defaults()
                .setObjectMapperFactory(new PropertyMapperFactory());
        TypeSerializerCollection serializers = TypeSerializers.newCollection();
        serializers.registerType(new TypeToken<Property<?>>() {
        }, new PropertySerializer());
        serializers = TypeSerializers.getDefaultSerializers().and(serializers);
        options = options.setSerializers(serializers);

        return options;
    }

    /* methods */
    public static @NotNull Config fromPath(@NotNull Path path) {
        return new Config(path);
    }

    public @NotNull ConfigurationNode getNode(String version) {
        ConfigurationNode rootNode = getRootNode();

        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("version may not be blank!");
        }

        return rootNode.getNode(version);
    }

    public void load() throws LoadConfigException {
        Path file = this.file;

        try {
            if(Files.notExists(file)) {
                try {
                    Path parent = file.getParent();

                    if(parent != null) {
                        Files.createDirectories(parent);
                    }

                    Files.createFile(file);
                } catch(IOException e) {
                    throw new IOException("Cannot create new file: " + file.getFileName(), e);
                }
            }

            if(Files.isDirectory(file)) {
                try {
                    Files.move(file,
                            file.resolveSibling(file.getFileName() + "-" + Math.abs(ThreadLocalRandom.current().nextInt())),
                            REPLACE_EXISTING);
                    Files.createFile(file);

                    load();
                    return;
                } catch(IOException e) {
                    throw new LoadConfigException("An error occurred while renaming a folder with the same name as config's", e);
                }
            }

            this.rootNode = loader.load();
        } catch(Throwable t) {
            if(t.getCause() instanceof ConfigException) {
                try {
                    Files.move(file,
                            file.resolveSibling(file.getFileName() + "-" + Math.abs(ThreadLocalRandom.current().nextInt())),
                            REPLACE_EXISTING);
                    Files.createFile(file);

                    load();
                    return;
                } catch(IOException e1) {
                    throw new LoadConfigException("An error occurred while renaming the config file", e1);
                }
            }

            throw new LoadConfigException("An error occurred while setting up the config!", t);
        }
    }

    public void save() throws IOException {
        try {
            loader.save(getRootNode());
        } catch (IOException e) {
            throw new IOException("An error occurred while saving the config!", e);
        }
    }

    //region Lombok
    @NotNull
    public HoconConfigurationLoader getLoader() {
        return loader;
    }

    @NotNull
    public ConfigurationNode getRootNode() {
        if (rootNode == null) throw new IllegalStateException("config is not loaded yet!");
        return rootNode;
    }

    public void setRootNode(@NotNull ConfigurationNode rootNode) {
        this.rootNode = rootNode;
    }
    //endregion

}
