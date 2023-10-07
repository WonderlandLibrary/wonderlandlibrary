package cc.novoline.modules;

import cc.novoline.Novoline;
import cc.novoline.utils.java.Checks;
import cc.novoline.utils.java.Helpers;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import ninja.leaping.configurate.ConfigurationNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public final class PlayerManager {

    /* field */
    private final Logger logger = LogManager.getLogger();
    private final Config config;

    private final Map<String, EnumPlayerType> players = new Object2ObjectArrayMap<>();

    /* constructors */
    public PlayerManager(@NotNull Novoline novoline, @NotNull String s) {
        this.config = Config.fromPath(novoline.getDataFolder().resolve(s));
        config.load();

        addPlayersFromConfig();
    }

    /* methods */
    private void addPlayersFromConfig() {
        EnumPlayerType[] types = EnumPlayerType.values();
        Set<? extends Map.Entry<Object, ? extends ConfigurationNode>> entries = config.getRootNode()
                .getChildrenMap().entrySet();

        Map<String, EnumPlayerType> toAdd = new Object2ObjectArrayMap<>();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : entries) {
            String name = entry.getKey().toString().toLowerCase(Locale.ROOT);
            EnumPlayerType type = types[entry.getValue().getInt()];

            toAdd.put(name, type);
        }

        players.putAll(toAdd);
    }

    public List<String> whoHas(@NotNull EnumPlayerType type) {
        return players.entrySet().stream().filter(e -> e.getValue() == type).map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    public @Nullable EnumPlayerType getType(String name) {
        if (Helpers.isBlank(name)) {
            return null;
        }

        String key = name.toLowerCase(Locale.ROOT);
        return players.get(key);
    }

    public boolean hasType(String name, @NotNull EnumPlayerType type) {
        if (Helpers.isBlank(name)) {
            return false;
        }

        String key = name.toLowerCase(Locale.ROOT);
        return players.get(key) == type;
    }

    public boolean setType(String name, @NotNull EnumPlayerType type) {
        Checks.notBlank(name, "name");

        String key = name.toLowerCase(Locale.ROOT);
        EnumPlayerType currentValue = players.get(key);

        if (currentValue == type) {
            return false;
        }

        boolean equals = players.put(key, type) == currentValue;

        if (equals) {
            updateConfigNode();
        }

        return equals;
    }

    public boolean removeType(@NotNull EnumPlayerType type,
                              @NotNull Predicate<Map.Entry<String, EnumPlayerType>> predicate) {
        boolean removed = players.entrySet().removeIf(predicate);

        if (removed) {
            updateConfigNode();
        }

        return removed;
    }

    public boolean removeType(String name, @NotNull EnumPlayerType type) {
        if (Helpers.isBlank(name)) {
            return false;
        }

        String key = name.toLowerCase(Locale.ROOT);
        EnumPlayerType currentValue = players.get(key);

        boolean equals = currentValue == type && players.remove(key) == currentValue;

        if (equals) {
            updateConfigNode();
        }

        return equals;
    }

    public boolean removePlayer(String name) {
        Checks.notBlank(name, "name");

        String key = name.toLowerCase(Locale.ROOT);
        boolean removed = players.remove(key) != null;

        if (removed) {
            updateConfigNode();
        }

        return removed;
    }

    private void updateConfigNode() {
        ConfigurationNode root = config.getLoader().createEmptyNode();

        for (Map.Entry<String, EnumPlayerType> entry : players.entrySet()) {
            String name = entry.getKey();
            EnumPlayerType type = entry.getValue();

            root.getNode(name).setValue(type.ordinal());
        }

        config.setRootNode(root);
    }

    public Config getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * @author xDelsy
     */
    public enum EnumPlayerType {

        FRIEND(),
        TARGET(),
        ;

    }

}
