package wtf.diablo.commands.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import wtf.diablo.Diablo;
import wtf.diablo.commands.Command;
import wtf.diablo.commands.CommandData;
import wtf.diablo.utils.chat.ChatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@CommandData(
        name = "friend",
        aliases = {
            "friends","friend","f"
        },
        description = "Handles configs"
)
public class FriendCommand extends Command {

    public static ArrayList<EntityPlayer> friends = new ArrayList<>();

    public static boolean isFriend(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return friends.contains(entity);
        }
        return false;
    }

    @Override
    public void run(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("clear")) {
                friends.clear();
                ChatUtil.log("Cleared friends list!");
                return;
            }
            if(args.length > 1){
                ChatUtil.log("Please enter a valid username.");
                return;
            }
            for (EntityPlayer player : mc.theWorld.playerEntities) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    if(player == mc.thePlayer){
                        ChatUtil.log("We know you lack friends, but you can't be your own friend. :(");
                        return;
                    }
                    if (friends.contains(player)) {
                        friends.remove(player);
                        ChatUtil.log("Removed " + player.getName() + " from friends!");
                        return;
                    }
                    friends.add(player);
                    ChatUtil.log("Added " + player.getName() + " to friends!");
                    return;
                }
            }
            ChatUtil.log("Couldn't Find Player '" + args[0]);
            return;
        }
        ChatUtil.log("Invalid Arguments for [Friend]!");
    }
}
