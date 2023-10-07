package cc.novoline.modules.configurations;

import cc.novoline.modules.ModuleArrayMap;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.DelsyConfig;
import cc.novoline.modules.configurations.holder.CreatingModuleHolder;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import cc.novoline.modules.configurations.property.Property;
import cc.novoline.modules.configurations.property.mapper.PropertyMapperFactory;
import cc.novoline.modules.exceptions.OutdatedConfigException;
import cc.novoline.modules.exceptions.ReadConfigException;
import cc.novoline.modules.serializers.ConfigSerializer;
import cc.novoline.modules.serializers.ModuleMapSerializer;
import cc.novoline.modules.serializers.PropertySerializer;
import cc.novoline.utils.java.Checks;
import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cc.novoline.Novoline.getLogger;
import static java.nio.file.Files.*;


public final class ConfigManager {

    /* fields */
    private final ModuleManager moduleManager;
    private final int configVersion;

    /* constructors */
    public ConfigManager(@NotNull ModuleManager moduleManager, int configVersion) {
        this.moduleManager = moduleManager;
        this.configVersion = configVersion;
    }

    /* methods */
    public void load(@NotNull String name, boolean disable) throws IOException, ObjectMappingException {
        final Path path = getConfigPath(name);
        load(path, disable);
    }

    public void load(@NotNull Path path, boolean disable) throws IOException, ObjectMappingException {
        if (notExists(path)) {
            throw new FileNotFoundException("file doesn't exist");
        }

        final ConfigurationOptions options = defaultOptions();
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setDefaultOptions(options).setPath(path).build();
        final ConfigurationNode node;

        try {
            node = loader.load(options);
        } catch (IOException e) {
            throw new ReadConfigException(e);
        }

        final ClientConfig value = node.getValue(TypeToken.of(ClientConfig.class));

        if (value == null) {
            return;
        }

        if (value.getConfigVersion() != this.configVersion) {
            throw new OutdatedConfigException();
        }

        final ModuleArrayMap destModulesMap = this.moduleManager.getModuleManager();

        if (disable) {
            for (final ModuleHolder<?> moduleHolder : destModulesMap.values()) {
                moduleHolder.getModule().setEnabled(false);
            }
        }

        CreatingModuleHolder<?> destHolder;

        for (final Map.Entry<String, ModuleHolder<?>> moduleEntry : value.getModules().entrySet()) {
            final String moduleName = moduleEntry.getKey();
            final ModuleHolder<?> srcHolder = moduleEntry.getValue();

            if ((destHolder = (CreatingModuleHolder<?>) destModulesMap.get(moduleName)) != null) {
                int success = 0, error = 0;

                for (final Map.Entry<String, CreatingModuleHolder.PropertyFieldData> propertyEntry : destHolder.getFields().entrySet()) {
                    final String propertyName = propertyEntry.getKey();
                    final CreatingModuleHolder.PropertyFieldData propertyFieldData = propertyEntry.getValue();

                    try {
                        propertyFieldData.copyValue(srcHolder.getModule(), destHolder.getModule(), disable);
                        // System.out.println("copied value " + propertyName + " - " + moduleName);

                        success++;
                    } catch (Throwable t) {
                        getLogger().warn("An error occurred while updating property: value: " + propertyName + ", module: " + destHolder.getName(), t);
                        error++;
                    }
                }

				/*if(success + error > 0) {
					getLogger().info("Loaded " + (success + error) + " properties for " + moduleName + " (" + success + " successes, " + error + " errors)");
				}*/
            }
        }
    }

    public String saveToString(@NotNull String name) throws IOException, ObjectMappingException {
        final Path path = getConfigPath(name);

        StringWriter config = new StringWriter();

        final ConfigurationOptions options = defaultOptions();
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setDefaultOptions(options)
                .setPath(path)
                .setSink(() -> new BufferedWriter(config))
                .build();
        final ConfigurationNode node;

        try {
            node = loader.load(options);
        } catch (IOException e) {
            throw new ReadConfigException(e);
        }

        final ClientConfig value = ClientConfig.of(this, name);
        node.setValue(TypeToken.of(ClientConfig.class), value);

        loader.save(node);
        return config.toString();
    }

