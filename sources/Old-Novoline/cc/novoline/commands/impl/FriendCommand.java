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
import java.util.List;
import java.util.stream.Stream;

import static cc.novoline.modules.PlayerManager.EnumPlayerType.FRIEND;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.utils.messages.MessageFactory.text;
import static cc.novoline.utils.messages.MessageFactory.usage;
import static net.minecraft.util.EnumChatFormatting.GRAY;


public final class FriendCommand extends NovoCommand {

    /* constructors */
    public FriendCommand(@NonNull Novoline novoline) {
        super(novoline, "f", "Adds friend", "friend");
    }

    private void sendHelp() {
        sendHelp( // @off
                "Friends help:", ".friend",
                usage("add (name)", "adds friend"),
                usage("remove (name)", "removes friend"),
                usage("list", "shows friends")
        ); // @on
    }

    /* methods */
    @Override
    public void process(String[] args) {
        if (args.length < 1) {
            sendHelp();
            return;
        }

        if (args.length == 1) {
            final String arg = args[0];

            switch (arg.toLowerCase()) {
                case "l":
                case "list": {
                    final List<String> friends = this.novoline.getPlayerManager().whoHas(FRIEND);
                    final TextMessage text = text("Friends list:");

                    if (friends.isEmpty()) {
                        text.append(text(" (empty)", EnumChatFormatting.RED));
                    }

                    send(text, true);

                    for (String target : friends) {
                        send(text(" - ").append(text(target, GRAY)));
                    }

                    break;
                }

                case "clear": {
                    if (this.novoline.getPlayerManager().removeType(FRIEND, e -> true)) {
                        notify("Friend list was cleared");
                    } else {
                        notify("Friend list is empty");
                    }

                    break;
                }

                default: {
                    final PlayerManager.EnumPlayerType type = this.novoline.getPlayerManager().getType(arg);

                    if (type == null || type == TARGET) {
                        add(arg);
                    } else if (type == FRIEND) {
                        remove(arg);
                    }

                    break;
                }
            }
        } else {
            try {
                final String action = args[0], // @off
                        name = args[1]; // @on

                switch (action.toLowerCase()) {
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

        if (type != null) switch (type) {
            case TARGET:
                if (manager.removeType(lowercase, TARGET)) {
                    notifyWarning("Removed " + name + " from targets!");
                } else {
                    notifyWarning("Cannot remove " + name + " from targets!");
                }

                break;
            case FRIEND:
                notifyError(name + " is friend already!");
                return;
        }

        final boolean wasSet = manager.setType(lowercase, FRIEND);

        if (wasSet) {
            notify("Added " + name + " to friends!");

            try {
                manager.getConfig().save();
            } catch (IOException e) {
                notifyError("Can't save to file");
                manager.getLogger().warn("An error occurred while saving friends list", e);
            }
        } else {
            notifyError("Cannot add " + name + " to friends!");
        }
    }

    public void remove(String name) {
        Checks.notBlank(name, "name");
        final String lowercase = name.toLowerCase();

        final PlayerManager manager = this.novoline.getPlayerManager();

        if (manager.getType(lowercase) != FRIEND) {
            notifyError(name + " is not friend!");
            return;
        }

        final boolean contained = manager.removePlayer(lowercase);

        if (contained) {
            notify("Removed " + name + " from friends!");

            try {
                manager.getConfig().save();
            } catch (IOException e) {
                notifyError("Can't save to file");
                manager.getLogger().warn("An error occurred while saving friends list", e);
            }
        } else {
            notifyError("Cannot remove " + name + " from friends!");
        }
    }

    @Override
    public @Nullable List<String> completeTabOptions(String[] args) {
        switch (args.length) { // @off
            case 1:
                return completeTab(Stream.of("add", "remove", "list"), args[0], true);
            case 2:
                if (args[0].equalsIgnoreCase("add")) {
                    return completeTab(NetHandlerPlayClient.playerInfoMap.values().stream().map(NetworkPlayerInfo::getGameProfile).map(GameProfile::getName).filter(s -> !this.novoline.getPlayerManager().hasType(s, FRIEND)), args[1], true);
                } else if (args[0].equalsIgnoreCase("remove")) {
                    return completeTab(this.novoline.getPlayerManager().whoHas(FRIEND), args[1], true);
                } else {
                    return null;
                }
            default:
                return null;
        } // @on
    }

}
