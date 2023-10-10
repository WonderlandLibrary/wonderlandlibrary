package ravenweave.client.command.commands;

import net.minecraft.entity.Entity;
import ravenweave.client.clickgui.raven.Terminal;
import ravenweave.client.command.Command;
import ravenweave.client.module.modules.combat.AimAssist;

public class Friends extends Command {
    public Friends() {
        super("friends", "Allows you to manage and view your friends list", 1, 2,
                new String[] { "add / remove / list", "Player's name" }, new String[] { "f", "amigos", "lonely4ever" });
    }

    @Override
    public void onCall(String[] args) {
        if (args.length == 0) {
            listFriends();
        } else if (args[0].equalsIgnoreCase("list")) {
            listFriends();
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                boolean added = AimAssist.addFriend(args[1]);
                if (added) {
                    Terminal.print("Successfully added " + args[1] + " to your friends list!");
                } else {
                    Terminal.print("An error occurred!");
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                boolean removed = AimAssist.removeFriend(args[1]);
                if (removed) {
                    Terminal.print("Successfully removed " + args[1] + " from your friends list!");
                } else {
                    Terminal.print("An error occurred!");
                }
            }
        } else {
            this.incorrectArgs();
        }
    }

    public void listFriends() {
        if (AimAssist.getFriends().isEmpty()) {
            Terminal.print("You have no friends. :(");
        } else {
            Terminal.print("Your friends are:");
            for (Entity entity : AimAssist.getFriends()) {
                Terminal.print(entity.getName());
            }
        }
    }
}
