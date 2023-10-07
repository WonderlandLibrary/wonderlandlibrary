package ninja.leaping.configurate;

import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.DefaultObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import ninja.leaping.configurate.util.MapFactories;
import ninja.leaping.configurate.util.MapFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This object is a holder for general configuration options.
 *
 * <p>This is meant to hold options that are used in configuring how the configuration data
 * structures are handled, rather than the serialization configuration that is located in
 * {@link ConfigurationLoader}s.</p>
 *
 * <p>This class is immutable.</p>
 */
public class ConfigurationOptions {

    private final MapFactory mapFactory;
    private final String header;
    private final TypeSerializerCollection serializers;
    private final ImmutableSet<Class<?>> acceptedTypes;
    private final ObjectMapperFactory objectMapperFactory;
    private final boolean shouldCopyDefaults;

    private ConfigurationOptions(@NotNull MapFactory mapFactory,
                                 @Nullable String header,
                                 @NotNull TypeSerializerCollection serializers,
                                 @Nullable Set<Class<?>> acceptedTypes,
                                 @NotNull ObjectMapperFactory objectMapperFactory,
                                 boolean shouldCopyDefaults) {
        this.mapFactory = mapFactory;
        this.header = header;
        this.serializers = serializers;
        this.acceptedTypes = acceptedTypes == null ? null : ImmutableSet.copyOf(acceptedTypes);
        this.objectMapperFactory = objectMapperFactory;
        this.shouldCopyDefaults = shouldCopyDefaults;
    }

    /**
     * Create a new options object with defaults set
     *
     * @return A new default options object
     */
    @NotNull
    public static ConfigurationOptions defaults() {
        return new ConfigurationOptions(MapFactories.insertionOrdered(), null, TypeSerializers.getDefaultSerializers(), null, DefaultObjectMapperFactory.getInstance(), false);
    }

    /**
     * Gets the {@link MapFactory} specified in these options.
     *
     * @return The map factory
     */
    @NotNull
    public MapFactory getMapFactory() {
        return mapFactory;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified {@link MapFactory}
     * set, and all other settings copied from this instance.
     *
     * @param mapFactory The new factory to use to create a map
     * @return The new options object
     */
    @NotNull
    public ConfigurationOptions setMapFactory(@NotNull MapFactory mapFactory) {
        requireNonNull(mapFactory, "mapFactory");
        if (this.mapFactory == mapFactory) {
            return this;
        }
        return new ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    /**
     * Gets the header specified in these options.
     *
     * @return The current header. Lines are split by \n,
     */
    @Nullable
    public String getHeader() {
        return this.header;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified header
     * set, and all other settings copied from this instance.
     *
     * @param header The new header to use
     * @return The new options object
     */
    @NotNull
    public ConfigurationOptions setHeader(@Nullable String header) {
        if (Objects.equals(this.header, header)) {
            return this;
        }

        return new ninja.leaping.configurate.ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    /**
     * Gets the {@link TypeSerializerCollection} specified in these options.
     *
     * @return The type serializers
     */
    @NotNull
    public TypeSerializerCollection getSerializers() {
        return this.serializers;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified {@link TypeSerializerCollection}
     * set, and all other settings copied from this instance.
     *
     * @param serializers The serializers to use
     * @return The new options object
     */
    @NotNull
    public ConfigurationOptions setSerializers(@NotNull TypeSerializerCollection serializers) {
        requireNonNull(serializers, "serializers");
        if (this.serializers == serializers) {
            return this;
        }
        return new ninja.leaping.configurate.ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    /**
     * Gets the {@link ObjectMapperFactory} specified in these options.
     *
     * @return The factory used to construct ObjectMapper instances
     */
    @NotNull
    public ObjectMapperFactory getObjectMapperFactory() {
        return this.objectMapperFactory;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified {@link ObjectMapperFactory}
     * set, and all other settings copied from this instance.
     *
     * @param objectMapperFactory The factory to use to produce object mapper instances. Must not be null
     * @return updated options object
     */
    @NotNull
    public ConfigurationOptions setObjectMapperFactory(@NotNull ObjectMapperFactory objectMapperFactory) {
        requireNonNull(objectMapperFactory, "factory");

        if (this.objectMapperFactory == objectMapperFactory) {
            return this;
        }

        return new ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    /**
     * Gets whether objects of the provided type are accepted as values for nodes with this as
     * their options object.
     *
     * @param type The type to check
     * @return Whether the type is accepted
     */
    public boolean acceptsType(@NotNull Class<?> type) {
        requireNonNull(type, "type");

        if (this.acceptedTypes == null) {
            return true;
        }
        if (this.acceptedTypes.contains(type)) {
            return true;
        }

        for (Class<?> clazz : this.acceptedTypes) {
            if (clazz.isAssignableFrom(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified accepted types
     * set, and all other settings copied from this instance.
     *
     * <p>'Accepted types' are types which are accepted as native values for the configuration.</p>
     *
     * <p>Null indicates that all types are accepted.</p>
     *
     * @param acceptedTypes The types that will be accepted to a call to {@link ConfigurationNode#setValue(Object)}
     * @return updated options object
     */
    @NotNull
    public ConfigurationOptions setAcceptedTypes(@Nullable Set<Class<?>> acceptedTypes) {
        if (Objects.equals(this.acceptedTypes, acceptedTypes)) {
            return this;
        }

        return new ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    /**
     * Gets whether or not default parameters provided to {@link ConfigurationNode} getter methods
     * should be set to the node when used.
     *
     * @return Whether defaults should be copied into value
     */
    public boolean shouldCopyDefaults() {
        return shouldCopyDefaults;
    }

    /**
     * Creates a new {@link ninja.leaping.configurate.ConfigurationOptions} instance, with the specified 'copy defaults' setting
     * set, and all other settings copied from this instance.
     *
     * @param shouldCopyDefaults whether to copy defaults
     * @return updated options object
     * @see #shouldCopyDefaults() for information on what this method does
     */
    @NotNull
    public ConfigurationOptions setShouldCopyDefaults(boolean shouldCopyDefaults) {
        if (this.shouldCopyDefaults == shouldCopyDefaults) {
            return this;
        }

        return new ConfigurationOptions(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    @Nullable
    public ImmutableSet<Class<?>> getAcceptedTypes() {
        return this.acceptedTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationOptions)) return false;
        ConfigurationOptions that = (ConfigurationOptions) o;
        return Objects.equals(shouldCopyDefaults, that.shouldCopyDefaults) && Objects.equals(mapFactory, that.mapFactory) && Objects.equals(header, that.header) && Objects.equals(serializers, that.serializers) && Objects.equals(acceptedTypes, that.acceptedTypes) && Objects.equals(objectMapperFactory, that.objectMapperFactory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapFactory, header, serializers, acceptedTypes, objectMapperFactory, shouldCopyDefaults);
    }

    @Override
    public String toString() {
        return "ConfigurationOptions{" + "mapFactory=" + mapFactory + ", header='" + header + '\'' + ", serializers=" + serializers + ", acceptedTypes=" + acceptedTypes + ", objectMapperFactory=" + objectMapperFactory + ", shouldCopyDefaults=" + shouldCopyDefaults + '}';
    }

}
