package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.misc.StringUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.file.configs.FriendsConfig;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendCommand extends Command {

    public FriendCommand(){
        super("friend", "friends");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            final FriendsConfig friendsConfig = LiquidSense.fileManager.friendsConfig;
            String arg = args[1].toLowerCase();

            switch (arg) {
                case "add":
                    if (args.length > 2) {
                        final String name = args[2];

                        if (name.isEmpty()) {
                            chat("The name is empty.");
                            return;
                        }

                        if (args.length > 3 ? friendsConfig.addFriend(name, StringUtils.toCompleteString(args, 3)) : friendsConfig.addFriend(name)) {
                            LiquidSense.fileManager.saveConfig(friendsConfig);
                            chat("§a§l" + name + "§3 was added to your friend list.");
                            playEdit();
                        } else {
                            chat("The name is already in the list.");
                        }
                    }
                    chatSyntax("friend add <name> [alias]");
                    break;
                case "remove":
                    if (args.length > 2) {
                        final String name = args[2];

                        if (name.isEmpty()) {
                            chat("The name is empty.");
                            return;
                        }

                        if (friendsConfig.removeFriend(name)) {
                            LiquidSense.fileManager.saveConfig(friendsConfig);
                            chat("§a§l" + name + "§3 was removed from your friend list.");
                            playEdit();
                        } else {
                            chat("This name is not in the list.");
                        }
                    }
                    chatSyntax("friend remove <name>");
                    break;
                case "clear":
                    final int friends = friendsConfig.getFriends().size();
                    friendsConfig.clearFriends();
                    LiquidSense.fileManager.saveConfig(friendsConfig);
                    chat("Removed " + friends + " friend(s).");
                    break;
                case "list":
                    chat("Your Friends:");
                    for (final FriendsConfig.Friend friend : friendsConfig.getFriends()) {
                        chat("§7> §a§l" + friend.getPlayerName() + " §c(§7§l" + friend.getAlias() + "§c)");
                    }
                    chat("You have §c" + friendsConfig.getFriends().size() + "§3 friends.");
                    break;
            }
            return;
        }
        chatSyntax("friend <add/remove/list/clear>");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        switch (args.length) {
            case 1:
                return Arrays.stream(new String[]{"add", "remove", "list", "clear"})
                        .filter(it -> it.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            case 2:{
                switch (args[0].toLowerCase()){
                    case "add":
                        return mc.theWorld.playerEntities.stream().map(EntityPlayer::getName).filter(it -> it.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                    case "remove":
                        return LiquidSense.fileManager.friendsConfig.getFriends().stream().map(FriendsConfig.Friend::getPlayerName).filter(it -> it.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                    default:
                        return new ArrayList<>();
                }
            }
            default:
                return new ArrayList<>();
        }
    }
}
