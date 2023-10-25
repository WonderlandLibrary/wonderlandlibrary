package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(name = "friend")
public class FriendCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (Main.f.isFriend(args[1])) {
            Main.f.remove(args[1]);
            ChatUtil.print(args[1] + " Удален из друзей.");
        } else {
            Main.f.add(args[1]);
            ChatUtil.print(args[1] + " Добавлен в друзья.");
        }
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> friend <имя игрока>");
    }
}
