package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.utils.java.Checks;
import cc.novoline.utils.java.HttpUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static cc.novoline.utils.java.HttpUtils.createConnection;
import static cc.novoline.utils.java.HttpUtils.parseConnectionInput;
import static net.minecraft.util.EnumChatFormatting.*;

public final class NameCommand extends NovoCommand {

    /* fields */
    private final Cache<String, Player> cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    /* constructors */
    public NameCommand(@NonNull Novoline novoline) {
        super(novoline, "name", "names");
    }

    /* methods */
    @Override
    public void process(String[] args) {
        if (args.length < 1) {
            notifyError("Use .name (name)");
            return;
        }

        final String name = args[0];

        print(name, 0);
    }

    public void print(String name, int retry) {
        ForkJoinPool.commonPool().execute(() -> {
            try {
                final Player player = getPlayer(name);
                if (player == null) return;

                try {
                    send(namesToFormattedString(player));
                } catch (Throwable ignored) {
                    if (retry < 1) {
                        print(name, retry + 1);
                    } else {
                        send(RED + "Could not find name " + name + "!");
                    }
                }
            } catch (Throwable t) {
                getLogger().warn("An error occurred while trying to reach Mojang API!", t);
            }
        });
    }

    public NameCommand.Player getPlayer(@NonNull String name) throws ExecutionException {
        Checks.notBlank(name, "name");

        return this.cache.get(name.toLowerCase(), () -> {
            HttpURLConnection connection = createConnection("https://api.mojang.com/users/profiles/minecraft/" + name);
            JsonElement element = parseConnectionInput(connection, con -> {
                try {
                    send(RED + "Could not find name " + name + "! (" + con.getResponseCode() + ")");
                } catch (IOException e) {
                    send(RED + "Could not find name " + name + "!");
                    getLogger().warn("An I/O error occurred while trying to get response code!", e);
                }
            }, JsonElement.class);
            if (element == null) return null;

            element = element.getAsJsonObject().get("id");

            if (element == null || !element.isJsonPrimitive()) {
                throw new RuntimeException("Mojang API schema has been changed!");
            }

            final String undashedUuid = element.getAsString();

            connection = createConnection("https://api.mojang.com/user/profiles/" + undashedUuid + "/names");
            final JsonArray array = parseConnectionInput(connection,
                    httpURLConnection -> send(RED + "Mojang API schema has been changed. Let the developers know!"),
                    JsonArray.class);

            // noinspection ConstantConditions
            return new Player(HttpUtils.getGson().fromJson(array, new TypeToken<List<Name>>() {
            }.getType()));
        });
    }

    @NonNull
    private String namesToFormattedString(@NonNull final Player player) {
        final List<Name> names = player.getNames();

        if (names.isEmpty()) {
            throw new RuntimeException();
        } else if (names.size() == 1) {
            return "\n  " + GOLD + BOLD + names.get(0).getName() + "\n";
        }

        Name entry = names.get(0);
        final String first = "\n  " + GOLD.toString() + BOLD + entry.getName() + GOLD + " (Original name)\n";
        final StringBuilder builder = new StringBuilder(first);

        int i = 1;

        while (i < names.size() - 1) {
            entry = names.get(i++);
            builder.append("  ").append(GOLD).append(" - ").append(entry.getName()).append(" (")
                    .append(formatTimestamp(entry.getTimestamp())).append(")\n");
        }

        entry = names.get(i);
        builder.append("  ").append(GOLD).append(" - ").append(entry.getName()).append(" (")
                .append(formatTimestamp(entry.getTimestamp())).append(")");

        return builder.toString();
    }

    @NonNull
    private String formatTimestamp(final long timestamp) {
        return this.dateFormatter.format(new Date(timestamp));
    }

    private static class Player {

        private final List<Name> names;

        private Player(@NonNull List<Name> names) {
            Checks.notEmpty(names, "names");
            this.names = names;
        }

        @NonNull
        private static Player withNames(@NonNull List<Name> names) {
            return new Player(names);
        }

        public Name getCurrentName() {
            return this.names.get(this.names.size() - 1);
        }

        public List<Name> getNames() {
            return this.names;
        }

        //region Lombok-alternative
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Player)) return false;
            final Player player = (Player) o;
            return Objects.equals(this.names, player.names);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.names);
        }

        @Override
        public String toString() {
            return "Player{" + "names=" + this.names + '}';
        }
        //endregion

    }

    private static class Name {

        @Expose
        @SerializedName("name")
        private final String name;
        @Expose
        @SerializedName("changedToAt")
        private final long timestamp;

        public Name(String name, long timestamp) {
            this.name = name;
            this.timestamp = timestamp;
        }

        public boolean isFirst() {
            return this.timestamp == 0;
        }

        //region Lombok-alternative
        public String getName() {
            return this.name;
        }

        public long getTimestamp() {
            return this.timestamp;
        }
        //endregion

    }

}