    public boolean save(@NotNull String name) throws ReadConfigException, IOException, ObjectMappingException {
        boolean logged = false;
        final Path path = getConfigPath(name);

        if (notExists(path)) {
            createFile(path);
        }

        final ConfigurationOptions options = defaultOptions();
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setDefaultOptions(options)
                .setPath(path).build();
        final ConfigurationNode node;

        try {
            node = loader.load(options);
        } catch (IOException e) {
            throw new ReadConfigException(e);
        }

        final ClientConfig value = ClientConfig.of(this, name);
        node.setValue(TypeToken.of(ClientConfig.class), value);

        loader.save(node);
        return true;
    }

    public boolean delete(@NotNull String name) throws IOException {
        final Path path = getConfigPath(name);

        if (Files.notExists(path)) {
            throw new FileNotFoundException();
            // NotificationManager.pop("Config does not exist!", 5_000, NotificationType.ERROR);
            // return false;
        }

        Files.deleteIfExists(path);
        return true;
    }

    @NotNull
    public List<@NotNull String> getConfigs() {
        Stream<Path> filesStream;

        try {
            filesStream = walk(getConfigsFolder());
        } catch (IOException e) {
            getLogger().error("An I/O error occurred while getting contents of configs folder", e);
            return Collections.emptyList();
        }

        return filesStream // @off
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(s -> s.endsWith(EXTENSION))
                .map(s -> s.substring(0, s.length() - EXTENSION.length()))
                .collect(Collectors.toCollection(ObjectArrayList::new)); // @on
    }

    @NotNull
    public Path getConfigPath(@NotNull String name) {
        Checks.notBlank(name, "name");
        return getConfigsFolder().resolve(name + EXTENSION);
    }

    //region Lombok
    @NotNull
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public int getConfigVersion() {
        return this.configVersion;
    }

    @NotNull
    public Path getConfigsFolder() {
        final Path path = this.moduleManager.getNovoline().getDataFolder();
        final Path configsFolder = path.resolve("configs");

        if (isRegularFile(configsFolder)) {
            boolean b = false;

            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                try {
                    move(configsFolder, path.resolve("configs-" + i));

                    b = true;
                    break;
                } catch (FileAlreadyExistsException ignored) {
                } catch (IOException e) {
                    getLogger().error("An I/O error occurred while creating configs folder", e);
                }
            }

            if (!b) getLogger().error("User created 2.147.483.648 configs-# files. Cannot create configs folder");
        }

        if (notExists(configsFolder)) {
            try {
                createDirectories(configsFolder);
            } catch (IOException e) {
                getLogger().error("An I/O error occurred while creating configs folder", e);
                e.printStackTrace();
            }
        }

        return configsFolder;
    }

    //endregion
    //region Hocon-loader options
    private static ConfigurationOptions DEFAULT_OPTIONS;

    private @NotNull ConfigurationOptions defaultOptions() {
        if (DEFAULT_OPTIONS == null) {
            ConfigurationOptions options = ConfigurationOptions.defaults()
                    .setObjectMapperFactory(new PropertyMapperFactory());
            TypeSerializerCollection serializers = TypeSerializers.newCollection();
            serializers.registerType(new TypeToken<Property<?>>() {
            }, new PropertySerializer());
            serializers.registerType(TypeToken.of(ModuleArrayMap.class), new ModuleMapSerializer(this.moduleManager));
            serializers.registerPredicate(input -> {
                Class<? super Object> rawType = input.getRawType();
                boolean b;

                do {
                    if (!(b = rawType.isAnnotationPresent(DelsyConfig.class))) {
                        rawType = rawType.getSuperclass();
                    }
                } while (!b && rawType != null && rawType.getSuperclass() != null);

                return b;
            }, new ConfigSerializer(this.moduleManager));
            serializers = TypeSerializers.getDefaultSerializers().and(serializers);
            options = options.setSerializers(serializers);

            return DEFAULT_OPTIONS = options;
        } else {
            return DEFAULT_OPTIONS;
        }
    }

    private static final String EXTENSION = ".novo";

    public static String getExtension() {
        return EXTENSION;
    }
    //endregion

}
