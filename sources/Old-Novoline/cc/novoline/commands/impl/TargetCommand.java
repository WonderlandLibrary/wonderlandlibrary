package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.modules.PlayerManager;
import cc.novoline.utils.java.Checks;
import cc.novoline.utils.messages.TextMessage;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static cc.novoline.modules.PlayerManager.EnumPlayerType.FRIEND;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.utils.messages.MessageFactory.text;
import static cc.novoline.utils.messages.MessageFactory.usage;
import static net.minecraft.util.EnumChatFormatting.GRAY;


public final class TargetCommand extends NovoCommand {

    /* constructors */
    public TargetCommand(@NonNull Novoline novoline) {
        super(novoline, "target", Arrays.asList("tar", "target"));
    }

    /* methods */
    @Override
    public void process(String[] args) {
        if (args.length < 1) {
            sendHelp( // @off
                    "Targets help:", ".target",
                    usage("add (name)", "adds target"),
                    usage("remove (name)", "removes target"),
                    usage("list", "shows targets")
            ); // @on
            return;
        }

        if (args.length == 1) {
            final String arg = args[0];

            switch (arg.toLowerCase()) {
                case "list":
                case "l":
                    final List<String> friends = this.novoline.getPlayerManager().whoHas(TARGET);
                    final TextMessage text = text("Targets list:");

                    if (friends.isEmpty()) {
                        text.append(text(" (empty)", EnumChatFormatting.RED));
                    }

                    send(text, true);

                    for (String target : friends) {
                        send(text(" - ").append(text(target, GRAY)));
                    }

                    break;

                case "clear":
                    if (this.novoline.getPlayerManager().removeType(TARGET, e -> true)) {
                        notify("Target list was cleared");
                    } else {
                        notify("Target list is empty");
                    }

                    break;

                default:
                    final PlayerManager.EnumPlayerType type = this.novoline.getPlayerManager().getType(arg);

                    if (type == FRIEND || type == null) {
                        add(arg);
                    } else if (type == TARGET) {
                        remove(arg);
                    }

                    break;
            }
        } else {
            try {
                final String action = args[0], // @off
                        name = args[1]; // @on

                switch (action.toLowerCase()) {
                    case "clear": {
                        this.novoline.getPlayerManager().removeType(TARGET, e -> true);
                        break;
                    }

                    case "add": {
                        add(name);
                        break;
                    }

                    case "remove":
                    case "delete":
                    case "del":
                    case "rem": {
                        remove(name);
                        break;
                    }

                    default:
                        notifyError("Illegal command specified: " + args[0] + "!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(String name) {
        Checks.notBlank(name, "name");
        final String lowercase = name.toLowerCase();

        final PlayerManager manager = this.novoline.getPlayerManager();
        final PlayerManager.EnumPlayerType type = manager.getType(lowercase);

        if (type != null) {
            switch (type) {
                case FRIEND:
                    notifyError(name + " is friend");
                    return;

                case TARGET:
                    notifyError(name + " is target already!");
                    return;
            }
        }

        final boolean wasSet = manager.setType(lowercase, TARGET);

        if (wasSet) {
            notify("Added " + name + " to targets!");

            try {
                manager.getConfig().save();
            } catch (IOException e) {
                notifyError("Can't save to file");
                manager.getLogger().warn("An error occurred while saving targets list", e);
            }
        } else {
            notifyError("Cannot add " + name + " to targets!");
        }
    }

    public void remove(String name) {
        Checks.notBlank(name, "name");
        final String lowercase = name.toLowerCase();

        final PlayerManager manager = this.novoline.getPlayerManager();

        if (manager.getType(lowercase) != TARGET) {
            notifyError(name + " is not target!");
            return;
        }

        final boolean contained = manager.removePlayer(lowercase);

        if (contained) {
            notify("Removed " + name + " from targets!");

            try {
                manager.getConfig().save();
            } catch (IOException e) {
                notifyError("Can't save to file");
                manager.getLogger().warn("An error occurred while saving targets list", e);
            }
        } else {
            notifyError("Cannot remove " + name + " from targets!");
        }
    }

    @Override
    public @Nullable List<String> completeTabOptions(String[] args) {
        switch (args.length) { // @off
            case 1:
                return completeTab(Stream.of("add", "remove", "list"), args[0], true);
            case 2:
                final PlayerManager playerManager = this.novoline.getPlayerManager();

                if (args[0].equalsIgnoreCase("add")) {
                    return completeTab(NetHandlerPlayClient.playerInfoMap.values().stream()
                            .map(NetworkPlayerInfo::getGameProfile)
                            .map(GameProfile::getName)
                            .filter(s -> {
                                final PlayerManager.EnumPlayerType type = playerManager.getType(s);
                                return type != TARGET && type != FRIEND;
                            }), args[1], true);
                } else if (args[0].equalsIgnoreCase("remove")) {
                    return completeTab(playerManager.whoHas(TARGET), args[1], true);
                } else {
                    return null;
                }
            default:
                return null;
        } // @on
    }

}
