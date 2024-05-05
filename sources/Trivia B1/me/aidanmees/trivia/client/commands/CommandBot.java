/*package me.aidanmees.trivia.client.commands;

import java.util.Map;

import me.aidanmees.trivia.client.bot.BotConsole;
import me.aidanmees.trivia.client.bot.BotEntity;
import me.aidanmees.trivia.client.bot.BotUtil;
import me.aidanmees.trivia.client.bot.generator.UsernameGenerator;
import me.aidanmees.trivia.client.bot.gui.HGuiNewChat;
import me.aidanmees.trivia.client.main.trivia;

public class CommandBot extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
        if (args[1].equalsIgnoreCase("help")) {
            BotConsole.Success("Commands [1]");
            BotConsole.Success("<join> <username:password>");
        }
        if (args[1].equalsIgnoreCase("join") || args[1].equalsIgnoreCase("connect")) {
            final String name = UsernameGenerator.generateNaturalUsername();
            BotConsole.Attempt("Adding account...");
            BotUtil.join(args[2]);
        }
        if (args[1].equalsIgnoreCase("box") || args[1].equalsIgnoreCase("chatbox")) {
            final String user = args[1];
            trivia.chatMessage(user);
            if (user.equalsIgnoreCase("-")) {
                ((HGuiNewChat)this.mc.ingameGUI.getChatGUI()).openBot(null);
            }
            else {
                boolean set = false;
                for (final Map.Entry<String, BotEntity> entry : trivia.botManager.bots.entrySet()) {
                    final BotEntity bot = entry.getValue();
                    if (bot.name.equalsIgnoreCase(user)) {
                        ((HGuiNewChat)this.mc.ingameGUI.getChatGUI()).openBot(bot);
                        set = true;
                        break;
                    }
                }
            }
        }
    }

	@Override
	public String getActivator() {
		return ".bot";
	}

	@Override
	public String getSyntax() {
		return ".bot ";
	}

	@Override
	public String getDesc() {
		return "lets a bot join.";
	}
}
*/