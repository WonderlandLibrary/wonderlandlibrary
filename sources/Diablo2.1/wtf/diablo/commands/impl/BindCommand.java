package wtf.diablo.commands.impl;

import org.lwjgl.input.Keyboard;
import wtf.diablo.Diablo;
import wtf.diablo.commands.Command;
import wtf.diablo.commands.CommandData;
import wtf.diablo.utils.chat.ChatUtil;

@CommandData(
        name = "config",
        description = "Handles configs"
)
public class BindCommand extends Command {
    @Override
    public void run(String[] args) {
        String module = args[0];
        int key = Keyboard.getKeyIndex(args[1]);

        try {
            Diablo.getInstance().getModuleManager().getModuleByName(module).setKey(key);
            String keyString = Keyboard.getKeyName(key);
            ChatUtil.log("Bound " + module  + " to key " + keyString + "!");
        } catch (Exception e) {
            ChatUtil.log("Could not find module!");
        }
    }
}
